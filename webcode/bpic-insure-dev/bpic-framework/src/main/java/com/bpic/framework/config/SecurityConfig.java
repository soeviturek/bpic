package com.bpic.framework.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutFilter;
import org.springframework.web.filter.CorsFilter;
import com.bpic.framework.security.filter.JwtAuthenticationTokenFilter;
import com.bpic.framework.security.handle.AuthenticationEntryPointImpl;
import com.bpic.framework.security.handle.LogoutSuccessHandlerImpl;

/**
 * spring security配置
 * 
 * @author ruoyi
 */
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter
{
    /**
     * 自定义用户认证逻辑
     */
    @Autowired
    private UserDetailsService userDetailsService;
    
    /**
     * 认证失败处理类
     */
    @Autowired
    private AuthenticationEntryPointImpl unauthorizedHandler;

    /**
     * 退出处理类
     */
    @Autowired
    private LogoutSuccessHandlerImpl logoutSuccessHandler;

    /**
     * token认证过滤器
     */
    @Autowired
    private JwtAuthenticationTokenFilter authenticationTokenFilter;

    /**
     * 跨域过滤器
     */
    @Autowired
    private CorsFilter corsFilter;
    
    /**
     * 解决 无法直接注入 AuthenticationManager
     *
     * @return
     * @throws Exception
     */
    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception
    {
        return super.authenticationManagerBean();
    }

    /**
     * anyRequest          |   匹配所有请求路径
     * access              |   SpringEl表达式结果为true时可以访问
     * anonymous           |   匿名可以访问
     * denyAll             |   用户不能访问
     * fullyAuthenticated  |   用户完全认证可以访问（非remember-me下自动登录）
     * hasAnyAuthority     |   如果有参数，参数表示权限，则其中任何一个权限可以访问
     * hasAnyRole          |   如果有参数，参数表示角色，则其中任何一个角色可以访问
     * hasAuthority        |   如果有参数，参数表示权限，则其权限可以访问
     * hasIpAddress        |   如果有参数，参数表示IP地址，如果用户IP和参数匹配，则可以访问
     * hasRole             |   如果有参数，参数表示角色，则其角色可以访问
     * permitAll           |   用户可以任意访问
     * rememberMe          |   允许通过remember-me登录的用户访问
     * authenticated       |   用户登录后可访问
     */
    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception
    {
        httpSecurity
                // CRSF禁用，因为不使用session
                .csrf().disable()
                // 认证失败处理类
                .exceptionHandling().authenticationEntryPoint(unauthorizedHandler).and()
                // 基于token，所以不需要session
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
                // 过滤请求
                .authorizeRequests()
                // 对于登录login 验证码captchaImage 允许匿名访问
                .antMatchers("/web/lapiogin", "/web/captchaImage").anonymous()
                .antMatchers(
                        HttpMethod.GET,
                        "/*.html",
                        "/**/*.html",
                        "/**/*.css",
                        "/**/*.js"
                ).permitAll()
                .antMatchers("/api/share/refreshClick").permitAll()
                .antMatchers("/web/system/productConfig/selectByNum").anonymous()
                .antMatchers("/web/profile/**").anonymous()
                .antMatchers("/web/common/download**").anonymous()
                .antMatchers("/web/common/download/resource**").anonymous()
                .antMatchers("/swagger-ui.html").anonymous()
                .antMatchers("/swagger-resources/**").anonymous()
                .antMatchers("/v2/api-docs").anonymous()
                .antMatchers("/web/webjars/**").anonymous()
                .antMatchers("/*/api-docs").anonymous()
                .antMatchers("/web/druid/**").anonymous()
                .antMatchers("/api/sendSms/**").anonymous()
//              .antMatchers("/api/product/**").anonymous()
//               .antMatchers("/api/userinfo/**").anonymous()
				.antMatchers("/test/**").anonymous()
                .antMatchers("/api/weChat/jsApiConfig","/api/weChat/createMenu","/api/weChat/delete").anonymous()
                //招募认证移除
                .antMatchers("/personnel/customer/app-personnel/exclusions/checkUrl").anonymous()
                .antMatchers("/web/system/dict/data/type/findByCode").anonymous()
                .antMatchers("/web/system/dict/data/type/findByCodes").anonymous()
                //.antMatchers("/web/system/dict/data/type/findBankByCodes").anonymous()
                .antMatchers("/personnel/customer/app-personnel/**").anonymous()
                .antMatchers("/personnel/manage/personnel/**").anonymous()
                .antMatchers("/attachment/**").anonymous()
                .antMatchers("/web/system/productImage/uploadImage").anonymous()
                //自定义上传图片到服务器
                .antMatchers("/web/system/productImage/customizedUploadImage").anonymous()


                .antMatchers("/api/share/selectClickTotal").anonymous()
                //自定义
                .antMatchers("/api/share/selectTotalAllReplicate").anonymous()
                //自定义2根据电话查询click
                .antMatchers("/api/share/selectClickCount").anonymous()

                //自定义插入
                .antMatchers("/api/share/insertData").anonymous()
                //自定义update
                .antMatchers("/api/share/updateTelphone").anonymous()
                //自定义delete
                .antMatchers("/api/share/deleteData").anonymous()



                .antMatchers("/api/share/selectIsShare").permitAll()
                  // 除上面外的所有请求全部需要鉴权认证
                .anyRequest().authenticated()
                .and()
                .headers().frameOptions().disable();
        httpSecurity.logout().logoutUrl("/*/logout").logoutSuccessHandler(logoutSuccessHandler);
        // 添加JWT filter
        httpSecurity.addFilterBefore(authenticationTokenFilter, UsernamePasswordAuthenticationFilter.class);
        // 添加CORS filter
        httpSecurity.addFilterBefore(corsFilter, JwtAuthenticationTokenFilter.class);
        httpSecurity.addFilterBefore(corsFilter, LogoutFilter.class);
    }

    
    /**
     * 强散列哈希加密实现
     */
    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder()
    {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public MD5PasswordEncoder md5PasswordEncoder()
    {
        return new MD5PasswordEncoder();
    }

    /**
     * 身份认证接口
     */
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception
    {
        auth.userDetailsService(userDetailsService).passwordEncoder(md5PasswordEncoder());
    }
}
