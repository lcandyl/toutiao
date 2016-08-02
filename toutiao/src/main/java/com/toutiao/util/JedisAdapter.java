package com.toutiao.util;

import com.alibaba.fastjson.JSON;
import com.toutiao.controller.LoginController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import redis.clients.jedis.BinaryClient;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.Tuple;

import java.util.List;


/**
 * Created by admin on 16-7-25.
 */
@Service
public class JedisAdapter implements InitializingBean{
    //按得ctrl + o
    private static final Logger logger = LoggerFactory.getLogger(JedisAdapter.class );


    //演示的不用看了
    public static  void print(int index,Object obj){
        System.out.println(String.format("%d,%s",index,obj.toString()));
    }
    public static void main(String[] argv){
        Jedis jedis = new Jedis();

        //清空所有数据
        jedis.flushAll();

        jedis.set("hello","world");
        print(1,jedis.get("hello"));
        //改名字
        jedis.rename("hello","newhello");
        print(2,jedis.get("newhello"));
        //设置过期时间 15秒
        jedis.setex("hello2",15,"world");

        //数值
        jedis.set("pv","100");
        jedis.incr("pv");//增加 加1
        print(2,jedis.get("pv"));

        jedis.incrBy("pv",5);//默认的多增加
        print(3,jedis.get("pv"));

        //数据结构 列表操作  list是一个双线的列表 左右都可以操作
        String listName = "listA";
        for(int i=0;i<10;i++){
            jedis.lpush(listName,"a"+String.valueOf(i));
        }
        print(3,jedis.lrange(listName,0,12));
        print(4,jedis.llen(listName));
        print(5,jedis.lpop(listName));
        print(6,jedis.llen(listName));
        print(3,jedis.lrange(listName,0,12));
        print(5,jedis.lpop(listName));
        print(7,jedis.lindex(listName,3));//看第三个位置是什么值 从0开始算
        print(8,jedis.linsert(listName,BinaryClient.LIST_POSITION.AFTER,"a4","xxx"));
        print(9,jedis.linsert(listName,BinaryClient.LIST_POSITION.BEFORE,"a4","xx"));
        print(10,jedis.lrange(listName,0,12));
        print(6,jedis.llen(listName));

        //哈希set
        String userKey = "user12";
        jedis.hset(userKey,"name","Jim");
        jedis.hset(userKey,"age","12");
        jedis.hset(userKey,"phone","18618918181");

        print(12,jedis.hget(userKey,"name"));
        print(13,jedis.hgetAll(userKey));
        //删除掉
        jedis.hdel(userKey,"phone");
        print(14,jedis.hgetAll(userKey));
        print(15,jedis.hkeys(userKey));
        print(16,jedis.hvals(userKey));
        print(17,jedis.hexists(userKey,"email"));
        print(18,jedis.hexists(userKey,"age"));

        //不存在的属性才回加进去，存在的话属性值不会改变
        jedis.hsetnx(userKey,"school","daxue");
        jedis.hsetnx(userKey,"name","zhangqi");
        print(19,jedis.hgetAll(userKey));

        //集合 set的概念
        String likeKeys1 = "newsLike1";
        String likeKeys2 = "newsLike2";
        for (int i =0;i<10;i++){
            jedis.sadd(likeKeys1,String.valueOf(i));
            jedis.sadd(likeKeys2,String.valueOf(i*2));
        }
        print(20,jedis.smembers(likeKeys1));
        print(21,jedis.smembers(likeKeys2));
        //像微博共同好友 可以用集合求交集

        //交、并、差
        print(22,jedis.sinter(likeKeys1,likeKeys2));
        print(23,jedis.sunion(likeKeys1,likeKeys2));
        print(24,jedis.sdiff(likeKeys1,likeKeys2));
        print(25,jedis.sismember(likeKeys1,"5"));
        jedis.srem(likeKeys1,"5");//删除
        print(26,jedis.smembers(likeKeys1));//显示集合里面所有元素
        print(27,jedis.scard(likeKeys1));//里面还有多少个值
        jedis.smove(likeKeys2,likeKeys1,"14");//把likeKey2里面的值转移到1里面
        print(28,jedis.scard(likeKeys1));
        print(29,jedis.smembers(likeKeys1));

        //sorted set 很重要 优先队列 每一个key带一个 优先级额数值
        // 用途就是排名

        String rankKey = "rankKey";
        jedis.zadd(rankKey,15,"Jim");
        jedis.zadd(rankKey,60,"Ben");
        jedis.zadd(rankKey,90,"Lee");
        jedis.zadd(rankKey,80,"Mei");
        jedis.zadd(rankKey,75,"Lucy");
        print(30,jedis.zcard(rankKey));//所有的值得个数
        print(31,jedis.zcount(rankKey,61,100));//区间内有多少值
        print(32,jedis.zscore(rankKey,"Lucy"));//具体的值是多少
        jedis.zincrby(rankKey,2,"Lucy");
        print(33,jedis.zscore(rankKey,"Lucy"));
        jedis.zincrby(rankKey,2,"Luc");//也可以为之前不存在的人加值，新增一个人
        print(34,jedis.zscore(rankKey,"Luc"));
        print(30,jedis.zcard(rankKey));//所有的值得个数， 现在值变为6
        //打印第一名到第三名
        print(35,jedis.zrange(rankKey,0,3));//从小到大排序  从0开始算
        print(36,jedis.zrevrange(rankKey,0,3));//从大到小排序

        //  Tuple 元组类型，当我们希望函数返回多个值的时候,
        // 我们可以使用Tuple类型作为函数的返回值
        for(Tuple tuple:jedis.zrangeByScoreWithScores(rankKey,0,100)){
            print(37,tuple.getElement()+":"+String.valueOf(tuple.getScore()));
        }

        print(38,jedis.zrank(rankKey,"Ben"));//从小到大顺序数
        print(39,jedis.zrevrank(rankKey,"Ben"));//从大到小的顺序进行数

        //JedisPool
        JedisPool pool = new JedisPool();
        for(int i = 0; i<100; i++){
            Jedis j = pool.getResource();//每次去一条资源
            j.get("a");//使用该资源，不放回去，打印结果为八条 默认为八条线程
            System.out.println("POOL" + i);
            j.close();//所以每次使用完线程要close掉
        }

    }

    private JedisPool pool =null;//给服务加个pool
    private Jedis jedis = null;

    @Override
    public void afterPropertiesSet() throws Exception {
        pool = new JedisPool("localhost",6379);
    }

    private Jedis getJedis(){
        return pool.getResource();
    }

    public String get(String key) {
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return getJedis().get(key);
        } catch (Exception e) {
            logger.error("发生异常" + e.getMessage());
            return null;
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }

    public void set(String key, String value) {
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            jedis.set(key, value);
        } catch (Exception e) {
            logger.error("发生异常" + e.getMessage());
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }

    public long sadd(String  key,String value){
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.sadd(key,value);//直接是对redis的操作
        }catch (Exception e){
            logger.error("发生异常"+e.getMessage());
            return 0;
        }finally {
            if (jedis != null){
                jedis.close();
            }
        }
    }

    public long srem(String  key,String value){
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.srem(key,value);
        }catch (Exception e){
            logger.error("发生异常"+e.getMessage());
            return 0;
        }finally {
            if (jedis != null){
                jedis.close();
            }
        }
    }

    //用的时候是key 和UserId
    public boolean sismember(String  key,String value){
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.sismember(key,value);
        }catch (Exception e){
            logger.error("发生异常"+e.getMessage());
            return false;
        }finally {
            if (jedis != null){
                jedis.close();
            }
        }
    }

    //一共有多少个人
    public long scard(String  key){
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.scard(key);
        }catch (Exception e){
            logger.error("发生异常"+e.getMessage());
            return 0;
        }finally {
            if (jedis != null){
                jedis.close();
            }
        }
    }

    //增加和取出对象
    public void setObject(String key,Object obj){
      set(key,JSON.toJSONString(obj));
    }

    public <T> T getObject(String key,Class<T> clazz){
        String value = get(key);
        if(value != null){
            return JSON.parseObject(value,clazz);
        }
        return null;
    }

    public long lpush(String key, String value) {
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.lpush(key, value);
        } catch (Exception e) {
            logger.error("发生异常" + e.getMessage());
            return 0;
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }

    public List<String> brpop(int timeout, String key) {
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.brpop(timeout, key);
        } catch (Exception e) {
            logger.error("发生异常" + e.getMessage());
            return null;
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }

}
