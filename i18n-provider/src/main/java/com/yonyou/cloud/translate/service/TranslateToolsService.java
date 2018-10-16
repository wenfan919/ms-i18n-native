package com.yonyou.cloud.translate.service;

import com.yonyou.cloud.i18n.service.I18nToolsService;
import com.yonyou.cloud.i18n.service.ITranslateToolsService;
import com.yonyou.cloud.translate.entity.Translate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * spring-boot工程示例，服务具体实现，统计功能
 *
 * @author liujmc
 * @version 5.0.0-RELEASE
 */
@Service
public class TranslateToolsService implements ITranslateToolsService {

    private static final Logger logger = LoggerFactory.getLogger(I18nToolsService.class);

    private TranslateService translateService;

    @Autowired
    public void setTranslateService(TranslateService translateService) {
        this.translateService = translateService;
    }


    @Override
    public Boolean saveTranslate(Properties properties) throws Exception {

        logger.info("开始执行资源写入");

        List<Translate> listData = new ArrayList<Translate>();
        Translate translate = null;
        for (String key : properties.stringPropertyNames()) {

            translate = new Translate();
            translate.setPropertyCode(key);
            translate.setChinese(properties.getProperty(key));

            listData.add(translate);
        }

        this.translateService.saveBatch(listData);

        logger.info("执行资源写入结束");

        return true;
    }


//    @Override
    public List<Translate> getList(Properties properties) throws Exception {

        return this.translateService.findByCode(properties);

    }

    @Override
    public Boolean updateTranslate(Properties properties, String locales) throws Exception {

        logger.info("开始执行资源更新！");

        List<Translate> listData = this.getList(properties);

        for(Translate translate : listData){

            String key = translate.getPropertyCode();

            if (locales == null || "".equals(locales) || "zh_CN".equalsIgnoreCase(locales) || "cn".equalsIgnoreCase(locales)) {
                translate.setChinese(properties.getProperty(key));
            } else if ("zh_TW".equalsIgnoreCase(locales) || "tw".equalsIgnoreCase(locales)) {
                translate.setTraditional(properties.getProperty(key));
            } else if ("en_US".equalsIgnoreCase(locales) || "en_UK".equalsIgnoreCase(locales) || "en".equalsIgnoreCase(locales)) {
                translate.setEnglish(properties.getProperty(key));
            } else if ("fr_FR".equalsIgnoreCase(locales) || "fr".equalsIgnoreCase(locales)) {
                translate.setFrench(properties.getProperty(key));
            } else {
                translate.setReserve1(properties.getProperty(key));
            }

            translate.setChinese(properties.getProperty(key));

            listData.add(translate);
        }

        this.translateService.saveBatch(listData);

        logger.info("执行资源写入更新结束！");

        return true;

    }

    @Override
    public Boolean saveTranslate(List<Translate> list) throws Exception {

        logger.info("开始执行资源写入");

        this.translateService.saveBatch(list);

        logger.info("执行资源写入结束");

        return true;

    }
}
