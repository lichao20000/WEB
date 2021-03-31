package com.linkage.litms.webtopo.snmpgather;


/**
 * 为实现接口做的简单的赋值方法
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2006</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */
public class DeviceCommonData {
  String device_id = "";
//  String account = "";
//  String passwd = "";
  //如果是调用的第一种方法，则返回一；如果返回的是第二种方法，则返回２
  int    type=1 ;
  public DeviceCommonData() {

  }

  public void setDevice_ID(String device_id) {
    this.device_id = device_id;
  }

//  public void setAccountInfo(String account, String passwd) {
//    this.account = account;
//    this.passwd = passwd;
//  }

  public int getReportType()
  {
    return type;

  }



}