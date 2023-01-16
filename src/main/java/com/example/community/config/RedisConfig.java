package com.example.community.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;

@Configuration
public class RedisConfig {
    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory factory) {

        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(factory);

        /* 指定序列化方法 */
        //key的序列化方法
        template.setKeySerializer(RedisSerializer.string());//返回一个序列化字符串的序列化器
        //设置普通value的序列化方式
        template.setValueSerializer(RedisSerializer.json());//使用json序列化器
        //设置hash的key序列化方式
        template.setHashKeySerializer(RedisSerializer.string());
        //设置hash的value的序列化方式
        template.setValueSerializer(RedisSerializer.json());

        template.afterPropertiesSet();
        return template;
    }
}
