package com.revature;

import io.javalin.Javalin;

public class Server {
    Javalin app = Javalin.create().start(7070);
}
