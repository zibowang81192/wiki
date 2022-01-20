package com.wangzb.wiki.controller;

import com.wangzb.wiki.req.EbookQueryReq;
import com.wangzb.wiki.req.EbookSaveReq;
import com.wangzb.wiki.resp.CommonResp;
import com.wangzb.wiki.resp.EbookQueryResp;
import com.wangzb.wiki.resp.PageResp;
import com.wangzb.wiki.service.EbookService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;

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
    public CommonResp list(@Valid EbookQueryReq ebookReq){
        CommonResp<PageResp<EbookQueryResp>> ebookCommonResp = new CommonResp<>();
        PageResp<EbookQueryResp> list = ebookService.list(ebookReq);
        ebookCommonResp.setContent(list);
        return ebookCommonResp;
    }

    @PostMapping("/save")
    public CommonResp save(@Valid @RequestBody EbookSaveReq req){
        CommonResp resp = new CommonResp<>();
        ebookService.save(req);
        return resp;
    }
    @DeleteMapping("/delete/{id}")
    public CommonResp delete(@PathVariable long id){
        CommonResp resp = new CommonResp<>();
        ebookService.delete(id);
        return resp;
    }

}
