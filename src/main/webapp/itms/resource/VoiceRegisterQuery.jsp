<%@ page language="java" contentType="text/html; charset=GBK"
	pageEncoding="GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="lk" uri="/linkage"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<title>����ע���ѯ</title>

<link rel="stylesheet" href="<s:url value="/css3/c_table.css"/>"
	type="text/css">
<link rel="stylesheet" href="<s:url value="/css3/global.css"/>"
	type="text/css">
<script type="text/javascript" src="<s:url value="/Js/jquery.js"/>"></script>
<SCRIPT LANGUAGE="JavaScript" SRC="/Js/CheckFormForm.js"></SCRIPT>
<script type="text/javascript" src="<s:url value="/Js/My97DatePicker/WdatePicker.js"/>"></script>
<script type="text/javascript">
	function query() {
		document.selectForm.submit();
	}
	
	function change_select(){
		$("select[@name='modelId']").html("");
		var url = "<s:url value="/gwms/share/gwDeviceQuery!getDeviceModel.action"/>";
		var vendorId = $("select[@name='vendorId']").val();
		if("-1"==vendorId){
			$("select[@name='modelId']").html("<option value='-1'>==����ѡ����==</option>");
			$("select[@name='modelId']").attr('disabled',true);
			return;
		}	
		$("select[@name='modelId']").attr('disabled',false);
		$.post(url,{
			gwShare_vendorId:vendorId
		},function(ajax){
			parseMessage(ajax,$("select[@name='modelId']"));
		});
	
}
	
	//������ѯ�豸�ͺŷ���ֵ�ķ���
	function parseMessage(ajax,field){
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
			option = "<option value='"+xValue+"' selected>=="+xText+"==</option>";
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

	$(function() {
		dyniframesize();
	});

	$(window).resize(function() {
		dyniframesize();
	});
</script>
</head>
<body>
	<form id="form" name="selectForm"
		action="<s:url value='/itms/resource/VoiceRegisterQuery!VoiceRegisterQueryInfo.action'/>"  target="dataForm">
		<table>
			<tr>
				<td HEIGHT=20>&nbsp;</td>
			</tr>
			<tr>
				<td>
					<table class="green_gargtd">
						<tr>
							<td width="162" align="center" class="title_bigwhite">
							����ע���ѯ</td>
							<td><img src="<s:url value="/images/attention_2.gif"/>"
								width="15" height="12" /></td>
						</tr>
					</table>
				</td>
			</tr>
			<tr>
				<td>
					<table class="querytable">

						<TR>
							<th colspan="4">����ע���ѯ </th>
						</tr>
	
						<TR>
							<TD class=column width="15%" align='right'>ʱ��</TD>
							<TD width="35%">
									<input type="text" name="endOpenDate" readonly class=bk
										value="<s:property value="endOpenDate" />">
									<img name="shortDateimg"
										onClick="WdatePicker({el:document.selectForm.endOpenDate,dateFmt:'yyyy-MM-dd',skin:'whyGreen'})"
										src="../../images/dateButton.png" width="15" height="12"
										border="0" alt="ѡ��">
							</TD>
							
							<TD class=column width="15%" align='right'>�豸���к�</TD>
							<TD width="35%"><input type="text" name="device_sn" id="device_sn" class=bk value=""></TD>
						</TR>
						
						<TR>

							<TD class=column width="15%" align='right'>����</TD>
							<TD width="35%"><s:select list="vendorMap" name="vendorId" onchange="change_select()" headerKey="-1" headerValue="==��ѡ����=="
									 listKey="key" listValue="value" cssClass="bk"></s:select>&nbsp;&nbsp;<font color="red">*</font> </TD>
									 
							<TD class="column" width='15%' align="right">�豸�ͺ�</td>
							<td width='35%' align="left"><select name="modelId" class="bk" disabled="disabled" >
								<option value="-1" selected="selected">==��ѡ���豸�ͺ�==</option>
								</select></TD>
						</TR>
						
						<TR>
							<TD  class=column width="15%" align='right'>LOID</TD>
							<TD><input type="text" name="loid" id="loid" class=bk value=""></TD>
							<TD  class=column width="15%" align='right'>�ն��ͺ�</TD>
							<TD><select name="device_type" class="bk">
									<option value="-1" selected="selected">==��ѡ��==</option>
									<option value="e8-b" >==e8-b==</option>
									<option value="e8-c" >==e8-c==</option>
								</select> </TD>
						</TR>
						
						<TR>
							<TD class=column width="15%" align='right'>�����˿��Ƿ�����</TD>
							<TD>
								<select name="enabled" class="bk">
										<option value="-1" selected="selected">==��ѡ��==</option>
										<option value="Enabled" >==e8-b==</option>
										<option value="Disabled" >==e8-c==</option>
								</select>
							</TD>
							<TD class=column width="15%" align='right'>�����˿ں���</TD>
							<TD><input type="text" name="voip_phone" id="voip_phone" class=bk value=""></TD>
						</TR>
						
						<TR>
							<TD class=column width="15%" align='right'>����ע��ɹ�״̬</TD>
							<TD>
								<select name="status" class="bk">
										<option value="-1" selected="selected">==��ѡ��==</option>
										<option value="up" >==up==</option>
										<option value="Disabled" >==Disabled==</option>
								</select>
							</TD>
							<TD class=column width="15%" align='right'>����ע��ʧ��ԭ��</TD>
							<TD><select name="reason" class="bk">
										<option value="-1" selected="selected">==��ѡ��==</option>
										<option value="0" >==�ɹ�==</option>
										<option value="1" >==IADģ�����==</option>
										<option value="2" >==����·�ɲ�ͨ==</option>
										<option value="3" >==���ʷ���������Ӧ==</option>
										<option value="4" >==�ʺš��������==</option>
										<option value="5" >==δ֪����==</option>
								</select></TD>
						</TR>
						<TR>
							<td colspan="4" align="right" class=foot>
								<button  onclick="query()" id="button" name="button" >&nbsp;��&nbsp;ѯ&nbsp;</button>
							</td>
						</TR>
					</table>
				</td>
			</tr>
			<tr>
				<td height="25" id="resultStr"></td>
			</tr>
				<tr>
				<td><iframe id="dataForm" name="dataForm" height="0" frameborder="0" scrolling="no" width="100%" src=""></iframe></td>
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