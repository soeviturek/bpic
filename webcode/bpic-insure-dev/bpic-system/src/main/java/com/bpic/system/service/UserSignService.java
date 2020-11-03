package com.bpic.system.service;

import com.bpic.system.domain.UserSign;
import com.bpic.system.domain.vo.UserSignVo;

import java.util.List;

public interface UserSignService {

    void save(UserSign userSign);

    List<UserSign> queryUserSign(String cellphone);

    void resetUserSign();

    Integer queryMaxDay(String cellphone);

    UserSign queryToday(String cellphone);

    UserSign queryYesteday(String cellphone);

    List<UserSignVo> queryEarly(String cellphone);
}
