<%@ page contentType="text/html;charset=gbk"%>
<%@taglib prefix="s" uri="/struts-tags"%>
<html>
<head>
<title>���������绰����</title>
<link href="../../css/css_green.css" rel="stylesheet" type="text/css">
<SCRIPT LANGUAGE="JavaScript" SRC="../../Js/jquery.js"></SCRIPT>
<SCRIPT LANGUAGE="JavaScript">

	$(function(){
	
		var retResult = '<s:property value="retResult" />';
		var excelFile = '<s:property value="fileName2" />';
		var txtFile = '<s:property value="fileName1" />';
		
		var filePath = "/export/home/cluster/itms/WEB_RH/temp/"
		
		if(retResult == '0'){
			document.getElementById("td01").innerHTML="<a href=\"javascript:void(0);\" onClick=\"doDownLoad('"+txtFile+"','"+filePath+"');\">"+txtFile+"</a>&nbsp;&nbsp;��������ӽ������ء�";
			document.getElementById("td02").innerHTML="<a href=\"javascript:void(0);\" onClick=\"doDownLoad('"+excelFile+"','"+filePath+"');\">"+excelFile+"</a>&nbsp;&nbsp;��������ӽ������ء�";
			
			$("tr[@id='tr00']").css("display","");
			$("tr[@id='tr01']").css("display","");
			$("tr[@id='tr02']").css("display","");
			
		}else{
			if("-1" == retResult){
				document.getElementById("actLabel").innerHTML = "<font color='red'>�ϴ���Excel�ļ�Ϊ�գ�</font>";
				$("tr[@id='tr00']").css("display","");
				$("tr[@id='tr03']").css("display","");
			}
			if("-2" == retResult){
				document.getElementById("actLabel").innerHTML = "<font color='red'>����Excelʧ�ܣ�</font>";
				$("tr[@id='tr00']").css("display","");
				$("tr[@id='tr03']").css("display","");
			}
			if("-3" == retResult){
				document.getElementById("actLabel").innerHTML = "<font color='red'>����Excel�ļ�ʧ�ܣ�</font>";
				$("tr[@id='tr00']").css("display","");
				$("tr[@id='tr03']").css("display","");
			}
		}
	});
	
	
	function doDownLoad(fileName, filePath){
		$("input[@name='fileName']").val(fileName);
		$("input[@name='filePath']").val(filePath);
		$("form[@name='myForm']").submit();
	}
</SCRIPT>
</head>
<body>
<form name="myForm" action="fileDownLoad.jsp" >
	<input name="fileName" type="hidden" value=""/>
	<input name="filePath" type="hidden" value=""/>
</form>
<TABLE width="100%" border=0 cellspacing=0 cellpadding=0  align="center" class="text">
	<TR>
		<TD bgcolor=#999999>
		<table border=0 cellspacing=1 cellpadding=2 width="100%">
			</tr>
			<tr id="tr01" style="display: none" bgcolor="#FFFFFF">
				<td width="10%" align="right">δ�����ļ���</td>
				<td id="td01" colspan="3" height="20"></td>
			</tr>
			<tr id="tr01" style="display: none" bgcolor="#FFFFFF">
				<td align="right">��ѯ�����</td>
				<td id="td02" colspan="3" height="20"></td>
			</tr>
			<tr id="tr03" style="display: none" bgcolor="#FFFFFF">
				<td align="center" colspan="4" height="20" ><SPAN id="actLabel"></SPAN></td>
			</tr>
		</table>
		</td>
	</TR>
</table>
</body>
</html>