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
				<table class="listtable">
					<caption>
						�������
					</caption>
					<thead>
						<tr>
							<th >�û��˺�</th>
							<th >����</th>
							<th >�󶨵��豸</th>
						</tr>
					</thead>
					<tr>
						<td colspan="3">
							�����ǿ��в����ڵ��û�
						</td>
					</tr>
					<tbody>
						<tr>
							<td align="center">
								aaaaa
							</td>
							<td align="center">
								
							</td>
							<td align="center">
								
							</td>
						</tr>
					</tbody>
					<tr>
						<td  colspan="3">
							�������Ѱ��豸���û�
						</td>
					</tr>
					<tbody>
					
						<tr>
							<td align="center">
								bbb
							</td>
							<td align="center">
								��������
							</td>
							<td align="center">
								FSDFADSFEWDE
							</td>
						</tr>
						<tr>
							<td align="center">
								dd
							</td>
							<td align="center">
								��������
							</td>
							<td align="center">
								FSDFADSSDFFEWDE
							</td>
						</tr>
					</tbody>
					<tr>
						<td colspan="3">
							�����ǿ���ɾ�����û�
						</td>
					</tr>
					<tbody>
						<tr>
							<td align="center">
								fff
							</td>
							<td align="center">
								��������
							</td>
							<td align="center">
								
							</td>
						</tr>
						<tr>
							<td align="center">
								hhh
							</td>
							<td align="center">
								��������
							</td>
							<td align="center">
								
							</td>
						</tr>
					</tbody>
					<tfoot>
						<tr>
							<td colspan="3" align="left">
								��Խ����Ľ������Ҫ��Ϊ�����֣�
								<br>��һ���֣��ϴ��ļ��е��û��˺���ϵͳ�в����ڣ��˲�����������ɾ����
								<br>�ڶ����֣��ϴ��ļ����û��˺���ϵͳ���Ѱ��豸���ⲿ���豸������ٴ���
								<br>�������֣��ϴ��ļ����û��˺���ϵͳ�д��ڣ�����δ���豸���ⲿ���û�����ɾ����
							</td>
						</tr>
						<tr>
							<td colspan="3" align="right">
								<button> ɾ �� </button>
							</td>
						</tr>
					</tfoot>
				</table>
			</td>
		</tr>
</body> 
</html> 
<%@ include file="../foot.jsp"%>