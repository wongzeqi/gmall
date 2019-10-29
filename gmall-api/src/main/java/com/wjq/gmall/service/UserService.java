package com.wjq.gmall.service;

import com.wjq.gmall.bean.UmsMember;
import com.wjq.gmall.bean.UmsMemberReceiveAddress;

import java.util.List;


public interface UserService {


    List<UmsMemberReceiveAddress> getReceiveAddressByMemberId(String memberId);

    List<UmsMember> getAllUser();

    UmsMember login(UmsMember umsMember);

    void addUserToken(String token, String memberId);

    void addOauthUser(UmsMember umsMember);

    UmsMember checkOAuthUserBySourceId(String sourceUid);
}
