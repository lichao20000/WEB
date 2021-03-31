
package com.linkage.module.gwms.util;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * SocketChannelClient
 * @author zhangsm(67310) Tel:��
 * @version 1.0
 * @since 2012-9-12 下午02:27:24
 * @category com.linkage.module.gwms.util<br>
 * @copyright asiainfo-linkage NBS.
 */
public class SocketChannelClient
{
	/**
	 * 日志记录
	 */
	private static Logger logger = LoggerFactory
			.getLogger(SocketChannelClient.class);
	private static SocketChannelClient socketChannelClient = null;
	// 信道选择器
	private Selector selector;
	// 与服务器通信的信道
	SocketChannel socketChannel;
	// 要连接的服务器Ip地址
	private String hostIp;
	// 要连接的远程服务器在监听的端口
	private int hostListenningPort;
	public static SocketChannelClient getSocketChannelClientInstance(String HostIp, int HostListenningPort)
	{
		if(socketChannelClient == null)
		{
			try
			{
				socketChannelClient = new SocketChannelClient(HostIp , HostListenningPort);
			}
			catch (IOException e)
			{
				logger.error("创建SocketChannelClient失败，请检查IP和端口是否正确。");
				e.printStackTrace();
			}
		}
		return socketChannelClient;
	}
	/**
	 * 构造函数
	 * 
	 * @param HostIp
	 * @param HostListenningPort
	 * @throws IOException
	 */
	private SocketChannelClient(String HostIp, int HostListenningPort) throws IOException
	{
		this.hostIp = HostIp;
		this.hostListenningPort = HostListenningPort;
		initialize();
	}

	/**
	 * 初始化
	 * @author zhangsm(67310)
	 * @date 2012-9-12 下午02:33:57
	 */
	private void initialize()
	{
		try
		{
			logger.warn("初始化SocketChannel服务器连接,HOST_IP=[{}],HOST_PORT=[{}]",new Object[]{hostIp,hostListenningPort});
			// 打开监听信道并设置为非阻塞模式
			socketChannel = SocketChannel.open(new InetSocketAddress(hostIp,
					hostListenningPort));
			socketChannel.configureBlocking(false);
			// 打开并注册选择器到信道
			selector = Selector.open();
			socketChannel.register(selector, SelectionKey.OP_READ);
			// 启动读取线程
//			 new TCPClientReadThread(selector);
		}
		catch (Exception e)
		{
			logger.warn("与SocketChannelClient服务器连接异常，重新连接");
			try
			{
				Thread.sleep(1000);
			}
			catch (InterruptedException e1)
			{
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			initialize();
			// TODO Auto-generated catch block
		}
	}
	public static void main(String[] args)
	{
		try
		{
			
			while(true)
			{
				for(int i = 0 ; i < 3 ; i ++ )
				{
					SocketChannelClient channelClient = SocketChannelClient.getSocketChannelClientInstance("127.0.0.1", 3030);
					System.out.println(channelClient.sendMsg(i+"---发送消息"));
					channelClient.close();
				}
				Thread.sleep(3000);
			}
		}
		catch (Exception e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	/**
	 * 发送消息到服务器
	 * @author zhangsm(67310)
	 * @date 2012-9-12 下午02:34:09
	 * @param message
	 * @return
	 * @throws IOException
	 */
	public String sendMsg(String message) throws IOException
	{
		ByteBuffer writeBuffer = ByteBuffer.wrap(message.getBytes("GBK"));
		if (null == socketChannel || !socketChannel.isOpen())
		{
			logger.warn("与SocketChannelClient服务器连接异常，重新连接");
			initialize();
		}
		socketChannel.write(writeBuffer);
		String receivedString = readReturnMsg();
		if(null==receivedString)
		{
			logger.warn("SocketChannelClient服务器未返回");
		}
		return receivedString;
	}
	/**
	 * 客户端实例销毁并关闭连接
	 * @throws IOException
	 */
	public void close() throws IOException
	{
		// add by yinlei3 at 20150729 for JXDX-ITMS-REQ-20150728-LINBX-001 begin
		if (selector.isOpen())
		{
			selector.close();
		}
		// add by yinlei3 at 20150729 end
		if(socketChannel.isOpen())
		{
			socketChannel.close();
		}
		if(null != socketChannelClient)
		{
			socketChannelClient = null;
		}
	}
	/**
	 * 读取server回复的消息
	 * @author zhangsm(67310)
	 * @date 2012-9-12 下午02:24:04
	 * @return
	 */
	public String readReturnMsg()
	{
		String receivedString = "";
		try
		{
			if (selector.select() > 0)
			{
				// 遍历每个有可用IO操作Channel对应的SelectionKey
				for (SelectionKey sk : selector.selectedKeys())
				{
					// 如果该SelectionKey对应的Channel中有可读的数据
					if (sk.isReadable())
					{
						// 使用NIO读取Channel中的数据
						SocketChannel sc = (SocketChannel) sk.channel();
						ByteBuffer buffer = ByteBuffer.allocate(1024);
						sc.read(buffer);
						buffer.flip();
						// 将字节转化为为UTF-16的字符串
						receivedString = Charset.forName("GBK").newDecoder()
								.decode(buffer).toString();
						// 为下一次读取作准备
						sk.interestOps(SelectionKey.OP_READ);
					}
					// 删除正在处理的SelectionKey
					selector.selectedKeys().remove(sk);
				}
			}
		}
		catch (IOException ex)
		{
			ex.printStackTrace();
		}
		return receivedString;
	}
}