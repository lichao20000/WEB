<%@ include file="../../timelater.jsp"%>
<%@ include file="../head.jsp"%>
<%@ page language="java" contentType="text/html; charset=GBK" pageEncoding="GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@taglib prefix="lk" uri="/linkage" %>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<title>?ײͶ?Ӧ?ն?ͳ??</title>
<link href="<s:url value="../../css/css_green.css"/>" rel="stylesheet" type="text/css">
<script type="text/javascript" src="<s:url value="/Js/jquery.js"/>"></script>
<script type="text/javascript" src="../../Js/jsDate/WdatePicker.js"></script>
<SCRIPT LANGUAGE="JavaScript">

$(function(){
	init();
});

function init(){
	
	var uniformid = "<s:property value="reportType" />";
	if("none"=="<s:property value="booked" />"){

		if(2==uniformid){
			$("#td1").attr("className","curendtab_bbms");
			$("#td1").show();
			$("#td2").hide();
			$("#td3").hide();
			$("#td4").hide();
			$("#query1").show();
			$("#query2").hide();
			$("#query3").hide();
			$("#query4").hide();
			queryData(uniformid,"<s:property value="cityId" />",strTime2Second("<s:property value="hourData" />"));
		}
		
		if(2==uniformid){
			$("#td2").attr("className","curendtab_bbms");
			$("#td1").hide();
			$("#td2").show();
			$("#td3").hide();
			$("#td4").hide();
			$("#query1").hide();
			$("#query2").show();
			$("#query3").hide();
			$("#query4").hide();
			queryData(uniformid,"<s:property value="cityId" />",strTime2Second2("<s:property value="dayData" />"));
		}

		if(3==uniformid){
			$("#td3").attr("className","curendtab_bbms");
			$("#td1").hide();
			$("#td2").hide();
			$("#td3").show();
			$("#td4").hide();
			$("#query1").hide();
			$("#query2").hide();
			$("#query3").show();
			$("#query4").hide();
			queryData(uniformid,"<s:property value="cityId" />",strTime2Second2("<s:property value="weekData" />"));
		}
		
		if(4==uniformid){
			$("#td4").attr("className","curendtab_bbms");
			$("#td1").hide();
			$("#td2").hide();
			$("#td3").hide();
			$("#td4").show();
			$("#query1").hide();
			$("#query2").hide();
			$("#query3").hide();
			$("#query4").show();
			queryData(uniformid,"<s:property value="cityId" />",strTime2Second2("<s:property value="monthData" />"));
		}	
	}else{
		queryData(uniformid,"<s:property value="cityId" />",strTime2Second("<s:property value="hourData" />"));
	}
}

function winNavigate(uniformid)
{

	//alert(uniformid);
	switch (uniformid)
	{
		case 1:
		{
			document.all("td1").className="curendtab_bbms";
			document.all("td2").className="endtab_bbms";
			document.all("td3").className="endtab_bbms";
			document.all("td4").className="endtab_bbms";
			document.all("query1").style.display="";
			document.all("query2").style.display="none";
			document.all("query3").style.display="none";
			document.all("query4").style.display="none";
			break;
		}
		case 2:
		{
			document.all("td1").className="endtab_bbms";
			document.all("td2").className="curendtab_bbms";
			document.all("td3").className="endtab_bbms";
			document.all("td4").className="endtab_bbms";
			document.all("query1").style.display="none";
			document.all("query2").style.display="";
			document.all("query3").style.display="none";
			document.all("query4").style.display="none";
			break;
		}
		case 3:
		{
			document.all("td1").className="endtab_bbms";
			document.all("td2").className="endtab_bbms";
			document.all("td3").className="curendtab_bbms";
			document.all("td4").className="endtab_bbms";
			document.all("query1").style.display="none";
			document.all("query2").style.display="none";
			document.all("query3").style.display="";
			document.all("query4").style.display="none";
			break;
		}
		
		case 4:
		{
			document.all("td1").className="endtab_bbms";
			document.all("td2").className="endtab_bbms";
			document.all("td3").className="endtab_bbms";
			document.all("td4").className="curendtab_bbms";
			document.all("query1").style.display="none";
			document.all("query2").style.display="none";
			document.all("query3").style.display="none";
			document.all("query4").style.display="";
			break;
		}
		
	}
}

function getLastDay(strDay)
{
	//ѡ?????ڵ?ǰһ??
	var intDay1=DateParse(strDay)-86400000;
	var d=new Date(intDay1);
	strDay=d.getFullYear()+"-"+(d.getMonth()+1)+"-"+d.getDate();		
	return strDay;
}

function queryUser(querytype)
{
	winNavigate(querytype);
	var strDay1="";
	
	var submitStart = "";
	
	var d=new Date();
	var today=d.getFullYear()+"-"+(d.getMonth()+1)+"-"+d.getDate();
	var intToday=DateParse(today);
	
	var cityId = "";
	
	switch (querytype)
	{
		case 1:
		{
			cityId = document.form1.cityId_hour.value;
			if(""==cityId){
				alert("??ѡ?????أ?");
				return;
			}
			
			strDay1=form1.hour_date.value;
			submitStart = form1.hour_date.value;
			
			var intDay1=DateParse(strDay1);
			
			if(intDay1>intToday)
			{
				window.alert("??ѯʱ?䲻?ܴ???ϵͳ??ǰʱ??:"+today);
				return;
			}
			
			break;
		}
		case 2:
		{
			cityId = document.form1.cityId_data.value;
			if(""==cityId){
				alert("??ѡ?????أ?");
				return;
			}
			
			strDay1=form1.q_date.value;
			submitStart = form1.q_date.value;
			
			var intDay1=DateParse(strDay1);
			
			if(intDay1>intToday)
			{
				window.alert("??ѯʱ?䲻?ܴ???ϵͳ??ǰʱ??:"+today);
				return;
			}
			
			break;
		}
		case 3:
		{
			cityId = document.form1.cityId_week.value;
			if(""==cityId){
				alert("??ѡ?????أ?");
				return;
			}

			strDay1=form1.start.value;
			
			submitStart = form1.start.value;
			
			var intDay1=DateParse(strDay1);
			
			if( intDay1>intToday)
			{
				window.alert("??ѯʱ?䲻?ܴ???ϵͳ??ǰʱ??:"+today);
				return;
			}

			break;
		}
		
		case 4:
		{
			cityId = document.form1.cityId_month.value;
			if(""==cityId){
				alert("??ѡ?????أ?");
				return;
			}
		
			strDay1=form1.q_month.value;
			
			submitStart = form1.q_month.value;
			
			var intDay1=DateParse(strDay1);
			
			if( intDay1>intToday)
			{
				window.alert("??ѯʱ?䲻?ܴ???ϵͳ??ǰʱ??:"+today);
				return;
			}
			
			break;
		
		}
	}
	
	if("1"==querytype){
		queryData(querytype,cityId,strTime2Second(submitStart));
	}else{
		queryData(querytype,cityId,strTime2Second2(submitStart));
	}
}

//?ύ??ѯ
function queryData(reportType,city_id,timeStart){
	
	var url = "gwms/report/servPackageDevice!getDayReport.action";
	
	var dataList = document.getElementById("dataList");
	var bookparam = document.form1.bookparam.value;
	
	if("null"!=dataList && null!=dataList ){
		dataList.innerHTML = "????????ͳ????...";
	}
	
	$.post(url,{
		reportType:reportType,
		cityId:city_id,
		longData:timeStart,
		bookparam:bookparam
    },function(mesg){
    	document.getElementById("dataList").innerHTML = mesg;
    });
}

//????excel??ѯ
function queryDataForExcel(reportType,city_id,timeStart){
	
	var url = "gwms/report/servPackageDevice!getDayReport.action";	
	var bookparam = document.form1.bookparam.value;
	
	url = url + "?reportType="+reportType+"&cityId="+city_id+"&longData="+timeStart+"&bookparam="+bookparam;
	
	url = url+"&isReport=excel"
	document.frm2.action = url;
	document.frm2.method = "post";
	document.frm2.submit();
}

//????pdf??ѯ
function queryDataForPdf(reportType,city_id,timeStart){
	
	var url = "gwms/report/servPackageDevice!getDayReport.action";	
	var bookparam = document.form1.bookparam.value;
	url = url + "?reportType="+reportType+"&cityId="+city_id+"&longData="+timeStart+"&bookparam="+bookparam;
	
	url = url+"&isReport=pdf"
	document.frm2.action = url;
	document.frm2.method = "post";
	document.frm2.submit();
}

function queryDataForPrint(reportType,city_id,timeStart){
	
	var url = "gwms/report/servPackageDevice!getDayReport.action";	
	var bookparam = document.form1.bookparam.value;
	url = url + "?reportType="+reportType+"&cityId="+city_id+"&longData="+timeStart+"&bookparam="+bookparam;
	url = url+"&isReport=print";
	
	window.open(url,"","left=80,top=80,width=800,height=400,resizable=yes,scrollbars=yes");
	
}

//??ʽ??ʱ??
function formatDate(strValue)
{
	var str1=strValue.substring(strValue.indexOf("-")+1,strValue.lastIndexOf("-"));
		str1=(str1.length==1)?"0"+str1:str1;
	var str2=strValue.substring(strValue.lastIndexOf("-")+1);
		str2=(str2.length==1)?"0"+str2:str2;
	strValue=strValue.substring(0,strValue.indexOf("-")+1)+str1+"-"+str2;
	return strValue;
}

//??2004-10-20??????ʱ??ת????long??
function DateParse(strValue)
{
	strValue=strValue.substring(5,10)+"-"+strValue.substring(0,4);	
	return Date.parse(strValue);
}

function strTime2Second(dateStr){
	
	var temp = dateStr.split(' ')
	var date = temp[0].split('-');   
    var time = temp[1].split(':'); 
	
	var reqReplyDate = new Date();
	reqReplyDate.setYear(date[0]);
    reqReplyDate.setMonth(date[1]-1);
    reqReplyDate.setDate(date[2]);
    reqReplyDate.setHours(time[0]);
    reqReplyDate.setMinutes(time[1]);
    reqReplyDate.setSeconds(time[2]);

	return Math.floor(reqReplyDate.getTime()/1000);
}

function strTime2Second2(dateStr){
	
	var temp = dateStr.split('-')
	
	var reqReplyDate = new Date();
	reqReplyDate.setYear(temp[0]);
    reqReplyDate.setMonth(temp[1]-1);
    reqReplyDate.setDate(temp[2]);
	
	return Math.floor(reqReplyDate.getTime()/1000);
}

//???Ʊ???
function subscribe_to(){
	document.all("url").value = "/gwms/report/servPackageDevice.action";
	var page = "reportBook.jsp?tt="+ new Date().getTime();
	var height = 420;
	var width = 700;
	var left = (screen.width-width)/2;
	var top  = (screen.height-height)/2;
	var w = window.open(page,"ss","left="+left+",top="+top+",width="+width+",height="+height+",resizable=yes,scrollbars=no,toolbar=no");
	
}

</SCRIPT>
<%@ include file="../../toolbar.jsp"%>
<style>
span
{
	position:static;
	border:0;
}
</style>
</head>
<body>
<form name="form1">
<input type="hidden" name="bookparam" value="<s:property value="bookparam"/>"/>
<TABLE border=0 cellspacing=0 cellpadding=0 width="100%">
	<TR>
		<TD HEIGHT=20>&nbsp;</TD>
	</TR>
	<TR>
  		<TD>
			<TABLE width="95%" border=0 cellspacing=0 cellpadding=0 align="center" valign="middle">
				<TR>
					<TD>
						<TABLE width="30%" border=0 cellspacing=0 cellpadding=0>
							<TR height=30>
								<TH class="curendtab_bbms" id="td1"><a class="tab_A" href="javascript:queryUser(1);">Сʱͳ??</a></TH>
								<TH class="endtab_bbms" id="td2"><a class="tab_A" href="javascript:queryUser(2);">??ͳ??</a></TH>
								<TH class="endtab_bbms" id="td3"><a class="tab_A" href="javascript:queryUser(3);">??ͳ??</a></TH>
								<TH class="endtab_bbms" id="td4"><a class="tab_A" href="javascript:queryUser(4);">??ͳ??</a></TH>
							</TR>
						</TABLE>
 					</TD>
				</TR>
				<TR>
					<TD bgcolor="#999999">
						<TABLE width="100%" border=0 cellspacing=1 cellpadding=1 align="center">
	     					<TR bgcolor="#FFFFFF" align="right">
	      						<TD class=column height="20">
									<input type="button" value="????????" class="jianbian" style="display:<s:property value="booked"/>" onclick="subscribe_to()">&nbsp;
									<input type="hidden" name="url" value="">
									<input type="hidden" name="bookparamfirst" value="<s:property value="bookparamfirst" />">
								</TD>
	   						</TR>
	   						<tr bgcolor="#FFFFFF" id="query1" style="display:">
								<td align="right">
									ѡ??????
									<s:select label="ѡ??????" list="cityList" name="cityId_hour" listKey="city_id" listValue="city_name"
            							emptyOption="true"  value="cityId"/>
									&nbsp;??ѯʱ??
									<input type="text" name="hour_date" value='<s:property value="hourData" />' readonly class=bk> <img name="shortDateimg"
										onclick="new WdatePicker(document.form1.hour_date,'%Y-%M-%D %h:%m:%s',true,'whyGreen')"
										src="../../images/search.gif" width="15" height="12" border="0" alt="ѡ??">(YYYY-MM-DD HH:mm:ss)

									<input type="button" name="btn0" value=" ?? ѯ " class=jianbian onclick="javascript:queryUser(1)">
								</td>
							</tr>
							<tr bgcolor="#FFFFFF" id="query2" style="display:none">
								<td align="right">
									ѡ??????
									<s:select label="ѡ??????" list="cityList" name="cityId_data" listKey="city_id" listValue="city_name"
            							emptyOption="true"  value="cityId"/>
									&nbsp;??ѯʱ??
									<input type="text" name="q_date" value='<s:property value="dayData" />' readonly class=bk> <img name="shortDateimg"
										onclick="new WdatePicker(document.form1.q_date,'%Y-%M-%D',true,'whyGreen')"
										src="../../images/search.gif" width="15" height="12" border="0" alt="ѡ??">(YYYY-MM-DD)

									<input type="button" name="btn0" value=" ?? ѯ " class=jianbian onclick="javascript:queryUser(2)">
								</td>
							</tr>
							<tr bgcolor="#FFFFFF" id="query3" style="display:none">
								<td align=right>
									ѡ??????
									<s:select label="ѡ??????" list="cityList" name="cityId_week" listKey="city_id" listValue="city_name"
            							emptyOption="true"  value="cityId"/>
									&nbsp;??ѯʱ??
									<input type="text" name="start" value='<s:property value="weekData" />' readonly class=bk> <img name="shortDateimg"
										onclick="new WdatePicker(document.form1.start,'%Y-%M-%D',true,'whyGreen')"
										src="../../images/search.gif" width="15" height="12" border="0" alt="ѡ??">(YYYY-MM-DD)
									<input type="button" name="btn2" value=" ?? ѯ " class=jianbian onclick="javascript:queryUser(3);">
								</td>
							</tr>
							<tr bgcolor="#FFFFFF" id="query4" style="display:none">
								<td align=right>
									ѡ??????
									<s:select label="ѡ??????" list="cityList" name="cityId_month" listKey="city_id" listValue="city_name"
            							emptyOption="true"  value="cityId"/>
									&nbsp;??ѯʱ??
									<input type="text" name="q_month" value='<s:property value="weekData" />' readonly class=bk> <img name="shortDateimg"
										onclick="new WdatePicker(document.form1.q_month,'%Y-%M-%D',true,'whyGreen')"
										src="../../images/search.gif" width="15" height="12" border="0" alt="ѡ??">(YYYY-MM-DD)
									<input type="button" name="btn3" value=" ?? ѯ " class=jianbian onclick="javascript:queryUser(4)">&nbsp;
								</td>
							</tr>
						</TABLE>
					</TD>
				</TR>
				<TR bgcolor="#FFFFFF">
					<TD bgcolor=#999999><div id="dataList">????????ͳ????...</div></TD>
				</TR>
			</TABLE>
		</TD>
	</TR>
</TABLE>
</form>
<form name="frm2"></form>
</body>
<br>
<br>
<%@ include file="../foot.jsp"%>