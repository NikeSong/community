package com.example.community;

import org.springframework.beans.BeansException;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.test.context.ContextConfiguration;

@SpringBootTest
@ContextConfiguration(classes = CommunityApplication.class)

class CommunityApplicationTests implements ApplicationContextAware {
    private ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
/*
    @Test
    public void testApplicationContext()
    {
        System.out.println(applicationContext);
        AlphaDao bean = applicationContext.getBean(AlphaDao.class);
        System.out.println(bean.select());
        AlphaDao alpha = applicationContext.getBean("alpha", AlphaDao.class);
        System.out.println(alpha.select());
    }
    @Test
    public void testBeanManagement()
    {
        AlphaService bean = applicationContext.getBean(AlphaService.class);
        System.out.println(bean);
    }
    @Test
    public void testSimpleDateFormat()
    {
        SimpleDateFormat simpleDateFormat = applicationContext.getBean("simpleDateFormat", SimpleDateFormat.class);
        System.out.println(simpleDateFormat.format(new Date()));
    }

    @Autowired
    @Qualifier("alpha")
    private AlphaDao alphaDao;
    @Test
    public void testDI()
    {
        System.out.println(alphaDao);
    }*/
}
