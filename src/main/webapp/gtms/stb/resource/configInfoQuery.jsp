<%@ page language="java" contentType="text/html; charset=GBK"
	pageEncoding="GBK"%>
<%@taglib prefix="s" uri="/struts-tags"%>
<%@taglib prefix="lk" uri="/linkage"%>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<title>��������ʷ������Ϣ��ѯ</title>
<lk:res />
<link href="/lims/css/css_blue.css" rel="stylesheet" type="text/css">

<link href="/lims/css2/global.css" rel="stylesheet" type="text/css">
<link href="/lims/css2/c_table.css" rel="stylesheet" type="text/css">
<script type="text/javascript">
	function chooseQuery() {
       
		var servAccount = $("input[@name='servAccount']").val();
		var serialnumber = $("input[@name='serialnumber']").val();
		var startTime = $("input[@name='startTime']").val();
		var endTime = $("input[@name='endTime']").val();

		startTime1 = startTime.replace(/-/gi, '/');
		endTime1 = endTime.replace(/-/gi, '/');

		//var miStart = Long.parseLong(Date.parse(startTime1));
		//alert(miStart);
		//	  var maStart = Integer.parseInt();
		var maStart = Date.parse(startTime1);
		var miStart = Date.parse(endTime1);
     if (servAccount == '' && serialnumber == '') {
                alert("ҵ���˺ź��豸���кű�������һ��");
                return;
           //     if (((miStart - maStart) / 86400000) > 7) {
                  //   alert("ҵ���˺������к�Ϊ��ʱʱ���Ȳ��ó���7��");
    			//	return;
    		//	}

		} else {

			if (((miStart - maStart) / 86400000) > 31) {

				alert("ʱ���Ȳ��ܳ���31��");
				return;
			}

		}

		document.selectForm.action = "<s:url value='/gtms/stb/resource/configInfo!query.action'/>";
		document.selectForm.submit();
	}
</script>
</head>
<body>
<form id="selectForm" name="selectForm" action="" target="first"
	method="post"><input type="hidden" name="status" value="6">
<table class="querytable" align="center"  width="98%"
	 id="tabs">
	<tr>
		<td class="title_1" colspan="4">��������ʷ������Ϣ��ѯ</td>
	</tr>
	<tr align="center">
		<td class="title_2" width="15%">ҵ���ʺţ�</td>
		<td><input type="text" maxlength="50" name="servAccount"
			id="servAccount" /></td>
		<td class="title_2" width="15%">���кţ�</td>
		<td><input type="text" maxlength="50" name="serialnumber"
			id="serialnumber" /></td>
	</tr>
	<tr align="center">
		<td class="title_2" width="15%">��ʼʱ�䣺</td>
		<td><lk:date id="startTime" name="startTime" dateOffset="-1"
			maxDateOffset="0" type="all" defaultDate="%{queryTime}"></lk:date></td>
		
		<td class="title_2" width="15%">����ʱ��</td>
		<td><lk:date id="endTime" name="endTime" dateOffset="0"
			maxDateOffset="0" type="all"></lk:date></td>
	</tr>
	<tr align="right">
		<td colspan="4" class="foot" align="right">
		<div class="right">
		<button type="button" onclick=
	chooseQuery();
>��ѯ</button>
		&nbsp;&nbsp;</div>
		</td>
	</tr>
	
</table>
</form>

	
<TABLE width="98%" border=0 cellspacing=1 cellpadding=0 align="center">
	<TR>
		<TD bgcolor=#999999 id="td"><iframe id="first"
			name="first" height="0" frameborder="0" scrolling="no"
			width="100%" src=""></iframe></TD>
	</TR>
</TABLE>	
	
	
	
</body>


<SCRIPT LANGUAGE="JavaScript">


//** iframe�Զ���Ӧҳ�� **//
//������ϣ������ҳ��߶��Զ������߶ȵ�iframe�����Ƶ��б�
//�ö��Ű�ÿ��iframe��ID�ָ�. ����: ["myframe1", "myframe2"]������ֻ��һ�����壬���ö��š�
//����iframe��ID
var iframeids=["first"];

//����û����������֧��iframe�Ƿ�iframe���� yes ��ʾ���أ�no��ʾ������
var iframehide="yes";

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
			var tempobj=document.all? document.all[iframeids[i]] : document.getElementById(iframeids[i])
    		tempobj.style.display="block";
		}
	}
}
$(function(){
	dyniframesize();
});

$(window).resize(function(){
	dyniframesize();
}); 

</SCRIPT>
</html>