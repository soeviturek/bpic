package com.bpic.common.core.domain.entity;

import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class ProductConfig {
    private static final long serialVersionUID = 1L;
    /**
     *主键id
     */
    private int	id;

    /**
     *配置权限代码
     */
    private Long  configCode;
    /**
     *权限配置名称
     */
    private String	configName;
    /**
     *生效时间
     */
    private String	authStartTime;
    /**
     *失效时间
     */
    private String	authEndTime;
    /**
     *备注
     */
    private String	message;

    /**
     *配置人
     */
    private String	creater;
    /**
     *配置时间
     */
    private Date createTime;
    /**
     *状态
     */
    private int	status;

}
