package com.toutiao;

import com.toutiao.dao.CommentDAO;
import com.toutiao.dao.LoginTicketDAO;
import com.toutiao.dao.NewsDAO;
import com.toutiao.dao.UserDAO;
import com.toutiao.model.*;
import com.toutiao.util.JedisAdapter;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Date;
import java.util.Random;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = ToutiaoApplication.class)
public class JedisTests {

 	@Autowired
	JedisAdapter jedisAdapter;

	@Test
	public void testObject() {
		User user = new User();
		user.setHeadUrl("http://image.nowcoder.com/head/100t.png");
		user.setName("user1");
		user.setPassword("pwd");
		user.setSalt("salt");
		jedisAdapter.setObject("user1xxxxx",user);
		/* "{\"headUrl\":\"http://image.nowcoder.com/head/100t.png\",\"id\":0,\"name\":\"us
		er1\",\"password\":\"pwd\",\"salt\":\"salt\"}"  */
		//斜杠是转义字符 双引号给斜杠出来
		User u = jedisAdapter.getObject("user1xxxxx",User.class);

	   System.out.println(ToStringBuilder.reflectionToString(u));
	}
}
