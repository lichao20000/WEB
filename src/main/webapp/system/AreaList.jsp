<%@ include file="../timelater.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>
<%
request.setCharacterEncoding("GBK");

%>
<script language=javascript>
<!--
var tree;

//-->
</SCRIPT>
<%@ include file="../head.jsp"%>
<%@ include file="../toolbar.jsp"%>
<FORM NAME="frm" METHOD=POST ACTION="UpdateAreaForm.jsp" target="childFrm">
<TABLE CELLPADDING="0" CELLSPACING="0" WIDTH="100%" BORDER="0"">
<TR>
	<TD valign=top>
	<TABLE width="90%" border=0 cellspacing=0 cellpadding=0 align="center">
		<TR height="10" nowrap><TD></TD></TR>
		<TR>
			<TD bgcolor=#999999>
				<TABLE border=0 cellspacing=1 cellpadding=2 width="100%">
				<TR bgcolor="#FFFFFF">
					<TD class=column align="right" >分配区域</TD>
					<TD bgcolor="#F1F1F1">
						<script language="JavaScript" src="./Js/tree_maker.js"></script>
						<script language="JavaScript" src="./Js/tree_res.js"></script>
						<div id="idTreeView" XMLSrc="area_xml.jsp" onselectstart='return false' style="overflow:auto;width:220;height:400;padding:2pt 2pt 2pt 2pt" > 
						<script language="JavaScript">
							init_tree();
							//freshAreaItem();
						</script>
					</div>
					</TD>
				</TR>
				<TR>
					<TH  colspan="2" align="center"><B>新建区域</B></TH>
				</TR>
				<TR bgcolor="#FFFFFF">
					<TD class=column align="right" >区域名称</TD>
					<TD><INPUT TYPE="text" NAME="area_name" maxlength=25 class=bk size=20>&nbsp;<font color="#FF0000">*</font></TD>
				</TR>
				<TR bgcolor="#FFFFFF">
					<TD class=column align="right" >区域描述</TD>
					<TD><INPUT TYPE="text" NAME="area_desc" maxlength=40 class=bk size=40></TD>
				</TR>
				<TR style="background-color:#A0C6E5" >
					<TD colspan="2">区域权限</TD>
				</TR>
				<TR>
					<TD colspan="2" align="center" class=foot>
						<INPUT TYPE="button" value="创建选择区域的子区域" class=btn  onclick=createAreaChild()>&nbsp;&nbsp;
						<INPUT TYPE="button" value="修改区域信息" class=btn  onclick=updateAreaInfo()>&nbsp;&nbsp;
						<INPUT TYPE="button" value="删除选择区域" class=btn  onclick=delAreaItems()>&nbsp;&nbsp;
						<INPUT TYPE="button" value="创建根区域" class=btn onclick=createAreaRoot()>&nbsp;&nbsp;
						<INPUT TYPE="button" value="配置涉及设备范围" class=btn onclick=matchDevicesOfArea()>&nbsp;&nbsp;
						<INPUT TYPE="reset" value="重写" name=reset class=btn>&nbsp;&nbsp;
						<INPUT TYPE="hidden" name="action" value="add">
						<INPUT TYPE="hidden" name="area_layer" value="1">
						<INPUT TYPE="hidden" name="area_rootid" value="0">
						<INPUT TYPE="hidden" name="area_pid" value="0">
						<INPUT TYPE="hidden" name="area_id" value="0">
					</TD>
				</TR>
			</TABLE>
			</TD>
		</TR>
	</TABLE>
	</TD>
</TR>
</TABLE>
</FORM>
<IFRAME name="childFrm" ID=childFrm SRC="" STYLE="display:none"></IFRAME>
<%@ include file="../foot.jsp"%>
<SCRIPT LANGUAGE="JavaScript">
<!--
function CheckForm(){
	var obj = document.frm;
	if(!IsNull(obj.role_name.value,'角色名称')){
		obj.role_name.focus();
		obj.role_name.select();
		return false;
	}
	/*
	else if(!IsNull(obj.gu_name.value,'角色用户')){
		return false;
	}
	*/
	else if(!isChecked()){
		alert("分配的权限不能为空");
		return false;
	}
	else{
		return true;
	}	
}

/**
 * 根据flag参数进行判断 
 */
function createAreaObjectResult(flag){
	if(flag){
		window.alert("节点添加成功");	
	}else{
		window.alert("节点添加失败，请确认重新尝试");	
	}
}

//创建根节点
function createAreaRoot(){
	document.all("area_layer").value = "1";
	document.all("area_rootid").value = "0";
	document.all("area_pid").value = "0";
	document.all("action").value = "add";
	
	document.frm.submit();
	document.frm.reset.click();	
}

/**
 * 创建根节点对象
 */
function createAreaRootItem(text,hint,status){
	//add root node
	var node = tree.getRoot();
	node.addChild(Tree_LAST,text,hint,status);
	node.expand(true);

	window.location.reload();
}
	
/**
 * 提示删除节点必须从最末端节点开始删除
 */
function isAreaItemExist(){
	window.alert("此节点下面存在子节点，必须从终端节点才可以删除");
}

/**
 * 根据flag参数进行判断
 */
function delAreaItemResult(flag){
	if(flag){
		window.alert("节点删除成功");	
	}else{
		window.alert("节点删除失败，请确认重新尝试");	
	}	
}



function getSelectNodeId(){
	var pobj = tree.getSelect();
	//alert(pobj.id+"\n"+pobj.text+"\n"+pobj.value)
	var v = pobj.value;
	var area_id = v.substring(0,v.indexOf(","));
	//alert(area_id);
	
	return area_id;	
}

/**
 * 提交删除请求，并在界面删除指定对象
 */
function delAreaItems(){	
	//reset value
	document.all("action").value = "del";
	//submit
	document.frm.submit();
	document.frm.reset.click();	
}

//删除区域结构树中选中节点对象	
function delAreaItem()
{
	if(tree.selectID==null)
	{
		alert('没有选择的菜单');
		return;	
	}
	if(tree.selectID)
	{
		var next=tree.getSelect().next();
		var id=next!=null?next.id:null;
		tree.del(tree.getSelect().id);
		if(id)tree.select(id);		
	}
}

/**
 * 刷新所有节点
 */
function freshAreaItem(){
	tree.refresh();
	//tree.expandAll();
}

function getValueOfString(v){
	return v.substring(0,v.indexOf(","));
}

//点击结点时变更隐藏参数内容
tree.callback_click=function my_click(nodeID){
	//alert(nodeID);
  var node = tree.getNode(nodeID);
	//alert(node.value);
	//var node_id = getSelectNodeId();
	//var pobj = tree.getSelect();
	var v = node.value;
	var area_id = v.substring(0,v.indexOf(","));
	document.all("area_id").value = area_id;
	document.all("area_name").value = node.text;
	document.all("area_desc").value = node.status;
	//alert(area_id);
	node = node.parent;
	v = node.value.toString();//alert(v.indexOf(",") != -1);
	var area_pid = (v.indexOf(",") != -1)?(v.substring(0,v.indexOf(","))):v;	
	document.all("area_pid").value = area_pid;
	
	node = tree.getNode(nodeID);
	node = node.getParentEx(2);//alert(node);
	var v = node.value.toString();
	var area_rootid = v.substring(0,v.indexOf(","));	
	document.all("area_rootid").value = area_rootid;
	
	var area_layer = getCurrentLayer();
	document.all("area_layer").value = area_layer;
	
  return true;//返回true调用默认的click,false 取消click
}

/**
 * 更新区域信息
 */
function updateAreaInfo(){
	//reset hidden param value
	document.all("action").value = "update";

	//submit
	document.frm.submit();
	document.frm.reset.click();	
}

/**
 * 刷新区域信息反馈
 */
function updateAreaInfoResult(flag){
	if(flag){
		alert("刷新区域信息成功");
	}else{
		alert("刷新区域信息失败,请重新尝试");
	}
}

/**
 * 完成对界面树结构刷新操作
 */
function resetAreaInfo(area_name,area_desc){
	var node = tree.getSelect();
	if(node){
		node.setText(area_name);
		node.hint = area_desc;
		node.status = area_desc;
	}
}

/**
 * 创建子对象
 */
function createAreaChild(){
	document.all("action").value = "addChild";
	
	//submit
	document.frm.submit();
	document.frm.reset.click();	
}

/**
 * 创建界面子节点对象
 */
function createAreaChildItem(area_name,hint,status){
	var node = tree.getSelect();
	if(node){
		node.addChild(Tree_LAST,text,hint,status);
		node.expand(true);
	}
	
	window.location.reload();
}

/**
 * 区域涉及到范围设备
 */
function matchDevicesOfArea(){
	var node = tree.getSelect();
	if(!node){
		alert("未选择配置区域设备节点");
	}
	
	var area_id = document.all("area_id").value;
	var area_pid = document.all("area_pid").value;
	var area_layer = document.all("area_layer").value;
	var page = "./AreaMatchDevices.jsp?area_id="+ area_id +"&area_pid="+ area_pid +"&area_layer="+ area_layer;
	window.open(page,"","left=20,top=20,width=700,height=400,resizable=no,scrollbars=no");	
}
//-->
</SCRIPT>
