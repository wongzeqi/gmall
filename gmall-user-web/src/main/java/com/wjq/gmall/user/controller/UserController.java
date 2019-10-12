package com.wjq.gmall.user.controller;


import com.alibaba.dubbo.config.annotation.Reference;
import com.wjq.gmall.bean.UmsMember;
import com.wjq.gmall.bean.UmsMemberReceiveAddress;
import com.wjq.gmall.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;


import java.util.List;

@Controller
@ResponseBody
public class UserController {
    @Reference
    UserService userService;

    @RequestMapping("index")
    public String index(){
        return "hello world";
    }


    //将所有的
    @RequestMapping("getAllUser")
    public List<UmsMember>  getAllUser(){
        List<UmsMember> umsrMembers = userService.getAllUser();
        return umsrMembers;
    }

    //根据用户id查询收货地址
    @RequestMapping("getReceiveAddressByMemberId")
    public List<UmsMemberReceiveAddress> getAddressByMemberId(String memberId){
        List<UmsMemberReceiveAddress> umsrMembersRecesiveAddresses = userService.getReceiveAddressByMemberId(memberId);
        return umsrMembersRecesiveAddresses;
    }

}
