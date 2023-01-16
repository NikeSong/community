package com.example.community.service;

import com.example.community.util.RedisKeyUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class LikeService {

    @Autowired
    private RedisTemplate redisTemplate;

    //点赞
    public void like(int userId,int entityType,int entityId) {
        String entityLikeKey = RedisKeyUtil.getEntityLikeKey(entityType,entityId);
        Boolean liked = redisTemplate.opsForSet().isMember(entityLikeKey, userId);//判断已经点过没有
        if(liked){
            redisTemplate.opsForSet().remove(entityLikeKey,userId);
        } else {
            redisTemplate.opsForSet().add(entityLikeKey,userId);
        }
    }

    //查询实体点赞的数量
    public long findEntityLikeCount(int entityType,int entityId)
    {
        String entityLikeKey = RedisKeyUtil.getEntityLikeKey(entityType,entityId);
        return redisTemplate.opsForSet().size(entityLikeKey);
    }

    //某人有没有对某实体点过赞
    public int findEntityLikeStatus(int userId, int entityType,int entityId){
        String entityLikeKey = RedisKeyUtil.getEntityLikeKey(entityType,entityId);
        return redisTemplate.opsForSet().isMember(entityLikeKey,userId) ? 1:0;
    }
}
