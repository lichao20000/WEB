<!-- �½������������� -->
<%@ include file="/timelater.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>


<link rel="stylesheet" href="<s:url value="/css3/c_table.css"/>" type="text/css">
<link rel="stylesheet" href="<s:url value="/css3/global.css"/>" type="text/css">

<script type="text/javascript" src="../../Js/My97DatePicker/WdatePicker.js"></script>
<SCRIPT LANGUAGE="JavaScript" SRC="../../Js/jquery.js"></SCRIPT>
<SCRIPT LANGUAGE="JavaScript" SRC="../../Js/CheckFormForm.js"></SCRIPT>

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
	var user_info = $("input[@name='user_info']").val().trim();
	var device_sn = $("input[@name='device_sn']").val().trim();
	var service_type = $("select[@name='service_type']").val().trim();
	var operate_type = $("select[@name='operate_type']").val().trim();
	var cityId = $("select[@name='cityId']").val().trim();
	var starttime = $("input[@name='starttime']").val().trim();
	var endtime = $("input[@name='endtime']").val().trim();
	var cpe_mac = $("input[@name='cpe_mac']").val().trim();
	
	if(""==user_info && ""==device_sn && ""==cpe_mac){
		alert("�˺�/LOID���豸���к�������һ�");
		return false;
	}
	
	if(service_type=="-1"){
		alert("��ѡ��ҵ�����ͣ�");
		return false;
	}
	
	var sValue=starttime.substring(5)+"-"+starttime.substring(0,4);	
	var s_date=new Date(Date.parse(sValue));
	var s_day=s_date.getDate();
	var s_month=s_date.getMonth()+1;
	var s_year=s_date.getFullYear();
	
	var eValue=endtime.substring(5)+"-"+endtime.substring(0,4);	
	var e_date=new Date(Date.parse(eValue));
	var e_day=e_date.getDate();
	var e_month=e_date.getMonth()+1;
	var e_year=e_date.getFullYear();

	if(e_year-s_year==1){
		e_month=e_month+12;
	}
	
	if((e_year-s_year>1) || (e_month-s_month>1) || (e_month>s_month && e_day>s_day)){
		alert("�������ڿ�Ȳ��ܴ���һ���£�");
		return false;
	}
	
	var url="<s:url value='itms/service/zeroconfiguration_XJ!query.action'/>";
	document.frm.submit();
}


</script>

<br>
<table>
	<tr>
		<td>
			<table class="green_gargtd">
				<tr>
					<th>�½�������������</th>
					<td align="left">
						<img src="<s:url value="/images/attention_2.gif"/>" width="15" height="12">������������
					</td>
				</tr>
			</table>
		</td>
	</tr>
	<tr>
		<td>
			<form name="frm" id="frm" method="post" action="<s:url value='itms/service/zeroconfiguration_XJ!query.action'/>" 
				onSubmit="return false" target="dataForm" >
				<table class="querytable">
					<tr>
						<th colspan=4>������������</th>
					</tr>
					<tr bgcolor="#ffffff">
						<td class="column" align="center" width="15%">
							�˺�/LOID
						</td>
						<td>
							<input type="text" name="user_info" />
						</td>
						<td class="column" align="center" width="15%">
							�豸���к�
						</td>
						<td>
							<input type="text" name="device_sn" /><span style="color:red">(��ͬʱ����LOID���豸���кŲ�ѯ��ֻ��LOID��ѯ)</span>
						</td>
					</tr>
					<tr bgcolor="#ffffff">
						<td class="column" align="center" width="15%">
							ҵ������
						</td>
						<td>
							<select name="service_type" class="bk">
								<option value="4">������</option>								
						</select>
							<font color="red">*</font>
						</td>
						<td class="column" align="center" width="15%">
							��������
						</td>
						<td>
							<s:select list="operate_typeList" name="operate_type" headerKey="-1"
									headerValue="��ѡ��" listKey="operate_type" listValue="operate_type_name"
									value="operate_type" cssClass="bk">
							</s:select>
						</td>
					</tr>
					<tr bgcolor=#ffffff>
						<td class="column" align="center" width="15%">
								����
						</td>
						<td>
							<s:select list="cityList" name="cityId" headerKey="-1"
									headerValue="��ѡ������" listKey="city_id" listValue="city_name"
									value="cityId" cssClass="bk">
							</s:select>
						</td>
						<td class=column align=center width="25%">
							��������
						</td>
						<td>
							<input type="text" name="starttime" class='bk' readonly value="<s:property value='starttime'/>">
							<img name="shortDateimg" onClick="WdatePicker({el:document.frm.starttime,dateFmt:'yyyy-MM-dd HH:mm:ss',skin:'whyGreen'})"
								src="../../images/dateButton.png" width="15" height="12" border="0" alt="ѡ��" /> 
							&nbsp;~&nbsp;
							<input type="text" name="endtime" class='bk' readonly value="<s:property value='endtime'/>"> 
							<img name="shortDateimg" onClick="WdatePicker({el:document.frm.endtime,dateFmt:'yyyy-MM-dd HH:mm:ss',skin:'whyGreen'})"
								src="../../images/dateButton.png" width="15" height="12" border="0" alt="ѡ��" /> 
						</td>
					</tr>
					<tr bgcolor=#ffffff>
						<td class="column" align="center" width="15%">
							MAC��ַ
						</td>
						<td colspan="3">
							<input type="text" name="cpe_mac" />
						</td>
					</tr>
					<tr bgcolor=#ffffff>
						<td class="foot" colspan="4" align="right">
							<button onclick="doQuery();" name="subBtn">&nbsp;��ѯ&nbsp;</button>
						</td>
					</tr>
				</table>
			</form>
		</td>
	</tr>
	<tr>
		<td bgcolor=#999999 id="idData" >
			<iframe id="dataForm" name="dataForm" height="0" frameborder="0" scrolling="no" width="100%" src=""/>
		</td>
	</tr>
	
</table>

<%@ include file="/foot.jsp"%>

