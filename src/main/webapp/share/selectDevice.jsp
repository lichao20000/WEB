<%--
FileName	: selectDevice.jsp
Date		: 2009年2月2日
Desc		: 选择设备.
--%>
<%@ page language="java" contentType="text/html; charset=GBK"
	pageEncoding="GBK"%>
<%@taglib prefix="s" uri="/struts-tags"%>
<%@taglib prefix="ms" uri="/ailk-itms-web-tags"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<title>设备选择</title>
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
				<TD align="right" width="15%">查询方式</TD>
				<TD align="left" width="85%" colspan="3">
					<label STYLE="display:<s:property value="byDevicenoState" />">
						<input type="radio" value="2" onclick="deviceSelect_ShowDialog(this.value)" 
							name="checkType" <s:property value="byDevicenoChecked"/>>
						按设备&nbsp;
					</label>
					<label STYLE="display:<s:property value="byUsernameState" />">
						<input type="radio" value="1" onclick="deviceSelect_ShowDialog(this.value)" 
							name="checkType" <s:property value="byUsernameChecked"/>>
					<ms:inArea areaCode="sx_lt">
						唯一标识
					</ms:inArea>
					<ms:inArea areaCode="sx_lt" notInMode="true">
						LOID
					</ms:inArea>
					</label> 
					<ms:inArea areaCode="sx_lt" notInMode="true">
					<label STYLE="display:<s:property value="byCityState" />">
						<input type="radio" value="0" onclick="deviceSelect_ShowDialog(this.value)" 
							name="checkType" <s:property value="byCityChecked"/>>
						高级查询
					</label> 
					</ms:inArea>
					<label STYLE="display:<s:property value="byImportState" />">
						<input type="radio" value="3" onclick="deviceSelect_ShowDialog(this.value)" 
							name="checkType" <s:property value="byImportChecked"/>>
						按文件导入
					</label> 
					<ms:inArea areaCode="sx_lt" notInMode="true">
					<label STYLE="display:<s:property value="byItvState" />">
						<input type="radio" value="5" onclick="deviceSelect_ShowDialog(this.value)" 
							name="checkType" <s:property value="byItvChecked"/>>
						按iTV
					</label> 
					</ms:inArea>
					<label STYLE="display:<s:property value="byNetAccountState" />">
						<input type="radio" value="6" onclick="deviceSelect_ShowDialog(this.value)" 
							name="checkType" <s:property value="byNetAccountChecked"/>>
						按宽带帐号
					</label>
					<label STYLE="display:<s:property value="byVOIPState" />">
						<input type="radio" value="4" onclick="deviceSelect_ShowDialog(this.value)" 
							name="checkType" <s:property value="byVOIPChecked"/>>
						按VOIP电话号码
					</label>
				</TD>
			</TR>
			<TR bgcolor="#FFFFFF" id="tr11"
				STYLE="display:<s:property value="byCityParam" />">
				<TD align="right" width="15%">属 地</TD>
				<TD align="left" width="35%"><select name="city_id" class="bk">
					<option value="">==请选择==</option>
					<s:iterator value="cityList">
						<option value="<s:property value="city_id" />">==<s:property
							value="city_name" />==</option>
					</s:iterator>
				</select></TD>
				<TD align="right" width="15%">局 向</TD>
				<TD align="left" width="35%"><select name="office_id"
					class="bk">
					<option value="">==请选择==</option>
					<s:iterator value="officeList">
						<option value="<s:property value="office_id" />">==<s:property
							value="office_name" />==</option>
					</s:iterator>
				</select></TD>
			</TR>
			<TR bgcolor="#FFFFFF" id="tr12"
				STYLE="display:<s:property value="byCityParam" />">
				<TD align="right" width="15%">厂 商</TD>
				<TD width="35%"><select name="vendor_id" class="bk"
					onchange="deviceSelect_change_select('vendor')">
					<option value="">==请选择==</option>
					<s:iterator value="vendorList">
						<option value="<s:property value="vendor_id" />">==<s:property
							value="vendor_add" />(<s:property value="vendor_name" />)==</option>
					</s:iterator>
				</select></TD>
				<TD align="right" width="15%">设备型号</TD>
				<TD align="left" width="35%"><select name="device_model_id"
					class="bk" onchange="deviceSelect_change_select('model')">
					<option value="-1">请先选择厂商</option>
				</select></TD>
			</TR>
			<TR bgcolor="#FFFFFF" id="tr13"
				STYLE="display:<s:property value="byCityParam" />">
				<TD align="right" width="15%">设备版本</TD>
				<TD width="35%"><select name="devicetype_id" class="bk"
					onchange="deviceSelect_change_select('version')">
					<option value="-1">请先选择设备型号</option>
				</select></TD>
				<TD align="right" width="15%">上线状态</TD>
				<TD width="35%"><select name="online_status" class="bk">
					<option value="-1">==请选择==</option>
					<option value="0">下线</option>
					<option value="1">在线</option>
					<option value="2">未知</option>
				</select></TD>
			</TR>
			<TR bgcolor="#FFFFFF" id="tr14"
				STYLE="display:<s:property value="byCityParam" />">
				<!-- <TD align="right" width="15%">设 备 IP</TD> -->
				<!-- <TD width="35%"><input type="text" name="loopback_ip" value=""
					class="bk" /></TD> -->
				<TD align="center" width="100%" colspan="4"><input type="button"
					class=jianbian value=" 查  询 "
					onclick="deviceSelect_relateDeviceBySenior()" /></TD>
			</TR>
			<TR bgcolor="#FFFFFF" id="tr21"
				STYLE="display:<s:property value="byUsernameParam" />">
				<TD align="right" width="15%">
					<ms:inArea areaCode="sx_lt">
						唯一标识
					</ms:inArea>
					<ms:inArea areaCode="sx_lt" notInMode="true">
						LOID(宽带主帐号)
					</ms:inArea>
				</TD>
				<TD align="left" width="35%"><input type="text" name="hguser"
					value="" class="bk" /><font color="red">&nbsp;*</font></TD>
				<TD align="right" width="15%"></TD>
				<TD width="35%"><input type="button" class=jianbian id ="Usernamebutton"
					value=" 查  询 " onclick="deviceSelect_relateDeviceByUsername()" /></TD>
			</TR>
			<TR bgcolor="#FFFFFF" id="tr31"
				STYLE="display:<s:property value="byDevicenoParam" />">
				<TD align="right" width="15%">设备序列号</TD>
				<TD align="left" width="35%"><input type="text"
					name="device_serialnumber" value="" class="bk" /> <font color="red">*至少输入后六位</font>
				</TD>
				<!-- <TD align="right" width="15%">设 备 IP</TD> -->
				<TD width="50%">
					<!-- <input type="text" name="loopback_ip_" value=""
					class="bk" /> -->
					<input type="button" id="Serialnobutton" class=jianbian value=" 查  询 "
					onclick="deviceSelect_relateDeviceBySerialno()" /></TD>
			</TR>

			<!-- 用于新疆 按VOIP电话号码查询  add by zhangchy 2012-02-21 begin -->
			<TR bgcolor="#FFFFFF" id="tr15"
				STYLE="display:<s:property value="byVOIPParam" />">
				<TD align="right" width="15%">VOIP电话号码</TD>
				<TD align="left" width="35%"><input type="text" name="voipPara"
					value="" class="bk" /><font color="red">&nbsp;*</font></TD>
				<TD align="right" width="15%"></TD>
				<TD width="35%"><input type="button" class=jianbian id="VOIPNumbutton"
					value=" 查  询 " onclick="deviceSelect_relateDeviceByVOIPNum()" /></TD>
			</TR>
			<!--  用于新疆 按VOIP电话号码查询 add by zhangchy 2012-02-21 end -->

			<!-- 需求单：JSDX_ITMS-REQ-20120222-LUHJ-001  begin add by zhangchy 2012-02-29-->
			<TR bgcolor="#FFFFFF" id="tr16"
				STYLE="display:<s:property value="byItvParam" />">
				<TD align="right" width="15%">iTV帐号</TD>
				<TD align="left" width="35%"><input type="text"
					name="iTVUserName" value="" class="bk" /><font color="red">&nbsp;*</font>
				</TD>
				<TD align="right" width="15%"></TD>
				<TD width="35%"><input type="button" class=jianbian id="ItvUserNamebutton"
					value=" 查  询 " onclick="deviceSelect_relateDeviceByItvUserName()" />
				</TD>
			</TR>


			<TR bgcolor="#FFFFFF" id="tr17"
				STYLE="display:<s:property value="byWideNetParam" />">
				<TD align="right" width="15%">宽带帐号</TD>
				<TD align="left" width="35%"><input type="text"
					name="wideNetPara" value="" class="bk" /><font color="red">&nbsp;*</font>
				</TD>
				<TD align="right" width="15%"></TD>
				<TD width="35%"><input type="button" class=jianbian id="WideNetbutton"
					value=" 查  询 " onclick="deviceSelect_relateDeviceByWideNet()" /></TD>
			</TR>
			<!-- 需求单：JSDX_ITMS-REQ-20120222-LUHJ-001  end -->

			<tr id="tr41" bgcolor="#FFFFFF"
				style="display:<s:property value="byImportParam" />">
				<td align="right" width="15%">提交文件</td>
				<td colspan="3" width="85%">
				<div id="importUsername"><iframe name="loadForm" FRAMEBORDER=0
					SCROLLING=NO
					src="selectDeviceTag!initImport.action?selectType=<s:property value="selectType"/>&jsFunctionName=<s:property value="jsFunctionName"/>&maxFileNum=<s:property value="maxFileNum"/>"
					height="30" width="100%"> </iframe></div>
				</td>



			</tr>
			<tr id="tr42" style="display:<s:property value="byImportParam" />">
				<td CLASS="green_foot" align="right">注意事项</td>
				<td colspan="3" CLASS="green_foot">1、需要导入的文件格式为Excel。 <br>
				2、文件的第一行为标题行，即【用户账号】。 <br>
				3、文件只有一列。 <br>
				4、文件的行数不超过<s:property value="maxFileNum" />行,如超过<s:property
					value="maxFileNum" />行，只解析前<s:property value="maxFileNum" />行数据(第一行除外)。
				</td>
			</tr>
			<TR bgcolor="#FFFFFF" STYLE="display: ">
				<TD align="right" width="15%">设备列表 <s:if
					test="selectType=='checkbox'">
					<br>
					<INPUT TYPE="checkbox"
						onclick="deviceSelect_selectAll('device_id')" name="device">全选
						</s:if></TD>
				<TD align="left" width="85%" colspan="3">
				<div id="div_device"
					style="width: 100%; height: 100px; z-index: 1; top: 100px; overflow: scroll">
				<span>请查询设备！</span></div>
				</TD>
			</TR>
		</TABLE>
		</TD>
	</TR>
</TABLE>
</body>
</html>