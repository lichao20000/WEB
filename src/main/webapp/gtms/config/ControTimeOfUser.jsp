<%@ include file="../../timelater.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<html><head>
<link rel="stylesheet" href="<s:url value="/css3/c_table.css"/>" type="text/css">
<link rel="stylesheet" href="<s:url value="/css3/global.css"/>" type="text/css">
<SCRIPT LANGUAGE="JavaScript" SRC="<s:url value="/Js/jquery.js"/>"></SCRIPT>
<script type="text/javascript" src="<s:url value="/Js/My97DatePicker/WdatePicker.js"/>"></script>
<script language="JavaScript">
var	width=600;    
var height=200;
//初始化时间
function initDate()
{
	//初始化时间  
	var theday=new Date();
	var day=theday.getDate();
	var month=theday.getMonth()+1;
	var year=theday.getFullYear();
	var hour = theday.getHours();
	var mimute = theday.getMinutes();
	var second = theday.getSeconds();
	
	var flag = '<s:property value="conTimeStart"/>' ;
    if(null!=flag &&""!=flag){
    	$("input[@name='conTimeStart']").val('<s:property value="conTimeStart"/>');
    	$("input[@name='conTimeEnd']").val('<s:property value="conTimeEnd"/>');
    }else{
	    $("input[@name='conTimeStart']").val(year+"-1-1 00:00:00");
	    $("input[@name='conTimeEnd']").val(year+"-" + month + "-" + day + " " + hour +":"+ mimute +":" + second);
    }
}
function doQuery(){
    var starttime = $("input[@name='conTimeStart']").val();           // 开始时间(用户受理时间)
    var endtime = $("input[@name='conTimeEnd']").val();     // 结束时间(用户受理时间)
    var typeId =  $("select[@name='typeId']").val();
    
    $("#tr_resList").show();   
   	$("#resList").html("正在努力为您查询，请稍后。。。");
    
    var url = "<s:url value='/gtms/config/timeSet!queryInfo.action'/>"; 
	$.post(url,{
		typeId : typeId,	
		conTimeStart:starttime,
		conTimeEnd:endtime
	},function(ajax){
		$("#tr_resList").show();
		$("#resList").html("");
		$("#resList").append(ajax);
	});
	
}
function openAdd(){
	
   var url = "<s:url value='/gtms/config/ControTimeOfUserAdd.jsp'/>";
   var sFeatures = 'dialogWidth='+width+'px;';
	   sFeatures += 'dialogHeight='+ height+'px';
	   sFeatures += 'resizable=no;help=no;center=yes;status=no;scroll=yes;edge=raised';
	window.showModalDialog(url ,'',sFeatures); 
}
</script>
</head>
<body>
<table id="resultTable" >
	<TR>
		<TD HEIGHT=20>&nbsp;</TD>
	</TR>
	<tr>
		<td>
			<table class="green_gargtd" >
				<tr>
					<th>
						用户受理时间管理
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
		<form id="frm" name="frm">
			<table class="querytable">
					<tr>
							<th colspan=4 class="gwShare_thTitle">
								用户受理时间管理
							</th>
							
					</tr>
					<TR bgcolor="#FFFFFF" id="tr_time" STYLE="">
							<td class="column" align="right" width="15%">
								时间类型
							</td>
							<td width="85%" colspan="3">
								<SELECT name="typeId">
									<option value="-1">==请选择==</option>
									<option value="1" selected="selected">用户受理时间</option>
									
								</SELECT>
							</td>
						</TR>
						<tr bgcolor=#ffffff>
							<td class=column align=center width="15%">
								开始时间
							</td>
							<td>
								<input type="text" name="conTimeStart" class='bk' readonly value="<s:property value='conTimeStart'/>">
								<img name="shortDateimg"
									onClick="WdatePicker({el:document.frm.conTimeStart,dateFmt:'yyyy-MM-dd HH:mm:ss',skin:'whyGreen'})"
									src="../../images/dateButton.png" width="15" height="12"
									border="0" alt="选择" />
							</td>
							<td class=column align=center width="15%">
								结束时间
							</td>
							<td>
								<input type="text" name="conTimeEnd" class='bk' readonly value="<s:property value='conTimeEnd'/>">
								<img name="shortDateimg"
									onClick="WdatePicker({el:document.frm.conTimeEnd,dateFmt:'yyyy-MM-dd HH:mm:ss',skin:'whyGreen'})"
									src="../../images/dateButton.png" width="15" height="12"
									border="0" alt="选择" />
							</td>
						</tr>
					<tr bgcolor="#ffffff"  >
							<td class=foot  width="100%" colspan="4" style="text-align:right;">
								<button name="button"  onclick="doQuery()">查询</button>
									&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
								<button name="button"  onclick="openAdd()">新增</button>
							</td>
					</tr>
				</table>
		</form>
		</td>
	<TR>
		<TD HEIGHT=20>&nbsp;</TD>
	</TR>
	<tr id="tr_resList" style="display:none;width: 100%; z-index: 1; top: 100px">
		<td>
			<div id="resList"/>
		</td>
	</tr>
</table>
</body>
<SCRIPT type="text/javascript">
	initDate();
	doQuery();
</SCRIPT>
</html>