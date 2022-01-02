package com.wangzb.wiki.service;

import com.wangzb.wiki.domain.Ebook;

import com.wangzb.wiki.domain.EbookExample;
import com.wangzb.wiki.mapper.EbookMapper;

import com.wangzb.wiki.req.EbookReq;
import com.wangzb.wiki.resp.EbookResp;
import com.wangzb.wiki.util.CopyUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

import java.util.ArrayList;
import java.util.List;

@Service
public class EbookService {

    @Resource
    private EbookMapper ebookMapper;


    public List<EbookResp> list(EbookReq ebookReq){
        EbookExample ebookExample = new EbookExample();
        EbookExample.Criteria criteria = ebookExample.createCriteria();
        criteria.andNameLike("%" + ebookReq.getName() + "%");
        //return ebookMapper.selectByExample(ebookExample);
        List<Ebook> ebooksList = ebookMapper.selectByExample(ebookExample);

//        List<EbookResp> respList = new ArrayList<>();
//        for(Ebook ebook : ebooksList){
//            /*
//            EbookResp ebookResp = new EbookResp();
//            BeanUtils.copyProperties(ebook, ebookResp);//复制的工具类
//            */
//
//            EbookResp ebookResp = CopyUtil.copy(ebook, EbookResp.class);//工具类对象复制
//
//            respList.add(ebookResp);
//
//            // ebookResp.setId(123L);
//        }
        List<EbookResp> respList = CopyUtil.copyList(ebooksList,EbookResp.class);//工具类列表复制
        return respList;

    }
}
