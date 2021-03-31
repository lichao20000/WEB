package com.linkage.module.itms.resource.act;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.SessionAware;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import action.splitpage.splitPageAction;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.linkage.litms.LipossGlobals;
import com.linkage.litms.common.util.DateTimeUtil;
import com.linkage.module.gwms.util.StringUtil;
import com.linkage.module.itms.resource.bio.ServTemplateBIO;
import com.linkage.module.itms.resource.obj.TreeNode;
/**
 * 业务参数模板
 * @author HP (AILK No.)
 * @version 1.0
 * @since 2020-3-5
 * @category com.linkage.module.itms.resource.act
 * @copyright AILK NBS-Network Mgt. RD Dept.
 */
public class ServTemplateACT extends splitPageAction implements SessionAware,
		ServletRequestAware {

	// 日志记录
	private static Logger logger = LoggerFactory
			.getLogger(ServTemplateACT.class);
	/** 查询的所有数据列表 */

	// request取登陆帐号使用
	private HttpServletRequest request;
	private List<Map> deviceList;
	private ServTemplateBIO bio;
	private Map session;
	private String name = "";
	private String vlan = "";
	private String serv = "";
	private String update_time = "";
	private String describe = "";
	private String tree = "";
	private String checkedData;
	private int id = -1;
	//参数信息
	private ArrayList<HashMap<String, String>> tempParam = new ArrayList<HashMap<String, String>>();
	/** 厂家 */
	private int vendor = -1;
	/** 设备型号 */
	private int device_model = -1;
	/** 硬件版本 */
	private String hard_version;
	/** 软件型号 */
	private String soft_version;
	private String ajax;
	private long deviceTypeId;
	/** 开始时间 */
	private String startTime;
	/** 结束时间 */
	private String endTime;
	private String gw_type;
	private String nserv_svlan_del;
	private String service_id;
	private String sport_del;
	private String sserv_del;
	private String sserv_svlan_del;
	private String cm = "0";
	
	public String init() {
		return "index";
	}
	


	public String queryList() {
		logger.debug("queryList() begin");
		/*if (startTime != null && !"".equals(startTime)) {
			startTime = dealTime(startTime);
		}
		if (endTime != null && !"".equals(endTime)) {
			endTime = dealTime(endTime);
		}
		if (startOpenDate != null && !"".equals(startOpenDate)) {
			startOpenDate = dealTime(startOpenDate);
		}
		if (endOpenDate != null && !"".equals(endOpenDate)) {
			endOpenDate = dealTime(endOpenDate);
		} */

		deviceList = bio.queryDeviceList(name, vlan, serv, curPage_splitPage, num_splitPage, cm);
		maxPage_splitPage = bio.getMaxPage_splitPage();
		logger.warn("maxPage_splitPage"+maxPage_splitPage);	
		return "queryList";
		
	}
	
	/**
	 * 定制批量参数页面返回模板加载select用
	 * @return
	 */
	public String queryTemplateStr() {
		logger.debug("queryList() begin");
		ajax = bio.queryTemplateStr(name, vlan, serv, cm);
		return "ajax";
		
	}


	/**
	 * 解析文件并转换成TreeNode对象
	 * 根据后台数据库中已有节点进行自动勾选
	 * @return
	 */
	public String queryDetail4Edit() {
		//查询模板信息，回填到详情页面
		Map<String, String> template = bio.queryTemplate(id);
		if(null!=template){
			name = template.get("name");
			describe = template.get("describe");
			serv = template.get("serv");
			vlan = template.get("vlanid");
			update_time = new DateTimeUtil(template.get("update_time")).getLongDate();
			
			if(!StringUtil.IsEmpty(service_id)){
				service_id = template.get("service_id");//业务编码
				nserv_svlan_del = template.get("nserv_svlan_del");//释放不同业务冲突vlan的WAN连接
				sserv_del = template.get("sserv_del");//释放同业务的WAN连接
				sserv_svlan_del = template.get("sserv_svlan_del");//释放同业务冲突vlan的WAN连接
				sport_del = template.get("sport_del");//释放冲突的LAN口
				cm = "1";
			}
		}
		
		//查询模板参数，已有节点自动勾选
		tempParam = bio.queryTemplateParam(id);
		TreeNode root = getTreeNode();
		tree = JSON.toJSONString(root);
		request.setAttribute("tree",tree);
		return "edit";
	}
	
	
	/**
	 * 解析文件并转换成TreeNode对象
	 * 根据后台数据库中已有节点进行自动勾选
	 * @return
	 */
	public String queryDetail() {
		//查询模板信息，回填到详情页面
		Map<String, String> template = bio.queryTemplate(id);
		if(null!=template){
			name = template.get("name");
			describe = template.get("describe");
			serv = template.get("serv");
			vlan = template.get("vlanid");
			
			if(!StringUtil.IsEmpty(service_id)){
				service_id = template.get("service_id");//业务编码
				nserv_svlan_del = template.get("nserv_svlan_del");//释放不同业务冲突vlan的WAN连接
				sserv_del = template.get("sserv_del");//释放同业务的WAN连接
				sserv_svlan_del = template.get("sserv_svlan_del");//释放同业务冲突vlan的WAN连接
				sport_del = template.get("sport_del");//释放冲突的LAN口
				cm = "1";
			}
			update_time = new DateTimeUtil(template.get("update_time")).getLongDate();
		}
		
		//查询模板参数，已有节点自动勾选
		tempParam = bio.queryTemplateParam(id);
		TreeNode root = getTreeNode();
		tree = JSON.toJSONString(root);
		request.setAttribute("tree",tree);
		return "detail";
	}
	
	
	/**
	 * 解析文件并转换成TreeNode对象
	 * 根据后台数据库中已有节点进行自动勾选
	 * @return
	 */
	public String queryDetail4Add() {
		//查询模板信息，回填到详情页面
		/*Map<String, String> template = bio.queryTemplate(id);*/
		/*if(null!=template){
			name = template.get("name");
			describe = template.get("describe");
			serv = template.get("serv");
			vlan = template.get("vlanid");
			update_time = new DateTimeUtil(template.get("update_time")).getLongDate();
		}*/
		
		//因为是新增，所以不存在已有节点自动勾选
		tempParam = new ArrayList<HashMap<String, String>>();
		TreeNode root = getTreeNode();
		tree = JSON.toJSONString(root);
		request.setAttribute("tree",tree);
		return "add";
	}
	
	/**
	 * 解析文件并转换成TreeNode对象
	 * @param tempParam
	 * @return
	 */
	private TreeNode getTreeNode(){
		Document doc = null;
		Element rootElement = null;
		SAXBuilder saxBuilder = new SAXBuilder();
		String path = LipossGlobals.G_ServerHome + File.separator + "WEB-INF" + File.separator + "classes" + File.separator + "CPENodes_itms_CUC.xml";
		//String path = "E:\\AsiaWork\\workspace\\ailk-itms-web-common\\src\\main\\resources\\CPENodes_itms_CUC_test.xml";
		try {
			doc = saxBuilder.build(new File(path));
		} catch (JDOMException e) {
			logger.error("JDOMException:\n{}", e);
		}catch(IOException e) {
			logger.error("IOException:\n{}", e);
		}

		if (null != doc) {
			rootElement = doc.getRootElement();
		}
		
		if(null == rootElement) return null;
		TreeNode treeNode = new TreeNode("InternetGatewayDevice","true","true",null);
		List<Element> children = rootElement.getChildren();
		for(int i=0;i<children.size();i++){
			getchildren("InternetGatewayDevice", treeNode, children.get(i));
		}
		return treeNode;
	}
	
	/**
	 * 递归解析XML节点，生成树对象
	 * @param path 当前的节点路径
	 * @param treeNode 当前节点
	 * @param element XML中当前节点的某个子节点
	 */
	private void getchildren(String path, TreeNode treeNode, Element element){
		TreeNode thisNode = new TreeNode(element.getName(),"true","true",null);
		String type = element.getAttributeValue("type");
		path = path + "." + element.getName();
		List<Element> children = element.getChildren();
		if(null == children || children.size()==0){
			//当前子节点element是叶子节点，填充该叶子的相关信息
			setParams(path, type, thisNode);
			
			ArrayList treeChildren = treeNode.getChildren();
			if(null == treeChildren){
				treeChildren = new ArrayList<Element>();
			}
			treeChildren.add(thisNode);
			treeNode.setChildren(treeChildren);
		}
		else{
			if("i".equals(element.getName())){
				setParams(path, type, thisNode);
			}
			ArrayList treeChildren = treeNode.getChildren();
			if(null == treeChildren){
				treeChildren = new ArrayList<Element>();
			}
			treeChildren.add(thisNode);
			treeNode.setChildren(treeChildren);
			//当前子节点element不是叶子节点，继续递归
			for(int i=0;i<children.size();i++){
				getchildren(path, thisNode, children.get(i));
			}
		}
	}

	
	/**
	 * 设置叶子节点的参数值
	 * @param path
	 * @param type
	 * @param thisNode
	 */
	private void setParams(String path, String type, TreeNode thisNode)
	{
		thisNode.setNocheck("false");
		thisNode.setType(type);
		//path带数字的形式为：InternetGatewayDevice.LANDevice.i.LANEthernetInterfaceConfig.i.Enable
		//如果与InternetGatewayDevice.LANDevice.{}.LANEthernetInterfaceConfig.{}.Enable
		//或者InternetGatewayDevice.LANDevice.1.LANEthernetInterfaceConfig.2.Enable
		//能匹配上，则需要将该叶子节点自动勾选，即设置checked=true，以及相应path、value、type、priority属性
		String tempPath = "";
		boolean isCheck = false;
		int index = -1;
		for(int i=0;i<tempParam.size();i++){
			tempPath = replaceI(tempParam.get(i).get("path"));
			if(path.equals(tempPath)){
				isCheck = true;
				index = i;
				break;
			}
		}
		//能匹配上，则需要将该叶子节点自动勾选，即设置checked=true，以及相应path、value、type、priority属性
		if(isCheck){
			thisNode.setChecked("true");
			thisNode.setPath(tempParam.get(index).get("path"));
			thisNode.setValue(tempParam.get(index).get("value"));
			//type按xml文件的为准
			//thisNode.setType(tempParam.get(index).get("type"));
			thisNode.setPriority(tempParam.get(index).get("priority"));
		}
	}
	
	
	
	/**
	 * 编辑页面提交，保存模板
	 * @return
	 */
	public String saveTemplate() {
		List<TreeNode> templateParams = (List<TreeNode>) JSONArray.parseArray(checkedData, TreeNode.class);
		if("1".equals(cm)){
			transServiceID();
		}
		int res = bio.saveTemplate(templateParams, id, serv, vlan, describe, name, nserv_svlan_del, sserv_del, sserv_svlan_del, sport_del, service_id);
		if(res > 0) ajax = "1";
		else ajax = "0";
		return "ajax";
	}
	
	/**
	 * 新增页面提交，保存模板
	 * @return
	 */
	public String addTemplate() {
		List<TreeNode> templateParams = (List<TreeNode>) JSONArray.parseArray(checkedData, TreeNode.class);
		int maxId = bio.getMaxId();
		if(maxId<=0) maxId =0;
		if("1".equals(cm)){
			transServiceID();
			//service_id="1";//service_id暂时没有实际用处，为1代表是业务下发的模板
		}
		int res = bio.addTemplate(templateParams, maxId + 1, serv, vlan, describe, name, nserv_svlan_del, sserv_del, sserv_svlan_del, sport_del, service_id);
		if(res > 0) ajax = "1";
		else ajax = "0";
		return "ajax";
	}
	
	private void transServiceID()
	{
		if("10".equals(serv)) service_id="1001";
		else if("11".equals(serv)) service_id="1101";
		else if("14".equals(serv)) service_id="1401";
	}



	/**
	 * 删除模板以及对应节点信息
	 * @return
	 */
	public String deleteDevice() {
		bio.deleteDevice(id);
		ajax = "1";
		return "ajax";
	}
	
	public static void main(String[] args)
	{
		System.out.println(new ServTemplateACT().replaceI("InternetGatewayDevice.WANDevice.{}.WANConnectionDevice.{}"));
	}

	
	
	private String replaceI(String dbpath)
	{
		dbpath = dbpath.replace(".{}", ".i");
		return dbpath;
	}

	/** getters and setters **/
	public ServTemplateBIO getBio() {
		return bio;
	}

	public void setBio(ServTemplateBIO bio) {
		this.bio = bio;
	}

	public int getDevice_model() {
		return device_model;
	}

	public void setDevice_model(int device_model) {
		this.device_model = device_model;
	}

	public List<Map> getDeviceList() {
		return deviceList;
	}

	public void setDeviceList(List<Map> deviceList) {
		this.deviceList = deviceList;
	}

	public String getHard_version() {
		return hard_version;
	}

	public void setHard_version(String hard_version) {
		try {
			this.hard_version = java.net.URLDecoder.decode(hard_version,
					"UTF-8");
		} catch (Exception e) {
			this.hard_version = hard_version;
		}
	}


	public String getSoft_version() {
		return soft_version;
	}

	public void setSoft_version(String soft_version) {
		try {
			this.soft_version = java.net.URLDecoder.decode(soft_version,
					"UTF-8");
		} catch (Exception e) {
			this.soft_version = soft_version;
		}
	}

	public int getVendor() {
		return vendor;
	}

	public void setVendor(int vendor) {
		this.vendor = vendor;
	}

	public Map getSession() {
		return session;
	}

	public String getAjax() {
		return ajax;
	}

	public void setAjax(String ajax) {
		this.ajax = ajax;
	}

	public long getDeviceTypeId() {
		return deviceTypeId;
	}

	public void setDeviceTypeId(long deviceTypeId) {
		this.deviceTypeId = deviceTypeId;
	}

	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	public void setSession(Map session) {
		this.session = session;
	}


	public void setServletRequest(HttpServletRequest arg0) {
		this.request = arg0;

	}


	public String getGw_type() {
		return gw_type;
	}

	public void setGw_type(String gwType) {
		gw_type = gwType;
	}


	public HttpServletRequest getRequest()
	{
		return request;
	}
	
	public void setRequest(HttpServletRequest request)
	{
		this.request = request;
	}
	
	
	public String getName()
	{
		return name;
	}
	
	public void setName(String name)
	{
		this.name = name;
	}
	
	public String getVlan()
	{
		return vlan;
	}
	
	public void setVlan(String vlan)
	{
		this.vlan = vlan;
	}
	
	public String getServ()
	{
		return serv;
	}
	
	public void setServ(String serv)
	{
		this.serv = serv;
	}
	
	public String getTree()
	{
		return tree;
	}
	
	public void setTree(String tree)
	{
		this.tree = tree;
	}
	
	public int getId()
	{
		return id;
	}
	
	public void setId(int id)
	{
		this.id = id;
	}
	
	public String getCheckedData()
	{
		return checkedData;
	}
	
	public void setCheckedData(String checkedData)
	{
		this.checkedData = checkedData;
	}
	
	public String getUpdate_time()
	{
		return update_time;
	}
	
	public void setUpdate_time(String update_time)
	{
		this.update_time = update_time;
	}
	
	public String getDescribe()
	{
		return describe;
	}
	
	public void setDescribe(String describe)
	{
		this.describe = describe;
	}



	
	public ArrayList<HashMap<String, String>> getTempParam()
	{
		return tempParam;
	}



	
	public void setTempParam(ArrayList<HashMap<String, String>> tempParam)
	{
		this.tempParam = tempParam;
	}



	
	public String getNserv_svlan_del()
	{
		return nserv_svlan_del;
	}



	
	public void setNserv_svlan_del(String nserv_svlan_del)
	{
		this.nserv_svlan_del = nserv_svlan_del;
	}



	
	public String getService_id()
	{
		return service_id;
	}



	
	public void setService_id(String service_id)
	{
		this.service_id = service_id;
	}



	
	public String getSport_del()
	{
		return sport_del;
	}



	
	public void setSport_del(String sport_del)
	{
		this.sport_del = sport_del;
	}



	
	public String getSserv_del()
	{
		return sserv_del;
	}



	
	public void setSserv_del(String sserv_del)
	{
		this.sserv_del = sserv_del;
	}



	
	public String getSserv_svlan_del()
	{
		return sserv_svlan_del;
	}



	
	public void setSserv_svlan_del(String sserv_svlan_del)
	{
		this.sserv_svlan_del = sserv_svlan_del;
	}



	
	public String getCm()
	{
		return cm;
	}



	
	public void setCm(String cm)
	{
		this.cm = cm;
	}

}
