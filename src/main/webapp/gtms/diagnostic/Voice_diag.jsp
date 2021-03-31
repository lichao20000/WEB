
<%@ include file="../../timelater.jsp"%>
<%@ page language="java" contentType="text/html; charset=GBK"%>
<%@taglib prefix="s" uri="/struts-tags"%>
<%
	String deviceId = request.getParameter("deviceId");
	String gwType = request.getParameter("gw_type");
 %>
<link href="../../css/css_green.css" rel="stylesheet" type="text/css">
<link href="../../css/listview.css" rel="stylesheet" type="text/css">
<SCRIPT LANGUAGE="JavaScript" SRC="../../Js/jquery.js"></SCRIPT>
<SCRIPT LANGUAGE="JavaScript" SRC="../../Js/CheckFormForm.js"></SCRIPT>
<SCRIPT LANGUAGE="JavaScript">
<!--//
//��ѯ�������

var voipLine = "1";
var gwType = <%=gwType%>;
/**
 * ת����ʾ
 */
function changeType()
{
	var servTypeId = $("select[@name='queryServTypeId']").val();
	//alert(servTypeId);
	//���������Բ���
	if("PhoneConnectivityTest" == servTypeId)
	{
		$("tr[@id='conntest']").css("display","");
		$("tr[@id='simtest']").css("display","none");
		$("td[@id='testName']").html("���������Բ���");
	}else
	{
		//���з������
		$("tr[@id='conntest']").css("display","none");
		$("tr[@id='simtest']").css("display","");
		$("td[@id='testName']").html("���з������");
	}

	//���ť
	$("button[@name='connSubBtn']").attr("disabled", false);
	$("button[@name='simSubBtn']").attr("disabled", false);
	
	//���ť
	$("button[@name='connQueryBtn']").attr("disabled", false);
	$("button[@name='simQueryBtn']").attr("disabled", false);
	
	//���ƽ����ʧ
	$("tr[@id='connresultTR']").css("display","none");
	$("tr[@id='simresultTR']").css("display","none");
	
	//��ѯ��ť��ʧ
	$("tr[@id='connQueryBtnTR']").css("display","none");
	$("tr[@id='simQueryBtnTR']").css("display","none");
	
	//��ѯ�����ʧ
	$("tr[@id='connQueryResultTR']").css("display","none");
	$("tr[@id='simQueryResultTR']").css("display","none");
	
	parent.dyniframesize();
}

//ҵ���·�
function doBusiness(){
	
	var devId = $("input[@name='devId']").val();
	var testSelector = $("select[@name='queryServTypeId']").val();
	
	if('' == devId){
		alert("���Ȳ�ѯ����ѡ���豸");
		return false;
	}

	//ҵ�񴥷�
	var url = "<s:url value='/gwms/config/voiceConnectionTest!setValue.action'/>";

	//�һ���ť
	//$("button[@name='connSubBtn']").attr("disabled", true);
	//$("button[@name='simSubBtn']").attr("disabled", true);

	voipLine = document.getElementById("line1").checked ? "1" : "2";
	
	//���������Բ���
	if("PhoneConnectivityTest" == testSelector)
	{
		$("tr[@id='connresultTR']").css("display","");
		$("div[@id='connresult']").html("������...");
		//alert(voipLine);
		$.post(url,{
			deviceId:devId,
			testSelector:testSelector,
			voipLine:voipLine,
			gw_type:gwType
		},function(ajax){
			//alert(ajax);
			$("div[@id='connresult']").html(ajax);
			
			//���ݽ�����ж��Ƿ������ѯ
			//if("���Ƴɹ�" == ajax)
			//{
				$("tr[@id='connQueryBtnTR']").css("display","");
			//}
			parent.dyniframesize();
		});
		
		
	}else
	{
		$("tr[@id='simresultTR']").css("display","");
		$("div[@id='simresult']").html("������...");
		//alert(voipLine);
		var calledNumber = $("input[@name='calledNumber']").val();
		var testType = document.getElementById("testType").options[document.getElementById("testType").selectedIndex].value;
		var dailDTMFConfirmEnable = document.getElementById("dailDTMFConfirmEnable").options[document.getElementById("dailDTMFConfirmEnable").selectedIndex].value;
		var dailDTMFConfirmNumber = $("input[@name='dailDTMFConfirmNumber']").val();
		$.post(url,{
			deviceId:devId,
			testSelector:testSelector,
			voipLine:voipLine,
			calledNumber:calledNumber,
			testType:testType,
			dailDTMFConfirmEnable:dailDTMFConfirmEnable,
			dailDTMFConfirmNumber:dailDTMFConfirmNumber,
			gw_type:gwType
		},function(ajax){
			//alert(ajax);
			$("div[@id='simresult']").html(ajax);
			
			//���ݽ�����ж��Ƿ������ѯ
			//if("���Ƴɹ�" == ajax)
			//{
				$("tr[@id='simQueryBtnTR']").css("display","");
			//}
			parent.dyniframesize();
		});
	}
	parent.dyniframesize();
}

function doQuery()
{
	var devId = $("input[@name='devId']").val();
	var testSelector = $("select[@name='queryServTypeId']").val();
	voipLine = document.getElementById("line1").checked ? "1" : "2";
	//alert(devId);
	//alert(testSelector);
	if('' == devId){
		alert("���Ȳ�ѯ����ѡ���豸");
		return false;
	}
	
	//ҵ�񴥷�
	var url = "<s:url value='/gwms/config/voiceConnectionTest!getValue.action'/>";

	//�һ���ť
	$("button[@name='connQueryBtn']").attr("disabled", true);
	$("button[@name='simQueryBtn']").attr("disabled", true);
	
	//���������Բ���
	if("PhoneConnectivityTest" == testSelector)
	{

		$("tr[@id='connQueryResultTR']").css("display","");
		$("div[@id='connQueryResult']").html("������...");
		$.post(url,{
			deviceId:devId,
			voipLine:voipLine,
			testSelector:testSelector,
			gw_type:gwType
		},function(ajax){
			//alert(ajax);
			//��ѯ���Ϊ"����","δ����"
			//0 ��ҳ�滻�� ���������ߣ�1��ҳ�滻�� ������������
			if(ajax=="0"){
				ajax="����������";
			}
			if(ajax=="1"){
				ajax="������������";
			}
			
			$("div[@id='connQueryResult']").html(ajax);
		});
	}else
	{

		$("tr[@id='simQueryResultTR']").css("display","");
		$("div[@id='simQueryResult']").html("������...");
		$.post(url,{
			deviceId:devId,
			testSelector:testSelector,
			gw_type:gwType
		},function(ajax){
			//alert(ajax);
			var res = ajax.split("###");
			if("��ȡʧ��" != res[0])
			{
				//����״̬ �϶���Complete
				//���Խ��� Success Fail 
				//ý����ģ�ⰴ��ȷ�Ͻ�� true false
				//����ԭ�� CallerFailReason
				var resHtml = "����״̬:"+res[0];
				resHtml = "," + resHtml + "���Խ���" + res[1];
				resHtml = "," + resHtml + "ý����ģ�ⰴ��ȷ�Ͻ��" + res[2];
				if("" != res[3])
				{
					resHtml = "," + resHtml + "����ԭ��" + res[3];
				}
				$("div[@id='simQueryResult']").html(resHtml);
			}else
			{
				$("div[@id='simQueryResult']").html(res[0]);
			}
		});
	}
	parent.dyniframesize();
}
//-->
</script>
<FORM NAME="frm" METHOD="post" action="">
<TABLE border=0 cellspacing=0 cellpadding=0 width="100%" id="myTable">
	<TR>
		<TD HEIGHT="20">&nbsp;</TD>
	</TR>
	<TR>
		<TD>
		<TABLE width="100%" border=0 cellspacing=0 cellpadding=0 align="center">
			<TR>
				<TH colspan="4">���������������</TH>
			</TR>
			<TR>
				<TD colspan="4" bgcolor="#999999">
				<TABLE border=0 cellspacing=1 cellpadding=2 width="100%">
					<TR id="ppp_type_1" bgcolor="#FFFFFF" STYLE="display: none">
						<TD width="15%" class=column align="right">�ն�:</TD>
						<TD width="35%" id="tdDevice">���ѯ��ѡ���豸</TD>
						<TD class=column align="right" nowrap width="15%">����:</TD>
						<TD width="35%" id="tdDeviceCityName"></TD>
					</TR>
					<TR bgcolor="#FFFFFF">
						<TD class=column align="right" nowrap width="15%">ģ��ѡ��:</TD>
						<TD class=column width="35%"><SELECT
							name="queryServTypeId" class="bk" onchange="changeType()">
							<OPTION value="PhoneConnectivityTest">���������Բ���</OPTION>
							<option value="X_CT-COM_SimulateTest">���з������</option>
						</SELECT> <input type="hidden" name="devId" value="<%=deviceId %>"></TD>
						<td nowrap class=column align="right" width="15%">�����˿�:</td>
						<td class=column>
							<input type="radio" name="type" id="line1" class=btn value="1" checked>����1&nbsp;
							<input type="radio" name="type" id="line2" class=btn value="2">����2
						</td>
					</TR>
				</TABLE>
				</TD>
			</TR>

			<tr>
				<TH colspan="4"><label id="testName">���������Բ���</label></TH>
			</tr>
			<tr id="conntest" style="display: ">
				<TD colspan="4" bgcolor="#999999">
				<table border=0 cellspacing=1 cellpadding=2 width="100%">
					<tr bgcolor="#FFFFFF">
						<TD width="100%" class=column align="right" colspan="4">
						<button type="button" name="connSubBtn" onclick="doBusiness()">���</button>
						</TD>
					</tr>
					<tr bgcolor="#FFFFFF" id="connresultTR" style="display: none">
						<TD class=column width="15%" align="right">��Ͻ��:</TD>
						<TD class=column width="85%" colspan="3">
						<DIV id="connresult"></DIV>
						</TD>
					</tr>
					<tr bgcolor="#FFFFFF" id="connQueryBtnTR" style="display: none">
						<TD width="100%" class=column align="right" colspan="4">
						<button type="button" name="connQueryBtn" onclick="doQuery()">
						�� ѯ ״ ̬ </button>
						</TD>
					</tr>
					<tr bgcolor="#FFFFFF" id="connQueryResultTR" style="display: none">
						<TD class=column width="15%" align="right">���Խ��:</TD>
						<TD class=column width="85%" colspan="3">
						<DIV id="connQueryResult"></DIV>
						</TD>
					</tr>
				</table>
				</TD>
			</tr>
			<tr id="simtest" style="display: none">
				<TD colspan="4" bgcolor="#999999">
				<table border=0 cellspacing=1 cellpadding=2 width="100%">
					<tr bgcolor="#FFFFFF">
						<td nowrap class=column align="right" width="15%">�����������:</td>
						<td class=column width="35%"><SELECT name="testType"
							id="testType" class="bk"">
							<OPTION value="Caller" selected>���з���</OPTION>
							<option value="Called">���з���</option>
							<option value="None">ȡ������</option>
						</SELECT></td>
						<td nowrap class=column align="right" width="15%">����ȷ��:</td>
						<td class=column width="35%"><SELECT
							name="dailDTMFConfirmEnable" id="dailDTMFConfirmEnable"
							class="bk"">
							<OPTION value="1" selected>����</OPTION>
							<option value="0">�ر�</option>
						</SELECT></td>
					</tr>
					<tr bgcolor="#FFFFFF">
						<td nowrap class=column align="right" width="15%">���к���:</td>
						<td class=column width="35%"><input type="text" class="bk"
							name="calledNumber" id="calledNumber" value="10000"></td>
						<td nowrap class=column align="right" width="15%">��ý��ģ�ⰴ��:</td>
						<td class=column><input type="text" class="bk"
							name="dailDTMFConfirmNumber" id="dailDTMFConfirmNumber">
						</td>
					</tr>
					<tr bgcolor="#FFFFFF">
						<TD width="100%" class=column align="right" colspan="4">
						<button type="button" name="simSubBtn" onclick="doBusiness()">���</button>
						</TD>
					</tr>
					<tr bgcolor="#FFFFFF" id="simresultTR" style="display:none">
						<TD class=column width="15%" align="right">��Ͻ��:</TD>
						<TD class=column width="85%" colspan="3">
						<DIV id="simresult"></DIV>
						</TD>
					</tr>
					<tr bgcolor="#FFFFFF" id="simQueryBtnTR" style="display:none">
						<TD width="100%" class=column align="right" colspan="4">
						<button type="button" name="simQueryBtn" onclick="doQuery()">
						�� ѯ ״ ̬ </button>
						</TD>
					</tr>
					<tr bgcolor="#FFFFFF" id="simQueryResultTR" style="display:none">
						<TD class=column width="15%" align="right">���Խ��:</TD>
						<TD class=column width="85%" colspan="3">
						<DIV id="simQueryResult"></DIV>
						</TD>
					</tr>
				</table>
				</td>
			</tr>
			<TR>
				<TD colspan="4" bgcolor="#FFFFFF">��ע������ģ�嶨�Ʋ���Ϊʵʱ���ԣ��ն˱������ߡ�</TD>
			</TR>
			<tr>
				<td height="15"></td>
			</tr>
			<TR>
				<TD colspan="4" bgcolor="#999999" id="ppp_type_2"
					style="display: none">
				<TABLE border=0 cellspacing=1 cellpadding=2 width="100%">
					<TR id="resultTR1" bgcolor="#FFFFFF">
						<TD class=column align="right" width="50%" id="resultTD1">���Խ��</TD>
						<TD width="50%" id="resultTD2">
						<DIV id="result2"></DIV>
						</TD>
					</TR>
				</TABLE>
				</TD>
			</TR>
		</TABLE>
		</TD>
	</TR>
</TABLE>
</FORM>