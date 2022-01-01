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

//public class DemoController {
//
//
//    @Resource
//    private DemoService DemoService;
//
//    @GetMapping("/Demo/list")//每次都要写Demo,其实可以改进一下
//    public List<Demo> list(){
//        return DemoService.list();
//    }
//
//}
@RequestMapping("/Demo")
public class DemoController {


    @Resource
    private DemoService DemoService;

    @GetMapping("/list")//不用在写Demo了,其实可以改进一下
    public List<Demo> list(){
        return DemoService.list();
    }

}
