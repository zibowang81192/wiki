package com.wangzb.wiki.controller;

import com.wangzb.wiki.req.DocQueryReq;
import com.wangzb.wiki.req.DocSaveReq;
import com.wangzb.wiki.resp.CommonResp;
import com.wangzb.wiki.resp.DocQueryResp;
import com.wangzb.wiki.resp.PageResp;
import com.wangzb.wiki.service.DocService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.Arrays;
import java.util.List;

@RestController

@RequestMapping("/Doc")
public class DocController {


    @Resource
    private DocService docService;

//    @GetMapping("/list")
//    public List<Doc> list(){
//        return docService.list();
//    }
    @GetMapping("/list")
    public CommonResp list(@Valid DocQueryReq docReq){
        CommonResp<PageResp<DocQueryResp>> docCommonResp = new CommonResp<>();
        PageResp<DocQueryResp> list = docService.list(docReq);
        docCommonResp.setContent(list);
        return docCommonResp;
    }
    @GetMapping("/all")
    public CommonResp all(){
        CommonResp<List<DocQueryResp>> docCommonResp = new CommonResp<>();
        List<DocQueryResp> list = docService.all();
        docCommonResp.setContent(list);
        return docCommonResp;
    }

    @PostMapping("/save")
    public CommonResp save(@Valid @RequestBody DocSaveReq req){
        CommonResp resp = new CommonResp<>();
        docService.save(req);
        return resp;
    }
    @DeleteMapping("/delete/{idsStr}")
    public CommonResp delete(@PathVariable String idsStr){
        CommonResp resp = new CommonResp<>();
        List<String> list = Arrays.asList(idsStr.split(","));
        docService.delete(list);
        return resp;
    }

}
