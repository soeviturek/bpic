package com.bpic.system.service.impl;

import com.bpic.common.core.domain.entity.News;
import com.bpic.system.mapper.SysNewsMapper;
import com.bpic.system.service.ISysNewsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SysNewsServiceImpl implements ISysNewsService {

    @Autowired
    private SysNewsMapper sysNewsMapper;

    @Override
    public List<News> selectNews() {
        return sysNewsMapper.selectNews();
    }

    @Override
    public void updateBanner(News news) {
        sysNewsMapper.updateBanner(news);
    }

    @Override
    public void updateNews(News news) {
        sysNewsMapper.updateNews(news);
    }
}
