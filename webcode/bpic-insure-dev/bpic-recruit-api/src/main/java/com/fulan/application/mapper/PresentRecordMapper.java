package com.fulan.application.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.fulan.api.personnel.domain.PresentRecord;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface PresentRecordMapper  extends BaseMapper<PresentRecord>{
	@Select("select MAX(sub_code) as subCode from er_present_record"
			+ " where pre_code = #{preCode} and type=1 "
			)
	String selectLastRecord(@Param(value = "preCode") String preCode);

	
	@Select("select MAX(sub_code) as subCode from er_present_record"
			+ " where pre_code = #{preCode} and type=2 "
			)
	String selectContractLastRecord(@Param(value = "preCode") String preCode);
}