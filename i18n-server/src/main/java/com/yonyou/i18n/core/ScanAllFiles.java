/**
 * 
 */
package com.yonyou.i18n.core;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.yonyou.i18n.model.PageNode;
import com.yonyou.i18n.utils.ConfigUtils;
import com.yonyou.i18n.utils.LogUtils;

/**
 * 
 * 扫描完整目录，获取所有的需要替换中文的文件
 * 
 * @author wenfa
 *
 */
public class ScanAllFiles {

	private static Logger logger = Logger.getLogger(ScanAllFiles.class);
	
	// 获取需要解析的项目根路径
	static String parseProjectPath = ConfigUtils.getPropertyValue("parseProjectPath");
	static String javasDirectory = ConfigUtils.getPropertyValue("javasDirectory");
	static String scanFileType = ConfigUtils.getPropertyValue("scanFileType");
	static String defaultProject = ConfigUtils.getPropertyValue("defaultProject");
	static String resourcePrefix = ConfigUtils.getPropertyValue("resourcePrefix");

	public ScanAllFiles(String path){

		if(path != null && !"".equals(path)){
			this.parseProjectPath = path;
			ConfigUtils.props.setProperty("parseProjectPath", path);
		}
	}
			
	/**
	 * 加载定义的所有类型文件
	 * 
	 * @return
	 */
	public  List<PageNode> loadNodes(){
		
		// TODO 找不到路径报错
		File project = new File(parseProjectPath);
		// File[] files = (new File(parseProjectPath)).listFiles(); 
		
		// 判断该目录是否是项目（包含java、UUI、React）
		// 如果是java项目，则存在src文件夹以及.project文件
		// UUI与java项目一致
		// 如果是独立的项目，则按照标准的目录结构进行文件操作:src/main/java;src/main/resources等
		// 如果不是独立的项目，则按照统一的项目 default project 进行操作
		
		List<PageNode> pageNodes = new ArrayList<PageNode>();
		
		PageNode pageNode = new PageNode(project,null,defaultProject);
		
		pageNode.setPrefix(resourcePrefix+"_");
		
		pageNodes.add(pageNode);
		
		//递归扫描
		scanDir(pageNodes, project, scanFileType, pageNode);
		
		logger.info(LogUtils.printMessage("加载待检索文件成功！"));

		return pageNodes; 
	}

	
	/**
	 * 扫描文件夹
	 * 
	 * 如果有扫描文件类型范围的即添加到对象中，如果没有相应的文件类型，则不用添加
	 * 
	 * @param pageNodes
	 * @param file
	 */
	private void scanDir(List<PageNode> pageNodes, File file,String fileType,PageNode parentNode){
		
		// 判断是否存在扫描类型的文件
		if(isHaveTheScanFile(file,  fileType)){
			
			PageNode pageNode = new PageNode(file,file.getParentFile().getName(),parentNode.getRoot());
			
			pageNode.setParent(parentNode);
			pageNode.setRoot(parentNode.getRoot());
			parentNode.getChildrens().add(pageNode);
			pageNodes.add(pageNode);
			
			if(file.isDirectory()){
				
				File[] fileList = file.listFiles();
				
				for (int i = 0; i < fileList.length; i++) {

					scanDir(pageNodes, fileList[i],fileType, pageNode);
					
				}
			} else {
				// set the file type
				String fileName = file.getName();
		        String suffix = fileName.substring(fileName.lastIndexOf(".") + 1);
		        pageNode.setType(suffix);
			}
		}
		
	}

	
	/**
	 * 判断是否含扫描类型的文件
	 * 
	 * @param fold
	 * @return
	 */
	private boolean isHaveTheScanFile(File file, String fileType) {
		
		if(file.isDirectory()){
			
			File[] fileList = file.listFiles();
			
			for (int i = 0 ; i<fileList.length; i++){
				
				if(isHaveTheScanFile(fileList[i],fileType))
					return true;
			}
			
		}else{
			String fileName = file.getName();
	        String suffix = fileName.substring(fileName.lastIndexOf(".") + 1);
	        
			if (fileType.indexOf(suffix) >= 0) {
				return true;
			}
		}
		
		return false;
	}

	

}
