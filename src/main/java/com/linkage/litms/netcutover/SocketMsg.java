/**
 * @(#)SocketMsg.java 2006-1-19
 * 
 * Copyright 2005 联创科技.版权所有
 */
package com.linkage.litms.netcutover;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Vector;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.litms.LipossGlobals;

/**
 * 通过TCP协议建立socket连接与C++通信的客户端.
 * 
 * @author yanhj
 * @version 1.00
 * @since Liposs 2.1
 */
public class SocketMsg {
	/** log */
	private static Logger logger = LoggerFactory.getLogger(SocketMsg.class);
	/**host:ip*/
	private String host = null;
	/**port*/
	private int port = 0;
	/**the result message of sending*/
	private String msg;
	/**recive back message or not*/
	private boolean blnBack = false;
	/**timeout(s)*/
	private int soTimeout = 10;
	/**debug*/
	private static boolean DEBUG = true;
	/**the message to be sent*/
	private Vector list = null;
	
	/**
	 * Constrator:IP,Port,isCallBack.
	 */
	public SocketMsg(){
		this.host = LipossGlobals.getLipossProperty("socket.NetCutover.IP");
		
		String sPort = LipossGlobals.getLipossProperty("socket.NetCutover.Port");
		if(sPort != null)
			this.port = Integer.parseInt(sPort);
			
		String sBlnBack = LipossGlobals.getLipossProperty("socket.NetCutover.IsCallBack");
		if(sBlnBack != null)
			this.blnBack = Boolean.getBoolean(sBlnBack);
		
		this.msg = "Socket 连接未初始化";			
	}
	
	/**
	 * Constrator:分发不同的采集机.
	 * 
	 * @desc 江苏集中移植.
	 * @author yanhj
	 * @date 2006-7-28
	 * 
	 */
	public SocketMsg(String gather_id){
		String tem = "socket.gather_id_" + gather_id + ".";
		this.host = LipossGlobals.getLipossProperty(tem + "NetCutover.IP");
		
		String sPort = LipossGlobals.getLipossProperty(tem + "NetCutover.Port");
		if(sPort != null)
			this.port = Integer.parseInt(sPort);
			
		String sBlnBack = LipossGlobals.getLipossProperty(tem + "NetCutover.IsCallBack");
		if(sBlnBack != null)
			this.blnBack = Boolean.getBoolean(sBlnBack);
		
		this.msg = "Socket 连接未初始化";			
	}
	
	/**
	 * Constrator:IP,Port,isCallBack.
	 * 
	 * @param _host String 
	 * @param _port int 
	 * @param _back boolean
	 */
	public SocketMsg(String _host, int _port, boolean _back){
		this.host = _host;
		this.port = _port;
		this.blnBack  = _back;
		this.msg = "Socket 连接未初始化";
	}
	
	/**
	 * 设置通信服务器地址
	 * 
	 * @param _host String类型 代表要通信的服务器地址
	 */
	public void setHost(String _host){
		this.host = _host;
	}
	
	/**
	 * 设置通信端口
	 * 
	 * @param _port 整形 代表要通信的端口
	 */
	public void setPort(int _port){
		this.port = _port;
	}
	
	/**
	 * 获取Socket发送后返回的信息
	 * @return 消息内容.(String)
	 */
	public String getMsg() {
		return msg;
	}

	/**
	 * 设置是否接收服务器信息
	 * @param b
	 */
	public void setBlnBack(boolean _blnBack) {
		this.blnBack = _blnBack;
	}
	
	/**
	 * 设置时延(秒)
	 * @param i	时间.(int)
	 */
	public void setSoTimeout(int i) {
		soTimeout = i;
	}

	/**
	 * 设置传输数据
	 * @param _msg 传输数据内容.(String)
	 */
	public void addBatch(String _msg){
		if(list == null) list = new Vector();
		list.add(_msg);
	}
	
	/**
	 * 建立socket连接,发送消息
	 * @return	boolean 发送结果
	 */
	public boolean doBatch(){
		boolean result = false;
		Socket con = null;
		
		if(DEBUG)
			logger.debug(host+" "+port+"\n");
		try {
			con = new Socket(host, port);
			logger.debug("work length = "+list.size());
			if(con != null && con.isConnected()){
				con.setSoTimeout(soTimeout*1000);
				Writer out = new OutputStreamWriter(con.getOutputStream());
				String s_msg;
				
				for(int i=0;i<list.size();i++){
					s_msg = (String)list.get(i);
					if(DEBUG) logger.debug(s_msg);
					out.write(s_msg);
					out.write("\n");
					out.flush();
				}
				this.msg = "Socket 传输数据成功";
				result = true;
				out.close();
				out = null;
			}
		}catch (UnknownHostException e) {
			this.msg = e.getMessage(); 
		} catch (IOException e) {
			this.msg = e.getMessage(); 
		} catch (Exception e) {
			this.msg = e.getMessage();
		} finally {
			try {
				if (con != null){
					con.close();
					con = null;
				}	
			} 
			catch (IOException e) {
			}
		}
				
		return result;
	}
	
	
	/**
	 * 建立socket连接,发送消息
	 * @param list (Vector)
	 * @return 发送结果.(boolean)
	 * @see (#)doBatch().
	 */
	public boolean doBatch(Vector list){
		boolean result = false;
		if (DEBUG) {
			for (int i = 0; i < list.size(); i++) {
				logger.debug(list.get(i).toString());
			}
		}
		
		Socket con = null;
		if (DEBUG)
			logger.debug(host + " " + port);
		try {
			con = new Socket(host, port);
			if (con != null && con.isConnected()) {
				con.setSoTimeout(soTimeout * 1000);
				Writer out = new OutputStreamWriter(con.getOutputStream());
				for (int i = 0; i < list.size(); i++) {
					out.write((String) list.get(i));
					out.write("\n");
					out.flush();
				}

				this.msg = "Socket 传输数据成功";
				result = true;
				out.close();
				out = null;
			}
		} catch (UnknownHostException e) {
			this.msg = e.getMessage();
		} catch (IOException e) {
			this.msg = e.getMessage();
		} catch (Exception e) {
			this.msg = e.getMessage();
		} finally {
			try {
				if (con != null) {
					con.close();
					con = null;
				}
			} catch (IOException e) {
			}
		}
		
		return result;
	}
	
	
	/**
	 * 发送字符串到服务器,并判断是否接收服务器信息.
	 * 
	 * @param msg String类型 待发送的字符串
	 * @return boolean类型，发送成功返回true，否则返回false
	 */
	public boolean SendMsg(String _msg) {
		boolean result = false;
		Socket con = null;
		if(DEBUG)
			logger.debug(host+" "+port+"\n"+_msg);
		try {
			con = new Socket(host, port);
			if(con != null && con.isConnected()){
				con.setSoTimeout(soTimeout*1000);
				Writer out = new OutputStreamWriter(con.getOutputStream());
				out.write(_msg);
				out.write("\n");
				out.flush();
			
				if(blnBack){
					InputStream in = con.getInputStream();
					BufferedInputStream buffer = new BufferedInputStream(in);
					InputStreamReader r = new InputStreamReader(buffer);
					int c;
					StringBuffer sb = new StringBuffer(100);
					while ((c = r.read()) != -1) {
						if (c == '\r' || c == '\n' || c == -1)
							break;
						sb.append((char) c);
					}
					r.close();
					r = null;
					this.msg = sb.toString();
					sb = null;
				}
				else{
					this.msg = "Socket 传输数据成功";
				}
				result = true;
				out.close();
				out = null;
			}
		}catch (UnknownHostException e) {
			this.msg = e.getMessage(); 
		} catch (IOException e) {
			this.msg = e.getMessage(); 
		} catch (Exception e) {
			this.msg = e.getMessage();
		} finally {
			try {
				if (con != null){
					con.close();
					con = null;
				}	
			} 
			catch (IOException e) {
			}
		}
		logger.debug(msg);
		
		return result;
	}
	
	
	public static void main(String[] args) {
		/*String host = NetworkGlobals.getNetworkProperty("socket.Flaut10000.IP");
		String sPort = NetworkGlobals.getNetworkProperty("socket.Flaut10000.Port");
		int port=0;
		boolean blnBack=false;
		
		if(sPort != null)
			port = Integer.parseInt(sPort);
			
		String sBlnBack = NetworkGlobals.getNetworkProperty("socket.Flaut10000.IsCallBack");
		
		if(sBlnBack != null)
			blnBack = sBlnBack.equals("true")?true:false;

		SocketMsg client = new SocketMsg(host,port,blnBack);
		String sendMsg = "3|||7|||b00290920|||ID1|||ID2|||ID4|||ID8|||ID5";*/
		SocketMsg client = new SocketMsg("192.168.4.228",5000,true);
		//String sendMsg = "42|||10|||adsltest|||ID3|||ID2|||ID4|||ID5|||ID9|||ID1";
		//boolean blnSuccess = client.SendMsg(sendMsg);
		//String recvMsg = client.getMsg();
		//logger.debug(recvMsg+" "+blnSuccess);
		
	}
}
