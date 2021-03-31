<%@ include file="/timelater.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="lk" uri="/linkage"%>
<html>
	<head>
	    <link rel="stylesheet" href="<s:url value="/css3/global.css"/>" type="text/css">
		<link rel="stylesheet" href="<s:url value="/css3/c_table.css"/>" type="text/css">		
		<SCRIPT LANGUAGE="JavaScript" SRC="<s:url value="/Js/jquery.js"/>"></SCRIPT>
		<script type="text/javascript" src="<s:url value="/Js/date/WdatePicker.js"/>"></script>
		<SCRIPT LANGUAGE="JavaScript" SRC="<s:url value="/Js/CheckForm.js"/>"></SCRIPT>
		
		<lk:res />
		<script language="JavaScript">
			function doQuery(){
				var starttime = $.trim($("input[@name='starttime']").val());
			    var endtime = $.trim($("input[@name='endtime']").val());
			    if(validateOneMonth(starttime,endtime)){
			    	alert("时间跨度不能超过一个月");
			    	return false;
			    }
			    
			    var servAccount = $.trim($("input[@name='servAccount']").val());
			    var deviceSerialnumber = $.trim($("input[@name='deviceSerialnumber']").val());
			    if(servAccount == "" && deviceSerialnumber == ""){
			    	alert("业务账号和设备序列号至少有一项不能为空！");
			    	return false;
			    }
			    
				$("#resultFrame").hide("");
				$("#tip_loading").show();
				var url = "<s:url value='/gtms/stb/resource/zeroConfigHistory!doZeroHistoryQuery.action'/>";
				$("#queryForm").attr("action", url);
				$("#queryForm").attr("target", "resultFrame");
				$("#queryForm").submit();
			}

			function doExcel() {
			    var starttime = $.trim($("input[@name='starttime']").val());
			    var endtime = $.trim($("input[@name='endtime']").val());		    
			    if(validateOneMonth(starttime,endtime)){
			    	alert("时间跨度不能超过一个月");
			    	return false;
			    }
			    
			    var servAccount = $.trim($("input[@name='servAccount']").val());
			    var deviceSerialnumber = $.trim($("input[@name='deviceSerialnumber']").val());
			    if(servAccount == "" && deviceSerialnumber == ""){
			    	alert("业务账号和设备序列号至少有一项不能为空！");
			    	return false;
			    }
			    
				var page="<s:url value='/gtms/stb/resource/zeroConfigHistory!doZeroHistoryExcel.action'/>?"
					+ "starttime=" + starttime + "&endtime=" + endtime
					+ "&servAccount=" + servAccount + "&deviceSerialnumber=" + deviceSerialnumber;
				document.all("childFrm").src=page;
			}	
			
			function setDataSize(dataHeight) {
				$("#resultFrame").height(dataHeight);
			}

			function showIframe() {
				$("#tip_loading").hide();
				$("#resultFrame").show();
			}
		</script>	
	</head>
	<body>	
	<TABLE class="querytable" style="width: 98%!important;" align="center">
		<TR>
			<TD >
				<IMG height=20 src="<s:url value='/images/bite_2.gif'/>" width=24>
	            	您当前的位置：零配置机顶盒历史配置查询
			</TD>
		</TR>
	</TABLE>
	<TABLE style="width: 98%!important;" align="center" cellpadding="0" cellspacing="0">
		<tr>
		<td>
			<form name="queryForm" id="queryForm">
			<table class="querytable">
				<tr> <th colspan=4>零配置机顶盒历史配置查询</th> </tr>
				<tr bgcolor=#ffffff>
					<td class=column align="center" width="15%">
						业务账号
					</td>
					<td width="35%">
						<input type="text" id="servAccount" name="servAccount" class='bk' value="<s:property value='servAccount'/>"/>&nbsp;<font color="red">*</font>			
					</td>
					<td class=column align="center" width="15%">
						设备序列号
					</td>
					<td width="35%">
						<input type="text" id="deviceSerialnumber" name="deviceSerialnumber" class='bk' value="<s:property value='deviceSerialnumber'/>"/>&nbsp;<font color="red">*</font>
					</td>
				</tr>
				<tr bgcolor=#ffffff>
					<td class=column align="center" width="15%">
						开始时间
					</td>
					<td width="35%">
						<lk:date id="starttime" name="starttime"
							defaultDate="%{starttime}" />
					</td>
					<td class=column align="center" width="15%">
						结束时间
					</td>
					<td width="35%">
						<lk:date id="endtime" name="endtime"
							defaultDate="%{endtime}" />
					</td>
				</tr>
				<tr bgcolor=#ffffff>
					<td class=foot colspan=4 align=right>
						<button id="btn" onclick="doQuery();">&nbsp;查询&nbsp;</button>
						<button id="btn" onclick="doExcel();">&nbsp;导出&nbsp;</button>
					</td>
				</tr>
			</table>
			<iframe id="resultFrame" name="resultFrame" width="100%"
				frameborder="0" scrolling="no" align="center"></iframe>
		    <div style="width: 100%; display: none; text-align: center;"
				id="tip_loading">
				正在加载数据,请耐心等待......
			</div>
			</form>
		</td>
		</tr>
		<tr STYLE="display: none">
			<td colspan="12">
				<iframe id="childFrm" src=""></iframe>
			</td>
		</tr>		
	</TABLE>
	
	</body>
	<%@ include file="/foot.jsp"%>
</html>