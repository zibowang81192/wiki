package com.wangzb.wiki.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.wangzb.wiki.domain.User;
import com.wangzb.wiki.domain.UserExample;
import com.wangzb.wiki.exception.BusinessException;
import com.wangzb.wiki.exception.BusinessExceptionCode;
import com.wangzb.wiki.mapper.UserMapper;
import com.wangzb.wiki.req.UserLoginReq;
import com.wangzb.wiki.req.UserQueryReq;
import com.wangzb.wiki.req.UserResetPasswordReq;
import com.wangzb.wiki.req.UserSaveReq;
import com.wangzb.wiki.resp.UserLoginResp;
import com.wangzb.wiki.resp.UserQueryResp;
import com.wangzb.wiki.resp.PageResp;
import com.wangzb.wiki.util.CopyUtil;
import com.wangzb.wiki.util.SnowFlake;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;
import java.util.List;

@Service
public class UserService {

    private static final Logger LOG = LoggerFactory.getLogger(UserService.class);

    @Resource
    private UserMapper userMapper;

    @Resource
    private SnowFlake snowFlake;

    public PageResp<UserQueryResp> list(UserQueryReq userReq){
        UserExample userExample = new UserExample();
        UserExample.Criteria criteria = userExample.createCriteria();
        if (!ObjectUtils.isEmpty(userReq.getLoginName())){ //动态SQL
            criteria.andLoginNameLike("%" + userReq.getLoginName() + "%");
        }

        //return userMapper.selectByExample(userExample);
        PageHelper.startPage(userReq.getPage(), userReq.getSize());
        List<User> usersList = userMapper.selectByExample(userExample);

        PageInfo<User> pageInfo = new PageInfo<>(usersList);
        LOG.info("总行数：{}", pageInfo.getTotal());
        LOG.info("总页数：{}", pageInfo.getPages());


//        List<UserResp> respList = new ArrayList<>();
//        for(User user : usersList){
//            /*
//            UserResp userResp = new UserResp();
//            BeanUtils.copyProperties(user, userResp);//复制的工具类
//            */
//
//            UserResp userResp = CopyUtil.copy(user, UserResp.class);//工具类对象复制
//
//            respList.add(userResp);
//
//            // userResp.setId(123L);
//        }

        List<UserQueryResp> respList = CopyUtil.copyList(usersList, UserQueryResp.class);//工具类列表复制
        PageResp<UserQueryResp> pageResp = new PageResp<>();
        pageResp.setTotal(pageInfo.getTotal());
        pageResp.setList(respList);

        return pageResp;

    }

    /**
     * 保存
     */
    public void save(UserSaveReq req){
        User user = CopyUtil.copy(req, User.class);
        if(ObjectUtils.isEmpty(req.getId())){

            if(ObjectUtils.isEmpty(selectByLoginName(req.getLoginName()))){
                //新增
                user.setId(snowFlake.nextId());
                userMapper.insert(user);
            }
            else{
                // 用户名已存在
                throw new BusinessException(BusinessExceptionCode.USER_LOGIN_NAME_EXIST);
            }

        }
        else{
            //更新
            user.setLoginName(null);
            user.setPassword(null);
            userMapper.updateByPrimaryKeySelective(user);
        }

    }

    /**
     * 重置密码
     */
    public void resetPassword(UserResetPasswordReq req){
        User user = CopyUtil.copy(req, User.class);
        userMapper.updateByPrimaryKeySelective(user);

    }

    /**
     * 登录
     */
    public UserLoginResp login(UserLoginReq req){
        User userDb = selectByLoginName(req.getLoginName());
        if(ObjectUtils.isEmpty(userDb)){
            // 用户名不存在
            LOG.info("用户名不存在，{}", req.getLoginName());
            throw new BusinessException((BusinessExceptionCode.LOGIN_USER_ERROR));
        }
        else{
            if(userDb.getPassword().equals(req.getPassword())){
                // 登录成功
                UserLoginResp userLoginResp = CopyUtil.copy(userDb,UserLoginResp.class);
                return userLoginResp;
            }
            else{
                // 密码错误
                LOG.info("密码错误，输入密码：{}，数据库密码：{}",req.getPassword(), userDb.getPassword());
                throw new BusinessException((BusinessExceptionCode.LOGIN_USER_ERROR));
            }
        }

    }

    public void delete(long id){
        userMapper.deleteByPrimaryKey(id);
    }

    public User selectByLoginName (String LoginName) {
        UserExample userExample = new UserExample();
        UserExample.Criteria criteria = userExample.createCriteria();
        criteria.andLoginNameLike("%" + LoginName + "%");

        List<User> usersList = userMapper.selectByExample(userExample);

        if (CollectionUtils.isEmpty(usersList)) {
            return null;
        }
        else{
            return usersList.get(0);
        }
    }
}
