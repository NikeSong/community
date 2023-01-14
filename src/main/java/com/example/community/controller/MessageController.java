package com.example.community.controller;

import com.example.community.entity.Message;
import com.example.community.entity.Page;
import com.example.community.entity.User;
import com.example.community.service.MessageService;
import com.example.community.service.UserService;
import com.example.community.util.HostHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class MessageController {
    @Autowired
    private MessageService messageService;
    @Autowired
    private HostHolder hostHolder;
    @Autowired
    private UserService userService;

    //私信列表
    @RequestMapping(path = "/letter/list",method = RequestMethod.GET)
    public String getLetterList(Model model, Page page)
    {
        User user = hostHolder.getUser();

        //分页信息设置
        page.setLimit(5);
        page.setPath("/letter/list");
        page.setRows(messageService.findConversationCount(user.getId()));

        //查询会话列表
        List<Message> conversationList = messageService.findConversation(
                user.getId(), page.getOffset(), page.getLimit());

        //查询总的未读消息数目
        List<Map<String,Object>> conversations = new ArrayList<>();
        if(conversations != null) {
            for(Message message:conversationList){
                Map<String,Object> map = new HashMap<>();
                map.put("conversation",message);//当前会话最近的那条消息
                map.put("letterCount",messageService.findLettersCount(message.getConversationId()));//当前会话的消息数量
                map.put("unreadCount",messageService.findLetterUnreadCount(
                        user.getId(), message.getConversationId()));//当前会话未读的消息数目
                int targetId = user.getId()==message.getFromId() ? message.getToId() : message.getFromId();
                map.put("target",userService.findUserById(targetId));
                conversations.add(map);
            }
        }
        model.addAttribute("conversations",conversations);
        //查询未读消息总数
        int letterUnreadCount = messageService.findLetterUnreadCount(user.getId(), null);
        model.addAttribute("letterUnreadCount",letterUnreadCount);
        return "/site/letter";
    }

    @RequestMapping(path = "/letter/detail/{conversationId}",method = RequestMethod.GET)
    public String getLetterDetail(@PathVariable("conversationId")String conversationId,Page page,Model model)
    {
        //设置分页
        page.setLimit(5);//每页显示多少条数据
        page.setPath( "/letter/detail/"+conversationId);
        page.setRows(messageService.findLettersCount(conversationId));//设置一共有多少行

        //查询私信列表
        List<Message> letterList = messageService.findLetters(conversationId, page.getOffset(), page.getLimit());
        List<Map<String,Object>> letters = new ArrayList<>();
        if(letterList != null){
            for(Message message:letterList){
                Map<String,Object> map = new HashMap<>();
                map.put("letter",message);
                map.put("fromUser",userService.findUserById(message.getFromId()));
                letters.add(map);
            }
        }
        model.addAttribute("letters",letters);
        //私信目标
        model.addAttribute("target",getLetterTarget(conversationId));

        return "/site/letter-detail";
    }

    private User getLetterTarget(String conversationId){
        String[] s = conversationId.split("_");
        int d0 = Integer.parseInt(s[0]),d1= Integer.parseInt(s[1]);
        if(hostHolder.getUser().getId() == d0) return userService.findUserById(d1);
        else return userService.findUserById(d0);
    }
}
