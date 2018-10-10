package com.yonyou.cloud.i18n.service;

import com.yonyou.i18n.main.StepBy;
import com.yonyou.i18n.utils.ZipUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.File;

/**
 * spring-boot工程示例，服务具体实现，统计功能
 *
 * @author liujmc
 * @version 5.0.0-RELEASE
 */
@Service
public class I18nToolsService implements II18nToolsService {

    private static final Logger logger = LoggerFactory.getLogger(I18nToolsService.class);


    /**
     * 执行工具逻辑
     * <p>
     * 国际化工具类入口
     * <p>
     * 一、后台java国际化
     * <p>
     * 1、 针对java的国际化
     * <p>
     * 二、前台国际化
     * 1、 基于react的国际化
     * <p>
     * 1.1、主要针对js的国际化
     * <p>
     * <p>
     * 2、 基于UUI的国际化
     * <p>
     * 2.1、针对html的国际化
     * <p>
     * 2.2、针对js的国际化
     *
     * @param sourcePath /iuap/i18ntools/images/***.zip
     * @return
     * @throws Exception
     */
    @Override
    public String operateTools(String sourcePath) throws Exception {

        logger.info("识别文件：" + sourcePath);

        String path = sourcePath.substring(0, sourcePath.lastIndexOf(".")) + "_" + System.currentTimeMillis();

        String zipFile = path + ".zip";

        path = path + "/";

        logger.info("解压缩路径：" + path);

        ZipUtils.unZipForFilePath(sourcePath, path);

        StepBy sb = new StepBy();

        sb.init(path);

        sb.extract();

        sb.resource();

        sb.replace();

        ZipUtils.zip(new File(zipFile), path);


        logger.info("执行完成后压缩路径：" + zipFile);

        return zipFile;
    }
}
