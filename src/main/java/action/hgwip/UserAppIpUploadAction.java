package action.hgwip;

import java.io.File;
import java.util.UUID;

import org.apache.commons.io.FileUtils;

import bio.hgwip.IPGlobal;

import com.opensymphony.xwork2.ActionSupport;

import dao.hgwip.SubnetOperationDAO;

/**
 * ip地址管理的ip取消/回收的管理action
 * 
 * @author 王志猛(5194) tel:13701409234
 * @version 1.0
 * @since 2007-11-12
 * @category ipmg
 */
public class UserAppIpUploadAction extends ActionSupport
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -7779844873956581328L;
	private File appfile;
	private String appfileContentType;
	private String appfileFileName;
	private boolean allow = false;
	private String act = "give";
	private String userstat;
	private String stat;// 上传状态 ok 成功， fail 失败
	private SubnetOperationDAO sbdao;
	private String attr;
	private String subnet;//
	private String subnetGrp;//
	private String inetMask;//
	private String rwaddr;//
	private String city_name;// 城市名称
	private String addrnum;//
	private String assignInfo;//
	private String comment;//
	@Override
	public String execute() throws Exception
	{
		getinfo();
		String path = IPGlobal.FILE_PATH_SPL;
		String fn = UUID.randomUUID().toString();
		try
			{
				int in = appfileFileName.lastIndexOf(".");
				String type = ".doc";
				if (in != -1)
					{
						type = appfileFileName.substring(in);
					}
				String fileRealPath = path + fn + "/" + type;
				FileUtils.copyFile(appfile, new File(fileRealPath));
				sbdao.applySubnetToUser(subnet, subnetGrp, Integer
						.parseInt(inetMask), rwaddr, Integer.parseInt(addrnum),
						assignInfo, comment, fileRealPath);
			} catch (Exception e)
			{
				e.printStackTrace();
				stat = "fail";
			}
		stat = "ok";
		return SUCCESS;
	}
	public void setAppfile(File appfile)
	{
		this.appfile = appfile;
	}
	public void setAppfileContentType(String appfileContentType)
	{
		this.appfileContentType = appfileContentType;
	}
	public void setAppfileFileName(String appfileFileName)
	{
		this.appfileFileName = appfileFileName;
	}
	public boolean isAllow()
	{
		return allow;
	}
	public String getAct()
	{
		return act;
	}
	public String getUserstat()
	{
		return userstat;
	}
	public String getStat()
	{
		return stat;
	}
	public void setAttr(String attr)
	{
		this.attr = attr;
	}
	public String getAttr()
	{
		return attr;
	}
	private void getinfo()
	{
		String[] attrs = attr.split("/");
		userstat = attrs[0];
		subnet = attrs[1];
		subnetGrp = attrs[2];
		inetMask = attrs[3];
	}
	public void setRwaddr(String rwaddr)
	{
		this.rwaddr = rwaddr;
	}
	public String getRwaddr()
	{
		return rwaddr;
	}
	public String getAddrnum()
	{
		return addrnum;
	}
	public void setAddrnum(String addrnum)
	{
		this.addrnum = addrnum;
	}
	public void setAssignInfo(String assignInfo)
	{
		this.assignInfo = assignInfo;
	}
	public void setComment(String comment)
	{
		this.comment = comment;
	}
	public void setSbdao(SubnetOperationDAO sbdao)
	{
		this.sbdao = sbdao;
	}
	public String getCity_name()
	{
		return city_name;
	}
	public void setCity_name(String city_name)
	{
		this.city_name = city_name;
	}
}
