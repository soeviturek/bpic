package com.bpic.system.service;

import com.bpic.common.core.domain.entity.UserHistory;

public interface IUserHistoryService {
    String insertHistory(String telPhone,String msg);

    UserHistory selectByTel(String telPhone);

    void insertNew(UserHistory userHistory);

    void deleteByTel(String old);
}
