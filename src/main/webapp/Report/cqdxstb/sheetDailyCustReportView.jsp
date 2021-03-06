<%@ page language="java" contentType="text/html; charset=GBK"
	pageEncoding="GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="lk" uri="/linkage"%>
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=GBK">
		<title>每日激活用户统计</title>
		<%
			 /**
			 * 工单统计报表查询
			 * 
			 * @author chenjie(67371)
			 * @version 1.0
			 * @since 2011-08-26
			 * @category
			 */
		%>
		<link rel="stylesheet" href="<s:url value="/css3/c_table.css"/>"
			type="text/css">
		<link rel="stylesheet" href="<s:url value="/css3/global.css"/>"
			type="text/css">
		<script type="text/javascript" src="<s:url value="/Js/jquery.js"/>"></script>
		<script type="text/javascript"
			src="<s:url value="/Js/My97DatePicker/WdatePicker.js"/>"></script>
		<script type="text/javascript">
		
		
		function getDays(strDateStart,strDateEnd){
			   var strSeparator = "-"; //日期分隔符
			   var oDate1;
			   var oDate2;
			   var iDays;
			   oDate1= strDateStart.split(strSeparator);
			   oDate2= strDateEnd.split(strSeparator);
			   var strDateS = new Date(oDate1[0], oDate1[1]-1, oDate1[2]);
			   var strDateE = new Date(oDate2[0], oDate2[1]-1, oDate2[2]);
			   iDays = parseInt(Math.abs(strDateS - strDateE ) / 1000 / 60 / 60 /24)//把相差的毫秒数转换为天数 
			   return iDays ;
			}
		
		
function query(){
	
	var qryType = $.trim($("#servTypeId").val());
    if(qryType == "-1"){
         alert("请选统计类型");
         return false;
    } 
    var startdate = $.trim($("#startOpenDate").val());
    var enddate = $.trim($("#endOpenDate").val());
    var daynum = getDays(startdate,enddate);
    if(daynum>6){
    	alert("时间区间请调整为七天之内！");
    	return;
    }
    $("#querybtn").attr("disabled","disabled");
    document.selectForm.action = "<s:url value='/gtms/stb/report/sheetDailyCustReport!getStatsReport.action'/>";
    document.selectForm.target = "dataForm";
	document.selectForm.submit();
}

//根据需要选择高级查询选项
function ShowDialog(leaf){
	//pobj = obj.offsetParent;
	oTRs = document.getElementsByTagName("TR");
	var m_bShow;
	var setvalueTemp = 0;
	for(var i=0; i<oTRs.length; i++){
		if(oTRs[i].leaf == leaf){
			m_bShow = (oTRs[i].style.display=="none");
			oTRs[i].style.display = m_bShow?"":"none";
		}
	}
	if(m_bShow){
		setvalueTemp = "1";
	}
	setValue(setvalueTemp);
}

//** iframe自动适应页面 **//
//输入你希望根据页面高度自动调整高度的iframe的名称的列表
//用逗号把每个iframe的ID分隔. 例如: ["myframe1", "myframe2"]，可以只有一个窗体，则不用逗号。
//定义iframe的ID
var iframeids=["dataForm"]

//如果用户的浏览器不支持iframe是否将iframe隐藏 yes 表示隐藏，no表示不隐藏
var iframehide="yes"

function dyniframesize() 
{
	var dyniframe=new Array()
	for (i=0; i<iframeids.length; i++)
	{
		if (document.getElementById)
		{
			//自动调整iframe高度
			dyniframe[dyniframe.length] = document.getElementById(iframeids[i]);
			if (dyniframe[i] && !window.opera)
			{
     			dyniframe[i].style.display="block"
     			//如果用户的浏览器是NetScape
     			if (dyniframe[i].contentDocument && dyniframe[i].contentDocument.body.offsetHeight)
      				dyniframe[i].height = dyniframe[i].contentDocument.body.offsetHeight; 
      			//如果用户的浏览器是IE
     			else if (dyniframe[i].Document && dyniframe[i].Document.body.scrollHeight) 
      				dyniframe[i].height = dyniframe[i].Document.body.scrollHeight;
   			 }
   		}
		//根据设定的参数来处理不支持iframe的浏览器的显示问题
		if ((document.all || document.getElementById) && iframehide=="no")
		{
			var tempobj=document.all? document.all[iframeids[i]] : document.getElementById(iframeids[i])
    		tempobj.style.display="block"
		}
	}
}





$(function(){
	//setValue();
	dyniframesize();
});

$(window).resize(function(){
	dyniframesize();
}); 

</script>
	</head>

	<body>
		<form name="selectForm" action="<s:url value='/gtms/stb/report/sheetDailyCustReport!getStatsReport.action'/>"
			target="dataForm">
			<input type="hidden" name="selectType" value="0" />
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
								<td width="162" align="center" class="title_bigwhite">
									每日激活用户统计
								</td>
								<td>
									<img src="<s:url value="/images/attention_2.gif"/>" width="15"
										height="12" />
									查询工单执行情况，每日激活用户统计
								</td>
							</tr>
						</table>
					</td>
				</tr>
				<tr>
					<td>
						<table class="querytable">

							<TR>
								<th colspan="4">
									每日激活用户统计
								</th>
							</tr>


							<TR bgcolor="#FFFFFF">
								<TD class=column align="right" width="20%">统计类型</TD>
								<TD width="30%" colspan="3">
								<select name="servTypeId" id="servTypeId" class=bk >
									<option value="-1">==请选择统计类型==</option>
									<option value="1">设备类型</option>
									<option value="2">区域</option>
									<option value="3">终端模式</option>
									<option value="4">业务类型</option>
									<option value="5">用户类型</option>
								</select>&nbsp; <font color="#FF0000">* </font></TD>
								<%-- <TD class=column align="right" width="20%">操作类型</TD>
								<TD width="30%"><select name="operateType" id="operateType" class=bk>
										<option value="-1">==请选择操作类型==</option>
										<option value="1">开户</option>
										<option value="3">销户</option>
								</select>&nbsp; <font color="#FF0000">* </font></TD> --%>
							</TR>
							
							<TR>
								<TD class=column width="15%" align='right'>
									开始时间
								</TD>
								<TD width="35%">
									<input type="text" name="startOpenDate" id="startOpenDate" readonly class=bk
										value="<s:property value="startOpenDate" />">
									<img name="shortDateimg"
										onClick="WdatePicker({el:document.selectForm.startOpenDate,dateFmt:'yyyy-MM-dd',skin:'whyGreen'})"
										src="../../../images/dateButton.png" width="15" height="12"
										border="0" alt="选择">
									&nbsp;
									<font color="red"> *</font>
								</TD>
								<TD class=column width="15%" align='right'>
									结束时间
								</TD>
								<TD width="35%">
									<input type="text" name="endOpenDate" id="endOpenDate" readonly class=bk
										value="<s:property value="endOpenDate" />">
									<img name="shortDateimg"
										onClick="WdatePicker({el:document.selectForm.endOpenDate,dateFmt:'yyyy-MM-dd',skin:'whyGreen'})"
										src="../../../images/dateButton.png" width="15" height="12"
										border="0" alt="选择">
									&nbsp;
									<font color="red"> *</font>
								</TD>
							</TR>
							<TR>
								<td colspan="4" align="right" class=foot>
									<button onclick="query()"  id="querybtn">
										&nbsp;查 询&nbsp;
									</button>
								</td>
							</TR>
						</table>
					</td>
				</tr>
				<tr>
					<td height="25" id="resultStr">

					</td>
				</tr>
				<tr>
					<td>
						<iframe id="dataForm" name="dataForm" height="0" frameborder="0"
							scrolling="no" width="100%" src=""></iframe>
					</td>
				</tr>
				<tr>
				<td>
				  <div style="border:1px solid #ACA899;width:100%;font-size:12px;text-align:left;padding:8px" id="sheetMsgs">
			               报表概述：查询最多连续七天的每日新开户且下发业务的用户数<br/>
			               统计维度：设备类型(机顶盒)，属地分组，终端类型，业务类型(iptv)，用户类型(IPTV用户)
</br>
 			</div>
				</td>
				</tr>
			</table>
			<br>
		</form>
	</body>
</html>