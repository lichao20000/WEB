<%@ page language="java" contentType="text/html; charset=GBK" pageEncoding="GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@taglib prefix="ms" uri="/ailk-itms-web-tags"%>
<%@ taglib prefix="lk" uri="/linkage"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<title>�豸��������ͳ��</title>
<link rel="stylesheet" href="<s:url value="/css3/c_table.css"/>" type="text/css">
<link rel="stylesheet" href="<s:url value="/css3/global.css"/>" type="text/css">
<script type="text/javascript" src="<s:url value="/Js/jquery.js"/>"></script>
<SCRIPT LANGUAGE="JavaScript" SRC="<%=request.getContextPath()%>/Js/CheckFormForm.js"></SCRIPT>
<script type="text/javascript" src="<s:url value="/Js/My97DatePicker/WdatePicker.js"/>"></script>
<% 
	request.setCharacterEncoding("GBK");
	String gw_type = request.getParameter("gw_type");
%>
<script type="text/javascript">
function query() 
{
	var mainForm = document.getElementById("selectForm");
	mainForm.action = "<s:url value='DevBatchRestartInfoListStat.jsp' />";
	mainForm.submit();
	isButn(false);
}

function isButn(flag)
{
	if(flag){
		$("button[@id='btn']").css("display", "");
	}else{
		$("button[@id='btn']").css("display", "none");
	}
}


/*------------------------------------------------------------------------------
//������:		deviceSelect_change_select
//����  :	type 
      vendor      �����豸����
      deviceModel �����豸�ͺ�
      devicetype  �����豸�汾
//����  :	����ҳ������̡��ͺż�����
//����ֵ:		
//˵��  :	
//����  :	Create 2009-12-25 of By qxq
------------------------------------------------------------------------------*/
function gwShare_change_select(type,selectvalue)
{
	var gwType = $("input[@name='gw_type']").val();
	switch (type)
	{
		case "vendor":
			var url;
			if("4" == gwType){
				//�����в�ѯ���̱�
				url = "<s:url value="/gtms/stb/share/shareDeviceQuery!getVendor.action"/>";
			}else{
				url = "<s:url value="/gwms/share/gwDeviceQuery!getVendor.action"/>";
			}
			$.post(url,{
			},function(ajax){
				gwShare_parseMessage(ajax,$("select[@name='gwShare_vendorId']"),selectvalue);
				$("select[@name='gwShare_deviceModelId']").html("<option value='-1'>==����ѡ���豸����==</option>");
				$("select[@name='gwShare_devicetypeId']").html("<option value='-1'>==����ѡ���豸�ͺ�==</option>");
			});
			break;
		case "deviceModel":
			var url;
			if("4" == gwType){
				//�����в�ѯ�豸�ͺ�
				url = "<s:url value="/gtms/stb/share/shareDeviceQuery!getDeviceModel.action"/>";
			}else{
				url = "<s:url value="/gwms/share/gwDeviceQuery!getDeviceModel.action"/>";
			}
			var vendorId = $("select[@name='gwShare_vendorId']").val();
			if("-1"==vendorId){
				$("select[@name='gwShare_deviceModelId']").html("<option value='-1'>==����ѡ����==</option>");
				$("select[@name='gwShare_devicetypeId']").html("<option value='-1'>==����ѡ���豸�ͺ�==</option>");
				break;
			}
			$.post(url,{
				gwShare_vendorId:vendorId
			},function(ajax){
				gwShare_parseMessage(ajax,$("select[@name='gwShare_deviceModelId']"),selectvalue);
				$("select[@name='gwShare_devicetypeId']").html("<option value='-1'>==����ѡ���豸�ͺ�==</option>");
			});
			break;
		case "devicetype":
			var url;
			if("4" == gwType){
				//�����в�ѯ�汾��
				url = "<s:url value="/gtms/stb/share/shareDeviceQuery!getDevicetype.action"/>";
			}else{
				url = "<s:url value="/gwms/share/gwDeviceQuery!getDevicetype.action"/>";
			}
			var vendorId = $("select[@name='gwShare_vendorId']").val();
			var deviceModelId = $("select[@name='gwShare_deviceModelId']").val();
			if("-1"==deviceModelId){
				$("select[@name='gwShare_devicetypeId']").html("<option value='-1'>==����ѡ���豸�ͺ�==</option>");
				break;
			}
			$.post(url,{
				gwShare_vendorId:vendorId,
				gwShare_deviceModelId:deviceModelId
			},function(ajax){
				gwShare_parseMessage(ajax,$("select[@name='gwShare_devicetypeId']"),selectvalue);
			});
			break;	
		default:
			alert("δ֪��ѯѡ�");
			break;
	}	
}

//������ѯ�豸�ͺŷ���ֵ�ķ���
function gwShare_parseMessage(ajax,field,selectvalue)
{
	if(""==ajax){
		return;
	}
	
	var flag = true;
	
	var lineData = ajax.split("#");
	if(!typeof(lineData) || !typeof(lineData.length)){
		return false;
	}
	field.html("");
	option = "<option value='-1' selected>==��ѡ��==</option>";
	field.append(option);
	for(var i=0;i<lineData.length;i++)
	{
		var oneElement = lineData[i].split("$");
		var xValue = oneElement[0];
		var xText = oneElement[1];
		if(selectvalue==xValue){
			flag = false;
			//����ÿ��value��text��ǵ�ֵ����һ��option����
			option = "<option value='"+xValue+"' selected>=="+xText+"==</option>";
		}else{
			//����ÿ��value��text��ǵ�ֵ����һ��option����
			option = "<option value='"+xValue+"'>=="+xText+"==</option>";
		}
		
		try{
			field.append(option);
		}catch(e){
			alert("�豸�ͺż���ʧ�ܣ�");
		}
	}
	
	if(flag){
		field.attr("value","-1");
	}
}

//** iframe�Զ���Ӧҳ�� **//
//������ϣ������ҳ��߶��Զ������߶ȵ�iframe�����Ƶ��б�
//�ö��Ű�ÿ��iframe��ID�ָ�. ����: ["myframe1", "myframe2"]������ֻ��һ�����壬���ö��š�
//����iframe��ID
var iframeids = [ "dataForm" ]

//����û����������֧��iframe�Ƿ�iframe���� yes ��ʾ���أ�no��ʾ������
var iframehide = "yes"

function dyniframesize() {
	var dyniframe = new Array()
	for (i = 0; i < iframeids.length; i++) {
		if (document.getElementById) {
			//�Զ�����iframe�߶�
			dyniframe[dyniframe.length] = document.getElementById(iframeids[i]);
			if (dyniframe[i] && !window.opera) {
				dyniframe[i].style.display = "block"
				//����û����������NetScape
				if (dyniframe[i].contentDocument
						&& dyniframe[i].contentDocument.body.offsetHeight)
					dyniframe[i].height = dyniframe[i].contentDocument.body.offsetHeight;
				//����û����������IE
				else if (dyniframe[i].Document
						&& dyniframe[i].Document.body.scrollHeight)
					dyniframe[i].height = dyniframe[i].Document.body.scrollHeight;
			}
		}
		//�����趨�Ĳ���������֧��iframe�����������ʾ����
		if ((document.all || document.getElementById) && iframehide == "no") {
			var tempobj = document.all ? document.all[iframeids[i]]
					: document.getElementById(iframeids[i])
			tempobj.style.display = "block"
		}
	}
}

$(function() {
	dyniframesize();
	gwShare_change_select("vendor","-1");
});

$(window).resize(function() {
	dyniframesize();
});
</script>
</head>

<body>
<form id="selectForm" name="selectForm" action="" target="dataForm" onSubmit="return false;">
	<input name="id" type="hidden" value="">
	<input type="hidden" name="gw_type" value="<%=gw_type%>" />
	<input type="hidden" name="city_id" value='<s:property value="city_id" />' />
	<table>
		<tr><td HEIGHT=20>&nbsp;</td></tr>
		<tr>
			<td>
				<table class="green_gargtd">
					<tr>
						<%if(!"1".equals(gw_type)){ %>
							<td width="162" align="center" class="title_bigwhite">
								������������������ͳ��
							</td>
						<%}else{ %>	
							<td width="162" align="center" class="title_bigwhite">
								��è������������ͳ��
							</td>
						<%}%>	
						<td>
							<img src="<s:url value="/images/attention_2.gif"/>"
								width="15" height="12" />
						</td>
					</tr>
				</table>
			</td>
		</tr>
		<tr>
			<td>
				<table class="querytable">
					<TR>
						<%if(!"1".equals(gw_type)){ %>
							<th colspan="4">������������������ͳ�� </th>
						<%}else{ %>	
							<th colspan="4">��è������������ͳ�� </th>
						<%}%>	
					</tr>
					<TR>
						<TD class=column width="15%" align='right'>����ʱ��</TD>
						<TD width="35%" colspan="3">
							<input type="text" name="startOpenDate" readonly class=bk
								value="<s:property value="startOpenDate" />">
							<img name="shortDateimg"
								onClick="WdatePicker({el:document.selectForm.startOpenDate,dateFmt:'yyyy-MM-dd',skin:'whyGreen'})"
								src="../../images/dateButton.png" width="15" height="12"
								border="0" alt="ѡ��">
							 &nbsp;&nbsp; ~ &nbsp;&nbsp;
							<input type="text" name="endOpenDate" readonly class=bk
								value="<s:property value="endOpenDate" />">
							<img name="shortDateimg"
								onClick="WdatePicker({el:document.selectForm.endOpenDate,dateFmt:'yyyy-MM-dd',skin:'whyGreen'})"
								src="../../images/dateButton.png" width="15" height="12"
								border="0" alt="ѡ��">
						</TD>
					</TR>
					<TR>
						<TD align="right" class=column width="15%">��    ��</TD>
						<TD width="35%">
							<select name="gwShare_vendorId" class="bk" onchange="gwShare_change_select('deviceModel','-1')">
								<option value="-1">==��ѡ��==</option>
							</select>
						</TD>
						<TD align="right" class=column width="15%">�豸�ͺ�</TD>
						<TD align="left" width="35%">
							<select name="gwShare_deviceModelId" class="bk" onchange="gwShare_change_select('devicetype','-1')">
								<option value="-1">����ѡ����</option>
							</select>
						</TD>
					</TR>
					<TR>	
						<TD align="right" class=column width="15%">�豸�汾</TD>
						<TD width="35%">
							<select name="gwShare_devicetypeId" class="bk"">
								<option value="-1">����ѡ���豸�ͺ�</option>
							</select>
						</TD>
						<td colspan="2" align="right" class=foot>
							<button id="btn" onclick="query()">&nbsp;��&nbsp;ѯ&nbsp;</button>
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
				<iframe id="dataForm" name="dataForm" height="0"
					frameborder="0" scrolling="no" width="100%" src="">
				</iframe>
			</td>
		</tr>
		<tr>
			<td height="25"></td>
		</tr>
	</table>
	<br>
</form>
</body>
</html>
<%@ include file="../../foot.jsp"%>