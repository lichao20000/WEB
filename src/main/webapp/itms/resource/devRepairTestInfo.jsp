<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ include file="/timelater.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="lk" uri="/linkage"%>
<%@ include file="/toolbar.jsp"%>
<%
String select_type = request.getParameter("select_type"); 
%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK" />
<title>�ն�ά�޼�����ͳ��</title>
<link href="<s:url value="/css/uploadAndParse.css"/>" rel="stylesheet" type="text/css"></link>
<script type="text/javascript" src="<s:url value="/Js/jquery.js"/>"></script>
<script type="text/javascript" src="<s:url value="/Js/My97DatePicker/WdatePicker.js"/>"></script>
<script type="text/javascript">
var select_type = '<%=select_type%>';

	$(function() {
		if ("1" != select_type) {
			document.title = '���������ն�ά�޼�����';
			document.getElementById("it_webtt").innerHTML = "���������ն�";
		} else {
			gwShare_change_select("vendor","-1");
		}
		dyniframesize();
	});

	function queryRepairDev() {
		if ("1" != select_type) {
			var deviceSerialnumber = trim($(
					"input[@name='device_serialnumber']").val());
			if ("" == deviceSerialnumber || deviceSerialnumber.length < 6) {
				alert("�豸���к��������������6λ��");
				return false;
			}
		} else {
			var attribution_city = $("select[@name='attribution_city']").val();
			if ("-1" == attribution_city) {
				alert("��ѡ�������У�");
				return false;
			}
		}
		document.getElementById("queryButton").disabled = true;
		var form = document.getElementById("form");
		form.action = "<s:url value='/itms/resource/devRepairTestInfo!queryRepairDev.action'/>";
		form.submit();
	}

	function exportQueryRepairDev() {
		var attribution_city = $("select[@name='attribution_city']").val();
		if ("-1" == attribution_city) {
			alert("��ѡ�������У�");
			return false;
		}
		var form = document.getElementById("form");
		form.action = "<s:url value='/itms/resource/devRepairTestInfo!parseExcel.action'/>";
		form.submit();
	}

	function trim(str) {
		return str.replace(/(^\s*)|(\s*$)/g, "");
	}

	function getSendCity() {

		var url = "<s:url value='/itms/resource/devRepairTestInfo!getSendCity.action'/>";
		var attribution_city = $("select[@name='attribution_city']").val();
		if ("-1" == attribution_city) {
			$("select[@name='send_city']").html(
					"<option value='-1'>==����ѡ��������==</option>");
			return;
		}
		$.post(url, {
			attribution_city : attribution_city
		}, function(ajax) {
			gwShare_parseMessage1(ajax, $("select[@name='send_city']"));
		});
	}

	function gwShare_change_select(type,selectvalue){
	switch (type){
		case "vendor":
			var url = "<s:url value='/gwms/share/gwDeviceQuery!getVendor.action'/>";
			$.post(url,{
			},function(ajax){
				gwShare_parseMessage(ajax,$("select[@name='device_vendor']"),selectvalue);
			});
			break;
		case "deviceModel":
			var url = "<s:url value='/gwms/share/gwDeviceQuery!getDeviceModel.action'/>";
			var vendorId = $("select[@name='device_vendor']").val();
			if("-1"==vendorId){
				$("select[@name='device_model']").html("<option value='-1'>==����ѡ���豸����==</option>");
				break;
			}
			$.post(url,{
				gwShare_vendorId:vendorId
			},function(ajax){
				gwShare_parseMessage(ajax,$("select[@name='device_model']"),selectvalue);
			});
			break;
		default:
			alert("δ֪��ѯѡ�");
			break;
		}	
	}

	//������ѯ�豸�ͺŷ���ֵ�ķ���
	function gwShare_parseMessage(ajax,field,selectvalue){
		var flag = true;
		if(""==ajax){
			return;
		}
		var lineData = ajax.split("#");
		if(!typeof(lineData) || !typeof(lineData.length)){
			return false;
		}
		field.html("");

		option = "<option value='-1' selected>==��ѡ��==</option>";
		field.append(option);
		for(var i=0;i<lineData.length;i++){
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

	//������ѯ�豸�ͺŷ���ֵ�ķ���
	function gwShare_parseMessage1(ajax, field, selectvalue) {
		if ("" == ajax) {
			return;
		}
		var lineData = ajax.split("#");
		if (!typeof (lineData) || !typeof (lineData.length)) {
			return false;
		}
		field.html("");
		var option = "<option value='-1' selected>==��ѡ��==</option>";
		field.append(option);
		for (var i = 0; i < lineData.length; i++) {
			var oneElement = lineData[i].split("$");
			var xValue = oneElement[0];
			var xText = oneElement[1];
			//����ÿ��value��text��ǵ�ֵ����һ��option����
			option = "<option value='"+xValue+"'>" + xText + "</option>";
			try {
				field.append(option);
			} catch (e) {
				alert("���ͳ��м���ʧ�ܣ�");
			}
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
				dyniframe[dyniframe.length] = document
						.getElementById(iframeids[i]);
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

	$(window).resize(function() {
		dyniframesize();
	});
</script>
</head>

<body>
	<div class="it_main">
		<h1 class="it_webtt" id="it_webtt">�ն�ά�޼�����ͳ��</h1>
		<form id="form" name="selectForm" action="" target="dataForm" method="post">
		<%
			if ("1".equals(select_type))
			{
		%>
			<table width="100%" border="0" cellspacing="0" cellpadding="0"
				class="mainSearch" id="morequerytable">
				<tr>
					<td class="tit">ά�޳��ң�</td>
					<td><s:select list="repair_vendor_list"
							name="repair_vendor" headerKey="-1" headerValue="==��ѡ��ά�޳���=="
							listKey="repair_vendor_id" listValue="repair_vendor" value="repair_vendor"
							cssClass="bk"></s:select></td>
					<td class="tit">�ն˳��ң�</td>
					<td><select name="device_vendor" class="bk" onchange="gwShare_change_select('deviceModel','-1')"></select></td>
					<td class="tit">�ն��ͺţ�</td>
					<td><select name="device_model" class="bk">
							<option value="-1">==����ѡ���ն˳���==</option>
						</select>
					</td>
				</tr>
				<tr>
					<td class="tit">�������У�</td>
					<td><s:select list="attribution_city_list"
							name="attribution_city" headerKey="-1" headerValue="==��ѡ��������=="
							listKey="city_id" listValue="city_name" value="attribution_city"
							cssClass="bk" onchange="getSendCity();"></s:select></td>
					<td class="tit">�������أ�</td>
					<td><select name="send_city" class="bk">
						<option value="-1">==����ѡ��������==</option></select>
					</td>
					<td class="tit">���޳������ڣ�</td>
					<td><input type="text" name="manufacture_date_start"
						class='bk' readonly
						value="<s:property value='manufacture_date_start'/>"> <img
						name="shortDateimg"
						onClick="WdatePicker({el:document.selectForm.manufacture_date_start,dateFmt:'yyyy-MM-dd HH:mm:ss',skin:'whyGreen'})"
						src="../../images/dateButton.png" width="15" height="12"
						border="0" alt="ѡ��">&nbsp;��&nbsp;<input type="text"
						name="manufacture_date_end" class='bk' readonly
						value="<s:property value='manufacture_date_end'/>"> <img
						name="shortDateimg"
						onClick="WdatePicker({el:document.selectForm.manufacture_date_end,dateFmt:'yyyy-MM-dd HH:mm:ss',skin:'whyGreen'})"
						src="../../images/dateButton.png" width="15" height="12"
						border="0" alt="ѡ��"></td>
				</tr>
			</table>
			<div class="mainSearch_btn" id="morequerybutton">
				<a href="javascript:void(0);" onclick="queryRepairDev()" id="queryButton"> �� ѯ </a><a href="javascript:void(0);" onclick="exportQueryRepairDev()"> �� �� </a>
			</div>
			<%
			} else {
			%>
			<table width="100%" border="0" cellspacing="0" cellpadding="0" class="mainSearch" id="singletable">
				<tr>
					<td class="tit" width="100">�ն����кţ�</td>
					<td width="200"><input type="text" name="device_serialnumber"/></td>
					<td width="100"><div class="mainSearch_btn"><a href="javascript:void(0);" onclick="queryRepairDev()" id="queryButton"> �� ѯ </a></div></td>
					<td>&nbsp;</td>
				</tr>
			</table>
			<%
			}
			%>
			
		</form>
		<div class="content">
			<iframe id="dataForm" name="dataForm" height="0" frameborder="0"
				scrolling="no" width="100%" src=""></iframe>
		</div>
	</div>
</body>
<%@ include file="/foot.jsp"%>
</html>
