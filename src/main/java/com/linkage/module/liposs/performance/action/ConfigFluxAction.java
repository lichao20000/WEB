package com.linkage.module.liposs.performance.action;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.module.liposs.performance.bio.ConfigFluxBio;
import com.linkage.module.liposs.performance.bio.Flux_Map_Instance;
import com.linkage.module.liposs.performance.bio.snmpGather.FluxPortInfo;
import com.linkage.module.liposs.performance.bio.snmpGather.PortJudgeAttr;
import com.linkage.module.liposs.performance.dao.ConfigFluxDao;
import com.linkage.module.liposs.system.basesupport.BaseSupportAction;

public class ConfigFluxAction extends BaseSupportAction {
	
	private static Logger log = LoggerFactory.getLogger(ConfigFluxAction.class);
	private static final long serialVersionUID = 1512894189488920360L;
	private boolean isbatch=false;//是否批量配置
	private boolean ismodule=false;//是否是模板配置【用于控制关闭按钮,true关闭按钮不显示,false显示】
	private ConfigFluxBio cfb;
	private ConfigFluxDao cfd;
	private String ajax;//返回页面的AJAX
	//********************************设备基本信息**************************************
	private String device_id;
	private String dev_id;//用于保存配置流量【融合批量配置和单个配置】
	private String serial;//设备型号
	private String device_name;//设备名称【单个设备使用】
	private String loopback_ip;//设备IP【单个设备使用】
	private int auto;//是否自动配置 1：是,0否
	private String coltype;//0:V2版本64位计数器 1:V1版本32位计数器 2:V2版本32位计数器
	private int total;//是否总体配置 1:是 0:否
	private int polltime;//采集时间间隔
	private int intodb=1;//是否入库 1:是 0:否
	private boolean iskeep;//是否保留原有配置 true：保留原有配置 false：更新配置
	private boolean configresult=false;//单个设备的配置状态
	private List<Map> configList;//配置状态列表【批量配置时使用】
	//**********************************端口信息******************************************
	private List<FluxPortInfo> portList;
	private List<PortJudgeAttr> portInfoList;
	private List<Integer> getWayList;
	private List<Map<String,String>> cfgPortList;//已配置端口列表
	private String port_info;//端口信息
	private String ifindex="";//端口索引
	private String ifdescr="";// 端口描述
	private String ifname="";//端口名字
	private String ifnamedefined="";//端口别名
	private int iftype=-1;//端口类型
	private int ifspeed=-1;//端口速率
	private int ifmtu=-1;//端口最大传输单元
	private int ifhighspeed=-1;//高速端口速率
	private int gatherflag=1;//是否采集 0：不采集 1：采集
	//**********************************告警配置字段【兼容江苏的】****************************
	private int ifinoct_maxtype=0;//端口流入利用率阈值一比较操作符
	private float ifinoctetsbps_max=-1L;//端口流入利用率阈值一(%)
	private int ifoutoct_maxtype=0;//端口流出利用率阈值一比较操作符
	private float ifoutoctetsbps_max=-1L;//端口流出利用率阈值一(%)
	private float ifindiscardspps_max=-1L;//端口流入丢包率阈值(%)
	private float ifoutdiscardspps_max=-1L;//端口流出丢包率阈值(%)
	private float ifinerrorspps_max=-1L;//端口流入错包率阈值(%)
	private float ifouterrorspps_max=-1L;//端口流出错包率阈值(%)
	private int warningnum=3;//超出阈值的次数（发告警）
	private int warninglevel=0;//发出阈值告警时的告警级别
	private int reinstatelevel=0;//恢复告警级别
	//*****************固定阈值二***************************//
	private int ifinoct_mintype=0;//端口流入利用率阈值二比较操作符
	private float ifinoctetsbps_min=-1L;//端口流入利用率阈值二(%)
	private int ifoutoct_mintype=0;//端口流出利用率阈值二比较操作符
	private float ifoutoctetsbps_min=-1L;//端口流出利用率阈值二(%)
	private int warningnum_min=3;//超出阈值二的次数（发告警）
	private int warninglevel_min=0;//发出阈值二告警时的告警级别
	private int reinlevel_min=0;//阈值二恢复告警级别
	//*****************动态阈值一************************************************************//
	private int overmax=0;//动态阈值一操作符
	private float overper=-1L;//动态阈值一(%)
	private int overnum=3;//超出动态阈值一的次数(发告警)
	private int overlevel=0;//发出动态阈值一告警时的告警级别
	private int reinoverlevel=0;//发出恢复告警时的级别
	//*****************动态阈值二*************************//
	private int overmin=0;//动态阈值二操作符
	private float overper_min=-1L;//动态阈值二(%)
	private int overnum_min=3;//超出动态阈值二次数(发告警)
	private int overlevel_min=0;//发出动态阈值二告警时的告警级别
	private int reinoverlevel_min=0;//发出恢复告警时的级别
	private int com_day=3;//生成动态阈值一的天数(天)
	//*****************突变阈值************************************************************//
	private int intbflag=0;//判断是否配置流入突变告警操作
	private float ifinoctets=-1L;//流入速率变化率阈值(%)
	private int inoperation=0;//流入速率突变告警操作符
	private int inwarninglevel=0;//流入速率突变告警级别
	private int inreinstatelevel=0;//流入速率恢复突变告警级别
	private int outtbflag=0;//是否配置流出突变告警操作
	private float ifoutoctets=-1L;//流出速率变化率阈值(%)
	private int outoperation=0;//流出速率突变告警操作符
	private int outwarninglevel=0;//流出速率突变告警级别
	private int outreinstatelevel=0;//流出速率恢复突变告警级别
	//********************************告警信息*********************************************
	/**
	 * 初始化
	 */
	public String execute() throws Exception {
		configList=cfb.getDevConfigResult(dualDeviceId(device_id));
		if(!isbatch){//批量配置
			Map map=configList.get(0);
			if(map.get("serial")!=null){
				serial=map.get("serial")+"";
			}else{
				serial="-1";
			}
			device_name=(String)map.get("device_name");
			loopback_ip=(String)map.get("loopback_ip");
			configresult=Boolean.parseBoolean((String)map.get("result"));
		}
		log.debug("serial:"+serial);
		return SUCCESS;
	}
	/**
	 * 初始化portList
	 */
	private void initPortList(){
		String[] port_tmp=port_info.split("-/-");
		int n=port_tmp.length;
		String[] tmp;
		FluxPortInfo fpi;
		int num=0;
		portList=new ArrayList<FluxPortInfo>(n);
		String collectParam="0000000000000";
		int tmp_num=0;
		for(int i=0;i<n;i++){
			tmp=port_tmp[i].split("\\|\\|\\|");
			num=tmp.length;
			if(num<8){
				log.debug("ERROR:"+port_tmp[i]);
				continue;
			}
			fpi=new FluxPortInfo();
			fpi.setIfindex(tmp[0]);
			fpi.setIfdescr(tmp[1]);
			fpi.setIfname(tmp[2]);
			fpi.setIfnamedefined(tmp[3]);
			fpi.setIfportip(tmp[4]);
			fpi.setGetway(Integer.parseInt(tmp[5]));
			fpi.setPort_info(tmp[6]);
			collectParam="0000000000000";
			for(int j=7;j<num-7;j++){
				tmp_num=Integer.parseInt(tmp[j]);
				collectParam = collectParam.substring(0, tmp_num) + "1"+ collectParam.substring(tmp_num + 1);
			}
			fpi.setCollectParm(collectParam);
			portList.add(fpi);
			fpi=null;
		}
		fpi=null;
	}
	/**
	 * 保存流量
	 * @return
	 * @throws Exception
	 */
	public String saveFlux() throws Exception{
		if(total==0){
			initPortList();
		}
		cfb.setSlb(getSysBean());
		ajax=String.valueOf(cfb.saveFlux(dualDeviceId(dev_id), setWarnInit(), portList, auto, total, coltype, intodb, polltime, iskeep));
		execute();
		return SUCCESS;
	}
	/**
	 * 保存端口
	 * @return
	 * @throws Exception
	 */
	public String SavePort() throws Exception{
		cfb.setSlb(getSysBean());
		ajax=String.valueOf(cfb.editFluxPort(device_id, port_info.split("-/-"), intodb, gatherflag,setWarnInit()));
		return "editPort";
	}
	/**
	 * 删除已配置的流量
	 * @return
	 * @throws Exception
	 */
	public String DelFlux() throws Exception{
		cfb.setSlb(getSysBean());
		ajax=String.valueOf(cfb.delFluxConfig(device_id));
		return "ajax";
	}
	/**
	 * 编辑端口
	 * @return
	 * @throws Exception
	 */
	public String EditPort() throws Exception{
		if(port_info.split("-/-").length>1){//多个端口
			ifinoctetsbps_max=80;    
			ifoutoctetsbps_max=80;   
			ifindiscardspps_max=-1;  
			ifoutdiscardspps_max=-1; 
			ifinerrorspps_max=-1;    
			ifouterrorspps_max=-1;   
			warningnum=3;            
			warninglevel=3;          
			reinstatelevel=0;        
			overper=-1;              
			overnum=3;               
			com_day=3;               
			overlevel=3;             
			reinoverlevel=1;         
			intbflag=0;              
			ifinoctets=-1;           
			inoperation=1;           
			inwarninglevel=3;        
			inreinstatelevel=1;      
			outtbflag=0;             
			ifoutoctets=-1;          
			outwarninglevel=3;       
			outreinstatelevel=0;     
			outoperation=1;          
		}else{//单个端口
			List<Map> list=cfd.getFluxPortInfo(device_id, port_info);
			if(list==null){
				return "editPort";
			}
			for(Map m:list){
				ifindex=(String)m.get("ifindex");
				ifdescr=(String)m.get("ifdescr");
				ifname=(String)m.get("ifname");
				ifnamedefined=(String)m.get("ifnamedefined");
				iftype=((Number)m.get("iftype")).intValue();
				ifspeed=((Number)m.get("ifspeed")).intValue();              
				ifmtu=((Number)m.get("ifmtu")).intValue();                
				ifhighspeed=((Number)m.get("ifhighspeed")).intValue();          
				intodb=((Number)m.get("intodb")).intValue();                
				ifinoctetsbps_max=((Number)m.get("ifinoctetsbps_max")).intValue();    
				ifoutoctetsbps_max=((Number)m.get("ifoutoctetsbps_max")).intValue();   
				ifindiscardspps_max=((Number)m.get("ifindiscardspps_max")).intValue();  
				ifoutdiscardspps_max=((Number)m.get("ifoutdiscardspps_max")).intValue(); 
				ifinerrorspps_max=((Number)m.get("ifinerrorspps_max")).intValue();    
				ifouterrorspps_max=((Number)m.get("ifouterrorspps_max")).intValue();   
				warningnum=((Number)m.get("warningnum")).intValue();            
				warninglevel=((Number)m.get("warninglevel")).intValue();          
				reinstatelevel=((Number)m.get("reinstatelevel")).intValue();        
				overper=((Number)m.get("overper")).intValue();              
				overnum=((Number)m.get("overnum")).intValue();               
				com_day=((Number)m.get("com_day")).intValue();               
				overlevel=((Number)m.get("overlevel")).intValue();             
				reinoverlevel=((Number)m.get("reinoverlevel")).intValue();         
				intbflag=((Number)m.get("intbflag")).intValue();              
				ifinoctets=((Number)m.get("ifinoctets")).intValue();           
				inoperation=((Number)m.get("inoperation")).intValue();           
				inwarninglevel=((Number)m.get("inwarninglevel")).intValue();        
				inreinstatelevel=((Number)m.get("inreinstatelevel")).intValue();      
				outtbflag=((Number)m.get("outtbflag")).intValue();             
				ifoutoctets=((Number)m.get("ifoutoctets")).intValue();          
				outwarninglevel=((Number)m.get("outwarninglevel")).intValue();       
				outreinstatelevel=((Number)m.get("outreinstatelevel")).intValue();     
				outoperation=((Number)m.get("outoperation")).intValue();          
				gatherflag=((Number)m.get("gatherflag")).intValue();  
				if(null!=m.get("ifinoct_maxtype")){//江苏专用
					ifinoct_maxtype=((Number)m.get("ifinoct_maxtype")).intValue();
					ifoutoct_maxtype=((Number)m.get("ifoutoct_maxtype")).intValue();
					ifinoct_mintype=((Number)m.get("ifinoct_mintype")).intValue();
					ifinoctetsbps_min=((Number)m.get("ifinoctetsbps_min")).intValue();
					ifoutoct_mintype=((Number)m.get("ifoutoct_mintype")).intValue();
					ifoutoctetsbps_min=((Number)m.get("ifoutoctetsbps_min")).intValue();
					warningnum_min=((Number)m.get("warningnum_min")).intValue();
					warninglevel_min=((Number)m.get("warninglevel_min")).intValue();
					reinlevel_min=((Number)m.get("reinlevel_min")).intValue();
					overmax=((Number)m.get("overmax")).intValue();
					overmin=((Number)m.get("overmin")).intValue();
					overper_min=((Number)m.get("overper_min")).intValue();
					overnum_min=((Number)m.get("overnum_min")).intValue();
					overlevel_min=((Number)m.get("overlevel_min")).intValue();
					reinoverlevel_min=((Number)m.get("reinoverlevel_min")).intValue();
				}
			}
		}
		return "editPort";
	}
	
	
	/**
	 * 删除单个端口
	 * @return
	 * @throws Exception
	 */
	public String delPort() throws Exception{
		cfb.setSlb(getSysBean());
		ajax=String.valueOf(cfb.delFluxPort(device_id, port_info));
		return "ajax";
	}
	
	/**
	 * 获取配置端口列表
	 * @return
	 * @throws Exception
	 */
	public String getConfigPortList() throws Exception{
			cfgPortList=cfd.getConfigPortList(device_id);
			return "portList";
	}
	
	/**
	 * 获取设备端口信息
	 * @return
	 * @throws Exception
	 */
	public String getDevicePort() throws Exception{
		if(auto==1){//自动配置
			//因为不同版本、计数器的端口信息OID都相同，故默认取v2版本的64位计数器
			List<PortJudgeAttr> oiList =cfb.getOIDList(serial,"2","64","2");
			portList= cfb.getPortBaseInfo(oiList,device_id,serial);
		    //这边默认给v2版本64位计数器的性能OID，不是很正确
			portInfoList=cfb.getOIDList(serial,"2","64","1");
		}else{//自定义版本
			 String snmpversion = coltype.split("_")[0];
		     String counternum = coltype.split("_")[1];
		     List<PortJudgeAttr> oiList =cfb.getOIDList(serial,snmpversion,counternum,"2");
		     portList= cfb.getPortBaseInfo(oiList,device_id,serial);
		    //这边默认给v2版本64位计数器的性能OID，不是很正确
			portInfoList=cfb.getOIDList(serial,snmpversion,counternum,"1");
		}
		return "devicePort";
	}
	
	public String getDevPortStr() throws Exception{
		getDevicePort();
		StringBuilder sb_info=new StringBuilder();
		//封装采集参数
		int n=1;
		sb_info.append("<table width='100%' border='0' cellpadding='0' cellspacing='0'><tr>");
		for(PortJudgeAttr pj:portInfoList){
			sb_info.append("<td><input type=\"checkbox\" checked value='").append(pj.getOrder()).append("'>").append(pj.getDesc()).append("</td>");
			if(n%4==0){
				sb_info.append("</tr><tr>");
			}
			n++;
		}
		String _info=sb_info.toString();
		if(_info.endsWith("<tr>")){
			_info=_info.substring(0,_info.length()-4);
		}else{
			_info+="</tr>";
		}
		_info+="</table>";
		//封装AJAX
		StringBuilder sb=new StringBuilder();
		for(FluxPortInfo fp:portList){
			sb.append("<tr><td><input type='checkbox' name='chk' value='").append(fp.getIfindex()).append("|||")
			                                                              .append(fp.getIfdescr()).append("|||")
			                                                              .append(fp.getIfname() ).append("|||")
			                                                              .append(fp.getIfnamedefined()).append("|||")
			                                                              .append(fp.getIfportip()).append("|||")
			                                                              .append(fp.getGetway()).append("|||")
			                                                              .append(fp.getPort_info()).append("'></td>")
			  .append("<td>端口IP:").append(fp.getIfportip()).append("<br>")
			  .append("端口描述:").append(fp.getIfdescr()).append("<br>")
			  .append("端口名字").append(fp.getIfname()).append("<br>")
			  .append("端口别名").append(fp.getIfnamedefined()).append("<br>")
			  .append("端口索引").append(fp.getIfindex()).append("<br></td>")
			  .append("<td>").append(_info).append("</td></tr>");
		}
		ajax=sb.toString();
		sb=null;
		sb_info=null;
		_info=null;
		return "ajax";
	}
	
	/**
	 * 获取告警页面
	 * @return
	 * @throws Exception
	 */
	public String getWarnJSP() throws Exception{
		return "Warn";
	}
	
	/**
	 * 查询设备配置情况【批量配置时使用】 
	 * @return
	 * @throws Exception
	 */
	public String getConfigResult() throws Exception{
		configList=cfb.getDevConfigResult(dualDeviceId(device_id));
		StringBuilder sb=new StringBuilder();
		int n=1;
		for(Map m:configList){
			sb.append("<tr class='").append((n%2==0)?"even":"odd")
			      .append("onmouseover=\"className='odd'\"")
			      .append("onmouseout=\"className='").append((n%2==0)?"even":"odd").append("'\">")
			      .append("<td>").append(m.get("device_name")).append("</td>")
			      .append("<td>").append(m.get("loopback_ip")).append("</td>")
			      .append("<td>").append(m.get("vendor_name")).append("</td>")
			      .append("<td>").append(m.get("device_model")).append("</td>");
			 if(("true").equals(m.get("result"))){
				 sb.append("<td>已配置</td><td><a href='#'>刷新配置</a>&nbsp;&nbsp;<a href='#'>端口列表</a>&nbsp;&nbsp;<a href='#'>删除配置</a></td></tr>");
			 }else{
				 sb.append("<td class='no'>未配置</td><td><a href='#'>配置流量</a></td></tr>");
			 }
		}
		ajax=sb.toString();
		return "ajax";
	}
	
	/**
	 * 创建告警实例
	 * @return
	 */
	private Flux_Map_Instance setWarnInit(){
		Flux_Map_Instance fmi=new Flux_Map_Instance(ifinoct_maxtype,ifinoctetsbps_max,ifoutoct_maxtype,ifoutoctetsbps_max,
				ifindiscardspps_max,ifoutdiscardspps_max,ifinerrorspps_max,ifouterrorspps_max,warningnum,warninglevel,reinstatelevel,
				ifinoct_mintype,ifinoctetsbps_min,ifoutoct_mintype,ifoutoctetsbps_min,warningnum_min,warninglevel_min,reinlevel_min,
				overmax,overper,overnum,overlevel,reinoverlevel,overmin,overper_min,overnum_min,overlevel_min,reinoverlevel_min,com_day,
				intbflag,ifinoctets,inoperation,inwarninglevel,inreinstatelevel,outtbflag,ifoutoctets,outoperation,outwarninglevel,outreinstatelevel);
		return fmi;
	}
	/**
	 * 组装设备ID
	 * @param dev_id
	 * @return
	 */
	private List<String> dualDeviceId(String dev_id){
		String[] did=dev_id.split(",");
		int n=did.length;
		List<String> list=new ArrayList<String>(n);
		for(int i=0;i<n;i++){
			list.add(did[i]);
		}
		return list;
	}
	
	public String getAjax() {
		return ajax;
	}

	public void setAjax(String ajax) {
		this.ajax = ajax;
	}

	public boolean isIsbatch() {
		return isbatch;
	}

	public void setIsbatch(boolean isbatch) {
		this.isbatch = isbatch;
	}

	public String getSerial() {
		return serial;
	}

	public void setSerial(String serial) {
		this.serial = serial;
	}

	public ConfigFluxBio getCfb() {
		return cfb;
	}

	public void setCfb(ConfigFluxBio cfb) {
		this.cfb = cfb;
	}

	public boolean isConfigresult() {
		return configresult;
	}

	public void setConfigresult(boolean configresult) {
		this.configresult = configresult;
	}

	public String getDevice_id() {
		return device_id;
	}

	public void setDevice_id(String device_id) {
		this.device_id = device_id;
	}

	public boolean isIsmodule() {
		return ismodule;
	}

	public void setIsmodule(boolean ismodule) {
		this.ismodule = ismodule;
	}

	public String getDevice_name() {
		return device_name;
	}

	public void setDevice_name(String device_name) {
		this.device_name = device_name;
	}

	public String getLoopback_ip() {
		return loopback_ip;
	}

	public void setLoopback_ip(String loopback_ip) {
		this.loopback_ip = loopback_ip;
	}

	public int getAuto() {
		return auto;
	}

	public void setAuto(int auto) {
		this.auto = auto;
	}

	public String getColtype() {
		return coltype;
	}

	public void setColtype(String coltype) {
		this.coltype = coltype;
	}

	public static long getSerialVersionUID() {
		return serialVersionUID;
	}

	public int getTotal() {
		return total;
	}

	public void setTotal(int total) {
		this.total = total;
	}

	public int getPolltime() {
		return polltime;
	}

	public void setPolltime(int polltime) {
		this.polltime = polltime;
	}

	public int getIntodb() {
		return intodb;
	}

	public void setIntodb(int intodb) {
		this.intodb = intodb;
	}

	public boolean isIskeep() {
		return iskeep;
	}

	public void setIskeep(boolean iskeep) {
		this.iskeep = iskeep;
	}

	public ConfigFluxDao getCfd() {
		return cfd;
	}

	public void setCfd(ConfigFluxDao cfd) {
		this.cfd = cfd;
	}

	public List<Map> getConfigList() {
		return configList;
	}

	public void setConfigList(List<Map> configList) {
		this.configList = configList;
	}

	public int getIfinoct_maxtype() {
		return ifinoct_maxtype;
	}

	public void setIfinoct_maxtype(int ifinoct_maxtype) {
		this.ifinoct_maxtype = ifinoct_maxtype;
	}

	public float getIfinoctetsbps_max() {
		return ifinoctetsbps_max;
	}

	public void setIfinoctetsbps_max(float ifinoctetsbps_max) {
		this.ifinoctetsbps_max = ifinoctetsbps_max;
	}

	public int getIfoutoct_maxtype() {
		return ifoutoct_maxtype;
	}

	public void setIfoutoct_maxtype(int ifoutoct_maxtype) {
		this.ifoutoct_maxtype = ifoutoct_maxtype;
	}

	public float getIfoutoctetsbps_max() {
		return ifoutoctetsbps_max;
	}

	public void setIfoutoctetsbps_max(float ifoutoctetsbps_max) {
		this.ifoutoctetsbps_max = ifoutoctetsbps_max;
	}

	public float getIfindiscardspps_max() {
		return ifindiscardspps_max;
	}

	public void setIfindiscardspps_max(float ifindiscardspps_max) {
		this.ifindiscardspps_max = ifindiscardspps_max;
	}

	public float getIfoutdiscardspps_max() {
		return ifoutdiscardspps_max;
	}

	public void setIfoutdiscardspps_max(float ifoutdiscardspps_max) {
		this.ifoutdiscardspps_max = ifoutdiscardspps_max;
	}

	public float getIfinerrorspps_max() {
		return ifinerrorspps_max;
	}

	public void setIfinerrorspps_max(float ifinerrorspps_max) {
		this.ifinerrorspps_max = ifinerrorspps_max;
	}

	public float getIfouterrorspps_max() {
		return ifouterrorspps_max;
	}

	public void setIfouterrorspps_max(float ifouterrorspps_max) {
		this.ifouterrorspps_max = ifouterrorspps_max;
	}

	public int getWarningnum() {
		return warningnum;
	}

	public void setWarningnum(int warningnum) {
		this.warningnum = warningnum;
	}

	public int getWarninglevel() {
		return warninglevel;
	}

	public void setWarninglevel(int warninglevel) {
		this.warninglevel = warninglevel;
	}

	public int getReinstatelevel() {
		return reinstatelevel;
	}

	public void setReinstatelevel(int reinstatelevel) {
		this.reinstatelevel = reinstatelevel;
	}

	public int getIfinoct_mintype() {
		return ifinoct_mintype;
	}

	public void setIfinoct_mintype(int ifinoct_mintype) {
		this.ifinoct_mintype = ifinoct_mintype;
	}

	public float getIfinoctetsbps_min() {
		return ifinoctetsbps_min;
	}

	public void setIfinoctetsbps_min(float ifinoctetsbps_min) {
		this.ifinoctetsbps_min = ifinoctetsbps_min;
	}

	public int getIfoutoct_mintype() {
		return ifoutoct_mintype;
	}

	public void setIfoutoct_mintype(int ifoutoct_mintype) {
		this.ifoutoct_mintype = ifoutoct_mintype;
	}

	public float getIfoutoctetsbps_min() {
		return ifoutoctetsbps_min;
	}

	public void setIfoutoctetsbps_min(float ifoutoctetsbps_min) {
		this.ifoutoctetsbps_min = ifoutoctetsbps_min;
	}

	public int getWarningnum_min() {
		return warningnum_min;
	}

	public void setWarningnum_min(int warningnum_min) {
		this.warningnum_min = warningnum_min;
	}

	public int getWarninglevel_min() {
		return warninglevel_min;
	}

	public void setWarninglevel_min(int warninglevel_min) {
		this.warninglevel_min = warninglevel_min;
	}

	public int getReinlevel_min() {
		return reinlevel_min;
	}

	public void setReinlevel_min(int reinlevel_min) {
		this.reinlevel_min = reinlevel_min;
	}

	public int getOvermax() {
		return overmax;
	}

	public void setOvermax(int overmax) {
		this.overmax = overmax;
	}

	public float getOverper() {
		return overper;
	}

	public void setOverper(float overper) {
		this.overper = overper;
	}

	public int getOvernum() {
		return overnum;
	}

	public void setOvernum(int overnum) {
		this.overnum = overnum;
	}

	public int getOverlevel() {
		return overlevel;
	}

	public void setOverlevel(int overlevel) {
		this.overlevel = overlevel;
	}

	public int getReinoverlevel() {
		return reinoverlevel;
	}

	public void setReinoverlevel(int reinoverlevel) {
		this.reinoverlevel = reinoverlevel;
	}

	public int getOvermin() {
		return overmin;
	}

	public void setOvermin(int overmin) {
		this.overmin = overmin;
	}

	public float getOverper_min() {
		return overper_min;
	}

	public void setOverper_min(float overper_min) {
		this.overper_min = overper_min;
	}

	public int getOvernum_min() {
		return overnum_min;
	}

	public void setOvernum_min(int overnum_min) {
		this.overnum_min = overnum_min;
	}

	public int getOverlevel_min() {
		return overlevel_min;
	}

	public void setOverlevel_min(int overlevel_min) {
		this.overlevel_min = overlevel_min;
	}

	public int getReinoverlevel_min() {
		return reinoverlevel_min;
	}

	public void setReinoverlevel_min(int reinoverlevel_min) {
		this.reinoverlevel_min = reinoverlevel_min;
	}

	public int getCom_day() {
		return com_day;
	}

	public void setCom_day(int com_day) {
		this.com_day = com_day;
	}

	public int getIntbflag() {
		return intbflag;
	}

	public void setIntbflag(int intbflag) {
		this.intbflag = intbflag;
	}

	public float getIfinoctets() {
		return ifinoctets;
	}

	public void setIfinoctets(float ifinoctets) {
		this.ifinoctets = ifinoctets;
	}

	public int getInoperation() {
		return inoperation;
	}

	public void setInoperation(int inoperation) {
		this.inoperation = inoperation;
	}

	public int getInwarninglevel() {
		return inwarninglevel;
	}

	public void setInwarninglevel(int inwarninglevel) {
		this.inwarninglevel = inwarninglevel;
	}

	public int getInreinstatelevel() {
		return inreinstatelevel;
	}

	public void setInreinstatelevel(int inreinstatelevel) {
		this.inreinstatelevel = inreinstatelevel;
	}

	public int getOuttbflag() {
		return outtbflag;
	}

	public void setOuttbflag(int outtbflag) {
		this.outtbflag = outtbflag;
	}

	public float getIfoutoctets() {
		return ifoutoctets;
	}

	public void setIfoutoctets(float ifoutoctets) {
		this.ifoutoctets = ifoutoctets;
	}

	public int getOutoperation() {
		return outoperation;
	}

	public void setOutoperation(int outoperation) {
		this.outoperation = outoperation;
	}

	public int getOutwarninglevel() {
		return outwarninglevel;
	}

	public void setOutwarninglevel(int outwarninglevel) {
		this.outwarninglevel = outwarninglevel;
	}

	public int getOutreinstatelevel() {
		return outreinstatelevel;
	}

	public void setOutreinstatelevel(int outreinstatelevel) {
		this.outreinstatelevel = outreinstatelevel;
	}

	public List<FluxPortInfo> getPortList() {
		return portList;
	}

	public void setPortList(List<FluxPortInfo> portList) {
		this.portList = portList;
	}

	public List<PortJudgeAttr> getPortInfoList() {
		return portInfoList;
	}

	public void setPortInfoList(List<PortJudgeAttr> portInfoList) {
		this.portInfoList = portInfoList;
	}

	public List<Integer> getGetWayList() {
		return getWayList;
	}

	public void setGetWayList(List<Integer> getWayList) {
		this.getWayList = getWayList;
	}

	public List<Map<String, String>> getCfgPortList() {
		return cfgPortList;
	}

	public void setCfgPortList(List<Map<String, String>> cfgPortList) {
		this.cfgPortList = cfgPortList;
	}

	public String getPort_info() {
		return port_info;
	}

	public void setPort_info(String port_info) {
		this.port_info = port_info;
	}

	public String getIfindex() {
		return ifindex;
	}

	public void setIfindex(String ifindex) {
		this.ifindex = ifindex;
	}

	public String getIfdescr() {
		return ifdescr;
	}

	public void setIfdescr(String ifdescr) {
		this.ifdescr = ifdescr;
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

	public int getIftype() {
		return iftype;
	}

	public void setIftype(int iftype) {
		this.iftype = iftype;
	}

	public int getIfspeed() {
		return ifspeed;
	}

	public void setIfspeed(int ifspeed) {
		this.ifspeed = ifspeed;
	}

	public int getIfmtu() {
		return ifmtu;
	}

	public void setIfmtu(int ifmtu) {
		this.ifmtu = ifmtu;
	}

	public int getIfhighspeed() {
		return ifhighspeed;
	}

	public void setIfhighspeed(int ifhighspeed) {
		this.ifhighspeed = ifhighspeed;
	}

	public int getGatherflag() {
		return gatherflag;
	}

	public void setGatherflag(int gatherflag) {
		this.gatherflag = gatherflag;
	}
	public String getDev_id() {
		return dev_id;
	}
	public void setDev_id(String dev_id) {
		this.dev_id = dev_id;
	}
}
