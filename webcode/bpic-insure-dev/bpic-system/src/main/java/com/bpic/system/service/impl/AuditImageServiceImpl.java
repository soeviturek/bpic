package com.bpic.system.service.impl;

import com.bpic.system.domain.AuditImage;
import com.bpic.system.domain.vo.AuditImageVo;
import com.bpic.system.mapper.AuditImageMapper;
import com.bpic.system.service.AuditImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class AuditImageServiceImpl implements AuditImageService {

    @Autowired
    AuditImageMapper auditImageMapper;

    @Override
    public int insertAuditImage(AuditImage auditImage) {
        return auditImageMapper.insertAuditImage(auditImage);
    }

    @Override
    public int updateAuditImage(AuditImage auditImage) {
        return auditImageMapper.updateAuditImage(auditImage);
    }

    @Override
    public AuditImageVo selectAuditImage(String cellphone) {
        return auditImageMapper.selectAuditImage(cellphone);
    }

    @Override
    public int deleteAuditImage(String cellphone) {
        return auditImageMapper.deleteAuditImage(cellphone);
    }
}
