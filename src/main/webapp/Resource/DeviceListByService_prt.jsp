<%@ include file="../timelater.jsp"%>
<%@page import="com.linkage.litms.common.database.Cursor"%>
<%@page import="java.util.List"%>
<%@page import="com.linkage.litms.common.util.StringUtils,java.util.Map"%>
<jsp:useBean id="DeviceAct" scope="request" class="com.linkage.litms.resource.DeviceAct"/>

<%@ page import="com.linkage.module.gwms.dao.tabquery.CityDAO" %>

<%@ page contentType="text/html;charset=GBK"%>
<%
	String isNewOut = request.getParameter("isNewOut");
	String gw_type_Str = request.getParameter("gw_type");

	if (null == gw_type_Str) {
		gw_type_Str = "1";
	}
	int gw_type = Integer.parseInt(gw_type_Str);

	if("1".equals(isNewOut)){
		int res  = DeviceAct.toEXcel(request, gw_type);
		response.setContentType("text/html;charset=utf-8");
		if(res>0){

			response.getWriter().write("<script>alert('���������Ѿ����ɣ����Ժ��� \"���������������ҳ��\" ����');</script>");

		}else{
			response.getWriter().write("<script>alert('������������ʧ��');</script>");

		}

		return;
	}

	request.setCharacterEncoding("GBK");
	response.setContentType("Application/msexcel");

	response.setHeader("Content-disposition","attachment; filename=device.xls" );

	List list  = null;
	String strData = "";
	String isOut = request.getParameter("isOut");




if ("1".equals(isOut)){
	list = DeviceAct.getDeviceInfoListOutService(request, gw_type);
}
else{
	list = DeviceAct.getDeviceInfoListByService(request, gw_type);
}

String strBar = String.valueOf(list.get(0)); 
Cursor cursor = (Cursor)list.get(1);
Map fields = cursor.getNext();
Map area_Map = DeviceAct.getAreaIdMapName();
//Map city_Map = DeviceAct.getCityMap(request);
Map city_Map = CityDAO.getCityIdCityNameMap();
Map venderMap = DeviceAct.getOUIDevMap();

if (fields == null) {
    strData = "<TR><TD class=column COLSPAN=10 HEIGHT=30>��ϵͳû���豸��Դ</TD></TR>";
} else {
	String device_id = null;
	String device_id_ex = null;
	String strOper = null;
	String city_id = null;
	String city_name = null;
    while (fields != null) {
		device_id = (String)fields.get("device_id");
        device_id = device_id.replaceAll("\\+", "%2B");
        device_id = device_id.replaceAll("&", "%26");
        device_id = device_id.replaceAll("#", "%23");
		device_id_ex = (String)fields.get("device_id_ex");
		city_id = (String)fields.get("city_id");
		city_name = (String)city_Map.get(city_id);
		city_name = city_name == null ? "&nbsp;" : city_name;
        //strData += "<TR><TD class=column2 align='center'><input type=checkbox name=chkCheck value='"+ device_id +"'></TD>";
		strData += "<TR><TD align=center>" + device_id_ex + "</TD>";
		strData += "<TD align=center>" + venderMap.get(fields.get("oui")) + "</TD>";
        strData += "<TD align=center>" + (String)fields.get("device_name") + "</TD>";
		strData += "<TD align=center>" + (String)fields.get("loopback_ip") + "</TD>";
		strData += "<TD align=center>" + city_name + "</TD>";
		strData += "<TD align=left>" + (String)fields.get("device_serialnumber") + "</TD>";
		strData += "<TD align=center>" + area_Map.get((String)fields.get("area_id")) + "</TD>";//������
		
		//strData += "<TD class=column2>" + strOper + "</TD>";
		strData += "</TR>";
        fields = cursor.getNext();
    }
   
}
%>
<HTML>


<HEAD>
<TITLE>�豸ͳ�Ƶ�������</TITLE>


<META HTTP-EQUIV="Content-Type" CONTENT="Application/msexcel; charset=gb2312">


<style>

TD{

  FONT-FAMILY: "����", "Tahoma"; FONT-SIZE: 14px;

}

</style>


</HEAD>




<BODY>
<TABLE border=1 cellspacing=0 cellpadding=0 width="100%">
<TR><TD>
		<TABLE width="90%" border=1 cellspacing=1 cellpadding=0 align="center">
		<tr>
			<td align="center"><b>�豸��Դͳ��</b></td>
		</tr>
			<TR>
				<TD>
					<TABLE border=1 cellspacing=1 cellpadding=2 width="100%">
						
						<TR>
							<TH>�豸����</TH>
							<TH>�豸����</TH>
							<TH>�豸����</TH>
							<TH>�豸IP</TH>
							<TH>����</TH>
							<TH>�豸���к�</TH>
							<TH>������</TH>
						</TR>
						<%=strData%>
					</TABLE>
				</TD>
			</TR>
		</TABLE>	
</TD></TR>
</TABLE>
</BODY>
</HTML>