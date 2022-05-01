package com.revature.user;

import io.javalin.Javalin;
import io.javalin.http.Handler;

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
        UserRole role = UserRole.valueOf(ctx.req.getParameter("role"));
        String username = ctx.req.getParameter("username");
        String password = ctx.req.getParameter("password");
        String firstName = ctx.req.getParameter("firstName");
        String lastName = ctx.req.getParameter("lastName");
        String email = ctx.req.getParameter("email");

        User user = userDAO.createUser(role, username,password,firstName,lastName, email);
        System.out.println("200 OK - "+role+" "+username+" created");
        ctx.status(201).json(user);
    };

    public Handler updateUser = ctx -> {
        String username = ctx.pathParam("username");


        User user = userDAO.getUser(username);
        if (user == null) {
            System.out.println("404 Not Found - User \""+username+"\" does not exist.");
            ctx.status(404);
            return;
        }

        String role = ctx.req.getParameter("role");
        String password = ctx.req.getParameter("password");
        String firstName = ctx.req.getParameter("firstName");
        String lastName = ctx.req.getParameter("lastName");
        String email = ctx.req.getParameter("email");
        String phone = ctx.req.getParameter("phone");
        String address = ctx.req.getParameter("address");

        user.setRole(UserRole.valueOf(role));
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
        ctx.status(200);
    };
}
