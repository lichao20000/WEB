<%@page import="com.linkage.litms.LipossGlobals"%>
<%@page import="com.linkage.litms.system.*"%>
<%@ page language="java" contentType="text/html; charset=GBK" pageEncoding="GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@taglib prefix="lk" uri="/linkage" %>
<%@taglib prefix="ms" uri="/ailk-itms-web-tags"%>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<link href="<s:url value="/css/css_green.css"/>" rel="stylesheet" type="text/css">
<script type="text/javascript" src="<s:url value="/Js/jquery.js"/>"></script>
<script type="text/javascript" src="<s:url value="/Js/jQeuryExtend-linkage.js"/>"></script>
<script type="text/javascript" src="/Js/jsDate/WdatePicker.js"></script>
<SCRIPT LANGUAGE="JavaScript">
var area = "<%=LipossGlobals.getLipossProperty("InstArea.ShortName")%>";


//��֤��������ĳ����Ƿ�Ϸ�
function do_test()
{
	  var idsShare_queryParam = $("input[@name='idsShare_queryParam']").val();
	  idsShare_queryParam = $.trim(idsShare_queryParam);
	
	  if(0 == idsShare_queryParam.length){
			alert("�������ѯ������");
			$("input[@name='idsShare_queryParam']").focus();
			return false;
		}
	
	//��ȡѡ�������
	var idsShare_queryFields = document.getElementsByName("idsShare_st_queryField");
	
	//"�豸���к�"��ѡ��
	if(idsShare_queryFields[0].checked)
	{
		if(idsShare_queryParam.length < 6 && idsShare_queryParam.length > 0){
			alert("�������������6λ�豸���кŽ��в�ѯ��");
			document.idsShare_selectForm.idsShare_queryParam.focus();
			return false;
		}
	}
	return true;
}

/**
 *��ѯ����
 */
function idsShare_queryField_selected(value){
	$("input[@name='idsShare_queryField']").val(value);
}


/**
 * ��ѯ�豸��Ϣ
 */
function idsShare_queryDevice(){
	closeMessageInfo();
	$("input[@name='gwShare_queryButton']").attr("disabled", true);
	$("td[@id='trDeviceResult']").css("display","none");
	$("td[@id='tdData']").css("display","none");
	
	//��ѯ����
	var idsShare_queryField = $("input[@name='idsShare_queryField']").val();
	//��ѯ����
	var idsShare_queryParam = $("input[@name='idsShare_queryParam']").val();
	//����ȥ�ո�
	idsShare_queryParam = $.trim(idsShare_queryParam);
	//����ʼ
	var url = "<s:url value='/ids/IdsShareDevice!queryIdsDevice.action'/>";
	$.post(url, {idsShare_queryField : idsShare_queryField,
				 idsShare_queryParam : idsShare_queryParam,
				 idsShare_queryType : "httpSpeed"},
				 function(ajax){
					var calldata = ajax;
					var ajax = ajax.split("#");
					if("1"==ajax[0]){
						$("input[@name='gwShare_queryButton']").attr("disabled", false);
						idsShare_revalue();
						alert("��ѯ�豸ʧ��: "+ajax[1]);
						return false;
					}else{
						deviceResult(calldata);
					}  
	});
	document.all("tr_userinfo").style.display="";
}

//����
function idsShare_revalue(){
	$("input[@name='idsShare_st_queryField']").get(0).checked = true;
	$("input[@name='gwShare_queryButton']").attr("disabled", false);
	$("input[@name='idsShare_queryField']").val("6");
	$("input[@name='idsShare_queryParam']").val("");
	$("td[@id='trDeviceResult']").css("display","none");
	$("td[@id='tdData']").css("display","none");
}

</SCRIPT>
<style>
span
{
	position:static;
	border:0;
}
</style>
</head>
<form name="idsShare_selectForm" action="" target="dataForm">
<TABLE border=0 cellspacing=0 cellpadding=0 width="100%" align="center">
	<tr>
		<td bgcolor=#999999>
			<table border=0 cellspacing=1 cellpadding=2 width="100%" align="center">
				<tr><th colspan="4" id="gwShare_thTitle">�� �� �� ѯ</th></tr>
				<tr bgcolor="#FFFFFF" id="gwShare_tr11" STYLE="display:">
					<td colspan="4" align="center" width="100%">
						<div>
							<input type="input" class="bk" name="idsShare_queryParam" size="60" maxlength="60"/>
							<input type="hidden" name="idsShare_queryField" id="idsShare_queryField" value="6" />
							<br /> 
						</div>
					</td>
				</tr>
				<tr bgcolor="#FFFFFF" id="gwShare_tr12" STYLE="display:">
					<td colspan="4" align="center" width="100%">
						<!-- �豸���кţ�SearchType=����Ϊ1 -->
						<input type="radio" class=jianbian name="idsShare_st_queryField" value="6" checked onclick="idsShare_queryField_selected('6')"/>
						 �豸���к� &nbsp;&nbsp;
						<input type="radio" class=jianbian name="idsShare_st_queryField" value="1" onclick="idsShare_queryField_selected('1')"/>
						 �û�����ʺ� &nbsp;&nbsp;
						<input type="radio" class=jianbian name="idsShare_st_queryField" value="2" onclick="idsShare_queryField_selected('2')"/>
						LOID &nbsp;&nbsp;
					</td>
				</tr>
				<tr bgcolor="#FFFFFF">
					<td colspan="4" align="right" class="green_foot" width="100%">
						<input type="button" onclick="javascript:do_test()&&idsShare_queryDevice()" class=jianbian 
						name="gwShare_queryButton" value=" �� ѯ " />
						<input type="button" class=jianbian onclick="javascript:idsShare_revalue()" 
						name="gwShare_reButto" value=" �� �� " />
					</td>
				</tr>
			</table>
		</td>
	</tr>
	<TR style="display: none;width: 100%;" id="tr_userinfo">
		<TD colspan="4" width="100%">
			<div id="div_user"
				style="width: 100%;">
			</div>
		</TD>
	</TR>
</TABLE>
</form>