<%@ include file="../timelater.jsp"%>
<%@page import="java.util.Map"%>
<%@page import="com.linkage.litms.common.database.Cursor"%>
<%@ page contentType="text/html;charset=GBK"%>
<%@page import="com.linkage.litms.common.database.DataSetBean"%>
<%@page import="java.util.HashMap"%>
<%@ page import="com.linkage.commons.db.DBUtil" %>
<jsp:useBean id="DeviceAct" scope="request" class="com.linkage.litms.resource.DeviceAct"/>
<%
request.setCharacterEncoding("GBK");

String strVendorList = DeviceAct.getVendorList(true,"","");	

//页面初始化所有命令
String cmd_sql = "select * from tb_c_devicecmd";
// teledb
if (DBUtil.GetDB() == 3) {
    cmd_sql = "select cmdcode, device_model, serv_type, os_version, cmdstr, remark from tb_c_devicecmd";
}
com.linkage.commons.db.PrepareSQL psql = new com.linkage.commons.db.PrepareSQL(cmd_sql);
psql.getSQL();
Cursor cursor = DataSetBean.getCursor(cmd_sql);
Map field = new HashMap();
String msg = "";
String optionStr = "";

//命令下拉框初始化
if(cursor!=null&&cursor.getRecordSize()!=0){
	field = cursor.getNext();
	String tmp_cmdcode,tmp_device_model,tmp_serv_type,tmp_os_version,tmp_cmdstr,tmp_remark;
    while (field != null) {
    	//命令编码
        tmp_cmdcode = (String) field.get("cmdcode");
    	//设备型号
        tmp_device_model = (String) field.get("device_model");
    	//命令类型 QOS VPN
        tmp_serv_type = (String) field.get("serv_type");
    	//设备版本
        tmp_os_version = (String) field.get("os_version");
    	//命令内容
        tmp_cmdstr = (String) field.get("cmdstr");
    	//命令描述
        tmp_remark = (String) field.get("remark");
        
        tmp_cmdcode = tmp_cmdcode==null?"":tmp_cmdcode.trim();
        tmp_device_model = tmp_device_model==null?"":tmp_device_model.trim();
        tmp_serv_type = tmp_serv_type==null?"":tmp_serv_type.trim();
        tmp_os_version = tmp_os_version==null?"":tmp_os_version.trim();
        tmp_cmdstr = tmp_cmdstr==null?"":tmp_cmdstr.trim();
        tmp_remark = tmp_remark==null?"":tmp_remark.trim();
        
        //将双引号转换为HTML特殊编码
        tmp_cmdstr = tmp_cmdstr.replaceAll("\"","&quot;");
    	
        optionStr += "<option value=\""+tmp_cmdcode+"\" device_model=\""+tmp_device_model+"\" serv_type=\""+tmp_serv_type+"\" os_version=\""+tmp_os_version+"\" cmdstr=\""+tmp_cmdstr+"\">"
        			+ tmp_remark+"("+tmp_cmdstr+")</option>";
        
        field = cursor.getNext();
    }
}	
else
	msg = "暂无相关命令";

%>
<%@ include file="../head.jsp"%>
<style>
span
{
	position:static;
	border:0;
}
</style>
<FORM NAME="frm" METHOD="post" ACTION="deviceServCmdAddSave.jsp" onSubmit="return isNull();" target="childFrm">
<table width="95" border="0" cellspacing="0" cellpadding="0" align="center">
  <tr>
    <td bgcolor="#FFFFFF" align="center">
        <table width="100%" border=0 align="center" cellpadding="1" cellspacing="1" bgcolor="#999999" class="text">
          <tr>
		  <th colspan=4>新增服务模版</th>
		  </tr>
		<tr>
		  <td class=column align="right" width="25%">模版描述：</td>
		  <td class=column width="25%"><input type="text" name="servRemark"><font color="red"> *</font></td>
		  <td class=column align="right" width="25%">模版类型：</td>
		  <td class=column><select name="serv_type" onChange="filtCmd()"><option value=0> QOS </option><option value=1> VPN </option></select><font color="red"> *</font></td>
		</tr>
          <tr bgcolor="#FFFFFF" onmouseout="className='blue_trOut'">
			<td colspan=4>
			<table width="100%" border="0">
			<TR bgcolor="#FFFFFF">
					<TD class=column align="right">设备厂商：</TD>
					<TD><span id="span2"><%=strVendorList%></span></TD>	
					
					<TD class=column align="right">设备型号：</TD>
					<TD><span id=strModelList>请先选择设备厂商</span><font color="red"> *</font></TD>

					<TD class=column align="right">设备版本：</TD>
					<TD><span id=strVersionList>请先选择设备型号</span><font color="red"> *</font></TD>
			</TR>
			</table>
			</td>
         </tr>
          <tr bgcolor="#FFFFFF" class=text>
            <td valign="top" colspan=4>
                  <table width="100%" border="0" align="center">
                    <tr> 
                      <td width="50%" colspan=2>
                        <div align="center">可选命令</div>
                      </td>
                    <td width="50%" colspan=2>
                        <div align="center">命令信息</div>
                      </td>
					</tr>
                    <tr> 
                      <td width="40%" rowspan=2> 
						<span id=source_CMD>
							<select name="sourceCMD" size="6" style="width:300;height:260;overflow:scroll;" onDblClick="addInfo()" multiple>
							</select>
						</span>
                      </td>
                      <td width="10%" rowspan=2> 
                        <div align="center"> 
                          <input type="button" name="add" value=">>" class="btn" onclick="addInfo()">
                          <br>
                          <br>
                          <input type="button" name="delete" value="<<" class="btn" onclick="delInfo()">
                        </div>
                      </td>
                      <td width="40%"> <span id=targetCMD>
                        <select name="addCMD" size="6" style="width:300;height:130;overflow:scroll;" onDblClick="delInfo()" multiple>
                        </select></span>
                      </td>
					<td width="10%"> 
                        <div align="center"> 
                          <input type="button" name="up" value="▲" class="btn" onclick="optionMove(1)">
                          <br>
                          <br>
                          <input type="button" name="down" value="▼" class="btn" onclick="optionMove(2)">
                        </div>
                      </td>
                    </tr>
					<tr>
					<td colspan=2>
					<!-- 不能被选中 -->
					<span id=targetCMDStr>
                        <select name="CMDStr" size="6" style="width:300;height:130;overflow:scroll;" multiple onmouseover="this.setCapture()" onmouseout="this.releaseCapture()">
                        </select>
					</span>
					</td>
					</tr>
                  </table>
            </td>
          </tr>
          <tr>
            <td class="blue_foot" colspan=4>
              <div align="center">
                <INPUT TYPE="submit" value="保存服务模版" class=btn>&nbsp;&nbsp;
                <INPUT TYPE="button" value="服务模版列表" class=btn onclick="javaScript:location.href='VPN_ServList.jsp'">
                <INPUT TYPE="hidden" value="" name="hid_addCMD_text">
                <INPUT TYPE="hidden" value="" name="hid_CMDStr_text">
              </div>
            </td>
          </tr>
      </table>        
    </td>
  </tr>
</table>
</form>
<IFRAME name="childFrm" ID=childFrm SRC="" STYLE="display:none"></IFRAME>
<%
field = null;
cursor = null;
%>

<SCRIPT LANGUAGE="JavaScript">
<!--
//用单引号 java的optionStr变量中可能存在双引号
var initOption = '<select name="sourceCMD" size=6 style="width:300;height:260;overflow:scroll;" onDblClick="addInfo()" multiple>'+'<%=optionStr%>'+'</selet>';
//页面初始为QOS命令
filtCmd();

//增加数据
function addInfo(){
	var objSelect=document.frm.sourceCMD;
	var objSelected=document.frm.addCMD;
	var objSelectedStr=document.frm.CMDStr;

	var flag = false;
	
	for(var j=0;j<objSelect.options.length;j++){
	
		if(objSelect.options[j].selected){
			//取得字段值
			var elementSelectName=objSelect.options[j].text;
			var elementSelectValue=objSelect.options[j].value;
			
			//判断addCMD框是否已存在该选择项
			if(elementSelectValue == "null"){
					flag = true;
			}else{
				//注释掉不可重复加入的限制 可为服务分配多条相同命令
				//for(var i=0;i<objSelected.options.length;i++){
				//	if(objSelected.options[i].value == (elementSelectValue)){
				//		flag = true;
				//		break;
				//	}
				//}
			}
			
			//若为真
			if(!flag){
				//追加节点
				tmp = objSelect.options[j];
				objSelected.add(new Option(tmp.text,tmp.value));
				objSelectedStr.add(new Option(tmp.cmdstr,tmp.value))
			}
		}
			
			flag = false;
	}
}

//删除数据
function delInfo(){
	var objSelected=document.frm.addCMD;
	var objSelectedStr=document.frm.CMDStr;

	for(var j=0;j<objSelected.options.length;){
		if(objSelected.options[j].selected){
			objSelected.options[j].removeNode();
			objSelectedStr.options[j].removeNode();
		}else
			j++;
	}
}

//移动
function optionMove(v){
	var obj = document.frm.addCMD;
	var strObj=document.frm.CMDStr;
	
	for(var i=0;i<obj.options.length;i++){
		if(obj.options[i].selected==true){
			//上移
			if(v==1&&i!=0){
				//移动提交下拉框
				var tem_text = obj.options[i-1].text;
				var tem_value = obj.options[i-1].value;

				obj.options[i-1].text=obj.options[i].text;
				obj.options[i-1].value=obj.options[i].value;

				obj.options[i].text=tem_text;
				obj.options[i].value=tem_value;

				obj.options[i-1].selected=true;
				obj.options[i].selected=false;
				
				//移动命令展示下拉框
				tem_text = strObj.options[i-1].text;
				tem_value = strObj.options[i-1].value;

				strObj.options[i-1].text=strObj.options[i].text;
				strObj.options[i-1].value=strObj.options[i].value;

				strObj.options[i].text=tem_text;
				strObj.options[i].value=tem_value;

				//只支持单个移动 待改进
				break;
			}
			//下移
			if(v==2&&i!=obj.options.length-1){
				var tem_text = obj.options[i+1].text;
				var tem_value = obj.options[i+1].value;

				obj.options[i+1].text=obj.options[i].text;
				obj.options[i+1].value=obj.options[i].value;

				obj.options[i].text=tem_text;
				obj.options[i].value=tem_value;

				obj.options[i+1].selected=true;
				obj.options[i].selected=false;
				
				//移动命令展示下拉框
				tem_text = strObj.options[i+1].text;
				tem_value = strObj.options[i+1].value;

				strObj.options[i+1].text=strObj.options[i].text;
				strObj.options[i+1].value=strObj.options[i].value;

				strObj.options[i].text=tem_text;
				strObj.options[i].value=tem_value;

				//只支持单个移动
				break;
			}
		}
	}
}

//判断提交数据元素是否为空,为空就不允许提交
function isNull(){
	var elementSelectedNumber=document.frm.addCMD.options.length;
	if(!IsNull(document.frm.servRemark.value,"模版描述")){
		return false;
	}
	if(typeof(document.all.device_model)=="undefined"||document.all.device_model.value=="-1"){
		alert("请选择设备型号");
		return false;
	}
	if(typeof(document.all.os_version)=="undefined"||document.all.os_version.value=="-1"){
		alert("请选择设备版本");
		return false;
	}
	if(elementSelectedNumber==0){
		window.alert("请为此服务添加命令");
		return false;
	}
	
	//全部选择
	selectAll("addCMD");
	selectAll("CMDStr");

	return true;
}

//选择全部
function selectAll(oStr){
	var oSelect = document.all(oStr);
	var tem_text = "";
	
	for(var i=0; i<oSelect.length; i++){
		oSelect[i].selected = true;
		if(tem_text==""){
			tem_text = oSelect[i].text;
		}else{
			tem_text += ","+oSelect[i].text;
		}
	}

	//将下拉框中的显示值拼接后提交
	document.all("hid_"+oStr+"_text").value=tem_text;
}

//清除所有
function removeAll(oStr){
	var oSelect = document.all(oStr);
	
	//alert(oStr+" "+oSelect);
	
	for(var i=0; i<oSelect.length;){
		oSelect.options[i].removeNode();
	}
}

//初始化所有下拉框
function initOptionFun(){
	var selectObj = document.all.sourceCMD;
	selectObj.outerHTML=initOption;
	
	removeAll("addCMD");
	removeAll("CMDStr");
}

//命令过滤
function filtCmd(){
	//初始化各下拉框
	initOptionFun();
		
	//用户选择的设备类型、软件版本及服务类型值
	//当页面还未生成下拉框(undefined) 或用户还没有选择某一具体值(-1)时 将值置为'all'
	var device_modle = (typeof(document.all.device_model)=="undefined")?"all":(document.all.device_model.value=="-1"?"all":document.all.device_model.value);
	var os_version = (typeof(document.all.os_version)=="undefined")?"all":(document.all.os_version.value=="-1"?"all":document.all.os_version.value);
	var serv_type = (typeof(document.all.serv_type)=="undefined")?"all":(document.all.serv_type.value=="-1"?"all":document.all.serv_type.value);
	
	filt(device_modle,os_version,serv_type);
}

function filt(device_modle,os_version,serv_type){
	//alert("filt in with "+device_modle+" "+os_version+" "+serv_type);
	
	var obj = document.all.sourceCMD;
	for(var i=0;i<obj.options.length;){
		var tem_option = obj.options[i];
		if(isOK(device_modle,tem_option.device_model)&&isOK(os_version,tem_option.os_version)&&isOK(serv_type,tem_option.serv_type)){
			i++;
		}else{
			obj.remove(i);
		}
	}
}

function isOK(v1,v2){
	if(v1=="all")
		return true;
	else if(v1==v2)
		return true;
	else
		return false;

}

//下拉框触发函数
function showChild(parname){
	//厂家
	if(parname=="vendor_id"){
		var o = document.all("vendor_id");
		var id = o.options[o.selectedIndex].value;
		document.all("childFrm").src = "vpn_model_top_getDeviceModel.jsp?vendor_id="+ id;
	//型号
	} else if(parname=="device_model"){
		var o = parent.document.all("device_model");
		var id = o.options[o.selectedIndex].value;
		parent.document.all("childFrm").src = "vpn_model_top_getOsVersion.jsp?device_model="+ id;
		//将版本下拉框的值置为-1
		if(typeof(document.all.os_version)!="undefined")
			document.all.os_version.value="-1";
		//重新过滤命令下拉框
		filtCmd();
	//版本
	}else if(parname=="os_version"){
		//重新过滤命令下拉框
		filtCmd();
	}
}
//-->
</SCRIPT>