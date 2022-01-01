package com.wangzb.wiki.controller;

import com.wangzb.wiki.domain.Ebook;
import com.wangzb.wiki.resp.CommonResp;
import com.wangzb.wiki.service.EbookService;
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

@RequestMapping("/Ebook")
public class EbookController {


    @Resource
    private EbookService ebookService;

//    @GetMapping("/list")
//    public List<Ebook> list(){
//        return ebookService.list();
//    }
    @GetMapping("/list")
    public CommonResp list(){
        CommonResp<List<Ebook>> ebookCommonResp = new CommonResp<>();
        List<Ebook> list = ebookService.list();
        ebookCommonResp.setContent(list);
        return ebookCommonResp;
    }

}
