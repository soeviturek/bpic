package com.bpic.common.core.domain.entity;

import lombok.Data;

import java.util.Date;

@Data
public class News {

    private int id;
    private String type;
    private String title;
    private String content;
    private Date sendTime;
}
