package com.bpic.system.service.impl;

import com.bpic.common.core.domain.entity.TdepartmentSales;
import com.bpic.system.domain.AuditImage;
import com.bpic.system.domain.vo.AuthenticationVo;
import com.bpic.system.mapper.SysAuthMapper;
import com.bpic.system.service.ISysAuthService;
import com.bpic.system.service.ISysDeptService;
import com.bpic.system.service.ISysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class SysAuthServiceImpl implements ISysAuthService {

    @Autowired
    private SysAuthMapper sysAuthMapper;

    @Autowired
    private ISysDeptService sysDeptService;


    @Override
    public List<AuthenticationVo> select(AuthenticationVo authenticationVo) {
        return sysAuthMapper.select(authenticationVo);
    }

    @Override
    public void audit(AuthenticationVo authenticationVo) {
        if(authenticationVo.getFlage().equals("1")){
            authenticationVo.setStatus("2");
        }else {
            authenticationVo.setStatus("3");
        }
        sysAuthMapper.audit(authenticationVo);
    }

    @Override
    public AuditImage check(String telPhone) {
        return sysAuthMapper.check(telPhone);
    }
}
