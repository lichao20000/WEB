
package action.resource;

import java.io.File;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.struts2.interceptor.ServletRequestAware;

import bio.resource.ItvConfigBIO;

import com.linkage.litms.system.UserRes;

import dao.resource.DeviceVendorModelDao;

/**
 * @author Jason(3412)
 * @date 2008-12-16
 */
public class DevVenderModelAction implements ServletRequestAware
{

	private String vendorname;
	private String deviceModel;
	private String hardwareversion;
	private String softwareversion;
	private String city_id;
	private String gwType;
	private String ajax;
	private DeviceVendorModelDao devDao;
	private String username;
	private String telephone;
	private String deviceSerialnumber;
	private String ipAddress;
	// 查询方式
	private String checkType;
	// request取登陆帐号使用
	private HttpServletRequest request;
	private final String AJAX = "ajax";
	// 导入文件,根据此文件解析用户账号
	private File file;
	// 导入的文件中，查询异常数据返回
	private List deviceExceptionList = null;
	// 导入的文件中，正常数据，
	private List deviceNormalList = null;
	private ItvConfigBIO itvConfigBIO;

	@SuppressWarnings("unchecked")
	public String getDeviceInfoList()
	{
		String queryCity = city_id;
		if (queryCity == null || "".equals(queryCity))
		{
			HttpSession session = request.getSession();
			UserRes curUser = (UserRes) session.getAttribute("curUser");
			queryCity = curUser.getCityId();
		}
		if ("1".equals(checkType))
		{
			ajax = devDao.getHTMLDeviceList(devDao.getDeviceList(username, telephone,
					checkType, queryCity, null, gwType));
		}
		else if ("2".equals(checkType))
		{
			ajax = devDao.getHTMLDeviceList(devDao.getDeviceList(deviceSerialnumber,
					ipAddress, checkType, queryCity, null, gwType));
		}
		else
		{
			if ("".equals(softwareversion) || "-1".equals(softwareversion))
			{
				ajax = devDao.getHTMLDeviceList(devDao.getDeviceList(deviceModel, null,
						checkType, queryCity, vendorname, gwType));
			}
			else
			{
				ajax = devDao.getHTMLDeviceList(devDao.getDeviceList(deviceModel,
						softwareversion, checkType, queryCity, vendorname, gwType));
			}
		}
		return AJAX;
	}

	/**
	 * @category 批量导入用户账号查询设备,查询的设备主要分为两种， 一：异常设备，例如库中不存在的数据； 二：正常设备，包括已经做过IPTV的设备；
	 * @author qxq(4174)
	 * @date 2009-02-16
	 * @return String
	 */
	public String getDeviceByImportUsername()
	{
		String queryCity = null;
		HttpSession session = request.getSession();
		UserRes curUser = (UserRes) session.getAttribute("curUser");
		queryCity = curUser.getCityId();
		// 导入的文件中，前100条数据
		List<String> username50List = itvConfigBIO.getUsernameByIportFile(this.file);
		if (1 <= username50List.size())
		{
			this.deviceExceptionList = devDao.getDeviceExceptionList(queryCity,
					username50List);
			this.deviceNormalList = devDao.getDeviceNormalList(queryCity, username50List);
		}
		else
		{
			this.deviceExceptionList = null;
			this.deviceNormalList = null;
		}
		return "deviceByImport";
	}

	public String getCityList()
	{
		HttpSession session = request.getSession();
		UserRes curUser = (UserRes) session.getAttribute("curUser");
		String curCity = curUser.getCityId();
		ajax = devDao.getCityList(curCity);
		return AJAX;
	}

	public String getVendorList()
	{
		ajax = devDao.getVendorList();
		return AJAX;
	}

	public String getDeviceModelList()
	{
		ajax = devDao.getDeviceModelList(vendorname);
		return AJAX;
	}

	public String getVersionList()
	{
		ajax = devDao.getVersionList(vendorname, deviceModel);
		return AJAX;
	}

	public String getSoftVersionList()
	{
		ajax = devDao.getVersionList(vendorname, null);
		return AJAX;
	}

	@SuppressWarnings("unchecked")
	public String getHardwareversionList()
	{
		ajax = devDao.getHTMLCheckList(
				devDao.getDevHardwareList(vendorname, deviceModel), "hardwareversion");
		return AJAX;
	}

	@SuppressWarnings("unchecked")
	public String getHardwareversionStr()
	{
		List strList = devDao.getDevHardwareList(vendorname, deviceModel);
		StringBuffer sb = new StringBuffer();
		Iterator<String> itor = strList.iterator();
		String tmpStr = "";
		while (itor.hasNext())
		{
			tmpStr = itor.next();
			sb.append(tmpStr + ";");
		}
		
		// 20200424 解决空指针问题
		if(sb.length() == 0) {
			ajax = "";
		}else {
			ajax = sb.toString().substring(0, sb.toString().length() - 1);
		}
		
		return AJAX;
	}

	@SuppressWarnings("unchecked")
	public String getDevModelChkbox()
	{
		ajax = devDao.getHTMLCheckList(devDao.getDevModelList(vendorname), "deviceModel");
		return AJAX;
	}

	public String getHardwareversion()
	{
		return hardwareversion;
	}

	public void setHardwareversion(String hardwareversion)
	{
		this.hardwareversion = hardwareversion;
	}

	public String getVendorname()
	{
		return vendorname;
	}

	public void setVendorname(String vendorname)
	{
		this.vendorname = vendorname;
	}

	public String getDeviceModel()
	{
		return deviceModel;
	}

	public void setDeviceModel(String deviceModel)
	{
		this.deviceModel = deviceModel;
	}

	public String getSoftwareversion()
	{
		return softwareversion;
	}

	public void setSoftwareversion(String softwareversion)
	{
		this.softwareversion = softwareversion;
	}

	public String getUsername()
	{
		return username;
	}

	public void setUsername(String username)
	{
		this.username = username;
	}

	public String getTelephone()
	{
		return telephone;
	}

	public void setTelephone(String telephone)
	{
		this.telephone = telephone;
	}

	public String getDeviceSerialnumber()
	{
		return deviceSerialnumber;
	}

	public void setDeviceSerialnumber(String deviceSerialnumber)
	{
		this.deviceSerialnumber = deviceSerialnumber;
	}

	public String getIpAddress()
	{
		return ipAddress;
	}

	public void setIpAddress(String ipAddress)
	{
		this.ipAddress = ipAddress;
	}

	public String getCheckType()
	{
		return checkType;
	}

	public void setCheckType(String checkType)
	{
		this.checkType = checkType;
	}

	public String getGwType()
	{
		return gwType;
	}

	public void setGwType(String gwType)
	{
		this.gwType = gwType;
	}

	public void setServletRequest(HttpServletRequest arg0)
	{
		this.request = arg0;
	}

	public String getAjax()
	{
		return ajax;
	}

	public void setDevDao(DeviceVendorModelDao devDao)
	{
		this.devDao = devDao;
	}

	public void setCity_id(String city_id)
	{
		this.city_id = city_id;
	}

	/**
	 * @return the file
	 */
	public File getFile()
	{
		return file;
	}

	/**
	 * @param file
	 *            the file to set
	 */
	public void setFile(File file)
	{
		this.file = file;
	}

	/**
	 * @return the deviceExceptionList
	 */
	public List getDeviceExceptionList()
	{
		return deviceExceptionList;
	}

	/**
	 * @param deviceExceptionList
	 *            the deviceExceptionList to set
	 */
	public void setDeviceExceptionList(List deviceExceptionList)
	{
		this.deviceExceptionList = deviceExceptionList;
	}

	/**
	 * @return the deviceNormalList
	 */
	public List getDeviceNormalList()
	{
		return deviceNormalList;
	}

	/**
	 * @param deviceNormalList
	 *            the deviceNormalList to set
	 */
	public void setDeviceNormalList(List deviceNormalList)
	{
		this.deviceNormalList = deviceNormalList;
	}

	/**
	 * @return the itvConfigBIO
	 */
	public ItvConfigBIO getItvConfigBIO()
	{
		return itvConfigBIO;
	}

	/**
	 * @param itvConfigBIO
	 *            the itvConfigBIO to set
	 */
	public void setItvConfigBIO(ItvConfigBIO itvConfigBIO)
	{
		this.itvConfigBIO = itvConfigBIO;
	}
}
