<%@ include file="../timelater.jsp"%>
<%@ page language="java" contentType="text/html; charset=GBK"
	pageEncoding="GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@taglib prefix="lk" uri="/linkage"%>
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=GBK">
		<link href="<s:url value="/css2/global.css"/>" rel="stylesheet"
			type="text/css">
		<link href="<s:url value="/css2/c_table.css"/>" rel="stylesheet"
			type="text/css">
		<script type="text/javascript" src="<s:url value="/Js/jquery.js"/>"></script>
		<lk:res />
<SCRIPT LANGUAGE="JavaScript">
	function ExecMod(){
		var cityId = $("select[@name='cityId']").val();
		var startIp = $("input[@name='startIp']").val(); 
		var endIp = $("input[@name='endIp']").val(); 
		if(""==cityId||"-1"==cityId){
			alert("请选择属地");
			return false;
		}
		if(""==trim(startIp) || ""==trim(endIp)){
			alert("起始IP或终止IP不能为空！");
			return false; 
		} 
		
		if(!checkIP(startIp) || !checkIP(endIp))
		{
			return;
		}
		else
		{
			if(convertIP(startIp)>convertIP(endIp))
			{
				alert("起始IP不能大于终止IP");
				return false;
			}
		}
		
		
    	$("#doButton").attr("disabled",true);
    	var url = "<s:url value='/hgwipMgSys/ItvIpMg!add.action'/>";
		$.post(url,{
			cityId : cityId,
			startIp : startIp,
			endIp : endIp
		},function(ajax){
			 alert(ajax);
			 query();
		}); 
	    
	}
	
	function query()
	{
		var url = "<s:url value='/hgwipMgSys/ItvIpMg.action'/>";
	    //window.location.href=url;
		window.opener.location.href = url;
		window.close();
	}
   
	//去掉空格
	function trim(str){
      return str.replace(/(^\s*)|(\s*$)/g,"");
	}
</SCRIPT>

	</head>
	<body>
		<TABLE width="98%" align="center" class="querytable">
			<TR>
				<TD>
					<IMG height=20 src="<s:url value='/images/bite_2.gif'/>" width=24>
					您当前的位置：ip地址管理
				</TD>
			</TR>
		</TABLE>
		<br>
		<s:form action="ItvIpMg.action" method="post" enctype="multipart/form-data" name="batchexform" onsubmit="">
			<table class="querytable" width="98%" align="center">
				<tr>
					<td colspan="4" class="title_1">
						ip地址管理
					</td>
				</tr>
				<TR>
					<TD class="title_2" align="center" width="15%">
						属地
					</TD>
				<TD width="85%" colspan="3">
						<s:select list="cityList" name="cityId" headerKey="-1"
							headerValue="请选择属地" listKey="city_id" listValue="city_name"
							value="cityId" cssClass="bk" theme="simple" onchange="getNextCity();"></s:select>
					</TD>
			    </TR>
			    <TR>
					<TD class="title_2" align="center" width="15%">起始IP地址</TD>
					<TD colspan="3"> <input type="text" id="startIp" name="startIp" width="500"> </TD>
				</TR>
				<TR>
					<TD class="title_2" align="center" width="15%">终止IP地址</TD>
					<TD colspan="3"> <input type="text" id="endIp" name="endIp" width="500"> </TD>
				</TR>
				<TR>
					<td colspan="4" class="foot" align="right">
						<div class="right">
							<button id="doButton" onclick="ExecMod()">
								定制
							</button>
						</div>
					</td>
				</TR>
			</table>
		</s:form>
		<br>  
		<br>
	</body>
<script type="text/javascript">
   
	//IP 地址验证
	function checkIP(ip)
	{
		var exp=/^((\d{1,2}|1\d\d|2[0-4]\d|25[0-5])\.){3}(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])$/;
		var rev = false;
		if(null == ip.match(exp))
		{
			alert("IP地址格式不正确");
		}
		else
		{
			rev = true;
		}
		return rev;
	}
	//转换IP地址为数字
	function convertIP(ip)
	{
		var ipArray = ip.split(".");
		var newIP = "";
		for(var i=0;i<ipArray.length;i++)
		{
			if(ipArray[i].length==1)
			{
				ipArray[i] = "00"+ipArray[i];
			}
			else if(ipArray[i].length==2)
			{
				ipArray[i] = "0"+ipArray[i];
			}
			newIP += ipArray[i] + ".";
		}
		return newIP.substring(0,newIP.length-1);
	}
	
</script>