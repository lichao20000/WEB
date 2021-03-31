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
	var con = $.trim($("select[@name='con']").val());
    var condition = $.trim($("input[@name='condition']").val());
//	var starttime = $.trim($("input[@name='starttime']").val());
//	var endtime = $.trim($("input[@name='endtime']").val());
//	var openState = $.trim($("select[@name='openState']").val());
//	var type = $.trim($("input[@name='type']").val());
	var awifi_type ='<s:property value="awifi_type" />';
	if(condition == null || condition == ""){
    	alert("���Ǻŵ�Ϊ�����");
   	    return false;
	}
	if(con=="1" && condition.length < 6){
		 alert("�豸���кŵ���Ч�ַ���������Ϊ6��");
    	 return false;
	}
    $("tr[@id='trData']").show();
    $("div[@id='QueryData']").html("");
	$("button[@name='button']").attr("disabled", true);
	var url="<s:url value='/itms/report/strategyQuery!getCountAll.action'/>";
	document.frm.submit();
	
}

</script>

<br>
<table>
	<tr>
		<td>
			<table class="green_gargtd">
				<tr>
					<s:if test="awifi_type==1">
						<th>awifiҵ����Բ�ѯ</th>
					</s:if>
					<s:elseif test="awifi_type==2">
						<th>У԰������ҵ����Բ�ѯ</th>
					</s:elseif>
					<s:elseif test="awifi_type==3">
						<th>����ר��ҵ����Բ�ѯ</th>
					</s:elseif>
					<td><img src="<s:url value="/images/attention_2.gif"/>"
						width="15" height="12"></td>
					<td>��ѯ����ҵ��ͨ���رղ����������ʼʱ�䡢����ʱ��Ϊ����ʱ�䡣</td>
				</tr>
			</table>
		</td>
	</tr>
	<tr>
		<td>
			<form name="frm" id="frm" method="post" action="<s:url value='/itms/report/strategyQuery!getCountAll.action'/>" target="dataForm">
				<input type='hidden' name="awifi_type" value="<s:property value='awifi_type'/>" />
				<table class="querytable">
					<tr>
						<s:if test="awifi_type==1">
							<th colspan=4>awifiҵ����Բ�ѯ</th>
						</s:if>
						<s:elseif test="awifi_type==2">
							<th colspan=4>У԰������ҵ����Բ�ѯ</th>
						</s:elseif>
						<s:elseif test="awifi_type==3">
							<th colspan=4>����ר��ҵ����Բ�ѯ</th>
						</s:elseif>
					</tr>
					<tr bgcolor=#ffffff>
						<td class=column align=center width="25%">
							<select name="con" class=column>
								<option value="1">�豸���к�</option>
								<option value="0">LOID</option>
								<option value="-1">����˺�</option>
							</select>
						</td>
						<td align=center width="25%">
							<input type="text" name="condition" class='bk' /> 
							<font color="red">*</font>
						</td>
						<td class=column  width="25%">
							��ͨ״̬
						</td>
						<td bgcolor=#eeeeee>
							<select name="openState" class=column>
								<option value="2">��ѡ��</option>
								<option value="1">�ɹ�</option>
								<option value="0">ʧ��</option>
								<option value="3">δ��</option>
							</select>
						</td>
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
					<tr bgcolor=#ffffff>
						<td class=foot colspan=4>
							<button onclick="doQuery()" name="button" id="button">&nbsp;��ѯ&nbsp;</button>
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

