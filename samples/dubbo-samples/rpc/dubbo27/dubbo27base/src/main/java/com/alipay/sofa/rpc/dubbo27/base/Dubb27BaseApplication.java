/*
 * Ant Group
 * Copyright (c) 2004-2023 All Rights Reserved.
 */
package com.alipay.sofa.rpc.dubbo27.base;

import java.io.File;

import com.alipay.sofa.ark.api.ArkClient;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 *
 * @author syd
 * @version BaseApplication.java, v 0.1 2023年10月31日 19:27 syd
 */

@SpringBootApplication
public class Dubb27BaseApplication implements CommandLineRunner {
    private static Logger LOGGER = LoggerFactory.getLogger(Dubb27BaseApplication.class);

    public static void main(String[] args) {

        //Prevent to get IPV6 address,this way only work in debug mode
        //But you can pass use -Djava.net.preferIPv4Stack=true,then it work well whether in debug mode or not
        System.setProperty("java.net.preferIPv4Stack", "true");

        // 设置内嵌方式启动多模块运行容器, 也可以放在启动参数中
        System.setProperty("sofa.ark.embed.enable", "true");
        System.setProperty("sofa.ark.plugin.export.class.enable", "true");

        SpringApplication.run(Dubb27BaseApplication.class, args);
    }

    /**
     * Install biz when base started
     * @param args
     * @throws Exception
     */
    @Override
    public void run(String... args) throws Exception {
        try {
            installBiz("dubbo27biz/target/dubbo27biz-0.0.1-SNAPSHOT-ark-biz.jar");
            installBiz("dubbo27biz2/target/dubbo27biz2-0.0.1-SNAPSHOT-ark-biz.jar");
        } catch (Throwable e) {
            LOGGER.error("Install biz failed", e);
        }
    }

    protected void installBiz(String bizDir) throws Throwable {
        String pathRoot = "samples/dubbo-samples/rpc/dubbo27/";
        File bizFile = new File(pathRoot + bizDir);
        if (bizFile.exists()) {
            File tmpFile = new File(pathRoot + "target/" + bizFile.getName());
            if(tmpFile.exists()){
                tmpFile.delete();
            }
            FileUtils.copyFile(bizFile, tmpFile);
            ArkClient.installBiz(tmpFile);
        } else {
            LOGGER.warn(bizFile.getAbsolutePath() + " do not exist");
        }
    }
}