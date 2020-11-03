package com.bpic.common.core.domain.entity;

import lombok.Data;

import java.util.Date;

@Data
public class ShareInformation {
    /**
     * 手机号
     */
    private String telPhone;
    /**
     * 分享链接
     */
    private String shareUrl;
    /**
     * 分享名称（取产品名称）
     */
    private String shareName;
    /**
     * 分享时间
     */
    private Date shareTime;
    /**
     * 点击数
     */
    private int click;
    /**
     * 分享成交量
     */
    private int volume;
    /**
     * 产品代码
     */
    private String productCode;
    /**
     * 图片url
     */
    private String imgUrl;
    /**
     * 分享人工号
     */
    private String sharerEmpno;
    /**
     * 分享人姓名
     */
    private String sharerName;
    /**
     * 分享人上级手机号
     */
    private String sharerSuperiorPhone;
    /**
     * 分享人上级姓名
     */
    private String sharerSuperiorName;
    /**
     * 分享人上级工号
     */
    private String sharerSuperiorEmpno;
    /**
     * 分享ID
     */
    private String shareProductId;

	/**
	 *分享状态
	 */
	private String status;

    /**
     * 是否分享
     */
    private String type;

    /**
     * 分享描述信息
     */
    private String describe;

    /**
     * 分享标题
     */
    private String title;

    /**
     * 成交金额
     */
    private String money;

    /**
     * 是否允许分享出单
     */
    private String isShare;
}
