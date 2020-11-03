package com.bpic.system.service.impl;

import com.bpic.common.utils.DateUtils;
import com.bpic.system.domain.UserSign;
import com.bpic.system.domain.vo.UserSignVo;
import com.bpic.system.mapper.UserSignMapper;
import com.bpic.system.service.UserSignService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Service
@Transactional
@Slf4j
public class UserSignServiceImpl implements UserSignService {

    @Resource
    UserSignMapper userSignMapper;

    /**
     * 保存签到信息
     * @param userSign
     */
    @Override
    public void save(UserSign userSign) {
        //查询前一天是否有签到记录
        UserSign userSigns = queryYesteday(userSign.getUser_mob());
        //如果没有，将最大记录设为从1开始
        if(userSigns == null){
            userSign.setMaxs(1);
        }else{
            //如果有签到记录,最大记录 + 1
            Integer maxs = userSigns.getMaxs();
            Integer j = maxs + 1;
            userSign.setMaxs(j);
        }
        //设置签到日期
        userSign.setSign_date(DateUtils.parseDateToStr("yyyy-MM-dd",new Date()));
        //设置签到记录
        userSign.setIs_signed(1);
        //保存签到记录
        userSignMapper.save(userSign);
    }

    /**
     * 查询签到记录
     * @param cellphone
     * @return
     */
    @Override
    public List<UserSign> queryUserSign(String cellphone) {
        return userSignMapper.queryUserSign(cellphone);
    }

    /**
     * 当月已过完，到下一月,重置签到信息
     * @param
     */
    @Override
    public void resetUserSign() {
        userSignMapper.resetUserSign();
    }

    /**
     * 查询最长签到记录
     * @param cellphone
     * @return
     */
    @Override
    public Integer queryMaxDay(String cellphone) {
        return userSignMapper.queryMaxDay(cellphone);
    }

    /**
     * 查询当日是否签到
     * @param cellphone
     * @return
     */
    @Override
    public UserSign queryToday(String cellphone) {
        return userSignMapper.queryToday(cellphone);
    }

    /**
     * 查询前一天是否有记录
     * @param cellphone
     * @return
     */
    @Override
    public UserSign queryYesteday(String cellphone) {
        return userSignMapper.queryYesteday(cellphone);
    }

    /**
     * 查询签到排名
     * @param cellphone
     * @return Integer
     */
    @Override
    public List<UserSignVo> queryEarly(String cellphone) {
        return userSignMapper.queryEarly(cellphone);
    }
}
