package com.bpic.system.service;

import com.bpic.common.core.domain.entity.News;

import java.util.List;

public interface ISysNewsService {
    List<News> selectNews();

    void updateBanner(News news);

    void updateNews(News news);
}
