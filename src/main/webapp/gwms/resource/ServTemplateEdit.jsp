<%@ page contentType="text/html;charset=GBK"%>
<%@page import="com.linkage.litms.LipossGlobals"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="lk" uri="/linkage"%>
<%@ include file="../../timelater.jsp"%>
<%@ include file="../../head.jsp"%>
<%@ include file="../../toolbar.jsp"%>
<%
	request.setCharacterEncoding("gbk");
	String tree = (String)request.getAttribute("tree");
%>
<script type="text/javascript" src="<s:url value="/Js/jquery.js"/>"></script>
<SCRIPT LANGUAGE="JavaScript" SRC="../../Js/CheckForm.js"></SCRIPT>
<SCRIPT LANGUAGE="JavaScript" SRC="../../Js/Calendar.js"></SCRIPT>
<link rel="stylesheet" href="../../css/liulu.css" type="text/css">
<link rel="stylesheet" href="../../css/css_green.css" type="text/css">
<link rel="stylesheet" href="../../css/tab.css" type="text/css">
<link rel="stylesheet" href="../../css/listview.css" type="text/css">
<link rel="stylesheet" href="../../css/css_ico.css" type="text/css">
<link rel="stylesheet" href="../../css/user-defined.css" type="text/css">
  <link rel="stylesheet" href="../../zTree_v3/css/demo.css" type="text/css">
  <link rel="stylesheet" href="../../zTree_v3/css/zTreeStyle/zTreeStyle.css" type="text/css">
    <style type="text/css">
	td {font-family:"微软雅黑";}
	.text{font-family:"微软雅黑";}
</style>
  <script type="text/javascript" src="../../zTree_v3/js/jquery-1.4.4.min.js"></script>
  <script type="text/javascript" src="../../zTree_v3/js/jquery.ztree.core.js"></script>
  <script type="text/javascript" src="../../zTree_v3/js/jquery.ztree.excheck.js"></script>
<SCRIPT LANGUAGE="JavaScript" SRC="../../Js/toolbars.js"></SCRIPT>
<SCRIPT LANGUAGE="JavaScript">
   var zTreeObj;
   // zTree 的参数配置，深入使用请参考 API 文档（setting 配置详解）
   var setting = {};
   // zTree 的数据属性，深入使用请参考 API 文档（zTreeNode 节点数据详解）
   var tree = '<%=tree%>';
   var zNodes = JSON.parse(tree);
   $(document).ready(function(){
      zTreeObj = $.fn.zTree.init($("#treeDemo"), setting, zNodes);
      zTreeObj.expandAll(false);
  		var serv = "<s:property value='serv'/>";
  		$("#serv").val(serv);
   });

   
   /*
   		将勾选的所有节点及其信息提保存到模板
   */
   function saveTemplate(id){
    var treeObj = $.fn.zTree.getZTreeObj("treeDemo");
    var sNodes = treeObj.getCheckedNodes();
    if(sNodes.length==0){
    	alert("请至少选择一个节点并填写相应信息");
    	return;
    }
    else{
    	for(var i=0;i<sNodes.length;i++){
		   if(sNodes[i].path == '' || sNodes[i].value == ''){
			   alert(sNodes[i].name + "节点的参数名或参数值为空，请编辑并保存");
			   return;
		   }
	   }
    }
    if('' == $("#name").val()){
    	alert("模板名称不可为空");
    	return;
    }
    else{
    	$.ajax({
    	    type:"POST",
    	    async:false,
    	    url:"<s:url value='/gwms/resource/servTemplate!saveTemplate.action' />?id="+id,
    	    dataType:"text",
    	    data: {"checkedData":JSON.stringify(sNodes),"name":$("#name").val(),"vlan":$("#vlan").val(),"describe":$("#describe").val(),"serv":$("#serv").val(),"update_time":$("#update_time").val()},
    	    success:function (data) {
    	    	if(data==1){
    	    		alert("模板保存成功");
    	    	}
    	    	else
    	 		{
    	 			alert("保存模板时异常,请与管理员联系");
    	 		}
    	    	//刷新父页面的原页面（父页面是ServTemplateList,原页面ServTempalte）
    	    	window.opener.location.reload();
	   			window.close();
    	    },
    	    error:function(){
    	    	debugger;
    	    	alert("保存模板时异常,请与管理员联系");
    	    }
    	});
    }
   }
   
   /*
   	   点击某个节点， 回填之前保存的节点信息，如果之前未编辑过该节点，则回填默认值
   */
   function zTreeOnClick(event, treeId, treeNode) {
	   //非叶子节点不用编辑信息
	   if(treeNode.children != undefined){
		   $("#trName").hide();
		   $("#trValue").hide();
		   $("#trType").hide();
		   $("#trPriority").hide();
		   $("#trSave").hide();
		   return;
	   }
	   else{
		   $("#trName").show();
		   $("#trValue").show();
		   $("#trType").show();
		   $("#trPriority").show();
		   $("#trSave").show();
	   }
	   var path = treeNode.path;
	   debugger;
	   if(undefined == path || '' == path){
		   path = '';
		   var allNodes = treeNode.getPath();
		   for(var i=0;i<allNodes.length;i++){
			   if(path != '') path = path + ".";
			   if(allNodes[i].name == 'i' || allNodes[i].name == 'j' || allNodes[i].name == 'k'){
				   path = path + "{}";
			   }
			   else{
				   path = path + allNodes[i].name;
			   }
		   }
		   $("#nodeName").val(path);
	   }
	   else{
		   $("#nodeName").val(path);
	   }
	   if(undefined != treeNode.value && '' != treeNode.value){
		   $("#nodeValue").val(treeNode.value);
	   }
	   else{
		   $("#nodeValue").val('');
	   }
	   if(undefined != treeNode.type && '' != treeNode.type){
		   $("#nodeType").val(treeNode.type);
	   }
	   else{
		   $("#nodeType").val("1");
	   }
	   if(undefined != treeNode.priority && '' != treeNode.priority){
		   $("#priority").val(treeNode.priority);
	   }
	   else{
		   $("#priority").val("10");
	   }
	};
	
	/*
		保存选中的节点信息
	*/
	function save(){
		var selectNodes = zTreeObj.getSelectedNodes();
		if(selectNodes == null || selectNodes.length==0){
			alert("请先选中需要保存的节点");
		}
		else if($("#nodeName").val()=='' || $("#nodeValue").val() ==''){
			alert("节点名称、节点值不可为空");
		}
		else{
			selectNodes[0].path = $("#nodeName").val();
			selectNodes[0].value = $("#nodeValue").val();
			selectNodes[0].type = $("#nodeType").val();
			selectNodes[0].priority = $("#priority").val();
			alert("该节点保存成功");
		}
	}

var setting = {treeId : "",
treeObj : null,

async : {autoParam : [],
contentType : "application...",
dataFilter : null,
dataType : "text",
enable : false,
otherParam : [],
type : "post",
headers : {},
xhrFields : {},
url : ""

},

callback : {beforeAsync : null,
beforeCheck : null,
beforeClick : null,
beforeCollapse : null,
beforeDblClick : null,
beforeDrag : null,
beforeDragOpen : null,
beforeDrop : null,
beforeEditName : null,
beforeExpand : null,
beforeMouseDown : null,
beforeMouseUp : null,
beforeRemove : null,
beforeRename : null,
beforeRightClick : null,

onAsyncError : null,
onAsyncSuccess : null,
onCheck : null,
onClick : zTreeOnClick,
onCollapse : null,
onDblClick : null,
onDrag : null,
onDragMove : null,
onDrop : null,
onExpand : null,
onMouseDown : null,
onMouseUp : null,
onNodeCreated : null,
onRemove : null,
onRename : null,
onRightClick : null

},

check : 
{
autoCheckTrigger : true,
chkboxType : {"Y": "", "N": ""},
chkStyle : "checkbox",
enable : true,
nocheckInherit : false,
chkDisabledInherit : false,
radioType : "level"
},

data : {keep : {leaf : false,
parent : false

},
key : {checked : "checked",
children : "children",
isParent : "isParent",
isHidden : "isHidden",
name : "name",
title : "",
url : "url"
},
simpleData : {enable : false,
idKey : "id",
pIdKey : "pId",
rootPId : null

}

},

edit : {drag : {autoExpandTrigger : true,
isCopy : true,
isMove : true,
prev : true,
next : true,
inner : true,
borderMax : 10,
borderMin : -5,
minMoveSize : 5,
maxShowNodeNum : 5,
autoOpenTime : 500

},
editNameSelectAll : false,
enable : false,
removeTitle : "remove",
renameTitle : "rename",
showRemoveBtn : true,
showRenameBtn : true

},

view : {addDiyDom : null,
addHoverDom : null,
autoCancelSelected : true,
dblClickExpand : true,
expandSpeed : "fast",
fontCss : {},
nameIsHTML : false,
removeHoverDom : null,
selectedMulti : true,
showIcon : false,
showLine : true,
showTitle : true,
txtSelectedEnable : false

}

}

  </SCRIPT>
<TABLE border=0 cellspacing=0 cellpadding=0 width="100%" id="myTable">
	<TR>
		<TD HEIGHT=20>&nbsp;</TD>
	</TR>
	<TR>
		<TD>
			<TABLE width="100%" border=0 cellspacing=0 cellpadding=0
				align="center">
				<TR>
					<TD bgcolor=#000000>

						<TABLE border=0 cellspacing=1 cellpadding=2 width="100%">
							<TR style="cursor: hand; background-color: #cccccc">
								<TD colspan="4">
									<TABLE CELLPADDING="0" CELLSPACING="0" WIDTH="100%" BORDER="0">
										<TR>
											<TH>编辑模板
											</TH>
										</TR>
									</TABLE>
								</TD>
							</TR>
							<tr style="cursor: hand; background-color: #cccccc">
								<TD class="column text" nowrap align="center">模板名称</TD>
								<TD colspan="3" class="column text" nowrap>
								<INPUT TYPE="text" style="font-size: 12px;" id="name" name="name" size=15 value="<s:property value='name'/>">&nbsp;<font color="#FF0000"></font>
								</TD>
							</tr>
							<tr style="cursor: hand; background-color: #cccccc">
								<TD class="column text" nowrap align="center" valign="top">模板描述</TD>
								<TD colspan="3" class="column text" nowrap>
								<textarea rows="3" cols="40" id="describe" style="font-size: 12px;"><s:property value='describe'/></textarea>
								</TD>
							</tr>
							<%-- <tr style="cursor: hand; background-color: #cccccc">
								<TD class="column text" nowrap align="center">VlanId</TD>
								<TD colspan="3" class="column text" nowrap>
								<INPUT TYPE="text" style="font-size: 12px;" id="vlan" name="vlan" size=15 value="<s:property value='vlan'/>">&nbsp;<font color="#FF0000"></font>
								</TD>
							</tr>
							<tr style="cursor: hand; background-color: #cccccc">
								<TD class="column text" nowrap align="center">业务类型</TD>
								<TD colspan="3" class="column text" nowrap>
								<select name="serv" id="serv" style="color: black;font-size: 12px">
									<option value="" style="color: black;font-size: 12px">全 部</option>
									<option value="10" style="color: black;font-size: 12px">宽 带</option>
									<option value="11" style="color: black;font-size: 12px">IPTV</option>
									<option value="14" style="color: black;font-size: 12px">语 音</option>
									<option value="69" style="color: black;font-size: 12px">TR069</option>
								</select></TD>
							</tr> --%>
							<tr style="cursor: hand; background-color: #cccccc">
								<TD class="column text" nowrap align="center">更新时间</TD>
								<TD colspan="3" class="column text" nowrap>
								<INPUT TYPE="text" style="font-size: 12px;" id="update_time" name="update_time" size=15 readonly="readonly" value="<s:property value='update_time'/>">&nbsp;<font color="#FF0000"></font>
								</TD>
							</tr>
							<tr style="cursor: hand; background-color: #cccccc">
								<td colspan="4" bgcolor="#F4F4FF" bordercolor="#F4F4FF">
	   									<ul id="treeDemo" class="ztree" style="width: 100%;background-color: #F4F4FF;border: 0px;"></ul>
								</td>
							</tr>
							<tr style="cursor: hand; background-color: #cccccc" id="trName">
								<TD class="column text" nowrap align="center">参数名</TD>
								<TD colspan="3" class="column text" nowrap>
								<INPUT TYPE="text" id="nodeName" size=150>&nbsp;<font color="#FF0000"></font>
								</TD>
							</tr>
							<tr style="cursor: hand; background-color: #cccccc" id="trValue">
								<TD class="column text" nowrap align="center">参数值</TD>
								<TD colspan="3" class="column text" nowrap>
								<INPUT TYPE="text" id="nodeValue" size=40>&nbsp;<font color="#FF0000"></font>
								</TD>
							</tr>
							<tr style="cursor: hand; background-color: #cccccc" id="trType">
								<TD class="column text" nowrap align="center">参数类型</TD>
								<TD colspan="3" class="column text" nowrap><select id="nodeType" disabled="disabled">
								<option value="1">string</option>
								<option value="2">int</option>
								<option value="3">unsignedInt</option>
								<option value="4">boolean</option>
							</select></TD>
							</tr>
							<tr style="cursor: hand; background-color: #cccccc" id="trPriority">
								<TD class="column text" nowrap align="center">优先级</TD>
								<TD colspan="3" class="column text" nowrap><select id="priority" >
								<option value="10">10</option>
								<option value="9">9</option>
								<option value="8">8</option>
								<option value="7">7</option>
								<option value="6">6</option>
								<option value="5">5</option>
								<option value="4">4</option>
								<option value="3">3</option>
								<option value="2">2</option>
								<option value="1">1</option>
							</select><span style="font-size:10px; color:red; font-family:"微软雅黑";">  数字越小优先级越高</span></TD>
							</tr>
							<TR style="cursor: hand; background-color: #cccccc;height: 24px" id="trSave">
								<TD class="column text" style="vertical-align: middle;" nowrap colspan="4" ><input TYPE="button" style="height: 20px;font-size: 11px;vertical-align: middle;" value="保存节点信息" onclick="save()"> </TD>
							</TR>
							<TR>
								<TD colspan="4" align="right" class=foot style="vertical-align: middle;"><input TYPE="button" value="提交模板" style="height: 22px;font-size: 13px;" onclick="saveTemplate('<s:property value="id" />')"> </TD>
							</TR>
						</TABLE>
					</TD>
				</TR>
			</TABLE>
		</TD>
	</TR>
</TABLE>
