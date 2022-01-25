package com.wangzb.wiki.controller;

import com.wangzb.wiki.req.UserLoginReq;
import com.wangzb.wiki.req.UserQueryReq;
import com.wangzb.wiki.req.UserResetPasswordReq;
import com.wangzb.wiki.req.UserSaveReq;
import com.wangzb.wiki.resp.CommonResp;
import com.wangzb.wiki.resp.UserLoginResp;
import com.wangzb.wiki.resp.UserQueryResp;
import com.wangzb.wiki.resp.PageResp;
import com.wangzb.wiki.service.UserService;
import com.wangzb.wiki.util.SnowFlake;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.concurrent.TimeUnit;

@RestController

@RequestMapping("/User")
public class UserController {

    private static final Logger LOG = LoggerFactory.getLogger(UserController.class);

    @Resource
    private UserService userService;

    @Resource
    private RedisTemplate redisTemplate;

    @Resource
    private SnowFlake snowFlake;


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
        req.setPassword(DigestUtils.md5DigestAsHex(req.getPassword().getBytes()));
        CommonResp resp = new CommonResp<>();
        userService.save(req);
        return resp;
    }

    @PostMapping("/reset-password")
    public CommonResp resetPassword(@Valid @RequestBody UserResetPasswordReq req){
        req.setPassword(DigestUtils.md5DigestAsHex(req.getPassword().getBytes()));
        CommonResp resp = new CommonResp<>();
        userService.resetPassword(req);
        return resp;
    }

    @PostMapping("/login")
    public CommonResp login(@Valid @RequestBody UserLoginReq req){
        req.setPassword(DigestUtils.md5DigestAsHex(req.getPassword().getBytes()));
        CommonResp<UserLoginResp> resp = new CommonResp<>();
        UserLoginResp userLoginResp = userService.login(req);

        //生成单点登录 token 并放入redis中
        Long token = snowFlake.nextId();
        LOG.info("生成单点登录 token: {}, 并放入redis中", token);
        userLoginResp.setToken(token.toString());
        redisTemplate.opsForValue().set(token, userLoginResp, 3600*24, TimeUnit.SECONDS);

        resp.setContent(userLoginResp);
        return resp;
    }

    @DeleteMapping("/delete/{id}")
    public CommonResp delete(@PathVariable long id){
        CommonResp resp = new CommonResp<>();
        userService.delete(id);
        return resp;
    }

}
