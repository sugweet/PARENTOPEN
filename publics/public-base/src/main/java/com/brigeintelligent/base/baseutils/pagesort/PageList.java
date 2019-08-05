package com.brigeintelligent.base.baseutils.pagesort;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

/**
 * @ClassName PageList
 * @Description List分页排序实体
 * @Author Sugweet Chen
 * @Date 2019/8/5 22:53
 * @Version 1.0
 **/
@Getter
@Builder
public class PageList {

    // 总页数
    private Integer totalPages;
    // 总条数
    private Long totalElements;
    // 分页排序集合
    private List objectList;

}
