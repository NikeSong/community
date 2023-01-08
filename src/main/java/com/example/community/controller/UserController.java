package com.example.community.controller;

import com.example.community.annotation.LoginRequired;
import com.example.community.entity.User;
import com.example.community.service.UserService;
import com.example.community.util.CommunityUtil;
import com.example.community.util.HostHolder;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Controller
@RequestMapping(path = "/user")
public class UserController {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Value("${community.path.upload}")
    private String uploadPath;

    @Value("${community.path.demain}")
    private String domain;

    @Value("${server.servlet.context-path}")
    private String contextPath;

    @Autowired
    private UserService userService;

    @Autowired
    private HostHolder hostHolder;

    //访问设置用户信息的页面
    @LoginRequired
    @RequestMapping(path = "/setting",method = RequestMethod.GET)
    public String getSettingPage()
    {
        return "/site/setting";
    }

    //处理上传图片,用SpringMVC提供的专有数据类型
    @LoginRequired
    @RequestMapping(path = "/upload", method = RequestMethod.POST)
    public String uploadHeader(MultipartFile headerImage, Model model) {
        if(headerImage == null){
            model.addAttribute("error","您还没有选择图片");
            return "/site/setting";
        }

        //读取文件名，保存后缀
        String fileName = headerImage.getOriginalFilename();
        String suffix = fileName.substring(fileName.lastIndexOf("."));
        if(StringUtils.isBlank(suffix)){
            model.addAttribute("error","文件格式不正确！");
            return "/site/setting";
        }
        //生成随机文件名
        fileName = CommunityUtil.generateUUID() + suffix;
        //确定文件存放的路径
        File dest = new File(uploadPath+"/"+fileName);
        try {
            //存储文件
            headerImage.transferTo(dest);
        } catch (IOException e) {
            logger.error("上传文件失败"+e.getMessage());
            throw new RuntimeException("上传文件失败，服务器异常",e);
        }
        //更新当前用户头像路径(web路径)
        //http://localhost:8080/community/user/Header/xxx.png
        User user = hostHolder.getUser();
        String headerUrl= domain + contextPath + "/user/header/" + fileName;
        userService.updateHeader(user.getId(),headerUrl);

        return "redirect:/index";
    }

    //响应的是一个二进制数据
    @RequestMapping(path = "/header/{filename}",method = RequestMethod.GET)
    public void getHeader(@PathVariable("filename") String filename, HttpServletResponse response){
        //服务器存放的路径
        filename = uploadPath + "/" + filename;
        //解析文件后缀
        String suffix = filename.substring(filename.lastIndexOf("."));
        //响应图片
        response.setContentType("image/"+suffix);

        try(FileInputStream fis = new FileInputStream(filename);
            OutputStream os = response.getOutputStream();
            )
        {
            byte[] buffer = new byte[1024];
            int b = 0;//读到的位数
            while((b = fis.read(buffer)) != -1) {
                os.write(buffer,0,b);
            }
        } catch (IOException e) {
            logger.error("读取头像失败"+e.getMessage());
        }
    }

    @RequestMapping(path = "/modifyPassword",method = RequestMethod.POST)
    public String modifyPassword(HttpServletRequest request, String iniPassword, String newPassword, String repNewPassword, Model model)
    {
        Map<String,String> error = new HashMap<>();
        Cookie[] cookies = request.getCookies();
        User user = hostHolder.getUser();
        userService.updatePassword(user,iniPassword,newPassword,repNewPassword,error);
        if(error.size() != 0)   //如果有报错信息，则把错误信息转存到model，回到setting界面
        {
            Set<String> strings = error.keySet();
            for(String key:strings) model.addAttribute(key,error.get(key));
            return "/site/setting";
        }
        return "redirect:/index";
    }
}
