var YearStr,MonthStr,DayStr,NowDayStr;
var WeekStr,week,week_one;
var WeekArr = new Array("星期日", "星期一", "星期二", "星期三","星期四", "星期五", "星期六");
var dt = new Date();
var showtype;
var caleX = 0;
var caleY = 0;

YearStr  = dt.getFullYear();
MonthStr = dt.getMonth() + 1;
NowDayStr = DayStr   = dt.getDate();
week     = dt.getDay();
WeekStr  = WeekArr[week];
document.write("<DIV id=idCalendar style='position:absolute;top:100px;left:100px;width:260px;border: #000000 1px solid;background-color: #ffffff;display:none;z-index:100'></DIV>");

function initCalendar(){
	var CaleHTML="";
	CaleHTML += "<TABLE border=0 cellspacing=0 cellpadding=1 width='100%'>";
	CaleHTML += "<TR bgcolor=#33246C><TD><BUTTON onclick='prevMonth()' class=btn2><span lang=EN-US style='font-family:Arial'>&#9668;</span></BUTTON></TD>";
	CaleHTML += "<TD align=center><SPAN style='color:white'>"+ YearStr +"年"+ MonthStr +"月</SPAN></TD>"
	CaleHTML += "<TD align=right><BUTTON onclick='nextMonth()' class=btn2><span lang=EN-US style='font-family:Arial'>&#9658;</span></BUTTON></TD></TR>";
	CaleHTML += "<TR><TD colspan=3>";
	CaleHTML += "<TABLE border=0 cellspacing=0 cellpadding=0 width='100%'><TR>";
	for(var i=0; i<WeekArr.length; i++){
		CaleHTML += "<TD>" + WeekArr[i] + "</TD>";
	}
	CaleHTML += "</TR><TR><TD colspan=7><HR size=1 color=blank width='90%'></TD></TR>";
	CaleHTML += bodyCalendar();
	CaleHTML += "</TABLE>";
	CaleHTML += "</TD></TR>";
	CaleHTML += "<TR><TD colspan=3>今天是：<Font color=red>"+ (dt.getFullYear()) +"-"+ (dt.getMonth() + 1) +"-"+ dt.getDate() +"</font></TD></TR>";
	CaleHTML += "</TABLE>";
	
	return CaleHTML;
}

function bodyCalendar(){
	var ResultStr = "<TR>";
	var tempStr = "" + MonthStr + "/1/" + YearStr;
	var d = new Date(tempStr);
	week_one = d.getDay();
	for(var i=0;i<week_one;i++){
		ResultStr += "<TD></TD>";
	}
	var bYear = YearStr%4;
	var maxDay;

	switch(MonthStr){
		case 1:
		case 3:
		case 5:
		case 7:
		case 8:
		case 10:
		case 12:
			maxDay = 31;
			break;
		case 2:
			if(bYear!=0)
				maxDay = 28;
			else
				maxDay = 29;
			break;
		default:
			maxDay = 30;
			break;
	}
	var k;
	var bday;
	for(var i=0;i<maxDay;i++){

		if((i+1) == DayStr)
			bday = "border: #006432 1px solid;";
		//else if((i+1) == NowDayStr)
			//bday = "border: #ff0000 1px solid;";
		else
			bday = "border: #ffffff 1px solid;";

		BRStr = i+week_one+1;
		if(BRStr>=7 && BRStr%7==0){

			ResultStr += "<TD align=right style='cursor:hand;"+ bday +"' onmouseover='TDMove(event)' onmouseout='TDOut(event)' onclick='TDClick(event)'>"+ eval(i+1) +"</TD></TR><TR>";
			k=0;
		}
		else{
			ResultStr += "<TD align=right style='cursor:hand;"+ bday +"' onmouseover='TDMove(event)' onmouseout='TDOut(event)' onclick='TDClick(event)'>"+ eval(i+1) +"</TD>";
			k+=1;
		}
	}
	for(var i=0;i<7-k;i++){
		ResultStr += "<TD></TD>";
	}
	ResultStr += "</TR>";
	return ResultStr;
}

function prevMonth(){
	MonthStr -= 1;
	if(MonthStr == 0){
		MonthStr = 12;
		YearStr -= 1;
	}

	idCalendar.innerHTML = initCalendar();
}

function nextMonth(){
	MonthStr += 1;
	if(MonthStr > 12){
		MonthStr = 1;
		YearStr += 1;
	}
	idCalendar.innerHTML = initCalendar();
}


function TDMove(event){
	var obj = window.event ? event.srcElement:event.currentTarget;
	obj.setAttribute("border", "#005AB5 1px solid");
}

function TDOut(event){
	
	var obj = window.event ? event.srcElement:event.currentTarget;
	if(parseInt(obj.innerText) == DayStr)
		obj.setAttribute("border","#006432 1px solid");
	//else if(parseInt(obj.innerText) == NowDayStr)
		//obj.style.setAttribute("border","#ff0000 1px solid");
	else
		obj.setAttribute("border","#ffffff 1px solid");
}
var tobj;
var timeStr="";
function initYMD(v){
	if(v=="" || showtype=="addday") return;
    pos = v.indexOf(" ");
	if(pos != -1){
		timeStr = v.substring(pos+1,v.length);
		v = v.substring(0,pos);
	}

    arr = v.split("-");
    if(arr.length==1){
		if(v.length>4) {
			YearStr = 0;
			return;
		}
        YearStr = parseInt(arr[0],10);
    }
    else if(arr.length==2){
		if(v.length>7) {
			YearStr = 0;
			return;
		}
        YearStr = parseInt(arr[0],10);
        MonthStr = parseInt(arr[1],10);
        //MonthStr = parseInt(MonthStr,10);
    }
    else{
		if(v.length>10) {
			YearStr = 0;
			return;
		}
        YearStr = parseInt(arr[0],10);
        MonthStr = parseInt(arr[1],10);
        //MonthStr = parseInt(MonthStr,10);     
        DayStr = parseInt(arr[2],10);
        //DayStr = parseInt(DayStr,10);  
    }
}


function showCalendar(stype,event){
	showtype = stype;
	var  event = window.event || event;
	var  obj  = window.event ? event.srcElement:event.currentTarget;
	//modify 2004-10-21 
	var pobj = obj.parentElement;
	
	// 20200717
	if(obj.id == 'start_day'){
		tobj = pobj.childNodes[1]
	}else if(obj.id == 'end_day'){
		tobj = pobj.childNodes[5]
	}else{
		tobj = (pobj.childNodes[0].value == undefined ? pobj.childNodes[1] : pobj.childNodes[0]);
	}
	
	//tobj = document.all(obj.sourceIndex-1);
	//end	
	v = tobj.value == undefined ? "" : tobj.value;
	initYMD(v);

	if(showtype!="addday" && (isNaN(YearStr) || isNaN(MonthStr) || isNaN(DayStr) || YearStr<1900||MonthStr<1||DayStr<1)){
		alert("输入的日期格式不正确");
		tobj.value = "";
		YearStr  = dt.getFullYear();
		MonthStr = dt.getMonth() + 1;
		NowDayStr = DayStr   = dt.getDate();
	}

	var objParent = obj.offsetParent;
	var left      = obj.offsetLeft;
	var top		  = obj.offsetTop;
	while(objParent.tagName.toUpperCase() != "BODY"){
		left += objParent.offsetLeft;
		top  += objParent.offsetTop;
		objParent = objParent.offsetParent;
	}
	left += obj.offsetWidth;
	top  += obj.offsetHeight;

	idCalendar.style.top = top+1;
	idCalendar.style.left = left-260;
	caleX = left - 260;
	caleY = top + 1;
	idCalendar.innerHTML = initCalendar();
	idCalendar.style.display="";
	hiddenElement("SELECT");
	/*var left = event.clientX;
	var top  = event.clientY;
	idCalendar.style.top = top+5;
	idCalendar.style.left = left-250;
	idCalendar.innerHTML = initCalendar();
	idCalendar.style.display="";*/
}

function TDClick(event){
	var evt = window.event ? event.srcElement:event.currentTarget;
	DayStr = evt.innerText;
	//alert(showtype);
	if(showtype=="day"){
		tobj.value = YearStr + "-" + MonthStr + "-" + DayStr;
	}
	else if(showtype=="month"){
		tobj.value = YearStr + "-" + MonthStr;
	}
	else if(showtype=="all"){
		if(timeStr=="")
			tobj.value = YearStr + "-" + MonthStr + "-" + DayStr +" 00:00:00";
		else
			tobj.value = YearStr + "-" + MonthStr + "-" + DayStr +" "+timeStr;
	}
	else if(showtype=="addday")
	{
		if(tobj.value=="" || tobj.value.length==0)
		{
			tobj.value=YearStr + "-" + MonthStr + "-" + DayStr;
		}
		else
		{
			tobj.value=tobj.value+","+YearStr + "-" + MonthStr + "-" + DayStr;
		}
	}
	else{
		tobj.value = YearStr;
	}
	hideCalendar();
}

function getCaleToSec(){
	var dt;
    v = document.frm.day.value;
	initYMD(v);
	dt = new Date(MonthStr+"/"+DayStr+"/"+YearStr);
	var s  = dt.getTime();
	return s/1000;
}

function hideCalendar(){
	idCalendar.style.display = "none";
	showElement("SELECT");
}

function C_MouseUp(event){
	var event = event||window.event;
	if(idCalendar == null)
		return;
	var top  = parseInt(idCalendar.style.top);
	var left = parseInt(idCalendar.style.left);
	var wi   = idCalendar.offsetWidth;
	var he   = idCalendar.offsetHeight;
	var x    = event.clientX;
	var y    = event.clientY + document.body.scrollTop;
	var x1 = left + wi;
	var y1 = top + he;

	if(x>x1||x<left||y>y1||y<top){
		hideCalendar();
		showElement("SELECT");
	}
}

function hiddenElement(elmID){
	for (i = 0; i < document.getElementsByTagName(elmID).length; i++){
		obj = document.getElementsByTagName(elmID)[i];
		if (! obj || ! obj.offsetParent)
			continue;

		objLeft   = obj.offsetLeft;
		objTop    = obj.offsetTop;
		objParent = obj.offsetParent;
		while (objParent.tagName.toUpperCase() != "BODY")
		{
			objLeft  += objParent.offsetLeft;
			objTop   += objParent.offsetTop;
			objParent = objParent.offsetParent;
		}

		if(caleX > (objLeft + obj.offsetWidth) || objLeft > (caleX + idCalendar.offsetWidth))
			;
		else if(objTop > (caleY + idCalendar.offsetHeight))
			;
		else if(caleY > (objTop + obj.offsetHeight))
			;
		else
			obj.style.visibility = "hidden";
	}
}

function showElement(elmID){
	for (i = 0; i < document.all.tags(elmID).length; i++){
		obj = document.all.tags(elmID)[i];
		if (! obj || ! obj.offsetParent)
			continue;
		obj.style.visibility = "";
	}
}

document.onmouseup = C_MouseUp;