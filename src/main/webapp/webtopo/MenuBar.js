//单个菜单的结构
function leftmenu( linkID, label, url, icon, target, submenu){
	this.linkID		= linkID;			//菜单标识
	this.label			= label;			//菜单名称
	this.url			= url;				//菜单URL
	this.icon			= icon;			//菜单图标
	this.target		= target;		//菜单连接方向
	this.submenu	= submenu;		//子菜单
}

function ActiveMenu(mnuID,mnuType){
	this.id   = mnuID;
	this.type = mnuType;
}

var MMenu = null;
var XmlDoc = null;
var strIconPath = "images/";
var strGenericIcon = "s_generic_icon_1.gif";
var activeMenu = new Array();

function loadMMenuRes(){
	try{
		XmlDoc = new ActiveXObject("Microsoft.XMLDOM");
		XmlDoc.async = false;
		XmlDoc.load(idMenuBar.xmlSrc);
		var nodes = XmlDoc.selectNodes("//MMenu/MenuNode");
		MMenu = new Array();
		for(var i=0; i<nodes.length; i++){
			el = nodes.item(i);
			MMenu[i] = new leftmenu(el.getAttribute("linkID"), 
										   el.getAttribute("label"), 
										   el.getAttribute("url"),
										   el.getAttribute("icon"),
										   el.getAttribute("target"),
										   el.getAttribute("submenu")
				);
		}
	} catch(e){
		alert(e);
	}
}

function loadSMenuRes(fname){
	if(XmlDoc == null) return;
	XmlDoc.setProperty("SelectionLanguage", "XPath");
	el = XmlDoc.selectSingleNode("//SMenu[@ID='"+ fname +"']");
	nodes = el.childNodes;
	this.item = new Array();

	for(var i=0;i<nodes.length; i++){
		el = nodes.item(i);
		this.item[i]= new leftmenu(
									el.getAttribute("linkID"),
									el.getAttribute("label"),
									el.getAttribute("url") ,
									el.getAttribute("icon"),
									el.getAttribute("target"),
									el.getAttribute("submenu")
			);
	}
}

function drawMenuBar(){
	var s,strURL;
	if(MMenu == null){
		loadMMenuRes();
	}

	for(var i=0; i<MMenu.length; i++){
		if(MMenu[i].url != null && MMenu[i].url != ""){
			strURL = '<a href="'+ MMenu[i].url +'"><font class=text_white><b>'+ MMenu[i].label +'</b></font></a>'
		}
		else{
			strURL = '<font class=text_white><b>'+ MMenu[i].label +'</b></font>';
		}
		s = '<div id="Flyout'+ MMenu[i].linkID +'" style="width:100%;cursor:default;" >'
			+'	<table width="100%" border="0" cellspacing="0" cellpadding="0">'
			+'	  <tr>'
			+'		<td width="4"><img src="images/kuang_left.gif" width="4" height="25"></td>'
			+'		<td background="images/kuang_mid.gif" class="text_white">'
			+'			<table width="100%" border="0" cellspacing="0" cellpadding="0">'
			+'			  <tr>'
			+'				<td width="30"><img src="images/'+MMenu[i].icon+'"></td>'
			+'				<td>'+ strURL +'</td>'
			+'				<td width="12" onclick="MM_Click(this,\''+ MMenu[i].submenu +'\')"><img src="images/button_s.gif" width="12" height="12" border="0"></td>'
			+'			  </tr>'
			+'			</table>'
			+'		</td>'
			+'		<td width="4"><img src="images/kuang_right.gif" width="4" height="25"></td>'
			+'	  </tr>'
			+'	</table>'
		if(MMenu[i].submenu != null && MMenu[i].submenu != ""){
			s +='	<table id="ID'+ MMenu[i].submenu +'" width="100%" border="0" cellspacing="0" cellpadding="0" >'
				+'	  <tr>'
				+'		<td width="4" background="images/kuang_left2.gif"></td>'
				+'		<td><table width="100%" border="0" cellspacing="0" cellpadding="0">'
				+'		  <tr><td colspan="3" height="2"></td></tr>';

			SMenu = new loadSMenuRes(MMenu[i].submenu);
			submenu = SMenu.item;
			for(var j=0; j<submenu.length; j++){
				s += '<tr id="ID'+ submenu[j].linkID +'" onmouseover="SM_Over(this)" onmouseout="SM_Out(this)" onclick="SM_Click(\''+ submenu[j].url +'\', \'' + submenu[j].target + '\')">';
				s +=  formatIcon(submenu[j].icon,submenu[j].linkID);
				s += '<td id="ID'+ submenu[j].linkID +'_TEXT" class="CM_Text">'+ submenu[j].label +'</td>';
				s += '<td id="ID'+ submenu[j].linkID +'_SUB" nowrap align=center width=16 class="CM_Sub">';

				if(submenu[j].submenu != null && submenu[j].submenu != ""){
					s += "<span class='SubFlag'>}</span>";
				}
				else{
					s += "&nbsp;"
				}
				s += '</td></tr>'
			}

			s +='		  <tr><td colspan="3" height="2"></td></tr>'
				+'		</table></td>'
				+'		<td width="4" background="images/kuang_right2.gif"></td>'
				+'	  </tr>'
				+'	  <tr>'
				+'		<td background="images/kuang_down.gif"></td>'
				+'		<td height="9" background="images/kuang_down.gif"></td>'
				+'		<td background="images/kuang_down.gif"></td>'
				+'	  </tr>'
				+'	</table>';
		}

		s += '</div>';

		idMenuBar.innerHTML += s;
	}
}

function formatIcon(mnuIcon, mnuName){
	var result = '<td id="ID'+ mnuName +'_ICON" nowrap align=center width=18 class="CM_Icon">';
	if(mnuIcon != null && mnuIcon != ""){
		result += '<IMG SRC="images/'+ mnuIcon +'" BORDER=0>';
	}
	else{
		result += '<IMG SRC="images/s_generic_icon_1.gif" BORDER=0>';
	}
	result += "</td>";

	return result;
}

function SM_Over(obj){
	document.all.item(obj.id + "_ICON").className  = "CM_IconOver";
	document.all.item(obj.id + "_TEXT").className = "CM_TextOver";
	document.all.item(obj.id + "_SUB").className  = "CM_SubOver";

	var iconObj = document.all.item(obj.id + "_ICON");
	nodeList = iconObj.getElementsByTagName("IMG");
	strTmp = nodeList[0].src;
	pos = strTmp.indexOf("_1.gif");
	if(pos !=  -1){
		nodeList[0].src = strTmp.substring(0,pos)+"_2.gif";
	}
}

function SM_Out(obj){
	document.all.item(obj.id + "_ICON").className  = "CM_Icon";
	document.all.item(obj.id + "_TEXT").className = "CM_Text";
	document.all.item(obj.id + "_SUB").className  = "CM_Sub";

	var iconObj = document.all.item(obj.id + "_ICON");
	nodeList = iconObj.getElementsByTagName("IMG");
	strTmp = nodeList[0].src;
	pos = strTmp.indexOf("_2.gif");
	if(pos !=  -1){
		nodeList[0].src = strTmp.substring(0,pos)+"_1.gif";
	}
}

function SM_Click(url, target){
	if(target == "" && url != null && url != ""){
		this.location = url;
	}
	else if(target == "javacript" && url != null && url != "")
	{
		fun = url+"()";
		eval(fun);
	}
	else if (target == "childFrm" && url != null && url != "")
	{
		document.all("childFrm").src = url;
	}
}

function MM_Click(obj,sid){
	nodeList = obj.getElementsByTagName("IMG");
	sObj = document.all("ID"+sid);
	if(sObj.style.display==""){
		sObj.style.display = "none";
		nodeList[0].src = "images/button_f.gif";
	}
	else{
		sObj.style.display = "";
		nodeList[0].src = "images/button_s.gif";		
	}
}

window.attachEvent("onload",drawMenuBar);