package com.linkage.module.intelspeaker.verconfigfile.entity;

/**
 * Ftp文件服务器
 * @author jlp
 *
 */
public class FtpUrl {
	private String path;
	private String user;
	private String password;
	private String ftpip;
	private int port;
	
	public FtpUrl(String ftpip,String path, String user, String password) {
		super();
		this.ftpip = ftpip;
		this.path = path;
		this.user = user;
		this.password = password;
	}
	public FtpUrl(String ftpip,String path, String user, String password, int port) {
		super();
		this.ftpip = ftpip;
		this.path = path;
		this.user = user;
		this.password = password;
		this.port = port;
	}
	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
	}
	public String getUser() {
		return user;
	}
	public void setUser(String user) {
		this.user = user;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getFtpip() {
		return ftpip;
	}
	public void setFtpip(String ftpip) {
		this.ftpip = ftpip;
	}
	public int getPort() {
		return port;
	}
	public void setPort(int port) {
		this.port = port;
	}
	@Override
	public String toString() {
		return "FtpUrl [path=" + path + ", user=" + user + ", password=" + password + ", ftpip=" + ftpip + ", port="
				+ port + "]";
	}
	
}
