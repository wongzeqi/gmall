package com.wjq.gmall.user.service.impl;

import com.wjq.gmall.user.bean.UmsMember;
import com.wjq.gmall.user.bean.UmsMemberReceiveAddress;
import com.wjq.gmall.user.mapper.UmsMemberReceiveAddressMapper;
import com.wjq.gmall.user.mapper.UserMapper;
import com.wjq.gmall.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.List;


@Service
public class UserServiceImpl implements UserService {

    @Autowired
    UmsMemberReceiveAddressMapper umsMemberReceiveAddressMapper;
    @Autowired
    UserMapper userMapper;

    @Override
    public List<UmsMemberReceiveAddress> getReceiveAddressByMemberId(String memberId) {
        //通用的mapper的条件查询
        Example e = new Example(UmsMemberReceiveAddress.class);
        e.createCriteria().andEqualTo("memberId",memberId);

        //或者这样查询
        //UmsMemberReceiveAddress ura = new UmsMemberReceiveAddress();
        //ura.setMemberId(memberId);

        return umsMemberReceiveAddressMapper.selectByExample(e);
    }

    @Override
    public List<UmsMember> getAllUser() {
        return userMapper.selectAll();
    }
}

