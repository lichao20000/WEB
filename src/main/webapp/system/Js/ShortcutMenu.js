var oList;
var oRoleList = null;

function ShortcutMenu(sname,url,role,chk){
	this._sname = sname;
	this._url   = url;
	this._chk	= chk;
	this._role  = role;
}

function loadShortcutMenu(){
	var XMLDoc = new ActiveXObject("Microsoft.XMLDOM");
	XMLDoc.async = false;
	XMLDoc.load(idTree.XMLSrc);
	Node = XMLDoc.selectNodes("//ShortcutMenu");
	oList = new Array(Node.length);	
	
	var chkStr = "";
	for(var i=0;i<Node.length;i++){
		tmp = Node.item(i);
		if(oRoleList != null && hasSelect(tmp.getAttribute("url")))
			chkStr = "checked";
		else 
			chkStr = "";
		oList[i] = new ShortcutMenu(tmp.getAttribute("sname"),tmp.getAttribute("url"),tmp.getAttribute("role"),chkStr)
	}
}

function initTree(){
	loadRoleShortcutMenu();
	loadShortcutMenu();
	loadHtml();
}

function loadHtml(){
	var Html = "";
	for(var i=0;i<oList.length;i++){
		if(chkPremession(oList[i]._role))
			Html += "<input type=checkbox name=shortcut value='"+oList[i]._url+","+oList[i]._sname+"' "+ oList[i]._chk +">&nbsp;"+oList[i]._sname+"<br>";
	}
	document.write(Html);
}

function loadRoleShortcutMenu(){
	if(typeof(idRoleTree) != "object") return;
	oRoleList = idRoleTree.innerText;
}

function hasSelect(url){
	arr = oRoleList.split(",");
	for(var i=0;i<arr.length;i++){
		if(arr[i] == url)
			return true;
	}
	return false;	
}
