package com.wjq.gmall.user.service.impl;

//这里  service 需要使用dubbo的注解
import com.alibaba.dubbo.config.annotation.Service;
import com.wjq.gmall.bean.UmsMember;
import com.wjq.gmall.user.mapper.UserMapper;
import com.wjq.gmall.bean.UmsMemberReceiveAddress;
import com.wjq.gmall.service.UserService;
import com.wjq.gmall.user.mapper.UmsMemberReceiveAddressMapper;
import org.springframework.beans.factory.annotation.Autowired;
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

