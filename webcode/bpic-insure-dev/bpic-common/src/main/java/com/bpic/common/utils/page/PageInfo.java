package com.bpic.common.utils.page;

import lombok.Data;

import java.util.Collections;
import java.util.List;

/**
 * 分页信息
 */
@Data
public class PageInfo<T> {
	/**
	 * 记录总数
	 */
	private Integer pageRecords;
	/**
	 * 总页数
	 */
	private Integer pageTotal;
	/**
	 * 当前页
	 */
	private Integer pageNo = 1;
	/**
	 * 每页记录数
	 */
	private Integer pageSize = 10;
	/**
	 * 总页数
	 */
	private Integer pages;
	/**
	 * 排序字段
	 */
	/**
	 * 记录总数
	 */
	private String pageSortFiled;
	/**
	 * 排序类型:asc升序  desc降序
	 */
	private String pageSortType;
	
	/**
	 * 数据列表
	 */
	private List<T> records = Collections.emptyList();
	
	private T record;
	
}
