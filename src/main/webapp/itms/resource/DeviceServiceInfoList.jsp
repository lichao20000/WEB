<%@ page language="java" contentType="text/html; charset=GBK"
	pageEncoding="GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="lk" uri="/linkage"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<title>�����豸��Դ</title>
<%
	/**
	 * e8-cҵ���ѯ
	 * 
	 * @author qixueqi(4174)
	 * @version 1.0
	 * @since 2010-09-08
	 * @category
	 */
%>
<link rel="stylesheet" href="<s:url value="/css3/c_table.css"/>"
	type="text/css">
<link rel="stylesheet" href="<s:url value="/css3/global.css"/>"
	type="text/css">
<script type="text/javascript" src="<s:url value="/Js/jquery.js"/>"></script>
<script type="text/javascript"
	src="<s:url value="/Js/jQuerySplitPage-linkage.js"/>"></script>

<script type="text/javascript">
function ToExcel() {
	var mainForm = document.getElementById("selectForm");
	mainForm.submit();
}
	function DetailDevice(device_id) {
		var strpage = "../../Resource/DeviceShow.jsp?device_id=" + device_id;
		window.open(strpage, "","left=20,top=20,width=900,height=650,resizable=yes,scrollbars=yes");
	}
</script>
</head>
<body>

<TABLE boder=0 cellspacing=0 cellpadding=0 width="100%">
<TR><TD HEIGHT=20>&nbsp;</TD></TR>
<TR><TD>
	<TABLE width="100%" border=0 cellspacing=0 cellpadding=0 align="center" >
		<TR>
			<TD>
				<table width="100%" height="30" border="0" cellspacing="0" cellpadding="0" class="green_gargtd">
					<tr>
						<td width="162" align="center" class="title_bigwhite">
							�豸��Դ
						</td>
					</tr>
				</table>
			</TD>
		</TR>
		<TR>
			<TD>
				<table class="listtable" id="listTable">
					<caption>��ϵͳ�������������豸��Դ</caption>
					<thead>
						<tr>
							<TH>�豸����</TH>
							<TH>�ͺ�</TH>
							<TH>����汾</TH>
							<TH>����</TH>
							<TH>�豸���к�</TH>
							<TH>������</TH>
							<TH>����</TH>
						</tr>
					</thead>
					<tbody>
						<s:if test="deviceList!=null">
							<s:if test="deviceList.size()>0">
								<s:iterator value="deviceList">
									<tr>
										<td><s:property value="vendor" /></td>
										<td><s:property value="devicemodel" /></td>
										<td><s:property value="softwareversion" /></td>
										<td><s:property value="city_name" /></td>
										<td><s:property value="device_serialnumber" /></td>
										<td><s:property value="area_name" /></td>
										<td><a
											href="javascript:DetailDevice('<s:property value="device_id" />')">��ϸ��Ϣ</a></td>
									</tr>
								</s:iterator>
							</s:if>
							<s:else>
								<tr>
									<td colspan=7>ϵͳû�и��û���ҵ����Ϣ!</td>
								</tr>
							</s:else>
						</s:if>
						<s:else>
							<tr>
								<td colspan=7>ϵͳû�д��û�!</td>
							</tr>
						</s:else>
					</tbody>
			
					<tfoot>
						<tr>
							<td colspan="7" align="right"><lk:pages
									url="/itms/resource/DeviceService!DeviceVendorList.action"
									styleClass="" showType="" isGoTo="true"  changeNum="true" /></td>
						</tr>
						<tr>
							<form id="form" name="selectForm" action="<s:url value='/itms/resource/DeviceService!excelDeviceListByEdition.action'/>">
								<input type="hidden" name="startOpenDate" id="startOpenDate" value="<s:property  value='startOpenDate'/>" />
								<input type="hidden" name="endOpenDate" id="endOpenDate"  value="<s:property  value='endOpenDate'/>"/>
								<input type="hidden" name="vendorId" id="vendorId" value="<s:property  value='vendorId'/>" />
								<input type="hidden" name="softwareversion" id="softwareversion" value="<s:property  value='softwareversion'/>" />
								<input type="hidden" name="hardwareversion" id="hardwareversion" value="<s:property  value='hardwareversion'/>" />
								<input type="hidden" name="gw_type" id="gw_type" value="<s:property  value='gw_type'/>"/>
								<input type="hidden" name="city_id" id="city_id" value="<s:property  value='city_id'/>"/>
							<td colspan="7" align="right"><IMG SRC="../../images/excel.gif"
								BORDER='0' ALT='�����б�' style='cursor: hand' onclick="ToExcel()">
							</td>
							</form>
						</tr>
					</tfoot>
				</table>
			</TD>
		</TR>
	</TABLE>
</TD></TR>

</TABLE>
</body>
</html>