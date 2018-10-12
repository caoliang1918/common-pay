// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: PayReq.proto

package org.zhongweixian.request;

public interface PayRequestOrBuilder extends
    // @@protoc_insertion_point(interface_extends:src.main.proto.PayRequest)
    com.google.protobuf.MessageOrBuilder {

  /**
   * <pre>
   *支付渠道
   * </pre>
   *
   * <code>optional .src.main.proto.Channel channel = 1;</code>
   */
  int getChannelValue();
  /**
   * <pre>
   *支付渠道
   * </pre>
   *
   * <code>optional .src.main.proto.Channel channel = 1;</code>
   */
  org.zhongweixian.request.Channel getChannel();

  /**
   * <pre>
   *类型
   * </pre>
   *
   * <code>optional .src.main.proto.PayType payType = 2;</code>
   */
  int getPayTypeValue();
  /**
   * <pre>
   *类型
   * </pre>
   *
   * <code>optional .src.main.proto.PayType payType = 2;</code>
   */
  org.zhongweixian.request.PayType getPayType();

  /**
   * <pre>
   * 支付金额
   * </pre>
   *
   * <code>optional int64 amount = 3;</code>
   */
  long getAmount();

  /**
   * <pre>
   * </pre>
   *
   * <code>optional string orderNo = 4;</code>
   */
  java.lang.String getOrderNo();
  /**
   * <pre>
   * </pre>
   *
   * <code>optional string orderNo = 4;</code>
   */
  com.google.protobuf.ByteString
      getOrderNoBytes();

  /**
   * <pre>
   * </pre>
   *
   * <code>optional string clientIp = 5;</code>
   */
  java.lang.String getClientIp();
  /**
   * <pre>
   * </pre>
   *
   * <code>optional string clientIp = 5;</code>
   */
  com.google.protobuf.ByteString
      getClientIpBytes();

  /**
   * <pre>
   *商品描述
   * </pre>
   *
   * <code>optional string body = 6;</code>
   */
  java.lang.String getBody();
  /**
   * <pre>
   *商品描述
   * </pre>
   *
   * <code>optional string body = 6;</code>
   */
  com.google.protobuf.ByteString
      getBodyBytes();

  /**
   * <pre>
   *签名
   * </pre>
   *
   * <code>optional string sign = 7;</code>
   */
  java.lang.String getSign();
  /**
   * <pre>
   *签名
   * </pre>
   *
   * <code>optional string sign = 7;</code>
   */
  com.google.protobuf.ByteString
      getSignBytes();

  /**
   * <pre>
   *签名类型
   * </pre>
   *
   * <code>optional string signType = 8;</code>
   */
  java.lang.String getSignType();
  /**
   * <pre>
   *签名类型
   * </pre>
   *
   * <code>optional string signType = 8;</code>
   */
  com.google.protobuf.ByteString
      getSignTypeBytes();

  /**
   * <pre>
   *随机数
   * </pre>
   *
   * <code>optional string random = 9;</code>
   */
  java.lang.String getRandom();
  /**
   * <pre>
   *随机数
   * </pre>
   *
   * <code>optional string random = 9;</code>
   */
  com.google.protobuf.ByteString
      getRandomBytes();

  /**
   * <pre>
   *货币种类
   * </pre>
   *
   * <code>optional string feeType = 10;</code>
   */
  java.lang.String getFeeType();
  /**
   * <pre>
   *货币种类
   * </pre>
   *
   * <code>optional string feeType = 10;</code>
   */
  com.google.protobuf.ByteString
      getFeeTypeBytes();

  /**
   * <pre>
   *商品详情
   * </pre>
   *
   * <code>optional string detail = 11;</code>
   */
  java.lang.String getDetail();
  /**
   * <pre>
   *商品详情
   * </pre>
   *
   * <code>optional string detail = 11;</code>
   */
  com.google.protobuf.ByteString
      getDetailBytes();

  /**
   * <pre>
   *请求地址
   * </pre>
   *
   * <code>optional string requestUrl = 12;</code>
   */
  java.lang.String getRequestUrl();
  /**
   * <pre>
   *请求地址
   * </pre>
   *
   * <code>optional string requestUrl = 12;</code>
   */
  com.google.protobuf.ByteString
      getRequestUrlBytes();

  /**
   * <pre>
   *回调通知地址
   * </pre>
   *
   * <code>optional string notifyUrl = 13;</code>
   */
  java.lang.String getNotifyUrl();
  /**
   * <pre>
   *回调通知地址
   * </pre>
   *
   * <code>optional string notifyUrl = 13;</code>
   */
  com.google.protobuf.ByteString
      getNotifyUrlBytes();

  /**
   * <pre>
   *前端返回地址
   * </pre>
   *
   * <code>optional string returnUrl = 14;</code>
   */
  java.lang.String getReturnUrl();
  /**
   * <pre>
   *前端返回地址
   * </pre>
   *
   * <code>optional string returnUrl = 14;</code>
   */
  com.google.protobuf.ByteString
      getReturnUrlBytes();

  /**
   * <pre>
   *扩展字段
   * </pre>
   *
   * <code>map&lt;string, string&gt; ext = 15;</code>
   */
  int getExtCount();
  /**
   * <pre>
   *扩展字段
   * </pre>
   *
   * <code>map&lt;string, string&gt; ext = 15;</code>
   */
  boolean containsExt(
      java.lang.String key);
  /**
   * Use {@link #getExtMap()} instead.
   */
  @java.lang.Deprecated
  java.util.Map<java.lang.String, java.lang.String>
  getExt();
  /**
   * <pre>
   *扩展字段
   * </pre>
   *
   * <code>map&lt;string, string&gt; ext = 15;</code>
   */
  java.util.Map<java.lang.String, java.lang.String>
  getExtMap();
  /**
   * <pre>
   *扩展字段
   * </pre>
   *
   * <code>map&lt;string, string&gt; ext = 15;</code>
   */

  java.lang.String getExtOrDefault(
      java.lang.String key,
      java.lang.String defaultValue);
  /**
   * <pre>
   *扩展字段
   * </pre>
   *
   * <code>map&lt;string, string&gt; ext = 15;</code>
   */

  java.lang.String getExtOrThrow(
      java.lang.String key);
}
