<%--
Author		: yanhj
Date		: 2002-9-24
Note		:
Modifiy		: yanhj for maxupstreamrate,oper_accounts 2007-1-2
--%>

<%@ include file="../timelater.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>
<%@ page import="com.linkage.litms.common.filter.*,com.linkage.litms.common.database.*"%>
<%
request.setCharacterEncoding("GBK");

//ת��ҵ������
Map productTypeMap = com.linkage.litms.netcutover.CommonMap.getProductTypeMap();
//ת����������
Map servTypeMap = com.linkage.litms.netcutover.CommonMap.getServTypeMap();

SearchSheet searchSheet = new SearchSheet();
searchSheet.setRequest(request);
Map field 	= searchSheet.getWorkSheetRecord();
Map field1 	= searchSheet.get97orginalRecord();

//���������Ϣ
String username 		= null;
String worksheet_source		= null;
String system_id		= null;
String sheet_id			= null;
String product_id		= null;
String worksheet_receive_time	= null;
String producttype		= null;
String worksheet_exec_time	= null;
String servtype			= null;
String worksheet_start_time	= null;
String worksheet_end_time	= null;
String worksheet_priority	= null;
String worksheet_desc		= null;
String worksheet_error_desc	= null;

if(field != null){
	username 		= String.valueOf(field.get("username"));
	worksheet_source	= (field.get("worksheet_source").equals("0"))?"97ϵͳ":"�ֹ�¼��";
	system_id		= String.valueOf(field.get("system_id"));
	sheet_id		= String.valueOf(field.get("sheet_id"));
	product_id		= String.valueOf(field.get("product_id"));
	worksheet_receive_time	= String.valueOf(field.get("worksheet_receive_time"));
	producttype		= String.valueOf(productTypeMap.get(String.valueOf(field.get("producttype"))));
	worksheet_exec_time	= String.valueOf(field.get("worksheet_exec_time"));
	servtype		= String.valueOf(servTypeMap.get(String.valueOf(field.get("servtype"))));
	worksheet_start_time	= String.valueOf(field.get("worksheet_start_time"));
	worksheet_end_time	= String.valueOf(field.get("worksheet_end_time"));
	worksheet_priority	= String.valueOf(field.get("worksheet_priority"));
	worksheet_desc		= String.valueOf(field.get("worksheet_desc"));
	worksheet_error_desc	= String.valueOf(field.get("worksheet_error_desc"));
	if (worksheet_desc == null || worksheet_desc.equals("null"))
        worksheet_desc = "";
	if (worksheet_error_desc == null || worksheet_error_desc.equals("null"))
        worksheet_error_desc = "";
}else{
	username 		= "";
	worksheet_source	= "";
	system_id		= "";
	sheet_id		= "";
	product_id		= "";
	worksheet_receive_time	= "";
	producttype		= "";
	worksheet_exec_time	= "";
	servtype		= "";
	worksheet_start_time	= "";
	worksheet_end_time	= "";
	worksheet_priority	= "";
	worksheet_desc		= "";
	worksheet_error_desc	= "";
}

//����Ӧ�������豸�����ת������������
String city_id 			= null;
String office_id 		= null;
String zone_id 			= null;
String adslbindphone		= null;
String dslam_ban_old_ip 	= null;
String dslam_ban_old_shelf 	= null;
String dslam_ban_old_frame 	= null;
String dslam_ban_old_slot 	= null;
String dslam_ban_old_port 	= null;
String maxdownstreamrate 	= null;
String maxupstreamrate		= null;
String oper_accounts		= null;
String dslam_ban_devicecode	= null;
String dslam_ban_old_devicecode	= null;
String dslam_ban_ip 		= null;
String dslam_ban_shelf 		= null;
String dslam_ban_frame 		= null;
String dslam_ban_slot 		= null;
String dslam_ban_port 		= null;
try{
city_id		= (String)field.get("system_id");
}
catch(Exception e){
city_id = "-1";
}

if(field1 != null){
	office_id 	= String.valueOf(field1.get("office_id"));	//����
	zone_id 	= String.valueOf(field1.get("zone_id"));	//С��
	adslbindphone	= String.valueOf(field1.get("adslbindphone"));	//С��
	
	dslam_ban_old_devicecode= String.valueOf(field1.get("dslam_ban_old_devicecode"));	//�ɽ����豸����
	dslam_ban_old_ip 	= String.valueOf(field1.get("dslam_ban_old_ip"));		//�ɽ����豸IP
	dslam_ban_old_shelf 	= String.valueOf(field1.get("dslam_ban_old_shelf"));	//�ɽ����豸���ܺ�
	dslam_ban_old_frame 	= String.valueOf(field1.get("dslam_ban_old_frame"));	//�ɽ����豸���
	dslam_ban_old_slot 	= String.valueOf(field1.get("dslam_ban_old_slot"));	//�ɽ����豸��λ��
	dslam_ban_old_port 	= String.valueOf(field1.get("dslam_ban_old_port"));	//�ɽ����豸�˿�	
	
	dslam_ban_devicecode	= String.valueOf(field1.get("dslam_ban_devicecode"));	//�½����豸����
	dslam_ban_ip 		= String.valueOf(field1.get("dslam_ban_ip"));		//�½����豸IP
	dslam_ban_shelf 	= String.valueOf(field1.get("dslam_ban_shelf"));		//�½����豸���ܺ�
	dslam_ban_frame 	= String.valueOf(field1.get("dslam_ban_frame"));		//�½����豸���
	dslam_ban_slot 		= String.valueOf(field1.get("dslam_ban_slot"));		//�½����豸��λ��
	dslam_ban_port 		= String.valueOf(field1.get("dslam_ban_port"));		//�½����豸�˿�
	
	maxdownstreamrate 	= String.valueOf(field1.get("maxdownstreamrate"));	//�����������
	maxupstreamrate 	= String.valueOf(field1.get("maxupstreamrate"));	//�����������
	oper_accounts 	= String.valueOf(field1.get("oper_accounts"));	//����Ա
	if (maxupstreamrate == null || maxupstreamrate.equals("null"))
        maxupstreamrate = "";
	if (oper_accounts == null || oper_accounts.equals("null"))
        oper_accounts = "";
}else{
	office_id 		= "";	//����
	zone_id 		= "";	//С��
	adslbindphone 		= "";	//Adsl�󶨵绰

	dslam_ban_old_devicecode= "";	//�ɽ����豸����
	dslam_ban_old_ip 	= "";	//�ɽ����豸IP
	dslam_ban_old_shelf 	= "";	//�ɽ����豸���ܺ�
	dslam_ban_old_frame 	= "";	//�ɽ����豸���
	dslam_ban_old_slot 	= "";	//�ɽ����豸��λ��
	dslam_ban_old_port 	= "";	//�ɽ����豸�˿�	
	
	dslam_ban_devicecode	= "";	//�½����豸����
	dslam_ban_ip 		= "";	//�½����豸IP
	dslam_ban_shelf 	= "";	//�½����豸���ܺ�
	dslam_ban_frame 	= "";	//�½����豸���
	dslam_ban_slot 		= "";	//�½����豸��λ��
	dslam_ban_port 		= "";	//�½����豸�˿�

	maxdownstreamrate 	= "";	//����	
	maxupstreamrate		= "";	//��������
	oper_accounts		= "";	//����Ա
}

String city_name = null;
String office_name = null;
String zone_name = null;

if(field1 != null){
    String sql1 = "select * from tab_city where city_id='"+ ((city_id.length() == 0)?"-1":city_id)+"'";
    // teledb
    if (DBUtil.GetDB() == 3) {
        sql1 = "select city_name from tab_city where city_id='"+ ((city_id.length() == 0)?"-1":city_id)+"'";
    }
    com.linkage.commons.db.PrepareSQL psql = new com.linkage.commons.db.PrepareSQL(sql1);
    psql.getSQL();
	Map field2 = DataSetBean.getRecord(sql1);
	city_name = (field2 != null)?(String.valueOf(field2.get("city_name"))):"";

    String sql2 = "select * from tab_office where office_id='"+ office_id +"'";
    // teledb
    if (DBUtil.GetDB() == 3) {
        sql2 = "select office_name from tab_office where office_id='"+ office_id +"'";
    }
    com.linkage.commons.db.PrepareSQL psql2 = new com.linkage.commons.db.PrepareSQL(sql2);
    psql2.getSQL();
	field2 = DataSetBean.getRecord(sql2);
	office_name = (field2 != null)?(String.valueOf(field2.get("office_name"))):"";

    String sql3 = "select * from tab_zone where zone_id='"+ zone_id +"'";
    // teledb
    if (DBUtil.GetDB() == 3) {
        sql3 = "select zone_name from tab_zone where zone_id='"+ zone_id +"'";
    }
    com.linkage.commons.db.PrepareSQL psql3 = new com.linkage.commons.db.PrepareSQL(sql3);
    psql3.getSQL();
	field2 = DataSetBean.getRecord(sql3);
	zone_name = (field2 != null)?(String.valueOf(field2.get("zone_name"))):"";
}else{
	city_name = "";
	office_name = "";
	zone_name = "";
}

productTypeMap.clear();
servTypeMap.clear();
%>
<%@ include file="../head.jsp"%>
<HTML>
<BODY>
<TABLE width="95%" height="30"  border="0" cellpadding="0" cellspacing="0" class="green_gargtd" align="center">
<TR>
  <TD width="162" align="center" class="title_bigwhite"> ������ϸ��Ϣ</TD>
</TR>
</TABLE>
<TABLE border=0 cellspacing=0 cellpadding=0 width="100%">
<TR><TD>
	<TABLE width="95%" border=0 cellspacing=0 cellpadding=0 align="center">
		<TR>
			<TD bgcolor=#999999>
			<TABLE border=0 cellspacing=1 cellpadding=2 width="100%">
              <TR> 
                <td class=column width="19%">�û��ʺ�</td>
                <td class=column width="28%"><%=username%>&nbsp;</td>
                <td class=column width="22%">Adsl�󶨵绰</td>
                <td class=column width="31%"><%=adslbindphone%>&nbsp;</td>
              </TR>
              <TR> 
                <td class=column width="19%">����</td>
                <td class=column width="28%"><%=city_name%>&nbsp;</td>
                <td class=column width="22%">&nbsp;</td>
                <td class=column width="31%">&nbsp;</td>
              </TR>
              <TR> 
                <td class=column width="19%">����</td>
                <td class=column width="28%"><%=office_name%>&nbsp;</td>
                <td class=column width="22%">С��</td>
                <td class=column width="31%"><%=zone_name%>&nbsp;</td>
              </TR>
              <TR> 
                <td class=green_foot colspan="4">&nbsp;</td>
              </TR>
              <TR> 
                <td class=column width="19%">���豸����</td>
                <td class=column width="28%"><%=com.linkage.litms.common.util.CommonMap.getDeviceIdExByDeviceId(dslam_ban_old_devicecode)== ""?dslam_ban_old_devicecode:com.linkage.litms.common.util.CommonMap.getDeviceIdExByDeviceId(dslam_ban_old_devicecode)%>&nbsp;</td>
                <td class=column width="22%">&nbsp;</td>
                <td class=column width="31%">&nbsp;</td>
              </TR>			  
              <TR> 
                <td class=column width="19%">���豸IP</td>
                <td class=column width="28%"><%=dslam_ban_old_ip%>&nbsp;</td>
                <td class=column width="22%">���豸���ܺ�</td>
                <td class=column width="31%"><%=dslam_ban_old_shelf%>&nbsp;</td>
              </TR>
              <TR> 
                <td class=column width="19%">���豸���</td>
                <td class=column width="28%"><%=dslam_ban_old_frame%>&nbsp;</td>
                <td class=column width="22%">���豸��λ��</td>
                <td class=column width="31%"><%=dslam_ban_old_slot%>&nbsp;</td>
              </TR>
              <TR> 
                <td class=column width="19%">���豸�˿�</td>
                <td class=column width="28%"><%=dslam_ban_old_port%>&nbsp;</td>
                <td class=column width="22%">&nbsp;</td>
                <td class=column width="31%">&nbsp;</td>
              </TR>
              <%if(null!=DataSetBean.getRecord("select * from tab_deviceresource where device_id='"+dslam_ban_old_devicecode+"'")) {%>
              <TR> 
                <td class=blue_foot colspan="4" align="right"><A HREF=../Resource/UpdateDeviceForm.jsp?device_id=<%=dslam_ban_old_devicecode %>>�༭���豸</A></td>
              </TR>
             <%} %>
              <TR> 
                <td class=column width="19%">���豸����</td>
                <td class=column width="28%"><%=com.linkage.litms.common.util.CommonMap.getDeviceIdExByDeviceId(dslam_ban_devicecode)==""?dslam_ban_devicecode:com.linkage.litms.common.util.CommonMap.getDeviceIdExByDeviceId(dslam_ban_devicecode)%>&nbsp;</td>
				<%
				String portcode=" ";
				boolean isSZ_b = SelectCityFilter.isSZ(curUser.getCityId());
				if(isSZ_b == true && (field.get("producttype").equals("3")||field.get("producttype").equals("31"))){
						portcode=String.valueOf(field1.get("deviceencode"));
				%>
                <td class=column width="22%">�˿ڱ���</td>
                <td class=column width="31%"><%=portcode%></td>
				<%}
				else{%>
				<td class=column width="22%">&nbsp;</td>
                <td class=column width="31%">&nbsp;</td>
				<%}%>
              </TR>			  
              <TR> 
                <td class=column width="19%">���豸IP</td>
                <td class=column width="28%"><%=dslam_ban_ip%>&nbsp;</td>
                <td class=column width="22%">���豸���ܺ�</td>
                <td class=column width="31%"><%=dslam_ban_shelf%>&nbsp;</td>
              </TR>
              <TR> 
                <td class=column width="19%">���豸���</td>
                <td class=column width="28%"><%=dslam_ban_frame%>&nbsp;</td>
                <td class=column width="22%">���豸��λ��</td>
                <td class=column width="31%"><%=dslam_ban_slot%>&nbsp;</td>
              </TR>
              <TR> 
                <td class=column width="19%">���豸�˿�</td>
                <td class=column width="28%"><%=dslam_ban_port%>&nbsp;</td>
                <td class=column width="22%">&nbsp;</td>
                <td class=column width="31%">&nbsp;</td>
              </TR>
              <TR> 
                <td class=column width="19%">�����������</td>
                <td class=column width="28%"><%=maxdownstreamrate%>&nbsp;</td>
                <td class=column width="22%">�����������</td>
                <td class=column width="31%"><%=maxupstreamrate%>&nbsp;</td>
              </TR>
              <%if(null!=DataSetBean.getRecord("select * from tab_deviceresource where device_id='"+dslam_ban_devicecode+"'")) { %>
              <TR> 
                <td class=blue_foot colspan="4" align="right"><A HREF=../Resource/UpdateDeviceForm.jsp?device_id=<%=dslam_ban_devicecode %>>�༭���豸</A></td>
              </TR>
              <%} %>
              <TR> 
                <td class=column width="19%">���Ժδ�</td>
                <td class=column width="28%"><%=worksheet_source%>&nbsp;</td>
                <td class=column width="22%">97���ر�ʶ</td>
                <td class=column width="31%"><%=system_id%>&nbsp;</td>
              </TR>
              <TR> 
                <td class=column width="19%">97����Ψһ��ʶ</td>
                <td class=column width="28%"><%=sheet_id%>&nbsp;</td>
                <td class=column width="22%">ҵ��Ψһ��ʶ</td>
                <td class=column width="31%"><%=product_id%>&nbsp;</td>
              </TR>
              <TR> 
                <td class=column width="19%">��������ʱ��</td>
                <td class=column width="28%"><%=worksheet_receive_time%>&nbsp;</td>
                <td class=column width="22%">ҵ������</td>
                <td class=column width="31%"><%=producttype%>&nbsp;</td>
              </TR>
              <TR> 
                <td class=column width="19%">����ִ�д���</td>
                <td class=column width="28%"><%=worksheet_exec_time%>&nbsp;</td>
                <td class=column width="22%">��������</td>
                <td class=column width="31%"><%=servtype%>&nbsp;</td>
              </TR>
              <TR> 
                <td class=column width="19%">���ο�ʼִ��ʱ��</td>
                <td class=column width="28%"><%=worksheet_start_time%>&nbsp;</td>
                <td class=column width="22%">����ִ�����ʱ��</td>
                <td class=column width="31%"><%=worksheet_end_time%>&nbsp;</td>
              </TR>
              <TR> 
                <td class=column width="19%">�������ȼ�</td>
                <td class=column width="28%"><%=worksheet_priority%>&nbsp;</td>
                <td class=column width="22%">��������</td>
                <td class=column width="31%"><%=worksheet_desc%>&nbsp;</td>
              </TR>
              <TR> 
                <td class=column width="19%">����Ա</td>
                <td class=column width="28%"><%=oper_accounts%>&nbsp;</td>
                <td class=column width="22%">&nbsp;</td>
                <td class=column width="31%">&nbsp;</td>
              </TR>
              <TR> 
                <td class=column width="19%">����ִ�н������</td>
                <td class=column colspan="3"><%=worksheet_error_desc%>&nbsp;</td>
              </TR>
              <TR> 
                <TD colspan=7 class=green_foot align=right> 
                  <INPUT TYPE="button" value=" �� �� " onclick="javascript:window.close();" class=jianbian>
                </TD>
              </TR>
            </TABLE>
		  </TD>
		</TR>
	</TABLE>
</TD></TR>
<TR><TD HEIGHT=20>&nbsp;</TD></TR>
</TABLE>
</BODY>
<%@page import="java.util.Map"%>
<%@page import="com.linkage.litms.netcutover.SearchSheet"%>
<%@page import="com.linkage.litms.common.database.DataSetBean"%>
<%@ page import="com.linkage.commons.db.DBUtil" %>
</HTML>