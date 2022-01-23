package com.wangzb.wiki.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.wangzb.wiki.domain.Content;
import com.wangzb.wiki.domain.Doc;
import com.wangzb.wiki.domain.DocExample;
import com.wangzb.wiki.mapper.ContentMapper;
import com.wangzb.wiki.mapper.DocMapper;
import com.wangzb.wiki.req.DocQueryReq;
import com.wangzb.wiki.req.DocSaveReq;
import com.wangzb.wiki.resp.DocQueryResp;
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
public class DocService {

    private static final Logger LOG = LoggerFactory.getLogger(DocService.class);

    @Resource
    private DocMapper docMapper;

    @Resource
    private ContentMapper contentMapper;


    @Resource
    private SnowFlake snowFlake;

    public PageResp<DocQueryResp> list(DocQueryReq docReq){
        DocExample docExample = new DocExample();
        docExample.setOrderByClause("sort asc");
        DocExample.Criteria criteria = docExample.createCriteria();
        if (!ObjectUtils.isEmpty(docReq.getName())){ //动态SQL
            criteria.andNameLike("%" + docReq.getName() + "%");
        }
        //return docMapper.selectByExample(docExample);
        PageHelper.startPage(docReq.getPage(), docReq.getSize());
        List<Doc> docsList = docMapper.selectByExample(docExample);

        PageInfo<Doc> pageInfo = new PageInfo<>(docsList);
        LOG.info("总行数：{}", pageInfo.getTotal());
        LOG.info("总页数：{}", pageInfo.getPages());


//        List<DocResp> respList = new ArrayList<>();
//        for(Doc doc : docsList){
//            /*
//            DocResp docResp = new DocResp();
//            BeanUtils.copyProperties(doc, docResp);//复制的工具类
//            */
//
//            DocResp docResp = CopyUtil.copy(doc, DocResp.class);//工具类对象复制
//
//            respList.add(docResp);
//
//            // docResp.setId(123L);
//        }

        List<DocQueryResp> respList = CopyUtil.copyList(docsList, DocQueryResp.class);//工具类列表复制
        PageResp<DocQueryResp> pageResp = new PageResp<>();
        pageResp.setTotal(pageInfo.getTotal());
        pageResp.setList(respList);

        return pageResp;

    }

    /**
     * 全表查询
     */
    public List<DocQueryResp> all(Long ebookId){
        DocExample docExample = new DocExample();
        docExample.createCriteria().andEbookIdEqualTo(ebookId);
        docExample.setOrderByClause("sort asc");
        //return docMapper.selectByExample(docExample);
        List<Doc> docsList = docMapper.selectByExample(docExample);

        List<DocQueryResp> respList = CopyUtil.copyList(docsList, DocQueryResp.class);//工具类列表复制

        return respList;
    }

    /**
     * 保存
     */
    public void save(DocSaveReq req){
        Doc doc = CopyUtil.copy(req, Doc.class);
        Content content = CopyUtil.copy(req, Content.class);
        if(ObjectUtils.isEmpty(req.getId())){
            //新增
            doc.setId(snowFlake.nextId());
            docMapper.insert(doc);
            content.setId(doc.getId());
            contentMapper.insert(content);

        }
        else{
            //更新
            docMapper.updateByPrimaryKey(doc);
            int count = contentMapper.updateByPrimaryKeyWithBLOBs(content);
            if(count==0){
                contentMapper.insert(content);
            }
        }

    }

    public void delete(long id){
        docMapper.deleteByPrimaryKey(id);
    }

    public String findContent(long id){
        Content content = contentMapper.selectByPrimaryKey(id);
        return content.getContent();
    }

    public void delete(List<String> ids){

        DocExample docExample = new DocExample();
        DocExample.Criteria criteria = docExample.createCriteria();
        criteria.andIdIn(ids);

        docMapper.deleteByExample(docExample);
    }

    public String findContent(Long id) {
        Content content = contentMapper.selectByPrimaryKey(id);
        if(ObjectUtils.isEmpty(content)){
            return "";
        }
        else{
            return content.getContent();
        }
    }
}
