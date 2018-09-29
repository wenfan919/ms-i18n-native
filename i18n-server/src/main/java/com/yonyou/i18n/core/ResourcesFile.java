/**
 *
 */
package com.yonyou.i18n.core;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.*;
import java.util.Map.Entry;

import com.google.gson.Gson;
import com.google.gson.JsonParser;
import org.apache.log4j.Logger;

import com.google.gson.JsonObject;
import com.yonyou.i18n.model.MLResSubstitution;
import com.yonyou.i18n.model.OrderedProperties;
import com.yonyou.i18n.model.PageNode;
import com.yonyou.i18n.utils.ConfigUtils;
import com.yonyou.i18n.utils.Helper;
import com.yonyou.i18n.utils.StringUtils;

/**
 * 读取资源文件&&写入资源文件
 * <p>
 * 资源文件在 WEB-INF/i18n 目录下
 *
 * @author wenfa
 */
public class ResourcesFile {

    private static Logger logger = Logger.getLogger(ResourcesFile.class);

    static String parseProjectPath = ConfigUtils.getPropertyValue("parseProjectPath");

    static String testMultiLangResourceType = ConfigUtils.getPropertyValue("testMultiLangResourceType");

    static String resourcePrefix = ConfigUtils.getPropertyValue("resourcePrefix");

    static String resourceFileEncoding = ConfigUtils.getPropertyValue("resourceFileEncoding");

    static String resourceDirectory = ConfigUtils.getPropertyValue("resourceDirectory");


    /**
     * 将抽取出来的资源写入资源 文件中
     *
     * @param pageNodes
     */
    public static void writeResourceFile(List<PageNode> pageNodes) {

        // 读取配置，确定生成类型的文件
        Map<String, String> mlrtMap = StringUtils.getResourceFileList(resourcePrefix, testMultiLangResourceType);

        Iterator<Entry<String, String>> mlrts = mlrtMap.entrySet().iterator();

        while (mlrts.hasNext()) {

            Entry<String, String> mlrt = mlrts.next();
            String locales = mlrt.getKey().toUpperCase();
            locales = locales.length() > 2 ? locales.substring(0, 2) : locales;
            locales = "zh".equalsIgnoreCase(locales) ? "" : locales;

            File file = new File(parseProjectPath + File.separator + mlrt.getValue());

            // 针对pageNodes生成资源文件
            if (mlrt.getValue().toLowerCase().endsWith(".properties")) {
                writePropertiesFile(file, pageNodes, locales);
            } else if (mlrt.getValue().toLowerCase().endsWith(".json")) {
                writeJsonFile(file, pageNodes, locales);
            }
        }
    }


    /**
     * 将抽取出来的资源写入资源 文件中
     *
     * @param pageNodes
     */
    public static void writeResourceFileByDirectory(List<PageNode> pageNodes) {

//		Map<String, String> mlrtMap = ;

        Iterator<Entry<String, String>> mlrts = null;

        // 将资源文件放到locales目录下
        // 将资源文件放到分层的目录下
        for (PageNode pageNode : pageNodes) {

            if (pageNode.isFile() && pageNode.getSubstitutions().size() > 0) {

                String resourceSubDirect = parseProjectPath + File.separator + resourceDirectory + File.separator + pageNode.getParent().getResModuleName() + File.separator + pageNode.getResModuleName();
                File fileDirect = new File(resourceSubDirect);
                if (!fileDirect.isDirectory()) fileDirect.mkdirs();

                // 按照类型生成文件
                mlrts = StringUtils.getResourceFileList(resourcePrefix, testMultiLangResourceType).entrySet().iterator();

                while (mlrts.hasNext()) {

                    Entry<String, String> mlrt = mlrts.next();
                    String locales = mlrt.getKey().toUpperCase();
                    locales = locales.length() > 2 ? locales.substring(0, 2) : locales;
                    locales = "zh".equalsIgnoreCase(locales) ? "" : locales;

                    File file = new File(resourceSubDirect + File.separator + mlrt.getValue());

                    // properties资源文件
                    if (mlrt.getValue().toLowerCase().endsWith(".properties")) {
                        writeSinglePropertiesFile(file, pageNode, locales);
                        // json资源文件
                    } else if (mlrt.getValue().toLowerCase().endsWith(".json")) {
                        writeSingleJsonFile(file, pageNode, locales);

                    }
                }
            }
        }
    }


    /**
     * 将pageNode的资源以locales 的形式保持至文件中
     *
     * @param file
     * @param pageNodes
     * @param locales
     */
    private static void writePropertiesFile(File file, List<PageNode> pageNodes, String locales) {

        BufferedWriter output = null;

        try {
            // 为了保证资源的顺序，采用LinkedHashSet存储
            OrderedProperties prop = new OrderedProperties();

            if (file.exists())
                prop.load(new InputStreamReader(new FileInputStream(file), resourceFileEncoding));

            output = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), resourceFileEncoding));

            // 设置属性值
            for (PageNode pageNode : pageNodes) {
                ArrayList<MLResSubstitution> rss = pageNode.getSubstitutions();

                for (MLResSubstitution rs : rss) {
                    // 在写入资源文件时，去掉前后的界定符号
                    String v = rs.getValue();
                    if (v.length() <= 2) continue;

                    prop.setProperty(rs.getKey(), Helper.unwindEscapeChars(StringUtils.getStrByDeleteBoundary(v)) + locales);
                }
            }

            // 保存属性值
            prop.store(output, "create the resource file");

            output.flush();
            output.close();

        } catch (IOException io) {
            io.printStackTrace();
        } finally {
            if (output != null) {
                try {
                    output.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    /**
     * 将pageNode的资源以locales 的形式保持至文件中
     *
     * @param file
     * @param pageNode
     * @param locales
     */
    private static void writeSinglePropertiesFile(File file, PageNode pageNode, String locales) {

        BufferedWriter output = null;

        try {
            // 为了保证资源的顺序，采用LinkedHashSet存储
            // 该部分需要在output之前进行读取保存
            OrderedProperties props = new OrderedProperties();

            if (file.exists())
                props.load(new InputStreamReader(new FileInputStream(file), resourceFileEncoding));

            // 该部分会将源文件清空，然后再写入
            output = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), resourceFileEncoding));

            ArrayList<MLResSubstitution> rss = pageNode.getSubstitutions();

            for (MLResSubstitution rs : rss) {
                // 在写入资源文件时，去掉前后的界定符号
                String v = rs.getValue();
                if (v.length() <= 2) continue;

                props.setProperty(rs.getKey(), Helper.unwindEscapeChars(StringUtils.getStrByDeleteBoundary(v)) + locales);
            }

            // 保存属性值
            props.store(output, "create the resource file");

            output.flush();
            output.close();

        } catch (IOException io) {
            io.printStackTrace();
        } finally {
            if (output != null) {
                try {
                    output.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }

    /**
     * 将pageNode的资源以locales 的形式保持至文件中
     *
     * @param file
     * @param pageNodes
     * @param locales
     */
    private static void writeJsonFile(File file, List<PageNode> pageNodes, String locales) {

        BufferedWriter output = null;

        try {

            JsonObject object = new JsonObject(); //创建Json格式的数据
            if (file.exists()) {
                object = new JsonParser().parse(new InputStreamReader(new FileInputStream(file), resourceFileEncoding)).getAsJsonObject();
            }

            output = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), resourceFileEncoding));

            // 设置属性值
            for (PageNode pageNode : pageNodes) {
                ArrayList<MLResSubstitution> rss = pageNode.getSubstitutions();
                for (MLResSubstitution rs : rss) {
                    // 在写入资源文件时，去掉前后的界定符号
                    String v = rs.getValue();
                    if (v.length() <= 2) continue;

                    object.addProperty(rs.getKey(), Helper.unwindEscapeChars(StringUtils.getStrByDeleteBoundary(v)) + locales);
                }
            }

            // 保存属性值
            output.write(object.toString());

            output.flush();
            output.close();

        } catch (IOException io) {
            io.printStackTrace();
        } finally {
            if (output != null) {
                try {
                    output.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    /**
     * 将pageNode的资源以locales 的形式保持至文件中
     *
     * @param file
     * @param pageNode
     * @param locales
     */
    private static void writeSingleJsonFile(File file, PageNode pageNode, String locales) {

        BufferedWriter output = null;

        try {

            JsonObject object = new JsonObject(); //创建Json格式的数据

            if (file.exists()) {
                object = new JsonParser().parse(new InputStreamReader(new FileInputStream(file), resourceFileEncoding)).getAsJsonObject();
            }

            // 该部分会将源文件清空，然后再写入
            output = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), resourceFileEncoding));

            // 设置属性值
            ArrayList<MLResSubstitution> rss = pageNode.getSubstitutions();
            for (MLResSubstitution rs : rss) {
                // 在写入资源文件时，去掉前后的界定符号
                String v = rs.getValue();
                if (v.length() <= 2) continue;

                object.addProperty(rs.getKey(), Helper.unwindEscapeChars(StringUtils.getStrByDeleteBoundary(v)) + locales);
            }

            // 保存属性值
            output.write(object.toString());

            output.flush();
            output.close();

        } catch (IOException io) {
            io.printStackTrace();
        } finally {
            if (output != null) {
                try {
                    output.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 将抽取出来的资源写入资源 文件中
     * TODO
     * 做英文资源文件，测试用
     *
     * @param pageNodes
     */
    @Deprecated
    public static void writeEnglishResourceFile(List<PageNode> pageNodes) {

        File file = new File(ConfigUtils.getPropertyValue("testENGResourcesDirectory"));

        // 为了保证资源的顺序，采用LinkedHashSet存储
        OrderedProperties prop = new OrderedProperties();

//		OutputStream output = null;
        BufferedWriter output = null;

        try {
//			 output = new FileOutputStream(file);
            output = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), resourceFileEncoding));

            // 设置属性值
            for (PageNode pageNode : pageNodes) {
                ArrayList<MLResSubstitution> rss = pageNode.getSubstitutions();
                for (MLResSubstitution rs : rss) {
                    // 在写入资源文件时，去掉前后的界定符号
                    String v = rs.getValue();
                    if (v.length() <= 2) continue;
                    v = v.substring(1);
                    v = v.substring(0, v.length() - 1);
                    prop.setProperty(rs.getKey(), StringUtils.getStrByDeleteBoundary(v) + "EN");
                }
            }

            // 保存属性值
            prop.store(output, "create the resource file");

        } catch (IOException io) {
            io.printStackTrace();
        } finally {
            if (output != null) {
                try {
                    output.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    /**
     * 将抽取出来的资源写入资源 文件中
     *
     * @param pageNodes
     */
    @Deprecated
    public static void writeJsonResourceFile(List<PageNode> pageNodes) {

        File file = new File(""/*testResourcesDirectory*/);

        // 为了保证资源的顺序，采用LinkedHashSet存储
        OrderedProperties prop = new OrderedProperties();

//		OutputStream output = null;
        BufferedWriter output = null;

        try {
//			 output = new FileOutputStream(file);
            output = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), resourceFileEncoding));

            // 设置属性值
            for (PageNode pageNode : pageNodes) {
                ArrayList<MLResSubstitution> rss = pageNode.getSubstitutions();
                for (MLResSubstitution rs : rss) {
                    // 在写入资源文件时，去掉前后的界定符号
                    String v = rs.getValue();
                    if (v.length() <= 2) continue;

//					ISNConvert.string2Unicode(Helper.unwindEscapeChars(v.trim()));
                    prop.setProperty(rs.getKey(), Helper.unwindEscapeChars(StringUtils.getStrByDeleteBoundary(v))/*v.trim()*/);
                }
            }

            // 保存属性值
            prop.store(output, "create the resource file");

        } catch (IOException io) {
            io.printStackTrace();
        } finally {
            if (output != null) {
                try {
                    output.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    /**
     * 将抽取出来的资源写入资源 文件中
     * TODO
     * 做英文资源文件，测试用
     *
     * @param pageNodes
     */
    @Deprecated
    public static void writeJsonEnglishResourceFile(List<PageNode> pageNodes) {

        File file = new File(ConfigUtils.getPropertyValue("testENGResourcesDirectory"));

        // 为了保证资源的顺序，采用LinkedHashSet存储
        OrderedProperties prop = new OrderedProperties();

//		OutputStream output = null;
        BufferedWriter output = null;

        try {
//			 output = new FileOutputStream(file);
            output = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), resourceFileEncoding));

            // 设置属性值
            for (PageNode pageNode : pageNodes) {
                ArrayList<MLResSubstitution> rss = pageNode.getSubstitutions();
                for (MLResSubstitution rs : rss) {
                    // 在写入资源文件时，去掉前后的界定符号
                    String v = rs.getValue();
                    if (v.length() <= 2) continue;
                    v = v.substring(1);
                    v = v.substring(0, v.length() - 1);
                    prop.setProperty(rs.getKey(), StringUtils.getStrByDeleteBoundary(v) + "EN");
                }
            }

            // 保存属性值
            prop.store(output, "create the resource file");

        } catch (IOException io) {
            io.printStackTrace();
        } finally {
            if (output != null) {
                try {
                    output.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    /**
     * 读取资源文件
     */
    @Deprecated
    public static void readResourceFile() {

        File file = new File(""/*testResourcesDirectory*/);

        Properties props = null;

        try {
//			InputStream in = ConfigUtils.class.getResourceAsStream("/messages.properties");

            InputStreamReader read = new InputStreamReader(new FileInputStream(file), "GBK");// 考虑到编码格式

            props = new Properties();

            props.load(read);


        } catch (IOException e) {
        }

    }

    public static void main(String[] args) {

        File file = new File("C:\\Users\\wenfa\\Desktop\\workbench\\aabc\\dbcde\\ab.cc");
        file.mkdirs();

//			String url1 = ResourcesFile.class.getResource("").getPath().replaceAll("%20", " ");
//          String path = url1.substring(0, url1.indexOf("WEB-INF")) + "WEB-INF/i18n/logs.properties";
//			String path=Thread.currentThread().getContextClassLoader().getResource("").toString();
//			URL url = ResourcesFile.class.getClassLoader().getResource("");

    }

}


