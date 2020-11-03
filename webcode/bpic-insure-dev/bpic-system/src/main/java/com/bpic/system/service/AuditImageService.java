package com.bpic.system.service;

import com.bpic.system.domain.AuditImage;
import com.bpic.system.domain.vo.AuditImageVo;

public interface AuditImageService {

    int insertAuditImage(AuditImage auditImage);

    int updateAuditImage(AuditImage auditImage);

    AuditImageVo selectAuditImage(String cellphone);

    int deleteAuditImage(String cellphone);
}
