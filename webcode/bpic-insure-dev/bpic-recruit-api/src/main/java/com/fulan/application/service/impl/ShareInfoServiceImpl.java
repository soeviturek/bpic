package com.fulan.application.service.impl;

import com.bpic.common.core.domain.model.LoginUser;
import com.bpic.common.utils.id.IdGenerator;
import com.bpic.common.utils.reponse.Response;
import com.bpic.framework.web.service.TokenService;
import com.fulan.api.personnel.domain.ShareInfo;
import com.fulan.application.mapper.ShareInfoMapper;

import com.fulan.application.service.ShareInfoService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;


@Service("ShareInfoServiceImpl")
public class ShareInfoServiceImpl implements ShareInfoService {

    @Autowired
    private ShareInfoMapper shareInfoMapper;

    @Autowired
    private IdGenerator idGenerator;

//    @Autowired
//    private RedisUtil redisUtil;

    @Autowired
    private TokenService tokenService;

    /**
     *  保存邀请的信息
     * @param shareInfoVo
     * @return 邀请入库的id
     */
    @Override
    public Response<ShareInfo> addShare(ShareInfo shareInfoVo) {
        LoginUser account = tokenService.getLoginUser();
        Response<ShareInfo> response = new Response<>(Response.SUCCESS,Response.SUCCESS_MESSAGE);
        Long shareInfoId = idGenerator.generate();
        shareInfoVo.setCreateTime(new Date());
        shareInfoVo.setUpdateTime(new Date());
        shareInfoVo.setId(shareInfoId);
        //拼接url保存shareInfoId
        shareInfoVo.setShareUrl(shareInfoVo.getShareUrl() + "&shareInfoId=" + shareInfoId);
        shareInfoMapper.insert(shareInfoVo);
        response.setData(shareInfoVo);
        return response;
    }

    @Override
    public int checkUrl(long shareInfoId) {
        ShareInfo shareInfo = shareInfoMapper.selectById(shareInfoId);
        if(shareInfo != null){
            long time = new Date().getTime()-shareInfo.getCreateTime().getTime();
            if(time > (60*24*60*60*1000L)){
                return 1;
            }else{
                return 0;
            }
        }else{
            return 1;
        }

    }
}
