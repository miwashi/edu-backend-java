package net.miwashi.edu.controller;

import org.springframework.web.bind.annotation.RequestMapping;

public class HelloWorldController {

    @RequestMapping({ "/hello" })
    public String firstPage() {
        return "Hello World";
    }
}
