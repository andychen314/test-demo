package com.test.sharding;

import com.alibaba.fastjson.JSON;
import com.test.sharding.entities.TestBean;
import com.test.sharding.service.TestBeanService;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.ArrayList;
import java.util.List;

/**
 * @author csf/chenshifeng@ksjgs.com
 * @date 2016/7/30.
 */
public class TestSharding {

    private TestBeanService testBeanService;

    @Before
    public void init() {
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("test.xml");
        testBeanService = context.getBean(TestBeanService.class);
    }

    private List<TestBean> getTestBeans() {
        List<TestBean> testBeans = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            TestBean testBean = new TestBean();
            testBean.setId(i + 1);
            testBean.setTestName("test" + (i + 1));
            testBeans.add(testBean);
        }
        return testBeans;
    }

//    @Test
    public void testInsert() {
        System.out.println("**************   testInsert start   **************");
        getTestBeans().forEach(testBeanService::insert);
        System.out.println("**************   testInsert end   **************");
    }

    @Test
    public void testQuery() {
        TestBean testBean = testBeanService.getById(6);
        System.out.println("**************   testQuery   **************");
        System.out.println(JSON.toJSONString(testBean));

        List<TestBean> test = testBeanService.getByName("test");
        System.out.println("**************   testQuery By Name   **************");
        System.out.println(JSON.toJSONString(test));
    }
}
