package com.linkage.module.liposs.performance.bio.snmpGather;

public class FluxPortInfo {
  //端口索引
  private String ifindex="";
  //端口描述
  private String ifdescr="";
  //端口名称
  private String ifname="";
  //端口别名
  private String ifnamedefined="";
  //端口ip
  private String ifportip="";
  //端口标识，跟getway结合使用
  private String port_info="";
  private int getway=1;//5;端口ip,4:端口别名,3:端口名字,2:端口描述
  private String collectParm="00000000000000000000";//采集参数 
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

public int getGetway() {
	return getway;
}

public void setGetway(int getway) {
	this.getway = getway;
}


public String getPort_info()
{
	return port_info;
}


public void setPort_info(String port_info)
{
	this.port_info = port_info;
}

public String getCollectParm() {
	return collectParm;
}

public void setCollectParm(String collectParm) {
	this.collectParm = collectParm;
}

}