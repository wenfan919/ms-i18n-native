package com.yonyou.cloud.translate.service;

import com.yonyou.iuap.baseservice.intg.service.GenericIntegrateService;
import com.yonyou.iuap.baseservice.intg.support.ServiceFeature;
import com.yonyou.cloud.translate.dao.TranslateMapper;
import com.yonyou.cloud.translate.entity.Translate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yonyou.iuap.baseservice.ref.service.RefCommonService;
import java.util.List;

import static com.yonyou.iuap.baseservice.intg.support.ServiceFeature.*;
@Service

/**
 * Translate CRUD 核心服务,提供逻辑删除/乐观锁
 */
public class TranslateService extends GenericIntegrateService<Translate>{


    private TranslateMapper translateMapper;

    @Autowired
    public void setTranslateMapper(TranslateMapper translateMapper) {
        this.translateMapper = translateMapper;
        super.setGenericMapper(translateMapper);
    }
    
        @Autowired
    private RefCommonService refService;
        public List selectListByExcelData(List idsList) {
                List list  = translateMapper.selectListByExcelData(idsList);
                list = refService.fillListWithRef(list);
                return list;
        }


    /**
     * @CAU 可插拔设计
     * @return 向父类 GenericIntegrateService 提供可插拔的特性声明
     */
    @Override
    protected ServiceFeature[] getFeats() {
        return new ServiceFeature[]{ REFERENCE };
    }
}