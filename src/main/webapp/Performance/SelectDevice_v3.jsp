<%@ page contentType="text/html;charset=GBK"%>
<%@page import="com.linkage.litms.resource.DeviceAct"%>
<%
	//������Ϣ
	
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
						<div align="center" class="title_bigwhite">�豸����</div>
						</td>
						<td><img src="../images/attention_2.gif" width="15"
							height="12"> <input
							type="radio" value="0" onclick="ShowDialog(this.value)"
							name="checkType" checked>�����غ��ͺ�&nbsp;&nbsp; <input
							type="radio" value="1" onclick="ShowDialog(this.value)"
							name="checkType">���û�&nbsp;&nbsp; <input type="radio"
							value="2" onclick="ShowDialog(this.value)" name="checkType">���豸
						</td>

					</tr>
				</table>
				</td>
			</tr>
			<TR>
				<TH colspan=4>
					��������
				</TH>
			</TR>
			<tr>
				<td bgcolor="#FFFFFF">
				<table width="100%" border=0 align="center" cellpadding="1"
					cellspacing="1" bgcolor="#999999" class="text">
					<TR bgcolor="#FFFFFF" id="tr1" STYLE="display: ">
						<TD align="right" width="10%">����:</TD>
						<TD align="left" width="30%"><%=strCityList%></TD>
						<TD align="right" width="10%">����:</TD>
						<TD align="left" width="30%">
							<%=strVendorList%>
						</TD>
					</TR>
					<TR bgcolor="#FFFFFF" id="tr2" STYLE="display: ">
						<TD align="right" width="10%">�豸�ͺ�:</TD>
						<TD width="30%">
							<div id="div_devicetype">
								<select name=devicetype_id class="bk">
								<option value="-1">--����ѡ����--</option>
								</select>
							</div>
						</TD>
						<TD align="right" width="10%">�豸�汾:</TD>
						<TD width="30%">
						<div id="div_deviceversion"><select name="device_version"
							class="bk">
							<option value="-1">--����ѡ���豸�ͺ�--</option>
						</select></div>
						</TD>
					</TR>
					<TR bgcolor="#FFFFFF" id="tr3" STYLE="display:none">
						<TD align="right" width="10%">�û���:</TD>
						<TD width="30%"><input type="text" name="hguser" value=""
							class=bk></TD>
						<TD align="right" width="10%">�û��绰����:</TD>
						<TD width="30%"><input type="text" name="telephone" value=""
							class=bk> <input type="button" class=btn value="��ѯ"
							onclick="relateDevice()"></TD>
					</TR>
					<TR bgcolor="#FFFFFF" id="tr4" STYLE="display:none">
						<TD align="right" width="10%">�豸���к�:</TD>
						<TD width="30%"><input type="text" name="serialnumber"
							value="" class=bk></TD>
							<TD align="right" width="10%">�豸������IP:</TD>
						<TD width="30%"><input type="text" name="loopback_ip" value=""
							class=bk> <input type="button" class=btn value=" �� ѯ "
							onclick="relateDeviceBySerialno()">
						</TD>
					</TR>
					<TR bgcolor="#FFFFFF">
						<TD align="right" width="10%">�豸�б�: <br>
						<!-- 
						<INPUT TYPE="checkbox" onclick="selectAll('device_id')" name="device">
						ȫѡ
						--></TD>
						<TD colspan="3">
						<div id="div_device"
							style="width: 95%; height: 100px; z-index: 1; top: 100px; overflow: scroll">
						<span>��ѡ���豸��</span></div>
						</TD>
					</TR>
				</table>
				</td>
			</tr>
			
			<TR><TD height="20"></TD></TR>
			<TR>
				<TH colspan=4>
					�������ò���
				</TH>
			</TR>
			<TR>
				<td bgcolor="#FFFFFF">
				<table width="100%" border=0 align="center" cellpadding="1"
					cellspacing="1" bgcolor="#999999" class="text">
						  <tr>
							<td class=column1 align=center width="10%">���ܱ��ʽ</td>
							
							<td class=column2 align=left width="30%">
								<div id="pmdiv">
								<select name="exp_name" onchange="Pm_Name()" class="bk">
									<option value="-1">��ѡ����豸�����ܱ��ʽ</option>
								</select>
								</div>
							</td>
							<td class=column1 width="10%">�汾:</td>
							<td class=column2 width="30%">
								<input type="radio" name="V" value="1" id='v1'><label for="v1">V1</label>
								<input type="radio" name="V" value="2" id='v2'><label for="v2">V2</label>
								<input type="radio" name="V" value="3" id='v3' checked><label for="v3">V3</label>
							</td>
						  </tr>
						  <tr>
							<td class=column1 align=center width="100">�������</td>
							<td class=column2 align=left><input name="samp_distance" type="text" value="300" class="bk" style="width:100"></td>
							<td class=column1 align=center width="100">ԭʼ�����Ƿ����</td>
							<td class=column2 align=left><select name="ruku" class="bk">
							  <option value="0">��</option>
							  <option value="1">��</option>
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