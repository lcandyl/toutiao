package com.toutiao.service;

import com.alibaba.fastjson.JSONObject;
import com.qiniu.common.QiniuException;
import com.qiniu.http.Response;
import com.qiniu.storage.UploadManager;
import com.qiniu.util.Auth;
import com.toutiao.util.ToutiaoUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

/**
 * Created by admin on 16-7-15.
 */
@Service
public class QiniuService {
    private static final Logger logger = LoggerFactory.getLogger(QiniuService.class);
    //设置好账号的ACCESS_KEY和SECRET_KEY
    String ACCESS_KEY = "AKsrLmF1KsoE2R-vFcNuQ7B7Nch0ZxsEMM-5vP3q";
    String SECRET_KEY = "PnM87mf5euowd6Lm_3QgPpvHuL5n9NW7AL-pA4vV";
    //要上传的空间
    String bucketname = "qiqi-javaexercise";

    //密钥配置
    Auth auth = Auth.create(ACCESS_KEY, SECRET_KEY);
    //创建上传对象
    UploadManager uploadManager = new UploadManager();

    //简单上传，使用默认策略，只需要设置上传的空间名就可以了
    public String getUpToken(){
        return auth.uploadToken(bucketname);
    }

    public String saveImage(MultipartFile file) throws IOException {
        try {
            int dotPos = file.getOriginalFilename().lastIndexOf(".");
            if (dotPos < 0){
                return null;
            }
            //都改成小写
            String fileExt = file.getOriginalFilename().substring(dotPos + 1).toLowerCase();
            if(!ToutiaoUtil.isFileAllowed(fileExt)){
                return null;
            }
            String fileName = UUID.randomUUID().toString().replaceAll("-","")+"."+fileExt;
            //调用put方法上传
            Response res = uploadManager.put(file.getBytes(),fileName,getUpToken());
            //打印返回的信息
            System.out.println(res.toString());
          //  return null;
            if(res.isOK()&&res.isJson()){
                String key = JSONObject.parseObject(res.bodyString()).get("key").toString();
                return ToutiaoUtil.QINIU_DOMAIN_PREFIX + key;
            }
            else {
                logger.error("七牛异常:"+res.bodyString());
                return null;
            }
        } catch (QiniuException e) {
            logger.error("七牛异常"+ e.getMessage());
            return null;

        }
    }
}
