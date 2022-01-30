package com.example.httpsapi.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SecuredHttpsOnlyApiController {

    @GetMapping
    public String sayHi() {
         return "<p style='color:blue'>Hola desde la app spring securizada por SSL<p>";
    }

}
