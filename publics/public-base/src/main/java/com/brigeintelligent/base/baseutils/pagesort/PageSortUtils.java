package com.brigeintelligent.base.baseutils.pagesort;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;

import java.util.ArrayList;
import java.util.List;

/**
 * @Description：分页排序工具类
 * @Author：Sugweet
 * @Time：2019/8/5 17:37
 */
@Slf4j
public class PageSortUtils {

    /**
     * Jpa排序方法
     *
     * @param pageSize
     * @param pageNum
     * @param sortColumn
     * @return
     */
    public static PageRequest getPageRequest(Integer pageSize, Integer pageNum, String sortColumn) {
        Sort sort = PageSortUtils.multiSort(sortColumn);
        if (sort == null) {
            return PageRequest.of(pageNum, pageSize);
        }
        return PageRequest.of(pageNum, pageSize, sort);
    }

    /**
     * 获取sort
     *
     * @param colunms
     * @return
     */
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

    /**
     * 处理排序规则
     *
     * @param rule
     * @return
     */
    private static Order multiOrder(String rule) {
        Order order = null;
        String[] rules = rule.split("_");
        if (rules.length == 2) {
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

    /**
     * List集合分页排序
     *
     * @param <E>
     */
    public static class PageSortList<E> {

    }




}
