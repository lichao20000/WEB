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
					<TD class=column align="right" >��������</TD>
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
					<TH  colspan="2" align="center"><B>�½�����</B></TH>
				</TR>
				<TR bgcolor="#FFFFFF">
					<TD class=column align="right" >��������</TD>
					<TD><INPUT TYPE="text" NAME="area_name" maxlength=25 class=bk size=20>&nbsp;<font color="#FF0000">*</font></TD>
				</TR>
				<TR bgcolor="#FFFFFF">
					<TD class=column align="right" >��������</TD>
					<TD><INPUT TYPE="text" NAME="area_desc" maxlength=40 class=bk size=40></TD>
				</TR>
				<TR style="background-color:#A0C6E5" >
					<TD colspan="2">����Ȩ��</TD>
				</TR>
				<TR>
					<TD colspan="2" align="center" class=foot>
						<INPUT TYPE="button" value="����ѡ�������������" class=btn  onclick=createAreaChild()>&nbsp;&nbsp;
						<INPUT TYPE="button" value="�޸�������Ϣ" class=btn  onclick=updateAreaInfo()>&nbsp;&nbsp;
						<INPUT TYPE="button" value="ɾ��ѡ������" class=btn  onclick=delAreaItems()>&nbsp;&nbsp;
						<INPUT TYPE="button" value="����������" class=btn onclick=createAreaRoot()>&nbsp;&nbsp;
						<INPUT TYPE="button" value="�����漰�豸��Χ" class=btn onclick=matchDevicesOfArea()>&nbsp;&nbsp;
						<INPUT TYPE="reset" value="��д" name=reset class=btn>&nbsp;&nbsp;
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
	if(!IsNull(obj.role_name.value,'��ɫ����')){
		obj.role_name.focus();
		obj.role_name.select();
		return false;
	}
	/*
	else if(!IsNull(obj.gu_name.value,'��ɫ�û�')){
		return false;
	}
	*/
	else if(!isChecked()){
		alert("�����Ȩ�޲���Ϊ��");
		return false;
	}
	else{
		return true;
	}	
}

/**
 * ����flag���������ж� 
 */
function createAreaObjectResult(flag){
	if(flag){
		window.alert("�ڵ���ӳɹ�");	
	}else{
		window.alert("�ڵ����ʧ�ܣ���ȷ�����³���");	
	}
}

//�������ڵ�
function createAreaRoot(){
	document.all("area_layer").value = "1";
	document.all("area_rootid").value = "0";
	document.all("area_pid").value = "0";
	document.all("action").value = "add";
	
	document.frm.submit();
	document.frm.reset.click();	
}

/**
 * �������ڵ����
 */
function createAreaRootItem(text,hint,status){
	//add root node
	var node = tree.getRoot();
	node.addChild(Tree_LAST,text,hint,status);
	node.expand(true);

	window.location.reload();
}
	
/**
 * ��ʾɾ���ڵ�������ĩ�˽ڵ㿪ʼɾ��
 */
function isAreaItemExist(){
	window.alert("�˽ڵ���������ӽڵ㣬������ն˽ڵ�ſ���ɾ��");
}

/**
 * ����flag���������ж�
 */
function delAreaItemResult(flag){
	if(flag){
		window.alert("�ڵ�ɾ���ɹ�");	
	}else{
		window.alert("�ڵ�ɾ��ʧ�ܣ���ȷ�����³���");	
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
 * �ύɾ�����󣬲��ڽ���ɾ��ָ������
 */
function delAreaItems(){	
	//reset value
	document.all("action").value = "del";
	//submit
	document.frm.submit();
	document.frm.reset.click();	
}

//ɾ������ṹ����ѡ�нڵ����	
function delAreaItem()
{
	if(tree.selectID==null)
	{
		alert('û��ѡ��Ĳ˵�');
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
 * ˢ�����нڵ�
 */
function freshAreaItem(){
	tree.refresh();
	//tree.expandAll();
}

function getValueOfString(v){
	return v.substring(0,v.indexOf(","));
}

//������ʱ������ز�������
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
	
  return true;//����true����Ĭ�ϵ�click,false ȡ��click
}

/**
 * ����������Ϣ
 */
function updateAreaInfo(){
	//reset hidden param value
	document.all("action").value = "update";

	//submit
	document.frm.submit();
	document.frm.reset.click();	
}

/**
 * ˢ��������Ϣ����
 */
function updateAreaInfoResult(flag){
	if(flag){
		alert("ˢ��������Ϣ�ɹ�");
	}else{
		alert("ˢ��������Ϣʧ��,�����³���");
	}
}

/**
 * ��ɶԽ������ṹˢ�²���
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
 * �����Ӷ���
 */
function createAreaChild(){
	document.all("action").value = "addChild";
	
	//submit
	document.frm.submit();
	document.frm.reset.click();	
}

/**
 * ���������ӽڵ����
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
 * �����漰����Χ�豸
 */
function matchDevicesOfArea(){
	var node = tree.getSelect();
	if(!node){
		alert("δѡ�����������豸�ڵ�");
	}
	
	var area_id = document.all("area_id").value;
	var area_pid = document.all("area_pid").value;
	var area_layer = document.all("area_layer").value;
	var page = "./AreaMatchDevices.jsp?area_id="+ area_id +"&area_pid="+ area_pid +"&area_layer="+ area_layer;
	window.open(page,"","left=20,top=20,width=700,height=400,resizable=no,scrollbars=no");	
}
//-->
</SCRIPT>
