package com.bpic.mobile.user;

import com.alibaba.fastjson.JSONObject;
import com.bpic.common.constant.Constants;
import com.bpic.common.core.domain.AjaxResult;
import com.bpic.common.core.domain.entity.SysLoginUser;
import com.bpic.common.core.domain.entity.TempCdeSales;
import com.bpic.common.core.domain.model.LoginUser;
import com.bpic.common.utils.SecurityUtils;
import com.bpic.common.utils.StringUtils;
import com.bpic.common.utils.http.HttpClient;
import com.bpic.common.utils.sign.Base64;
import com.bpic.common.utils.uuid.IdUtils;
import com.bpic.framework.web.service.TokenService;
import com.bpic.sms.SendSmsUtil;
import com.bpic.system.domain.WechatUser;
import com.bpic.system.domain.vo.SysLoginUserVo;
import com.bpic.system.service.ISysUserService;
import com.bpic.system.service.IWechatService;
import com.bpic.system.service.impl.SysLoginUserServiceImpl;
import com.bpic.weixin.CheckUtil;
import com.bpic.weixin.MD5Util;
import com.bpic.weixin.WeChatUtil;
import com.google.code.kaptcha.Producer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.util.FastByteArrayOutputStream;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.TimeUnit;

/**
 *
 * @data 2020/8/3
 * @author xuyy
 * @function 验证码认证和登录,微信认证授权，手机认证，用户信息查询
 */
@RestController
@RequestMapping("/api/sendSms")
public class MobileUserController {

    private static final Logger logger = LoggerFactory.getLogger(MobileUserController.class);

    @Autowired
    public RedisTemplate<String, String> redisTemplate;

    @Resource(name = "captchaProducer")
    private Producer captchaProducer;

    @Resource
    SysLoginUserServiceImpl sysLoginUserService;

    @Resource
    ISysUserService iSysUserService;

    @Value("${wechat.appId}")
    private String appId;

    @Value("${wechat.secret}")
    private String secret;

    @Value("${wechat.token}")
    private String wxtoken;

    @Value("${wechat.wechatAuthorizationUrl}")
    private String wechatAuthorizationUrl;

    @Value("${wechat.token_refresh}")
    private String refresh_token;

    @Resource(name = "captchaProducerMath")
    private Producer captchaProducerMath;

    @Resource
    IWechatService iWechatService;
    @Resource
    TokenService tokenService;

    @Autowired
    SendSmsUtil sendSmsUtil;


    /**
     * 生成短信验证码接口
     * @param requestBody
     * @return AjaxResult
     */
    @PostMapping("/sendCode")
    public AjaxResult sendSms(@RequestBody HashMap<String, String> requestBody){
        //1.生成6位短信验证码
        String code = MD5Util.randomCode();
        //2.存入redis,以手机号码为key
        String cellphone = requestBody.get("cellphone");
        logger.info("开始请求发送手机验证码接口，入参:{}",cellphone);
        if(StringUtils.isEmpty(cellphone)){
            return new AjaxResult(400,"手机号不能为空");
        }
        try{
            redisTemplate.opsForValue().set(cellphone,code,600,TimeUnit.SECONDS);
        }catch (Exception e){
            logger.info("短信验证码入库失败,{}",e.getMessage());
            return new AjaxResult(500,"验证码发送失败");
        }
        try{
            ArrayList<String> strings = sendSmsUtil.sendSms(cellphone,code);
            logger.info("短信发送结果打印：{}",strings);
            if(strings.toString().contains("001")){
                //4.发送成功
                logger.info("请求手机验证码接口结束，验证码发送成功，code:{}",code);
                return new AjaxResult(200,"发送成功,请留意手机短信");
            }
        }catch (Exception e){
            logger.info("短信验证码发送失败,{}",e.getMessage());
            return new AjaxResult(500,"验证码发送失败");
        }
        return new AjaxResult(400,"验证码发送失败");
    }

    /**
     * 生成四位验证码接口
     * @param
     * @return AjaxResult
     */
    @GetMapping("/sendRandomCode")
    public AjaxResult sendRandomCode(){
        logger.info("开始请求发送手机验证码sendRandomCode接口");
//        String str="ABCDEFGHIJKLMNOPQRSTUVWXYZ";
//        StringBuilder sb = new StringBuilder(4);
//        for(int i = 0;i < 4;i++) {
//            char ch=str.charAt(new Random().nextInt(str.length()));
//            sb.append(ch);
//        }
        String capStr = null, code = null;
        BufferedImage image = null;
        String capText = captchaProducerMath.createText();
        capStr = capText.substring(0, capText.lastIndexOf("@"));
        code = capText.substring(capText.lastIndexOf("@") + 1);
        image = captchaProducerMath.createImage(capStr);
        String uuid = IdUtils.simpleUUID();
        String verifyKey = Constants.CAPTCHA_CODE_KEY + uuid;
        //BufferedImage image = captchaProducer.createImage(sb.toString().toUpperCase());
        //2.存入redis
        redisTemplate.opsForValue().set(verifyKey,code,Constants.CAPTCHA_EXPIRATION,TimeUnit.MINUTES);
        // 转换流信息写出
        FastByteArrayOutputStream os = new FastByteArrayOutputStream();
        try {
            ImageIO.write(image, "jpg", os);
        }
        catch (IOException e) {
            logger.info("验证码写入失败,{}",e.getMessage());
            return new AjaxResult(500,"生成验证码失败");
        }
        logger.info("请求sendRandomCode接口结束");
        AjaxResult ajax = AjaxResult.success();
        ajax.put("uuid", uuid);
        ajax.put("img", Base64.encode(os.toByteArray()));
        return ajax;
    }

    /**
     * 手机密码登录
     * @param requestBody
     * @return AjaxResult
     */
    @PostMapping("/loginByCellphone")
    public AjaxResult loginByCellphone(@RequestBody HashMap<String, String> requestBody){
        //1.参数校验
        String open_id = requestBody.get("open_id");
        String cellphone = requestBody.get("cellphone");
        String password = requestBody.get("password");
        String tokens = requestBody.get("token");   //??
        String randomCode = requestBody.get("randomCode").toUpperCase();
        String verify = requestBody.get("verify");
        String refresh = requestBody.get("refresh");
        logger.info("=====开始请求loginByCellphone接口======");
        logger.info("cellphone:{},password：{}，tokens:{}，randomCode:{},verify:{},refresh:{}------",cellphone,password,tokens,randomCode,verify,refresh);
        if(StringUtils.isEmpty(open_id)){
            return new AjaxResult(400,"open_id不能为空");
        }
        if(StringUtils.isEmpty(cellphone)){
            return new AjaxResult(400,"手机号不能为空");
        }
        if(StringUtils.isEmpty(password)){
            return new AjaxResult(400,"密码不能为空");
        }
        if(StringUtils.isEmpty(randomCode)){
            return new AjaxResult(400,"验证码不能为空");
        }
        try{
            //2.查redis里的code
            String verifyKey = Constants.CAPTCHA_CODE_KEY + verify;
            String redisCode = redisTemplate.opsForValue().get(verifyKey);
            if(StringUtils.isEmpty(redisCode)){
                return new AjaxResult(400,"验证码已失效请重新发送");
            }
            //3.验证码是否一致
            if(randomCode.equals(redisCode)){
                //4.根据手机号和密码验证用户信息，不为空返回      sys_login_user表
                SysLoginUserVo sysLoginUserVo = sysLoginUserService.selectByCellphone(cellphone);
                SysLoginUser sysLoginUser = new SysLoginUser();
                sysLoginUser.setC_tel_mob(cellphone);
                if(sysLoginUserVo != null){//本地表
                    if (SecurityUtils.md5MatchesPassword(password,sysLoginUserVo.getC_passwd())){
                        //查微信信息表
                        WechatUser wechat = new WechatUser();
                        WechatUser wechatUser = iWechatService.selectByOpenId(open_id);
                        if(wechatUser == null){//为空,说明这个人有两个微信号
                            //设置新的入库
                            wechat.setOpen_id(open_id);
                            iWechatService.insertWechatUser(wechat);
                            //为wechatUser重新赋值
                            wechatUser = iWechatService.selectByOpenId(open_id);
                        }
                        WechatUser wechatUserInfo;
                        if(StringUtils.isEmpty(wechatUser.getNick_name()) || StringUtils.isEmpty(wechatUser.getCellphone()) || StringUtils.isEmpty(wechatUser.getHead_img_url())){
                            //其他信息为空，则重新绑定
                            logger.info("-----------------------其他信息为空，则重新绑定---------------------");
                            String binding = sysLoginUserService.wechatBinding(cellphone, tokens, open_id,sysLoginUserVo.getUser_name(),refresh);
                            logger.info("绑定结果bingding:{}",binding);
                            //重新绑定后的微信信息
                            wechatUserInfo = iWechatService.selectByOpenId(open_id);
                            logger.info("-------------wechatUserInfo:{}------------",wechatUserInfo);
                        }else{
                            //如果不为空把wechatuser赋值给wechatUserInfo
                            wechatUserInfo= wechatUser;
                        }
                        Map<String, Object> map  = new HashMap<>();
                        try{
                            LoginUser loginUser = new LoginUser(new TempCdeSales(sysLoginUserVo.getUser_name(),sysLoginUserVo.getC_passwd(),sysLoginUserVo.getC_emp_cde2()), null);
                            String token = tokenService.createToken(loginUser);
                            sysLoginUserVo.setC_passwd(null);
                            map.put("userInfo",sysLoginUserVo);
                            map.put("token",token);
                            map.put("wechatInfo",wechatUserInfo);
                            logger.info("=====请求loginByCellphone接口结束======");
                            return new AjaxResult(200,"登录成功",map);
                        }catch(Exception e){
                            logger.info("登陆失败,{}",e.getMessage());
                            return new AjaxResult(500,"token保存失败");
                        }
                    }
                    return new AjaxResult(400,"密码验证失败,请重试");
                }
                //渤海用户表
                TempCdeSales tempCdeSales = iSysUserService.selectUserByPhone(cellphone);
                if(tempCdeSales == null){
                    return new AjaxResult(400,"用户不存在,请注册");
                }
                if(tempCdeSales.getC_state().equals("1")){
                    return new AjaxResult(400,"用户已被禁用");
                }
                sysLoginUser.setUser_name(tempCdeSales.getC_emp_cnm());
                sysLoginUser.setC_passwd(SecurityUtils.md5Password(password));
                sysLoginUser.setC_tel_mob(cellphone);
                sysLoginUser.setC_title_cde(tempCdeSales.getC_title_cde());
                sysLoginUser.setC_emp_cde2(tempCdeSales.getC_emp_cde());
                logger.info("sysLoginUser:{}",sysLoginUser);
                int i = sysLoginUserService.insertUser(sysLoginUser);
                logger.info("微信信息绑定入库");
                String binding = sysLoginUserService.wechatBinding(cellphone, tokens, open_id,tempCdeSales.getC_emp_cnm(),refresh);
                WechatUser wechatUser1 = iWechatService.selectByOpenId(open_id);
                logger.info("绑定结果bingding:{}",binding);
                logger.info("===============registerByCellphone接口请求结束================");
                if( i > 0 ){
                    LoginUser loginUser = new LoginUser(new TempCdeSales(tempCdeSales.getC_emp_cnm(),tempCdeSales.getC_passwd(),tempCdeSales.getC_emp_cde()),null);
                    String token = tokenService.createToken(loginUser);
                    HashMap<String, Object> map1 = new HashMap<>();
                    map1.put("userInfo",sysLoginUser);
                    map1.put("token",token);
                    map1.put("wechatInfo",wechatUser1);
                    logger.info("=====请求loginByCellphone接口结束======");
                    return new AjaxResult(200,"登录成功！正在跳转至首页……",map1);
                }
                return new AjaxResult(400,"用户信息不存在");
            }
            return new AjaxResult(400,"验证码错误，请重新输入！");
        }catch (Exception e){
            logger.info("登录失败,{}",e.getMessage());
            return new AjaxResult(500,"登陆失败,请重试");
        }
    }


    /**
     * fangxie
     */
    @PostMapping("/loginByCellphoneCopy")
    public AjaxResult loginByCellphoneCopy(@RequestBody HashMap<String, String> requestBody){
        //1.参数校验
        String open_id = requestBody.get("open_id");       //open id
        String cellphone = requestBody.get("cellphone");    //手机
        String password = requestBody.get("password");      //密码
        String tokens = requestBody.get("token");           //token
        String randomCode = requestBody.get("randomCode").toUpperCase();        //验证码
        String verify = requestBody.get("verify");                              //拼接key用的
        String refresh = requestBody.get("refresh");                            //刷新？
        logger.info("=====开始请求loginByCellphone接口======");
        logger.info("cellphone:{},password：{}，tokens:{}，randomCode:{},verify:{},refresh:{}------",cellphone,password,tokens,randomCode,verify,refresh);
        if(StringUtils.isEmpty(open_id)){
            return new AjaxResult(400,"open_id不能为空");
        }
        if(StringUtils.isEmpty(cellphone)){
            return new AjaxResult(400,"手机号不能为空");
        }
        if(StringUtils.isEmpty(password)){
            return new AjaxResult(400,"密码不能为空");
        }
        if(StringUtils.isEmpty(randomCode)){
            return new AjaxResult(400,"验证码不能为空");
        }
        try{
            //2.查redis里的code
            String verifyKey = Constants.CAPTCHA_CODE_KEY + verify;
            //Redis中获取captcha_codes:    每个verifyKey对应一个验证码
            String redisCode = redisTemplate.opsForValue().get(verifyKey);
            if(StringUtils.isEmpty(redisCode)){
                return new AjaxResult(400,"验证码已失效请重新发送");
            }
            //3.验证码是否一致      用户请求时的验证码=redis中的验证码
            if(randomCode.equals(redisCode)){
                //4.根据手机号和密码验证用户信息，不为空返回
                //返回一个封装的vo对象，储存基本信息    从sys_login_user表中，登记了手机号
                SysLoginUserVo sysLoginUserVo = sysLoginUserService.selectByCellphone(cellphone);
                //登陆的用户对象，赋值电话
                SysLoginUser sysLoginUser = new SysLoginUser();
                sysLoginUser.setC_tel_mob(cellphone);
                if(sysLoginUserVo != null){//本地表   如果存在该用户

                   /*
                   如果del_flag=2 提示下线已禁用
                    */
                    if(sysLoginUserVo.getDel_flag().equals("2")){
                        return new AjaxResult(400,"下线已禁用");
                    }

                    if (SecurityUtils.md5MatchesPassword(password,sysLoginUserVo.getC_passwd())){ //验证密码，如果正确
                        //查微信信息表
                        WechatUser wechat = new WechatUser();
                        //wechat_user表，返回WechatUser对象
                        WechatUser wechatUser = iWechatService.selectByOpenId(open_id);
                        if(wechatUser == null){//为空,说明这个人有两个微信号
                            //设置新的入库
                            wechat.setOpen_id(open_id);
                            iWechatService.insertWechatUser(wechat);
                            //为wechatUser重新赋值
                            wechatUser = iWechatService.selectByOpenId(open_id);
                        }
                        //WuchatUser对象
                        WechatUser wechatUserInfo;
                        if(StringUtils.isEmpty(wechatUser.getNick_name()) || StringUtils.isEmpty(wechatUser.getCellphone()) || StringUtils.isEmpty(wechatUser.getHead_img_url())){
                            //其他信息为空，则重新绑定
                            logger.info("-----------------------其他信息为空，则重新绑定---------------------");
                            String binding = sysLoginUserService.wechatBinding(cellphone, tokens, open_id,sysLoginUserVo.getUser_name(),refresh);
                            logger.info("绑定结果bingding:{}",binding);
                            //重新绑定后的微信信息
                            wechatUserInfo = iWechatService.selectByOpenId(open_id);
                            logger.info("-------------wechatUserInfo:{}------------",wechatUserInfo);
                        }else{
                            //如果不为空把wechatuser赋值给wechatUserInfo
                            wechatUserInfo= wechatUser;
                        }
                        Map<String, Object> map  = new HashMap<>();
                        try{
                            LoginUser loginUser = new LoginUser(new TempCdeSales(sysLoginUserVo.getUser_name(),sysLoginUserVo.getC_passwd(),sysLoginUserVo.getC_emp_cde2()), null);
                            String token = tokenService.createToken(loginUser);
                            sysLoginUserVo.setC_passwd(null);
                            map.put("userInfo",sysLoginUserVo);
                            map.put("token",token);
                            map.put("wechatInfo",wechatUserInfo);
                            logger.info("=====请求loginByCellphone接口结束======");
                            return new AjaxResult(200,"登录成功",map);
                        }catch(Exception e){
                            logger.info("登陆失败,{}",e.getMessage());
                            return new AjaxResult(500,"token保存失败");
                        }
                    }
                    return new AjaxResult(400,"密码验证失败,请重试");
                }
                //如果本地表中不存在用户
                //渤海用户表
                //获取用户    t_emp_cde_sales
                TempCdeSales tempCdeSales = iSysUserService.selectUserByPhone(cellphone);
                if(tempCdeSales == null){
                    return new AjaxResult(400,"用户不存在,请注册");
                }
                //c_status 最多的是120113
                if(tempCdeSales.getC_state().equals("1")){
                    return new AjaxResult(400,"用户已被禁用");
                }
                sysLoginUser.setUser_name(tempCdeSales.getC_emp_cnm());
                sysLoginUser.setC_passwd(SecurityUtils.md5Password(password));
                sysLoginUser.setC_tel_mob(cellphone);
                sysLoginUser.setC_title_cde(tempCdeSales.getC_title_cde());
                sysLoginUser.setC_emp_cde2(tempCdeSales.getC_emp_cde());
                logger.info("sysLoginUser:{}",sysLoginUser);
                int i = sysLoginUserService.insertUser(sysLoginUser);
                logger.info("微信信息绑定入库");
                String binding = sysLoginUserService.wechatBinding(cellphone, tokens, open_id,tempCdeSales.getC_emp_cnm(),refresh);
                WechatUser wechatUser1 = iWechatService.selectByOpenId(open_id);
                logger.info("绑定结果bingding:{}",binding);
                logger.info("===============registerByCellphone接口请求结束================");
                if( i > 0 ){
                    LoginUser loginUser = new LoginUser(new TempCdeSales(tempCdeSales.getC_emp_cnm(),tempCdeSales.getC_passwd(),tempCdeSales.getC_emp_cde()),null);
                    String token = tokenService.createToken(loginUser);
                    HashMap<String, Object> map1 = new HashMap<>();
                    map1.put("userInfo",sysLoginUser);
                    map1.put("token",token);
                    map1.put("wechatInfo",wechatUser1);
                    logger.info("=====请求loginByCellphone接口结束======");
                    return new AjaxResult(200,"登录成功！正在跳转至首页……",map1);
                }
                return new AjaxResult(400,"用户信息不存在");
            }
            return new AjaxResult(400,"验证码错误，请重新输入！");
        }catch (Exception e){
            logger.info("登录失败,{}",e.getMessage());
            return new AjaxResult(500,"登陆失败,请重试");
        }
    }

    /**
     * 手机号注册接口
     * @param requestBody
     * @return AjaxResult
     */
    @PostMapping("/registerByCellphone")
    public AjaxResult registerByCellphone(@RequestBody HashMap<String, String> requestBody){
        //1.参数验证
        logger.info("进入手机注册接口");
        String open_id = requestBody.get("open_id");
        String token = requestBody.get("token");
        //上线员工手机号
        String cphone = requestBody.get("cphone");
        //上线员工编号
        String c_emp_cde = requestBody.get("c_emp_cde");
        String cellphone = requestBody.get("cellphone");
        String password = requestBody.get("password");
        String name = requestBody.get("userName");
        String code = requestBody.get("code").toUpperCase();
        String refresh = requestBody.get("refresh");
        logger.info("=====请求registerByCellphone====入参,open_id:{},token:{},cphone:{},c_emp_cde:{},cellphone:{},password:{},code:{},refresh:{}==",open_id,token,cphone,c_emp_cde,cellphone,password,code,refresh);
        if(StringUtils.isEmpty(open_id)){
            return new AjaxResult(400,"open_id不能为空");
        }
        if(StringUtils.isEmpty(token)){
            return new AjaxResult(400,"token不能为空");
        }
        if(StringUtils.isEmpty(password)){
            return new AjaxResult(400,"密码不能为空");
        }
        if(StringUtils.isEmpty(cellphone)){
            return new AjaxResult(400,"手机号不能为空");
        }
        if(StringUtils.isEmpty(code)){
            return new AjaxResult(400,"验证码不能为空");
        }
        if(StringUtils.isEmpty(name)){
            return new AjaxResult(400,"姓名不能为空");
        }
        try{
            //2.查redis里的code
            logger.info("验证码的code:{}",code);
            logger.info("cellphone：{}",cellphone);
            logger.info("开始请求redis内的code");
            String redisCode = redisTemplate.opsForValue().get(cellphone);
            logger.info("redis存储的redisCode:{}",redisCode);
            if(StringUtils.isEmpty(redisCode)){
                return new AjaxResult(400,"验证码已失效请重新发送");
            }
            //3.验证码是否一致
            if(code.equals(redisCode)){
                return sysLoginUserService.registerByCellphone(open_id,token,cphone,c_emp_cde,cellphone,password,name,refresh);
            }
            return new AjaxResult(400,"短信验证码错误，请重新输入！");
        }catch (Exception e){
            logger.info("短信验证码查询失败,{}",e.getMessage());
            return new AjaxResult(500,"注册失败,请重试");
        }
    }

    /**
     * 验证手机号和短信验证码
     * @param requestBody
     * @return
     */
    @PostMapping("/verifyforgetPassword")
    public AjaxResult verifyforgetPassword(@RequestBody HashMap<String, String> requestBody){
        //1.参数校验
        String code = requestBody.get("code").toUpperCase();
        String cellphone = requestBody.get("cellphone");
        if(StringUtils.isEmpty(cellphone)){
            return new AjaxResult(400,"手机号不能为空");
        }
        if(StringUtils.isEmpty(code)){
            return new AjaxResult(400,"验证码不能为空");
        }
        try{
            //2.查redis里的code
            String redisCode = redisTemplate.opsForValue().get(cellphone);
            if(StringUtils.isEmpty(redisCode)){
                return new AjaxResult(400,"验证码已失效请重新发送");
            }
            //3.验证码是否一致
            if(code.equals(redisCode)){
                try{
                    SysLoginUserVo sysLoginUserVo = sysLoginUserService.selectByCellphone(cellphone);
                    if(sysLoginUserVo == null){
                        return new AjaxResult(400,"该手机号未注册");
                    }
                    return new AjaxResult(200,"验证成功");
                }catch (Exception e){
                    logger.info(e.getMessage());
                    return new AjaxResult(500,"验证失败");
                }
            }
            return new AjaxResult(400,"验证码错误，请重新输入！");
        }catch (Exception e){
            logger.info("验证码查询失败,{}",e.getMessage());
            return new AjaxResult(500,"修改密码失败,请重试");
        }
    }


    /**
     * 忘记密码，修改密码
     * @param requestBody
     * @return AjaxResult
     */
    @PostMapping("/forgetPassword")
    public AjaxResult forgetPassword(@RequestBody HashMap<String, String> requestBody){
        //1.参数校验
        String cellphone = requestBody.get("cellphone");
        String newPassword = requestBody.get("newPassword");
        String code = requestBody.get("code").toUpperCase();
        if(StringUtils.isEmpty(cellphone)){
            return new AjaxResult(400,"手机号不能为空");
        }
        if(StringUtils.isEmpty(code)){
            return new AjaxResult(400,"验证码不能为空");
        }
        if(StringUtils.isEmpty(newPassword)){
            return new AjaxResult(400,"新密码不能为空");
        }
        try{
            //2.查redis里的code
            String redisCode = redisTemplate.opsForValue().get(cellphone);
            if(StringUtils.isEmpty(redisCode)){
                return new AjaxResult(400,"验证码已失效请重新发送");
            }
            //3.验证码是否一致
            if(code.equals(redisCode)){
                //4.更新用户密码
                SysLoginUser sysLoginUser = new SysLoginUser();
                sysLoginUser.setC_tel_mob(cellphone);
                sysLoginUser.setC_passwd(SecurityUtils.md5Password(newPassword));
                try{
                    int pwd = sysLoginUserService.updatePwd(sysLoginUser);
                    if(pwd > 0){
                        return new AjaxResult(200,"修改密码成功,请重新登录");
                    }
                    return new AjaxResult(400,"修改密码失败,请重试");
                }catch (Exception e){
                    logger.info("密码修改失败,{}",e.getMessage());
                    return new AjaxResult(500,"修改密码失败,请重试");
                }
            }
            return new AjaxResult(400,"验证码错误，请重新输入！");
        }catch (Exception e){
            logger.info("验证码查询失败,{}",e.getMessage());
            return new AjaxResult(500,"修改密码失败,请重试");
        }
    }

    /**
     * 验证微信连通性，由微信服务器去请求此接口
     */
    @GetMapping("/wx")
    public void wxverify(HttpServletRequest request, HttpServletResponse response){
        logger.info("==========================微信开发者权限校验接口开始==========start============");
        String signature = request.getParameter("signature");
        String timestamp = request.getParameter("timestamp");
        String nonce = request.getParameter("nonce");
        String echostr = request.getParameter("echostr");
        logger.info("=====请求参数signature:{},timestamp:{},nonce:{},echostr:{}", signature, timestamp, nonce, echostr);
        PrintWriter out = null;
        try {
            out = response.getWriter();
        } catch (IOException e) {
            logger.info(e.getMessage());
        }
        if (CheckUtil.checkSignature(wxtoken,signature, timestamp, nonce)) {
            logger.info("==========================微信开发者权限校验通过==============pass========");
            assert out != null;
            out.print(echostr);// 校验通过，原样返回echostr参数内容
            logger.info("==========================微信开发者权限校验接口结束============end==========");
        }
    }



    /**
     * 微信授权 获取token和openid
     */
    @PostMapping(value = "/wechatAuthorization")
    public AjaxResult wechatAuthorization(@RequestBody HashMap<String, String>  requestBody) {
        String code = requestBody.get("code");
        if(StringUtils.isEmpty(code)){
            return new AjaxResult(400,"临时code不能为空");
        }
        JSONObject jsonObject = WeChatUtil.getWxUserOpenid(wechatAuthorizationUrl,code, appId, secret);
        logger.info("=============获取openid和token结果：{}================",jsonObject.toJSONString());
        if(jsonObject != null && !jsonObject.containsKey("openid")) {
            return new AjaxResult(400,"请求微信平台失败");
        }
        //从返回的JSON数据中取出access_token和openid，拉取用户信息时用
        String openid = jsonObject.getString("openid");
        try{
            WechatUser wechatUser = iWechatService.selectByOpenId(openid);
            if(wechatUser != null){
                return new AjaxResult(200,"微信授权成功",jsonObject);
            }
            WechatUser wechatUser1 = new WechatUser();
            wechatUser1.setOpen_id(openid);
            iWechatService.insertWechatUser(wechatUser1);
            return new AjaxResult(200,"微信授权成功",jsonObject);
        }catch (Exception e){
            logger.info("用户微信授权登录平台失败,{}",e.getMessage());
            return new AjaxResult(500,"用户微信授权登录平台错误");
        }
    }
}
