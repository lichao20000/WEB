<%@ page language="java" contentType="text/html; charset=GBK" pageEncoding="GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@taglib prefix="lk" uri="/linkage" %>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<title>mac�����������</title>
<link href="<s:url value="/css2/global.css"/>" rel="stylesheet" type="text/css">
<link href="<s:url value="/css2/c_table.css"/>" rel="stylesheet" type="text/css">
<script type="text/javascript" src="<s:url value="/Js/jquery.js"/>"></script>
<SCRIPT LANGUAGE="JavaScript" SRC="<s:url value="/Js/jquery.blockUI.js"/>"></SCRIPT>
<script type="text/javascript" src="<s:url value="/Js/My97DatePicker/WdatePicker.js"/>"></script>
<SCRIPT LANGUAGE="JavaScript">

$(function(){
	
	closeMsgDlg()
	
	var retResult = '<s:property value="retResult" />';
	var fileName = '<s:property value="fileName" />';
	
	if("" != retResult){
		
		if(retResult == '1'){
			document.getElementById("actLabel").innerHTML = "<font color='red'>��������ļ� "+fileName+" ������Ҫ���밴��ע�������ϴ��ļ����ߵ������ģ��</font>";
			$("tr[@id='tr00']").css("display","none");
			$("tr[@id='tr01']").css("display","");
		}
		if(retResult == '-1'){
			document.getElementById("actLabel").innerHTML = "<font color='red'>�ļ� "+fileName+" �г��̣�Ҫ�󲻿�Ϊ�գ�����ȷ�ϣ�</font>";
			$("tr[@id='tr00']").css("display","none");
			$("tr[@id='tr01']").css("display","");
		}
		if(retResult == '-2'){
			document.getElementById("actLabel").innerHTML = "<font color='red'>�ļ� "+fileName+" �в�Ʒ�ͺţ�Ҫ�󲻿���Ϊ�գ�����ȷ�ϣ�</font>";
			$("tr[@id='tr00']").css("display","none");
			$("tr[@id='tr01']").css("display","");
		}
		if(retResult == '-6'){
			document.getElementById("actLabel").innerHTML = "<font color='red'>�ļ� "+fileName+" ���豸MACʼ��Ҫ�󲻿���Ϊ�գ�����ȷ�ϣ�</font>";
			$("tr[@id='tr00']").css("display","none");
			$("tr[@id='tr01']").css("display","");
		}
		if(retResult == '6'){
			document.getElementById("actLabel").innerHTML = "<font color='red'>�ļ� "+fileName+" δ�ܵ��룬���а����ֶΡ��豸MACʼ����ʽ������ȷ�ϣ�</font>";
			$("tr[@id='tr00']").css("display","none");
			$("tr[@id='tr01']").css("display","");
		}
		if(retResult == '-7'){
			document.getElementById("actLabel").innerHTML = "<font color='red'>�ļ� "+fileName+" ���豸MAC�գ�Ҫ�󲻿���Ϊ�գ�����ȷ�ϣ�</font>";
			$("tr[@id='tr00']").css("display","none");
			$("tr[@id='tr01']").css("display","");
		}
		if(retResult == '7'){
			document.getElementById("actLabel").innerHTML = "<font color='red'>�ļ� "+fileName+" δ�ܵ��룬���а����ֶΡ��豸MAC�ա���ʽ������ȷ�ϣ�</font>";
			$("tr[@id='tr00']").css("display","none");
			$("tr[@id='tr01']").css("display","");
		}
		
		if(retResult == '-4'){
			document.getElementById("actLabel").innerHTML = "<font color='red'>�ļ� "+fileName+" �еĳ��̱���Ϊ����Ϊ�����ˡ���ά�����š����硢UT���������г��̣���ȷ�ϣ�</font>";
			$("tr[@id='tr00']").css("display","none");
			$("tr[@id='tr01']").css("display","");
		}
		if(retResult == '0'){
			alert("����ɹ���");
			document.getElementById("actLabel").innerHTML = "<font color='red'>�ļ� "+fileName+" ����ɹ���</font>";
			$("tr[@id='tr00']").css("display","none");
			$("tr[@id='tr01']").css("display","");
		}
	
		if(retResult == '3'){
			document.getElementById("actLabel").innerHTML = "<font color='red'>���ϴ��� "+fileName+" Ϊ�գ���ȷ�ϣ�</font>";
			$("tr[@id='tr00']").css("display","none");
			$("tr[@id='tr01']").css("display","");
		}
		if(retResult == '5'){
			document.getElementById("actLabel").innerHTML = "<font color='red'>���� "+fileName+" ����</font>";
			$("tr[@id='tr00']").css("display","none");
			$("tr[@id='tr01']").css("display","");
		}
	}
});

//����ģ��
function getExcelTemplate(){
	$("form[@name='importFrm']").attr("action","importMac!downloadTemplate.action");
	$("form[@name='importFrm']").submit();
}


//���ݷ���
function doUpload(){
	
	
	
	$("tr[@id='tr01']").css("display","none");

	var filePath = document.importFrm.file.value;
	
	if(filePath.replace(/(^\s*)|(\s*$)/g, "") == ""){
		alert("����ѡ���ļ���");
		$("input[@name='file']").focus();
		return false;
	}
	
	var filet = "";
	var fileP = "";
	 
	filet = filePath.split(".");
	if(filet.length < 2){
		$("input[@name='file']").focus();
		return false;
	}
	
	if("xls"!=filet[filet.length-1] && "XLS"!=filet[filet.length-1]){
				alert("ֻ֧��Excel2003");
				$("input[@name='file']").focus();
				return false;
	}else{
				$("input[@name='fileType']").val("xls");
	}
	
	
	<%--
	if("xls"!=filet[filet.length-1] && "XLS"!=filet[filet.length-1] && 
	   "txt"!=filet[filet.length-1] && "TXT"!=filet[filet.length-1]){
		alert("ֻ֧��Excel2003����TXT�ı�");
		$("input[@name='file']").focus();
		return false;
	}else{
		if("xls" == filet[filet.length-1] || "XLS" == filet[filet.length-1]){
			$("input[@name='fileType']").val("xls");
		}else if("txt" == filet[filet.length-1] || "TXT" == filet[filet.length-1]){
			$("input[@name='fileType']").val("txt");
		}
	}
	--%>
	
	
	fileP = filePath.split("\\");
	
	$("input[@name='fileName']").val(fileP[fileP.length-1]);
	document.importFrm.upload.disabled = true;
	
	showMsgDlg();
	$("form[@name='importFrm']").attr("action","importMac!readUploadFile.action");
	$("form[@name='importFrm']").submit();
}

//��ʼ����ʱ�����
	function showMsgDlg(){
			w = document.body.clientWidth;
			h = document.body.clientHeight;
			
			l = (w-250)/2;
			t = h/2-200;
			PendingMessage.style.left = l;
			PendingMessage.style.top  = t;
			PendingMessage.style.display="";
	}
	
	//������ݣ�����ҳ��
	function closeMsgDlg(){
			PendingMessage.style.display="none";
	}


</SCRIPT>
</head>
<body>
<div id="PendingMessage"
	style="position:absolute;z-index:3;top:240px;left:250px;width:300;height:60;border-width:1;border-style:ridge;background-color:#eeeeee;padding-top:10px;filter:alpha(opacity=80);display:none">
		<center>
				<table border="0">
					<tr>
						<td valign="middle"><img src="<s:url value='/images/cursor_hourglas.gif'/>"  
							border="0" WIDTH="30" HEIGHT="30"></td>
						<td>&nbsp;&nbsp;</td>
						<td valign="middle"><span id=txtLoading
							style="font-size:14px;font-family: ����">���Ե�,���ڽ����ĵ�������������</span></td>
					</tr>
				</table>
		</center>
</div>

<TABLE width="98%" align="center" class="querytable">
	<TR>
		<TD >
			<IMG height=20 src="<s:url value='/images/bite_2.gif'/>" width=24>
            	����ǰ��λ�ã�mac����ģ�嵼�����
		</TD>
	</TR>
</TABLE>
<TABLE width="98%" class="querytable" align="center">
	<tr><th colspan="4" id="thTitle" class="title_1">��������MAC����</th></tr>
	<TR id="tr21" STYLE="display:">
		<TD width="10%"  class="title_2">�ύ�ļ�</TD>
		<TD width="90%" colspan="3" align="center">
			<form name="importFrm" action="importMac!readUploadFile.action"
									method="POST" enctype="multipart/form-data">
					<input type="hidden" name="fileType" value="" >
					<input type="hidden" name="fileName" value="" >
					<input name="rowNum" type="hidden" value="20000"><!-- ֻ����Excel��ǰ20000�У������������� -->
					<table>
						<tr>
							<td>
								<input type="file" size="60" name="file" />
								<input type="button" value=" �� �� "  name="upload" onclick="doUpload()" />
								&nbsp;&nbsp;
								<a href="javascript:void(0);" onClick="getExcelTemplate();"><font color='red'>�������ģ��</font></a>
								&nbsp;&nbsp;
							</td>
						</tr>
					</table>
			</form>
		</TD>
	</TR>
	<TR  STYLE="display:">
								<td   align="right" class="title_2">ģ��˵����
								<td colspan="3" valign="middle" >
								<font color="#7f9db9"> 
								<ol type="1">
								<li>��Ҫ������ļ���ʽΪExcel2003,��xls;</li>
								<li>Excel2003��ʽ��ÿ�����֣��ļ�ֻ�а�����,�������š���װ��š����̡�������ʽ���豸MACʼ���豸MAC�ա��豸���к�;</li>
								<li>���У������š���װ��š�������ʽ���豸���к�<font color="red">����Ϊ��</font>,���̡���Ʒ�ͺš��豸MACʼ���豸MAC��Ϊ<font color="red">������</font>;</li>
								<!-- JXDX-ITV-REQ-20170622-WUWF-001(ITV�ն˹���ƽ̨MAC���������豸���ҹ���ȥ��) -->
								<!-- <li>���̵�ѡ��Χ����Ϊ�����ˡ���ά�����š����硢UT�����ӡ��Ĺ㡢������;</li> -->
								<li>�豸MAC��ַΪ6λ16������,����ʱ�轫�ֺ�ȥ��,��ʽΪ��000A29FAEC7D;</li>
								<li>�ļ���������಻����2����,�糬��2���У�ֻ����ǰ2��������;</li>
								</ol>
								</font></td>
						
	</TR>
	
	<!-- ���һ�����ص�ռλ�������ֽ�����һ�� -->
	<TR class="right">
								<td colspan="4" align="right" CLASS="green_foot">&nbsp;</td>
	</TR>
	<tr id="tr00" class="right" style="display: none" >
	</TR>
	<TR id="tr01" style="display: none" class="right">
								<td align="right" class="title_2"  width="10%">��������</td>
								<td id="td02" colspan="3" height="20" width="90%"><SPAN id="actLabel"></SPAN></td>
	</TR>
</TABLE>
<br>
</body>
<br>
<br>
