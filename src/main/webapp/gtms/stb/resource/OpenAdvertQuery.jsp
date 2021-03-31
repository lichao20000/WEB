<%@ page language="java" contentType="text/html; charset=GBK"
	pageEncoding="GBK"%>
<%@taglib prefix="s" uri="/struts-tags"%>
<%@taglib prefix="lk" uri="/linkage"%>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<title>��������ʷ������Ϣ��ѯ</title>
<lk:res />
<link href="/lims/css/css_blue.css" rel="stylesheet" type="text/css">

<link href="/lims/css2/global.css" rel="stylesheet" type="text/css">
<link href="/lims/css2/c_table.css" rel="stylesheet" type="text/css">
<%
	String vendorId = request.getParameter("vendorId");
	if(vendorId==null||"".equals(vendorId)){
		vendorId ="-1";
	}
// 	String queryFlag = request.getParameter("queryFlag");
%>
<script type="text/javascript">

	function doQuery(){
		var taskid = $("input[@name='taskId']").val();
		var taskname = $("input[@name='taskName']").val();
		if(taskid=="" && taskname==""){
			alert("�������ơ������ű�����һ��");
			return;
		}
		document.selectForm.action = "<s:url value='/gtms/stb/resource/openAdvertQuery!queryAdvertResultCount.action'/>";
		document.selectForm.submit();
	}

$(function(){
	var vendorId = "<%=vendorId%>";
	deviceSelect_change_select("vendor",vendorId);
	
});

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
function deviceSelect_change_select(type,selectvalue){
	switch (type){
		case "vendor":
			var url = "<s:url value='/gtms/stb/resource/openAdvertQuery!getVendor.action'/>";
			$.post(url,{
			},function(ajax){
				deviceSelect_parseMessage(ajax,$("select[@name='vendorId']"),selectvalue);
				$("select[@name='deviceModelId']").html("<option value='-1'>==����ѡ���豸����==</option>");
				$("select[@name='deviceTypeId']").html("<option value='-1'>==����ѡ���豸�ͺ�==</option>");
			});
			break;
		case "deviceModel":
			var url = "<s:url value='/gtms/stb/resource/openAdvertQuery!getDeviceModel.action'/>";
			var vendorId = $("select[@name='vendorId']").val();
			$.post(url,{
				vendorId:vendorId
			},function(ajax){
				deviceSelect_parseMessage(ajax,$("select[@name='modelId']"),selectvalue);
				$("select[@name='deviceTypeId']").html("<option value='-1'>==����ѡ���豸�ͺ�==</option>");
			});
			break;
		default:
			alert("δ֪��ѯѡ�");
			break;
	}
}
	/*------------------------------------------------------------------------------
	//������:		deviceSelect_parseMessage
	//����  :	ajax 
	            	������XXX$XXX#XXX$XXX
	            field
	            	��Ҫ���ص�jquery����
	//����  :	����ajax���ز���
	//����ֵ:		
	//˵��  :	
	//����  :	Create 2009-12-25 of By qxq
	------------------------------------------------------------------------------*/
	//������ѯ�豸�ͺŷ���ֵ�ķ���
	function deviceSelect_parseMessage(ajax, field, selectvalue) {
		var flag = true;
		if ("" == ajax) {
			return;
		}
		var lineData = ajax.split("#");
		if (!typeof (lineData) || !typeof (lineData.length)) {
			return false;
		}
		field.html("");
		if (selectvalue == "-1") {
			field.append("<option value='-1' selected>==��ѡ��==</option>");
		} else {
			field.append("<option value='-1'>==��ѡ��==</option>");
		}
		for ( var i = 0; i < lineData.length; i++) {
			var oneElement = lineData[i].split("$");
			var xValue = oneElement[0];
			var xText = oneElement[1];
			if (selectvalue == xValue) {
				flag = false;
				//����ÿ��value��text��ǵ�ֵ����һ��option����
				option = "<option value='"+xValue+"' selected>==" + xText
						+ "==</option>";
			} else {
				//����ÿ��value��text��ǵ�ֵ����һ��option����
				option = "<option value='"+xValue+"'>==" + xText
						+ "==</option>";
			}
			try {
				field.append(option);
			} catch (e) {
				alert("�豸�ͺż���ʧ�ܣ�");
			}
		}
		if (flag) {
			field.attr("value", "-1");
		}
	}
</script>
</head>
<body>
	<form id="selectForm" name="selectForm" action="" target="first"
		method="post">
		<input type="hidden" name="status" value="6">
		<table class="querytable" align="center" width="98%" id="tabs">
			<tr>
				<td class="title_1" colspan="4">��������·����ͳ��</td>
			</tr>
			<tr>
				<td class="title_2" width="15%">�������ƣ�</td>
				<td><input type="text" maxlength="50" name="taskName"
					id="taskname" /></td>
				<td class="title_2" width="15%">�����ţ�</td>
				<td><input type="text" maxlength="50" name="taskId"
					id="taskid" /></td>
			</tr>
			<tr>
				<td class="title_2" width="15%">���̣�</td>
				<td><select name="vendorId" class="bk"
					onchange="deviceSelect_change_select('deviceModel','-1')"
					style="width: 200px">
					<option value="-1">
						==��ѡ��==
					</option>
				</select></td>

				<td class="title_2" width="15%">�ͺ�</td>
				<td><select name="modelId" class="bk" style="width: 200px">
						<option value="-1">==����ѡ����==</option>
				</select></td>
			</tr>
			<tr align="right">
				<td colspan="4" class="foot" align="right">
					<div class="right">
						<button type="button" onclick="doQuery()">��ѯ</button>
						&nbsp;&nbsp;
					</div>
				</td>
			</tr>

		</table>
	</form>


	<TABLE width="98%" border=0 cellspacing=1 cellpadding=0 align="center">
		<TR>
			<TD bgcolor=#999999 id="td"><iframe id="first" name="first"
					height="0" frameborder="0" scrolling="no" width="100%" src=""></iframe></TD>
		</TR>
	</TABLE>



</body>


<SCRIPT LANGUAGE="JavaScript">
	//** iframe�Զ���Ӧҳ�� **//
	//������ϣ������ҳ��߶��Զ������߶ȵ�iframe�����Ƶ��б�
	//�ö��Ű�ÿ��iframe��ID�ָ�. ����: ["myframe1", "myframe2"]������ֻ��һ�����壬���ö��š�
	//����iframe��ID
	var iframeids = [ "first" ];

	//����û����������֧��iframe�Ƿ�iframe���� yes ��ʾ���أ�no��ʾ������
	var iframehide = "yes";

	function dyniframesize() {
		var dyniframe = new Array()
		for (i = 0; i < iframeids.length; i++) {
			if (document.getElementById) {
				//�Զ�����iframe�߶�
				dyniframe[dyniframe.length] = document
						.getElementById(iframeids[i]);
				if (dyniframe[i] && !window.opera) {
					dyniframe[i].style.display = "block";
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
				tempobj.style.display = "block";
			}
		}
	}
	$(function() {
		dyniframesize();
	});

	$(window).resize(function() {
		dyniframesize();
	});
</SCRIPT>
</html>