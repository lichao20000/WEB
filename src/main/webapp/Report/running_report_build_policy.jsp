<%@ include file="../timelater.jsp"%>
<%@ page contentType="text/html;charset=gbk"%>
<%@ include file="../head.jsp"%>
<%@ include file="../toolbar.jsp"%>
<%@ page  import="java.util.Map,java.util.TreeMap,java.util.List,java.util.Iterator,java.text.SimpleDateFormat,java.util.Date,java.util.Set,com.linkage.litms.common.database.Cursor,com.linkage.litms.common.database.DataSetBean,com.linkage.litms.LipossGlobals"%>
<%@ page import="com.linkage.commons.db.DBUtil" %>
<jsp:useBean id="deviceAct" scope="request" class="com.linkage.litms.resource.DeviceAct" />
<%
request.setCharacterEncoding("GBK");
String send_type = "0";
//String policy = "1,3|1_1,1_3|1_1,1_3|1_1_1,1_1_3";
String policy = "";

String querySql = "select * from tab_rrct_policy where device_id='rrct'";
// teledb
if (DBUtil.GetDB() == 3) {
	querySql = "select send_type, policy from tab_rrct_policy where device_id='rrct'";
}
com.linkage.commons.db.PrepareSQL psql = new com.linkage.commons.db.PrepareSQL(querySql);
psql.getSQL();
Map map = DataSetBean.getRecord(querySql);
if (null != map) {
	String send_type_ = (String)map.get("send_type");
	if (null != send_type_) {
		send_type = send_type_;
	}
	String policy_ = (String)map.get("policy");
	if (null != policy_) {
		policy = policy_;
	}
} else {
	String send_type_ = LipossGlobals.getRrctPolicySendType();
	if (null != send_type_) {
		send_type = send_type_;
	}
	String policy_ = LipossGlobals.getRrctPolicyValue();
	if (null != policy_) {
		policy = policy_;
	}
}
String device_id = request.getParameter("device_id");
String gather_id = request.getParameter("gather_id");
String vendor_id = request.getParameter("vendor_id");
String devicetype_id = request.getParameter("devicetype_id");
String softwareversion = request.getParameter("softwareversion");

String checkType = request.getParameter("checkType");
String hguser = request.getParameter("hguser");
if(null==hguser)
{
	hguser ="";
}
String telephone = request.getParameter("telephone");
if(null==telephone)
{
	telephone ="";
}

//�ɼ���
String gatherList = deviceAct.getGatherList(session, "", "", true);

//�豸����
String strVendorList = deviceAct.getVendorList(true, "", "");
%>
<SCRIPT LANGUAGE="JavaScript">

var did;
var device_id ="<%=device_id%>";
var selDispStyle = "checkbox";

function CheckForm(){
	var hguser=document.all("hguser").value;
    var telephone= document.all("telephone").value;
    var checkradios = document.all("checkType");
    var checkType = "";
    for(var i=0;i<checkradios.length;i++)
    {
      if(checkradios[i].checked)
	  {
	    checkType = checkradios[i].value;
	    break;
	  }
    }
	if(checkType==0&&document.frm.gather_id.value == -1){
		alert("��ѡ��ɼ��㣡");
		document.frm.gather_id.focus();
		return false;
  	}
  	 if(checkType==0&&document.frm.vendor_id.value == -1){
		alert("��ѡ���̣�");
		document.frm.vendor_id.focus();
		return false;
 	}
 	if(checkType==0&&document.frm.devicetype_id.value == -1){
		alert("��ѡ���豸�ͺţ�");
		document.frm.devicetype_id.focus();
		return false;
 	}
 	if(checkType==0&&document.frm.softwareversion.value == -1){
			alert("��ѡ���豸�汾��");
			document.frm.softwareversion.focus();
			return false;
		}
		
	if(checkType==1&&""==hguser&&""==telephone)
	{
	  alert("����д�û�����绰���룡");
	  document.all("hguser").focus();
	  return false;
	}
 	
 	var oselect = document.all("device_id");
 	if(oselect == null){
		alert("��ѡ���豸��");
		return false;
	 }
	var num = 0;
	if(typeof(oselect.length)=="undefined"){
		if(oselect.checked){
			did = oselect.value;
			num = 1;
		}
 	}else{
		for(var i=0;i<oselect.length;i++){
			if(oselect[i].checked){
			did = oselect[i].value;
			  num++;
			}
		}

 	}
 	if(num ==0){
		alert("��ѡ���豸��");
		return false;
	}
	return true;
}

function showChild(param)
{
	var page ="";
	if(param == "gather_id")
	{
		document.all("div_vendor").innerHTML = "<%=strVendorList%>";
	}
	if(param == "device_model_id"){
			// document.frm.device.checked = false;
			page = "showDeviceVersion.jsp?vendor_id="+document.frm.vendor_id.value + "&device_model_id="+ encodeURIComponent(document.frm.device_model_id.value);
			document.all("childFrm2").src = page;
		}
	if(param == "softwareversion")
	{
		page = "showDevice.jsp?gather_id="+document.frm.gather_id.value + "&selDispStyle=" + selDispStyle +"&vendor_id="+document.frm.vendor_id.value+ "&devicetype_id="+document.frm.softwareversion.value+"&flag=paramInstanceadd_Config&refresh="+Math.random();
		document.all("childFrm").src = page;
	}
	if(param == "vendor_id")
	{
		page = "showDevicetype.jsp?vendor_id="+document.frm.vendor_id.value;
		document.all("childFrm1").src = page;	
	}
}

function relateDevice()
{
   var hguser=document.all("hguser").value;
   var telephone= document.all("telephone").value;
   var checkradios = document.all("checkType");
   var checkType = "";
   for(var i=0;i<checkradios.length;i++)
   {
     if(checkradios[i].checked)
	 {
	   checkType = checkradios[i].value;
	   break;
	 }
   }
   //alert("hguser:"+hguser+"   telephone:"+telephone+"   checkType:"+checkType);
   if(1==checkType&&""==hguser&&""==telephone)
   {
      alert("����д�û�����绰���룡");
      document.all("hguser").focus();
   }
   else if(1==checkType)
   {
      var page="";
      page="showDevice.jsp?hguser="+hguser+"&telephone="+telephone+ "&selDispStyle=" + selDispStyle +"&flag=paramInstanceadd_Config&refresh="+Math.random();
      document.all("childFrm1").src = page;
   }
   
}

function relateDeviceBySerialno()
{
   var serialnumber=document.all("serialnumber").value;
   var checkradios = document.all("checkType");
   var checkType = "";
   for(var i=0;i<checkradios.length;i++)
   {
     if(checkradios[i].checked)
	 {
	   checkType = checkradios[i].value;
	   break;
	 }
   }
   //alert("hguser:"+hguser+"   telephone:"+telephone+"   checkType:"+checkType);
   if(checkType==2 && serialnumber=="")
   {
      alert("����д�豸���кţ�");
      document.all("serialnumber").focus();
   }
   else if(checkType==2)
   {
      var page="";
      page="showDevice.jsp?serialnumber="+serialnumber+ "&selDispStyle=" + selDispStyle +"&flag=paramInstanceadd_Config&refresh="+Math.random();
      document.all("childFrm1").src = page;
   }
   
}

function ShowDialog(param)
{
  //�����û�����ѯ
  if(param==1)
  {
     tr1.style.display="none";
     tr2.style.display="none";
     tr3.style.display="";
     tr4.style.display="none";
	selDispStyle = "radio";
  }
  
  //�����豸�汾����ѯ
  if(param==0)
  {
     tr1.style.display="";
     tr2.style.display="";
     tr3.style.display="none";
     tr4.style.display="none";
	selDispStyle = "checkbox";
  }
  
  //�����豸���к�����ѯ
  if(param==2)
  {
     tr1.style.display="none";
     tr2.style.display="none";
     tr3.style.display="none";
     tr4.style.display="";
	selDispStyle = "radio";
  }
}
</SCRIPT>
<style type='text/css'>
<!--
	BODY.bd {
		BACKGROUND-COLOR: #ffffff;
		COLOR: #000000;
		FONT-FAMILY: '����', 'Arial';
		FONT-SIZE: 12px;
		MARGIN: 0px
	}
	TH.thh {
		background-color: #B0E0E6;
		font-size: 9pt;
		color: #000000;
		text-decoration: none;
		font-weight: bold;
		line-height: 22px;
		text-align: center;
	}
	TR.trr {
		background-color:'#FFFFFF';
	}
	TD.tdd {
		FONT-FAMILY: '����', 'Tahoma';
		FONT-SIZE: 12px;
		background-color:'#FFFFFF';
	}
	TD.hd {
		background-color:'#EEE8AA';
	}
	TD.policy {
		text-align:right;
	}
-->
</style><SCRIPT LANGUAGE="JavaScript">
<!--
	
	var weekArr = new Array ("","�� һ","�� ��","�� ��","�� ��","�� ��","�� ��","�� ��");
	function writeDay(name, selObj) {
		document.write("<select name='" + name + "'>");
		for (var i=1; i<=31; i++) {
			document.write("<option value='" + i + "'" + ((selObj != null && i == selObj) ? " selected" : "") + ">" + i + " ��");
		}
		document.write("</select>");
	}
	function writeWeek(name, selObj) {
		document.write("<select name='" + name + "'>");
		for (var i=1; i<=7; i++) {
			document.write("<option value='" + i + "'" + ((selObj != null && i == selObj) ? " selected" : "") + ">" + weekArr[i]);
		}
		document.write("</select>");
	}
	function writeMonth(name, selObj) {
		document.write("<select name='" + name + "'>");
		for (var i=1; i<=12; i++) {
			document.write("<option value='" + i + "'" + ((selObj != null && i == selObj) ? " selected" : "") + ">" + i + " ��");
		}
		document.write("</select>");
	}
	function writeHour(name, selObj) {
		document.write("<select name='" + name + "'>");
		for (var i=1; i<=24; i++) {
			document.write("<option value='" + i + "'" + ((selObj != null && i == selObj) ? " selected" : "") + ">" + i + " ʱ");
		}
		document.write("</select>");
	}
//-->
</SCRIPT>
<TABLE border=0 cellspacing=0 cellpadding=0 width="98%" align="center" >
	<TR>
		<TD HEIGHT=20>
			&nbsp;
		</TD>
	</TR>
	<TR>
		<TD>
			<FORM NAME="frm" METHOD="post" ACTION="paramAllList.jsp" onsubmit="return CheckForm()">
				<table width="100%" border="0" cellpadding="0" cellspacing="0" class="text">
					<tr>
						<td>
						<table width="100%" height="30" border="0" cellspacing="0" cellpadding="0" class="green_gargtd">
							<tr>
								<td width="162" align="center" class="title_bigwhite" nowrap>
									���б�������
								</td>
								<td nowrap>
									<img src="../images/attention_2.gif" width="15" height="12">
									�����豸���б�����ԡ�
									<!-- <input type="radio" value="0" onclick="ShowDialog(this.value)" name="checkType" checked>�߼���ѯ&nbsp;&nbsp;  -->
			                        <input type="radio" value="1" onclick="ShowDialog(this.value)" name="checkType">���û�&nbsp;&nbsp;
			                        <input type="radio" value="2" onclick="ShowDialog(this.value)" name="checkType" checked>���豸
								</td>
							</tr>
						</table>
						</td>
					</tr>
					<tr>
						<td>
							<TABLE width="100%" border=0 cellspacing=0 cellpadding=0>
								<TR>
									<TD bgcolor=#999999>
										<TABLE border=0 cellspacing=1 cellpadding=2 width="100%">
											<TR>
												<TH colspan="4" align="center">
													�豸��ѯ
												</TH>
											</TR>
											<TR bgcolor="#FFFFFF" id="tr1" STYLE="display:none">
											    <TD align="right" width="20%">
													�ɼ���:
												</TD>
												<TD align="left" width="30%">
												    <%=gatherList%>
												</TD>
												<TD align="right" width="20%">
													����:
												</TD>
												<TD align="left" width="30%">
													<div id="div_vendor">
														<select name="vendor" class="bk">
															<option value="-1">
																--����ѡ��ɼ���--
															</option>
														</select>													
													</div>
												</TD>
											</TR>
											<TR bgcolor="#FFFFFF" id="tr2" STYLE="display:none">
											    <TD align="right" width="20%">
													�豸�ͺ�:
												</TD>
												<TD width="30%">
													<div id="div_devicetype">
														<select name=devicetype_id class="bk">
															<option value="-1">
																--����ѡ����--
															</option>
														</select>
													</div>
												</TD>
												<TD align="right" width="20%">
													�豸�汾:
												</TD>
												<TD width="30%">
													<div id="div_deviceversion">
														<select name="device_version" class="bk">
															<option value="-1">
																--����ѡ���豸�ͺ�--
															</option>
														</select>													
													</div>
												</TD>
											</TR>
											<TR bgcolor="#FFFFFF" id="tr3" STYLE="display:none">
											    <TD align="right" width="20%">
													�û���:
												</TD>
												<TD width="30%">
													<input type="text" name="hguser" value="" class=bk>
												</TD>
												<TD align="right" width="20%">
													�û��绰����:
												</TD>
												<TD width="30%">
													<input type="text" name="telephone" value="" class=bk>
													<input type="button" class=btn value="��ѯ" onclick="relateDevice()">
												</TD>
											</TR>
											<TR bgcolor="#FFFFFF" id="tr4" STYLE="display:">
											    <TD align="right" width="20%">
													�豸���к�:
												</TD>
												<TD width="30%">
													<input type="text" name="serialnumber" value="" class=bk>
												</TD>
												<TD colspan=2>
													<input type="button" class=btn value="��ѯ" onclick="relateDeviceBySerialno()">
												</TD>
											</TR>
											<TR bgcolor="#FFFFFF">
												<TD align="right">
													�豸�б�:
													<br>
													<!-- 
													<INPUT TYPE="checkbox" onclick="selectAll('device_id')" name="device">
													ȫѡ
													-->
												</TD>
												<TD colspan="3">
													<div id="div_device" style="width:95%; height:100px; z-index:1; top: 100px; overflow:scroll">
														<span>��ѡ���豸��</span>
													</div>
												</TD>
											</TR>
										  <TR bgcolor="#FFFFFF">
											<TH align="right">��������</TH>
											<TH>����ʱ��</TH>
											<TH colspan="2">����ʱ��</TH>
										  </TR>
										  <!-- 
										  <TR bgcolor="#FFFFFF">
											<TD align="right">�ձ���</TD>
											<TD>
											-->
												<SCRIPT LANGUAGE="JavaScript">
												<!--
													//writeHour("day_h_b", 2);
												//-->
												</SCRIPT>
											<!-- 
											</TD>
											<TD colspan="2">
											 -->
												<SCRIPT LANGUAGE="JavaScript">
												<!--
													//writeHour("day_h_s", 2);
												//-->
												</SCRIPT>
											<!-- 
											</TD>
										  </TR>
										  <TR bgcolor="#FFFFFF">
											<TD align="right">�ܱ���</TD>
											<TD>
											 -->
												<SCRIPT LANGUAGE="JavaScript">
												<!--
													//writeWeek("week_w_b", 2);
													//writeHour("week_h_b", 2);
												//-->
												</SCRIPT>
											<!-- 
											</TD>
											<TD colspan="2">
											 -->
												<SCRIPT LANGUAGE="JavaScript">
												<!--
													//writeWeek("week_w_s", 2);
													//writeHour("week_h_s", 2);
												//-->
												</SCRIPT>
											<!-- 
											</TD>
										  </TR>
										   -->
										  <TR bgcolor="#FFFFFF">
											<TD align="right">�±���</TD>
											<TD><SCRIPT LANGUAGE="JavaScript">
											<!--
												writeDay("month_d_b", 2);
												writeHour("month_h_b", 2);
											//-->
											</SCRIPT></TD>
											<TD colspan="2"><SCRIPT LANGUAGE="JavaScript">
											<!--
												writeDay("month_d_s", 2);
												writeHour("month_h_s", 2);
											//-->
											</SCRIPT></TD>
										  </TR>
										  <!-- 
										  <TR bgcolor="#FFFFFF">
											<TD align="right">�걨��</TD>
											<TD>
										 	-->
												<SCRIPT LANGUAGE="JavaScript">
												<!--
												//writeMonth("year_m_b", 2);
												//writeDay("year_d_b", 2);
												//writeHour("year_h_b", 2);
												//-->
												</SCRIPT>
											<!-- 
											</TD>
											<TD colspan="2">
											-->
												<SCRIPT LANGUAGE="JavaScript">
												<!--
													//writeMonth("year_m_s", 2);
													//writeDay("year_d_s", 2);
													//writeHour("year_h_s", 2);
												//-->
												</SCRIPT>
											<!-- 
											</TD>
										  </TR>
										   -->
											<TR bgcolor="#FFFFFF">
												<TD align="right">����������</TD>
												<TD colspan="3">
													<input type="checkbox" checked name="data_type" id="dt_1">�ն�
													<input type="checkbox" checked name="data_type" id="dt_3">�ͻ�
													<input type="checkbox" checked name="data_type" id="dt_2">�û�
													<input type="checkbox" checked name="data_type" id="dt_4">����
													<input type="checkbox" checked name="data_type" id="dt_5">�ӿ�
													<input type="checkbox" checked name="data_type" id="dt_7">����
													<input type="checkbox" checked name="data_type" id="dt_6">�澯
													<input type="checkbox" checked name="data_type" id="dt_8">����
												</TD>
											</TR>
										  <TR bgcolor="#FFFFFF">
											<TD align="right">��������</TD>
											<TD colspan="3"><INPUT TYPE="radio" NAME="send_type" id="send_type_1" checked><label for="send_type_1">�Զ�����</label><INPUT TYPE="radio" NAME="send_type" id="send_type_2"><label for="send_type_2">ȷ�Ϻ���</label></TD>
										  </TR>
											<TR bgcolor="#FFFFFF">
												<TD align="right">���ձ�������</TD>
												<TD colspan="3"><INPUT TYPE="text" NAME="email"/></TD>
											</TR>
											<TR bgcolor="#FFFFFF">
												<TD colspan="4" align="right" class="green_foot">
													<INPUT TYPE="button" onclick="getPolicyOfDev()" value=" �� �� " class=btn >&nbsp;&nbsp;
													<INPUT TYPE="button" onclick="setPolicyOfDev()" value=" �� �� " class=btn >
												</TD>
											</TR>
											<TR bgcolor="#FFFFFF" id="queryResultTr" style="display:none">
												<td colspan="4">
													<span id="queryResultSpan"></span>
												</TD>
											</TR>
										</TABLE>
									</TD>
								</TR>
							</TABLE>
						</td>
					</tr>
				</table>
			</FORM>
		</TD>
	</TR>
	<TR>
		<TD HEIGHT=20>
			&nbsp;
			<IFRAME ID=childFrm SRC="" STYLE="display:none"></IFRAME>
			<IFRAME ID=childFrm1 SRC="" STYLE="display:none"></IFRAME>
			<IFRAME ID=childFrm2 SRC="" STYLE="display:none"></IFRAME>
		</TD>
	</TR>
</TABLE>
<script language="javascript">
<%if ("0".equals(checkType)){%>
	document.frm.gather_id.value = "<%=gather_id%>";
	document.all("div_vendor").innerHTML = "<%=strVendorList%>";
	document.frm.vendor_id.value = "<%=vendor_id%>";
	
	page = "showDevicetype.jsp?vendor_id=<%=vendor_id%>&defaultValue=<%=devicetype_id%>";
	document.all("childFrm1").src = page;
	page = "showDeviceVersion.jsp?vendor_id=<%=vendor_id%>&devicetype_id=<%=devicetype_id%>&defaultValue=<%=softwareversion%>";
	document.all("childFrm2").src = page;
	page = "showDevice.jsp?gather_id=<%=gather_id%>&vendor_id=<%=vendor_id%>&devicetype_id=<%=softwareversion%>&flag=paramInstanceadd_Config&refresh="+Math.random();
	document.all("childFrm").src = page;
<%}else if("1".equals(checkType)){%>
    document.all("telephone").value="<%=telephone%>";
    document.all("hguser").value="<%=hguser%>";
    selectHgw();
    ShowDialog(1);
    relateDevice();  
<%}%>
</script>
<%@ include file="../foot.jsp"%>
<SCRIPT LANGUAGE="JavaScript">
<!--
	var frm = document.forms[0];
	init();
	function init() {
		var send_type = "<%=send_type%>";
		var policy = "<%=policy%>";

		if ("0" == send_type) {
			getId("send_type_1").checked = true;
		}

		//String policy = "1,3|1_1,1_3|1_1,1_3|1_1_1,1_1_3";

		var arr1 = policy.split("|");
		if (null != arr1) {
			for (var i=0; i<arr1.length; i++) {
				var s2 = arr1[i];
				if (null == s2) {
					continue;
				}
				var arr2 = s2.split(",");
				/**
				if (i == 0) {
					if (null != arr2 && arr2.length == 2) {
						frm.day_h_b.value = arr2[0];
						frm.day_h_s.value = arr2[1];
					}
				} else if (i == 1) {
					if (null != arr2 && arr2.length == 2) {
						var s31 = arr2[0];
						var s32 = arr2[1];
						if (null != s31) {
							var arr31 = s31.split("_");
							if (null != arr31 && arr31.length == 2) {
								frm.week_w_b.value = arr31[0];
								frm.week_h_b.value = arr31[1];
							}
						}
						if (null != s32) {						
							var arr32 = s32.split("_");
							if (null != arr32 && arr32.length == 2) {
								frm.week_w_s.value = arr32[0];
								frm.week_h_s.value = arr32[1];
							}
						}
					}
				} else
				**/ 
				if (i == 2) {
					if (null != arr2 && arr2.length == 2) {
						var s31 = arr2[0];
						var s32 = arr2[1];
						if (null != s31) {
							var arr31 = s31.split("_");
							if (null != arr31 && arr31.length == 2) {
								frm.month_d_b.value = arr31[0];
								frm.month_h_b.value = arr31[1];
							}
						}
						if (null != s32) {						
							var arr32 = s32.split("_");
							if (null != arr32 && arr32.length == 2) {
								frm.month_d_s.value = arr32[0];
								frm.month_h_s.value = arr32[1];
							}
						}
					}
				}
				/** 
				else {
					if (null != arr2 && arr2.length == 2) {
						var s31 = arr2[0];
						var s32 = arr2[1];
						if (null != s31) {
							var arr31 = s31.split("_");
							if (null != arr31 && arr31.length == 3) {
								frm.year_m_b.value = arr31[0];
								frm.year_d_b.value = arr31[1];
								frm.year_h_b.value = arr31[2];
							}
						}
						if (null != s32) {						
							var arr32 = s32.split("_");
							if (null != arr32 && arr32.length == 3) {
								frm.year_m_s.value = arr32[0];
								frm.year_d_s.value = arr32[1];
								frm.year_h_s.value = arr32[2];
							}
						}
					}
				}
				**/
			}
		}
	}

	function getPolicyOfDev() {
		if (!CheckForm()) return;
		var deviceList = "";
		if (selDispStyle == "radio") {
			deviceList = "&device_id=" + did;
		} else {
 			var oselect = document.getElementsByName("device_id");
			if (null != oselect) {
				for (var i=0; i<oselect.length; i++) {
					if (oselect[i].checked) {
						deviceList += "&device_id=" + oselect[i].value;
					}
				}
			}
		}
  		page = "running_report_build_policy_save.jsp?action_type=2" + deviceList + "&refresh=" + new Date().getTime();
		//alert(page);
		document.all("childFrm").src = page;
	}

	function setPolicyOfDev() {
		if (!CheckForm()) return;
		//alert(did);
		var policy = getPolicy();
		var customize = getCustomize();
		//alert(policy);
  		page = "running_report_build_policy_save.jsp?device_id=" + did + "&customize=" + customize + "&email=" + frm.email.value + "&send_type=" + (getId("send_type_1").checked ? "0" : "1") + "&policy=" + policy +  "&action_type=1&retry=-1&refresh=" + new Date().getTime();
		//alert(page);
		document.all("childFrm").src = page;
	}
	function getCustomize() {
		var str = "";
		str += (document.getElementById("dt_1").checked ? "1" : "0") + (document.getElementById("dt_2").checked ? "1" : "0") + (document.getElementById("dt_3").checked ? "1" : "0")  + (document.getElementById("dt_4").checked ? "1" : "0") + (document.getElementById("dt_5").checked ? "1" : "0") + (document.getElementById("dt_6").checked ? "1" : "0") + (document.getElementById("dt_7").checked ? "1" : "0") + (document.getElementById("dt_8").checked ? "1" : "0");
		return str;
	}
	function getPolicy() {
		//var day = frm.day_h_b.value + "," + frm.day_h_s.value;
		//var week = frm.week_w_b.value + "_" + frm.week_h_b.value + "," + frm.week_w_s.value + "_" + frm.week_h_s.value;
		var month = frm.month_d_b.value + "_" + frm.month_h_b.value + "," + frm.month_d_s.value + "_" + frm.month_h_s.value;
		//var year = frm.year_m_b.value + "_" + frm.year_d_b.value + "_" + frm.year_h_b.value + "," + frm.year_m_s.value + "_" + frm.year_d_s.value + "_" + frm.year_h_s.value;
		//return day + "|" + week + "|" + month + "|" + year;
		return "day" + "|" + "week" + "|" + month + "|" + "year";
	}
	function getId(id) {
		return document.getElementById(id);
	}
	function setId(id, msg) {
		document.getElementById(id).innerHTML = msg;
	}

	function showId(id) {
		document.getElementById(id).style.display = "";
	}

	function hideId(id) {
		document.getElementById(id).style.display = "none";
	}
//-->
</SCRIPT>