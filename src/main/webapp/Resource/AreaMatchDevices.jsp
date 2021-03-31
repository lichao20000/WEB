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

//获取属地数据
String city_id = curUser.getCityId();
SelectCityFilter City = new SelectCityFilter(request);
String city_name = City.getNameByCity_id(city_id);
String strCityList =City.getSelfAndNextLayer(true,city_id,"");

//获得父区域范围内设备
Cursor pcursor = null;
//资源类型 1 代表设备
String res_type = "1";

AreaManageSyb areaManage = new AreaManageSyb();
//modified by w5221 admin.com用户可选择配置的设备是tab_gw_device中的去掉所选择域下的设备
if(user.isAdmin()){
	pcursor = areaManage.getOtherAllDevices(res_type,area_id,m_ProcessesList,"-1",city_id,"1");
}else{
	//int m_db_i = StringUtils.getDB();
	//为了应付江苏测试
	int m_db_i = 1; //StringUtils.getDB(); 
	
	if(m_db_i == 1) { //sybase
		pcursor = areaManage.getToConfigDevice(res_type,area_id,m_ProcessesList,cur_area_id,"-1",city_id,"1");
	} else if (m_db_i == 2) { //oracle
		pcursor = areaManage.getOtherDevicesOfAreaIdNew(res_type,area_id,m_ProcessesList,cur_area_id,"-1",city_id,"1");
	}
}

Map pfield = pcursor.getNext();
//Map pfield = null;

//获得自身区域范围内设备
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
          <TR><TH colspan='2'>域配置</TH></TR>
          <tr bgcolor="#FFFFFF" onmouseout="className='blue_trOut'">
			<td><div align="center">设备IP</div></td>
            <td><input type="text" name="IPinput" value="">&nbsp;&nbsp;<input type="button" name="" value=" 搜索 " onClick="search()">
              （可通过*或?进行多个或一个字符的模糊定位）</td>
          </tr>
         <!--  <tr bgcolor="#FFFFFF" onmouseout="className='blue_trOut'">
			<td><div align="center">根据设备类型过滤</div></td>
            <td><div align="left"><%=strResourceList%></div></td>
          </tr>
		   <tr bgcolor="#FFFFFF" onmouseout="className='blue_trOut'">
			<td><div align="center">根据采集点过滤</div></td>
            <td><div align="left" >
				<%=m_GaherIdStr %>
            </div></td>
          </tr> -->
			<TR class=blue_trOut onmouseout="className='blue_trOut'" bgColor=#ffffff>
			   <TD class="" width=180 align="center">属地</TD>
			   <TD class=""><%=strCityList%>
				<span id="my_city<%=city_id%>"></span>
				&nbsp;当前:<span id=city_name><font color='red'><%=city_name%></font></span>
				&nbsp;包含下级:<input type="radio" name="ifcontainChild" value='1' checked>是
				<input type="radio" name="ifcontainChild" value='0'>否<input type="hidden" name="hid_city_id" value="<%=city_id%>">
				</TD>
			</TR>
          <tr bgcolor="#FFFFFF" class=text>
            <td valign="top" colspan=2>
                  <table width="80%" border="0" align="center">
                    <tr> 
                      <td width="40%">
                        <div align="center">可选择设备</div>
                      </td>
                      <td width="20%">&nbsp;</td>
                      <td width="40%">
                        <div align="center">已选择设备</div>
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
                          <input type="button" name="add" value="添 加" class="btn" onclick="addInfoMath()">
                          <br>
                          <br>
                          <input type="button" name="deleteall" value="全 删" class="btn" onclick="delAll('target')">
                          <br>
                          <br>
                          <input type="button" name="delete" value="删 除" class="btn" onclick="delInfoMath()">
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
                <INPUT TYPE="submit" value=" 保 存 " class=btn>
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
//添加全部
function getAll(){
	selectAll("source");
	addInfoMath();
}

//删除全部
function delAll(){
	var objSelected=document.frm.target;
	for(var j=0;j<objSelected.options.length;j++){
		addRmv_(objSelected.options[j].text);
		objSelected.options[j].removeNode();
		j--;
	}
}

//增加数据
function addInfo(){
	var objSelect=document.frm.source;
	var objSelected=document.frm.target;

	var objOption;
	var flag = false;
	
	for(var j=0;j<objSelect.options.length;j++){
	
		if(objSelect.options[j].selected){
			//取得字段值
			var elementSelectName=objSelect.options[objSelect.selectedIndex].text;
			var elementSelectValue=objSelect.options[objSelect.selectedIndex].value;
			
			//判断target框是否已存在该选择项
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
			
			//若为真
			if(!flag){
				//追加节点
				if(objSelected.options.length < 30){
					objOption=new Option(elementSelectValue,elementSelectValue);
					objSelected.add(objOption);
				}
			}
			
			flag = false;
		}
	}
}

//增加一批数据
function addInfoMath(){
	var objSelect=document.frm.source;
	var objSelected=document.frm.target;
	
	var flag = false;

	for(var j=0;j<objSelect.options.length;j++){
		if(objSelect.options[j].selected){
			//取得字段值
			var elementSelectName=objSelect.options[j].text;
			var elementSelectValue=objSelect.options[j].value;
			
			//判断target框是否已存在该选择项
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
			
			//若为真
			if(!flag){
				//追加节点
				tmp = objSelect.options[j];
				objSelected.add(new Option(tmp.text,tmp.value));
				addSel_(tmp.text);
			}
			
			flag = false;
		}
	}
}

//删除数据
function delInfo(){
	var objSelected=document.frm.target;

	for(var j=0;j<objSelected.options.length;j++){
		if(objSelected.options[j].selected){
			addRmv_(objSelected.options[j].text);
			objSelected.options[j].removeNode();
		}
	}
}

//删除一批数据
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

//判断提交数据元素是否为空,为空就不允许提交
function isNull(){
	var elementSelectedNumber=document.frm.target.options.length;
	if(elementSelectedNumber==0){
		var add_temp = document.frm.add_string.value;
		var rmv_temp = document.frm.rmv_string.value;
		if(add_temp=="" && rmv_temp==""){
			window.alert("请添加区域范围内设备");
			return false;
		}
	}
	//全部选择
	selectAll("target");
	
	var tempStr = "";
	//为了让该域所有的设备都通知到中间件
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

//选择全部
function selectAll(oStr){
	var oSelect = document.all(oStr);
	for(var i=0; i<oSelect.length; i++){
		oSelect[i].selected = true;
	}
}

//不选择全部
function unSelectAll(oStr){
	var oSelect = document.all(oStr);
	for(var i=0; i<oSelect.length; i++){
		oSelect[i].selected = false;
	}
}

//刷新区域内设备成功与否
function refreshAreaDevices(flag){
	if(flag){
		alert("区域内设备刷新成功");
	}else{
		alert("区域内刷新设备失败，请重新尝试");
	}
}


//设备过滤
var devicetype_id = "-1";
var gather_id = "-1";
var ifcontainChild;
var user_city_id = "<%=city_id%>";//用户所属属地编号
var city_id = "<%=city_id%>";//用户所选中的属地编号
function showChild(parname){

	var o = event.srcElement;	
	
		//得到是否关联下属属地标识
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
	     alert("请选择一个有效的采集点");
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
		   alert("请选择一个有效的属地");
		   return;
		}
			
	}
		
	document.all("childFrm").src = "getDevicesFilter.jsp?gather_id="+ gather_id +"&devicetype_id="+ devicetype_id +"&area_layer="+ document.all("area_layer").value +"&area_id="+document.all("area_id").value+"&area_pid="+ document.all("area_pid").value+"&city_id="+ city_id +"&ifcontainChild="+ifcontainChild;
	document.all("sourceDevices").innerHTML="<select size=6 style=width:300;height:300><option>正在载入.......</option></select>";
}

//IP地址模糊匹配入口函数
function search(){
	var s = document.all.IPinput.value;
	
	unSelectAll("source");
	
	//用户输入IP格式校验
	if(IsIPAddr()){
		s = inputTransform(s);
		//alert(s);
		setIP(s);
	}

}

//转换用户查找IP信息为正则表达式
function inputTransform(s){
	var re1 = new RegExp("\\.","g");
	var re2 = new RegExp("\\?","g");
	var re3 = new RegExp("\\*","g");
	//在.前添加转意符
	s = s.replace(re1,"\\.");
	//单字符匹配符
	s = s.replace(re2,".");
	//多字符匹配符（最多3个）
	s = s.replace(re3,"\\d{1,3}");
	return s;
}

//判断传入的字符穿s2是否符合正则表达式s1的要求
function isMatch(s1,s2){
	var re4 = new RegExp(s1);
	//return(re4.test(s2));
	//完全匹配时返回true
	return(s2.replace(re4,"")=="");
}

//设置下拉框选中状态
function setIP(re){
	var objSelect=document.frm.source;
	
	var n = objSelect.options.length;
	if(n==0)
		alert("暂无可选择设备");
	else{
		for(var j=0;j<n;j++){
		
			var elementSelectName=objSelect.options[j].text.split("/")[0];
			
			if(isMatch(re,elementSelectName)){
				objSelect.options[j].selected=true;
			}
		}
	}		

}

//IP地址格式校验
function IsIPAddr(){
	var strValue = document.all.IPinput.value;
	var msg = "IP地址";
	if(IsNull(strValue,msg)){
		var pos;
		var tmpStr;
		var v = strValue;
		var i=0;
		var bz=true;
		while(bz){
			pos = v.indexOf(".");
			if(i != 3 && pos == -1){
				alert(msg + "格式不符合");
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
			alert(msg + "格式不符合");
			return false;
		}
	}
}

//IP地址4个段位中 对每段具体内容的判断
//支持模糊匹配 没段可输入一个* 或1～3个?
function chkIPArea(strIP)
{
	if(isNaN(strIP)&&strIP!="*"){
		//去掉数字部分 只剩下?
		if(strIP.replace(/\d{1,3}/,"")!=""&&strIP.replace(/\d{1,3}/,"").replace(/\?{1,3}/,"")!=""){
			alert("IP地址只能为数字\n或查找通配符 *(一个)，?(最多3个)");return false
		}
	}
	if(parseInt(strIP)>255){
		alert("IP地址中" + strIP + "大于255");
		return false;
	}
	return true;
}
//-->
</SCRIPT>
