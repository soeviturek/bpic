package com.bpic.system.mapper;

import com.bpic.system.domain.AuditImage;
import com.bpic.system.domain.vo.AuditImageVo;

public interface AuditImageMapper {
    /**
     * 插入证件照
     * @param auditImage
     * @return
     */
    int insertAuditImage(AuditImage auditImage);

    /**
     * 更新证件照
     * @param auditImage
     * @return
     */
    int updateAuditImage(AuditImage auditImage);

    AuditImageVo selectAuditImage(String cellphone);

    int deleteAuditImage(String cellphone);
}
