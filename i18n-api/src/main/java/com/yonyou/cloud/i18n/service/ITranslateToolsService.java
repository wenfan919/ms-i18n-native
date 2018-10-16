package com.yonyou.cloud.i18n.service;

import com.wordnik.swagger.annotations.ApiOperation;
import com.yonyou.cloud.i18n.constants.AppI18nConstant;
import com.yonyou.cloud.middleware.rpc.Async;
import com.yonyou.cloud.middleware.rpc.RemoteCall;
import com.yonyou.cloud.mwclient.servmeta.annotation.ApiParam;
import com.yonyou.cloud.mwclient.servmeta.annotation.ApiReturnValue;
import com.yonyou.cloud.translate.entity.Translate;

import java.util.List;
import java.util.Properties;

/**
 * 国际化工具的接口
 * 执行国际化工具完成后对资源的数据库保存，主要用于后续的翻译工作
 *
 * @author yy
 */
@RemoteCall(AppI18nConstant.APP_I18N_PROVIDER)
public interface ITranslateToolsService {

    /**
     * server -> provider
     * 执行国际化工具完成后对资源的数据库保存，主要用于后续的翻译工作
     * 异步调用。
     *
     * @param properties
     * @return Boolean
     */
    @Async
    @ApiOperation("执行国际化工具完成后对资源的数据库保存，主要用于后续的翻译工作")
    public @ApiReturnValue(name = "执行状态", description = "是否执行成功")
    Boolean saveTranslate(@ApiParam(name = "资源", required = true, description = "整体的资源列表") Properties properties) throws Exception;


    /**
     * server -> provider
     * 执行国际化工具完成后对资源的数据库保存，主要用于后续的翻译工作
     * 异步调用。
     *
     * @param properties
     * @return Boolean
     */
    @Async
    @ApiOperation("执行国际化工具完成后对资源的数据库保存，主要是按照编码对资源进行更新，特别是其他的翻译信息的更新")
    public @ApiReturnValue(name = "执行状态", description = "是否执行成功")
    Boolean updateTranslate(@ApiParam(name = "资源", required = true, description = "整体的资源列表") Properties properties,
                            @ApiParam(name = "语种", required = true, description = "描述更新的是哪个语种的数据") String locales) throws Exception;


//    /**
//     * server -> provider
//     * 数据获取： 根据编码获取所有的资源信息
//     *
//     * @param properties
//     * @return List
//     */
////    @Async
//    @ApiOperation("根据编码获取所有资源列表")
//    public @ApiReturnValue(name = "资源列表", description = "资源列表")
//    List getList(@ApiParam(name = "资源", required = true, description = "整体的资源列表") Properties properties) throws Exception;


    /**
     * server -> provider
     * 执行国际化工具完成后对资源的数据库保存，主要用于后续的翻译工作
     * 异步调用。
     *
     * @param list 资源的整体对象
     * @return Boolean
     */
    @Async
    @ApiOperation("执行国际化工具完成后对资源的数据库保存，主要用于后续的翻译工作")
    public @ApiReturnValue(name = "执行状态", description = "是否执行成功")
    Boolean saveTranslate(@ApiParam(name = "资源", required = true, description = "整体的资源列表") List<Translate> list) throws Exception;



}



