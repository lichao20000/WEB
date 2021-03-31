<%@ include file="../timelater.jsp"%>
<%@ page contentType="text/html;charset=gb2312"%>
<!--取得上一个页面提交的参数-->
<%@ include file="../head.jsp"%>
<TITLE>综合网管 - 选择时间 tttt</TITLE>
<SCRIPT LANGUAGE="JavaScript" src="../Js/LayWriter.js"></SCRIPT>
<SCRIPT LANGUAGE="JavaScript">
<!--
function getDetailTime(newdate) {
	var ny = newdate.getYear(); //获取当前年份(2位)
	var nm = newdate.getMonth()+1; //获取当前月份(0-11,0代表1月) 
	var nd = newdate.getDate(); //获取当前日(1-31) 
	var nw = newdate.getDay(); //获取当前星期X(0-6,0代表星期天) 
	var nh = newdate.getHours(); //获取当前小时数(0-23) 
	var nmi = newdate.getMinutes(); //获取当前分钟数(0-59)
	
	//return nmi+":"+nh+":"+nd+":"+nw+":"+nm+":"+ny;
	return "0:"+nmi+":"+nh+":"+nd+":"+nm+":?:"+ny;
}
function returnTime(para)
{
	if(para=="false") 
	{
		window.returnValue="";
		window.close();
	}
	var myDate = new Date(); 
	var newdate = new Date();

	var y = myDate.getYear(); //获取当前年份(2位)
	var m = myDate.getMonth()+1; //获取当前月份(0-11,0代表1月) 
	var d = myDate.getDate(); //获取当前日(1-31) 
	var w = myDate.getDay(); //获取当前星期X(0-6,0代表星期天) 
	var h = myDate.getHours(); //获取当前小时数(0-23) 
	var mi = myDate.getMinutes(); //获取当前分钟数(0-59)
	var content="0:";
	var myfrm=document.frm;
	//如果输入月份
	if (!myfrm.month.disabled) {
		var hMonthLa = myfrm.month.value;
		if (hMonthLa==null || hMonthLa == "") {
			alert("请输入几个月后!");
			return false;
		}
		var newMonth = Number(m)+Number(hMonthLa);
		if (newMonth > 12 && newMonth%12 == 0) {
			y = Number(y) + Math.floor(Number(newMonth/12))-1;
			newMonth = 12;
		} else if (newMonth > 12) {
			y = Number(y) + Math.floor(Number(newMonth/12));
			newMonth = Math.ceil(newMonth%12);
		}
		content+=mi+":"+h+":"+d+":"+newMonth+":?:"+y;
		//content = y+":"+w+":"+newMonth+":"+d+":"+h+":"+mi;
	}
	//如果输入周
	if (!myfrm.week.disabled) {
		var hWeekLa = myfrm.week.value;
		if (hWeekLa==null || hWeekLa == "") {
			alert("请输入几个周后!");
			return false;
		}
		var newtimems=newdate.getTime()+(hWeekLa*7*24*60*60*1000);
		newdate.setTime(newtimems);
		content = getDetailTime(newdate);
	}
	//如果输入天数
	if (!myfrm.day.disabled) {
		var hDayLa = myfrm.day.value;
		if (hDayLa==null || hDayLa == "") {
			alert("请输入几个天后!");
			return false;
		}
		var newtimems=newdate.getTime()+(hDayLa*24*60*60*1000);
		newdate.setTime(newtimems);
		content = getDetailTime(newdate);
	}
	//如果输入小时
	if (!myfrm.hour.disabled) {
		var hHourLa = myfrm.hour.value;
		if (hHourLa==null || hHourLa == "") {
			alert("请输入几小时后!");
			return false;
		}
		var newtimems=newdate.getTime()+(hHourLa*60*60*1000);
		newdate.setTime(newtimems);
		content = getDetailTime(newdate);
	}
	//如果输入分钟
	if (!myfrm.minute.disabled) {
		var hMinuteLa = myfrm.minute.value;
		if (hMinuteLa==null || hMinuteLa == "") {
			alert("请输入几分钟后!");
			return false;
		}
		var newtimems=newdate.getTime()+(hMinuteLa*60*1000);
		newdate.setTime(newtimems);
		content = getDetailTime(newdate);
	}

	content = "秒:分:时:日:月:周:年 =" + content;

	if(confirm(content+"，确认编辑好了时间吗？")==true){
		window.returnValue= content;
		window.close();
	}
	else {
		window.returnValue=null;
		return false;
	}
}


function radioClick(radioVal) {
	if (radioVal == "1") {
	document.all("month").disabled = 0;
	document.all("week").disabled = 1;
	document.all("day").disabled = 1;
	document.all("hour").disabled = 1;
	document.all("minute").disabled = 1;
	} else if (radioVal == "2") {
	document.all("month").disabled = 1;
	document.all("week").disabled = 0;
	document.all("day").disabled = 1;
	document.all("hour").disabled = 1;
	document.all("minute").disabled = 1;
	} else if (radioVal == "3") {
	document.all("month").disabled = 1;
	document.all("week").disabled = 1;
	document.all("day").disabled = 0;
	document.all("hour").disabled = 1;
	document.all("minute").disabled = 1;
	} else if (radioVal == "4") {
	document.all("month").disabled = 1;
	document.all("week").disabled = 1;
	document.all("day").disabled = 1;
	document.all("hour").disabled = 0;
	document.all("minute").disabled = 1;
	} else if (radioVal == "5") {
	document.all("month").disabled = 1;
	document.all("week").disabled = 1;
	document.all("day").disabled = 1;
	document.all("hour").disabled = 1;
	document.all("minute").disabled = 0;
	} 
}
//-->
</SCRIPT>
<BODY>
<BR>
<form name="frm" method="post">
<table border=0 cellpadding=0 cellspacing=0  width="90%" align=center class="GG">
	<tr height=24>
		<td valign=middle width=30 align=center nowrap><img src="images/attention.gif" width=15 height=12></td>
		<td>注: 指定一个时间,在当前的多少时间后执行</td>
	</tr>
</table>
<br><br>
<TABLE bgcolor="#666666" width="90%" border=0 cellspacing="1" cellpadding="0" align="center" >
	<TR>
		<TD colspan="2" class=column2 valign="top">
			<TABLE CELLPADDING="0" CELLSPACING="0" WIDTH="100%" BORDER="0">
				<TR><TH colspan="2" align="center">系统维护计划时间规则配置</TH></TR>
			</TABLE>
		</TD>
	</TR>
	<TR  height="20"  bgColor=#DDDDFF class="text">
		<TD>选择</TD>
		<TD>何时以后</TD>
	</TR>
	<TR BGCOLOR=#ffffff height="20">
		<TD>
		<input type='radio' name='radioChk' value='1' checked onclick="radioClick(1)">
	  	</TD>
		<TD align=left class=column> 几月以后:<INPUT name="month" type="text" value=""></TD>
    </TR>
	<TR BGCOLOR=#ffffff >
		<TD>
		<input type='radio' name='radioChk' value='2' onclick="radioClick(2)">
	  	</TD>
      	<TD  align=left class=column> 几周以后:<INPUT name="week" type="text" value="" disabled> </TD>
    </TR>
	<TR BGCOLOR=#ffffff >
		<TD>
		<input type='radio' name='radioChk' value='3' onclick="radioClick(3)">
	  	</TD>
		<TD  align=left class=column> 几天以后:<INPUT name="day" type="text" value="" disabled> </TD>
    </TR>
	<TR  bgcolor=#ffffff>
		<TD>
		<input type='radio' name='radioChk' value='4' onclick="radioClick(4)">
	  	</TD>
		<TD align=left class=column> 几小时后:<INPUT name="hour" type="text" value="" disabled> </TD>
	</TR>
	<TR bgcolor=#ffffff >
		<TD>
		<input type='radio' name='radioChk' value='5' onclick="radioClick(5)">
	  	</TD>
		<TD align=left class=column> 几分钟后:<INPUT name="minute" type="text" value="" disabled> </TD>
    <TR >
      <TD class=foot colspan=4 height="25">
        <div align="right">
          <input name="but1" type=button class=btn onClick="returnTime('true');" value=" 确 定 ">&nbsp;&nbsp;&nbsp;&nbsp;
		  <input name="but1" type=button class=btn onClick="returnTime('false');"value=" 关 闭  " >
        </div>
	  </TD>
    </TR>
  </table>
</form>
<%@ include file="../foot.jsp"%>