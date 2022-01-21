package com.wangzb.wiki.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.wangzb.wiki.domain.Ebook;

import com.wangzb.wiki.domain.EbookExample;
import com.wangzb.wiki.mapper.EbookMapper;

import com.wangzb.wiki.req.EbookQueryReq;
import com.wangzb.wiki.req.EbookSaveReq;
import com.wangzb.wiki.resp.EbookQueryResp;
import com.wangzb.wiki.resp.PageResp;
import com.wangzb.wiki.util.CopyUtil;
import com.wangzb.wiki.util.SnowFlake;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;

import java.util.List;

@Service
public class EbookService {

    private static final Logger LOG = LoggerFactory.getLogger(EbookService.class);

    @Resource
    private EbookMapper ebookMapper;

    @Resource
    private SnowFlake snowFlake;

    public PageResp<EbookQueryResp> list(EbookQueryReq ebookReq){
        EbookExample ebookExample = new EbookExample();
        EbookExample.Criteria criteria = ebookExample.createCriteria();
        if (!ObjectUtils.isEmpty(ebookReq.getName())){ //动态SQL
            criteria.andNameLike("%" + ebookReq.getName() + "%");
        }
        if (!ObjectUtils.isEmpty(ebookReq.getCategoryId2())){ //动态SQL
            criteria.andCategory2IdEqualTo(ebookReq.getCategoryId2());
        }
        //return ebookMapper.selectByExample(ebookExample);
        PageHelper.startPage(ebookReq.getPage(), ebookReq.getSize());
        List<Ebook> ebooksList = ebookMapper.selectByExample(ebookExample);

        PageInfo<Ebook> pageInfo = new PageInfo<>(ebooksList);
        LOG.info("总行数：{}", pageInfo.getTotal());
        LOG.info("总页数：{}", pageInfo.getPages());


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

        List<EbookQueryResp> respList = CopyUtil.copyList(ebooksList, EbookQueryResp.class);//工具类列表复制
        PageResp<EbookQueryResp> pageResp = new PageResp<>();
        pageResp.setTotal(pageInfo.getTotal());
        pageResp.setList(respList);

        return pageResp;

    }

    /**
     * 保存
     */
    public void save(EbookSaveReq req){
        Ebook ebook = CopyUtil.copy(req, Ebook.class);
        if(ObjectUtils.isEmpty(req.getId())){
            //新增
            ebook.setId(snowFlake.nextId());
            ebookMapper.insert(ebook);
        }
        else{
            //更新
            ebookMapper.updateByPrimaryKey(ebook);
        }

    }

    public void delete(long id){
        ebookMapper.deleteByPrimaryKey(id);
    }
}
