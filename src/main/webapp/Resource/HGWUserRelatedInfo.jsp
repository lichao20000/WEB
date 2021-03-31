<%@ page contentType="text/html;charset=GBK"%>
<%@ page
	import="com.linkage.litms.common.database.Cursor,com.linkage.litms.common.database.DataSetBean,java.util.*,com.linkage.litms.common.util.DateTimeUtil,com.linkage.litms.Global,java.util.List,java.util.Iterator"%>
<%@ page import="java.text.SimpleDateFormat"%>
<%@page import="com.linkage.module.gwms.util.StringUtil"%>
<%@page import="com.linkage.litms.LipossGlobals"%>
<jsp:useBean id="HGWUserInfoAct" scope="request"
	class="com.linkage.litms.resource.HGWUserInfoAct" />

<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="lk" uri="/linkage"%>

<%@ include file="../timelater.jsp"%>

<script type="text/javascript" src="<s:url value="/Js/jquery.js"/>"></script>
<SCRIPT LANGUAGE="JavaScript">
	function DetailDevice(device_id) {
		var strpage = "DeviceShow.jsp?device_id=" + device_id;
		window
				.open(strpage, "",
						"left=20,top=20,width=900,height=650,resizable=yes,scrollbars=yes");
	}
</SCRIPT>


<%@ include file="../head.jsp"%>
<%@ include file="../toolbar.jsp"%>
<%
	String user_id = request.getParameter("user_id");
  //  String access_style_id = request.getParameter("access_style_id");
	String gather_id = request.getParameter("gather_id");
	//获得当前用户的基本信息
	Cursor cursor = HGWUserInfoAct.getUserRelatedBaseInfo(request);
	Map fields = cursor.getNext();
	String user_type_name = HGWUserInfoAct.getUserDevType(user_id);
	//获得可用业务
	Cursor businessCursor = HGWUserInfoAct.getServiceInfo(user_id);
	//获得业务变更信息
	//Cursor cursor_all_chg_ser = HGWUserInfoAct.getUserRelatedSerInfo(user_id,
	//		true);
	//Map fields_all_chg_ser = cursor_all_chg_ser.getNext();
	//获得用户的管理域
	Cursor cursor_area = HGWUserInfoAct.getUserArea(user_id);
	Cursor cardCursor = HGWUserInfoAct.getUserCardInfo(user_id);
	Map fields_area = cursor_area.getNext();
	SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd kk:mm:ss");
	Calendar time = Calendar.getInstance();
	//绑定方式
	Map<String, String> bindTypeMap = HGWUserInfoAct.getBindType();
	Map<String, String> userTypeMap = HGWUserInfoAct.getUserType();
%>
<TABLE border=0 cellspacing=0 cellpadding=0 width="100%" id="myTable">
	<TR>
		<TD HEIGHT=20>&nbsp;</TD>
	</TR>
	<TR>
		<TD>
			<TABLE width="100%" border=0 cellspacing=0 cellpadding=0
				align="center">
				<TR>
					<TD bgcolor=#000000>

						<TABLE border=0 cellspacing=1 cellpadding=2 width="100%">
							<TR style="cursor: hand; background-color: #cccccc">
								<TD colspan="4">
									<TABLE CELLPADDING="0" CELLSPACING="0" WIDTH="100%" BORDER="0">
										<TR>
											<TH>用户基本信息</TH>
										</TR>
									</TABLE>
								</TD>
							</TR>
							<TR bgcolor="#FFFFFF">
								<TD class=column align="right" width="20%">用户帐户：</TD>
								<TD width="30%">
									<%	
							String username ="";
							if(null!=fields){
								if(null!=fields.get("username"));
								username = (String)fields.get("username");
							}
						%> <%=username%>
								</TD>
								<TD class=column align="right" width="20%">用户ID：</TD>
								<TD width="30%">
									<%
						    String userId ="" ;
						  if(null!=fields){
						  	if(null!=fields.get("user_id")){
						     	userId = (String)fields.get("user_id");
						  	}
						  }
						%><%=userId %>
								</TD>
							</TR>

							<TR bgcolor="#FFFFFF">
								<TD class=column align="right" width="20%">用户姓名：</TD>
								<TD width="30%">
									<%
						String realname ="";
						 if(null!=fields){
						  	if(null!=fields.get("realname")){
						     	realname = (String)fields.get("realname");
						  	}
						  }
						%><%=realname %>
								</TD>
								<TD class=column align="right" width="20%">接入方式：</TD>
								<TD width="30%">
									<% String type_name ="";
						 if(null!=fields){
						  	if(null!=fields.get("type_name")){
						  		type_name = (String)fields.get("type_name");
						  	}
						  }
						%><%=type_name %>
								</TD>
							</TR>

							<TR bgcolor="#FFFFFF">
								<TD class=column align="right" width="20%">终端规格：</TD>
								<TD width="30%">
									<% String spec_name ="";
						 if(null!=fields){
						  	if(null!=fields.get("spec_name")){
						  		spec_name = (String)fields.get("spec_name");
						  	}
						  }
						%><%=spec_name %>
								</TD>
								<TD class=column align="right">用户状态：</TD>
								<TD>
									<% String user_state = "";
						 if(null!=fields){
						  	if(null!=fields.get("user_state")){
						     			user_state = (String)fields.get("user_state");
										if ("0".equals(user_state))
										{
											user_state = "删除用户";
										}
										else if ("1".equals(user_state))
										{
											user_state = "开户";
										}
										else if ("2".equals(user_state))
										{
											user_state = "暂停";
										}
										else if ("3".equals(user_state))
										{
											user_state = "销户";
										}
										else if ("4".equals(user_state))
										{
											user_state = "更换设备";
										}
										else
										{
											user_state = "-";
										}
										
							}
						   }
						%> <%=user_state%></TD>
							</TR>

							<TR bgcolor="#FFFFFF">
								<TD class=column align="right">用户来源：</TD>
								<TD>
									<%
					         String tmp = "手工添加";
					         	if(null!=fields){
					         		if(null!=fields.get("user_type_id")){
										String user_type_id = (String) fields.get("user_type_id");
										if (false == StringUtil.IsEmpty(user_type_id))
										{
											tmp = userTypeMap.get(user_type_id);
											if (true == StringUtil.IsEmpty(tmp))
											{
												tmp = "其他";
											}
										}
									}
								}
						%> <%=tmp%></TD>
								<TD class=column align="right">证件类型：</TD>
								<TD>
									<%
						   if(null!=fields){
					         		if(null!=fields.get("cred_type_id")){
										String cred_type_id = (String) fields.get("cred_type_id");
										if (cred_type_id.equals("0"))
										{
											out.println("未选择");
										}
										else if (cred_type_id.equals("1"))
										{
											out.println("其他");
										}
										else if (cred_type_id.equals("2"))
										{
											out.println("身份证");
										}
										else if (cred_type_id.equals("3"))
										{
											out.println("军官证");
										}
										else if (cred_type_id.equals("4"))
										{
											out.println("工作证");
										}
										else
										{
											out.println("未知");
										}
									}
								}
									%>
								</TD>
							</TR>


							<TR bgcolor="#FFFFFF">
								<TD class=column align="right">证件号码：</TD>
								<TD>
									<%
						String credno ="";
						  if(null!=fields){
					         		if(null!=fields.get("credno")){
					         		  credno = (String) fields.get("credno");
					         		}
					         	}
						 %> 
						 <%if("xj_dx".equals(LipossGlobals.getLipossProperty("InstArea.ShortName"))
								 || "jx_dx".equals(LipossGlobals.getLipossProperty("InstArea.ShortName"))) {%>
						    <input type="password" value="<%=credno%>" readonly="true" style="border:none"/>
						 <%}else{%>
						   <%=credno%>
						  <%} %>
						 </TD>
								<TD class=column align="right">所属省市：</TD>
								<TD>
									<%
						
						    if(null!=fields){
						       if(null!=fields.get("city_id")){
												String strsql_1 = "select city_id,city_name from tab_city where city_id='"
												+ (String) fields.get("city_id".toLowerCase())
												+ "'  order by city_id";
										Cursor cursor_1 = DataSetBean.getCursor(strsql_1);
										Map fields_1 = cursor_1.getNext();
										if (fields_1 == null)
										{
											out.println("未知");
										}
										else
										{
											String city_name = (String) fields_1.get("city_name".toLowerCase());
											out.println(city_name);
										}
									}
								}
									%>
								</TD>
							</TR>
							<TR bgcolor="#FFFFFF">
								<TD class=column align="right">局向标识：</TD>
								<TD>
									<%
							  if(null!=fields){
						       if(null!=fields.get("office_id")){
												String strsql_2 = "select office_id,office_name from tab_office where office_id='"
												+ (String) fields.get("office_id".toLowerCase())
												+ "' order by office_id";
										Cursor cursor_2 = DataSetBean.getCursor(strsql_2);
										Map fields_2 = cursor_2.getNext();
										if (fields_2 == null)
										{
											out.println("未知");
										}
										else
										{
											String office_name = (String) fields_2.get("office_name".toLowerCase());
											out.println(office_name);
										}
									}
									}
									%>
								</TD>

								<TD class=column align="right">管理域：</TD>
								<TD>
									<%
										if (fields_area == null)
										{
											out.print("未知");
										}
										else
										{
											out.print((String) fields_area.get("area_name".toLowerCase()));
										}
									%>&nbsp;
								</TD>
							</TR>
							<TR bgcolor="#FFFFFF">
								<TD class=column align="right">用户类型：</TD>
								<TD><%=user_type_name%></TD>
								<!-- <TD class=column align="right">
									接入设备IP：
								</TD>
								<TD><=(String) fields.get("device_ip".toLowerCase())%></TD>
                                -->
								<TD class=column align="right">受理时间：</TD>
								<TD>
									<%
										String dealdate = "";
										if(null!=fields){
											if (!(fields.get("dealdate").equals("") && fields.get("dealdate") != null))
											{
												time
												.setTimeInMillis((Long.parseLong((String) fields.get("dealdate"))) * 1000);
												dealdate = df.format(time.getTime());
											}
										}
									%> <%=dealdate%></TD>
							</TR>


							<TR bgcolor="#FFFFFF">
								<TD class=column align="right">开户时间：</TD>
								<TD>
									<%
										String opendate = "";
										if(null!= fields){
											if (fields.get("opendate") != null && !(fields.get("opendate").equals("")))
											{
												time
												.setTimeInMillis((Long.parseLong((String) fields.get("opendate"))) * 1000);
												opendate = df.format(time.getTime());
											}
										}
									%> <%=opendate%></TD>
								<TD class=column align="right">开通时间：</TD>
								<TD>
									<%
										String onlinedate = "";
										if(null!=fields){
										if (!(fields.get("onlinedate").equals("") && fields.get("onlinedate") != null))
										{
											time
											.setTimeInMillis((Long.parseLong((String) fields
													.get("onlinedate"))) * 1000);
											onlinedate = df.format(time.getTime());
										}
									}
									%> <%=onlinedate%></TD>
							</TR>

							<TR bgcolor="#FFFFFF">
								<TD class=column align="right">暂停时间：</TD>
								<TD>
									<%
										String pausedate = "";
										if(null!=fields){
											if (!(fields.get("pausedate").equals("") && fields.get("pausedate") != null))
											{
												time
												.setTimeInMillis((Long
														.parseLong((String) fields.get("pausedate"))) * 1000);
												pausedate = df.format(time.getTime());
											}
										}
									%> <%=pausedate%></TD>
								<TD class=column align="right">销户时间：</TD>
								<TD>
									<%
										String closedate = "";
										if(null!=fields){
										if (!(fields.get("closedate").equals("") && fields.get("closedate") != null))
										{
											time
											.setTimeInMillis((Long
													.parseLong((String) fields.get("closedate"))) * 1000);
											closedate = df.format(time.getTime());
										}
										}
									%> <%=closedate%></TD>
							</TR>

							<TR bgcolor="#FFFFFF">
								<TD class=column align="right">更新时间：</TD>
								<TD>
									<%
										String updatetime = "";
										if(null!=fields){
										if (null!=(fields.get("updatetime"))&&!fields.get("updatetime").equals(""))
										{
											time
											.setTimeInMillis((Long.parseLong((String) fields
													.get("updatetime"))) * 1000);
											updatetime = df.format(time.getTime());
										}
									 }
									%> <%=updatetime%></TD>
								<!-- 对于江苏地区，显示活跃用户 -->
								<% if ("js_dx".equals(LipossGlobals.getLipossProperty("InstArea.ShortName"))
								     ||"sd_lt".equals(LipossGlobals.getLipossProperty("InstArea.ShortName"))){ %>
								<TD class=column align="right">活跃用户：</TD>
								<TD>
									<%
						  			if(null!=fields){
					         		if(null!=fields.get("is_active")){
										String is_active = (String) fields.get("is_active");
										if (is_active.equals("0"))
										{
											out.println("否");
										}
										else if (is_active.equals("1"))
										{
											out.println("是");
										}
										else
										{
											out.println("未知");
										}
									}
								}
								%>
								</TD>
								<%}
								/*对于非江苏地区*/
								else if(!"sd_lt".equals(LipossGlobals.getLipossProperty("InstArea.ShortName"))){ %>
								<TD class=column align="right">地址方式：</TD>
								<TD>
									<%
										String ipType = HGWUserInfoAct.getTabNetServParamByUserId(user_id);
									%> <%=ipType %>
								</TD>
								<%} %>
							</TR>
							
							<%if("sx_lt".equals(LipossGlobals.getLipossProperty("InstArea.ShortName"))){ %>
							<TR bgcolor="#FFFFFF">
								<TD class=column align="right" width="20%">用户地址：</TD>
								<TD width="30%" colspan="3">
									<%
									String linkaddres ="";
									 if(null!=fields){
									  	if(null!=fields.get("linkaddress")){
									  		linkaddres = (String)fields.get("linkaddress");
									  	}
									  }
									%><%=linkaddres %>
								</TD>
							</TR>
							<%} %>


							<!-- 用户信息不可泄露，对于江苏用户-->
							<% if ("js_dx".equals(LipossGlobals.getLipossProperty("InstArea.ShortName"))){ %>
							<TR bgcolor="#FFFFFF">
								<TD height="20" colspan="4"></TD>
							</TR>

							<TR style="cursor: hand; background-color: #cccccc">
								<TD colspan="4">
									<TABLE CELLPADDING="0" CELLSPACING="0" WIDTH="100%" BORDER="0">
										<TR>
											<Td class="green_title2">用户联系信息</Td>
										</TR>
									</TABLE>
								</TD>
							</TR>
							<TR bgcolor="#FFFFFF">

								<TD class=column align="right">联系人Email：</TD>
								<TD>
									<%
							String email = "";
							if(null!=fields){
								if(null!=fields.get("email")){
									email = (String) fields.get("email");
								}
							}
						%> <%=email %></TD>
								<TD class=column align="right">联系电话：</TD>
								<TD>
									<%
							String linkphone = "";
							if(null!=fields){
								if(null!=fields.get("linkphone")){
									linkphone = (String) fields.get("linkphone");
								}
							}
						%> <%=linkphone %></TD>
							</TR>
							<TR bgcolor="#FFFFFF">

								<TD class=column align="right">联系人地址：</TD>
								<TD>
									<%
							String linkaddress = "";
							if(null!=fields){
								if(null!=fields.get("linkaddress")){
									linkaddress = (String) fields.get("linkaddress");
								}
							}
						%> <%=linkaddress %>
								</TD>
								<TD class=column align="right">安装住址：</TD>
								<TD>
									<%
							String address = "";
							if(null!=fields){
								if(null!=fields.get("address")){
									address = (String) fields.get("address");
								}
							}
						%> <%=address %>
								</TD>

							</TR>
							<TR bgcolor="#FFFFFF">
								<TD class=column align="right">客户类型：</TD>
								<TD>
									<%
									if(null!=fields){
									  if(null!= fields.get("cust_type_id")){
										String cust_type_id = (String) fields.get("cust_type_id".toLowerCase());
										if (cust_type_id.equals("0"))
										{
											%> 公司客户 <%
												}
												else if (cust_type_id.equals("1"))
												{
											%> 网吧客户 <%
												}
												else
												{
											%> 个人客户 <%
											}
								      }
								     }
						 %>
								</TD>
								<TD class=column align="right"></TD>
								<TD></TD>
							</TR>
							<%} %>
							<!-- 用户信息不可泄露 -->
							<TR bgcolor="#FFFFFF">
								<TD height="20" colspan="4"></TD>
							</TR>
							<TR style="cursor: hand; background-color: #cccccc">
								<TD colspan="4">
									<TABLE CELLPADDING="0" CELLSPACING="0" WIDTH="100%" BORDER="0">
										<TR>
											<Td class="green_title2">用户绑定设备信息</Td>
										</TR>
									</TABLE>
								</TD>
							</TR>
							<%
					   if(null!=fields){
					   	if(null!=fields.get("device_id")){
								if (false == StringUtil.IsEmpty((String) fields.get("device_id")))
								{
							%>
							<TR bgcolor="#FFFFFF">
								<TD class=column align="right">当前使用的设备：</TD>
								<TD><a
									onclick="DetailDevice('<%=((String) fields.get("device_id"))%>')"
									href="javascript:"><%=((String) fields.get("device_id"))%></a></TD>
								<TD class=column align="right">设备序列号</TD>
								<td><%=((String) fields.get("oui") + "-" + (String) fields
								.get("device_serialnumber"))%></td>
							</TR>
							<TR bgcolor="#FFFFFF">

								<TD class=column align="right">设备MAC地址：</TD>
								<td><%=(String) fields.get("cpe_mac") == null ? ""
						: (String) fields.get("cpe_mac")%></td>
								<TD class=column align="right">绑定时间：</TD>
								<TD>
									<%
											String binddate = "";
											if (!(fields.get("binddate").equals("") && fields.get("binddate") != null))
											{
												time
												.setTimeInMillis((Long.parseLong((String) fields
												.get("binddate"))) * 1000);
												binddate = df.format(time.getTime());
											}
									%> <%=binddate%></TD>
							</TR>
							<TR bgcolor="#FFFFFF">
								<TD class=column align="right">绑定方式：</TD>
								<TD>
									<%
											String bindtype = "";
											String userline = (String) fields.get("userline");
											bindtype = bindTypeMap.get(userline);
											if (false == StringUtil.IsEmpty(bindtype))
											{
											}
											else
											{
												bindtype = "-";
											}
									%> <%=bindtype%></TD>
								<%
										if ("js_dx".equals(LipossGlobals.getLipossProperty("InstArea.ShortName")))
										{
								%>
								<TD class=column align="right">绑定验证：</TD>
								<TD>
									<%
												String is_chk_bind = "";
												if ("0".equals((String) fields.get("is_chk_bind")))
												{
											is_chk_bind = "未验证&nbsp;<IMG SRC='../images/button_s.gif' BORDER='0' ALT='未验证' style='cursor:hand'>";
												}
												else if ("1".equals((String) fields.get("is_chk_bind")))
												{
											is_chk_bind = "MAC绑定验证成功&nbsp;<IMG SRC='../images/check.gif' BORDER='0' ALT='验证成功' style='cursor:hand'>";
												}
												else if ("2".equals((String) fields.get("is_chk_bind")))
												{
											is_chk_bind = "桥接账号验证成功&nbsp;<IMG SRC='../images/check.gif' BORDER='0' ALT='验证成功' style='cursor:hand'>";
												}
									%> <%=is_chk_bind%></TD>
								<%
										}
										else
										{
								%>
								<TD class=column align="right"></TD>
								<TD></TD>
							</TR>
							<%
								}
								}
								else
								{
							%>
							<TR bgcolor="#FFFFFF">
								<TD class=column colspan="4" align="center"><font
									color="red">用户未绑定设备！</font> <%
											if (false == StringUtil.IsEmpty((String) fields
											.get("device_serialnumber")))
											{
									%> <br> <font color="red">以下设备序列号是BSS发过来的工单中带的，但设备没有上报，无法绑定！</font>
									<%
									}
									%></TD>
							</TR>
							<TR bgcolor="#FFFFFF">
								<TD class=column align="right">设备序列号</TD>
								<td><%=((String) fields.get("oui") + "-" + (String) fields
								.get("device_serialnumber"))%></td>
								<TD class=column align="right">绑定时间：</TD>
								<TD>
									<%
											String binddate = "";
											if (!(fields.get("binddate").equals("") && fields.get("binddate") != null))
											{
												time
												.setTimeInMillis((Long.parseLong((String) fields
												.get("binddate"))) * 1000);
												binddate = df.format(time.getTime());
											}
									%> <%=binddate%></TD>
							</TR>

							<%
							}
						}
						}
							%>
							<%if( null != cardCursor.getNext()){%>
							<!-- 用户卡信息 -->
							<TR bgcolor="#FFFFFF">
								<TD height="20" colspan="4"></TD>
							</TR>
							<TR style="cursor: hand; background-color: #cccccc">
								<TD colspan="4">
									<TABLE CELLPADDING="0" CELLSPACING="0" WIDTH="100%" BORDER="0">
										<TR>
											<Td class="green_title2">用户绑定卡信息</Td>
										</TR>
									</TABLE>
								</TD>
							</TR>

							<TR bgcolor="#FFFFFF">
								<%
								Map cardMap = cardCursor.getNext();
								if( null != cardMap)
								{
								 String device_id = cardMap.get("device_id") == null ? "" : (String)cardMap.get("device_id");
							%>
								<TD class=column align="right">卡序列号</TD>
								<TD>
									<%
										out.print(cardMap.get("card_serialnumber"));
									%>
								</TD>
								<TD class=column align="right">在线状态</TD>
								<TD><span> <%
										/**
										if(cardMap.get("online_status") == null)
										{
											out.print("下线");
										}
										else if(cardMap.get("online_status").equals("0"))
										{
											out.print("下线");
										}
										else
										{
											out.print("在线");
										}
										**/
									%>
								</span> &nbsp;&nbsp;&nbsp;&nbsp;
									<button TYPE="button" class=btn
										onclick="testOnline(this,'<%= device_id%>')">检测在线状态</button></TD>
								<%
									}
									else
									{
									%>
								<TD class=column colspan="4" align="center"><font
									color="red">用户未绑定卡信息！</font></TD>
								<%
									}
									%>
							</TR>
							<%}%>
							<!--  -->

							<TR bgcolor="#FFFFFF">
								<TD height="20" colspan="4"></TD>
							</TR>
							<TR style="cursor: hand; background-color: #cccccc">
								<TD colspan="4">
									<TABLE CELLPADDING="0" CELLSPACING="0" WIDTH="100%" BORDER="0">
										<TR>
											<Td class="green_title2">用户当前拥有业务</Td>
										</TR>
									</TABLE>
								</TD>
							</TR>

							<%
								Map map = businessCursor.getNext();
								int i = 1;
								while (null != map)
								{
							%>
							<TR bgcolor="#FFFFFF">
								<TD class=column align="right">业务名称<%=i%>：
								</TD>
								<TD>
									<%
									out.print(map.get("serv_type_name"));
									%>
								</TD>
								<TD class=column align="right">业务账号<%=i%>：
								</TD>
								<TD>
									<%
									out.print(map.get("username"));
									%>
								</TD>
							</TR>
							<%
									if ("10".equals(map.get("serv_type_id")))
									{
										//上网业务
										Cursor intCursor = HGWUserInfoAct.getInternetInfo(user_id);
										Map tmap = intCursor.getNext();
										if (null != tmap)
										{
							%>
							<tr bgcolor="#FFFFFF">
								<td class=column></td>
								<td colspan="3" align="center">
									<TABLE border=0 cellspacing=1 cellpadding=2 width="95%"
										bgcolor=#000000>
										<tr bgcolor="#FFFFFF">
											<td><strong>上网方式</strong></td>
											<td><strong>用户账号</strong></td>
											<td><strong>密码</strong></td>
											<td><strong>状态</strong></td>

										</tr>
										<%
												while (null != tmap)
												{
													String parm_type = "";
													if ("1".equals(tmap.get("parm_type_id")))
													{
														parm_type = "桥接";
													}
													else if ("2".equals(tmap.get("parm_type_id")))
													{
														parm_type = "路由";
													}
													else
													{
														parm_type = "未知";
													}
													String parm_stat = "";
													if ("1".equals(tmap.get("parm_stat")))
													{
														parm_stat = "开通";
													}
													else
													{
														parm_stat = "未开通";
													}
										%>
										<tr bgcolor="#FFFFFF">
											<td><%=parm_type%></td>
											<td><%=tmap.get("username")%></td>
											<td><%=tmap.get("passwd")%></td>
											<td><%=parm_stat%></td>

										</tr>
										<%
												tmap = intCursor.getNext();
												}
										%>
									</table>
								</td>
							</tr>


							<%
									}
									}
									if ("14".equals(map.get("serv_type_id")))
									{
										//Voip业务
										Cursor voipCursor = HGWUserInfoAct.getVoipInfo(user_id);
										Map vmap = voipCursor.getNext();
										if (null != vmap)
										{
							%>
							<tr bgcolor="#FFFFFF">
								<td class=column></td>
								<td colspan="3" align="center">
									<TABLE border=0 cellspacing=1 cellpadding=2 width="95%"
										bgcolor=#000000>
										<tr bgcolor="#FFFFFF">
											<td><strong>线路</strong></td>
											<td><strong>用户账号</strong></td>
											<td><strong>电话号码</strong></td>
										</tr>
										<%
												while (null != vmap)
												{
										%>
										<tr bgcolor="#FFFFFF">
											<td><%=vmap.get("line_id")%></td>
											<td><%=vmap.get("voip_username")%></td>
											<td><%=vmap.get("voip_phone")%></td>
										</tr>
										<%
												vmap = voipCursor.getNext();
												}
										%>
									</table>
								</td>
							</tr>


							<%
									}
									}
							%>
							<%
									map = businessCursor.getNext();
									i++;
								}
							%>

							<TR>
								<TD colspan="4" align="right" class=foot><INPUT
									TYPE="button" value=" 关 闭 " class="btn" style="cursor:pointer" 
									onclick="javascript:window.close()"> &nbsp;&nbsp;</TD>
							</TR>
						</TABLE>
					</TD>
				</TR>
			</TABLE>
		</TD>
	</TR>
</TABLE>
<script>
	function testOnline(obj, device_id) {
		if (device_id == "") {
			alert("卡没有绑定设备！");
			return;
		}
		obj.disabled = true;
		var span = obj.parentNode.firstChild;
		span.innerHTML = "<font color=blue>正在获取在线状态...</font>"

		var url = "<s:url value='/itms/resource/cardInfo!getCardStatus.action'/>";
		$.post(url, {
			device_id : device_id
		}, function(ajax) {
			//alert(ajax);
			// 采集成功
			if (parseInt(ajax.split("|")[0]) == 1) {
				if (parseInt(ajax.split("|")[1]) == 1) {
					span.innerHTML = "<font color=green>在线(实时)</font>"
				} else {
					span.innerHTML = "<font color=red>下线(实时)</font>"
				}
			}
			// 采集失败
			else {
				alert(ajax.split("|")[1]);
			}
			obj.disabled = false;
		});
	}

	
</script>