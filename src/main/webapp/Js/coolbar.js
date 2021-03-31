/**

* 新风格标头

* 2005-3-8 by yuht

*/

var HtmlStr;

var firstICPMenu = true;

var firstSubICPMenu = true;

var MenuHeadStr = "<TABLE id='idICPMenuPanel' border=0 cellspacing=0 cellpadding=0><TR>";

var MenuFootStr  = "</TR></TABLE>";

var activeStr = null;



HtmlStr = "<table id=idCoolbar border=0 cellspacing=0 cellpadding=0 width='1002' align='center'>";

HtmlStr += "<tr><td id=idBanner><!-- ICPBanner --><td></tr>";

HtmlStr += "<tr height=30><td id=idMenu background='images/button_line.jpg'>"+ MenuHeadStr +"<!-- ICPMenu --><td>&nbsp;</td>"+ MenuFootStr +"</td></tr>";

HtmlStr += "</table>";

HtmlStr += "<!-- SubICPMenu -->";



function drawToolbar(){

	document.write(HtmlStr);

	//document.all("idtest").innerText = HtmlStr;

}



function setICPBanner(IMGStr,ALTStr,LINKStr,TARGETStr){

	var BannerStr;

	if(LINKStr != "" ){

		BannerStr = "<A HREF='"+ LINKStr +"' TARGET='"+ TARGETStr+"'><IMG SRC='"+ IMGStr +"' ALT='"+ ALTStr +"' BORDER=0 usemap='#Map'></A>";

	}

	else{

		BannerStr = "<IMG SRC='"+ IMGStr +"' ALT='"+ ALTStr +"' BORDER=0 usemap='#Map'>";

	}

	HtmlStr = HtmlStr.replace("<!-- ICPBanner -->",BannerStr);

}



function AddLSMenu(TagStr,MenuStr,LinkStr,TitleStr){

}



function AddICPMenu(TagStr,MenuStr,LinkStr,TitleStr){

	tempID = "ICP_" + TagStr;

	AddMenu(tempID,MenuStr,LinkStr,TitleStr,true,"<!-- ICPMenu -->");

}



function AddMenu(IDStr,MenuStr,LinkStr,TitleStr,bICPMenu,AddStr){

	TMenuStr = "";

	if(firstICPMenu){

		TMenuStr += "<TD width=200>&nbsp;</TD>";

		firstICPMenu = false;

	}



	TMenuStr += "<TD id="+ IDStr +" align=center width='123' height='38' class=button ";

	TMenuStr += "onmouseover=\"MouseOver(this,"+ bICPMenu +");ShowMenu('"+ IDStr +"',"+ bICPMenu +")\" ";

	TMenuStr += "onmouseout='MouseOut(this,"+ bICPMenu +")'";

	if(LinkStr !=""){

		TMenuStr += " onclick=location.href='"+ LinkStr +"'";

	}

	TMenuStr += "><font style='filter:glow(color=#FFFFFF,strength=2);zoom:1'>"+ MenuStr + "</Font>";

	TMenuStr += "</TD>" + AddStr;



	HtmlStr = HtmlStr.replace(AddStr,TMenuStr);

}



function AddSubLSMenu(TagStr,MenuStr,LinkStr,TitleStr){



}



function AddSubICPMenu(TagStr,MenuStr,LinkStr,TitleStr){

	tempID = "Sub_ICP_" + TagStr;

	AddSubMenu(tempID,MenuStr,LinkStr,TitleStr,true,"<!-- SubICPMenu -->");

}



function AddSubMenu(IDStr,MenuStr,LinkStr,TitleStr,bICPMenu,AddStr){

	TSubMenuStr = "";

	tempAddStr  = "<!-- "+ IDStr +" -->";

	if(HtmlStr.indexOf(IDStr) == -1){

		tempStr = "<DIV id="+ IDStr +" style='position:absolute;top:0px;left:0px;display:none;z-index:10'>";

		tempStr += "<TABLE border=0 cellspacing=1 cellpadding=2>";

		tempStr += "<TR><TD>→ </TD><TD>";

		tempStr += tempAddStr + "</TD></TR></TABLE></DIV>" + AddStr;

		HtmlStr = HtmlStr.replace(AddStr,tempStr);

	}

	if(!firstSubICPMenu){

		TSubMenuStr += "&nbsp;|&nbsp;";

	}

	else{

		firstSubICPMenu = false;

	}

	TSubMenuStr += formatURL(MenuStr,LinkStr,TitleStr,bICPMenu);

	TSubMenuStr += tempAddStr;

	HtmlStr = HtmlStr.replace(tempAddStr,TSubMenuStr);

}



function formatURL(MenuStr,LinkStr,TitleStr,bICPMenu){

	ResultStr = "";

	if(LinkStr == ""){

		ResultStr += "<Font STYLE='cursor:hand'>" + MenuStr + "</Font>";

	}

	else{

		ResultStr += "<A HREf='"+ LinkStr +"' TITLE='"+ TitleStr +"' STYLE='text-decoration: none'>" + MenuStr + "</A>";

	}



	return ResultStr;

}



function MouseOver(obj,bICPMenu){

	obj.className = "buttonover";

}



function MouseOut(obj,bICPMenu){

	obj.className = "button";

}



function ShowMenu(IDStr,bICPMenu){

	tempID = "Sub_" + IDStr;

	subObj = document.getElementById(tempID);



	if(activeStr == null){

		activeStr = IDStr;

	}

	else{

		if(activeStr==IDStr){

			return;

		}

		else{

			CloseSubMenu();

			activeStr = IDStr;

		}

	}

	if(subObj == null) return;

	subObj.style.display = "";

	subObj.style.left = document.body.clientWidth-subObj.offsetWidth;

	subObj.style.top  = 88;



	MoveSubMenu(IDStr,10);

}



function CloseSubMenu(){

	if(activeStr != null){

		activeObj = document.getElementById("Sub_"+activeStr);

		if(activeObj != null) activeObj.style.display="none";

	}

}



function MoveSubMenu(IDStr,step){

	tempID = "Sub_" + IDStr;

	subObj = document.getElementById(tempID);

	var i = parseInt(subObj.style.left) - step;

	var w = idCoolbar.offsetLeft + 200;

	if(i>w){

		subObj.style.left = i;

		setTimeout("MoveSubMenu('"+IDStr+"',"+step+")",30);

	}

	else{

		subObj.style.left = w;

	}

}



function setMenuWidth(LSW, ICPW){



}