<%@ include file="/timelater.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>

<link rel="stylesheet" href="<s:url value="/css3/c_table.css"/>"
	type="text/css">
<link rel="stylesheet" href="<s:url value="/css3/global.css"/>"
	type="text/css">
<SCRIPT LANGUAGE="JavaScript" SRC="<s:url value="/Js/jquery.js"/>"></SCRIPT>
<script type="text/javascript"
	src="<s:url value="/Js/jsDate/WdatePicker.js"/>"></script>

<script language="JavaScript">


function doQuery(){
	var cityId = $.trim($("select[@name='cityId']").val());
    var starttime = $.trim($("input[@name='starttime']").val());
    var endtime = $.trim($("input[@name='endtime']").val());
    if(cityId == "-1"){
         alert("请选择属地");
         return false;
    }
    $("tr[@id='trData']").show();
    $("div[@id='QueryData']").html("正在统计，请稍等....");
    var url = '<s:url value='/itms/report/bindException.action'/>'; 
	$.post(url,{
		cityId:cityId,
		starttime:starttime,
		endtime:endtime
	},function(ajax){	
	    $("div[@id='QueryData']").html("");
		$("div[@id='QueryData']").append(ajax);
	});
}

function ToExcel(cityId,starttime1,endtime1) {
	var page="<s:url value='/itms/report/bindException!getExcel.action'/>?"
		+ "&starttime1=" + starttime1
		+ "&endtime1=" + endtime1
		+ "&cityId=" + cityId;
	document.all("childFrm").src=page;
}

function openHgw(cityId,starttime1,endtime1,flag,isAll){
	var page="<s:url value='/itms/report/PVCReport!getHgw.action'/>?"
		+ "cityId=" + cityId 
		+ "&starttime1=" +starttime1
		+ "&endtime1=" +endtime1
		+ "&reform_flag=" +flag
		+ "&isAll=" +isAll;
	window.open(page,"","left=50,top=50,height=550,width=900,toolbar=no,menubar=no,location=no,resizable=yes scrollbars=yes");
}

function countBycityId(cityId,starttime,endtime){

    $("tr[@id='trData']").show();
    $("div[@id='QueryData']").html("正在统计，请稍等....");
    var url = '<s:url value='/itms/report/bindException.action'/>'; 
	$.post(url,{
		cityId:cityId,
		starttime:starttime,
		endtime:endtime
	},function(ajax){	
	    $("div[@id='QueryData']").html("");
		$("div[@id='QueryData']").append(ajax);
	});
}

</script>

<br>
<TABLE>
	<tr>
		<td>
			<table class="green_gargtd">
				<tr>
					<th>
						用户路由开通
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
			<form name=frm>
				<table class="querytable">
					<tr>
					<th colspan=4>
					用户路由开通
					</th>
					</tr>
					<tr bgcolor=#ffffff>
						<td class=column align=center width="15%">
							用户账号
						</td>
						<td>
							<input type="text" name="starttime" class='bk'>			
						</td>
					</tr>
					<tr bgcolor=#ffffff>
						<td class=foot colspan=2 align=right>
							<button onclick="doQuery()">路由下发支持情况查询</button>
						</td>
					</tr>
				</table>
			</form>
		</td>
	</tr>

	<tr id="trData" style="display:">
		<td class="colum">
			<table class="listtable">
				<caption>
					查询结果
				</caption>
				<thead>
					<tr>
						<th colspan="1">
							设备厂商
						</th>
						<th colspan="1">
							设备型号
						</th>
						<th colspan="1">
							设备序列号
						</th>
						<th colspan="1">
							是否支持路由
						</th>
						<th colspan="1">
							用户账号
						</th>
						<th colspan="1">
							用户密码
						</th>
					</tr>
				</thead>
				<tbody>
					<tr>
						<td align="center">
							华为
						</td>
						<td align="center">
							XXXX
						</td>
						<td align="center">
							SAF2323SDFSF93
						</td>
						<td align="center">
							支持
						</td>
						<td align="center">
							02534563326
						</td>
						<td align="center">
							<input type="text" name="starttime" class='bk'>	
						</td>
					</tr>
				</tbody>
				<tfoot>
					<tr>
						<td colspan="6">
							<button onclick="doQuery()">路由下发</button>
						</td>
					</tr>
				</tfoot>
			</table>
		</td>
	</tr>
	<tr>
		<td height="20">
		</td>
	</tr>
</TABLE>

<%@ include file="/foot.jsp"%>
