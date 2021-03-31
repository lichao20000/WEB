package com.linkage.litms.webtopo.snmpgather;

import java.util.ArrayList;

public interface InterfaceReadInfo {
  public void setDevice_ID(String device_id);
//  public void setAccountInfo(String account,String passwd);
  public ArrayList getDeviceInfo();
  public int getReportType();
}