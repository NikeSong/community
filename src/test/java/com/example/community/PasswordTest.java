package com.example.community;

import com.example.community.dao.UserMapper;
import com.example.community.entity.User;
import com.example.community.util.CommunityUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

@SpringBootTest
@ContextConfiguration(classes = CommunityApplication.class)
public class PasswordTest {
    @Autowired
    UserMapper userMapper;

    @Test
    public void testCorrectPassword()
    {
        String username = "test";
        User user = userMapper.selectByName(username);
        System.out.println(user.getPassword());
        String guess = "test";
        String testPassword = CommunityUtil.md5(guess + user.getSalt());
        System.out.println(testPassword);
        System.out.println("是否相等：" + testPassword.equals(user.getPassword()));
    }
    @Test
    public void modifyPassword()
    {
        String s = "d05b4c20b0fc03ab93e1332606b95554";
        userMapper.updatePassword(154,s);
    }
}
