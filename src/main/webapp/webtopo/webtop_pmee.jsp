<%@ include file="../timelater.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>
<%@ include file="../head.jsp"%>
<%@ page import="com.linkage.litms.webtopo.PmeeDataList" %>
<%@ page import="com.linkage.litms.common.corba.interfacecontrol.PmeeInterface" %>
<%@ page import="com.linkage.litms.webtopo.DeviceResourceInfo" %>
<%@ page import="org.omg.CORBA.StringHolder" %>
<%@ page import="java.text.SimpleDateFormat" %>
<%@ page import="java.util.Date" %>
<title>���ܲ鿴</title>



<!-- <body onLoad="MM_preloadImages('../images/button_save2.gif')"> -->
<body>
<% 
  	request.setCharacterEncoding("GBK");
String strExpressionid="";
String strName="";
String strDescr="";

String id_1 = "";
String Name_1 = "";
String Descr_1 = "";

String id_2 = "";
String Name_2 = "";
String Descr_2 = "";

PMEE.Data[] pmeeDataTab_1 = null;
PMEE.Data[] pmeeDataTab_2 = null;

request.setCharacterEncoding("GBK");
String device_id= request.getParameter("device_id");
PmeeDataList pmeeData = new PmeeDataList();
Cursor cursor = pmeeData.getPm_expressionInfo(device_id);
Map myMap = cursor.getNext();

if(myMap == null) {
} else {
        while(myMap != null) {
        		strExpressionid += (String)myMap.get("expressionid") + "|";
        		strName +=(String)myMap.get("name") + "|";
        		strDescr +=(String)myMap.get("descr") + "|";
                myMap = cursor.getNext();
        }
}
	String str_polltime = request.getParameter("polltime");
	
	id_1 = strExpressionid.split("\\|")[0];
	Name_1 = strName.split("\\|")[0];
	Descr_1 = strDescr.split("\\|")[0];
	pmeeDataTab_1 = PmeeInterface.GetInstance().GetDataList(id_1,device_id,new StringHolder());
	
	if (2 == strExpressionid.split("\\|").length) {
		//��CPU���ڴ�
		id_2 = strExpressionid.split("\\|")[1];
		Name_2 = strName.split("\\|")[1];
		Descr_2 = strDescr.split("\\|")[1];
		pmeeDataTab_2 = PmeeInterface.GetInstance().GetDataList(id_2,device_id,new StringHolder());
	}

	DeviceResourceInfo deviceInfo = new DeviceResourceInfo();
	Cursor cursorDeviceInfo = deviceInfo.getDeviceResource(device_id);
	Map deviceMap = cursorDeviceInfo.getNext();
	String deviceName = (String)deviceMap.get("device_name");
	String loopbackip = (String)deviceMap.get("loopback_ip");
	
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	Date date = new Date();
	String today = sdf.format(date);

	//�����Զ�ˢ�¹���
	//2007-3-12 ���ղ���
	if (str_polltime != null) {
		int polltime = Integer.parseInt(str_polltime) * 60;
		out.println("<meta http-equiv=\"refresh\" content=\""+ polltime + "\">");
	}
%>

<table width="98%" border="0" cellspacing="0" cellpadding="0">
  <tr>
    <td bgcolor="#FFFFFF" align="center"> <br>
        <br>
        <td width="100%">
	       <table width="100%" border="0" cellspacing="0" cellpadding="0" bgcolor="#999999">
	        <tr>
				<TD class="column8" colspan="10" style='border:1px solid #999999;'>����ʵʱ��ʾ</TD>
		  	</tr>
	          
	        </table>
        <table width="100%" border=0 align="center" cellpadding="1" cellspacing="1" bgcolor="#999999" class="text">
          <tr bgcolor="#FFFFFF" class="blue_title">
          	<th nowrap>ѡ��</th>
            <th nowrap>����</th>
            <th nowrap>���ܱ��ʽ</th>
            <th nowrap>��������</th>
            <th nowrap>����</th>
            <th nowrap>ֵ</th>
            <th nowrap>����</th>
          </tr>
		  <%
			  if(pmeeDataTab_1!=null)
			  for(int i=0;i<pmeeDataTab_1.length;i++){
		  %>
          <tr bgcolor="#FFFFFF">
          	<td nowrap><input type="checkbox" name="indexid" value="<%=pmeeDataTab_1[i].index %>"></td>
            <td nowrap><%= Name_1 %></td>
			<td nowrap><%= Descr_1 %></td>
			<td nowrap><%= Encoder.AsciiToChineseString(pmeeDataTab_1[i].desc)%></td>
			<td nowrap><%= pmeeDataTab_1[i].index %></td>
			<td nowrap><%= pmeeDataTab_1[i].value %></td>
			<td nowrap width="15%" align="right">
			 	<a href="#" onclick="JavaScript:window.open('webtopo_PMReport.jsp?expressionid=<%= id_1%>&ip=<%= device_id%>','��ʷ������ʾ','scrollbars=yes,width=820,height=500')">��ʷ������ʾ</a>
	            <a href="#" onclick="JavaScript:window.open('webtop_xinnen_tuxin.jsp?device_id=<%= device_id%>&device_name=<%= deviceName %>&loopback_ip=<%= loopbackip%>&expID=<%= id_1 %>&expName=<%=Name_1%>&expDescr=<%=Descr_1%>','ͼ����ʾ','scrollbars=yes,width=820,height=500')"><img src="../images/chart.gif" border="0"> ͼ����ʾ</a>
	            <a href="javascript:ViewReport('<%= id_1%>','<%= Name_1%>','<%= Descr_1%>')"><img src="../images/chart.gif"  border="0"> MRTGͼ</a>
			</td>
		  </tr>
		  <% 
		   } 
		  %>
		   <%
			  if(pmeeDataTab_2!=null)
			  for(int i=0;i<pmeeDataTab_2.length;i++){
		  %>
          <tr bgcolor="#FFFFFF">
          	<td nowrap><input type="checkbox" name="indexid" value="<%=pmeeDataTab_2[i].index %>"></td>
            <td nowrap><%= Name_2 %></td>
			<td nowrap><%= Descr_2 %></td>
			<td nowrap><%= Encoder.AsciiToChineseString(pmeeDataTab_2[i].desc)%></td>
			<td nowrap><%= pmeeDataTab_2[i].index %></td>
			<td nowrap><%= pmeeDataTab_2[i].value %></td>
			<td nowrap>
				
			 	<a href="#" onclick="JavaScript:window.open('webtopo_PMReport.jsp?expressionid=<%= id_2%>&ip=<%= device_id%>','��ʷ������ʾ','scrollbars=yes,width=820,height=500')">��ʷ������ʾ</a>
	            <a href="#" onclick="JavaScript:window.open('webtop_xinnen_tuxin.jsp?device_id=<%= device_id%>&device_name=<%= deviceName %>&loopback_ip=<%= loopbackip%>&expID=<%= id_2 %>&expName=<%=Name_2%>&expDescr=<%=Descr_2%>','ͼ����ʾ','scrollbars=yes,width=820,height=500')"><img src="../images/chart.gif" width="20" height="19" border="0"> ͼ����ʾ</a>
	            <a href="javascript:ViewReport('<%= id_2%>','<%= Name_2%>','<%= Descr_2%>')"><img src="../images/chart.gif" width="20" height="19" border="0"> MRTGͼ</a>
			</td>
		  </tr>
		  <% 
		   } 
		  %>
		  
          <tr bgcolor="#FFFFFF" class=text onmouseout="className='trOverblue'">

            <td colspan="9" nowrap class="green_foot" ><table width="100%"  border="0" align="center" cellpadding="0" cellspacing="0">
              <tr>
                <td width="50%">&nbsp;&nbsp;<img src="../images/attention.gif" width="15" height="12"> �ɼ�ʱ��:<%=today %></td>
                <td width="50%" align="right">	
                <a href="#" onclick="window.location.reload();">ˢ��</a>
                  <!--<input name="Submit" type="submit" class="jianbian" value=" ˢ ��">-->&nbsp;&nbsp;
				  <!--<input name="Submit2" type="submit" class="jianbian" value=" MRTGͼ " onClick="javascriprt:ViewReport()">-->
                </td>
              </tr>
            </table></td>
          </tr>
          
      </table>
      </td>
    
  </tr>
  <tr>
    <td>&nbsp;</td>
  </tr>
</table>
<script type="text/javascript">
<!--
	function ViewReport(id,Name,Descr){
		var checkObj = document.all("indexid");
		var indexid = "";
		if(checkObj == null){
			return;
		}
		//����������ֻ��һ��ʱ.
		if(typeof(checkObj.length) == "undefined"){ 
			if(checkObj.checked){
				indexid = checkObj.value;
			}
		}else{//�����ɵ����ݶ���һ��ʱ.
			for(var i=0;i<checkObj.length;i++){
				if(checkObj[i].checked){
					if(indexid == "")
						indexid = checkObj[i].value;
					else
						indexid += "|" + checkObj[i].value;
				}
			}
		}
		//���indexidΪ��ʱ,��...
		if(indexid == "")
			alert("����ѡ��Ҫʵ��");
		else{
			var url = "./now_device_flux.jsp?device_id=<%=device_id%>";
			url += "&indexid=" + indexid;
			url += "&expID="+id+"&expName="+Name;
			url += "&Descr="+Descr;
			url += "&loopback_ip=<%=loopbackip%>&deviceName=<%=deviceName%>";
			window.open(url);
		 }
	}

//�����Զ�ˢ�¹��� 
 //2007-3-12 ���ղ��� 
<%if(str_polltime != null){%>
parent.closeMsgDlg();
<%}%>
//-->
</script>
<%@ include file="../foot.jsp"%>
