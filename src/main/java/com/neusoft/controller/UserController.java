package com.neusoft.controller;

import com.neusoft.domain.User;
import com.neusoft.mapper.UserMapper;
import com.neusoft.respones.RegRespObj;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

@Controller
@RequestMapping("user")
public class UserController {
    @RequestMapping("reg")
    public String reg(){
        return "user/reg";
    }
    @Autowired
    UserMapper userMapper;
    @RequestMapping("doreg")
    @ResponseBody
    public RegRespObj doReg(User user,HttpServletRequest request){
        RegRespObj regRespObj = new RegRespObj();
        String email = user.getEmail();
        User user1 = userMapper.selectByEmail(email);
        if (user1==null){
            user.setKissNum(100);
            user.setJoinTime(new Date());
            int i = userMapper.insertSelective(user);
            if (i>0){
                regRespObj.setStatus(0);
                regRespObj.setAction(request.getServletContext().getContextPath() +"/");
            }else {
                regRespObj.setStatus(1);
                regRespObj.setMsg("数据库错误");
            }
        }else {
            regRespObj.setStatus(1);
            regRespObj.setMsg("邮箱重复，请重新输入");
        }
        return regRespObj;
    }

    @RequestMapping("/checkEmail")
    @ResponseBody
    public RegRespObj checkEmail(User user){
        RegRespObj regRespObj = new RegRespObj();
        User user1 = userMapper.selectByEmail(user.getEmail());
        if(user1==null){
            regRespObj.setMsg("可以注册");
        }else {
            regRespObj.setMsg("邮箱重复，请更换邮箱");
        }
        return regRespObj;
    }

    @RequestMapping("login")
    public String login(){
        return "user/login";
    }
}
