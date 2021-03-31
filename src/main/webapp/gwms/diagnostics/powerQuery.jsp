<%@ include file="/timelater.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>

<link rel="stylesheet" href="<s:url value="/css3/c_table.css"/>"
	type="text/css">
<link rel="stylesheet" href="<s:url value="/css3/global.css"/>"
	type="text/css">
<SCRIPT LANGUAGE="JavaScript" SRC="<s:url value="/Js/jquery.js"/>"></SCRIPT>
<script type="text/javascript"
	src="../../Js/My97DatePicker/WdatePicker.js"></script>

<script language="JavaScript">

function selVersion(oselect){
	$("#soft_version").html("");
	var version = document.getElementById("soft_version");
	version.options.add(new Option("ȫ���汾","0"));
	var vendor_id = oselect.value;
		var url = "<s:url value='/itms/resource/VersionQuery!querySoftVersion.action'/>";
		$.post(url,{
			vendor_id:vendor_id
		},function(ajax){
		var strArr = new Array(); 
		strArr = ajax.split(","); 
		for (i = 0; i < strArr.length; i++){
			version.options.add(new Option(strArr[i],strArr[i]));
		} 
	});
}
//** iframe�Զ���Ӧҳ�� **//
//������ϣ������ҳ��߶��Զ������߶ȵ�iframe�����Ƶ��б�
//�ö��Ű�ÿ��iframe��ID�ָ�. ����: ["myframe1", "myframe2"]������ֻ��һ�����壬���ö��š�
//����iframe��ID
var iframeids=["dataForm"]

//����û����������֧��iframe�Ƿ�iframe���� yes ��ʾ���أ�no��ʾ������
var iframehide="yes"

function dyniframesize() 
{
	var dyniframe=new Array()
	for (i=0; i<iframeids.length; i++)
	{
		if (document.getElementById)
		{
			//�Զ�����iframe�߶�
			dyniframe[dyniframe.length] = document.getElementById(iframeids[i]);
			if (dyniframe[i] && !window.opera)
			{
   			dyniframe[i].style.display="block"
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
			var tempobj=document.all? document.all[iframeids[i]] : document.getElementById(iframeids[i])
  		tempobj.style.display="block"
		}
	}
}

function ToExcel() {
	var mainForm = document.getElementById("frm");
	mainForm.action = "<s:url value='/itms/resource/VersionQuery!getPowerExcel.action' />";
	mainForm.submit();
	mainForm.action = "<s:url value='/itms/resource/VersionQuery!powerByQuery.action' />";
}

$(function(){
	dyniframesize();
});

$(window).resize(function(){
	dyniframesize();
}); 

function doQuery(){
    $("tr[@id='trData']").show();
    $("div[@id='QueryData']").html("���ڲ�ѯ�����Ե�....");
	$("button[@name='button']").attr("disabled", true);
	var url="<s:url value='/itms/resource/VersionQuery!powerByQuery.action'/>";
	document.frm.submit();
	
}

</script>

<br>
<table>
	<tr>
		<td>
			<table class="green_gargtd">
				<tr>
					<th>�⹦�ʲɼ������ѯ</th>
					<td align="left"><img src="<s:url value="/images/attention_2.gif"/>"
						width="15" height="12">�⹦�ʲɼ������ѯ��</td>
				</tr>
			</table>
		</td>
	</tr>
	<tr>
		<td>
			<form name="frm" id="frm" method="post" action="<s:url value='/itms/resource/VersionQuery!powerByQuery.action'/>" target="dataForm">
				<table class="querytable">
					<tr>
						<th colspan=4>�⹦�ʲɼ������ѯ</th>
					</tr>
					<tr bgcolor=#ffffff>
						<td class=column align=center width="25%">
							��ͨʱ��
						</td>
						<td>
							<input type="text" name="starttime" class='bk' readonly value="<s:property value='starttime'/>">
							<img name="shortDateimg" onClick="WdatePicker({el:document.frm.starttime,dateFmt:'yyyy-MM-dd HH:mm:ss',skin:'whyGreen'})"
								src="../../images/dateButton.png" width="15" height="12" border="0" alt="ѡ��" /> 
							<font color="red">*</font>
						</td>
						<td class=column align=center width="15%">
							����ʱ��
						</td>
						<td><input type="text" name="endtime" class='bk' readonly value="<s:property value='endtime'/>"> 
							<img name="shortDateimg" onClick="WdatePicker({el:document.frm.endtime,dateFmt:'yyyy-MM-dd HH:mm:ss',skin:'whyGreen'})"
								src="../../images/dateButton.png" width="15" height="12" border="0" alt="ѡ��" /> 
							<font color="red">*</font>
						</td>
					</tr>
					<tr bgcolor="#ffffff">
							<td class="column" align="center" width="15%">
								�߼�ID
							</td>
							<td>
								<input type="text" name="username" />
							</td>
							<td class="column" align="center" width="15%">
								�豸���к�
							</td>
							<td>
								<input type="text" name="device_serialnumber" />
							</td>
						</tr>
						<tr bgcolor="#ffffff">
							<td class="column" align="center" width="15%">
								����
							</td>
							<td>
								<s:select list="cityList" name="cityId" headerKey="-1"
										headerValue="��ѡ������" listKey="city_id" listValue="city_name"
										value="cityId" cssClass="bk"></s:select>
							</td>
							<td class="column" align="center" width="15%">
								����
							</td>
							<td>
								<s:select list="vendorList" name="vendor_id" headerKey="0" 
								headerValue="ȫ������" listKey="vendor_id" listValue="vendorName" 
								value="vendor_id" onchange="selVersion(this);" theme="simple"/>
							</td>
						</tr>
						<tr bgcolor="#ffffff">
							<td class="column" align="center" width="15%">
								����汾
							</td>
							<td>
								<select id="soft_version" name="soft_version">
									<option value="0" id="version">ȫ������汾</option>
								</select>
							</td>
							<td class="column" align="center" width="15%">
								ָ�귧ֵ
							</td>
							<td>
								<select name="powerVal">
									<option value="0">ȫ��</option>
									<option value="1">���͹⹦���쳣</option>
									<option value="2">���չ⹦���쳣</option>
									<option value="-1">�ɼ�ʧ��</option>
								</select>
							</td>
						</tr>
						<tr bgcolor="#ffffff">
							<td class="column" align="center" width="15%">
								��ֵ��Χ
							</td>
							<td>
								<select name="powerScope">
									<option value="0">ȫ��</option>
									<option value="1">���ڷ�ֵ</option>
								</select>
							</td>
						</tr>
						<tr bgcolor=#ffffff>
							<td class="foot" colspan="4" align="right">
								<button onclick="doQuery();" name="button">&nbsp;��ѯ&nbsp;</button>
							</td>
						</tr>
				</table>
			</form>
		</td>
	</tr>
	<tr>
		<td bgcolor=#999999 id="idData">
			<iframe id="dataForm" name="dataForm" height="0" frameborder="0" scrolling="no" width="100%" src=""/>
		</td>
	</tr>
	
</table>

<%@ include file="/foot.jsp"%>

