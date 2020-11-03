package com.fulan.api.flow.vo;

import java.io.Serializable;
import java.util.List;

import com.fulan.api.flow.domain.Flow;
import com.fulan.api.flow.domain.FlowAction;

import lombok.Data;

/**
 * <p>
 * 流程接收参数
 * </p>
 *
 * @author fulan123
 * @since 2018-01-19
 */
@Data
public class FlowAcceptVo implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 4258620003566609705L;

	Flow flow;
	
	List<FlowAction> flowActionList;

}
