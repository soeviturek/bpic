package com.bpic.mobile.channel;

import com.bpic.common.core.domain.AjaxResult;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/channel")
public class UserChannelController {

    @Value("${channelCode}")
    private String channelCode;

    @GetMapping("/channelCode")
    public AjaxResult queryChannel(){
        String[] split = channelCode.split("=");
        return new AjaxResult(200,"操作成功",split[1]);
    }
}
