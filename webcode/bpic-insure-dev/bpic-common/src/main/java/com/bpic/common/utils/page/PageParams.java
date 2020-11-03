package com.bpic.common.utils.page;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Map;

@Data
@Api(tags = "PageParams", description = "分页实体类")
public class PageParams<T>{

    @ApiModelProperty(value="当前页数)")
    private int current=1;

    @ApiModelProperty(value="每页显示条数")
    private int pageSize=10;

    @ApiModelProperty(value="排序参数map",example = "{'id':true}")
    private Map<String,Boolean> sortParams;

    @ApiModelProperty(value="模糊查询map",example = "{'id':'111'}")
    private Map<String,String> searchFuzzy;

    @ApiModelProperty(value="时间查询,开始时间",example = "{'create_date':'2019-01-24'}")
    private Map<String,String> sectionStartParams;

    @ApiModelProperty(value="时间查询,结束时间",example = "{'create_date':'2020-01-24'}")
    private Map<String,String> sectionEndParams;

    @ApiModelProperty(value="查询实体VO")
    T t;

}
