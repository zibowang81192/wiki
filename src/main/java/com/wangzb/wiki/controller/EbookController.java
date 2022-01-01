package com.wangzb.wiki.controller;

import com.wangzb.wiki.domain.Ebook;
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

    @GetMapping("/list")//不用在写Ebook了,其实可以改进一下
    public List<Ebook> list(){
        return ebookService.list();
    }

}
