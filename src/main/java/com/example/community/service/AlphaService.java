package com.example.community.service;

import com.example.community.dao.DiscussPostMapper;
import com.example.community.dao.UserMapper;
import com.example.community.entity.DiscussPost;
import com.example.community.entity.User;
import com.example.community.util.CommunityUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;

@Service
public class AlphaService {
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private DiscussPostMapper discussPostMapper;
    @Autowired
    private TransactionTemplate transactionTemplate;
/*
    * REQUIRED(0),   支持当前事务(外部事务)，A调B，B被调用，按照A的隔离机制进行
    * REQUIRES_NEW(3),  创建一个新的事务，并且暂停当前事务
    NESTED(6);   存在外部事务，就嵌套在外部事务中执行（有独立的提交和回滚），否则则与required相同
 */
    @Transactional(isolation = Isolation.READ_COMMITTED,propagation = Propagation.REQUIRED)
    public Object save1()
    {
        //新增用户
        User user = new User();
        user.setUsername("alpha");
        user.setSalt(CommunityUtil.generateUUID().substring(0,5));
        user.setPassword(CommunityUtil.md5("12332112331"+user.getSalt() ));
        user.setEmail("122@122.com");
        userMapper.insertUser(user);
        //新用户发帖
        DiscussPost post = new DiscussPost();
        post.setUserId(user.getId());
        post.setTitle("20230111test");
        discussPostMapper.insertDiscussPost(post);

        Integer.valueOf("abc");

        return "ok";
    }

    public Object save2()
    {
        transactionTemplate.setIsolationLevel(
                TransactionDefinition.ISOLATION_READ_COMMITTED);//设置隔离级别
        transactionTemplate.setPropagationBehavior(
                TransactionDefinition.PROPAGATION_REQUIRED);//设置传播机制


        return transactionTemplate.execute(new TransactionCallback<Object>(){
            @Override
            public Object doInTransaction(TransactionStatus status) {
                //新增用户
                User user = new User();
                user.setUsername("beta");
                user.setSalt(CommunityUtil.generateUUID().substring(0,5));
                user.setPassword(CommunityUtil.md5("12332112331"+user.getSalt() ));
                user.setEmail("122@122.com");
                userMapper.insertUser(user);
                //新用户发帖
                DiscussPost post = new DiscussPost();
                post.setUserId(user.getId());
                post.setTitle("20230111testB");
                discussPostMapper.insertDiscussPost(post);

                Integer.valueOf("abc");
                return "ok";
            }
        });
    }
}
