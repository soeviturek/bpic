package com.bpic.system.mapper;

import com.bpic.common.core.domain.entity.News;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SysNewsMapper {
    List<News> selectNews();

    void updateBanner(News news);

    void updateNews(News news);
}
