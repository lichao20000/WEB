<%@ page language="java" contentType="text/html; charset=GBK"
	pageEncoding="GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="lk" uri="/linkage"%>
<%@taglib prefix="ms" uri="/ailk-itms-web-tags"%>
<%
		request.setCharacterEncoding("GBK");
		String instAreaName = (String)request.getAttribute("instAreaName");
		String telecom = LipossGlobals.getLipossProperty("telecom");
%>
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=GBK">
		<title>BSSҵ���ѯ</title>
		<%
			 /**
			 * BSSҵ���ѯ
			 * 
			 * @author qixueqi(4174)
			 * @version 1.0
			 * @since 2010-09-08
			 * @category
			 */
		%>
		<link rel="stylesheet" href="<s:url value="/css3/c_table.css"/>" type="text/css">
		<link rel="stylesheet" href="<s:url value="/css3/global.css"/>" type="text/css">
		<style type="text/css">
		#openUserTr{
			text-align: right;
		}
		</style>
		<script type="text/javascript" src="<s:url value="/Js/jquery.js"/>"></script>
		<SCRIPT LANGUAGE="JavaScript" SRC="../../Js/CheckFormForm.js"></SCRIPT>
		<script type="text/javascript" src="<s:url value="/Js/My97DatePicker/WdatePicker.js"/>"></script>
		<script type="text/javascript">
function query(){
	configInfoClose();
	bssSheetClose();
	var cityId = $.trim($("select[@name='cityId']").val());
	var area = '<%=instAreaName%>';
	if(area!="cq_dx"){
		if(cityId == "-1"){
	         alert("��ѡ������");
	         return false;
	    } 
	}
	if(area == "sd_lt"){
		$("#tip_loading").show();
	}
    var username = $.trim($("input[@name='username']").val());
    if(username.length > 0 && username.length <6){
    	alert("�������������6λLOID���в�ѯ��");
    	return false;
    }
    $("input[@name='username']").val(Trim($("input[@name='username']").val()));
	document.selectForm.submit();
}
function openUser(){
	var strpage = "/itms/itms/service/bssSheetByHand4HBLT.action?gw_type=1";
	window.open(strpage, "",
					"left=20,top=20,width=900,height=650,resizable=yes,scrollbars=yes");
}

function ToExcel(){
	var usernameType=$("select[@name='cityId']").val();
	var username=Trim($("input[@name='username']").val());
	var cityId=$("select[@name='cityId']").val();
	var servTypeId=$("select[@name='servTypeId']").val();
	var openstatus=$("select[@name='openstatus']").val();
	var startOpenDate=$("input[@name='startOpenDate']").val();
	var endOpenDate=$("input[@name='endOpenDate']").val();
	var devicetype=$("select[@name='devicetype']").val();
	var area = '<%=instAreaName%>';
	
	var url = "<s:url value='/itms/service/bssSheetServSDLT!getBssSheetServInfoCount.action'/>";
	$.post(url, {
		usernameType : usernameType,
		username : username,
		cityId : cityId,
		servTypeId : servTypeId,
		openstatus : openstatus,
		startOpenDate : startOpenDate,
		endOpenDate : endOpenDate,
		devicetype : devicetype
		}, function(ajax) {
			if(area!="cq_dx" && ajax>100000){
   				alert("������̫��֧�ֵ��� ");
   				return;
			}else if (area=="cq_dx" && ajax>20000){
				alert("������̫��֧�ֵ��� ");
   				return;
			}else{
				var mainForm = document.getElementById("form");
				mainForm.action="<s:url value='/itms/service/bssSheetServSDLT!getBssSheetServInfoExcel.action'/>";
				mainForm.submit();
	    		mainForm.action="<s:url value='/itms/service/bssSheetServSDLT!getBssSheetServInfo.action'/>";
			}
	});
}


function queryServerType(){
   var serValue = document.selectForm.servTypeId.value;
   if(serValue=="14"){
		this.tr_voipProtocalType.style.display="";
   }else{
   		this.tr_voipProtocalType.style.display="none";
   		document.selectForm.voipProtocalType.value="";
   }
}

function configInfoClose(){
	$("td[@id='configInfoEm']").hide();
	$("td[@id='configInfo']").hide();
}

function configDetailClose(){
	$("td[@id='bssSheetDetail']").hide();
}

function bssSheetClose(){
	$("td[@id='bssSheetInfo']").hide();
}

function loading(){
	$("#tip_loading").hide();
}


function configDetailInfo(strategyId,deviceSN,servTypeId){
	var gw_type = document.getElementsByName("gw_type")[0].value;
	servTypeId = servTypeId ? servTypeId : "";
	var page = "<s:url value='/itms/service/bssSheetServSDLT!getConfigDetail.action'/>?gw_type=" +gw_type+ "&strategyId="+strategyId+"&deviceSN="+deviceSN
			+ "&servTypeId="+servTypeId;
	window.open(page,"","left=20,top=20,width=600,height=450,resizable=yes,scrollbars=yes");
}

function solutions(result_id,deviceSN){
	var url = "<s:url value='/itms/service/bssSheetServSDLT!getSolution.action'/>?&deviceSN="+deviceSN+"&result_id="+result_id;
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

function resetData(userId, deviceId, oui, deviceSN, servTypeId, servstauts) {
	if (deviceId == "") {
		alert("�û�δ���豸�����Ȱ��豸���ټ��");
		return;
	}
	if (confirm('���¼����ǽ���ҵ����Ϊδ��״̬��ȷʵҪ������?')) {
		var url = "<s:url value='/itms/service/bssSheetServSDLT!callPreProcess.action'/>";
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
function configLog(deviceSN,deviceId,servTypeId,servstauts,wanType){
	var gw_type = document.getElementsByName("gw_type")[0].value;
	var page = "<s:url value='/itms/service/bssSheetServSDLT!getConfigLogInfo.action'/>?"
		+ "deviceSN=" + deviceSN
		+ "&deviceId=" + deviceId
		+ "&servTypeId=" + servTypeId
		+ "&servstauts=" + servstauts
		+ "&wanType=" + wanType
		+ "&gw_type=" + gw_type;
	window.open(page,"","left=20,top=20,width=700,height=350,resizable=yes,scrollbars=yes");
}

//������Ҫѡ��߼���ѯѡ��
function ShowDialog(leaf){
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

function setValue(){
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
		}
		else{
			bbmsOptions[bbmsIndex++] = option;
		}
	}
}

<%-- ���¿ͻ����ͣ���ʵ�ֿͻ�������ͻ��ն˹���� --%>
function changeCustomerType() {
	var specSelect = $("#spec_id").get(0);
	specSelect.length = 0;
	if ("1" == $("#cust_type_id option:selected").val()){
		// itms
		for (var i = 0; i < itmsOptions.length; i++){
			specSelect.options[i] = itmsOptions[i];
		}
	}
	else{
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
		<form id="form" name="selectForm" action="<s:url value='/itms/service/bssSheetServSDLT!getBssSheetServInfo.action'/>"
			target="dataForm">
			<input type="hidden" name="selectType" value="0" />
			<input type="hidden" name="gw_type" value='<s:property value="gw_type" />' />
			<input type="hidden" name="netServUp" value='<s:property value="netServUp" />' />
			<table>
				<tr>
					<td HEIGHT=20>
						&nbsp;
					</td>
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
										height="12" />
									��ѯBSSҵ��ͨ���
								</td>
								<s:if test='%{instAreaName == "hb_lt"}'>
								<td id="openUserTr">
									<button onclick="openUser()">
										&nbsp;��&nbsp;��&nbsp;
									</button>
								</td>
								</s:if>
							</tr>
						</table>
					</td>
				</tr>
				<tr>
					<td>
						<table class="querytable">

							<TR>
								<th colspan="4">
									BSSҵ���ѯ
								</th>
							</tr>

							<TR>
								<TD class=column width="15%" align='right'>
									<SELECT name="usernameType">
										<option value="1">
											LOID
										</option>
										<option value="2">
											��������˺�
										</option>
										<option value="3">
											IPTV����˺�
										</option>
										<option value="4">
											VoIP��֤����
										</option>
										<option value="5">
											VoIP�绰����
										</option>
										<ms:inArea areaCode="sd_lt" notInMode="true">
                                            <option value="6">
                                                VPDN����˺�
                                            </option>
										</ms:inArea>
										<ms:inArea areaCode="hb_lt">
											<option value="7">
												����EID
											</option>
										</ms:inArea>
										<ms:inArea areaCode="cq_dx">
											<option value="8">
												��ѡ����˽���˺�
											</option>
										</ms:inArea>
										<%if(telecom.equals("CTC")){%>
											<option value="12">����������˺�</option>
										<%}%>
									</SELECT>
								</TD>
								<TD width="35%">
									<input type="text" name="username" size="20" maxlength="30"
										class=bk />
								</TD>
								<TD class=column width="15%" align='right'>
									����
								</TD>
								<TD width="35%">
									<s:select list="cityList" name="cityId" headerKey="-1"
										headerValue="��ѡ������" listKey="city_id" listValue="city_name"
										 cssClass="bk"></s:select>
								</TD>
							</TR>
							<TR>
								<TD class=column width="15%" align='right'>
									ҵ������
								</TD>
								<TD width="35%">
									<SELECT name="servTypeId"  onchange="queryServerType()">
										<option value="">
											==��ѡ��==
										</option>
										<option value="10">
											==����ҵ��==
										</option>
										<ms:inArea areaCode="sd_lt" notInMode="false">
											<option value="10_2">
												==ͣ��==
											</option>
											<option value="10_1">
												==����==
											</option>
										</ms:inArea>
										<option value="11">
											==IPTV==
										</option>
										<option value="14">
											==VoIP==
										</option>
										<option value="16">
											==·�ɿ��==
										</option>
										<ms:inArea areaCode="sd_lt" notInMode="true">
                                            <option value="28">
                                                ==VPDN==
                                            </option>
										</ms:inArea>
										<ms:inArea areaCode="cq_dx">
											<option value="33">
												==��ѡ����˽��==
											</option>
										</ms:inArea>
										<%if(telecom.equals("CTC")){%>
											<option value="47">==���������==</option>
										<%}%>
									</SELECT>
								</TD>
								<TD class=column width="15%" align='right'>
									����Э������
								</TD>
								<TD id="tr_voipProtocalType" width="35%" style="display:none">
									<SELECT name="voipProtocalType"  >
										<option value="">
											==��ѡ��==
										</option>
										<option value="2">
											==H248==
										</option>
										<option value="1">
											==����SIP==
										</option>
										<option value="0">
											==IMS SIP==
										</option>
									</SELECT>
								</TD>
							</TR>
							<TR>
								<TD class=column width="15%" align='right'>
									BSS����ʼʱ��
								</TD>
								<TD width="35%">
									<input type="text" name="startOpenDate" readonly class=bk
										value="<s:property value="startOpenDate" />">
									<img name="shortDateimg"
										onClick="WdatePicker({el:document.selectForm.startOpenDate,dateFmt:'yyyy-MM-dd HH:mm:ss',skin:'whyGreen'})"
										src="../../images/dateButton.png" width="15" height="12"
										border="0" alt="ѡ��">
									&nbsp;
									<font color="red"> *</font>
								</TD>
								<TD class=column width="15%" align='right'>
									BSS�������ʱ��
								</TD>
								<TD width="35%">
									<input type="text" name="endOpenDate" readonly class=bk
										value="<s:property value="endOpenDate" />">
									<img name="shortDateimg"
										onClick="WdatePicker({el:document.selectForm.endOpenDate,dateFmt:'yyyy-MM-dd HH:mm:ss',skin:'whyGreen'})"
										src="../../images/dateButton.png" width="15" height="12"
										border="0" alt="ѡ��">
									&nbsp;
									<font color="red"> *</font>
								</TD>
							</TR>
							<TR>
							<s:if test='%{instAreaName != "jl_dx"}'>
								<TD class=column width="15%" align='right'>
									BSS�ն�����
								</TD>
								<TD width="35%" >
									<!-- <SELECT name="devicetype">
										<option selected value="2">E8-C</option>
										<option value="1">E8-B</option>
										<option value="0">ȫ��</option>
									</SELECT>-->
									<s:select list="devicetypeList" name="devicetype" listKey="type_id" listValue="type_name"
										 cssClass="bk"></s:select>
								</TD>
							</s:if>
								<TD class=column width="15%" align='right'>
									��ͨ״̬ <s:property value="gwType"/>
								</TD>
								<TD width="35%">
									<SELECT name="openstatus">
										<option value="">
											==��ѡ��==
										</option>
										<option value="1">
											==�ɹ�==
										</option>
										<option value="0">
											==δ��==
										</option>
										<option value="-1">
											==ʧ��==
										</option>
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
							<TR>
								<td colspan="4" align="right" class=foot>
									<button onclick="query()">
										&nbsp;�� ѯ&nbsp;
									</button>
								</td> 
							</TR>
						</table>
					</td>
				</tr>
				<tr>
					<td height="25" id="resultStr">

					</td>
				</tr>
				<tr>
		           <td>
					  <div style="width: 100%; display: none; text-align: center;"
						id="tip_loading">
						���ڼ�������,�����ĵȴ�......
					   </div>
				    </td>
			    </tr>
				<tr>
					<td>
						<iframe id="dataForm" name="dataForm" height="0" frameborder="0"
							scrolling="no" width="100%" src=""></iframe>
					</td>
				</tr>
				<tr>
					<td height="25" id="configInfoEm" style="display: none">

					</td>
				</tr>
				
				<tr>
					<td id="configInfo">

					</td>
				</tr>
				<tr>
					<td height="25">

					</td>
				</tr>
				<tr>
					<td id="bssSheetInfo">

					</td>
				</tr>
				
				<tr>
					<td id="bssSheetDetail">

					</td>
				</tr>
				
				
			</table>
			<br>
		</form>
	</body>
</html>
<%@ include file="../../foot.jsp"%>
