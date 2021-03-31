<%@ include file="../timelater.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>
<%@page import="java.util.*,com.linkage.litms.software.CmdUpgrade"%>
<%@page import="com.linkage.litms.common.util.RPCUtilHandler,com.linkage.litms.common.util.StringUtils"%>
<%@page import="com.linkage.litms.resource.DeviceAct"%>
<%@page import="java.util.HashMap"%>
<%
request.setCharacterEncoding("GBK");
String[] deviceArr = request.getParameterValues("device_id");
//�����ݿ��в�ѯ��òɼ��㣬�����Ǵ�ҳ����
DeviceAct act = new DeviceAct();
HashMap deviceInfoMap= act.getDeviceInfo(deviceArr[0]);
String strGatherId = (String)deviceInfoMap.get("gather_id");
String devicetype_id = (String)deviceInfoMap.get("devicetype_id");

String [] cmdParamArr = request.getParameterValues("cmdParam");
String object_name="ACS_" + strGatherId;
String object_Poaname="ACS_Poa_" + strGatherId;
String strIor = user.getIor(object_name,object_Poaname);

CmdUpgrade cmpUpgrade = new CmdUpgrade(request);
//���Ƚ�ǰ̨ҳ�洫������Ӻ�̨��ȡֵget
Map mapDeviceParamValue = cmpUpgrade.getParamValue(strGatherId,strIor,deviceArr,cmdParamArr);
Map mapParamValue = null;
//�ܻ�ȡ�豸�������豸id�б�
List listDeviceId = new ArrayList();

%>
<%@ include file="../head.jsp"%>
<%@ include file="../toolbar.jsp"%>
<SCRIPT LANGUAGE="JavaScript" SRC="../Js/prototype.js"></SCRIPT>
<SCRIPT LANGUAGE="JavaScript">
//����豸��Ӧ�� �������Ժ�ֵ��map����
var deviceParamValue = new Object();
//��¼�û�ѡ��Ĳ�������
var cmdParamArr = "<%=StringUtils.weave(Arrays.asList(cmdParamArr))%>";
var paramValue = null;
var gather_id = "<%=strGatherId%>";
<%
for(int i=0;i<deviceArr.length;i++){
	out.println("paramValue = new Object();");
	mapParamValue = (Map)mapDeviceParamValue.get(deviceArr[i]);	
	for(int j=0;j<cmdParamArr.length;j++){
		if(mapParamValue != null){
			out.println("paramValue['" + cmdParamArr[j] + "']='" + mapParamValue.get(cmdParamArr[j]) + "';");
			listDeviceId.add(deviceArr[i]);
		}else{//��ȡ�������ʧ�ܵ��豸id
			
		}
	}
}
//�������豸id
%>
//�Ѿ����������豸id���飬δ��ɵ�id���������С�
//ע����java��deviceArr�豸id��һ��
var deviceArr = [<%=StringUtils.weave(listDeviceId)%>];
deviceArr = $A(deviceArr);
<%
//ִ�й���----------------------------------------------------------------//
List listSheetId = cmpUpgrade.executeSheet(strIor,strGatherId,devicetype_id);
//ִ�й�������------------------------------------------------------------//
%>
//��̨���ڴ���Ĺ���id���飬���������󹤵�(sheet.time=-3���豸���ڲ���)
var sheetArr = [<%=StringUtils.weave(listSheetId)%>];
sheetArr = $A(sheetArr);
</SCRIPT>
<br>
<table width="95%" border="0" align="center" cellpadding="0" cellspacing="0" class="text">
	<tr>
		<td>
			<table width="100%" height="30" border="0" cellspacing="0" cellpadding="0" class="green_gargtd">
				<tr>
					<td width="162" align="center" class="title_bigwhite">
						���������
					</td>
					<td>
						<img src="../images/attention_2.gif" width="15" height="12">
						��ҳ���ÿ5���м�鹤��ִ��״̬������ִ�гɹ��󣬻��������ָ�����!
					</td>
				</tr>
			</table>
		</td>
	</tr>
	<tr >
		<td>
			<TABLE width="100%" border=0 cellspacing=1 cellpadding=2 align="center" bgcolor=#999999>
				<tr bgcolor=#ffffff>
					<th>�豸OUI</th>
					<th>�豸���к�</th>
					<th>���ݲ���״̬</th>
				</tr>
					<%
						String[] deviceInfo = null;
						//�����û�ѡ��������豸
						for(int i=0;i<deviceArr.length;i++){
						    deviceInfo = RPCUtilHandler.getDeviceResInfo(deviceArr[i]);
							out.println("<tr bgcolor=#ffffff>");
						    out.println("<td>"+ deviceInfo[0] +"</td>");
						    out.println("<td>"+ deviceInfo[1] +"</td>");
							out.println("<td><span id='devInfo_"+ deviceArr[i] +"'>-</span></td>");
							out.println("</tr>");
						}
					%>
			</TABLE>
		</td>
	</tr>
</table>
<script language="JavaScript">
<!--
var SH_FAULT_CODE = new Object();
SH_FAULT_CODE["-1"]="���Ӳ���";
SH_FAULT_CODE["0"]="δ֪����";
SH_FAULT_CODE["1"]="ִ�гɹ�";
SH_FAULT_CODE["-2"]="���ӳ�ʱ";
SH_FAULT_CODE["-3"]="û�й���";
SH_FAULT_CODE["-4"]="û���豸";
SH_FAULT_CODE["-5"]="û��ģ��";
SH_FAULT_CODE["-6"]="�豸��æ";
SH_FAULT_CODE["-7"]="��������";
function getSheetDetail(fault_code){
	var str = SH_FAULT_CODE[fault_code];
	if(str == null) str = "δ֪״̬";
	return str;
}
function setDeviceInfo(device_id,info){
	var spDev = $("devInfo_" + device_id);
	if(spDev == null) return ;
	spDev.innerHTML = info;
	//��־λ������������ɹ�
	//spDev.isok = 1;
}
//���ò���
function setDeviceParamValue(device_id){
	paramValue = deviceParamValue[device_id];
	if(typeof(paramValue) == "undefined"){
		alert("error:" + paramValue);
		return ;
	}	
	var pageParam = "device_id=" + device_id;
	pageParam += "&gather_id=" + gather_id;
	for(var j=0;j<cmdParamArr.length;j++){
		pageParam += "&" + cmdParamArr[j] + "=" + paramValue[cmdParamArr[j]];
	}
	pageParam += "&tt=" + new Date();
	CreateAjaxReq("setDeviceCmdUpgrade.jsp",pageParam,completeSetDevicePV,errorSetDevicePV);
}

//����ݳɹ����ȴ�����ִ�����
//for(var k=0;k<deviceArr.length;k++){
//	setDeviceInfo(deviceArr[k],"�豸������������ɹ������ڵȴ�����ִ����ɺ�ָ���������...");
//}

//��鹤��ִ��״̬
function checkSheet(){
	var sheetStr = "";
	sheetArr.each(function(item){
		sheetStr += "," + item;
	});
	if(sheetStr == "")
		return ;
	sheetStr = sheetStr.substring(1);
	var param = "sheetId=" + sheetStr + "&tt=" + new Date().getTime();
	CreateAjaxReq("getSheetStatusOfCmdUpgrade.jsp",param,completeSheetAct,errorSheetAct);
}
//������鹤��״̬
checkSheet();
var tiger = window.setInterval("checkSheet()",5000);
//��������Ajax��������
function CreateAjaxReq(url,param,successFunc,errorFunc){
	var myAjax
		= new Ajax.Request(
							url,
							{
								method:"post",
								parameters:param,
								onFailure:errorFunc,
								onSuccess:successFunc
							 }
						  );
}
//����ִ�����֮���޸��豸״̬��Ϣ���ָ�����
function completeSheetAct(response){
	var result = eval(response.responseText);//[device_id$sheet_id,device_id$sheet_id,device_id$sheet_id]
	var funstr = null;
	//alert("completeSheetAct:" + result + "==>" + response.responseText);
	//var arrDevSheet = null;
	//����ִ�гɹ��Ĺ���
	result = $A(result);
	result.each(function(item){
		funstr = "view('" + item.sheet_id + "','" + item.receive_time + "')";
		//��������ִ��
		if(item.exec_status == 0){
			setDeviceInfo(item.device_id,"����ִ�й���...<a href=javascript:"+ funstr +">�鿴����</a>");
		}else if(item.exec_status == 1){//�������
			if(item.fault_code == 1){//����ִ�гɹ�
				setDeviceInfo(item.device_id,"����ִ�гɹ������ڽ�������ָ�����....<a href=javascript:"+ funstr +">�鿴����</a>");
				setDeviceParamValue(item.device_id);
			}else{
				setDeviceInfo(item.device_id,"����ִ��ʧ��("+ getSheetDetail(item.fault_code) + ")������ʧ��!<a href=javascript:"+ funstr +">�鿴����</a>");
			}
			deleteSheet(item.sheet_id);
		}else{
			alert("error:" + item);
		}
	});
	//��ɺ���������������ɣ����д���
	if(sheetArr.length == 0){//result.length == 0 || 
		window.clearInterval(tiger);
		tiger = null;
		//�������豸������Ϣ�޸�Ϊ���,����������ʧ��
		//var spArr = $$('span[isok="0"]');
		//spArr.each(function(item){
		//	item.innerHTML = "����ִ��ʧ�ܣ����豸�˴α�������ʧ��!";
		//});
	}
}
function view(sheet_id,receive_time){
	if(item == null){
		alert("error:" + item);
		return ;
	}
	var page = "sheet_detail.jsp?sheet_id="+ sheet_id + "&receive_time="+ receive_time;
	window.open(page,new Date().getTime(),"left=20,top=20,width=500,height=420,resizable=no,scrollbars=no");
}
function errorSheetAct(response){
	$("debug").innerHTML = response.responseText;
}
function completeSetDevicePV(response){
	var resutlDev = eval(response.responseText);
	if(typeof(resutlDev) == "object" && resutlDev.flag == true){
		setDeviceInfo(resutlDev.device_id,"����ִ����ɣ��豸����ݳɹ�!");
	}
}
function errorSetDevicePV(response){
	alert(response.responseText);
}

function deleteSheet(sheet_id){
	sheetArr = sheetArr.without(sheet_id);
}

//-->
</script>
<div id=debug></div>
<%@ include file="../foot.jsp"%>