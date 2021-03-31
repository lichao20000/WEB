package bio.hgwip;

import java.util.HashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class IpTool
{
	/** log */
	private static final Logger LOG = LoggerFactory.getLogger(IpTool.class);
	/**
	 * 根据指定子网的最低地址、主机数，来计算出下个子网的最小地址
	 * 限于子网划分使用，指定子网和计算出的子网，都是通过一个子网划分出来的
	 * @param lowAddr
	 * @param hostNumber
	 * @return
	 */
	public static String getNextSubnet(String lowAddr,int hostNumber)
	{
		int[] lowAddrArray = new int[4];
		for(int i=0;i<4;i++)
		{
			lowAddrArray[i]=Integer.parseInt(lowAddr.split("\\.")[i]);
		}
		
		lowAddrArray[3] +=hostNumber;
		
		if(lowAddrArray[3]>255)
		{
			int k= lowAddrArray[3]/256;
			lowAddrArray[3] = lowAddrArray[3]%256;
			lowAddrArray[2]+=k;
			if(lowAddrArray[2]>255)
			{
				k=lowAddrArray[2]/256;
				lowAddrArray[2]=lowAddrArray[2]%256;
				lowAddrArray[1]+=k;
				if(lowAddrArray[1]>255)
				{
					k=lowAddrArray[1]/256;
					lowAddrArray[1] = lowAddrArray[1]%256;
					lowAddrArray[0]+=k;					
				}
			}
		}
		
		String nextSubnetLowAddr ="";
		for(int i=0;i<4;i++)
		{
			if("".equals(nextSubnetLowAddr))
			{
				nextSubnetLowAddr=String.valueOf(lowAddrArray[i]);
			}
			else
			{
				nextSubnetLowAddr+="."+String.valueOf(lowAddrArray[i]);
			}
		}
		
		return nextSubnetLowAddr;
	}

	/**
	 * 根据子网掩码位数计算子网掩码
	 * @param inetMask
	 * @return 返回""，表示参数错误
	 */
	public static String getNetMask(int inetMask)
	{
		String netMask="";
		if(inetMask>32)
		{
			return netMask;
		}
    	//子网掩码为1占了几个字节
    	int num1 = inetMask/8;
    	//子网掩码的补位位数
    	int num2 = inetMask%8;
    	int[] array = new int[4];
    	for(int i=0;i<num1;i++)
    	{
    		array[i]=255;
    	}
    	for(int i=num1;i<4;i++)
    	{
    		array[i]=0;
    	}
    	for(int i=0;i<num2;num2--)
    	{
    		array[num1]+=Math.pow(2D,8-num2);
    	}
    	netMask=array[0]+"."+array[1]+"."+array[2]+"."+array[3];
    	
    	return netMask;		
	}
	
	
	/**
	 * 根据地址段和子网掩码获取最低地址
	 * @param ip
	 * @param netMask  255.255.0.0的格式
	 * @return
	 */
	public static String getLowAddr(String ip,String netMask)
	{
		String lowAddr="";		
		int[] ipArray = new int[4];
		int[] netMaskArray = new int[4];
		try
		{
			//I参数不正确			
			if(4!=ip.split("\\.").length||"".equals(netMask))
			{
				return "";
			}
			for(int i=0;i<4;i++)
			{
				ipArray[i] = Integer.parseInt(ip.split("\\.")[i]);
				netMaskArray[i]=Integer.parseInt(netMask.split("\\.")[i]);
				if(ipArray[i]>255||ipArray[i]<0||netMaskArray[i]>255&&netMaskArray[i]<0)
				{
					return "";
				}
				ipArray[i]= ipArray[i]&netMaskArray[i];			    
			}
			
			//构造最小地址
			for(int i=0;i<4;i++)
			{
				if("".equals(lowAddr))
			    {
			    	lowAddr+=String.valueOf(ipArray[i]);
			    }
			    else
			    {
			    	lowAddr+="."+String.valueOf(ipArray[i]);
			    }
			}
			
		}
		catch(Exception e)
		{
			lowAddr="";
		}
		
		return lowAddr;
	
	}
	
	
	/**
	 * 获取网段的最小网络地址
	 * @param ip
	 * @param inetMask
	 * @return
	 */
	public static String getLowAddr(String ip,int inetMask)
	{
		String lowAddr="";
		String netMask = getNetMask(inetMask);
		int[] ipArray = new int[4];
		int[] netMaskArray = new int[4];
		try
		{
			//I参数不正确			
			if(4!=ip.split("\\.").length||"".equals(netMask))
			{
				return "";
			}
			for(int i=0;i<4;i++)
			{
				ipArray[i] = Integer.parseInt(ip.split("\\.")[i]);
				netMaskArray[i]=Integer.parseInt(netMask.split("\\.")[i]);
				if(ipArray[i]>255||ipArray[i]<0||netMaskArray[i]>255&&netMaskArray[i]<0)
				{
					return "";
				}
				ipArray[i]= ipArray[i]&netMaskArray[i];			    
			}
			
			//构造最小地址
			for(int i=0;i<4;i++)
			{
				if("".equals(lowAddr))
			    {
			    	lowAddr+=String.valueOf(ipArray[i]);
			    }
			    else
			    {
			    	lowAddr+="."+String.valueOf(ipArray[i]);
			    }
			}
			
		}
		catch(Exception e)
		{
			lowAddr="";
		}
		
		return lowAddr;
	}
	
	/**
	 * 根据子网掩码，计算出主机数
	 * @param inetMask
	 * @return 子网掩码位数不对，则返回0
	 */
	public static int getHostNumber(int inetMask)
	{
		if(inetMask>32||inetMask<0)
		{
			return 0;
		}
		return (int)Math.pow(2D,32-inetMask);
	}
	
	/**
	 * 返回子网掩码位数为inetMask1的划分成子网掩码位数为inetMask2，可以划分的子网个数
	 * @param inetMask1  要划分的网段的子网掩码位数
	 * @param inetMask2  划分的子网的子网掩码位数
	 * @return  指定网段能分配成的子网个数
	 */
	public static int getNetNumber(int inetMask1,int inetMask2)
	{
		if(inetMask1>=inetMask2)
		{
			return 0;
		}
		return (int)Math.pow(2D,inetMask2-inetMask1);
	}
	
	
	/**
	 * 根据子网掩码获取主机位数
	 * @param netMask
	 */
	public static int getHostNumber(String netMask)
	{
		int hostNumber=0;
		int[] netMaskArray= new int[4];
		for(int i=0;i<4;i++)
		{
			netMaskArray[i]=Integer.parseInt(netMask.split("\\.")[i]);
			if(netMaskArray[i]<255)
			{
				//logger.debug(i);
				hostNumber=(int)(Math.pow(256,3-i)+255-netMaskArray[i]);
				break;
			}
		}
		return hostNumber;
	}
	
	/**
	 * 
	 * @param ip
	 * @param netMask
	 * @return
	 */
	public static String getHighAddr(String ip,String netMask)
	{
		String lowAddr = getLowAddr(ip,netMask);
		int hostNumber = getHostNumber(netMask);
		if("".equals(lowAddr)||hostNumber==0)
		{
			return "";
		}
		
		int[] lowAddrArray = new int[4];
		for(int i=0;i<4;i++)
		{
			lowAddrArray[i]=Integer.parseInt(lowAddr.split("\\.")[i]);
		}
		
		lowAddrArray[3] +=hostNumber-1;
		
		if(lowAddrArray[3]>255)
		{
			int k= lowAddrArray[3]/256;
			lowAddrArray[3] = lowAddrArray[3]%256;
			lowAddrArray[2]+=k;
			if(lowAddrArray[2]>255)
			{
				k=lowAddrArray[2]/256;
				lowAddrArray[2]=lowAddrArray[2]%256;
				lowAddrArray[1]+=k;
				if(lowAddrArray[1]>255)
				{
					k=lowAddrArray[1]/256;
					lowAddrArray[1] = lowAddrArray[1]%256;
					lowAddrArray[0]+=k;					
				}
			}
		}
		
		String highAddr ="";
		for(int i=0;i<4;i++)
		{
			if("".equals(highAddr))
			{
				highAddr=String.valueOf(lowAddrArray[i]);
			}
			else
			{
				highAddr+="."+String.valueOf(lowAddrArray[i]);
			}
		}
		
		return highAddr;	
	}
	
	
	/**
	 * 获取网段中的最高地址
	 * @param ip
	 * @param inetMask
	 * @return 参数不对的情况下返回""
	 */
	public static String getHighAddr(String ip,int inetMask)
	{
		String lowAddr = getLowAddr(ip,inetMask);
		int hostNumber = getHostNumber(inetMask);
		if("".equals(lowAddr)||hostNumber==0)
		{
			return "";
		}
		
		int[] lowAddrArray = new int[4];
		for(int i=0;i<4;i++)
		{
			lowAddrArray[i]=Integer.parseInt(lowAddr.split("\\.")[i]);
		}
		
		lowAddrArray[3] +=hostNumber-1;
		
		if(lowAddrArray[3]>255)
		{
			int k= lowAddrArray[3]/256;
			lowAddrArray[3] = lowAddrArray[3]%256;
			lowAddrArray[2]+=k;
			if(lowAddrArray[2]>255)
			{
				k=lowAddrArray[2]/256;
				lowAddrArray[2]=lowAddrArray[2]%256;
				lowAddrArray[1]+=k;
				if(lowAddrArray[1]>255)
				{
					k=lowAddrArray[1]/256;
					lowAddrArray[1] = lowAddrArray[1]%256;
					lowAddrArray[0]+=k;					
				}
			}
		}
		
		String highAddr ="";
		for(int i=0;i<4;i++)
		{
			if("".equals(highAddr))
			{
				highAddr=String.valueOf(lowAddrArray[i]);
			}
			else
			{
				highAddr+="."+String.valueOf(lowAddrArray[i]);
			}
		}
		
		return highAddr;	
	}
	
	
	
	/**
	 * 对IP进行0补位，格式：00x.0xx.xxx.xxx
	 * 
	 * @param ip
	 * @return
	 */
	public static  String getFillIP(String ip)
	{
		LOG.warn("getFillIP:"+ip);
		String fillIP = ip;
		String[] ipArray = new String[4];
		ipArray = ip.split("\\.");
		for (int i = 0; i < 4; i++)
		{
			if (ipArray[i].length() == 1)
			{
				ipArray[i] = "00" + ipArray[i];
			}
			else if (ipArray[i].length() == 2)
			{
				ipArray[i] = "0" + ipArray[i];
			}
		}
		fillIP = ipArray[0] + ipArray[1] + ipArray[2] + ipArray[3];

		return fillIP;
	}
	
	
	/**
	 * 网段的子网可以划分的子网掩码位数（默认不能超过30）
	 * @param inetMak 要划分子网的网段掩码位数
	 * @return 子网掩码为inetMak的网段划分子网可以选择的掩码位数
	 * key:子网掩码位数，value:子网掩码位数
	 */
	@SuppressWarnings("unchecked")
	public static HashMap<String,String> getInetMaskMap(int inetMak)
	{
		HashMap<String,String> inetMaskMap = new HashMap();
		for(int i=inetMak+1;i<31;i++)
		{
			inetMaskMap.put(String.valueOf(i),String.valueOf(i));
		}
		return inetMaskMap;		
	}
	
	
	/**
	 * 返回子网掩码位数为inetMask网段，划分子网可以选择的子网掩码个数
	 * @param inetMask 要划分子网的网段掩码位数
	 * @return key：子网掩码位数，value：子网个数
	 */
	@SuppressWarnings("unchecked")
	public static HashMap<String,String> getInetMaskAndChildCount(int inetMask)
	{
		HashMap<String,String> inetMaskMap = new HashMap();
		for(int i=1;i+inetMask<31;i++)
		{
			inetMaskMap.put(String.valueOf(i+inetMask),String.valueOf((int)Math.pow(2,i)));
		}
		return inetMaskMap;
	}	
	
	public static void main(String[] args)
	{		
		String lowAddr="192.168.28.10";
		String highAddr=getHighAddr(lowAddr,25);
		String highAddr2=getHighAddr(lowAddr,"255.255.255.128");
		LOG.debug("lowAddr:"+lowAddr+"    highAddr:"+highAddr+"    highAddr2:"+highAddr2);		
	}
	

}
