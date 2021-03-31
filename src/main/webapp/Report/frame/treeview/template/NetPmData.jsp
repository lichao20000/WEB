
<%@ page contentType="text/html;charset=GBK"%>
<%@ page import="com.linkage.litms.common.database.*"%>
<%@ page import="java.util.HashMap"%>
<%@ page import="com.linkage.litms.common.util.DateTimeUtil"%>
<%@ page import="com.linkage.litms.common.util.StringUtils"%>
<%@ page import="com.linkage.litms.performance.GeneralNetPerf"%>
<%@ page import="com.linkage.litms.common.util.DateTimeUtil"%>
<%@ page import="java.util.Map"%>
<%@ page import="java.util.ArrayList" %>
<%@ page import="com.linkage.commons.db.DBUtil"%>
<%@ page import="com.linkage.module.gwms.util.StringUtil"%>

<%
	request.setCharacterEncoding("GBK");
	Cursor cursor;
	Map fields=null;
	String type_tmp=request.getParameter("type");
	String start_tmp=request.getParameter("start");
	String hour=request.getParameter("hour");
	String chk_val;
	DateTimeUtil dt=new DateTimeUtil(start_tmp);
	// 通过ajax传递中文，需要将字符集合转码的。
	try
	{
		chk_val=java.net.URLDecoder.decode(request.getParameter("chk_val"),"UTF-8");
	} catch (Exception e)
	{
		chk_val=request.getParameter("chk_val");
	}
	String device_id=request.getParameter("device_id");
	String vendor_id=request.getParameter("vendor_id");
	int type = 0;
	if(type_tmp != null && !"".equals(type_tmp.trim()) && !"null".equals(type_tmp)){
		type = Integer.parseInt(type_tmp);
	}
	long start=0;
	long end=0;
	String tab_name="";
	switch(type){
		case 1:
			start=dt.getLongTime();
			if(hour != null && !"".equals(hour.trim())){
				start += (Integer.parseInt(hour))*3600;
			}
			end=start+3600;
			tab_name="pm_hour_stats_"+dt.getYear()+"_"+dt.getMonth();
			break;
		case 2:
			start=dt.getLongTime();
			end=start+3600 * 24;
			tab_name="pm_day_stats_"+dt.getYear();
			break;
		case 3:
			start_tmp=dt.getFirstDayOfWeek("US");
			dt=new DateTimeUtil(start_tmp);
			start=dt.getLongTime();
			end=start+3600 * 24*7;
			tab_name="pm_week_stats_"+dt.getYear();
			break;
		case 4:
			String[] tmp=start_tmp.split("-");
			dt=new DateTimeUtil(tmp[0]+"-"+tmp[1]+"-1");
			start=dt.getLongTime();
			end=new DateTimeUtil(dt.getLastDayOfMonth()).getLongTime();
			tab_name="pm_month_stats_"+dt.getYear();
			break;
	}
	String[] tmp=chk_val.split(",");
	int n=tmp.length;
	String[] exp_id=new String[n];
	String[] exp_desc=new String[n];
	for(int i=0;i<n;i++){
		String[] temp = tmp[i].split("-/-");
		if(temp != null && temp.length > 0){
			exp_id[i]=temp[0];
		}
		if(temp.length > 1){
			exp_desc[i]=temp[1];
		}
	}
	tmp=device_id.split(",");
	String dev_id="";
	int num=tmp.length;
	for(int i=0;i<num;i++){
		dev_id+=",'"+tmp[i]+"'";
	}
	dev_id=dev_id.substring(1);
	String sql="select device_id, device_name,loopback_ip,device_serialnumber from tab_gw_device where device_id in("+dev_id+")";
	cursor=DataSetBean.getCursor(sql);
	fields=cursor.getNext();
	sql="select count(*) from sysobjects where name ='"+tab_name+"'";
	if (DBUtil.GetDB() == 1)
	{// oracle
		sql = "select count(*) from user_tables where table_name='"
				+ StringUtil.getUpperCase(tab_name) + "'";
	}
	else if (DBUtil.GetDB() == 2)
	{// sybase
		sql = "select count(*) from sysobjects where name ='"+tab_name+"'";
	}
	Cursor cu=DataSetBean.getCursor(sql);
	HashMap map=new HashMap();
	if(cu.getRecordSize()==0){
		fields=null;
	}else{
		sql="select a.maxvalue,a.minvalue,a.avgvalue,b.expressionid,b.device_id,b.descr from "
		+tab_name+" a,pm_map_instance b ,pm_map c where a.id=b.id and a.gathertime>="+start
		+" and a.gathertime<="+end+" and c.expressionid=b.expressionid and c.isok=1 and c.device_id in("+dev_id+") order by b.indexid desc";
		Cursor cursor1=DataSetBean.getCursor(sql);
		Map map_tmp=cursor1.getNext();
		while(map_tmp!=null){
			map.put(map_tmp.get("device_id")+"-"+map_tmp.get("expressionid"),map_tmp);
			map_tmp=cursor1.getNext();
		}
		if(map==null || map.isEmpty()) fields=null;
	}
%>
<div id=idData>
<div width="100%" align="right"><INPUT TYPE="button" onclick="initialize('outData',0,1)" value=" 导出数据报表 " class="jianbian"></div>
<TABLE width="100%" border=0 cellspacing=0 cellpadding=0>
	<TR>
		<TD bgcolor=#999999 width="100%">
			<TABLE border=0 cellspacing=1 cellpadding=2 width="100%" id="outData">
				<TR>					
					<TH nowrap>
						设备名称
					</TH>
					<TH nowrap>
						设备IP
					</TH>					
					<TH nowrap>
						设备序列号
					</TH>
				<%
					for(int i=0;i<n;i++){
						out.println("<th nowrap colspan=3>"+exp_desc[i]+"</th>");
					}
				%>
				</TR>
				<tr bgcolor=#ffffff>
					<td>&nbsp;</td>
					<td>&nbsp;</td>
					<td>&nbsp;</td>
				<%
					for(int i=0;i<n;i++){
						out.println("<td>最大值</td><td>平均值</td><td>最小值</td>");
					}
					
				%>
				</tr>
				
				<%
					if(fields!=null){
						Map tmpmap=null;
						String id_tmp;
						while(fields!=null){
							out.println("<tr bgcolor=#ffffff>");
							out.println("<td>"+fields.get("device_name")+"</td>");
							out.println("<td>"+fields.get("loopback_ip")+"</td>");
							out.println("<td>"+fields.get("device_serialnumber")+"</td>");
							
							for(int i=0;i<n;i++){
								if(map!=null && !map.isEmpty()){
									tmpmap=(Map)map.get(fields.get("device_id")+"-"+exp_id[i]);
								}
								if(tmpmap!=null && !tmpmap.isEmpty()){
									out.println("<td>"+tmpmap.get("maxvalue")+"</td>");
									out.println("<td>"+tmpmap.get("avgvalue")+"</td>");
									out.println("<td>"+tmpmap.get("minvalue")+"</td>");
								}else{
									out.println("<td>-</td>");
									out.println("<td>-</td>");
									out.println("<td>-</td>");
								}
								 
								
							}
							out.println("</tr>");
							fields=cursor.getNext();
						}
					}else{
						out.println("<TR bgcolor=#ffffff>");
						out.println("<TD class=column  colspan="+(n*3+3)+" align=center  width=\"100%\">查询无数据</TD>");
						out.println("</TR>");
					}
						
					
				%>
			</TABLE>
			
		</TD>
	</TR>
</TABLE>
</div>

<SCRIPT LANGUAGE="JavaScript">
<!--
parent.document.all("idLayerView").innerHTML = document.getElementById("idData").innerHTML;
//-->
</SCRIPT>
