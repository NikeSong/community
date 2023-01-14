package com.example.community.service;

import com.example.community.dao.DiscussPostMapper;
import com.example.community.entity.DiscussPost;
import com.example.community.util.SensitiveFilter;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.util.HtmlUtils;

import java.util.List;

@Service
public class DiscussPostService {
    @Autowired
    DiscussPostMapper discussPostMapper;
    @Autowired
    private SensitiveFilter sensitiveFilter;


    public List<DiscussPost> findDiscussPost(int userId, int offset, int limit) {
        return discussPostMapper.selectDiscussPost(userId,offset,limit);
    }

    public int findDiscussPostRows(@Param("userId") int userId) {
        return discussPostMapper.selectDiscussPostRows(userId);
    }

    public int addDiscussPost(DiscussPost post){
        //检查参数是否为空
        if(post == null){
            throw new IllegalArgumentException("参数不能为空");
        }
        //去除内容中的标签
        post.setTitle(HtmlUtils.htmlEscape(post.getTitle()));
        post.setContent(HtmlUtils.htmlEscape(post.getContent()));
        //过滤敏感词
        post.setTitle(sensitiveFilter.filter(post.getTitle()));
        post.setContent(sensitiveFilter.filter(post.getContent()));
        //插入数据
        return discussPostMapper.insertDiscussPost(post);
    }

    public DiscussPost findDiscusPostById(int id)
    {
        return discussPostMapper.selectDiscussPostById(id);
    }

    public int updateCommentCount(int id,int commentCount)
    {
        return discussPostMapper.updateCommentCount(id,commentCount);
    }
}
