package action.hgwip;

import static action.cst.ADD;
import static action.cst.DEL;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.struts2.interceptor.SessionAware;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import bio.hgwip.BrasVbrasManageBIO;
import bio.hgwip.IpTool;

import com.linkage.litms.system.UserRes;
import com.linkage.module.gwms.dao.tabquery.CityDAO;
import com.opensymphony.xwork2.ActionSupport;

import dao.hgwip.IPManagerDAO;

/**
 * 
 * @author fanjm (Ailk No.35572)
 * @version 1.0
 * @since 2017年2月16日
 *
 */
public class BrasVbrasManagAction extends ActionSupport implements SessionAware {

	private static Logger logger = LoggerFactory.getLogger(BrasVbrasManagAction.class);
	private static final long serialVersionUID = -1619955441753753481L;
	private Map<String, Object> session;// 服务器的会话对象
	private UserRes res;// 用户资源
	private List<Map<String,String>> brasList;// 显示的vbras、bras信息
	private List<Map<String,String>> brasStateList;// 显示的设备vbras、bras统计信息
	private IPManagerDAO ipmdao;// 获取用户信息类
	private ArrayList<Map<String,String>> cityList; //存放市级地区（city_id city_name）
	private String ajax = "";
	private int netMaskLen;// 掩码长度
	private String ipAdr;//网络地址
	private String netMask;//子网掩码
	private String brasType;//BRAS/VBRAS
	private String city;//属地
	private String ip;//查询的ip条件
	private BrasVbrasManageBIO brasVbrasManageBIO;//业务类
	
	/**
	 * 起始的Vbras/bras管理主界面（查询页面）
	 */
	@Override
	public String execute() throws Exception
	{
		brasList = brasVbrasManageBIO.getBrasList(ip);
		return SUCCESS;
	}
	
	
	/**
	 * 增加VBRAS BRAS配置
	 * 
	 * @return "add"
	 */
	public String add() throws Exception
	{
		logger.debug("add()==>方法开始{}");
		int addRes = brasVbrasManageBIO.addBras(ipAdr, netMaskLen, netMask, brasType, city);
		ajax = String.valueOf(addRes);
		logger.debug("add()==>方法结束{}",ajax);
		return ADD;
	}
	
	/**
	 * add页面初始化城市列表
	 * @return "ajax"
	 */
	public String initCityList(){
		logger.debug("initCityList()==>方法开始{}");
		cityList = (ArrayList<Map<String, String>>) CityDAO.getNextCityListByCityPid("00");
		ajax +="<option value=''>==请选择==</option>";
		for(Map<String,String> map:cityList){
			ajax += "<option value='" + map.get("city_id").toString()
					+ "'>==" + map.get("city_name") + "==</option>";
		}
		logger.debug("initCityList()==>方法结束{}",cityList);
		return "initCity";
	}
	
	/**
	 * 根据掩码长度返回掩码
	 * 
	 * @return "getNtMk"
	 */
	public String getNtMk()
	{
		logger.debug("getNtMk()==>方法开始{}");
		ajax = IpTool.getNetMask(netMaskLen);;
		logger.debug("getNtMk()==>方法结束{}",ajax);
		return "getNtMk";
	}
	
	/**
	 * 删除Vbras/Bras网段
	 * 
	 * @return "del"
	 */
	public String delVbras() 
	{
		logger.debug("del()==>方法开始{}");
		int del = brasVbrasManageBIO.delVbras(ip, netMask);
		ajax = String.valueOf(del);
		logger.debug("del()==>方法结束}",ajax);
		return DEL;
	}
	
	
	/**
	 * 统计Vbras/Bras网段
	 * 
	 * @return "state"
	 */
	public String brasState() 
	{
		logger.debug("brasState()==>方法开始{}");
		brasStateList = brasVbrasManageBIO.brasState(city);
		logger.debug("brasState()==>方法结束{}",brasStateList);
		return "state";
	}
	
	
	
	@Override
	public void setSession(Map<String, Object> session) {
		this.session = session;
	}

	public void setIpmdao(IPManagerDAO ipmdao)
	{
		this.ipmdao = ipmdao;
	}


	public IPManagerDAO getIpmdao() {
		return ipmdao;
	}


	public ArrayList<Map<String, String>> getCityList() {
		return cityList;
	}


	public void setCityList(ArrayList<Map<String, String>> cityList) {
		this.cityList = cityList;
	}
	
	public String getAjax()
	{
		return ajax;
	}

	public void setRes(UserRes res) {
		this.res = res;
	}

	public void setNetMaskLen(int netMaskLen) {
		this.netMaskLen = netMaskLen;
	}


	public void setIpAdr(String ipAdr) {
		this.ipAdr = ipAdr;
	}


	public void setNetMask(String netMask) {
		this.netMask = netMask;
	}


	public void setBrasType(String brasType) {
		this.brasType = brasType;
	}


	public void setCity(String city) {
		this.city = city;
	}


	public BrasVbrasManageBIO getBrasVbrasManageBIO() {
		return brasVbrasManageBIO;
	}


	public void setBrasVbrasManageBIO(BrasVbrasManageBIO brasVbrasManageBIO) {
		this.brasVbrasManageBIO = brasVbrasManageBIO;
	}


	public void setIp(String ip) {
		this.ip = ip;
	}


	public String getIp() {
		return ip;
	}


	public List<Map<String, String>> getBrasList() {
		return brasList;
	}


	public void setBrasList(List<Map<String, String>> brasList) {
		this.brasList = brasList;
	}


	public List<Map<String, String>> getBrasStateList() {
		return brasStateList;
	}


	public void setBrasStateList(List<Map<String, String>> brasStateList) {
		this.brasStateList = brasStateList;
	}


}
