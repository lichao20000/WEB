<%@ include file="../timelater.jsp"%>
<%@ page contentType="text/html;charset=gb2312"%>
<!--ȡ����һ��ҳ���ύ�Ĳ���-->
<%@ include file="../head.jsp"%>
<TITLE>�ۺ����� - ѡ��ʱ�� tttt</TITLE>
<SCRIPT LANGUAGE="JavaScript" src="../Js/LayWriter.js"></SCRIPT>
<SCRIPT LANGUAGE="JavaScript">
<!--
function getDetailTime(newdate) {
	var ny = newdate.getYear(); //��ȡ��ǰ���(2λ)
	var nm = newdate.getMonth()+1; //��ȡ��ǰ�·�(0-11,0����1��) 
	var nd = newdate.getDate(); //��ȡ��ǰ��(1-31) 
	var nw = newdate.getDay(); //��ȡ��ǰ����X(0-6,0����������) 
	var nh = newdate.getHours(); //��ȡ��ǰСʱ��(0-23) 
	var nmi = newdate.getMinutes(); //��ȡ��ǰ������(0-59)
	
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

	var y = myDate.getYear(); //��ȡ��ǰ���(2λ)
	var m = myDate.getMonth()+1; //��ȡ��ǰ�·�(0-11,0����1��) 
	var d = myDate.getDate(); //��ȡ��ǰ��(1-31) 
	var w = myDate.getDay(); //��ȡ��ǰ����X(0-6,0����������) 
	var h = myDate.getHours(); //��ȡ��ǰСʱ��(0-23) 
	var mi = myDate.getMinutes(); //��ȡ��ǰ������(0-59)
	var content="0:";
	var myfrm=document.frm;
	//��������·�
	if (!myfrm.month.disabled) {
		var hMonthLa = myfrm.month.value;
		if (hMonthLa==null || hMonthLa == "") {
			alert("�����뼸���º�!");
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
	//���������
	if (!myfrm.week.disabled) {
		var hWeekLa = myfrm.week.value;
		if (hWeekLa==null || hWeekLa == "") {
			alert("�����뼸���ܺ�!");
			return false;
		}
		var newtimems=newdate.getTime()+(hWeekLa*7*24*60*60*1000);
		newdate.setTime(newtimems);
		content = getDetailTime(newdate);
	}
	//�����������
	if (!myfrm.day.disabled) {
		var hDayLa = myfrm.day.value;
		if (hDayLa==null || hDayLa == "") {
			alert("�����뼸�����!");
			return false;
		}
		var newtimems=newdate.getTime()+(hDayLa*24*60*60*1000);
		newdate.setTime(newtimems);
		content = getDetailTime(newdate);
	}
	//�������Сʱ
	if (!myfrm.hour.disabled) {
		var hHourLa = myfrm.hour.value;
		if (hHourLa==null || hHourLa == "") {
			alert("�����뼸Сʱ��!");
			return false;
		}
		var newtimems=newdate.getTime()+(hHourLa*60*60*1000);
		newdate.setTime(newtimems);
		content = getDetailTime(newdate);
	}
	//����������
	if (!myfrm.minute.disabled) {
		var hMinuteLa = myfrm.minute.value;
		if (hMinuteLa==null || hMinuteLa == "") {
			alert("�����뼸���Ӻ�!");
			return false;
		}
		var newtimems=newdate.getTime()+(hMinuteLa*60*1000);
		newdate.setTime(newtimems);
		content = getDetailTime(newdate);
	}

	content = "��:��:ʱ:��:��:��:�� =" + content;

	if(confirm(content+"��ȷ�ϱ༭����ʱ����")==true){
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
		<td>ע: ָ��һ��ʱ��,�ڵ�ǰ�Ķ���ʱ���ִ��</td>
	</tr>
</table>
<br><br>
<TABLE bgcolor="#666666" width="90%" border=0 cellspacing="1" cellpadding="0" align="center" >
	<TR>
		<TD colspan="2" class=column2 valign="top">
			<TABLE CELLPADDING="0" CELLSPACING="0" WIDTH="100%" BORDER="0">
				<TR><TH colspan="2" align="center">ϵͳά���ƻ�ʱ���������</TH></TR>
			</TABLE>
		</TD>
	</TR>
	<TR  height="20"  bgColor=#DDDDFF class="text">
		<TD>ѡ��</TD>
		<TD>��ʱ�Ժ�</TD>
	</TR>
	<TR BGCOLOR=#ffffff height="20">
		<TD>
		<input type='radio' name='radioChk' value='1' checked onclick="radioClick(1)">
	  	</TD>
		<TD align=left class=column> �����Ժ�:<INPUT name="month" type="text" value=""></TD>
    </TR>
	<TR BGCOLOR=#ffffff >
		<TD>
		<input type='radio' name='radioChk' value='2' onclick="radioClick(2)">
	  	</TD>
      	<TD  align=left class=column> �����Ժ�:<INPUT name="week" type="text" value="" disabled> </TD>
    </TR>
	<TR BGCOLOR=#ffffff >
		<TD>
		<input type='radio' name='radioChk' value='3' onclick="radioClick(3)">
	  	</TD>
		<TD  align=left class=column> �����Ժ�:<INPUT name="day" type="text" value="" disabled> </TD>
    </TR>
	<TR  bgcolor=#ffffff>
		<TD>
		<input type='radio' name='radioChk' value='4' onclick="radioClick(4)">
	  	</TD>
		<TD align=left class=column> ��Сʱ��:<INPUT name="hour" type="text" value="" disabled> </TD>
	</TR>
	<TR bgcolor=#ffffff >
		<TD>
		<input type='radio' name='radioChk' value='5' onclick="radioClick(5)">
	  	</TD>
		<TD align=left class=column> �����Ӻ�:<INPUT name="minute" type="text" value="" disabled> </TD>
    <TR >
      <TD class=foot colspan=4 height="25">
        <div align="right">
          <input name="but1" type=button class=btn onClick="returnTime('true');" value=" ȷ �� ">&nbsp;&nbsp;&nbsp;&nbsp;
		  <input name="but1" type=button class=btn onClick="returnTime('false');"value=" �� ��  " >
        </div>
	  </TD>
    </TR>
  </table>
</form>
<%@ include file="../foot.jsp"%>