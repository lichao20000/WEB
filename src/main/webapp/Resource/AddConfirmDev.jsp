<%@ page contentType="text/html;charset=GBK"%>
<%@ page import ="com.linkage.litms.common.database.Cursor"%>
<jsp:useBean id="HGWUserInfoAct" scope="request" class="com.linkage.litms.resource.HGWUserInfoAct" />
<%@ include file="../timelater.jsp"%>
<SCRIPT LANGUAGE="JavaScript" id="idParentFun">
</SCRIPT>

<%@ include file="../head.jsp"%>
<%@ include file="../toolbar.jsp"%>

<%
	request.setCharacterEncoding("GBK");
	String oui = request.getParameter("oui");
	String device_serialnumber = request.getParameter("device_serialnumber");
	//device_serialnumber = URLDecoder.decode(device_serialnumber,"GBK")
	if (device_serialnumber != null)
	{
		device_serialnumber = new String(device_serialnumber.getBytes("ISO8859-1"),"GBK");
	}
	String city_id = request.getParameter("city_id");
	String strAction = request.getParameter("action");
//	String str_oui = request.getParameter("str_oui");
//	String str_device_serialnumber = request.getParameter("str_device_serialnumber");
	if (oui == null) {
		oui = "";
	}
	if (device_serialnumber == null) {
		device_serialnumber = "";
	}
	if (city_id == null) {
		city_id = "";
	}
	//Map fields = HGWUserInfoAct.getConfirmDev(request);
	
	//����OUI�б�
	String strOUIList = HGWUserInfoAct.getVendorList(false, oui, "");
	//CITY�б�
	String strCityList = HGWUserInfoAct.getCityListSelf(false, city_id, "", request);
	
%>
<SCRIPT LANGUAGE="JavaScript">
<!--

	function CheckForm() {
		var obj = document.frm;
		if (obj.vendor_id.value == -1) {
			alert("��ѡ����!");
			obj.vendor_id.focus();
			return false;
		} else if(!IsNull(obj.device_serialnumber.value,"�豸���к�!")){
			obj.device_serialnumber.focus();
			obj.device_serialnumber.select();
			return false;
		} else if (obj.city_id.value == -1) {
			alert("��ѡ�����ر���!");
			obj.city_id.focus();
			return false;
		}
	}
	

//-->
</SCRIPT>

<TABLE border=0 cellspacing=0 cellpadding=0 width="100%" id="myTable">
<TR><TD HEIGHT=20>&nbsp;</TD></TR>
<tr>
	<td>
	<table width="95%" height="30" border="0" cellspacing="0" cellpadding="0" class="green_gargtd" align="center">
		<tr>
			<td width="162" align="center" class="title_bigwhite">
				ȷ���豸��Դ
			</td>
			<td>
				<img src="../images/attention_2.gif" width="15" height="12">
				��'<font color="#FF0000">*</font>'�ı�������д��ѡ��
			</td>
		</tr>
	</table>
	</td>
</tr>
		
<TR><TD>
    <FORM NAME="frm" METHOD="post" ACTION="ConfirmDevSave.jsp" onsubmit="return CheckForm()">
	<TABLE width="95%" border=0 cellspacing=0 cellpadding=0 align="center">
		<TR>
			<TD bgcolor=#000000>
				
              <TABLE border=0 cellspacing=1 cellpadding=2 width="100%">
                <TR>
                  <TH colspan="4" align="center">����豸ȷ����Ϣ</TH>
                </TR>
               <TR bgcolor="#FFFFFF"> 
				  <TD class=column align="left">����</TD>
                  <TD>
                    <%= strOUIList%>&nbsp;
                    <font color="#FF0000">*</font></TD>
                  <TD class=column align="left">�豸���к�</TD>
                  <TD>
                    <INPUT TYPE="text" NAME="device_serialnumber" maxlength=100 class=bk value="<%= device_serialnumber%>">&nbsp;
                    <font color="#FF0000">*</font></TD>
                </TR>
				<TR bgcolor="#FFFFFF">
				 <TD class=column align="left">���ر���</TD>
                  <TD><%= strCityList%>&nbsp;<font color="#FF0000">*</font></TD>
				  <TD></TD>
				  <TD> 
                  </TD>
				</TR>
                <TR>
                  <TD colspan="4" align="right" class=foot>
					<%
						if (strAction == null) {
					%>
						<INPUT TYPE="submit" value=" �� �� " class=btn>
	                    &nbsp;&nbsp;
	                    <INPUT TYPE="hidden" name="action" value="add">
					<%
						} else {
					%>
						<INPUT TYPE="submit" value=" �� �� " class=btn>
	                    &nbsp;&nbsp;
	                    <INPUT TYPE="hidden" name="action" value="update">
	                    <INPUT TYPE="hidden" name="hid_oui" value="<%= oui%>">
	                    <INPUT TYPE="hidden" name="hid_device_serialnumber" value="<%= device_serialnumber%>">
					<%		
						}
					%>
                    
                    <INPUT TYPE="reset" value=" �� д " class=btn>
                  </TD>
                </TR>
              </TABLE>
			</TD>
		</TR>
	</TABLE>
    </FORM>
  </TD></TR>
</TABLE>
