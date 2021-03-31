<%@ include file="/timelater.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>

<link rel="stylesheet" href="<s:url value="/css3/c_table.css"/>"
	type="text/css">
<link rel="stylesheet" href="<s:url value="/css3/global.css"/>"
	type="text/css">
<SCRIPT LANGUAGE="JavaScript" SRC="<s:url value="/Js/jquery.js"/>"></SCRIPT>
<script type="text/javascript"
	src="../../Js/My97DatePicker/WdatePicker.js"></script>
<script type="text/javascript">
function doQuery(){
	var con = $.trim($("select[@name='con']").val());
    var condition = $.trim($("input[@name='condition']").val());
    var openState = $.trim($("select[@name='openState']").val());
    var starttime = $.trim($("input[@name='starttime']").val());
	var endtime = $.trim($("input[@name='endtime']").val()); 
	if(condition == null || condition == ""){
    	alert("带星号的为必填项！");
   	    return false;
	}
	if(con=="1" && condition.length < 6){
		 alert("设备序列号的有效字符长度至少为6！");
    	 return false;
	}
	$("tr[@id='trData']").show();
    $("div[@id='QueryData']").html("正在统计，请稍等....");
    $("button[@name='button']").attr("disabled", true);
    var operType = "<s:property value = 'operType' />";
    var url="<s:url value='/gtms/config/netByDHCPStop!queryNetByDHCPList.action'/>";
	$.post(url,{
		con:con,
		condition:condition,
		openState:openState,
		starttime:starttime,
		endtime:endtime
	},function(ajax){	
		if(ajax > 1){
			alert("该宽带帐号对应多个用户，请使用逻辑sn或者设备序列号查询!");
			$("div[@id='QueryData']").html("");
		} else{
			$("div[@id='QueryData']").html("");
			$("div[@id='QueryData']").append(ajax);
		}
		$("button[@name='button']").attr("disabled", false);
	});
	
}

</script>

<br>
<table>
	<tr>
		<td>
			<table class="green_gargtd">
				<tr>
					<th>宽带DHCP关闭策略查询</th>
					<td><img src="<s:url value="/images/attention_2.gif"/>"
						width="15" height="12"></td>
					<td>查询宽带DHCP关闭策略情况。开始时间、结束时间为定制时间。</td>
				</tr>
			</table>
		</td>
	</tr>
	<tr>
		<td>
			<form name="frm">
				<table class="querytable">
					<tr>
						<th colspan=4>宽带DHCP关闭策略查询</th>
					</tr>
					<tr bgcolor=#ffffff>
						<td class=column align=center width="25%">
							<select name="con" class=column>
								<option value="1">设备序列号</option>
								<option value="0">LOID</option>
								<option value="-1">宽带账号</option>
							</select>
						</td>
						<td align=center width="25%">
							<input type="text" name="condition" class='bk' /> 
							<font color="red">*</font>
						</td>
						<td class=column  width="25%">
							开通状态
						</td>
						<td bgcolor=#eeeeee>
							<select name="openState" class=column>
								<option value="2">请选择</option>
								<option value="1">成功</option>
								<option value="0">失败</option>
								<option value="3">未做</option>
							</select>
						</td>
					</tr>
					<tr bgcolor=#ffffff>
						<td class=column align=center width="25%">
							开通时间
						</td>
						<td>
							<input type="text" name="starttime" class='bk' readonly value="<s:property value='starttime'/>">
							<img name="shortDateimg" onClick="WdatePicker({el:document.frm.starttime,dateFmt:'yyyy-MM-dd HH:mm:ss',skin:'whyGreen'})"
								src="../../images/dateButton.png" width="15" height="12" border="0" alt="选择" /> 
							<font color="red">*</font>
						</td>
						<td class=column align=center width="15%">
							结束时间
						</td>
						<td><input type="text" name="endtime" class='bk' readonly value="<s:property value='endtime'/>"> 
							<img name="shortDateimg" onClick="WdatePicker({el:document.frm.endtime,dateFmt:'yyyy-MM-dd HH:mm:ss',skin:'whyGreen'})"
								src="../../images/dateButton.png" width="15" height="12" border="0" alt="选择" /> 
							<font color="red">*</font>
						</td>
					</tr>
					<tr bgcolor=#ffffff>
						<td class=foot colspan=4>
							<button onclick="doQuery()" name="button" id="button">&nbsp;查询&nbsp;</button>
						</td>
					</tr>
				</table>
			</form>
		</td>
	</tr>
	
	<tr id="trData" style="display: none">
		<td class="colum">
			<div id="QueryData" style="width: 100%; z-index: 1; top: 100px">
				正在统计，请稍等....
			</div>
		</td>
	</tr>
	<tr>
		<td height="20">
		</td>
	</tr>
</table>
<%@ include file="/foot.jsp"%>

