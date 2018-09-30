package com.yonyou.cloud.i18n.controller;

import com.yonyou.iuap.mvc.constants.RequestStatusEnum;
import com.yonyou.iuap.mvc.type.JsonResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.yonyou.iuap.baseservice.controller.GenericController;
import com.yonyou.cloud.i18n.entity.I18n;
import com.yonyou.cloud.i18n.service.TI18nService;
import com.yonyou.iuap.mvc.annotation.FrontModelExchange;
import com.yonyou.iuap.mvc.type.SearchParams;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 说明：国际化 基础Controller——提供数据增、删、改、查、导入导出等rest接口
 *
 * @date 2018-9-28 14:32:11
 */
@Controller
@RequestMapping(value = "/i18n")
public class I18nController extends GenericController<I18n> {

    private Logger logger = LoggerFactory.getLogger(I18nController.class);

//    public TI18nService gettI18nService() {
//        return tI18nService;
//    }

    @Autowired
    public void settI18nService(TI18nService tI18nService) {
        this.tI18nService = tI18nService;
        super.setService(tI18nService);
    }


    private TI18nService tI18nService;

//    public void setTI18nService(TI18nService i18nService) {
//        this.tI18nService = i18nService;
//        super.setService(i18nService);
//    }

    @Override
    public Object list(PageRequest pageRequest,
                       @FrontModelExchange(modelType = I18n.class) SearchParams searchParams) {

        Page<I18n> page = this.tI18nService.selectAllByPage(pageRequest, searchParams);

//        List<I18n> list = page.getContent();
//        for(I18n i18n : list){
//            i18n.setAttachId(i18n.getAttachment().get(0).getOriginalFileName());
//        }
        Map<String, Object> map = new HashMap();
        map.put("data", page);
        return this.buildMapSuccess(map);

//        return super.list(pageRequest, searchParams);
    }


    //    @RequestMapping({"/save"})
//    @ResponseBody
    @Override
    public Object save(@RequestBody I18n entity) {
//        JsonResponse jsonResp;
//        try {
//            this.service.save(entity);
//            jsonResp = this.buildSuccess(entity);
//        } catch (Exception var4) {
//            jsonResp = this.buildError("msg", var4.getMessage(), RequestStatusEnum.FAIL_FIELD);
//        }

        return super.save(entity);
    }


    /**
     * 该部分执行下载（国际化后的文件）
     *
     * @param listData
     * @param request
     * @param response
     * @return
     */
    @RequestMapping({"/download"})
    @ResponseBody
    public Object download(@RequestBody List<I18n> listData, HttpServletRequest request, HttpServletResponse response) {

        System.out.println("++++++++++");
        return super.buildSuccess();
    }


    /**
     * 该部分执行国际化的整体逻辑
     *
     * @param listData
     * @param request
     * @param response
     * @return
     */
    @RequestMapping({"/operation"})
    @ResponseBody
    public Object operation(@RequestBody List<I18n> listData, HttpServletRequest request, HttpServletResponse response) {

        System.out.println("++++++++++");
        return super.buildSuccess();
    }

}