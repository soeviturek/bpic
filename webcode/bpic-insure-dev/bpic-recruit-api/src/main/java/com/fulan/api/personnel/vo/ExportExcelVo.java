package com.fulan.api.personnel.vo;


import com.bpic.system.domain.Attachment;
import lombok.Data;

import java.util.List;

@Data
public class ExportExcelVo {

    private Attachment attachment;

    private List<Attachment> imgList;

    private List<Attachment> contractList;
}
