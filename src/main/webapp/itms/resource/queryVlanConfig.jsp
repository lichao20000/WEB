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


$(function(){
	dyniframesize();
});

$(window).resize(function(){
	dyniframesize();
}); 

function doQuery(){
	$("button[@name='button']").attr("disabled", true);
	var deviceSerialnumber = $.trim(document.frm.deviceSerialnumber.value);
	if (deviceSerialnumber != "") {
		if (deviceSerialnumber.length < 6) {
			alert("�������������6λ�豸���к� !");
			document.frm.deviceSerialnumber.focus();
			return false;
		}
	}
	var frm = document.getElementById("frm");
	frm.action="<s:url value='/itms/resource/queryVlanConfig!queryVlanConfigList.action'/>";
	document.frm.submit();
	}
	
function exportList() {
	var frm = document.getElementById("frm");
	frm.action = "<s:url value='/itms/resource/queryVlanConfig!exportVlanConfigList.action'/>";
	frm.submit();
}

</script>

<br>
<table>
	<tr>
		<td>
			<table class="green_gargtd">
				<tr>
					<th>��ͥ�������ò�ѯ</th>
					<td align="left"><img src="<s:url value="/images/attention_2.gif"/>"
						width="15" height="12">��ͥ���ض˿�VLAN���������ѯ��</td>
				</tr>
			</table>
		</td>
	</tr>
	<tr>
		<td>
			<form name="frm" id="frm" method="post"  target="dataForm">
				<table class="querytable">
					<tr>
						<th colspan=4>��ͥ���ض˿�VLAN���������ѯ</th>
					</tr>
					<tr bgcolor="#ffffff">
							<td class="column" align="center" width="15%">
								<select name="selectType">
									<option value="0">�߼�ID</option>
									<option value="1">����˺�</option>
								</select>
							</td>
							<td>
								<input type="text" name="username" />
							</td>
							<td class="column" align="center" width="15%">
								�豸���к�
							</td>
							<td>
								<input type="text" name="deviceSerialnumber" />
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
								�Ƿ��ѯ�쳣�ӿ�
							</td>
							<td>
								<select name="IsErrPort">
									<option value="0">ȫ��</option>
									<option value="1">��</option>
									<option value="-1">��</option>
								</select>
							</td>
						</tr>
						<tr bgcolor=#ffffff>
							<td class="foot" colspan="4" align="right">
							<button onclick="exportList();" name="button">&nbsp;����&nbsp;</button>&nbsp;&nbsp;
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

