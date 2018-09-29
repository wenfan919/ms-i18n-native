/**
 * 
 */
package com.yonyou.i18n.core;

import java.io.File;
import java.util.*;
import java.util.Map.Entry;

import com.yonyou.i18n.model.MLResElement;
import com.yonyou.i18n.model.MLResSubstitution;
import com.yonyou.i18n.model.PageNode;
import com.yonyou.i18n.utils.*;


/**
 * 抽取中文字符
 * 
 * @author wenfa
 *
 */
public class ExtractChar {

	static String testMultiLangResourceType = ConfigUtils.getPropertyValue("testMultiLangResourceType");
	static String parseProjectPath = ConfigUtils.getPropertyValue("parseProjectPath");
	static String importJavaMessagesStr = ConfigUtils.getPropertyValue("importJavaMessagesStr");
	static String importJSMessagesStr = ConfigUtils.getPropertyValue("importJSMessagesStr");
	static String replaceJavaString = ConfigUtils.getPropertyValue("replaceJavaString");
	static String replaceHTMLString = ConfigUtils.getPropertyValue("replaceHTMLString");
	static String replaceJSString = ConfigUtils.getPropertyValue("replaceJSString");

	// 全局的目录清单
	static HashSet<String> keyPrefixs = new HashSet<String>();

	/**
	 * init
	 */
	private static void init(){

		if(testMultiLangResourceType.contains("json")){

			JsonFileUtil jsonFileUtil = new JsonFileUtil();

			jsonFileUtil.init(parseProjectPath + File.separator + "locales", ".json");
			keyPrefixs = jsonFileUtil.getKeyPrefix();

		} else {
			ResourceFileUtil resourceFileUtil = new ResourceFileUtil();

			resourceFileUtil.init(parseProjectPath + File.separator + "locales", ".properties");
			keyPrefixs = resourceFileUtil.getKeyPrefix();
		}


	}
	
	/**
	 * 执行抽取
	 * 
	 * 该部分可以采用多线程去进行文件的解析工作，主要抽取出需要替换的中文字符串
	 * 
	 * *******java文件*****
	 * 同时将抽取出来的中文字符串以目录结构层次以及流水码进行编码写入map中
	 * 暂时按照项目统一保存至message.properties文件，之后按需要再进行资源拆分
	 * 
	 * 之后将map写入properties文件
	 * 
	 * ********html文件******
	 * 
	 */
	public static void doExtract(List<PageNode> pageNodes){

		init();

		for (PageNode pageNode : pageNodes) { 
			
			// 针对每个独立的文件，进行中文的解析
			if(pageNode.isFile()){
				
				writeMap2Node(pageNode);
				
				doDataWash(pageNode);
			}
		}
		
	}
	
	/**
	 * 将文件中的获取的中文map信息转换到pageNode对象中
	 * 
	 * @param pageNode
	 */
	public static void writeMap2Node(PageNode pageNode){
		
		File  file = pageNode.getFile();
		
		ArrayList<MLResSubstitution> substitutions = pageNode.getSubstitutions();
		
		try {
			LinkedHashMap<Integer, LinkedHashMap<Integer, String>> theWholeChineseInfo = null;
			
			if("html".equalsIgnoreCase(pageNode.getType())){
				theWholeChineseInfo = MatchChar.htmlSearch(file);
			}else{
				theWholeChineseInfo = MatchChar.search(file);
			}
			
			Iterator<Entry<Integer, LinkedHashMap<Integer, String>>>  fileInfo = theWholeChineseInfo.entrySet().iterator();
			
			// 识别文件中存在中文的行，给最后的写入文件使用
			List<Integer> existChiLine = new ArrayList<Integer>();
			boolean isLineExistChinese = false;
			
			// 基于文件的中文信息
			while(fileInfo.hasNext()){
				
				pageNode.setExistChinese(true);// 存在中文信息
				
				Entry<Integer, LinkedHashMap<Integer, String>> theFileInfo = fileInfo.next();
				
				int i = theFileInfo.getKey().intValue();
				LinkedHashMap<Integer, String> lineInfo = theFileInfo.getValue();
				
				Iterator<Entry<Integer, String>> theLineInfo = lineInfo.entrySet().iterator(); 
				
				isLineExistChinese = false;
				// 基于行的中文信息
				while(theLineInfo.hasNext()){
					isLineExistChinese=true;
					Entry<Integer, String> filedInfo = theLineInfo.next();
					int j = filedInfo.getKey().intValue();
					String v = filedInfo.getValue();
					
					// 先判断该值在本文件的多语资源清单中是否存在
					// 如果存在，则找到该资源，并将i、j录入元素对象中
					// 如果不存在，则新建对象并写入list
					boolean isExist = false;
					for(MLResSubstitution substitution : substitutions){
						if(substitution.getValue().equals(v)){
							isExist = true;
							
							MLResElement ele = new MLResElement();
							ele.setValue(v);
							ele.setLine(i);
							ele.setIndex(j);
							ele.setLength(v.length());
							substitution.getElements().add(ele);
						}
					}
					if(!isExist){
						MLResSubstitution res = new MLResSubstitution();
						res.setValue(v);
						MLResElement ele = new MLResElement();
						ele.setValue(v);
						ele.setLine(i);
						ele.setIndex(j);
						ele.setLength(v.length());
						res.getElements().add(ele);
						pageNode.getSubstitutions().add(res);
					}
				}
				
				if(isLineExistChinese){
					existChiLine.add(i);
				}
			}
			
			pageNode.setExistChiLine(existChiLine);
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	/**
	 * 获取key的前缀
	 * 
	 * @param keyPrefixs
	 * @param pageNode
	 * @return
	 */
	public static String getKeyPrefix(HashSet<String> keyPrefixs, PageNode pageNode){
		
		
//		String keyPrefix = pageNode.getType().substring(0, 2) +"." + pageNode.getParent().getResModuleName()+"." + pageNode.getResModuleName()+".";
		StringBuffer sb = new StringBuffer();
		
		
		sb.append(pageNode.getType().length() >= 2 ? pageNode.getType().substring(0, 2) : pageNode.getType());
		sb.append(".");
		sb.append(pageNode.getParent().getResModuleName().length() >= 3 ? pageNode.getParent().getResModuleName().substring(0, 3) : pageNode.getParent().getResModuleName());
		sb.append(".");
		sb.append(pageNode.getResModuleName().length() >= 3 ? pageNode.getResModuleName().substring(0, 3) : pageNode.getResModuleName());
		
		if(keyPrefixs.contains(sb.toString())){
			// size + 1
			int size = 0;
			for(String str : keyPrefixs){
				if(str.startsWith(sb.toString())){
					size ++;
				}
			}
			sb.append(size);
		}
		
		// add to list
		keyPrefixs.add(sb.toString());
		
		return sb.append(".").toString();
	
	}
	
	/**
	 * 执行数据的清洗操作--针对单个文件及pageNode && 数据已经被归类了
	 * 
	 * 将pageNode中的数据进行完善：
	 * 主要是对资源数据的key值进行定义，对不同类型的文件的资源进行替换时需要考虑不同的写入内容
	 * 
	 */
	public static void doDataWash(PageNode pageNode){
		
		ArrayList<MLResSubstitution> substitutions = pageNode.getSubstitutions();
		
		// 针对java文件的处理
		if("java".equals(pageNode.getType())){
			
			pageNode.setAddContent(importJavaMessagesStr);
			
			int flowNumber = 1;
			
			String keyPrefix = getKeyPrefix(keyPrefixs, pageNode);
			
			for(MLResSubstitution substitution : substitutions){

				flowNumber ++;

				// key：通过父级节点的模块名称+“.”+本模块名称+“.”+流水生成
				String key = keyPrefix + NumberUtils.getSeqNumByLong(new Long(flowNumber), 4);

				substitution.setKey(key);
				
				// MessageSourceUtil.getMessage(\"{0}\", null, \"{1}\")
				String replaceStr = replaceJavaString.replace("{0}", key).replace("{1}", StringUtils.getStrByDeleteBoundary(substitution.getValue()));

				substitution.setReplaceStr(replaceStr);
				
				// pageNode
				substitution.setPageNode(pageNode);
			}
			
		}else if("html".equals(pageNode.getType())){
			
//			pageNode.setAddContent(importMessagesStr);
			
			int flowNumber = 1;
			
			String keyPrefix = getKeyPrefix(keyPrefixs, pageNode);

			for(MLResSubstitution substitution : substitutions){
				
				// key
				// key：通过父级节点的模块名称+“.”+本模块名称+“.”+流水生成
				String flowString = NumberUtils.getSeqNumByLong(new Long(flowNumber), 4);
				String key = keyPrefix + flowString;
				flowNumber ++;
				substitution.setKey(key);
				
				// replaceStr
				// <label class=\"i18n\" name=\"{0}\"/>
				String replaceStr = replaceHTMLString.replace("{0}", key);
				substitution.setReplaceStr(replaceStr);
				
				// pageNode
				substitution.setPageNode(pageNode);
				
			}
			
		}else if("js".equals(pageNode.getType())){
			
			pageNode.setAddContent(importJSMessagesStr);
			
			int flowNumber = 1;
			String keyPrefix = getKeyPrefix(keyPrefixs, pageNode);
//			String keyPrefix = pageNode.getType() +"." + pageNode.getParent().getResModuleName()+"." + pageNode.getResModuleName()+".";
			
			for(MLResSubstitution substitution : substitutions){
				
				// key
				// key：通过父级节点的模块名称+“.”+本模块名称+“.”+流水生成
				String flowString = NumberUtils.getSeqNumByLong(new Long(flowNumber), 4);
				String key = keyPrefix + flowString;
				flowNumber ++;
				substitution.setKey(key);
				
				// replaceStr
				// $.i18n.prop(\'{0}\')
				String replaceStr = replaceJSString.replace("{0}", key).replace("{1}", StringUtils.getStrByDeleteBoundary(substitution.getValue()));
				substitution.setReplaceStr(replaceStr);
				
				// pageNode
				substitution.setPageNode(pageNode);
				
			}
			
		}
		 
	}
	
	
	
} 
