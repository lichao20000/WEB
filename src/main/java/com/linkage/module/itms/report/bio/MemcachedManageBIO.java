
package com.linkage.module.itms.report.bio;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.TimeoutException;

import net.rubyeye.xmemcached.MemcachedClient;
import net.rubyeye.xmemcached.MemcachedClientBuilder;
import net.rubyeye.xmemcached.XMemcachedClientBuilder;
import net.rubyeye.xmemcached.command.BinaryCommandFactory;
import net.rubyeye.xmemcached.exception.MemcachedException;
import net.rubyeye.xmemcached.impl.KetamaMemcachedSessionLocator;
import net.rubyeye.xmemcached.utils.AddrUtil;

import org.apache.commons.net.telnet.TelnetClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.commons.util.StringUtil;
import com.linkage.module.itms.report.dao.MemcachedManageDAO;

/**
 * @author chenzhangjian (Ailk No.)
 * @version 1.0
 * @since 2015-8-13
 * @category com.linkage.module.itms.report.bio
 * @copyright Ailk NBS-Network Mgt. RD Dept.
 */
public class MemcachedManageBIO
{

	private static Logger logger = LoggerFactory.getLogger(MemcachedManageBIO.class);
	private MemcachedManageDAO memcachedManageDAO = null;

	/**
	 * 获取初始化的缓存库信息，并检测状态
	 * 
	 * @return 缓存库列表
	 */
	public ArrayList<HashMap<String, String>> queryMemcachedInfos()
	{
		ArrayList<HashMap<String, String>> resultList = memcachedManageDAO
				.queryMemcachedInfos();
		for (int i=0;i<resultList.size();i++)
		{
			String ipAddress = StringUtil.getStringValue(resultList.get(i), "ipaddress");
			String port = StringUtil.getStringValue(resultList.get(i), "port");
			// 缓存库正在运行
			if (testStatus(ipAddress, port))
			{
				resultList.get(i).put("status", "1");
			}
			// 缓存库未运行
			else
			{
				logger.info("缓存库未运行,{}:{}",ipAddress,port);
				resultList.get(i).put("status", "0");
			}
		}
		return resultList;
	}

	public String queryFromMemcached(String ipaddress, String port, String key)
	{
		String result = "";
		if (testStatus(ipaddress, port))
		{
			MemcachedClient client = getMemcachedClient(ipaddress, port);
			if (null == client)
			{
				result = "err";
			}
			else
			{
				Object obj = null;
				try
				{
					obj = client.get(key);
					if (null == obj)
					{
						result = "no";
					}
					else
					{
						result = StringUtil.getStringValue(obj);
					}
				}
				catch (TimeoutException e)
				{
					e.printStackTrace();
					logger.error("从缓存库获取数据失败(ipaddress={},port={},key={})!",
							new String[] { ipaddress, port, key });
					result = "err";
				}
				catch (InterruptedException e)
				{
					e.printStackTrace();
					logger.error("从缓存库获取数据失败(ipaddress={},port={},key={})!",
							new String[] { ipaddress, port, key });
					result = "err";
				}
				catch (MemcachedException e)
				{
					e.printStackTrace();
					logger.error("从缓存库获取数据失败(ipaddress={},port={},key={})!",
							new String[] { ipaddress, port, key });
					result = "err";
				}
				finally
				{
					try
					{
						client.shutdown();
					}
					catch (IOException e)
					{
						e.printStackTrace();
						logger.error("释放缓存库连接失败(ipaddress={},port={})!", new String[] {
								ipaddress, port });
					}
				}
			}
		}
		else
		{
			result = "off";
		}
		return result;
	}

	public String delFromMemcached(String ipaddress, String port, String key)
	{
		String result = "";
		if (testStatus(ipaddress, port))
		{
			MemcachedClient client = getMemcachedClient(ipaddress, port);
			if (null == client)
			{
				result = "err";
			}
			else
			{
				try
				{
					if (client.delete(key))
					{
						result = "ok";
					}
					else
					{
						result = "no";
					}
				}
				catch (TimeoutException e)
				{
					e.printStackTrace();
					logger.error("从缓存库删除数据失败(ipaddress={},port={},key={})!",
							new String[] { ipaddress, port, key });
					result = "err";
				}
				catch (InterruptedException e)
				{
					e.printStackTrace();
					logger.error("从缓存库删除数据失败(ipaddress={},port={},key={})!",
							new String[] { ipaddress, port, key });
					result = "err";
				}
				catch (MemcachedException e)
				{
					e.printStackTrace();
					logger.error("从缓存库删除数据失败(ipaddress={},port={},key={})!",
							new String[] { ipaddress, port, key });
					result = "err";
				}
				finally
				{
					try
					{
						client.shutdown();
					}
					catch (IOException e)
					{
						e.printStackTrace();
						logger.error("释放缓存库连接失败(ipaddress={},port={})!", new String[] {
								ipaddress, port });
					}
				}
			}
		}
		else
		{
			result = "off";
		}
		return result;
	}

	/**
	 * 从缓存库更新记录，如果key不存在也会成功，通用增加和修改
	 * 
	 * @param ipaddress
	 * @param port
	 * @param key
	 * @param value
	 * @return
	 */
	public String updToMemcached(String ipaddress, String port, String key, String value)
	{
		String result = "";
		if (testStatus(ipaddress, port))
		{
			MemcachedClient client = getMemcachedClient(ipaddress, port);
			if (null == client)
			{
				result = "err";
			}
			else
			{
				try
				{
					if (client.set(key, 0, value))
					{
						result = "ok";
					}
					else
					{
						result = "err";
					}
				}
				catch (TimeoutException e)
				{
					e.printStackTrace();
					logger.error("从缓存库更新数据失败(ipaddress={},port={},key={},value={})!",
							new String[] { ipaddress, port, key, value });
					result = "err";
				}
				catch (InterruptedException e)
				{
					e.printStackTrace();
					logger.error("从缓存库更新数据失败(ipaddress={},port={},key={},value={})!",
							new String[] { ipaddress, port, key, value });
					result = "err";
				}
				catch (MemcachedException e)
				{
					e.printStackTrace();
					logger.error("从缓存库更新数据失败(ipaddress={},port={},key={},value={})!",
							new String[] { ipaddress, port, key, value });
					result = "err";
				}
				finally
				{
					try
					{
						client.shutdown();
					}
					catch (IOException e)
					{
						e.printStackTrace();
						logger.error("释放缓存库连接失败(ipaddress={},port={})!", new String[] {
								ipaddress, port });
					}
				}
			}
		}
		else
		{
			result = "off";
		}
		return result;
	}

	public String getMemcachedStats(String ipaddress, String port)
	{
		String result = "";
		if (testStatus(ipaddress, port))
		{
			TelnetClient telnet = new TelnetClient();
			InputStream in = null;
			PrintStream out = null;
			try
			{
				// 连接服务器端
				telnet.connect(ipaddress, StringUtil.getIntegerValue(port));
				// 设置输入输出流
				in = telnet.getInputStream();
				out = new PrintStream(telnet.getOutputStream());
				out.println("stats");
				out.flush();
				String pattern = "END";
				char lastChar = pattern.charAt(pattern.length() - 1);
				StringBuffer sb = new StringBuffer();
				char ch = (char) in.read();
				while (true)
				{
					sb.append(ch);
					if (ch == lastChar)
					{
						if (sb.toString().endsWith(pattern))
						{
							result = sb.toString();
							break;
						}
					}
					ch = (char) in.read();
				}
			}
			catch (SocketException e)
			{
				e.printStackTrace();
				logger.error("获取缓存库状态失败(ipaddress={},port={})!", new String[] {
						ipaddress, port });
				result = "err";
			}
			catch (IOException e)
			{
				e.printStackTrace();
				logger.error("获取缓存库状态失败(ipaddress={},port={})!", new String[] {
						ipaddress, port });
				result = "err";
			}
			finally
			{
				try
				{
					if (null != in)
					{
						in.close();
					}
				}
				catch (Exception e)
				{
					e.printStackTrace();
					logger.warn("释放telnet连接失败(ipaddress={},port={})!", new String[] {
							ipaddress, port });
				}
				
				try
				{
					if (null != out)
					{
						out.close();
					}
				}
				catch (Exception e)
				{
					e.printStackTrace();
					logger.warn("释放telnet连接失败(ipaddress={},port={})!", new String[] {
							ipaddress, port });
				}
			}
		}
		else
		{
			result = "off";
		}
		return result;
	}

	/**
	 * 检测缓存库在线状态
	 * 
	 * @param ipAddress
	 * @param port
	 * @return false:未运行 true:正在运行
	 */
	public boolean testStatus(String ipAddress, String port)
	{
		boolean result = false;
		if (StringUtil.IsEmpty(ipAddress) || StringUtil.IsEmpty(port))
		{
			result = false;
		}
		else
		{
			TelnetClient telnet = new TelnetClient();
			try
			{
				telnet.connect(ipAddress, StringUtil.getIntegerValue(port));
				result = true;
			}
			catch (SocketException e)
			{
				result = false;
			}
			catch (IOException e)
			{
				result = false;
			}
			finally
			{
				if (null != telnet)
				{
					try
					{
						telnet.disconnect();
					}
					catch (IOException e)
					{
					}
				}
			}
		}
		return result;
	}

	/**
	 * 获取MemcachedClientBuilder
	 * 
	 * @param memcached
	 * @return
	 */
	private MemcachedClient getMemcachedClient(String ipaddress, String port)
	{
		MemcachedClientBuilder builder = null;
		builder = new XMemcachedClientBuilder(AddrUtil.getAddresses(ipaddress + ":"
				+ port));
		// 默认分布的策略是按照key的哈希值模以连接数得到的余数,一致性哈希（consistent hash
		builder.setSessionLocator(new KetamaMemcachedSessionLocator());
		// Nio连接池
		builder.setConnectionPoolSize(1);
		// 使用二进制协议/默认使用的TextCommandFactory，也就是文本协议
		builder.setCommandFactory(new BinaryCommandFactory());
		// 失败模式
		builder.setFailureMode(false);
		MemcachedClient client = null;
		try
		{
			client = builder.build();
		}
		catch (IOException e)
		{
			e.printStackTrace();
			logger.error("初始化缓存库({}:{})失败", ipaddress, port);
		}
		return client;
	}

	public MemcachedManageDAO getMemcachedManageDAO()
	{
		return memcachedManageDAO;
	}

	public void setMemcachedManageDAO(MemcachedManageDAO memcachedManageDAO)
	{
		this.memcachedManageDAO = memcachedManageDAO;
	}
}
