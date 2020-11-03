package com.bpic.system.service.impl;

import com.bpic.common.config.BpicConfig;
import com.bpic.common.core.domain.AjaxResult;
import com.bpic.common.utils.file.FileUploadUtils;
import com.bpic.mobile.weixin.BASE64DecodedMultipartFile;
import com.bpic.system.domain.AdviceFeedback;
import com.bpic.system.mapper.AdviceFeedbackMapper;
import com.bpic.system.service.AdviceFeedbackService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Service
@Slf4j
public class AdviceFeedbackServiceImpl implements AdviceFeedbackService {

    @Value("${bpic.imageUrl}")
    private String imageUrl;

    @Resource
    AdviceFeedbackMapper adviceFeedbackMapper;

    @Override
    public void save(AdviceFeedback adviceFeedback) {
        String filePath = BpicConfig.getUploadPath();
        List<String> picture = adviceFeedback.getPicture();
        List<String> list = new ArrayList<>();
        for (String s : picture) {
            try {
                //正面， 上传后返回的文件名
                String fileNames = FileUploadUtils.upload(filePath, BASE64DecodedMultipartFile.base64ToMultipart(s));
                String url = imageUrl + fileNames;
                list.add(url);
            }
            catch (Exception e) {
                log.error("上传失败：{}",e.getMessage());
            }
        }
        adviceFeedback.setPicture(list);
        adviceFeedbackMapper.save(adviceFeedback);
    }

    @Override
    public AdviceFeedback queryOne(String cellphone) {
        return adviceFeedbackMapper.queryOne(cellphone);
    }

    @Override
    public List<AdviceFeedback> queryList(String advice_type) {
        return adviceFeedbackMapper.queryList(advice_type);
    }
}
