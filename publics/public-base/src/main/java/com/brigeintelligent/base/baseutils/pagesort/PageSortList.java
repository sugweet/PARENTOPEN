package com.brigeintelligent.base.baseutils.pagesort;

import com.brigeintelligent.base.basemethod.BaseException;
import lombok.Builder;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @ClassName PageSortList
 * @Description List集合分页排序通用工具类
 * @Author Sugweet Chen
 * @Date 2019/8/5 22:52
 * @Version 1.0
 **/
@Slf4j
public class PageSortList {

    /**
     * 分页排序
     *
     * @param list
     * @param pageSize
     * @param pageNum
     * @param sortColumns 倒序时加“_d” 例如：id_d
     * @return
     */
    public static PageList getPageList(List<?> list, Integer pageSize, Integer pageNum, String sortColumns) {

        if (pageNum == 0 && pageSize == 0) {
            pageSize = Integer.MAX_VALUE;
        }
        // 总页数
        int totalPages = list.size() / pageSize;
        if (list.size() % pageSize > 0) {
            totalPages += 1;
        }
        // sortColumns为空则不排序
        if (StringUtils.isNotEmpty(sortColumns)) {

            String[] columns = sortColumns.split(",");
            for (String column : columns) {
                list.sort((a, b) -> {
                    int i = 0;
                    try {
                        String[] split = column.split("_");
                        // 将属性名首字母大写
                        String name = Character.toUpperCase(split[0].charAt(0)) + split[0].substring(1);
                        Method m1 = (a).getClass().getMethod("get" + name, (Class<?>[]) null);
                        Method m2 = (b).getClass().getMethod("get" + name, (Class<?>[]) null);
                        if (split.length == 2 && "d".equals(split[1]))
                            // 倒序
                            i = m2.invoke((b), (Object[]) null).toString().compareTo(
                                    m1.invoke((a), (Object[]) null).toString());
                        else
                            // 正序
                            i = m1.invoke((a), (Object[]) null).toString().compareTo(
                                    m2.invoke((b), (Object[]) null).toString());
                    } catch (Exception e) {
                        log.error("============list集合分页排序异常", e);
                        throw new BaseException("list集合分页排序异常");
                    }
                    return i;
                });
            }
        }

        // 分页查询
        List<?> collect = list.stream().skip(pageSize * pageNum).limit(pageSize).collect(Collectors.toList());

        return PageList.builder()
                .objectList(collect)
                .totalPages(totalPages)
                .totalElements((long) list.size())
                .build();
    }

    /**
     * List分页排序实体
     */
    @Getter
    @Builder
    public static class PageList {

        // 总页数
        private Integer totalPages;
        // 总条数
        private Long totalElements;
        // 分页排序集合
        private List objectList;

    }

    public static void main(String[] args) {
        List<Student> list = new ArrayList<>();
        Student student = new Student("1", "张小凡", "1");
        Student student1 = new Student("2", "陆雪琪", "3");
        Student student2 = new Student("3", "碧瑶", "7");
        Student student3 = new Student("4", "鬼厉", "2");
        list.add(student);
        list.add(student1);
        list.add(student2);
        list.add(student3);
        System.out.println(list);
        PageList pageList = PageSortList.getPageList(list, 2, 1, "id_d");
        System.out.println(pageList.getObjectList());
        System.out.println(pageList.getTotalElements());
        System.out.println(pageList.getTotalPages());

    }
}
