package com.toutiao;

import com.toutiao.service.LikeService;
import org.junit.*;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.IllegalTransactionStateException;

/**
 * Created by admin on 16-7-31.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = ToutiaoApplication.class)
public class LikeServiceTest {
    @Autowired
    LikeService likeService;
    @Test
    public void testLikeA(){
        //跑测试用例
       likeService.like(123,1,1);
        //看看值是不是等于我预想的值
        Assert.assertEquals(1,likeService.getLikeStatus(123,1,1));
    }

    @Test
    public void testDisLike(){
        //跑测试用例
        likeService.disLike(123,1,1);
        //看看值是不是等于我预想的值
        Assert.assertEquals(-1,likeService.getLikeStatus(123,1,1));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testException(){
        throw new IllegalArgumentException("xxxx");
    }

    @Before
    //初始化数据
    public void setUp(){
        System.out.println("setUp");
    }

    @After
    //清理数据 回调
    public void tearDown(){
        System.out.println("tearDown");
    }

    //下面这两个跑所有的测试用例之前和之后，只跑一次
    @BeforeClass
    public static void beforClass(){
        System.out.println("beforClass");
    }

    @AfterClass
    public static void afterClass(){
        System.out.println("afterClass");
    }
}
