var HTMLStr;

var MenuHeadStr1;

var MenuHeadStr2;

var MenuFootStr;

//var SubMenuHeadStr;

//var SubMenuHeadStr;

var LSMenuStr;

var ICPMenuStr;

var LSDefColor  = new Array(3);

var ICPDefColor = new Array(3);

var firstLSMenu = true;

var firstICPMenu= true;

var toolbarBgColor;

var ICPMenuBgColor;

var activeObj= null;

var MenuWidth = new Array(2);

var mnuX = 0;

var mnuY = 0;

var g_bICPMenu = false;

var site = "/ldims_new/";



toolbarBgColor = "white";

LSDefColor[0]  = "#6386DE";

ICPDefColor[0] = "#2951AD";

LSDefColor[1]  = ICPDefColor[1] = "white";

LSDefColor[2]  = ICPDefColor[2] = "#FF9900";

MenuWidth[0]   = "300";

MenuWidth[1]   = "200";



MenuHeadStr1 = "<TABLE id='idLSMenuPanel' border=0 cellspacing=0 cellpadding=0 style='width:"+ MenuWidth[0] +";'><TR>";

MenuHeadStr2 = "<TABLE id='idICPMenuPanel' border=0 cellspacing=0 cellpadding=0 style='width:"+ MenuWidth[1] +";'><TR height=23>";

MenuFootStr  = "</TR></TABLE>";



HTMLStr = "<TABLE id=idToolbar border=0 cellspacing=0 cellpadding=0 width='100%' style='background-color: "+ toolbarBgColor +"'>";

HTMLStr += "<TR><TD rowspan=2 colspan=2><!-- ICPBanner --></TD>";

HTMLStr += "<TD colspan=2><TABLE border=0 cellspacing=0 cellpadding=0 align=right><TR>";

HTMLStr += "<TD width='18'><IMG SRC='../images/curve.gif' WIDTH=18 HEIGHT=20 BORDER=0></TD>";

HTMLStr += "<TD style='background-color:"+ LSDefColor[0] +"'>"+ MenuHeadStr1 +"<!-- LSMenu -->"+ MenuFootStr +"</TD></TR></TABLE></TD></TR>";

//HTMLStr += "<TR><TD align=center><!-- ADSBanner --></TD><TD align=right><!-- Logo --></TD></TR>";

HTMLStr += "<TR><TD align=right><!-- Logo --></TD></TR>";

HTMLStr += "<TR><TD colspan=3 style='background-color:white;height:1px'></TD></TR>";

HTMLStr += "<TR><TD id='idICPPanel' colspan=3 style='background-color:"+ ICPDefColor[0] +"'>"+ MenuHeadStr2 +"<!-- ICPMenu -->"+ MenuFootStr +"</TD></TR>";

HTMLStr += "</TABLE>";

HTMLStr += "<!-- SubLSMenu --><!-- SubICPMenu -->";



function drawToolbar(){

	document.write(HTMLStr);

	idICPPanel.style.backgroundColor=ICPDefColor[0];

	idToolbar.style.backgroundColor=toolbarBgColor;

	idLSMenuPanel.style.width  = MenuWidth[0];

	idICPMenuPanel.style.width = MenuWidth[1];

}



function setMenuWidth(LSW, ICPW){

	MenuWidth[1] = ICPW;

	MenuWidth[0] = LSW;

}



function setToolbarBGColor(color){

	toolbarBgColor = color;

}



function setLSMenuColor(c1,c2,c3){

	LSDefColor[0] = c1

	LSDefColor[1] = c2;

	LSDefColor[2] = c3;

}



function setICPMenuColor(c1,c2,c3){

	ICPDefColor[0] = c1;

	ICPDefColor[1] = c2;

	ICPDefColor[2] = c3;

}



function setICPBanner(IMGStr,ALTStr,LINKStr,TARGETStr){

	BannerStr = "<A HREF='"+ LINKStr +"' TARGET='"+ TARGETStr+"'><IMG SRC='"+ IMGStr +"' ALT='"+ ALTStr +"' BORDER=0></A>";

	HTMLStr = HTMLStr.replace("<!-- ICPBanner -->",BannerStr);

}



function setADSBanner(IMGStr,ALTStr,LINKStr,TARGETStr){

	BannerStr = "<A HREF='"+ LINKStr +"' TARGET='"+ TARGETStr+"'><IMG SRC='"+ IMGStr +"' ALT='"+ ALTStr +"'  BORDER=0></A>";

	HTMLStr = HTMLStr.replace("<!-- ADSBanner -->",BannerStr);

}



function setLogo(IMGStr,ALTStr,LINKStr,TARGETStr){

	if(LINKStr!="")

		BannerStr = "<A HREF='"+ LINKStr +"' TARGET='"+ TARGETStr+"'><IMG SRC='"+ IMGStr +"' ALT='"+ ALTStr +"' BORDER=0></A>";

	else

		BannerStr = "<IMG SRC='"+ IMGStr +"' ALT='"+ ALTStr +"' BORDER=0>";

	HTMLStr = HTMLStr.replace("<!-- Logo -->",BannerStr);

}



function AddLSMenu(TagStr,MenuStr,LinkStr,TitleStr){

	tempID = "LS_" + TagStr;

	AddMenu(tempID,MenuStr,LinkStr,TitleStr,false,"<!-- LSMenu -->");

	if(firstLSMenu) firstLSMenu = false;

}



function AddICPMenu(TagStr,MenuStr,LinkStr,TitleStr){

	tempID = "ICP_" + TagStr;

	AddMenu(tempID,MenuStr,LinkStr,TitleStr,true,"<!-- ICPMenu -->");

	if(firstICPMenu) firstICPMenu = false;

}



function AddMenu(IDStr,MenuStr,LinkStr,TitleStr,bICPMenu,AddStr){

	firstMenu = bICPMenu?firstICPMenu:firstLSMenu;

	color    = bICPMenu?ICPDefColor[1]:LSDefColor[1];

	

	TMenuStr = "";

	if(!firstMenu)

		TMenuStr += "<TD align=center><FONT COLOR='"+ color +"'>|</FONT></TD>";



	TMenuStr += "<TD id="+ IDStr +" align=center ";

	TMenuStr += "onmouseover=\"MouseOver(this,"+ bICPMenu +");ShowMenu('"+ IDStr +"',"+ bICPMenu +")\" ";

	TMenuStr += "onmouseout='MouseOut(this,"+ bICPMenu +")'>";

	TMenuStr += formatURL(MenuStr,LinkStr,TitleStr,bICPMenu);

	TMenuStr += "</TD>" + AddStr;



	HTMLStr = HTMLStr.replace(AddStr,TMenuStr);

}



function AddSubLSMenu(TagStr,MenuStr,LinkStr,TitleStr){

	tempID = "Sub_LS_" + TagStr;

	AddSubMenu(tempID,MenuStr,LinkStr,TitleStr,false,"<!-- SubLSMenu -->");

}



function AddSubICPMenu(TagStr,MenuStr,LinkStr,TitleStr){

	tempID = "Sub_ICP_" + TagStr;

	AddSubMenu(tempID,MenuStr,LinkStr,TitleStr,true,"<!-- SubICPMenu -->");

}



function AddSubMenu(IDStr,MenuStr,LinkStr,TitleStr,bICPMenu,AddStr){

	TSubMenuStr = "";

	color       = bICPMenu?ICPDefColor[0]:LSDefColor[0];

	tempAddStr  = "<!-- "+ IDStr +" -->";

	if(HTMLStr.indexOf(IDStr) == -1){

		tempStr = "<DIV id="+ IDStr +" style='position:absolute;top:0px;left:0px;display:none;z-index:100'>";

		tempStr += "<TABLE border=0 cellspacing=1 cellpadding=2 style='background-color: "+ color +"'>";

		tempStr += "<TR><TD height=3></TD></TR>"

		tempStr += tempAddStr + "<TR><TD height=5></TD></TR></TABLE></DIV>" + AddStr;

		HTMLStr = HTMLStr.replace(AddStr,tempStr);

	}

	

	TSubMenuStr += "<TR><TD ";

	TSubMenuStr += "onmouseover='MouseOver(this,"+ bICPMenu +")' ";

	TSubMenuStr += "onmouseout='MouseOut(this,"+ bICPMenu +")'>&nbsp;";

	TSubMenuStr += formatURL(MenuStr,LinkStr,TitleStr,bICPMenu);

	TSubMenuStr += "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</TD></TR>" + tempAddStr;



	HTMLStr = HTMLStr.replace(tempAddStr,TSubMenuStr);

}



function AddHR(TagStr,bICPMenu){

	color    = bICPMenu?ICPDefColor[1]:LSDefColor[1];

	tempID   = bICPMenu?"Sub_ICP_" + TagStr:"Sub_LS_" + TagStr;

	tempAddStr  = "<!-- "+ tempID +" -->";

	SubHRStr = "<TR><TD><HR color='"+ color +"' size=1></TD></TR>" + tempAddStr;

	

	HTMLStr  = HTMLStr.replace(tempAddStr,SubHRStr); 

}



function formatURL(MenuStr,LinkStr,TitleStr,bICPMenu){

	ResultStr = "";

	color    = bICPMenu?ICPDefColor[1]:LSDefColor[1];

	if(LinkStr == ""){

		ResultStr += "<Font STYLE='color:"+ color +";cursor:hand'>" + MenuStr + "</Font>";

	}

	else{

		ResultStr += "<A HREf='"+ LinkStr +"' TITLE='"+ TitleStr +"' STYLE='color:"+ color +";text-decoration: none'>" + MenuStr + "</A>";

	}



	return ResultStr;

}



function MouseOver(obj,bICPMenu){

	color    = bICPMenu?ICPDefColor[2]:LSDefColor[2];

	for(var i=0; i<obj.childNodes.length; i++){

		childObj = obj.childNodes(i);

		var tagStr = childObj.tagName;

		if(tagStr == "A" || tagStr == "FONT")

			break;

	}

	childObj.style.color = color;

}



function MouseOut(obj,bICPMenu){

	color    = bICPMenu?ICPDefColor[1]:LSDefColor[1];

	for(var i=0; i<obj.childNodes.length; i++){

		childObj = obj.childNodes(i);

		var tagStr = childObj.tagName;

		if(tagStr == "A" || tagStr == "FONT")

			break;

	}

	childObj.style.color = color;

}



function ShowMenu(IDStr,bICPMenu){

	tempID = "Sub_" + IDStr;

	subObj = document.getElementById(tempID);

	if(activeObj != null)

		HideMenu(activeObj);



	if(subObj == null) return;

	Obj  = document.getElementById(IDStr);

	var top  = Obj.offsetTop;

	var left = Obj.offsetLeft;

	var cw = document.body.clientWidth;

	top  = bICPMenu?top+84:top+21;

	left = bICPMenu?left:cw-MenuWidth[0]+left;

	subObj.style.display = "";

	var MaxLeft = cw-subObj.offsetWidth;

	if(left>MaxLeft)

		left = MaxLeft;



	subObj.style.top  = top;

	subObj.style.left = left;

	mnuX = left;

	mnuY = top;

	activeObj = subObj;

	g_bICPMenu = bICPMenu;

	hideElement("SELECT");

}



function HideMenu(obj){

	if(obj != null){

		obj.style.display = "none";

		activeObj = null;

	}

}



function MouseMove(){

	if(activeObj == null)

		return;

	var top  = parseInt(activeObj.style.top);

	var left = parseInt(activeObj.style.left);

	var wi   = activeObj.offsetWidth;

	var he   = activeObj.offsetHeight;

	var x    = event.clientX;

	var y    = event.clientY + document.body.scrollTop;

	var x1 = left + wi;

	var y1 = top + he;

	var y2 = top - 24;



	if(x>x1||x<left||y>y1||y<y2){

		if(activeObj.style.display != "none"){

			showElement("SELECT");

			HideMenu(activeObj);

		}

	}

}



function hideElement(elmID){

	for (i = 0; i < document.all.tags(elmID).length; i++){

		obj = document.all.tags(elmID)[i];

		if (! obj || ! obj.offsetParent)

			continue;

		

		if(obj.style.display=="none" || obj.style.visility == "hidden")

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

		//alert("mnuX="+mnuX+" mnuY="+ mnuY+" w="+activeObj.offsetWidth+" h="+activeObj.offsetHeight);

		//alert("x="+ objLeft+" y="+ objTop+" w="+obj.offsetWidth+" h="+obj.offsetHeight);



		if(mnuX > (objLeft + obj.offsetWidth) || objLeft > (mnuX + activeObj.offsetWidth))

			continue;

		else if(objTop > (mnuY + activeObj.offsetHeight))

			continue;

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





document.onmousemove = MouseMove;



function showFloatMenu(){

	i = parseInt(idFloatLayer.style.left);

	if(i!=-150) return;

	MoveLayer(30,8,true);

}



function hideFloatMenu(){

	i = parseInt(idFloatLayer.style.left);

	if(i!=1) return;

	MoveLayer(30,-8,false);

}



function MoveLayer(tStep,wStep,bOut){

	i = parseInt(idFloatLayer.style.left);

	i += wStep;

	if(bOut){

		if(i<1){

			idFloatLayer.style.left = i;

			setTimeout("MoveLayer("+tStep+","+wStep+","+bOut+")",tStep);

		}

		else

			idFloatLayer.style.left = 1;

	}

	else{

		if(i>-150){

			idFloatLayer.style.left = i;

			setTimeout("MoveLayer("+tStep+","+wStep+","+bOut+")",tStep);

		}

		else

			idFloatLayer.style.left = -150;

	}

}



function ScollLayer(){

	idFloatLayer.style.top = 85 + document.body.scrollTop;

}