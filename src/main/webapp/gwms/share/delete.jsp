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
		alert("��ѡ���ļ�!");
		return false;
	}
	if(myFile.length<4 || ("txt"!= myFile.substr(myFile.length-3,3) && "xls"!= myFile.substr(myFile.length-3,3))){
		alert("�밴��ע�������ϴ��ļ���");
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
							δʹ�ö����ն��û�����
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
						<th colspan="4">�ϴ��ļ�</th>
					</tr>
					<tr >
						<td ><%@ include file="/gwms/share/FileUpload.jsp" %></td>
					</tr>
					<tr>
						<td>
							��ע��<br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
								1���ϴ��ļ��ĸ�ʽΪExcel97-2003��ʽ����׺��Ϊxls��β��
								<br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
								2���ϴ����ļ���һ����Ϊ���⣻
								<br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
								3���ϴ����ļ�������100�����ݣ�
						</td>
					</tr>
				</table>
			</td>
		</tr>
</body> 
</html> 
<%@ include file="../foot.jsp"%>