<%@ include file="../timelater.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>
<%@page import="java.util.*"%>
<%@page import="java.util.HashMap,com.linkage.litms.resource.*,com.linkage.litms.common.database.*"%>
<%@ page import="com.linkage.module.gwms.dao.tabquery.CityDAO" %>
<%
request.setCharacterEncoding("GBK");


	String loopback_ip = request.getParameter("loopback_ip");
	String device_ser = request.getParameter("device_ser");
	String city_id = request.getParameter("city_id");
	
	if(loopback_ip == null){
		loopback_ip = "";
	}
	if(device_ser == null){
		device_ser = "";
	}
	if(city_id == null){
		city_id = "";
	}	
	//������Ϣ
	
	DeviceAct deviceAct = new DeviceAct();
	String 	strCityList = deviceAct.getCityListSelf(false, city_id, "", request);
	
	
	
	
	
List list  = new ArrayList();
list.clear();


Map map = new HashMap();
map = CityDAO.getCityIdCityNameMap();
DeviceAct deviceact = new DeviceAct();
// �豸����Map
Map venderMap = deviceact.getOUIDevMap();
Map modelMap = DeviceAct.getDevice_Model();
//out.println(strSQL);
String strData = "";
list = DeviceAct.getDevicestatus(request);
String strBar = String.valueOf(list.get(0)); 

Cursor cursor = (Cursor)list.get(1);

Date date = new Date();
long nowtime = date.getTime();

//�豸״̬
String status = "";
Map fields = cursor.getNext();

if (fields == null) {
    strData = "<TR><TD class=column COLSPAN=8 >��ϵͳû���豸��Դ</TD></TR>";
} else {

    while (fields != null) {
    
    	
    	if((nowtime - Long.parseLong((String) fields.get("max_time"))*1000) > 5*60*1000){  	
    		status = "<font color='red'>�豸����</font>";   		
    	} else {
    		status = "<font color='green'>�豸����</font>";   
    	}

        strData += "<TR>";
        strData += "<TD class=column2>" + venderMap.get((String) fields.get("oui")) + "</TD>";
        strData += "<TD class=column2>" + modelMap.get((String) fields.get("device_model_id")) + "</TD>";
        strData += "<TD class=column2>" + (String) fields.get("device_name") + "</TD>";
        strData += "<TD class=column2>" + (String) fields.get("loopback_ip") + "</TD>";
        strData += "<TD class=column2>" + (String) fields.get("device_ip") + "</TD>";
        strData += "<TD class=column2>" + (map.get((String) fields.get("city_id")) == null ? "" : map.get((String) fields.get("city_id"))) + "</TD>";        
        strData += "<TD class=column2 align=center>" + status + "</TD>";

       strData += "<TD class=column1 nowrap=\"nowrap\" align='center'><A HREF=\"javascript:GoContent('" + (String) fields.get("device_id") + "');\" TITLE=�鿴" + (String) fields.get("device_name") + "��ϸ����> ��ϸ���� </A></TD>";
		
                
        strData += "</TR>";
        fields = cursor.getNext();
    }
    strData += "<TR><TD class=column COLSPAN=10 align=right>" + strBar + "</TD></TR>";
}

map = null;
%>
<%@ include file="../head.jsp"%>
<SCRIPT LANGUAGE="JavaScript">
<!--

function GoContent(device_id){

	strpage="../Resource/devicedetail.jsp?device_id=" + device_id;   
	window.open(strpage,"","left=20,top=20,width=450,height=300,resizable=no,scrollbars=yes");
}

function Golist(){
	this.location = "deviceStatus.jsp?city_id=<%=city_id%>&loopback_ip=<%=loopback_ip%>&device_ser=<%=device_ser%>"
}

//-->
</SCRIPT>
<%@ include file="../toolbar.jsp"%>
<TABLE border=0 cellspacing=0 cellpadding=0 width="100%">
<TR><TD HEIGHT=20>&nbsp;</TD></TR>
<TR><TD>
	<FORM NAME="frm" METHOD="post" ACTION="" onsubmit="return CheckForm()">
		<TABLE width="98%" border=0 cellspacing=0 cellpadding=0 align="center">
			<tr>
				<td>
					<table width="100%" height="30" border="0" cellspacing="0"
						cellpadding="0" class="green_gargtd">
						<tr>
							<td width="162" align="center" class="title_bigwhite">
								�豸���
							</td>
							<td>
								<img src="../images/attention_2.gif" width="15" height="12">
								��ѯ�豸����״����
							</td>
							<td align="right">
								<A HREF="javascript:Golist();" TITLE= ˢ�� >ˢ��</A>
							</td>
						</tr>
					</table>
				</td>
			</tr>
			<TR>
				<TD bgcolor=#999999>
					<TABLE border=0 cellspacing=1 cellpadding=2 width="100%">
						<TR><TH colspan=8>SNMP�豸���</TH></TR>
						<TR>
							<TD class='green_title2'>�豸����</TD>
							<TD class='green_title2'>�豸�ͺ�</TD>
							<TD class='green_title2'>�豸����</TD>
							<TD class='green_title2'>�豸����</TD>
							<TD class='green_title2'>�豸IP</TD>
							<TD class='green_title2'>����</TD>
							<TD class='green_title2'>״̬</TD>
							<TD class='green_title2' width=150>����</TD>
						</TR>
						<%=strData%>
					</TABLE>
				</TD>
			</TR>
		</TABLE>
	</FORM>	
</TD></TR>
<TR><TD>
	<TABLE  width="90%" border=0 cellspacing=0 cellpadding=0 align="center">
	<TR>
		<TD><B>�� �� �� ѯ</B><br><hr size=2 color=#646464></TD>
	</TR>
	<TR>
		<TD>
		<FORM NAME="frm1" METHOD=POST ACTION="deviceStatus.jsp">
		�豸����:&nbsp;<INPUT TYPE="text" NAME="loopback_ip"  class=bk value="<%=loopback_ip%>">&nbsp;�豸���к�:<INPUT TYPE="text" NAME="device_ser" size=20 maxlength=30 class=bk value="<%=device_ser%>">&nbsp;	
		<br><br> �豸����:&nbsp;<%=strCityList%>&nbsp;&nbsp;&nbsp;&nbsp;<INPUT TYPE="submit" name="cmdQuery" value=" �� ѯ " class=btn>&nbsp;&nbsp;		
		</FORM>
		</TD>
	</TR>
	</TABLE>
</TD></TR>
<TR><TD HEIGHT=20><IFRAME ID=childFrm SRC="" STYLE="display:none"></IFRAME>&nbsp;</TD></TR>
</TABLE>
<%@ include file="../foot.jsp"%>