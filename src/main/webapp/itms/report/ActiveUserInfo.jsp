<%@ page language="java" contentType="text/html; charset=GBK"
	pageEncoding="GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="lk" uri="/linkage"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<title>������ÿ�»�Ծ�û�ͳ��</title>
<%
	/**
	 * ������ÿ�»�Ծ�û�ͳ��
	 * 
	 * @author zszhao6
	 * @version 1.0
	 * @since 2018-08-13
	 * @category
	 */
%>
<link rel="stylesheet" href="<s:url value="/css3/c_table.css"/>"
	type="text/css">
<link rel="stylesheet" href="<s:url value="/css3/global.css"/>"
	type="text/css">
<script type="text/javascript" src="<s:url value="/Js/jquery.js"/>"></script>
<script type="text/javascript" src="<s:url value="/Js/My97DatePicker/WdatePicker.js"/>"></script>
<script type="text/javascript">			
	function ToExcel() {
      var mainForm = document.getElementById("selectForm");
      mainForm.action = "<s:url value='/itms/report/activeUserReport!getActiveUserInfoExcel.action'/>";
      mainForm.submit();
      mainForm.action = "<s:url value='/itms/report/activeUserReport!getActiveUserInfo.action'/>";
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
			case "vendor":
				var url;
					//�����в�ѯ���̱�
					url = "<s:url value="/gtms/stb/share/shareDeviceQuery!getVendor.action"/>";
				$.post(url,{
				},function(ajax){
					gwShare_parseMessage(ajax,$("select[@name='gwShare_vendorId']"),selectvalue);
					$("select[@name='gwShare_deviceModelId']").html("<option value='-1'>==����ѡ���豸����==</option>");
					$("select[@name='gwShare_devicetypeId']").html("<option value='-1'>==����ѡ���豸�ͺ�==</option>");
				});
				break;
			case "deviceModel":
				var url;
					//�����в�ѯ�豸�ͺ�
					url = "<s:url value="/gtms/stb/share/shareDeviceQuery!getDeviceModel.action"/>";
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
					//�����в�ѯ�汾��
					url = "<s:url value="/gtms/stb/share/shareDeviceQuery!getDevicetype.action"/>";
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
		gwShare_change_select("vendor","-1");
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
		action="<s:url value='/itms/report/activeUserReport!getActiveUserInfo.action'/>"  target="dataForm">
		<table>
			<tr>
				<td HEIGHT=20>&nbsp;</td>
			</tr>
			<tr>
				<td>
					<table class="green_gargtd">
						<tr>
							<td width="162" align="center" class="title_bigwhite">
							������ÿ�»�Ծ�û�ͳ��</td>
							<td><img src="<s:url value="/images/attention_2.gif"/>"
								width="15" height="12" /> ��Ծ�û�Ϊһ�����������߼�¼���ն�</td>
						</tr>
					</table>
				</td>
			</tr>
			<tr>	
			<td>
					<table class="querytable">
						<TR>
							<th colspan="8">��Ծ�û���Ϣ</th>
						</TR>
						<TR>
							<TD class=column width="15%" align='right'>��ʼʱ��</TD>
							<TD width="35%">
									<input type="text" name="startOpenDate" readonly class=bk
										value="<s:property value="startOpenDate" />">
									<img name="shortDateimg"
										onClick="WdatePicker({el:document.selectForm.startOpenDate,dateFmt:'yyyy-MM-dd',skin:'whyGreen'})"
										src="../../images/dateButton.png" width="15" height="12"
										border="0" alt="ѡ��">
							</TD>
							<TD class=column width="15%" align='right'>����ʱ��</TD>
							<TD width="35%">
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
							<TD align="right" class="column" width="15%">��    ��</TD>
							<TD align="left" width="35%">
								<select name="gwShare_cityId" class="bk">
									<option value="-1">==��ѡ��==</option>
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