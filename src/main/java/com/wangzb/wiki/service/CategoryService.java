package com.wangzb.wiki.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.wangzb.wiki.domain.Category;
import com.wangzb.wiki.domain.CategoryExample;
import com.wangzb.wiki.mapper.CategoryMapper;
import com.wangzb.wiki.req.CategoryQueryReq;
import com.wangzb.wiki.req.CategorySaveReq;
import com.wangzb.wiki.resp.CategoryQueryResp;
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
public class CategoryService {

    private static final Logger LOG = LoggerFactory.getLogger(CategoryService.class);

    @Resource
    private CategoryMapper categoryMapper;

    @Resource
    private SnowFlake snowFlake;

    public PageResp<CategoryQueryResp> list(CategoryQueryReq categoryReq){
        CategoryExample categoryExample = new CategoryExample();
        CategoryExample.Criteria criteria = categoryExample.createCriteria();
        if (!ObjectUtils.isEmpty(categoryReq.getName())){ //动态SQL
            criteria.andNameLike("%" + categoryReq.getName() + "%");
        }
        //return categoryMapper.selectByExample(categoryExample);
        PageHelper.startPage(categoryReq.getPage(), categoryReq.getSize());
        List<Category> categorysList = categoryMapper.selectByExample(categoryExample);

        PageInfo<Category> pageInfo = new PageInfo<>(categorysList);
        LOG.info("总行数：{}", pageInfo.getTotal());
        LOG.info("总页数：{}", pageInfo.getPages());


//        List<CategoryResp> respList = new ArrayList<>();
//        for(Category category : categorysList){
//            /*
//            CategoryResp categoryResp = new CategoryResp();
//            BeanUtils.copyProperties(category, categoryResp);//复制的工具类
//            */
//
//            CategoryResp categoryResp = CopyUtil.copy(category, CategoryResp.class);//工具类对象复制
//
//            respList.add(categoryResp);
//
//            // categoryResp.setId(123L);
//        }

        List<CategoryQueryResp> respList = CopyUtil.copyList(categorysList, CategoryQueryResp.class);//工具类列表复制
        PageResp<CategoryQueryResp> pageResp = new PageResp<>();
        pageResp.setTotal(pageInfo.getTotal());
        pageResp.setList(respList);

        return pageResp;

    }

    /**
     * 保存
     */
    public void save(CategorySaveReq req){
        Category category = CopyUtil.copy(req, Category.class);
        if(ObjectUtils.isEmpty(req.getId())){
            //新增
            category.setId(snowFlake.nextId());
            categoryMapper.insert(category);
        }
        else{
            //更新
            categoryMapper.updateByPrimaryKey(category);
        }

    }

    public void delete(long id){
        categoryMapper.deleteByPrimaryKey(id);
    }
}
