<%@ include file="../timelater.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>
<%@taglib prefix="s" uri="/struts-tags"%>
<jsp:useBean id="paramTreeObject" scope="request" class="com.linkage.litms.paramConfig.ParamTreeObject"/>
<%@page import="com.linkage.litms.resource.DeviceAct"%>
<%@page import="com.linkage.litms.LipossGlobals"%>
<%@page import="java.util.HashMap"%>
<%@page import="java.util.List"%>
<%


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
* read_flag �ж��Ƿ�Ϊ��ѯ���߲����˵�,1�����ѯ�˵��� 2��������˵�
*
*/
String read_flag = request.getParameter("read_flag");

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
		   this.location = "paramAllConfig.jsp?vendor_id=<%=vendor_id%>&devicetype_id=<%=devicetype_id%>&softwareversion=<%=softwareversion%>&checkType=<%=checkType%>&gw_type=<%=gw_type%>";
		}
		else
		{
		   this.location = "paramAllConfig.jsp?<%=param%>&checkType=<%=checkType%>&gw_type=<%=gw_type%>";
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

		var para_name=document.getElementById("para_name");
		var hid_para_name=document.getElementById("hid_para_name");

		var node=tree.getNode(nodeID);
		
		var text = node.text;
		
		if(area == "gs_dx" || area == "sx_lt"){
			// ������� wan�ڵ�˿���ʾ ������vlan��Ϣ����ʾ�������Ҫȥ��
			if(text.indexOf("_vlanIdMark") != -1){
				text = text.substring(0,text.indexOf("_vlanIdMark"));
			}
			
			if(node.text.indexOf(".") != -1){
				document.frm.getAllValue.disabled = false;
				document.frm.setAllValue.disabled = false;
			}else{
				document.frm.getAllValue.disabled = true;
				document.frm.setAllValue.disabled = true;
			}
			$("#path").html(node.hint.split(",")[0]);
		}
		
		
		para_name.value = text;
		hid_para_name.value=node.hint;
		
		var node=Tree_node_array[nodeID];
		
		if(node.childCount == 0 && node.text.indexOf(".") != -1){
			showMsgDlg();
			getChild();
		}else{
			if(area == "gs_dx"){
				setLeafNodesValue(node.hint.split(",")[0],"");
			}
		}
		
		var value_arr = node.hint.split(",");

		if(value_arr[1] == 1 && node.text.indexOf(".") == -1){
			document.frm.setValue.disabled = false;
			//document.frm.config_para.disabled = true;
			
		}else{
			document.frm.setValue.disabled = true;
			//document.frm.config_para.disabled = false;
		}
		
		if(node.text.indexOf(".") != -1){
			document.frm.getValue.disabled = true;
		}else{
			document.frm.getValue.disabled = false;
		}
		
		return true;//����true����Ĭ�ϵ�click,false ȡ��click
	}
}

// Ҷ�ӽڵ�
window.nodes={};
//���ڵ�para_name Ҷ�ӽڵ��б� leafNodeList = [{name,path,writable}]
function setLeafNodes(para_name,leafNodeList){
	window.nodes[para_name] = str2obj(leafNodeList);
}

// Ҷ�ӽڵ�ֵmap
window.nodesValue = {};
// ���ڵ�para_name Ҷ�ӽڵ��б� leafNodesStr = path,value,writable|path,value,writable
function setLeafNodesValue(para_name,leafNodeList){
	if(leafNodeList == ""){
		leafNodeList = window.nodesValue[para_name];
	}else{
		leafNodeList = str2obj(leafNodeList);
	}
	if(leafNodeList == undefined || leafNodeList.length == 0){
		var leafNodeHtml = 
			"<table id=\"leafNode\"  border=0 cellspacing=5 cellpadding=0 width=\"100%\">" +
				"<tr>" +
					"<td>" +
			    		"�޿����ò���" +
			    	"</td>" +
				"</tr>" +
			"</table>" ;
		$("#leafNode").replaceWith(leafNodeHtml);
		document.frm.setAllValue.disabled = true;
		return;
	}else{
		
		window.nodesValue[para_name] = leafNodeList;
		document.frm.setAllValue.disabled = false;
	
		var nodeArr;// �ڵ�����
		var nodeValueHtml;
		var leafNodeHtml = "<table id=\"leafNode\" border=0 cellspacing=5 cellpadding=0 width=\"100%\">";
		
		var path;
		var text;
		var value;
		var writable;
		
		for (var i = 0; i < leafNodeList.length; i++) {
			
			path = leafNodeList[i]["path"];
			text = leafNodeList[i]["name"];
			value =leafNodeList[i]["value"];
			writable = leafNodeList[i]["writable"];
			
			if(writable == 1){
				nodeValueHtml="<input maxlength=255 name=\"nodeValue\" type=\"text\" size=\"20\" value=\""+ value +"\"/>";
			}else{
				nodeValueHtml="<input maxlength=255 name=\"nodeValue\" type=\"text\" size=\"20\" value=\""+ value +"\" disabled=\"disabled\" />";
			}
			
		 	leafNodeHtml +=
			"<tr>"+
		      "<td align=\"right\">" +
		       	"������" +
		      "</td>" +
		      "<td>" +
		       	"<input name=\"nodeParam\" type=\"text\" size=\"20\"  value=\""+ text +"\" disabled=\"disabled\"  style=\"color: #000\" />" +
		      "</td> " + 
		      "<td align=\"right\">" +
		       	"ֵ��" +
		      "</td>" +
		      "<td>" + 
		      	nodeValueHtml +
		      "</td>" +
			  "<td>" +
			  	"<a onclick=\"del('"+ path +"')\" style=\"cursor:pointer;\">ɾ��</a>" +
			  "</td>" +					     	
		   "</tr>" 
		}
		leafNodeHtml += "</table>"
		$("#leafNode").replaceWith(leafNodeHtml);
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
		
		var hand_name = document.frm.hand_name.value;
		var para_name = document.frm.para_name.value;
		var hint_para = document.frm.hid_para_name.value;
		
		var new_param = hint_para.split(",")[0];
		if(hand_name!='') new_param = hand_name;
		
		showMsgDlg();
		console.log("para_name="+para_name+",hint_para="+hint_para+",new_param="+new_param);
		var page="para_configForm.jsp?param_name=" 
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
		var page="para_configForm.jsp?param_name=" 
			+new_param+ "&device_id=<%=device_id%>&gw_type=<%=gw_type%>&action=setValue&para_value=" + param_value +"&refresh=" + Math.random();
		document.all("childFrm").src = page;
}

// ��ȡ����Ҷ�ӽڵ�
function getAllParam(){
	var node = tree.getSelect();
	if(!node){
		alert("δѡ������ڵ㣡");
		return;
	}
	
	var hint_para = document.frm.hid_para_name.value;
	// �ڵ�·��
	var path = hint_para.split(",")[0];

	var leafNodes = window.nodes;
	var	leafNodeList = leafNodes[path];
	// ���û��Ҷ�ӽڵ�ͷ���
	if(leafNodeList == undefined || !leafNodeList.length > 0){
		setLeafNodesValue(path,"");
		return;
	}
	// ����list��������ҳ�棨������������������ʱû���֣���ƴ��get������ʱ��̫����������û��Ӧ��ʧ�ܣ�����ֻƴ�ӱ�Ҫ�ַ�
	var leafNodeListStr = "";//name,writable|name,writable
	for (var i = 0; i < leafNodeList.length; i++) {
		leafNodeListStr += leafNodeList[i]["name"] + "," + leafNodeList[i]["writable"] + "|"
	} 
	if(leafNodeListStr.indexOf("|") != -1){
		leafNodeListStr = leafNodeListStr.substring(0, leafNodeListStr.lastIndexOf('|'));
	}
	
	showMsgDlg();
	var page="para_configForm.jsp?param_name=" 
		+path  +"&leafNodeListStr="+leafNodeListStr  + "&device_id=<%=device_id%>&gw_type=<%=gw_type%>&action=getAllValue&refresh=" + Math.random();
	document.all("childFrm").src = page;
}

// �������в���
function setAllParam(){
	
	// ���ڵ�·��
	var path = document.getElementById("path").innerText;
	// Ҷ�ӽڵ��б�
	var leafNodeList = window.nodesValue[path];
	// ����ֵ����
	var valueArr = document.frm.nodeValue;

	var nameListStr = "";
	var newValueListStr = "";
	
	for (var i = 0; i < leafNodeList.length; i++) {
		
		var name = leafNodeList[i]["name"];// �ڵ�����
		var value = leafNodeList[i]["value"];// �ڵ�ֵ
		var newValue = valueArr[i].value; // ���ĵ�ֵ 
		
		if(value != newValue)
		{
			
			newValue = newValue.replace(/\%/g,'%25');
			newValue = newValue.replace(/\&/g,'%26');
			newValue = newValue.replace(/\+/g,'%2B');
			newValue = newValue.replace(/\#/g,'%23');
			newValue = newValue.replace(/\?/g,'%3F');
			newValue = newValue.replace(/\=/g,'%3D');
			newValue = newValue.replace(/\//g,'%2F');
			newValue = newValue.replace(new RegExp(" ","gm"),"%20");

			nameListStr += name + "___";
			newValueListStr += newValue + "___";
		}
	}

	if(newValueListStr != ""){
		nameListStr = nameListStr.substring(0,nameListStr.lastIndexOf('___'));// ȥ�����һ������
		newValueListStr = newValueListStr.substring(0,newValueListStr.lastIndexOf('___'));// ȥ�����һ������
		
		showMsgDlg();
		var page="para_configForm.jsp?param_name=" 
			+path+"&leafNodeListStr="+nameListStr+ "&device_id=<%=device_id%>&gw_type=<%=gw_type%>&action=setAllValue&para_value=" + newValueListStr +"&refresh=" + Math.random();
		document.all("childFrm").src = page;
	}else{
		alert("����ֵ�ޱ䶯");
		return;
	}
	
}

function str2obj(str){
	str = str.replace(/=/g,"':'");
	str = str.replace(/,/g,"','");
	str = str.replace(/{/g,"{'");
	str = str.replace(/}/g,"'}");
	str = str.replace(new RegExp("'\\[{","g"),"[{");
	str = str.replace(/}]'/g,"}]");
	str = str.replace(/}',' {/g,"},{");
	str = str.replace(/' /g,"'");
	var obj = eval("("+str+")");
	return obj;
}


/**
 * �����Ӷ���
 */
function getChild(){

	var param = document.frm.hid_para_name.value;
	param = param.split(",")[0];
	
	if(area == "gs_dx" || area == "sx_lt"){
		var page="para_configForm.jsp?param_name=" 
			+ param + "&device_id=<%=device_id%>&gw_type=<%=gw_type%>&action=addChild2";
		document.all("childFrm").src = page;
		return;
	}
	
	var page="para_configForm.jsp?param_name=" 
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

/*
 * ɾ���ڵ�
 */
function del(hint_param){
		showMsgDlg();
		var page="para_configForm.jsp?param_name=" 
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
		var page="para_configForm.jsp?param_name=" 
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
		var page="para_configForm.jsp?param_name=" 
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
				<TR bgcolor="#FFFFFF">
					<TD class=column align="right" width=10%>��������</TD>
					<!-- ������ţ��Ҳ��������ã����Բ���Ҫ�ϲ��� -->
					<s:if test='#request.area=="gs_dx" || #request.area=="sx_lt"'>
					<TD bgcolor="#F1F1F1">
					</s:if>
					<s:else>
					<TD bgcolor="#F1F1F1" colspan="3">
					</s:else>
						<script language="JavaScript" src="../Js/tree_maker.js"></script>
						<div id="idTreeView" onselectstart='return false' style="overflow:auto;width:600;height:260;padding:2pt 2pt 2pt 2pt" >
							<script language="JavaScript">;
								init_tree('<%=nodeStr%>');
							</script>
						</div>
					</TD>
					<!-- ������ţ�����������ʾ���Ҳ� -->
					<s:if test='#request.area=="gs_dx" || #request.area=="sx_lt"'>
						<TD class=column align="right" width=5%>��������</TD>
						<TD bgcolor="#F1F1F1">
						<div style="overflow:auto;width:100%;height:260;padding:2pt 2pt 2pt 2pt">
							<table  border=0 cellspacing=10 cellpadding=0 width="100%">
								<tr>
							    	<td id = "path">
							    		InternetGatewayDevice.
							    	</td>
							    </tr>
								<tr>
									<td>
										
										<table border=0 cellspacing=5 cellpadding=0 width="100%">
											<tr>
												<td align="right">
										    		��������
										    	</td>
										    	<td>
												    <INPUT TYPE="text" NAME="para_name" id="para_name" maxlength=255 size="20"><font color="#FF0000">*</font>&nbsp;
													<input type="hidden" id="hid_para_name" name="hid_para_name" value="">
													<INPUT TYPE="button" name="getValue" id="getValue" value="��ȡ����ֵ" class=btn  onclick="getParam()">
												</td>
											</tr>
											<tr>
												<td align="right">
											       	����ֵ��
											    </td>
										    	<td>
												    <INPUT TYPE="text" NAME="para_value" maxlength=255 size="20">&nbsp;&nbsp;
												    <INPUT TYPE="button" name="setValue" id="setValue" value="���ò���ֵ" class=btn  onclick="setParam()">
												</td>
											</tr>
											<tr>
												<td>
												</td>
												<td>
													<a name="add_child" id="add_child" onclick="createParam()" style="cursor:pointer;">
														���Ӳ���ʵ��
													</a>
													<a name="upconfigvalue" id="upconfigvalue" onclick="upParamConfig()" style="cursor:pointer;">
														���������ϱ�
													</a>
													<a name="delValue" id=delValue onclick="delParam()" style="cursor:pointer;">
														ɾ������ʵ��
													</a>
													<!--<INPUT TYPE="button" name="delValue" id="delValue" value="ɾ������ʵ��" class=btn  onclick="delParam()">
													 <INPUT TYPE="button" name="add_child" id="add_child" value="���Ӳ���ʵ��" class=btn onclick="createParam()">&nbsp;&nbsp;
													<INPUT TYPE="button" name="upconfigvalue" id="upconfigvalue" value="���������ϱ�" class=btn  onclick="upParamConfig()"> -->
												</td>
											</tr>
										</table>
								    </td>
							    </tr>
							    <tr>
							    	<td>
							    		Ҷ�ӽڵ����ã��������ȡ����ֵ����ť����ȡ�������ݣ���
							    	</td>
							    </tr>
							    <tr>
							    	<td>
									  	<INPUT TYPE="button" disabled="disabled" NAME="getAllValue" value="��ȡ����ֵ" class=btn  onclick="getAllParam()">
									  	<INPUT TYPE="button" disabled="disabled" NAME="setAllValue" value="���ò���ֵ" class=btn  onclick="setAllParam()">
									</td>
							    </tr>
							    <tr>
									<td>
										<table id="leafNode"  border=0 cellspacing=5 cellpadding=0 width="100%">
											<tr>
												<td>
										    	</td>
											</tr>
										</table>
									</td>
								</tr>
							</table>
						</div>
					</TD>
				</s:if>
				</TR>
				<!-- ������Ǹ�����ţ�����ʵ��������ʾ -->
				<s:if test='#request.area!="gs_dx" && #request.area!="sx_lt"'>
					<TR>
						<TH  colspan="4" align="center"><B>���ò���ʵ��</B></TH>
					</TR>
					<TR bgcolor="#FFFFFF">
						<TD class=column align="right"  width=10%>��������</TD>
						<TD colspan="3">
							<INPUT TYPE="text" NAME="para_name" id="para_name" maxlength=255 class=bk size="50">&nbsp;<font color="#FF0000">*</font>
							<input type="hidden" id="hid_para_name" name="hid_para_name" value="">
						</TD>
					</TR>
					
					<TR bgcolor="#FFFFFF">
						<TD class=column align="right"  width=10%>�ֶ����������</TD>
						<TD colspan="3">
							<INPUT TYPE="text" NAME="hand_name" id="hand_name" maxlength=255 class=bk size="50">&nbsp;<font color="#FF0000">*</font>
						</TD>
					</TR>
					
					<TR bgcolor="#FFFFFF">
						<TD class=column align="right"  width=10%>����ֵ</TD>
						<TD colspan="3"><INPUT TYPE="text" NAME="para_value" class=bk size="80"></TD>
					</TR>
					
					<TR>
						<TD colspan="4" class=green_foot align=center>
							<INPUT TYPE="button" name="getValue" id="getValue" value="��ȡ����ֵ" class=btn  onclick="getParam()">&nbsp;&nbsp;
							<s:if test="#parameters.read_flag[0]==2">
								<INPUT TYPE="button" name="setValue" id="setValue" value="���ò���ֵ" class=btn  onclick="setParam()">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;	
								<INPUT TYPE="button" name="add_child" id="add_child" value="���Ӳ���ʵ��" class=btn onclick="createParam()">&nbsp;&nbsp;
								<INPUT TYPE="button" name="delValue" id="delValue" value="ɾ������ʵ��" class=btn  onclick="delParam()">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
								<INPUT TYPE="button" name="upconfigvalue" id="upconfigvalue" value="���������ϱ�" class=btn  onclick="upParamConfig()">&nbsp;&nbsp;
							</s:if>
						</TD>
					</TR>
				</s:if>
				
				<TR bgcolor="#FFFFFF" id="show_write_able" style="display: ">
					<TH nowrap colspan="4" onclick="showWriteableconfig()">�����û���д���Ԧ�</TH>
				</TR>
				<TR bgcolor="#FFFFFF" id="hide_write_able" style="display: none">
					<TH nowrap colspan="4" onclick="hideWriteableconfig()">�����û���д���Ԧ�</TH>
				</TR>				
				<TR bgcolor="#FFFFFF" id="write_able" style="display: none">
					<TD class=column nowrap width=10%></TD>
					<TD width="40%">
						<INPUT TYPE="checkbox" NAME="doesInform" id="doesInform" class="bk" value="1" onclick="ChangeNotify()">�Ƿ�֪ͨ
					</TD>
					<TD class=column align="right" nowrap width=10%>֪ͨ��ʽ</TD>
					<TD width="40%">
						<SELECT name="informWay" id="informWay" onchange="ChangeNotifyWay(this.value)">
						<OPTION value="0">��֪ͨ</OPTION>
						<OPTION value="1">����֪ͨ</OPTION>
						<OPTION value="2">����֪ͨ</OPTION>
						</SELECT>
					</TD>
				</TR>
				<TR bgcolor="#FFFFFF" id="write_able" style="display: none">
					<TD class=column align="right" nowrap width=10%></TD>
					<TD width="40%">
						<INPUT TYPE="checkbox" NAME="propertyInform" class="bk" value="1" checked onclick="ChangeProperty()">�����Ƿ�仯
					</TD>
					<TD align="right" width=10%>������Ŀ:</TD>
					<TD  width="40%"><INPUT type="text" name="propertyNum" onkeyup = "ABC_CheckNUM(this)" ONKEYDOWN = "checkIsNum(this)"></TD>
				</TR>
				
				<TR bgcolor="#FFFFFF"  id="write_able" style="display: none">
					<TD align="right" width=10%></TD>
					<TD colspan="3">
						<table id="myTable" width="100%" bgcolor="#999999" border=0 cellspacing=1 cellpadding=2>
							
						</table> 
					</TD>
					
				</TR>				
				<TR class=green_foot  id="write_able" style="display: none">
					<TD colspan="4" class=foot align=right>
						<INPUT TYPE="button" name="writeableconfig" id="writeableconfig" value="���ÿ�д����" class=btn  onclick="writeableConfig()">&nbsp;&nbsp;				
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

