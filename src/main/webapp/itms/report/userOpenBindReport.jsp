<%@ include file="../../timelater.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="lk" uri="/linkage"%>

<link rel="stylesheet" href="<s:url value="/css3/c_table.css"/>" type="text/css">
<link rel="stylesheet" href="<s:url value="/css3/global.css"/>" type="text/css">
<SCRIPT LANGUAGE="JavaScript" SRC="<s:url value="/Js/jquery.js"/>"></SCRIPT>
<script type="text/javascript" src="../../Js/My97DatePicker/WdatePicker.js"></script>
<script language="JavaScript">
// 查询
function doQuery(){
	
	var cityId = $.trim($("select[@name='cityId']").val());                // 设备属地
    var starttime = $.trim($("input[@name='starttime']").val());           // 开始时间(开户时间)
    var endtime = $.trim($("input[@name='endtime']").val());               // 结束时间(开户时间)
    
    // 入参校验
    if(!checkQuery(cityId, starttime, endtime)){
    	return;
    }
    
    $("#dataList").html("");
    $("tr[@id='trData']").show();
    $("div[@id='QueryData']").html("正在查询，请稍等....");
    var url = '<s:url value='/itms/report/userOpenBindACT!userOpenBindList.action'/>'; 
	$.post(url,{
		cityId:cityId,
		starttime:starttime,
		endtime:endtime
	},function(ajax){
	    $("div[@id='QueryData']").html("");
		$("div[@id='QueryData']").append(ajax);
	});
}

//入参校验
function checkQuery(cityId, starttime, endtime){
	if(cityId == "-1"){
        alert("请选择属地");
        return false;
    }
	
	var startTime = strToTimestamp(starttime);
	var endTime = strToTimestamp(endtime);
	if(startTime > endTime){
		alert("开始时间不能大于结束时间");
        return false;
	}
	if((endTime - startTime) > 60 * 60 * 24 * 31 * 3){
		alert("开始至结束时间范围不能超过3个月");
        return false;
	}
	
	return true;
}

// 导出Excel
function toExcel( cityId, starttime, endtime ) {
	var page="<s:url value='/itms/report/userOpenBindACT!userOpenBindListToExcel.action'/>?"
		+ "cityId=" + cityId 
		+ "&starttime=" +starttime
		+ "&endtime=" +endtime;
	document.all("childFrm").src=page;
}


// 时间字符串转化为时间戳
function strToTimestamp(date){
	date = date.substring(0,19);    
	date = date.replace(/-/g,'/');
	return new Date(date).getTime() / 1000;
}


</script>

<br>
<TABLE>
	<tr>
		<td>
			<table class="green_gargtd">
				<tr>
					<th>
						新开用户绑定终端情况
					</th>
					<td>
						<img src="<s:url value="/images/attention_2.gif"/>" width="15" height="12">
						时间为开户时间
					</td>
				</tr>
			</table>
		</td>
	</tr>
	<tr>
		<td>
			<form name=frm>
				<table class="querytable">
					<tr>
						<th colspan=4>
							新开用户绑定终端情况
						</th>
					</tr>
					<TR bgcolor="#FFFFFF" id="gwShare_tr21" STYLE="">
						<td class="column" align="right" width="15%">
							设备属地
						</td>
						<td width="35%">
							<s:select list="cityList" name="cityId" headerKey="-1"
								headerValue="请选择属地" listKey="city_id" listValue="city_name"
								value="cityId" cssClass="bk"></s:select>
							&nbsp;<font style="color:red">*</font>
						</td>
						<td class="column" width='15%' align="right"></td>
                      	<td width='35%' align="left"></td>
					</TR>
					<tr bgcolor=#ffffff>
						<td class=column align=center width="15%">
							开始时间
						</td>
						<td>
							<input type="text" name="starttime" class='bk' readonly value="<s:property value="starttime"/>">
							<img name="shortDateimg"
								onClick="WdatePicker({el:document.frm.starttime,dateFmt:'yyyy-MM-dd HH:mm:ss',skin:'whyGreen'})"
								src="../../images/dateButton.png" width="15" height="12"
								border="0" alt="选择" />
						</td>
						<td class=column align=center width="15%">
							结束时间
						</td>
						<td>
							<input type="text" name="endtime" class='bk' readonly value="<s:property value="endtime"/>">
							<img name="shortDateimg"
								onClick="WdatePicker({el:document.frm.endtime,dateFmt:'yyyy-MM-dd HH:mm:ss',skin:'whyGreen'})"
								src="../../images/dateButton.png" width="15" height="12"
								border="0" alt="选择" />
						</td>
					</tr>
					<tr bgcolor=#ffffff>
						<td class=foot colspan=4 align=right>
							<button onclick="doQuery()">
								&nbsp;查 询&nbsp;
							</button>
						</td>
					</tr>
				</table>
			</form>
		</td>
	</tr>

	<tr id="trData" style="display: none">
		<td class="colum">
			<div id="QueryData" style="width: 100%; z-index: 1; top: 100px">
				正在查询，请稍等....
			</div>
		</td>
	</tr>
	<tr id="dataList" style="display: none">
		<td class="colum">
			<table class="listtable">
				<caption>
					用户绑定终端情况列表
				</caption>
				<thead>
					<tr>
						<th>LOID</th>
						<th>宽带账号</th>
						<th>属地</th>
						<th>厂商</th>
						<th>型号</th>
						<th>业务开通时间</th>
						<th>绑定时间</th>
						<th>最近上线时间</th>
					</tr>
				</thead>
				<tbody>
					<s:if test="data.size()>0">
						<s:iterator value="data">
							<tr>
								<td><s:property value="loid" /></td>
								<td><s:property value="broadband_account" /></td>
								<td><s:property value="city_name" /></td>
								<td><s:property value="vendor_name" /></td>
								<td><s:property value="device_model" /></td>
								<td><s:property value="opendate" /></td>
								<td><s:property value="binddate" /></td>
								<td><s:property value="last_time" /></td>
							</tr>
						</s:iterator>
					</s:if>
					<s:else>
						<tr>
							<td colspan=9>
								系统没有相关的用户信息!
							</td>
						</tr>
					</s:else>
				</tbody>
				<tfoot>
					<tr>
						<td colspan="9">
							<span style="float: right;"> 
								<lk:pages url="/itms/report/userOpenBindACT!userOpenBindListForPage.action" 
									styleClass="" showType="" isGoTo="true" changeNum="true" /> 
							</span>
							<IMG SRC="<s:url value="/images/excel.gif"/>" BORDER='0' ALT='导出列表' style='cursor: hand'
								onclick="toExcel('<s:property value="cityId"/>', '<s:property value="starttime"/>',
								'<s:property value="endtime"/>')">
						</td>
					</tr>
				</tfoot>
			
				<tr STYLE="display: none">
					<td colspan="9">
						<iframe id="childFrm" src=""></iframe>
					</td>
				</tr>
			</table>
		</td>
	</tr>
	<tr>
		<td height="20">
		</td>
	</tr>
</TABLE>

<%@ include file="/foot.jsp"%>
<script language="JavaScript">
	// 如果是点击页码跳转进来的话走这里
	var starttime1 = '<s:property value="starttime1"/>';
	if(starttime1){
		$("#dataList").show();
	}
</script>
