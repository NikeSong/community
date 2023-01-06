package com.example.community;

import com.example.community.util.MailClient;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@SpringBootTest
@ContextConfiguration(classes = Community1Application.class)

public class MailTests {

    @Autowired
    private MailClient mailClient;
    @Autowired
    private TemplateEngine templateEngine;

    //String myQqClient = "xxx@qq.com";
    String myQqClient = "709918868@qq.com";
    @Test
    public void testTextMail() {
        mailClient.sendMail(myQqClient,"傻子你好","卿卿性命，交付汝心。\n" +
                "\n" +
                "余生得罪，厮磨月夜。\n" +
                "\n" +
                "唇齿丹心，妙手抚裳。\n" +
                "\n" +
                "解沫青烟，青山苍穹。\n" +
                "\n" +
                "扁舟孤芥，泛江阳朔。\n" +
                "\n" +
                "白条四作，着绛捕鱼。\n" +
                "\n" +
                "鱼干铺里，莺燕夜鸣。\n" +
                "\n" +
                "采得秋水，还望君心。");
        mailClient.sendMail(myQqClient,"聪明人你好","山中何事？松花酿酒，春水煎茶。");
        mailClient.sendMail(myQqClient,"我的爱人你好","你知道你为啥这么傻吗？因为你有一个傻的灵魂");
        mailClient.sendMail(myQqClient,"无恶不作的坏人你好","那就折一张阔些的荷叶，包一片月光回去，\n" +
                "\n" +
                "回去夹在唐诗里，扁扁的，像压过的相思。");
        mailClient.sendMail(myQqClient,"薛之谦的爱人你好","当华美的叶片落尽，生命的脉络才历历可见。");
        mailClient.sendMail(myQqClient,"刁某的暗恋对象人你好","他日漂浮进我生命中的云，不再倾吐雨水或掀起风暴，只是给我黄昏的天空增色添彩。");
        mailClient.sendMail(myQqClient,"秃头少女你好","你好！覃雨静！我是你的老公，请记住这一点！");
        mailClient.sendMail(myQqClient,"今天的雪下得很大","掉头一去是风吹黑发，回首再来已雪满白头。");
        mailClient.sendMail(myQqClient,"今天的我很想你","你好！覃雨静！我是你的老公，请记住这一点！");
        mailClient.sendMail(myQqClient,"不会求不定积分的雨静","记得上初中时忘带试卷就请假回去取，可是怕路上太无聊就找到了班上最爱美最不爱学习的同学—同前往。我和他说:「我们旷课去玩吧。」他说:「去哪里?」我说:「去我家。」他也没多想就跟我坐上了回家的公共汽车。路上他担心地说:「要是被班主任发现就惨了。」我只是笑而没有回答，我说:「到目的地我会给你一个惊喜。」到了家我说:「你想知道惊喜吗?」他非常兴奋地说:「当然当然。」我说:「我请假了。」\n");
        mailClient.sendMail(myQqClient,"爱你的倩倩","疏影横斜，远映西湖清浅；暗香浮动，长陪夜月黄昏。");
        mailClient.sendMail(myQqClient,"你明天考试会顺利的","你好！覃雨静！我是你的老公，请记住这一点！");
        mailClient.sendMail(myQqClient,"你会成功的","晚上，在床上躺着看电子书，手机余光照到一根长头发，以为又是老婆掉在床上的，随手抓住，准备丢下床，却听到一声尖叫。哦，这根头发目前还长在老婆头上。然后，我被暴打。\n");
        mailClient.sendMail(myQqClient,"你是一个漂亮的女孩","你好！覃雨静！我是你的老公，请记住这一点！");
        mailClient.sendMail(myQqClient,"你是一个骄纵的女孩","你好！覃雨静！我是你的老公，请记住这一点！");
        mailClient.sendMail(myQqClient,"你是一个脾气机","我老公昨天吃坏了肚子，我们一起去看医生，医生问年龄，老公答:「25。」医生惊奇地问:「你有25岁?」老公说:「是。」医生说:「看上去还像高中生—样啊。」说完复杂地看了我一眼。\n" +
                "你以为我是他妈吗?\n");
        mailClient.sendMail(myQqClient,"你是一个傻子","人不喜欢没有灵魂的东西，前面发的那些可能让你感到无趣，但是我真的很想你");
    }

    @Test
    public void testHtmlMail()
    {
        Context context = new Context();
        context.setVariable("username","sunday");

        String process = templateEngine.process("/mail/demo", context);
        System.out.println(process);

        for(int i = 0;i<30;i++) mailClient.sendMail(myQqClient,"123",process);
    }
}
