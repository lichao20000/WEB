<%--
FileName	: selectDevice.jsp
Date		: 2009��2��2��
Desc		: ѡ���豸.
--%>
<%@ page language="java" contentType="text/html; charset=GBK"
	pageEncoding="GBK"%>
<%@taglib prefix="s" uri="/struts-tags"%>
<%@taglib prefix="ms" uri="/ailk-itms-web-tags"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<title>�豸ѡ��</title>
<link href="../css/css_green.css" rel="stylesheet" type="text/css">

<SCRIPT LANGUAGE="JavaScript" SRC="../Js/jquery.js"></SCRIPT>
<SCRIPT LANGUAGE="JavaScript">
$(function(){
	//alert(window.gw_type);
	});
</SCRIPT>



</head>

<body>

<input type="hidden" name="selectType"
	value="<s:property value="selectType"/>" class=bk />
<input type="hidden" name="jsFunctionName"
	value="<s:property value="jsFunctionName"/>" class=bk />
<input type="hidden" name="jsFunctionNameBySn"
	value="<s:property value="jsFunctionNameBySn"/>" class=bk />
<input type="hidden" name="maxFileNum"
	value="<s:property value="maxFileNum"/>" class=bk />
<input type="hidden" name="listControl"
	value="<s:property value="listControl"/>" class=bk />


<TABLE width="100%" border=0 cellspacing=0 cellpadding=0 align="center">
	<TR>
		<TD bgcolor=#999999>
		<TABLE border=0 cellspacing=1 cellpadding=2 width="100%">
			<TR bgcolor="#FFFFFF" STYLE="display: ">
				<TD align="right" width="15%">��ѯ��ʽ</TD>
				<TD align="left" width="85%" colspan="3">
					<label STYLE="display:<s:property value="byDevicenoState" />">
						<input type="radio" value="2" onclick="deviceSelect_ShowDialog(this.value)" 
							name="checkType" <s:property value="byDevicenoChecked"/>>
						���豸&nbsp;
					</label>
					<label STYLE="display:<s:property value="byUsernameState" />">
						<input type="radio" value="1" onclick="deviceSelect_ShowDialog(this.value)" 
							name="checkType" <s:property value="byUsernameChecked"/>>
					<ms:inArea areaCode="sx_lt">
						Ψһ��ʶ
					</ms:inArea>
					<ms:inArea areaCode="sx_lt" notInMode="true">
						LOID
					</ms:inArea>
					</label> 
					<ms:inArea areaCode="sx_lt" notInMode="true">
					<label STYLE="display:<s:property value="byCityState" />">
						<input type="radio" value="0" onclick="deviceSelect_ShowDialog(this.value)" 
							name="checkType" <s:property value="byCityChecked"/>>
						�߼���ѯ
					</label> 
					</ms:inArea>
					<label STYLE="display:<s:property value="byImportState" />">
						<input type="radio" value="3" onclick="deviceSelect_ShowDialog(this.value)" 
							name="checkType" <s:property value="byImportChecked"/>>
						���ļ�����
					</label> 
					<ms:inArea areaCode="sx_lt" notInMode="true">
					<label STYLE="display:<s:property value="byItvState" />">
						<input type="radio" value="5" onclick="deviceSelect_ShowDialog(this.value)" 
							name="checkType" <s:property value="byItvChecked"/>>
						��iTV
					</label> 
					</ms:inArea>
					<label STYLE="display:<s:property value="byNetAccountState" />">
						<input type="radio" value="6" onclick="deviceSelect_ShowDialog(this.value)" 
							name="checkType" <s:property value="byNetAccountChecked"/>>
						������ʺ�
					</label>
					<label STYLE="display:<s:property value="byVOIPState" />">
						<input type="radio" value="4" onclick="deviceSelect_ShowDialog(this.value)" 
							name="checkType" <s:property value="byVOIPChecked"/>>
						��VOIP�绰����
					</label>
				</TD>
			</TR>
			<TR bgcolor="#FFFFFF" id="tr11"
				STYLE="display:<s:property value="byCityParam" />">
				<TD align="right" width="15%">�� ��</TD>
				<TD align="left" width="35%"><select name="city_id" class="bk">
					<option value="">==��ѡ��==</option>
					<s:iterator value="cityList">
						<option value="<s:property value="city_id" />">==<s:property
							value="city_name" />==</option>
					</s:iterator>
				</select></TD>
				<TD align="right" width="15%">�� ��</TD>
				<TD align="left" width="35%"><select name="office_id"
					class="bk">
					<option value="">==��ѡ��==</option>
					<s:iterator value="officeList">
						<option value="<s:property value="office_id" />">==<s:property
							value="office_name" />==</option>
					</s:iterator>
				</select></TD>
			</TR>
			<TR bgcolor="#FFFFFF" id="tr12"
				STYLE="display:<s:property value="byCityParam" />">
				<TD align="right" width="15%">�� ��</TD>
				<TD width="35%"><select name="vendor_id" class="bk"
					onchange="deviceSelect_change_select('vendor')">
					<option value="">==��ѡ��==</option>
					<s:iterator value="vendorList">
						<option value="<s:property value="vendor_id" />">==<s:property
							value="vendor_add" />(<s:property value="vendor_name" />)==</option>
					</s:iterator>
				</select></TD>
				<TD align="right" width="15%">�豸�ͺ�</TD>
				<TD align="left" width="35%"><select name="device_model_id"
					class="bk" onchange="deviceSelect_change_select('model')">
					<option value="-1">����ѡ����</option>
				</select></TD>
			</TR>
			<TR bgcolor="#FFFFFF" id="tr13"
				STYLE="display:<s:property value="byCityParam" />">
				<TD align="right" width="15%">�豸�汾</TD>
				<TD width="35%"><select name="devicetype_id" class="bk"
					onchange="deviceSelect_change_select('version')">
					<option value="-1">����ѡ���豸�ͺ�</option>
				</select></TD>
				<TD align="right" width="15%">����״̬</TD>
				<TD width="35%"><select name="online_status" class="bk">
					<option value="-1">==��ѡ��==</option>
					<option value="0">����</option>
					<option value="1">����</option>
					<option value="2">δ֪</option>
				</select></TD>
			</TR>
			<TR bgcolor="#FFFFFF" id="tr14"
				STYLE="display:<s:property value="byCityParam" />">
				<!-- <TD align="right" width="15%">�� �� IP</TD> -->
				<!-- <TD width="35%"><input type="text" name="loopback_ip" value=""
					class="bk" /></TD> -->
				<TD align="center" width="100%" colspan="4"><input type="button"
					class=jianbian value=" ��  ѯ "
					onclick="deviceSelect_relateDeviceBySenior()" /></TD>
			</TR>
			<TR bgcolor="#FFFFFF" id="tr21"
				STYLE="display:<s:property value="byUsernameParam" />">
				<TD align="right" width="15%">
					<ms:inArea areaCode="sx_lt">
						Ψһ��ʶ
					</ms:inArea>
					<ms:inArea areaCode="sx_lt" notInMode="true">
						LOID(������ʺ�)
					</ms:inArea>
				</TD>
				<TD align="left" width="35%"><input type="text" name="hguser"
					value="" class="bk" /><font color="red">&nbsp;*</font></TD>
				<TD align="right" width="15%"></TD>
				<TD width="35%"><input type="button" class=jianbian id ="Usernamebutton"
					value=" ��  ѯ " onclick="deviceSelect_relateDeviceByUsername()" /></TD>
			</TR>
			<TR bgcolor="#FFFFFF" id="tr31"
				STYLE="display:<s:property value="byDevicenoParam" />">
				<TD align="right" width="15%">�豸���к�</TD>
				<TD align="left" width="35%"><input type="text"
					name="device_serialnumber" value="" class="bk" /> <font color="red">*�����������λ</font>
				</TD>
				<!-- <TD align="right" width="15%">�� �� IP</TD> -->
				<TD width="50%">
					<!-- <input type="text" name="loopback_ip_" value=""
					class="bk" /> -->
					<input type="button" id="Serialnobutton" class=jianbian value=" ��  ѯ "
					onclick="deviceSelect_relateDeviceBySerialno()" /></TD>
			</TR>

			<!-- �����½� ��VOIP�绰�����ѯ  add by zhangchy 2012-02-21 begin -->
			<TR bgcolor="#FFFFFF" id="tr15"
				STYLE="display:<s:property value="byVOIPParam" />">
				<TD align="right" width="15%">VOIP�绰����</TD>
				<TD align="left" width="35%"><input type="text" name="voipPara"
					value="" class="bk" /><font color="red">&nbsp;*</font></TD>
				<TD align="right" width="15%"></TD>
				<TD width="35%"><input type="button" class=jianbian id="VOIPNumbutton"
					value=" ��  ѯ " onclick="deviceSelect_relateDeviceByVOIPNum()" /></TD>
			</TR>
			<!--  �����½� ��VOIP�绰�����ѯ add by zhangchy 2012-02-21 end -->

			<!-- ���󵥣�JSDX_ITMS-REQ-20120222-LUHJ-001  begin add by zhangchy 2012-02-29-->
			<TR bgcolor="#FFFFFF" id="tr16"
				STYLE="display:<s:property value="byItvParam" />">
				<TD align="right" width="15%">iTV�ʺ�</TD>
				<TD align="left" width="35%"><input type="text"
					name="iTVUserName" value="" class="bk" /><font color="red">&nbsp;*</font>
				</TD>
				<TD align="right" width="15%"></TD>
				<TD width="35%"><input type="button" class=jianbian id="ItvUserNamebutton"
					value=" ��  ѯ " onclick="deviceSelect_relateDeviceByItvUserName()" />
				</TD>
			</TR>


			<TR bgcolor="#FFFFFF" id="tr17"
				STYLE="display:<s:property value="byWideNetParam" />">
				<TD align="right" width="15%">����ʺ�</TD>
				<TD align="left" width="35%"><input type="text"
					name="wideNetPara" value="" class="bk" /><font color="red">&nbsp;*</font>
				</TD>
				<TD align="right" width="15%"></TD>
				<TD width="35%"><input type="button" class=jianbian id="WideNetbutton"
					value=" ��  ѯ " onclick="deviceSelect_relateDeviceByWideNet()" /></TD>
			</TR>
			<!-- ���󵥣�JSDX_ITMS-REQ-20120222-LUHJ-001  end -->

			<tr id="tr41" bgcolor="#FFFFFF"
				style="display:<s:property value="byImportParam" />">
				<td align="right" width="15%">�ύ�ļ�</td>
				<td colspan="3" width="85%">
				<div id="importUsername"><iframe name="loadForm" FRAMEBORDER=0
					SCROLLING=NO
					src="selectDeviceTag!initImport.action?selectType=<s:property value="selectType"/>&jsFunctionName=<s:property value="jsFunctionName"/>&maxFileNum=<s:property value="maxFileNum"/>"
					height="30" width="100%"> </iframe></div>
				</td>



			</tr>
			<tr id="tr42" style="display:<s:property value="byImportParam" />">
				<td CLASS="green_foot" align="right">ע������</td>
				<td colspan="3" CLASS="green_foot">1����Ҫ������ļ���ʽΪExcel�� <br>
				2���ļ��ĵ�һ��Ϊ�����У������û��˺š��� <br>
				3���ļ�ֻ��һ�С� <br>
				4���ļ�������������<s:property value="maxFileNum" />��,�糬��<s:property
					value="maxFileNum" />�У�ֻ����ǰ<s:property value="maxFileNum" />������(��һ�г���)��
				</td>
			</tr>
			<TR bgcolor="#FFFFFF" STYLE="display: ">
				<TD align="right" width="15%">�豸�б� <s:if
					test="selectType=='checkbox'">
					<br>
					<INPUT TYPE="checkbox"
						onclick="deviceSelect_selectAll('device_id')" name="device">ȫѡ
						</s:if></TD>
				<TD align="left" width="85%" colspan="3">
				<div id="div_device"
					style="width: 100%; height: 100px; z-index: 1; top: 100px; overflow: scroll">
				<span>���ѯ�豸��</span></div>
				</TD>
			</TR>
		</TABLE>
		</TD>
	</TR>
</TABLE>
</body>
</html>