var intDelay=10; //设置菜单显示速度，越大越慢
var intInterval=20; //每次更改的透明度
var sheet_id;//source,sheet_id,worksheet_status,product_id,servtype,receive_time;

var subMenuHTML = "<DIV id='idMnu1' class=menuitems url='javascript:click_obj(0)'>????????</DIV><DIV id='idMnu2' class=menuitems url='javascript:click_obj(1)'>????????</DIV><DIV class=menuhr><hr style='width:100%'></DIV><DIV id='idMnu3' class=menuitems url=javascript:click_obj(2)>????????????</DIV>";

var obj = null;

function showmenuie5(){
	var curObj = event.srcElement;
	
	while(curObj.tagName != "TR"){
		curObj = curObj.parentElement;
	}
	if(obj != null){	
		obj.runtimeStyle.backgroundColor = "";
		obj.runtimeStyle.color = ""
	}
	
	obj = curObj;
	obj.runtimeStyle.backgroundColor = "#0000A0";
	obj.runtimeStyle.color = "white"
	
	TR_parames = obj.parames;
	arrParame = TR_parames.split(",");
	sheet_id = arrParame[0];
	receive_time = arrParame[1];
	gather_id = arrParame[2];
//	alert(sheet_id + ";" + receive_time);
//	source = arrParame[1];
//	sheet_id = arrParame[2];
//	worksheet_status = arrParame[3];
//	product_id = arrParame[5];
//	servtype = arrParame[4];
//	receive_time = arrParame[6];
//	alert(arrParame.length)
	v = parseInt(obj.value);
	if(v == 0){
		idMnu1.disabled = true;
		idMnu2.disabled = true;
		idMnu3.disabled = true;
//		idMnu4.disabled = false;
	}
	else if (v == 2)
	{
		idMnu1.disabled = true;
		idMnu2.disabled = true;
		idMnu3.disabled = false;
	}
	else{
		if(v == 1){
			idMnu1.disabled = true;
			idMnu2.disabled = false;
			idMnu3.disabled = false;
//		idMnu4.disabled = true;	
		}else{
			idMnu1.disabled = false;
			idMnu2.disabled = true;
			idMnu3.disabled = false;			
		}
	}
//	else{
//		idMnu1.disabled = true;
//		idMnu2.disabled = true;
//		idMnu3.disabled = false;
//		idMnu4.disabled = true;		
//	}
//
	var rightedge=document.body.clientWidth-event.clientX
	var bottomedge=document.body.clientHeight-event.clientY
	if (rightedge<ie5menu.offsetWidth)
		ie5menu.style.left=document.body.scrollLeft+event.clientX-ie5menu.offsetWidth
	else
		ie5menu.style.left=document.body.scrollLeft+event.clientX
	if (bottomedge<ie5menu.offsetHeight)
		ie5menu.style.top=document.body.scrollTop+event.clientY-ie5menu.offsetHeight
	else
		ie5menu.style.top=document.body.scrollTop+event.clientY
	ie5menu.style.visibility="visible"
	//ie5menu.style.visibility=""
	ie5menu.filters.alpha.opacity=100;
	//GradientShow()
	return false
}
function hidemenuie5(){
	ie5menu.style.visibility="hidden"
	//GradientClose()
}
function highlightie5(){
	if (event.srcElement.className=="menuitems"){
		//event.srcElement.style.backgroundColor="highlight"
		//event.srcElement.style.color="white"
		event.srcElement.className = "menuitems_over"
	}
}
function lowlightie5(){
	if (event.srcElement.className=="menuitems_over"){
		//event.srcElement.style.backgroundColor=""
		//event.srcElement.style.color="#000000"
		event.srcElement.className = "menuitems"
	}
} 
function jumptoie5(){
	if (event.srcElement.className=="menuitems_over"){
		if (event.srcElement.url != ''){
			if (event.srcElement.getAttribute("target")!=null)
				window.open(event.srcElement.url,event.srcElement.getAttribute("target"))
			else
				window.location=event.srcElement.url
		}
	}
}

function GradientShow() //实现淡入的函数 
{ 
    ie5menu.filters.alpha.opacity+=intInterval 
    if (ie5menu.filters.alpha.opacity<100) setTimeout("GradientShow()",intDelay)
} 
    
function GradientClose() //实现淡出的函数 
    { 
    ie5menu.filters.alpha.opacity-=intInterval 
    if (ie5menu.filters.alpha.opacity>0) { 
     setTimeout("GradientClose()",intDelay) 
     } 
    else { 
     ie5menu.style.visibility="hidden"
     } 
} 

function ChangeBG() //改变菜单项的背景颜色，这里的两种颜色值可以改为你需要的 
{ 
    oEl=event.srcElement 
    if (oEl.style.background!="navy") { 
        oEl.style.background="navy" 
    } 
    else { 
        oEl.style.background="#cccccc" 
    } 
} 

function click_obj(id){
	//alert(document.all("idMnu"+(id+1)).disabled)
	if(document.all("idMnu"+(id+1)).disabled) {
		ie5menu.style.visibility="visible";
		return;
	}
	
    switch(id){
        case 0:    
            reWork();
            break;
        case 1:
            nextWork();
            break;
        case 2:
            goDetail();
            break;
        case 3:
            createWork();
            break;
    }
}


function createWork(){
	page = "AddDeviceForm_New.jsp?product_id="+ product_id +"&sheet_id="+ sheet_id +"&servtype="+ servtype;
	
	//alert(page);
	var win = window.showModalDialog(page,window,"status:no;resizable:yes;edge:raised;scroll:no;help:no;center:yes;dialogHeight:400px;dialogWidth:780px");
	
	if(win == 1){
		refreshPage();
	}
}

function reWork(){
	document.all("childFrm2").src="sheet_reExec.jsp?sheet_id=" + sheet_id + "&receive_time="+ receive_time +"&gather_id=" + gather_id;
}

function nextWork(){

	document.all("childFrm2").src="sheet_next_Exec.jsp?sheet_id=" + sheet_id + "&gather_id=" + gather_id;

}

function goDetail(){
	page = "sheet_detail.jsp?sheet_id="+ sheet_id + "&receive_time="+ receive_time;

	window.open(page,new Date().getTime(),"left=20,top=20,width=500,height=420,resizable=no,scrollbars=no");
}

function doClick(o){
	if(obj != null){	
		obj.runtimeStyle.backgroundColor = "";
		obj.runtimeStyle.color = ""
	}
	
	obj = o;
	obj.runtimeStyle.backgroundColor = "#0000A0";
	obj.runtimeStyle.color = "white"
}

function doDbClick(o){
	parames = o.parames;
	arrParame = parames.split(",");
	sheet_id = arrParame[0];
	receive_time = arrParame[1];
	gather_id = arrParame[2];
	page = "sheet_detail.jsp?sheet_id="+ sheet_id + "&receive_time="+ receive_time;

	window.open(page,new Date().getTime(),"left=20,top=20,width=500,height=420,resizable=no,scrollbars=no");
}

function doDb(o){
	parames = o.parames;
	arrParame = parames.split(",");
	worksheet_id = arrParame[0];
	sheet_id = arrParame[2];
	product_id = arrParame[5];
	receive_time = arrParame[6];
	page = "Err_adslWork_List_detail.jsp?source=search&worksheet_id="+ worksheet_id +"&sheet_id="+ sheet_id +"&product_id="+ product_id +"&receive_time="+ receive_time;

	window.open(page,new Date().getTime(),"left=20,top=20,width=600,height=500,resizable=yes,scrollbars=yes");
}

function sendMsgToSocket(flag){
	if(eval(flag)){
		window.alert("工单数据传输系统后台成功！");
	}else{
		window.alert("工单数据传输系统后台失败！");
	}
}

function reStart(flag){
	if(eval(flag)){
		window.alert("工单重新激活成功！");
	}else{
		window.alert("工单重新激活失败！");
	}
}

function refreshPage(){
	page = document.all("childFrm").src;
	pos = page.indexOf("&filter=");
	if(pos == -1)
		pos = page.indexOf("&polltime=");
	var s1="",s2=""
	if(pos != -1) s2=page.substring(pos,page.length);
	pos = page.indexOf("&refresh=");
	s1 = page.substring(0,pos);
	page = s1+"&refresh="+Math.random()+s2;
	//alert(page);
	showMsgDlg();
	document.all("childFrm").src = page;
}

function updateBgSetting(){
	document.all("childFrm2").src="Err_adslWork_Exec.jsp?work=update&t=" + new Date().getTime();
}