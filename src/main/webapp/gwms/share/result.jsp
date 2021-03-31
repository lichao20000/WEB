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
				<table class="listtable">
					<caption>
						解析结果
					</caption>
					<thead>
						<tr>
							<th >用户账号</th>
							<th >属地</th>
							<th >绑定的设备</th>
						</tr>
					</thead>
					<tr>
						<td colspan="3">
							以下是库中不存在的用户
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
							以下是已绑定设备的用户
						</td>
					</tr>
					<tbody>
					
						<tr>
							<td align="center">
								bbb
							</td>
							<td align="center">
								云南市区
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
								云南市区
							</td>
							<td align="center">
								FSDFADSSDFFEWDE
							</td>
						</tr>
					</tbody>
					<tr>
						<td colspan="3">
							以下是可以删除的用户
						</td>
					</tr>
					<tbody>
						<tr>
							<td align="center">
								fff
							</td>
							<td align="center">
								云南市区
							</td>
							<td align="center">
								
							</td>
						</tr>
						<tr>
							<td align="center">
								hhh
							</td>
							<td align="center">
								云南市区
							</td>
							<td align="center">
								
							</td>
						</tr>
					</tbody>
					<tfoot>
						<tr>
							<td colspan="3" align="left">
								针对解析的结果，主要分为三部分：
								<br>第一部分，上传文件中的用户账号在系统中不存在，此部分数据无需删除；
								<br>第二部分，上传文件的用户账号在系统中已绑定设备，这部分设备请解绑后再处理；
								<br>第三部分，上传文件的用户账号在系统中存在，而且未绑定设备，这部分用户可以删除。
							</td>
						</tr>
						<tr>
							<td colspan="3" align="right">
								<button> 删 除 </button>
							</td>
						</tr>
					</tfoot>
				</table>
			</td>
		</tr>
</body> 
</html> 
<%@ include file="../foot.jsp"%>