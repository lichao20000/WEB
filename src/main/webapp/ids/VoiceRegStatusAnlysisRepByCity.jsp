<%@ page language="java" contentType="text/html; charset=GBK"
	pageEncoding="GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="lk" uri="/linkage"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<title>语音注册状态分析统计(按区域)</title>

<link rel="stylesheet" href="<s:url value="/css3/c_table.css"/>"
	type="text/css">
<link rel="stylesheet" href="<s:url value="/css3/global.css"/>"
	type="text/css">
<link rel="stylesheet" href="../css/tab.css" type="text/css">
<script type="text/javascript" src="<s:url value="/Js/jquery.js"/>"></script>
<SCRIPT LANGUAGE="JavaScript" SRC="/Js/CheckFormForm.js"></SCRIPT>
<script type="text/javascript" src="<s:url value="/Js/My97DatePicker/WdatePicker.js"/>"></script>
<script type="text/javascript">
	//var path = "" ;
    var dateType ="1";
	function query() {
		var reportType = dateType;
		$("tr[@id='trData']").show();
	    $("div[@id='QueryData']").html("正在统计，请稍等....");
		$("button[@name='button']").attr("disabled", true); 
		var cityId = $.trim($("select[@name='city_id']").val());
		 var endOpenDate = "";
	    if(dateType=='1'){
	    	endOpenDate = $.trim($("input[@name='endOpenDate']").val())
	    }else if(dateType=='2'){
	    	endOpenDate = $.trim($("input[@name='endOpenDate1']").val())
	    }else if(dateType=='3'){
	    	endOpenDate = $.trim($("input[@name='endOpenDate2']").val())
	    }
	    var url = "<s:url value='/ids/voiceRegStatusAnanlyReport!voiceRegQueryInfoByCity.action' />";
	    $.post(url,{
	    	city_id : cityId,
	    	endOpenDate : endOpenDate,
	    	reportType : reportType
	    },function(ajax){
	    	 $("div[@id='QueryData']").html("");		
	    	 $("div[@id='QueryData']").append(ajax);
	 		 $("button[@name='button']").attr("disabled", false); 
	    });	
	}
	//get the file path  按照区域的
	function downFile(city_id,numInfo){
		var endOpenDate = $.trim($("input[@name='endOpenDate']").val());
		if(dateType=='2'){
			endOpenDate =$("#weektime").val();
		}else if(dateType=='3'){
			endOpenDate = $.trim($("input[@name='endOpenDate2']").val());
		}
		var num = "";
		
		switch(numInfo){
		case '1': num="1"; 
		break;
		case '2': num="1_disabled";
		break;
		case '3': num="2";
		break;
		case '4': num="2_disabled";
		break;
		case '5': num="1_2_disabled";
		break;
		}
		var tempPath = "/voicePortByCity_" +city_id+"_line"+num+"_"+dateType+"_"+endOpenDate+".zip";
		
		var url = "<s:url value='/ids/voiceRegStatusAnanlyReport!downloadFile.action' />";
		 $.post(url,{filePath : tempPath}
		    ,function(ajax){
		    	var path = ajax.split(",");
		 		if("1"==path[0]){
		 			window.location.href = path[1];
		 		}else{
		 			alert("该文件不存在，请确定该文件是否生成！");
		 		}
		 		
		    });
	}
	
</script>
</head>

<body>
	<form id="form" name="selectForm"
		action="<s:url value=''/>">
		<table>
			<tr>
				<td HEIGHT=20>&nbsp;</td>
			</tr>
			<tr>
				<td>
					<table class="green_gargtd">
						<tr>
							<td width="162" align="center" class="title_bigwhite">
							语音注册状态分析统计(按区域)</td>
							<td><img src="<s:url value="/images/attention_2.gif"/>"
								width="15" height="12" /></td>
						</tr>
					</table>
				</td>
			</tr>
			<tr>
				<td>
					<table class="querytable">
						<TR>
							<td colspan="4">
								<table class="querytable">
									<td class="curendtab_bbms" id="td1" width="5%" ><a
										class="tab_A" href="javascript:chooseTypes(1);">日报表</a></td>
									<td class="endtab_bbms" id="td2" width="5%"><a class="tab_A"
										href="javascript:chooseTypes(2);">周报表</a></td>
									<td class="endtab_bbms" id="td3" width="5%"><a class="tab_A"
										href="javascript:chooseTypes(3)">月报表</a></td>
									<td width="85%"></td>
								</table>
							</td>
						</TR>
						<TR>
							<th colspan="4">语音注册状态分析统计 </th>
						</TR>
	
						<TR>
							<TD class=column width="15%" align='right'>时间</TD>
							<TD width="35%">
							<input type="hidden" id="weektime" name="weektime" value="">
								<div id="dayDate" style="display : block">
										<input type="text" id="endOpenDate" name="endOpenDate" readonly class=bk
											value="<s:property value="endOpenDate" />">
										<img id="shortDateimg"
											name="shortDateimg"
											onClick="WdatePicker({el:document.selectForm.endOpenDate,dateFmt:'yyyy-MM-dd',skin:'whyGreen'})"
											src="../images/dateButton.png" width="15" height="12"
											border="0" alt="选择">
								</div>
								<div id="weekDate" style="display : none">
										<input type="text" id="endOpenDate1" name="endOpenDate1" readonly class=bk
											value="<s:property value="endOpenDate" />">
										<img id="shortDateimg1"
											name="shortDateimg1"
											onClick="WdatePicker({el:document.selectForm.endOpenDate1,skin:'whyGreen',isShowWeek:true,onpicked:function(){$dp.$('weektime').value=$dp.cal.getP('W',$dp.cal.getDateStr('yyyy-WW'));}})"
											src="../images/dateButton.png" width="15" height="12"
											border="0" alt="选择">
								</div>
								<div id="monthDate" style="display : none">
										<input type="text" id="endOpenDate2" name="endOpenDate2" readonly class=bk
											value="<s:property value="endOpenDate" />">
										<img id="shortDateimg2"
											name="shortDateimg2"
											onClick="WdatePicker({el:document.selectForm.endOpenDate2,dateFmt:'yyyy-MM',skin:'whyGreen'})"
											src="../images/dateButton.png" width="15" height="12"
											border="0" alt="选择">
								</div>
							</TD>
							<TD class=column width="15%" align='right'>属地</TD>
							<TD width="35%"><s:select list="cityList" name="city_id"
									headerKey="00" headerValue="请选择属地" listKey="city_id"
									listValue="city_name" cssClass="bk"></s:select></TD>
						</TR>
	
						<TR>
							<td colspan="4" align="right" class=foot>
								<button  onclick="query()" id="button" name="button" >&nbsp;查&nbsp;询&nbsp;</button>
								<input type="hidden" id="filePath" name="filePath" value="">
							</td>
						</TR>
					</table>
				</td>
			</tr>
			<tr>
				<td height="25" id="resultStr"></td>
			</tr>
			<tr id="trData" style="display: none">
						<td class="colum">
							<div id="QueryData" style="width: 100%; z-index: 1; top: 100px">
								正在查询，请稍等....
							</div>
						</td>
			</tr>
			<tr>
				<td height="25"></td>
			</tr>
			<tr>
				<td id="bssSheetInfo"></td>
			</tr>
		</table>
		<br>
	</form>
</body>
</html>
<script type="text/javascript">
//** iframe自动适应页面 **//
//输入你希望根据页面高度自动调整高度的iframe的名称的列表
//用逗号把每个iframe的ID分隔. 例如: ["myframe1", "myframe2"]，可以只有一个窗体，则不用逗号。
//定义iframe的ID
var iframeids = [ "dataForm" ]

//如果用户的浏览器不支持iframe是否将iframe隐藏 yes 表示隐藏，no表示不隐藏
var iframehide = "yes"

function dyniframesize() {
	var dyniframe = new Array()
	for (i = 0; i < iframeids.length; i++) {
		if (document.getElementById) {
			//自动调整iframe高度
			dyniframe[dyniframe.length] = document
					.getElementById(iframeids[i]);
			if (dyniframe[i] && !window.opera) {
				dyniframe[i].style.display = "block"
				//如果用户的浏览器是NetScape
				if (dyniframe[i].contentDocument
						&& dyniframe[i].contentDocument.body.offsetHeight)
					dyniframe[i].height = dyniframe[i].contentDocument.body.offsetHeight;
				//如果用户的浏览器是IE
				else if (dyniframe[i].Document
						&& dyniframe[i].Document.body.scrollHeight)
					dyniframe[i].height = dyniframe[i].Document.body.scrollHeight;
			}
		}
		//根据设定的参数来处理不支持iframe的浏览器的显示问题
		if ((document.all || document.getElementById) && iframehide == "no") {
			var tempobj = document.all ? document.all[iframeids[i]]
					: document.getElementById(iframeids[i])
			tempobj.style.display = "block"
		}
	}
}

$(function() {
	dyniframesize();
});

$(window).resize(function() {
	dyniframesize();
});
/**
 * 初始化日报表中的日期参数将
 */
 function chooseTypes(type) {
		if (type == "1") {
			document.all("td1").className = "curendtab_bbms";
			document.all("td2").className = "endtab_bbms";
			document.all("td3").className = "endtab_bbms";
			$("#dayDate").css('display','block');
		    $("#weekDate").css('display','none');
	   		$("#monthDate").css('display','none');
			dateType ="1";
			
		}
		if (type == "2") {
			document.all("td1").className = "endtab_bbms";
			document.all("td2").className = "curendtab_bbms";
			document.all("td3").className = "endtab_bbms";
			$("#dayDate").css('display','none');
	 	    $("#weekDate").css('display','block');
		 	$("#monthDate").css('display','none');
			dateType ="2";
			
		}
		if (type == "3") {
			document.all("td1").className = "endtab_bbms";
			document.all("td2").className = "endtab_bbms";
			document.all("td3").className = "curendtab_bbms";
			$("#QueryData").removeClass("hover");
			 $("#dayDate").css('display','none');
			 $("#weekDate").css('display','none');
			 $("#monthDate").css('display','block');
			dateType ="3";
		}
	}
</script>
<%@ include file="../../foot.jsp"%>