package com.bpic.system.mapper;

import com.bpic.common.core.domain.entity.SysLoginUser;
import com.bpic.system.domain.vo.SysLoginUserVo;
import com.bpic.system.domain.vo.TempCdeSalesVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface SysLoginUserMapper {


    int insertUser(SysLoginUser sysLoginUser);

    int updateUserByCellphone(SysLoginUser sysLoginUser);

    SysLoginUserVo selectByCellphone(String cellphone);

    int updatePwd(SysLoginUser sysLoginUser);

    String queryUnionId(SysLoginUser sysLoginUser);

    int deleteUserByCellphone(String cellphone);

    List<SysLoginUser> selectSxUser(@Param("cellphone") String cellphone,
                                  @Param("user_name") String user_name,
                                  @Param("start_time") String start_time,
                                  @Param("end_time") String end_time);


    SysLoginUser selectUserByAgentCode(@Param("agentCode") String agentCode);

    Integer selectCount(String cellphone);

    List<SysLoginUserVo> queryrelationList(@Param("cellphone") String cellphone,
                                           @Param("user_name") String user_name);

    int updateOnlineUser(SysLoginUserVo sysLoginUserVo);


}
