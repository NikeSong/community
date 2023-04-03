package com.example.community;

import com.example.community.util.MailClient;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@SpringBootTest
@ContextConfiguration(classes = CommunityApplication.class)

public class MailTests {

    @Autowired
    private MailClient mailClient;
    @Autowired
    private TemplateEngine templateEngine;

    //String myQqClient = "xxx@qq.com";
    String myQqClient = "709918868@qq.com";
    @Test
    public void testTextMail() {
    }

    @Test
    public void testHtmlMail()
    {
        Context context = new Context();
        context.setVariable("username","sunday");

        String process = templateEngine.process("/mail/demo", context);
        System.out.println(process);

        //for(int i = 0;i<30;i++) mailClient.sendMail(myQqClient,"123",process);
    }
}
