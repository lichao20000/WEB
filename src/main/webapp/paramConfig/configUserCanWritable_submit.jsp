<%@ include file="../timelater.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>
<jsp:useBean id="paramTreeObject" scope="request" class="com.linkage.litms.paramConfig.ParamTreeObject"/>
<%@page import="com.linkage.litms.resource.DeviceAct"%>
<%@page import="java.util.HashMap"%>
<%
request.setCharacterEncoding("GBK");

String device_id = request.getParameter("device_id");
//由数据库中查询获得采集点，而不是从页面中
DeviceAct act = new DeviceAct();
HashMap deviceInfo= act.getDeviceInfo(device_id);
String gather_id = (String)deviceInfo.get("gather_id");

String ior = user.getIor("ACS_" + gather_id,"ACS_Poa_" + gather_id);
String paraV = "InternetGatewayDevice.";


Map paraMap = paramTreeObject.getParaMap(paraV,ior,device_id,gather_id);
String nodeStr = paramTreeObject.getString(paraMap);
%>
<script language="javascript">


var $E = document.getElementById; 
var ABC_Check_Temp1='' //用来存放输入框改变前的值   
var ABC_Check_Msg1="输入应为1位以内整数！"   
var ABC_Check_Msg6="不应输入字符！"   
var ABC_Check_Msg7="不应输入汉字！" 
  
var tree;
function init_tree(nodeStr)
{	
	
	if(nodeStr==""){
		alert("参数实例获取失败，请返回重新尝试！");
		this.location = "configUserCanWritable.jsp";
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
	
		var para_name=document.getElementById("paraV");
		var hid_para_name=document.getElementById("hid_para_name");

		var node=tree.getNode(nodeID);
		para_name.value=node.text;
		hid_para_name.value=node.hint;
		//var node=Tree_node_array[nodeID];
		if(node.childCount == 0 && node.text.indexOf(".") != -1){
			showMsgDlg();
			getChild();
			//node.expand(true);
		}
		
		if(node.text.indexOf(".") != -1){
			document.frm.writeableconfig.disabled = true;
		}else{
			document.frm.writeableconfig.disabled = false;
		}
		return true;//返回true调用默认的click,false 取消click
	}
}


//增加子节点
function addsub(nodeText,hint)
{
	
	
	var node=tree.add(tree.selectID,Tree_CHILD,
			Tree_LAST,nodeText,hint,"","","");
	//node.expand(true);	
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
	//var node = tree.getSelect();
	//node.expand(true);

}

function writeableConfig(){
		var node = tree.getSelect();
		if(!node){
			alert("未选择参数节点！");
			return;
		}
		
		var propertyNum = document.frm.propertyNum.value;
				
		//var para_name = document.frm.para_name.value;
		var hint_para = document.frm.hid_para_name.value;
		
		var new_param = hint_para.split(",")[0];
		
		// 是否通知
		var _notificationChange = document.all("doesInform");
		var _notificationValue = "";
		if(_notificationChange.checked){
			_notificationValue = 1;
		}else{
			_notificationValue = 0;
		}
		//通知方式
		
		
		var _notification = document.all("informWay").value;
		//属性通知
		var  _accessListChange = document.all("propertyInform");
			
		var _accessValue="";
		
		//属性数目值
		var _accessList = "";
		if(_accessListChange.checked){
			_accessValue = 1;
			if(propertyNum>0){
				var _obj =  document.all("attrName");//document.all("attrName"));
				if(typeof(_obj.length) == "undefined"){
					_accessList += _obj.value;
				}else{
					for(var i=0;i<_obj.length;i++){
						_accessList +=_obj[i].value + ","
					}
				}				
			}
		}else{
			_accessValue = 0;
		}
		showMsgDlg();
		//document.all("id_param").innerHTML = "<TR><TD align=left colspan=3>正在上报属性,请稍后.......</TD></TR>";
		var page="para_configForm.jsp?param_name=" 
			+new_param+ "&gather_id=<%=gather_id%>&device_id=<%=device_id%>&ior=<%=ior%>"
			+ "&_notificationValue=" +_notificationValue+"&_notification="+ _notification
			+ "&_accessValue=" + _accessValue + "&_accessList="+_accessList+ "&action=writeableConfig&refresh=" + Math.random();
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
function closeMsgDlg(){
	PendingMessage.style.display="none";
}


///////////////////////////////////////////

	function ChangeAttrNum(_objTable,_value){
		var objTab = _objTable;
		var _len = objTab.rows.length;
		//如果输入的参数大于表格行数，则需要增加
		if(_value >= _len){
			CreateRows(objTab, _value - _len);
		}else{
			DelRows(objTab, _len - _value, _len);
		}
	}
	/**
	 *添加多行
	 *objTable：表格对象
	 *length：需要创建的行数
	 */
	function CreateRows(objTable,_length){
		for(var i=0;i<_length;i++){
			AddRow(objTable);
		}
	}
	/**
	 *删除多行
	 *objTable：表格对象
	 *length：需要删除的行数
	 */
	function DelRows(objTable,_length, _len){
		for(var i= 0; i< _length ;i++){
			objTable.deleteRow(_len - i -1);
		}
	}
	/*
	 *添加一行
	 *objTable：表格对象
	 */
	function AddRow(objTable){
		var oRow = objTable.insertRow();
		oRow.bgColor = "#FFFFFF";
		oRow.align = "left";
		var index = objTable.rows.length;
		var oCell = oRow.insertCell();
		oCell.innerHTML = index;
		oCell = oRow.insertCell();
		oCell.innerHTML = "<input type=\"text\" value=\"Subscriber\" name=\"attrName\" id=\"attrName\" width=\"80%\">";
	} 

	function checkIsNum(obj){
	  var intKey = event.keyCode;
	  //允许功能键
	  if (intKey == 9 || intKey == 13){ 
	  	//alert("key");
	  	ChangeAttrNum(document.all.myTable, obj.value);
	  	return;
	  }
	  if (intKey > 64 && intKey < 91) {
	  	alert(ABC_Check_Msg6);
	  	event.returnValue = false;
	  	return;
	  }   //如果输入字符则忽略！   
	  if(intKey == 229) {
	  	alert(ABC_Check_Msg7);
	  	event.returnValue = false;
	  	return;
	  }   //如果输入字符则忽略！(中文)
	  
	  //ABC_CheckNUM(obj);
	}
	
	function ABC_CheckNUM(obj){
		//alert("ABC");
		if(ABC_CheckNUM_A(obj.value)){   
	  	ABC_Check_Temp1 = obj.value; //保存正确历史值   
	  }

		if (!obj.value.length) return;		//空则返回	
		var re1 = /^-{0,1}\d{0,1}$/;   //正确数字表达式（没有小数）
		if (!obj.value.match(re1)) {
			obj.value = ABC_Check_Temp1;
			alert(ABC_Check_Msg1);
			obj.select();
		}
	}
	
	
	function ABC_CheckNUM_A(str) {   
	  if (!str.length) return; //空则返回   
	  var re1 = /^-{0,1}\d{0,1}$/; //正确数字表达式（没有小数）   
	  return str.match(re1);  
	  } 
	  
	  //是否通知复选框触发事件
	function ChangeNotify(){
		var notifyWay = $E("informWay");
		var isNotify = $E("doesInform");
		//复选框选中,通知。
		if(isNotify.checked){
			notifyWay.value = "1";
		}else{//不通知
			notifyWay.value = "0";
		}
	}
	//下拉框通知方式触发事件
	function ChangeNotifyWay(_value){
		var isNotify = $E("doesInform");
		//选择了不通知
		isNotify.checked = (_value != "0");
	}
	//属性通知复选框触发事件
	function ChangeProperty() {
		var propertyInform = $E("propertyInform");
		if (propertyInform.checked) {
			document.all.propertyNum.disabled = 0;
			document.all.myTable.disabled = 0;
		} else {
			document.all.propertyNum.disabled = 1;
			document.all.myTable.disabled = 1;
		}
		
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
<TABLE CELLPADDING="0" CELLSPACING="0" WIDTH="95%" BORDER="0"">
<TR>
	<TD HEIGHT=20>
		&nbsp;
	</TD>
</TR>
<TR>
	<TD valign=top colspan="3">
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
					<TH colspan="4" align="center">
						参数实例列表
					</TH>
				</TR>
				<TR bgcolor="#FFFFFF">
					<TD class=column align="right" width=100 >参数名称</TD>
					<TD bgcolor="#F1F1F1" colspan="3">
						<script language="JavaScript" src="../Js/tree_maker.js"></script>
						
						<div id="idTreeView" onselectstart='return false' style="overflow:auto;width:600;height:260;padding:2pt 2pt 2pt 2pt" >
						<script language="JavaScript">;
							init_tree('<%=nodeStr%>');
						</script>
						
					</div>
					</TD>
				</TR>
				<TR bgcolor="#FFFFFF">
					<TH nowrap colspan="4">配置用户可写属性</TH>
				</TR>				
				<TR bgcolor="#FFFFFF">
					<TD class=column align="right" width="10%" nowrap>参数</TD>
					<TD width="40%" colspan="3">
						<INPUT TYPE="text" NAME="paraV" class="bk" size="60" readOnly>&nbsp;
					</TD>
				</TR>
				<TR bgcolor="#FFFFFF">
					<TD class=column align="right" width="10%" nowrap></TD>
					<TD width="40%">
						<INPUT TYPE="checkbox" NAME="doesInform" id="doesInform" class="bk" value="1" onclick="ChangeNotify()">是否通知
					</TD>
					<TD class=column align="right" width="10%" nowrap>通知方式</TD>
					<TD width="40%">
						<SELECT name="informWay" id="informWay" onchange="ChangeNotifyWay(this.value)">
						<OPTION value="0">不通知</OPTION>
						<OPTION value="1">被动通知</OPTION>
						<OPTION value="2">主动通知</OPTION>
						</SELECT>
					</TD>
				</TR>
				<TR bgcolor="#FFFFFF">
					<TD class=column align="right" width="10%" nowrap></TD>
					<TD width="40%">
						<INPUT TYPE="checkbox" NAME="propertyInform" class="bk" value="1" checked onclick="ChangeProperty()">属性是否变化
					</TD>
					<TD align="right" width="20%">属性数目:</TD>
					<TD ><INPUT type="text" name="propertyNum" onkeyup = "ABC_CheckNUM(this)" ONKEYDOWN = "checkIsNum(this)"></TD>
				</TR>
				
				<TR bgcolor="#FFFFFF">
					<TD align="right" width="20%"></TD>
					<TD colspan="3">
						<table id="myTable" width="100%" bgcolor="#999999" border=0 cellspacing=1 cellpadding=2>
							
						</table> 
					</TD>
					
				</TR>				
				<TR class=green_foot >
					<TD colspan="4" class=foot align=right>
						<INPUT TYPE="button" name="writeableconfig" id="writeableconfig" value="配置可写属性" class=btn  onclick="writeableConfig()">&nbsp;&nbsp;				
						<input type="hidden" name="hid_para_name" value="">
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

