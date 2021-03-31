<%@ include file="../../../timelater.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>
<%request.setCharacterEncoding("GBK");%>
<%
//权限角色编码
String role_id = String.valueOf(user.getRoleId());
%>
<HTML>
<HEAD>
<TITLE></TITLE>
<META HTTP-EQUIV="Content-Type" CONTENT="text/html; charset=gb2312">
<LINK REL="stylesheet" HREF="style.css" TYPE="text/css">
</HEAD>

<style>
	#details span{
		font-size:10px;
		font-family:arial;
		display:block;
	}
	#details input,#details textarea{
		border: 1px solid gray;
	}
	#actions a{
		font-size:12px;
		font-family:arial;
		margin:5px;
		text-decoration:none;
	}
	#actions {
		border-bottom:1px solid blue;
		padding:2px;
		margin-bottom:10px;
		margin-top:10px;
	}
	#showproc{
		border:1px solid;
		border-color: lightblue blue blue lightblue;
		background-color: #87ceeb;
		font-family:arial;
		font-size: 14px;
		padding: 5px;
		position: absolute;
		top:100px;
		left:600px;
	}
</style>

<link rel="STYLESHEET" type="text/css" href="../../../droitTree/css/dhtmlXTree.css">
<script  src="../../../droitTree/js/dhtmlXCommon.js"></script>
<script  src="../../../droitTree/js/dhtmlXTree.js"></script>

<script>
//tree object
var tree;
//xml loader to load details from database
var xmlLoader = new dtmlXMLLoaderObject(doLoadDetails,window);
//default label for new item
var newItemLabel = "New Item";
//id for new (unsaved) item
var newItemId = "-1";
var iTimerID;

//load tree on page
function loadTree(){	
	tree = new dhtmlXTreeObject("treebox","100%","100%",0);
	//tree = new dhtmlXTreeObject(document.getElementById("treebox"),"100%","100%",0);	
	tree.setImagePath("../../../droitTree/imgs/");	
	tree.setOnClickHandler(doOnClick);	
	tree.enableCheckBoxes(true);	
	tree.loadXML("./rp_tree_list_db.jsp?role_id=<%=role_id%>");		
	
	//每秒调用展开菜单函数
//	iTimerID = window.setInterval(OpenItem,1000);
}

function preLoadImages(){
	var imSrcAr = new Array("line1.gif","line2.gif","line3.gif","line4.gif","minus2.gif","minus3.gif","minus4.gif","plus2.gif","plus3.gif","plus4.gif","book.gif","books_open.gif","books_close.gif","magazines_open.gif","magazines_close.gif","tombs.gif","tombs_mag.gif","book_titel.gif","iconCheckAll.gif")
	var imAr = new Array(0);
	for(var i=0;i<imSrcAr.length;i++){
		imAr[imAr.length] = new Image();
		imAr[imAr.length-1].src = "./imgs/"+imSrcAr[i]
	}
}	

//add new node next to currently selected (or the first in tree)
function addNewPeer(){
	if(tree.getLevel(newItemId)!=0){//check if unsaved item already exists
		alert("New Item (unsaved) already exists")
		return false;
	}
	var selectedId = tree.getSelectedItemId();
	if(selectedId!=""){
		tree.insertNewNext(selectedId,newItemId,newItemLabel,"","","","","SELECT,CALL",0)
	}else{
		tree.insertNewItem(0,newItemId,newItemLabel,"","","","","SELECT,CALL",0)
	}
}

//add new child node to selected item (or the first item in tree)
function addNewChild(){
	if(tree.getLevel(newItemId)!=0){//check if unsaved item already exists
		alert("New Item (unsaved) already exists")
		return false;
	}
	var selectedId = tree.getSelectedItemId();
	if(selectedId!=""){
		tree.insertNewItem(selectedId,newItemId,newItemLabel,"","","","","SELECT,CALL",0)
	}else{
		tree.insertNewItem(0,newItemId,newItemLabel,"","","","","SELECT,CALL",0)
	}
}

//delete item (from database)
function deleteNode(){
	status(true);
	var f = document.forms["detailsForm"];
	if(tree.getSelectedItemId()!=newItemId){//delete node from db
		if(!confirm("Are you sure you want to delete selected node?"))
			return false;
		f.action = "deletenode.jsp"
		f.submit()
	}else{//delete unsaved node
		doDeleteTreeItem(newItemId);
	}
}

//remove item from tree
function doDeleteTreeItem(id){
	document.getElementById("details").style.visibility = "hidden";
	var pId = tree.getParentId(id);
	tree.deleteItem(id);
	if(pId!="0")
		tree.selectItem(pId,true);
	status();
}

//save item
function saveItem(){
	status(true);
	var f = document.forms["detailsForm"];
	f.action = "savenode.jsp";
	f.submit();
	
	//var list = tree.getAllChecked();
	//tree.setSubChecked(id,true);
}

//save moved (droped) item to db. Cancel drop if save failed or item is new
function doOnBeforeDrop(id,parentId){
	if(id==newItemId)
		return false;
	else{
		status(true);
		var dropSaver = new dtmlXMLLoaderObject(null,null,false);//sync mode
		dropSaver.loadXML("dropprocessor.jsp?id="+id+"&parent_id="+parentId);
		var root = dropSaver.getXMLTopNode("succeedded");
		var id = root.getAttribute("id");
		if(id==-1){
			alert("Save failed");
			status();
			return false;
		}else{
			if(tree.getSelectedItemId()==id){//update details (really need only parent id)
				loadDetails(id);
			}
		}
		status();
		return true;
	}
}

//update item
function doUpdateItem(id, label){
	var f = document.forms["detailsForm"];
	f.tree_id.value = id;
	tree.changeItemId(tree.getSelectedItemId(),id);
	tree.setItemText(id,label);
	tree.setItemColor(id,"black","white");
	status();
}

//what to do when item selected
function doOnSelect(itemId){
	if(itemId!=newItemId){
		if(tree.getLevel(newItemId)!=0){
			if(confirm("Do you want to save changes?")){//save changes to new item
				tree.selectItem(newItemId,false)
				saveItem();
				return;
			}
			tree.deleteItem(newItemId);
		}	
	}else{//set color to new item label
		tree.setItemColor(itemId,"red","pink")
	}
	
	loadDetails(itemId);//load details for selected item
	window.returnValue = itemId;
	window.close();
}
//send request to the server to load details
function loadDetails(id){
	alert("loadDetails");
	status(true);
	xmlLoader.loadXML("loaddetails.jsp?id="+id);
}
//populate form of details
function doLoadDetails(obj){
	var f = document.forms["detailsForm"];
	var root = xmlLoader.getXMLTopNode("details")
	var id = root.getAttribute("id");
	document.getElementById("details").style.visibility = "visible";
	if(id==newItemId){
		f.tree_name.value = tree.getItemText(id);
		f.tree_desc.value = "";
	}else{
		f.tree_name.value = root.getElementsByTagName("name")[0].firstChild.nodeValue;
		if(root.getElementsByTagName("desc")[0].firstChild!=null)
			f.tree_desc.value = root.getElementsByTagName("desc")[0].firstChild.nodeValue;
		else
			f.tree_desc.value = "";
	}
	f.tree_id.value = id
	f.tree_parent_id.value = tree.getParentId(id);
	status();
}

//show status of request on page
function status(fl){
	var d = document.getElementById("showproc");
	if(fl)
		d.style.display = "";
	else
		d.style.display = "none";
}

var net_flag = true;
function openAllItems(){
	if(net_flag){
		net_flag = false;
		tree.openAllItems(0);
	}
}

function doOnClick(id){
	var fileNm = getFile(id)||"";
	//alert(fileNm);
	if(fileNm!=""){
		if(!tree.getUserData(id,"type")){
			//alert(fileNm+"#"+id);
			if(fileNm.indexOf("?") > 0){
				parent.parent.window.frames[2].document.all('viewPage').src = "../.."+fileNm+"&tmp_treeitemid="+ tree.getParentId(id) +"|"+id;
			}else{
				parent.parent.window.frames[2].document.all('viewPage').src = "../.."+fileNm+"?tmp_treeitemid="+ tree.getParentId(id) +"|"+id;
				window.close
			}
		}else{
			window.open(fileNm+"#"+id)
		}
	}
}
function getFile(id){
	while(!tree.getUserData(id,"file") && tree.getLevel(id)>0){
		id = tree.getParentId(id)
	}
	return tree.getUserData(id,"file");
}



function RoleLeafLink(){
	var list = tree.getAllCheckedBranches();
	//alert(list);
	if(list.length > 0 )
	{
		window.returnValue = list;
		window.close();
  }
  else
  {	
  	alert("请选择一个目录");
  }
	//submit
  //var f = document.forms["detailsForm"];
  //f.submit();
}
</script>


<body onload="loadTree()" style="height:100%"  leftmargin="0" topmargin="0" marginwidth="0" marginheight="0">
<table width="100%" border="0" cellpadding="0" cellspacing="0" background="images/left_back2.jpg">
	<tr>
		<td>&nbsp;</td>
	</tr>
</table>
<table border=0 cellspacing=1 cellpadding=2 width="100%">
<tr>
	<td class=column colspan="2">报表模板定制<font coloc="red">*请选择一个目录</font></td>
</tr>
<tr>
<!--- tree area --->
<td colspan="2" width="350px"  class="jive-setup-category-header" bgcolor=white>
	<div id="treebox" 
					style="	width:350px; 
							height:300px;
							background-color:#f5f5f5;
							border :1px solid Silver;"/>
</td>
</tr>
<tr>
	<td colspan="2" bgcolor=white>
		<INPUT type="button" value='确定' onClick="RoleLeafLink()" class=btn>
		<INPUT type="button" value='关闭' onClick="window.close()" class=btn>
	</td>
</tr>
</table>
</body>
</html>


