package com.example.community.dao;

import com.example.community.entity.Message;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface MessageMapper {

    //查询当前用户的会话列表,针对每个会话只返回一条最新的私信
    List<Message> selectConversations(int userId,int offset,int limit);

    //查询当前用户的会话数量
    int selectConversationCount(int userId);

    //查询某会话包含的私信列表
    List<Message> selectLetters(String conversationId,int offset,int limit);

    //查询某会话包含的私信数量
    int selectLetterCount(String conversationId);

    //查询未读私信数量(总共的或者针对某一个会话的)
    int selectLetterUnreadCount(int userId,String conversationId);

    //增加一条私信
    int insertMessage(Message message);

    //更改消息状态
    int updateStatus(List<Integer> ids, int status);
}
