package com.brigeintelligent.base.baseutils.pagesort;

import lombok.Data;

/**
 * @ClassName Student
 * @Description TODO
 * @Author Sugweet Chen
 * @Date 2019/8/5 23:17
 * @Version 1.0
 **/
@Data
public class Student {
    private String id;
    private String name;
    private String age;

    public Student(String id, String name, String age) {
        this.id = id;
        this.name = name;
        this.age = age;
    }

    public Student() {
    }
}
