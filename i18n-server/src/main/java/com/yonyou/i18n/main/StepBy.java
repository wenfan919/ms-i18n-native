/**
 * 
 */
package com.yonyou.i18n.main;

import com.yonyou.i18n.core.ExtractChar;
import com.yonyou.i18n.core.ReplaceFile;
import com.yonyou.i18n.core.ResourcesFile;
import com.yonyou.i18n.core.ScanAllFiles;
import com.yonyou.i18n.model.PageNode;

import java.util.List;

/**
 * 
 * 整体的实现步骤是：
 * 
 * 1、 对文件中的中文进行抽取
 * 2、 对中文进行替换
 * 3、 同时需要往文件中写入固定的依赖行
 * @author wenfa
 *
 */
public class StepBy {

	// 所有的数据都是通过该对象进行传递的
	private List<PageNode> pageNodes = null;
	
	/**
	 * 初始化项目目录
	 * 
	 * 加载所有文件
	 */
	public void init(String path){
		
		this.pageNodes = (new ScanAllFiles(path)).loadNodes();
		
	}
	
	/**
	 * 通过字符集范围进行抽取
	 * 
	 * @param
	 */
	public  void extract() {

		ExtractChar.doExtract(pageNodes);
		
				
	}
	
	/**
	 * 写入资源文件
	 */
	public void resource(){
		
		ResourcesFile.writeResourceFile(pageNodes);
		
		// 分目录写入资源文件
		ResourcesFile.writeResourceFileByDirectory(pageNodes);
		
		
	}
	
	/**
	 * 直接替换
	 * @param
	 */
	public  void replace() {

		ReplaceFile.updateFilesByReplace(pageNodes);
		
	}
	

	public static void main(String [] args){




//		logger.info("识别文件：" + sourcePath);
//
//		String path = sourcePath.substring(0, sourcePath.indexOf(".")) + "_" + System.currentTimeMillis();
//
//		String zipFile = path + ".zip";
//
//		path = path + "/";
//
//		logger.info("解压缩路径：" + path);
//
//		ZipUtils.unZipForFilePath(sourcePath, path);

		StepBy sb = new StepBy();

		sb.init("/Users/yanyong/Desktop/controller/java");

		sb.extract();

		sb.resource();

		sb.replace();

//		ZipUtils.zip(new File(zipFile), path);


//		logger.info("执行完成后压缩路径：" + zipFile);
	}
	
	
}
