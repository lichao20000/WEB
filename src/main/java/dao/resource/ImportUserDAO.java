package dao.resource;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import jxl.Sheet;
import jxl.Workbook;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;

import com.linkage.commons.db.DBUtil;
import com.linkage.commons.db.PrepareSQL;
import com.linkage.commons.util.DbUtils;
import com.linkage.litms.common.database.DataSetBean;
import com.linkage.litms.common.util.DateTimeUtil;
import com.linkage.module.gwms.Global;

public class ImportUserDAO {
	
	private static Logger log = LoggerFactory.getLogger(ImportUserDAO.class);

	//用户信息的临时数据集
	private Map<String,String> userMap = new HashMap<String,String>();
	
	//jdbc模板
	private JdbcTemplate jt;
	
	//执行的sql列表
	private String[] sqlList = null;
	
	private ArrayList<String> sqlArr = null;
	
	//标题
	private String[] title = null;
	
	//导入的资源类型 0：家庭网关 1：企业网关
	private String infoType = "";
	
	//导入文件的来源 0：BSS  1：IPOSS
	private String resArea = "0";
	
	/**
	 * 输入excel文件解析入库，若数据已有值则更新，没有则新插入
	 * @param file 输入的excel文件
	 */
	public void importFile(File file,String type){
		
		infoType = type;
		sqlArr = new ArrayList<String>();
		
		//初始化列名
		title = new String[5];
		title[0] = "username";
		title[1] = "phonenumber";
		title[2] = "city_id";
		title[3] = "dealdate";
		title[4] = "updatetime";
		
		//解析excel文件
		ArrayList<Map> data = importFromExcel(file);
		
		//查询当前所有的用户数据并放入map中
		getAllUser();
		
		//取user_id
//		long user_id = DataSetBean.getMaxId("tab_hgwcustomer", "user_id");
		long user_id = getMaxUserId();
		if ("1".equals(infoType)){
			user_id = DataSetBean.getMaxId("tab_egwcustomer", "user_id");
		}
		
		//遍历数据，解析入库
		for (int i=0;i<data.size();i++){
			Map dataMap = (Map)data.get(i);
			
			String userName = (String)dataMap.get("username");
			if (dataMap != null){
				
				//取得数据后比较数据中是否已有，若有则更新，若没有则新增
				if (userMap.containsKey(userName)){
					updUserInfo(dataMap, (String)userMap.get(userName));
				}
				else{
					
					//只有BSS导入的帐号需要新增记录 
					if ("0".equals(resArea)){
						user_id++;
						insertUserInfo(dataMap, user_id);
					}
				}
			}
		}
		
		//清空数据
		data = null;

		//初始化sql数组
		int arrSize = sqlArr.size();
		sqlList = new String[arrSize];
		for (int j=0;j<arrSize;j++){
			sqlList[j] = sqlArr.get(j);
		}
		
		sqlArr = null;
		
		//执行全部sql
		//excuteSql();
	}
	
	
	/**
	 * 输入excel文件，解析后返回ArrayList
	 * @param file 输入的excel文件
	 * @return ArrayList<Map>，其中的map以第一行的内容为键值
	 */
	private ArrayList<Map> importFromExcel(File file){
		//初始化返回值和字段名数组
		ArrayList<Map> arr = new ArrayList<Map>();
		
		
		Workbook wwb = null;
		Sheet ws = null;
		
		try{
			//读取excel文件
			wwb = Workbook.getWorkbook(file);
			
			//总sheet数
			int sheetNumber = wwb.getNumberOfSheets();
			log.debug("sheetNumber:" + sheetNumber);
			
			for (int m=0;m<sheetNumber;m++){
				ws = wwb.getSheet(m);
				
				//当前页总记录行数和列数
				int rowCount = ws.getRows();
				int columeCount = ws.getColumns();
				
				log.debug("rowCount:" + rowCount);
				log.debug("columeCount:" + columeCount);
				
				//第一行为字段名，所以行数大于1才执行
				if (rowCount > 1 && columeCount > 0){
					
					//取当前页所有值放入list中
					for (int i=1;i<rowCount;i++){
						
						Map<String,String> dataMap = new HashMap<String,String>();
						for (int j=0;j<columeCount;j++){
							dataMap.put(title[j], ws.getCell(j, i).getContents());
						}
						
						arr.add(dataMap);
					}
				}
				
			}
		}
		catch(Exception e){
			e.printStackTrace();
		}
		finally{
			wwb.close();
			
		}
		
		return arr;
	}
	
	/**
	 * 查询当前所有的用户信息，并放入临时map中
	 *
	 */
	private void getAllUser(){
		
		//查询用户名
		List list = null;
		
		if ("1".equals(infoType)){
			PrepareSQL psql = new PrepareSQL("select username,user_id from tab_egwcustomer");
	    	psql.getSQL();
			list = jt.queryForList("select username,user_id from tab_egwcustomer");
		}
		else{
			PrepareSQL psql = new PrepareSQL("select username,user_id from tab_hgwcustomer");
	    	psql.getSQL();
			list = jt.queryForList("select username,user_id from tab_hgwcustomer");
		}
		
		//遍历数据
		Iterator it = list.iterator();
		while (it.hasNext()){
			Map map = (Map)it.next();
			
			//若数据存在且不为空则放入map中
			String username = (String)map.get("username");
			String user_id = map.get("user_id").toString();
			if (username != null && !"".equals(username)){
				userMap.put(username, user_id);
			}
		}
	}
	
	/**
	 * 更新已有的用户数据
	 * @param dataMap 解析出来的数据集
	 */
	private void updUserInfo(Map dataMap,String user_id){
		//初始化sql语句
		String sql = "";
		
		if ("1".equals(infoType)){
			sql += "update tab_egwcustomer ";
		}
		else{
			sql += "update tab_hgwcustomer ";
		}
		
		String dateStr = (String)dataMap.get("dealdate");
		log.debug("GSJ-------------B:" + dateStr);
		
		//BSS的时间直接是以秒为单位，直接可以入库
		//iposs网管的时间格式为“2007/12/11 12:12”，需要重新转化
		long dealDate = 0;
		//转化时间
		//如果时间中含有/并且不含：2008/05/13
		if (dateStr.indexOf("/") != -1 && dateStr.indexOf(":") == -1) {
			String day = dateStr.substring(0, dateStr.indexOf("/"));
			String month = dateStr.substring(dateStr.indexOf("/")+1, dateStr.lastIndexOf("/"));
			String year = dateStr.substring(dateStr.lastIndexOf("/")+1, dateStr.length());
			dateStr = year + "-" + month + "-" + day + " 00:00:00";
			
		} else if (dateStr.indexOf("-") != -1 && dateStr.indexOf(":") == -1) {
			//2008-05-13
			dateStr += " 00:00:00";
		} else if (dateStr.indexOf(":") != -1) {
			if (dateStr.indexOf(":") == dateStr.lastIndexOf(":")) {
				dateStr += ":00";
			}
		}
		log.debug("GSJ-------------A:" + dateStr);
		
		if (dateStr.indexOf(":") != -1) {
			DateTimeUtil dt = new DateTimeUtil(dateStr);
			dealDate = dt.getLongTime();
			dateStr = String.valueOf(dealDate);
		} else{
			dateStr = "0";
		}
		
		
		//更新时间取当前时间
		long updatetime = (new DateTimeUtil()).getLongTime();
		
		sql += "set username='" + dataMap.get("username") 
				+  "',phonenumber='" + dataMap.get("phonenumber") 
				+ "',city_id='" + dataMap.get("city_id") 
				+ "',dealdate=" + dateStr
				+ ",updatetime=" + updatetime 
				+ " where user_id=" + user_id;
		
		PrepareSQL psql = new PrepareSQL(sql);
    	psql.getSQL();
		
		sqlArr.add(sql);
	}
	
	/**
	 * 新增用户数据
	 * @param dataMap 解析出来的数据集
	 */
	private void insertUserInfo(Map dataMap, long user_id){
		//初始化sql语句
		String sql = "";
		int serv_type_id=10;
		if ("1".equals(infoType)){
			sql += "insert into tab_egwcustomer";
			 serv_type_id=50;
		} else{
			sql += "insert into tab_hgwcustomer";
			 serv_type_id=10;
		}
		
		String dateStr = (String)dataMap.get("dealdate");
		log.debug("GSJ-------------BB:" + dateStr);
		
		//BSS的时间直接是以秒为单位，直接可以入库
		//iposs网管的时间格式为“2007/12/11 12:12”，需要重新转化
		long dealDate = 0;
		//转化时间
		//如果时间中含有/并且不含：2008/05/13
		if (dateStr.indexOf("/") != -1 && dateStr.indexOf(":") == -1) {
			String day = dateStr.substring(0, dateStr.indexOf("/"));
			String month = dateStr.substring(dateStr.indexOf("/")+1, dateStr.lastIndexOf("/"));
			String year = dateStr.substring(dateStr.lastIndexOf("/")+1, dateStr.length());
			
			dateStr = year + "-" + month + "-" + day + " 00:00:00";
			
		} else if (dateStr.indexOf("-") != -1 && dateStr.indexOf(":") == -1) {
			//2008-05-13
			dateStr += " 00:00:00";
		} else if (dateStr.indexOf(":") != -1) {
			if (dateStr.indexOf(":") == dateStr.lastIndexOf(":")) {
				dateStr += ":00";
			}
		}
		log.debug("GSJ-------------AA:" + dateStr);
		
		if (dateStr.indexOf(":") != -1) {
			DateTimeUtil dt = new DateTimeUtil(dateStr);
			dealDate = dt.getLongTime();
			dateStr = String.valueOf(dealDate);
		} 
		
		sql += "(user_id,username,phonenumber,city_id,dealdate,updatetime,serv_type_id,user_state,open_status,cust_type_id,oui,device_serialnumber) values(" 
					+ user_id + ",'" + dataMap.get("username") + "','" + dataMap.get("phonenumber") 
					+ "','" + dataMap.get("city_id") + "'," + dateStr + "," + dataMap.get("updatetime") + "," + serv_type_id + ",'1',1,2,'','')";
		
		PrepareSQL psql = new PrepareSQL(sql);
    	psql.getSQL();
		
		sqlArr.add(sql);
	}
	
	/**
	 * 执行sql数组
	 *
	 */
	@SuppressWarnings("unused")
	private void excuteSql(){
		
		if (sqlList != null && sqlList.length > 0 && sqlList[0] != null ){
			jt.batchUpdate(sqlList);
		}
		
		//清空数组
		sqlList = null;
	}
	
	/**
	 * 初始化数据库连接
	 * @param dao
	 */
	public void setDao(DataSource dao) {
		jt = new JdbcTemplate(dao);
	}


	public void setResArea(String resArea) {
		this.resArea = resArea;
	}
	
    public long getMaxUserId() {
		if(DBUtil.GetDB() == Global.DB_ORACLE || DBUtil.GetDB() == Global.DB_SYBASE) {
			return getMaxUserIdOld();
		}
		return DbUtils.getUnusedID("sql_tab_hgwcustomer", 1);
	}
	
	public long getMaxUserIdOld() {
		long userid = -1L;
		String str_userId = "";
		String callPro = "maxHgwUserIdProc 1";

		Map map = DataSetBean.getRecord(callPro);
		if (null != map && !map.isEmpty()) {
			str_userId = map.values().toArray()[0].toString();
		} else {
			userid = DataSetBean.getMaxId("tab_hgwcustomer",
					"user_id");
			str_userId = String.valueOf(userid);
			log.debug("----get-str_userid-from-sql-");
		}
		return Long.valueOf(str_userId);
	}
}
