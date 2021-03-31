package com.linkage.litms.vipms.flux;

public class FluxPortInfo {
  //端口索引
  private String ifindex;
  //端口描述
  private String ifdescr;
  //端口名称
  private String ifname;
  //端口别名
  private String ifnamedefined;
  //端口ip
  private String ifportip;


  public FluxPortInfo() {

  }

  public String getIfdescr() {
    return ifdescr;
  }

  public void setIfdescr(String ifdescr) {
    this.ifdescr = ifdescr;
  }

  public String getIfindex() {
    return ifindex;
  }

  public void setIfindex(String ifindex) {
    this.ifindex = ifindex;
  }

  public String getIfname() {
    return ifname;
  }

  public void setIfname(String ifname) {
    this.ifname = ifname;
  }

  public String getIfnamedefined() {
    return ifnamedefined;
  }

  public void setIfnamedefined(String ifnamedefined) {
    this.ifnamedefined = ifnamedefined;
  }

  public String getIfportip() {
    return ifportip;
  }

  public void setIfportip(String ifportip) {
    this.ifportip = ifportip;
  }

}