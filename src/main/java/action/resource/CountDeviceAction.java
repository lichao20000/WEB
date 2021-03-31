package action.resource;

import java.util.List;
import java.util.Map;

import org.apache.struts2.interceptor.SessionAware;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import bio.resource.CountDeviceBIO;

import com.linkage.litms.system.User;
import com.linkage.litms.system.UserRes;
import com.linkage.module.gwms.util.StringUtil;
import com.linkage.system.extend.struts.splitpage.SplitPageAction;

/**
 * 网关设备统计Action(家庭网关 flag==1 / 企业网关 flag==2)
 * 
 * @author 段光锐（5250）
 * @version 1.0
 * @since 2008-1-16
 * @category 资源管理
 * 
 */
public class CountDeviceAction extends SplitPageAction implements SessionAware
{

	/**
	 * 
	 */
	private static Logger logger = LoggerFactory
			.getLogger(CountDeviceAction.class);
	
	private static final long serialVersionUID = 6894288209891319648L;

	private List resultList = null;// 查询返回的结果

	private Map session;// 当前会话

	private String flag = null;// 标志位，1代表家庭网关，2代表企业网关
	
	private String cityId = null;
	
	private String isBindDevice = null;
	
	
	public String getCityId()
	{
		return cityId;
	}

	
	public void setCityId(String cityId)
	{
		this.cityId = cityId;
	}

	
	public String getIsBindDevice()
	{
		return isBindDevice;
	}

	
	public void setIsBindDevice(String isBindDevice)
	{
		this.isBindDevice = isBindDevice;
	}

	
	public String getGw_type()
	{
		return gw_type;
	}

	
	public void setGw_type(String gw_type)
	{
		this.gw_type = gw_type;
	}

	
	public CountDeviceBIO getCountDeviceBio()
	{
		return countDeviceBio;
	}

	
	public void setResultList(List resultList)
	{
		this.resultList = resultList;
	}

	/** 详细信息展示 */
	private List<Map> detailResultList = null;

	private CountDeviceBIO countDeviceBio = null;// 业务封装类
	
	/** 导出Excel文件名 */
	private String fileName = null;
	
	/** 导出Excel的标题 */
	private String [] title = null;
	
	/** 导出Excel的列 */
	private String [] column = null;
	
	/** 导出Excel的数据*/
	private List<Map> data =null;
	
	/** 设备类型 */
	private String gw_type = null;
	
	
	
	public String execute() throws Exception
	{
		
		User curUser = ((UserRes) session.get("curUser")).getUser();
		cityId = curUser.getCityId();
		resultList = countDeviceBio.getDeviceCount(flag, curUser);
		return "success";
	}

	public String getDetail()
	{
		if(!StringUtil.IsEmpty(cityId) && !StringUtil.IsEmpty(isBindDevice))
		{
			session.put("cityId", cityId);
			session.put("isBindDevice", isBindDevice);
			session.put("flag", flag);
		}
		else {
			cityId = (String) session.get("cityId");
			isBindDevice = (String) session.get("isBindDevice");
			flag = (String) session.get("flag");
		}
		detailResultList = countDeviceBio.getDetail(flag, cityId, isBindDevice, curPage_splitPage, num_splitPage);
		
		maxPage_splitPage = countDeviceBio.getCount(flag, cityId, isBindDevice, curPage_splitPage, num_splitPage);
		return "detail";
	}
	
	public String toExcel()
	{
		logger.debug("action==>CountDeviceToExcel()");
		fileName = "家庭网关按属地统计";
		title = new String[] { "属地", "所挂设备数", "与用户关联设备数"};
		column = new String[] { "city_name", "devicenum", "cusnum"};
		
		User curUser = ((UserRes) session.get("curUser")).getUser();
		cityId = curUser.getCityId();
		data = countDeviceBio.getDeviceCount(flag, curUser);
		logger.warn("toExcel(150)");
		return "excel";
	}
	
	/**
	 * 详细信息导出
	 * @return
	 */
	public String getDetailExcel(){
		
		logger.debug("action==>getDetailExcel()");
		
		if(!StringUtil.IsEmpty(cityId) && !StringUtil.IsEmpty(isBindDevice))
		{
			session.put("cityId", cityId);
			session.put("isBindDevice", isBindDevice);
		}
		else {
			cityId = (String) session.get("cityId");
			isBindDevice = (String) session.get("isBindDevice");
		}
		
		fileName = "家庭网关设备信息导出";
		title = new String[] { "设备厂商", "型号", "软件版本", "属地", "设备序列号"};
		column = new String[] { "vendor_name", "device_model", "softwareversion",
				"city_name", "device_serialnumber" };
		
		data = countDeviceBio.getDetailExcel(cityId, gw_type, isBindDevice);
		
		return "excel";
	}
	
	
	
	
	public String getFileName()
	{
		return fileName;
	}


	
	public void setFileName(String fileName)
	{
		this.fileName = fileName;
	}


	
	public String[] getTitle()
	{
		return title;
	}


	
	public void setTitle(String[] title)
	{
		this.title = title;
	}


	
	public String[] getColumn()
	{
		return column;
	}


	
	public void setColumn(String[] column)
	{
		this.column = column;
	}


	
	public List<Map> getData()
	{
		return data;
	}


	
	public void setData(List<Map> data)
	{
		this.data = data;
	}


	
	public Map getSession()
	{
		return session;
	}


	public List<Map> getDetailResultList()
	{
		return detailResultList;
	}

	
	public void setDetailResultList(List<Map> detailResultList)
	{
		this.detailResultList = detailResultList;
	}

	public List getResultList()
	{
		return resultList;
	}

	public String getFlag()
	{
		return flag;
	}

	public void setFlag(String flag)
	{
		this.flag = flag;
	}

	public void setSession(Map session)
	{
		this.session = session;
	}

	public void setCountDeviceBio(CountDeviceBIO countDeviceBio)
	{
		this.countDeviceBio = countDeviceBio;
	}

}
