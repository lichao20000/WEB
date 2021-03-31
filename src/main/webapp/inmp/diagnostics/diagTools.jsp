<%@ page language="java" contentType="text/html; charset=GBK"%>
<%@taglib prefix="s" uri="/struts-tags"%>
<%@ include file="../timelater.jsp"%>
<html>
	<head>
		<link href="../../css/inmp/css/css_green.css" rel="stylesheet" type="text/css">
		<SCRIPT LANGUAGE="JavaScript" SRC="../../Js/inmp/jquery.js"></SCRIPT>
		<SCRIPT LANGUAGE="JavaScript" SRC="../../Js/inmp/CheckForm.js"></SCRIPT>
		<title>��Ϲ���</title>
<script type="text/javascript">
<!--//
function IsNull(strValue,strMsg){
	if(Trim(strValue).length > 0) return true;
	else{
		alert(strMsg + "����Ϊ��");
		return false;
	}
}

function IsNumber(strValue,strMsg){
	var bz = true;
	if(IsNull(strValue,strMsg)){
		for(var i=0;i < strValue.length;i++){
			var ch = strValue.substring(i,i+1);
			if(ch<'0' || ch>'9'){
				alert(strMsg+'ӦΪ��С��0������');
				bz = false;
				break;
			}
		}
	}else{
		bz = false;
	}
	if(bz){return true;}
	else{return false;}
}
function Trim(strValue){
	var v = strValue;
	var i = 0;
	while(i<v.length){
		if(v.substring(i,i+1) != ' '){
			v = v.substring(i,v.length);
			break;
		}
		i = i + 1;
		if(i==v.length){
			v="";
		}
	}
	i = v.length;
	while(i>0){
		if(v.substring(i-1,i) != ' '){
			v = v.substring(0,i);
			break;
		}
		i = i - 1;
	}
	
	return v;
}
//ҵ�����ͣ�����
var serv_type_id = "10";
var device_id = '<s:property value="deviceId"/>';
var user_id = '<s:property value="userId"/>';
var gw_type= '<s:property value="gw_type"/>' ;

//pingDNS��ַ
function ping(){
	var ping_addr = $("input[@name='pingAddr']").val();
	var package_size = $("input[@name='packageSize']").val();
	var ping_times = $("input[@name='pingTimes']").val();
	if(false == IsNull(ping_addr,"pingĿ�ĵ�ַ")){
		return false;
	}
	if(false == IsNull(package_size,"����С")){
		return false;
	}
	if(false == IsNumber(package_size,"����С")){
		return false;
	}
	if(false == IsNull(ping_times,"ping����")){
		return false;
	}
	if(false == IsNumber(ping_times,"ping����")){
		return false;
	}
	if(ping_times > 10){
		alert("ping�������ܳ���20");
		return false;
	}
	parent.block1();
	$("div[@id='divPing']").html("");
	var diagUrl = '<s:url value="/inmp/diagnostics/diagTools!ping.action"/>';
	//��ѯ
	$.post(diagUrl,{
		deviceId:device_id,
		pingAddr:ping_addr,
		packageSize:package_size,
		gw_type:gw_type,
		times:ping_times
	},function(ajaxMesg){
		//alert(ajaxMesg);
		$("div[@id='divPing']").append(ajaxMesg);
		parent.dyniframesize1();
		parent.unblock1();
	});
}

//����
function reboot(){
	parent.block1();
	$("div[@id='divReboot']").html("");
	var diagUrl = '<s:url value="/inmp/diagnostics/diagTools!reboot.action"/>';
	//��ѯ
	$.post(diagUrl,{
		deviceId:device_id,
		gw_type:gw_type
	},function(ajaxMesg){
		//alert(ajaxMesg);
		$("div[@id='divReboot']").append(ajaxMesg);
		parent.dyniframesize1();
		parent.unblock1();
	});
}

//�ָ���������
function factoryReset(){
	parent.block1();
	$("div[@id='divReset']").html("");
	var diagUrl = '<s:url value="/inmp/diagnostics/diagTools!factoryReset.action"/>';
	//��ѯ
	$.post(diagUrl,{
		deviceId:device_id,
		gw_type:gw_type
	},function(ajaxMesg){
		//alert(ajaxMesg);
		$("div[@id='divReset']").append(ajaxMesg);
		parent.dyniframesize1();
		parent.unblock1();
	});
}

//�ϴ���־
function upLogFile(obj){
	parent.block1();
	$("div[@id='divUpLogFile']").html("");
	var diagUrl = '<s:url value="/inmp/diagnostics/diagTools!upLogFile.action"/>';
	var link = '<s:url value="/inmp/diagnostics/WorkSheetView_device.jsp"/>'
	var ajx = '';
	$.post(diagUrl,{
		deviceId:device_id,
		gw_type:gw_type
	},function(ajaxMesg){
		//alert(ajaxMesg);
		if('1' == ajaxMesg){
			ajx = " ���óɹ�, <a href='" + link + "' target='_blank'>�鿴����ִ�����</a>";
		}else{
			ajx = " <font color=red>���ú�̨ʧ��</font>";
		}
		$("div[@id='divUpLogFile']").append(ajx);
		parent.dyniframesize1();
		parent.unblock1();
	});
}

//��������
function openStream(){
	var diagUrl = '<s:url value="/inmp/diagnostics/diagTools!openStream.action"/>';
	$.post(diagUrl,{
		deviceId:device_id,
		gw_type:gw_type
	},function(ajaxMesg){
		alert(ajaxMesg);
	});
}

//�����ر�
function closeStream(){
	var diagUrl = '<s:url value="/inmp/diagnostics/diagTools!closeStream.action"/>';
	$.post(diagUrl,{
		deviceId:device_id,
		gw_type:gw_type
	},function(ajaxMesg){
		alert(ajaxMesg);
	});
}

//�����������
function clearStream(){
	var diagUrl = '<s:url value="/inmp/diagnostics/diagTools!clearStream.action"/>';
	$.post(diagUrl,{
		deviceId:device_id
	},function(ajaxMesg){
		alert(ajaxMesg);
	});
}

//�鿴��������
function showInterStream(){
	var page = '<s:url value="/inmp/diagnostics/diagTools!showInterStream.action"/>';
	page += "?deviceId=" + device_id;
	var height = 600;
	var width = 800;
	var left = (screen.width-width)/2;
	var top  = (screen.height-height)/2;
	var w = window.open(page,"ss","left="+left+",top="+top+",width="+width+",height="+height+",resizable=yes,scrollbars=yes,toolbar=no");
}

//�鿴��ʷping��Ϣ
function showPing(){
	var page = '<s:url value="/inmp/diagnostics/pingResult.action"/>';
	page += "?deviceId=" + device_id;
	var height = 600;
	var width = 800;
	var left = (screen.width-width)/2;
	var top  = (screen.height-height)/2;
	var w = window.open(page,"ss","left="+left+",top="+top+",width="+width+",height="+height+",resizable=yes,scrollbars=yes,toolbar=no");

}
//-->
</script>
	</head>
	<body>
		<form name="frm" action="diagTools">
			<TABLE width="100%" border=0 cellspacing=0 cellpadding=0
				align="center">
				<TR>
					<TD bgcolor=#999999>
						<table border=0 cellspacing=1 cellpadding=1 width="100%"
							id="myTable">
							<tr CLASS="green_title" height=25>
								<td width="20%">
									PING����
								</td>
								<td align="right">
									<input type="button" value="��ʷping�����鿴" onclick="showPing()">
									&nbsp; &nbsp;&nbsp;&nbsp;&nbsp;
									<input type="button" value="ִ ��" onclick="ping()">
									&nbsp;
								</td>
							</tr>
							<tr bgcolor=#ffffff height=25>
								<td colspan="2" bgcolor=#999999>
									<table width="100%" border=0 cellspacing=0 cellpadding=0
										align="center">
										<tr bgcolor=#ffffff>
											<td class=column align="right" width="20%">
												Ŀ�ĵ�ַ&nbsp;
											</td>
											<td>
												&nbsp;
												<input type="text" name="pingAddr">
												&nbsp;&nbsp;
												<font color="red">������IP</font>
											</td>
											<td class=column align="right">
												����С&nbsp;
											</td>
											<td>
												&nbsp;
												<input type="text" name="packageSize" value="200" size=15>
											</td>
											<td class=column align="right">
												����&nbsp;
											</td>
											<td>
												&nbsp;
												<input type="text" name="pingTimes" value="5" size=15>
											</td>
										</tr>
									</table>
								</td>
							</tr>
							<tr height=25 bgcolor=#ffffff>
								<td class=column align="right">
									PING���Խ��&nbsp;
								</td>
								<td bgcolor=#ffffff>
									&nbsp;
									<div id="divPing"></div>
								</td>
							</tr>
							<tr CLASS="" height=25 bgcolor=#ffffff>
								<td>
									Զ������
								</td>
								<td align="right">
									<input type="button" value="ִ ��" onclick="reboot()">
									&nbsp;
								</td>
							</tr>
							<tr height=25 bgcolor=#ffffff>
								<td class=column align="right">
									Զ���������&nbsp;
								</td>
								<td bgcolor=#ffffff>
									&nbsp;
									<div id="divReboot"></div>
								</td>
							</tr>

							<tr CLASS="" height=25 bgcolor=#ffffff>
								<td>
									�ָ���������
								</td>
								<td align="right">
									<input type="button" value="ִ ��" onclick="factoryReset()">
									&nbsp;
								</td>
							</tr>
							<tr height=25 bgcolor=#ffffff>
								<td class=column align="right">
									�ָ��������ý��&nbsp;
								</td>
								<td bgcolor=#ffffff>
									&nbsp;
									<div id="divReset"></div>
								</td>
							</tr>

							<tr CLASS="" height=25 bgcolor=#ffffff>
								<td>
									�ϴ���־�ļ�
								</td>
								<td align="right">
									<input type="button" value="ִ ��" onclick="upLogFile()">
									&nbsp;
								</td>
							</tr>
							<tr height=25 bgcolor=#ffffff>
								<td class=column align="right">
									�ϴ���־�ļ����&nbsp;
								</td>
								<td bgcolor=#ffffff>
									&nbsp;
									<div id="divUpLogFile"></div>
								</td>
							</tr>

							<tr CLASS="" height=25 bgcolor=#ffffff>
								<td>
									������������
								</td>
								<td align="right">
									<input type="button" value="�� ��" onclick="openStream()">
									&nbsp;&nbsp;
									<input type="button" value="�� ��" onclick="closeStream()">
									&nbsp;&nbsp;
								</td>
							</tr>
							<tr height=25 bgcolor=#ffffff>
								<td align="right" class=column>
									�����������&nbsp;
								</td>
								<td>
									<input type="button" onclick="showInterStream()"
										value="�鿴ACS���豸��������">
									&nbsp;&nbsp;
									<input type="button" onclick="clearStream()" value="�����¼">
									<!-- &nbsp&nbsp<a href='#' onclick='showInterStream()'>�鿴ACS���豸��������</a>
							&nbsp&nbsp&nbsp&nbsp<a href='#' onclick='clearStream()'>�����¼</a> -->
								</td>
							</tr>

						</table>
					</TD>
				</TR>
			</TABLE>
		</form>
	</body>
</html>