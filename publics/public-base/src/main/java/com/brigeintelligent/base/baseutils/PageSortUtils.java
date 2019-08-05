package com.brigeintelligent.base.baseutils;

import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Description：分页排序工具类
 * @Author：Sugweet
 * @Time：2019/8/5 17:37
 */
public class PageSortUtils {

    public static PageList getPageList(List<Object> list,Integer pageSize, Integer pageNum, String sortColumn){
        List<Object> collect = list.stream().skip(pageSize * pageNum).limit(pageSize).collect(Collectors.toList());

        return null;
    }

    // Jpa排序方法
    public static PageRequest getPageRequest(Integer pageSize, Integer pageNum, String sortColumn) {
        Sort sort = PageSortUtils.multiSort(sortColumn);
        if (sort == null) {
            return PageRequest.of(pageNum, pageSize);
        }
        return PageRequest.of(pageNum, pageSize, sort);
    }

    // 获取sort
    private static Sort multiSort(String... colunms) {
        if (StringUtils.isAllEmpty(colunms)) {
            return null;
        }
        List<Order> orders = new ArrayList<>();
        for (String colunm : colunms) {
            orders.add(multiOrder(colunm));
        }
        return Sort.by(orders);

    }

    // 处理排序规则
    private static Order multiOrder(String rule) {
        Order order = null;
        String[] rules = rule.split("_");
        if (rules.length > 2) {
            if ("d".equals(rules[1])) {
                order = new Order(Direction.DESC, rules[0]);
            } else {
                order = new Order(Direction.ASC, rules[0]);
            }
            return order;
        }
        order = new Order(Sort.Direction.ASC, rules[0]);
        return order;
    }

    @Data
    public class PageList {
        // 总页数
        private Integer totalPages;
        // 总条数
        private Long totalElements;
        // 分页排序集合
        private List<Object> objectList;
    }
}
