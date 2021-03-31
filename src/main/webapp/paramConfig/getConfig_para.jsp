<%@ include file="../timelater.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>
<%@ page import="java.util.*"%>
<jsp:useBean id="paramTreeObject" scope="request"
	class="com.linkage.litms.paramConfig.ParamTreeObject" />
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
	
    //�����ݿ��в�ѯ��òɼ��㣬�����Ǵ�ҳ����
	DeviceAct act = new DeviceAct();
	HashMap deviceInfo= act.getDeviceInfo(device_id);
	gather_id = (String)deviceInfo.get("gather_id");
	
	String ior = user
			.getIor("ACS_" + gather_id, "ACS_Poa_" + gather_id);
	String paraV = "InternetGatewayDevice.";

//	Map paraMap = paramTreeObject.getParaMap(paraV, ior, device_id,gather_id);
	Map paraMap = paramTreeObject.getParaMap(paraV, device_id);
	String nodeStr = paramTreeObject.getString(paraMap);
%>
<script language="javascript">
var tree;
function init_tree(nodeStr)
{	
	
	if(nodeStr==""){
		alert("����ʵ����ȡʧ�ܣ��뷵�����³��ԣ�");
		if(<%=checkType%>==0)
		{
		   this.location = "paramInstanceadd_Config.jsp?gather_id=<%=gather_id%>&vendor_id=<%=vendor_id%>&devicetype_id=<%=devicetype_id%>&softwareversion=<%=softwareversion%>&checkType=<%=checkType%>";
		}
		else
		{
		   this.location = "paramInstanceadd_Config.jsp?<%=param%>&checkType=<%=checkType%>";
		}		
		return;		
	}
	tree=new Tree_treeView();
	//����tree_view����
	tree.showLine=true;//��ʾ�������
	tree.folderImg1="./treeimg/clsfld.gif";//Ĭ���ļ����۵�ͼ��
	tree.folderImg2="./treeimg/openfld.gif";//Ĭ���ļ���չ��ͼ��
	tree.fileImg="./treeimg/persoinfo.gif";//�ļ�ͼ��
	tree.target="_blank";//Ŀ����
	tree.folderClass1="FOLDER_1";//�ļ�����ʽ(����״̬)
	tree.folderClass2="FOLDER_2";//�ļ�����ʽ(���λ���ļ�����ʱ)
	tree.folderClass3="FOLDER_3";//�ļ�����ʽ(ѡ��״̬)
	tree.fileClass1="FILE_1";//�ļ���ʽ(����״̬)
	tree.fileClass2="FILE_2";//�ļ���ʽ(���λ���ļ���ʱ)
	tree.fileClass3="FILE_3";//�ļ���ʽ(ѡ��״̬)

	//����CSS��ʽ��ע�⣬��ʽ��TD.XXX{...},(XXX����ʽ��,��folderClass1,selectClass)
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

	//��Ӹ����node
	var node=tree.add(0,Tree_ROOT,0,"InternetGatewayDevice.");
	
	var nodelist = nodeStr.split("|");
	for(var i=0;i<nodelist.length;i++){
		var nodeText = nodelist[i].substring(nodelist[i].indexOf(".")+1,nodelist[i].indexOf(","));
		//alert(nodeText);
		var child=node.addChild(Tree_LAST,nodeText,
		nodelist[i],"");
	}
	//չ����node
	node.expand(true);
	//����¼�����,���������չ����Ӧ���Ӧ�á�
	tree.callback_expanding=function my_expand(nodeID){
		var text=tree.getNode(nodeID).text;
		return text!="cancel";//����������Ϊ 'cancel',��չ��
	}
	
	//������ʱ�����ı��������
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
		var value_arr = node.hint.split(",");

		if(value_arr[1] == 0){
			//document.frm.add_child.disabled = true;
			//document.frm.config_para.disabled = true;
			
		}else{
			//document.frm.add_child.disabled = false;
			//document.frm.config_para.disabled = false;
		}
		return true;//����true����Ĭ�ϵ�click,false ȡ��click
	}
}

function createParam(){
		var node = tree.getSelect();
		if(!node){
			alert("δѡ������ڵ㣡");
			return;
		}
		
		var para_name = document.frm.para_name.value;
		var hint_para = document.frm.hid_para_name.value;
		//var para_value = document.frm.para_value.value;
		
		var new_param = hint_para.split(",")[0] + para_name;
		for(var i=0;i<node.childCount;i++){
			if(node.child[i].text == para_name){				
				alert("����ʵ������!");
				return false;
				break;
			}
		}
		showMsgDlg();
		var page="para_configForm.jsp?param_name=" 
			+new_param+ "&gather_id=<%=gather_id%>&device_id=<%=device_id%>&ior=<%=ior%>&action=newChild";
		document.all("childFrm").src = page;
		
}
/*
 * ɾ��ѡ�еĽڵ��µ����нڵ㣡
 */
function delNodeAll(){
	var node = tree.getSelect();
	
	while(node.childCount>0){	
		var count = node.childCount - 1;
		tree.del(node.child[count].id);
	}	
}
/*
 * ɾ��ѡ�еĽڵ�
 */
function delNode(){
	var node = tree.getSelect();
	tree.del(node.id);	
}
/**
 * �����Ӷ���
 */
function getChild(){

	var param = document.frm.hid_para_name.value;
	param = param.split(",")[0];
	var page="para_configForm.jsp?param_name=" 
			+ param + "&gather_id=<%=gather_id%>&device_id=<%=device_id%>&ior=<%=ior%>&action=addChild";
	document.all("childFrm").src = page;

}

//�����ӽڵ�
function addsub(nodeText,hint)
{
	
	
	var node=tree.add(tree.selectID,Tree_CHILD,
			Tree_LAST,nodeText,hint,"","","");
}


/**
 * ����flag���������ж� 
 */
function createObjectResult(flag){
	if(flag==1){
		window.alert("�¼�����ʵ����ȡ�ɹ�");	
	}else{
		window.alert("�¼�����ʵ����ȡʧ�ܣ������³���");	
	}
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
</script>
<%@ include file="../head.jsp"%>
<%@ include file="../toolbar.jsp"%>
<div id="PendingMessage"
	style="position:absolute;z-index:3;top:240px;left:250px;width:250;height:60;border-width:1;border-style:ridge;background-color:#eeeeee;padding-top:10px;display:none">
	<center>
		<table border="0">
			<tr>
				<td valign="middle">
					<img src="../images/cursor_hourglas.gif" border="0" WIDTH="30"
						HEIGHT="30">
				</td>
				<td>
					&nbsp;&nbsp;
				</td>
				<td valign="middle">
					<span id=txtLoading style="font-size:14px;font-family: ����">���Եȡ�����������</span>
				</td>
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
				<TABLE width="90%" border=0 cellspacing=0 cellpadding=0
					align="center">
					<tr>
						<td>
							<table width="100%" height="30" border="0" cellspacing="0"
								cellpadding="0" class="green_gargtd">
								<tr>
									<td width="162" align="center" class="title_bigwhite">
										����ʵ������
									</td>
									<td>
										<img src="../images/attention_2.gif" width="15" height="12">
										��ѡ����豸��Ӳ���ʵ����
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
										����ʵ���б�
									</TH>
								</TR>
								<TR bgcolor="#FFFFFF">
									<TD class=column align="right" width=100>
										��������
									</TD>
									<TD bgcolor="#F1F1F1">
										<script language="JavaScript" src="../Js/tree_maker.js"></script>

										<div id="idTreeView" onselectstart='return false'
											style="overflow:auto;width:600;height:260;padding:2pt 2pt 2pt 2pt">
											<script language="JavaScript">;
							init_tree('<%=nodeStr%>');
						</script>

										</div>
									</TD>
								</TR>
								<TR>
									<TH colspan="2" align="center">
										<B>���Ӳ���ʵ��</B>
									</TH>
								</TR>
								<TR bgcolor="#FFFFFF">
									<TD class=column align="right">
										��������
									</TD>
									<TD>
										<INPUT TYPE="text" NAME="para_name" id="para_name"
											maxlength=255 class=bk size="40">
										&nbsp;
										<font color="#FF0000">*</font>
									</TD>
									<input type="hidden" id="hid_para_name" name="hid_para_name"
										value="">
								</TR>
								<!-- 
				<TR bgcolor="#FFFFFF">
					<TD class=column align="right" >����ֵ</TD>
					<TD><INPUT TYPE="text" NAME="para_value" maxlength=40 class=bk size=80></TD>
				</TR>
				-->
								<TR class=green_foot>
									<TD colspan="2" class=foot align=right>
										<INPUT TYPE="button" name="add_child" id="add_child"
											value="���Ӳ���ʵ��" class=btn onclick="createParam()">
										&nbsp;&nbsp;
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

