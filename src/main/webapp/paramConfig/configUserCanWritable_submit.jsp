<%@ include file="../timelater.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>
<jsp:useBean id="paramTreeObject" scope="request" class="com.linkage.litms.paramConfig.ParamTreeObject"/>
<%@page import="com.linkage.litms.resource.DeviceAct"%>
<%@page import="java.util.HashMap"%>
<%
request.setCharacterEncoding("GBK");

String device_id = request.getParameter("device_id");
//�����ݿ��в�ѯ��òɼ��㣬�����Ǵ�ҳ����
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
var ABC_Check_Temp1='' //������������ı�ǰ��ֵ   
var ABC_Check_Msg1="����ӦΪ1λ����������"   
var ABC_Check_Msg6="��Ӧ�����ַ���"   
var ABC_Check_Msg7="��Ӧ���뺺�֣�" 
  
var tree;
function init_tree(nodeStr)
{	
	
	if(nodeStr==""){
		alert("����ʵ����ȡʧ�ܣ��뷵�����³��ԣ�");
		this.location = "configUserCanWritable.jsp";
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
		return true;//����true����Ĭ�ϵ�click,false ȡ��click
	}
}


//�����ӽڵ�
function addsub(nodeText,hint)
{
	
	
	var node=tree.add(tree.selectID,Tree_CHILD,
			Tree_LAST,nodeText,hint,"","","");
	//node.expand(true);	
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
	//var node = tree.getSelect();
	//node.expand(true);

}

function writeableConfig(){
		var node = tree.getSelect();
		if(!node){
			alert("δѡ������ڵ㣡");
			return;
		}
		
		var propertyNum = document.frm.propertyNum.value;
				
		//var para_name = document.frm.para_name.value;
		var hint_para = document.frm.hid_para_name.value;
		
		var new_param = hint_para.split(",")[0];
		
		// �Ƿ�֪ͨ
		var _notificationChange = document.all("doesInform");
		var _notificationValue = "";
		if(_notificationChange.checked){
			_notificationValue = 1;
		}else{
			_notificationValue = 0;
		}
		//֪ͨ��ʽ
		
		
		var _notification = document.all("informWay").value;
		//����֪ͨ
		var  _accessListChange = document.all("propertyInform");
			
		var _accessValue="";
		
		//������Ŀֵ
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
		//document.all("id_param").innerHTML = "<TR><TD align=left colspan=3>�����ϱ�����,���Ժ�.......</TD></TR>";
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
		//�������Ĳ������ڱ������������Ҫ����
		if(_value >= _len){
			CreateRows(objTab, _value - _len);
		}else{
			DelRows(objTab, _len - _value, _len);
		}
	}
	/**
	 *��Ӷ���
	 *objTable��������
	 *length����Ҫ����������
	 */
	function CreateRows(objTable,_length){
		for(var i=0;i<_length;i++){
			AddRow(objTable);
		}
	}
	/**
	 *ɾ������
	 *objTable��������
	 *length����Ҫɾ��������
	 */
	function DelRows(objTable,_length, _len){
		for(var i= 0; i< _length ;i++){
			objTable.deleteRow(_len - i -1);
		}
	}
	/*
	 *���һ��
	 *objTable��������
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
	  //�����ܼ�
	  if (intKey == 9 || intKey == 13){ 
	  	//alert("key");
	  	ChangeAttrNum(document.all.myTable, obj.value);
	  	return;
	  }
	  if (intKey > 64 && intKey < 91) {
	  	alert(ABC_Check_Msg6);
	  	event.returnValue = false;
	  	return;
	  }   //��������ַ�����ԣ�   
	  if(intKey == 229) {
	  	alert(ABC_Check_Msg7);
	  	event.returnValue = false;
	  	return;
	  }   //��������ַ�����ԣ�(����)
	  
	  //ABC_CheckNUM(obj);
	}
	
	function ABC_CheckNUM(obj){
		//alert("ABC");
		if(ABC_CheckNUM_A(obj.value)){   
	  	ABC_Check_Temp1 = obj.value; //������ȷ��ʷֵ   
	  }

		if (!obj.value.length) return;		//���򷵻�	
		var re1 = /^-{0,1}\d{0,1}$/;   //��ȷ���ֱ��ʽ��û��С����
		if (!obj.value.match(re1)) {
			obj.value = ABC_Check_Temp1;
			alert(ABC_Check_Msg1);
			obj.select();
		}
	}
	
	
	function ABC_CheckNUM_A(str) {   
	  if (!str.length) return; //���򷵻�   
	  var re1 = /^-{0,1}\d{0,1}$/; //��ȷ���ֱ��ʽ��û��С����   
	  return str.match(re1);  
	  } 
	  
	  //�Ƿ�֪ͨ��ѡ�򴥷��¼�
	function ChangeNotify(){
		var notifyWay = $E("informWay");
		var isNotify = $E("doesInform");
		//��ѡ��ѡ��,֪ͨ��
		if(isNotify.checked){
			notifyWay.value = "1";
		}else{//��֪ͨ
			notifyWay.value = "0";
		}
	}
	//������֪ͨ��ʽ�����¼�
	function ChangeNotifyWay(_value){
		var isNotify = $E("doesInform");
		//ѡ���˲�֪ͨ
		isNotify.checked = (_value != "0");
	}
	//����֪ͨ��ѡ�򴥷��¼�
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
			style="font-size:14px;font-family: ����">���Եȡ�����������</span></td>
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
						����ʵ������
					</td>
					<td>
						<img src="../images/attention_2.gif" width="15" height="12">
						��ѡ����豸���ò���ʵ����
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
						����ʵ���б�
					</TH>
				</TR>
				<TR bgcolor="#FFFFFF">
					<TD class=column align="right" width=100 >��������</TD>
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
					<TH nowrap colspan="4">�����û���д����</TH>
				</TR>				
				<TR bgcolor="#FFFFFF">
					<TD class=column align="right" width="10%" nowrap>����</TD>
					<TD width="40%" colspan="3">
						<INPUT TYPE="text" NAME="paraV" class="bk" size="60" readOnly>&nbsp;
					</TD>
				</TR>
				<TR bgcolor="#FFFFFF">
					<TD class=column align="right" width="10%" nowrap></TD>
					<TD width="40%">
						<INPUT TYPE="checkbox" NAME="doesInform" id="doesInform" class="bk" value="1" onclick="ChangeNotify()">�Ƿ�֪ͨ
					</TD>
					<TD class=column align="right" width="10%" nowrap>֪ͨ��ʽ</TD>
					<TD width="40%">
						<SELECT name="informWay" id="informWay" onchange="ChangeNotifyWay(this.value)">
						<OPTION value="0">��֪ͨ</OPTION>
						<OPTION value="1">����֪ͨ</OPTION>
						<OPTION value="2">����֪ͨ</OPTION>
						</SELECT>
					</TD>
				</TR>
				<TR bgcolor="#FFFFFF">
					<TD class=column align="right" width="10%" nowrap></TD>
					<TD width="40%">
						<INPUT TYPE="checkbox" NAME="propertyInform" class="bk" value="1" checked onclick="ChangeProperty()">�����Ƿ�仯
					</TD>
					<TD align="right" width="20%">������Ŀ:</TD>
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
						<INPUT TYPE="button" name="writeableconfig" id="writeableconfig" value="���ÿ�д����" class=btn  onclick="writeableConfig()">&nbsp;&nbsp;				
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

