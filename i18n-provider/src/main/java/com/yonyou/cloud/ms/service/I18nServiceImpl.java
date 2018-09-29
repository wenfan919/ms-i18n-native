package com.yonyou.cloud.ms.service;

import java.io.File;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yonyou.cloud.i18n.service.II18nService;

import com.yonyou.cloud.ms.listener.CustomServletContextListener;

@Service
public class I18nServiceImpl implements II18nService {

    private static final Logger LOGGER = LoggerFactory.getLogger(I18nServiceImpl.class);

    /**
     * @param id
     * @return
     * @throws Exception
     */
    @Override
    public Boolean operation(String id) throws Exception {
        return null;
    }


    /**
     * 执行工具逻辑
     *
     * @param sourcePath /iuap/i18ntools/images/***.zip
     * @return
     * @throws Exception
     */
    @Override
    public String operateTools(String sourcePath) throws Exception {

        return sourcePath;
    }

}
