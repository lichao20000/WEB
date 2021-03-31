<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
"http://www.w3.org/TR/html4/loose.dtd">
<%@ include file="../timelater.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>
<%@ page import="java.text.*,java.util.*" %>
<%@ page import= "com.linkage.litms.common.util.*"%>
<%@ page import="com.linkage.litms.common.corba.interfacecontrol.*"%>
<%@ page import="com.linkage.litms.webtopo.DeviceResourceInfo" %>
<%@ page import="org.omg.CORBA.StringHolder" %>
<%@ include file="../head.jsp"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<title>����ʵʱ��ʾ</title>
</head>
<body >
<% 
  	request.setCharacterEncoding("GBK");
	String device_id= request.getParameter("device_id");
	String str_polltime = request.getParameter("polltime");
		
	//ͨ��FluxDataList�����ݿ��Mastercontrol���������Ϣ
	//FluxDataList fluxData = new FluxDataList();
	//HashMap hMap = fluxData.getInterfaceDevicePort(device_id);
	int type =1;
	//��Դ.2��VPN������ͼ
	String srcType = request.getParameter("type");
	if(srcType!=null&&!"".equals(srcType))
	{
		type = Integer.parseInt(srcType.trim());
	}	
	FluxControl.FluxData[] fluxDataTab = null;
	fluxDataTab = FluxManagerInterface.GetInstance().getPortData(device_id, new StringHolder());
	int size = fluxDataTab.length;
	long[] fluxKey = new long[size];
	Map fluxMap = new HashMap();
	//com.linkage.liposs.common.util.ArrayListSort sort = new com.linkage.liposs.common.util.ArrayListSort();
 
	for(int i=0;i<fluxDataTab.length;i++){
	    fluxMap.put(fluxDataTab[i].index,fluxDataTab[i]);
	    fluxKey[i] = Long.parseLong(fluxDataTab[i].index);
	}
	Arrays.sort(fluxKey);
	String [] arr_port = new String[fluxDataTab.length];
	//session.setAttribute("fluxDatas",fluxDataTab);
	
	//ͨ��DeviceResourceInfo�����ݿ����豸��Ϣ
	DeviceResourceInfo deviceInfo = new DeviceResourceInfo();
	Cursor cursor = deviceInfo.getDeviceResource(device_id);
	Map myMap = cursor.getNext();
	//��õ�ǰʱ��
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	Date date = new Date();
	String today = sdf.format(date);
	String deviceName= (String)myMap.get("device_name");
	String loopbackip= (String)myMap.get("loopback_ip");
	
	
	double unit = 1024;

//�����Զ�ˢ�¹���
//2007-3-12 ���ղ���
if (str_polltime != null) {
	int polltime = Integer.parseInt(str_polltime) * 60;
	out.println("<meta http-equiv=\"refresh\" content=\""+ polltime + "\">");
}

//�˿���������
Map<String, String> portNameMap = new HashMap<String, String>();
String tempSql = "select port_name, port_desc from tab_port_name_map where device_model_id=(select device_model_id from tab_gw_device where device_id='"+device_id+"')";
Cursor cursor1 = DataSetBean.getCursor(tempSql);
Map tempMap = cursor1.getNext();
while (null != tempMap) {
	portNameMap.put((String)tempMap.get("port_name"), (String)tempMap.get("port_desc"));
	tempMap = cursor1.getNext();
}

%>

<table width="98%" border="0" cellspacing="0" cellpadding="0">
  <tr>
    <td bgcolor="#FFFFFF"> <br>
        <br>
		<td width="100%">
        <table width="100%" height="30"  border="0" align="center" cellpadding="0" cellspacing="0" class="green_gargtd">
          <tr>
            <td align="center" nowrap  class="title_bigwhite">�豸���ܼ��</td>
            <td align="left" nowrap>&nbsp;&nbsp;<img src="../images/attention_2.gif" width="15" height="12"> �������豸:<%= deviceName %>:&nbsp;&nbsp;<%=loopbackip%>�����ܼ����Ϣ </td>
            <td align="right" nowrap><a href="webtopo_liuliang_historyForm.jsp?device_id=<%= device_id%>&type=<%=type %>&device_name=<%=deviceName%>&device_ip=<%=loopbackip%>">��ʷ������ʾ </a><a href="webtop_liuliang_report.jsp?device_id=<%= device_id %>&device_name=<%= deviceName %>&loopback_ip=<%= loopbackip%>&type=<%=type %>"><img src="../images/chart.gif" width="20" height="19" border="0"> ͼ����ʾ</a></td>
          </tr>
		<tr>
			<TD class="column8" colspan="10" style='border:1px solid #999999;'>����ʵʱ��ʾ</TD>
	  	</tr>
		</table>
        <table width="100%" border=0 align="center" cellpadding="1" cellspacing="1" bgcolor="#999999" class="text">
          <tr bgcolor="#FFFFFF" class="green_title">
            <td nowrap>����</td>
            <td nowrap>�˿�����</td>
			<td nowrap>�˿�����</td>
            <td nowrap>�˿ڱ���</td>
			<td nowrap>�˿�IP</td>
            <td nowrap>��������/bps</td>
			<td nowrap>��������/bps</td>
            <td nowrap>��������</td>
			<td nowrap>���������</td>
            <td nowrap>���붪����</td>           
            <td nowrap>����������</td>
            <td nowrap>����㲥������</td>
            <td nowrap>�����㲥������</td>
            <td nowrap>�˿�����</td>
          </tr>
		  
		  
			<%				
				if(fluxDataTab!=null)
				{
				String inoctets;
				String outoctets;
				for(int i=0;i<size;i++){
				    fluxDataTab[i] = (FluxControl.FluxData)fluxMap.get(String.valueOf(fluxKey[i]));
					inoctets = fluxDataTab[i].ifinoctetsbps;
					
					if(inoctets.length()>=6 && inoctets.length()<=9) {
						inoctets =StringUtils.formatString((Double.parseDouble(inoctets)/(unit*unit)),2) + "M";
					}
					else if (inoctets.length()>=10) {
						inoctets = StringUtils.formatString((Double.parseDouble(inoctets)/(unit*unit*unit)),2) + "G";
					}
					else {
						inoctets = fluxDataTab[i].ifinoctetsbps;
					}
					
					 outoctets= fluxDataTab[i].ifoutoctetsbps;
					
					if(outoctets.length()>=6 && outoctets.length()<=9) {
						outoctets = StringUtils.formatString((Double.parseDouble(outoctets)/(unit*unit)),2) + "M";
					}
					else if (outoctets.length()>=10) {
						outoctets = StringUtils.formatString((Double.parseDouble(outoctets)/(unit*unit*unit)),2) + "G";
					}
					else {
						outoctets = fluxDataTab[i].ifoutoctetsbps ;
					}
					//tmpParam = "'" + fluxDataTab[i].index + "','" + Encoder.ParseXMLCode(fluxDataTab[i].ifdescr) + "','" + Encoder.ParseXMLCode(fluxDataTab[i].ifname) + "','" + Encoder.ParseXMLCode(fluxDataTab[i].ifnamedefined) + "','" + fluxDataTab[i].ifportip + "'";
			%>
          <tr bgcolor="#FFFFFF">
            <td><%= fluxDataTab[i].index %></td>
			<td><%= fluxDataTab[i].ifdescr %></td>
			<td><%= fluxDataTab[i].ifname %></td>
			<td><%= fluxDataTab[i].ifnamedefined %></td>
			<td><%= fluxDataTab[i].ifportip %></td>
			<td align=right><%=inoctets %></td>
			<td align=right><%=outoctets %></td>
			<td align=right><%=fluxDataTab[i].ifinerrorspps %></td>
			<td align=right><%=fluxDataTab[i].ifouterrorspps %></td>
			<td align=right><%=fluxDataTab[i].ifindiscardspps %></td>			
			<td align=right><%=fluxDataTab[i].ifoutdiscardspps %></td>
			<td align=right><%=fluxDataTab[i].ifinucastpktspps%></td>
			<td align=right><%=fluxDataTab[i].ifoutucastpktspps %></td>
			<td align=right><%=portNameMap.get(fluxDataTab[i].ifdescr) %></td>
		  </tr>
		  <% 
			  arr_port[i] = fluxDataTab[i].index;
		  }
		}

		myMap.clear();
		fluxMap.clear();
	%>
          <tr bgcolor="#FFFFFF" class=text onmouseout="className='blue_trOut'">
            <td colspan="18" class="blue_foot" ><table width="98%"  border="0" align="center" cellpadding="0" cellspacing="0">
              <tr>
                <td width="77%">&nbsp;&nbsp;<img src="../images/attention.gif" width="15" height="12"> �ɼ�ʱ��:<%=today %></td>
                <td width="23%" align=right>
                </td>
              </tr>
            </table>
			</td>
          </tr>
      </table>
    </td>
  </tr>
  <tr>
    <td>&nbsp;</td>
  </tr>
</table>
<script language="javascript">
	//�����ϴε�����ж���
	var _srcObj;
	function ViewReport(){
		var ifindex,ifdescr,ifname,ifnamedefined,ifportip;
		if(_srcObj != null){
			_srcObj.className="";
		}
		var obj = event.srcElement.parentElement;
		
		obj.className="trOver";
		_srcObj = obj;

		if(obj.tagName != "TR") return ;
		ifindex = obj.cells[0].innerText;
		ifdescr = obj.cells[1].innerText;
		ifname = obj.cells[2].innerText;
		ifnamedefined = obj.cells[3].innerText;
		ifportip = obj.cells[4].innerText;

		var $ = document.all;
		$("ifindex").value = ifindex;
		$("ifdescr").value = ifdescr;
		$("ifname").value = ifname;
		$("ifnamedefined").value = ifnamedefined;
		$("ifportip").value = ifportip;
		$("MRTG_FORM").submit();
		//page = "../Visualman/now_getPort.jsp?device_id=<%=device_id%>" + "&ifindex=" + ifindex + "&ifdescr=" + ifdescr +"&ifname=" + ifname +"&ifnamedefined=" + ifnamedefined+"&ifportip=" + ifportip;
		//window.open(page);
	}
</script>
<form name="MRTG_FORM" action="./now_getPort.jsp" target="_blank">
	<input name="device_id"  type="hidden" value="<%=device_id%>">
	<input name="ifindex" type="hidden" value="">
	<input name="ifdescr" type="hidden" value="">
	<input name="ifname" type="hidden" value="">
	<input name="ifnamedefined" type="hidden" value="">
	<input name="ifportip" type="hidden" value="">
</form>
</body>
</html>
<!-- //�����Զ�ˢ�¹��� -->
<!-- //2007-3-12 ���ղ��� -->
<script language="JavaScript">
<!--
<%if(str_polltime != null){%>
parent.closeMsgDlg();
<%}%>
//-->
</script>