package com.bpic.system.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.bpic.common.core.domain.AjaxResult;
import com.bpic.common.core.domain.entity.ShareInformation;
import com.bpic.common.core.domain.entity.SysLoginUser;
import com.bpic.common.core.domain.entity.TdepartmentSales;
import com.bpic.common.core.domain.entity.UserHistory;
import com.bpic.common.utils.DateUtils;
import com.bpic.common.utils.SecurityUtils;
import com.bpic.common.utils.StringUtils;
import com.bpic.mobile.service.ShareInformationService;
import com.bpic.common.utils.http.HttpClient;
import com.bpic.mobile.weixin.WeChatUtil;
import com.bpic.system.domain.AuditImage;
import com.bpic.system.domain.WechatUser;
import com.bpic.system.domain.vo.AuditImageVo;
import com.bpic.system.domain.vo.SysLoginUserVo;
import com.bpic.system.domain.vo.TempCdeSalesVo;
import com.bpic.system.mapper.SysLoginUserMapper;
import com.bpic.system.service.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

@Service
@Transactional
public class SysLoginUserServiceImpl implements SysLoginUserService {
    private static final Logger logger = LoggerFactory.getLogger(SysLoginUserServiceImpl.class);
    @Resource
    SysLoginUserMapper sysLoginUserMapper;
    @Resource
    IWechatService iWechatService;
    @Resource
    AuditImageService auditImageService;

    @Value("${wechat.wechatBingingUrl}")
    private String wechatBingingUrl;

    @Value("${wechat.appId}")
    private String appId;

    @Value("${wechat.token_refresh}")
    private String refresh_token;

    @Resource
    ISysUserService iSysUserService;

    @Autowired
    private ShareInformationService shareInformationService;

    @Autowired
    private IUserHistoryService userHistoryService;

    @Override
    public int insertUser(SysLoginUser sysLoginUser) {
        return sysLoginUserMapper.insertUser(sysLoginUser);
    }

    @Override
    public int updateUserByCellphone(SysLoginUser sysLoginUser) {
        return sysLoginUserMapper.updateUserByCellphone(sysLoginUser);
    }

    @Override
    public SysLoginUserVo selectByCellphone(String cellphone) {
        return sysLoginUserMapper.selectByCellphone(cellphone);
    }

    @Override
    public int updatePwd(SysLoginUser sysLoginUser) {
        return sysLoginUserMapper.updatePwd(sysLoginUser);
    }

    @Override
    public String queryUnionId(SysLoginUser sysLoginUser) {
        return sysLoginUserMapper.queryUnionId(sysLoginUser);
    }

    @Override
    public int deleteUserByCellphone(String cellphone) {
        return sysLoginUserMapper.deleteUserByCellphone(cellphone);
    }

    @Override
    public List<SysLoginUser> selectSxUser(String cellphone, String user_name, String start_time, String end_time) {
        return sysLoginUserMapper.selectSxUser(cellphone,user_name,start_time,end_time);
    }

    @Override
    public Integer selectCount(String cellphone) {
        return sysLoginUserMapper.selectCount(cellphone);
    }

    @Override
    public List<SysLoginUserVo> queryrelationList(String cellphone, String user_name) {
        return sysLoginUserMapper.queryrelationList(cellphone,user_name);
    }

    @Override
    public int updateOnlineUser(SysLoginUserVo sysLoginUserVo) {
        return sysLoginUserMapper.updateOnlineUser(sysLoginUserVo);
    }

    public AjaxResult registerByCellphone(String open_id,String token,String cphone,String c_emp_cde,
                                   String cellphone,String password,String name,String refresh){
        SysLoginUserVo loginUserVo = selectByCellphone(cellphone);
        if(loginUserVo != null ){
            return new AjaxResult(400,"该手机号已被注册");
        }
       SysLoginUser sysLoginUser = new SysLoginUser();
       sysLoginUser.setCphone(cphone);
       sysLoginUser.setC_emp_cde(c_emp_cde);
       sysLoginUser.setC_tel_mob(cellphone);
       sysLoginUser.setC_passwd(SecurityUtils.md5Password(password));
       sysLoginUser.setUser_name(name);
        //根据上线员工编号查询，c_emp_cnm(上线员工姓名),c_dpt_cde(上线员工所属部门)
       TempCdeSalesVo tempCdeSalesVo = iSysUserService.selectUserById(Long.valueOf(c_emp_cde));
        if (tempCdeSalesVo != null){
            sysLoginUser.setC_dpt_cde(tempCdeSalesVo.getC_dpt_cde());
            sysLoginUser.setC_emp_cnm(tempCdeSalesVo.getC_emp_cnm());
        }
       //注册的用户信息入库
        int register = insertUser(sysLoginUser);
        //查询微信用户信息,手机号绑定入库
        logger.info("微信信息绑定入库");
        String binding = wechatBinding(cellphone, token, open_id,name,refresh);
        logger.info("绑定结果bingding:{}",binding);
        logger.info("===============registerByCellphone接口请求结束================");
        if(register > 0){
            if("200".equals(binding)){
                return new AjaxResult(200,"注册成功！正在跳转至登录页……");
            }
        }
        deleteUserByCellphone(cellphone);
        return new AjaxResult(400,"注册失败,请重试","查询微信用户信息失败");
    }

    public AjaxResult updateUserInfo(String old, String anew, String open_id) {
        //查出旧的用户信息
        SysLoginUserVo sysLoginUserVo = selectByCellphone(old);
        if(sysLoginUserVo == null){
            return new AjaxResult(200,"查询不到个人信息");
        }
        //存入新的个人信息
        SysLoginUser sysLoginUser = new SysLoginUser();
        sysLoginUser.setC_emp_cde2(sysLoginUserVo.getC_emp_cde2());
        sysLoginUser.setC_emp_cde(sysLoginUserVo.getC_emp_cde());
        sysLoginUser.setC_emp_cnm(sysLoginUserVo.getC_emp_cnm());
        sysLoginUser.setUser_name(sysLoginUserVo.getUser_name());
        sysLoginUser.setC_dpt_cde(sysLoginUserVo.getC_dpt_cde());
        sysLoginUser.setC_title_cde(sysLoginUserVo.getC_title_cde());
        sysLoginUser.setCphone(sysLoginUserVo.getCphone());
        sysLoginUser.setSex(sysLoginUserVo.getSex());
        sysLoginUser.setC_tel_mob(anew);
        sysLoginUser.setC_passwd(sysLoginUserVo.getC_passwd());
        sysLoginUser.setStatus(sysLoginUserVo.getStatus());
        sysLoginUser.setImg(sysLoginUserVo.getImg());
        //删除旧的个人信息
        int deleteUserByCellphone = deleteUserByCellphone(old);
        //插入
        int insertUser = insertUser(sysLoginUser);

        //更新微信表的手机号
        WechatUser user = iWechatService.selectByOpenId(open_id);
        logger.info("微信信息：{}",user);
        if(user != null){//有值才更新
            user.setCellphone(anew);
            user.setOpen_id(open_id);
            iWechatService.updateByOpenId(user);
        }
        //更新审核表
        AuditImage image = new AuditImage();
        //查出原有记录
        AuditImageVo auditImageVo = auditImageService.selectAuditImage(old);
        //删除原有记录
        if(auditImageVo != null){//有值才更新
            auditImageService.deleteAuditImage(old);
            image.setC_tel_mob(anew);
            image.setFront_path(auditImageVo.getFront_path());
            image.setBack_path(auditImageVo.getBack_path());
            image.setAudit_status(auditImageVo.getAudit_status());
            image.setAdvice(auditImageVo.getAdvice());
            //保存新值
            auditImageService.insertAuditImage(image);
        }
        //如果是渤海员工修改手机号
        //对应的下线的上线手机号也要更改
        List<SysLoginUser> sysLoginUsers = selectSxUser(old, null, null, null);
        if(!sysLoginUsers.isEmpty()){
            for(SysLoginUser loginUser: sysLoginUsers){
                //上线员工换成新手机号
                loginUser.setCphone(anew);
                loginUser.setDevelop_time(null);
                updateUserByCellphone(loginUser);
            }
        }
        //同步分享成功的产品
        List<ShareInformation> shareInformations = shareInformationService.selectByTel(old, 1);
        if(!shareInformations.isEmpty()){
            for (ShareInformation shareInformation : shareInformations) {
                logger.info("====================同步修改的分享信息：[{}]====================",shareInformation);
                shareInformation.setTelPhone(anew);
                shareInformationService.insertShareInformation(shareInformation);
            }
        }
        //同步历史搜索
        UserHistory userHistory = userHistoryService.selectByTel(old);
        if(userHistory!=null){
            logger.info("==================同步修改的搜索历史：{}==================",userHistory);
            userHistory.setTelphone(anew);
            userHistoryService.insertNew(userHistory);
            userHistoryService.deleteByTel(old);
        }
        logger.info("===========请求updateCellphone结束================");
        if(insertUser > 0){
            if(deleteUserByCellphone > 0){
                return new AjaxResult(200,"个人信息更新成功");
            }
        }
        return new AjaxResult(400,"个人信息更新失败");
    }


    /**
     * 获取用户信息进行微信绑定入库
     */
    public String wechatBinding(String cellphone,String token,String id,String name,String refresh) {
        try{
            JSONObject jsonObject = WeChatUtil.getWxUserInfo(wechatBingingUrl,id,token);
            logger.info("================{}=================",jsonObject.toJSONString());
            if(jsonObject != null && jsonObject.containsKey("openid")){
                String open_id = jsonObject.getString("openid");
                logger.info("===================查询库里有没有=======================");
                WechatUser wechatUser1 = iWechatService.selectByOpenId(open_id);
                if(wechatUser1 != null){
                    logger.info("=======================昵称是不是空:{}====================",wechatUser1);
                    if (!StringUtils.isEmpty(wechatUser1.getNick_name())){
                        logger.info("微信信息已经有了,{}",wechatUser1);
                        return "200";
                    }
                }
                String sex = jsonObject.getString("sex");
                String province = jsonObject.getString("province");
                String city = jsonObject.getString("city");
                String country = jsonObject.getString("country");
                String head_img_url = jsonObject.getString("headimgurl");
                String privilege = jsonObject.getString("privilege");
                String unionId = jsonObject.getString("unionid");
                WechatUser wechatUser = new WechatUser();
                wechatUser.setOpen_id(open_id);
                wechatUser.setCellphone(cellphone);
                wechatUser.setNick_name(name);
                wechatUser.setSex(sex);
                wechatUser.setProvince(province);
                wechatUser.setCity(city);
                wechatUser.setCountry(country);
                wechatUser.setHead_img_url(head_img_url);
                wechatUser.setPrivilege(privilege);
                wechatUser.setUnion_id(unionId);
                logger.info("微信用户信息:{}",wechatUser);
                int i = iWechatService.updateByOpenId(wechatUser);
                if( i > 0 ){
                    return "200";
                }
                return "400";
            }else {
                logger.info("=====token过期，刷新token开始=====");
                String refreshtoken = refreshtoken(refresh);
                logger.info("------------刷新结果------------{}",refreshtoken);
                if(StringUtils.isEmpty(refreshtoken)){
                    return "400";
                }
                JSONObject json = WeChatUtil.getWxUserInfo(wechatBingingUrl,id,refreshtoken);
                logger.info("================{}=================",json.toJSONString());
                if(json != null && json.containsKey("openid")){
                    String open_id = json.getString("openid");
                    logger.info("===================查询库里有没有=======================");
                    WechatUser wechatUser1 = iWechatService.selectByOpenId(open_id);
                    if(wechatUser1 != null){
                        logger.info("=======================昵称是不是空:{}====================",wechatUser1);
                        if (!StringUtils.isEmpty(wechatUser1.getNick_name())){
                            logger.info("微信信息已经有了,{}",wechatUser1);
                            return "200";
                        }
                    }
                    String sex = json.getString("sex");
                    String province = json.getString("province");
                    String city = json.getString("city");
                    String country = json.getString("country");
                    String head_img_url = json.getString("headimgurl");
                    String privilege = json.getString("privilege");
                    String unionId = json.getString("unionid");
                    WechatUser wechatUser = new WechatUser();
                    wechatUser.setOpen_id(open_id);
                    wechatUser.setCellphone(cellphone);
                    wechatUser.setNick_name(name);
                    wechatUser.setSex(sex);
                    wechatUser.setProvince(province);
                    wechatUser.setCity(city);
                    wechatUser.setCountry(country);
                    wechatUser.setHead_img_url(head_img_url);
                    wechatUser.setPrivilege(privilege);
                    wechatUser.setUnion_id(unionId);
                    logger.info("微信用户信息:{}",wechatUser);
                    int i = iWechatService.updateByOpenId(wechatUser);
                    if( i > 0 ){
                        return "200";
                    }
                    return "400";
                }
            }
            return "400";
        }catch (Exception e){
            logger.info("绑定微信用户错误,{}",e.getMessage());
            return "400";
        }
    }

    /**
     * token过期，刷新token
     */
    public String refreshtoken(String refreshToken){
        String refreshUrl = refresh_token + "appid="+appId+"&grant_type=refresh_token"+"&refresh_token="+refreshToken;
        String responseContent =  HttpClient.sendGet(refreshUrl);
        if (responseContent.contains("access_token")){
            String result = JSON.parseObject(responseContent).getString("access_token");
            logger.info("===========refreshtoken结果：{}==========",result);
            return result;
        } else {
            return null;
        }
    }
}
