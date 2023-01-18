package com.example.community.util;

public class RedisKeyUtil {
    private static final String SPLIT = ":";
    private static final String PREFIX_LIKE = "like:entity";
    private static final String PREFIX_USER_LIKE = "like:user";
    private static final String PREFIX_FOLLOWEE = "followee";
    private static final String PREFIX_FLOOWER = "follower";
    //某个实体的赞的key
    //like:entity:entityType:entityId -> set<userId>
    public static String getEntityLikeKey(int entityType,int entityId) {
        return PREFIX_LIKE + SPLIT + entityType + SPLIT + entityId;
    }

    //某用户的获赞数目
    //like:user:userId -> int
    public static String getUserLikeKey(int userId)
    {
        return PREFIX_USER_LIKE + SPLIT + userId;
    }

    //某用户关注的实体
    //followee:userId:entityType -> zset(entityId,nowTime)
    public static String getFolloweeKey(int userId,int entityType){
        return PREFIX_FOLLOWEE + SPLIT + userId + SPLIT + entityType;
    }

    //某个实体拥有的粉丝
    //follower:entityType:entityId -> zset(userId,nowTime)
    public static String getFollowerKey(int entityType,int entityId) {
        return PREFIX_FLOOWER + SPLIT + entityType + SPLIT + entityId;
    }

}
