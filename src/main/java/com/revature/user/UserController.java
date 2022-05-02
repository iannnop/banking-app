package com.revature.user;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import io.javalin.Javalin;
import io.javalin.http.Handler;

import java.util.HashMap;

public class UserController {
    UserDAOImpl userDAO;

    public UserController(Javalin app) {

        userDAO = new UserDAOImpl();

        app.get("/users/{username}", getUser);
        app.post("/users", createUser);
        app.put("/users/{username}", updateUser);
        app.delete("/users/{username}", deleteUser);
    }

    public Handler getUser = ctx -> {

        String username = ctx.pathParam("username");
        User user = userDAO.getUser(username);
        if (user == null) {
            System.out.println("404 Not Found - User \""+username+"\" does not exist");
            ctx.status(404);
            return;
        }

        System.out.println("200 OK - User "+username+" found");
        ctx.status(200).json(user);
    };

    /*
    * Expects { role, username, password, firstName, lastName, email } in body
    * */
    public Handler createUser = ctx -> {
        String jsonString = ctx.body();
        HashMap<String,String> body = new Gson().fromJson(jsonString, new TypeToken<HashMap<String, String>>(){}.getType());

        UserRole role = UserRole.valueOf(body.get("role"));
        String username = body.get("username");
        String password = body.get("password");
        String firstName = body.get("firstName");
        String lastName = body.get("lastName");
        String email = body.get("email");

        User user = userDAO.createUser(role,username,password,firstName,lastName,email);
        System.out.println("200 OK - "+role+" "+username+" created");
        ctx.status(201).json(user);
    };

    /*
     * Expects { role, password, firstName, lastName, email, phone, address } in body
     * */
    public Handler updateUser = ctx -> {
        String username = ctx.pathParam("username");

        User user = userDAO.getUser(username);
        if (user == null) {
            System.out.println("404 Not Found - User \""+username+"\" does not exist.");
            ctx.status(404);
            return;
        }

        String jsonString = ctx.body();
        HashMap<String,String> body = new Gson().fromJson(jsonString, new TypeToken<HashMap<String, String>>(){}.getType());

        UserRole role = UserRole.valueOf(body.get("role"));
        String password = body.get("password");
        String firstName = body.get("firstName");
        String lastName = body.get("lastName");
        String email = body.get("email");
        String phone = body.get("phone");
        String address = body.get("address");

        user.setRole(role);
        user.setPassword(password);
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setEmail(email);
        user.setPhone(phone);
        user.setAddress(address);

        userDAO.updateUser(user);

        System.out.println("200 OK - "+"USER "+username+" updated");
        ctx.status(200).json(user);
    };

    public Handler deleteUser = ctx -> {
        String username = ctx.pathParam("username");

        User user = userDAO.getUser(username);
        if (user == null) {
            System.out.println("404 Not Found - User \""+username+"\" does not exist.");
            ctx.status(404);
            return;
        }

        userDAO.deleteUser(user);

        System.out.println("200 OK - "+"USER "+username+" deleted");
        ctx.status(200).json(user);
    };
}
