package com.brigeintelligent.base.baseutils;

import com.brigeintelligent.base.baseutils.pagesort.Student;
import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.Element;
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
     * 将xml文件转成实体对象
     * @param xmlFile
     * @return
     */
    @SuppressWarnings("unchecked")
    public static List xmlToEntity(String xmlFile,Object entity) {
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
                Object obj = entity.getClass().newInstance();

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
                            setMethod.
                                    invoke(obj, child.getStringValue());
                        }
                    }
                }
                list.add(obj);
                obj=null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public static void main(String[] args) {
        String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\" ?>\n" +
                "<Service>\n" +
                "    <student id=\"1\">\n" +
                "        <name>张小凡</name>\n" +
                "        <age>18</age>\n" +
                "    </student>\n" +
                "</Service>";
        Student student = new Student();
        List list = XmlUtils.xmlToEntity(xml, student);
        System.out.println(list);
    }

}
