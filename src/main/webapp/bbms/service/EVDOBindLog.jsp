<%@ page contentType="text/html;charset=GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@taglib prefix="lk" uri="/linkage" %>
<%@ include file="../../timelater.jsp"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<title>EVDO����־</title>
<style>
.#msg{background:#68af02;color:#fff;left:50%;top:0px;position:absolute;margin-left:-25%;margin-left:expression(-this.offsetWidth/2);font-size:12px;text-align:center;padding:0 28px;height:20px;line-height:20px;white-space:nowrap;}
.errmsg {background:#ef8f00;}
</style>
<script type="text/javascript" src="<s:url value="/Js/jsDate/WdatePicker.js"/>"></script>
<script type="text/javascript" src="<s:url value="/Js/jquery.js"/>"></script>
<link href="<s:url value="/css/css_green.css"/>" rel="stylesheet" type="text/css">
<script language="JavaScript">
<!--
function query(){
	$("iframe[@id='dataForm']").css("display","");
	dyniframesize();
	document.selectForm.submit();
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

$(window).resize(function(){
	dyniframesize();
}); 

//-->
</script>
</head>
<body>
<form name="selectForm" action="<s:url value="/bbms/service/EVDOBindLog!queryData.action"/>" target="dataForm">
<TABLE border=0 cellspacing=0 cellpadding=0 width="90%" align=center>
	<tr>
		<td>
			<table width="100%" height="30" border="0" cellspacing="0" cellpadding="0" class="green_gargtd">
				<tr>
					<td width="162" align="center" class="title_bigwhite">
						EVDO����־
					</td>
					<td>&nbsp;</td>
				</tr>
			</table>
		</td>
	</tr>
	<tr>
		<td>
			<table width="100%"  border="0" cellspacing="1" cellpadding="2" bgcolor="#999999">
				<tr bgcolor=#ffffff>
					<td class=column align=center>��ʼʱ��</td>
					<td>
						<input type="text" size="14" name="startDate" value='<s:property value="startDate" />' readonly class=bk>
						<img name="shortDateimg" onclick="new WdatePicker(document.selectForm.startDate,'%Y-%M-%D',true,'whyGreen')"
							src="<s:url value="/images/search.gif"/>" width="15" height="12" border="0" alt="ѡ��">(YYYY-MM-DD)
					</td>
					<td class=column align=center>����ʱ��</td>
					<td>
						<input type="text" size="14" name="endDate" value='<s:property value="endDate" />' readonly class=bk>
						<img name="shortDateimg" onclick="new WdatePicker(document.selectForm.endDate,'%Y-%M-%D',true,'whyGreen')"
							src="<s:url value="/images/search.gif"/>" width="15" height="12" border="0" alt="ѡ��">(YYYY-MM-DD)
					</td>
				</tr>
				<tr bgcolor=#ffffff>
					<td class=column align=center>��    ��</td>
					<td>
						<s:select label="ѡ������" list="cityList" name="cityId" listKey="city_id" listValue="city_name"
            				emptyOption="true"  value="cityId"/>
					</td>
					<td class=column align=center>�豸���к�</td>
					<td><input type="text" size="20" class=bk name="deviceNo" /></td>
				</tr>
				<tr bgcolor=#ffffff>
					<td class=column colspan=4 align=right>
						<input type="button" value=" �� ѯ " class=jianbian onclick="query()">
					</td>
				</tr>
			</table>
		</td>
	</tr>
	<tr>
		<td>&nbsp;</td>
	</tr>
	<tr>
		<td>
			<iframe id="dataForm" name="dataForm" height="58%" frameborder="0" scrolling="no" width="100%" src="" STYLE="display:none">����ͳ��...</iframe>
		</td>
	</tr>
</TABLE>
</form>
</body>
<%@ include file="../foot.jsp"%>