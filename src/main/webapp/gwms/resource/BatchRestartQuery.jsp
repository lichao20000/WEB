<%@ include file="/timelater.jsp"%>
<%@ include file="../../head.jsp"%>
<%@ page language="java" contentType="text/html; charset=GBK" pageEncoding="GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="lk" uri="/linkage"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<title>��ʱ����������</title>
<link rel="stylesheet" href="<s:url value="/css/css_green.css"/>" type="text/css">
<script type="text/javascript" src="<s:url value="/Js/jquery.js"/>"></script>
<script type="text/javascript"
	src="<s:url value="/Js/My97DatePicker/WdatePicker.js"/>"></script>
<script type="text/javascript">
//��ѯ
function query()
{
	// ��ͨ��ʽ�ύ
	var form = document.getElementById("mainForm");
	form.action = "<s:url value='/gwms/resource/batchRestartManagerACT!qryList.action'/>"
	form.submit();
}

//����
function toExcel(){
	var form = document.getElementById("mainForm");
	form.action = "<s:url value='/gwms/resource/batchRestartManagerACT!qryExcel.action'/>"
	form.submit();
	form.action = "<s:url value='/gwms/resource/batchRestartManagerACT!qryList.action'/>"
}

//��������
function operTask(taskId,operType)
{
	var url = "<s:url value='/gwms/resource/batchRestartManagerACT!operTask.action'/>";
	$.post(url,{
		taskId : taskId,
		operType:operType
	},function(ajax){
		 alert(ajax);
		 if(operType == "1"){
			 query();
		 }
	});
}

//����
function detail(taskId,type){
	var strpage = "<s:url value='/gwms/resource/batchRestartManagerACT!qryDetail.action'/>?taskId="
			+ taskId
			+ "&type="+type
	window.open(strpage, "",
					"left=20,top=20,width=900,height=650,resizable=yes,scrollbars=yes");
}
</script>
</head>
<body>
	<TABLE border=0 cellspacing=0 cellpadding=0 width="100%">
		<TR>
			<TD HEIGHT=20>&nbsp;</TD>
		</TR>
		<TR>
			<TD>
				<FORM NAME="mainForm" id="mainForm" METHOD="post" ACTION=""
					target="dataForm">
					<table width="98%" border=0 cellspacing=0 cellpadding=0
					align="center">
						<tr>
							<td>
							 <table width="100%" height="30" border="0" cellspacing="0"
								cellpadding="0" class="green_gargtd">
								<tr>
									<td width="162" align="center" class="title_bigwhite">
										���������������
									</td>
									<td>
										<img src="../../images/attention_2.gif" width="15" height="12">
										 ��ʱ�����߹�è���������������
									</td>
								</tr>
							 </table>
							</td>
						</tr>
					</table>
					 
					<TABLE border=0 cellspacing=0 cellpadding=0 width="98%"
						align="center">
						<tr>
							<td bgcolor=#999999>
								<table border=0 cellspacing=1 cellpadding=2 width="100%"
									align="center">
									<tr>
										<th colspan="4" bgcolor="#ffffff" align="center">��ʱ���������߹�è������������</th>
									</tr>
									<TR bgcolor="#FFFFFF" id="gwShare_tr21" STYLE="">
										<TD align="right" class=column width="15%">�����</TD>
										<TD align="left" width="35%">
										<input type="text" class=bk value="" name="taskName"></TD>
										<TD align="right" class=column width="15%">��������</TD>
										<TD width="35%">
										<input type="text" class=bk value="" name="taskDesc"></TD>
									</TR>
									<TR bgcolor="#FFFFFF" STYLE="">
										<TD align="right" class=column width="15%">���ƿ�ʼʱ��</TD>
										<TD align="left" width="35%"><input type="text"
											name="addTime" id="add_time" class='bk' readonly
											value="<s:property value='addTime'/>"> <img
											name="shortDateimg"
											onClick="WdatePicker({el:document.mainForm.addTime,dateFmt:'yyyy-MM-dd HH:mm:ss',skin:'whyGreen'})"
											src="<s:url value="/images/dateButton.png"/>" width="15"
											height="12" border="0" alt="ѡ��" /></TD>
										<TD align="right" class=column width="15%">���ƽ���ʱ��</TD>
										<TD width="35%" nowrap><input type="text" name="finalTime"
											id=""finalTime"" class='bk' readonly
											value="<s:property value='finalTime'/>"> <img
											name="shortDateimg"
											onClick="WdatePicker({el:document.mainForm.finalTime,dateFmt:'yyyy-MM-dd HH:mm:ss',skin:'whyGreen'})"
											src="<s:url value="/images/dateButton.png"/>" width="15"
											height="12" border="0" alt="ѡ��" /></TD>
									</TR>
									<TR bgcolor="#FFFFFF" id="gwShare_tr21" STYLE="">
										<TD align="right" class=column width="15%">ִ�п�ʼʱ��</TD>
										<TD align="left" width="35%"><input type="text"
											name="startTime" id="start_time" class='bk' readonly
											value="<s:property value='startTime'/>"> <img
											name="shortDateimg"
											onClick="WdatePicker({el:document.mainForm.startTime,dateFmt:'yyyy-MM-dd HH:mm:ss',skin:'whyGreen'})"
											src="<s:url value="/images/dateButton.png"/>" width="15"
											height="12" border="0" alt="ѡ��" /></TD>
										<TD align="right" class=column width="15%">ִ�н���ʱ��</TD>
										<TD width="35%" nowrap><input type="text" name="endTime"
											id="end_time" class='bk' readonly
											value="<s:property value='endTime'/>"> <img
											name="shortDateimg"
											onClick="WdatePicker({el:document.mainForm.endTime,dateFmt:'yyyy-MM-dd HH:mm:ss',skin:'whyGreen'})"
											src="<s:url value="/images/dateButton.png"/>" width="15"
											height="12" border="0" alt="ѡ��" /></TD>
									</TR>
									<TR bgcolor="#FFFFFF" STYLE="">
										<TD align="right" class=column width="15%">ִ��״̬</TD>
										<TD align="left" width="35%"><select name="status"
											class="bk">
												<option value="-1">==��ѡ��==</option>
												<option value="1">δִ��</option>
												<option value="2">ִ����</option>
												<option value="3">���</option>
												<option value="4">ʧ��</option>
										</select></TD>
										<TD align="right" class=column width="15%">�豸���к�</TD>
										<TD width="35%">
										  <input type="text" class=bk value="" name="subDevSn">
										</TD>
									</TR>
									<tr bgcolor="#FFFFFF">
										<td colspan="4" align="right" class="green_foot" width="100%">
											<input type="button" onclick="javascript:query()" class=jianbian
											name="gwShare_queryButton" value=" �� ѯ " />
											<input type="button" class=jianbian 
											value=" �� �� " onclick="javascript:reset();" />
										</td>
									</tr>
								</table>
							</td>
						</tr>
					</table>
				</FORM> <!-- չʾ���part -->
				<TABLE width="98%" border=0 cellspacing=0 cellpadding=0
					align="center">
					<TR>
						<TD bgcolor=#999999 id="idData"><iframe id="dataForm"
								name="dataForm" height="0" frameborder="0" scrolling="no"
								width="100%" src=""></iframe></TD>
					</TR>
				</TABLE>
			</TD>
		</TR>
	</TABLE>
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
	for (var i=0; i<iframeids.length; i++)
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
    		tempobj.style.display="block";
		}
	}
}
</SCRIPT>
</body>
<%@ include file="../foot.jsp"%>