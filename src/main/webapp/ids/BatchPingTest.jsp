<%@ include file="/timelater.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="lk" uri="/linkage"%>
<html>
<head>
<link rel="stylesheet" href="<s:url value="/css3/c_table.css"/>"
	type="text/css">
<link rel="stylesheet" href="<s:url value="/css3/global.css"/>"
	type="text/css">
<SCRIPT LANGUAGE="JavaScript" SRC="<s:url value="/Js/jquery.js"/>"></SCRIPT>
<script type="text/javascript"
	src="<s:url value="/Js/My97DatePicker/WdatePicker.js"/>"></script>
<SCRIPT LANGUAGE="JavaScript">
function doQuery(){
	var taskname = $("input[@name='taskname']").val();
	var filename = $("input[@name='gwShare_fileName']").val();
	var endtime = $("input[@name='endtime']").val();
	var starttime = $("input[@name='starttime']").val();
	var enddate = $("input[@name='enddate']").val();
	var pingtype = $("input[@name='pingtype']:checked").val();
	var packetsize = $("input[@name='packetsize']").val();
	var packetnum = $("input[@name='packetnum']").val();
	var timeout = $("input[@name='timeout']").val();
	var pingurl = $("input[@name='url']").val();
	if(starttime>=endtime){
		if(starttime>endtime){
			alert("��ʼʱ�䲻�ܴ��ڽ���ʱ��");
		}else{
			alert("��ʼʱ�䲻�ܵ��ڽ���ʱ��");
		}
		return;
	}
	if(taskname == ""){
		alert("����������Ϊ��");
		return;
	}
	if(packetsize == ""){
		alert("����С����Ϊ��");
		return;
	}
	if(packetnum == ""){
		alert("��������Ϊ��");
		return;
	}
	if(timeout == ""){
		alert("��ʱʱ�䲻��Ϊ��");
		return;
	}
	if(pingurl == ""){
		alert("url����Ϊ��");
		return;
	}
	if(filename == ""){
		alert("�����ϴ��ļ�");
		return;
	}
	
	var url = "<s:url value='/ids/batchPingTest!getExcelRows.action'/>";
	$.post(url,{
		filename : filename
	},function(ajax){
		if(ajax == "false"){
			alert("�������2000��");
			return;
		}
		$("button[@id='btn']").attr("disabled",true);
		$("tr[@id='trData']").show();
		$("div[@id='QueryData']").html("����ִ���������Ʋ��������Ե�....");
		url = "<s:url value='/ids/batchPingTest!addTaskInfo.action'/>";
		$.post(url,{
			taskname : taskname,
			filename : filename,
			endtime : endtime,
			starttime : starttime,
			enddate : enddate,
			pingtype : pingtype,
			packetsize :packetsize,
			packetnum : packetnum,
			timeout : timeout,
			url : pingurl
		},function(ajax){
			if(ajax == "false"){
				$("div[@id='QueryData']").html("�������Ʋ���ִ��ʧ��");
		}else if(ajax=="������������ִ�гɹ���"){
			$("div[@id='QueryData']").html("�������Ʋ���ִ�гɹ�");
			
		}
		else
		{
			$("div[@id='QueryData']").html("����ʧ�ܣ�ҳ�泤ʱ��δ������ˢ��ҳ�棡");
		}
		$("button[@id='btn']").attr("disabled",false);
		});
	});
	
}

//** iframe�Զ���Ӧҳ�� **//
//������ϣ������ҳ��߶��Զ������߶ȵ�iframe�����Ƶ��б�
//�ö��Ű�ÿ��iframe��ID�ָ�. ����: ["myframe1", "myframe2"]������ֻ��һ�����壬���ö��š�
//����iframe��ID
var iframeids = [ "dataForm" ]

//����û����������֧��iframe�Ƿ�iframe���� yes ��ʾ���أ�no��ʾ������
var iframehide = "yes"

function dyniframesize() {
	var dyniframe = new Array()
	for (i = 0; i < iframeids.length; i++) {
		if (document.getElementById) {
			//�Զ�����iframe�߶�
			dyniframe[dyniframe.length] = document
					.getElementById(iframeids[i]);
			if (dyniframe[i] && !window.opera) {
				dyniframe[i].style.display = "block"
				//����û����������NetScape
				if (dyniframe[i].contentDocument
						&& dyniframe[i].contentDocument.body.offsetHeight)
					dyniframe[i].height = dyniframe[i].contentDocument.body.offsetHeight;
				//����û����������IE
				else if (dyniframe[i].Document
						&& dyniframe[i].Document.body.scrollHeight)
					dyniframe[i].height = dyniframe[i].Document.body.scrollHeight;
			}
		}
		//�����趨�Ĳ���������֧��iframe�����������ʾ����
		if ((document.all || document.getElementById) && iframehide == "no") {
			var tempobj = document.all ? document.all[iframeids[i]]
					: document.getElementById(iframeids[i])
			tempobj.style.display = "block"
		}
	}
}

$(function() {
	dyniframesize();
});

$(window).resize(function() {
	dyniframesize();
});

// function openCus(cityId,starttime,endtime,temperature,bais_current,vottage){
// 	alert();
// 	return;
// 	var page="<s:url value='/ids/deviceTVB!queryByTVBList.action'/>?"
// 		+"starttime="+starttime
// 		+ "&endtime=" + endtime
// 		+ "&cityId=" + cityId
// 		+ "&temperature=" + temperature
// 		+ "&bais_current=" + bais_current
// 		+ "&vottage=" + vottage;
// 	window.open(page,"","left=50,top=50,height=550,width=900,toolbar=no,menubar=no,location=no,resizable=yes scrollbars=yes");
// }
</SCRIPT>
	
	
	
</head>

<TABLE>
	<tr>
		<td>
			<table class="green_gargtd">
				<tr>
					<th>����������</th>
					<td><img src="<s:url value="/images/attention_2.gif"/>"
						width="15" height="12"></td>
				</tr>
			</table>
		</td>
	</tr>
	<tr>
		<td>
			<form name="frm" id="selectForm" method="post"
				action="<s:url value='/ids/batchPingTest.action'/>"
				target="dataForm">
				<table class="querytable">
					<tr>
						<th colspan=4>����������</th>
					</tr>
					<tr bgcolor=#ffffff>
					<td class=column align=center width="15%">��������</td>
					<td colspan="3"><input type="text" name="taskname" class='bk'></td>						
					</tr>
					<tr class=column bgcolor="#FFFFFF" id="upload">
					<td  class=column align=center width="15%" height="12">�����ļ���</td>
					<td colspan="3">
						<div id="importUsername">
							<iframe name="gwShare_loadForm" FRAMEBORDER=0 SCROLLING=NO src="FileUpload.jsp" height="22" width=600>
							</iframe><br/><span style="color:green">Ŀǰֻ֧��xls</span>
							<input type="hidden" name="gwShare_fileName" value=""/>
						</div>
					</td>
					</tr>
					
					<tr bgcolor=#ffffff>
					<td class=column align=center width="15%">ѡ���ӽӿ�</td>
					<td colspan="3">
					<input type="radio" width="45%" name="pingtype" value="2" checked="checked">�������
					<input type="radio" width="45%" name="pingtype" value="1">TR069
					</td>						
					</tr>
					<tr bgcolor=#ffffff>
						<td class=column align=center width="10%">��ʼʱ��</td>
						<td width="20%"><input type="text" name="starttime" class='bk' readonly
							value="<s:property value="starttime"/>"> <img
							name="shortDateimg"
							onClick="WdatePicker({el:document.frm.starttime,dateFmt:'HH:mm',skin:'whyGreen'})"
							src="<s:url value="/images/dateButton.png"/>" width="15"
							height="12" border="0" alt="ѡ��" /></td>
						<td class=column align=center width="10%">����ʱ��</td>
						<td width="20%"><input type="text" name="endtime" class='bk' readonly
							value="<s:property value="endtime"/>"> <img
							name="shortDateimg"
							onClick="WdatePicker({el:document.frm.endtime,dateFmt:'HH:mm',skin:'whyGreen'})"
							src="<s:url value="/images/dateButton.png"/>" width="15"
							height="12" border="0" alt="ѡ��" /></td>
					</tr>
					<tr bgcolor=#ffffff>
						<td class=column align=center width="10%">�ɼ���ֹʱ��</td>
						<td colspan="3"><input type="text" name="enddate" class='bk' readonly
							value="<s:property value="enddate"/>"> <img
							name="shortDateimg"
							onClick="WdatePicker({el:document.frm.enddate,dateFmt:'yyyy-MM-dd HH:mm:ss',skin:'whyGreen'})"
							src="<s:url value="/images/dateButton.png"/>" width="15"
							height="12" border="0" alt="ѡ��" /></td>
					</tr>
					
					<tr bgcolor=#ffffff>
						<td class=column align=center width="15%">����С</td>
						<td><input type="text" name="packetsize" class='bk'/></td>
						<td class=column align=center width="15%">����</td>
						<td><input type="text" name="packetnum" class='bk'/></td>
					</tr>
					
					<tr bgcolor=#ffffff>
						<td class=column align=center width="15%">��ʱʱ��</td>
						<td><input type="text" name="timeout" class='bk'/></td>
						<td class=column align=center width="15%">URL��ַ</td>
						<td><input type="text" name="url" class='bk'/></td>
					</tr>
					<tr bgcolor=#ffffff>
						<td class=foot colspan=4 align=right>
							<button id="btn" onclick="doQuery();">&nbsp;��&nbsp;��&nbsp;</button>
						</td>
					</tr>
				</table>
			</form>
		</td>
	</tr>
	<tr id="trData" style="display: none">
		<td>
			<div id="QueryData" style="width: 100%; z-index: 1; top: 100px">
				����Ŭ��Ϊ����ѯ�����Ե�....</div>
		</td>
	</tr>
	<tr>
		<td height="80%"><iframe id="dataForm" name="dataForm"
				height="100%" frameborder="0" scrolling="no" width="100%" src=""></iframe></td>
	</tr>
	<tr>
		<td height="20"></td>
	</tr>
</TABLE>
<%@ include file="/foot.jsp"%>
</html>