package com.wjq.gmall.user.service.impl;

//这里  service 需要使用dubbo的注解
import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSON;
import com.wjq.gmall.bean.UmsMember;
import com.wjq.gmall.user.mapper.UserMapper;
import com.wjq.gmall.bean.UmsMemberReceiveAddress;
import com.wjq.gmall.service.UserService;
import com.wjq.gmall.user.mapper.UmsMemberReceiveAddressMapper;
import com.wjq.gmall.util.RedisUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import redis.clients.jedis.Jedis;
import tk.mybatis.mapper.entity.Example;

import java.util.List;


@Service
public class UserServiceImpl implements UserService {

    @Autowired
    UmsMemberReceiveAddressMapper umsMemberReceiveAddressMapper;
    @Autowired
    UserMapper userMapper;
    @Autowired
    RedisUtil redisUtil;

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

    @Override
    public UmsMember login(UmsMember umsMember) {
        Jedis jedis = null;
        try {
            jedis = redisUtil.getJedis();

            if(jedis!=null){
                String umsMemberStr = jedis.get("user:" + umsMember.getPassword() + ":info");

                if (StringUtils.isNotBlank(umsMemberStr)) {
                    // 密码正确
                    UmsMember umsMemberFromCache = JSON.parseObject(umsMemberStr, UmsMember.class);
                    return umsMemberFromCache;
                }
            }
            // 链接redis失败，开启数据库
            UmsMember umsMemberFromDb =loginFromDb(umsMember);
            if(umsMemberFromDb!=null){
                jedis.setex("user:" + umsMember.getPassword() + ":info",60*60*24, JSON.toJSONString(umsMemberFromDb));
            }
            return umsMemberFromDb;
        }finally {
            if(jedis!=null){
                jedis.close();
            }
        }
    }

    private UmsMember loginFromDb(UmsMember umsMember) {

        List<UmsMember> umsMembers = userMapper.select(umsMember);

        if(umsMembers!=null&&umsMembers.size()>0){
            return umsMembers.get(0);
        }

        return null;

    }



    //将token和memberid写入redis中
    @Override
    public void addUserToken(String token, String memberId) {
        Jedis jedis = redisUtil.getJedis();

        jedis.setex("user:"+memberId+":token",60*60*2,token);

        jedis.close();
    }

    @Override
    public void addOauthUser(UmsMember umsMember) {
        userMapper.insert(umsMember);
    }

    @Override
    public UmsMember checkOAuthUserBySourceId(String sourceUid) {
        UmsMember umsMember = new UmsMember();
        umsMember.setSourceUid(sourceUid);
        return userMapper.selectOne(umsMember);
    }
}

