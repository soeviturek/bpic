package com.bpic.system.service;

import com.bpic.common.core.domain.entity.SysLoginUser;
import com.bpic.system.domain.vo.SysLoginUserVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface SysLoginUserService {

    int insertUser(SysLoginUser sysLoginUser);

    int updateUserByCellphone(SysLoginUser sysLoginUser);

    SysLoginUserVo selectByCellphone(String cellphone);

    int updatePwd(SysLoginUser sysLoginUser);

    String queryUnionId(SysLoginUser sysLoginUser);

    int deleteUserByCellphone(String cellphone);

    List<SysLoginUser> selectSxUser(String cellphone,
                                    String user_name,
                                    String start_time,
                                    String end_time);
    Integer selectCount(String cellphone);

    List<SysLoginUserVo> queryrelationList(String cellphone,
                                           String user_name);

    int updateOnlineUser(SysLoginUserVo sysLoginUserVo);
}
