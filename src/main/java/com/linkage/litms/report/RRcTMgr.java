package com.linkage.litms.report;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.commons.db.DBUtil;
import com.linkage.commons.db.PrepareSQL;
import com.linkage.litms.LipossGlobals;
import com.linkage.litms.common.database.Cursor;
import com.linkage.litms.common.database.DataSetBean;
import com.linkage.litms.common.database.QueryPage;
import com.linkage.module.gwms.Global;

/**
 * @author Bruce(工号) tel：12345678
 * @version 1.0
 * @since Jun 17, 2008
 * @category com.linkage.litms.report 版权：南京联创科技 网管科技部
 * 
 */
public class RRcTMgr {
	/** log */
	private static Logger logger = LoggerFactory.getLogger(RRcTMgr.class);
    // 程序管理接口命令集合
    /**
     * 关闭应用程序命令
     */
    public static final int SHUTDOWN_APPLICATION = -1;

    /**
     * 即时生成报表
     */
    public static final int REALTIME_GATHER = 1010;
    /**
     * 即时发送报表
     */
    public static final int REALTIME_SEND = 1011;
    /**
     * 即时生成报表并发送
     */
    public static final int REALTIME_GATHER_SEND = 1012;

    /**
     * 更新报表策略
     * 
     * @param request
     * @return
     */
    public synchronized boolean updatePolicy(HttpServletRequest request) {
		String device_id = request.getParameter("device_id");
		if (null == device_id || "".equals(device_id)) {
			return false;
		}
		String send_type = request.getParameter("send_type");
		if (null == send_type) {
			send_type = "0";
		}
		String policy = request.getParameter("policy");
		if (null == policy) {
			policy = "";
		}
		String customize = request.getParameter("customize");
		if (null == customize) {
			customize = "";
		}
		String retry = request.getParameter("retry");
		if (null == retry) {
			retry = "";
		}
		String email = request.getParameter("email");
		if (null == email) {
			email = "";
		}
		String querySql = "select count(1) num from tab_rrct_policy where device_id='"
			+ device_id + "'";
		// teledb
		if (DBUtil.GetDB() == Global.DB_MYSQL) {
			querySql = "select count(*) num from tab_rrct_policy where device_id='"
					+ device_id + "'";
		}
		PrepareSQL psql = new PrepareSQL(querySql);
		psql.getSQL();
		Map map = DataSetBean.getRecord(querySql);
		String updateSql = "";
		if (null != map) {
			String num = (String) map.get("num");
			if ("0".equals(num)) {
			updateSql = "insert into tab_rrct_policy values ('" + device_id
				+ "'," + send_type + "," + retry + ",'" + policy
				+ "','" + customize + "','" + email + "')";
			} else {
			updateSql = "update tab_rrct_policy set send_type=" + send_type
				+ ", retry=" + retry + ", policy='" + policy
				+ "', customize='" + customize + "', email='" + email
				+ "' where device_id='" + device_id + "'";
			}
		} else {
			updateSql = "insert into tab_rrct_policy values ('" + device_id
				+ "'," + send_type + "," + retry + ",'" + policy + "','"
				+ customize + "','" + email + "')";
		}
		PrepareSQL psql1 = new PrepareSQL(updateSql);
		psql1.getSQL();
		int ret = DataSetBean.executeUpdate(updateSql);
		if (ret >= 1) {
			return true;
		} else {
			return false;
		}
    }
    /**
     * 生成并发送报告
     * @param request
     * @return
     */
    public String buildAndSend(HttpServletRequest request) {
	String ret = null;
	String query_type = request.getParameter("query_type");
	String deviceId = request.getParameter("device_id");
	String startTime = request.getParameter("startTime");
	String endTime = request.getParameter("endTime");
	String isSend = request.getParameter("isSend");
	String param = query_type + "|" + deviceId + "|" + startTime + "|" + endTime + "|" + isSend;
	logger.debug("&&&&&&&&&&&&&&&&&&&&&&&& param=" + param);

	byte[] command = TransDataUtil.getBytes(REALTIME_GATHER_SEND, 4);
	byte[] ss_ = param.getBytes();

	int len = ss_.length;
	byte[] ss_len = TransDataUtil.getBytes(len, 4);

	byte[] b = new byte[command.length + ss_len.length + ss_.length];

	System.arraycopy(command, 0, b, 0, command.length);
	System.arraycopy(ss_len, 0, b, command.length, ss_len.length);
	System.arraycopy(ss_, 0, b, (command.length + ss_len.length),
		ss_.length);
	byte[] t = new byte[4];
	System.arraycopy(b, 0, t, 0, 4);

	byte[] tt = new byte[4];
	System.arraycopy(b, 4, tt, 0, 4);

//	byte[] ttt = new byte[28];
//	System.arraycopy(b, 8, ttt, 0, 28);

	Socket socket=null;
	BufferedReader in=null;
    DataOutputStream out=null;
	try {
	    logger.debug("&&&&&&&&&&&&&&&& get into socket ....");
	    String ip = LipossGlobals.getLipossProperty("rrct.manager.ip");
	    String port = LipossGlobals.getLipossProperty("rrct.manager.port");
	    // logger.debug("ip=" + ip);
	    // logger.debug("port=" + port);
	    socket = new Socket(ip, Integer.parseInt(port));
	    socket.setSoTimeout(10000);
	    
	    if (socket.isConnected()) {
		out = new DataOutputStream(socket.getOutputStream());
		out.write(b);
		out.flush();
		in = new BufferedReader(new InputStreamReader(socket
			.getInputStream()));
		ret = in.readLine();
		return ret;
	    }
	} catch (UnknownHostException e1) {
	    e1.printStackTrace();
	} catch (IOException e1) {
	    e1.printStackTrace();
	}finally{
		try {
			if(in!=null){
				in.close();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		try {
			if(out!=null){
				out.close();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		try {
			if(socket!=null){
				socket.close();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	return ret;
    }
    /**
     * 报表查询
     * 
     * @param request
     * @return
     */
    public List queryReportList(HttpServletRequest request) {
	List list = new ArrayList();
	// 查找条件
	String query_type = request.getParameter("query_type");
	if (query_type == null)
	    query_type = "";
	String device_serialnumber = request
		.getParameter("device_serialnumber");
	if (device_serialnumber == null)
	    device_serialnumber = "";
	String user_name = request.getParameter("user_name");
	if (user_name == null)
	    user_name = "";
	String customer_name = request.getParameter("customer_name");
	if (customer_name == null)
	    customer_name = "";
	String is_send = request.getParameter("is_send");
	if (is_send == null)
	    is_send = "";
	String report_type = request.getParameter("report_type");
	if (report_type == null)
	    report_type = "";
	String startTime = request.getParameter("startTime");
	if (startTime == null)
	    startTime = "";
	String endTime = request.getParameter("endTime");
	if (endTime == null)
	    endTime = "";

	String sql = "select r.*, d.device_serialnumber from tab_rrct_report r, tab_gw_device d";
	// teledb
	if (DBUtil.GetDB() == Global.DB_MYSQL) {
		sql = "select r.build_time, r.report_type, r.is_send, r.is_suggest, r.file_path, r.remark, d.device_serialnumber " +
				" from tab_rrct_report r, tab_gw_device d";
	}
	String param = "";
	param += "&query_type=" + query_type;
	if ("1".equals(query_type)) {
	    sql += " where r.device_id=d.device_id";
	    sql += " and r.device_id in (select device_id from tab_gw_device where 1=1 ";
	    if(device_serialnumber.length()>5){
	    	sql += " and dev_sub_sn ='" + device_serialnumber.substring(device_serialnumber.length()-6, device_serialnumber.length()) + "'";
		}
	    sql += " and device_serialnumber like '%"
		    + device_serialnumber + "')";
	    param += "&device_serialnumber=" + device_serialnumber;
	} else if ("2".equals(query_type)) {
	    sql += ", tab_customerinfo c where c.customer_name like '%"
		    + customer_name
		    + "%' and c.customer_id=d.customer_id and d.device_id=r.device_id";
	    param += "&customer_name=" + customer_name;
	} else {
	    sql += ", tab_customerinfo c, tab_egwcustomer e where e.username = '"
		    + user_name
		    + "' and e.customer_id=c.customer_id and c.customer_id=d.customer_id and d.device_id=r.device_id";
	    param += "&user_name=" + user_name;
	}

	if (!"".equals(is_send) && !"-1".equals(is_send)) {
	    sql += " and r.is_send=" + is_send;
	}
	param += "&is_send=" + is_send;
	if (!"".equals(report_type) && !"-1".equals(report_type)) {
	    sql += " and r.report_type=" + report_type;
	}
	param += "&report_type=" + report_type;
	if (startTime.equals(endTime) && !"".equals(startTime)) {
	    sql += " and r.build_time=" + startTime;
	} else {
	    if (!"".equals(startTime)) {
		sql += " and r.build_time>=" + startTime;
	    }
	    if (!"".equals(endTime)) {
		sql += " and r.build_time<=" + endTime;
	    }
	}
	param += "&startTime=" + startTime;
	param += "&endTime=" + endTime;

	PrepareSQL psql = new PrepareSQL(sql);
	psql.getSQL();
	String stroffset = request.getParameter("offset");

	int pagelen = 50;
	int offset;
	if (stroffset == null) {
	    offset = 1;
	} else {
	    offset = Integer.parseInt(stroffset);
	}
	QueryPage qryp = new QueryPage();
	qryp.initPage(sql, offset, pagelen);
	Cursor cursor = DataSetBean.getCursor(sql, offset, pagelen);
	// logger.debug("$$$$$$$$$$$$$$$$$$$$$ param=" + param);

	String strBar = qryp.getPageBar(param);

	list.add(strBar);
	list.add(cursor);
	return list;
    }

    /**
     * 查询设备策略配制信息
     * 
     * @param request
     * @return
     */
    public Cursor queryPolicy(HttpServletRequest request) {
		String[] device_id = request.getParameterValues("device_id");
		String sql = "select r.*, d.device_serialnumber from tab_rrct_policy r, tab_gw_device d where r.device_id=d.device_id and r.device_id in (";
		// teledb
		if (DBUtil.GetDB() == Global.DB_MYSQL) {
			sql = "select r.send_type, r.retry, r.policy, r.customize, r.email, d.device_serialnumber " +
					" from tab_rrct_policy r, tab_gw_device d where r.device_id=d.device_id and r.device_id in (";
		}
		String ss = null;
		if (null != device_id) {
			for (String s : device_id) {
			if (null != s) {
				if (ss != null) {
				sql += ",";
				}
				sql += "'" + s + "'";
				ss = s;
			}
			}
		}
		sql += ")";
		PrepareSQL psql = new PrepareSQL(sql);
		psql.getSQL();
		return DataSetBean.getCursor(sql);
    }

    public List<Map<String, String>> getNotSendFilesInfo(
	    HttpServletRequest request) {
		String device_id = request.getParameter("device_id");
		List<Map<String, String>> list = new ArrayList<Map<String, String>>();
		// 要改进，将来要排除实时的邮件
		String sql = "select * from tab_rrct_report where is_send=0";
		// teledb
		if (DBUtil.GetDB() == Global.DB_MYSQL) {
			sql = "select file_path, build_time from tab_rrct_report where is_send=0";
		}
		String html = "";
		PrepareSQL psql = new PrepareSQL(sql);
		psql.getSQL();
		Cursor cursor = DataSetBean.getCursor(sql);
		if (cursor.getRecordSize() != 0) {
			html += "<select name='fileList'>";
			Map fields = cursor.getNext();
			while (fields != null) {
			String file_path = (String) fields.get("file_path");
			long build_time = Long.parseLong((String) fields
				.get("build_time"));
			SimpleDateFormat df = new SimpleDateFormat(
				"yyyy-MM-dd hh:mm:ss");
			String time = df.format(new Date(build_time));
			html += "<option value='" + file_path + "'>" + time;
			fields = cursor.getNext();
			}
			html += "</select>";
		}
		return list;
    }

    public String delFile(HttpServletRequest request) {
	String file_path = request.getParameter("file_path");
	if (null != file_path && !"".equals(file_path)) {
	    String[] files = file_path.split("\\|");
	    if (files != null) {
		for (int i = 0; i < files.length; i++) {
		    String path = files[i];
		    if (null != path && !"".equals(path)) {
			File file = new File(path);
			if (file.exists()) {
			    if (file.delete()) {
				String sql = "delete from tab_rrct_report where file_path='"
					+ path + "'";
				PrepareSQL psql = new PrepareSQL(sql);
		        psql.getSQL();
				DataSetBean.executeUpdate(sql);
			    }
			}
		    }
		}
	    }
	}
	return "";
    }

    public String sendFile(HttpServletRequest request) {

	String file_path = request.getParameter("file_path");

	byte[] command = TransDataUtil.getBytes(REALTIME_SEND, 4);
	byte[] ss_ = file_path.getBytes();

	int len = ss_.length;
	byte[] ss_len = TransDataUtil.getBytes(len, 4);

	byte[] b = new byte[command.length + ss_len.length + ss_.length];

	System.arraycopy(command, 0, b, 0, command.length);
	System.arraycopy(ss_len, 0, b, command.length, ss_len.length);
	System.arraycopy(ss_, 0, b, (command.length + ss_len.length),
		ss_.length);
	byte[] t = new byte[4];
	System.arraycopy(b, 0, t, 0, 4);

	byte[] tt = new byte[4];
	System.arraycopy(b, 4, tt, 0, 4);

//	byte[] ttt = new byte[28];
//	System.arraycopy(b, 8, ttt, 0, 28);

	Socket socket=null;
	BufferedReader in=null;
    DataOutputStream out=null;
	try {
	    // logger.debug("get into socket ....");
	    String ip = LipossGlobals.getLipossProperty("rrct.manager.ip");
	    String port = LipossGlobals.getLipossProperty("rrct.manager.port");
	    // logger.debug("ip=" + ip);
	    // logger.debug("port=" + port);
	    socket = new Socket(ip, Integer.parseInt(port));
	    socket.setSoTimeout(10000);
	    
	    if (socket.isConnected()) {
		out = new DataOutputStream(socket.getOutputStream());
		out.write(b);
		out.flush();
		in = new BufferedReader(new InputStreamReader(socket
			.getInputStream()));
		String ret = in.readLine();
		return ret;
	    } else {
		return "";
	    }
	} catch (UnknownHostException e1) {
	    e1.printStackTrace();
	    return "";
	} catch (IOException e1) {
	    e1.printStackTrace();
	    return "";
	}finally{
		try {
			if(in!=null){
				in.close();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		try {
			if(out!=null){
				out.close();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		try {
			if(socket!=null){
				socket.close();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
    }

    public String gatherNow(HttpServletRequest request) {
	String device_time = request.getParameter("device_time");
	String gather_time = request.getParameter("gather_time");

	byte[] command = TransDataUtil.getBytes(REALTIME_GATHER, 4);
	byte[] ss_ = (device_time + "_" + gather_time).getBytes();

	int len = ss_.length;
	byte[] ss_len = TransDataUtil.getBytes(len, 4);

	byte[] b = new byte[command.length + ss_len.length + ss_.length];

	System.arraycopy(command, 0, b, 0, command.length);
	System.arraycopy(ss_len, 0, b, command.length, ss_len.length);
	System.arraycopy(ss_, 0, b, (command.length + ss_len.length),
		ss_.length);
	byte[] t = new byte[4];
	System.arraycopy(b, 0, t, 0, 4);

	byte[] tt = new byte[4];
	System.arraycopy(b, 4, tt, 0, 4);

//	byte[] ttt = new byte[28];
//	System.arraycopy(b, 8, ttt, 0, 28);

	Socket socket=null;
	try {
	    logger.debug("get into socket ....");
	    // Socket socket = new Socket("192.168.228.192", 5147);
	    socket = new Socket("192.9.100.1", 5147);
	    logger.debug("socket ip=" + "192.9.100.1");
	    BufferedReader in;
	    DataOutputStream out;
	    if (socket.isConnected()) {
		in = new BufferedReader(new InputStreamReader(socket
			.getInputStream()));
		out = new DataOutputStream(socket.getOutputStream());
		out.write(b);
		out.flush();
	    }
	} catch (UnknownHostException e1) {
	    e1.printStackTrace();
	} catch (IOException e1) {
	    e1.printStackTrace();
	}finally{
		try {
			if(socket!=null){
				socket.close();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	// 发送返回值
	// session.write(ByteBuffer.wrap(b));
	return "";
    }
}
