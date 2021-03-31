<%@ include file="../../timelater.jsp"%>
<%@ page language="java" contentType="text/html; charset=GBK" pageEncoding="GBK"%>
<%@ taglib prefix="s"  uri="/struts-tags"%>
<%@ taglib prefix="lk" uri="/linkage"%>

<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=GBK">
		<title>���Ź���</title>
		<link rel="stylesheet" href="<s:url value="/css3/c_table.css"/>" type="text/css">
		<link rel="stylesheet" href="<s:url value="/css3/global.css"/>" type="text/css">
		<script type="text/javascript" src="<s:url value="/Js/jquery.js"/>"></script>
		<script type="text/javascript" src="<s:url value="/Js/My97DatePicker/WdatePicker.js"/>"></script>
<script type="text/javascript">
$(function(){
	query();
});
	
function query(){
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

$(function(){
	dyniframesize();
});

$(window).resize(function(){
	dyniframesize();
}); 

function add() {
	reset();
	$("#addtable").css("display", "block");
}
function cancel() {
	$("#addtable").css("display", "none");	
}
function reset() {
	$("#deptname").val("");
	$("#deptdesc").val("");
	$("#departid").val("");
}


function alertAjax(ajax) {
	if ("0" == ajax) {
		alert("�����ɹ���");
		return;
	}
	if ("-1" == ajax) {
		alert("����ʧ�ܣ�");
		return;
	}
	if ("1" == ajax) {
		alert("�Ѵ�����ͬ���ţ�");
		return;
	}
}

function save() {
	var deptname = $("#deptname").val();
	var deptdesc = $("#deptdesc").val();
	var departid = $("#departid").val();
	if (deptname == "") {
		alert("�������Ʋ���Ϊ�գ�");
		$("#deptname").focus();
		return;
	}
	var url;
	if (departid == "") {
		url = "<s:url value="/itms/resource/departManage!addDepartInfo.action"/>";
	}
	else {
		url = "<s:url value="/itms/resource/departManage!editDepartInfo.action"/>";
	}
	$.post(url,{
		deptname: deptname,
		deptdesc: deptdesc,
		departid: departid
	},function(ajax){
		alertAjax(ajax);
		if("0" == ajax) {
			 document.getElementById("selectForm").submit();
			 cancel();
		}
	});
}

function delDeaprt(departid) {
	if(!confirm("���Ҫɾ���ò�����\n��������ɾ���Ĳ��ָܻ�������")){
		return;
	}
	var url = "<s:url value="/itms/resource/departManage!deleteDepart.action"/>";
	$.post(url,{
		departid: departid
	},function(ajax){
		alertAjax(ajax);
		if("0" == ajax) {
			 document.getElementById("selectForm").submit();
			 cancel();
		}
	});
}

function editDeaprt(departid, departname, departdesc) {
	$("#deptname").val(departname);
	$("#deptdesc").val(departdesc);
	$("#departid").val(departid);
	$("#addtable").css("display", "block");
}
</script>
	</head>
	<body>
		<form name="selectForm" action="<s:url value='/itms/resource/departManage!queryList.action'/>" target="dataForm">
			<table>
				<tr>
					<td HEIGHT=20>
						&nbsp;
					</td>
				</tr>
				<tr>
					<td>
						<table class="green_gargtd">
							<tr>
								<th>
									���Ź���
								</th>
								<td>
									<img src="<s:url value="/images/attention_2.gif"/>" width="15" height="12">���Ź���ҳ��
								</td>
							</tr>
						</table>
					</td>
				</tr>
				<tr>
					<td>
						<table class="querytable">
							<tr leaf="simple">
								<th colspan="6">���Ų�ѯ</th>
							</tr>
							<tr leaf="high" style="display: ">
								<td class=column width="10%" align='right'>
									�������ƣ�
								</td>
								<td width="24%">
									<input type="text" id="nameSearch" name="nameSearch" class=bk>
								</td>
								<td class=column width="10%" align='right'>
									��ʼʱ�䣺
								</td>
								<td>
									<input type="text" id="startTime" name="startTime" readonly class=bk>
									<img name="shortDateimg"
										onClick="WdatePicker({maxDate:'#F{$dp.$D(\'endTime\',{d:-0});}',el:document.selectForm.startTime,dateFmt:'yyyy-MM-dd HH:mm:ss',skin:'whyGreen'})"
										src="../../images/dateButton.png" width="15" height="12"
										border="0" alt="ѡ��" />
								</td>
								<td class=column width="10%" align='right'>
									����ʱ�䣺
								</td>
								<td>
									<input type="text" id="endTime" name="endTime" readonly class=bk>
									<img name="shortDateimg"
										onClick="WdatePicker({minDate:'#F{$dp.$D(\'startTime\',{d:0});}',el:document.selectForm.endTime,dateFmt:'yyyy-MM-dd HH:mm:ss',skin:'whyGreen'})"
										src="../../images/dateButton.png" width="15" height="12"
										border="0" alt="ѡ��" />
								</td>
							</tr>
							
							<TR>
								<td colspan="6" align="right" class=foot>
									<button onclick="query()">&nbsp;��  ѯ&nbsp;</button>&nbsp;&nbsp;
									<button onclick="add()">&nbsp;��  ��&nbsp;</button>
								</td>
							</TR>
						</table>
					</td>
				</tr>
			</table>
			<br>
		</form>
		
		<!-- չʾ���part -->
		<TABLE width="95%" border=0 cellspacing=0 cellpadding=0 align="center">
			<TR>
				<TD bgcolor=#999999 id="idData">
				<iframe id="dataForm" name="dataForm" frameborder="0" scrolling="no" width="100%" src=""></iframe>
				</TD>
			</TR>
		</TABLE>
		
		<form name="addFrom" action="" target="dataForm">
			<input type="hidden" id="departid">
			<table class="querytable" style="width:95%;display:none;" id="addtable">
				<TR>
					<TH colspan="2" align="center"><SPAN id="actLabel">���ű༭</SPAN></TH>
				</TR>
				
								
				<TR bgcolor="#FFFFFF">
					<TD class=column align="right" width="45%">�������ƣ�</TD>
					<TD>
					<INPUT TYPE="text" NAME="deptname" id="deptname" class="bk" style="width:200px;">
					<font color="#FF0000">*</font>
					</TD>
				</TR>
				<TR bgcolor="#FFFFFF">
					<TD class=column align="right" width="45%">������</TD>
					<TD>
					<INPUT TYPE="text" NAME="deptdesc" id="deptdesc" class="bk" style="width:200px;">
					</TD>
				</TR>
				<TR>
					<TD colspan="4" align="right" class=foot>
						<button onclick="save()">&nbsp;��  ��&nbsp;</button>&nbsp;&nbsp;
						<button onclick="cancel()">&nbsp;ȡ  ��&nbsp;</button>
					</TD>
				</TR>
			</table>
		</form>
	</body>
</html>
<%@ include file="../../foot.jsp"%>