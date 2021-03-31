<%@ include file="../timelater.jsp"%>
<%@ include file="../head.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>
<%
request.setCharacterEncoding("GBK");
%>
<%@page import="com.linkage.litms.tree.*"%>
<%
	String role_id = "" + (user.getRoleId());
	long acc_oid = user.getId();
	Droit Droit = new Droit();
%>
<%@page import="com.linkage.litms.system.Role"%>
<%@page import="com.linkage.litms.system.dbimpl.RoleSyb"%>
<%@page import="java.util.List"%>
<%@page import="java.util.Map"%>
<HTML>
<HEAD>
<TITLE></TITLE>
<META HTTP-EQUIV="Content-Type" CONTENT="text/html; charset=GBK">
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

<link rel="STYLESHEET" type="text/css" href="./css/dhtmlXTree.css">
<script src="./js/dhtmlXCommon.js"></script>
<script src="./js/dhtmlXTree.js"></script>

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
var iTimerID1;

//load tree on page
function loadTree(){
	
	tree = new dhtmlXTreeObject("treebox","100%","100%",0);
	tree.setImagePath("./imgs/");
	tree.enableCheckBoxes(true)
	tree.enableThreeStateCheckboxes(true);
	//tree.enableDragAndDrop(true);
	//tree.setDragHandler(doOnBeforeDrop);
	//tree.setOnClickHandler(doOnSelect);
	tree.loadXML("treefromdroitdb.jsp?t="+ new Date().getTime());
	
	//每秒调用展开菜单函数   modify by lizhaojun  2007-06-19
	iTimerID = window.setInterval(OpenItem,1000);
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
}
//send request to the server to load details
function loadDetails(id){
	status(true);
	xmlLoader.loadXML("loaddetails.jsp?id="+id);
}
//populate form of details
function doLoadDetails(obj){
	var f = document.forms["detailsForm"];
	var root = xmlLoader.getXMLTopNode("details")
	var id = root.getAttribute("id");
	//document.getElementById("details").style.visibility = "visible";
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

//选择功能点列表		
function leafLink(){
	var f = document.forms["detailsForm"];
	var tree_id = f.tree_id.value;
	var returnObj = window.showModalDialog("chooseItem.jsp?tree_id="+ tree_id +"&t="+new Date().getTime(),window,"status:yes;resizable:yes;edge:raised;scroll:no;help:no;center:yes;dialogHeight:500px;dialogWidth:600px");
}

function Save(){
	var list = tree.getAllCheckedBranches();
	//alert(list);
	document.all("tree_id_query").value = list;
	
	//submit
	var f = document.forms["detailsForm"];
	f.submit();
}

function DroitChange(flag,name){
	if(flag){
		window.alert("角色："+ name +" 操作成功");
	}else{
		window.alert("角色："+ name +" 操作失败");
	}
}

var net_flag = true;
function checkNet(){

if(net_flag){
net_flag = false;
//定位选择功能点
<%
List m_ItemList = Droit.getShortCutItemList(acc_oid);
Node item = null;
for(int i=0;i<m_ItemList.size();i++){
	item = (Node)m_ItemList.get(i);
%>
	tree.setCheck('<%=item.getNode_self_id()%>',true);
<%
}
Droit = null;
item = null;
m_ItemList.clear();
m_ItemList = null;
%>
status();

}

}

//展开第一级菜单
function OpenItem(){
	tree.openAllItems(0);	
<%
	Tree myTree = new Tree();
	List treeIdList = myTree.getTreeIds("0");
	Map _item = (Map)treeIdList.get(1);
	String _itemId = (String)_item.get("tree_id");
%>
	//如果菜单已打开 取消定时调用
	if(tree._getOpenState(tree._globalIdStorageFind('<%=_itemId%>'))==1){	    
		window.clearInterval(iTimerID);
		iTimerID1 = window.setTimeout("checkNet()",4000);
		checkNet();
	}	
<%
	_itemId = null;
	myTree = null;
	_item = null;
	treeIdList.clear();
	treeIdList = null;
%>			
}
</script>

<BODY onload="loadTree();">
<form name="detailsForm" action="droitShortCutSave.jsp"
	target="actionframe" method="post"><!--- block for displaying status of request on page --->
<div id="showproc" style="display:none;">Processing...</div>

<TABLE border=0 cellspacing=0 cellpadding=0 width="98%" align='center'>
<TR><TD HEIGHT=20>&nbsp;</TD></TR>
<TR><TD>
	<table width="100%" height="30" border="0" cellspacing="0" cellpadding="0" class="green_gargtd">
		<tr>
			<td width="162" align="center" class="title_bigwhite">
				快捷菜单
			</td>
			<td ><img
					src="../images/attention.gif" width=15 height=12>&nbsp;点击选择需要添加到快捷菜单项目的功能点</td>
		</tr>
	</table>
</TD></TR>
<TR><TD bgcolor=#999999>
		<table border=0 cellspacing=1 cellpadding=2 width="100%">
			<TR>
				<TH colspan="2" align="center"><B>快捷菜单</B></TH>
			</TR>
			<tr BGCOLOR=#ffffff >
				<td class=column colspan="1">帐号</TD>
				<TD><input type=text name="acc_loginname" class=bk size="40" value="<%=user.getAccount() %>">
	                      <input type=hidden name="acc_oid"  value="<%=user.getId()%>">&nbsp;<font color="#FF0000">*</font>
				</td>
			</tr>
			<tr>
				<!--- tree area --->
				<td width="500px" bgcolor=white colspan="2">
				<div id="treebox"
					style="	width:500px; 
							height:300px;
							background-color:#f5f5f5;
							border :1px solid Silver;" />
				</td>
			</tr>
			<tr>
				<td colspan="2" class='green_foot' align='right'><INPUT type="submit"
					value=' 保 存 ' onclick="Save()" class=btn></td>
			</tr>
		</table>
		</td>
	</tr>
</table>

<input type=hidden name=tree_id_query value="">
<iframe name="actionframe" id="actionframe" frameborder="0" width="100%"
	height="100"></iframe>
</form>
<%@ include file="../foot.jsp"%>