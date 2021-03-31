var oList;
var oRoleList = null;
var oPIDList = new Array();
var maxLayer=1;

function Operator(oid,poid,layer,name,remark,chk){
	this._oid	= oid;
	this._poid	= poid;
	this._layer = layer;
	this._name  = name;
	this._remark= remark;
	this._chk	= chk;
}


function loadOperator(){
	var XMLDoc = new ActiveXObject("Microsoft.XMLDOM");
	XMLDoc.async = false;
	XMLDoc.load(idTree.XMLSrc);
	Node = XMLDoc.selectNodes("//OperatorNode");
	oList = new Array(Node.length);
	
	var tmp;
	var chkStr = "";
	for(var i=0; i<Node.length; i++){
		tmp = Node.item(i);
		if(oRoleList != null && hasSelect(tmp.getAttribute("oid")))
			chkStr = "checked";
		else 
			chkStr = "";
		oList[i] = new Operator(tmp.getAttribute("oid"),tmp.getAttribute("poid"),tmp.getAttribute("layer"),tmp.getAttribute("name"),tmp.getAttribute("remark"),chkStr);
		//存放PID列表
		if(!checkPID(tmp.getAttribute("poid")))
			oPIDList[oPIDList.length] = tmp.getAttribute("poid");
		//获取Tree的深度，即最大层数
		if(maxLayer<tmp.getAttribute("layer")*1) maxLayer = tmp.getAttribute("layer")*1;
	}
}

function loadRoleOperator(){
	var XMLDoc = new ActiveXObject("Microsoft.XMLDOM");
	XMLDoc.async = false;
	if(typeof(idRoleTree) != "object") return;
	XMLDoc.load(idRoleTree.XMLSrc);
	Node = XMLDoc.selectNodes("//OperatorNode");
	oRoleList = new Array(Node.length);
	var tmp;
	for(var i=0; i<Node.length; i++){
		tmp = Node.item(i);
		oRoleList[i] = tmp.getAttribute("oid");
	}
}

function initTree(){
	loadRoleOperator();
	loadOperator();
	syncAll();
}

function syncAll(){
	var sHtml = "";
	for(var i=1;i<=maxLayer;i++){
		for(var j=0;j<oList.length;j++){
			if(oList[j]._layer == i){
				if(i==1){
					idTree.innerHTML += loadHTML(oList[j]);
					//alert("idTree_"+oList[j]._oid);
				}
				else{
					pObj = document.all("idTree_"+oList[j]._poid);
					if(typeof(pObj) != "object" || pObj==null) {continue;}
					obj = getObj(pObj);
					if(obj != null)
						obj.innerHTML += loadHTML(oList[j])
				}
			}
		}
	}
}

function getObj(objParent){
	var oChildren;
	try{
		oChildren  = objParent.children;
	}
	catch(e){
		alert("JSCRIPT出错：找不到父对象");
	}
	var obj = null;
	for(var i=0;i<oChildren.length;i++){
		if(oChildren[i].type == "container"){
			obj = oChildren[i];
			break;
		}
	}
	return obj;
}

function loadHTML(curObj){
	var blnChild;
	var HTMLStr="";
	blnChild = checkPID(curObj._oid);

	HTMLStr += '<div id="idTree_'+curObj._oid+'" type="leaf">'
	HTMLStr += '<span type="text"><input name=operator_id type=checkbox value="'+curObj._oid+'" onclick="syncCheckBox()" '+curObj._chk+'></span>';
	if(blnChild){
		HTMLStr += '<span class="clsSpace" type="img"><span class="clsCollapse" onclick="syncTree('+curObj._oid+')">+</span></span>';
	}
	else{
		HTMLStr += '<span class="clsSpace" type="img"><span class="clsLeaf">.</span></span>';
	}
	HTMLStr += '<span type="label">'+curObj._remark+'</span>';
	if(blnChild){
		HTMLStr += '<div id="idcontainer" class="hide" type="container"></div>';
	}
	HTMLStr += '</div>';

	return HTMLStr;	
}

function checkPID(_pid){
	for(var i=0;i<oPIDList.length;i++){
		if(oPIDList[i] == _pid)
			return true;
	}
	return false;
}

function syncTree(_oid){
	objCurrent = event.srcElement;
	objParent  = objCurrent.parentElement.parentElement;
	obj = getObj(objParent);
	
	if(obj.className=="hide"){
		obj.className = "shown";
		objCurrent.innerHTML = "-";
		objCurrent.className = "clsExpand";
	}
	else{
		obj.className = "hide";
		objCurrent.innerHTML = "+";
		objCurrent.className = "clsCollapse";
	}	
}

function hasSelect(oid){
	for(var i=0;i<oRoleList.length;i++){
		if(oRoleList[i] == oid)
			return true;
	}
	return false;	
}

function syncCheckBox(){
	objCurrent = event.srcElement;
	blnChk = objCurrent.checked;
	objParent  = objCurrent.parentElement.parentElement;
	objCheckBox = objParent.getElementsByTagName("input");
	for(var i=0; i<objCheckBox.length; i++){
		objCheckBox[i].checked = blnChk;
	}

	//modify 2005-4-28 by yuht 选择节点时，所有关系的父节点都选中
	objParent= objParent.parentElement;
	while(objParent.tagName.toUpperCase() != "BODY"){
		if(objParent.id.indexOf("idTree_") != -1){
			objCheckBox = objParent.getElementsByTagName("input");
			objCheckBox[0].checked = getBlnCheck(objParent);
		}

		objParent = objParent.parentElement;
		
	}
}

//modify 2005-5-8 by yuht 判断参数节点的一级子节点中是否有被选中的
function getBlnCheck(obj){
	objContainer = obj.children("idcontainer");
	if(objContainer != null){
		objCheckBox = objContainer.getElementsByTagName("input");
		for(var i=0;i<objCheckBox.length;i++){
			if(objCheckBox[i].checked)
				return true;
		}
	}
	return false;
}