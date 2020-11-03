package com.bpic.mobile.user;

import com.bpic.common.core.domain.AjaxResult;
import com.bpic.common.utils.SecurityUtils;
import com.bpic.common.utils.StringUtils;
import com.bpic.system.domain.UserSign;
import com.bpic.system.domain.vo.UserSignVo;
import com.bpic.system.service.UserSignService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;


/**
 *
 * @data 2020/9/4
 * @author xuyy
 * @function 用户签到
 */
@RestController
@RequestMapping("/api/usersign")
public class MobileUserSignController {

    private static final Logger logger = LoggerFactory.getLogger(MobileUserSignController.class);

    @Resource
    UserSignService userSignService;

    @PostMapping("/save")
    public AjaxResult save(@RequestBody HashMap<String, Object> requestBody){
        String cellphone = (String)requestBody.get("cellphone");
        logger.info("----------{},开始签到----------",cellphone);
        if(StringUtils.isEmpty(cellphone)){
            return new AjaxResult(400,"签到失败,参数不能为空");
        }
        UserSign userSign1 = userSignService.queryToday(cellphone);
        if(userSign1 != null){
            return new AjaxResult(200,"操作成功","当日已签到,不要重复签到");
        }
        UserSign userSign = new UserSign();
        userSign.setUser_mob(cellphone);
        userSignService.save(userSign);
        logger.info("----------{},签到完成----------",cellphone);
        return new AjaxResult(200,"操作成功","签到成功");
    }


    @GetMapping("/query")
    public AjaxResult query(@RequestParam("cellphone") String cellphone){
        if(StringUtils.isEmpty(cellphone)){
            return new AjaxResult(400,"签到失败,参数不能为空");
        }
        logger.info("----------{},查询签到记录----------",cellphone);
        //查询用户签到记录
        List<UserSign> userSigns = userSignService.queryUserSign(cellphone);
        //获取连续签到记录
        Integer integer = userSignService.queryMaxDay(cellphone);
        HashMap<String, Object> map = new HashMap<>();
        if(!userSigns.isEmpty()){
            map.put("total",userSigns.size());
            map.put("info",userSigns);
            map.put("max",integer);
            logger.info("----------{},查询签到记录结束----------",cellphone);
            return new AjaxResult(200,"操作成功",map);
        }
        logger.info("----------{},查询签到记录结束----------",cellphone);
        return new AjaxResult(200,"操作成功",map);
    }

    /**
     * 获取用户签到排名
     * @return
     */
    @GetMapping("/queryEarly")
    public AjaxResult queryEarly(@RequestParam("cellphone")String cellphone){
        List<UserSignVo> list= userSignService.queryEarly(cellphone);
        //过滤
        if(!list.isEmpty()){
            List<UserSignVo> collect = list.stream()
                    //取用户的手机号
                    .filter(userSignVo -> userSignVo.getUser_mob().equals(cellphone))
                    .collect(Collectors.toList());
            return new AjaxResult(200,"操作成功",collect);
        }
        return new AjaxResult(200,"操作成功",null);
    }

    /**
     * 过完当月，清除当月签到记录
     */
    @Scheduled(cron = "59 59 23 28-31 * ? ")
    public void reset(){
        logger.info("----------清除签到记录----------");
        final Calendar c = Calendar.getInstance();
        if (c.get(Calendar.DATE) == c.getActualMaximum(Calendar.DATE)) {
            //是最后一天
            userSignService.resetUserSign();
        }else{
            logger.info("----------，不是月底，不清除签到记录----------");
        }
    }
}
