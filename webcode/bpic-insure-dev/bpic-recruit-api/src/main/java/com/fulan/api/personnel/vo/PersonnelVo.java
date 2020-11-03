package com.fulan.api.personnel.vo;

import java.io.Serializable;
import java.util.List;

import com.bpic.system.domain.Attachment;
import com.fulan.api.personnel.domain.Personnel;


import lombok.Data;

/**
 * <p>
 * 人才信息主表
 * </p>
 *
 * @author fulan123
 * @since 2018-01-19
 */
@Data
public class PersonnelVo  implements Serializable{


	/**
	 * 
	 */
	private static final long serialVersionUID = -7868362327445883063L;

	/**
	 * 
	 */

	private Personnel personnel;
	
	List<Attachment> imgList;
	

	
}
