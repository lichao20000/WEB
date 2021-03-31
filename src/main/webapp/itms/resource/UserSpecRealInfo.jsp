<%@ page language="java" contentType="text/html; charset=GBK"
	pageEncoding="GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="lk" uri="/linkage"%>
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=GBK">
		<title>�û�ʵ���ն˹����BSS�ն˹��һ�±ȶ�</title>
		<%
			 /**
			 * BSS�ն˹����ʵ���ն˹��һ�±ȶ�
			 * 
			 * @author gaoyi
			 * @version 4.0.0
			 * @since 2013-08-13
			 * @category
			 */
		%>
		<link rel="stylesheet" href="<s:url value="/css3/c_table.css"/>" type="text/css">
		<link rel="stylesheet" href="<s:url value="/css3/global.css"/>" type="text/css">
		<script type="text/javascript" src="<s:url value="/Js/jquery.js"/>"></script>
		<SCRIPT LANGUAGE="JavaScript" SRC="../../Js/CheckFormForm.js"></SCRIPT>
		<script type="text/javascript" src="<s:url value="/Js/My97DatePicker/WdatePicker.js"/>"></script>
		<script type="text/javascript">
function query(){
	configInfoClose();
	bssSheetClose();
	var _cityId = $("select[@name='city_id']");
	var _devicetype = $("select[@name='devicetype']");
	var _cust_type_id = $("select[@name='cust_type_id']");
	var _spec_id = $("select[@name='_spec_id']");
	
	if('' == _cityId.val() || '-1' == _cityId.val()){
		alert("��ѡ������");
		_cityId.focus();
		return false;
	}
	if('' == _devicetype.val() || '-1' == _devicetype.val()){
		alert("��ѡ���ն�����");
		_devicetype.focus();
		return false;
	}
	if('' == _cust_type_id.val() || '0' == _cust_type_id.val()){
		alert("��ѡ��ͻ�����");
		_cust_type_id.focus();
		return false;
	}
	if('' == _spec_id.val() || '0' == _spec_id.val()){
		alert("��ѡ��ʵ���ն˹��");
		_spec_id.focus();
		return false;
	}
	document.selectForm.submit();
}

function changeCust(){
	var _cust_type_id = $("select[@name='cust_type_id']").val();
	var url = "<s:url value='/itms/resource/BssSpecRealInfo!getTabBssDevPort.action'/>";
	$.post(url,{
		cust_type_id:_cust_type_id},function(ajax){
		 	$("select[@name='spec_id']").html("");
			var result = ajax.split(";");
			for(var i=0; i<result.length-1; i++){
				var oneElement = result[i].split("-");
				var xValue = oneElement[0];
				var xText = oneElement[1];
				var selectboxtxt = "<option value='"+xValue+"'>"+xText+"</option>";
				$("select[@name='spec_id']").append(selectboxtxt);
			}
			$("select[@name='spec_id']").attr("disabled",false);
		});
}


function configInfoClose(){
	$("td[@id='configInfoEm']").hide();
	$("td[@id='configInfo']").hide();
}

function bssSheetClose(){
	$("td[@id='bssSheetInfo']").hide();
}



function setValue(){
	theday=new Date();
	day=theday.getDate();
	month=theday.getMonth()+1;
	year=theday.getFullYear();
	
	$("input[@name='time_start']").val(year+"-"+month+"-"+day+" 00:00:00");
	$("input[@name='time_end']").val(year+"-"+month+"-"+day+" 23:59:59");
	
}

//** iframe�Զ���Ӧҳ�� **//
//������ϣ������ҳ��߶��Զ������߶ȵ�iframe�����Ƶ��б�
//�ö��Ű�ÿ��iframe��ID�ָ�. ����: ["myframe1", "myframe2"]������ֻ��һ�����壬���ö��š�
//����iframe��ID
var iframeids=["dataForm"];

//����û����������֧��iframe�Ƿ�iframe���� yes ��ʾ���أ�no��ʾ������
var iframehide="yes";

function dyniframesize() 
{
	var dyniframe=new Array();
	for (var i=0; i<iframeids.length; i++)
	{
		if (document.getElementById)
		{
			//�Զ�����iframe�߶�
			dyniframe[dyniframe.length] = document.getElementById(iframeids[i]);
			if (dyniframe[i] && !window.opera)
			{
     			dyniframe[i].style.display="block";
     			//����û����������NetScape
     			if (dyniframe[i].contentDocument && dyniframe[i].contentDocument.body.offsetHeight)
      				dyniframe[i].height = dyniframe[i].contentDocument.body.offsetHeight; 
      			//����û����������IE
     			else if (dyniframe[i].Document && dyniframe[i].Document.body.scrollHeight) 
      				dyniframe[i].height = dyniframe[i].Document.body.scrollHeight;
   			 }
   		}
		//�����趨�Ĳ���������֧��iframe�����������ʾ����
		if ((document.all || document.getElementById) && iframehide=="no")
		{
			var tempobj=document.all? document.all[iframeids[i]] : document.getElementById(iframeids[i]);
    		tempobj.style.display="block";
		}
	}
}

$(function(){
	//setValue();
	dyniframesize();
});

$(window).resize(function(){
	dyniframesize();
});

</script>
	</head>

	<body>
		<form id="form" name="selectForm" action="<s:url value='/itms/resource/UserSpecRealInfo!getUserSpecRealInfo.action'/>"
			target="dataForm">
			<input type="hidden" name="selectType" value="0" />
			<input type="hidden" name="gw_type" value='<s:property value="gw_type" />' />
			<table>
				<tr>
					<td HEIGHT=20>
						&nbsp;
					</td>
				</tr>
				<tr>
					<td>
						<table class="green_gargtd">
							<tr>
								<td width="162" align="center" class="title_bigwhite">
									�û�ʵ���ն˹��
								</td>
								<td>
									<img src="<s:url value="/images/attention_2.gif"/>" width="15"
										height="12" />
										ȫ��Ϊ������<font color="red">*</font>
								</td>
							</tr>
						</table>
					</td>
				</tr>
				<tr>
					<td>
						<table class="querytable">

							<TR>
								<th colspan="4">
									 ʵ���ն˹����BSS�ն˹��һ�±ȶ�
								</th>
							</tr>
							<TR>
								<TD class=column width="15%" align='right'>
									��ʼʱ��
								</TD>
								<TD width="35%">
									<input type="text" name="startOpenDate" readonly class=bk
										value="<s:property value="startOpenDate" />">
									<img name="shortDateimg"
										onClick="WdatePicker({el:document.selectForm.startOpenDate,dateFmt:'yyyy-MM-dd HH:mm:ss',skin:'whyGreen'})"
										src="../../images/dateButton.png" width="15" height="12"
										border="0" alt="ѡ��">
									&nbsp;
									<font color="red"> *</font>
								</TD>
								<TD class=column width="15%" align='right'>
									����ʱ��
								</TD>
								<TD width="35%">
									<input type="text" name="endOpenDate" readonly class=bk
										value="<s:property value="endOpenDate" />">
									<img name="shortDateimg"
										onClick="WdatePicker({el:document.selectForm.endOpenDate,dateFmt:'yyyy-MM-dd HH:mm:ss',skin:'whyGreen'})"
										src="../../images/dateButton.png" width="15" height="12"
										border="0" alt="ѡ��">
									&nbsp;
									<font color="red"> *</font>
								</TD>
							</TR>
							<TR>
								<TD class=column width="15%" align='right'>
									����
								</TD>
								<TD width="35%">
									<s:select list="cityList" name="city_id" headerKey="00" 
										headerValue="��ѡ������" listKey="city_id" listValue="city_name"
										 cssClass="bk"></s:select>
										 &nbsp;
									<font color="red"> *</font>
								</TD>
								<!-- 
								<TD class=column width="15%" align='right'>
									BSS�ն�����
								</TD>
								<TD width="35%" >
									<SELECT name="devicetype">
										<option selected value="2">E8-C</option>
										<option value="1">E8-B</option>
										<option value="0">ȫ��</option>
									</SELECT>
									&nbsp;
									<font color="red"> *</font>
								</TD>
								 -->
							</TR>
							
							<TR>
								<TD class=column width="15%" align='right'>�ͻ�����</TD>
								<TD width="35%" >
									<SELECT id="cust_type_id" name="cust_type_id" onchange="changeCust()">
										<option selected value="0">==��ѡ��==</option>
										<option value="1">��ͥ�ͻ�</option>
										<option value="2">����ͻ�</option>
									</SELECT>
									&nbsp;
									<font color="red"> *</font>
								</TD>
								<TD class=column width="15%" align='right'>�û�ʵ���ն˹��</TD>
								<TD width="35%">
									<select name="spec_id" id="spec_id" disabled="true">
										<option value="0">==����ѡ��ͻ�����==</option>
									</select>
									&nbsp;
									<font color="red"> *</font>
								</TD>
							</TR>
							<TR>
								<td colspan="4" align="right" class=foot>
									<button onclick="query()">
										&nbsp;�� ѯ&nbsp;
									</button>
								</td> 
							</TR>
						</table>
					</td>
				</tr>
				<tr>
					<td height="25" id="resultStr">

					</td>
				</tr>
				<tr>
					<td>
						<iframe id="dataForm" name="dataForm" height="0" frameborder="0"
							scrolling="no" width="100%" src=""></iframe>
					</td>
				</tr>
				<tr>
					<td height="25" id="configInfoEm" style="display: none">

					</td>
				</tr>
				<tr>
					<td id="configInfo">

					</td>
				</tr>
				<tr>
					<td height="25">
					</td>
				</tr>
				<tr>
					<td id="bssSheetInfo">

					</td>
				</tr>
			</table>
			<br>
		</form>
	</body>
</html>
<%@ include file="../../foot.jsp"%>