<%--
FileName	: softUpgrade.jsp
Date		: 2007��5��10��
Desc		: �������.
--%>
<%@ include file="../timelater.jsp"%>
<%@ page contentType="text/html;charset=gbk"%>

<jsp:useBean id="versionManage" scope="request" class="com.linkage.litms.software.VersionManage" />

<jsp:useBean id="StrategyBean" scope="request" class="com.linkage.litms.paramConfig.StrategyBean" />

<%
	request.setCharacterEncoding("gbk");
	//com.linkage.litms.resource.DeviceAct deviceAct = new com.linkage.litms.resource.DeviceAct();
	//String gatherList = deviceAct.getGatherList(session, "", "", true);
	
	//�豸�ͺ�������
	//String deviceModel = versionManage.getDeviceTypeList("", true);

	//String file_path = versionManage.getURLPath(2);
	
	//�豸����
	//String strVendorList = deviceAct.getVendorList(true, "", "");

	//ȡ��"�����ļ���/tab_vercon_file"���������е������ļ���
	//String all_ver_con_files = versionManage.getAllConfFileNames();
	
	//ȡ�õ�ǰʱ��
	//String current_Time = versionManage.getCurrentTime();
	
	//String ssid2 = 
	//�ȴ����豸���е����ݣ����б�Ҫ���������ڴ�
	StrategyBean.dealDevMap();
	
	//String gw_type = request.getParameter("gw_type");

%>
<%@ include file="../head.jsp"%>
<link href="../css/css_green.css" rel="stylesheet" type="text/css">
<style type="text/css">
<!--
.data_table {
	border-collapse: collapse;
}
.data_table th {
	border:1px solid #999999;
}
-->
</style>
<SCRIPT LANGUAGE="JavaScript">
	function selectAll(elmID){
		t_obj = document.all(elmID);
		if(!t_obj) return;
		obj = event.srcElement;
		if(obj.checked){
			if(typeof(t_obj) == "object" ) {
				if(typeof(t_obj.length) != "undefined") {
					for(var i=0; i<t_obj.length; i++){
						if (t_obj[i].disabled == false) {
							t_obj[i].checked = true;
						}
					}
				} else {
					if (t_obj[i].disabled == false) {
							t_obj[i].checked = true;
						}
				}
			}
		
		}else{
			if(typeof(t_obj) == "object" ) {
				if(typeof(t_obj.length) != "undefined") {
					for(var i=0; i<t_obj.length; i++){
						t_obj[i].checked = false;
					}
				} else {
					t_obj.checked = false;
				}
			}
		}
	}
	
	function CheckForm(){
		var hguser=document.all("hguser").value;
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
	    if(checkType==1&&""==hguser)
		{
		  alert("����д�û�����˺ţ�");
		  document.all("hguser").focus();
		  return false;
		}
	    if(hguser.length < 4)
		{
		  alert("���������롰ǰ��λ���û�����˺ţ�");
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
				num = 1;
			}
		}else{
			for(var i=0;i<oselect.length;i++){
				if(oselect[i].checked){
					num++;
				}
			}
		}
		if(num ==0){
			alert("��ѡ���豸��");
			return false;
		}
		var obj = document.frm;	
		if (("" == obj.vpiid.value)) {
		  alert("������VPI��");
		  document.all("vpiid").focus();
		  return false;
		}
		if (("" == obj.vciid.value)) {
		  alert("������VCI��");
		  document.all("vciid").focus();
		  return false;
		}
	}
	
	function selectChg() {
		if (document.all("url_path").value == -1) {
			document.all("dir_id").value = "";
			document.all("hid_url_path").value="";
		} else {
			var _url = document.all("url_path").value;			
			var _urlArr = _url.split("|");
			document.all("hid_url_path").value = _urlArr[0];	
			document.all("dir_id").value = _urlArr[1];	
		}
	}
	
function relateDevice()
{
   var hguser=document.all("hguser").value;
   var checkradios = document.all("checkType");
   var isOpened_user = document.all("isOpened_user");
   
   var checkType = "";
   var isOpened_userValue="";
   for(var i=0;i<checkradios.length;i++)
   {
     if(checkradios[i].checked)
	 {
	   checkType = checkradios[i].value;
	   break;
	 }
   }
   if(hguser.length < 4)
	{
	  alert("���������롰ǰ��λ���û�����˺ţ�");
	  document.all("hguser").focus();
	  return false;
	}
   if(1==checkType&&""==hguser)
   {
      alert("����д�û�����˺ţ�");
      document.all("hguser").focus();
   }
   
   else if(1==checkType) {
      for(var i=0;i<isOpened_user.length;i++) {
	      if(isOpened_user[i].checked)
		  {
		    isOpened_userValue = isOpened_user[i].value;
		    break;
		  }
      }
      if (isOpened_userValue == 1) {
      	showMsgDlg("δ����");
      } else if (isOpened_userValue == 2) {
      	showMsgDlg("�ѿ�������δ�ɹ�");
      } else {
      	showMsgDlg("�ѿ��������ѳɹ�");
      } 
      var page="";
      page="configiTV_strategy_showDev.jsp?hguser="+hguser+"&isOpened_userValue="+isOpened_userValue+"&flag=user&refresh="+Math.random();
      document.all("childFrm1").src = page;
      //alert("A:" + page);
   }
   
}

function relateDeviceBySerialno()
{
   var serialnumber=document.all("serialnumber").value;
   var checkradios = document.all("checkType");
    var isOpened_sn = document.all("isOpened_sn");
    var isOpened_snValue="";
   var checkType = "";
   for(var i=0;i<checkradios.length;i++) {
     if(checkradios[i].checked) {
	   checkType = checkradios[i].value;
	   break;
	 }
   }
   if(checkType==2 && serialnumber=="")
   {
      alert("����д�豸���кţ�");
      document.all("serialnumber").focus();
   }
   else if(checkType==2) {
      for(var i=0;i<isOpened_sn.length;i++) {
	      if(isOpened_sn[i].checked)
		  {
		    isOpened_snValue = isOpened_sn[i].value;
		    break;
		  }
      }
      var page="";
      page="configiTV_strategy_showDev.jsp?serialnumber="+serialnumber+"&isOpened_snValue="+isOpened_snValue+"&flag=sn&refresh="+Math.random();
      document.all("childFrm1").src = page;
      //alert(page);
   }
   
}

function ShowDialog(param)
{
  //�����û�����ѯ
  if(param==1)
  {
     tr1.style.display="";
     tr2.style.display="none";
  }
  
  //�����豸���к�����ѯ
  if(param==2)
  {
     tr1.style.display="none";
     tr2.style.display="";
  }
}

function chg_auth(enc_value) {
	if (enc_value == "Basic") {
		tr_encrypt_wep_1.style.display="";
		tr_encrypt_wpa_1.style.display="none";
	} else if (enc_value == "WPA") {
		tr_encrypt_wep_1.style.display="none";
		tr_encrypt_wpa_1.style.display="";
	} else {
		tr_encrypt_wep_1.style.display="none";
		tr_encrypt_wpa_1.style.display="none";
	}
}
function showMsgDlg(strMsg){
	strMsg = "���ڲ�ѯ��"+strMsg+"�����豸�����Ժ�......";
	w = document.body.clientWidth;
	h = document.body.clientHeight;

	l = (w-250)/2;
	t = (h-60)/2;
	PendingMessage.style.left = l;
	PendingMessage.style.top  = t;
	PendingMessage.style.display="";
	document.all.txtLoading.innerText=strMsg;
}

function closeMsgDlg(){
	PendingMessage.style.display="none";
}
</SCRIPT>
<div id="PendingMessage"
	style="position:absolute;z-index:3;top:200px;left:200px;width:350;height:60;border-width:1;border-style:ridge;background-color:#eeeeee;padding-top:10px;display:none">
<center>
<table border="0">
	<tr>
		<td valign="middle"><img src="../images/cursor_hourglas.gif"
			border="0" WIDTH="30" HEIGHT="30"></td>
		<td>&nbsp;&nbsp;</td>
		<td valign="middle">
			<span id=txtLoading style="font-size:14px;font-family: ����"></span>
		</td>
	</tr>
</table>
</center>
</div>
<%@ include file="../toolbar.jsp"%>

<TABLE border="0" cellspacing="0" cellpadding="0" width="100%">
	<TR>
		<TD HEIGHT=20>
			&nbsp;
		</TD>
	</TR>
	<TR>
		<TD>
			<FORM NAME="frm" METHOD="post" ACTION="configiTV_save.jsp" onsubmit="return CheckForm()">
				<table width="98%" border="0" align="center" cellpadding="0" cellspacing="0" class="text">
					<tr>
						<td>
						<table width="100%" height="30" border="0" cellspacing="0" cellpadding="0" class="green_gargtd">
							<tr>
								<td width="162" align="center" class="title_bigwhite" nowrap>
									��������
								</td>
								<td nowrap>
									<img src="../images/attention_2.gif" width="15" height="12">
									ע����ѡ����豸������������IPTV��
			                          <input type="radio" value="1" onclick="ShowDialog(this.value)" name="checkType" checked style="display:none">
			                          <input type="radio" value="2" onclick="ShowDialog(this.value)" name="checkType" style="display:none">
								</td>
							</tr>
						</table>
						</td>
					</tr>
					<tr>
						<td>
							<TABLE width="100%" border=0 cellspacing=0 cellpadding=0 align="center">
								<TR>
									<TD bgcolor=#999999>
										<TABLE border=0 cellspacing=1 cellpadding=2 width="100%">
											<TR>
												<TH colspan="4" align="center">
													iTV�������ã����Է�ʽ��
												</TH>
											</TR>
											<TR bgcolor="#FFFFFF" id="tr1" STYLE="display:">
											    <TD align="right" width="15%">
													�û�����˺�:
												</TD>
												<TD width="30%">
													<input type="text" name="hguser" value="" class=bk size="15">&nbsp;�����������˺ŵ�ǰ��λ
												</TD>
												<TD colspan=2>
													<!--<input type="radio" name="isOpened_user" value="0" onclick="" checked>ȫ��-->
													<input type="radio" name="isOpened_user" value="1" onclick="" checked>δ����
													<input type="radio" name="isOpened_user" value="2" onclick="">�ѿ���(δ�ɹ�)
													<input type="radio" name="isOpened_user" value="3" onclick="">�ѿ���(�ѳɹ�)
													<input type="button" class=btn value=" �� ѯ " onclick="relateDevice()">
												</TD>
											</TR>
											<TR bgcolor="#FFFFFF" id="tr2" STYLE="display:none">
											    <TD align="right" width="15%">
													�豸���к�:
												</TD>
												<TD width="30%">
													<input type="text" name="serialnumber" value="" class=bk size="30">
												</TD>
												<TD colspan=2>
													<!-- <input type="radio" name="isOpened_sn" value="0" onclick="" checked>ȫ�� -->
													<input type="radio" name="isOpened_sn" value="1" onclick="">δ����
													<input type="radio" name="isOpened_sn" value="2" onclick="">�ѿ���(ʧ��)
													<input type="radio" name="isOpened_sn" value="3" onclick="">�ѿ���(�ɹ�)
													<input type="button" class=btn value=" �� ѯ " onclick="relateDeviceBySerialno()">
												</TD>
											</TR>
											<TR bgcolor="#FFFFFF">
												<TD align="right">
													�豸�б�:
													<br>
													<INPUT TYPE="checkbox" onclick="selectAll('device_id')" name="device">
													ȫѡ
												</TD>
												<TD colspan="3">
													<div id="div_device" style="width:100%; height:200px; z-index:1; top: 100px; overflow:scroll">
													</div>
												</TD>
											</TR>
											<TR bgcolor="#FFFFFF">
												<TD align="right" width="15%">
													ҵ������:
												</TD>
												<TD width="30%">
													IPTV�Žӿ���
													<input type="hidden" name="serv_type_id" value="11">
													<input type="hidden" name="service_id" value="1101">
												</TD>
												<TD align="right" width="15%">
													�󶨶˿�:
												</TD>
												<TD width="30%">
												LAN2��WLAN2
												<input type="hidden" name="bind_port" value="InternetGatewayDevice.LANDevice.1.LANEthernetInterfaceConfig.2,InternetGatewayDevice.LANDevice.1.WLANConfiguration.2">
												</TD>
											</TR>
											<TR bgcolor="#FFFFFF">
												<TD align="right" width="15%">
													SSID2:
												</TD>
												<TD>
													iTV + ���к������λ
													<input type="hidden" name="ssid2" value="">
												</TD>
												
												<TD align="right" width="15%">
													���Է�ʽ:
												</TD>
												<TD width="30%">
													<SELECT name="strategy_type" class="bk">
													<OPTION value="4">�豸�´����ӵ�ITMSʱ�Զ�����</OPTION>
													<OPTION value="0">����ִ��</OPTION>
													</SELECT>
													<!-- <input type="hidden" name="strategy_type" value="4"> -->
												</TD>
												
											</TR>
											<TR bgcolor="#FFFFFF">
												<TD align="right" width="15%">
													VPI/VCI��
												</TD>
												<TD colspan="" width="30%">
													<input type="text" name="vpiid" size="2"
														value="8" class="bk">&nbsp;/&nbsp;<input type="text" name="vciid" size="3"
														value="85" class="bk">
													<font color=red>*</font>
												</TD>
												
												<TD align="right" width="15%">
													��֤��ʽ:
												</TD>
												<TD width="30%" nowrap>
													<SELECT name="Au_way" class="bk" onchange="chg_auth(this.value)">
													<OPTION value="None">None</OPTION>
													<OPTION value="Basic">Basic</OPTION>
													<OPTION value="WPA">WPA</OPTION>
													</SELECT>
												</TD>
											</TR>
											<TR id="tr_encrypt_wep_1" bgcolor="#FFFFFF" style="display:none">
												<TD align="right" width="15%">
													WEPģʽ��Կ:
												</TD>
												<TD colspan="3">
													<input type="text" name="WEP_pw" value="1111111111" class="bk">
												</TD>
											</TR>
											<TR id="tr_encrypt_wpa_1" bgcolor="#FFFFFF" style="display:none">
												
												<TD align="right" width="15%">
													WPAģʽ��Կ:
												</TD>
												<TD colspan="3">
													<input type="text" name="WPA_pw" value="1111111111" class="bk">
												</TD>
												
											</TR>
											<TR>
												<TD colspan="4" align="right" CLASS="green_foot">
													<INPUT TYPE="submit" value=" �� �� " class=btn>
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
		</TD>
	</TR>
</TABLE>
<%@ include file="../foot.jsp"%>
<%
	//deviceAct = null;
%>
