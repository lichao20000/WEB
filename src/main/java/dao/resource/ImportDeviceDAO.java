package dao.resource;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import jxl.Sheet;
import jxl.Workbook;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;

import com.linkage.commons.db.PrepareSQL;

public class ImportDeviceDAO
{
	private static Logger log = LoggerFactory.getLogger(ImportDeviceDAO.class);
	
	//jdbc模板
	private JdbcTemplate jt;
	
	//标题
	private String[] title = null;
	
	//执行的sql列表
	private String[] sqlList = null;
	
	private ArrayList<String> sqlArr = null;
	
	/**
	 * 初始化数据库连接
	 * @param dao
	 */
	public void setDao(DataSource dao) {
		jt = new JdbcTemplate(dao);
	}
	
	/**
	 * 输入excel文件解析入库，若数据已有值则更新，没有则新插入
	 * @param file 输入的excel文件
	 */
	public void importFile(File file){
		sqlArr = new ArrayList<String>();
		String sql = "";
		
		//初始化列名
		title = new String[5];
		title[0] = "oui";
		title[1] = "serialnumber";
		
		//解析excel文件
		ArrayList<Map> data = importFromExcel(file);
		
		//遍历数据，解析入库
		for (int i=0;i<data.size();i++){
			Map dataMap = (Map)data.get(i);
			
			sql = "insert into tab_user_dev (oui,serialnumber) values ('" 
				+ dataMap.get("oui") + "','" + dataMap.get("serialnumber") + "')";
			
			PrepareSQL psql = new PrepareSQL(sql);
			psql.getSQL();
			
			sqlArr.add(sql);
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
		excuteSql();
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
	 * 执行sql数组
	 *
	 */
	private void excuteSql(){
		
		if (sqlList != null && sqlList.length > 0 && sqlList[0] != null ){
			jt.batchUpdate(sqlList);
		}
		
		//清空数组
		sqlList = null;
	}
}
