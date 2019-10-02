package com.wjq.gmall.user.service;

import com.wjq.gmall.user.bean.UmsMember;
import com.wjq.gmall.user.bean.UmsMemberReceiveAddress;

import java.util.List;


public interface UserService {


    List<UmsMemberReceiveAddress> getReceiveAddressByMemberId(String memberId);

    List<UmsMember> getAllUser();
}
