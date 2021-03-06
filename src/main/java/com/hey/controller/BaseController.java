package com.hey.controller;

import com.hey.entity.Order;
import com.hey.entity.SysMember;
import com.hey.entity.User;
import com.hey.result.MultiResult;
import com.hey.result.SingleResult;
import com.hey.service.BaseService;
import com.hey.util.UploadSomething;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.models.auth.In;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by heer on 2018/6/11.
 */
@RestController
public class BaseController {

    @Autowired
    private BaseService baseService;

    public static final String IMAGE_DIR = "/public/image/";
    public static final String SERVER_URL = "";

    @PostMapping(value = "/register",produces="application/json")
    @ApiOperation(value = "注册用户进入系统",httpMethod = "POST")
    public SingleResult addUser(@ApiParam(name="user",value = "用户实体类",required = true)
                          @RequestBody(required = true)User user,
                              @ApiParam(name="imageUrl",value = "昵称",required = true)
                          @RequestParam(value = "imageUrl",required = true)String imageUrl
    ){
        return baseService.saveUser(user,imageUrl);
    }

    @PostMapping(value = "/user/login",produces="application/json")
    @ApiOperation(value = "用户登录",httpMethod = "POST")
    public SingleResult userLogin(@ApiParam(name="tel",value = "电话号码",required = true)
                                @RequestParam(value = "tel",required = true)String tel,
                                @ApiParam(name="password",value = "密码",required = true)
                                @RequestParam(value = "password",required = true)String password
    ){
        return baseService.userLogin(tel,password);
    }


    @PostMapping(value = "/update",produces="application/json")
    @ApiOperation(value = "修改用户信息",httpMethod = "POST")
    public SingleResult updateUserInfo(@ApiParam(name="user",value = "用户实体类",required = true)
                              @RequestBody(required = true)User user
    ){
        return baseService.updateUser(user);
    }

    @PostMapping(value = "/upload/image")
    @ApiOperation(value = "上传图片",httpMethod = "POST")
    public SingleResult userUploadImage(@ApiParam(name="image",value = "图片",required = true)
                                  @RequestBody(required = true)MultipartFile image,
                                      @ApiParam(name="flag",value = "是否需要保存，保存为1，不保存为0，用户第一次提交需要保存。后面申请发货和注册就不保存",required = true)
                                      @RequestParam(value = "flag",required = true)Integer flag,
                                  HttpServletRequest request){
             String path = request.getServletContext().getRealPath(IMAGE_DIR);
             String temp = UploadSomething.uploadImg(path,image,IMAGE_DIR);
             String imageUrl = SERVER_URL+temp;
             String imagePath = IMAGE_DIR+temp;
             if (flag==1){
                 //保存到数据库
                 return baseService.saveImage(imageUrl,imagePath);
             }
             return new SingleResult(imageUrl);
    }

    @PostMapping(value = "/order/save",produces="application/json")
    @ApiOperation(value = "保存订单",httpMethod = "POST")
    public SingleResult addOrder(@ApiParam(name="order",value = "订单实体类",required = true)
                                @RequestBody(required = true)Order order,
                                @ApiParam(name="imageUrl",value = "昵称",required = true)
                                @RequestParam(value = "imageUrl",required = true)String imageUrl
    ){
        return baseService.saveOrder(order,imageUrl);
    }

    @GetMapping(value = "/user/list",produces="application/json")
    @ApiOperation(value = "条件查询用户列表",httpMethod = "GET")
    public MultiResult getUserList(@ApiParam(name="flag",value = "flag=0表示按时间从低到高排序，1表示从高到低排序",required = true)
                                @RequestParam(value = "flag",required = true)Integer flag,
                               @ApiParam(name="userStatus",value = "userStatus按用户状态进行筛选",required = false)
                               @RequestParam(value = "userStatus",required = false)Integer userStatus,
                               @ApiParam(name="start",value = "分页起始页",required = true)
                                   @RequestParam(value = "start",required = true)Integer start,
                               @ApiParam(name="size",value = "分页大小",required = true)
                                   @RequestParam(value = "size",required = true)Integer size,
                               @ApiParam(name="tel",value = "根据号码搜索",required = false)
                                @RequestParam(value = "tel",required = false)String tel
    ){
        return baseService.getUserList(tel,flag,userStatus,start,size);
    }

    @GetMapping(value = "/image/list",produces="application/json")
    @ApiOperation(value = "条件查询公章列表",httpMethod = "GET")
    public MultiResult getImageList(@ApiParam(name="flag",value = "flag=0表示按时间从低到高排序，1表示从高到低排序",required = true)
                                   @RequestParam(value = "flag",required = true)Integer flag,
                                   @ApiParam(name="userStatus",value = "userStatus按用户状态进行筛选",required = false)
                                   @RequestParam(value = "userStatus",required = false)Integer userStatus,
                                   @ApiParam(name="start",value = "分页起始页",required = true)
                                   @RequestParam(value = "start",required = true)Integer start,
                                   @ApiParam(name="size",value = "分页大小",required = true)
                                   @RequestParam(value = "size",required = true)Integer size,
                                   @ApiParam(name="imageMd5",value = "根据图片唯一标示搜索",required = false)
                                   @RequestParam(value = "imageMd5",required = false)String imageMd5
    ){
        return baseService.getImageList(imageMd5,flag,start,size);
    }

    @GetMapping(value = "/order/list",produces="application/json")
    @ApiOperation(value = "条件查询公章列表",httpMethod = "GET")
    public MultiResult getOrderList(@ApiParam(name="flag",value = "flag=0表示按时间从低到高排序，1表示从高到低排序",required = true)
                                    @RequestParam(value = "flag",required = true)Integer flag,
                                    @ApiParam(name="orderStatus",value = "orderStatus按订单状态进行筛选",required = false)
                                    @RequestParam(value = "orderStatus",required = false)Integer orderStatus,
                                    @ApiParam(name="userId",value = "按用户ID进行筛选",required = false)
                                        @RequestParam(value = "userId",required = false)Long userId,
                                    @ApiParam(name="start",value = "分页起始页",required = true)
                                    @RequestParam(value = "start",required = true)Integer start,
                                    @ApiParam(name="size",value = "分页大小",required = true)
                                    @RequestParam(value = "size",required = true)Integer size,
                                    @ApiParam(name="imageMd5",value = "根据图片唯一标示搜索",required = false)
                                    @RequestParam(value = "imageMd5",required = false)String imageMd5,
                                    @ApiParam(name="cardNum",value = "根据车牌号搜索",required = false)
                                        @RequestParam(value = "cardNum",required = false)String cardNum,
                                    @ApiParam(name="tel",value = "根据电话号码搜索",required = false)
                                        @RequestParam(value = "tel",required = false)String tel,
                                    @ApiParam(name="orderNo",value = "根据订单号搜索",required = false)
                                        @RequestParam(value = "orderNo",required = false)String orderNo
    ){
        return baseService.getOrderList(tel,cardNum,imageMd5,flag,userId,orderNo,orderStatus,start,size);
    }

    @GetMapping(value = "/sys/list",produces="application/json")
    @ApiOperation(value = "获取管理员列表",httpMethod = "GET")
    public MultiResult getSysUser(@ApiParam(name="operatorId",value = "操作员ID",required = true)
                                    @RequestParam(value = "operatorId",required = true)Long operatorId,
                                    @ApiParam(name="start",value = "分页起始页",required = true)
                                    @RequestParam(value = "start",required = true)Integer start,
                                    @ApiParam(name="size",value = "分页大小",required = true)
                                    @RequestParam(value = "size",required = true)Integer size
    ){
        return baseService.getSysUser(operatorId,start,size);
    }


    @PostMapping(value = "/sys/login",produces="application/json")
    @ApiOperation(value = "管理员登录",httpMethod = "POST")
    public SingleResult sysLogin(@ApiParam(name="sysName",value = "管理员名",required = true)
                                 @RequestParam(value = "sysName",required = true)String sysName,
                                 @ApiParam(name="sysPass",value = "管理员密码",required = true)
                                     @RequestParam(value = "sysPass",required = true)String sysPass
    ){
        return baseService.sysLogin(sysName,sysPass);
    }

    @PostMapping(value = "/sys/add",produces="application/json")
    @ApiOperation(value = "经理添加管理员",httpMethod = "POST")
    public SingleResult addSysUser(@ApiParam(name="sysMember",value = "管理员实体类",required = true)
                                 @RequestBody(required = true)SysMember sysMember,
                                 @ApiParam(name="operatorId",value = "操作员ID",required = true)
                                 @RequestParam(value = "operatorId",required = true)Long operatorId
    ){
        return baseService.addSysUser(sysMember,operatorId);
    }

    @DeleteMapping(value = "/sys/delete",produces="application/json")
    @ApiOperation(value = "经理删除管理员",httpMethod = "DELETE")
    public SingleResult deleteSysUser(@ApiParam(name="sysId",value = "被删除管理员ID",required = true)
                                          @RequestParam(value = "sysId",required = true)Long sysId,
                                   @ApiParam(name="operatorId",value = "操作员ID",required = true)
                                   @RequestParam(value = "operatorId",required = true)Long operatorId
    ){
        return baseService.deleteSysUser(sysId,operatorId);
    }
}
