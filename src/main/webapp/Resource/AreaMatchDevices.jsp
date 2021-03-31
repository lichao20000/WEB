<%@ include file="../timelater.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>
<%
request.setCharacterEncoding("GBK");
String area_id = request.getParameter("area_id");
String area_pid = request.getParameter("area_pid");
String area_layer = request.getParameter("area_layer");


String cur_area_id = "" + user.getAreaId();

DeviceAct act = new DeviceAct();

String strResourceList;
strResourceList = act.getDeviceTypeSelectStr(true,"","devicetype_id");
List m_ProcessesList = curUser.getUserProcesses();
String m_GaherIdStr = act.getGatherList(session,"","gather_id",true);

//��ȡ��������
String city_id = curUser.getCityId();
SelectCityFilter City = new SelectCityFilter(request);
String city_name = City.getNameByCity_id(city_id);
String strCityList =City.getSelfAndNextLayer(true,city_id,"");

//��ø�����Χ���豸
Cursor pcursor = null;
//��Դ���� 1 �����豸
String res_type = "1";

AreaManageSyb areaManage = new AreaManageSyb();
//modified by w5221 admin.com�û���ѡ�����õ��豸��tab_gw_device�е�ȥ����ѡ�����µ��豸
if(user.isAdmin()){
	pcursor = areaManage.getOtherAllDevices(res_type,area_id,m_ProcessesList,"-1",city_id,"1");
}else{
	//int m_db_i = StringUtils.getDB();
	//Ϊ��Ӧ�����ղ���
	int m_db_i = 1; //StringUtils.getDB(); 
	
	if(m_db_i == 1) { //sybase
		pcursor = areaManage.getToConfigDevice(res_type,area_id,m_ProcessesList,cur_area_id,"-1",city_id,"1");
	} else if (m_db_i == 2) { //oracle
		pcursor = areaManage.getOtherDevicesOfAreaIdNew(res_type,area_id,m_ProcessesList,cur_area_id,"-1",city_id,"1");
	}
}

Map pfield = pcursor.getNext();
//Map pfield = null;

//�����������Χ���豸
Cursor cursor = areaManage.getDevicesOfAreaId(res_type,area_id);
Map field = cursor.getNext();

%>
<%@ include file="../head.jsp"%>
<%@page import="com.linkage.litms.resource.DeviceAct"%>
<%@page import="java.util.List"%>
<%@page import="com.linkage.litms.common.filter.SelectCityFilter"%>
<%@page import="com.linkage.litms.system.dbimpl.AreaManageSyb"%>
<%@page import="com.linkage.litms.common.util.StringUtils"%>
<%@page import="java.util.Map"%>
<style>
span
{
	position:static;
	border:0;
}
</style>
<FORM NAME="frm" METHOD="post" ACTION="AreaMatchDevicesSave.jsp" onSubmit="return isNull();" target="childFrm">
<table width="750" border="0" cellspacing="0" cellpadding="0">
  <tr>
    <td bgcolor="#FFFFFF">
        <table width="100%" border=0 align="center" cellpadding="1" cellspacing="1" bgcolor="#999999" class="text">
          <TR><TH colspan='2'>������</TH></TR>
          <tr bgcolor="#FFFFFF" onmouseout="className='blue_trOut'">
			<td><div align="center">�豸IP</div></td>
            <td><input type="text" name="IPinput" value="">&nbsp;&nbsp;<input type="button" name="" value=" ���� " onClick="search()">
              ����ͨ��*��?���ж����һ���ַ���ģ����λ��</td>
          </tr>
         <!--  <tr bgcolor="#FFFFFF" onmouseout="className='blue_trOut'">
			<td><div align="center">�����豸���͹���</div></td>
            <td><div align="left"><%=strResourceList%></div></td>
          </tr>
		   <tr bgcolor="#FFFFFF" onmouseout="className='blue_trOut'">
			<td><div align="center">���ݲɼ������</div></td>
            <td><div align="left" >
				<%=m_GaherIdStr %>
            </div></td>
          </tr> -->
			<TR class=blue_trOut onmouseout="className='blue_trOut'" bgColor=#ffffff>
			   <TD class="" width=180 align="center">����</TD>
			   <TD class=""><%=strCityList%>
				<span id="my_city<%=city_id%>"></span>
				&nbsp;��ǰ:<span id=city_name><font color='red'><%=city_name%></font></span>
				&nbsp;�����¼�:<input type="radio" name="ifcontainChild" value='1' checked>��
				<input type="radio" name="ifcontainChild" value='0'>��<input type="hidden" name="hid_city_id" value="<%=city_id%>">
				</TD>
			</TR>
          <tr bgcolor="#FFFFFF" class=text>
            <td valign="top" colspan=2>
                  <table width="80%" border="0" align="center">
                    <tr> 
                      <td width="40%">
                        <div align="center">��ѡ���豸</div>
                      </td>
                      <td width="20%">&nbsp;</td>
                      <td width="40%">
                        <div align="center">��ѡ���豸</div>
                      </td>
                    </tr>                  	
                    <tr> 
                      <td width="40%"> 
						<span id=sourceDevices>
							<select name="source" size="6" style="width:300;height:300" onDblClick="addInfoMath()" multiple>
							  <%while(pfield != null){%>
							  <option value="<%=pfield.get("device_id")%>"><%=(pfield.get("oui"))+"/"+(pfield.get("device_serialnumber"))%></option>
							  <% pfield = pcursor.getNext();}pfield=null;pcursor=null;%>
							</select>
						</span>
                      </td>
                      <td width="20%"> 
                        <div align="center"> 
                          <input type="button" name="add" value="�� ��" class="btn" onclick="addInfoMath()">
                          <br>
                          <br>
                          <input type="button" name="deleteall" value="ȫ ɾ" class="btn" onclick="delAll('target')">
                          <br>
                          <br>
                          <input type="button" name="delete" value="ɾ ��" class="btn" onclick="delInfoMath()">
                        </div>
                      </td>
                      <td width="40%"> <span id=targetDevices>
                        <select name="target" size="6" style="width:300;height:300" onDblClick="delInfo()" multiple>
                          <%while(field != null){%>
                          <option value="<%=field.get("device_id")%>"><%=(String)field.get("oui")+"/"+(String)field.get("device_serialnumber")%></option>
                          <% field = cursor.getNext();}field=null;cursor=null;%>
                        </select></span>
                      </td>
                    </tr>

                  </table>
            </td>
          </tr>
          <tr>
            <td class="green_foot" colspan=2>
              <div align="center">
                <INPUT TYPE="submit" value=" �� �� " class=btn>
                <INPUT TYPE="hidden" value="<%=area_id%>" name=area_id id=area_id>
                <INPUT TYPE="hidden" value="<%=area_layer%>" name=area_layer id=area_layer>
                <INPUT TYPE="hidden" value="<%=area_pid%>" name=area_pid id=area_pid>
                <INPUT TYPE="hidden" name="add_string">
                <INPUT TYPE="hidden" name="rmv_string">
              </div>
            </td>
          </tr>
      </table>        
    </td>
  </tr>
</table>
</form>
<IFRAME name="childFrm" ID=childFrm SRC="" STYLE="display:none"></IFRAME>
<IFRAME name="childFrm1" ID=childFrm1 SRC="" STYLE="display:none"></IFRAME>
<%@page import="com.linkage.litms.common.database.Cursor"%>
<%
pfield = null;
pcursor = null;
field = null;
cursor = null;
%>

<SCRIPT LANGUAGE="JavaScript">
<!--
//var target_ = new Array();//target
//var _target = new Array();//src
var add_str = "";
var rmv_str = "";
var add_ = new Array();
var rmv_ = new Array();
var targetSel = document.frm.target;
var targetSrc = document.frm.source;
//init_();

function initStr() {
	add_str = "";
	rmv_str = "";
	for (var i=0; i<add_.length; i++) {
		add_str += add_[i];
		if (i < add_.length - 1) {
			add_str += ",";
		}
	}
	for (var i=0; i<rmv_.length; i++) {
		rmv_str += rmv_[i];
		if (i < rmv_.length - 1) {
			rmv_str += ",";
		}
	}
	document.frm.add_string.value = add_str;
	document.frm.rmv_string.value = rmv_str;
	//alert("add_str=" + add_str + "#rmv_str=" + rmv_str);
}

/*
function init_() {
	for (var i=0; i<targetSel.options.length; i++) {
		var text = targetSel.options[i].innerText;
		var textA = text.split("/");
		if (textA != null && textA.length > 0) {
			target_.push(textA[1]);
		}
	}
	for (var i=0; i<targetSrc.options.length; i++) {
		var text = targetSrc.options[i].innerText;
		var textA = text.split("/");
		if (textA != null && textA.length > 0) {
			_target.push(textA[1]);
		}
	}
}*/

function addSel_(text) {
	var v = text.split("/")[1];
	for (var i=0; i<rmv_.length; i++) {
		if (rmv_[i] == v) {
			newRmv_(i);
			initStr();
			return;
		}
	}
	add_.push(v);
	initStr();
}

function newRmv(pos) {
	var tmp = new Array();
	for (var i=0; i<rmv_.length; i++) {
		if (i != pos) {
			tmp.push(rmv_[i]);
		}
	}
	rmv_ = tmp;
}

function addRmv_(text) {
	var v = text.split("/")[1];
	for (var i=0; i<add_.length; i++) {
		if (add_[i] == v) {
			newAdd_(i);
			initStr();
			return;
		}
	}
	rmv_.push(v);
	initStr();
}

function newAdd_(pos) {
	var tmp = new Array();
	for (var i=0; i<add_.length; i++) {
		if (i != pos) {
			tmp.push(add_[i]);
		}
	}
	add_ = tmp;
}

function debug_() {
	alert("add_.length=" + add_.length + "  rmv_.length=" + rmv_.length);
}
//���ȫ��
function getAll(){
	selectAll("source");
	addInfoMath();
}

//ɾ��ȫ��
function delAll(){
	var objSelected=document.frm.target;
	for(var j=0;j<objSelected.options.length;j++){
		addRmv_(objSelected.options[j].text);
		objSelected.options[j].removeNode();
		j--;
	}
}

//��������
function addInfo(){
	var objSelect=document.frm.source;
	var objSelected=document.frm.target;

	var objOption;
	var flag = false;
	
	for(var j=0;j<objSelect.options.length;j++){
	
		if(objSelect.options[j].selected){
			//ȡ���ֶ�ֵ
			var elementSelectName=objSelect.options[objSelect.selectedIndex].text;
			var elementSelectValue=objSelect.options[objSelect.selectedIndex].value;
			
			//�ж�target���Ƿ��Ѵ��ڸ�ѡ����
			if(elementSelectValue == "null"){
					flag = true;
			}else{
				for(var i=0;i<objSelected.options.length;i++){
					if(objSelected.options[i].value == (elementSelectValue)){
						flag = true;
						break;
					}
				}
			}
			
			//��Ϊ��
			if(!flag){
				//׷�ӽڵ�
				if(objSelected.options.length < 30){
					objOption=new Option(elementSelectValue,elementSelectValue);
					objSelected.add(objOption);
				}
			}
			
			flag = false;
		}
	}
}

//����һ������
function addInfoMath(){
	var objSelect=document.frm.source;
	var objSelected=document.frm.target;
	
	var flag = false;

	for(var j=0;j<objSelect.options.length;j++){
		if(objSelect.options[j].selected){
			//ȡ���ֶ�ֵ
			var elementSelectName=objSelect.options[j].text;
			var elementSelectValue=objSelect.options[j].value;
			
			//�ж�target���Ƿ��Ѵ��ڸ�ѡ����
			if(elementSelectValue == "null"){
					flag = true;
			}else{
				for(var i=0;i<objSelected.options.length;i++){
					if(objSelected.options[i].value == (elementSelectValue)){
						flag = true;
						break;
					}
				}
			}
			
			//��Ϊ��
			if(!flag){
				//׷�ӽڵ�
				tmp = objSelect.options[j];
				objSelected.add(new Option(tmp.text,tmp.value));
				addSel_(tmp.text);
			}
			
			flag = false;
		}
	}
}

//ɾ������
function delInfo(){
	var objSelected=document.frm.target;

	for(var j=0;j<objSelected.options.length;j++){
		if(objSelected.options[j].selected){
			addRmv_(objSelected.options[j].text);
			objSelected.options[j].removeNode();
		}
	}
}

//ɾ��һ������
function delInfoMath(){
	var objSelected=document.frm.target;

	//var elementSelectedNumber=objSelected.options.length;

	for(var j=0;j<objSelected.options.length;j++){
		if(objSelected.options[j].selected){
			addRmv_(objSelected.options[j].text);
			objSelected.options[j].removeNode();
			j--;
		}
	}
}

//�ж��ύ����Ԫ���Ƿ�Ϊ��,Ϊ�վͲ������ύ
function isNull(){
	var elementSelectedNumber=document.frm.target.options.length;
	if(elementSelectedNumber==0){
		var add_temp = document.frm.add_string.value;
		var rmv_temp = document.frm.rmv_string.value;
		if(add_temp=="" && rmv_temp==""){
			window.alert("���������Χ���豸");
			return false;
		}
	}
	//ȫ��ѡ��
	selectAll("target");
	
	var tempStr = "";
	//Ϊ���ø������е��豸��֪ͨ���м��
	var objSelected=document.frm.target;
	for(var j=0;j<objSelected.options.length;j++){
		var v = objSelected.options[j].text.split("/")[1];
		if(j==0){
			tempStr = v;
		}else{
			tempStr = tempStr + "," + v;
		}
	}
	document.frm.add_string.value = tempStr;
	return true;
}

//ѡ��ȫ��
function selectAll(oStr){
	var oSelect = document.all(oStr);
	for(var i=0; i<oSelect.length; i++){
		oSelect[i].selected = true;
	}
}

//��ѡ��ȫ��
function unSelectAll(oStr){
	var oSelect = document.all(oStr);
	for(var i=0; i<oSelect.length; i++){
		oSelect[i].selected = false;
	}
}

//ˢ���������豸�ɹ����
function refreshAreaDevices(flag){
	if(flag){
		alert("�������豸ˢ�³ɹ�");
	}else{
		alert("������ˢ���豸ʧ�ܣ������³���");
	}
}


//�豸����
var devicetype_id = "-1";
var gather_id = "-1";
var ifcontainChild;
var user_city_id = "<%=city_id%>";//�û��������ر��
var city_id = "<%=city_id%>";//�û���ѡ�е����ر��
function showChild(parname){

	var o = event.srcElement;	
	
		//�õ��Ƿ�����������ر�ʶ
		for(var i=0;i<document.all("ifcontainChild").length;i++){
     		if(document.all("ifcontainChild")[i].checked){
           		ifcontainChild=document.all("ifcontainChild")[i].value;
            break;
      		}
   		}
	
	if(parname=="devicetype_id")
		devicetype_id = o.value;
	else if(parname=="gather_id")
	{
	   if("-1"==o.value)
	   {
	     alert("��ѡ��һ����Ч�Ĳɼ���");
	     return;
	   }
	   else
	   {
	     gather_id = o.value;
	   }
	}
				
	else if(parname=='city_id'){
		var obj = event.srcElement;
		city_id = obj.options[obj.selectedIndex].value;
		if(city_id!=-1){
			if(user_city_id != city_id){
				document.all("childFrm1").src = "cityFilter.jsp?city_id="+city_id;
			}else{
				document.all("my_city<%=city_id%>").innerHTML = "";
				document.all("city_name").innerHTML = "<font color='red'><%=city_name%></font>";
			}
			document.all("hid_city_id").value = city_id;
		}else
		{
		   alert("��ѡ��һ����Ч������");
		   return;
		}
			
	}
		
	document.all("childFrm").src = "getDevicesFilter.jsp?gather_id="+ gather_id +"&devicetype_id="+ devicetype_id +"&area_layer="+ document.all("area_layer").value +"&area_id="+document.all("area_id").value+"&area_pid="+ document.all("area_pid").value+"&city_id="+ city_id +"&ifcontainChild="+ifcontainChild;
	document.all("sourceDevices").innerHTML="<select size=6 style=width:300;height:300><option>��������.......</option></select>";
}

//IP��ַģ��ƥ����ں���
function search(){
	var s = document.all.IPinput.value;
	
	unSelectAll("source");
	
	//�û�����IP��ʽУ��
	if(IsIPAddr()){
		s = inputTransform(s);
		//alert(s);
		setIP(s);
	}

}

//ת���û�����IP��ϢΪ������ʽ
function inputTransform(s){
	var re1 = new RegExp("\\.","g");
	var re2 = new RegExp("\\?","g");
	var re3 = new RegExp("\\*","g");
	//��.ǰ���ת���
	s = s.replace(re1,"\\.");
	//���ַ�ƥ���
	s = s.replace(re2,".");
	//���ַ�ƥ��������3����
	s = s.replace(re3,"\\d{1,3}");
	return s;
}

//�жϴ�����ַ���s2�Ƿ����������ʽs1��Ҫ��
function isMatch(s1,s2){
	var re4 = new RegExp(s1);
	//return(re4.test(s2));
	//��ȫƥ��ʱ����true
	return(s2.replace(re4,"")=="");
}

//����������ѡ��״̬
function setIP(re){
	var objSelect=document.frm.source;
	
	var n = objSelect.options.length;
	if(n==0)
		alert("���޿�ѡ���豸");
	else{
		for(var j=0;j<n;j++){
		
			var elementSelectName=objSelect.options[j].text.split("/")[0];
			
			if(isMatch(re,elementSelectName)){
				objSelect.options[j].selected=true;
			}
		}
	}		

}

//IP��ַ��ʽУ��
function IsIPAddr(){
	var strValue = document.all.IPinput.value;
	var msg = "IP��ַ";
	if(IsNull(strValue,msg)){
		var pos;
		var tmpStr;
		var v = strValue;
		var i=0;
		var bz=true;
		while(bz){
			pos = v.indexOf(".");
			if(i != 3 && pos == -1){
				alert(msg + "��ʽ������");
				return false;
			}
			if(pos == -1){pos = v.length;bz=false;}
			tmpStr = v.substring(0,pos);
			if(!chkIPArea(tmpStr)) return false;
			v = v.substring(pos+1,v.length);
			i=i+1;
		}
		if(i=4) return true;
		else{
			alert(msg + "��ʽ������");
			return false;
		}
	}
}

//IP��ַ4����λ�� ��ÿ�ξ������ݵ��ж�
//֧��ģ��ƥ�� û�ο�����һ��* ��1��3��?
function chkIPArea(strIP)
{
	if(isNaN(strIP)&&strIP!="*"){
		//ȥ�����ֲ��� ֻʣ��?
		if(strIP.replace(/\d{1,3}/,"")!=""&&strIP.replace(/\d{1,3}/,"").replace(/\?{1,3}/,"")!=""){
			alert("IP��ַֻ��Ϊ����\n�����ͨ��� *(һ��)��?(���3��)");return false
		}
	}
	if(parseInt(strIP)>255){
		alert("IP��ַ��" + strIP + "����255");
		return false;
	}
	return true;
}
//-->
</SCRIPT>
