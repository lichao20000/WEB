package action.pmee;
/**
 * 设备性能配置
 */
import static action.cst.AJAX;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.struts2.interceptor.SessionAware;

import com.linkage.litms.system.UserRes;
import com.linkage.module.gwms.dao.tabquery.CityDAO;
import com.opensymphony.xwork2.ActionSupport;

import dao.pmee.DevPmeeConfigDao;
public class DevPmeeConfigAction extends ActionSupport implements SessionAware{
	private static final long serialVersionUID = -8162625837887371912L;
	//****************************定义参数************************
	private UserRes res;// 用户资源
	private Map session;// 服务器的会话对象
	private String ajax="";
	private DevPmeeConfigDao pcd;
	private List<Map<String,String>> CityList;//属地列表
	private List<Map<String,String>> VendorList;//厂商列表
	private List<Map<String,String>> list;//获取数据使用
	private String vendor_id;//厂商ID
	private String device_model;//设备型号
	private String version;//设备版本
	private String username;//用户名
	private String phone;//电话号码
	private String serial;//设备序列号
	private String ip;//设备IP
	private String city_id;//属地
	private String expressionid;//性能表达式
	private String pm_name;//性能表达式名称
	private String device_id;//选中的设备
	private String interval;//采集时间间隔
	private int ruku;//是否入库
	private static HashMap<String,String> class_map=new HashMap<String,String>(7);
	private static HashMap<String,String> isok_map=new HashMap<String,String>(3);
	private static HashMap<String,String> remark_map =new HashMap<String,String>(7);
	//****************************以下是方法***********************
	/**
	 * 初始化
	 */
	public String execute() throws Exception {
		res = (UserRes) session.get("curUser");
		CityList=CityDAO.getAllNextCityListByCityPid(res.getCityId());
		VendorList=pcd.getVendor();
		return SUCCESS;
	}
	/**
	 * 根据厂商获取设备型号
	 * @return
	 * @throws Exception
	 */
	public String getModel() throws Exception{
		list=pcd.getDevModel(vendor_id);
		if(list==null || list.size()==0){//列表为空
			ajax="<option value=''>==查询无记录==</option>";
		}else{
			ajax="<option value=''>===请选择===</option>";
			for(Map<String,String> m:list){
				ajax+="<option value='"+m.get("device_model")+"'>=="+m.get("device_model")+"==</option>";
			}
		}
		return AJAX;
	}
	/**
	 * 根据设备型号获取版本
	 * @return
	 * @throws Exception
	 */
	public String getVersion() throws Exception{
		list=pcd.getVersion(vendor_id, device_model);
		if(list==null || list.size()==0){//列表为空
			ajax="<option value=''>==查询无记录==</option>";
		}else{
			ajax="<option value=''>===请选择===</option>";
			for(Map m:list){
				ajax+="<option value='"+m.get("devicetype_id")+"'>=="+m.get("softwareversion")+"==</option>";
			}
		}
		return AJAX;
	}
	/**
	 * 根据属地和版本获取设备
	 * @return
	 * @throws Exception
	 */
	public String getDevByModel() throws Exception{
		res = (UserRes) session.get("curUser");
		list=pcd.getDevByVersion(res.getUser().isAdmin(),res.getAreaId(), vendor_id, version,city_id);
		if(list==null || list.size()==0){
			ajax="查询无设备";
		}else{
			String name="";
			for(Map<String,String> m:list){
				name=pcd.getUser(m.get("device_serialnumber"));
				ajax+="<input type='checkbox' name='device' dev_serial='"+m.get("device_serialnumber")
				                                        +"' gather_id='"+m.get("gather_id")
				                                        +"' username='"+name
				                                        +"' value='"+m.get("device_id")
				                                        +"' oui='"+m.get("oui")
				                                        +"' onclick=getExp('"+m.get("oui")+"')>";
				ajax+="&nbsp;"+m.get("device_serialnumber")+"|"+m.get("loopback_ip")+"| SNMP设备 ||"+name+"<br>";
			}
		}
		return AJAX;
	}
	/**
	 * 根据用户获取设备
	 * @return
	 * @throws Exception
	 */
	public String getDevByUser() throws Exception{
		res = (UserRes) session.get("curUser");
		list=pcd.getDevByUser(res.getUser().isAdmin(),res.getAreaId(), username, phone);
		if(list==null || list.size()==0){
			ajax="查询无设备";
		}else{
			String name="";
			for(Map<String,String> m:list){
				name=pcd.getUser(m.get("device_serialnumber"));
				ajax+="<input type='checkbox' name='device' dev_serial='"+m.get("device_serialnumber")
				                                        +"' gather_id='"+m.get("gather_id")
				                                        +"' username='"+name
				                                        +"' value='"+m.get("device_id")
				                                        +"' oui='"+m.get("oui")
				                                        +"' onclick=getExp('"+m.get("oui")+"')>";
				ajax+="&nbsp;"+m.get("device_serialnumber")+"|"+m.get("loopback_ip")+"| SNMP设备 ||"+name+"<br>";
			}
		}
		return AJAX;
	}
	/**
	 * 根据序列号获取设备
	 * @return
	 * @throws Exception
	 */
	public String getDevByIP() throws Exception{
		res = (UserRes) session.get("curUser");
		list=pcd.getDevByIP(res.getUser().isAdmin(),res.getAreaId(), serial, ip);
		if(list==null || list.size()==0){
			ajax="查询无设备";
		}else{
			String name="";
			for(Map<String,String> m:list){
				name=pcd.getUser(m.get("device_serialnumber"));
				ajax+="<input type='checkbox' name='device' dev_serial='"+m.get("device_serialnumber")
				                                        +"' gather_id='"+m.get("gather_id")
				                                        +"' username='"+name
				                                        +"' value='"+m.get("device_id")
				                                        +"' oui='"+m.get("oui")
				                                        +"' onclick=getExp('"+m.get("oui")+"')>";
				ajax+="&nbsp;"+m.get("device_serialnumber")+"|"+m.get("loopback_ip")+"| SNMP设备 ||"+name+"<br>";
			}
		}
		return AJAX;
	}
	/**
	 * 获取性能表达式
	 * @return
	 * @throws Exception
	 */
	public String getExpression() throws Exception{
		list=pcd.getExpression(vendor_id);
		if(list==null || list.size()==0){
			ajax="<option value=''>==查询无数据==</option>";
		}else{
			ajax="<option value=''>===请选择===</option>";
			for(Map m:list){
				ajax+="<option value='"+m.get("expressionid")+"'>=="+m.get("name")+"==</option>";
			}
		}
		return AJAX;
	}
	/**
	 * 查看设备是否已经配置
	 * @return
	 * @throws Exception
	 */
	public String IsConfig() throws Exception{
		list=pcd.HasConfigExpression(expressionid, device_id);
		if(list==null || list.size()==0){
			ajax="";
		}else{
			ajax="";
			for(Map<String,String>m:list){
				ajax+=m.get("device_serialnumber")+"|"+m.get("loopback_ip")+"已经配置\n";
			}
		}
		return AJAX;
	}
	/**
	 * 配置性能
	 * @return
	 * @throws Exception
	 */
	public String ConfigDev() throws Exception{
		int num=0;
		String[] id_tmp=device_id.split(",");
		String[] serial_tmp=serial.split("-/-");
		int n=id_tmp.length;
		ajax="";
		String tmp="";
		for(int i=0;i<n;i++){
			num=pcd.actionPerformedOne(expressionid, id_tmp[i], interval);
			if(num==0){
				ajax+=serial_tmp[i]+"性能定义成功,系统后台开始调用PMEE!\n";
				tmp+=",true";
			}else{
				ajax+=serial_tmp[i]+"性能定义失败，请重新操作!\n";
				tmp+=",false";
			}
		}
		if(!"".equals(tmp)){
			ajax=ajax+"-/-"+tmp.substring(1);
		}
		return AJAX;
	}
	/**
	 * 通知后台
	 * @return
	 * @throws Exception
	 */
	public String ConfigPmee() throws Exception{
		res = (UserRes) session.get("curUser");
		String[] id_tmp=device_id.split(",");
		int n=id_tmp.length;
		String[] serial_tmp=serial.split(",");
		for(int i=0;i<n;i++){
			if(serial_tmp[i].equals("true")){
				pcd.actionTwo(res.getUser().getAccount(),res.getUser().getPasswd(), null, id_tmp[i].substring(1,id_tmp[i].length()-1), pm_name, expressionid,
						"","","","", 0,0,0,0,0,0,0,0, 0, 0, 0, 0, 
						0, 0, 0, 0, 0,
						Integer.parseInt(interval), ruku, 0f, 0f, 0f,0f);
			}
		}
		return AJAX;
	}
	/**
	 * 获取配置结果
	 * @return
	 * @throws Exception
	 */
	public String getConfigResult() throws Exception{
		list=pcd.getConfigResult(device_id);
		if(list==null || list.size()==0){
			ajax="<tr bgcolor='#FFFFFF'><td colspan=8>该设备暂时没有配置性能</td></tr>";
		}else{
			if(class_map==null || class_map.isEmpty()){
				InitMap();
			}
			String tmp="";
			ajax="";
			for(Map m:list){
				tmp=remark_map.get(String.valueOf((BigDecimal)m.get("remark")));
				if(tmp==null || tmp.equals("null") || tmp.equals("NULL")){
					tmp="";
				}
				if(m.get("isok")!=null && String.valueOf((BigDecimal)m.get("isok")).equals("1")){
					ajax+="<tr bgcolor='#FFFFFF'>";
				}else{
					ajax+="<tr bgcolor='red'>";
				}
				ajax+="<td>"+m.get("device_name")+"</td>";
				ajax+="<td>"+m.get("device_serialnumber")+"</td>";
				ajax+="<td>"+m.get("name")+"</td>";
				ajax+="<td>"+class_map.get(String.valueOf((BigDecimal)m.get("class1")))+"</td>";
				ajax+="<td>"+m.get("interval")+"</td>";
				ajax+="<td>"+isok_map.get(String.valueOf((BigDecimal)m.get("isok")))+"</td>";
				ajax+="<td>"+tmp+"</td>";
				ajax+="<td><a href='javascript://' onclick=Del('"+m.get("device_id")+"','"+m.get("expressionid")+"',$(this))>删除</a></td>";
				ajax+="</tr>";
			}
		}
		return AJAX;
	}
	/**
	 * 删除性能表达式
	 * @return
	 * @throws Exception
	 */
	public String DelExpressionID() throws Exception
	{
		ajax=String.valueOf(pcd.DelExpressionID(device_id, expressionid));
		return AJAX;
	}
	/**
	 * 初始化Map
	 */
	private void InitMap(){
		class_map.put("0","其他");
		class_map.put("1","CPU利用率");
		class_map.put("2","内存利用率");
		class_map.put("3","地址池利用率");
		class_map.put("4","温度");
		class_map.put("5","电源");
		class_map.put("6","风扇");
		isok_map.put("-1","没有初始化");
		isok_map.put("0","初始化失败");
		isok_map.put("1","初始化成功");
		remark_map.put("-1","超时");
		remark_map.put("-2","不支持");
		remark_map.put("-21","其中一个oid采不到数据");
		remark_map.put("-3","无法采集描述信息");
		remark_map.put("-4","oid采集的索引数不一致");
		remark_map.put("-41","性能和索引采集到的索引不一致");
		remark_map.put("-6","表达式ID超过了999");
	}
	//*****************************END****************************

	public String getAjax() {
		return ajax;
	}

	public void setAjax(String ajax) {
		this.ajax = ajax;
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

	public void setPcd(DevPmeeConfigDao pcd) {
		this.pcd = pcd;
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
	public List<Map<String, String>> getList() {
		return list;
	}
	public void setList(List<Map<String, String>> list) {
		this.list = list;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getSerial() {
		return serial;
	}
	public void setSerial(String serial) {
		this.serial = serial;
	}
	public String getIp() {
		return ip;
	}
	public void setIp(String ip) {
		this.ip = ip;
	}
	public DevPmeeConfigDao getPcd() {
		return pcd;
	}
	public void setVersion(String version) {
		this.version = version;
	}
	public String getCity_id() {
		return city_id;
	}
	public void setCity_id(String city_id) {
		this.city_id = city_id;
	}
	public String getExpressionid() {
		return expressionid;
	}
	public void setExpressionid(String expressionid) {
		this.expressionid = expressionid;
	}
	public String getDevice_id() {
		return device_id;
	}
	public void setDevice_id(String device_id) {
		this.device_id = device_id;
	}
	public String getInterval() {
		return interval;
	}
	public void setInterval(String interval) {
		this.interval = interval;
	}
	public String getPm_name() {
		return pm_name;
	}
	public void setPm_name(String pm_name) {
		this.pm_name = pm_name;
	}
	public int getRuku() {
		return ruku;
	}
	public void setRuku(int ruku) {
		this.ruku = ruku;
	}
	public UserRes getRes() {
		return res;
	}
	public void setRes(UserRes res) {
		this.res = res;
	}
	public Map getSession() {
		return session;
	}
	public void setSession(Map session) {
		this.session = session;
	}
	
}
