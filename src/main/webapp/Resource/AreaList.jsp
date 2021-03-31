<%@ include file="../timelater.jsp"%>
<jsp:useBean id="areaManage" scope="request" class="com.linkage.litms.system.dbimpl.AreaManageSyb"/>
<%@ page contentType="text/html;charset=GBK"%>
<%
request.setCharacterEncoding("GBK");

//String area_pid = request.getParameter("area_pid");
/*
String area_pid = String.valueOf(user.getAreaId());

ArrayList m_AreaPidList = new ArrayList();
ArrayList m_AreaIdList = new ArrayList();

m_AreaPidList.clear();
m_AreaIdList.clear();
if(user.getAccount().equals("admin")){
	m_AreaPidList.add("0");
}else{
	m_AreaPidList.add(area_pid);
}

m_AreaIdList = areaManage.getAreaIdsByAreaPid(m_AreaPidList,m_AreaIdList);
m_AreaIdList.add(String.valueOf(user.getAreaId()));

String m_AreaIdStr = m_AreaIdList.toString();
m_AreaIdList.clear();
m_AreaIdList = null;

m_AreaPidList.clear();
m_AreaPidList = null;
*/
%>
<script language=javascript>
<!--
var tree;

function selectAreaItem(){
	if(tree.getSelect()){
		var area_id = document.all("area_id").value;
		var area_name = document.all("area_name").value;
		var areaIdMore = document.all("areaIdMore").value;
		
		if(areaIdMore.indexOf("["+ area_id +",")!=-1 || areaIdMore.indexOf(", "+ area_id +"]")!=-1 || areaIdMore.indexOf(", "+ area_id +",")!=-1){
			//opener.document.all("area_name").value = area_name;
			//opener.document.all("area_id").value = area_id;
			//window.close();	
			
			return true;
		}else{
			alert("您选择区域不在权限范围以内");
		}
	}else{
		alert("请先选择区域节点，然后点击确定选择按钮");
	}
	
	return false;
}

//-->
</SCRIPT>
<%@ include file="../head.jsp"%>
<%@ include file="../toolbar.jsp"%>
<FORM NAME="frm" METHOD=POST ACTION="UpdateAreaForm.jsp" target="childFrm">
<TABLE CELLPADDING="0" CELLSPACING="0" WIDTH="100%" BORDER="0"">
<TR height="20"><TD></TD></TR>
<tr>
	<td>
	<table width="98%" height="30" border="0" cellspacing="0" cellpadding="0" class="green_gargtd" align="center">
		<tr>
			<td width="162" align="center" class="title_bigwhite">
				基础资源
			</td>
			<td>
				<img src="../images/attention_2.gif" width="15" height="12">				
			</td>
		</tr>
	</table>
	</td>
</tr>
<TR>
	<TD valign=top>
	<TABLE width="98%" border=0 cellspacing=0 cellpadding=0 align="center">
		<TR>
			<TD bgcolor=#999999>
				<TABLE border=0 cellspacing=1 cellpadding=2 width="100%">
				<TR>
					<TH  colspan="2" align="center">区域资源</TH>
				</TR>
				<TR bgcolor="#FFFFFF">
					<TD class=column align="right" width=100>分配区域</TD>
					<TD bgcolor="#F1F1F1">
						<script language="JavaScript" src="../Js/tree_maker.js"></script>
						<script language="JavaScript" src="../Js/tree_res.js"></script>
						<div id="idTreeView" XMLSrc="area_xml.jsp" onselectstart='return false' style="overflow:auto;width:520;height:260;padding:2pt 2pt 2pt 2pt" > 
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
				<TR class=green_foot>
					<TH colspan="2"><b>区域权限</b></TH>
				</TR>
				<TR>
					<TD colspan="2" class=green_foot align=right>				
						<INPUT TYPE="button" value="创建子区域" class=btn  onclick=createAreaChild()>&nbsp;&nbsp;
						<INPUT TYPE="button" value="修改区域" class=btn  onclick=updateAreaInfo()>&nbsp;&nbsp;
						<INPUT TYPE="button" value="删除区域" class=btn  onclick=delAreaItems()>
						<!-- <INPUT TYPE="button" value="创建根区域" class=btn onclick=createAreaRoot()>&nbsp;&nbsp; -->
					</TD>
				</TR>
				<TR>
					<TD colspan="2" class=green_foot align=right>
					   
						<!--<INPUT TYPE="button" value="配置主机资源" class=btn onclick=matchHostsOfArea()>&nbsp;&nbsp; -->
						<INPUT TYPE="button" value="配置设备资源" class=btn onclick=matchDevicesOfArea()>&nbsp;&nbsp;
						<INPUT TYPE="button" value="配置采集机资源" class=btn  onclick=matchGathersOfArea()>
						<INPUT TYPE="hidden" name="action" value="add">
						<INPUT TYPE="hidden" name="area_layer" value="1">
						<INPUT TYPE="hidden" name="area_rootid" value="0">
						<INPUT TYPE="hidden" name="area_pid" value="0">
						<INPUT TYPE="hidden" name="area_id" value="0">

						<div style="display:none"><INPUT TYPE="reset" name=reset id=reset class=btn></div>
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
<BR>
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
	if(flag==0){
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
		var state = window.confirm("确认删除选择区域？");
		if(state)
		{
		//reset value
		document.all("action").value = "del";
		//submit
		document.frm.submit();
		document.frm.reset.click();	
		}
		else
		{
		return false;
		}
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
	
	//判断是否为根节点
	if(v == 1){
		return true;
	}
	
	var area_id = v.substring(0,v.indexOf(","));
	document.all("area_id").value = area_id;
	document.all("area_name").value = node.text;
	document.all("area_desc").value = node.status;
	//alert(area_id);
	node = node.parent;
	v = node.value.toString();//alert(v.indexOf(",") != -1);
	
	//var area_pid = (v.indexOf(",") != -1)?(v.substring(0,v.indexOf(","))):v;	
	var area_pid = 0;
	if (v != 1){
		area_pid = (v.indexOf(",") != -1)?(v.substring(0,v.indexOf(","))):v;
	}
	
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
		
		if(Trim(document.frm.area_name.value)==""){
			alert("请输入区域名称！");
			document.frm.area_name.focus();
			return false;
		}
		var state = window.confirm("确认修改区域信息？");
		if(state)
		{
		document.all("action").value = "update";
	
		//submit
		document.frm.submit();
		document.frm.reset.click();	
		}
		else
		{
		return false;
		}
}

/**
 * 刷新区域信息反馈
 */
function updateAreaInfoResult(flag){
	if(flag == 0){
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
	//submit
		document.all("action").value = "addChild";
		
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
 * 区域涉及到主机视图中的主机
 */
function matchHostsOfArea(){
	var node = tree.getSelect();
	if(!node){
		alert("未选择配置区域设备节点");
		return;
	}
	
	var area_id = document.all("area_id").value;
	var area_pid = document.all("area_pid").value;
	var area_layer = document.all("area_layer").value;

	var page = "./AreaMatchHosts.jsp?area_id="+ area_id +"&area_pid="+ area_pid +"&area_layer="+ area_layer;
	window.open(page,"","left=20,top=20,width=740,height=450,resizable=no,scrollbars=no");
}


/**
 * 区域涉及到范围设备
 */
function matchDevicesOfArea(){
	var node = tree.getSelect();
	if(!node){
		alert("未选择配置区域设备节点");
		return;
	}
	
	var area_id = document.all("area_id").value;
	var area_pid = document.all("area_pid").value;
	var area_layer = document.all("area_layer").value;
	if(area_layer==1)
	{
	    alert("你只能配置自己的子域，请重新选择配置的域！");
	    return false;
	}	
	var page = "./AreaMatchDevices.jsp?area_id="+ area_id +"&area_pid="+ area_pid +"&area_layer="+ area_layer;
	window.open(page,"","left=20,top=20,width=740,height=450,resizable=no,scrollbars=no");
}

/**
 * 为区域配置采集机
 */
function matchGathersOfArea(){
	var node = tree.getSelect();
	if(!node){
		alert("未选择配置区域设备节点");
		return;
	}
	
	var area_id = document.all("area_id").value;
	var area_pid = document.all("area_pid").value;
	var area_layer = document.all("area_layer").value;
	var page = "./AreaMatchGathers.jsp?area_id="+ area_id +"&area_pid="+ area_pid +"&area_layer="+ area_layer;
	window.open(page,"","left=20,top=20,width=700,height=400,resizable=no,scrollbars=no");
}

//配置大客户资源信息
function matchVIPResource()
{
	var node = tree.getSelect();
	if(!node)
	{
		alert("未选择配置区域");
		return;
	}

	var area_id = document.all("area_id").value;
	var area_pid = document.all("area_pid").value;
	var area_layer = document.all("area_layer").value;
	var page = "./MatchVIPResource.jsp?area_id="+ area_id +"&area_pid="+ area_pid +"&area_layer="+ area_layer;
	window.open(page,"","left=20,top=20,width=740,height=450,resizable=no,scrollbars=no");
}
//-->
</SCRIPT>
