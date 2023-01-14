package com.example.community.dao;

import com.example.community.entity.Comment;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface CommentMapper {
//需要分页
    List<Comment> selectCommentByEntity(int entityType,int entityId,int offset,int limit);//根据此评论是针对的哪个实体来进行查询
                                            //实体如是哪个帖子，还是哪个评论

    int selectCountByEntity(int entityType,int entityId);

    int insertComment(Comment comment);

}
