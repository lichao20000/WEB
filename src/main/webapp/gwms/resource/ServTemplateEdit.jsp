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
	td {font-family:"΢���ź�";}
	.text{font-family:"΢���ź�";}
</style>
  <script type="text/javascript" src="../../zTree_v3/js/jquery-1.4.4.min.js"></script>
  <script type="text/javascript" src="../../zTree_v3/js/jquery.ztree.core.js"></script>
  <script type="text/javascript" src="../../zTree_v3/js/jquery.ztree.excheck.js"></script>
<SCRIPT LANGUAGE="JavaScript" SRC="../../Js/toolbars.js"></SCRIPT>
<SCRIPT LANGUAGE="JavaScript">
   var zTreeObj;
   // zTree �Ĳ������ã�����ʹ����ο� API �ĵ���setting ������⣩
   var setting = {};
   // zTree ���������ԣ�����ʹ����ο� API �ĵ���zTreeNode �ڵ�������⣩
   var tree = '<%=tree%>';
   var zNodes = JSON.parse(tree);
   $(document).ready(function(){
      zTreeObj = $.fn.zTree.init($("#treeDemo"), setting, zNodes);
      zTreeObj.expandAll(false);
  		var serv = "<s:property value='serv'/>";
  		$("#serv").val(serv);
   });

   
   /*
   		����ѡ�����нڵ㼰����Ϣ�ᱣ�浽ģ��
   */
   function saveTemplate(id){
    var treeObj = $.fn.zTree.getZTreeObj("treeDemo");
    var sNodes = treeObj.getCheckedNodes();
    if(sNodes.length==0){
    	alert("������ѡ��һ���ڵ㲢��д��Ӧ��Ϣ");
    	return;
    }
    else{
    	for(var i=0;i<sNodes.length;i++){
		   if(sNodes[i].path == '' || sNodes[i].value == ''){
			   alert(sNodes[i].name + "�ڵ�Ĳ����������ֵΪ�գ���༭������");
			   return;
		   }
	   }
    }
    if('' == $("#name").val()){
    	alert("ģ�����Ʋ���Ϊ��");
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
    	    		alert("ģ�屣��ɹ�");
    	    	}
    	    	else
    	 		{
    	 			alert("����ģ��ʱ�쳣,�������Ա��ϵ");
    	 		}
    	    	//ˢ�¸�ҳ���ԭҳ�棨��ҳ����ServTemplateList,ԭҳ��ServTempalte��
    	    	window.opener.location.reload();
	   			window.close();
    	    },
    	    error:function(){
    	    	debugger;
    	    	alert("����ģ��ʱ�쳣,�������Ա��ϵ");
    	    }
    	});
    }
   }
   
   /*
   	   ���ĳ���ڵ㣬 ����֮ǰ����Ľڵ���Ϣ�����֮ǰδ�༭���ýڵ㣬�����Ĭ��ֵ
   */
   function zTreeOnClick(event, treeId, treeNode) {
	   //��Ҷ�ӽڵ㲻�ñ༭��Ϣ
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
		����ѡ�еĽڵ���Ϣ
	*/
	function save(){
		var selectNodes = zTreeObj.getSelectedNodes();
		if(selectNodes == null || selectNodes.length==0){
			alert("����ѡ����Ҫ����Ľڵ�");
		}
		else if($("#nodeName").val()=='' || $("#nodeValue").val() ==''){
			alert("�ڵ����ơ��ڵ�ֵ����Ϊ��");
		}
		else{
			selectNodes[0].path = $("#nodeName").val();
			selectNodes[0].value = $("#nodeValue").val();
			selectNodes[0].type = $("#nodeType").val();
			selectNodes[0].priority = $("#priority").val();
			alert("�ýڵ㱣��ɹ�");
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
											<TH>�༭ģ��
											</TH>
										</TR>
									</TABLE>
								</TD>
							</TR>
							<tr style="cursor: hand; background-color: #cccccc">
								<TD class="column text" nowrap align="center">ģ������</TD>
								<TD colspan="3" class="column text" nowrap>
								<INPUT TYPE="text" style="font-size: 12px;" id="name" name="name" size=15 value="<s:property value='name'/>">&nbsp;<font color="#FF0000"></font>
								</TD>
							</tr>
							<tr style="cursor: hand; background-color: #cccccc">
								<TD class="column text" nowrap align="center" valign="top">ģ������</TD>
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
								<TD class="column text" nowrap align="center">ҵ������</TD>
								<TD colspan="3" class="column text" nowrap>
								<select name="serv" id="serv" style="color: black;font-size: 12px">
									<option value="" style="color: black;font-size: 12px">ȫ ��</option>
									<option value="10" style="color: black;font-size: 12px">�� ��</option>
									<option value="11" style="color: black;font-size: 12px">IPTV</option>
									<option value="14" style="color: black;font-size: 12px">�� ��</option>
									<option value="69" style="color: black;font-size: 12px">TR069</option>
								</select></TD>
							</tr> --%>
							<tr style="cursor: hand; background-color: #cccccc">
								<TD class="column text" nowrap align="center">����ʱ��</TD>
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
								<TD class="column text" nowrap align="center">������</TD>
								<TD colspan="3" class="column text" nowrap>
								<INPUT TYPE="text" id="nodeName" size=150>&nbsp;<font color="#FF0000"></font>
								</TD>
							</tr>
							<tr style="cursor: hand; background-color: #cccccc" id="trValue">
								<TD class="column text" nowrap align="center">����ֵ</TD>
								<TD colspan="3" class="column text" nowrap>
								<INPUT TYPE="text" id="nodeValue" size=40>&nbsp;<font color="#FF0000"></font>
								</TD>
							</tr>
							<tr style="cursor: hand; background-color: #cccccc" id="trType">
								<TD class="column text" nowrap align="center">��������</TD>
								<TD colspan="3" class="column text" nowrap><select id="nodeType" disabled="disabled">
								<option value="1">string</option>
								<option value="2">int</option>
								<option value="3">unsignedInt</option>
								<option value="4">boolean</option>
							</select></TD>
							</tr>
							<tr style="cursor: hand; background-color: #cccccc" id="trPriority">
								<TD class="column text" nowrap align="center">���ȼ�</TD>
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
							</select><span style="font-size:10px; color:red; font-family:"΢���ź�";">  ����ԽС���ȼ�Խ��</span></TD>
							</tr>
							<TR style="cursor: hand; background-color: #cccccc;height: 24px" id="trSave">
								<TD class="column text" style="vertical-align: middle;" nowrap colspan="4" ><input TYPE="button" style="height: 20px;font-size: 11px;vertical-align: middle;" value="����ڵ���Ϣ" onclick="save()"> </TD>
							</TR>
							<TR>
								<TD colspan="4" align="right" class=foot style="vertical-align: middle;"><input TYPE="button" value="�ύģ��" style="height: 22px;font-size: 13px;" onclick="saveTemplate('<s:property value="id" />')"> </TD>
							</TR>
						</TABLE>
					</TD>
				</TR>
			</TABLE>
		</TD>
	</TR>
</TABLE>
