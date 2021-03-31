package com.linkage.litms.warn;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.linkage.commons.db.DBUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.commons.db.PrepareSQL;
import com.linkage.litms.LipossGlobals;
import com.linkage.litms.common.database.Cursor;
import com.linkage.litms.common.database.DataSetBean;
import com.linkage.litms.common.database.QueryPage;
import com.linkage.litms.common.util.CommonMap;
import com.linkage.litms.common.util.FormUtil;
import com.linkage.litms.common.util.StringUtils;
import com.linkage.litms.fault.TrapProbe;
import com.linkage.litms.system.User;
import com.linkage.litms.system.UserRes;
import com.linkage.litms.system.dbimpl.DbUserRes;
import com.linkage.module.gwms.Global;
import com.linkage.module.gwms.dao.gw.EventLevelLefDAO;

/**
 * @author liuli
 * @version 1.00, 4/18/2007
 * @since jtwg_1.1.0
 */
@SuppressWarnings("unchecked")
public class WarnService
{
	/** log */
	private static Logger logger = LoggerFactory.getLogger(WarnService.class);
	private String m_VendorType_SQL = "select distinct event_layer_id,event_layer_name  from event_layer_def ";
	private String m_EventType_SQL = "select distinct event_level_id, event_level_name from event_level_def";
	private String m_resourceType_SQL = "select distinct resource_type_id,resource_name  from tab_resourcetype";
	private String m_EventtiType_SQL = "select distinct event_level_id,event_level_name from event_level_def";
	private String m_EventleiType_SQL = "select distinct event_oid,event_name from event_def where event_type=2  or event_type=3";
	private String m_shuxingType_SQL = "select distinct a.event_attr_name from event_attr_def a ,event_def b,event_attr_relation c where b.event_oid=c.event_oid and c.event_attr_oid = a.event_attr_oid";
	private String m_eventnameType_SQL = "select distinct  event_oid,event_name from event_def where event_type=2 or event_type=3";
	private String m_eventnametrapType_SQL = "select distinct  event_oid,event_name from event_def where event_type=1";
	private Cursor cursor = null;
	QueryPage qryp = new QueryPage();
	//设备型号
	private Map<String,String> typeMap = new HashMap<String,String>();
	// 生成所属层次下拉框
	public String getlevelList(String test)
	{
		PrepareSQL psql = new PrepareSQL(m_VendorType_SQL);
		psql.getSQL();
		cursor = DataSetBean.getCursor(m_VendorType_SQL);
		String strVendorTypeList = FormUtil.createListBox(cursor, "event_layer_id",
				"event_layer_name", false, test, "");
		return strVendorTypeList;
	}
	// 生成事件类型下拉框
	public String geteventleiList(String test)
	{
		PrepareSQL psql = new PrepareSQL(m_EventleiType_SQL);
		psql.getSQL();
		cursor = DataSetBean.getCursor(m_EventleiType_SQL);
		String streventleiList = FormUtil.createListBox(cursor, "event_oid",
				"event_name", false, test, "");
		return streventleiList;
	}
	// 生成告警提升级别下拉框
	public String geteventtiList(String test)
	{
		PrepareSQL psql = new PrepareSQL(m_EventtiType_SQL);
		psql.getSQL();
		cursor = DataSetBean.getCursor(m_EventtiType_SQL);
		String streventleiList = FormUtil.createListBox(cursor, "event_level_id",
				"event_level_name", false, test, "");
		return streventleiList;
	}
	// 生成告警等级下拉框
	public String geteventTypeList(String test)
	{
		PrepareSQL psql = new PrepareSQL(m_EventType_SQL);
		psql.getSQL();
		cursor = DataSetBean.getCursor(m_EventType_SQL);
		String strVendorTypeList = FormUtil.createListBox(cursor, "event_level_id",
				"event_level_name", false, test, "");
		return strVendorTypeList;
	}
	// 新增的迁移方法 for
	public String getresourceTypeList(String initValue)
	{
		PrepareSQL psql = new PrepareSQL(m_resourceType_SQL);
		psql.getSQL();
		cursor = DataSetBean.getCursor(m_resourceType_SQL);
		String strVendorTypeList = FormUtil.createListBox(cursor, "resource_type_id",
				"resource_name", true, initValue == null ? "" : initValue, "");
		return strVendorTypeList;
	}
	// 生成资源类型下拉框
	public String getresourceTypeList()
	{
		return getresourceTypeList(null);
	}
	// 生成属性名下拉框
	public String getshuxingTypeList()
	{
		PrepareSQL psql = new PrepareSQL(m_shuxingType_SQL);
		psql.getSQL();
		cursor = DataSetBean.getCursor(m_shuxingType_SQL);
		String strVendorTypeList = FormUtil.createListBox(cursor, "event_attr_name",
				"event_attr_name", true, "", "");
		return strVendorTypeList;
	}
	// //新增的迁移方法 for
	public String geteventnameTypeList(String initValue)
	{
		PrepareSQL psql = new PrepareSQL(m_eventnameType_SQL);
		psql.getSQL();
		cursor = DataSetBean.getCursor(m_eventnameType_SQL);
		String strVendorTypeList = FormUtil.createListBox(cursor, "event_oid",
				"event_name", false, initValue == null ? "" : initValue, "");
		return strVendorTypeList;
	}
	// 生成事件名称下拉框
	public String geteventnameTypeList()
	{
		return geteventnameTypeList(null);
	}
	// 生成Trap事件名称下拉框
	public String geteventnameTrapTypeList()
	{
		PrepareSQL psql = new PrepareSQL(m_eventnametrapType_SQL);
		psql.getSQL();
		cursor = DataSetBean.getCursor(m_eventnametrapType_SQL);
		String strVendorTypeList = FormUtil.createListBox(cursor, "event_oid",
				"event_name", true, "", "");
		return strVendorTypeList;
	}
	// 事件定义入库操作
	public String EventDing(HttpServletRequest request)
	{
		String strMsg = null;
		String strSQL = null;
		String strAction = request.getParameter("action");
		String str_event_oid = request.getParameter("event_oid");
		String ruleqw = request.getParameter("ruleqw");
		// logger.debug("str_event_oid------------------------"
		// +str_event_oid);
		if (strAction.equals("delete"))
			{
				// 删除操作
				strSQL = "delete from event_def where  event_type=1 and event_oid='"
						+ str_event_oid + "'";
			}
		else
			{
				// String str_event_oid = request.getParameter("event_oid");
				String str_event_name = request.getParameter("event_name");
				String str_level = request.getParameter("event_layer_id");
				// logger.debug("str_level------------------------"
				// +str_level);
				String str_layer = request.getParameter("event_level_id");
				String str_event_ma = request.getParameter("event_ma");
				String str_event_desc = request.getParameter("event_desc");
				String[] str_ipport = request.getParameterValues("ipport");
				// 增加操作
				if (strAction.equals("add"))
					{
						// 判断是否重复
						String tmpSql = "select * from event_def where event_oid='"
								+ str_event_oid + "'";
						Map map = null;
						PrepareSQL psql = new PrepareSQL(tmpSql);
						psql.getSQL();
						map = DataSetBean.getRecord(tmpSql);
						if (map != null)
							{
								strMsg = "事件OID\"" + str_event_oid
										+ "\"已经存在，请换一个事件OID名称。";
							}
						else
							{
								strSQL = "insert into event_def (event_oid,event_name,event_layer_id,event_level_id,event_fmt,event_desc,event_type) values ('"// 告警事件定义表
										+ str_event_oid
										+ "','"
										+ str_event_name
										+ "',"
										+ str_level
										+ ","
										+ str_layer
										+ ",'"
										+ str_event_ma + "','" + str_event_desc + "',1)";
								logger.debug("strSQL------------------------"
										+ strSQL);
								if (str_ipport != null)
									{
										for (int i = 0; i < str_ipport.length; i++)
											{
												String tmpipport = (String) str_ipport[i];
												String[] str = tmpipport.split("\\|");
												strSQL += "  insert into event_attr_relation (event_oid, event_attr_oid) values('" // 告警属性定义表
														+ str_event_oid
														+ "','"
														+ str[0]
														+ "') ";
												strSQL = StringUtils.replace(strSQL,
														",,", ",null,");
												strSQL = StringUtils.replace(strSQL,
														",,", ",null,");
												strSQL = StringUtils.replace(strSQL,
														",)", ",null)");
											}
									}
							}
					}
				else
					{
						// 修改操作
						// 判断是否重复
						String tmpSql = "select * from event_def where event_oid='"
								+ str_event_oid + "'";
						Map map = null;
						PrepareSQL psql = new PrepareSQL(tmpSql);
						psql.getSQL();
						map = DataSetBean.getRecord(tmpSql);
						if (map != null)
							{
								strMsg = "事件OID\"" + str_event_oid
										+ "\"已经存在，请换一个事件OID名称。";
							}
						else
							{
								strSQL = "update event_def set event_name='"
										+ str_event_name + "',event_layer_id="
										+ str_level + ",event_level_id=" + str_layer
										+ ",event_fmt='" + str_event_ma
										+ "',event_desc='" + str_event_desc
										+ "'  where event_oid='" + ruleqw + "'";
								// logger.debug("strSQL-------------liuli"
								// +strSQL);
								if (str_ipport != null)
									{
										for (int i = 0; i < str_ipport.length; i++)
											{
												String tmpipport = (String) str_ipport[i];
												String[] str = tmpipport.split("\\|");
												strSQL += "  update event_attr_relation set event_attr_oid='"
														+ str[0]
														+ "'  where event_oid='"
														+ ruleqw + "'  ";
												// logger.debug("strSQL----aaaaaaa---------liuli"
												// +strSQL);
												strSQL = StringUtils.replace(strSQL,
														",,", ",null,");
												strSQL = StringUtils.replace(strSQL,
														",,", ",null,");
												strSQL = StringUtils.replace(strSQL,
														",)", ",null)");
											}
									}
							}
					}
			}
		try
			{
				if (strMsg == null || strSQL != null)
					{
						PrepareSQL psql = new PrepareSQL(strSQL);
						psql.getSQL();
						int iCode[] = DataSetBean.doBatch(strSQL);
						if (iCode != null && iCode.length > 0)
							{
								strMsg = "操作成功！";
								// String gather_id = "";
								String _s = " ";
								HttpSession session = request.getSession();
								UserRes curUser = (UserRes) session
										.getAttribute("curUser");
								List _tem = curUser.getUserProcesses();
								for (int i = 0; i < _tem.size(); i++)
									{
										_s = (String) _tem.get(i);
										TrapProbe trapProbe = new TrapProbe(_s);
										try
											{
												trapProbe.I_NotifyEvent("qewee");
											}
										catch (Exception ex)
											{
												ex.printStackTrace();
												trapProbe.clearService();
											}
									}
							}
						else
							{
								strMsg = "操作失败，请返回重试或稍后再试！";
							}
					}
			}
		catch (Exception e)
			{
				e.printStackTrace();
			}
		return strMsg;
	}
	/**
	 * 事件定义显示操作
	 * 
	 * @param request
	 * @return
	 */
	public String getEventDingyiHtml(HttpServletRequest request)
	{
		if (Global.instAreaShortName != null && Global.instAreaShortName.equals(Global.JSDX))
			{
				HttpSession session = request.getSession();
				DbUserRes dbUserRes = (DbUserRes) session.getAttribute("curUser");
				User user = dbUserRes.getUser();
				String path = request.getServletPath();
				String role = String.valueOf(user.getRoleId());
				logger.debug("url路径:\n" + path + "角色：\n" + role);
			}
		// add end
		String strData = "";
		String sql = "select event_oid,event_name,event_layer_id,event_level_id,event_fmt,event_desc from event_def where event_type=1 order by event_level_id desc";
		String stroffset = request.getParameter("offset");
		int pagelen = 15;
		int offset;
		if (stroffset == null)
			offset = 1;
		else
			offset = Integer.parseInt(stroffset);
		qryp.initPage(sql, offset, pagelen);
		String strBar = qryp.getPageBar();
		PrepareSQL psql = new PrepareSQL(sql);
		psql.getSQL();
		Cursor cursor = DataSetBean.getCursor(sql, offset, pagelen);
		Map fields = cursor.getNext();
		if (fields == null)
			{
				strData = "<TR><TD COLSPAN=7 HEIGHT=30 CLASS=column>系统中没有定义告警事件信息</TD></TR>";
			}
		else
			{
				int i = 1;
				while (fields != null)
					{
						String content = (String) fields.get("event_layer_id");
						if (content.equals("1"))
							{
								content = content.replaceAll("1", "端口级");
							}
						if (content.equals("2"))
							{
								content = content.replaceAll("2", "版卡级");
							}
						if (content.equals("3"))
							{
								content = content.replaceAll("3", "设备级");
							}
						String content11 = (String) fields.get("event_level_id");
						EventLevelLefDAO eventLevelLefDAO=new EventLevelLefDAO();
						Map warnMap = eventLevelLefDAO.getWarnLevel();
						String content1 = (String)warnMap.get(content11);
//						String content1 = (String) fields.get("event_level_id");
//						if (content1.equals("1"))
//							{
//								content1 = content1.replaceAll("1", "正常日志");
//							}
//						if (content1.equals("2"))
//							{
//								content1 = content1.replaceAll("2", "提示告警");
//							}
//						if (content1.equals("3"))
//							{
//								content1 = content1.replaceAll("3", "一般告警");
//							}
//						if (content1.equals("4"))
//							{
//								content1 = content1.replaceAll("4", "严重告警");
//							}
//						if (content1.equals("5"))
//							{
//								content1 = content1.replaceAll("5", "紧急告警");
//							}
						strData += "<TR>";
						strData += "<TD CLASS=column1 align=center><input type=checkbox  name=Mycheckbox1 value= "
								+ fields.get("event_oid") + "></TD>";
						strData += "<TD CLASS=column1 align=center nowrap>"
								+ fields.get("event_name") + "</TD>";
						strData += "<TD CLASS=column2 align=center nowrap>" + content
								+ "</TD>";
						strData += "<TD CLASS=column1 align=center nowrap>" + content1
								+ "</TD>";
						strData += "<TD CLASS=column2 align=center nowrap><textarea readonly style='overflow:auto;vertical-align:middle;width:120px;height:20px;margin:0 0;'"
								+ " onmouseout='nd(); return true;' onmouseover=\"overlib('"
								+ fields.get("event_fmt")
								+ "'); return true;\">"
								+ fields.get("event_fmt") + "</textarea></TD>";
						strData += "<TD CLASS=column1 align=center nowrap><textarea readonly style='overflow:auto;vertical-align:middle;width:120px;height:20px;margin:0 0;'"
								+ " onmouseout='nd(); return true;' onmouseover=\"overlib('"
								+ fields.get("event_desc")
								+ "'); return true;\">"
								+ fields.get("event_desc") + "</textarea></TD>";
						
						strData += "<TD CLASS=column2 align=center nowrap><A HREF=event_dingyi_form_edit.jsp?event_oid="
								+ (String) fields.get("event_oid")
								+ ">修改| <A HREF=event_dingyi_from_save.jsp?action=delete&event_oid="
								+ (String) fields.get("event_oid")
								+ " onclick='return delWarn();'>删除</A></TD>";
						strData += "</TR>";
						i++;
						fields = cursor.getNext();
					}
				strData += "<TR><TD COLSPAN=7 align=right CLASS=column>" + strBar
						+ "</TD></TR>";
			}
		fields = null;
		return strData;
	}
	/**
	 * 过滤显示操作
	 * 
	 * @param request
	 * @return
	 */
	public String getEventGuolvHtml(HttpServletRequest request)
	{
		//初始化
		getDevicetype();
		if (Global.instAreaShortName != null && Global.instAreaShortName.equals(Global.JSDX))
			{
				HttpSession session = request.getSession();
				DbUserRes dbUserRes = (DbUserRes) session.getAttribute("curUser");
				User user = dbUserRes.getUser();
				String path = request.getServletPath();
				String role = String.valueOf(user.getRoleId());
				logger.debug("url路径:\n" + path + "角色：\n" + role);
			}
		// add end
		HttpSession session = request.getSession();
		UserRes curUser = (UserRes) session.getAttribute("curUser");
		User user = curUser.getUser();
		String username = (String) user.getAccount();
		String strData = "";
		String strData1 = "";// 临时告警
		String strData2 = "";// 常用告警
		String sql = "select rule_id,event_rule_name,device_model,rule_time,resource_type_id,event_level_id,gather_id,rule_user,def_time  from fault_filter_ruler where 1=1 ";
		if (!user.isAdmin())
			{
				List m_ProcessesList = curUser.getUserProcesses();
				for (int i = 0; i < m_ProcessesList.size(); i++)
					{
						String strChildList = (String) m_ProcessesList.get(i);
						sql += " and gather_id like '%" + strChildList + "%' ";
						logger.debug("sql------------liuli" + sql);
					}
			}
		String color = "white";// 区别临时告警和常用告警
		String stroffset = request.getParameter("offset");
		int pagelen = 15;
		int offset;
		if (stroffset == null)
			offset = 1;
		else
			offset = Integer.parseInt(stroffset);
		qryp.initPage(sql, offset, pagelen);
		String strBar = qryp.getPageBar();
		PrepareSQL psql = new PrepareSQL(sql);
		psql.getSQL();
		Cursor cursor = DataSetBean.getCursor(sql, offset, pagelen);
		String tempstr;
		String rule_time;
		String timerange = "";// 时间范围
		SimpleDateFormat st = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		long currenttime = new java.util.Date().getTime() / 1000;
		Map fields = cursor.getNext();
		if (fields == null)
			{
				strData = "<TR><TD COLSPAN=9 HEIGHT=30 CLASS=column>系统中没有定义告警过滤规则信息</TD></TR>";
			}
		else
			{
				int i = 1;
				while (fields != null)
					{
						rule_time = (String) fields.get("rule_time");
						// 对临时告警的颜色的处理
						color = "bgcolor='white'";
						logger.debug("rule_time=" + rule_time);
						if (rule_time != null && rule_time.indexOf("*") != -1
								&& !rule_time.endsWith("*"))
							{
								int startindex = rule_time.lastIndexOf(":");
								tempstr = rule_time.substring(startindex + 1, rule_time
										.length());
								logger.debug("tempstr=" + tempstr);
								long endtime = Long.parseLong(tempstr.substring(tempstr
										.lastIndexOf("-") + 1, tempstr.length()));
								logger.debug("endtime=" + endtime);
								long starttime = Long.parseLong(tempstr.substring(0,
										tempstr.lastIndexOf("-")));
								logger.debug("starttime=" + starttime);
								timerange = st
										.format(new java.util.Date(starttime * 1000))
										+ "-"
										+ st.format(new java.util.Date(endtime * 1000));
								if (starttime > currenttime || endtime < currenttime)
									{
										color = "bgcolor='red'";
									}
								else
									{
										color = "bgcolor='yellow'";
									}
								strData1 = getOneLineData(Global.instAreaShortName, username, strData1,
										color, timerange, fields);
								;
							}
						else
							{
								timerange = "";
								strData2 = getOneLineData(Global.instAreaShortName, username, strData2,
										color, timerange, fields);
								;
							}
						i++;
						fields = cursor.getNext();
					}
				strData = strData1 + strData2;
				strData += "<TR><TD COLSPAN=9 align=right CLASS=column>" + strBar
						+ "</TD></TR>";
			}
		fields = null;
		return strData;
	}
	//
	private String getOneLineData(String ShortName, String username, String strData,
			String color, String timerange, Map fields)
	{
		String content = "";
		if (fields.get("resource_type_id") != null)
			{
				content = (String) fields.get("resource_type_id");
				if (!content.equals("null"))
					{
						content = (String) (CommonMap.getResourceTypeMap().get(content));
					}
			}
		String content11 = (String) fields.get("event_level_id");
		EventLevelLefDAO eventLevelLefDAO=new EventLevelLefDAO();
		Map warnMap = eventLevelLefDAO.getWarnLevel();
		String content1 = (String)warnMap.get(content11);
//		String content1 = (String) fields.get("event_level_id");
//		if (content1.equals("0"))
//			{
//				content1 = content1.replaceAll("0", " ");
//			}
//		if (content1.equals("1"))
//			{
//				content1 = content1.replaceAll("1", "正常日志");
//			}
//		if (content1.equals("2"))
//			{
//				content1 = content1.replaceAll("2", "提示告警");
//			}
//		if (content1.equals("3"))
//			{
//				content1 = content1.replaceAll("3", "一般告警");
//			}
//		if (content1.equals("4"))
//			{
//				content1 = content1.replaceAll("4", "严重告警");
//			}
//		if (content1.equals("5"))
//			{
//				content1 = content1.replaceAll("5", "紧急告警");
//			}
		String content8 = (String) fields.get("device_model");
		if (content8.equals("-1"))
			{
				content8 = " ";
			}
		else
			{
				content8 = typeMap.get((String) fields.get("device_model"));
			}
		strData += "<TR rowspan=1 colspan=1 " + color + ">";
		strData += "<TD align=center><input type=checkbox  name=Mycheckbox value= "
				+ fields.get("rule_id") + "></TD>";// 选择
		strData += "<TD align=center>" + fields.get("event_rule_name") + "</TD>";// 规则名称
		strData += "<TD  align=center >" + content8 + "</textarea></TD>";// 设备型号
		strData += "<TD  align=center>" + timerange + "</TD>";// 告警等级 改为时间范围
		strData += "<TD  align=center><textarea readonly style='overflow:auto;vertical-align:middle;width:100px;height:20px;margin:0 0;'"
				+ " onmouseout='nd(); return true;' onmouseover=\"overlib('"
				+ ((String) fields.get("gather_id")).replaceAll(";", " ")
				+ "'); return true;\">"
				+ ((String) fields.get("gather_id")).replaceAll(";", " ")
				+ "</textarea></TD>";
		strData += "<TD  align=center>" + fields.get("rule_user") + "</TD>";// 规则定义者
		strData += "<TD  align=center>" + fields.get("def_time") + "</TD>";// 规则定义时间
		if (ShortName != null && ShortName.equals(Global.JSDX))
			{
				if (username.equals(fields.get("rule_user")))
					// 自己创建的 可以操作
					strData += "<TD  align=center><A HREF=event_guolv_edit_from.jsp?flag=1&rule_id="
							+ (String) fields.get("rule_id")
							+ ">修改</A>| <A HREF=event_guolv_from_save.jsp?action=delete&rule_id="
							+ (String) fields.get("rule_id")
							+ "&gather_id="
							+ (String) fields.get("gather_id")
							+ " onclick='return delWarn();'>删除</A>|";
				// 非自己创建的 只能查看
				else
					strData += "<TD CLASS=column2 align=center>";
			}
		else
			{
				if (username.equals("admin"))
					{
						strData += "<TD CLASS=column2 align=center><A HREF=event_guolv_edit_from.jsp?flag=1&rule_id="
								+ (String) fields.get("rule_id")
								+ ">修改| <A HREF=event_guolv_from_save.jsp?action=delete&rule_id="
								+ (String) fields.get("rule_id")
								+ "&gather_id="
								+ (String) fields.get("gather_id")
								+ " onclick='return delWarn();'>删除</A>|";
					}
				else
					if (username.equals((String) fields.get("rule_user")))
						{
							strData += "<TD CLASS=column2 align=center><A HREF=event_guolv_edit_from.jsp?flag=1&rule_id="
									+ (String) fields.get("rule_id")
									+ ">修改| <A HREF=event_guolv_from_save.jsp?action=delete&rule_id="
									+ (String) fields.get("rule_id")
									+ "&gather_id="
									+ (String) fields.get("gather_id")
									+ " onclick='return delWarn();'>删除</A>|";
						}
					else
						{
							strData += "<TD CLASS=column2 align=center>";
						}
			}
		strData += "<A HREF=event_guolv_edit_from.jsp?flag=2&rule_id="
				+ (String) fields.get("rule_id") + ">详细信息</A> </TD></TR>";
		return strData;
	}
	/**
	 * 获取事件类型的名称
	 * 
	 * @return
	 */
	public Map getAreaIdMapName()
	{
		String sql = "select event_oid,event_name from event_def";
		PrepareSQL psql = new PrepareSQL(sql);
		psql.getSQL();
		Map result = new HashMap();
		cursor = DataSetBean.getCursor(sql);
		Map fields = cursor.getNext();
		while (fields != null)
			{
				result.put(fields.get("event_oid"), fields.get("event_name"));
				fields = cursor.getNext();
			}
		return result;
	}
	/**
	 * 排重显示操作
	 * 
	 * @param request
	 * @return
	 */
	public String getEventpaichongHtml(HttpServletRequest request)
	{
		if (Global.instAreaShortName != null && Global.instAreaShortName.equals(Global.JSDX))
			{
				HttpSession session = request.getSession();
				DbUserRes dbUserRes = (DbUserRes) session.getAttribute("curUser");
				User user = dbUserRes.getUser();
				String path = request.getServletPath();
				String role = String.valueOf(user.getRoleId());
				logger.debug("url路径:\n" + path + "角色：\n" + role);
			}
		// add end
		Map Devicelist = this.getAreaIdMapName();
		HttpSession session = request.getSession();
		UserRes curUser = (UserRes) session.getAttribute("curUser");
		User user = curUser.getUser();
		String username = (String) user.getAccount();
		logger.debug("username---------------------------" + username);
		String strData = "";
		String sql = "select distinct rule_id,rule_name,event_oid,device_ip,elapse_time,event_level_id,is_active,gather_id ,rule_user,def_time from fault_filter_multi where 1=1 ";
		if (!user.isAdmin())
			{
				List m_ProcessesList = curUser.getUserProcesses();
				for (int i = 0; i < m_ProcessesList.size(); i++)
					{
						String strChildList = (String) m_ProcessesList.get(i);
						sql += " and gather_id like '%" + strChildList + "%' ";
						logger.debug("sql------------liuli" + sql);
					}
			}
		// sql +=" and a.event_oid=b.event_oid";
		logger.debug(">>>>>>> sql :" + sql);
		String stroffset = request.getParameter("offset");
		int pagelen = 15;
		int offset;
		if (stroffset == null)
			offset = 1;
		else
			offset = Integer.parseInt(stroffset);
		qryp.initPage(sql, offset, pagelen);
		String strBar = qryp.getPageBar();
		PrepareSQL psql = new PrepareSQL(sql);
		psql.getSQL();
		Cursor cursor = DataSetBean.getCursor(sql, offset, pagelen);
		Map fields = cursor.getNext();
		if (fields == null)
			{
				strData = "<TR><TD COLSPAN=10 HEIGHT=30 CLASS=column>系统中没有定义告警排重规则信息</TD></TR>";
			}
		else
			{
				int i = 1;
				while (fields != null)
					{
						String content = (String) fields.get("is_active");
						if (content.equals("1"))
							{
								content = content.replaceAll("1", "启用");
							}
						if (content.equals("0"))
							{
								content = content.replaceAll("0", "暂停");
							}
						String content11 = (String) fields.get("event_level_id");
						EventLevelLefDAO eventLevelLefDAO=new EventLevelLefDAO();
						Map warnMap = eventLevelLefDAO.getWarnLevel();
						String content1 = (String)warnMap.get(content11);
//						String content1 = (String) fields.get("event_level_id");
//						if (content1.equals("1"))
//							{
//								content1 = content1.replaceAll("1", "正常日志");
//							}
//						if (content1.equals("2"))
//							{
//								content1 = content1.replaceAll("2", "提示告警");
//							}
//						if (content1.equals("3"))
//							{
//								content1 = content1.replaceAll("3", "一般告警");
//							}
//						if (content1.equals("4"))
//							{
//								content1 = content1.replaceAll("4", "严重告警");
//							}
//						if (content1.equals("5"))
//							{
//								content1 = content1.replaceAll("5", "紧急告警");
//							}
						strData += "<TR>";
						strData += "<TD CLASS=column1 align=center><input type=checkbox  name=Mycheckbox value= "
								+ fields.get("rule_id")
								+ "&"
								+ (String) fields.get("gather_id")
								+ "&"
								+ (String) fields.get("is_active") + "></TD>";
						strData += "<TD CLASS=column2 align=center>"
								+ fields.get("rule_name") + "</TD>";
						String DEvice = "";
						if (Devicelist.get((String) fields.get("event_oid")) == null)
							{
								DEvice = "";
							}
						else
							{
								DEvice = (String) (Devicelist.get((String) fields
										.get("event_oid")));
							}
						strData += "<TD CLASS=column1 align=center>" + DEvice + "</TD>";
						strData += "<TD CLASS=column2 align=center>"
								+ fields.get("device_ip") + "</TD>";
						strData += "<TD CLASS=column1 align=center>"
								+ fields.get("elapse_time") + "</TD>";
						strData += "<TD CLASS=column2 align=center>" + content1 + "</TD>";
						strData += "<TD CLASS=column2 align=center>" + content + "</TD>";
						strData += "<TD CLASS=column2 align=center>"
								+ fields.get("rule_user") + "</TD>";
						strData += "<TD CLASS=column2 align=center>"
								+ fields.get("def_time") + "</TD>";
						if (Global.instAreaShortName != null && Global.instAreaShortName.equals(Global.JSDX))
							{
								// 看到所有按钮
								if (username.equals("admin")
										|| username.equals("superadmin"))
									{
										// 如果是admin看到所有的。
										strData += "<TD CLASS=column2 align=center><A HREF=event_paichong_edit_from.jsp?rule_id="
												+ (String) fields.get("rule_id")
												+ "&is_active="
												+ (String) fields.get("is_active")
												+ ">修改|<A HREF=event_paichong_show.jsp?rule_id="
												+ (String) fields.get("rule_id")
												+ "&DEvice="
												+ DEvice
												+ ">详细信息| <A HREF=event_paichong_from_save.jsp?action=delete&rule_id="
												+ (String) fields.get("rule_id")
												+ "&gather_id="
												+ (String) fields.get("gather_id")
												+ "&is_active="
												+ (String) fields.get("is_active")
												+ "  onclick='return delWarn();'>删除</A></TD>";
									}
								else
									if (username.equals((String) fields.get("rule_user")))
										{
											// 如果是本地网用户，只能看到自己定义的规则和admin的规则，admin的规则不能修改
											strData += "<TD CLASS=column2 align=center><A HREF=event_paichong_edit_from.jsp?rule_id="
													+ (String) fields.get("rule_id")
													+ "&is_active="
													+ (String) fields.get("is_active")
													+ ">修改|<A HREF=event_paichong_show.jsp?rule_id="
													+ (String) fields.get("rule_id")
													+ "&DEvice="
													+ DEvice
													+ ">详细信息| <A HREF=event_paichong_from_save.jsp?action=delete&rule_id="
													+ (String) fields.get("rule_id")
													+ "&gather_id="
													+ (String) fields.get("gather_id")
													+ "&is_active="
													+ (String) fields.get("is_active")
													+ "  onclick='return delWarn();'>删除</A></TD>";
										}
									else
										{
											strData += "<TD CLASS=column2 align=center></TD>";
										}
							}
						else
							{
								if (username.equals("admin"))
									{
										strData += "<TD CLASS=column2 align=center><A HREF=event_paichong_edit_from.jsp?rule_id="
												+ (String) fields.get("rule_id")
												+ "&is_active="
												+ (String) fields.get("is_active")
												+ ">修改|<A HREF=event_paichong_show.jsp?rule_id="
												+ (String) fields.get("rule_id")
												+ "&DEvice="
												+ DEvice
												+ ">详细信息| <A HREF=event_paichong_from_save.jsp?action=delete&rule_id="
												+ (String) fields.get("rule_id")
												+ "&gather_id="
												+ (String) fields.get("gather_id")
												+ "&is_active="
												+ (String) fields.get("is_active")
												+ "  onclick='return delWarn();'>删除</A></TD>";
									}
								else
									if (username.equals((String) fields.get("rule_user")))
										{
											strData += "<TD CLASS=column2 align=center><A HREF=event_paichong_edit_from.jsp?rule_id="
													+ (String) fields.get("rule_id")
													+ "&is_active="
													+ (String) fields.get("is_active")
													+ ">修改|<A HREF=event_paichong_show.jsp?rule_id="
													+ (String) fields.get("rule_id")
													+ "&DEvice="
													+ DEvice
													+ ">详细信息| <A HREF=event_paichong_from_save.jsp?action=delete&rule_id="
													+ (String) fields.get("rule_id")
													+ "&gather_id="
													+ (String) fields.get("gather_id")
													+ "&is_active="
													+ (String) fields.get("is_active")
													+ "  onclick='return delWarn();'>删除</A></TD>";
										}
									else
										{
											strData += "<TD CLASS=column2 align=center></TD>";
										}
							}
						strData += "</TR>";
						i++;
						fields = cursor.getNext();
					}
				strData += "<TR><TD COLSPAN=10 align=right CLASS=column>" + strBar
						+ "</TD></TR>";
			}
		fields = null;
		return strData;
	}
	/**
	 * 获取设备型号的对应关系
	 */
	private void getDevicetype(){
		String sql = "select * from gw_device_model";
		// teledb
		if (DBUtil.GetDB() == Global.DB_MYSQL) {
			sql = "select device_model_id, device_model from gw_device_model";
		}
		PrepareSQL psql = new PrepareSQL(sql);
		psql.getSQL();
		Cursor cursor = DataSetBean.getCursor(sql);
		Map fields = cursor.getNext();
		while (fields != null){
			typeMap.put(fields.get("device_model_id").toString(), (String)fields.get("device_model"));
			fields = cursor.getNext();
		}
	}
}
