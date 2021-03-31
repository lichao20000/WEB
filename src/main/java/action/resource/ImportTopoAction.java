package action.resource;

import java.io.File;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.struts2.interceptor.ServletRequestAware;

import com.linkage.litms.system.UserRes;
import com.opensymphony.xwork2.ActionSupport;

import dao.resource.ImportTopoDAO;

public class ImportTopoAction extends ActionSupport implements ServletRequestAware
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -5111891654085472670L;
	// DAO
	private ImportTopoDAO importTopo;
	// topo图父id
	private String pid;
	// 坐标x
	private String x;
	// 坐标y
	private String y;
	// file
	private File file;
	// 返回信息
	private String msg;
	// request取登陆帐号使用
	private HttpServletRequest request;
	public void setImportTopo(ImportTopoDAO importTopo)
	{
		this.importTopo = importTopo;
	}
	public void setFile(File file)
	{
		this.file = file;
	}
	public void setServletRequest(HttpServletRequest arg0)
	{
		this.request = arg0;
	}
	public void setPid(String pid)
	{
		this.pid = pid;
	}
	public void setX(String x)
	{
		this.x = x;
	}
	public void setY(String y)
	{
		this.y = y;
	}
	public String getMsg()
	{
		return msg;
	}
	public String getPid()
	{
		return pid;
	}
	public String getX()
	{
		return x;
	}
	public String getY()
	{
		return y;
	}
	/**
	 * 导入设备资料
	 */
	public String execute() throws Exception
	{
		// request
		HttpSession session = request.getSession();
		UserRes curUser = (UserRes) session.getAttribute("curUser");
		int ret = importTopo.importTopo(file, curUser.getUser(), pid, x, y);
		// 输出返回信息
		if (ret == 0)
			{
				msg = "导入设备成功";
			}
		else
			if (ret == -1)
				{
					msg = "通知后台失败";
				}
			else
				if (ret == -2)
					{
						msg = "入库失败";
					}
				else
					if (ret == -3)
						{
							msg = "获取设备id失败";
						}
		return SUCCESS;
	}
	public String importForm() throws Exception
	{
		return "importForm";
	}
}
