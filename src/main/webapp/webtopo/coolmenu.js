//------------------------------------------------------------
// Copyright (c) 2003-2004 LeadinSoft. All rights reserved.
// Version 1.0.3
// Ahthor dolphin
//------------------------------------------------------------

//�����˵��Ľṹ
function menu(mnuName,mnuText,mnuAlt,mnuIcon,mnuTarget,mnuKey,mnuType,mnuCmd){
	this.name   = mnuName;		//�˵���ʶ
	this.text	= mnuText;				//�˵�����
	this.alt	= mnuAlt;				//�˵�����
	this.icon	= mnuIcon;				//�˵�ͼ��
	this.target = mnuTarget;		//�˵����ӷ���
	this.key	= mnuKey;				//�˵���ݼ�
	this.type	= mnuType;			//�˵������Ƿ����Ӳ˵�
	this.cmd	= mnuCmd;				//�˵�����
}

//�˵���ṹ
function CMenu(mnuName){
	this.name	= mnuName;		
	this.menu	= new Array();		//�˵��������
}

//��˵�
function ActiveMenu(mnuID,mnuType){
	this.id   = mnuID;					//�˵���ʶID
	this.type = mnuType;			//�Ƿ������˵�
}

//��ӡ���˵�(Main-CM)
function drawMainMenu(CM, width, height){
	var HTMLStr, posLeft, posTop, i;

	HTMLStr = "<div id='" + CM.name + "' class='CM_MainMenuBar' style='width:"+ width + ";height:"+ height + "'>";
	document.write(HTMLStr);
	
	var obj  = document.all(CM.name);
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
        "onclick='return CMM_Click(this,\"" + CM.menu[i].cmd + "\")'>" + CM.menu[i].text;
		if(CM.menu[i].key != ""){
			HTMLStr += "(<u>" + CM.menu[i].key + "</u>)";
		}
		HTMLStr += "</div>";
		document.write(HTMLStr);
		posLeft += document.all.item(CM.menu[i].name).offsetWidth + 1;
	}

	HTMLStr = "</div>";
	document.write(HTMLStr);	
}

function drawSubMenu(CM){
	var HTMLStr, i, maxLn = 0;

	//add by smf ,����Ѿ����˶���
	
	if(document.all(CM.name))
	{
		document.body.removeChild(document.all(CM.name));
	}
	
	HTMLStr = "<div id='"+ CM.name +"' style='left:0px;top:0px;width:150px;overflow:hidden' class='CM_SubMenuBar'>";
	//document.body.insertAdjacentHTML("beforeEnd",HTMLStr);
	for(i=0; i<CM.menu.length; i++){
		if(CM.menu[i].text == "-"){
			HTMLStr += drawSubHR(CM.menu[i].name);
			continue;
		}
		tmp = getTextLength(CM.menu[i].text);
		if(maxLn < tmp) maxLn = tmp;
		
		HTMLStr += "<div id='"+ CM.menu[i].name +"' class='CM_SubMenu'"+
				   " onmouseover=\"CMS_Over(this,"+ CM.menu[i].type +",'"+ CM.menu[i].cmd +"')\""+
				   " onmouseout=\"CMS_Out(this,"+ CM.menu[i].type +",'"+ CM.menu[i].cmd +"')\""+
				   " onclick=\"CMS_Click(this,"+ CM.menu[i].type +",'"+ CM.menu[i].cmd +"','"+CM.menu[i].target+"')\">";
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
		HTMLStr += "</td><td nowrap width=1 class='CM_Space'></td></tr></table></div>";

		//document.body.insertAdjacentHTML("beforeEnd",HTMLStr);
	}

	HTMLStr += "</div>";
	//alert(HTMLStr);
	document.body.insertAdjacentHTML("beforeEnd",HTMLStr);

	//idRMenuHTML.innerHTML += HTMLStr;
	document.all(CM.name).style.width = maxLn*10+30;
}

function drawSubHR(sname){
	var HTMLStr;
		
	HTMLStr  = "<div id='"+ sname +"' class='CM_SubMenu'>";
	HTMLStr += "<table border=0 cellspacing=1 cellpadding=0 width='100%' align='center'>";
	HTMLStr += "<tr nowrap height=1><td nowrap width=1></td><td bgcolor=buttonface width=23 nowrap></td>";
	HTMLStr += "<td align=right colspan=3 bgcolor='appworkspace'>"	;
	HTMLStr += "</td></tr></table></div>";

	return HTMLStr;
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
	document.all.item(mnuSubID).style.left = l; //�����Ӳ˵�λ��
	document.all.item(mnuSubID).style.top  = t;  //�����Ӳ˵���λ��
	document.all.item("overline").style.left = l + 2;
	document.all.item("overline").style.top  = t;
	document.all.item("overline").style.width = mnuObj.offsetWidth - 2;
	document.all.item("overline").style.visibility = "visible";

	document.all.item(mnuSubID).style.visibility = "visible";   //��ʾ�Ӳ˵�
	g_actMenu[g_actMenu.length] = new ActiveMenu(mnuSubID,1); 

	//mnuObj.style.filter = g_shadowStr;
	//document.all.item(mnuSubID).style.filter = g_shadowStr;
}

function showRMenu(mnuSubID){
	//alert(mnuSubID);
	var l ,t;
	x = event.x+document.body.scrollLeft;

	y = event.y+document.body.scrollTop;
	//alert(y);
	obj = document.all.item(mnuSubID);

	if((x+obj.offsetWidth)>document.body.clientWidth)
		l = x - obj.offsetWidth;
	else
		l = x;

	if((y+obj.offsetHeight)>document.body.clientHeight)
		t = y - obj.offsetHeight;
	else
		t = y;

	//alert(l);
	//alert(t);

	obj.style.left = l;
	obj.style.top = t;
	obj.style.visibility = "visible";
	g_actMenu.length =0;
	g_actMenu[0] = new ActiveMenu(mnuSubID,1); 
}

function showSubMenu(mnuObj,mnuSubID){
	var l = mnuObj.parentElement.offsetLeft + mnuObj.offsetLeft + mnuObj.offsetWidth + 2;
	var t = mnuObj.parentElement.offsetTop + mnuObj.offsetTop + 1; 

	var mnuSubObj = document.all.item(mnuSubID);
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
	if(g_blnRMShow) return false;
	if(!g_blnMMClick){
		obj.className = "CM_MainMenuOver";
		g_actMenu[g_actMenu.length] = new ActiveMenu(obj.id,0); 
	}
	else{
		if(g_actMenu[0].id == obj.id) return;
		document.all.item(g_actMenu[0].id).className = "CM_MainMenu";
		for(var i=1;i<g_actMenu.length;i++){
			if (g_actMenu[i].type == 0) {
				document.all.item(g_actMenu[i].id + "_ico").className  = "CM_Icon";
				document.all.item(g_actMenu[i].id + "_text").className = "CM_Text";
				document.all.item(g_actMenu[i].id + "_sub").className  = "CM_Sub";
			}
			else{
				document.all.item(g_actMenu[i].id).style.visibility = "hidden";
			}
		}
		g_actMenu.length = 0;
		obj.className    = "CM_MainMenuClick";
		g_actMenu[g_actMenu.length] = new ActiveMenu(obj.id,0);
		showMenu(obj,cmd);
	}
	//showstatus();
}

function CMM_Out(obj){
	if(g_blnRMShow) return false;
	if(!g_blnMMClick){
		obj.className = "CM_MainMenu";
		g_actMenu.length --;
	}
}

function CMM_Click(obj,cmd){
	if(g_blnRMShow){
		g_blnRMShow = false;
		clearRMenu();
		obj.className = "CM_MainMenuOver";
		g_actMenu[g_actMenu.length] = new ActiveMenu(obj.id,0); 
	}

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
	var tmp=1;
	if(g_blnRMShow) tmp=0;

	for(var i=tmp;i<g_actMenu.length;i++){
		if(g_actMenu[i].id == obj.parentElement.id){
			for(var j=i+1;j<g_actMenu.length;j++){
				if(g_actMenu[j].type == 0){
					document.all.item(g_actMenu[j].id + "_ico").className  = "CM_Icon";
					document.all.item(g_actMenu[j].id + "_text").className = "CM_Text";
					document.all.item(g_actMenu[j].id + "_sub").className  = "CM_Sub";		
				}
				else{
					document.all.item(g_actMenu[j].id).style.visibility = "hidden";
				}
			}
			g_actMenu.length = i+1;
			break;
		}
	}
	
	g_actMenu[g_actMenu.length] = new ActiveMenu(obj.id,0);
	//showstatus();
	document.all.item(obj.id + "_ico").className  = "CM_IconOver";
	document.all.item(obj.id + "_text").className = "CM_TextOver";
	document.all.item(obj.id + "_sub").className  = "CM_SubOver";

	if(type == 0){
		showSubMenu(obj,cmd);
	}
	//showstatus();
}

function CMS_Out(obj, type, cmd){
	if(type != 0){
		document.all.item(obj.id + "_ico").className  = "CM_Icon";
		document.all.item(obj.id + "_text").className = "CM_Text";
		document.all.item(obj.id + "_sub").className  = "CM_Sub";	
		
		if(g_actMenu.length > 0) g_actMenu.length --;
	}
}

function CMS_Click(obj, type, cmd, target){
	if(type == 1){
		document_click();
		document.all.item("overline").style.visibility = "hidden";

		if(target=="javascript"){			
			if(cmd=="") {alert(arrObjectID);return;}
			//alert("����"+cmd+"���� ������"+type+","+arrObjectID);
			fun = cmd+"('"+type+"','"+arrObjectID+"')";
			try{eval(fun);}catch(e){}
		}
		else{
			//alert(formatCMD(cmd));
			document.all(target).src = formatCMD(cmd);
		}
	}
	else{
		g_blnSMClick = true;
	}
}

function formatCMD(s){
	//alert("formatCMD arrObjectID oid="+arrObjectID);
	if(s=="") return "";
	if(s.indexOf("?") != -1)
		return s+"&oid="+ arrObjectID+"&refresh="+(new Date()).getTime();
	else
		return s+"?oid="+ arrObjectID+"&refresh="+(new Date()).getTime();
}

function clearRMenu(){
	for(var i=0; i<g_actMenu.length; i++){
		if(g_actMenu[i].type == 0){
			document.all.item(g_actMenu[i].id + "_ico").className  = "CM_Icon";
			document.all.item(g_actMenu[i].id + "_text").className = "CM_Text";
			document.all.item(g_actMenu[i].id + "_sub").className  = "CM_Sub";	
		}
		else{
			document.all.item(g_actMenu[i].id).style.visibility = "hidden";
		}
	}
	g_actMenu.length = 0;
}

function document_click(){
	if(g_blnRMShow){
		g_blnRMShow = false;
		clearRMenu();
		return false;
	}
	if(g_blnSMClick){
		g_blnSMClick = false;
		return false;
	}

	if(g_actMenu.length >0){
		for(var i=1; i<g_actMenu.length; i++){
			if(g_actMenu[i].type == 0){
				document.all.item(g_actMenu[i].id + "_ico").className  = "CM_Icon";
				document.all.item(g_actMenu[i].id + "_text").className = "CM_Text";
				document.all.item(g_actMenu[i].id + "_sub").className  = "CM_Sub";	
			}
			else{
				document.all.item(g_actMenu[i].id).style.visibility = "hidden";
			}
		}
		document.all.item(g_actMenu[0].id).className = "CM_MainMenu"; 
		document.all.item("overline").style.visibility = "hidden";
	}
	g_actMenu.length = 0;
	g_blnMMClick = false;
}

function document_down(){
	document_click();
	//alert(document.all("RM_Menu").innerHTML)
	showRMenu("RM_Menu");	
	g_blnRMShow = true;
}

function window_onload() {
	//��ʼ�����ڳߴ�
	g_scrHeight = window.document.body.offsetHeight;
	g_scrWidth  = window.document.body.offsetWidth;
}

function showstatus() {   //���Խ׶��õĺ�������ʾ��ǰ�˵�ID�����ڵ�����
	lblMsg.innerText = "";
	for(var i=0;i<g_actMenu.length;i++) {
		lblMsg.innerText += g_actMenu[i].id + " > ";
	}
}

//ת��������ͼ
function goNetWork(){
	window.location.href = "./main.jsp?type=1";	
}

//ת��������ͼ
function goHost(){
	window.location.href = "./main.jsp?type=2";	
}

document.write("<div id='overline' class='CM_OverLine' style='left:100px;top:300px;width:100px;overflow:hidden'><table border=0 cellpadding=0 cellspacing=0 width='100%'><tr height=1><td width='100%' style='background-color:#EFEDDE' ></td></tr><tr height=1><td width='100%' style='background-color:menu' ></td></tr></table></div>");
var g_blnMMClick   = false; 
var g_actMenu = new Array();
var g_scrWidth, g_scrHeight;
var g_blnSMClick = false;
var g_blnRMShow = false;
var arrObjectID = null;					//��ǰ�����ҽ��˵������ڶ�������arrDev��topo.js���е�����ֵ

//var g_shadowStr = "progid:DXImageTransform.Microsoft.Shadow(color='#888888', Direction=135, Strength=4)";