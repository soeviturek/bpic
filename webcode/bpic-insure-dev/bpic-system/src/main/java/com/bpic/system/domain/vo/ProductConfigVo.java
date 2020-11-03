package com.bpic.system.domain.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.List;

public class ProductConfigVo {
    /**
     *权限配置名称
     */
    private String	configName;

    public String getConfigName() {
        return configName;
    }

    public void setConfigName(String configName) {
        this.configName = configName;
    }

    public String getCreater() {
        return creater;
    }

    public void setCreater(String creater) {
        this.creater = creater;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getAuthStartTime() {
        return authStartTime;
    }

    public void setAuthStartTime(String authStartTime) {
        this.authStartTime = authStartTime;
    }

    public String getAuthEndTime() {
        return authEndTime;
    }

    public void setAuthEndTime(String authEndTime) {
        this.authEndTime = authEndTime;
    }

    public String getDeptId() {
        return deptId;
    }

    public void setDeptId(String deptId) {
        this.deptId = deptId;
    }

    /**
     * list机构编码集合
     */

    private List<String> list;

    public List<String> getList() {
        return list;
    }

    public void setList(List<String> list) {
        this.list = list;
    }

    /**
     *配置人
     */
    private String	creater;

    /**
     *状态
     */
    private String	status;

    /**
     *生效时间
     */
    private String authStartTime;
    /**
     *失效时间
     */
    private String	authEndTime;

    /**
     * 所属机构
     */
    private String deptId;
}
