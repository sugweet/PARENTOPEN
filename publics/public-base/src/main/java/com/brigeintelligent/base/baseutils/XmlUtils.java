package com.brigeintelligent.base.baseutils;

import com.brigeintelligent.base.basemethod.BaseException;
import com.brigeintelligent.base.baseutils.pagesort.Student;
import org.dom4j.*;
import org.dom4j.io.SAXReader;

import java.beans.PropertyDescriptor;
import java.io.ByteArrayInputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @Description：xml文件解析工具类
 * @Author：Sugweet
 * @Time：2019/8/6 10:42
 */
public class XmlUtils {

    /**
     * 将xml文件转成实体对象集合
     *
     * @param xmlFile
     *
     * @return
     */
    @SuppressWarnings("unchecked")
    public static List xmlToEntitys(String xmlFile, Class<?> clazz) {
        SAXReader saxReader = new SAXReader();
        List list = null;
        try {
            ByteArrayInputStream bis = new ByteArrayInputStream(xmlFile.getBytes());
            Document document = saxReader.read(bis);
            // 获取到xml中的实体对象
            Element element = document.getRootElement();
            Iterator rootElement = element.elementIterator();
            list = new ArrayList();

            // 迭代element将获取到的内容放入实体对象中
            while (rootElement.hasNext()) {
                // 创建一个实体对象
                Object obj = clazz.newInstance();

                // 获取对象的属性
                Field[] fields = obj.getClass().getDeclaredFields();

                // 获取xml的头
                Element objElement = (Element) rootElement.next();

                // 获取xml的头属性
                Attribute attribute = objElement.attribute(0);
                for (Field field : fields) {
                    if (field.getName().equals(attribute.getName())) {
                        // 获取对应实体对象的set方法
                        PropertyDescriptor pd = new PropertyDescriptor(field.getName(), obj.getClass());
                        Method setMethod = pd.getWriteMethod();
                        // 将属性值set进实体对象中
                        setMethod.invoke(obj, attribute.getValue());
                        break;
                    }
                }

                // 获取xml的子属性
                Iterator objIt = objElement.elementIterator();
                while (objIt.hasNext()) {
                    Element child = (Element) objIt.next();
                    for (Field field : fields) {
                        if (child.getName().equals(field.getName())) {
                            // 获取对应实体对象的set方法
                            PropertyDescriptor pd = new PropertyDescriptor(field.getName(), obj.getClass());
                            Method setMethod = pd.getWriteMethod();
                            // 将属性值set进实体对象中
                            setMethod.invoke(obj, child.getStringValue());
                        }
                    }
                }
                list.add(obj);
                obj = null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    /**
     * xml文档Document转对象
     *
     * @param document  
     * @param clazz
     * @return
     */
    public static Object getObject(Document document, Class<?> clazz) {
        Object obj = null;
        //获取根节点
        Element root = document.getRootElement();
        try {
            obj = clazz.newInstance();//创建对象
            List<Element> properties = root.elements();
            for (Element pro : properties) {
                //获取属性名(首字母大写)
                String propertyname = pro.getName();
                String propertyvalue = pro.getText();
                Method m = obj.getClass().getMethod("set" + propertyname, String.class);
                m.invoke(obj, propertyvalue);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return obj;
    }

    /**
     * xml字符串转对象
     *
     * @param xmlString  
     * @param clazz
     * @return
     */
    public static Object getObject(String xmlString, Class<?> clazz) {
        Document document = null;
        try {
            document = DocumentHelper.parseText(xmlString);
        } catch (DocumentException e) {
            throw new BaseException("获取Document异常" + xmlString);
        }

        return getObject(document, clazz);
    }

    /**
     * 对象转xml文件
     *
     * @param b
     * @return
     */
    public static Document getDocument(Object b) {
        Document document = DocumentHelper.createDocument();
        try {
            // 创建根节点元素
            Element root = document.addElement(b.getClass().getSimpleName());
            Field[] field = b.getClass().getDeclaredFields(); // 获取实体类b的所有属性，返回Field数组
            for (int j = 0; j < field.length; j++) { // 遍历所有有属性
                String name = field[j].getName(); // 获取属属性的名字
                if (!name.equals("serialVersionUID")) {//去除串行化序列属性
                    name = name.substring(0, 1).toUpperCase()
                            + name.substring(1); // 将属性的首字符大写，方便构造get，set方法
                    Method m = b.getClass().getMethod("get" + name);
                    // System.out.println("属性get方法返回值类型:" + m.getReturnType());
                    String propertievalue = (String) m.invoke(b);// 获取属性值
                    Element propertie = root.addElement(name);
                    propertie.setText(propertievalue);
                }
            }

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();

        }

        return document;
    }

    /**
     * 对象转xml格式的字符串
     *
     * @param b
     * @return
     */

    public static String getXmlString(Object b) {
        return getDocument(b).asXML();
    }

    public static void main(String[] args) {
        String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\" ?>\n" +
                "<Service>\n" +
                "    <student id=\"1\">\n" +
                "        <name>张小凡</name>\n" +
                "        <age>18</age>\n" +
                "    </student>\n" +
                "</Service>";

        List list = XmlUtils.xmlToEntitys(xml, Student.class);
        System.out.println(list);

        //一般情况
        Student student = new Student("1","张小凡","18");

        String xmlString = XmlUtils.getXmlString(student);
//        <?xml version="1.0" encoding="UTF-8"?>
//        <Student><Id>1</Id><Name>张小凡</Name><Age>18</Age></Student>
        System.out.println(xmlString);
        System.out.println();
        Object object = XmlUtils.getObject(xmlString, Student.class);
        System.out.println(object);
    }

}
