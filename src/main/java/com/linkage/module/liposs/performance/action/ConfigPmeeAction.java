package com.linkage.module.liposs.performance.action;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.linkage.module.gwms.dao.tabquery.CityDAO;
import com.linkage.module.liposs.performance.bio.I_configPmeeBio;
import com.linkage.module.liposs.performance.bio.Pm_Map_Instance;
import com.linkage.module.liposs.performance.dao.I_configPmeeDao;
import com.linkage.module.liposs.system.basesupport.BaseSupportAction;
/**
 * 设备配置PMEE
 * 单个设备配置时先检查该设备是否已经配置过，如果已经配置则提示用户是否继续配置，继续配置则删除以前的配置项
 * 多个设备配置时不检查设备是否已经配置过，直接删除设备的配置项。
 * @author BENYP【5260】 E-MAIL:BENYP@LIANCHUANG.COM
 * @version 1.0
 * @since 2008-09-25<br>
 * ***********************************************修改记录******************************************************************************************
 * 序号     时间       修改人        需求&BUG单号      						增加、修改的方法            修改内容                                   备注        
 * ------------------------------------------------------------------------------------------------------------------------------------------------
 *  1   2008-10-17  BENYP(5260)                  					增加ChangeInterval()    贵州要求增加修改采集时间间隔，                                
 *                                               					修改getConfigExp()方法   同时在查询配置结果时将采集时间间隔显示                         
 * ------------------------------------------------------------------------------------------------------------------------------------------------                            
 *  2   2008-10-17  BENYP(5260)                  					修改execute()           和北京酒店网管模板融合，增加标志位ismodule,                    
 *                                                                  					    如果是则查询性能表达式时也要查询定制的性能表达式                  
 * ------------------------------------------------------------------------------------------------------------------------------------------------
 *  3   2008-10-22  BENYP(5260)                                     					    增加日志功能
 *  -----------------------------------------------------------------------------------------------------------------------------------------------
 *  4   2008-11-4   BENYP(5260)                  					修改getDevByNameIP      增加批量选择设备时配置流量的功能
 *                                                  				getDevByModel                                            
 *  -----------------------------------------------------------------------------------------------------------------------------------------------
 *  5   2008-12-1   BENYP(5260) JSDX_JS-REQ-20080523-zhur-001       修改execute()方法        对性能表达式进行排序               
 * ************************************************************************************************************************************************
 * 
 */
public class ConfigPmeeAction extends BaseSupportAction {
	private static final long serialVersionUID = -384269947599019678L;
	private I_configPmeeDao cpd;//DAO
	private I_configPmeeBio cpb;//BIO
	private boolean pmeeflg=true;//是否是性能配置 true：性能配置 false 流量配置
	//************************批量配置页面参数****************//
	private String device_name;//设备名称
	private String loopback_ip;//设备IP
	private String city_id=null;//属地
	private String vendor_id=null;//厂商
	private String device_model=null;//设备型号
	private List<Map<String,String>> CityList;
	private List<Map<String,String>> VendorList;
	//************************变量**************************//
	private boolean iskeep=true;//是否保留原有配置 true：保留 false：不保留
	private boolean isbatch=false;//是否批量配置 false:单个设备 true:多个设备批量配置 
	private String device_id=null;//设备列表【以，分隔】
	private String expressionid=null;//性能表达式列表【以，分隔】
	private List<Map> ExpList;//性能表达式列表
	private List<Map> ExpListUse;//已经定制的性能表达式列表
	private List<Map> ConfigResultList;//配置结果列表
	private Map DevInfoMap=null;//设备信息Map【存放设备名称、设备IP等信息】【单个设备配置时使用】
	private List<Map<String,String>> ConfigData=null;//配置结果列表【单个设备配置时使用】
	private int interval=300;//采集时间间隔【默认300秒】
	private int intodb=0;//是否入库 0:否 1：是
	private String ajax=null;
	private String type=null;//删除标志位 all删除所有实例 one 删除单个实例
	private String id=null;//单个实例ID
	//*********************固定阀值******************
	private int mintype=0;//固定阈值一类型【比较操作符一】 0:不使用 
	private String mindesc=null;//固定阈值一描述
	private String minthres=null;//固定阈值一
	private int mincount=1;//连续超出阈值一次数
	private int minwarninglevel=0;//固定阈值一告警级别
	private int minreinstatelevel=0;//固定阈值一恢复告警级别
	private int maxtype=0;//固定阈值二类型
	private String maxdesc=null;//固定阈值二描述
	private String maxthres=null;//固定阈值二
	private int maxcount=1;//连续超出阈值二次数
	private int maxwarninglevel=0;//固定阈值二告警级别
	private int maxreinstateleve=0;//固定阈值二恢复告警级别
	//***************动态阀值**********************
	private int dynatype=0;//是否启动动态阈值类型
	private String dynadesc=null;//	动态阈值描述
	private int dynacount=1;//连续超出动态阈值次数
	private int beforeday=1;//几天前的数据为基准值
	private String dynathres=null;//动态阈值
	private int dynawarninglevel=0;//动态阈值告警级别
	private int dynareinstatelevel=0;//动态阈值告警恢复告警级别
	//*****************突变阀值********************
	private int mutationtype=0;//是否启动突变阈值类型
	private String mutationthres=null;//突变阈值
	private String mutationdesc=null;//突变阈值描述
	private int mutationcount=1;//连续超出突变阈值次数
	private int mutationwarninglevel=0;//突变阈值告警级别
	//**********************************************
	private boolean ismodule=false;//是否是北京模板配置 默认false
	
	private String gw_type = null; // 系统类型
	//**********************以下是方法***********************//
	
	/**
	 * 显示性能、流量批量配置选择设备页面
	 * @return
	 */
	public String BatchConfig() throws Exception{
		CityList=CityDAO.getAllNextCityListByCityPid(getUser().getCityId());
		VendorList=cpd.getVendorList();
		return "batch";
	}
	
	/**
	 * 根据设备名称或设备IP获取设备
	 * @return
	 */
	public String getDevByNameIP() throws Exception{
		List<Map<String,String>> list=cpd.getDevListByName(gw_type, device_name, loopback_ip, getUser().getAreaId(),getUser().isAdmin());
		StringBuffer sb=new StringBuffer();
		int n=1;
		String cname,dname,ip,dmodel;
		for(Map<String,String> m:list){
			cname=m.get("city_name");
			dname=m.get("device_name");
			ip=m.get("loopback_ip");
			dmodel=m.get("device_model");
			
			sb.append("<tr class=").append(n%2==0?"'even'":"'odd'").append(">")
			  .append("<td align='center'>").append("<input type='checkbox' name='chk' value='").append(m.get("device_id")).append("'>").append("</td>")
			  .append("<td>").append(cname==null?"":cname).append("</td>")
			  .append("<td>").append(dname==null?"":dname).append("</td>")
			  .append("<td>").append(ip==null?"":ip).append("</td>")
			  .append("<td>").append(dmodel==null?"":dmodel).append("</td>")
			  .append("<td align='center'> ");
			if(pmeeflg){
				sb.append("<a href='#' onclick=\"ConSPmee('").append(m.get("device_id")).append("')\">配置性能</a>");
			}else{
				sb.append("<a href='#' onclick=\"ConSFlux('").append(m.get("device_id")).append("')\">配置流量</a>");
			}
			  sb.append("</td>").append("</tr>");
			  n++;
		}
		ajax=sb.toString();
		return "ajax";
	}
	
	/**
	 * 根据厂商获取设备型号
	 * @return
	 */
	public String getDevModelByVendor(){
		ajax="";
		List<Map<String,String>> data=cpd.getDeviceModelByVendor(vendor_id);
		for(Map<String,String> m:data){
			ajax+="<option value='"+m.get("device_model_id")+"'>"+m.get("device_model")+"</option>";
		}
		return "ajax";
	}
	
	/**
	 * 根据设备型号获取设备
	 * @return
	 */
	public String getDevByModel() throws Exception{
		List<Map<String,String>> list=cpd.getDeviceList(gw_type, city_id, vendor_id, device_model,getUser().getAreaId(),getUser().isAdmin());
		StringBuffer sb=new StringBuffer();
		int n=1;
		String cname,dname,ip,dmodel;
		for(Map<String,String> m:list){
			cname=m.get("city_name");
			dname=m.get("device_name");
			ip=m.get("loopback_ip");
			dmodel=m.get("device_model");
			
			sb.append("<tr class=").append(n%2==0?"'even'":"'odd'").append(">")
			  .append("<td align='center'>").append("<input type='checkbox' name='chk' value='").append(m.get("device_id")).append("'>").append("</td>")
			  .append("<td align='center'>").append(cname==null?"":cname).append("</td>")
			  .append("<td align='center'>").append(dname==null?"":dname).append("</td>")
			  .append("<td align='center'>").append(ip==null?"":ip).append("</td>")
			  .append("<td align='center'>").append(dmodel==null?"":dmodel).append("</td>")
			  .append("<td align='center'> ");
			if(pmeeflg){
				sb.append("<a href='#' onclick=\"ConSPmee('").append(m.get("device_id")).append("')\">性能配置</a>");
			}else{
				sb.append("<a href='#' onclick=\"ConSFlux('").append(m.get("device_id")).append("')\">流量配置</a>");
			}
			  sb.append("</td>")
			  .append("</tr>");
			n++;
		}
		ajax=sb.toString();
		return "ajax";
	}
	
	//*****************************以下是性能配置功能**********************************//
	
	/**
	 * 初始化
	 */
	public String execute() throws Exception {
		List<Map> list;
		if(isbatch){
			String vendor_id=cpd.getVendorID(device_id);
			list=cpd.getExpressList(vendor_id,"-1",false);
		}else{
			DevInfoMap=cpd.getDevInfo(device_id);//获取
			if(ismodule){//模板配置
				list=cpd.getExpressList(DevInfoMap.get("vendor_id")+"",expressionid,false);
				ExpListUse=cpd.getExpressList(DevInfoMap.get("vendor_id")+"",expressionid,true);
			}else{
				list=cpd.getExpressList(DevInfoMap.get("vendor_id")+"","-1",false);
			}
			ConfigResultList=cpd.getConfigResultList(device_id);
		}
		sortExpList(list);
		list=null;
		return SUCCESS;
	}
	/**
	 * 对性能表达式进行排序
	 * REQ:JSDX_JS-REQ-20080523-zhur-001
	 * 要求打*的表达式排在上面(也要做排序)，其余按字母先后排序
	 */
	private void sortExpList(List<Map> list){
		List<String> startList=new ArrayList<String>();
		List<String> normalList=new ArrayList<String>();
		Map<String,Map> _map=new HashMap<String,Map>();
		String _name="";
		ExpList=new ArrayList<Map>();
		for(Map m:list){
			_name=""+m.get("name");
			_map.put(_name,m);
			if(_name.indexOf("*")==0){
				startList.add(_name);
			}else{
				normalList.add(_name);
			}
		}
		Collections.sort(startList);
		Collections.sort(normalList);
		for(String s:startList){
			ExpList.add(_map.get(s));
		}
		
		for(String s:normalList){
			ExpList.add(_map.get(s));
		}
		//clear
		list=null;
		startList=null;
		normalList=null;
		_map=null;
		_name=null;
	} 
	
	/**
	 * 配置告警【打开配置告警页面】
	 * @return
	 */
	public String ConfigWarn(){
		return "Warn";
	}
	
	/**
	 * 保存配置告警【仅保存数据库，通知后台，不需走采集流程,即：只和PMEE相关，和SnmpGather无关】
	 * @return
	 */
	public String SaveWarn() throws Exception{
		Pm_Map_Instance pm=setPM();//初始化PM实例
		ajax=String.valueOf(cpb.SaveWarn(device_id, expressionid, pm,getSysBean()));
		return "Warn";
	}
	/**
	 * 获取已经分配的性能表达式
	 * @return
	 */
	public String getConfigExp(){
		ConfigResultList=cpd.getConfigResultList(device_id);
		StringBuffer sb=new StringBuffer();
		String tmp;
		for(Map m:ConfigResultList){
			tmp=(String)m.get("remark");
			tmp=tmp==null||tmp.equals("null")?"":tmp;
			sb.append("<tr style='color:").append(((Number)(m.get("isok"))).intValue()<1 ?"red":"").append("'>")
			  .append("<td>").append(m.get("name")).append("</td>")
			  .append("<td>").append(m.get("descr")).append("</td>")
			  .append("<td>").append("<label onclick=$.showT($(this));>").append(m.get("interval")).append("</label>")
			  .append("<input type='text' size='8' style='display:none;' onmouseout=$.MO('")
			  .append(m.get("interval")).append("',$(this)) onchange=$.changeT('")
			  .append(m.get("interval")).append("','").append(m.get("expressionid")).append("','").append(m.get("device_id")).append("',$(this)) value=").append(m.get("interval")).append(" >")
			  .append("</td>")
			  .append("<td>").append(m.get("result")).append("</td>")
			  .append("<td>").append(tmp).append("</td>")
			  .append("<td>")
			  .append("<a href='#' onclick=\"showDetail('").append(m.get("device_id")).append("','").append(m.get("device_name")).append("','").append(m.get("loopback_ip")).append("','").append(m.get("expressionid")).append("');\">详细</a>&nbsp;")
			  .append("<a href='#' onclick=\"RefreshExp('").append(m.get("expressionid")).append("')\">刷新</a>&nbsp;")
			  .append("<a href='#' onclick=\"ConfigWarn('").append(m.get("expressionid")).append("')\">配置告警</a>&nbsp;")
			  .append("<a href='#' onclick=\"Del('").append(m.get("device_id")).append("','").append(m.get("expressionid")).append("',$(this))\">删除</a>&nbsp;")
			  .append("</td>")
			  .append("</tr>");
		}
		ajax=sb.toString();
		return "ajax";
	}

	/**
	 * 修改采集时间间隔
	 * @return
	 */
	public String ChangeInterval() throws Exception{
		ajax=String.valueOf(cpb.ChangeInterval(String.valueOf(interval), expressionid, device_id,getSysBean()));
		return "ajax";
	}
	/**
	 * 检查是否已经配置:如果没有配置则返回空字符【“”】，否则返回配置的设备
	 * @return
	 */
	public String CheckConfig(){
		ajax=cpb.getConfigExp(device_id, expressionid);
		return "ajax";
	}
	
	/**
	 * 显示单个设备，单个实例的详细信息
	 * @return
	 * @throws Exception
	 */
	public String showDetail() throws Exception{
		ajax=null;
		String deviceid=device_id.contains("'")?device_id:("'"+device_id+"'");
		ConfigData=cpd.getConfigedExpInfo(deviceid, expressionid);
		return "detail";
	}
	/**
	 * 删除单个设备的实例
	 * @param device_id:设备ID
	 * @param expressionid：性能表达式ID
	 * @param id:单个实例ID【如果type为all则ID为null】
	 * @param type: all:删除该设备该性能表达式的所有实例
	 *              one:删除该设备该性能表达式的单个实例【如果为最后一个实例则删除pm_map中的数据】
	 * @return true:删除成功
	 *         false：删除失败
	 * @throws Exception
	 */
	public String delExp() throws Exception{
		ajax=String.valueOf(cpb.DelPmeeExpression(device_id, expressionid, id, type,getSysBean()));
		return "ajax";
	}
	/**
	 * 编辑单个实例
	 * @return
	 */
	public String editPxp() throws Exception{
		Pm_Map_Instance pm=setPM();//初始化PM实例
		ajax=String.valueOf(cpb.EditPmeeExpression(device_id, expressionid, id, pm,getSysBean()));
		String deviceid=device_id.contains("'")?device_id:("'"+device_id+"'");
		ConfigData=cpd.getConfigedExpInfo(deviceid, expressionid);
		return "detail";
	}
	/**
	 * 批量配置【刷新功能也是这个方法】
	 * @return
	 * @throws Exception
	 */
	public String Config() throws Exception{
		Pm_Map_Instance pm=setPM();//初始化PM实例
		ajax=String.valueOf(cpb.ConfigPmeeDB(device_id, expressionid,pm,getSysBean()));
		execute();
		return SUCCESS;
	}
	/**
	 * 生成PM实例对象
	 * @return
	 */
	private Pm_Map_Instance setPM(){
		minthres=(minthres==null || "".equals(minthres))?"0":minthres;
		maxthres=(maxthres==null || "".equals(maxthres))?"0":maxthres;
		dynathres=(dynathres==null || "".equals(dynathres))?"0":dynathres;
		mutationthres=(mutationthres==null || "".equals(mutationthres))?"0":mutationthres;
		
		Pm_Map_Instance pm=new Pm_Map_Instance(interval,intodb,mintype,mindesc,
				  Float.parseFloat(minthres),mincount,minwarninglevel,
				  minreinstatelevel,maxtype,maxdesc,
				  Float.parseFloat(maxthres),maxcount,maxwarninglevel,
				  maxreinstateleve,dynatype,dynadesc,
				  dynacount,beforeday,Float.parseFloat(dynathres),
				  dynawarninglevel,dynareinstatelevel,
				  mutationtype,Float.parseFloat(mutationthres),mutationdesc,mutationcount,mutationwarninglevel,
				  0);
		pm.setIskeep(iskeep);
		return pm;
	}
	//*******************************************************//
	public void setCpd(I_configPmeeDao cpd) {
		this.cpd = cpd;
	}

	public void setCpb(I_configPmeeBio cpb) {
		this.cpb = cpb;
	}
	public boolean isIsbatch() {
		return isbatch;
	}
	public void setIsbatch(boolean isbatch) {
		this.isbatch = isbatch;
	}
	public String getDevice_id() {
		return device_id;
	}
	public void setDevice_id(String device_id) {
		this.device_id = device_id;
	}
	public String getExpressionid() {
		return expressionid;
	}
	public void setExpressionid(String expressionid) {
		this.expressionid = expressionid;
	}
	public Map getDevInfoMap() {
		return DevInfoMap;
	}
	public void setDevInfoMap(Map devInfoMap) {
		DevInfoMap = devInfoMap;
	}
	public List<Map<String, String>> getConfigData() {
		return ConfigData;
	}
	public void setConfigData(List<Map<String, String>> configData) {
		ConfigData = configData;
	}
	public int getInterval() {
		return interval;
	}
	public void setInterval(int interval) {
		this.interval = interval;
	}
	public int getIntodb() {
		return intodb;
	}
	public void setIntodb(int intodb) {
		this.intodb = intodb;
	}
	public int getMintype() {
		return mintype;
	}
	public void setMintype(int mintype) {
		this.mintype = mintype;
	}
	public String getMindesc() {
		return mindesc;
	}
	public void setMindesc(String mindesc) {
		this.mindesc = mindesc;
	}
	public String getMinthres() {
		return minthres;
	}
	public void setMinthres(String minthres) {
		this.minthres = minthres;
	}
	public int getMincount() {
		return mincount;
	}
	public void setMincount(int mincount) {
		this.mincount = mincount;
	}
	public int getMinwarninglevel() {
		return minwarninglevel;
	}
	public void setMinwarninglevel(int minwarninglevel) {
		this.minwarninglevel = minwarninglevel;
	}
	public int getMinreinstatelevel() {
		return minreinstatelevel;
	}
	public void setMinreinstatelevel(int minreinstatelevel) {
		this.minreinstatelevel = minreinstatelevel;
	}
	public int getMaxtype() {
		return maxtype;
	}
	public void setMaxtype(int maxtype) {
		this.maxtype = maxtype;
	}
	public String getMaxdesc() {
		return maxdesc;
	}
	public void setMaxdesc(String maxdesc) {
		this.maxdesc = maxdesc;
	}
	public String getMaxthres() {
		return maxthres;
	}
	public void setMaxthres(String maxthres) {
		this.maxthres = maxthres;
	}
	public int getMaxcount() {
		return maxcount;
	}
	public void setMaxcount(int maxcount) {
		this.maxcount = maxcount;
	}
	public int getMaxwarninglevel() {
		return maxwarninglevel;
	}
	public void setMaxwarninglevel(int maxwarninglevel) {
		this.maxwarninglevel = maxwarninglevel;
	}
	public int getMaxreinstateleve() {
		return maxreinstateleve;
	}
	public void setMaxreinstateleve(int maxreinstateleve) {
		this.maxreinstateleve = maxreinstateleve;
	}
	public int getDynatype() {
		return dynatype;
	}
	public void setDynatype(int dynatype) {
		this.dynatype = dynatype;
	}
	public String getDynadesc() {
		return dynadesc;
	}
	public void setDynadesc(String dynadesc) {
		this.dynadesc = dynadesc;
	}
	public int getDynacount() {
		return dynacount;
	}
	public void setDynacount(int dynacount) {
		this.dynacount = dynacount;
	}
	public int getBeforeday() {
		return beforeday;
	}
	public void setBeforeday(int beforeday) {
		this.beforeday = beforeday;
	}
	public String getDynathres() {
		return dynathres;
	}
	public void setDynathres(String dynathres) {
		this.dynathres = dynathres;
	}
	public int getDynawarninglevel() {
		return dynawarninglevel;
	}
	public void setDynawarninglevel(int dynawarninglevel) {
		this.dynawarninglevel = dynawarninglevel;
	}
	public int getDynareinstatelevel() {
		return dynareinstatelevel;
	}
	public void setDynareinstatelevel(int dynareinstatelevel) {
		this.dynareinstatelevel = dynareinstatelevel;
	}
	public int getMutationtype() {
		return mutationtype;
	}
	public void setMutationtype(int mutationtype) {
		this.mutationtype = mutationtype;
	}
	public String getMutationthres() {
		return mutationthres;
	}
	public void setMutationthres(String mutationthres) {
		this.mutationthres = mutationthres;
	}
	public int getMutationwarninglevel() {
		return mutationwarninglevel;
	}
	public void setMutationwarninglevel(int mutationwarninglevel) {
		this.mutationwarninglevel = mutationwarninglevel;
	}
	public I_configPmeeDao getCpd() {
		return cpd;
	}
	public I_configPmeeBio getCpb() {
		return cpb;
	}
	public List<Map> getExpList() {
		return ExpList;
	}
	public void setExpList(List<Map> expList) {
		ExpList = expList;
	}
	public List<Map> getConfigResultList() {
		return ConfigResultList;
	}
	public void setConfigResultList(List<Map> configResultList) {
		ConfigResultList = configResultList;
	}
	public String getMutationdesc() {
		return mutationdesc;
	}
	public void setMutationdesc(String mutationdesc) {
		this.mutationdesc = mutationdesc;
	}
	public int getMutationcount() {
		return mutationcount;
	}
	public void setMutationcount(int mutationcount) {
		this.mutationcount = mutationcount;
	}
	public String getAjax() {
		return ajax;
	}
	public void setAjax(String ajax) {
		this.ajax = ajax;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}

	public String getCity_id() {
		return city_id;
	}

	public void setCity_id(String city_id) {
		this.city_id = city_id;
	}

	public String getVendor_id() {
		return vendor_id;
	}

	public void setVendor_id(String vendor_id) {
		this.vendor_id = vendor_id;
	}

	public String getDevice_model() {
		return device_model;
	}

	public void setDevice_model(String device_model) {
		this.device_model = device_model;
	}

	public List<Map<String, String>> getCityList() {
		return CityList;
	}

	public void setCityList(List<Map<String, String>> cityList) {
		CityList = cityList;
	}

	public List<Map<String, String>> getVendorList() {
		return VendorList;
	}

	public void setVendorList(List<Map<String, String>> vendorList) {
		VendorList = vendorList;
	}

	public boolean isPmeeflg() {
		return pmeeflg;
	}

	public void setPmeeflg(boolean pmeeflg) {
		this.pmeeflg = pmeeflg;
	}

	public String getDevice_name() {
		return device_name;
	}

	public void setDevice_name(String device_name) {
		// 通过ajax传递中文，需要将字符集合转码的。
		try
		{
			this.device_name = java.net.URLDecoder.decode(device_name, "UTF-8");
		} catch (Exception e)
		{
			this.device_name =device_name;
		}
	}

	public String getLoopback_ip() {
		return loopback_ip;
	}

	public void setLoopback_ip(String loopback_ip) {
		this.loopback_ip = loopback_ip;
	}

	public boolean isIskeep() {
		return iskeep;
	}

	public void setIskeep(boolean iskeep) {
		this.iskeep = iskeep;
	}

	public boolean isIsmodule() {
		return ismodule;
	}

	public void setIsmodule(boolean ismodule) {
		this.ismodule = ismodule;
	}

	public List<Map> getExpListUse() {
		return ExpListUse;
	}

	public void setExpListUse(List<Map> expListUse) {
		ExpListUse = expListUse;
	}

	
	public String getGw_type() {
		return gw_type;
	}

	
	public void setGw_type(String gw_type) {
		this.gw_type = gw_type;
	}

}
