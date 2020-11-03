package com.bpic.mobile.product;


import lombok.Data;

import java.util.List;

/**
 * 渤海销管系统-产品清单返回结果对象
 * by zyy
 * */

@Data
public class ChannelServerData {

    private List<ChannelServerGroupType> groupsType;//产品分类列表

    private List<ChannelServerProduct> groups;//产品详情列表

}
