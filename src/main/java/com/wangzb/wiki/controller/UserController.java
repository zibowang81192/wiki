package com.wangzb.wiki.controller;

import com.wangzb.wiki.req.UserQueryReq;
import com.wangzb.wiki.req.UserSaveReq;
import com.wangzb.wiki.resp.CommonResp;
import com.wangzb.wiki.resp.UserQueryResp;
import com.wangzb.wiki.resp.PageResp;
import com.wangzb.wiki.service.UserService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;

@RestController

@RequestMapping("/User")
public class UserController {


    @Resource
    private UserService userService;

//    @GetMapping("/list")
//    public List<User> list(){
//        return userService.list();
//    }
    @GetMapping("/list")
    public CommonResp list(@Valid UserQueryReq userReq){
        CommonResp<PageResp<UserQueryResp>> userCommonResp = new CommonResp<>();
        PageResp<UserQueryResp> list = userService.list(userReq);
        userCommonResp.setContent(list);
        return userCommonResp;
    }

    @PostMapping("/save")
    public CommonResp save(@Valid @RequestBody UserSaveReq req){
        CommonResp resp = new CommonResp<>();
        userService.save(req);
        return resp;
    }
    @DeleteMapping("/delete/{id}")
    public CommonResp delete(@PathVariable long id){
        CommonResp resp = new CommonResp<>();
        userService.delete(id);
        return resp;
    }

}
