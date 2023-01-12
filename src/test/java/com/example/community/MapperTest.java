package com.example.community;

import com.example.community.dao.DiscussPostMapper;
import com.example.community.dao.LoginTicketMapper;
import com.example.community.dao.UserMapper;
import com.example.community.entity.DiscussPost;
import com.example.community.entity.LoginTicket;
import com.example.community.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

import java.util.Date;
import java.util.List;

@SpringBootTest
@ContextConfiguration(classes = CommunityApplication.class)
public class MapperTest {

    @Autowired
    private UserMapper userMapper;
    @Autowired
    private DiscussPostMapper discussPostMapper;
    @Autowired
    private LoginTicketMapper loginTicketMapper;
    @Test
    public void testSelectUser()
    {
        User user = userMapper.selectById(101);
        User liubei = userMapper.selectByName("liubei");
        User user1 = userMapper.selectByEmail("nowcoder101@sina.com");
        System.out.println(user);
        System.out.println(user1);
        System.out.println(liubei);
        System.out.println((user == user1) +" " + (user1 == liubei));
    }

    @Test
    public void testInsert()
    {
        User user = new User();
        user.setUsername("nikeer");
        user.setPassword("1233442");
        user.setEmail("123@qq.com");
        user.setSalt("aaaa");
        userMapper.insertUser(user);
        int id = user.getId();
        System.out.println(id);
    }

    @Test
    public void testUpdateUser()
    {
        int rows = userMapper.updatePassword(123,"12334");
        System.out.println("修改"+rows+"行");

        rows = userMapper.updateState(123,1);
        System.out.println(rows);

        rows = userMapper.updateHeader(123,"123");
        System.out.println(rows);
    }

    @Test
    public void testDiscussPostFunc()
    {
        //discussPostMapper
        List<DiscussPost> discussPosts = discussPostMapper.selectDiscussPost(0, 3, 5);
        for(DiscussPost dis:discussPosts)
        {
            System.out.println(dis);
        }
        int rows = discussPostMapper.selectDiscussPostRows(0);
        System.out.println(rows);
    }

    @Test
    public void testInsertLoginTicket(){
        LoginTicket loginTicket = new LoginTicket();
        loginTicket.setUserId(101);
        loginTicket.setTicket("123");
        loginTicket.setStatus(0);
        loginTicket.setExpired(new Date(System.currentTimeMillis() + 1000*60*10));
        loginTicketMapper.insertLoginTicket(loginTicket);

    }

    @Test
    public void testSelectLoginTicket()
    {
        LoginTicket loginTicket = loginTicketMapper.selectByTicket("123");
        System.out.println(loginTicket.toString());
        loginTicketMapper.updateStatus("123",1);
        loginTicket = loginTicketMapper.selectByTicket("123");
        System.out.println(loginTicket.toString());
    }

    @Test
    public void testInsertPostMapper(){
        DiscussPost discussPost = new DiscussPost();
        discussPost.setScore(3);
        discussPost.setStatus(1);
        discussPost.setContent("123123test");
        discussPost.setCommentCount(2);
        discussPost.setTitle("21212121");
        discussPost.setUserId(1);
        discussPostMapper.insertDiscussPost(discussPost);
    }
}
