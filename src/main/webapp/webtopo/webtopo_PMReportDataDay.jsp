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
//��ȡҳ�洫�ݵĲ���
String start = request.getParameter("hidstart");
String expressionid = request.getParameter("expressionid");
String id    = request.getParameter("ip");
PmeeDataList pmeeData = new PmeeDataList();
String[] arr_iid = pmeeData.getPM_instance_ID(expressionid,id);
String sortcolumn = "avgvalue";

String strday = request.getParameter("start");

String stype = request.getParameter("SearchType");


int type = Integer.parseInt(stype);

//���������浽session
Map req_data=new HashMap();
req_data.put("start",start);
req_data.put("expressionid",expressionid);
req_data.put("id",id);
req_data.put("arr_iid",arr_iid);
req_data.put("sortcolumn",sortcolumn);
req_data.put("strday",strday);
req_data.put("type",stype);
session.setAttribute("req_data",req_data);

//��ʽ��ʵ������
String str_iid = null;
for(int i=0;i<arr_iid.length;i++){
	if(str_iid == null) str_iid = "'" + arr_iid[i] +"'";
	else str_iid += ",'"+ arr_iid[i] + "'";
}

//��ȡ���ʽ����
Map expressionMap = CommonSearch.getExpressionMap(expressionid);

//��ȡ�豸����
Map deviceMap = CommonSearch.getDeviceMap(id);

//��ȡʵ�������������Ϣ
Map indexMap = CommonSearch.getIndexMap(str_iid);


DbUserRes  dbUserRes = (DbUserRes) session.getAttribute("curUser");
List gather_id = dbUserRes.getUserProcesses();

Map ipMap = CommonMap.getDeviceIPMap(gather_id);

//�����ѯʱ��
int iStart = Integer.parseInt(start);
int iEnd   = iStart + 3600*24;

//����ʱ���ʽ��Сʱ�������
String[] arr   = StringUtils.secondToDateStr(iStart);
//year = arr[0];
//month = arr[1];
//String tblname = "pm_hour_stats_"+year+"_"+month;

//��ȡ���ʽ���ƺ͵�λ
String ex_name,ex_unit;
String s = (String)expressionMap.get(expressionid);
arr = s.split(",");
ex_name = arr[0];
ex_unit = arr[1];
//��ʽ����ѯSQL
//strSQL = "select * from "+ tblname +" where gathertime>="+ iStart +" and gathertime<"+ iEnd
//			   +" and id in("+ str_iid +") order by "+ sortcolumn +" desc";
////tmpSQL = "select * from "+ tblname +" where gathertime>=$start$ and gathertime<$end$ and id in("+ str_iid +") order by "+ sortcolumn +" desc";
//
//out.println(strSQL);
//ִ��SQL����ȡ����
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
	  <TD width="162" align="center" class="title_bigwhite">��ѯ���</TD>
	  <TD colspan=""> 
		&nbsp;&nbsp;<IMG height=12 src="../images/attention_2.gif" width=15>&nbsp;�豸��<%out.println(deviceMap.get(id) + "/" + ipMap.get(id));%>
	 </TD>
	</TABLE>
	<TABLE width="100%" border=0 cellspacing=0 cellpadding=0 align="center">
	  <TR>
	  	<TD bgcolor=#999999>
	    	  <TABLE border=0 cellspacing=1 cellpadding=2 width="100%">
                <TR> 
                  <TH width='15%'>����</TH>
                  <TH width='20%'>���ֵ(<%=ex_unit%>)</TH>
                  <TH width='20%'>��Сֵ(<%=ex_unit%>)</TH>
                  <TH width='20%'>ƽ��ֵ(<%=ex_unit%>)</TH>
				  <TH width='25%'>&nbsp;</TH>
                </TR>
				<%
				//��ӡ����
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
					//��ʾÿ���ɼ����ֵ
					out.println("<TR>");
					out.println("<TH>����</TH>");
					out.println("<TH>���ֵ("+ex_unit+")</TH>");
					out.println("<TH>��Сֵ("+ex_unit+")</TH>");
					out.println("<TH>ƽ��ֵ("+ex_unit+")</TH>");
					out.println("<TH>ʱ��</TH>");
					out.println("</TR>");
					
					GeneralNetPerf netPmMore = new GeneralNetPerf(iStart, iEnd, 3, arr_iid);
					cursor = netPmMore.getGeneralChartData();
					fields = cursor.getNext();
					
					if(fields == null){
						out.println("<TR bgcolor=#ffffff><TD colspan=5>Сʱ��������û�����</TD></TR>");
					}
					else{
						String old_id="",cur_id;
						String gathertime;
						String tmp;
						long lms;
						//һ��ʵ��һ��Cursor
						
						Cursor[] cursors = new Cursor[arr_iid.length];
						//ʵ������
						String[] rowKeys = new String[arr_iid.length];

						//ʵ��Cursor�����±�
						int ln = -1;
						while(fields != null&&ln<arr_iid.length-1){
							id = (String)fields.get("id");
							tmpStr = (String)indexMap.get(id);
							arr = tmpStr.split(",");

							cur_id = arr[2];
							gathertime  = (String)fields.get("gathertime");
							tmp			= arr[3];
							if(!old_id.equals(cur_id)){
								ln++;	//ʵ����ͬ��ʵ��Cursor��������
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
							
							//������ε�ʵ��
							old_id = cur_id;
							fields = cursor.getNext();
						}
						//������ͼ	
						String title = " ������ͳ�Ʊ���";
						TimeSeriesChart chart = new TimeSeriesChart();
						chart.setTimeStep(5);
						chart.setChartBaseinfo(title,"ʱ��",ex_name+": ��λ("+ex_unit+")","gathertime",sortcolumn,3);
												chart.setChartDataset(cursors,rowKeys,null);

						String filename = chart.createChart(session, new PrintWriter(out));

						String graphURL = request.getContextPath() + "/servlet/DisplayChart?filename=" + filename;
						out.println("<TR bgcolor=#ffffff><TD colspan=5 align=center><img src='"+ graphURL +"' border=0 usemap='#"+filename+"'></TD></TR>");
					}
				}
				else{
					out.println("<TR bgcolor=#ffffff>");
					out.println("<TD class=column  colspan=5 align=center>��ѯ������</TD>");
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
	//��ʽ������
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