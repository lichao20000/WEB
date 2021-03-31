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
      var serv = "<s:property value='serv'/>";
	  $("#serv").val(serv);
	  
	  var cm = $('#cm').val();
		if(cm=="1"){
			$("#service_id_tr").show();
			$("#nserv_svlan_del_tr").show();
			$("#sserv_del_tr").show();
			$("#sserv_svlan_del_tr").show();
			$("#sport_del_tr").show();
		}
		else{
			$("#service_id_tr").hide();
			$("#nserv_svlan_del_tr").hide();
			$("#sserv_del_tr").hide();
			$("#sserv_svlan_del_tr").hide();
			$("#sport_del_tr").hide();
		}
   });

   
   /*
   		����ѡ�����нڵ㼰����Ϣ�ᱣ�浽ģ��
   */
   function saveTemplate(id){
    var treeObj = $.fn.zTree.getZTreeObj("treeDemo");
    var sNodes = treeObj.getCheckedNodes();
    if(sNodes.length==0){
    	alert("������ѡ��һ���ڵ㲢��д��Ӧ��Ϣ");
    }
    else{
    	$.ajax({
    	    type:"POST",
    	    url:"<s:url value='/itms/resource/servTemplate!saveTemplate.action' />?id="+id,
    	    //data:{"checkedData":JSON.stringify(treeObj)},
    	    data: {"checkedData":JSON.stringify(sNodes),"name":$("#name").val(),"vlan":$("#vlan").val(),"describe":$("#describe").val(),"serv":$("#serv").val(),"update_time":$("#update_time").val()},
    	    success:function (data) {
    	 		alert("ajax����:"+data);
    	 		//window.location.reload();
    	    }
    	});
    	window.opener=null;
		window.open('','_self');
        window.close();
    }
   }
   
   /*
   	   ���ĳ���ڵ㣬 ����֮ǰ����Ľڵ���Ϣ�����֮ǰδ�༭���ýڵ㣬�����Ĭ��ֵ
   */
   function zTreeOnClick(event, treeId, treeNode) {
	   //��Ҷ�ӽڵ㲻�ñ༭��Ϣ(20200918 ������ҵ���·�ģ�������£�i�ڵ���Ա༭)
	   if(treeNode.name=='i'&&'1'==$('#cm').val()){
		   $("#trName").show();
		   $("#trValue").hide();
		   $("#trType").hide();
		   $("#trPriority").show();
		   $("#trSave").show();
	   }
	   else if(treeNode.children != undefined){
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
		����ڵ��ʱ��������չ�ֳ���
	*/
	function selectedNode(path){
		var node = zTreeObj.getNodeByParam("path", path);
		//zTreeObj.cancelSelectedNode();//��ȡ�����е�ѡ��״̬
		//zTreeObj.expandAll(false);
		zTreeObj.selectNode(node);//��ָ��ID�Ľڵ�ѡ��
		//zTreeObj.expandNode(node, true, false);//��ָ��ID�ڵ�չ��
	}
	
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
											<TH>ģ������
											</TH>
										</TR>
									</TABLE>
								</TD>
							</TR>
							<tr style="cursor: hand; background-color: #cccccc">
								<TD class="column text" nowrap align="center">ģ������</TD>
								<TD colspan="3" class="column text" nowrap>
								<INPUT readonly="readonly" TYPE="text" style="font-size: 12px;" id="name" name="name" size=15 value="<s:property value='name'/>">&nbsp;<font color="#FF0000"></font>
								</TD>
							</tr>
							<tr style="cursor: hand; background-color: #cccccc">
								<TD class="column text" nowrap align="center" valign="top">ģ������</TD>
								<TD colspan="3" class="column text" nowrap>
								<textarea readonly="readonly" rows="3" cols="40" id="describe" style="font-size: 12px;"><s:property value='describe'/></textarea>
								</TD>
							</tr>
							<tr style="cursor: hand; background-color: #cccccc">
								<TD class="column text" nowrap align="center">VlanId</TD>
								<TD colspan="3" class="column text" nowrap>
								<INPUT readonly="readonly" TYPE="text" style="font-size: 12px;" id="vlan" name="vlan" size=15 value="<s:property value='vlan'/>">&nbsp;<font color="#FF0000"></font>
								</TD>
							</tr>
							<tr style="cursor: hand; background-color: #cccccc">
								<TD class="column text" nowrap align="center">ҵ������</TD>
								<TD colspan="3" class="column text" nowrap>
								<select disabled="disabled" name="serv" id="serv" style="color: black;font-size: 12px">
									<option value="" style="color: black;font-size: 12px">ȫ ��</option>
									<option value="10" style="color: black;font-size: 12px">�� ��</option>
									<option value="11" style="color: black;font-size: 12px">IPTV</option>
									<option value="14" style="color: black;font-size: 12px">�� ��</option>
									<option value="69" style="color: black;font-size: 12px">TR069</option>
								</select></TD>
							</tr>
							<tr style="cursor: hand; background-color: #cccccc;display: none" >
								<TD class="column text" nowrap align="center">������ģ��</TD>
								<TD colspan="3" class="column text" nowrap>
									<INPUT TYPE="text" style="font-size: 12px;" name="cm" id="cm" size=15  value="<s:property value='cm'/>">
								</TD>
							</tr>
								
							<%-- <tr style="cursor: hand; background-color: #cccccc;display: none" id="service_id_tr" >
								<TD class="column text" nowrap align="center">ҵ�����</TD>
								<TD colspan="3" class="column text" nowrap>
								<INPUT TYPE="text" style="font-size: 12px;" id="service_id" name="service_id" size=15 value="<s:property value='service_id'/>">&nbsp;<font color="#FF0000"></font>
								</TD>
							</tr> --%>
							
							<tr style="cursor: hand; background-color: #cccccc;display: none" id="nserv_svlan_del_tr">
								<TD class="column text" nowrap align="center">������</TD>
								<TD colspan="3" class="column text" nowrap>
								<input disabled="disabled" name="nserv_svlan_del" type="radio" value="1" <s:if test="nserv_svlan_del==1">checked</s:if>/>��
								<input disabled="disabled" name="nserv_svlan_del" type="radio" value="" <s:if test="nserv_svlan_del!=1">checked</s:if>/>��
								&nbsp;&nbsp;&nbsp;&nbsp;<span style="font-size:10px; color:red; font-family:"΢���ź�";">  �ͷŲ�ͬҵ���ͻvlan��WAN����</span>
								</TD>
							</tr>
							
							<tr style="cursor: hand; background-color: #cccccc;display: none" id="sserv_del_tr">
								<TD class="column text" nowrap align="center">������</TD>
								<TD colspan="3" class="column text" nowrap>
								<input disabled="disabled" name="sserv_del" type="radio" value="1" <s:if test="sserv_del==1">checked</s:if>/>��
								<input disabled="disabled" name="sserv_del" type="radio" value="" <s:if test="sserv_del!=1">checked</s:if>/>��
								&nbsp;&nbsp;&nbsp;&nbsp;<span style="font-size:10px; color:red; font-family:"΢���ź�";">  �ͷ�ͬҵ���WAN����</span>
								</TD>
							</tr>
							
							<tr style="cursor: hand; background-color: #cccccc;display: none" id="sserv_svlan_del_tr">
								<TD class="column text" nowrap align="center">������</TD>
								<TD colspan="3" class="column text" nowrap>
								<input disabled="disabled" name="sserv_svlan_del" type="radio" value="1" <s:if test="sserv_svlan_del==1">checked</s:if>/>��
								<input disabled="disabled" name="sserv_svlan_del" type="radio" value="" <s:if test="sserv_svlan_del!=1">checked</s:if>/>��
								&nbsp;&nbsp;&nbsp;&nbsp;<span style="font-size:10px; color:red; font-family:"΢���ź�";">  �ͷ�ͬҵ���ͻvlan��WAN����</span>
								</TD>
							</tr>
							
							<tr style="cursor: hand; background-color: #cccccc;display: none" id="sport_del_tr">
								<TD class="column text" nowrap align="center">������</TD>
								<TD colspan="3" class="column text" nowrap>
								<input disabled="disabled" name="sport_del" type="radio" value="1" <s:if test="sport_del==1">checked</s:if>/>��
								<input disabled="disabled" name="sport_del" type="radio" value="" <s:if test="sport_del!=1">checked</s:if>/>��
								&nbsp;&nbsp;&nbsp;&nbsp;<span style="font-size:10px; color:red; font-family:"΢���ź�";">  �ͷų�ͻ��LAN��</span>
								</TD>
							</tr>
							
							<tr style="cursor: hand; background-color: #cccccc">
								<TD class="column text" nowrap align="center">����ʱ��</TD>
								<TD colspan="3" class="column text" nowrap>
								<INPUT readonly="readonly" TYPE="text" style="font-size: 12px;" id="update_time" name="update_time" size=15 value="<s:property value='update_time'/>">&nbsp;<font color="#FF0000"></font>
								</TD>
							</tr>
							<tr style="cursor: hand; background-color: #cccccc">
								<td colspan="4" bgcolor="#F4F4FF" bordercolor="#F4F4FF">
	   									<ul readonly="readonly" id="treeDemo" class="ztree" style="width: 100%;background-color: #F4F4FF;border: 0px;"></ul>
								</td>
							</tr>
							<tr style="cursor: hand; background-color: #cccccc" id="trName">
								<TD class="column text" nowrap align="center">������</TD>
								<TD colspan="3" class="column text" nowrap>
								<INPUT readonly="readonly" TYPE="text" id="nodeName" size=150>&nbsp;<font color="#FF0000"></font>
								</TD>
							</tr>
							<tr style="cursor: hand; background-color: #cccccc" id="trValue">
								<TD class="column text" nowrap align="center">����ֵ</TD>
								<TD colspan="3" class="column text" nowrap>
								<INPUT readonly="readonly" TYPE="text" id="nodeValue" size=40>&nbsp;<font color="#FF0000"></font>
								</TD>
							</tr>
							<tr style="cursor: hand; background-color: #cccccc" id="trType">
								<TD class="column text" nowrap align="center">��������</TD>
								<TD colspan="3" class="column text" nowrap><select disabled="disabled" id="nodeType" >
								<option value="1">string</option>
								<option value="2">int</option>
								<option value="3">unsignedInt</option>
								<option value="4">boolean</option>
							</select></TD>
							</tr>
							<tr style="cursor: hand; background-color: #cccccc" id="trPriority">
								<TD class="column text" nowrap align="center">���ȼ�</TD>
								<TD colspan="3" class="column text" nowrap><select id="priority" disabled="disabled">
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
						</TABLE>
					</TD>
				</TR>
				
				<TR>
					<TD bgcolor=#000000>
						<TABLE border=0 cellspacing=1 cellpadding=2 width="100%">
							<TR style="cursor: hand; background-color: #cccccc">
								<TD colspan="10">
									<TABLE CELLPADDING="0" CELLSPACING="0" WIDTH="100%" BORDER="0">
										<TR>
											<TH>�ڵ���ϸ</TH>
										</TR>
									</TABLE>
								</TD>
							</TR>
							<tr style="cursor: hand; background-color: #cccccc">
								<!-- <TD class="column text" nowrap align="center"></td> -->
								<TD class="column text" nowrap align="center">������</td>
								<TD class="column text" nowrap align="center">����ֵ</td>
								<TD class="column text" nowrap align="center">��������</td>
								<TD class="column text" nowrap align="center">���ȼ�</td>
							</tr>
									<s:if test="tempParam!=null">
										<s:if test="tempParam.size()>0">
											<s:iterator value="tempParam">
												<tr style="cursor: hand; background-color: #cccccc">
													<%-- <TD class="column text" nowrap align="center"><input type="radio" name="nodeDetail" onchange="selectedNode('<s:property value="path" />')"/></td> --%>
													<TD class="column text" nowrap align="left"><a href="javascript:selectedNode('<s:property value="path" />')"><s:property value="path" /></a></td>
													<TD class="column text" nowrap align="center"><s:property value="value" /></td>
													<TD class="column text" nowrap align="center"><s:property value="typeName" /></td>
													<TD class="column text" nowrap align="center"><s:property value="priority" /></td>
												</tr>
											</s:iterator>
										</s:if>
										<s:else>
											<tr style="cursor: hand; background-color: #cccccc">
												<TD class="column text" nowrap align="center" colspan="11">û�в�ѯ�������Ϣ��</td>
											</tr>
										</s:else>
									</s:if>
									<s:else>
										<tr style="cursor: hand; background-color: #cccccc">
											<TD class="column text" nowrap align="center" colspan="11">û�в�ѯ�������Ϣ��</td>
										</tr>
									</s:else>
							</tr>
						</TABLE>
					</TD>
				</TR>
			</TABLE>
		</TD>
	</TR>
</TABLE>
