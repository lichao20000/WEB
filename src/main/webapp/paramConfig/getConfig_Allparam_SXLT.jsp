<%@ include file="../timelater.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>
<%@taglib prefix="s" uri="/struts-tags"%>
<jsp:useBean id="paramTreeObject" scope="request" class="com.linkage.litms.paramConfig.ParamTreeObject"/>
<%@page import="com.linkage.litms.resource.DeviceAct"%>
<%@page import="com.linkage.litms.LipossGlobals"%>
<%@page import="java.util.HashMap"%>
<%@page import="java.util.List"%>
<%@ page import="org.apache.log4j.Logger" %>
<%

Logger log = Logger.getLogger(this.getClass());

request.setCharacterEncoding("GBK");
//����
String area = LipossGlobals.getLipossProperty("InstArea.ShortName");
request.setAttribute("area", area);

//String gather_id = request.getParameter("gather_id");
String vendor_id = request.getParameter("vendor_id");
String devicetype_id = request.getParameter("devicetype_id");
String softwareversion = request.getParameter("softwareversion");
//��������ITMS/BBMS�ֶ�
String gw_type = request.getParameter("gw_type");

/**
* read_flag �ж��Ƿ�Ϊ��ѯ���߲����˵�,1������ѯ�˵��� 2���������˵�
*
*/
//String read_flag = request.getParameter("read_flag");

//begin add by w5221 �޸İ����û���ѯû�вɼ�����Ϣ�Ĳ���
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
//DeviceAct act = new DeviceAct();
//HashMap deviceInfo= act.getDeviceInfo(device_id);
//gather_id = (String)deviceInfo.get("gather_id");
//end add by w5221 �޸İ����û���ѯû�вɼ�����Ϣ�Ĳ���
//String ior = user.getIor("ACS_" + gather_id,"ACS_Poa_" + gather_id);
String paraV = "InternetGatewayDevice.";
paramTreeObject.setGwType(gw_type);
Map paraMap = paramTreeObject.getParaMap(paraV,device_id);
String nodeStr = paramTreeObject.getString(paraMap);
%>
<script language="javascript">

var tree;
var gw_type = <%=gw_type%> ;

var area = '<%=area%>' ;

var nodesStr;

function init_tree(nodeStr)
{	
	if(nodeStr==""){
		alert("����ʵ����ȡʧ�ܣ��뷵�����³��ԣ�");
		if(<%=checkType%>==0)
		{
		   this.location = "paramAllConfig_SXLT.jsp?vendor_id=<%=vendor_id%>&devicetype_id=<%=devicetype_id%>&softwareversion=<%=softwareversion%>&checkType=<%=checkType%>&gw_type=<%=gw_type%>";
		}
		else
		{
		   this.location = "paramAllConfig_SXLT.jsp?<%=param%>&checkType=<%=checkType%>&gw_type=<%=gw_type%>";
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

	//���Ӹ����node
	var node=tree.add(0,Tree_ROOT,0,"InternetGatewayDevice.");
	
	var nodelist = nodeStr.split("|");
	
	for(var i=0;i<nodelist.length;i++){
		var nodeText = nodelist[i].substring(nodelist[i].indexOf(".")+1,nodelist[i].indexOf(","));
		var child=node.addChild(Tree_LAST, nodeText, nodelist[i], "");
	}
	//չ����node
	node.expand(true);
	//�����¼�����,���������չ����Ӧ���Ӧ�á�
	tree.callback_expanding=function my_expand(nodeID){
		var text=tree.getNode(nodeID).text;
		return text!="cancel";//����������Ϊ 'cancel',��չ��
	}
	
	//������ʱ�����ı��������
	tree.callback_click=function my_click(nodeID){

		var para_name=document.getElementById("para_name");
		var hid_para_name=document.getElementById("hid_para_name");

		var node=tree.getNode(nodeID);
		
		var text = node.text;
		
		// ����·��
		$("#path").html(node.hint.split(",")[0]);
		// ������
		$("#paraName").html(text);
		para_name.value = text;
		
		hid_para_name.value=node.hint;
		
		var node=Tree_node_array[nodeID];
		
		if(node.childCount == 0 && node.text.indexOf(".") != -1){
			showMsgDlg();
			getChild();
		}
		
		var value_arr = node.hint.split(",");

		if(value_arr[1] == 1 && node.text.indexOf(".") == -1){
			document.frm.setValue.disabled = false;
			//document.frm.config_para.disabled = true;
		}else{
			document.frm.setValue.disabled = true;
			//document.frm.config_para.disabled = false;
		}
		if(node.text.indexOf(".") == -1){
			// ��ȡҶ�ӽڵ�ֵ
			getParam();
		}else{
			document.frm.para_value.value = '';
			document.frm.result_msg.value = '';
		}
		
		return true;//����true����Ĭ�ϵ�click,false ȡ��click
	}
}


//�����ӽڵ�
function addsub(nodeText,hint)
{
	var node=tree.add(tree.selectID,Tree_CHILD,
			Tree_LAST,nodeText,hint,"","","");
}

function getParam(){
	var node = tree.getSelect();
	if(!node){
		alert("δѡ������ڵ㣡");
		return;
	}
	
	var para_name = document.frm.para_name.value;
	var hint_para = document.frm.hid_para_name.value;
	
	var new_param = hint_para.split(",")[0];
	
	showMsgDlg();
	var page="para_configForm_SXLT.jsp?param_name=" 
		+new_param+ "&device_id=<%=device_id%>&gw_type=<%=gw_type%>&action=getValue&refresh=" + Math.random();
	//window.open(page,"","left=100,top=100,width=550,height=400,resizable=no,scrollbars=yes");	
	document.all("childFrm").src = page;	
}



function setParam(){
	var node = tree.getSelect();
	if(!node){
		alert("δѡ������ڵ㣡");
		return;
	}
	
	var para_name = document.frm.para_name.value;
	var hint_para = document.frm.hid_para_name.value;
	
	var new_param = hint_para.split(",")[0];
	
	var param_value = document.frm.para_value.value;
	
	param_value = param_value.replace(/\%/g,'%25');
	param_value = param_value.replace(/\&/g,'%26');
	param_value = param_value.replace(/\+/g,'%2B');
	param_value = param_value.replace(/\#/g,'%23');
	param_value = param_value.replace(/\?/g,'%3F');
	param_value = param_value.replace(/\=/g,'%3D');
	param_value = param_value.replace(/\//g,'%2F');
	param_value = param_value.replace(new RegExp(" ","gm"),"%20");
	
	showMsgDlg();
	var page="para_configForm_SXLT.jsp?param_name=" 
		+new_param+ "&device_id=<%=device_id%>&gw_type=<%=gw_type%>&action=setValue&para_value=" + param_value +"&refresh=" + Math.random();
	document.all("childFrm").src = page;
}


/**
 * �����Ӷ���
 */
function getChild(){
	var param = document.frm.hid_para_name.value;
	param = param.split(",")[0];
	
	var page="para_configForm_SXLT.jsp?param_name=" 
			+ param + "&device_id=<%=device_id%>&gw_type=<%=gw_type%>&action=addChild";
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

/**
 * �����Ϣչʾ
 */ 
function resultMsg(code,msg){
	if(code == 1){
		document.frm.result_msg.style.color='green';
	}else{
		document.frm.result_msg.style.color='red';
	}
	document.frm.result_msg.value = msg
}

/*
 * ɾ���ڵ�
 */
function del(hint_param){
		showMsgDlg();
		var page="para_configForm_SXLT.jsp?param_name=" 
			+hint_param+ "&device_id=<%=device_id%>&gw_type=<%=gw_type%>&action=delValue";
		document.all("childFrm").src = page;
}

/*
 * ɾ���ڵ�
 */

function delParam(){
	var node = tree.getSelect();
	if(!node){
		alert("δѡ������ڵ㣡");
		return;
	}
	
	var hint_para = document.frm.hid_para_name.value;
	
	var new_param = hint_para.split(",")[0];		
	showMsgDlg();
	var page="para_configForm_SXLT.jsp?param_name=" 
		+new_param+ "&device_id=<%=device_id%>&gw_type=<%=gw_type%>&action=delValue";
	document.all("childFrm").src = page;
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
		
		var new_param = hint_para.split(",")[0];
		for(var i=0;i<node.childCount;i++){
			if(node.child[i].text == para_name){				
				alert("����ʵ������!");
				return false;
				break;
			}
		}
		showMsgDlg();
		var page="para_configForm_SXLT.jsp?param_name=" 
			+new_param+ "&device_id=<%=device_id%>&gw_type=<%=gw_type%>&action=newChild";
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

function upParamConfig(){
		var node = tree.getSelect();
		if(!node){
			alert("δѡ������ڵ㣡");
			return;
		}
		
		//var para_name = document.frm.para_name.value;
		var hint_para = document.frm.hid_para_name.value;
		
		var new_param = hint_para.split(",")[0];
		//alert("here");
		//document.all("id_param").innerHTML = "<TR><TD align=left colspan=3>�����ϱ�����,���Ժ�.......</TD></TR>";
		var page="upconfigList.jsp?param_name=" 
			+new_param+ "&gw_type="+gw_type+"&device_id=<%=device_id%>&action=upValue&refresh=" + Math.random();
		window.open(page,"","left=100,top=100,width=600,height=450,resizable=yes,scrollbars=yes");
}

function showWriteableconfig(){
	$("tr[@id='write_able']").show();
	$("tr[@id='hide_write_able']").show();
	$("tr[@id='show_write_able']").hide();
}

function hideWriteableconfig(){
	$("tr[@id='write_able']").hide();
	$("tr[@id='hide_write_able']").hide();
	$("tr[@id='show_write_able']").show();
}

var ABC_Check_Temp1='' //������������ı�ǰ��ֵ   
var ABC_Check_Msg1="����ӦΪ1λ����������"   
var ABC_Check_Msg6="��Ӧ�����ַ���"   
var ABC_Check_Msg7="��Ӧ���뺺�֣�" 

function ChangeAttrNum(_objTable,_value){
	var objTab = _objTable;
	var _len = objTab.rows.length;
	//�������Ĳ������ڱ�������������Ҫ����
	if(_value >= _len){
		CreateRows(objTab, _value - _len);
	}else{
		DelRows(objTab, _len - _value, _len);
	}
}

/**
 *���Ӷ���
 *objTable���������
 *length����Ҫ����������
 */
function CreateRows(objTable,_length){
	for(var i=0;i<_length;i++){
		AddRow(objTable);
	}
}
/**
 *ɾ������
 *objTable���������
 *length����Ҫɾ��������
 */
function DelRows(objTable,_length, _len){
	for(var i= 0; i< _length ;i++){
		objTable.deleteRow(_len - i -1);
	}
}
/*
 *����һ��
 *objTable���������
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



 //�Ƿ�֪ͨ��ѡ�򴥷��¼�
function ChangeNotify(){
	var notifyWay = document.getElementById("informWay");
	var isNotify = document.getElementById("doesInform");
	//��ѡ��ѡ��,֪ͨ��
	if(isNotify.checked){
		notifyWay.value = "1";
	}else{//��֪ͨ
		notifyWay.value = "0";
	}
}

//������֪ͨ��ʽ�����¼�
function ChangeNotifyWay(_value){
	var isNotify = document.getElementById("doesInform");
	//ѡ���˲�֪ͨ
	isNotify.checked = (_value != "0");
}

//����֪ͨ��ѡ�򴥷��¼�
function ChangeProperty() {
	var propertyInform = document.getElementById("propertyInform");
	if (propertyInform.checked) {
		document.all.propertyNum.disabled = 0;
		document.all.myTable.disabled = 0;
	} else {
		document.all.propertyNum.disabled = 1;
		document.all.myTable.disabled = 1;
	}
	
}

function ABC_CheckNUM(obj){
	//alert("ABC");
	if(ABC_CheckNUM_A(obj.value)){   
  	ABC_Check_Temp1 = obj.value; //������ȷ��ʷֵ   
  }

	if (!obj.value.length) return;		//���򷵻�	
	var re1 = /^-{0,1}\d{0,1}$/;   //��ȷ���ֱ���ʽ��û��С����
	if (!obj.value.match(re1)) {
		obj.value = ABC_Check_Temp1;
		alert(ABC_Check_Msg1);
		obj.select();
	}
}

function ABC_CheckNUM_A(str) {   
	if (!str.length) return; //���򷵻�   
	var re1 = /^-{0,1}\d{0,1}$/; //��ȷ���ֱ���ʽ��û��С����   
	return str.match(re1);  
}

function checkIsNum(obj){
	var intKey = event.keyCode;
	//�������ܼ�
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
		var page="para_configForm_SXLT.jsp?param_name=" 
			+new_param+ "&device_id=<%=device_id%>" + "&gw_type=<%=gw_type%>"
			+ "&_notificationValue=" +_notificationValue+"&_notification="+ _notification
			+ "&_accessValue=" + _accessValue + "&_accessList="+_accessList+ "&action=writeableConfig&refresh=" + Math.random();
		document.all("childFrm").src = page;
}

</script>
<%@ include file="../head.jsp"%>
<%@ include file="../toolbar.jsp"%>
<SCRIPT LANGUAGE="JavaScript" SRC="<s:url value='/Js/jquery.js'/>"></SCRIPT>
<div id="PendingMessage"
	style="position:absolute;z-index:3;top:240px;left:250px;width:250;height:60;border-width:1;border-style:ridge;background-color:#eeeeee;padding-top:10px;display:none">
<center>
<table border="0">
	<tr>
		<td valign="middle"><img src="../images/cursor_hourglas.gif"
			border="0" WIDTH="30" HEIGHT="30"></td>
		<td>&nbsp;&nbsp;</td>
		<td valign="middle"><span id="txtLoading" style="font-size:14px;font-family: ����">���Եȡ�����������</span></td>
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
				<tr>
					<TD class=column align="laft">��������</TD>
					<TD class=column align="laft">�����б�</TD>
				</tr>
				<TR bgcolor="#FFFFFF">
					<TD bgcolor="#F1F1F1" height="400px">
						<script language="JavaScript" src="../Js/tree_maker2.js"></script>
						<div id="idTreeView" onselectstart='return false' style="overflow:auto;width:420;height:100%;padding:2pt 2pt 2pt 2pt" >
							<script language="JavaScript">;
								init_tree('<%=nodeStr%>');
							</script>
						</div>
					</TD>
					<TD bgcolor="#F1F1F1" valign="top">
						<div style="overflow:auto;width:100%;height:260;padding:2pt 2pt 2pt 2pt">
							<table  border=0 cellspacing=10 cellpadding=0 width="100%">
								<tr>
									<td>
										����·��:
										<span id = "path">
											InternetGatewayDevice.
										</span>
									</td>
							    </tr>
								<tr>
									<td>
										��������:
										<span id = "paraName">
										</span>
										<INPUT TYPE="hidden" NAME="para_name" id="para_name" maxlength=255 size="20">
								    	<input type="hidden" id="hid_para_name" name="hid_para_name" value="">
									</td>
							    </tr>
								<tr>
									<td>
										�� �� ֵ:
										<INPUT TYPE="text" NAME="para_value" maxlength=255 size="20">&nbsp;
										<INPUT TYPE="button" style="background-color: white;border: solid 1px #000;cursor: pointer;" 
											name="setValue" id="setValue" value="�޸Ĳ���ֵ" class=btn onclick="setParam()">
									</td>
							    </tr>
								<tr>
									<td>
										ִ�н��:
									</td>
							    </tr>
								<tr>
									<td>
										<textarea id="result_msg" name="result_msg" style="width: 400px;height: 150px;" readonly="readonly"></textarea>
									</td>
							    </tr>
							    
							</table>
						</div>
					</TD>
				</TR>
				
				<TR class=green_foot>
					<TD colspan="4" class=foot></TD>
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
