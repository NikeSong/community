package com.example.community.service;

import com.example.community.util.RedisKeyUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SessionCallback;
import org.springframework.stereotype.Service;

@Service
public class LikeService {

    @Autowired
    private RedisTemplate redisTemplate;

    //点赞
    public void like(int userId,int entityType,int entityId,int entityUserId) {
        redisTemplate.execute(new SessionCallback() {
            @Override
            public Object execute(RedisOperations operations) throws DataAccessException {
                String entityLikeKey = RedisKeyUtil.getEntityLikeKey(entityType,entityId);
                String userLikeKey = RedisKeyUtil.getUserLikeKey(entityUserId);
                //查询
                boolean isMember = operations.opsForSet().isMember(entityLikeKey,userId);
                operations.multi();//开启事务

                if(isMember){
                    operations.opsForSet().remove(entityLikeKey,userId);//已经点过赞了那就取消赞
                    operations.opsForValue().decrement(userLikeKey);//目标用户获赞减一
                } else{
                    operations.opsForSet().add(entityLikeKey,userId);//点赞
                    operations.opsForValue().increment(userLikeKey);//目标用户获赞加一
                }


                return operations.exec();//执行事务
            }
        });
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

    //查询某用户获赞总数
    public int findUserLikeCount(int userId){
        String userLikeKey = RedisKeyUtil.getUserLikeKey(userId);
        Integer count = (Integer) redisTemplate.opsForValue().get(userLikeKey);
        return count == null ? 0:count.intValue();
    }

}
