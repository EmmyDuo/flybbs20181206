package com.neusoft.controller;

import com.alibaba.fastjson.JSON;
import com.neusoft.domain.Topic;
import com.neusoft.domain.TopicCategory;
import com.neusoft.domain.User;
import com.neusoft.mapper.TopicCategoryMapper;
import com.neusoft.mapper.TopicMapper;
import com.neusoft.respones.RegRespObj;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;

@Controller
@RequestMapping("jie")

public class JieController {
    @Autowired
    TopicCategoryMapper topicCategoryMapper;
    @Autowired
    TopicMapper topicMapper;
    @RequestMapping("add")
    public ModelAndView jie(){
        List<TopicCategory> topicCategoryList = topicCategoryMapper.getAllCategories();
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("categories",topicCategoryList);
        modelAndView.setViewName("jie/add");
        return modelAndView;
    }
    @RequestMapping("doadd")
    public void doadd(Topic topic, HttpServletResponse response, HttpServletRequest request) throws IOException, IOException {
        RegRespObj regRespObj = new RegRespObj();
        HttpSession httpSession = request.getSession();
        User user = (User)httpSession.getAttribute("userinfo");
        topic.setUserid(user.getId());
        int result = topicMapper.insertSelective(topic);
        if(result > 0)
        {
            regRespObj.setStatus(0);
            regRespObj.setAction(request.getServletContext().getContextPath() + "/");
            response.getWriter().println(JSON.toJSONString(regRespObj));
        }
        return;
    }
}
