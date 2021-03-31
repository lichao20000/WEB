<%@ include file="../timelater.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>
<%@ page import="com.linkage.litms.flux.GeneralFluxPerf,
				 com.linkage.litms.LipossGlobals,
				 com.linkage.litms.flux.*,
				 com.linkage.litms.common.database.Cursor,
				 com.linkage.litms.report.FluxUnit,
				 java.util.*,
				 java.sql.*,
				 java.lang.*,
                 com.linkage.litms.common.util.*"%>
<%
request.setCharacterEncoding("GBK");
//获取报表的类型，1为日，2为周，3为月，4，为年，5为阶段
String str_SearchType=request.getParameter("SearchType");
//获取查询时间
String str_day = request.getParameter("day");

//获取查询时间的时间秒
String str_day_start = request.getParameter("hidday");

//获取设备ID
String str_device_id = request.getParameter("dev_id");

//获取类型
String [] arr_kind   = request.getParameterValues("kind");
//获取端口
String [] arr_port   = request.getParameterValues("port");

long str_start=0;
long str_end=0;

str_start=Long.parseLong(str_day_start);

int SearchType=Integer.parseInt(str_SearchType);

String title = "";
switch(SearchType){
	case 1://日			
		str_end	= str_start + 24*60*60-1;
		title = "端口流量日统计报表";
		break;
	case 2://周
        str_end= str_start + 7*24*60*60-1;
		title = " 端口流量周统计报表";
		break;
	case 3://月
		String strmonth = request.getParameter("day")+"-1";
		java.sql.Date date = java.sql.Date.valueOf(strmonth);
		str_start = date.getTime()/1000;

		java.util.Date mydate = new java.util.Date((long)(str_start)*1000);
		int year  = mydate.getYear();
		int month = mydate.getMonth();
		String str_month = new Integer(year+1900).toString() +"-"+ new Integer(month+1).toString();
		mydate = new java.util.Date(year,month+1,1);
		str_end = (long)(mydate.getTime()/1000);
		title = "端口流量月统计报表";
		break;
	case 4://年
		String stryear = request.getParameter("day")+"-01-01";
		java.sql.Date dateyear = java.sql.Date.valueOf(stryear);
		str_start = dateyear.getTime()/1000;
		java.util.Date mydateyear = new java.util.Date(str_start*1000);
		int year1  = mydateyear.getYear();
		int str_year = year1+1900;
		mydateyear = new java.util.Date(year1+1,0,1);
		str_end = (long)(mydateyear.getTime()/1000);
		title = "端口流量年统计报表";
		break;
}
GeneralFluxPerf flux = new GeneralFluxPerf(str_start,str_end,SearchType);
//调用接口，并返回cursor
Cursor cursor = flux.getGeneralTxtData(request,0,0);

//获取查询记录
Map fields = cursor.getNext();

int colnum = arr_kind.length + 2;
//device_id与IP对应关系
DbUserRes  dbUserRes = (DbUserRes) session.getAttribute("curUser");
List gather_id = dbUserRes.getUserProcesses();
Map ipMap = com.linkage.litms.common.util.CommonMap.getDeviceIPMap(gather_id);

//获取配置文件中的单位
/*
String FluxBase=LipossGlobals.getLipossProperty("report.FluxBase");
String UnitName=LipossGlobals.getLipossProperty("report.UnitName");
String confirm=LipossGlobals.getLipossProperty("report.confirm");
String name="M";
double   l_unit=1000*1000;
double   unit=1000*1000;
if(UnitName!=null)
{
	l_unit=Double.parseDouble(FluxBase)*Double.parseDouble(FluxBase)*Double.parseDouble(FluxBase);
	unit =unit*Double.parseDouble(confirm);
	name=UnitName;
}
*/
FluxUnit fu=FluxUnit.getFluxUnit(session);
String name=fu.getUnitName();
double l_unit=0.0;
double unit=fu.getUnit()*fu.getUnit()*fu.getConfirm();
if(name.equals("M"))
{
	l_unit=fu.getFluxBase()*fu.getFluxBase();	
}
else
{
	l_unit=fu.getFluxBase()*fu.getFluxBase()*fu.getFluxBase();	

}
//映射中文描述字符
Map map = FluxMap.getFluxMap(name);
%>
<SCRIPT LANGUAGE="JavaScript">
<!--
function setDivStyle(){
	var maxScreenX = window.screen.width;
	var maxScreenY = window.screen.height;

	var w = maxScreenX * 0.9;
	var h = maxScreenY * 0.9 - 350; 
	idLayer.style.width = w;
	idLayer.style.height = h;
}
window.onload=setDivStyle;
//-->
</SCRIPT>
<SCRIPT LANGUAGE="JavaScript" SRC="../Js/CheckForm.js"></SCRIPT>
<%@ include file="../head.jsp"%>
<%@ include file="../toolbar.jsp"%>
<FORM method=POST action="" NAME="frm1">
<DIV id="idLayer" style="overflow:auto;width:'800px';height:'400px'">
  <TABLE  width="100%" border=0 align="center" cellpadding=0 cellspacing=0 bgcolor="#999999">
    <tr>
					<td>
						<Table width="100%" border=0 cellpadding=2 cellspacing=1 id="topTable">
							<TR>
								<td bgcolor="#FFFFFF" class="blue_td">
									请选择图形显示的流量类型
								</td>
								<td width="88%"  nowrap bgcolor=#FFFFFF>
									<table width="100%" height="30"  border="0" cellpadding="0" cellspacing="0" class="blue_gargtd">
									  <tr> 
										<td bgcolor=#FFFFFF  nowrap>										  
										<input name="kind_radio" type="radio"   value="ifinoctetsbps" checked>
										  流入速率(bps)&nbsp; </td>
										<td bgcolor=#FFFFFF  nowrap> <input name="kind_radio" type="radio"   value="ifindiscardspps">
										  流入丢包率&nbsp;</td>
										<td bgcolor=#FFFFFF  nowrap> <input name="kind_radio" type="radio"    value="ifinerrors">
										  流入错误包数&nbsp; </td>
										<td bgcolor=#FFFFFF  nowrap> <input name="kind_radio" type="radio"    value="ifinoctetsbpsmax">
										  流入峰值</td>
										<td bgcolor=#FFFFFF  nowrap> <input name="kind_radio" type="radio"   value="ifinucastpktspps">
										  每秒流入单播包数 </td>
										<td bgcolor=#FFFFFF  nowrap> <input name="kind_radio" type="radio"   value="u1">
										  流入带宽利用率</td>
									  </tr>
									  <tr> 
										<td bgcolor=#FFFFFF  nowrap><input name="kind_radio" type="radio"   value="ifoutoctetsbps">
										  流出速率(bps)&nbsp;</td>
										<td bgcolor=#FFFFFF  nowrap><input name="kind_radio" type="radio"    value="ifoutdiscardspps">
										  流出丢包率&nbsp;</td>
										<td bgcolor=#FFFFFF  nowrap><input name="kind_radio" type="radio"    value="ifouterrors">
										  流出错误包数</td>
										<td bgcolor=#FFFFFF  nowrap><input name="kind_radio" type="radio"    value="ifoutoctetsbpsmax">
										  流出峰值</td>
										<td bgcolor=#FFFFFF  nowrap><input name="kind_radio" type="radio"   value="ifinnucastpktspps">
										  每秒流入非单播包数</td>
										<td bgcolor=#FFFFFF  nowrap><input type="radio" name="kind_radio" value="u2" >
										  流出带宽利用率</td>
									  </tr>
									  <tr> 
										<td bgcolor=#FFFFFF  nowrap><input name="kind_radio" type="radio"   value="ifinerrorspps">
										  流入错包率</td>
										<td bgcolor=#FFFFFF  nowrap><input name="kind_radio" type="radio"    value="ifinoctets">
										  流入字节数</td>
										<td bgcolor=#FFFFFF  nowrap><input name="kind_radio" type="radio"    value="ifindiscards">
										  流入丢弃包数&nbsp;</td>
										<td bgcolor=#FFFFFF  nowrap><input name="kind_radio" type="radio"    value="u3">
										  最大流入利用率</td>
										<td bgcolor=#FFFFFF  nowrap><input name="kind_radio" type="radio"    value="ifoutucastpktspps">
										  每秒流出单播包数&nbsp;</td>
										<td bgcolor=#FFFFFF  nowrap>&nbsp;</td>
									  </tr>
									  <tr> 
										<td bgcolor=#FFFFFF  nowrap><input name="kind_radio" type="radio"    value="ifouterrorspps">
										  流出错包率</td>
										<td bgcolor=#FFFFFF  nowrap><input name="kind_radio" type="radio"  value="ifoutoctets">
										  流出字节数&nbsp;</td>
										<td bgcolor=#FFFFFF  nowrap><input name="kind_radio" type="radio"   value="ifoutdiscards">
										  流出丢弃包数&nbsp;</td>
										<td bgcolor=#FFFFFF  nowrap><input name="kind_radio" type="radio"    value="u4">
										  最大流出利用率</td>
										<td bgcolor=#FFFFFF  nowrap><input name="kind_radio" type="radio"    value="ifoutnucastpktspps">
										  每秒流出非单播包数</td>
										<td bgcolor=#FFFFFF  nowrap>&nbsp;</td>
									  </tr>
									  <td bgcolor=#FFFFFF  nowrap><input name="kind_radio" type="radio"    value="ifinunknownprotospps">
										  每秒流入未知协议包数</td>
										<td bgcolor=#FFFFFF  nowrap><input name="kind_radio" type="radio"    value="ifoutqlenpps">
										  每秒流出队列大小</td>
										<td bgcolor=#FFFFFF  nowrap>&nbsp;</td>
										<td bgcolor=#FFFFFF  nowrap>&nbsp;</td>
									  <tr>
									  </tr>
									</table>
								</td>
							</tr>
							<TR bgcolor="#FFFFFF"  onmouseout="className='blue_trOut'"> 
								<TD colspan=2 align=right class="blue_foot"> <div align="right"> 
									<input type="hidden" name="queryTime" >
									<input type="hidden" name="queryMonth">
					  
									<INPUT TYPE="button" name="cmdOK" value=" 图形显示"  class="jianbian" onclick="javascript:viewGraphic();">
									  &nbsp;&nbsp;&nbsp;&nbsp; </div></TD>
							</TR>
					  </table>
		</td>
	  </tr>
	
	<TR>
      <TD bgcolor="#FFFFFF" colspan="<%=colnum%>" ><table width="100%" height="30"  border="0" cellpadding="0" cellspacing="0" class="blue_gargtd">
          <tr> 
            <td width="162" align="center" class="title_bigwhite"> 统计信息</td>
            <td align="left"><span class="text">&nbsp;&nbsp;<img src="../images/attention_2.gif" width="15" height="12"> 
              以下是设备<font color=red><%=ipMap.get(str_device_id)%></font> <%=str_day%> 端口流量统计报表 
              </span></td>
          </tr>
        </table></TD>
    </TR>
    <TR> 
      <TD> <TABLE width="100%" border=0 cellpadding=2 cellspacing=1 id="topTable">
          <TR bgcolor="#FFFFFF"> 
            <td bgcolor="#FFFFFF" class="blue_td">端 口</td>
            <td bgcolor="#FFFFFF" class="blue_td">端口描述</td>
            <%
			String tmp;
			for(int i=0; i<colnum-2; i++){
				out.println("<td class=blue_td  nowrap>" + map.get(arr_kind[i]) + "</td>");
			}
			map.clear();
		  %>
          </TR>
          <%
			if(fields == null){
		  %>
          <TR bgcolor="#FFFFFF"> 
            <TD height="22" colspan=<%=colnum%>  bgcolor="#FFFFFF"> 设备&nbsp;<font color=red><%=ipMap.get(str_device_id)%></font>&nbsp;<%=str_day%>&nbsp;查询数据不存在 
              <input type="hidden" id="url" name="url" value="./template/visualman_sys_day_avg.jsp">	
            </TD>
         </TR>
          <%
		    }
			else{
				String show_kind;
				double dbluse = 0.0D;
				int j = 0;				
				while(fields != null){
					out.println("<TR><TD bgcolor=#FFFFFF nowrap>"+ fields.get("ifindex") +"</TD>");
					//out.println("<TD class=column>"+ arr_ifnamedefined[j] +"</TD>");
					out.println("<TD bgcolor=#FFFFFF  >"+ fields.get("ifdescr")+"/"+fields.get("ifname")+"/"+fields.get("ifnamedefined") +"</TD>");
					tmp = "1";
					String ifnamedifined=(String)fields.get("ifnamedefined");
					if(ifnamedifined!=null)
					{
						ifnamedifined=ifnamedifined.replaceAll("#","%23");
					}
					for(int i=0; i<colnum-2; i++){
						show_kind = arr_kind[i];
						//show_kind = show_kind.toUpperCase();
						if(Double.parseDouble((String)fields.get("if_real_speed")) != 0){
							tmp = (String)fields.get("if_real_speed");
						}
						if(show_kind.equals("u1")){
							dbluse = Double.parseDouble((String)fields.get("ifinoctetsbps"))/Double.parseDouble(tmp);
							out.println("<TD bgcolor=#FFFFFF  nowrap>"+ StringUtils.formatString((new Double(dbluse*100)).toString(),4) +"</TD>");
						}
						else if(show_kind.equals("u2")){
							dbluse = Double.parseDouble((String)fields.get("ifoutoctetsbps"))/Double.parseDouble(tmp);
							out.println("<TD bgcolor=#FFFFFF  nowrap>"+ StringUtils.formatString((new Double(dbluse*100)).toString(),4) +"</TD>");
						}
						else if(show_kind.equals("u3")){
							dbluse = Double.parseDouble((String)fields.get("ifinoctetsbpsmax"))/Double.parseDouble(tmp);							
							out.println("<TD  bgcolor=#FFFFFF  nowrap>"+ StringUtils.formatString((new Double(dbluse*100)).toString(),4) +"</TD>");
						}
						else if(show_kind.equals("u4")){
							dbluse = Double.parseDouble((String)fields.get("ifoutoctetsbpsmax"))/Double.parseDouble(tmp);							
							out.println("<TD bgcolor=#FFFFFF  nowrap>"+ StringUtils.formatString((new Double(dbluse*100)).toString(),4) +"</TD>");
						}
						else{
							String value=(String)fields.get(show_kind);
							//1代表M;2代表G
							String unitValue="0";
							if(value.length()>12)
							{
								unitValue="3";
							}
							else if(value.length()>9)
							{
								unitValue="2";
							}
							else if(value.length()>6)
							{
								unitValue="1";
							}
							if(show_kind.equals("ifinoctetsbps") || show_kind.equals("ifinoctets")|| show_kind.equals("ifoutoctetsbps")|| show_kind.equals("ifoutoctets") || show_kind.equals("ifinoctetsbpsmax") || show_kind.equals("ifoutoctetsbpsmax"))
							{
								out.println("<TD bgcolor=#FFFFFF  nowrap><A HREF='portDetail_hour.jsp?dev_id="+ str_device_id.trim() +"&port="+ ((String)fields.get("ifindex")).trim() +"&kind="+ show_kind +"&time="+ str_day_start +"&desc="+ifnamedifined+"&unit="+unitValue+"' onclick='return showopen()' target='_blank'>"+  StringUtils.formatString(Double.valueOf((String)fields.get(show_kind)).doubleValue()/l_unit,6) +"</A></TD>");					
							}
							else
								out.println("<TD  bgcolor=#FFFFFF   nowrap><A HREF='portDetail_hour.jsp?dev_id="+ str_device_id.trim() +"&port="+ ((String)fields.get("ifindex")).trim() +"&kind="+ show_kind +"&time="+ str_day_start +"&desc="+ifnamedifined+"&unit=-1' onclick='return showopen()' target='_blank'>"+ (String)fields.get(show_kind) +"</A></TD>");
						}
					}					
					j++;
					out.println("</TR>");
					fields = cursor.getNext();
				} 
				
			}
				map.clear();

		  %>
        </TABLE>
		</TD>
    </TR>
  </TABLE>
  </DIV>
</FORM>

<SCRIPT LANGUAGE="JavaScript" id="idChildFun">
<!--
function showit(){
	var page = "../Report/frame/treeview/addNodeTemplate.jsp?tt="+ new Date().getTime();
	
	var height = 200;
	var width = 400;
	var w = window.open(page,"ss","width="+width+",height="+height+",resizable=yes,scrollbars=no,toolbar=no");
	w.moveTo((screen.width-width)/2,(screen.height-height)/2);	
}

function showopen(){
	page = event.srcElement.href;
	window.open(page,"","left=20,top=20,width=500,height=400,resizable=yes,scrollbars=yes,status=yes");
	return false;
}
//-->
</SCRIPT>
<SCRIPT LANGUAGE="JavaScript">
<!--
if(typeof(parent.idLayerView1) == "object"){
	//alert(idLayer.innerHTML);
	parent.idLayerView1.innerHTML = idLayer.innerHTML;
	//parent.idParentFun.text = idChildFun.innerHTML;
}
//-->
</SCRIPT>