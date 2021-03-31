<%@ include file="../timelater.jsp"%>
<%@ page contentType="text/html;charset=gb2312"%>
<!--取得上一个页面提交的参数-->
<%@ include file="../head.jsp"%> 
<SCRIPT LANGUAGE="JavaScript" src="../Js/LayWriter.js"></SCRIPT>
<SCRIPT LANGUAGE="JavaScript">
<!--
var str=0;
function SelectMode(type1,type2)
{
    var sHtml="";
	var i=0;
	switch(type1)
	{
		case 1:
			switch(type2)
			{
				case 1: layerWrite('divmonth',null,sHtml);
						layerWrite('divmonth1',null,sHtml);
				break;
				case 2:
					sHtml="	<SELECT NAME='startmonth' class=bk onChange='month_check()'> <option value='1' selected>1     </option>";
					for(i=2;i<=12;i++) 	sHtml +="<option value='"+i+"'>"+i+"</option>";
					sHtml +=" </SELECT>     到     <SELECT name='endmonth' class=bk >";
	                for(i=1;i<=11;i++)	sHtml +="<option value='"+i+"'>"+i+"</option>";
					sHtml +="<option value='12'  selected>12     </option> </SELECT>";	

					layerWrite('divmonth1',null,sHtml);
				break;
				case 3:
					sHtml="<SELECT NAME='multimonth' class=bk multiple>";
					sHtml +="<option value='1' selected>1     </option>";
					for(i=2;i<=12;i++)
					sHtml +="<option value='"+i+"'>"+i+"     </option>";
					sHtml +="</SELECT>";
					layerWrite('divmonth',null,sHtml);
				break;
			}
		break;
		case 2:
			switch(type2)
			{
				case 1:
					layerWrite('divday1',null,sHtml);
					layerWrite('divday',null,sHtml);
					break;
				case 2:
					sHtml="<SELECT NAME='startday' class=bk onChange='day_check()'> <option value='1' selected>1</option>";
					for(i=2;i<=31;i++) 
					sHtml +="<option value='"+i+"'>"+i+"</option> ";
					sHtml +="</SELECT>     到     <SELECT name='endday' class=bk >";
					for(i=1;i<=29;i++)	sHtml +="<option value='"+i+"'>"+i+"</option>";
					sHtml +="<option value='30'  selected>30</option>";
					sHtml +="<option value='31'  >31</option> </SELECT>";
					layerWrite('divday1',null,sHtml);
					break;
				case 3:
					sHtml="<SELECT NAME='multiday' class=bk multiple>";
					sHtml +="<option value='1' selected>1     </option>";
					for(i=2;i<=31;i++)
					sHtml +="<option value='"+i+"'>"+i+"     </option>";
					sHtml +="</SELECT>";
					layerWrite('divday',null,sHtml);
					break;
			}
		break;
		case 3:
			switch(type2)
			{
				case 1:
					layerWrite('divweek1',null,sHtml);
					layerWrite('divweek',null,sHtml);
					break;
				case 2:
				    sHtml="<SELECT NAME='startweek' class=bk onChange='week_check()'><option value='1' selected>1</option>";
					for(i=2;i<=6;i++) 
					sHtml +="<option value='"+i+"'>"+i+"</option>";
					sHtml +=" <option value='7' >7</option> </SELECT>     到     <SELECT name='endweek' class=bk >";
					for(i=1;i<=5;i++)	sHtml +="<option value='"+i+"'>"+i+"</option>";
					sHtml +="<option value='6'>6</option> <option value='7' selected>7</option> </SELECT>";
					layerWrite('divweek1',null,sHtml);
					break;
				case 3:
					var i=0;
					sHtml="<SELECT NAME='multiweek' class=bk multiple>";
					sHtml +="<option value='1' selected>1</option>";
					for(i=2;i<=6;i++)
					sHtml +="<option value='"+i+"'>"+i+"</option>";
					sHtml +="<option value='0' selected>0</option>";
					sHtml +="</SELECT>";
					layerWrite('divweek',null,sHtml);
					break;
			}
		break;
		case 4:
			switch(type2)
			{
				case 1:
					layerWrite('divhour1',null,sHtml);
					layerWrite('divhour',null,sHtml);
					break;
				case 2:
					sHtml="<SELECT NAME='starthour' class=bk onChange='hour_check()'> <option value='0' selected>0     </option>";
					for(i=1;i<=23;i++) 
					sHtml +="<option value='"+i+"'>"+i+"</option>";
					sHtml +=" </SELECT>     到     <SELECT name='endhour' class=bk >";
					for(i=0;i<23;i++)	sHtml +="<option value='"+i+"'>"+i+"     </option>";
					sHtml +="<option value='23'  selected>23</option> </SELECT>";
					layerWrite('divhour1',null,sHtml);
				break;
				case 3:
					sHtml="<SELECT NAME='multihour' class=bk multiple>";
					sHtml +="<option value='0' selected>0</option>";
					for(i=1;i<=23;i++)
					sHtml +="<option value='"+i+"'>"+i+"</option>";
					sHtml +="</SELECT>";
					layerWrite('divhour',null,sHtml);
				break;
			}
		break;
		case 5:
			switch(type2)
			{
				case 1:
					layerWrite('divmin',null,sHtml);
					layerWrite('divmin1',null,sHtml);
					break;
				case 2:
					sHtml="<SELECT NAME='startmin' class=bk onChange='min_check()'> <option value='1' selected>1     </option>";
					for(i=2;i<=59;i++) 
					sHtml +="<option value='"+i+"'>"+i+"</option>";
					sHtml +=" </SELECT>     到     <SELECT name='endmin' class=bk >";
					for(i=1;i<59;i++)	sHtml +="<option value='"+i+"'>"+i+"     </option>";
					sHtml +="<option value='59'  selected>59</option> </SELECT>";
					layerWrite('divmin1',null,sHtml);
				break;
				case 3:
					sHtml="<SELECT NAME='multimin' class=bk multiple>";
					sHtml +="<option value='1' selected>1     </option>";
					for(i=2;i<=59;i++)
					sHtml +="<option value='"+i+"'>"+i+"</option>";
					sHtml +="</SELECT>";
					layerWrite('divmin',null,sHtml);
				break;
			}
		break;
	}
}
function getselect(obj1)
{
	for(i=0;i<obj1.length;i++)
	{
		if(obj1[i].checked)
		{
			return obj1[i].value;
		}
	}
	return "";
}
function getmutiselect(obj2)
{
	var str="";
	var flag=false;
	for(i=0;i<obj2.length;i++)
	{
		if(obj2.options[i].selected)
		{
			if(flag)
			{
			  str=obj2.options[i].value;
			  flag=true;
			}
			if(i!=obj2.length-1)
				str +=obj2.options[i].value+",";
			else
				str +=obj2.options[i].value;
		}
	}
	return str;
}
function returnTime(para)
{
	if(para=="false") 
	{
		window.returnValue="";
		window.close();
	}
	var content="*:",selectmode="";
	var myfrm=document.frm;
	selectmode=getselect(myfrm.month);
	if(selectmode=="*")		content +="*";
	if(selectmode=="-")		content +=myfrm.startmonth.value+"-"+myfrm.endmonth.value;
	if(selectmode==",")     content +=getmutiselect(myfrm.multimonth);
	content +=":";
	selectmode=getselect(myfrm.week);
	if(selectmode=="*")		content +="*";
	if(selectmode=="-")		content +=myfrm.startweek.value+"-"+myfrm.endweek.value;
	if(selectmode==",")  	content +=getmutiselect(myfrm.multiweek);
	content +=":";
	selectmode=getselect(myfrm.day);
	if(selectmode=="*")		content +="*";
	if(selectmode=="-")		content +=myfrm.startday.value+"-"+myfrm.endday.value;
	if(selectmode==",")		content +=getmutiselect(myfrm.multiday);
	content +=":";
	selectmode=getselect(myfrm.hour);
	if(selectmode=="*")		content +="*";
	if(selectmode=="-")		content +=myfrm.starthour.value+"-"+myfrm.endhour.value;
	if(selectmode==",")		content +=getmutiselect(myfrm.multihour);
	content +=":";
	selectmode=getselect(myfrm.minn);
	if(selectmode=="*")		content +="*";
	if(selectmode=="-")		content +=myfrm.startmin.value+"-"+myfrm.endmin.value;
	if(selectmode==",")
	if(selectmode==",")		content +=getmutiselect(myfrm.multimin);
	if(confirm(content+"，确认编辑好了时间吗？")==true){
		window.returnValue="年:月:周:日:时:分 =" + content;
		window.close();
	}
	else {
		window.returnValue=null;
		return false;
	}
}

//修改了选择区间让用户产生歧义问题
//下面为shenkejian 2005-11-3 添加
function month_check(){
	var j=0;
	var m=0;
    str=document.frm.startmonth.value;
       for(j=0;j<12;j++) {   
		   document.frm.endmonth.remove(0);
	   }
	   for (m=str;m<=12;m++)
	   {   
		  var month=document.createElement("option");
		  month.text=m;
          document.frm.endmonth.add(month);
	   }
}

function day_check(){
	var j=0;
	var m=0;
    str=document.frm.startday.value;
       for(j=0;j<31;j++) {   
		   document.frm.endday.remove(0);
	   }
	   for (m=str;m<=31;m++)
	   {   
		  var month=document.createElement("option");
		  month.text=m;
          document.frm.endday.add(month);
	   }
}

function week_check(){
	var j=0;
	var m=0;
    str=document.frm.startweek.value;
       for(j=0;j<=6;j++) {   
		   document.frm.endweek.remove(0);
	   }
	   for (m=str;m<=7;m++)
	   {   
		  var month=document.createElement("option");
		  month.text=m;
          document.frm.endweek.add(month);
	   }
}

function hour_check(){
	var j=0;
	var m=0;
    str=document.frm.starthour.value;
       for(j=0;j<=23;j++) {   
		   document.frm.endhour.remove(0);
	   }
	   for (m=str;m<=23;m++)
	   {   
		  var month=document.createElement("option");
		  month.text=m;
          document.frm.endhour.add(month);
	   }
}
function min_check(){
	var j=0;
	var m=0;
    str=document.frm.startmin.value;
       for(j=0;j<59;j++) {   
		   document.frm.endmin.remove(0);
	   }
	   for (m=str;m<=59;m++)
	   {   
		  var month=document.createElement("option");
		  month.text=m;
          document.frm.endmin.add(month);
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
		<td>注: 没有其他的值的参数为不可配置的参数</td>
	</tr>
</table>
<br><br>
<TABLE bgcolor="#666666" width="90%" border=0 cellspacing="1" cellpadding="0" align="center" >
	<TR>
		<TD colspan="4" class=column2 valign="top">
			<TABLE CELLPADDING="0" CELLSPACING="0" WIDTH="100%" BORDER="0">
				<TR><TH colspan="2" align="center">模板命令参数配置</TH></TR>
			</TABLE>
		</TD>
	</TR>
	<TR  height="20"  bgColor=#DDDDFF class="text">
		<TD ></TD>
		<TD>所有</TD>
		<TD>区间</TD>
		<TD>高级选择</TD>
	</TR>
	<TR BGCOLOR=#ffffff height="20">
		<TD align=right class=column height="18" width=50  colspan=""> 请选择月 </TD>
      	<TD ><INPUT name="month" type="radio" value="*" checked onClick="SelectMode(1,1)">
	  	<TD  colspan="" ><INPUT name="month" type="radio" value="-" onClick="SelectMode(1,2)">
        <DIV id="divmonth1">
				<SPAN CLASS="white">
				</SPAN>
			 </DIV>
	  	<TD  colspan=><INPUT name="month" type="radio" value="," onClick="SelectMode(1,3)"> 
			<DIV id="divmonth">
				<SPAN CLASS="white">
				</SPAN>
			 </DIV>
    </TR>
	<TR BGCOLOR=#ffffff >
      	<TD  align=right class=column height="18"  width=50 colspan="" > 请选择周 </TD>
		<TD  ><INPUT name="week" type="radio" value="*" checked onClick="SelectMode(3,1)">
	  	<TD  colspan=""><INPUT name="week" type="radio" value="-" onClick="SelectMode(3,2)">
			<DIV align="left" id="divweek1">
				<SPAN CLASS="white">
				</SPAN>
			 </DIV>
	  	<TD  colspan=><INPUT name="week" type="radio" value="," onClick="SelectMode(3,3)"> 
			<DIV align="left" id="divweek">
				<SPAN CLASS="white">	
				</SPAN>
			 </DIV>
    </TR>
	<TR BGCOLOR=#ffffff >
		<TD  align=right class=column  height="18"   width=50 colspan=""> 请选择天 </TD>
		<TD  ><INPUT name="day" type="radio" value="*" checked onClick="SelectMode(2,1)">
	  	<TD  colspan=""><INPUT name="day" type="radio" value="-" onClick="SelectMode(2,2)">
			<DIV align="left" id="divday1">
				<SPAN CLASS="white">
				</SPAN>
			 </DIV>
		<TD  colspan=><INPUT name="day" type="radio" value="," onClick="SelectMode(2,3)"> 
			<DIV align="left" id="divday">
				<SPAN CLASS="white">
				</SPAN>
			 </DIV>
    </TR>
	<TR  bgcolor=#ffffff>
		 <TD align=right class=column height="18"  width=50 colspan="" > 请选择时 </TD>
		 <TD  ><INPUT name="hour" type="radio" value="*" checked onClick="SelectMode(4,1)">
		 <TD colspan=""><INPUT name="hour" type="radio" value="-" onClick="SelectMode(4,2)">
			<DIV align="left" id="divhour1">
				<SPAN CLASS="white">
				</SPAN>
			 </DIV>
		 <TD  colspan><INPUT name="hour" type="radio" value="," onClick="SelectMode(4,3)"> 
			<DIV align="left" id="divhour">
				<SPAN CLASS="white">
				</SPAN>
			 </DIV>
	</TR>
	<TR bgcolor=#ffffff >
		<TD align=right class=column height="18"  width=50 colspan="" > 请选择分 </TD>
		<TD  ><INPUT name="minn" type="radio" value="*" checked onClick="SelectMode(5,1)">
	  	<TD colspan=""><INPUT name="minn" type="radio" value="-" onClick="SelectMode(5,2)">
			<DIV align="left" id="divmin1">
				<SPAN CLASS="white">
				</SPAN>
			 </DIV>
		<TD  colspan=><INPUT name="minn" type="radio" value="," onClick="SelectMode(5,3)"> 
			<DIV align="left" id="divmin">
				<SPAN CLASS="white">
				</SPAN>
			 </DIV>
			 </TR>	
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