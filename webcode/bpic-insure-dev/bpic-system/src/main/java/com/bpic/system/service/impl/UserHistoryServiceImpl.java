package com.bpic.system.service.impl;

import com.bpic.common.core.domain.entity.UserHistory;
import com.bpic.common.utils.StringUtils;
import com.bpic.system.mapper.UserHistoryMapper;
import com.bpic.system.service.IUserHistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

@Service
public class UserHistoryServiceImpl implements IUserHistoryService {

    @Autowired
    private UserHistoryMapper userHistoryMapper;

    @Override
    public String insertHistory(String telPhone, String msg) {
        if(!StringUtils.isEmpty(msg)) {
            StringBuffer sb = new StringBuffer();
            UserHistory userHistory = userHistoryMapper.selectByPhone(telPhone);
            if (userHistory != null) {
                String[] split = userHistory.getHistory().split(",");
                List<String> list = new ArrayList<>();
                for (String s : split) {
                    list.add(s);
                }
                for (int i = 0; i < list.size(); i++) {
                    if (list.get(i).equals(msg)) {
                        list.remove(i);
                    }
                }
                list.add(msg);
                if (list.size() > 3) {
                    for (int i = 1; i <= 3; i++) {
                        if (i == 3) {
                            sb.append(list.get(list.size() - (4 - i)));
                        } else {
                            sb.append(list.get(list.size() - (4 - i))).append(",");
                        }
                    }
                } else {
                    for (int i = 0; i < list.size(); i++) {
                        if (i == list.size() - 1) {
                            sb.append(list.get(i));
                        } else {
                            sb.append(list.get(i)).append(",");
                        }
                    }
                }
                userHistory.setHistory(sb.toString());
                userHistoryMapper.update(userHistory);
            } else {
                UserHistory u = new UserHistory();
                sb.append(msg);
                u.setHistory(sb.toString());
                u.setTelphone(telPhone);
                userHistoryMapper.insert(u);
            }
            return sb.toString();
        }
        UserHistory userHistory = userHistoryMapper.selectByPhone(telPhone);
        if(userHistory!=null){
            return userHistory.getHistory();
        }else {
            return "";
        }
    }

    @Override
    public UserHistory selectByTel(String telPhone) {
        return userHistoryMapper.selectByPhone(telPhone);
    }

    @Override
    public void insertNew(UserHistory userHistory) {
        userHistoryMapper.insert(userHistory);
    }

    @Override
    public void deleteByTel(String old) {
        userHistoryMapper.deleteByTel(old);
    }
}
