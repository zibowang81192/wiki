package com.wangzb.wiki.controller;

import com.wangzb.wiki.domain.Demo;
import com.wangzb.wiki.service.DemoService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

import static com.sun.tools.attach.VirtualMachine.list;

@RestController

public class DemoController {

    @Value("${Demo.hello}")
    private String DemoHello;

    @Resource
    private DemoService DemoService;

    @GetMapping("/Demo/list")
    public List<Demo> list(){
        return DemoService.list();
    }

}
