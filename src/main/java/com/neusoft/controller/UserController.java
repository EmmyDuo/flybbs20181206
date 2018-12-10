package com.neusoft.controller;

import com.neusoft.domain.User;
import com.neusoft.mapper.UserMapper;
import com.neusoft.respones.RegRespObj;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.UUID;

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
    @RequestMapping("dologin")
    @ResponseBody
    public RegRespObj dologin(User user,HttpServletRequest request){
        User user1 = userMapper.selectByEmailAndPass(user);
        RegRespObj regRespObj = new RegRespObj();
        if (user1 == null){
            regRespObj.setStatus(1);
            regRespObj.setMsg("账户名或密码错误");
        }else {
            HttpSession httpSession = request.getSession();
            httpSession.setAttribute("userinfo",user1);
            regRespObj.setStatus(0);
            regRespObj.setAction(request.getServletContext().getContextPath() +"/");
        }
        return regRespObj;
    }
    @RequestMapping("set")
    public String set(){
        return "user/set";
    }
    @RequestMapping("logout")
    public String logout(HttpServletRequest request)
    {
        request.getSession().invalidate();
        return "redirect:" + request.getServletContext().getContextPath() +"/";
    }
    @RequestMapping("upload")
    @ResponseBody
    public RegRespObj upload(@RequestParam MultipartFile file, HttpServletRequest request) throws IOException {
        RegRespObj regRespObj = new RegRespObj();
        if(file.getSize()>0){
            String realPath = request.getServletContext().getRealPath("/res/uploadImgs");
            File file1 = new File(realPath);
            if(!file1.exists()){
                file1.mkdirs();
            }
            UUID uuid = UUID.randomUUID();
            File file2 = new File(realPath+File.separator+uuid+file.getOriginalFilename());
            file.transferTo(file2);

            HttpSession session = request.getSession();
            User userinfo = (User)session.getAttribute("userinfo");
            userinfo.setPicPath(uuid+file.getOriginalFilename());
            session.setAttribute("userinfo",userinfo);

            userMapper.updateByPrimaryKeySelective(userinfo);

            regRespObj.setStatus(0);
        }
        else
        {
            regRespObj.setStatus(1);
        }
        return regRespObj;
    }
}
