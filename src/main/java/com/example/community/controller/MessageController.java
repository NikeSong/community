package com.example.community.controller;

import com.example.community.entity.Message;
import com.example.community.entity.Page;
import com.example.community.entity.User;
import com.example.community.service.MessageService;
import com.example.community.service.UserService;
import com.example.community.util.CommunityUtil;
import com.example.community.util.HostHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.*;

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

        //私信列表里未读的设置为已读
        List<Integer> ids = getLetterIds(letterList);
        if(!ids.isEmpty()) messageService.readMessage(ids);
        return "/site/letter-detail";
    }

    private List<Integer> getLetterIds(List<Message> letterList)
    {
        List<Integer> ids = new ArrayList<>();
        if(letterList!=null)
        {
            for(Message message:letterList){
                //当前用户是收信者,并且消息未读
                if(hostHolder.getUser().getId() == message.getToId() && message.getStatus() == 0){
                    ids.add(message.getId());
                }
            }
        }
        return ids;
    }

    private User getLetterTarget(String conversationId){
        String[] s = conversationId.split("_");
        int d0 = Integer.parseInt(s[0]),d1= Integer.parseInt(s[1]);
        if(hostHolder.getUser().getId() == d0) return userService.findUserById(d1);
        else return userService.findUserById(d0);
    }

    @RequestMapping(path = "/letter/send", method = RequestMethod.POST)
    @ResponseBody
    public String sendLetter(String toName, String content) {
        User target = userService.findUserByName(toName);
        if (target == null) {
            return CommunityUtil.getJSONString(1, "目标用户不存在!");
        }

        Message message = new Message();
        message.setFromId(hostHolder.getUser().getId());
        message.setToId(target.getId());
        if (message.getFromId() < message.getToId()) {
            message.setConversationId(message.getFromId() + "_" + message.getToId());
        } else {
            message.setConversationId(message.getToId() + "_" + message.getFromId());
        }
        message.setContent(content);
        message.setCreateTime(new Date());
        messageService.addMessage(message);

        return CommunityUtil.getJSONString(0);
    }
}
