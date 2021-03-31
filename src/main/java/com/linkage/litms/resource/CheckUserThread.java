package com.linkage.litms.resource;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.commons.db.PrepareSQL;
import com.linkage.litms.LipossGlobals;
import com.linkage.litms.common.database.Cursor;
import com.linkage.litms.common.database.DataSetBean;

/**
 * 读取radius发送的用户数据文件，解析并测试当前用户的设备是否能够管理的线程
 * @author chenzm
 *
 */

public class CheckUserThread extends Thread {
	/** log */
	private static Logger logger = LoggerFactory.getLogger(CheckUserThread.class);
	private CheckUserThreadPool manager = new CheckUserThreadPool(20);
	private HashMap onlineMap = new HashMap();
	private HashMap userCheckMap = new HashMap();
	private static HashMap outLineMap = new HashMap();
	private String path = "";
	private static boolean isRefresh = false;
	public static Map iorMap = new HashMap();
	
	public CheckUserThread(){
		super();
		
		//radius文件的路径
		path = LipossGlobals.getLipossProperty("radius.filePath");
		
		//初始化ior
		initIOR();
	}
	
	/**
	 * 自动解析radius发送的在线用户数据线程
	 */
	public void run(){
		while (true){	
			try{
				logger.debug("=========check username begin!");
				
				if (isRefresh){
					initIOR();
				}
				
				//获取用户数据
				getOnLine();
				getOutLine();
				
				//处理用户数据
				checkUser();
				
				logger.debug("=========check username end!");
				
				
			}
			catch(Exception e){
				e.printStackTrace();
			}
			finally{
				onlineMap.clear();
				
				//休眠15分钟
				try
				{
					sleep(900000);
				}
				catch(Exception e)
				{
					e.printStackTrace();
				}
			}
		}
		
	}
	
	/**
	 * 解析radius发送的在线用户文件
	 * @throws IOException 
	 */
	private void getOnLine(){
		FileReader radiusFile = null;
		BufferedReader br = null;
		String tmp = "";
		String[] info = null;
		
		//radius发送的文件列表
		File f = new File(path);
		File[] fileList = f.listFiles();
		
		//读取每个文件的数据
		if (fileList != null && fileList.length>0){
			for (int i=0;i<fileList.length;i++){
				try{
					logger.debug("radiusFile_name:"+ fileList[i].getPath());
					radiusFile = new FileReader(fileList[i]);
					br = new BufferedReader(radiusFile);
					tmp = br.readLine();
					
					while (tmp != null){
						info = tmp.split("\\|");
						
						if (info != null && info.length > 1){
							onlineMap.put(info[1], info[0]);
						}
						
						//读取下一行
						tmp = br.readLine();
					}
					
				}
				catch(Exception e){
					e.printStackTrace();
				}
				finally{
					try{
						//关闭流
						radiusFile.close();
					}
					catch(Exception e){
						e.printStackTrace();
					}
					
					try{
						//关闭流
						br.close();
					}
					catch(Exception e){
						e.printStackTrace();
					}
				}
				
				//删除处理完成的文件
				fileList[i].delete();
			}
		}
	}
	
	/**
	 * 查询当前系统中的所有用户数据
	 */
	private Cursor getAllUser(){
		String sql = "select username from tab_hgwcustomer where user_state = '1'";
		PrepareSQL psql = new PrepareSQL(sql);
    	psql.getSQL();
		Cursor cursor = DataSetBean.getCursor(sql);
		
		return cursor;
	}
	
	/**
	 * 查询当前系统中的所有掉线用户数据
	 */
	private void getOutLine(){
		String sql = "select username,outdate from tab_outlineuser";
		PrepareSQL psql = new PrepareSQL(sql);
    	psql.getSQL();
		Cursor cursor = DataSetBean.getCursor(sql);
		Map fields = cursor.getNext();
		
		while (fields != null){
			outLineMap.put(fields.get("username"), fields.get("outdate"));
			fields = cursor.getNext();
		}
	}
	
	/**
	 * 用系统中的用户数据进行遍历对比
	 * @throws InterruptedException 
	 */
	private void checkUser() throws InterruptedException{
		String username = "";
		boolean isCheck = false;
		String tmpDate = "";
		long checkTime = 0;
		long nowTime = (new Date()).getTime();
		String city_id = "";
		
		//获取当前itms中的所有用户数据
		Cursor cursor = getAllUser();
		Map fields = cursor.getNext();
		
		while (fields != null){
			username = (String)fields.get("username");
			
			//将状态初始化
			isCheck = false;
			
			//用户在线时进行如下操作
			if (onlineMap.containsKey(username)){
				
				//上一次测试连接的时间
				tmpDate = (String)userCheckMap.get(username);
				
				if (tmpDate != null && !"".equals(tmpDate)){
					checkTime = Long.parseLong(tmpDate);
				}
				else{
					checkTime = 0;
				}
				
				//判断时间是否大于一个小时
				if ((nowTime - checkTime) > 3600000){
					isCheck = true;
				}
				else{
					isCheck = false;
				}
				
				//当间隔时间大于一个小时时再进行测试连接
				if (isCheck){
					userCheckMap.put(username, String.valueOf(nowTime));
					city_id = (String)onlineMap.get(username);
					
					logger.debug("check username:"+username+"  city_id:"+city_id);
					
					//建立线程进行测试连接
					boolean isProcess = manager.process(username,city_id);
					
					int turn = 0;
					
					//若建立线程不成功则每过1分钟执行一次，循环10次
					while (!isProcess){
						sleep(60000);
						isProcess = manager.process(username,city_id);
						
						//循环超过10次后退出
						turn++;
						if (turn > 10){
							logger.warn("建立线程失败!");
							break;
						}
					}
					
					logger.debug("建立线程标志 isProcess：" + isProcess);
				}
			}
			fields = cursor.getNext();
		}
	}
	
	/**
	 * 进行数据库操作
	 */
	public static void informDB(String username, String city_id, boolean isLinked){
		
		long nowTime = (new Date()).getTime();
		
		//测试连接成功
		if (isLinked){
			if (outLineMap.containsKey(username)){
				//从表里删除数据
				delOutLineInfo(username);
			}
		}
		//测试连接失败
		else{
			if (!outLineMap.containsKey(username)){
				//向表里增加数据
				addOutLineInfo(username,nowTime,city_id);
			}
		}
	}
	
	/**
	 * 增加新的掉线用户数据
	 */
	private static void addOutLineInfo(String username,long time,String city_id){
		
		String sql = "insert into tab_outlineuser(username,outdate,city_id) values ('"
					+ username + "'," + time/1000 + ",'" + city_id + "')";
		PrepareSQL psql = new PrepareSQL(sql);
    	psql.getSQL();
		
		DataSetBean.executeUpdate(sql);
	}
	
	/**
	 * 删除掉线用户数据
	 */
	private static void delOutLineInfo(String username){
		String sql = "delete from tab_outlineuser where username='" + username + "'";
		PrepareSQL psql = new PrepareSQL(sql);
    	psql.getSQL();
		
		DataSetBean.executeUpdate(sql);
	}
	
	/**
	 * 查询当前系统中的所有的ior，并将其放入iorMap中
	 */
	private void initIOR(){
		String gather_id = "";
		String object_name = "";
		String object_Poaname = "";
		String ior = "";
		
		//查询所有的gather_id
		String sql = "select distinct gather_id from tab_process_desc";
		PrepareSQL psql = new PrepareSQL(sql);
    	psql.getSQL();
		Cursor cursor = DataSetBean.getCursor(sql);
		Map fields = cursor.getNext();
		
		//根据gather_id查询ior
		while (fields != null){
			gather_id = (String)fields.get("gather_id");
			object_name = "ACS_" + gather_id;
			object_Poaname = "ACS_Poa_" + gather_id;
			
			String sql1 = "select ior from tab_ior where object_name = '" 
						+ object_name + "' and object_poa = '" + object_Poaname + "'";
			PrepareSQL psql1 = new PrepareSQL(sql1);
	    	psql1.getSQL();
			Cursor cursor1 = DataSetBean.getCursor(sql1);
			Map fields1 = cursor1.getNext();
			
			if (fields1 != null){
				ior = (String)fields1.get("ior");
				
				if (ior != null){
					iorMap.put(gather_id, ior);
				}
			}
			fields = cursor.getNext();
		}
	}
	
	public static void setRefreshIOR(boolean flag){
		isRefresh = flag;
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		//CheckUserThread a = new CheckUserThread();
		//a.start();
		
		String a = "0011|yca8725269";
		String[] b = a.split("\\|");

	}

}
