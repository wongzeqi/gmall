//package com.wjq.gmall.cart.controller;
//
//
//import com.alibaba.dubbo.config.annotation.Reference;
//import com.alibaba.fastjson.JSON;
//import com.wjq.gmall.bean.OmsCartItem;
//import com.wjq.gmall.bean.PmsSkuInfo;
//import com.wjq.gmall.service.CartService;
//import com.wjq.gmall.service.SkuService;
//import com.wjq.gmall.util.CookieUtil;
//import org.apache.commons.lang3.StringUtils;
//import org.springframework.stereotype.Controller;
//import org.springframework.ui.ModelMap;
//import org.springframework.web.bind.annotation.RequestMapping;
//
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import java.math.BigDecimal;
//import java.util.ArrayList;
//import java.util.Date;
//import java.util.List;
//
//@Controller
//public class CartController_ {
//
//    @Reference
//    SkuService skuService;
//    @Reference
//    CartService cartService;
//
//    @RequestMapping("addToCart")
//    public String addToCart(String skuId, int quantity, HttpServletRequest request , HttpServletResponse response){
//        //调用商品的服务 查询商品的信息
//        PmsSkuInfo pmsSkuInfo = skuService.getSkuById(skuId);
//        //将商品信息封装成购物车信息
//        OmsCartItem omsCartItem = new OmsCartItem();
//        omsCartItem.setCreateDate(new Date());
//        omsCartItem.setDeleteStatus(0);
//        omsCartItem.setModifyDate(new Date());
//        omsCartItem.setPrice(pmsSkuInfo.getPrice());
//        omsCartItem.setProductAttr("");
//        omsCartItem.setProductBrand("");
//        omsCartItem.setProductCategoryId(pmsSkuInfo.getCatalog3Id());
//        omsCartItem.setProductId(pmsSkuInfo.getSpuId());
//        omsCartItem.setProductName(pmsSkuInfo.getProductName());
//        omsCartItem.setProductPic(pmsSkuInfo.getSkuDefaultImg());
//        omsCartItem.setQuantity(quantity);
//        omsCartItem.setProductSkuId(pmsSkuInfo.getId());
//        //判断用户是否登录
//        String memberId = "";
//        List<OmsCartItem> omsCartItems = new ArrayList<>();
//        if(StringUtils.isBlank(memberId)){
//            //没登录怎么样
//            String cartListCookie = CookieUtil.getCookieValue(request, "cartListCookie", true);
//            if(StringUtils.isNotBlank(cartListCookie)) {//存在这个cookie
//                List<OmsCartItem> omsCartItemsList = JSON.parseArray(cartListCookie, OmsCartItem.class);
//                boolean exist = if_cart_exist(omsCartItemsList, omsCartItem);
//                //判断添加的商品（sku）在原来的cookie中是否存在
//                if (exist) {
//                    //之前添加过  更新购物车添加数量  更新总价格
//                    for (OmsCartItem cartItem : omsCartItemsList) {
//                        if(omsCartItem.getProductSkuId().equals(omsCartItem.getProductSkuId())){
//                            cartItem.setQuantity(cartItem.getQuantity()+omsCartItem.getQuantity());
//                            cartItem.setPrice(cartItem.getPrice().add(omsCartItem.getPrice()));
//                        }
//                    }
//                } else {//如果这个商品在原来的cookie中不存在  则直接添加进去
//                    omsCartItems.add(omsCartItem);
//                }
//            }
//            //如果不存在这个cookie   直接set一个cookie  进去
//            CookieUtil.setCookie(request,response,"cartListCookie", JSON.toJSONString(omsCartItems),60*60*72,true);
//
//        }else{//已经登录了
//            omsCartItem.setMemberId(memberId);
//            //从数据库查询购物车
//            OmsCartItem omsCartItem2 = cartService.ifCartExitByUser(memberId,skuId); //根据userID和skuId查询  本商品是否被此用户添加过
//            if(omsCartItem2==null){
//                //該用戶添加過当前商品
//                cartService.addCart(omsCartItem);
//            }else{
//                //当前用户添加过当前商品
//                cartService.updateCart(omsCartItem);
//            }
//            //同步缓存
//            cartService.flushCartCache(memberId);//根据memberId查询出所有的  该用户的购物车信息 放入缓存中user:109:cart 109用户的购物车信息
//        }
//        return "redirect:/success.html";
//    }
//
//    @RequestMapping("cartList")
//    public String cartList(HttpServletRequest request , HttpServletResponse response, ModelMap map) {
//        List<OmsCartItem> omsCartItems = new ArrayList<>();
//        String memberId = "";
//        if (StringUtils.isNotBlank(memberId)) {
//            //查询db
//            cartService.getCartsByUser(memberId);//根据用户id查询他的购物车信息//应该是查缓存 在查数据库的......
//        } else {
//            //查询cookie
//            String omsCartItemsString = CookieUtil.getCookieValue(request, "cartListCookie", true);
//            if(StringUtils.isNotBlank(omsCartItemsString)) {
//                omsCartItems = JSON.parseArray(omsCartItemsString, OmsCartItem.class);
//            }
//        }
//
//        for (OmsCartItem omsCartItem : omsCartItems) {
//            BigDecimal bigDecimal = new BigDecimal(omsCartItem.getQuantity());
//            omsCartItem.setTotalPrice(omsCartItem.getPrice().multiply(bigDecimal));
//        }
//
//        map.put("cartList", omsCartItems);
//        return "cartList";
//    }
//
//    private boolean if_cart_exist(List<OmsCartItem> omsCartItemsList, OmsCartItem omsCartItem) {
//        boolean b = false;
//        for (OmsCartItem cartItem : omsCartItemsList) {
//            String skuId = cartItem.getProductSkuId();
//            if(skuId.equals(omsCartItem.getProductSkuId())){
//                b = true;
//            }
//        }
//        return b;
//    }
//
//
//}
