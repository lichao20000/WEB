<%@ include file="../timelater.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>
<%
request.setCharacterEncoding("GBK");
String querytype=request.getParameter("querytype");
String device_id=request.getParameter("device_id");

int stat_type=0;
if(querytype==null)
	stat_type=1;
else
	stat_type=Integer.parseInt(querytype);	
String script="showpage("+stat_type+")";

%>
<%@ include file="../head.jsp"%>
<%@ include file="../toolbar.jsp"%>
<SCRIPT LANGUAGE="JavaScript" id="idParentFun"></SCRIPT>
<SCRIPT LANGUAGE="JavaScript">
<!--
var vType=1;
function showpage(uniformid)
{

	var page="webtopo_report_searchForm.jsp?querytype="+uniformid+"&dev_id="+ <%=device_id%>;
	
	maxspeed.style.display = "";
	//maxspeed.innerHTML = "正在载入数据......";
	//alert(document.all("childFrm").tagName);
	document.all("childFrm").src=page;
}



//-->
</SCRIPT>
<SCRIPT LANGUAGE="JavaScript">
<!--
var flag = false;
 function formatDate(strValue,strHMS)
  {
	var str1=strValue.substring(strValue.indexOf("-")+1,strValue.lastIndexOf("-"));
		str1=(str1.length==1)?"0"+str1:str1;
	var str2=strValue.substring(strValue.lastIndexOf("-")+1);
		str2=(str2.length==1)?"0"+str2:str2;
	strValue=strValue.substring(0,strValue.indexOf("-")+1)+str1+"-"+str2;
	if(strHMS.length>0){
		var str3=strHMS.substring(strHMS.indexOf(":")+1,strHMS.lastIndexOf(":"));
		str3=(str3.length==1)?"0"+str3:str3;
		var str4=strHMS.substring(strHMS.lastIndexOf(":")+1);
		str4=(str4.length==1)?"0"+str4:str4;
		var str5=strHMS.substring(0,strHMS.indexOf(":"));
		str5=(str5.length==1)?"0"+str5:str5;
		strValue=strValue+" "+str5+":"+str3+":"+str4;
	}
	return strValue;
  }

 function getYearMonth(strValue)
 {
	var strYM=formatDate(strValue,"");
	strYM=strYM.substring(0,strYM.lastIndexOf("-"));
	strYM=strYM.substring(0,strYM.indexOf("-"))+"_"+parseInt(strYM.substring(strYM.indexOf("-")+1),10);
	return strYM;
 
 }

function IsDate1(strValue){
	var v = strValue;
	var tmpStr1,tmpStr2,tmpStr3;
	var pos = v.indexOf("-");
	if(pos == -1){
		//alert("日期格式不符合，正确格式是：年-月-日");
		return false;
	}
	tmpStr1 = v.substring(0,pos);
	if(tmpStr1.length !=4 || parseInt(tmpStr1)<1){
		//alert("日期格式不符合，正确格式是：年-月-日");
		return false;
	}
	v = v.substring(pos+1,v.length);
	pos = v.indexOf("-");
	if(pos == -1){
		//alert("日期格式不符合，正确格式是：年-月-日");
		return false;
	}
	tmpStr2 = v.substring(0,pos);
	if(tmpStr2.length >2 || parseInt(tmpStr2) > 31 || parseInt(tmpStr2)<1){
		//alert("日期格式不符合，正确格式是：年-月-日");
		return false;
	}
	tmpStr3=v.substring(pos+1);
	if(tmpStr3.length >2 || parseInt(tmpStr3) > 31 || parseInt(tmpStr3)<1){
		//alert("日期格式不符合，正确格式是：年-月-日");
		return false;
	}
	return true;
}


//时间转换成long型
function DateParse(strValue)
{
	strValue=strValue.substring(5)+"-"+strValue.substring(0,4);	
	return Date.parse(strValue);
}

function checkForm(){
 var type=0;
 //alert("sdfsdf");
 for(var i=0;i<document.frm.SearchType.length;i++){
     if(document.frm.SearchType[i].checked){
            type=document.frm.SearchType[i].value;
            break;
      }
  }

  //alert(type);
	if(type==1 || type==2 || type==3 || type==4){
	  document.frm.action="webtopo_report_search.jsp";
	  //alert(document.frm.action)
	}else if(type==5){
	  document.frm.action="sys_report_days.jsp";
	
	}


   if (type==5)
   {
	var queryTime="";
	var queryMonth="";
	// 日期
	var objectS_time = document.all.frm.s_day.value;
	var objectE_time = document.all.frm.e_day.value;
	
	if(frm.dayType.selectedIndex==0)
	{
		if(!IsDate1(objectS_time)){
			alert("请输入正确的开始日期！");
			return false;
		}
		if(!IsDate1(objectE_time)){
			alert("请输入正确的结束日期！");
			return false;
		}

		if(formatDate(objectS_time,"")>formatDate(objectE_time,"")){
			alert("开始时间不能大于结束时间！");
			return false;
		}
		
		var strValue=objectS_time.substring(5)+"-"+objectS_time.substring(0,4);	
		var s_date=new Date(Date.parse(strValue));
		strValue=objectE_time.substring(5)+"-"+objectE_time.substring(0,4);	
		var e_date=new Date(Date.parse(strValue));

		var s_year=s_date.getYear();
		var e_year=e_date.getYear();

		var s_month=s_date.getMonth()+1;
		var e_month=e_date.getMonth()+1;

		if((parseInt(e_year,10)-parseInt(s_year,10))>1)
		{
			alert("查询的时间间隔只能在两个月内!");
			return false;
		}
		else if((parseInt(e_year,10)-parseInt(s_year,10))==1 && (parseInt(s_month,10)!=12 || parseInt(e_month,10)!=1))
		{
			alert("查询的时间间隔只能在两个月内!");
			return false;
		}
		else if((parseInt(e_year,10)-parseInt(s_year,10))==0 && (parseInt(e_month,10)-parseInt(s_month,10)>2))
		{
			alert("查询的时间间隔只能在两个月内!");
			return false;
		}
		var l_s=DateParse(objectS_time);
		var l_e=DateParse(objectE_time);		
		queryTime=l_s/1000+"|"+l_e/1000;
		queryMonth=s_year+"_"+s_month+"|"+e_year+"_"+e_month;	
	}
	else
	{
		if(frm.allday.value=="")
		{
			alert("请选择要查询的日期");
			return false;
		}		
		var dayArray=frm.allday.value.split(",");
		var allM="";
		var num=0;
		var str1="";
		var str2="";

		var month1="";
		var month2="";
		for(var i=0;i<dayArray.length;i++)
		{
			if(allM.indexOf(getYearMonth(dayArray[i]))<0)
			{	
				if(num>1)
				{
					alert("您选择的日期只能在不同的两个月内，请重选!");
					return false;
				}
				if(num==0)
				{
					month1=getYearMonth(dayArray[i]);
					str1=DateParse(dayArray[i])/1000;
				}
				else if(num==1)
				{					
					month2=getYearMonth(dayArray[i]);
					str2=DateParse(dayArray[i])/1000;
				}
				allM =allM+getYearMonth(dayArray[i]);
				num++;
			}
			else if(allM.indexOf(getYearMonth(dayArray[i]))==0)
			{				
					str1 =str1+","+DateParse(dayArray[i])/1000;
			}
			else if(allM.indexOf(getYearMonth(dayArray[i]))>0)
			{				
					str2 =str2+","+DateParse(dayArray[i])/1000;				
			}
		}
		if(str2!="")
		{
			queryTime=str1+"|"+str2;
			queryMonth=month1+"|"+month2;
		}
		else
		{
			queryTime=str1;
			queryMonth=month1;
		}
	}	
	frm.queryTime.value=queryTime;
	frm.queryMonth.value=queryMonth;
   }
   
    if (type==1 || type==2 || type==3 || type==4)
    {
		if(!IsNull(document.frm.day.value,"日期")){
			
			document.frm.day.focus();
			document.frm.day.select();
			return false;
		}
		else{
			if(type==1){			
				document.frm.hidday.value = getCaleToSec();
			}else if(type==2){				
				var s = getCaleToSec();
				var tmp = new Date(s*1000);
				var w = tmp.getDay();
				var v = s - w*24*3600;
				
				document.frm.hidday.value = v;
			}else if(type==3){
				document.frm.hidday.value = getCaleToSec();
			}else if(type==4){
				document.frm.hidday.value = getCaleToSec();
			}else{
				document.frm.hidday.value = getCaleToSec();		
			}
			initCusKind();
			idLayerView1.style.width = document.body.clientWidth*0.95;
			idLayerView1.style.display = "";
			idLayerView1.innerHTML = "正在载入数据......";
			return true;
		}
    }

	
}

function viewGraphic()
{

	var page="";
	var device_id=<%=device_id%>;
	
	var type=0;

	 for(var i=0;i<document.frm.SearchType.length;i++){
		 if(document.frm.SearchType[i].checked){
				type=document.frm.SearchType[i].value;
				break;
		  }
	  }

	   if (type==1 || type==2 || type==3 || type==4)
		{
			if(!IsNull(document.frm.day.value,"日期")){
				
				document.frm.day.focus();
				document.frm.day.select();
				return false;
			}
			else{
				if(type==1){			
					document.frm.hidday.value = getCaleToSec();
				}else if(type==2){				
					var s = getCaleToSec();
					var tmp = new Date(s*1000);
					var w = tmp.getDay();
					var v = s - w*24*3600;				
					document.frm.hidday.value = v;
				}else if(type==3){
					document.frm.hidday.value = getCaleToSec();
				}else if(type==4){
					document.frm.hidday.value = getCaleToSec();
				}else{
					document.frm.hidday.value = getCaleToSec();		
				}
				
				
			}
		}


		
		var objs=document.all("port");
		var ports="";
		var j=0;
		for(var i=0;i<objs.length;i++)
		{
			if(objs[i].checked==true)
			{
				if(j==0)
				{
					ports = objs[i].value;
					
				}
				else
				{
					ports +=","+objs[i].value;
				}
				j++;
			
			}
		
		}

		var t_objs=document.all("kind_radio");

		var radio_kind="";
		j=0;
		for(var i=0;i<t_objs.length;i++)
		{
			if(t_objs[i].checked==true)
			{
				if(j==0)
				{
					radio_kind = t_objs[i].value;
					
				}
				else
				{
					radio_kind +=","+t_objs[i].value;
				}
				j++;
			
			}
		
		}
		
		
		var hidday=document.frm.hidday.value;

		page="webtopo_dayshow.jsp?hidday="+hidday+"&dev_id="+device_id+"&radio_kind="+radio_kind+"&ports="+ports+"&type="+type;

		//alert(page);

	window.open(page,"","");
}


function getSelectedValues(elmID)
{
	
	
	t_obj=document.all(elmID);
		
	var values="";
	var j=0;
	//alert(t_obj.length);

	for(var i=0;i<t_obj.length;i++)
	{
		if(t_obj[i].checked==true)
		{
			if(j==0)
			{
				values = t_obj[i].value;
				
			}
			else
			{
				values +=","+t_obj[i].value;
			}
			j++;
		
		}
	
	}
	
	//alert(values);
	return values;
}


function IsSelect(elmID){
	t_obj = document.all(elmID);
	if(!t_obj) return false;
	for(var i=0; i<t_obj.length; i++){
		if(t_obj[i].checked)
			return true;
	}
	return false;
}
function selectAll(elmID){
	t_obj = document.all(elmID);
	if(!t_obj) return;
	obj = event.srcElement;

	if(obj.checked){
		for(var i=0; i<t_obj.length; i++){
			t_obj[i].checked = true;
		}
	}
	else{
		for(var i=0; i<t_obj.length; i++){
			t_obj[i].checked = false;
		}
	}
}




function showport(){

 var type=0;
 
 for(var i=0;i<document.frm.SearchType.length;i++){
     if(document.frm.SearchType[i].checked){
            type=document.frm.SearchType[i].value;
            break;
      }
  }

var page="webtopo_getDevice_Port.jsp?dev_id=" + <%=device_id%>;
	//alert(page);
document.all("childFrm1").src= page;
}

function initCusKind(){
	t_obj = document.all("port");
	h_obj = document.all("if_real_speed");
	y_obj = document.all("ifnamedefined");
	if(!t_obj) return false;
	for(var i=0; i<t_obj.length; i++){
		if(t_obj[i].checked){
			h_obj[i].checked = true;
			y_obj[i].checked = true;
		}
	}
}
//日判断 yyyy-mm-dd
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
		if(v.length>0)
			d = parseInt(v);
		if(type == "start")
			dt = new Date(m+"/"+d+"/"+y);
		else
			dt = new Date(m+"/"+d+"/"+y);

		var s  = dt.getTime();
		return s/1000;
	}
	else
		return 0;
}
//月判断 yyyy-mm
function MonthToDes(v,type){
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
		else{
			m = v;
			d = "01";
			v = "";
		}
		if(v.length>0)
			d = parseInt(v);

		if(type == "start")
			dt = new Date(m+"/"+d+"/"+y);
		else
			dt = new Date(m+"/"+d+"/"+y);

		var s  = dt.getTime();
		return s/1000;
	}
	else
		return 0;
}
//年判断 yyyy
function YearToDes(y,type){
	if(type == "start")
		dt = new Date("01/01/"+y);
	else
		dt = new Date("01/01/"+y);

	var s  = dt.getTime();
	return s/1000;
}
function searchType(){
 var type=0;
 for(var i=0;i<document.frm.SearchType.length;i++){
     if(document.frm.SearchType[i].checked){
            type=document.frm.SearchType[i].value;
            break;
      }
   }

     if(type==1){//日
		document.all("changeStart").innerHTML =  "<input type=\"text\" name=\"day\" class=bk>" 
                    + "<input type=\"button\" value=\"▼\" class=btn onClick=\"showCalendar(\'day\')\" name=\"button\">" 
                    + "<input type=\"hidden\" name=\"hidday\"></span>";
		document.all("title").innerHTML="〖设备端口流量日报表〗";
		document.all("strdata").innerHTML="日:";
		//document.frm.day.value=getDateStr(1);
        r1.style.display="none";
	    r2.style.display="none";
	    r3.style.display="none";
		r4.style.display="";
	 }else if(type==2){//周
		document.all("changeStart").innerHTML =  "<input type=\"text\" name=\"day\" class=bk>" 
                    + "<input type=\"button\" value=\"▼\" class=btn onClick=\"showCalendar(\'day\')\" name=\"button\">" 
                    + "<input type=\"hidden\" name=\"hidday\"></span>";
		document.all("title").innerHTML="〖设备端口流量周报表〗";
		document.all("strdata").innerHTML="周:";
		//document.frm.day.value=getDateStr(1);
        r1.style.display="none";
	    r2.style.display="none";
	    r3.style.display="none";
		r4.style.display="";
	 }else if(type==3){//月
		document.all("changeStart").innerHTML =  "<input type=\"text\" name=\"day\" class=bk>" 
                    + "<input type=\"button\" value=\"▼\" class=btn onClick=\"showCalendar(\'month\')\" name=\"button\">" 
                    + "<input type=\"hidden\" name=\"hidday\"></span>";
        document.all("title").innerHTML="〖设备端口流量月报表〗";
		document.all("strdata").innerHTML="月:";
		//document.frm.day.value=getMonthStr(1);
        r1.style.display="none";
	    r2.style.display="none";
	    r3.style.display="none";
		r4.style.display="";
	 }else if(type==4){//年
		document.all("changeStart").innerHTML =  "<input type=\"text\" name=\"day\" class=bk>" 
                    + "<input type=\"button\" value=\"▼\" class=btn onClick=\"showCalendar(\'year\')\" name=\"button\">" 
                    + "<input type=\"hidden\" name=\"hidday\"></span>";
		document.all("title").innerHTML="〖设备端口流量年报表〗";
		document.all("strdata").innerHTML="年:";
		//document.frm.day.value=getYearStr();
        r1.style.display="none";
	    r2.style.display="none";
	    r3.style.display="none";
	 }else if(type==5){//阶段
        document.all("title").innerHTML="〖设备端口流量阶段报表〗";
        r1.style.display="";
	    r2.style.display="";   
		r4.style.display="none";
	 }
}

function typeChange()
{
	if(frm.dayType.selectedIndex==0)
	{
		document.all("r2").style.display="";		
		document.all("r3").style.display="none";
	}
	else
	{
		document.all("r2").style.display="none";		
		document.all("r3").style.display="";		
	}

}
//初始化日期为当天时间 
function initTime(){
	var vDate = new Date();
	lms = vDate.getTime();
	vDate = new Date(lms-3600*24*1000);
	var y  = vDate.getYear();
	var m  = vDate.getMonth()+1;
	var d  = vDate.getDate()-1;
	var Sday = vDate.getDate();
	document.all("day") = y+"-"+m+"-"+d;	
	document.all("week") = y+"-"+m+"-"+d;
	document.all("month") = y+"-"+m;
	document.all("year") = y;
	document.all("s_day")= y+"-"+m+"-"+d;
	document.all("e_day")= y+"-"+m+"-"+Sday;
}
//用于不同接口查询
var iPos = 0;
function addip(v){
	var sText = Trim(document.frm.dev_id.value)+"/"+v;
	var oSelect = document.all("ipport");
	if(DoSearch(oSelect,sText)){
		var oOption = document.createElement("OPTION");
		oSelect.options.add(oOption);
		oOption.innerText = sText;
		oOption.Value = sText;
		oSelect.selectedIndex = iPos;
		iPos++;
	}
}

function delip(){
	var oSelect = document.all("ipport");
	var oPos = oSelect.selectedIndex;
	if(oPos!=-1){
		oSelect.remove(oPos);
		iPos--;
		oSelect.selectedIndex = 0;
	}
}

function DoSearch(obj,text){
	var bz=true;
	for(var i=0;i<obj.options.length;i++){
		if(obj.options[i].text==text){
			bz=false;
		}
	}
	if(bz){return true;}
	else{return false;}
}

function addipport(elmID){
	t_obj = document.all(elmID);
	if(!t_obj) return;

	for(var i=0; i<t_obj.length; i++){
		if(t_obj[i].checked){
			addip(t_obj[i].value);
		}
	}
}

function AutoMultiple(elmID){
	t_obj = document.all(elmID);
	for(var i=0; i<t_obj.length; i++){
		t_obj[i].selected = true;
	}
}
//-->
</SCRIPT>

<TABLE border=0 cellspacing=0 cellpadding=0 width="100%">
  <TR>
    <TD HEIGHT=21>&nbsp;</TD>
  </TR>
  <TR>
    <TD bgcolor="#FFFFFF"> 
      <table width="95%" border="0" align="center" cellpadding="0" cellspacing="0" valign="middle">
        <tr> 
          <td width="159" height="25" class="button_onblue" id="td1"
 onClick="location.href='javascript:showpage(1);'">设备端口流量报表</td>
          <td align="left">&nbsp;</td>
        </tr>
        <tr> 
          <td height="3" colspan="4" align="center" class="blue_tag_line"></td>
        </tr>
      </table>
	  </td>
      </TR>
  <TR>
    <TD><div id="maxspeed"></div></TD>
 </TR>
 

	<TD HEIGHT=21>&nbsp;<IFRAME name="childFrm" SRC="" STYLE="display:"></IFRAME></TD>
	<TD HEIGHT=21>&nbsp;<IFRAME name="childFrm1" SRC="" STYLE="display:"></IFRAME></TD>
</TR>
</TABLE>
<script language="javascript">
<!--
<%= script%>
-->
</script>
<%@ include file="../foot.jsp"%>
<SCRIPT LANGUAGE="JavaScript">
<!--
//document.frm.day.value=getDateStr(2);
//document.frm.end.value=getDateStr(1);
//日初始化
function getDateStr(number)
{
	var date = new Date();
	var seconds = date.getTime()-24*number*60*60*1000;
	date = new Date(seconds);
	var year,month,day;
	year = date.getYear();
	if((date.getMonth()+1)>9)
		month = date.getMonth()+1;
	else
		month = "0"+(date.getMonth()+1);
	if(date.getDate()>9)
		day = date.getDate();
	else
		day = "0"+date.getDate();

	return year+"-"+month+"-"+day;
}
//月初始化
function getMonthStr(number)
{
	var date = new Date();
	var seconds = date.getTime()-24*number*60*60*1000;
	date = new Date(seconds);
	var year,month,day;
	year = date.getYear();
	if((date.getMonth()+1)>9)
		month = date.getMonth()+1;
	else
		month = "0"+(date.getMonth()+1);
	if(date.getDate()>9)
		day = date.getDate();
	else
		day = "0"+date.getDate();

	return year+"-"+month;
}
function getYearStr(){
	var date = new Date();
	var seconds = date.getTime()-24*60*60*1000;
	date = new Date(seconds);
	var year;
	year = date.getYear();
}
	



//-->
</SCRIPT>