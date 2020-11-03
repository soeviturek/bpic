package com.bpic.system.mapper;

import com.bpic.system.domain.UserSign;
import com.bpic.system.domain.vo.UserSignVo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
@Mapper
public interface UserSignMapper {
    /**
     * 签到
     * @param userSign
     */
    void save(UserSign userSign);

    /**
     * 查询用户是否签到
     * @param cellphone
     * @return
     */
    List<UserSign> queryUserSign(String cellphone);

    /**
     * 每月结束清空记录
     * @param
     */
    void resetUserSign();

    /**
     * 查询最长签到记录
     * @param cellphone
     * @return
     */
    Integer queryMaxDay(String cellphone);


    UserSign queryToday(String cellphone);

    /**
     * 查询昨天是否签到
     * @param cellphone
     * @return
     */
    UserSign queryYesteday(String cellphone);


    List<UserSignVo> queryEarly(String cellphone);
}
