package com.linkage.litms.common.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ch.ethz.ssh2.ChannelCondition;
import ch.ethz.ssh2.Connection;
import ch.ethz.ssh2.Session;
import ch.ethz.ssh2.StreamGobbler;

public class RemoteShellExecutor {

	private static Logger logger = LoggerFactory.getLogger(RemoteShellExecutor.class);
	private Connection conn;
	/** 远程机器IP */
	private String ip;
	/** 用户名 */
	private String userName;
	/** 密码 */
	private String userPwd;
	private String charset = Charset.defaultCharset().toString();

	private static final int TIME_OUT = 1000 * 5 * 60;

	/**
	 * 构造函数
	 * 
	 * @param ip
	 * @param usr
	 * @param pasword
	 */
	public RemoteShellExecutor(String ip, String usr, String pasword) {
		this.ip = ip;
		this.userName = usr;
		this.userPwd = pasword;
	}

	/**
	 * 登录
	 * 
	 * @return
	 * @throws IOException
	 */
	public Boolean login() {
		boolean flg = false;
		try {
			conn = new Connection(ip);
			conn.connect();// 连接
			flg = conn.authenticateWithPassword(userName, userPwd);// 认证
		} catch (IOException e) {
			e.printStackTrace();
		}
		return flg;
	}

	/**
	 * 执行脚本
	 * 
	 * @param cmds
	 * @return
	 * @throws Exception
	 */
	public int exec(String cmds) throws Exception {
		InputStream stdOut = null;
		InputStream stdErr = null;
		String outStr = "";
		String outErr = "";
		int ret = -1;
		try {
			if (login()) {
				// Open a new {@link Session} on this connection
				Session session = conn.openSession();
				// Execute a command on the remote machine.
				session.execCommand(cmds);

				stdOut = new StreamGobbler(session.getStdout());
				outStr = processStream(stdOut, charset);

				stdErr = new StreamGobbler(session.getStderr());
				outErr = processStream(stdErr, charset);

				session.waitForCondition(ChannelCondition.EXIT_STATUS, TIME_OUT);

				logger.warn("outStr=" + outStr);
				logger.warn("outErr=" + outErr);

				ret = session.getExitStatus();
			} else {
				logger.warn("登录远程机器失败" + ip);
			}
		} catch (Exception e) {
			logger.error("登录远程机器异常", e);
		} finally {
			if (conn != null) {
				conn.close();
			}
			IOUtils.closeQuietly(stdOut);
			IOUtils.closeQuietly(stdErr);
		}
		return ret;
	}

	/**
	 * @param in
	 * @param charset
	 * @return
	 * @throws IOException
	 * @throws UnsupportedEncodingException
	 */
	private String processStream(InputStream in, String charset)
			throws Exception {
		byte[] buf = new byte[1024];
		StringBuilder sb = new StringBuilder();
		while (in.read(buf) != -1) {
			sb.append(new String(buf, charset));
		}
		return sb.toString();
	}

	public static void main(String args[]) throws Exception {
		// String ip = LipossGlobals.getLipossProperty("bathPing.ip");
		// String user = LipossGlobals.getLipossProperty("bathPing.user");
		// String pswd = LipossGlobals.getLipossProperty("bathPing.pswd");
		// String cmds = LipossGlobals.getLipossProperty("bathPing.cmds");
		RemoteShellExecutor executor = new RemoteShellExecutor("192.168.28.72", "root", "aiNSG2$^");
		// 执行myTest.sh 参数为java Know dummy
		logger.warn("result:" + executor.exec("cd /export/home/rms/BatchPing/bin/;sh runBatchPing.sh"));
	}
}
