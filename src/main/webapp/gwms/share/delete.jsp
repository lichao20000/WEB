<%@ include file="/timelater.jsp"%>
<%@ page language="java" contentType="text/html; charset=GBK" pageEncoding="GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@taglib prefix="lk" uri="/linkage" %>
<link rel="stylesheet" href="<s:url value="/css3/c_table.css"/>" type="text/css">
<link rel="stylesheet" href="<s:url value="/css3/global.css"/>" type="text/css">
<script type="text/javascript" src="<s:url value="/Js/jquery.js"/>"></script>
<SCRIPT LANGUAGE="JavaScript">

function checkForm(){
	var myFile = $("input[@name='myFile']").val();
	if(""==myFile){
		alert("请选择文件!");
		return false;
	}
	if(myFile.length<4 || ("txt"!= myFile.substr(myFile.length-3,3) && "xls"!= myFile.substr(myFile.length-3,3))){
		alert("请按照注意事项上传文件！");
		return false;
	}
	
	return true;
}

</script>
<head> 
    <title> Struts 2 File Upload </title> 
</head> 
<body> 
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
							未使用定制终端用户处理
						</th>
						<td>
							<img src="<s:url value="/images/attention_2.gif"/>" width="15" height="12">
						</td>
					</tr>
				</table>
			</td>
		</tr>
		<tr>
			<td>
				<table class="querytable">
					<tr leaf="simple">
						<th colspan="4">上传文件</th>
					</tr>
					<tr >
						<td ><%@ include file="/gwms/share/FileUpload.jsp" %></td>
					</tr>
					<tr>
						<td>
							备注：<br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
								1、上传文件的格式为Excel97-2003格式，后缀名为xls结尾；
								<br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
								2、上传的文件第一行作为标题；
								<br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
								3、上传的文件最多放入100条数据；
						</td>
					</tr>
				</table>
			</td>
		</tr>
</body> 
</html> 
<%@ include file="../foot.jsp"%>