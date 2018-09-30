package com.yonyou.cloud.i18n.service;
import com.yonyou.cloud.i18n.entity.I18n;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Service;
import com.yonyou.iuap.baseservice.persistence.support.QueryFeatureExtension;
import com.yonyou.iuap.mvc.type.SearchParams;

@Service
public class I18nEnumService implements QueryFeatureExtension<I18n>{
    
        @Override
        public SearchParams prepareQueryParam(SearchParams searchParams, Class modelClass) {
                return searchParams;
        }
        
        @Override
        public List<I18n> afterListQuery(List<I18n> list) {
                List<I18n> resultList = new ArrayList<I18n>();
        for (I18n entity : list) {
                        resultList.add(entity);
                }
        
        return resultList;
        }
}

