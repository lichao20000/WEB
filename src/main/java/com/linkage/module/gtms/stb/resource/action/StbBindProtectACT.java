/**
 * 
 */
package com.linkage.module.gtms.stb.resource.action;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.struts2.interceptor.ServletRequestAware;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import action.splitpage.splitPageAction;

import com.linkage.litms.system.User;
import com.linkage.litms.system.UserRes;
import com.linkage.module.gtms.stb.resource.serv.StbBindProtectBIO;
import com.linkage.module.gwms.util.StringUtil;
/**
 * @author songxq
 * @version 1.0
 * @since 2019-9-16 下午3:16:58
 * @category 
 * @copyright Ailk NBS-Network Mgt. RD Dept.
 */
/**
 * @author ThinkPad
 *
 */
public class StbBindProtectACT extends splitPageAction implements
	ServletRequestAware 
{
	private static final long serialVersionUID = 1L;
	
	private static Logger logger = LoggerFactory
			.getLogger(StbBindProtectACT.class);
	
	private StbBindProtectBIO bio;
	
	private String userName = null;
	
	private String mac = null;
	
	private String userNameEdit = null;
	
	private String macEdit = null;
	
	private String remarkEdit = null;
	
	private String acc_oid = null;
	
	private String remark = null;
	
	private String ajax = null;
	
	private String[] title;
	
	private int total = 0;
	
	private String opr =null;
	
	private String ip = "";
	
	private String menuName = null;
	
	private String operationContent = null;
	
	
	private List<HashMap<String, String>> data = null;
	
	/** 详细信息展示 */
	private List<HashMap<String, String>> detailResultList = null;
	
	HttpServletRequest request = null;
	
	
	public String add()
	{
		menuName = "绑定关系保护增加";
		operationContent = "绑定关系保护管理-新增绑定关系["+userName+"]-["+mac+"]";
		ip =request.getRemoteHost();
		HttpSession session = request.getSession();
		UserRes curUser = (UserRes) session.getAttribute("curUser");
		User user = curUser.getUser();
		acc_oid = StringUtil.getStringValue(user.getId());
		ajax = bio.add(userName,mac,acc_oid,remark);
		if("1".equals(ajax))
		{
			logger.warn("增加绑定保护关系成功，操作者[{}],操作者ip[{}]",user.getId(),ip);
		}
		else 
		{
			logger.error("增加绑定保护关系失败，操作者[{}],操作者ip[{}]",user.getId(),ip);
		}
		bio.insertOperLog(ip, user.getId(), ajax, menuName, operationContent);
		return "ajax";
	}
	
	public String deleteData()
	{
		menuName = "绑定关系保护管理(带删除)";
		operationContent = "绑定关系保护管理-删除绑定关系["+userName+"]-["+mac+"]";
		ip =request.getRemoteHost();
		HttpSession session = request.getSession();
		UserRes curUser = (UserRes) session.getAttribute("curUser");
		User user = curUser.getUser();
		int result = bio.delete(userName,mac);
		ajax = StringUtil.getStringValue(result);
		if("1".equals(ajax))
		{
			logger.warn("删除绑定保护关系成功，操作者[{}],操作者ip[{}]",user.getId(),ip);
		}
		else 
		{
			logger.error("删除绑定保护关系失败，操作者[{}],操作者ip[{}]",user.getId(),ip);
		}
		bio.insertOperLog(ip,user.getId(),ajax,menuName,operationContent);
		
		return "ajax";
	}
	
	public String update()
	{
		menuName = "绑定关系保护管理(带编辑)";
		if("3".equals(opr))
		{
			menuName = "绑定关系保护管理(带删除)";
		}
		operationContent = "绑定关系保护管理-编辑绑定关系["+userName+"]-["+mac+"]";
		ip =request.getRemoteHost();
		HttpSession session = request.getSession();
		UserRes curUser = (UserRes) session.getAttribute("curUser");
		User user = curUser.getUser();
		int result = bio.update(userName,mac,userNameEdit,macEdit,remarkEdit);
		if(1 == result)
		{
			ajax = "1";
			logger.warn("编辑绑定保护关系成功，操作者[{}],操作者ip[{}]",user.getId(),ip);
		}else {
			ajax = "0";
			logger.error("编辑绑定保护关系失败，操作者[{}],操作者ip[{}]",user.getId(),ip);
		}
		bio.insertOperLog(ip, user.getId(), ajax, menuName, operationContent);
		return "ajax";
	}
	
	public String query()
	{
		data = bio.query(userName,mac,curPage_splitPage,num_splitPage);
		total = bio.queryNum(userName, mac);
		if (total % num_splitPage == 0) {
			maxPage_splitPage = total / num_splitPage;
		} else {
			maxPage_splitPage = total / num_splitPage + 1;
		}
		if("1".equals(opr))
		{
			//只有查看详情按钮
			return "queryResult";
		}
		else if ("2".equals(opr)) 
		{
			//有查看详情和编辑按钮
			return "editResult";
		}else 
		{
			//有查看详情,编辑和删除按钮
			return "deleteResult";
		}
	}
	
	public String getdetail()
	{
		data = bio.query(userName,mac);
		return "queryDetailResult";
	}
	
	
	@Override
	public void setServletRequest(HttpServletRequest arg0)
	{
		// TODO Auto-generated method stub
		this.request = arg0;
	}




	
	
	public StbBindProtectBIO getBio()
	{
		return bio;
	}

	
	public void setBio(StbBindProtectBIO bio)
	{
		this.bio = bio;
	}

	public String getUserName()
	{
		return userName;
	}


	
	public void setUserName(String userName)
	{
		this.userName = userName;
	}


	
	public String getMac()
	{
		return mac;
	}


	
	public void setMac(String mac)
	{
		this.mac = mac;
	}


	
	public String getAcc_oid()
	{
		return acc_oid;
	}


	
	public void setAcc_oid(String acc_oid)
	{
		this.acc_oid = acc_oid;
	}


	
	public String getRemark()
	{
		return remark;
	}


	
	public void setRemark(String remark)
	{
		try
		{
			this.remark = URLDecoder.decode(remark, "utf-8");
		}
		catch (UnsupportedEncodingException e)
		{
			this.remark = remark;
		}
	}

	
	public String getAjax()
	{
		return ajax;
	}

	
	public void setAjax(String ajax)
	{
		this.ajax = ajax;
	}

	public List<HashMap<String, String>> getData()
	{
		return data;
	}

	
	public void setData(List<HashMap<String, String>> data)
	{
		this.data = data;
	}


	
	
	public List<HashMap<String, String>> getDetailResultList()
	{
		return detailResultList;
	}

	
	public void setDetailResultList(List<HashMap<String, String>> detailResultList)
	{
		this.detailResultList = detailResultList;
	}

	public String[] getTitle()
	{
		return title;
	}

	
	public void setTitle(String[] title)
	{
		this.title = title;
	}

	
	public int getTotal()
	{
		return total;
	}

	
	public void setTotal(int total)
	{
		this.total = total;
	}

	
	public String getOpr()
	{
		return opr;
	}

	
	public void setOpr(String opr)
	{
		this.opr = opr;
	}

	
	public String getUserNameEdit()
	{
		return userNameEdit;
	}

	
	public void setUserNameEdit(String userNameEdit)
	{
		this.userNameEdit = userNameEdit;
	}

	
	public String getMacEdit()
	{
		return macEdit;
	}

	
	public void setMacEdit(String macEdit)
	{
		this.macEdit = macEdit;
	}

	
	public String getRemarkEdit()
	{
		return remarkEdit;
	}

	
	public void setRemarkEdit(String remarkEdit)
	{
		try
		{
			this.remarkEdit = URLDecoder.decode(remarkEdit, "utf-8");
		}
		catch (UnsupportedEncodingException e)
		{
			this.remarkEdit = remarkEdit;
		}
	}

	
	public String getIp()
	{
		return ip;
	}

	
	public void setIp(String ip)
	{
		this.ip = ip;
	}

	
	public String getMenuName()
	{
		return menuName;
	}

	
	public void setMenuName(String menuName)
	{
		this.menuName = menuName;
	}

	
	public String getOperationContent()
	{
		return operationContent;
	}

	
	public void setOperationContent(String operationContent)
	{
		this.operationContent = operationContent;
	}
	
	
	
	
}

