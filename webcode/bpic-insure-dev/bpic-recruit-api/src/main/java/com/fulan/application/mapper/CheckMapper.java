package com.fulan.application.mapper;

import com.fulan.api.personnel.domain.Check;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CheckMapper {

    Check selectByPersonnelId(@Param("personnelId") Long personnelId);

    int insert(Check check);

}
