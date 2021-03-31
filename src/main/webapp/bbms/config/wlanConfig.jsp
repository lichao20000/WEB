<%--
�������-�߼���ѯ-WLAN��
Linkage Technology (NanJing) Co., Ltd
Copyright 2008-2012. All right reserved.
Author: Alex.Yan (yanhj@lianchuang.com)
Version: 1.0.0
Date: 2009-7-8
Desc: WLAN.

Modify by zhaixf for BBMS
--%>

<%@ page language="java" contentType="text/html; charset=GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ include file="../head.jsp"%>
<HEAD>
<SCRIPT LANGUAGE="JavaScript" SRC="<s:url value='/Js/jquery.js'/>"></SCRIPT>
<SCRIPT LANGUAGE="JavaScript" SRC="<s:url value='/Js/CheckForm.js'/>"></SCRIPT>
<SCRIPT LANGUAGE="JavaScript">
<!--
$(function(){
	init();
});

//get data
function init() {

	parent.unblock();

	document.all("operResult").style.display = ""
	$("div[@id='operResult']").html("");
	$("div[@id='operResult']").html("���ݲɼ�:");
	$("div[@id='operResult']").append("<s:property value='result'/>");

	$("select[@name='apEnable'] option").each(function(){
		if($(this).val() == "<s:property value='apEnable'/>"){
			$(this).attr("selected",true);
			return;
		}
	});

	$("select[@name='hide'] option").each(function(){
		if($(this).val() == "<s:property value='hide'/>"){
			$(this).attr("selected",true);
			return;
		}
	});

	//setTimeout("clearResult()", 5000);
}

//config.
function config(lanId, lanWlanId) {
	var deviceId = $("input[@name='deviceId']");
	var apEnable = $("select[@name='apEnable']");
	var hide = $("select[@name='hide']");
	var powerlevel = $("input[@name='powerlevel']");
	var wpsKeyWord = $("input[@name='wpsKeyWord']");

	if (IsNumber2(powerlevel.val().trim(),"ģ�鹦�� ") == false) {
		powerlevel.focus();
		return;
	}

	document.all("operResult").style.display = ""
	$("div[@id='operResult']").html("");
	$("div[@id='operResult']").html("���ڱ༭WLAN...");
	var url = "<s:url value='/gwms/diagnostics/wlanACT!config.action'/>";
	$.post(url,{
		deviceId:deviceId.val(),
		lanId:lanId,
		lanWlanId:lanWlanId,
		apEnable:apEnable.val(),
		hide:hide.val(),
		powerlevel:powerlevel.val().trim(),
		wpsKeyWord:wpsKeyWord.val().trim()
	},function(ajax){

		$("div[@id='operResult']").html(ajax.split("|")[1]);
		setTimeout("clearResult()", 5000);
	});


}

// chg
function chg_auth(enc_value) {
	var beacontype = $("select[@name='beacontype']").val();
	var tr_key = document.getElementById("tr_key");
	var tr_key_sec = document.getElementById("tr_key_sec");

	if ( "None"==beacontype ) {
		tr_key.style.display = "none";
		tr_key_sec.style.display = "none";
	}else {
		tr_key.style.display = "";
		tr_key_sec.style.display = "";
	}
	parent.dyniframesize();
}

//del.
function del(lanId, lanWlanId) {

	if(!confirm("��ȷʵҪɾ����")){
		return false;
	}

	var deviceId = $("input[@name='deviceId']");

	undis();

	document.all("operResult").style.display = ""
	$("div[@id='operResult']").html("����ɾ��SSID...");
	var url = "<s:url value='/gwms/diagnostics/wlanACT!del.action'/>";
	$.post(url,{
		deviceId:deviceId.val(),
		lanId:lanId,
		lanWlanId:lanWlanId
	},function(ajax){

		$("div[@id='operResult']").html(ajax.split("|")[1]);
		setTimeout("clearResult()", 5000);
	});


}

//add.
function add() {
	var deviceId = $("input[@name='deviceId']").val();
	var lanId = $("input[@name='lanId']").val();
	var ssid = $("input[@name='ssid']").val().trim();
	var beacontype = $("select[@name='beacontype']").val();
	var key = $("input[@name='key']").val().trim();

	document.all("operResult").style.display = ""
	$("div[@id='operResult']").html("��������SSID...");
	var url = "<s:url value='/gwms/diagnostics/wlanACT!add.action'/>";
	$("tr[@id='tr002']").hide();
	parent.block();
	$.post(url,{
		deviceId:deviceId,
		lanId:lanId,
		ssid:ssid,
		beacontype:beacontype,
		key:key
	},function(ajax){
		var arr = ajax.split("|");
		$("div[@id='operResult']").html(arr[1]);
		//setTimeout("clearResult()", 5000);
		parent.dyniframesize();
		if(arr[0]=='1'){
			var url = "<s:url value='/servStrategy/ServStrategy!getStrategy.action'/>";
			var strategyId = arr[2];
			$.post(url,{
           		strategyId:strategyId
            },function(ajax){
          	   	$("div[@id='div_strategy']").html("");
				$("div[@id='div_strategy']").append(ajax);
				parent.dyniframesize();
            });
            $("tr[@id='tr002']").show();
			parent.dyniframesize();
		}
		parent.unblock();
	});
}

//edit.
function edit() {
	var deviceId = $("input[@name='deviceId']").val();
	var lanId = $("input[@name='lanId']").val();
	var lanWlanId = $("input[@name='lanWlanId']").val();
	var ssid = $("input[@name='ssid']").val().trim();
	var beacontype = $("select[@name='beacontype']").val();
	var key = $("input[@name='key']").val().trim();

	document.all("operResult").style.display = ""
	$("div[@id='operResult']").html("���ڱ༭SSID...");
	var url = "<s:url value='/gwms/diagnostics/wlanACT!edit.action'/>";
	$("tr[@id='tr002']").hide();
	parent.block();
	$.post(url,{
		deviceId:deviceId,
		lanId:lanId,
		lanWlanId:lanWlanId,
		ssid:ssid,
		beacontype:beacontype,
		key:key
	},function(ajax){
		var arr = ajax.split("|");
		$("div[@id='operResult']").html(arr[1]);
		//setTimeout("clearResult()", 5000);
		parent.dyniframesize();
		if(arr[0]=='1'){
			var url = "<s:url value='/servStrategy/ServStrategy!getStrategy.action'/>";
			var strategyId = arr[2];
			$.post(url,{
           		strategyId:strategyId
            },function(ajax){
          	   	$("div[@id='div_strategy']").html("");
				$("div[@id='div_strategy']").append(ajax);
				parent.dyniframesize();
            });
            $("tr[@id='tr002']").show();
			parent.dyniframesize();
		}
		parent.unblock();
	});
}

//type 1:add; 2:edit.
function chgForm (type, lanId, lanWlanId, ssid, beacontype, wep_key, wpa_key) {
	var subTabForm = document.getElementById("subTabForm");
	subTabForm.style.display = "block";
	$("input[@name='type']").val(type);
	if (type == 1)
	{
		document.all("actLabel").innerHTML = "����SSID";
		$("input[@name='lanId']").val(lanId);
		$("input[@name='ssid']").val("");
		$("select[@name='beacontype']").val("None");
		$("input[@name='key']").val("");
		$("input[@name='key_sec']").val("");
		$("input[@name='key_old']").val("");
		tr_key.style.display = "none";
		tr_key_sec.style.display = "none";
	}
	else
	{
		document.all("actLabel").innerHTML = "�༭SSID";

		$("input[@name='ssid']").val(ssid);
		$("input[@name='lanId']").val(lanId);
		$("input[@name='lanWlanId']").val(lanWlanId);
		$("select[@name='beacontype']").val(beacontype);

		if ("None" == beacontype)
		{
			tr_key.style.display = "none";
			tr_key_sec.style.display = "none";
		}
		else if("Basic" == beacontype)
		{
			tr_key.style.display = "";
			tr_key_sec.style.display = "";
			$("input[@name='key']").val(wep_key);
			$("input[@name='key_old']").val(wep_key);
			$("input[@name='key_sec']").val(wep_key);
		}
		else
		{
			tr_key.style.display = "";
			tr_key_sec.style.display = "";
			$("input[@name='key']").val(wpa_key);
			$("input[@name='key_old']").val(wpa_key);
			$("input[@name='key_sec']").val(wpa_key);
		}
	}
	parent.dyniframesize();
}


function editWlanConn() {
	var type = $("input[@name='type']").val();
	var ssid = $("input[@name='ssid']");
	var beacontype = $("select[@name='beacontype']").val();
	var key = $("input[@name='key']");
	var key_sec = $("input[@name='key_sec']");

	if (IsNull(ssid.val().trim(),"SSID ") == false)
	{
		ssid.focus();
		return;
	}

	if (beacontype != 'None')
	{
		if (IsNull(key.val().trim(),"��Կ ") == false)
		{
			key.focus();
			return;
		}

		if (key.val().trim().length != 10)
		{
			alert("��ԿΪʮλ���֡���ĸ");
			key.focus();
			return;
		}

		if (IsNull(key_sec.val().trim(),"ȷ����Կ ") == false)
		{
			key_sec.focus();
			return;
		}

		if (key.val().trim() != key_sec.val().trim())
		{
			alert("��Կ����һ��");
			key.focus();
			return;
		}
	}


	if (type == 1)
	{
		add();
	}
	else
	{
		edit();
	}

}


function undis() {
	var subTabForm = document.getElementById("subTabForm");
	subTabForm.style.display = "none";
	parent.dyniframesize();
}

function clearResult() {
	$("div[@id='operResult']").html("");
	document.all("operResult").style.display = "none"
}


//-->
</SCRIPT>
</HEAD>


<TABLE border=0 cellspacing=0 cellpadding=0 width="100%">
	<tr bgcolor=#ffffff>
		<td align=right>
			<div id="operResult" style='width:20%;display:none;background-color:#33CC00'></div>
		</td>
	</tr>
	<TR>
		<TD bgcolor=#999999>
			<TABLE border=0 cellspacing=1 cellpadding=2 width="100%" >
			<FORM NAME="frm1" METHOD="post">
				<TR>
					<TH colspan="9">WLAN����״̬</TH>
				</TR>
				<TR bgcolor="#FFFFFF">
					<TD colspan="1" align="right">WLAN����</TD>
					<TD colspan="1">
						<SELECT NAME="apEnable">
							<OPTION VALUE="1" SELECTED>����
							<OPTION VALUE="0">�ر�
						</SELECT>&nbsp;<font color="red">*</font></TD>
					<TD colspan="2" align="right">SSID�Ƿ�����</TD>
					<TD colspan="2">
						<SELECT NAME="hide" readonly>
							<OPTION VALUE="1" SELECTED>��
							<OPTION VALUE="0">��
						</SELECT>&nbsp;<font color="red">*</font></TD>
					<TD colspan="1" align="right">WPS����ؼ���</TD>
					<TD colspan="2">
						<INPUT TYPE="text" NAME="wpsKeyWord" size="8" maxlength="8" value="<s:property value='wpsKeyWord' default=''/>" readonly>
					</TD>
				</TR>
				<TR bgcolor="#FFFFFF">
					<TD colspan="1" align="right">����ģ�鹦�ʼ���</TD>
					<TD colspan="1">
						<INPUT TYPE="text" NAME="powerlevel"  size="8" maxlength="8" value="<s:property value='powerlevel'/>">&nbsp;<font color="red">*</font></TD>
					<TD colspan="2" align="right">��ǰ����</TD>
					<TD colspan="2"><s:property value='powervalue'/></TD>
					<TD colspan="3"></TD>
				</TR>
				<TR bgcolor="#FFFFFF">
					<TD colspan="9" class="green_foot" align=right>
						<INPUT NAME="deviceId" TYPE="hidden"  value="<s:property value='deviceId'/>">
						<input type="button" value=" �� �� " class="jianbian" onclick="javascript:config(<s:property value='lanId' default='1'/>,<s:property value='lanWlanId' default='1'/>);">&nbsp;&nbsp;
						<input type="button" value=" �� �� " class="jianbian" onclick="javascript:chgForm(1,<s:property value='lanId' default='1'/>,'0','','','','');">&nbsp;&nbsp;
						<input type="reset" value=" �� �� " class="jianbian">
					</TD>
				</TR>
			</FORM>
				<TR>
					<TH colspan="1" width="10%">ID</TH>
					<TH colspan="1" width="20%">SSID</TH>
					<TH colspan="1" width="10%">SSID����</TH>
					<TH colspan="1" width="5%">�㲥</TH>
					<TH colspan="1" width="10%">����ģʽ</TH>
					<TH colspan="1" width="10%">״̬</TH>
					<TH colspan="1" width="10%">�����ŵ�</TH>
					<TH colspan="1" width="10%">��ǰ�ŵ�</TH>
					<TH colspan="1" width="15%">����</TH>
				</TR>
				<s:if test="list.size == 0">
					<TR bgcolor="#FFFFFF">
						<TD colspan="9" class="green_foot" align=left>��������</TD>
					</TR>
				</s:if>
				<s:else>
					<s:iterator var="list" value="list" status="row">
						<TR bgcolor="#FFFFFF">
							<TD align="center"><s:property value="#list.lanId"/>-<s:property value="#list.lanWlanId"/></TD>
							<TD><s:property value="#list.ssid" default=""/></TD>
							<TD align="center">
								<s:if test="#list.enable == 1">
									��
								</s:if>
								<s:else>
									��
								</s:else>
							</TD>
							<TD align="center">
								<s:if test="#list.radioEnable == 1">
									��
								</s:if>
								<s:else>
									��
								</s:else>
							</TD>
							<TD align="center">
								<s:if test="#list.beacontype == 'Basic'">
									WEP
								</s:if>
								<s:else>
									<s:property value="#list.beacontype"/>
								</s:else>
							</TD>
							<TD align="center"><s:property value="#list.status"/></TD>
							<TD align="center"><s:property value="#list.channel"/></TD>
							<TD align="center">
								<s:if test="#list.channelInUse == '' || #list.channelInUse == 'null'">
									<s:property value="#list.channel"/>
								</s:if>
								<s:else>
									<s:property value="#list.channelInUse"/>
								</s:else>
							</TD>
							<TD align="center"><IMG SRC="../../images/edit.gif" WIDTH="14" HEIGHT="12" BORDER="0" ALT="�༭" onclick="chgForm(2,<s:property value='#list.lanId' default=''/>,<s:property value='#list.lanWlanId' default='1'/>,'<s:property value='#list.ssid' default='1'/>', '<s:property value='#list.beacontype' default='None'/>', '<s:property value='#list.wepKey' default=''/>', '<s:property value='#list.wpaKey' default=''/>')" onMouseOver="this.style.cursor='hand';">&nbsp;&nbsp;<IMG SRC="../../images/del.gif" WIDTH="14" HEIGHT="12" BORDER="0" ALT="ɾ��"  onclick="del(<s:property value='#list.lanId' default='1'/>,<s:property value='#list.lanWlanId' default='1'/>)" onMouseOver="this.style.cursor='hand';">&nbsp;&nbsp;<IMG SRC="../../images/add.gif" WIDTH="14" HEIGHT="12" BORDER="0" ALT="���"  onclick="chgForm(1,<s:property value='#list.lanId' default='1'/>,'','','','','')" onMouseOver="this.style.cursor='hand';"></TD>
						</TR>
					</s:iterator>
				</s:else>
			</TABLE>
		</TD>
	</TR>
	<FORM NAME="frm2" METHOD="post">
	<TR id="subTabForm" style="display:none">
		<TD bgcolor=#999999 colspan="9">
			<TABLE border=0 cellspacing=1 cellpadding=1 width="100%">
				<TR>
					<TH align="center" colspan=4 valign="center"><B><span id="actLabel" onclick="undis();"  style="cursor:hand">�޸�</span></B></TH>
				</TR>
				<TR bgcolor="#FFFFFF">
					<TD width="40%" class=column align="right">SSID</TD>
					<TD width="60%" colspan=3>
						<input type="hidden" name="ssid_old">
						<INPUT TYPE="text" NAME="ssid" maxlength=25 class=bk size=25>
						<font color="red">*</font>
					</TD>
				</TR>
				<TR bgcolor="#FFFFFF">
					<TD class=column align="right">����ģʽ</TD>
					<TD colspan=3>
						<input type="hidden" name="beacontype_old">
						<SELECT name="beacontype" class="bk" onchange="chg_auth(this.value)">
							<OPTION value="None">None</OPTION>
							<OPTION value="Basic">WEP</OPTION>
							<OPTION value="WPA">WPA</OPTION>
							<OPTION value="11i">11i</OPTION>
						</SELECT>
					</TD>
				</TR>
				<TR bgcolor="#FFFFFF" id="tr_key" style="display:none">
					<TD align="right" >��Կ</TD>
					<TD >
						<input type="hidden" name="key_old">
						<input type="text" name="key" class="bk" maxlength=10 size=10>
						<font color="red">* ������ʮλ���֡���ĸ��</font>
					</TD>
				</TR>
				<TR bgcolor="#FFFFFF" id="tr_key_sec" style="display:none">
					<TD align="right" >ȷ����Կ</TD>
					<TD >
						<input type="text" name="key_sec" class="bk" maxlength=10 size=10>
					<font color="red">* ������ʮλ���֡���ĸ��</font>
					</TD>
				</TR>
				<TR>
					<TD colspan="4" align="right" class=green_foot>
						<input type="hidden" name="type">
						<input type="hidden" name="lanId">
						<input type="hidden" name="lanWlanId">
						<INPUT TYPE="button" value=" �� �� " onclick="editWlanConn()" class=jianbian>
						&nbsp;&nbsp;<input type="reset" value=" �� �� " class="jianbian">
					</TD>
				</TR>
				<TR bgcolor="#FFFFFF" id="tr002" style="display: none">
					<td colspan="4" valign="top" class=column>
						<div id="div_strategy"
							style="width: 100%; z-index: 1; top: 100px">
						</div>
					</td>
				</TR>
			</TABLE>
		</td>
	</tr>
	</FORM>
</TABLE>
