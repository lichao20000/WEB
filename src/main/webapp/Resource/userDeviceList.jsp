<%@ include file="../timelater.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>
<%@ page import="com.linkage.litms.common.database.*"%>
<%@ page import="com.linkage.module.gwms.dao.tabquery.CityDAO"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ page import="java.util.HashMap"%>
<jsp:useBean id="DeviceAct" scope="request"
	class="com.linkage.litms.resource.DeviceAct" />
<%

	long areaid = curUser.getAreaId();

	//-----------------------------------------------------------------
	String gw_type = request.getParameter("gw_type");

	String tab_name = "";
	//String select1 = "";
	//String select2 = "";
	//String select3 = "";

	//��ͥ����
	if (gw_type == null || gw_type.equals("") || gw_type.equals("1")) {

		tab_name = "tab_hgwcustomer";
		//select1 = "checked";
		//select2 = "";
		//select3 = "";
		gw_type = "1";
	//��ҵ����
	} else if ("2".equals(gw_type)) {
		tab_name = "tab_egwcustomer";
		//select1 = "";
		//select2 = "checked";
		//select3 = "";
		gw_type = "2";
	//snmp�豸
	} else {
		tab_name = "cus_radiuscustomer";
		//select1 = "";
		//select2 = "";
		//select3 = "checked";
		gw_type = "3";
	}
	//-----------------------------------------------------------------

	//�������ҵ���غ������ļ����õ�SNMP�豸
int GwProtocol = LipossGlobals.getGwProtocol();	

String innerHtml = "";
//if(GwProtocol == 0){
//	innerHtml ="<input type=\"radio\" name=\"gw_type\" value=\"1\" onclick=\"changeType(this.value);\"" + select1 + ">��ͥ����"
//								 + "<input type=\"radio\" name=\"gw_type\" value=\"2\" onclick=\"changeType(this.value);\"" + select2 + ">��ҵ����"
//								 + "<input type=\"radio\" name=\"gw_type\" value=\"3\" onclick=\"changeType(this.value);\"" + select3 + ">SNMP�豸";
//}else{
//	innerHtml = "";
//}

	String sql = "";
	String sqlDevice = "";
	
	if (curUser.getUser().isAdmin()) {
		
		if (!"3".equals(gw_type)){
			sql = "select b.username,a.oui,a.device_serialnumber,a.devicetype_id,a.city_id,a.gather_id,b.serv_type_id,a.vendor_id"
				+ " from tab_gw_device a, "
				+ tab_name
				+ " b"
				+ " where a.cpe_allocatedstatus=1 and a.device_id = b.device_id and b.user_state in ('1','2')";
		}
		else{
			sql = "select b.username,a.device_serialnumber,a.device_model,a.city_id,a.gather_id"
				+ " from tab_deviceresource a, cus_radiuscustomer b"
				+ " where a.device_id = b.device_id"
				+ " and a.device_status != -1 and b.user_state in ('1','2')";
		}
		
	} else {
		
		if (!"3".equals(gw_type)){
			sql = "select b.username,a.oui,a.device_serialnumber,a.devicetype_id,a.city_id,a.gather_id,b.serv_type_id,a.vendor_id"
				+ " from tab_gw_device a, "
				+ tab_name
				+ " b, tab_gw_res_area c"
				+ " where a.cpe_allocatedstatus=1 and a.device_id = b.device_id and c.res_type=1 and c.area_id="
				+ areaid  + " and c.res_id = a.device_id and b.user_state in ('1','2')";
		}
		else{
			sql = "select b.username,a.device_serialnumber,a.device_model,a.city_id,a.gather_id"
				+ " from tab_deviceresource a, cus_radiuscustomer b, tab_gw_res_area c"
				+ " where a.device_id = b.device_id and c.res_id = a.device_id and c.res_type=1 and c.area_id="
				+ areaid
				+ " and a.device_status != -1 and b.user_state in ('1','2')";

		}
		
	}
	sql += " order by a.city_id";

	//��ѯ�豸��Ϣ
	//���ӷ�ҳ���� by zhaixf
	String stroffset = request.getParameter("offset");
	int pagelen = 50;
	int offset;
	if (stroffset == null) {
		offset = 1;
	} else {
		offset = Integer.parseInt(stroffset);
	}

    String strBar = DeviceAct.getQuery(sql, offset, pagelen,gw_type);
	Cursor cursor = DeviceAct.getUserDev(sql, offset, pagelen);
    Map fields = cursor.getNext();


	Map city_Map = CityDAO.getCityIdCityNameMap();
	Map venderMap = DeviceAct.getOUIDevMap();

	//�����豸�ͺŶ�Ӧ��Ϣ
	String devicetype_id = null;
	String devicemodel = null;
	String softwareversion = null;
	Map deviceTypeMap = DeviceAct.getDeviceTypeMap();
	
	//String tran = "�豸����,�豸���к�,�豸�ͺ�,����,ADSL�ʺ�(�Ž�),ADSL�ʺ�(·��),IPTV�ʺ�";
	//tran = java.net.URLEncoder.encode(tran,"GBK");
%>

<script type="text/javascript">
<!--
function PrintTableToExcel(objTab)
{
  try 
  {
    var xls = new ActiveXObject( "Excel.Application" );
  }
  catch(e) 
  {
    //alert( "Ҫ��ӡ�ñ������밲װExcel���ӱ�������ͬʱ�������ʹ�á�ActiveX �ؼ��������������������ִ�пؼ���");
    //return false;
  }
  
  var tableRows=null;
  for (var k = 0; k < objTab.rows[0].cells.length; k++){
  	if (tableRows != null){
  		tableRows += ',' + objTab.rows[0].cells[k].innerHTML.trim();
  	}
  	else {
  		tableRows = objTab.rows[0].cells[k].innerHTML.trim();
  	}
  }

  //tableRows = encodeURIComponent(tableRows);
  var retValue = window.showModalDialog("getRows.jsp",window,"dialogWidth:300px;dialogHeight:400px");
  
  
  var hidden = '';
  if (typeof(retValue) != 'undefined'){
  	hidden = retValue;
  }
  else{
  	return;
  }
  
  xls.visible = true;
  var xlBook = xls.Workbooks.Add;
  var xlsheet = xlBook.Worksheets(1);
  var x = 1;
  var y = 1;
  for (var i = 0; i < objTab.rows.length; i++)
  {
    y = 1;
    for (var j = 0; j < objTab.rows[i].cells.length; j++)
    {
      if (hidden.indexOf(","+j+",") == -1){
      	xlsheet.Cells(x, y).Value = objTab.rows[i].cells[j].innerHTML.trim();
      	xlsheet.Cells(x, y).Borders.LineStyle = 1;
      	y++;
      }
    }
    x++;
  }
  xlsheet.Columns.AutoFit; //�Զ���Ӧ��С
  return;
}


function changeType(gw_type){

	this.location="userDeviceList.jsp?gw_type=" + gw_type;
}
function ToExcel() {
	var page="userDeviceToExcel.jsp?gw_type="+document.all("gw_type2").value;
	document.all("childFrm").src=page;
	//window.open(page);
}
//-->
</script>

<%@ include file="../head.jsp"%>

<%@ include file="../toolbar.jsp"%>
<form name="form1" action="" method="POST">
	<input type="hidden" name="gw_type2" value = <%= gw_type%>>
	<input type="hidden" name="gw_type" value = <%= gw_type%>>
	<table width="98%" border=0 cellspacing=0 cellpadding=0 align="center">
		<tr>
			<td height="20"></td>
		</tr>
		<TR>
			<TD colspan="2">
				<TABLE width="100%" height="30" border="0" cellspacing="0"
					cellpadding="0" class="green_gargtd">
					<TR>
						<TD width="164" align="center" class="title_bigwhite">
							�豸��Դ
						</TD>
						<TD>
							<%=innerHtml%>&nbsp;
						</TD>
						<td align=right>
							<s:if test='#session.isReport=="1"'><a href="#" name="export" onclick="ToExcel()">������Excel</a></s:if>
						</td>
					</TR>
					<TR class=column>
						<TH colspan="7" align="center">
							�豸�ʺŶ�Ӧ����
						</TH>
					</TR>
				</TABLE>
			</TD>
		</TR>
		<tr>
			<td>
				<table width="100%" border=0 cellspacing=1 cellpadding=2
					bgcolor=#999999 id=userTable>
					<tr>
						<th>
							����
						</th>
						<th>
							�豸����
						</th>
						<th>
							�豸�ͺ�
						</th>
						<th>
							�豸���к�
						</th>
						<th>
							ADSL�ʺ�
						</th>
						<th>
							IPTV�ʺ�
						</th>
					</tr>
					<%
						if (fields != null) {
							while (fields != null) {
								String device_id = (String) fields.get("device_id");

								String username1 = "";
								String username2 = "";

								String serviceID1 = "";
								String serviceID2 = "";

								if (!"3".equals(gw_type)){
//									if (gw_type.equals("1")) {
//										if (serviceID1 = "10") {
//										};
//
//										serviceID2 = "11";
//									} else {
//										serviceID1 = "50";
//										serviceID2 = "51";
//									}

									username1 = (String)fields.get("username");
								}
								else{
									//SNMP�豸
									username1 = (String)fields.get("username");
								}
								
								//��ʾ����
								String city_name = "";
								if (city_Map.get(fields.get("city_id")) != null) {
									city_name = (String) city_Map.get(fields
									.get("city_id"));
								}
								
								//����
								String vendor_str = "";
								vendor_str = (String)venderMap.get(fields.get("vendor_id"));
								/**
								if ("3".equals(gw_type)){
									vendor_str = (String)venderMap.get(fields.get("vendor_id"));
								}
								else{
									vendor_str = (String)venderMap.get(fields.get("oui"));
								}
								**/
								//�ͺ�
								String deviceType_str = "";
								if ("3".equals(gw_type)){
									deviceType_str = (String)fields.get("device_model");
								}
								else{
									String[] tmp = (String[])deviceTypeMap.get(fields.get("devicetype_id"));
									if (tmp != null && tmp.length==2) {
										deviceType_str = tmp[0];		
									}									
								}
					%>
					<tr>
						<td class=column width="10%" nowrap>
							<%=city_name%>
						</td>
						<td class=column width="15%" nowrap>
							<%=vendor_str%>
						</td>
						<td class=column width="15%" nowrap>
							<%=deviceType_str%>
						</td>
						<td class=column width="30%" nowrap>
							<%=fields.get("device_serialnumber")%>
						</td>
						<td class=column width="15%" nowrap>
							<%=username1%>
						</td>
						<td class=column width="15%" nowrap>
							<%=username2%>
						</td>
					</tr>
					<%
							fields = cursor.getNext();
							}
						} else {
					%>
					<tr>
						<td colspan=6 align=left class=column>
							ϵͳû�й������û���Ϣ�豸
						</td>
					</tr>
					<%
					}
					%>
					<TR><TD class=column COLSPAN=6 align=right  nowrap><%=strBar%></TD></TR>
				</table>
			</td>
		</tr>
		<TR><TD HEIGHT=20><IFRAME ID=childFrm SRC="" STYLE="display:none"></IFRAME>&nbsp;</TD></TR>
	</table>
</form>

<%@ include file="../foot.jsp"%>
