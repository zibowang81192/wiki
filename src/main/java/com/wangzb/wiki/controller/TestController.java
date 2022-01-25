package com.wangzb.wiki.controller;

import com.wangzb.wiki.domain.Test;
import com.wangzb.wiki.service.TestService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static com.sun.tools.attach.VirtualMachine.list;

@RestController

public class TestController {

    private static final Logger LOG = LoggerFactory.getLogger(TestController.class);

    @Value("${test.hello}")
    private String testHello;

    @Resource
    private TestService testService;

    @Resource
    private RedisTemplate redisTemplate;
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

    @GetMapping("/test/list")
    public List<Test> list(){
        return testService.list();
    }

    @RequestMapping("/redis/set/{key}/{value}")
    public String set(@PathVariable Long key, @PathVariable String value) {
        redisTemplate.opsForValue().set(key, value, 3600, TimeUnit.SECONDS);
        LOG.info("key: {}, value: {}", key, value);
        return "success";
    }

    @RequestMapping("/redis/get/{key}")
    public Object get(@PathVariable Long key) {
        Object object = redisTemplate.opsForValue().get(key);
        LOG.info("key: {}, value: {}", key, object);
        return object;
    }

}
