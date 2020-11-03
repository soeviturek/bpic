package com.fulan.application.service;

import com.bpic.common.utils.reponse.Response;
import com.fulan.api.personnel.domain.ShareInfo;


public interface ShareInfoService {
    public Response<ShareInfo> addShare(ShareInfo shareInfoVo);

    int checkUrl(long shareInfoId);
}
