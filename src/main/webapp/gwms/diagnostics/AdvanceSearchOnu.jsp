<%--
�������-�߼���ѯ-ONU
--%>

<%@ page language="java" contentType="text/html; charset=GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ include file="../head.jsp"%>

<SCRIPT LANGUAGE="JavaScript" SRC="<s:url value='/Js/jquery.js'/>"></SCRIPT>
<SCRIPT LANGUAGE="JavaScript" SRC="<s:url value='/Js/CheckForm.js'/>"></SCRIPT>
<SCRIPT LANGUAGE="JavaScript">

var gw_type = '<s:property value="gw_type"/>';

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
	setTimeout("clearResult()", 3000);
}

//config.
function CheckPost() {	
	var deviceId = $("input[@name='deviceId']");
	var ntpserver1 = $("input[@name='ntpserver1']");
	var ntpserver2 = $("input[@name='ntpserver2']");
	var ntpserver3 = $("input[@name='ntpserver3']");
	var ntpserver4 = $("input[@name='ntpserver4']");
	var ntpserver5 = $("input[@name='ntpserver5']");
	var ntpinterval = $("input[@name='ntpinterval']");
	var ntpservertype = $("input[@name='ntpservertype']");
	
	if(!(/^\d+$/.test(ntpinterval.val()))){
		alert("ͬ�����ʱ�����Ϊ��������0��");
		ntpinterval.focus();
		return;
	}
	
	if(ntpservertype.val()!="0"&&ntpservertype.val()!="1"&&ntpservertype.val()!="2"&&ntpservertype.val()!="3"){
		alert("ʱ��ͬ����ʽ����Ϊ0��1��2��3�е�һ����");
		ntpservertype.focus();
		return;
	}
	
	parent.block();
	var url = "<s:url value='/gwms/diagnostics/onuACT!config.action'/>";
	$.post(url,{
		deviceId:deviceId.val(),
		ntpserver1:ntpserver1.val(),
		ntpserver2:ntpserver2.val(),
		ntpserver3:ntpserver3.val(),
		ntpserver4:ntpserver4.val(),
		ntpserver5:ntpserver5.val(),
		ntpinterval:ntpinterval.val(),
		ntpservertype:ntpservertype.val(),
		gw_type:gw_type
	},function(ajax){
		document.all("tr002").style.display = ""
		$("td[@id='td_save_result']").html(ajax);
		parent.dyniframesize();
        parent.unblock();
	});

	setTimeout("clearResult()", 3000);
}

function clearResult() {
	$("div[@id='operResult']").html("");
	document.all("operResult").style.display = "none"
}
</SCRIPT>

<BODY>

<FORM NAME="frm" METHOD="post">
<TABLE style="width: 100%;" border=0 cellspacing=0 cellpadding=0>
	<tr bgcolor=#ffffff>
		<td align=right>
			<div id="operResult" style='width:20%;display:none;background-color:#33CC00'></div>
		</td>
	</tr>
	<TR>
		<TD bgcolor=#999999>
			<TABLE style="width: 100%;" border=0 cellspacing=1 cellpadding=2>
				<TR align="center">
					<TH colspan="4">�豸ʱ��ͬ������</TH>
				</TR>
				<TR bgcolor="#FFFFFF">
				    <TD align="right">ʱ�������1��ַ</TD>
					<TD align="left">
					<s:if test="ntpserver1=='undefined'||ntpserver1=='null'">
					<input type="text" name="ntpserver1"/>
					</s:if>
					<s:else>
					<input type="text" name="ntpserver1" value="<s:property value='ntpserver1'/>"/>
					</s:else>						
					</TD>
					<TD align="right">ʱ�������2��ַ</TD>
					<TD align="left">
					<s:if test="ntpserver2=='undefined'||ntpserver2=='null'">
					<input type="text" name="ntpserver2"/>
					</s:if>
					<s:else>
					<input type="text" name="ntpserver2" value="<s:property value='ntpserver2'/>"/>
					</s:else>						
					</TD>
				</TR>
				<TR bgcolor="#FFFFFF">
				    <TD align="right">ʱ�������3��ַ</TD>
					<TD align="left">
					<s:if test="ntpserver3=='undefined'||ntpserver3=='null'">
					<input type="text" name="ntpserver3"/>
					</s:if>
					<s:else>
					<input type="text" name="ntpserver3" value="<s:property value='ntpserver3'/>"/>
					</s:else>						
					</TD>
					<TD align="right">ʱ�������4��ַ</TD>
					<TD align="left">
					<s:if test="ntpserver4=='undefined'||ntpserver4=='null'">
					<input type="text" name="ntpserver4"/>
					</s:if>
					<s:else>
					<input type="text" name="ntpserver4" value="<s:property value='ntpserver4'/>"/>
					</s:else>						
					</TD>
				</TR>
				<TR bgcolor="#FFFFFF">
				    <TD align="right">ʱ�������5��ַ</TD>
					<TD align="left">
					<s:if test="ntpserver5=='undefined'||ntpserver5=='null'">
					<input type="text" name="ntpserver5"/>
					</s:if>
					<s:else>
					<input type="text" name="ntpserver5" value="<s:property value='ntpserver5'/>"/>
					</s:else>						
					</TD>
					<TD align="right">ͬ�����ʱ��</TD>
					<TD align="left">
						<input type="text" name="ntpinterval" value="<s:property value='ntpinterval'/>"/>
						<img src="../../images/attention_2.gif" width="15" height="12" title="Ĭ�ϣ�86400���룩�����Ϊ0��ʾ���Զ���������ͬ��"/>
					</TD>
				</TR>
				<TR bgcolor="#FFFFFF">
				    <TD align="right">ʱ��ͬ����ʽ</TD>
					<TD align="left" colspan="3">
						<input type="text" name="ntpservertype" value="<s:property value='ntpservertype'/>"/>
						��ʽ��0,1,2,3��Ĭ�ϣ�0
						<img src="../../images/attention_2.gif" width="15" height="12" title="0��ͨ������ͨ����ʱ�ӷ�����ͬ����1��ͨ������ͨ����ʱ�ӷ�����ͬ����2��ͨ������ͨ����ʱ�ӷ�����ͬ����3��ͨ������ͨ����ʱ�ӷ�����ͬ����"/>
					</TD>
				</TR>
				<TR bgcolor="#FFFFFF">
					<TD colspan="4" class="green_foot" align=right>
					<INPUT NAME="deviceId" TYPE="hidden"  value="<s:property value='deviceId'/>">
					<input id="button_save" type="button" value=" �� �� " class="jianbian" onclick="javascript:CheckPost();"></TD>
				</TR>
				<TR bgcolor="#FFFFFF" id="tr002" style="display: none">
					<td align="center" colspan="2">
					�������
					</td>
					<td align="center" id="td_save_result" colspan="2">
					</td>
				</TR>
			</TABLE>
		</TD>
	</TR>
</TABLE>
</FORM>
</BODY>
