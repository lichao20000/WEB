<%@ include file="../timelater.jsp"%>
<%@ page contentType="text/html;charset=gb2312"%>
<%@ page import="java.text.SimpleDateFormat"%>
<!--取得上一个页面提交的参数-->
<%@ include file="../head.jsp"%>
<TITLE>综合网管 - 选择时间 tttt</TITLE>
<SCRIPT LANGUAGE="JavaScript" src="../Js/LayWriter.js"></SCRIPT>
<%
	String rule_time =session.getAttribute("rule_time")!= null?(String)session.getAttribute("rule_time"):null;
	String start=null,end=null,hidsstart=null,hidsend=null;
	SimpleDateFormat st= new SimpleDateFormat("yyyy-M-d HH:mm:ss");
	int currhour=-1;//默认时间
	if(rule_time!=null && !rule_time.endsWith("*")  && !"".equals(rule_time)){
		int startindex=rule_time.lastIndexOf(":");
		String tempstr=rule_time.substring(startindex+1,rule_time.length());

		long endtime=Long.parseLong(tempstr.substring(tempstr.lastIndexOf("-")+1,tempstr.length()));
		long starttime=Long.parseLong(tempstr.substring(0,tempstr.lastIndexOf("-")));
		currhour=(int)(endtime-starttime)/3600;
		start=st.format(new java.util.Date(starttime*1000));
		hidsstart=""+starttime;
	}

	String ruletype =request.getParameter("ruletype");

	String usual="",tempor="none";
	if(ruletype!=null&&ruletype.equals("0")){
		 usual="none";tempor="";
	}
%>
<SCRIPT LANGUAGE="JavaScript">
<!--
var str=0;
function checktime(){
  var iStart,iEnd;
	iStart = DateToDes(document.frm.start.value,"start");
	if(!IsNull(document.frm.start.value,"开始日期")){
		document.frm.start.focus();
		return false;
	}
	else{
		document.frm.hidstart.value = iStart;
		document.frm.hidend.value = iStart+document.all.lasthour.value*3600;
		return true;
	}	
}
function DateToDes(v,type){
	if(v != ""){
		pos = v.indexOf("-");
		if(pos != -1 && pos == 4){
			y = parseInt(v.substring(0,pos));
			v = v.substring(pos+1,v.length);
		}
		pos = v.indexOf("-");
		if(pos != -1){
			m = parseInt(v.substring(0,pos));
			v = v.substring(pos+1,v.length);
		}
		pos = v.indexOf(" ");
		if(pos != -1){
			d = parseInt(v.substring(0,pos));
			v = v.substring(pos+1,v.length);
		}

		dt = new Date(m+"/"+d+"/"+y+" "+v);

		var s  = dt.getTime();
		return s/1000;
	}
	else
		return 0;
}
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
					sHtml="<SELECT NAME='startmonth' class=bk onChange='month_check()'> <option value='1' selected>1     </option>";
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
	var timechk;	
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
	content +=":";
	if(document.all.timerange.style.display==""){
		timechk=checktime();
		if(!timechk){
			return false;
		}
		content+=document.all.hidstart.value+"-"+document.all.hidend.value;
		
	}else{
		content+="*";
	}
	if(confirm(content+"，确认编辑好了时间吗？")==true){
	    window.opener.document.all("ruler_time").value=content;
		window.close();
	}
	else {
		window.returnValue=null;
		return false;
	}
}

//修改了选择区间让用户产生歧义问题
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
		  month.value=m;
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
		  month.value=m;
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
		  month.value=m;
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
		  month.value=m;
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
		  month.value=m;
          document.frm.endmin.add(month);
	   }
}
function SelectTimeRange(type){
	if(type==1){
		document.all.timerange.style.display="none";
	}else{
		document.all.timerange.style.display="";
	}
	//alert(document.all.ruletype.value);
	
}
function initTime(){
	//today
	var vNow = new Date();

	var y0  = vNow.getYear();
	var m0  = vNow.getMonth()+1;
	var d0  = vNow.getDate();

	//yesterday
	lms = vNow.getTime();
	var vDate = new Date(lms-3600*24*1000);
	var y  = vDate.getYear();
	var m  = vDate.getMonth()+1;
	var d  = vDate.getDate();
	if(document.frm.start.value=="")
	document.frm.start.value = y + "-" + m + "-" + d;
	if(document.frm.end.value=="")
	document.frm.end.value = y + "-" + m + "-" + d;
}
//-->
</SCRIPT>
<BODY>
<BR>
<form name="frm" method="post">
<DIV id="timerange" style="display:<%=tempor %>">
<table border=0 cellpadding=0 cellspacing=1  width="90%" align=center class="GG" bgcolor="#666666">
	<TR #DDDDFF><TH colspan="2" align="center">临时告警前转时间规则配置</TH></TR>
		<tr ><td  bgcolor="#ffffff">
						开始日期&nbsp
                        <INPUT TYPE="text" NAME="start"value='<%=(start!=null?start:"")%>' class=bk>
                        <INPUT TYPE="button" value="▼" class=btn onclick="showCalendar('all')">
                        <INPUT TYPE="hidden" name="hidstart" value='<%=(hidsstart!=null?hidsstart:"")%>'>
                        </td>
                        <td  bgcolor="#ffffff">
						割接持续时间：
					 	<SELECT name='lasthour' class=bk >
					<% for(int i=0;i<24;i++){	%><option value='<%=i%>' <%if(currhour==i)out.print("selected"); %>><%=i%></option><% }%>
					</SELECT>  小时      
					<INPUT TYPE="hidden" name="hidend">

			 </td>
	</tr>
	      <TR >
      <TD  height="25" colspan="2"  align="right"  bgcolor="#DDDDFF">
          <input name="but1" type=button class=btn onClick="returnTime('true');" value=" 确 定 ">&nbsp;&nbsp;&nbsp;&nbsp;
		  <input name="but1" type=button class=btn onClick="returnTime('false');"value=" 关 闭  " >
	  </TD>
    </TR>
</table>
</DIV>
<br><br>
<div  id="usuanlrule" style="display:<%=usual%>">
<table border=0 cellpadding=0 cellspacing=0  width="90%" align=center class="GG">
	<tr height=24>
		<td valign=middle  align=left nowrap><img src="images/attention.gif" height=12>
		注: 在高级选择中,按"Ctrl"键可多选, 星期日为0 默认为全选</td>
	</tr>
</table>
<TABLE bgcolor="#666666" width="90%" border=0 cellspacing="1" cellpadding="0" align="center" >
	<TR>
		<TD colspan="4" class=column2 valign="top">
			<TABLE CELLPADDING="0" CELLSPACING="0" WIDTH="100%" BORDER="0">
				<TR><TH colspan="2" align="center">常用告警前转时间规则配置</TH></TR>
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
		<TD align=right class=column height="18"   colspan=""> 请选择月 </TD>
      	<TD ><INPUT name="month" type="radio" value="*" checked onClick="SelectMode(1,1)">
	  	<TD  colspan="" align =><INPUT name="month" type="radio" value="-" onClick="SelectMode(1,2)">
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
      	<TD  align=right class=column height="18"   colspan="" > 请选择周 </TD>
		<TD  ><INPUT name="week" type="radio" value="*" checked onClick="SelectMode(3,1)">
	  	<TD  colspan=""><INPUT name="week" type="radio" value="-" onClick="SelectMode(3,2)">
			<DIV align="left" id="divweek1">
				<SPAN CLASS="white">
				</SPAN>
			 </DIV>
			 </TD>
	  	<TD  colspan=><INPUT name="week" type="radio" value="," onClick="SelectMode(3,3)"> 
			<DIV align="left" id="divweek">
				<SPAN CLASS="white">	
				</SPAN>
			 </DIV>
			 </TD>
    </TR>
	<TR BGCOLOR=#ffffff >
		<TD  align=right class=column  height="18" colspan=""> 请选择天 </TD>
		<TD  ><INPUT name="day" type="radio" value="*" checked onClick="SelectMode(2,1)">
	  	<TD  colspan=""><INPUT name="day" type="radio" value="-" onClick="SelectMode(2,2)">
			<DIV align="left" id="divday1">
				<SPAN CLASS="white">
				</SPAN>
			 </DIV>
			 </TD>
		<TD  colspan=><INPUT name="day" type="radio" value="," onClick="SelectMode(2,3)"> 
			<DIV align="left" id="divday">
				<SPAN CLASS="white">
				</SPAN>
			 </DIV>
			 </TD>
    </TR>
	<TR  bgcolor=#ffffff>
		 <TD align=right class=column height="18" colspan="" > 请选择时 </TD>
		 <TD  ><INPUT name="hour" type="radio" value="*" checked onClick="SelectMode(4,1)">
		 <TD colspan=""><INPUT name="hour" type="radio" value="-" onClick="SelectMode(4,2)">
			<DIV align="left" id="divhour1">
				<SPAN CLASS="white">
				</SPAN>
			 </DIV>
			 </TD>
		 <TD  colspan><INPUT name="hour" type="radio" value="," onClick="SelectMode(4,3)"> 
			<DIV align="left" id="divhour">
				<SPAN CLASS="white">
				</SPAN>
			 </DIV>
			 </TD>
	</TR>
	<TR bgcolor=#ffffff >
		<TD align=right class=column height="18" colspan="" > 请选择分 </TD>
		<TD  ><INPUT name="minn" type="radio" value="*" checked onClick="SelectMode(5,1)">
	  	<TD colspan=""><INPUT name="minn" type="radio" value="-" onClick="SelectMode(5,2)">
			<DIV align="left" id="divmin1">
				<SPAN CLASS="white">
				</SPAN>
				 </DIV>
				</TD>
			
		<TD  colspan=><INPUT name="minn" type="radio" value="," onClick="SelectMode(5,3)"> 
			<DIV align="left" id="divmin">
				<SPAN CLASS="white">
				</SPAN>
			 </DIV>
			 </TD>
			 </TR>	
      <TR bgColor=#DDDDFF>
      <TD colspan=4 height="25" align="right" >
          <input name="but2" type=button class=btn onClick="returnTime('true');" value=" 确 定 ">&nbsp;&nbsp;&nbsp;&nbsp;
		  <input name="but2" type=button class=btn onClick="returnTime('false');"value=" 关 闭  " >
	  </TD>
    </TR>
  </table>
  </div>
</form>
<%@ include file="../foot.jsp"%>