<%@ include file="../timelater.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>
<%request.setCharacterEncoding("GBK");%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<title>dhtmlxTree sample</title>
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
	<link rel="STYLESHEET" type="text/css" href="../css/dhtmlXTree.css">
	<script  src="../js/dhtmlXCommon.js"></script>
	<script  src="../js/dhtmlXTree.js"></script>	
	
	<script>
		//tree object
		var tree;
		//xml loader to load details from database
		var xmlLoader = new dtmlXMLLoaderObject(doLoadDetails,window);
		//default label for new item
		var newItemLabel = "New Item";
		//id for new (unsaved) item
		var newItemId = "-1";
		
		//load tree on page
		function loadTree(){
			tree = new dhtmlXTreeObject("treebox","100%","100%",0);
			tree.setImagePath("../imgs/");
			//tree.enableCheckBoxes(true)
			//tree.enableThreeStateCheckboxes(true);
			tree.enableDragAndDrop(true);
			tree.setDragHandler(doOnBeforeDrop);
			tree.setOnClickHandler(doOnSelect);
			tree.loadXML("treefromdb.jsp");
			status();
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
	</script>	
</head>

<body onload="loadTree()" style="height:100%">
	<!--- block for displaying status of request on page --->
	<div id="showproc" style="display:block;">Processing...</div>
	<table width="100%">
		<tr>
			<!--- tree area --->
			<td width="250px">
				<div id="treebox" 
					style="	width:250px; 
							height:250px;
							background-color:#f5f5f5;
							border :1px solid Silver;"/>
			</td>
			<!--- details area. visible if any node selected --->
			<td id="details" 
				style="padding:5px;visibility:hidden;" valign="top">
					<form name="detailsForm" action="" target="actionframe" method="post">
						<span>Node ID:</span>
						<input  name="tree_id" type="Text" style="background-color:lightgrey;width:250px;text-align:right;" name="label" readonly="true"><br>
						<span>Parent ID:</span>
						<input  name="tree_parent_id" type="Text" style="background-color:lightgrey;width:250px;text-align:right;" name="label" readonly="true"><br>
						<span>Label:</span>
						<input name="tree_name" type="Text" maxlength="50" name="label" style="width:250px;"><br>
						<span>Descripton:</span>
						<textarea name="tree_desc" id="textarea" style="height:100px;width:250px;"></textarea><br>	
						<button type="button" onclick="saveItem()" style="width:200px;">Save</button>
					</form>
			</td>
		</tr>
		<tr>
			<!--- tree area --->
			<td width="250px">
				<button type="button" onclick="leafLink()" style="width:200px;">关联功能点</button>
			</td>
			<!--- details area. visible if any node selected --->
			<td style="padding:5px;" valign="top">
				<div id="treeItem" 
					style="	width:250px; 
							height:250px;
							background-color:#f5f5f5;
							border :1px solid Silver;"/>				
			</td>
		</tr>
	</table>
	<!--- iframe for submiting details form to --->
	<iframe name="actionframe" id="actionframe" frameborder="0" width="100%" height="0"></iframe>
	
	<h4>&copy; Scand LLC 2005</h4>	


</body>
</html>
