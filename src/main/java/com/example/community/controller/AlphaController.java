package com.example.community.controller;

import com.example.community.util.CommunityUtil;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

public class AlphaController {


    @RequestMapping(path = "/ajax",method = RequestMethod.POST)
    @ResponseBody
    public String testAjax(String name,String age){
        System.out.println(name);
        System.out.println(age);
        return CommunityUtil.getJSONString(0,"success");
    }
}
