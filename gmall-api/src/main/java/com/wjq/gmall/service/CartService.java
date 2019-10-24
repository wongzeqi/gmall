package com.wjq.gmall.service;

import com.wjq.gmall.bean.OmsCartItem;

import java.util.List;

public interface CartService {
    List<OmsCartItem> getCartsByUser(String memberId);

    void addCart(OmsCartItem omsCartItem);

    void updateCart(OmsCartItem omsCartItem);

    void flushCartCache(String memberId);

    OmsCartItem ifCartExitByUser(String memberId, String skuId);

    void checkCart(OmsCartItem omsCartItem);
}
