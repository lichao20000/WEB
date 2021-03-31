package com.linkage.litms.vipms.flux;

import java.util.ArrayList;

public class FluxConfigInfo {
    // 设备id
    private String device_id;

    // 端口索引
    private String ifindex;

    // 端口描述
    private String ifdescr ="null";

    // 端口名称
    private String ifname="null";

    // 端口别名
    private String ifnamedefined="null";

    // 端口ip
    private String ifportip="null";

    // 采集方式
    private int getway;

    // 采集参数
    private String getarg;

    // 端口告警
    private int ifportwarn = 0;

    // 是否考核
    private int ifexam = 0;

    // 端口告警级别
    private int portwarnlevel = 0;

    // 端口类型
    private long iftype = -1;

    // 端口最大传输单元
    private long ifmtu = -1;

    // 端口速率
    private long ifspeed = -1;

    // 端口高速速率
    private long ifhighspeed = -1;

    // 接口实际速率
    private long if_real_speed = -1;
    
    /**
     * 带宽利用率阈值一
     */
    
    //端口流入带宽利用率阈值一操作符
    private int ifinoct_maxtype=2;

    // 端口流入带宽利用率阈值一
    private double ifinoctetsbps_max = 80;
    
    //端口流出带宽利用率阈值一操作符
    private int ifoutoct_maxtype=2;

    // 端口流出带宽利用率阈值一
    private double ifoutoctetsbps_max = 80;

    // 端口流入丢包率阈值
    private double ifindiscardspps_max = -1;

    // 端口流出丢包率阈值
    private double ifoutdiscardspps_max = -1;

    // 端口流入错包率阈值
    private double ifinerrorspps_max = -1;

    // 端口流出错包率阈值
    private double ifouterrorspps_max = -1;

    // 连续多少次超出阈值一才发出告警
    private int warningnum = 3;

    // 发出阈值一告警时的告警级别
    private int warninglevel = 3;

    // 发出恢复告警时的告警级别
    private int reinstatelevel = 1;
    
    /**
     * 带宽利用率阈值二
     */
    
    //端口流入利用率阈值二操作符
    private int ifinoct_mintype=0;
    
    //端口流入利用率阈值二
    private double ifinoctetsbps_min=-1;
    
    //端口流出利用率阈值二操作符
    private int ifoutoct_mintype=0;
    
    //端口流出利用率阈值二
    private double ifoutoctetsbps_min=-1;
    
    //连续多少次超出阈值二才发出告警
    private int warningnum_min=3;
    
    //发出阈值二告警时的告警级别
    private int warninglevel_min=3;
    
    //带宽利用率阈值恢复告警级别
    private int reinlevel_min=1;  
    
    /**
     * 动态阈值一
     */
    
    //动态阈值一操作符
    private int overmax =0;
    
    // 动态阈值一阈值
    private int overper = -1;

    // 超出动态阈值一次数
    private int overnum = 3;

    // 动态阈值一告警级别
    private int overlevel = 3;

    // 动态阈值一告警恢复级别
    private int reinoverlevel = 1;
    
    /**
     * 动态阈值二
     */
    
    //动态阈值二操作符
    private int overmin=0;
    
    //动态阈值二阈值
    private  int overper_min=-1;
    
    //超出动态阈值二次数
    private int overnum_min=3;
    
    //动态阈值二的故障告警级别
    private int overlevel_min=3;
    
    //动态阈值二的恢复告警级别
    private int reinoverlevel_min=1;

    // 动态阈值比较天数
    private int com_day = 3;

    // 发突变告警流入速率变化的率
    private double ifinoctets = -1;

    // 发送流入速率突变告警级别
    private int inwarninglevel = 3;

    // 发送流入速率恢复突变告警的级别
    private int inreinstatelevel = 1;

    // 发送流入速率突变告警的操作符
    private int inoperation = 1;

    // 判断是否配置流入突变告警操作
    private int intbflag = 0;

    // 发突变告警流出速率变化的率
    private double ifoutoctets = -1;

    // 发送流出速率突变告警级别
    private int outwarninglevel = 3;

    // 发送流出速率恢复突变告警的级别
    private int outreinstatelevel = 0;

    // 发送流出速率突变告警的操作符
    private int outoperation = 1;

    // 是否配置流出突变告警操作
    private int outtbflag = 0;

    // 使用snmp的版本
    private int snmpversion;

    // 判断类型
    private String model;

    // 计数器位数
    private String counternum;

    // 轮询间隔
    private int polltime = 300;

    // 是否采集配置
    private int gatherflag = 1;

    // 端口详细配置
    private int ifconfigflag = 1;

    // 原始数据是否入库
    private int intodb = 0;

    // 要判断的原始性能的oid
    private ArrayList perList = null;

    // 要采集的oid
    private ArrayList baseList = null;

    // 原始性能oid(PortJudgeArrt)
    private ArrayList oidPerfList = null;

    /** 设备型号 */
    private String device_model = "";

    // 设备型号
    private String serial = null;  
   
    
    //端口的详细信息，如果getway为5，就是端口ip
    //2：端口描述，3：端口名字，4：端口别名，1：端口索引
    private String port_info="";
    

    public FluxConfigInfo() {
    }


	public ArrayList getBaseList()
	{
		return baseList;
	}


	public void setBaseList(ArrayList baseList)
	{
		this.baseList = baseList;
	}


	public int getCom_day()
	{
		return com_day;
	}


	public void setCom_day(int com_day)
	{
		this.com_day = com_day;
	}


	public String getCounternum()
	{
		return counternum;
	}


	public void setCounternum(String counternum)
	{
		this.counternum = counternum;
	}


	public String getDevice_id()
	{
		return device_id;
	}


	public void setDevice_id(String device_id)
	{
		this.device_id = device_id;
	}


	public String getDevice_model()
	{
		return device_model;
	}


	public void setDevice_model(String device_model)
	{
		this.device_model = device_model;
	}


	public int getGatherflag()
	{
		return gatherflag;
	}


	public void setGatherflag(int gatherflag)
	{
		this.gatherflag = gatherflag;
	}


	public String getGetarg()
	{
		return getarg;
	}


	public void setGetarg(String getarg)
	{
		this.getarg = getarg;
	}


	public int getGetway()
	{
		return getway;
	}


	public void setGetway(int getway)
	{
		this.getway = getway;
	}


	public long getIf_real_speed()
	{
		return if_real_speed;
	}


	public void setIf_real_speed(long if_real_speed)
	{
		this.if_real_speed = if_real_speed;
	}


	public int getIfconfigflag()
	{
		return ifconfigflag;
	}


	public void setIfconfigflag(int ifconfigflag)
	{
		this.ifconfigflag = ifconfigflag;
	}


	public String getIfdescr()
	{
		return ifdescr;
	}


	public void setIfdescr(String ifdescr)
	{
		this.ifdescr = ifdescr;
	}


	public int getIfexam()
	{
		return ifexam;
	}


	public void setIfexam(int ifexam)
	{
		this.ifexam = ifexam;
	}


	public long getIfhighspeed()
	{
		return ifhighspeed;
	}


	public void setIfhighspeed(long ifhighspeed)
	{
		this.ifhighspeed = ifhighspeed;
	}


	public String getIfindex()
	{
		return ifindex;
	}


	public void setIfindex(String ifindex)
	{
		this.ifindex = ifindex;
	}


	public double getIfindiscardspps_max()
	{
		return ifindiscardspps_max;
	}


	public void setIfindiscardspps_max(double ifindiscardspps_max)
	{
		this.ifindiscardspps_max = ifindiscardspps_max;
	}


	public double getIfinerrorspps_max()
	{
		return ifinerrorspps_max;
	}


	public void setIfinerrorspps_max(double ifinerrorspps_max)
	{
		this.ifinerrorspps_max = ifinerrorspps_max;
	}


	public int getIfinoct_maxtype()
	{
		return ifinoct_maxtype;
	}


	public void setIfinoct_maxtype(int ifinoct_maxtype)
	{
		this.ifinoct_maxtype = ifinoct_maxtype;
	}


	public int getIfinoct_mintype()
	{
		return ifinoct_mintype;
	}


	public void setIfinoct_mintype(int ifinoct_mintype)
	{
		this.ifinoct_mintype = ifinoct_mintype;
	}


	public double getIfinoctets()
	{
		return ifinoctets;
	}


	public void setIfinoctets(double ifinoctets)
	{
		this.ifinoctets = ifinoctets;
	}


	public double getIfinoctetsbps_max()
	{
		return ifinoctetsbps_max;
	}


	public void setIfinoctetsbps_max(double ifinoctetsbps_max)
	{
		this.ifinoctetsbps_max = ifinoctetsbps_max;
	}


	public double getIfinoctetsbps_min()
	{
		return ifinoctetsbps_min;
	}


	public void setIfinoctetsbps_min(double ifinoctetsbps_min)
	{
		this.ifinoctetsbps_min = ifinoctetsbps_min;
	}


	public long getIfmtu()
	{
		return ifmtu;
	}


	public void setIfmtu(long ifmtu)
	{
		this.ifmtu = ifmtu;
	}


	public String getIfname()
	{
		return ifname;
	}


	public void setIfname(String ifname)
	{
		this.ifname = ifname;
	}


	public String getIfnamedefined()
	{
		return ifnamedefined;
	}


	public void setIfnamedefined(String ifnamedefined)
	{
		this.ifnamedefined = ifnamedefined;
	}


	public double getIfoutdiscardspps_max()
	{
		return ifoutdiscardspps_max;
	}


	public void setIfoutdiscardspps_max(double ifoutdiscardspps_max)
	{
		this.ifoutdiscardspps_max = ifoutdiscardspps_max;
	}


	public double getIfouterrorspps_max()
	{
		return ifouterrorspps_max;
	}


	public void setIfouterrorspps_max(double ifouterrorspps_max)
	{
		this.ifouterrorspps_max = ifouterrorspps_max;
	}


	public int getIfoutoct_maxtype()
	{
		return ifoutoct_maxtype;
	}


	public void setIfoutoct_maxtype(int ifoutoct_maxtype)
	{
		this.ifoutoct_maxtype = ifoutoct_maxtype;
	}


	public int getIfoutoct_mintype()
	{
		return ifoutoct_mintype;
	}


	public void setIfoutoct_mintype(int ifoutoct_mintype)
	{
		this.ifoutoct_mintype = ifoutoct_mintype;
	}


	public double getIfoutoctets()
	{
		return ifoutoctets;
	}


	public void setIfoutoctets(double ifoutoctets)
	{
		this.ifoutoctets = ifoutoctets;
	}


	public double getIfoutoctetsbps_max()
	{
		return ifoutoctetsbps_max;
	}


	public void setIfoutoctetsbps_max(double ifoutoctetsbps_max)
	{
		this.ifoutoctetsbps_max = ifoutoctetsbps_max;
	}


	public double getIfoutoctetsbps_min()
	{
		return ifoutoctetsbps_min;
	}


	public void setIfoutoctetsbps_min(double ifoutoctetsbps_min)
	{
		this.ifoutoctetsbps_min = ifoutoctetsbps_min;
	}


	public String getIfportip()
	{
		return ifportip;
	}


	public void setIfportip(String ifportip)
	{
		this.ifportip = ifportip;
	}


	public int getIfportwarn()
	{
		return ifportwarn;
	}


	public void setIfportwarn(int ifportwarn)
	{
		this.ifportwarn = ifportwarn;
	}


	public long getIfspeed()
	{
		return ifspeed;
	}


	public void setIfspeed(long ifspeed)
	{
		this.ifspeed = ifspeed;
	}


	public long getIftype()
	{
		return iftype;
	}


	public void setIftype(long iftype)
	{
		this.iftype = iftype;
	}


	public int getInoperation()
	{
		return inoperation;
	}


	public void setInoperation(int inoperation)
	{
		this.inoperation = inoperation;
	}


	public int getInreinstatelevel()
	{
		return inreinstatelevel;
	}


	public void setInreinstatelevel(int inreinstatelevel)
	{
		this.inreinstatelevel = inreinstatelevel;
	}


	public int getIntbflag()
	{
		return intbflag;
	}


	public void setIntbflag(int intbflag)
	{
		this.intbflag = intbflag;
	}


	public int getIntodb()
	{
		return intodb;
	}


	public void setIntodb(int intodb)
	{
		this.intodb = intodb;
	}


	public int getInwarninglevel()
	{
		return inwarninglevel;
	}


	public void setInwarninglevel(int inwarninglevel)
	{
		this.inwarninglevel = inwarninglevel;
	}


	public String getModel()
	{
		return model;
	}


	public void setModel(String model)
	{
		this.model = model;
	}


	public ArrayList getOidPerfList()
	{
		return oidPerfList;
	}


	public void setOidPerfList(ArrayList oidPerfList)
	{
		this.oidPerfList = oidPerfList;
	}


	public int getOutoperation()
	{
		return outoperation;
	}


	public void setOutoperation(int outoperation)
	{
		this.outoperation = outoperation;
	}


	public int getOutreinstatelevel()
	{
		return outreinstatelevel;
	}


	public void setOutreinstatelevel(int outreinstatelevel)
	{
		this.outreinstatelevel = outreinstatelevel;
	}


	public int getOuttbflag()
	{
		return outtbflag;
	}


	public void setOuttbflag(int outtbflag)
	{
		this.outtbflag = outtbflag;
	}


	public int getOutwarninglevel()
	{
		return outwarninglevel;
	}


	public void setOutwarninglevel(int outwarninglevel)
	{
		this.outwarninglevel = outwarninglevel;
	}


	public int getOverlevel()
	{
		return overlevel;
	}


	public void setOverlevel(int overlevel)
	{
		this.overlevel = overlevel;
	}


	public int getOverlevel_min()
	{
		return overlevel_min;
	}


	public void setOverlevel_min(int overlevel_min)
	{
		this.overlevel_min = overlevel_min;
	}


	public int getOvermax()
	{
		return overmax;
	}


	public void setOvermax(int overmax)
	{
		this.overmax = overmax;
	}


	public int getOvermin()
	{
		return overmin;
	}


	public void setOvermin(int overmin)
	{
		this.overmin = overmin;
	}


	public int getOvernum()
	{
		return overnum;
	}


	public void setOvernum(int overnum)
	{
		this.overnum = overnum;
	}


	public int getOvernum_min()
	{
		return overnum_min;
	}


	public void setOvernum_min(int overnum_min)
	{
		this.overnum_min = overnum_min;
	}


	public int getOverper()
	{
		return overper;
	}


	public void setOverper(int overper)
	{
		this.overper = overper;
	}


	public int getOverper_min()
	{
		return overper_min;
	}


	public void setOverper_min(int overper_min)
	{
		this.overper_min = overper_min;
	}


	public ArrayList getPerList()
	{
		return perList;
	}


	public void setPerList(ArrayList perList)
	{
		this.perList = perList;
	}


	public int getPolltime()
	{
		return polltime;
	}


	public void setPolltime(int polltime)
	{
		this.polltime = polltime;
	}


	public String getPort_info()
	{
		return port_info;
	}


	public void setPort_info(String port_info)
	{
		this.port_info = port_info;
	}


	public int getPortwarnlevel()
	{
		return portwarnlevel;
	}


	public void setPortwarnlevel(int portwarnlevel)
	{
		this.portwarnlevel = portwarnlevel;
	}


	public int getReinlevel_min()
	{
		return reinlevel_min;
	}


	public void setReinlevel_min(int reinlevel_min)
	{
		this.reinlevel_min = reinlevel_min;
	}


	public int getReinoverlevel()
	{
		return reinoverlevel;
	}


	public void setReinoverlevel(int reinoverlevel)
	{
		this.reinoverlevel = reinoverlevel;
	}


	public int getReinoverlevel_min()
	{
		return reinoverlevel_min;
	}


	public void setReinoverlevel_min(int reinoverlevel_min)
	{
		this.reinoverlevel_min = reinoverlevel_min;
	}


	public int getReinstatelevel()
	{
		return reinstatelevel;
	}


	public void setReinstatelevel(int reinstatelevel)
	{
		this.reinstatelevel = reinstatelevel;
	}


	public String getSerial()
	{
		return serial;
	}


	public void setSerial(String serial)
	{
		this.serial = serial;
	}


	public int getSnmpversion()
	{
		return snmpversion;
	}


	public void setSnmpversion(int snmpversion)
	{
		this.snmpversion = snmpversion;
	}


	public int getWarninglevel()
	{
		return warninglevel;
	}


	public void setWarninglevel(int warninglevel)
	{
		this.warninglevel = warninglevel;
	}


	public int getWarninglevel_min()
	{
		return warninglevel_min;
	}


	public void setWarninglevel_min(int warninglevel_min)
	{
		this.warninglevel_min = warninglevel_min;
	}


	public int getWarningnum()
	{
		return warningnum;
	}


	public void setWarningnum(int warningnum)
	{
		this.warningnum = warningnum;
	}


	public int getWarningnum_min()
	{
		return warningnum_min;
	}


	public void setWarningnum_min(int warningnum_min)
	{
		this.warningnum_min = warningnum_min;
	}   
    
}