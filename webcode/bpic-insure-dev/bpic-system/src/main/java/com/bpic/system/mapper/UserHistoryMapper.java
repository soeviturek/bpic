package com.bpic.system.mapper;

import com.bpic.common.core.domain.entity.UserHistory;
import org.springframework.stereotype.Repository;

@Repository
public interface UserHistoryMapper {
    UserHistory selectByPhone(String telPhone);

    void update(UserHistory userHistory);

    void insert(UserHistory userHistory);

    void deleteByTel(String old);
}
