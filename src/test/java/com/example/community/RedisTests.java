package com.example.community;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.core.BoundValueOperations;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SessionCallback;
import org.springframework.test.context.ContextConfiguration;

import java.util.concurrent.TimeUnit;

@SpringBootTest
@ContextConfiguration(classes = CommunityApplication.class)
public class RedisTests {
    @Autowired
    private RedisTemplate redisTemplate;

    @Test
    public void testStrings(){
        String redisKey = "test:count";

        redisTemplate.opsForValue().set(redisKey,1);
        redisTemplate.opsForValue().set(redisKey+"1","abc");

        System.out.println(redisTemplate.opsForValue().get(redisKey));
        System.out.println(redisTemplate.opsForValue().get(redisKey+"1"));
        System.out.println(redisTemplate.opsForValue().increment(redisKey));
        System.out.println(redisTemplate.opsForValue().decrement(redisKey));
    }

    @Test
    public void testHash() {
        String redisKey = "test:user";
        redisTemplate.opsForHash().put(redisKey,"id",1);
        redisTemplate.opsForHash().put(redisKey,"username","张三");

        System.out.println(redisTemplate.opsForHash().get(redisKey,"id"));
        System.out.println(redisTemplate.opsForHash().get(redisKey,"username"));
    }

    @Test
    public void testLists()
    {
        String redisKey = "test:ids";
        redisTemplate.opsForList().leftPush(redisKey,"123");
        redisTemplate.opsForList().leftPush(redisKey,"321");
        redisTemplate.opsForList().leftPush(redisKey,"213");
        redisTemplate.opsForList().leftPush(redisKey,"231");
        Long size = redisTemplate.opsForList().size(redisKey);
        System.out.println(size);
        Object index = redisTemplate.opsForList().index(redisKey, 2);
        System.out.println(index);
    }

    @Test
    public void testSets()
    {
        String redisKey = "test:teachers";
        redisTemplate.opsForSet().add(redisKey,"刘备","关羽");
        System.out.println(redisTemplate.opsForSet().size(redisKey));
        System.out.println(redisTemplate.opsForSet().pop(redisKey));//随机弹出
        System.out.println(redisTemplate.opsForSet().members(redisKey));
    }

    @Test
    public void testSortedSets()
    {
        String redisKey = "test:students";
        redisTemplate.opsForZSet().add(redisKey,"唐僧",80);
        redisTemplate.opsForZSet().add(redisKey,"悟空",90);
        redisTemplate.opsForZSet().add(redisKey,"八戒",50);
        redisTemplate.opsForZSet().add(redisKey,"沙僧",70);
        System.out.println(redisTemplate.opsForZSet().zCard(redisKey));
        System.out.println(redisTemplate.opsForZSet().score(redisKey,"八戒"));
        System.out.println(redisTemplate.opsForZSet().reverseRank(redisKey,"八戒"));
    }

    @Test
    public void testKeys() {
        String redisKey = "test:user";
        redisTemplate.delete(redisKey);
        System.out.println(redisTemplate.hasKey(redisKey));

        redisTemplate.expire(redisKey,10, TimeUnit.SECONDS);//十秒
    }


    @Test
    public void testBoundOptions()  //绑定key
    {
        String redisKey = "test:count";
        BoundValueOperations option = redisTemplate.boundValueOps(redisKey);
        option.increment();
        option.increment();
        option.increment();
        option.increment();
        option.increment();
        System.out.println(option.get());
    }

    //事务
    //其实现原理是提交的语句不会立刻执行，而是放在一个队列里，等待提交后一起执行。
    //写在事务里面的查询语句不会立马执行而是提交后才执行，所以不要再里面写查询语句
    //要么事务之前查，要么事务之后查

    //编程式事务
    @Test
    public void testTransaction()
    {
        Object execute = redisTemplate.execute(new SessionCallback() {
            @Override
            public Object execute(RedisOperations operations) throws DataAccessException {
                String redisKey = "test:tx";
                operations.multi();//启用事务
                operations.opsForSet().add(redisKey,"张三");
                operations.opsForSet().add(redisKey,"李四");
                operations.opsForSet().add(redisKey,"王五");
                System.out.println(operations.opsForSet().members(redisKey));

                return operations.exec();
            }
        });
        System.out.println(execute);
    }

}
