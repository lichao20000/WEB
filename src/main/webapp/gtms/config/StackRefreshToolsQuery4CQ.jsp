<%@ include file="/timelater.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="lk" uri="/linkage"%>

<html>
<head>
<script type="text/javascript" src="<s:url value="/Js/jquery.js"/>"></script>
	<script type="text/javascript" src="<s:url value="/Js/My97DatePicker/WdatePicker.js"/>"></script>
<link href="<s:url value="/css/css_green.css"/>" rel="stylesheet" type="text/css">

<SCRIPT LANGUAGE="JavaScript">
//��ѯ
function query(){
	// ��ͨ��ʽ�ύ
	var form = document.getElementById("mainForm");
	form.action = "<s:url value='/gtms/config/stackRefreshTools!queryList.action'/>";
	form.submit();
}

</SCRIPT>

</head>

<body>
<TABLE border=0 cellspacing=0 cellpadding=0 width="100%">
	<TR>
		<TD HEIGHT=20>&nbsp;</TD>
	</TR>
	<TR>
		<TD>
		<FORM NAME="mainForm" id="mainForm" METHOD="post" ACTION="" target="dataForm">
		<table width="98%" height="30" border="0" align="center" cellpadding="0" cellspacing="0" class="green_gargtd">
			<tr>
				<td width="162"> <div align="center" class="title_bigwhite">IPv6���Թ���</div> </td>
				<td> <img src="/itms/images/attention_2.gif" width="15" height="12"></td>
			</tr>
		</table>
		<!-- �߼���ѯpart -->
		<TABLE border=0 cellspacing=0 cellpadding=0 width="98%" align="center">
			<tr>
				<td bgcolor=#999999>
				<table border=0 cellspacing=1 cellpadding=2 width="100%"
					align="center">
					<tr>
						<th colspan="4" id="gwShare_thTitle">IPv6���Թ���</th>
					</tr>
					<tr bgcolor="#FFFFFF">
						<td align="right" width="15%">
							�������ƣ�
						</td>
						<td align="left" width="30%">
							<INPUT TYPE="text" NAME="task_name_query"
								maxlength=60 class=bk size=20>
						</td>

						<td align="right" width="15%">
							����״̬��
						</td>
						<TD align="left" width="30%"><select name="status_query" class="bk">
							<option value="">ȫ��</option>
							<option value="1">����</option>
							<option value="7">��ͣ</option>
							</select></TD>
					</tr>
					<tr bgcolor="#FFFFFF">
						<td align="right" width="15%">
							���ƿ�ʼʱ�䣺
						</td>
						<td align="left" width="30%">
							<input type="text"   name="startOpenDate" readonly value="<s:property value='starttime'/>" class=bk >
								<img name="shortDateimg"
			 						onClick="WdatePicker({el:document.mainForm.startOpenDate,dateFmt:'yyyy-MM-dd HH:mm:ss',skin:'whyGreen'})"
			 							src="../../images/dateButton.png" width="15" height="12"
			 								border="0" alt="ѡ��">
						</td>

						<td align="right" width="15%">
							���ƽ���ʱ�䣺
						</td>
						<td align="left" width="30%">
							<input type="text"  name="endOpenDate" readonly value="<s:property value='endtime'/>" class=bk >
								<img name="shortDateimg"
			 						onClick="WdatePicker({el:document.mainForm.endOpenDate,dateFmt:'yyyy-MM-dd HH:mm:ss',skin:'whyGreen'})"
			 							src="../../images/dateButton.png" width="15" height="12"
			 								border="0" alt="ѡ��">
						</td>
					</tr>
					<tr bgcolor="#FFFFFF">
						<td colspan="4" align="right" class="green_foot" width="100%">
						<input type="button" id="button"
							onclick="javascript:query()" class=jianbian
							name="task_queryButton" value=" �� ѯ " /></td>
					</tr>
				</table>
				</td>
			</tr>
		</table>
		
		</FORM>
		<!-- չʾ���part -->
		<TABLE width="98%" border=0 cellspacing=0 cellpadding=0 align="center">
			<TR>
				<TD bgcolor=#999999 id="idData"><iframe id="dataForm"
					name="dataForm" height="0" frameborder="0" scrolling="no"
					width="100%" src=""></iframe></TD>
			</TR>
		</TABLE>
		
		</TD>
	</TR>
</TABLE>
</body>
<%@ include file="/foot.jsp"%>
<SCRIPT LANGUAGE="JavaScript">

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

</SCRIPT>
</html>