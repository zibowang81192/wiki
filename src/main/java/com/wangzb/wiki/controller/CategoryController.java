package com.wangzb.wiki.controller;

import com.wangzb.wiki.req.CategoryQueryReq;
import com.wangzb.wiki.req.CategorySaveReq;
import com.wangzb.wiki.resp.CommonResp;
import com.wangzb.wiki.resp.CategoryQueryResp;
import com.wangzb.wiki.resp.PageResp;
import com.wangzb.wiki.service.CategoryService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.List;

@RestController

@RequestMapping("/Category")
public class CategoryController {


    @Resource
    private CategoryService categoryService;

//    @GetMapping("/list")
//    public List<Category> list(){
//        return categoryService.list();
//    }
    @GetMapping("/list")
    public CommonResp list(@Valid CategoryQueryReq categoryReq){
        CommonResp<PageResp<CategoryQueryResp>> categoryCommonResp = new CommonResp<>();
        PageResp<CategoryQueryResp> list = categoryService.list(categoryReq);
        categoryCommonResp.setContent(list);
        return categoryCommonResp;
    }
    @GetMapping("/all")
    public CommonResp all(){
        CommonResp<List<CategoryQueryResp>> categoryCommonResp = new CommonResp<>();
        List<CategoryQueryResp> list = categoryService.all();
        categoryCommonResp.setContent(list);
        return categoryCommonResp;
    }

    @PostMapping("/save")
    public CommonResp save(@Valid @RequestBody CategorySaveReq req){
        CommonResp resp = new CommonResp<>();
        categoryService.save(req);
        return resp;
    }
    @DeleteMapping("/delete/{id}")
    public CommonResp delete(@PathVariable long id){
        CommonResp resp = new CommonResp<>();
        categoryService.delete(id);
        return resp;
    }

}
