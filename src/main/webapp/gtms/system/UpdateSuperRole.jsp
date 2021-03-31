<%@ include file="../../timelater.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>
<%request.setCharacterEncoding("GBK");%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ include file="../../head.jsp"%>
<HTML>
<HEAD>
<TITLE></TITLE>
<META HTTP-EQUIV="Content-Type" CONTENT="text/html; charset=GBK">
</HEAD>
<link rel="STYLESHEET" type="text/css" href="../../droitTree/css/dhtmlXTree.css">
<script  src="../../droitTree/js/dhtmlXCommon.js"></script>
<script  src="../../droitTree/js/dhtmlXTree.js"></script>	
<link rel="stylesheet" href="<s:url value="/css3/c_table.css"/>" type="text/css">
<link rel="stylesheet" href="<s:url value="/css3/global.css"/>" type="text/css">
<SCRIPT LANGUAGE="JavaScript" SRC="<s:url value="/Js/jquery.js"/>"></SCRIPT>
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

function DroitChange(flag,name){
	if(flag){
		window.alert("角色："+ name +" 操作成功");
	}else{
		window.alert("角色："+ name +" 操作失败");
	}
}

function testParent(){
	alert("testParent");
}

var net_flag = true;
function openAllItems(){
	if(net_flag){
		net_flag = false;
		tree.openAllItems(0);
	}
}
function checkRole(){
	var tree_id_query = $("#tree_id_query").val();
	var tree_ids = tree_id_query.split(",");
	var id ;
	for(var i=0;i<tree_ids.length;i++){
		id = tree_ids[i];
		tree.setCheck(id,true);
	}
	tree.setCheck(1,true);
	tree.setCheck(4,true);
	tree.setCheck(5,true);
	status();
}
//展开第一级菜单
function OpenItem(){
	tree.openAllItems(0);	

	//如果菜单已打开 取消定时调用
	if(tree._getOpenState(tree._globalIdStorageFind('0'))==1){	    
		window.clearInterval(iTimerID);
	//	iTimerID1 = window.setTimeout("checkRole()",4000);
		checkRole();
	}	
}
</script>
<%@ include file="../../toolbar.jsp"%>

<BODY onload="loadTree();">
<BR>
<form name="detailsForm" action="<s:url value='/gtms/system/superRoleManage!updateSuperRole.action'/>" target="actionframe" method="post">
<TABLE  border=0 cellspacing=0 cellpadding=0 width="98%" align='center'>
<TR>
	<TD>
		<TABLE width="100%"  height="30" border="0" cellspacing="0" cellpadding="0" class="green_gargtd">
			<TR>
				<TD width="162" align="center" class="title_bigwhite">修改角色</TD>
				<TD ><img src="../../images/attention.gif" width=15 height=12>&nbsp;修改角色</TD>
			</TR>
		</TABLE>
	</TD>
</TR>
<TR>
	<TD bgcolor="#999999">
		<table  border=0 cellspacing=1 cellpadding=2 WIDTH="100%" class="querytable">
			<TR >
				<TH colspan="4" align="center">超级权限修改</TH>
			</TR>
			<tr BGCOLOR="#ffffff">
				<TD class="column" colspan="1">权限名称</TD>
				<TD><input type="text" id="auth_name" name="auth_name" class=bk size="40"
				    value='<s:property value="srMap.auth_name"/>' >&nbsp;<font color="#FF0000">*</font>&nbsp;&nbsp;</TD>
				<TD class="column" colspan="1">权限简码</TD>
				<TD><input type="text" id="auth_code" name="auth_code" class=bk size="40" onblur="checkName();"
				            value='<s:property value="srMap.auth_code"/>'>
						&nbsp;<font color="#FF0000">*</font>&nbsp;&nbsp;<span id="isExsist" style="color:red"></span>
						</TD>
			</tr>
			<tr BGCOLOR="#ffffff">
				<TD class="column" colspan="1">描述</TD>
				<td colspan="1"><input type="text" id="auth_desc" name="auth_desc" class=bk size="40"
								value='<s:property value="srMap.auth_desc"/>'></td>
				<TD class=column colspan="1">控制方式</TD>
				<td><select id="relation_type" name="relation_type" onchange="chooseType();">
					<option value="-1">请选择</option>
					<s:if test='srMap.relation_type=="0"'>
						<option value="0" selected="selected">用户控制</option>
						<option value="1">角色控制</option>
					</s:if>
					<s:else>
						<option value="0">用户控制</option>
						<option value="1" selected="selected">角色控制</option>
					</s:else>
				</select></td>
			</tr>		
			<tr>
				<TD class=column colspan="1">用户角色列表</TD>
				<!--- tree area --->
				<td colspan="3" width="500px"  class="jive-setup-category-header" bgcolor=white>
					<div id="treebox" 
						style="	width:500px; 
								height:300px;
								background-color:#f5f5f5;
								border :1px solid Silver;">
						</div>
				</td>
			</tr>
			<tr  bgcolor="#ffffff">
				<td  class=foot colspan=4 align=right>
					<button name="button" onclick="updateRole()">保  存</button>&nbsp;&nbsp;
					<button name="button" value=' 重 置 ' onclick="reset()" > 重 置 </button>&nbsp;&nbsp;
					<button name="button" value=' 返回 ' onclick="backList()"> 返 回 </button>&nbsp;&nbsp;
					<input type=hidden id="tree_id_query" name="tree_id_query" value='<s:property value="srMap.tree_id_query"/>'>
					<input type=hidden name="action" value="update">	
				</td>
			</tr>
		</table>
	</TD>
</TR>
<TR>
	<TD>
		<iframe name="actionframe" id="actionframe" frameborder="0" width="100%" height="0"></iframe>
	</TD>
</TR>
</TABLE>
</form> 
<%@ include file="../../foot.jsp"%>
<script type="text/javascript">
function checkName(){
	 //$("div[@id='isExsist']").html("");
	 $("#isExsist").html("");
	  var shortName = $("#auth_code").val();
	  var url = "<s:url value='/gtms/system/superRoleManage!checkName.action'/>"; 
		$.post(url,{
			auth_code : shortName
		},function(ajax){
			if(ajax="0"){
				$("#isExsist").html("权限简码已存在，请重新命名！");
			}
		});
}
function updateRole(){
	   var auth_name = $("#auth_name").val();
	   var auth_code = $("#auth_code").val();
	   var auth_desc = $("#auth_desc").val();
	   var relation_type = $("#relation_type").val();
	   if(""==auth_name){
		   alert("请输入权限名称");
		   $("#auth_name").focus();
		   return false;
	   }
	   if(""==auth_code){
		   alert("请输入权限简码");
		   $("#auth_code").focus();
	   }
	   var tree_id_query = tree.getAllCheckedBranches();
	   var url = "<s:url value='/gtms/system/superRoleManage!updateSuperRole.action'/>";
	   $.post(url,{
			auth_name : auth_name,
			auth_code : auth_code,
			auth_desc : auth_desc,
			tree_id_query : tree_id_query,
			relation_type : relation_type
		},function(ajax){
			if(ajax="1"){
				alert("修改成功");
			}else{
				alert("修改失败");
			}
		});
	}
function reset(){
	$("#auth_code").val("");
	$("#auth_name").val("");
	$("#auth_desc").val("");
	$("#relation_type").val("");
}
function loadTree(){
	var relation_type = $("#relation_type").val();
	var url = "Treefromdroitdb.jsp?relation_type="+relation_type;
	tree = new dhtmlXTreeObject("treebox","100%","100%",0);
	tree.setImagePath("../../droitTree/imgs/");
	tree.enableCheckBoxes(true)
	tree.enableThreeStateCheckboxes(true);
	tree.loadXML(url);
	iTimerID = window.setInterval(OpenItem,1000);
}
function chooseType(){
	var relation_type = $("#relation_type").val();
	var url = "Treefromdroitdb.jsp?relation_type="+relation_type;
	$("#treebox").html("");
	tree = new dhtmlXTreeObject("treebox","100%","100%",0);
	tree.setImagePath("../../droitTree/imgs/");
	tree.enableCheckBoxes(true)
	//tree.enableThreeStateCheckboxes(true);
	tree.loadXML(url);
	status();
	
}
function backList(){
	window.location.href = "SuperRoleQuery.jsp";
}
</script>
