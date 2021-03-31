/**
 * @(#)ADSLSocketMsg.java 2007-11-27
 * 
 * Copyright 2007 联创科技.版权所有
 */

package com.linkage.litms.netcutover;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.Socket;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.linkage.commons.db.DBUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.commons.db.PrepareSQL;
import com.linkage.litms.LipossGlobals;
import com.linkage.litms.common.database.DataSetBean;
import com.linkage.module.gwms.Global;

/**
 * 
 * 
 * 
 * @author lizj
 * 
 * @version 1.00
 * 
 * @since litms 2.1
 * 
 */

public class AdslSocketMsg {

	/** log */
	private static Logger logger = LoggerFactory.getLogger(AdslSocketMsg.class);
	/** host:ip */
	private String host = null;

	/** port */
	private int port = 0;

	/** timeout(s) */
	private int soTimeout = 120;

	
	/**
	 * Constractor
	 * @param _gather_id_dev
	 * @author yanhj
	 * @date 2006-10-26
	 */
	public AdslSocketMsg(String _gather_id_dev) {
		
		String tem = "socket.gather_id_" + _gather_id_dev + ".";
        host = LipossGlobals.getLipossProperty(tem + "AdslWorkSheet.IP");
        String sPort = LipossGlobals.getLipossProperty(tem + "AdslWorkSheet.Port");
		if (sPort != null)
			this.port = Integer.parseInt(sPort);
		
		logger.debug("host :" + host + ":" + port);

	}
	/**
	 * ADSL批量工单下发 
	 * @author lizj （5202）
	 * @param request
	 * @return
	 */
	
	public String socketMsg(HttpServletRequest request){
		
		String msg = "";
		
		List socketList = new ArrayList();
		
		
//		//--------------------------------------------------------------------------------
//		//网络传真业务类型
//		String service_type700 = request.getParameter("service_type700");
//		//网络传真操作类型
//		String operate_type700  = request.getParameter("operate_type700");
//		//网络传真激活数量
//		String num700 = request.getParameter("num700");
//		//--------------------------------------------------------------------------------
		
		//用户ID
		String username = request.getParameter("username");
		//判断用户是否存在对应的设备
		if(!isExist(username)){
			msg = "用户关联的设备不存在！";
			return msg;
		}
		
		// Socket字串
		String socketString = ""; 
		
		//业务类型
		String service_type = "";
		
		//操作类型
		String operate_type = "";
		
		//激活数量
		String num = "";
		
		// 语音和软电话的特殊处理
		String values = "";
		
		for (int i=700; i < 711 ; i++){
			//业务类型
			service_type = request.getParameter("service_type" + i);
			
			//操作类型
			operate_type = request.getParameter("operate_type" + i);
			
			//选中的某个业务类型
			if(service_type != null && !service_type.equals("")){
				
				
				
				//语音留言的业务
				if(service_type.equals("704")){
					
					if(operate_type.equals("50")){
						String voip_user_num = request.getParameter("voip_user_num");
						String voip_time = request.getParameter("voip_time");
						String voip_mail_num = request.getParameter("voip_mail_num");
						
						// 按照固定格式拼接
						values = voip_user_num + "#" + voip_time + "#" + voip_mail_num;
						
					} else if(operate_type.equals("51")) {
						
						values = "-1" + "#" + "-1" + "#" + "-1";
						
					} else {
						
						values = "0" + "#" + "0" + "#" + "0";
					}
					
					num = "";
				} 
//				else if(service_type.equals("705")){ //软电话业务
//					if(operate_type.equals("50")){
//						String soft_phone_num =request.getParameter("soft_phone_num");
//						String phone_trans_num = request.getParameter("phone_trans_num");
//						//固定格式拼接
//						values = soft_phone_num + "#" + phone_trans_num;
//						
//					}else if(operate_type.equals("51")){						
//						values = "-1" + "#" + "-1";						
//					} else {
//						values = "0" + "#" + "0";
//					}	
//					num = "";
//				} 
				else if(service_type.equals("709")){ //防火墙业务
					if(operate_type.equals("50")){					
						num = "0";
					}else if(operate_type.equals("51")){						
						num = "1";				
					} else {
						num = "";
					}
				
				}
				else if(service_type.equals("707")){
					
					//如果操作类型选择的是数量激活
					if(operate_type.equals("50")){
						num = "1";
					} else {
						num = "";
					}
				} else {
					
					//如果操作类型选择的是数量激活
					if(operate_type.equals("50")){
						num = request.getParameter("num" + i);
					} else {
						num = "";
					}
					values = "";
				}
				
				logger.debug("service_type :" + service_type + "----operate_type :" + operate_type + "-----num :" + num + "---values :" + values);
				
				socketString = getSocketString(service_type,operate_type,username,num,values);
				socketList.add(socketString);
			}
		}
		//发送到socket接口，进行开通！
		if(socketSend(socketList)){
			msg = "工单发送成功";
		} else{
			msg = "工单发送失败";
		}
		
		
		return msg;
		
	}
	
	/**
	 * 查询用户以及对应的设备是否存在
	 * @param user_name
	 * @return
	 */
	public boolean isExist(String username){
		String sql = "select * from tab_egwcustomer a,tab_gw_device b where a.username='" + username + "' and a.device_id=b.device_id ";
		// teledb
		if (DBUtil.GetDB() == Global.DB_MYSQL) {
			sql = "select b.device_id from tab_egwcustomer a,tab_gw_device b where a.username='" + username + "' and a.device_id=b.device_id ";
		}
		PrepareSQL psql = new PrepareSQL(sql);
    	psql.getSQL();
		Map fields = DataSetBean.getRecord(sql);
		//logger.debug("sql : select * from cus_radiuscustomer a,tab_deviceresource b where a.username='" + user_id + "' and a.device_id = b.device_id");
		if(fields != null){
			return true;
		}
		return false;
	}
	
	/**
	 * 
	 * @param service_type
	 * @param operate_type
	 * @param user_name
	 * @param number
	 * @param starff_id
	 * @param m
	 * @return
	 */
	public String getSocketString(String service_type,String operate_type,String user_name,String number,String values){
		
		StringBuffer work = new StringBuffer();
		
		Date dt = new Date();
		long lms = dt.getTime();
		String dtStr = getDateTimeStr("yyyyMMddHHmmss", lms);
		String sql = "select * from tab_egwcustomer a,tab_gw_device b where a.username='" + user_name + "' and a.device_id=b.device_id ";
		// teledb
		if (DBUtil.GetDB() == Global.DB_MYSQL) {
			sql = "select b.city_id, b.device_id, b.loopback_ip from tab_egwcustomer a,tab_gw_device b where a.username='" + user_name + "' and a.device_id=b.device_id ";
		}
		PrepareSQL psql = new PrepareSQL(sql);
    	psql.getSQL();
		Map fields = DataSetBean.getRecord(sql);
		if(fields != null){
			
			String city_id  = (String)fields.get("city_id");
			String device_id = (String)fields.get("device_id");
			String loopback_ip = (String)fields.get("loopback_ip");
			
			// 加密字、97属地标识	
			work.append("web|||").append(city_id).append("|||"); 		
			// 工单唯一标识
			work.append(dtStr).append("_").append(device_id).append("|||");			
			// 业务唯一标识
			work.append("bws_").append(dtStr).append("_").append(device_id).append("|||");
			// 业务受理时间
			work.append(dtStr).append("|||");
			// 业务类型
			work.append(service_type).append("|||");
			// 操作类型
			work.append(operate_type).append("|||");
			// 处理级别、保留字段
			work.append("5|||0|||");
			
			
			//BAS设备编码
			work.append("|||");
			//BAS设备IP
			work.append("|||");
			//BASE设备机架号
			work.append("|||");
			//BASE设备框号
			work.append("|||");
			//BAS设备槽位号
			work.append("|||");
			
			//BAS设备端口
			if(service_type.equals("704")){
				if(values != null && !values.equals("")){
					work.append((String)values.split("#")[0]);
				}
			}
			if(service_type.equals("705")){
				if(values != null && !values.equals("")){
					work.append((String)values.split("#")[0]);
				}
			}
			work.append("|||");
			//VlanID号
			work.append("|||");
			//BAN设备编码
			work.append("|||");
			//BAN设备IP地址
			work.append(loopback_ip).append("|||");
			
			//BAN设备框号
			if(service_type.equals("704")){
				if(values != null && !values.equals("")){
					work.append((String)values.split("#")[1]);
				}
			}
			if(service_type.equals("705")){
				if(values != null && !values.equals("")){
					work.append((String)values.split("#")[1]);
				}
			}
			work.append("|||");
			
			//BAN设备槽位号
			if(service_type.equals("704")){
				if(values != null && !values.equals("")){
					work.append((String)values.split("#")[2]);
				}
			}			
			work.append("|||");
			//BAN设备端口号
			work.append(number).append("|||");
			//用户帐号
			work.append(user_name).append("\n");
			
			logger.debug("socket work :" + work.toString());
		}
		
		
		return work.toString();
		
	}
	
	/**
	 * 
	 * 
	 * @param pattern
	 * @param lms
	 * @return
	 * @author lizj
	 * @date 2007-11-27
	 */
	private String getDateTimeStr(String pattern, long lms) {
		SimpleDateFormat formatter = new SimpleDateFormat(pattern);
		Date nowc = new Date(lms);
		return formatter.format(nowc);
	}
	
	
	/**
	 * 
	 * @param worklist
	 * @return
	 */
	public boolean socketSend(List worklist) {
		boolean result = false;
		Socket con = null;
		try {

			con = new Socket(host, port);
			if (con != null && con.isConnected()) {

				con.setSoTimeout(soTimeout * 1000);
				Writer out = new OutputStreamWriter(con.getOutputStream());
				for (int i = 0; i < worklist.size(); i++) {
					out.write((String) worklist.get(i));
					out.write("\n");
					out.flush();
				}
				result = true;
				out.close();
				out = null;
			}

		} catch (UnknownHostException e) {
			logger.warn(e.getMessage());
			e.printStackTrace();
		} catch (IOException e) {
			logger.warn(e.getMessage());
			e.printStackTrace();
		} catch (Exception e) {
			logger.warn(e.getMessage());
			e.printStackTrace();
		} finally {

			try {
				if (con != null) {
					con.close();
					con = null;
				}
			} catch (IOException e) {
				con = null;
			}
		}		
		worklist = null;
		return result;
	}
}
