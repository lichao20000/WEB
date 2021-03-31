<%@ page contentType="text/html;charset=GBK"%>
<%@page import="com.linkage.litms.resource.DeviceAct"%>
<%
	//属地信息
	
	DeviceAct deviceAct = new DeviceAct();
	String 	strCityList = deviceAct.getCityListSelf(true, "", "", request);
	
	String strVendorList = deviceAct.getVendorList(true, "", "");
%>


<TABLE border=0 cellspacing=0 cellpadding=0 width="100%">
	<TR>
		<TD>
		<table width="98%" border="0" align="center" cellpadding="0" cellspacing="0">
			<tr>
				<td>
				<table width="100%" height="30" border="0" cellspacing="0" cellpadding="0" class="green_gargtd">
					<tr>
						<td width="162">
						<div align="center" class="title_bigwhite">设备性能</div>
						</td>
						<td><img src="../images/attention_2.gif" width="15"
							height="12"> <input
							type="radio" value="0" onclick="ShowDialog(this.value)"
							name="checkType" checked>按属地和型号&nbsp;&nbsp; <input
							type="radio" value="1" onclick="ShowDialog(this.value)"
							name="checkType">按用户&nbsp;&nbsp; <input type="radio"
							value="2" onclick="ShowDialog(this.value)" name="checkType">按设备
						</td>

					</tr>
				</table>
				</td>
			</tr>
			<TR>
				<TH colspan=4>
					性能配置
				</TH>
			</TR>
			<tr>
				<td bgcolor="#FFFFFF">
				<table width="100%" border=0 align="center" cellpadding="1"
					cellspacing="1" bgcolor="#999999" class="text">
					<TR bgcolor="#FFFFFF" id="tr1" STYLE="display: ">
						<TD align="right" width="10%">属地:</TD>
						<TD align="left" width="30%"><%=strCityList%></TD>
						<TD align="right" width="10%">厂商:</TD>
						<TD align="left" width="30%">
							<%=strVendorList%>
						</TD>
					</TR>
					<TR bgcolor="#FFFFFF" id="tr2" STYLE="display: ">
						<TD align="right" width="10%">设备型号:</TD>
						<TD width="30%">
							<div id="div_devicetype">
								<select name=devicetype_id class="bk">
								<option value="-1">--请先选择厂商--</option>
								</select>
							</div>
						</TD>
						<TD align="right" width="10%">设备版本:</TD>
						<TD width="30%">
						<div id="div_deviceversion"><select name="device_version"
							class="bk">
							<option value="-1">--请先选择设备型号--</option>
						</select></div>
						</TD>
					</TR>
					<TR bgcolor="#FFFFFF" id="tr3" STYLE="display:none">
						<TD align="right" width="10%">用户名:</TD>
						<TD width="30%"><input type="text" name="hguser" value=""
							class=bk></TD>
						<TD align="right" width="10%">用户电话号码:</TD>
						<TD width="30%"><input type="text" name="telephone" value=""
							class=bk> <input type="button" class=btn value="查询"
							onclick="relateDevice()"></TD>
					</TR>
					<TR bgcolor="#FFFFFF" id="tr4" STYLE="display:none">
						<TD align="right" width="10%">设备序列号:</TD>
						<TD width="30%"><input type="text" name="serialnumber"
							value="" class=bk></TD>
							<TD align="right" width="10%">设备域名或IP:</TD>
						<TD width="30%"><input type="text" name="loopback_ip" value=""
							class=bk> <input type="button" class=btn value=" 查 询 "
							onclick="relateDeviceBySerialno()">
						</TD>
					</TR>
					<TR bgcolor="#FFFFFF">
						<TD align="right" width="10%">设备列表: <br>
						<!-- 
						<INPUT TYPE="checkbox" onclick="selectAll('device_id')" name="device">
						全选
						--></TD>
						<TD colspan="3">
						<div id="div_device"
							style="width: 95%; height: 100px; z-index: 1; top: 100px; overflow: scroll">
						<span>请选择设备！</span></div>
						</TD>
					</TR>
				</table>
				</td>
			</tr>
			
			<TR><TD height="20"></TD></TR>
			<TR>
				<TH colspan=4>
					性能配置参数
				</TH>
			</TR>
			<TR>
				<td bgcolor="#FFFFFF">
				<table width="100%" border=0 align="center" cellpadding="1"
					cellspacing="1" bgcolor="#999999" class="text">
						  <tr>
							<td class=column1 align=center width="10%">性能表达式</td>
							
							<td class=column2 align=left width="30%">
								<div id="pmdiv">
								<select name="exp_name" onchange="Pm_Name()" class="bk">
									<option value="-1">请选择此设备的性能表达式</option>
								</select>
								</div>
							</td>
							<td class=column1 width="10%">版本:</td>
							<td class=column2 width="30%">
								<input type="radio" name="V" value="1" id='v1'><label for="v1">V1</label>
								<input type="radio" name="V" value="2" id='v2'><label for="v2">V2</label>
								<input type="radio" name="V" value="3" id='v3' checked><label for="v3">V3</label>
							</td>
						  </tr>
						  <tr>
							<td class=column1 align=center width="100">采样间隔</td>
							<td class=column2 align=left><input name="samp_distance" type="text" value="300" class="bk" style="width:100"></td>
							<td class=column1 align=center width="100">原始数据是否入库</td>
							<td class=column2 align=left><select name="ruku" class="bk">
							  <option value="0">否</option>
							  <option value="1">是</option>
							</select>					  
							</td>
						  </tr>
					</table>
				</TD>
			</TR>
	
	</table>
	</TD>
</TR>
	<TR>
		<TD HEIGHT=20>
		<IFRAME ID=childFrm3 SRC="" STYLE="display:none"></IFRAME>
		<IFRAME ID=childFrm1 SRC="" STYLE="display:none"></IFRAME>
		<IFRAME	ID=childFrm2 SRC="" STYLE="display:none"></IFRAME> &nbsp;
		</TD>
	</TR>
</TABLE>

<input type="hidden" name="expression_Name">