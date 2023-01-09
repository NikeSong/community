package com.example.community;

import com.example.community.util.SensitiveFilter;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

@SpringBootTest
@ContextConfiguration(classes = CommunityApplication.class)
public class SensitiveFilterTest {
    //abffsbs
    @Test
    public void noSymbolFilterTest()
    {
        String testString = "abcabcabcbaababcbacbaabc";
        SensitiveFilter ssf = new SensitiveFilter();
        String filter = ssf.filter(testString);
        System.out.println(filter);
    }
    @Test
    public void symbolFilterTest(){
        String testString = "$ab$c$a$bcabcbaababcbacbaabc";
        SensitiveFilter ssf = new SensitiveFilter();
        String filter = ssf.filter(testString);
        System.out.println(filter);
    }
}
