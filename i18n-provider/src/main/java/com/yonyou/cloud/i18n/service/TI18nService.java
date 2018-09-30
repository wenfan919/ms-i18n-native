package com.yonyou.cloud.i18n.service;

import com.yonyou.iuap.baseservice.intg.service.GenericIntegrateService;
import com.yonyou.iuap.baseservice.intg.support.ServiceFeature;
import com.yonyou.cloud.i18n.dao.TI18nMapper;
import com.yonyou.cloud.i18n.entity.I18n;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import static com.yonyou.iuap.baseservice.intg.support.ServiceFeature.*;

@Service
public class TI18nService extends GenericIntegrateService<I18n> {


    private TI18nMapper tI18nMapper;


    @Autowired
    public void settI18nMapper(TI18nMapper tI18nMapper) {
        this.tI18nMapper = tI18nMapper;
        super.setGenericMapper(tI18nMapper, "i18nEnumService");
    }

    @Override
    protected ServiceFeature[] getFeats() {
        return new ServiceFeature[]{REFERENCE, ATTACHMENT};
    }
}