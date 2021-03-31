<%@ include file="../timelater.jsp"%>
<%@page import="java.util.Map"%> 
<%@page import="com.linkage.litms.common.database.Cursor"%>
<%@ page contentType="text/html;charset=GBK"%>
<%@page import="com.linkage.litms.common.database.DataSetBean"%>
<%@page import="java.util.HashMap"%>
<%@page import="java.util.ArrayList"%>
<%@ page import="com.linkage.commons.db.DBUtil" %>
<jsp:useBean id="DeviceAct" scope="request" class="com.linkage.litms.resource.DeviceAct"/>
<%
request.setCharacterEncoding("GBK");

String servcode = request.getParameter("servcode");
String device_model = request.getParameter("device_model");
String os_version = request.getParameter("os_version");
String servRemark = "";
String serv_type ="";

//�õ����ϴ˷��������
String cmd_sql = "select * from tb_c_devicecmd where device_model='"+device_model+"' and os_version='"+os_version+"'";
// teledb
if (DBUtil.GetDB() == 3) {
    cmd_sql = "select cmdcode, device_model, serv_type, os_version, cmdstr, remark" +
            " from tb_c_devicecmd where device_model='"+device_model+"' and os_version='"+os_version+"'";
}
com.linkage.commons.db.PrepareSQL psql = new com.linkage.commons.db.PrepareSQL(cmd_sql);
psql.getSQL();
Cursor cursor = DataSetBean.getCursor(cmd_sql);
Map field = new HashMap();
String msg = "";
String optionStr = "";
//�����������ʼ��
if(cursor!=null&&cursor.getRecordSize()!=0){
	field = cursor.getNext();
	String tmp_cmdcode,tmp_device_model,tmp_serv_type,tmp_os_version,tmp_cmdstr,tmp_remark;
    while (field != null) {
    	//�������
        tmp_cmdcode = (String) field.get("cmdcode");
    	//�豸�ͺ�
        tmp_device_model = (String) field.get("device_model");
    	//�������� QOS VPN
        tmp_serv_type = (String) field.get("serv_type");
    	//�豸�汾
        tmp_os_version = (String) field.get("os_version");
    	//��������
        tmp_cmdstr = (String) field.get("cmdstr");
    	//��������
        tmp_remark = (String) field.get("remark");
        
        tmp_cmdcode = tmp_cmdcode==null?"":tmp_cmdcode.trim();
        tmp_device_model = tmp_device_model==null?"":tmp_device_model.trim();
        tmp_serv_type = tmp_serv_type==null?"":tmp_serv_type.trim();
        tmp_os_version = tmp_os_version==null?"":tmp_os_version.trim();
        tmp_cmdstr = tmp_cmdstr==null?"":tmp_cmdstr.trim();
        tmp_remark = tmp_remark==null?"":tmp_remark.trim();
        
        //��˫����ת��ΪHTML�������
        tmp_cmdstr = tmp_cmdstr.replaceAll("\"","&quot;");
    	
        optionStr += "<option value=\""+tmp_cmdcode+"\" device_model=\""+tmp_device_model+"\" serv_type=\""+tmp_serv_type+"\" os_version=\""+tmp_os_version+"\" cmdstr=\""+tmp_cmdstr+"\">"
        			+ tmp_remark+"("+tmp_cmdstr+")</option>";
        
        field = cursor.getNext();
    }
}	
else
	msg = "�����������";

//�õ�����Ļ�����Ϣ
cmd_sql = "select * from tm_c_serv_devicecmd where device_model='"+device_model+"' and os_version='"+os_version+"' and servcode='"+servcode+"'";
// teledb
if (DBUtil.GetDB() == 3) {
    cmd_sql = "select remark, serv_type from tm_c_serv_devicecmd where device_model='"+device_model+"' and os_version='"+os_version+"' and servcode='"+servcode+"'";
}
com.linkage.commons.db.PrepareSQL psql2 = new com.linkage.commons.db.PrepareSQL(cmd_sql);
psql2.getSQL();
cursor = DataSetBean.getCursor(cmd_sql);
if(cursor!=null&&cursor.getRecordSize()!=0){
	field = cursor.getNext();
	servRemark = (String)field.get("remark");
	serv_type = (String)field.get("serv_type");
}

//�õ������ѹ�����������
cmd_sql = "select * from tb_c_serv_devicecmd where device_model='"+device_model+"' and os_version='"+os_version+"' and servcode='"+servcode+"' order by cmdorder";
// teledb
if (DBUtil.GetDB() == 3) {
    cmd_sql = "select cmdcode" +
            " from tb_c_serv_devicecmd where device_model='"+device_model+"' and os_version='"+os_version+"' and servcode='"+servcode+"' order by cmdorder";
}
cursor = DataSetBean.getCursor(cmd_sql);
ArrayList cmdCodeWithOrder = new ArrayList();
cmdCodeWithOrder.clear();
if(cursor!=null&&cursor.getRecordSize()!=0){
	field = cursor.getNext();
	while (field != null) {
    	//�������
        cmdCodeWithOrder.add("'"+field.get("cmdcode")+"'");
    	field = cursor.getNext();
    }
}
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
		  <th colspan=4>����ģ�����</th>
		  </tr>
		<tr>
		  <td class=column align="right" width="25%">ģ��������</td>
		  <td class=column width="25%"><input type="text" name="servRemark" value="<%=servRemark%>"><font color="red"> *</font></td>
		  <td class=column align="right" width="25%">ģ�����ͣ�</td>
		  <td class=column><select name="serv_type" onChange="filtCmd()"><option value=0 <%if(serv_type.equals("0")){%>selected<%}%>> QOS </option><option value=1 <%if(serv_type.equals("1")){%>selected<%}%>> VPN </option></select><font color="red"> *</font></td>
		</tr>
          <tr bgcolor="#FFFFFF" onmouseout="className='blue_trOut'">
			<td colspan=4>
			<table width="100%" border="0">
			<TR bgcolor="#FFFFFF">					
					<TD class=column align="right" width="25%">�豸�ͺţ�</TD>
					<TD width="25%"><b><%=device_model%></b></TD>
					<TD class=column align="right" width="25%">�豸�汾��</TD>
					<TD width="25%"><b><%=os_version%></b></TD>
			</TR>
			</table>
			</td>
         </tr>
          <tr bgcolor="#FFFFFF" class=text>
            <td valign="top" colspan=4>
                  <table width="100%" border="0" align="center">
                    <tr> 
                      <td width="50%" colspan=2>
                        <div align="center">��ѡ����</div>
                      </td>
                    <td width="50%" colspan=2>
                        <div align="center">������Ϣ</div>
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
                          <input type="button" name="up" value="��" class="btn" onclick="optionMove(1)">
                          <br>
                          <br>
                          <input type="button" name="down" value="��" class="btn" onclick="optionMove(2)">
                        </div>
                      </td>
                    </tr>
					<tr>
					<td colspan=2>
					<!-- ���ܱ�ѡ�� -->
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
                <INPUT TYPE="submit" value="�����޸�" class=btn>&nbsp;&nbsp;
                <INPUT TYPE="button" value="�ر�" class=btn onclick="javaScript:window.close()">
                <INPUT TYPE="hidden" value="" name="hid_addCMD_text">
                <INPUT TYPE="hidden" value="" name="hid_CMDStr_text">
                <INPUT TYPE="hidden" value="edit" name="action">
                <INPUT TYPE="hidden" value="<%=servcode%>" name="servcode">
                <INPUT TYPE="hidden" value="<%=device_model%>" name="device_model">
                <INPUT TYPE="hidden" value="<%=os_version%>" name="os_version">
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
//�õ����� java��optionStr�����п��ܴ���˫����
var initOption = '<select name="sourceCMD" size=6 style="width:300;height:260;overflow:scroll;" onDblClick="addInfo()" multiple>'+'<%=optionStr%>'+'</selet>';
var initCmd = <%=cmdCodeWithOrder%>;

//��ʼ������
filtCmd();
//ѡ�з�������
setCmd();

//��������
function addInfo(){
	var objSelect=document.frm.sourceCMD;
	var objSelected=document.frm.addCMD;
	var objSelectedStr=document.frm.CMDStr;

	var flag = false;
	
	for(var j=0;j<objSelect.options.length;j++){
	
		if(objSelect.options[j].selected){
			//ȡ���ֶ�ֵ
			var elementSelectName=objSelect.options[j].text;
			var elementSelectValue=objSelect.options[j].value;
			
			//�ж�addCMD���Ƿ��Ѵ��ڸ�ѡ����
			if(elementSelectValue == "null"){
					flag = true;
			}else{
				//ע�͵������ظ���������� ��Ϊ������������ͬ����
				//for(var i=0;i<objSelected.options.length;i++){
				//	if(objSelected.options[i].value == (elementSelectValue)){
				//		flag = true;
				//		break;
				//	}
				//}
			}
			
			//��Ϊ��
			if(!flag){
				//׷�ӽڵ�
				tmp = objSelect.options[j];
				objSelected.add(new Option(tmp.text,tmp.value));
				objSelectedStr.add(new Option(tmp.cmdstr,tmp.value));
			}
		}
			
			flag = false;
	}
}

//ɾ������
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

//�ƶ�
function optionMove(v){
	var obj = document.frm.addCMD;
	var strObj=document.frm.CMDStr;
	
	for(var i=0;i<obj.options.length;i++){
		if(obj.options[i].selected==true){
			//����
			if(v==1&&i!=0){
				//�ƶ��ύ������
				var tem_text = obj.options[i-1].text;
				var tem_value = obj.options[i-1].value;

				obj.options[i-1].text=obj.options[i].text;
				obj.options[i-1].value=obj.options[i].value;

				obj.options[i].text=tem_text;
				obj.options[i].value=tem_value;

				obj.options[i-1].selected=true;
				obj.options[i].selected=false;
				
				//�ƶ�����չʾ������
				tem_text = strObj.options[i-1].text;
				tem_value = strObj.options[i-1].value;

				strObj.options[i-1].text=strObj.options[i].text;
				strObj.options[i-1].value=strObj.options[i].value;

				strObj.options[i].text=tem_text;
				strObj.options[i].value=tem_value;

				//ֻ֧�ֵ����ƶ� ���Ľ�
				break;
			}
			//����
			if(v==2&&i!=obj.options.length-1){
				var tem_text = obj.options[i+1].text;
				var tem_value = obj.options[i+1].value;

				obj.options[i+1].text=obj.options[i].text;
				obj.options[i+1].value=obj.options[i].value;

				obj.options[i].text=tem_text;
				obj.options[i].value=tem_value;

				obj.options[i+1].selected=true;
				obj.options[i].selected=false;
				
				//�ƶ�����չʾ������
				tem_text = strObj.options[i+1].text;
				tem_value = strObj.options[i+1].value;

				strObj.options[i+1].text=strObj.options[i].text;
				strObj.options[i+1].value=strObj.options[i].value;

				strObj.options[i].text=tem_text;
				strObj.options[i].value=tem_value;

				//ֻ֧�ֵ����ƶ�
				break;
			}
		}
	}
}

//�ж��ύ����Ԫ���Ƿ�Ϊ��,Ϊ�վͲ������ύ
function isNull(){
	var elementSelectedNumber=document.frm.addCMD.options.length;
	if(!IsNull(document.frm.servRemark.value,"ģ������")){
		return false;
	}
	if(elementSelectedNumber==0){
		window.alert("��Ϊ�˷����������");
		return false;
	}
	
	//ȫ��ѡ��
	selectAll("addCMD");
	selectAll("CMDStr");

	return true;
}

//ѡ��ȫ��
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

	//���������е���ʾֵƴ�Ӻ��ύ
	document.all("hid_"+oStr+"_text").value=tem_text;
}

//�������
function removeAll(oStr){
	var oSelect = document.all(oStr);
	
	//alert(oStr+" "+oSelect);
	
	for(var i=0; i<oSelect.length;){
		oSelect.options[i].removeNode();
	}
}

//��ʼ������������
function initOptionFun(){
	var selectObj = document.all.sourceCMD;
	selectObj.outerHTML=initOption;
	
	removeAll("addCMD");
	removeAll("CMDStr");
}

//�������
function filtCmd(){
	//��ʼ����������
	initOptionFun();
		
	//�û�ѡ����豸���͡�����汾����������ֵ
	//��ҳ�滹δ����������(undefined) ���û���û��ѡ��ĳһ����ֵ(-1)ʱ ��ֵ��Ϊ'all'
	var serv_type = (typeof(document.all.serv_type)=="undefined")?"all":(document.all.serv_type.value=="-1"?"all":document.all.serv_type.value);
	
	filt(serv_type);
}

function filt(serv_type){
	//alert("filt in with "+device_modle+" "+os_version+" "+serv_type);
	
	var obj = document.all.sourceCMD;
	for(var i=0;i<obj.options.length;){
		var tem_option = obj.options[i];
		if(isOK(serv_type,tem_option.serv_type)){
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

//ѡ�д˷��������
function setCmd(){
	var objSelect=document.frm.sourceCMD;
	var objSelected=document.frm.addCMD;
	var objSelectedStr=document.frm.CMDStr;
	
	var n = initCmd.length;
	for(i=0;i<n;i++){
		for(var j=0;j<objSelect.options.length;j++){
		
			var elementSelectName=objSelect.options[j].text;
			var elementSelectValue=objSelect.options[j].value;
			
			if(elementSelectValue == initCmd[i]){
				tmp = objSelect.options[j];
				objSelected.add(new Option(tmp.text,tmp.value));
				objSelectedStr.add(new Option(tmp.cmdstr,tmp.value));
				
				objSelect.options[j].selected=true;
				break;
			}
		}
	}
}
//-->
</SCRIPT>