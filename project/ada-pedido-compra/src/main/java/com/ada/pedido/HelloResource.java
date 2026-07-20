package com.ada.pedido;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;

@Path("/hello")
public class HelloResource {

    @GET
    public User hello() {
        User user = new User("admin","password");
        return user;
    }
}
