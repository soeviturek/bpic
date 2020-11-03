package com.fulan.api.personnel.vo;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import com.bpic.common.utils.json.LongJsonSerializer;
import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fulan.api.personnel.domain.WorkExperience;
import com.bpic.common.utils.json.LongJsonDeserializer;


import lombok.Data;

/**
 * <p>
 * 工作经历传参vo
 * </p>
 *
 * @since 2018-01-19
 */
@Data
public class WorkExperienceVO implements Serializable {

    private static final long serialVersionUID = 1L;

   
	private List<WorkExperience> workExperience;
	
	private int isInsuranceCompany;//是否有保险经验
	private int isOverSix;//是否超过6个月
	private String nativeWorkTime;//本地累计工作时间
	private String originalCompany;//原保险机构名称
	private int isQuit;//是否离职(1:是，0，否)
	
	@DateTimeFormat(pattern="yyyy-MM-dd")
	@JsonFormat(pattern="yyyy-MM-dd",timezone="GMT+8")
	private Date departureDate;//离职日期
	@JsonSerialize(using = LongJsonSerializer.class)
	@JsonDeserialize(using = LongJsonDeserializer.class)
	private long personnelId;//代理人id
	

}
