<%@ page language="java" contentType="text/html; charset=GBK"
	pageEncoding="GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="lk" uri="/linkage"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<title>�����Ӧ�ն�ͳ��</title>
<link rel="stylesheet" href="<s:url value="/css3/c_table.css"/>"
	type="text/css">
<link rel="stylesheet" href="<s:url value="/css3/global.css"/>"
	type="text/css">
<script type="text/javascript" src="<s:url value="/Js/jquery.js"/>"></script>
<script type="text/javascript" src="<s:url value="/Js/My97DatePicker/WdatePicker.js"/>"></script>
<script type="text/javascript">			
	function ToExcel() {
      var mainForm = document.getElementById("selectForm");
      mainForm.action = "<s:url value='/itms/report/bandwidthDeviceReport!getDeviceInfoExcel.action'/>";
      mainForm.submit();
      mainForm.action = "<s:url value='/itms/report/bandwidthDeviceReport!getDeviceInfo.action'/>";
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
	function gwShare_change_select(type,selectvalue){
		var gwType = $("input[@name='gw_type']").val();
		switch (type){
			case "city":
				var url ;
					//�����в�ѯ���ر�
					url = "<s:url value="/gtms/stb/share/shareDeviceQuery!getCityNextChild.action"/>";
				$.post(url,{
				},function(ajax){
					gwShare_parseMessage(ajax,$("select[@name='gwShare_cityId']"),selectvalue);
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
	$(function() {
		dyniframesize();
		gwShare_change_select("city","-1");
	});
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
	<form id="selectForm" name="selectForm"
		action="<s:url value='/itms/report/bandwidthDeviceReport!getDeviceInfo.action'/>"  target="dataForm">
		<table>
			<tr>
				<td HEIGHT=20>&nbsp;</td>
			</tr>
			<tr>
				<td>
					<table class="green_gargtd">
						<tr>
							<td width="162" align="center" class="title_bigwhite">
							�����Ӧ�ն�ͳ��</td>
							<td><img src="<s:url value="/images/attention_2.gif"/>"
								width="15" height="12" />�����Ӧ�ն�ͳ��</td>
						</tr>
					</table>
				</td>
			</tr>
			<tr>	
			<td>
					<table class="querytable">
						<TR>
							<th colspan="8">�����Ӧ�ն˲�ѯ</th>
						</TR>
						<TR>
							<TD align="right" class="column" width="10%">��    ��</TD>
							<TD align="left" width="23%">
								<select name="gwShare_cityId" class="bk">
									<option value="-1">==��ѡ��==</option>
								</select>
							</TD>
							<TD class=column width="10%" align='right'>��ǰ����</TD>
							<TD width="23%">
								<select name="bandwidth" class="bk">
									<option value="1">100M</option>
									<option value="2">200M</option>
									<option value="3">200M����</option>
								</select>
							</TD>
							<TD class=column width="10%" align='right'>�Ƿ�֧������</TD>
							<TD>
								<select name="isSpeedUp" class="bk">
									<option value="1">��</option>
									<option value="2">��</option>
								</select>
							</TD>
						</TR>
						<TR>
							<td colspan="8" align="center" class=foot>
								<button id="qy"  type="submit" >��&nbsp;ʼ&nbsp;ͳ&nbsp;��&nbsp;</button>
							</td>
								</TR>
					</table>
				</td>
			</tr>
				<tr>
				<td height="25" id="resultStr"></td>
			</tr>
			<tr>
				<td><iframe id="dataForm" name="dataForm" height="0"
						frameborder="0" scrolling="no" width="100%" src=""></iframe></td>
			</tr>
			<tr>
				<td height="25"></td>
			</tr>
			<tr>
				<td id="bssSheetInfo"></td>
			</tr>
			</table>
		<br>
	</form>
</body>
</html>
<%@ include file="../../foot.jsp"%>