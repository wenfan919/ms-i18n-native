package com.yonyou.cloud.i18n.service;

import com.yonyou.i18n.main.StepBy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * spring-boot工程示例，服务具体实现，统计功能
 *
 * @author liujmc
 * @version 5.0.0-RELEASE
 */
@Service
public class I18nToolsService implements II18nToolsService {

    private static final Logger LOGGER = LoggerFactory.getLogger(I18nToolsService.class);


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

        StepBy sb = new StepBy();

        sb.init();

        sb.extract();

        sb.resource();

        sb.replace();


        return sourcePath;
    }
}
