package com.example.community.util;

public class RedisKeyUtil {
    private static final String SPLIT = ":";
    private static final String PREFIX_LIKE = "like:entity";

    //某个实体的赞的key
    //like:entity:entityType:entityId -> set<userId>
    public static String getEntityLikeKey(int entityType,int entityId) {
        return PREFIX_LIKE + SPLIT + entityType + SPLIT + entityId;
    }
}
