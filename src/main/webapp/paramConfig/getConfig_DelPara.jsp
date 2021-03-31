<%@ include file="../timelater.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>
<jsp:useBean id="paramTreeObject" scope="request" class="com.linkage.litms.paramConfig.ParamTreeObject"/>
<%@page import="com.linkage.litms.resource.DeviceAct"%>
<%@page import="java.util.HashMap"%>
<%
request.setCharacterEncoding("GBK");

String gather_id = request.getParameter("gather_id");
String vendor_id = request.getParameter("vendor_id");
String devicetype_id = request.getParameter("devicetype_id");
String softwareversion = request.getParameter("softwareversion");

String checkType = request.getParameter("checkType");
String param ="";
if("1".equals(checkType))
{
	String hguser = request.getParameter("hguser");
	if(null!=hguser&&!"".equals(hguser))
	{
		param+="&hguser="+hguser;
	}
	String telephone = request.getParameter("telephone");
	if(null!=telephone&&!"".equals(telephone))
	{
		param+="&telephone="+telephone;
	}
	param = param.substring(1,param.length());

}
String device_id = request.getParameter("device_id");

//由数据库中查询获得采集点，而不是从页面中
DeviceAct act = new DeviceAct();
HashMap deviceInfo= act.getDeviceInfo(device_id);
gather_id = (String)deviceInfo.get("gather_id");

String ior = user.getIor("ACS_" + gather_id,"ACS_Poa_" + gather_id);
String paraV = "InternetGatewayDevice.";


Map paraMap = paramTreeObject.getParaMap(paraV,ior,device_id,gather_id);
String nodeStr = paramTreeObject.getString(paraMap);

%>
<script language="javascript">
var tree;
function init_tree(nodeStr)
{	
	
	if(nodeStr==""){
		alert("参数实例获取失败，请返回重新尝试！");
		if(<%=checkType%>==0)
		{
		   this.location = "paramInstanceDel.jsp?gather_id=<%=gather_id%>&vendor_id=<%=vendor_id%>&devicetype_id=<%=devicetype_id%>&softwareversion=<%=softwareversion%>&checkType=<%=checkType%>";
		}
		else
		{
		   this.location = "paramInstanceDel.jsp?<%=param%>&checkType=<%=checkType%>";
		}
		
		return;
	}
	tree=new Tree_treeView();
	//设置tree_view属性
	tree.showLine=true;//显示结点连线
	tree.folderImg1="./treeimg/clsfld.gif";//默认文件夹折叠图标
	tree.folderImg2="./treeimg/openfld.gif";//默认文件夹展开图标
	tree.fileImg="./treeimg/persoinfo.gif";//文件图标
	tree.target="_blank";//目标框架
	tree.folderClass1="FOLDER_1";//文件夹样式(正常状态)
	tree.folderClass2="FOLDER_2";//文件夹样式(鼠标位于文件夹上时)
	tree.folderClass3="FOLDER_3";//文件夹样式(选择状态)
	tree.fileClass1="FILE_1";//文件样式(正常状态)
	tree.fileClass2="FILE_2";//文件样式(鼠标位于文件上时)
	tree.fileClass3="FILE_3";//文件样式(选择状态)

	//生成CSS样式，注意，格式是TD.XXX{...},(XXX是样式名,如folderClass1,selectClass)
	var css=
	"<style>"+
	"TD.FOLDER_1{padding:1pt 5pt  }"+
	"TD.FOLDER_2{color:red;padding:1pt 5pt}"+
	"TD.FOLDER_3{text-decoration:underline;color:brown;padding:1pt 5pt}"+
	"TD.FILE_1{padding:1pt 5pt}"+
	"TD.FILE_2{color:blue;padding:1pt 5pt}"+
	"TD.FILE_3{text-decoration:underline;color:green;padding:1pt 5pt}"+
	"</style>";
	document.write(css);//tree.refresh();

	//添加根结点node
	var node=tree.add(0,Tree_ROOT,0,"InternetGatewayDevice.");
	
	var nodelist = nodeStr.split("|");
	for(var i=0;i<nodelist.length;i++){
		var nodeText = nodelist[i].substring(nodelist[i].indexOf(".")+1,nodelist[i].indexOf(","));
		//alert(nodeText);
		var child=node.addChild(Tree_LAST,nodeText,
		nodelist[i],"");
	}
	//展开根node
	node.expand(true);
	//添加事件函数,你可以作扩展以适应你的应用。
	tree.callback_expanding=function my_expand(nodeID){
		var text=tree.getNode(nodeID).text;
		return text!="cancel";//如果结点文字为 'cancel',则不展开
	}
	
	//点击结点时更新文本框的内容
	tree.callback_click=function my_click(nodeID){
	
		//var para_name=document.getElementById("para_name");
		var hid_para_name=document.getElementById("hid_para_name");

		var node=tree.getNode(nodeID);
		//para_name.value=node.text;
		hid_para_name.value=node.hint;
		var node=Tree_node_array[nodeID];
		if(node.childCount == 0 && node.text.indexOf(".") != -1){
			showMsgDlg();
			getChild();
		}
		return true;//返回true调用默认的click,false 取消click
	}
}


//增加子节点
function addsub(nodeText,hint)
{
	
	
	var node=tree.add(tree.selectID,Tree_CHILD,
			Tree_LAST,nodeText,hint,"","","");
}

/**
 * 创建子对象
 */
function getChild(){

	var param = document.frm.hid_para_name.value;
	param = param.split(",")[0];
	var page="para_configForm.jsp?param_name=" 
			+ param + "&gather_id=<%=gather_id%>&device_id=<%=device_id%>&ior=<%=ior%>&action=addChild";
	document.all("childFrm").src = page;

}
/*
 * 删除节点
 */

function delParam(){
		var node = tree.getSelect();
		if(!node){
			alert("未选择参数节点！");
			return;
		}
		
		var hint_para = document.frm.hid_para_name.value;
		
		var new_param = hint_para.split(",")[0];		
		showMsgDlg();
		var page="para_configForm.jsp?param_name=" 
			+new_param+ "&gather_id=<%=gather_id%>&device_id=<%=device_id%>&ior=<%=ior%>&action=delValue";
		document.all("childFrm").src = page;
}

function showMsgDlg(){
	w = document.body.clientWidth;
	h = document.body.clientHeight;

	l = (w-250)/2;
	t = (h-60)/2;
	PendingMessage.style.left = l;
	PendingMessage.style.top  = t;
	PendingMessage.style.display="";
}
function delNode(){
	var node = tree.getSelect();
	tree.del(node.id);	
}
function closeMsgDlg(){
	PendingMessage.style.display="none";
}
</script>
<%@ include file="../head.jsp"%>
<%@ include file="../toolbar.jsp"%>
<div id="PendingMessage"
	style="position:absolute;z-index:3;top:240px;left:250px;width:250;height:60;border-width:1;border-style:ridge;background-color:#eeeeee;padding-top:10px;display:none">
<center>
<table border="0">
	<tr>
		<td valign="middle"><img src="../images/cursor_hourglas.gif"
			border="0" WIDTH="30" HEIGHT="30"></td>
		<td>&nbsp;&nbsp;</td>
		<td valign="middle"><span id=txtLoading
			style="font-size:14px;font-family: 宋体">请稍等······</span></td>
	</tr>
</table>
</center>
</div>
<FORM NAME="frm" METHOD=POST ACTION="" target="childFrm">
<TABLE CELLPADDING="0" CELLSPACING="0" WIDTH="100%" BORDER="0"">
<TR>
	<TD HEIGHT=20>
		&nbsp;
	</TD>
</TR>
<TR>
	<TD valign=top>
	<TABLE width="90%" border=0 cellspacing=0 cellpadding=0 align="center">
		<tr>
			<td>
			<table width="100%" height="30" border="0" cellspacing="0" cellpadding="0" class="green_gargtd">
				<tr>
					<td width="162" align="center" class="title_bigwhite">
						参数实例配置
					</td>
					<td>
						<img src="../images/attention_2.gif" width="15" height="12">
						对选择的设备配置参数实例。
					</td>
				</tr>
			</table>
			</td>
		</tr>
		<TR>
			<TD bgcolor=#999999>
				<TABLE border=0 cellspacing=1 cellpadding=2 width="100%">
				<TR>
					<TH colspan="2" align="center">
						参数实例列表
					</TH>
				</TR>
				<TR bgcolor="#FFFFFF">
					<TD class=column align="right" width=100>参数名称</TD>
					<TD bgcolor="#F1F1F1">
						<script language="JavaScript" src="../Js/tree_maker.js"></script>
						
						<div id="idTreeView" onselectstart='return false' style="overflow:auto;width:600;height:260;padding:2pt 2pt 2pt 2pt" >
						<script language="JavaScript">;
							init_tree('<%=nodeStr%>');
						</script>
						
					</div>
					</TD>
				</TR>
			
				<TR class=green_foot >
					<TD colspan="2" class=foot align=right>
						<input type="hidden" id="hid_para_name" name="hid_para_name" value="">
						<INPUT TYPE="button" name="setValue" id="setValue" value="删除参数" class=btn  onclick="delParam()">&nbsp;&nbsp;				
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

