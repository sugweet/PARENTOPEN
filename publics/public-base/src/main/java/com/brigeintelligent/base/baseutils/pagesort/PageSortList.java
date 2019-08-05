package com.brigeintelligent.base.baseutils.pagesort;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @ClassName PageSortList
 * @Description List集合分页排序通用工具类（不支持基本类型及其包装类）
 * @Author Sugweet Chen
 * @Date 2019/8/5 22:52
 * @Version 1.0
 **/
@Slf4j
public class PageSortList<E> {

    /**
     * 分页排序
     *
     * @param list
     * @param pageSize
     * @param pageNum
     * @param sortColumns 倒序时加“_d” 例如：id_d
     * @return
     */
    public PageList getPageList(List<E> list, Integer pageSize, Integer pageNum, String sortColumns) {
        List<E> collect = list.stream().skip(pageSize * pageNum).limit(pageSize).collect(Collectors.toList());
        // 总页数
        int totalPages = list.size() / pageSize;
        if (list.size() % pageSize > 0) {
            totalPages += 1;
        }
        if (StringUtils.isEmpty(sortColumns)) {

            return PageList.builder()
                    .objectList(collect)
                    .totalPages(totalPages)
                    .totalElements((long) list.size())
                    .build();
        }
        String[] columns = sortColumns.split(",");
        for (String column : columns) {
            collect.sort((a, b) -> {
                int ret = 0;
                try {
                    String[] split = column.split("_");
                    // 将属性名首字母大写
                    String name = Character.toUpperCase(split[0].charAt(0)) + split[0].substring(1);
                    Method m1 = ((E) a).getClass().getMethod("get" + name, (Class<?>[]) null);
                    Method m2 = ((E) b).getClass().getMethod("get" + name, (Class<?>[]) null);
                    log.info("========length:" + split.length);
                    if (split.length == 2 && "d".equals(split[1]))// 倒序
                        ret = m2.invoke(((E) b), (Object[]) null).toString().compareTo(
                                m1.invoke(((E) a), (Object[]) null).toString());
                    else
                        // 正序
                        ret = m1.invoke(((E) a), (Object[]) null).toString().compareTo(
                                m2.invoke(((E) b), (Object[]) null).toString());
                } catch (Exception e) {
                    log.error("============list集合分页排序异常", e);

                }
                return ret;
            });
        }

        return PageList.builder()
                .objectList(collect)
                .totalPages(totalPages)
                .totalElements((long) list.size())
                .build();
    }

    public static void main(String[] args) {
        List<Student> list = new ArrayList<>();
        Student student = new Student("张小凡", 1);
        Student student1 = new Student("陆雪琪", 3);
        Student student2 = new Student("碧瑶", 8);
        Student student3 = new Student("鬼厉", 2);
        list.add(student);
        list.add(student1);
        list.add(student2);
        list.add(student3);
        System.out.println(list);
        PageSortList<Student> sortList = new PageSortList<>();
        PageList pageList = sortList.getPageList(list, 4, 0, "name_d,age_d");
        System.out.println(pageList.getObjectList());

    }
}
