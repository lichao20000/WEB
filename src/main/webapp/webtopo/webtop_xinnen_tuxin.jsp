<%@ include file="../timelater.jsp"%>
<%@ include file="../head.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>
<%@ page import="com.linkage.litms.common.corba.interfacecontrol.PmeeInterface" %>
<%@ page import="org.omg.CORBA.StringHolder" %>
<%@ page import="java.text.NumberFormat" %>
<%@ page import="com.linkage.litms.common.chart.VerticalBarChart" %>
<%@ page import="java.io.PrintWriter" %>
<%
	request.setCharacterEncoding("GBK");
	String strExpID = request.getParameter("expID");
	String deviceID = request.getParameter("device_id");
	String strExpName = request.getParameter("expName");
	String strExpDescr = request.getParameter("expDescr");
	String deviceName = request.getParameter("device_name");
	String loopbackip = request.getParameter("loopback_ip");
	String type = request.getParameter("type");
	
	String graphURL=null;
	String filename=null;
	String strTitle = "性能显示";
	
	//begin modifide by w5221 山东要求性能直接与PMEE通讯
	//PmeeData[] pmeeDatas = (PmeeData[])(session.getAttribute("pmeeDatas"));
	PMEE.Data[] pmeeDatas = null;
	pmeeDatas =PmeeInterface.GetInstance().GetDataList(strExpID,deviceID,new StringHolder());
	//end modifide by w5221 山东要求性能直接与PMEE通讯
	
	NumberFormat nf = NumberFormat.getInstance();
	nf.setMaximumFractionDigits(2);
	nf.setMinimumFractionDigits(2);
	
	Cursor pmeeValueCur=new Cursor();
	Cursor[] cursors = new Cursor[1];
	String[] rowKeys=new String[1];
	for(int i=0; i<pmeeDatas.length; i++){
		String key = null;
		String value = null;
		
		Map fild = new HashMap();
		
		key = pmeeDatas[i].index;
		value = pmeeDatas[i].value;
		fild.put("xvalue",key);
		fild.put("yvalue",value);
		pmeeValueCur.add(fild);
		
	}
	
	
	cursors[0] = pmeeValueCur;
	rowKeys[0] = strExpDescr;
	
	String strDesc = strExpDescr + "索引";
	
	VerticalBarChart chart = new VerticalBarChart();
	
	int len = pmeeValueCur.getRecordSize();
	int max = len;
	double dUpperMargin =0.3;
	
	if(max<10)
	{
		dUpperMargin=0.9*((10-max)/9);
		if(dUpperMargin<0.3)
		{
			dUpperMargin=0.3;
		}		
		chart.setDUpperMargin(dUpperMargin);
	}
	
	chart.setChartBaseinfo(strTitle,strDesc,"索引的值","xvalue","yvalue",3);
	chart.setChartDataset(cursors,rowKeys,null);
	
	filename = chart.createChart(session, new PrintWriter(out));
	graphURL = request.getContextPath() + "/servlet/DisplayChart?filename=" + filename;
	
	String strData ="<img src='"+ graphURL+"' border=0 usemap='#"+filename+"'>";
%>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<title>图形显示</title>

</head>
<table width="100%" border="0" cellspacing="0" cellpadding="0">
  <tr>
    <td bgcolor="#FFFFFF"> <br>
        <br>
        <table width="95%" height="30"  border="0" align="center" cellpadding="0" cellspacing="0" class="green_gargtd">
          <tr>
            <td width="162" align="center"  class="title_bigwhite"> 网络实时显示</td>
            <td align="left"><span class="text">&nbsp;&nbsp;</span></td>
            <td width="111" align="left" bordercolor="0"><div align="center">
              <a href="webtop_xinnenlist.jsp?id=<%=strExpID%>&device_id=<%=deviceID%>&name=<%=strExpName%>&descr=<%=strExpDescr%>&type=<%=type %>"><img src="../images/baobiao2.gif" width="15" height="15" border="0">
报表显示</a> </div></td>
          </tr>
        </table>
        <table width="95%" border=0 align="center" cellpadding="1" cellspacing="1" bgcolor="#999999" class="text">
          <tr bgcolor="#FFFFFF" class="blue_title">
            <td>设备名:<%= deviceName %>&nbsp;&nbsp;&nbsp;&nbsp 设备IP地址:<%= loopbackip %>&nbsp;&nbsp;&nbsp;&nbsp;<!-- Y轴最大值
              <select name="select" class="form_kuang" onchange="javascript:typeChange();">
                <option value="0" selected>100</option>
                <option value="1">200</option>
              </select>-->
            </td>
          </tr>
          <tr bgcolor="#FFFFFF">
            <td bgcolor="#E6E1DB"><%= strData %></td>
          </tr>
     
        </table>
    </td>
  </tr>
  <tr>
    <td>&nbsp;</td>
  </tr>
</table>
</html>
