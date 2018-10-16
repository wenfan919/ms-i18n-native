package com.yonyou.cloud.translate.service;

import com.yonyou.iuap.baseservice.entity.Model;
import com.yonyou.iuap.baseservice.intg.service.GenericIntegrateService;
import com.yonyou.iuap.baseservice.intg.support.ServiceFeature;
import com.yonyou.cloud.translate.dao.TranslateMapper;
import com.yonyou.cloud.translate.entity.Translate;

import com.yonyou.iuap.mvc.type.SearchParams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yonyou.iuap.baseservice.ref.service.RefCommonService;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import static com.yonyou.iuap.baseservice.intg.support.ServiceFeature.*;


/**
 * Translate CRUD 核心服务,提供逻辑删除/乐观锁
 */
@Service
public class TranslateService extends GenericIntegrateService<Translate> {


    private TranslateMapper translateMapper;

    @Autowired
    public void setTranslateMapper(TranslateMapper translateMapper) {
        this.translateMapper = translateMapper;
        super.setGenericMapper(translateMapper);
    }

    @Autowired
    private RefCommonService refService;

    public List selectListByExcelData(List idsList) {
        List list = translateMapper.selectListByExcelData(idsList);
        list = refService.fillListWithRef(list);
        return list;
    }

    public Translate findByCode(String codeValue) {
        return this.findUnique("propertyCode", codeValue);
    }

    public List<Translate> findByCode(Properties properties) {

        List<Translate> listData = new ArrayList<Translate>();
        for (String key : properties.stringPropertyNames()) {
            listData.add(this.findUnique("propertyCode", key));
        }
        return listData;
    }


//    public T findUnique(String name, Object value) {
//        SearchParams searchParams = new SearchParams();
//        searchParams.addCondition(name, value);
//        searchParams = this.prepareFeatSearchParam(searchParams);
//        List<T> listData = super.queryList(searchParams.getSearchMap());
//        listData = this.fillListFeatAfterQuery(listData);
//        if (listData != null && listData.size() == 1) {
//            return (Model)listData.get(0);
//        } else {
//            throw new RuntimeException("检索数据不唯一, " + name + ":" + value);
//        }
//    }


    /**
     * @return 向父类 GenericIntegrateService 提供可插拔的特性声明
     * @CAU 可插拔设计
     */
    @Override
    protected ServiceFeature[] getFeats() {
        return new ServiceFeature[]{REFERENCE};
    }
}