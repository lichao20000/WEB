<%@ include file="../../timelater.jsp"%>
<%@ page contentType="text/html; charset=GBK"%>
<%@ include file="../../head.jsp"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<link href="../css/css_green.css" rel="stylesheet" type="text/css">
<script type="text/javascript" src="<s:url value="/Js/jquery.js"/>"></script>
<SCRIPT LANGUAGE="JavaScript">
$(function(){
	gwShare_setGaoji();
});

function deviceResult(returnVal){
        var idArray = new Array();
		var deviceSnArray = new Array();
		for(var i=0;i<returnVal[2].length;i++){
			idArray[i]=returnVal[2][i][0];
			deviceSnArray[i]=returnVal[2][i][2];
			
		
		}
		var url = "<s:url value="/itms/resource/ReportUsernameACT!queryUsername.action"/>";
           url = url + "?idArray="+idArray+"&deviceSnArray="+deviceSnArray;
        //  window.location.href= url;
          document.getElementsByName("idArray")[0].value=idArray;
          document.getElementsByName("deviceSnArray")[0].value=deviceSnArray;
           var form = document.getElementById("mainForm");
         form.action = "<s:url value="/itms/resource/ReportUsernameACT!queryUsername.action"/>";
          //form.target = "dataForm";
         form.submit();
}
	
	
</SCRIPT>
<%@ include file="../../toolbar.jsp"%>

<TABLE border=0 cellspacing=0 cellpadding=0 width="100%">
	<TR>
		<TD HEIGHT=20>&nbsp;</TD>
	</TR>
	<TR>
		<TD>
		<table width="95%" border="0" align="center" cellpadding="0"
			cellspacing="0">
			<tr>
				<td>
				<table width="100%" height="30" border="0" cellspacing="0"
					cellpadding="0" class="green_gargtd">
					<tr>
						<td width="162">
						<div align="center" class="title_bigwhite">�豸�ϱ��ʺ�ͳ��</div>
						</td>
					</tr>
				</table>
				</td>
			</tr>


			<TR bgcolor="#FFFFFF">
				<td colspan="4"><%@ include
					file="/gwms/share/gwShareDeviceQuery.jsp"%>
				</td>
			</TR>


		</table>
		</TD>
	</TR>

	<tr>
		<td>
		<TABLE width="95%" border=0 cellspacing=0 cellpadding=0 align="center">
			<TR>
				<TD bgcolor=#999999 id="idData"><iframe id="dataForm"
					name="dataForm" height="0" frameborder="0" scrolling="no"
					width="100%" src=""></iframe></TD>
			</TR>
		</TABLE>
		</td>

	</tr>
	<tr>
		<td bgcolor="#FFFFFF">
		<FORM NAME="mainForm" id="mainForm" METHOD="post" ACTION=""
			target="dataForm"><input type="hidden" name="idArray" value=""></input>
		<input type="hidden" name="deviceSnArray" value=""></input></FORM>
		</td>
	</tr>

</TABLE>


<script>
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


</script>
<%@ include file="../../foot.jsp"%>
