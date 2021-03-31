<%@ page language="java" contentType="text/html; charset=GBK"
    pageEncoding="GBK"%>
<%@taglib prefix="s" uri="/struts-tags"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<link rel="stylesheet" href="<s:url value="/css/css_green.css"/>" type="text/css">
<script type="text/javascript" src="/Js/jquery.js"></script>
<script type="text/javascript" src="/Js/CheckFormForm.js"></script>
<script type="text/javascript" src="<s:url value="/Js/My97DatePicker/WdatePicker.js"/>"></script>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<title>BSS������Ϣ����</title>
<script type="text/javascript">
/* var servTypeId = '<s:property value="servTypeId" />'; */
var gwType = 1;
function change(obj){
	var value = obj.value;
	if(value==1){
		gwType = 1;
		$("#username").removeAttr("disabled");
		$("#username").val("");
	}else if(value==2){
		gwType = 2;
		$("#username").removeAttr("disabled");
		$("#username").val("");
	}else{
		$("#username").val("����ѡ���û�����!");
		$("#username").attr("disabled","disabled");	
	}
}

//���LOID��ҵ���ʺ�
function checkUserInfo(){
	var username = $.trim($("input[@name='username']").val());
	var url = "<s:url value='/itms/service/simulateSheet!checkUsername.action'/>";
	$.post(url,{
		gw_type : gwType,
		username : username
	}, function(ajax) {
		var relt = ajax.split("#");
		if(relt[0] != "1"){
			hasUsername = 0;
			$("font[@id='usernameDiv']").html("<font color=red>*"+relt[1]+"</font>");
		}
		else{
			hasUsername = 1;
			$("font[@id='usernameDiv']").html("*");
		}
	});
}

//����ע������
function sendSheet(){
	var servTypeId = $.trim($("input[@name='servTypeId']").val());
	var	operateType	= $.trim($("input[@name='operateType']").val());
	var dealdate = $.trim($("input[@name='dealdate']").val());
	var userType = $.trim($("select[@name='userType']").val());
	var username = $.trim($("input[@name='username']").val());
	var cityId = $.trim($("select[@name='cityId']").val());
	var voipTelepone = $.trim($("input[@name='voipTelepone']").val());
	//����ʱ��
	if(dealdate == ""){
		$("input[@name='dealdate']").focus();
		return false;
	}
	//�豸����
	if(userType == -1){
		alert("��ѡ���豸����");
		$("select[@name='userType']").focus();
		return false;
	}
	//�û��˺�
	if(username == ""){
		alert("�������û��˺�");
		$("input[@name='username']").focus();
		return false;
	}
	//����
	if(cityId  == ""){
		alert("��ѡ������");
		$("select[@name='cityId']").focus();
		return false;
	}
	if(servTypeId == 15){
		//ҵ��绰����
		if(voipTelepone  == ""){
			alert("������ҵ��绰����");
			$("input[@name='voipTelepone']").focus();
			return false;
		}
	}
	$("tr[@id='trData']").show();
    $("div[@id='QueryData']").html("���ڷ��ͣ����Ե�....");
    $("button[@name='e8cStopButton']").attr("disabled", true);
    var url = "<s:url value='/itms/service/simulateHLJSheet!sendSheet.action'/>";
    if(servTypeId == 15){
    	$.post(url, {
    		servTypeId:servTypeId,
    		operateType:operateType,
    		dealdate:dealdate,
    		userType:userType,
    		username:username,
    		cityId:cityId,
    		voipTelepone:voipTelepone
    	}, function(ajax) {
    		$("div[@id='QueryData']").html("");
    		$("div[@id='QueryData']").append(ajax);
    		$("button[@name='e8cStopButton']").attr("disabled", false);
    	});
    }else{
    	$.post(url, {
    		servTypeId:servTypeId,
    		operateType:operateType,
    		dealdate:dealdate,
    		userType:userType,
    		username:username,
    		cityId:cityId
    	}, function(ajax) {
    		$("div[@id='QueryData']").html("");
    		$("div[@id='QueryData']").append(ajax);
    		$("button[@name='e8cStopButton']").attr("disabled", false);
    	});
    }
	
}
</script>
</head>
<body>
	<form name="frm" method="post" style="text-align: left;" id="myTable">
		<table class="listtable"  width="100%"> 
			<tr class="green_title">
				<td colspan="7">
					<input type="hidden" name="servTypeId" value='<s:property value="servTypeId" />'> 
					<input	type="hidden" name="operateType" value='<s:property value="operateType" />'>
					<b>
						<s:if test="servTypeId==20">
							ȫҵ����������Ϣ
						</s:if>
						<s:if test="servTypeId==22">
							����ҵ����������Ϣ
						</s:if>
						<s:if test="servTypeId==15">
							VOIPH248 ���������Ϣ
						</s:if>
					</b>
				</td>
			</tr>
			<tr style="background-color: #ffffff;">
				<td class="column" align="right" width="20%">����ʱ��</td>
				<td width="30%">
					<input type="text" name="dealdate"	value='<s:property value="dealdate" />' 
						readonly class="bk">
					<img name="shortDateimg"
						onClick="WdatePicker({el:document.frm.dealdate,dateFmt:'yyyy-MM-dd HH:mm:ss',skin:'whyGreen'})"
						src="../../../../images/dateButton.png" width="15" height="12"
						border="0" alt="ѡ��">&nbsp; 
					<font color="#FF0000">*</font>
				</td>
				<td class="column" align="right" width="20%">�豸����</td>
				<td colspan="3">
					<select name="userType" class="bk" onchange="change(this)">
							<option value="-1">==��ѡ��==</option>
							<option value="1" >==��ͥ����==</option>
							<option value="2">==��ҵ����==</option>
					</select> &nbsp; 
					<font color="#FF0000">*</font>
				</td>
			</tr>
			<tr style="background-color: #ffffff;">
				<td class="column" align="right" nowrap>LOID</td>
				<td>
					<input type="text" id="username" name="username" maxlength=50 class="bk"  value="����ѡ���豸����" disabled="disabled" onblur="checkUserInfo()"/>&nbsp; 
					<font color="#FF0000" id="usernameDiv">* </font>
				</td>
				<td class="column" align="right" width="20%">����</td>
				<td colspan="3">
					<s:select list="cityList" name="cityId"
						headerKey="-1" headerValue="��ѡ������" listKey="city_id"
						listValue="city_name" value="cityId" cssClass="bk">
					</s:select>&nbsp; 
					<font color="#FF0000">*</font>
				</td>
			</tr>
			<s:if test="servTypeId==15">
				<tr style="background-color: #ffffff;">
					<td class="column" align="right" nowrap>ҵ��绰����</td>
					<td>
						<input type="text" name="voipTelepone" maxlength=50 class="bk" />&nbsp; 
						<font color="#FF0000">* </font>
					</td>
					<td colspan="4"></td>
				</tr>
			</s:if>
			<tr>
				<td colspan="7" align="right" class="green_foot" >
					<button name="e8cStopButton" onclick="sendSheet();">&nbsp;���͹���&nbsp;</button>
				</td>
			</tr>
		</table>
		<table>
			<tr>
				<td height="30"></td>
			</tr>
			<tr id="trData" style="display: none">
				<td>
					<div id="QueryData" style="width: 105%; z-index: 1; top: 100px;text-align: left;">
						���ڷ��ͣ����Ե�....
					</div>
				</td>
			</tr>
			<tr>
				<td height="20"></td>
			</tr>
		</table>
	</form>
</body>
</html>