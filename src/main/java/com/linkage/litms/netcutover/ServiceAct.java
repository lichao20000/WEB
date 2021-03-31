package com.linkage.litms.netcutover;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.linkage.commons.db.DBUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.commons.db.PrepareSQL;
import com.linkage.litms.common.database.Cursor;
import com.linkage.litms.common.database.DataSetBean;
import com.linkage.litms.common.filter.SelectCityFilter;
import com.linkage.litms.common.util.FormUtil;
import com.linkage.litms.resource.DeviceAct;
import com.linkage.litms.system.User;
import com.linkage.litms.system.UserRes;
import com.linkage.module.gwms.Global;

public class ServiceAct
{
	/** log */
	private static Logger logger = LoggerFactory.getLogger(ServiceAct.class);
	private PrepareSQL pSQL = null;    
    
    private String m_insertSQL="insert into tab_servicecode(servicecode,service_id,template_id,devicetype_id,citylist) values(?,?,?,?,?)";
    
    /**
     * 文件中每行内容的格式
     */
    private static String fileRegex="(\\d+\\|){4}(;\\d+;)*";

	public ServiceAct()
	{
		 pSQL = new PrepareSQL();
	}
	
	/**
	 * 根据fileList中的文件名，解析文件中数据，把数据入库
	 * @param fileList
	 * @return 0:入库成功，-1:文件格式不正确,-2:入库失败
	 */
	public int fileAddServicecode(ArrayList fileList)
	{		
		int resultCode = 0;
		DeviceAct act = new DeviceAct();
		List fileInfoList = new ArrayList(100);
		
		//读取各文件中内容
		List tempList = new ArrayList(100);
		for(int i=0;i<fileList.size();i++)
		{
			tempList.clear();
			tempList = act.readFileRes((String)fileList.get(i));
			fileInfoList.addAll(tempList);
		}		
			
		String service_code ="";	
		//存放sql的list
		ArrayList list = new ArrayList(10);	
		
		//准备批量sql语句
		for(int i=1;i<fileInfoList.size();i++)
		{
			service_code =(String)fileInfoList.get(i);
			//看文件输入内容，格式是否正确
			if(!Pattern.matches(fileRegex,service_code))
			{
				resultCode =-1;
				break;
			}
			String[] service_codes =service_code.split(",",service_code.length());			
			pSQL.setSQL(m_insertSQL);
			pSQL.setStringExt(1,service_codes[0],true);
			pSQL.setStringExt(2,service_codes[1],false);
			pSQL.setStringExt(3,service_codes[2],false);
			pSQL.setStringExt(4,service_codes[3],false);
			if(!"".equals(service_codes[4]))
			{
				pSQL.setStringExt(5,service_codes[4],true);
			}
			else
			{
				pSQL.setStringExt(5,null,false);
			}
			
			list.add(pSQL.getSQL());
		}
		
		if(list.size()>0&&resultCode==0)
		{
			int[] code =DataSetBean.doBatch(list);
			if(null==code||code.length<=0)
			{
				resultCode =-2;
			}
		}	
		
		//clear
		list = null;
		tempList = null;
		fileList = null;
		fileInfoList = null;
		
		return resultCode;
	}
	
	/**
	 * 根据查询条件查询 家庭网关，企业网关，snmp设备对应的用户
	 * 
	 * @author lizj （5202）
	 * @param request
	 * @param usertype  说明：0：家庭网关用户，1：企业网关用户，2：snmp 设备用户 ，3：所有用户
	 * @return
	 */
	public static String getAllUserList(HttpServletRequest request,int usertype){
		
		

		// 获取用户信息
		HttpSession session = request.getSession();
		UserRes curUser = (UserRes) session.getAttribute("curUser");
		User user = curUser.getUser();
		
		//查询类型 0：按用户名模糊查询 ，1：按属地进行过滤 2：按设备域名进行模糊查询。
		
		String searchType = request.getParameter("searchType");
		
		if(searchType == null || searchType.equals("") || searchType.equals("-1")){
			
			searchType = "1";
			
		}
		

        
        
		String user_name = request.getParameter("user_name");
		String loopback_ip = request.getParameter("loopback_ip");
		String city_id = request.getParameter("city_id");
		
		
		
		Cursor cursor = null;
		//按用户名模糊查询
		
		if(searchType.equals("0")){				

	        
			//获取下级所有属地
	        SelectCityFilter scf = new SelectCityFilter();           
	        String citys = scf.getAllSubCityIds(user.getCityId(),true);
	        //按属地进行过滤
			cursor = getUserByUserName(user_name,usertype,citys);			
		}
		//按属地查询
		else if(searchType.equals("1")){
			
			if(city_id == null || city_id.equals("") || city_id.equals("-1")){
				
				city_id = user.getCityId();
				
			}
			cursor = getUserByCity(city_id,usertype);
		}
		//按设备域名
		else if(searchType.equals("2")){
						
			//获取下级所有属地
	        SelectCityFilter scf = new SelectCityFilter();           
	        String citys = scf.getAllSubCityIds(user.getCityId(),true);
			cursor = getUserByDomain(loopback_ip,usertype,citys);
		}		
		
		String strList = FormUtil.createListBox(cursor, "username","username",false,"","");
		
		return strList;
		
	}
	
	/**
	 * 按用户模糊查询
	 * 
	 * @return
	 */
	public static Cursor getUserByUserName(String user_name, int usertype,String citys){
		Cursor cursor = null;
		
		String sql = "";
		
		if(usertype == 0){
			
			sql = "select a.user_id,a.username from tab_hgwcustomer a,tab_gw_device b where a.device_id=b.device_id and b.gw_type=1 and a.username like '%" + user_name + "%' and b.city_id in(" + citys + ")";
			PrepareSQL psql = new PrepareSQL(sql);
	    	psql.getSQL();
			cursor = DataSetBean.getCursor(sql);
			
		}
		else if(usertype == 1){
			
			sql = "select a.user_id,a.username from tab_egwcustomer a,tab_gw_device b,tab_customerinfo c" +
					" where a.device_id=b.device_id " +
					" and a.customer_id=c.customer_id" + 
					" and b.gw_type=2 and a.username like '%" + user_name + "%' and c.city_id in(" + citys + ")";	
			PrepareSQL psql = new PrepareSQL(sql);
	    	psql.getSQL();
			cursor = DataSetBean.getCursor(sql);
			
		}
		//else if(usertype == 2){
		//	sql = "select a.user_id,a.username from cus_radiuscustomer a ,tab_deviceresource b where a.device_id = b.device_id and a.username like '%" + user_name + "%' and b.city_id in(" + citys + ")";	
		//	cursor = DataSetBean.getCursor(sql);
		//}
		else{
			
			sql = "select a.user_id,a.username from tab_hgwcustomer a,tab_gw_device b where a.device_id=b.device_id and b.gw_type=1 and a.username like '%" + user_name + "%' and b.city_id in(" + citys + ")";
			PrepareSQL psql = new PrepareSQL(sql);
	    	psql.getSQL();
			cursor = DataSetBean.getCursor(sql);
			
			//sql = "select a.user_id,a.username from tab_egwcustomer a,tab_gw_device b where a.device_id = b.device_id and b.gw_type=2 and a.username like '%" + user_name + "%' and b.city_id in(" + citys + ")";	
			sql = "select a.user_id,a.username from tab_egwcustomer a,tab_gw_device b,tab_customerinfo c" +
			" where a.device_id=b.device_id " +
			" and a.customer_id=c.customer_id" + 
			" and b.gw_type=2 and a.username like '%" + user_name + "%' and c.city_id in(" + citys + ")";	
			PrepareSQL psql2 = new PrepareSQL(sql);
	    	psql2.getSQL();
			cursor.addCursor(DataSetBean.getCursor(sql));
			
			//sql = "select a.user_id,a.username from cus_radiuscustomer a ,tab_deviceresource b where a.device_id = b.device_id and a.username like '%" + user_name + "%' and b.city_id in(" + citys + ")";	
			
			//cursor.addCursor(DataSetBean.getCursor(sql));
			
		}		
		return cursor;
	}
	
	/**
	 * 根据属地查询用户
	 * @param city_id
	 * @param usertype
	 * @return
	 */
	public static Cursor getUserByCity(String city_id,int usertype){
		
		Cursor cursor = null;
		String sql = "";       
		
		//获取下级所有属地
        SelectCityFilter scf = new SelectCityFilter();           
        String citys = scf.getAllSubCityIds(city_id,true);
        
        logger.debug("citys :" + citys);
		
		if(usertype == 0){
			
			sql = "select a.user_id,a.username from tab_hgwcustomer a,tab_gw_device b where a.device_id=b.device_id and b.gw_type=1 and b.city_id in(" + citys + ")";
			PrepareSQL psql = new PrepareSQL(sql);
	    	psql.getSQL();
			cursor = DataSetBean.getCursor(sql);
			
		}
		else if(usertype == 1){
			
			//sql = "select a.user_id,a.username from tab_egwcustomer a,tab_gw_device b where a.device_id = b.device_id and b.gw_type=2 and b.city_id in(" + citys + ")";	
			sql = "select a.user_id,a.username from tab_egwcustomer a,tab_gw_device b,tab_customerinfo c" +
			" where a.device_id=b.device_id " +
			" and a.customer_id=c.customer_id" + 
			" and b.gw_type=2 and c.city_id in(" + citys + ")";	
			PrepareSQL psql = new PrepareSQL(sql);
	    	psql.getSQL();
			cursor = DataSetBean.getCursor(sql);
			
		}
		//else if(usertype == 2){
		//	sql = "select a.user_id,a.username from cus_radiuscustomer a ,tab_deviceresource b where a.device_id = b.device_id and b.city_id in(" + citys + ")";	
		//	cursor = DataSetBean.getCursor(sql);
		//}
		else{
			
			sql = "select a.user_id,a.username from tab_hgwcustomer a,tab_gw_device b where a.device_id=b.device_id and b.gw_type=1 and b.city_id in(" + citys + ")";
			PrepareSQL psql = new PrepareSQL(sql);
	    	psql.getSQL();
			cursor = DataSetBean.getCursor(sql);
			
			//sql = "select a.user_id,a.username from tab_egwcustomer a,tab_gw_device b where a.device_id = b.device_id and b.gw_type=2 and b.city_id in(" + citys + ")";	
			sql = "select a.user_id,a.username from tab_egwcustomer a,tab_gw_device b,tab_customerinfo c" +
			" where a.device_id=b.device_id " +
			" and a.customer_id=c.customer_id" + 
			" and b.gw_type=2 and c.city_id in(" + citys + ")";	
			PrepareSQL psql2 = new PrepareSQL(sql);
	    	psql2.getSQL();
			cursor.addCursor(DataSetBean.getCursor(sql));
			
			//sql = "select a.user_id,a.username from cus_radiuscustomer a ,tab_deviceresource b where a.device_id = b.device_id and b.city_id in(" + citys + ")";	
			
			//cursor.addCursor(DataSetBean.getCursor(sql));
			
		}
		
		return cursor;
		
	}
	
	/**
	 * 
	 * @param loopback_ip
	 * @param usertype
	 * @return
	 */
	public static Cursor getUserByDomain(String loopback_ip,int usertype,String citys){
		
		
		Cursor cursor = null;
		String sql = "";
		
		//家庭网关
		if(usertype == 0){
			
			sql = "select a.user_id,a.username from tab_hgwcustomer a ,tab_gw_device b where a.device_id=b.device_id and b.gw_type=1 and b.loopback_ip like '%" 
					+ loopback_ip + "%' and b.city_id in(" + citys + ")";
			PrepareSQL psql = new PrepareSQL(sql);
	    	psql.getSQL();
			cursor = DataSetBean.getCursor(sql);
			
		}
		//企业网关
		else if(usertype == 1){
			
//			sql = "select a.user_id,a.username from tab_egwcustomer a ,tab_gw_device b where a.device_id = b.device_id and b.gw_type=2 and b.loopback_ip like '%" 
//				+ loopback_ip + "%' and b.city_id in(" + citys + ")";
			sql = "select a.user_id,a.username from tab_egwcustomer a,tab_gw_device b,tab_customerinfo c" +
			" where a.device_id=b.device_id " +
			" and a.customer_id=c.customer_id and b.loopback_ip like '%" + loopback_ip + "%'" +
			" and b.gw_type=2 and c.city_id in(" + citys + ")";	
			PrepareSQL psql = new PrepareSQL(sql);
	    	psql.getSQL();
			cursor = DataSetBean.getCursor(sql);
			
		}
		// snmp
		//else if(usertype == 2){
			
		//	sql = "select a.user_id,a.username from cus_radiuscustomer a ,tab_deviceresource b where a.device_id = b.device_id and b.loopback_ip like '%" 
		//		+ loopback_ip + "%' and b.city_id in(" + citys + ")";
		//	cursor = DataSetBean.getCursor(sql);
		//}
		else{
			
			sql = "select a.user_id,a.username from tab_hgwcustomer a ,tab_gw_device b where a.device_id=b.device_id and b.gw_type=1 and b.loopback_ip like '%" 
				+ loopback_ip + "%' and b.city_id in(" + citys + ")";
			PrepareSQL psql = new PrepareSQL(sql);
	    	psql.getSQL();
			cursor = DataSetBean.getCursor(sql);
			
//			sql = "select a.user_id,a.username from tab_egwcustomer a ,tab_gw_device b where a.device_id = b.device_id and b.gw_type=2 and b.loopback_ip like '%" 
//				+ loopback_ip + "%' and b.city_id in(" + citys + ")";
			sql = "select a.user_id,a.username from tab_egwcustomer a,tab_gw_device b,tab_customerinfo c" +
			" where a.device_id=b.device_id " +
			" and a.customer_id=c.customer_id and b.loopback_ip like '%" + loopback_ip + "%'" +
			" and b.gw_type=2 and c.city_id in(" + citys + ")";	
			
			//cursor.addCursor(DataSetBean.getCursor(sql));
			
			//sql = "select a.user_id,a.username from cus_radiuscustomer a ,tab_deviceresource b where a.device_id = b.device_id and b.loopback_ip like '%" 
			//	+ loopback_ip + "%' and b.city_id in(" + citys + ")";
			PrepareSQL psql2 = new PrepareSQL(sql);
	    	psql2.getSQL();
			cursor.addCursor(DataSetBean.getCursor(sql));
			
		}		
		return cursor;
		
	}
	
	/**
	 * 查询用户和业务的对应关系
	 * @param request
	 * @return
	 */
	public static String getUserService(HttpServletRequest request){
		
		// 获取用户信息
		HttpSession session = request.getSession();
		UserRes curUser = (UserRes) session.getAttribute("curUser");
		User user = curUser.getUser();
		
		
		Map serMap = getGwServTypeMap();
		Map operMap = getGwOperTypeMap();
		
		//获取下级所有属地,根据属地过滤
        SelectCityFilter scf = new SelectCityFilter();           
        String citys = scf.getAllSubCityIds(user.getCityId(),true);		
		String username = request.getParameter("username");
	
		String strHtml = "<TABLE border=0 cellspacing=1 cellpadding=2 width=\"95%\" align=center bgcolor=#999999>";
    	strHtml +="<TR bgcolor=#ffffff>";
    	strHtml +="<TH nowrap>用户名称</TH>";
    	strHtml +="<TH nowrap>业务名称</TH>";
    	strHtml +="<TH nowrap>操作类型</TH>";
    	strHtml +="<TH nowrap>状态值</TH>";
    	strHtml +="</TR>";
    	
    	String innerHtml = "";
    	
		Cursor cursor = null;
		
		//家庭网关用户表
		String sql = "select a.*,b.username from tab_gw_user_serv a,tab_hgwcustomer b where a.is_active=1 and a.gw_type=1 and a.username = b.username and b.city_id in(" + citys + ") ";

		// teledb
		if (DBUtil.GetDB() == Global.DB_MYSQL) {
			sql = "select a.serv_type_id, a.oper_type_id, a.value, b.username " +
					"from tab_gw_user_serv a,tab_hgwcustomer b where a.is_active=1 and a.gw_type=1 and a.username = b.username and b.city_id in(" + citys + ") ";
		}
		if(username != null && !username.equals("") && !username.equals("-1")){
			sql+=" and a.username='" + username + "'";
		}
		sql+=" order by a.serv_type_id ";
		PrepareSQL psql = new PrepareSQL(sql);
    	psql.getSQL();
		cursor = DataSetBean.getCursor(sql);
		
		int i = cursor.getRecordSize();	
		Map fields = cursor.getNext();
		int m = 0;
		if(fields != null ){
			while(fields != null){
				
				if(m == 0){
					innerHtml += "<TR bgcolor=#ffffff>";
					innerHtml +="<TD class=column width='40%'>"+ (String)fields.get("username")+ "(家庭网关设备用户)" + "</TD>";
					innerHtml +="<TD class=column >"+ serMap.get((String)fields.get("serv_type_id")) +"</TD>";
					innerHtml +="<TD class=column >"+ operMap.get((String)fields.get("oper_type_id")) +"</TD>";				
					innerHtml +="<TD class=column >"+ (String)fields.get("value") +"</TD>";
					innerHtml +="</TR>"; 
				} else {
					
					innerHtml += "<TR bgcolor=#ffffff>";
					innerHtml +="<TD class=column width='40%'>"+ (String)fields.get("username")+ "(家庭网关设备用户)" + "</TD>";
					innerHtml +="<TD class=column>"+ serMap.get((String)fields.get("serv_type_id")) +"</TD>";
					innerHtml +="<TD class=column>"+ operMap.get((String)fields.get("oper_type_id")) +"</TD>";				
					innerHtml +="<TD class=column>"+ (String)fields.get("value") +"</TD>";
					innerHtml +="</TR>"; 				
				}
				
				m++;
				fields = cursor.getNext();
			}
			
		}
		

		
		
		
		//企业网关用户表
		sql = "select a.*,b.username from tab_gw_user_serv a,tab_egwcustomer b,tab_gw_device c " 
			+ "where a.is_active=1 and b.device_id=c.device_id and a.gw_type=2 and a.username = b.username and c.city_id in(" + citys + ") ";

		// teledb
		if (DBUtil.GetDB() == Global.DB_MYSQL) {
			sql = "select a.serv_type_id, a.oper_type_id, a.value, b.username " +
					"from tab_gw_user_serv a,tab_egwcustomer b,tab_gw_device c "
					+ "where a.is_active=1 and b.device_id=c.device_id and a.gw_type=2 and a.username = b.username and c.city_id in(" + citys + ") ";
		}
		if(username != null && !username.equals("") && !username.equals("-1")){
			sql+=" and a.username='" + username + "'";
		}
		
		sql+=" order by a.serv_type_id ";

		PrepareSQL psql2 = new PrepareSQL(sql);
    	psql2.getSQL();
    	cursor = null;
		fields = null;
		cursor = DataSetBean.getCursor(sql);
		
		
		i = cursor.getRecordSize();	
		fields = cursor.getNext();
		m = 0;
		if(fields != null ){
			while(fields != null){
				
				if(m == 0){
					innerHtml += "<TR bgcolor=#ffffff>";
					innerHtml +="<TD class=column width='40%' >"+ (String)fields.get("username")+ "(企业网关设备用户)" + "</TD>";
					innerHtml +="<TD class=column >"+ serMap.get((String)fields.get("serv_type_id")) +"</TD>";
					innerHtml +="<TD class=column>"+ operMap.get((String)fields.get("oper_type_id")) +"</TD>";				
					innerHtml +="<TD class=column>"+ (String)fields.get("value") +"</TD>";
					innerHtml +="</TR>"; 
				} else {
					
					innerHtml += "<TR bgcolor=#ffffff>";
					innerHtml +="<TD class=column width='40%'>"+ (String)fields.get("username")+ "(家庭网关设备用户)" + "</TD>";
					innerHtml +="<TD class=column>"+ serMap.get((String)fields.get("serv_type_id")) +"</TD>";
					innerHtml +="<TD class=column>"+ operMap.get((String)fields.get("oper_type_id")) +"</TD>";				
					innerHtml +="<TD class=column>"+ (String)fields.get("value") +"</TD>";
					innerHtml +="</TR>"; 				
				}
				
				m++;
				fields = cursor.getNext();
			}
			
		}
		
		//snmp用户
		sql = "select a.*,b.username from tab_gw_user_serv a,tab_egwcustomer b,tab_customerinfo c " +
				" where a.is_active=1  and a.gw_type=3 and a.username = b.username and c.city_id in(" + citys + ") " +
				" and b.customer_id=c.customer_id ";
		// teledb
		if (DBUtil.GetDB() == Global.DB_MYSQL) {
			sql = "select a.serv_type_id, a.oper_type_id, a.value, b.username from tab_gw_user_serv a,tab_egwcustomer b,tab_customerinfo c " +
					" where a.is_active=1  and a.gw_type=3 and a.username = b.username and c.city_id in(" + citys + ") " +
					" and b.customer_id=c.customer_id ";
		}
		if(username != null && !username.equals("") && !username.equals("-1")){
			sql+=" and a.username='" + username + "'";
		}
		sql+=" order by a.serv_type_id ";
		
		PrepareSQL psql3 = new PrepareSQL(sql);
    	psql3.getSQL();
		cursor = null;
		fields = null;
		cursor = DataSetBean.getCursor(sql);
		
		
		i = cursor.getRecordSize();	
		fields = cursor.getNext();
		m = 0;
		String value = "";
		
		Map descMap = getSerOperOrderMap();
		
		String[] arrValue = null;
		
		String serv_type_id = "";
		String oper_type_id = "";
		if(fields != null ){
			while(fields != null){
				
				serv_type_id = (String)fields.get("serv_type_id");
				oper_type_id = (String)fields.get("oper_type_id");
				value = (String)fields.get("value");
				
				arrValue = value.split("\\|\\|\\|");
				String _value = "";
				int serial = 0;
				for(int n = 0; n < arrValue.length ; n++){
					
					serial = n+1;
					
					if(n == 0){
						_value += descMap.get(serv_type_id + "," + oper_type_id + "," + serial) +   ":" + arrValue[n];
					}
					else{
						_value += "<br>" + descMap.get(serv_type_id + "," + oper_type_id + "," + serial) +  ":" + arrValue[n];
					}
						
					
				}
				
				if(m == 0){
					innerHtml += "<TR bgcolor=#ffffff>";
					innerHtml +="<TD class=column width='40%' >"+ (String)fields.get("username")+ "(SNMP网关设备用户)" + "</TD>";
					innerHtml +="<TD class=column>"+ serMap.get(serv_type_id) +"</TD>";
					innerHtml +="<TD class=column>"+ operMap.get(oper_type_id) +"</TD>";				
					innerHtml +="<TD class=column>"+ _value +"</TD>";
					innerHtml +="</TR>"; 
				} else {
					
					innerHtml += "<TR bgcolor=#ffffff>";
					innerHtml +="<TD class=column width='40%'>"+ (String)fields.get("username")+ "(家庭网关设备用户)" + "</TD>";
					innerHtml +="<TD class=column>"+ serMap.get(serv_type_id) +"</TD>";
					innerHtml +="<TD class=column>"+ operMap.get(oper_type_id) +"</TD>";				
					innerHtml +="<TD class=column>"+ _value +"</TD>";
					innerHtml +="</TR>"; 				
				}				
				m++;
				fields = cursor.getNext();
			}
			
		}	
		
		if(innerHtml.equals("")){
			innerHtml += "<TR bgcolor=#ffffff>";
			innerHtml +="<TD class=column colspan='4'>系统没有记录！</TD>";
			innerHtml +="</TR>"; 
		}
		
		strHtml +=innerHtml + "</TABLE>";
		
		//logger.debug("strHtml :" + strHtml);
		
		return strHtml;
		
	}
	
	/**
	 * 
	 * @return
	 */
	public static Map getGwServTypeMap(){
		
		Map map = new HashMap();
		PrepareSQL psql = new PrepareSQL("select serv_type_id,serv_type_name from tab_gw_serv_type");
    	psql.getSQL();
		Cursor cursor = DataSetBean.getCursor("select serv_type_id,serv_type_name from tab_gw_serv_type");
		Map fields = cursor.getNext();
		if(fields != null){
			
			while (fields != null){				
				map.put((String)fields.get("serv_type_id"),(String)fields.get("serv_type_name"));
				fields = cursor.getNext();
			}
			
		}
		
		return map;
		
	}
	
	/**
	 * 
	 * @return
	 */
	public static Map getGwOperTypeMap(){
		
		Map map = new HashMap();
		PrepareSQL psql = new PrepareSQL("select oper_type_id,oper_type_name from tab_gw_oper_type");
    	psql.getSQL();
		Cursor cursor = DataSetBean.getCursor("select oper_type_id,oper_type_name from tab_gw_oper_type");
		Map fields = cursor.getNext();
		if(fields != null){
			
			while (fields != null){				
				map.put((String)fields.get("oper_type_id"),(String)fields.get("oper_type_name"));
				fields = cursor.getNext();
			}
			
		}
		
		return map;
		
	}
	
	/**
	 * 业务，操作，序列号，参数描述对用关系
	 * @return
	 */
	public static Map getSerOperOrderMap(){
		
		Map map = new HashMap();
		String sql = "select * from gw_serv_oper_order_desc";
		// teledb
		if (DBUtil.GetDB() == Global.DB_MYSQL) {
			sql = "select serv_type_id, oper_type_id, serial, param_desc from gw_serv_oper_order_desc";
		}
		PrepareSQL psql = new PrepareSQL(sql);
    	psql.getSQL();
		Cursor cursor = DataSetBean.getCursor(sql);
		Map fields = cursor.getNext();
		if(fields != null){
			
			while (fields != null){				
				map.put((String)fields.get("serv_type_id") + "," + (String)fields.get("oper_type_id") + "," + (String)fields.get("serial"),(String)fields.get("param_desc"));
				fields = cursor.getNext();
			}
			
		}
		
		return map;	
	}
		
}
