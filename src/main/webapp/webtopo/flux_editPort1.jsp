<table width="100%" border="0" cellspacing="0" cellpadding="0">
	<tr>
		<td class="blue_title">
			<table width="100%" border="0" cellspacing="0" cellpadding="0">
				<tr>
					<td class="blue_title" width="10%">�˿���Ϣ</td>
					<td>&nbsp;</td>
				</tr>
			</table>
		</td>
	</tr>
	<tr>
		<td>
			<table width="100%" border="0" cellspacing="0" cellpadding="0">
				<tr><td HEIGHT="20"></td></tr>
					<tr>
						<td align="right" width="20%">�豸IP</td>
						<td width="2%">&nbsp;&nbsp;</td>
						<td align="left" width="25%"><input name="ip_address" type="text" class="bk" style="width:150px" readonly="true" value="<%= dev_ip%>"></td>
						<td align="right" width="20%">�˿�����</td>
						<td width="2%">&nbsp;&nbsp;</td>
						<td align="left" width="25%"><input name="port_index" type="text" class="bk" style="width:150px" readonly="true" value="<%= ifindex%>"></td>
					</tr>
					<tr><td HEIGHT="15"></td></tr>
					<tr>
						<td align="right">�˿�����</td>
						<td>&nbsp;&nbsp;</td>
						<td align="left"><input name="port_desc" type="text" class="bk" style="width:150px" readonly="true" value="<%= ifdescr%>"></td>
						<td align="right">�˿�����</td>
						<td>&nbsp;&nbsp;</td>
						<td align="left"><input name="port_name" type="text" class="bk" style="width:150px" readonly="true" value="<%= ifname%>"></td>
					</tr>
					<tr><td HEIGHT="15"></td></tr>
					<tr>
						<td align="right">�˿ڱ���</td>
						<td>&nbsp;&nbsp;</td>
						<td align="left"><input name="port_alias" type="text" class="bk" style="width:150px" readonly="true" value="<%= ifnamedefined%>"></td>
						<td align="right">�˿�����</td>
						<td>&nbsp;&nbsp;</td>
						<td align="left"><input name="port_type" type="text" class="bk" style="width:150px" readonly="true" value="<%= iftype%>"></td>
					</tr>
					<tr><td HEIGHT="15"></td></tr>
					<tr>
						<td align="right">�˿�����(bps)</td>
						<td>&nbsp;&nbsp;</td>
						<td align="left"><input name="port_speed" type="text" class="bk" style="width:150px" readonly="true" value="<%= ifspeed%>"></td>
						<td align="right">�˿�����䵥Ԫ</td>
						<td>&nbsp;&nbsp;</td>
						<td align="left"><input name="port_mostCell" type="text" class="bk" style="width:150px" readonly="true" value="<%= ifmtu%>"></td>
					</tr>
					<tr><td HEIGHT="15"></td></tr>
					<tr>
						<td align="right">���ٶ˿�����(bps)</td>
						<td>&nbsp;&nbsp;</td>
						<td align="left"><input name="port_highSpeed" type="text" class="bk" style="width:150px" readonly="true" value="<%= ifhighspeed%>"></td>
						<td align="right">�ɼ��Ƿ�ɹ�</td>
						<td>&nbsp;&nbsp;</td>
						<td align="left"><input name="port_collectionSucceed" type="text" class="bk" style="width:150px" readonly="true" value="���Գɹ��ɼ�"></td>
					</tr>
					<tr><td HEIGHT="15"></td></tr>
					<tr>
						<td align="right">�Ƿ�ɼ��˿�������Ϣ</td>
						<td>&nbsp;&nbsp;</td>
						<td align="left">
						<%
							switch (num_gatherflag) {
								case 0: {
									out.println("<select name=\"gatherflag\" style=\"width:150px\">");
									out.println("<option value=\"0\" selected>���ɼ�</option>");
									out.println("<option value=\"1\">�ɼ�</option>");
									out.println("</select>");
									break;
								}
								case 1: {
									out.println("<select name=\"gatherflag\" style=\"width:150px\">");
									out.println("<option value=\"0\">���ɼ�</option>");
									out.println("<option value=\"1\" selected>�ɼ�</option>");
									out.println("</select>");
									break;
								}
							}
						%>
						</td>
						<td align="right">ԭʼ�����Ƿ����</td>
						<td>&nbsp;&nbsp;</td>
						<td align="left">
						<%
							switch (num_intodb) {
								case 0: {
									out.println("<select name=\"intodb\" style=\"width:150px\">");
									out.println("<option value=\"0\" selected>�����</option>");
									out.println("<option value=\"1\">���</option>");
									out.println("<select>");
									break;
								}
								case 1: {
									out.println("<select name=\"intodb\" style=\"width:150px\">");
									out.println("<option value=\"0\">�����</option>");
									out.println("<option value=\"1\" selected>���</option>");
									out.println("<select>");
									break;
								}
							}
						%>
						</td>
					</tr>
					<tr><td HEIGHT="15"></td></tr>
			</table>
		</td>
	</tr>
</table>