package com.fulan.api.personnel.vo;


import lombok.Data;

import java.util.List;

@Data
public class ExportExcelSeacherVo {

    private Long id;//人员id

    private List<String> list;//证件查询码

    private List<String> list1;//合同查询码

    private String identityType;//证件类型编码


}
