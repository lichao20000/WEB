<%@ page language="java" contentType="text/html; charset=GBK" pageEncoding="GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="lk" uri="/linkage"%>
<%@page import="com.linkage.litms.LipossGlobals"%>
<%@taglib prefix="ms" uri="/ailk-itms-web-tags"%>
<%
	String telecom = LipossGlobals.getLipossProperty("telecom");
%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<title>BSSҵ���ѯ</title>
<link rel="stylesheet" href="<s:url value="/css3/c_table.css"/>" type="text/css">
<link rel="stylesheet" href="<s:url value="/css3/global.css"/>" type="text/css">
<script type="text/javascript" src="<s:url value="/Js/jquery.js"/>"></script>
<SCRIPT LANGUAGE="JavaScript" SRC="../../Js/CheckFormForm.js"></SCRIPT>
<script type="text/javascript" src="<s:url value="/Js/My97DatePicker/WdatePicker.js"/>"></script>
<script type="text/javascript">
function query()
{
	configInfoClose();
	bssSheetClose();
	var isRealtimeQuery = $.trim($("input[@name='isRealtimeQuery']").val());
	var uname = $.trim($("input[@name='username']").val());
	var cityId = $.trim($("select[@name='cityId']").val());
	if(isRealtimeQuery == "true"){
		if(uname == null || uname.length == 0){
			$("input[@name='username']").focus();
			alert("������LOID���û��ʺ���Ϣ���в�ѯ");
			return false;
		}
   	}
    if(cityId == "-1"){
         alert("��ѡ������");
         return false;
    }
    $("input[@name='username']").val(Trim($("input[@name='username']").val()));
	document.selectForm.submit();
}


function ToExcel()
{
	var usernameType=$("select[@name='cityId']").val();
	var username=Trim($("input[@name='username']").val());
	var cityId=$("select[@name='cityId']").val();
	var servTypeId=$("select[@name='servTypeId']").val();
	var openstatus=$("select[@name='openstatus']").val();
	var startOpenDate=$("input[@name='startOpenDate']").val();
	var endOpenDate=$("input[@name='endOpenDate']").val();
	var devicetype=$("select[@name='devicetype']").val();
	var user_type_id=$("select[@name='user_type_id']").val();
	var url = "<s:url value='/itms/service/bssSheetServ!getBssSheetServInfoCount.action'/>";
	var gw_type = window.document.getElementsByName("gw_type")[0].value;
	$.post(url, {
		usernameType : usernameType,
		username : username,
		cityId : cityId,
		servTypeId : servTypeId,
		openstatus : openstatus,
		startOpenDate : startOpenDate,
		endOpenDate : endOpenDate,
		devicetype : devicetype,
		user_type_id: user_type_id,
		gw_type : gw_type
		}, function(ajax) {
			if(ajax>100000){
   				alert("������̫��֧�ֵ��� ");
   				return;
			}
			else{
				var mainForm = document.getElementById("form");
				mainForm.action="<s:url value='/itms/service/bssSheetServ!getBssSheetServInfoExcel.action'/>";
				mainForm.submit();
	    		mainForm.action="<s:url value='/itms/service/bssSheetServ!getBssSheetServInfo.action'/>";
			}
	});
}

function queryServerType()
{
   var serValue = document.selectForm.servTypeId.value;
   if(serValue=="14"){
		this.tr_voipProtocalType.style.display="";
   }else if(serValue=="10"){
	    this.tr_wanProtocalType.style.display="";
   }else{
	    this.tr_wanProtocalType.style.display="none";
   		this.tr_voipProtocalType.style.display="none";
   		document.selectForm.voipProtocalType.value="";
   		document.selectForm.wanProtocalType.value="";
   }
}

function configInfoClose()
{
	$("td[@id='configInfoEm']").hide();
	$("td[@id='configInfo']").hide();
}

function configDetailClose()
{
	$("td[@id='bssSheetDetail']").hide();
}

function bssSheetClose()
{
	$("td[@id='bssSheetInfo']").hide();
}

function configDetailInfo(strategyId,deviceSN,servTypeId)
{
	var gw_type = document.getElementsByName("gw_type")[0].value;
	servTypeId = servTypeId ? servTypeId : "";
	var page = "<s:url value='/itms/service/bssSheetServ!getConfigDetail.action'/>?gw_type=" +gw_type+ "&strategyId="+strategyId+"&deviceSN="+deviceSN
			+ "&servTypeId="+servTypeId;
	window.open(page,"","left=20,top=20,width=600,height=450,resizable=yes,scrollbars=yes");
}

function solutions(result_id,deviceSN)
{
	var url = "<s:url value='/itms/service/bssSheetServ!getSolution.action'/>?&deviceSN="+deviceSN+"&result_id="+result_id;
	/*
	var tempForm = document.createElement("fm");
	tempForm.id="tempForm1";    
	tempForm.method="post";    
	tempForm.action=url;
	
	var hideInput1 = document.createElement("input");
	hideInput1.type="hidden";    
	hideInput1.name= "solution"  
	hideInput1.value= solution;  
	tempForm.appendChild(hideInput1);
	tempForm.attachEvent("onsubmit",function(){ openWindow();});
 	tempForm.fireEvent("onsubmit");  
	tempForm.submit();  
	document.body.removeChild(tempForm);
	*/
	//var obj= window.showModelessDialog(url,"","dialogHeight=350px;dialogwidth=410px;dialogLeft=260;dialogTop=100;help=no;resizable=o;status=no;scrollbars=no;"); 
	window.open(url,"","left=50,top=50,width=600,height=450,resizable=yes,scrollbars=yes");
}

function resetData(userId, deviceId, oui, deviceSN, servTypeId, servstauts) 
{
	if (deviceId == "") {
		alert("�û�δ���豸�����Ȱ��豸���ټ��");
		return;
	}
	if (confirm('���¼����ǽ���ҵ����Ϊδ��״̬��ȷʵҪ������?')) {
		var url = "<s:url value='/itms/service/bssSheetServ!callPreProcess.action'/>";
		$.post(url, {
			userId : userId,
			deviceSN : deviceSN,
			deviceId : deviceId,
			servTypeId : servTypeId,
			servstauts : servstauts,
			oui : oui
		}, function(ajax) {
			if (ajax == "1") {
				alert("��Ԥ���ɹ���");
				query();
			} else if (ajax == "-1") {
				alert("����Ϊ�գ�");
			} else if (ajax == "-2") {
				alert("��Ԥ��ʧ�ܣ�");
			}
		});
		//$("td[@id='temp123']").html("<font color='red'>δ��</font>");
		//$('#resultStr',window.parent.document).html("<font color='red'>02586588146���¼���ɹ���</font>");
	}
}

function configLog(deviceSN,deviceId,servTypeId,servstauts,wanType)
{
	var gw_type = document.getElementsByName("gw_type")[0].value;
	var page = "<s:url value='/itms/service/bssSheetServ!getConfigLogInfo.action'/>?"
		+ "deviceSN=" + deviceSN
		+ "&deviceId=" + deviceId
		+ "&servTypeId=" + servTypeId
		+ "&servstauts=" + servstauts
		+ "&wanType=" + wanType
		+ "&gw_type=" + gw_type;
	window.open(page,"","left=20,top=20,width=700,height=350,resizable=yes,scrollbars=yes");
}

//������Ҫѡ��߼���ѯѡ��
function ShowDialog(leaf)
{
	//pobj = obj.offsetParent;
	oTRs = document.getElementsByTagName("TR");
	var m_bShow;
	var setvalueTemp = 0;
	for(var i=0; i<oTRs.length; i++){
		if(oTRs[i].leaf == leaf){
			m_bShow = (oTRs[i].style.display=="none");
			oTRs[i].style.display = m_bShow?"":"none";
		}
	}
	if(m_bShow){
		setvalueTemp = "1";
	}
	setValue(setvalueTemp);
}

function setValue()
{
	theday=new Date();
	day=theday.getDate();
	month=theday.getMonth()+1;
	year=theday.getFullYear();
	
	$("input[@name='time_start']").val(year+"-"+month+"-"+day+" 00:00:00");
	$("input[@name='time_end']").val(year+"-"+month+"-"+day+" 23:59:59");
	
}

//** iframe�Զ���Ӧҳ�� **//
//������ϣ������ҳ��߶��Զ������߶ȵ�iframe�����Ƶ��б�
//�ö��Ű�ÿ��iframe��ID�ָ�. ����: ["myframe1", "myframe2"]������ֻ��һ�����壬���ö��š�
//����iframe��ID
var iframeids=["dataForm"];

//����û����������֧��iframe�Ƿ�iframe���� yes ��ʾ���أ�no��ʾ������
var iframehide="yes";

function dyniframesize() 
{
	var dyniframe=new Array();
	for (var i=0; i<iframeids.length; i++)
	{
		if (document.getElementById)
		{
			//�Զ�����iframe�߶�
			dyniframe[dyniframe.length] = document.getElementById(iframeids[i]);
			if (dyniframe[i] && !window.opera)
			{
     			dyniframe[i].style.display="block";
     			//����û����������NetScape
     			if (dyniframe[i].contentDocument && dyniframe[i].contentDocument.body.offsetHeight)
      				dyniframe[i].height = dyniframe[i].contentDocument.body.offsetHeight; 
      			//����û����������IE
     			else if (dyniframe[i].Document && dyniframe[i].Document.body.scrollHeight) 
      				dyniframe[i].height = dyniframe[i].Document.body.scrollHeight;
   			 }
   		}
		//�����趨�Ĳ���������֧��iframe�����������ʾ����
		if ((document.all || document.getElementById) && iframehide=="no")
		{
			var tempobj=document.all? document.all[iframeids[i]] : document.getElementById(iframeids[i]);
    		tempobj.style.display="block";
		}
	}
}

$(function(){
	//setValue();
	dyniframesize();
});

$(window).resize(function(){
	dyniframesize();
});

<%-- ֻ�е���ͥ�û��������û��ں�ʱ����չʾ�û����ͺ��ն����Ͳ�ѯ���� --%>
<s:if test="gw_type == 3">
$(function(){
	initSpecOptions();
	changeCustomerType();
	$("#cust_type_id").bind("change", changeCustomerType);
});

var itmsOptions = new Array();
var bbmsOptions = new Array();
function initSpecOptions()
{
	var itmsIndex = 0;
	var bbmsIndex = 0;
	itmsOptions[itmsIndex++] = new Option("==��ѡ��==", "");
	bbmsOptions[bbmsIndex++] = new Option("==��ѡ��==", "");
	var specSelect = $("#spec_id").get(0);
	for (var i = 0; i < specSelect.length; i++)
	{
		var option = specSelect.options[i];
		var spIndex = option.value.indexOf(",");
		var suffix = option.value.substring(0, spIndex);
		option.value = option.value.substring(spIndex + 1);
		if ("2" == suffix){
			// itms
			itmsOptions[itmsIndex++] = option;
		}else{
			bbmsOptions[bbmsIndex++] = option;
		}
	}
}

<%-- ���¿ͻ����ͣ���ʵ�ֿͻ�������ͻ��ն˹���� --%>
function changeCustomerType() 
{
	var specSelect = $("#spec_id").get(0);
	specSelect.length = 0;
	if ("1" == $("#cust_type_id option:selected").val()){
		// itms
		for (var i = 0; i < itmsOptions.length; i++){
			specSelect.options[i] = itmsOptions[i];
		}
	}else{
		for (var i = 0; i < bbmsOptions.length;i++){
			specSelect.options[i] = bbmsOptions[i];
		}
	}
	specSelect.selectedIndex = 0;
}
</s:if>
</script>
</head>

<body>
<form id="form" name="selectForm" target="dataForm"
	action="<s:url value='/itms/service/bssSheetServ!getBssSheetServInfo.action'/>" >
<input type="hidden" name="selectType" value="0" />
<input type="hidden" name="gw_type" value='<s:property value="gw_type" />' />
<input type="hidden" name="netServUp" value='<s:property value="netServUp" />' />
<input type="hidden" name="isRealtimeQuery" value='<s:property value="isRealtimeQuery" />' />
	<table>
		<tr>
			<td HEIGHT=20>&nbsp;</td>
		</tr>
		<tr>
			<td>
				<table class="green_gargtd">
					<tr>
						<td width="162" align="center" class="title_bigwhite">
							BSSҵ���ѯ
						</td>
						<td>
							<img src="<s:url value="/images/attention_2.gif"/>" width="15"
								height="12" />��ѯBSSҵ��ͨ���
						</td>
					</tr>
				</table>
			</td>
		</tr>
		<tr>
			<td>
				<table class="querytable">
					<TR>
						<th colspan="4">BSSҵ���ѯ</th>
					</tr>
					<TR>
						<TD class=column width="15%" align='right'>
							<SELECT name="usernameType">
								<ms:inArea areaCode="sx_lt">
									<option value="11">�豸���к�</option>
								</ms:inArea>
								<option value="1">
									<ms:inArea areaCode="sx_lt">
										Ψһ��ʶ
									</ms:inArea>
									<ms:inArea areaCode="sx_lt" notInMode="true">
										LOID
									</ms:inArea>
								</option>
								<option value="2">��������˺�</option>
								<option value="3">IPTV����˺�</option>
								<option value="4">VoIP��֤����</option>
								<option value="5">VoIP�绰����</option>
								<ms:inArea areaCode="hb_lt">
									<option value="6">����EID</option>
								</ms:inArea>
								<ms:inArea areaCode="cq_dx">
									<option value="9">��ѡ����˽���˺�</option>
								</ms:inArea>
								<ms:inArea areaCode="xj_dx">
									<option value="7">����wifi�˺�</option>
									<option value="8">ר���˺�</option>
								</ms:inArea>
								<%if(telecom.equals("CTC")){%>
									<option value="12">����������˺�</option>
								<%}%>
							</SELECT>
						</TD>
						<TD width="35%">
							<input type="text" name="username" size="20" maxlength="100" class=bk />
							<s:if test='isRealtimeQuery == "true"'>
								&nbsp;<font color="red"> *</font>
							</s:if>
						</TD>
						<TD class=column width="15%" align='right'>����</TD>
						<TD width="35%">
							<s:select list="cityList" name="cityId" headerKey="-1" headerValue="��ѡ������" 
								listKey="city_id" listValue="city_name" cssClass="bk">
							</s:select>
						</TD>
					</TR>
					<TR>
						<TD class=column width="15%" align='right'>ҵ������</TD>
						<TD width="35%">
							<SELECT name="servTypeId"  onchange="queryServerType()">
								<option value="">==��ѡ��==</option>
								<ms:inArea areaCode="nx_lt" notInMode="false">
								    <option value="10">==���==</option>
								</ms:inArea>
								<ms:inArea areaCode="nx_lt" notInMode="true">
                                    <option value="10">==����ҵ��==</option>
                                </ms:inArea>
								<s:if test="gw_type == 2">
									<option value="27">==��ר��==</option>
								</s:if>
								<option value="11">==IPTV==</option>
								<option value="14">==VoIP==</option>
								<ms:inArea areaCode="sx_lt,nx_lt" notInMode="true">
									<option value="16">==·�ɿ��==</option>
									<option value="20">==����wifi==</option>
									<option value="15">==��������==</option>
								</ms:inArea>
								<ms:inArea areaCode="sd_dx">
									<option value="45">==VXLANҵ��==</option>
								</ms:inArea>
								<ms:inArea areaCode="xj_dx">
									<option value="38">==ר��==</option>
									<option value="32">==��Դ==</option>
									<option value="51">==wifi����==</option>
								</ms:inArea>
								<ms:inArea areaCode="cq_dx">
									<option value="33">==��ѡ����˽��==</option>
								</ms:inArea>
								<%if(telecom.equals("CTC")){%>
									<option value="47">==���������==</option>
								<%}%>
								<ms:inArea areaCode="ah_dx" notInMode="false">
									<option value="40">==У԰���==</option>
								</ms:inArea>
							</SELECT>
						</TD>
						<TD class=column width="15%" align='right'>����Э������</TD>
						<TD id="tr_voipProtocalType" width="35%" style="display:none">
							<SELECT name="voipProtocalType"  >
								<option value="">==��ѡ��==</option>
								<option value="2">
									<ms:inArea areaCode="jl_dx" notInMode="false">
									    ==NGN H248==
									</ms:inArea>
									<ms:inArea areaCode="jl_dx" notInMode="true">
									    ==H248==
									</ms:inArea>
								</option>
								<option value="1">==����SIP==</option>
								<ms:inArea areaCode="sx_lt" notInMode="true">
									<option value="0">==IMS SIP==</option>
									<option value="3">==IMS H248==</option>
								</ms:inArea>
							</SELECT>
						</TD>
					</TR>
					<TR>
						<TD class=column width="15%" align='right'>BSS����ʼʱ��</TD>
						<TD width="35%">
							<input type="text" name="startOpenDate" readonly class=bk
								value="<s:property value="startOpenDate" />">
							<%if (LipossGlobals.inArea("sd_dx")) { %>
								<img name="shortDateimg" id="d4321"
									onClick="WdatePicker({minDate:'#F{$dp.$D(\'endOpenDate\',{d:-31});}', maxDate:'#F{$dp.$D(\'endOpenDate\',{d:-0});}',el:document.selectForm.startOpenDate,dateFmt:'yyyy-MM-dd HH:mm:ss',skin:'whyGreen'})"
									src="../../images/dateButton.png" width="15" height="12"
									border="0" alt="ѡ��">
							<%} else {%>	
								<img name="shortDateimg"
									onClick="WdatePicker({el:document.selectForm.startOpenDate,dateFmt:'yyyy-MM-dd HH:mm:ss',skin:'whyGreen'})"
									src="../../images/dateButton.png" width="15" height="12"
									border="0" alt="ѡ��">
							<%}%>
							&nbsp;
							<font color="red"> *</font>
						</TD>
						<TD class=column width="15%" align='right'>BSS�������ʱ��</TD>
						<TD width="35%">
							<input type="text" name="endOpenDate" readonly class=bk
								value="<s:property value="endOpenDate" />">
							<%if (LipossGlobals.inArea("sd_dx")) { %>
								<img name="shortDateimg" id="d4322"
									onClick="WdatePicker({minDate:'#F{$dp.$D(\'startOpenDate\',{d:0});}', maxDate:'#F{$dp.$D(\'startOpenDate\',{d:31});}',el:document.selectForm.endOpenDate,dateFmt:'yyyy-MM-dd HH:mm:ss',skin:'whyGreen'})"
									src="../../images/dateButton.png" width="15" height="12"
									border="0" alt="ѡ��">
							<%} else {%>
								<img name="shortDateimg"
									onClick="WdatePicker({el:document.selectForm.endOpenDate,dateFmt:'yyyy-MM-dd HH:mm:ss',skin:'whyGreen'})"
									src="../../images/dateButton.png" width="15" height="12"
									border="0" alt="ѡ��">
							<%}%>
							&nbsp;
							<font color="red"> *</font>
						</TD>
					</TR>
					<TR>
						<ms:inArea areaCode="ah_lt" notInMode="true">
						<TD class=column width="15%" align='right'>BSS�ն�����</TD>
						<TD width="35%" >
							<s:select list="devicetypeList" name="devicetype"  listKey="type_id" listValue="type_name"
								 cssClass="bk">
							</s:select>
						</TD>
						</ms:inArea>
						<ms:inArea areaCode="ah_lt" notInMode="false">
							<TD class=column width="15%" align='right'>�豸�汾����</TD>
							<TD width="35%" >
								<s:select list="devVersionTypeMap" name="deviceVersionType"  listKey="value" listValue="text"
										  cssClass="bk">
								</s:select>
							</TD>
						</ms:inArea>
						<TD class=column width="15%" align='right'>
							��ͨ״̬ <s:property value="gwType"/>
						</TD>
						<TD width="35%">
							<SELECT name="openstatus">
								<option value="">==��ѡ��==</option>
								<option value="1">==�ɹ�==</option>
								<option value="0">
									<ms:inArea areaCode="gs_dx" notInMode="false">
										<font color="#008000">==δע��==</font>
									</ms:inArea>
									<ms:inArea areaCode="gs_dx" notInMode="true">
										<font color="#008000">==δ��==</font>
									</ms:inArea>
								</option>
								<option value="-1">==ʧ��==</option>
							</SELECT>
						</TD>
					</TR>
					
					<s:if test="gw_type == 3">
					<TR>
						<TD class=column width="15%" align='right'>�ͻ�����</TD>
						<TD width="35%" >
							<SELECT id="cust_type_id" name="cust_type_id">
								<option selected value="">==��ѡ��==</option>
								<option value="2">��ͥ�ͻ�</option>
								<option value="1">����ͻ�</option>
							</SELECT>
						</TD>
						<TD class=column width="15%" align='right'>�ͻ����</TD>
						<TD width="35%">
							<lk:select name="spec_id" listValue="spec_name" listKey="gw_type+','+id" 
								listTable="tab_bss_dev_port"/>
						</TD>
					</TR>
					</s:if>
					
					<ms:inArea areaCode="js_dx,jl_dx" notInMode="false">
					<TR>
						<TD class=column width="15%" align='right'>�û�������Դ</TD>
						<TD width="35%" >
							<SELECT id="user_type_id" name="user_type_id">
								<option value="-1" selected>==��ѡ��==</option>
								<option value="1">�ֳ���װ</option>
								<option value="2">BSS����</option>
								<option value="3">�ֹ����</option>
								<option value="4">BSSͬ��</option>
								<option value="5">IPOSSͬ��</option>
							</SELECT>
						</TD>
						<TD class=column width="15%" align='right'>������ʽ</TD>
						<TD id="tr_wanProtocalType" width="35%" style="display:none">
							<SELECT name="wanProtocalType"  >
								<option value="">==��ѡ��==</option>
								<option value="1">==�Ž�==</option>
								<option value="2">==·��==</option>
							</SELECT>
						</TD>
					</TR>
					</ms:inArea>
					
					<TR>
						<td colspan="4" align="right" class=foot>
							<button onclick="query()">&nbsp;�� ѯ&nbsp;</button>
						</td> 
					</TR>
				</table>
			</td>
		</tr>
		<tr>
			<td height="25" id="resultStr"></td>
		</tr>
		<tr>
			<td>
				<iframe id="dataForm" name="dataForm" height="0" frameborder="0"
					scrolling="no" width="100%" src=""></iframe>
			</td>
		</tr>
		<tr>
			<td height="25" id="configInfoEm" style="display: none"></td>
		</tr>
		<tr>
			<td id="configInfo"></td>
		</tr>
		<tr>
			<td height="25"></td>
		</tr>
		<tr>
			<td id="bssSheetInfo"></td>
		</tr>
		<tr>
			<td id="bssSheetDetail"></td>
		</tr>	
</table>
<br>
</form>
</body>
</html>
<%@ include file="../../foot.jsp"%>