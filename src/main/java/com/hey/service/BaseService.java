package com.hey.service;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.hey.dao.BaseDao;
import com.hey.entity.Image;
import com.hey.entity.Order;
import com.hey.entity.User;
import com.hey.result.MultiResult;
import com.hey.result.ResultPageInfo;
import com.hey.result.SingleResult;
import com.hey.util.ImageMd5Util;
import com.hey.util.RandomNumberGenerator;
import com.hey.util.UUIDUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by heer on 2018/6/20.
 */
@Service
public class BaseService {

    @Autowired
    private BaseDao baseDao;

    //保存图片信息到数据库
    public SingleResult saveImage(String imageUrl, String imagePath){
        String imageMd5 = ImageMd5Util.getMD5(imageUrl);
        baseDao.saveImage(imageUrl,imagePath,imageMd5);
        return new SingleResult(imageMd5);
    }

    /**
     * 保存用户
     * @param user
     * @return
     */
    public SingleResult saveUser(User user,String imageUrl){
        if(imageIsUnique(imageUrl)){
            //图片是唯一的
            Long userId = Long.valueOf(RandomNumberGenerator.generateNumber2());
            user.setUserId(userId);
            baseDao.saveUser(user);
            return new SingleResult(userId);
        }else {
            //图片不是唯一的，驳回注册请求
            return new SingleResult("公章识别失败，请重新上传");
        }
    }

    public boolean imageIsUnique(String imageUrl){
        String imageMd5 = ImageMd5Util.getMD5(imageUrl);
        if(baseDao.imageIsUnique(imageMd5)){
            return true;
        }
        return false;
    }


    /**
     * 保存订单
     * @param order
     * @param imageUrl
     * @return
     */
    public SingleResult saveOrder(Order order,String imageUrl){
        if(imageIsUnique(imageUrl)){
            //图片是唯一的，保存订单
            String orderNo = UUIDUtil.getTimeStamp();
            order.setOrderNo(orderNo);
            baseDao.saveOrder(order);
            return new SingleResult(orderNo);
        }else {
            //图片不是唯一的，驳回注册请求
            return new SingleResult("公章识别失败，请重新上传");
        }
    }

    /**
     * 更新用户信息
     * @param user
     */
    public SingleResult updateUser(User user){
        baseDao.updateUser(user);
        return new SingleResult();
    }

    /**
     * 条件查询用户列表
     * @param
     * @param flag
     * @return
     */
    public MultiResult getUserList(String tel,int flag,int userStatus,int start,int size){
        PageHelper.startPage(start,size);
        Page<User> userPage = baseDao.getUserList(flag, userStatus, tel);
        ResultPageInfo pageInfo = new ResultPageInfo(userPage.getPages(),userPage.getPageNum(),userPage.getTotal(),userPage.getPageSize());
        String userList = JSON.toJSONString(userPage.getResult());
        return new MultiResult(userList,pageInfo);
    }

    /**
     * 条件查询订单列表
     * @param
     * @param flag
     * @return
     */
    public MultiResult getOrderList(String tel,String cardNum,String imageMd5,int flag,Long userId,String orderNo,int orderStatus,int userStatus,int start,int size){
        PageHelper.startPage(start,size);
        Page<Order> orderPage = baseDao.getOrderList(tel,cardNum,imageMd5,userId,orderStatus,flag,orderNo);
        ResultPageInfo pageInfo = new ResultPageInfo(orderPage.getPages(),orderPage.getPageNum(),orderPage.getTotal(),orderPage.getPageSize());
        String orderList = JSON.toJSONString(orderPage.getResult());
        return new MultiResult(orderList,pageInfo);
    }

    /**
     * 条件查询用户列表
     * @param imageMd5
     * @param flag
     * @return
     */
    public MultiResult getImageList(String imageMd5,int flag,int start,int size){
        PageHelper.startPage(start,size);
        Page<Image> imagePage = baseDao.getImageList(imageMd5,flag);
        ResultPageInfo pageInfo = new ResultPageInfo(imagePage.getPages(),imagePage.getPageNum(),imagePage.getTotal(),imagePage.getPageSize());
        String imageList = JSON.toJSONString(imagePage.getResult());
        return new MultiResult(imageList,pageInfo);
    }


    public Page<User> findAllUser(int start,int size){
        PageHelper.startPage(start, size);
        Page<User> userPage = baseDao.findAllUser();
        return userPage;
    }

    public MultiResult findUserByTel(int start, int size, String tel){
        PageHelper.startPage(start,size);
        Page<User> userPage = baseDao.findUserByTel(tel);
        ResultPageInfo pageInfo = new ResultPageInfo(userPage.getPages(),userPage.getPageNum(),userPage.getTotal(),userPage.getPageSize());
        String userList = JSON.toJSONString(userPage.getResult());
        return new MultiResult(userList,pageInfo);
    }
}
