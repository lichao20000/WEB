<%@ include file="../timelater.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>
<%@ page import="com.linkage.litms.webtopo.PmeeDataList"%>
<%@ page import="com.linkage.litms.performance.CommonSearch"%>
<%@ page import="com.linkage.litms.system.dbimpl.DbUserRes"%>
<%@ page import="com.linkage.litms.common.util.CommonMap"%>
<%@ page import="com.linkage.litms.common.util.StringUtils"%>
<%@ page import="com.linkage.litms.performance.GeneralNetPerf"%>
<%@ page import="com.linkage.litms.common.chart.TimeSeriesChart" %>
<%@ page import="java.io.PrintWriter" %>
<%
request.setCharacterEncoding("GBK");
String strSQL,tmpSQL;
Cursor cursor;
Map fields;
String year,month;
//获取页面传递的参数
String start = request.getParameter("hidstart");
String expressionid = request.getParameter("expressionid");
String id    = request.getParameter("ip");
PmeeDataList pmeeData = new PmeeDataList();
String[] arr_iid = pmeeData.getPM_instance_ID(expressionid,id);
String sortcolumn = "avgvalue";

String strday = request.getParameter("start");

String stype = request.getParameter("SearchType");


int type = Integer.parseInt(stype);

//将变量保存到session
Map req_data=new HashMap();
req_data.put("start",start);
req_data.put("expressionid",expressionid);
req_data.put("id",id);
req_data.put("arr_iid",arr_iid);
req_data.put("sortcolumn",sortcolumn);
req_data.put("strday",strday);
req_data.put("type",stype);
session.setAttribute("req_data",req_data);

//格式化实例索引
String str_iid = null;
for(int i=0;i<arr_iid.length;i++){
	if(str_iid == null) str_iid = "'" + arr_iid[i] +"'";
	else str_iid += ",'"+ arr_iid[i] + "'";
}

//获取表达式名字
Map expressionMap = CommonSearch.getExpressionMap(expressionid);

//获取设备名称
Map deviceMap = CommonSearch.getDeviceMap(id);

//获取实例索引及相关信息
Map indexMap = CommonSearch.getIndexMap(str_iid);


DbUserRes  dbUserRes = (DbUserRes) session.getAttribute("curUser");
List gather_id = dbUserRes.getUserProcesses();

Map ipMap = CommonMap.getDeviceIPMap(gather_id);

//计算查询时间
int iStart = Integer.parseInt(start);
int iEnd   = iStart + 3600*24;

//根据时间格式化小时报表表名
String[] arr   = StringUtils.secondToDateStr(iStart);
//year = arr[0];
//month = arr[1];
//String tblname = "pm_hour_stats_"+year+"_"+month;

//获取表达式名称和单位
String ex_name,ex_unit;
String s = (String)expressionMap.get(expressionid);
arr = s.split(",");
ex_name = arr[0];
ex_unit = arr[1];
//格式化查询SQL
//strSQL = "select * from "+ tblname +" where gathertime>="+ iStart +" and gathertime<"+ iEnd
//			   +" and id in("+ str_iid +") order by "+ sortcolumn +" desc";
////tmpSQL = "select * from "+ tblname +" where gathertime>=$start$ and gathertime<$end$ and id in("+ str_iid +") order by "+ sortcolumn +" desc";
//
//out.println(strSQL);
//执行SQL，获取数据
GeneralNetPerf netPM = new GeneralNetPerf(iStart, iEnd, type, arr_iid);
cursor = netPM.getGeneralTxtData();
fields = cursor.getNext();
%>
<%@ include file="../head.jsp"%>
<TABLE border=0 cellspacing=0 cellpadding=0 width="100%">
<TR><TD HEIGHT=20>&nbsp;</TD></TR>
<TR><TD align=center>
	<DIV id="idLayer" style="overflow:auto;width:'800px';height:'400px'">
	<TABLE width="100%" height="30"  border="0" cellpadding="0" cellspacing="0" class="green_gargtd">
	<TR>
	  <TD width="162" align="center" class="title_bigwhite">查询结果</TD>
	  <TD colspan=""> 
		&nbsp;&nbsp;<IMG height=12 src="../images/attention_2.gif" width=15>&nbsp;设备：<%out.println(deviceMap.get(id) + "/" + ipMap.get(id));%>
	 </TD>
	</TABLE>
	<TABLE width="100%" border=0 cellspacing=0 cellpadding=0 align="center">
	  <TR>
	  	<TD bgcolor=#999999>
	    	  <TABLE border=0 cellspacing=1 cellpadding=2 width="100%">
                <TR> 
                  <TH width='15%'>索引</TH>
                  <TH width='20%'>最大值(<%=ex_unit%>)</TH>
                  <TH width='20%'>最小值(<%=ex_unit%>)</TH>
                  <TH width='20%'>平均值(<%=ex_unit%>)</TH>
				  <TH width='25%'>&nbsp;</TH>
                </TR>
				<%
				//打印数据
				if(fields!=null){
					String id2,tmpStr;
					while(fields != null){
						id2 = (String)fields.get("id");
						tmpStr = (String)indexMap.get(id2);
						arr = tmpStr.split(",");

						out.println("<TR bgcolor=#ffffff align=right>");
						out.println("<TD class=column>"+ arr[2]+"/"+arr[3] +"</TD>");
						out.println("<TD class=column>"+ fields.get("maxvalue") +"</TD>");
						out.println("<TD class=column>"+ fields.get("minvalue") +"</TD>");
						out.println("<TD class=column>"+ fields.get("avgvalue") +"</TD>");
						out.println("<TD class=column>&nbsp;</TD>");
						out.println("</TR>");

						fields = cursor.getNext();
					}
					//显示每个采集点的值
					out.println("<TR>");
					out.println("<TH>索引</TH>");
					out.println("<TH>最大值("+ex_unit+")</TH>");
					out.println("<TH>最小值("+ex_unit+")</TH>");
					out.println("<TH>平均值("+ex_unit+")</TH>");
					out.println("<TH>时间</TH>");
					out.println("</TR>");
					
					GeneralNetPerf netPmMore = new GeneralNetPerf(iStart, iEnd, 3, arr_iid);
					cursor = netPmMore.getGeneralChartData();
					fields = cursor.getNext();
					
					if(fields == null){
						out.println("<TR bgcolor=#ffffff><TD colspan=5>小时报表数据没有入库</TD></TR>");
					}
					else{
						String old_id="",cur_id;
						String gathertime;
						String tmp;
						long lms;
						//一个实例一个Cursor
						
						Cursor[] cursors = new Cursor[arr_iid.length];
						//实例描述
						String[] rowKeys = new String[arr_iid.length];

						//实例Cursor数组下标
						int ln = -1;
						while(fields != null&&ln<arr_iid.length-1){
							id = (String)fields.get("id");
							tmpStr = (String)indexMap.get(id);
							arr = tmpStr.split(",");

							cur_id = arr[2];
							gathertime  = (String)fields.get("gathertime");
							tmp			= arr[3];
							if(!old_id.equals(cur_id)){
								ln++;	//实例不同，实例Cursor数组下移
								rowKeys[ln] = tmp.equals("")?arr[2]:tmp;
								cursors[ln] = new Cursor();
							}
							cursors[ln].add(fields);

							lms = Long.parseLong(gathertime)*1000;



							out.println("<TR align=right>");
							out.println("<TD class=column>"+arr[2]+"/"+arr[3]+"</TD>");
							out.println("<TD class=column>"+fields.get("maxvalue")+"</TD>");
							out.println("<TD class=column>"+fields.get("minvalue")+"</TD>");
							out.println("<TD class=column>"+fields.get("avgvalue")+"</TD>");
							out.println("<TD class=column>"+ StringUtils.getDateTimeStr("yyyy-MM-dd HH:mm:ss",lms)+"</TD>");
							out.println("</TR>");
							
							//保存这次的实例
							old_id = cur_id;
							fields = cursor.getNext();
						}
						//画曲线图	
						String title = " 性能天统计报表";
						TimeSeriesChart chart = new TimeSeriesChart();
						chart.setTimeStep(5);
						chart.setChartBaseinfo(title,"时间",ex_name+": 单位("+ex_unit+")","gathertime",sortcolumn,3);
												chart.setChartDataset(cursors,rowKeys,null);

						String filename = chart.createChart(session, new PrintWriter(out));

						String graphURL = request.getContextPath() + "/servlet/DisplayChart?filename=" + filename;
						out.println("<TR bgcolor=#ffffff><TD colspan=5 align=center><img src='"+ graphURL +"' border=0 usemap='#"+filename+"'></TD></TR>");
					}
				}
				else{
					out.println("<TR bgcolor=#ffffff>");
					out.println("<TD class=column  colspan=5 align=center>查询无数据</TD>");
					out.println("</TR>");
				}
				%>
              </TABLE>
              <input type="hidden" name="url" value="">
	  </TD></TR>
	</TABLE>	
	</DIV>
</TD></TR>
<TR><TD HEIGHT=20>&nbsp;</TD></TR>
</TABLE>
<SCRIPT LANGUAGE="JavaScript" id="idChildFun">
<!--
function subscribe_to(){
	//格式化参数
	var param = "?";
	param += "expressionid=<%=expressionid%>&amp;iid=<%=str_iid%>&amp;ip=<%=id%>";
	document.all("url").value = "template/netpm_hour_report.jsp"+ param;

	var page = "../Report/frame/treeview/addNodeTemplate.jsp?tt="+ new Date().getTime();
	var height = 200;
	var width = 400;
	var left = (screen.width-width)/2;
	var top  = (screen.height-height)/2;
	var w = window.open(page,"ss","left="+left+",top="+top+",width="+width+",height="+height+",resizable=yes,scrollbars=no,toolbar=no");
}

function toExcel(){
	
}  
//-->
</SCRIPT>
<%@ include file="../foot.jsp"%>
<SCRIPT LANGUAGE="JavaScript">
<!--
if(typeof(parent.idLayerView) == "object"){
	parent.idLayerView.innerHTML = idLayer.innerHTML;
//	parent.idParentFun.text = idChildFun.innerHTML;
}
//-->
</SCRIPT>