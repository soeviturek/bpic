package com.bpic.system.mapper;

import com.bpic.system.domain.AuditImage;
import com.bpic.system.domain.vo.AuthenticationVo;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SysAuthMapper {
    List<AuthenticationVo> select(AuthenticationVo authenticationVo);

    void audit(AuthenticationVo authenticationVo);

    AuditImage check(String telPhone);
}
