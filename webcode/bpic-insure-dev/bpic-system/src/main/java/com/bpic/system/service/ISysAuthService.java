package com.bpic.system.service;

import com.bpic.common.core.domain.AjaxResult;
import com.bpic.system.domain.AuditImage;
import com.bpic.system.domain.vo.AuthenticationVo;

import java.util.List;

public interface ISysAuthService {
    List<AuthenticationVo> select(AuthenticationVo authenticationVo);

    void audit(AuthenticationVo authenticationVo);

    AuditImage check(String telPhone);
}
