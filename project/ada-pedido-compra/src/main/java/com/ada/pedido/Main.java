package com.ada.pedido;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class Main {

    public static void main(String[] args) throws JsonProcessingException {

        User user = new User("admin", "1234");
        System.out.println(user.getUsername());

        ObjectMapper mapper = new ObjectMapper();
        String json =  mapper.writeValueAsString(user);

        System.out.println(json);

        /*
            {
                "username": "admin",
                "password": "1234"
            }
         */

    }
}
