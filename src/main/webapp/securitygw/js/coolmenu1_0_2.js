//------------------------------------------------------------
// Copyright (c) 2003-2004 LeadinSoft. All rights reserved.
// Version 1.0.2
// Ahthor dolphin
//------------------------------------------------------------

//单个菜单的结构
function menu(mnuName,mnuText,mnuAlt,mnuIcon,mnuTarget,mnuKey,mnuType,mnuCmd){
	this.name   = mnuName;
	this.text	= mnuText;
	this.alt	= mnuAlt;
	this.icon	= mnuIcon;
	this.target = mnuTarget;
	this.key	= mnuKey;
	this.type	= mnuType;
	this.cmd	= mnuCmd;
}

//菜单组结构
function CMenu(mnuName){
	this.name	= mnuName;
	this.menu	= new Array();
}

function ActiveMenu(mnuID,mnuType){
	this.id   = mnuID;
	this.type = mnuType;
}

//打印主菜单(Main-CM)
function drawMainMenu(CM, width, height,objId){
	var HTMLStr, posLeft, posTop, i;
	var mnuBarObj = document.getElementById(objId);

	HTMLStr = "<div id='" + CM.name + "' class='CM_MainMenuBar' style='width:"+ width + ";height:"+ height + "'></div>";
	mnuBarObj.insertAdjacentHTML("beforeEnd",HTMLStr);
	
	var obj  = document.getElementById(CM.name);
	posLeft  = obj.offsetLeft;
	posTop	 = obj.offsetTop;
	var pobj = obj.offsetParent;
	while(pobj.tagName.toUpperCase() != "BODY"){
		posLeft += pobj.offsetLeft;
		posTop  += pobj.offsetTop;
		pobj	=  pobj.offsetParent;
	}
	
	posLeft += 2;
	posTop  += 3;
	for(i=0; i<CM.menu.length; i++){
		HTMLStr = "<div id='" + CM.menu[i].name + "' nowrap class='CM_MainMenu' style='left:" + posLeft + ";top:" + posTop + ";height:21px' onmouseover='return CMM_Over(this,\"" + CM.menu[i].cmd + "\")' onmouseout='return CMM_Out(this)' " +
        "onclick='return CMM_Click(this,\"" + CM.menu[i].cmd + "\")' title='" + CM.menu[i].alt + "'>" + CM.menu[i].text;
		if(CM.menu[i].key != ""){
			HTMLStr += "(<u>" + CM.menu[i].key + "</u>)";
		}
		HTMLStr += "</div>";
		obj.insertAdjacentHTML("beforeEnd",HTMLStr);
		//alert("sleep")
		posLeft += document.getElementById(CM.menu[i].name).offsetWidth + 1;
	}
}

function drawSubMenu(CM){
	var HTMLStr, i, maxLn = 0;
	HTMLStr = "<div id='"+ CM.name +"' style='left:0px;top:0px;width:150px;overflow:hidden' class='CM_SubMenuBar'></div>";
	document.body.insertAdjacentHTML("beforeEnd",HTMLStr);
	var obj = document.getElementById(CM.name);

	for(i=0; i<CM.menu.length; i++){
		tmp = getTextLength(CM.menu[i].text);
		if(maxLn < tmp) maxLn = tmp;

		HTMLStr  = "<div id='"+ CM.menu[i].name +"' class='CM_SubMenu'"+
				   " title='"+ CM.menu[i].alt +"'"+
				   " onmouseover=\"CMS_Over(this,"+ CM.menu[i].type +",'"+ CM.menu[i].cmd +"')\""+
				   " onmouseout=\"CMS_Out(this,"+ CM.menu[i].type +",'"+ CM.menu[i].cmd +"')\""+
				   " onclick=\"CMS_Click(this,"+ CM.menu[i].type +",'"+ CM.menu[i].cmd +"')\">";
		HTMLStr += "<table border=0 cellspacing=0 cellpadding=0 width='100%' align='center'>";
		HTMLStr += "<tr nowrap height=20><td nowrap width=1></td>";
		HTMLStr += formatImage(CM.menu[i].icon,CM.menu[i].name);
		HTMLStr += "<td id='"+ CM.menu[i].name +"_text' class='CM_Text'>"+ CM.menu[i].text;
		if(CM.menu[i].key != ""){
			HTMLStr += "(<u>"+ CM.menu[i].key +"</u>)";
		}
		HTMLStr += "</td>";
		HTMLStr += "<td id='"+ CM.menu[i].name +"_sub' nowrap align=center width=16 class='CM_Sub'>";
		if(CM.menu[i].type == 0){
			HTMLStr += "<span class='SubFlag'>}</span>";
		}
		else{
			HTMLStr += "&nbsp;"
		}
		HTMLStr += "</td>"+
				   "<td nowrap width=1 class='CM_Space'></td></tr></table></div>";

		obj.insertAdjacentHTML("beforeEnd",HTMLStr);
	}
	document.getElementById(CM.name).style.width = maxLn*10+40;
}

function formatImage(mnuIcon, mnuName){
	var result = "<td id='"+ mnuName +"_ico' nowrap align=center width=20 class='CM_Icon'>";
	if(mnuIcon != ""){
		result += "<IMG SRC='"+ mnuIcon +"' BORDER='0'>";
	}
	else{
		result += "&nbsp;";
	}
	result += "</td>";

	return result;
}

function getTextLength(text){
    var ln = 0;
    for(var i=0; i<text.length; i++){
        if(text.charCodeAt(i) > 256){
          ln++;
        }
        ln++;
    }
    return ln;
}

function showMenu(mnuObj,mnuSubID){
	var l = mnuObj.offsetLeft - 1;
	var t = mnuObj.offsetTop + mnuObj.offsetHeight; 
	document.getElementById(mnuSubID).style.left = l; //设置子菜单位置
	document.getElementById(mnuSubID).style.top  = t;  //设置子菜单的位置
	document.getElementById("overline").style.left = l + 2;
	document.getElementById("overline").style.top  = t;
	document.getElementById("overline").style.width = mnuObj.offsetWidth - 2;
	document.getElementById("overline").style.visibility = "visible";

	document.getElementById(mnuSubID).style.visibility = "visible";   //显示子菜单
	g_actMenu[g_actMenu.length] = new ActiveMenu(mnuSubID,1); 
	
	if(g_shadowStr != null){
		mnuObj.style.filter = g_shadowStr;
		document.getElementById(mnuSubID).style.filter = g_shadowStr;
	}
}

function showSubMenu(mnuObj,mnuSubID){
	var l = mnuObj.parentElement.offsetLeft + mnuObj.offsetLeft + mnuObj.offsetWidth + 2;
	var t = mnuObj.parentElement.offsetTop + mnuObj.offsetTop + 1; 

	var mnuSubObj = document.getElementById(mnuSubID);
	if(l+mnuSubObj.offsetWidth>g_scrWidth)
		mnuSubObj.style.left = mnuObj.parentElement.offsetLeft - mnuSubObj.offsetWidth - 2;
	else
		mnuSubObj.style.left = l;
	if(t+mnuSubObj.offsetHeight>g_scrHeight)
		mnuSubObj.style.top = g_scrHeight - mnuSubObj.offsetHeight;
	else
		mnuSubObj.style.top = t;

	mnuSubObj.style.visibility = "visible";
	g_actMenu[g_actMenu.length] = new ActiveMenu(mnuSubID,1); 
}

function CMM_Over(obj, cmd){
	isShow = true;
	if(!g_blnMMClick){
		obj.className = "CM_MainMenuOver";
		g_actMenu[g_actMenu.length] = new ActiveMenu(obj.id,0); 
	}
	else{
		if(g_actMenu[0].id == obj.id) return;
		document.getElementById(g_actMenu[0].id).className = "CM_MainMenu";
		for(var i=1;i<g_actMenu.length;i++){
			if (g_actMenu[i].type == 0) {
				document.getElementById(g_actMenu[i].id + "_ico").className  = "CM_Icon";
				document.getElementById(g_actMenu[i].id + "_text").className = "CM_Text";
				document.getElementById(g_actMenu[i].id + "_sub").className  = "CM_Sub";
			}
			else{
				document.getElementById(g_actMenu[i].id).style.visibility = "hidden";
			}
		}
		g_actMenu.length = 0;
		obj.className    = "CM_MainMenuClick";
		g_actMenu[g_actMenu.length] = new ActiveMenu(obj.id,0);
		showMenu(obj,cmd);
	}
}

function CMM_Click(obj,cmd){
	if(!g_blnMMClick){
		obj.className = "CM_MainMenuClick";
		showMenu(obj,cmd);
		g_blnMMClick = true;
	}
	else{
		document_click();
	}
	g_blnSMClick = true;

	if(!g_blnMMClick) CMM_Over(obj, cmd);
}

function CMS_Over(obj, type, cmd){
	for(var i=1;i<g_actMenu.length;i++){
		if(g_actMenu[i].id == obj.parentElement.id){
			for(var j=i+1;j<g_actMenu.length;j++){
				if(g_actMenu[j].type == 0){
					document.getElementById(g_actMenu[j].id + "_ico").className  = "CM_Icon";
					document.getElementById(g_actMenu[j].id + "_text").className = "CM_Text";
					document.getElementById(g_actMenu[j].id + "_sub").className  = "CM_Sub";		
				}
				else{
					document.getElementById(g_actMenu[j].id).style.visibility = "hidden";
				}
			}
			g_actMenu.length = i+1;
			break;
		}
	}
	
	g_actMenu[g_actMenu.length] = new ActiveMenu(obj.id,0);
	//showstatus();
	document.getElementById(obj.id + "_ico").className  = "CM_IconOver";
	document.getElementById(obj.id + "_text").className = "CM_TextOver";
	document.getElementById(obj.id + "_sub").className  = "CM_SubOver";

	if(type == 0){
		showSubMenu(obj,cmd);
	}
	//showstatus();
}

function CMS_Out(obj, type, cmd){
	if(type != 0){
		document.getElementById(obj.id + "_ico").className  = "CM_Icon";
		document.getElementById(obj.id + "_text").className = "CM_Text";
		document.getElementById(obj.id + "_sub").className  = "CM_Sub";	
 
		if(g_actMenu.length > 0) g_actMenu.length --;
	} 
	//showstatus();
}

function CMM_Out(obj){
	isShow = false;
	if(!g_blnMMClick){
		obj.className = "CM_MainMenu";
		g_actMenu.length --;
	}
}

function CMS_Click(obj, type, cmd){
	if(type == 1){
		document_click();
		document.getElementById("overline").style.visibility = "hidden";
		//lblMsg.innerText = cmd;
		if(cmd != ""){
		iframe.location.href = cmd;
		}
		 
	}
	else{
		g_blnSMClick = true;
	}
}

function document_click(){
	if(g_blnSMClick){
		g_blnSMClick = false;
		return;
	}

	if(g_actMenu.length >0){
		for(var i=1; i<g_actMenu.length; i++){
			if(g_actMenu[i].type == 0){
				document.getElementById(g_actMenu[i].id + "_ico").className  = "CM_Icon";
				document.getElementById(g_actMenu[i].id + "_text").className = "CM_Text";
				document.getElementById(g_actMenu[i].id + "_sub").className  = "CM_Sub";	
			}
			else{
				document.getElementById(g_actMenu[i].id).style.visibility = "hidden";
			}
		}
		document.getElementById(g_actMenu[0].id).className = "CM_MainMenu"; 
		document.getElementById("overline").style.visibility = "hidden";
	}
	g_actMenu.length = 0;
	g_blnMMClick = false;
}

function window_onload() {
	//初始化窗口尺寸
	g_scrHeight = window.document.body.offsetHeight;
	g_scrWidth  = window.document.body.offsetWidth;
}

function showstatus() {   //测试阶段用的函数，显示当前菜单ID数组内的内容
	lblMsg.innerText = "";
	for(var i=0;i<g_actMenu.length;i++) {
		lblMsg.innerText += g_actMenu[i].id + " > ";
	}
}


var g_blnMMClick   = false; 
var g_actMenu = new Array();
var g_scrWidth, g_scrHeight;
var g_blnSMClick = false;

window.attachEvent("onload",window_onload);
document.onclick = document_click;
var g_shadowStr = null;
//var g_shadowStr = "progid:DXImageTransform.Microsoft.Shadow(color='#888888', Direction=135, Strength=4)";