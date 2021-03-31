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

import com.linkage.commons.db.PrepareSQL;
import com.linkage.litms.common.database.DataSetBean;
import com.linkage.litms.common.util.DateTimeUtil;

public class ImportUsersBBMSDAO {

	private static Logger log = LoggerFactory.getLogger(ImportUsersBBMSDAO.class);
	
	// 用户信息的临时数据集
	private Map<String, String> userMap = new HashMap<String, String>();

	// jdbc模板
	private JdbcTemplate jt;

	// 执行的sql列表
	private String[] sqlList = null;

	private ArrayList<String> sqlArr = null;

	// 标题
	private String[] title = null;

	// 导入文件的类型 0：excel文件 1：csv文件
	@SuppressWarnings("unused")
	private String fileType = "";

	// 导入文件的来源 0：BSS 1：IPOSS
	@SuppressWarnings("unused")
	private String resArea = "0";

	/**
	 * 输入excel文件解析入库，若数据已有值则更新，没有则新插入
	 * 
	 * @param file
	 *            输入的excel文件
	 */
	public String importFile(File file, String type) {

		fileType = type;
		sqlArr = new ArrayList<String>();
		String resultCode = "0";
		// 初始化列名
		title = new String[11];
		title[0] = "device_id";
		title[1] = "city_id";
		title[2] = "username";
		title[3] = "serv_type_id";
		title[4] = "linkaddress";
		title[5] = "realname";
		title[6] = "linkman";
		title[7] = "linkphone";
		title[8] = "cred_type_id";
		title[9] = "sex";
		title[10] = "address";

		// 解析excel文件
		ArrayList<Map> data = importFromExcel(file);

		// 查询当前所有的用户数据并放入map中
		getAllUser();

		// 取user_id
		long user_id = DataSetBean.getMaxId("tab_egwcustomer", "user_id");

		// 遍历数据，解析入库
		for (int i = 0; i < data.size(); i++) {
			Map dataMap = (Map) data.get(i);
			if ("1".equals(checkData(dataMap))) {
				log.debug("GSJ--------------A");
				return "lackDataErr";
			} else if ("2".equals(checkData(dataMap))) {
				log.debug("GSJ--------------B");
				return "numErr";
			} else {
				log.debug("GSJ--------------C");
				if (dataMap != null) {
					String userName = (String) dataMap.get("username");
					// 取得数据后比较数据中是否已有，若有则更新，若没有则新增
					if (userMap.containsKey(userName)) {
						updUserInfo(dataMap, (String) userMap.get(userName));
					} else {
						user_id++;
						insertUserInfo(dataMap, user_id);
					}
				}
			}
		}

		// 清空数据
		data = null;

		// 初始化sql数组
		int arrSize = sqlArr.size();
		sqlList = new String[arrSize];
		for (int j = 0; j < arrSize; j++) {
			sqlList[j] = sqlArr.get(j);
		}

		sqlArr = null;

		// 执行全部sql
		resultCode = "1"; //excuteSql();
		return resultCode;
	}

	private String checkData(Map dataMap) {
		String result = "succ";
		if (null == dataMap.get("device_id") || null == dataMap.get("city_id") || null == dataMap.get("username")
				 || null == dataMap.get("serv_type_id") || null == dataMap.get("linkaddress")
				 || null == dataMap.get("realname") || null == dataMap.get("linkman")
				 || null == dataMap.get("linkphone") || "".equals(dataMap.get("device_id")) || "".equals(dataMap.get("city_id")) || "".equals(dataMap.get("username"))
				 || "".equals(dataMap.get("serv_type_id")) || "".equals(dataMap.get("linkaddress"))
				 || "".equals(dataMap.get("realname")) || "".equals(dataMap.get("linkman"))
				 || "".equals(dataMap.get("linkphone"))) {
			result = "1";
		} else if (!"0".equals(checkIsNumber(dataMap.get("serv_type_id").toString()))) {
			result = "2";
		} else if (null != dataMap.get("cred_type_id") && !"0".equals(checkIsNumber(dataMap.get("cred_type_id").toString()))) {
			result = "2";
		}
		log.debug("GSJ--------------E:" + result);
		return result;
	}

	private String checkIsNumber(String value) {
		String result = "0";
		String s = value;
		if (s.length() > 5) {
			log.debug("您输入的数据太长!");
			result = "1";
		}
		for (int j = 0; j < s.length(); j++) {
			if (!(s.charAt(j) >= 48 && s.charAt(j) <= 57)) {
				log.debug("您输入的不是纯数字!");
				result = "2";
				break;
			}
		}
		log.debug("GSJ--------------F:" + result);
		return result;
	}

	/**
	 * 输入excel文件，解析后返回ArrayList
	 * 
	 * @param file
	 *            输入的excel文件
	 * @return ArrayList<Map>，其中的map以第一行的内容为键值
	 */
	private ArrayList<Map> importFromExcel(File file) {
		// 初始化返回值和字段名数组
		ArrayList<Map> arr = new ArrayList<Map>();

		Workbook wwb = null;
		Sheet ws = null;

		try {
			// 读取excel文件
			wwb = Workbook.getWorkbook(file);

			// 总sheet数
			int sheetNumber = wwb.getNumberOfSheets();
			log.debug("sheetNumber:" + sheetNumber);

			for (int m = 0; m < sheetNumber; m++) {
				ws = wwb.getSheet(m);

				// 当前页总记录行数和列数
				int rowCount = ws.getRows();
				int columeCount = ws.getColumns();

				log.debug("rowCount:" + rowCount);
				log.debug("columeCount:" + columeCount);

				// 第一行为字段名，所以行数大于1才执行
				if (rowCount > 1 && columeCount > 0) {

					// 取当前页所有值放入list中
					for (int i = 1; i < rowCount; i++) {

						Map<String, String> dataMap = new HashMap<String, String>();
						for (int j = 0; j < columeCount; j++) {
							dataMap.put(title[j], ws.getCell(j, i)
									.getContents());
						}

						arr.add(dataMap);
					}
				}

			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			wwb.close();

		}

		return arr;
	}

	/**
	 * 查询当前所有的用户信息，并放入临时map中
	 * 
	 */
	private void getAllUser() {

		// 查询用户名
		List list = null;
		PrepareSQL psql = new PrepareSQL("select username,user_id from tab_egwcustomer");
    	psql.getSQL();
		list = jt.queryForList("select username,user_id from tab_egwcustomer");

		// 遍历数据
		Iterator it = list.iterator();
		while (it.hasNext()) {
			Map map = (Map) it.next();

			// 若数据存在且不为空则放入map中
			String username = (String) map.get("username");
			String user_id = map.get("user_id").toString();
			if (username != null && !"".equals(username)) {
				userMap.put(username, user_id);
			}
		}
	}

	/**
	 * 更新已有的用户数据
	 * 
	 * @param dataMap
	 *            解析出来的数据集
	 */
	private void updUserInfo(Map dataMap, String user_id) {
		// 初始化sql语句
		String sql = "";

		sql += "update tab_egwcustomer ";

		String dateStr = (String) dataMap.get("dealdate");
		log.debug("GSJ-------------B:" + dateStr);

		// 更新时间取当前时间
		long updatetime = (new DateTimeUtil()).getLongTime();

		sql += "set device_id='" + dataMap.get("device_id") + "',username='"
				+ dataMap.get("username") + "',city_id='"
				+ dataMap.get("city_id") + "',username='"
				+ dataMap.get("username") + "',serv_type_id="
				+ dataMap.get("serv_type_id") + ",linkaddress='"
				+ dataMap.get("linkaddress") + "',realname='"
				+ dataMap.get("realname") + "',cred_type_id="
				+ dataMap.get("cred_type_id") + ",sex='" + dataMap.get("sex")
				+ "',address='" + dataMap.get("address") + "',linkman='"
				+ dataMap.get("linkman") + "',linkphone='"
				+ dataMap.get("linkphone") + "',dealdate=" + dateStr
				+ ",updatetime=" + updatetime + " where user_id=" + user_id;

		log.debug("updUserInfo: " + sql);
		PrepareSQL psql = new PrepareSQL(sql);
    	psql.getSQL();
		sqlArr.add(sql);
	}

	/**
	 * 新增用户数据
	 * 
	 * @param dataMap
	 *            解析出来的数据集
	 */
	private void insertUserInfo(Map dataMap, long user_id) {
		// 初始化sql语句
		String sql = "";
		@SuppressWarnings("unused")
		int serv_type_id = 50;
		sql += "insert into tab_egwcustomer";

		sql += "(user_id,username,device_id,city_id,serv_type_id,linkaddress,realname,cred_type_id,sex,address,linkman,linkphone) values("
				+ user_id
				+ ",'"
				+ dataMap.get("username")
				+ "','"
				+ dataMap.get("device_id")
				+ "','"
				+ dataMap.get("city_id")
				+ "',"
				+ dataMap.get("serv_type_id")
				+ ",'"
				+ dataMap.get("linkaddress")
				+ "','"
				+ dataMap.get("realname")
				+ "',"
				+ dataMap.get("cred_type_id")
				+ ",'"
				+ dataMap.get("sex")
				+ "','"
				+ dataMap.get("address")
				+ "','"
				+ dataMap.get("linkman")
				+ "','"
				+ dataMap.get("linkphone")
				+ "')";

		log.debug("insertUserInfo: " + sql);
		PrepareSQL psql = new PrepareSQL(sql);
    	psql.getSQL();
		sqlArr.add(sql);
	}

	/**
	 * 执行sql数组
	 * 
	 */
	@SuppressWarnings("unused")
	private String excuteSql() {

		String resultCode = "0";

		if (sqlList != null && sqlList.length > 0 && sqlList[0] != null) {
			int[] code = jt.batchUpdate(sqlList);
			if (code[0] > 0) {
				resultCode = "1";
			}
		}

		// 清空数组
		sqlList = null;
		return resultCode;
	}

	/**
	 * 初始化数据库连接
	 * 
	 * @param dao
	 */
	public void setDao(DataSource dao) {
		jt = new JdbcTemplate(dao);
	}

	public void setResArea(String resArea) {
		this.resArea = resArea;
	}
}
