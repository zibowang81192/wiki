package com.wangzb.wiki.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController

public class TestController {

    @Value("${test.hello}")
    private String testHello;
//    @RequestMapping("/hello")	支持以上四种请求
//
//    @GetMapping("/hello")	只支持Get
//
//    @PostMapping("/hello")	只支持Post
//
//    @PutMapping("/hello")
//
//    @DeleteMapping("/hello")
//
//    @RequestMapping(value = "/user/1", method = RequestMethod.GET)只支持GET


    @GetMapping("/hello")
    public String hello(){
        return "Hello World! "+testHello;
    }
    @PostMapping("/hello/post")
    public String helloPost(String name){
        return "Hello World! Post, "+ name;
    }
}