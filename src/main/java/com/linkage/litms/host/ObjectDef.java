/*
 * ObjectDef.java
 *
 * Created on 2006年1月11日, 上午10:16
 */

package com.linkage.litms.host;

/**
 * 被管对象
 * @author cheny
 */
public class ObjectDef {
    /** 对象编号 */
    public int dxbh;
    /** 对象名称 */
    public String dxmc;
    /** 对象说明 */
    public String dxsm;
    /** 对象类型 */
    public int dxlx;
    /** 父对象编号 */
    public int fdxbh;
    /** IP地址 */
    public String ipdz;
    /** 端口号 */
    public int dkh;
    /** 轮询间隔 */
    public int lxjg;
    /** 采样间隔 */
    public int cyjg;
    /** 告警间隔 */
    public int gjjg;
    /** 轮询标志 */
    public int lxbz;
    /** 所在位置 */
    public String wz;
    /** 通信密钥0 */
    public int key0;
    /** 通信密钥1 */
    public int key1;
    /** 通信密钥2 */
    public int key2;
    /** 通信密钥3 */
    public int key3;
    
    /** Creates a new instance of ObjectDef */
    public ObjectDef() {
    }
    
}
