<table width="100%" border="0" cellspacing="0" cellpadding="0">
	<tr>
		<td class="blue_title">
			<table width="100%" border="0" cellspacing="0" cellpadding="0">
				<tr>
					<td class="blue_title" width="10%">端口信息</td>
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
						<td align="right" width="20%">设备IP</td>
						<td width="2%">&nbsp;&nbsp;</td>
						<td align="left" width="25%"><input name="ip_address" type="text" class="bk" style="width:150px" readonly="true" value="<%= dev_ip%>"></td>
						<td align="right" width="20%">端口索引</td>
						<td width="2%">&nbsp;&nbsp;</td>
						<td align="left" width="25%"><input name="port_index" type="text" class="bk" style="width:150px" readonly="true" value="<%= ifindex%>"></td>
					</tr>
					<tr><td HEIGHT="15"></td></tr>
					<tr>
						<td align="right">端口描述</td>
						<td>&nbsp;&nbsp;</td>
						<td align="left"><input name="port_desc" type="text" class="bk" style="width:150px" readonly="true" value="<%= ifdescr%>"></td>
						<td align="right">端口名字</td>
						<td>&nbsp;&nbsp;</td>
						<td align="left"><input name="port_name" type="text" class="bk" style="width:150px" readonly="true" value="<%= ifname%>"></td>
					</tr>
					<tr><td HEIGHT="15"></td></tr>
					<tr>
						<td align="right">端口别名</td>
						<td>&nbsp;&nbsp;</td>
						<td align="left"><input name="port_alias" type="text" class="bk" style="width:150px" readonly="true" value="<%= ifnamedefined%>"></td>
						<td align="right">端口类型</td>
						<td>&nbsp;&nbsp;</td>
						<td align="left"><input name="port_type" type="text" class="bk" style="width:150px" readonly="true" value="<%= iftype%>"></td>
					</tr>
					<tr><td HEIGHT="15"></td></tr>
					<tr>
						<td align="right">端口速率(bps)</td>
						<td>&nbsp;&nbsp;</td>
						<td align="left"><input name="port_speed" type="text" class="bk" style="width:150px" readonly="true" value="<%= ifspeed%>"></td>
						<td align="right">端口最大传输单元</td>
						<td>&nbsp;&nbsp;</td>
						<td align="left"><input name="port_mostCell" type="text" class="bk" style="width:150px" readonly="true" value="<%= ifmtu%>"></td>
					</tr>
					<tr><td HEIGHT="15"></td></tr>
					<tr>
						<td align="right">高速端口速率(bps)</td>
						<td>&nbsp;&nbsp;</td>
						<td align="left"><input name="port_highSpeed" type="text" class="bk" style="width:150px" readonly="true" value="<%= ifhighspeed%>"></td>
						<td align="right">采集是否成功</td>
						<td>&nbsp;&nbsp;</td>
						<td align="left"><input name="port_collectionSucceed" type="text" class="bk" style="width:150px" readonly="true" value="可以成功采集"></td>
					</tr>
					<tr><td HEIGHT="15"></td></tr>
					<tr>
						<td align="right">是否采集端口流量信息</td>
						<td>&nbsp;&nbsp;</td>
						<td align="left">
						<%
							switch (num_gatherflag) {
								case 0: {
									out.println("<select name=\"gatherflag\" style=\"width:150px\">");
									out.println("<option value=\"0\" selected>不采集</option>");
									out.println("<option value=\"1\">采集</option>");
									out.println("</select>");
									break;
								}
								case 1: {
									out.println("<select name=\"gatherflag\" style=\"width:150px\">");
									out.println("<option value=\"0\">不采集</option>");
									out.println("<option value=\"1\" selected>采集</option>");
									out.println("</select>");
									break;
								}
							}
						%>
						</td>
						<td align="right">原始数据是否入库</td>
						<td>&nbsp;&nbsp;</td>
						<td align="left">
						<%
							switch (num_intodb) {
								case 0: {
									out.println("<select name=\"intodb\" style=\"width:150px\">");
									out.println("<option value=\"0\" selected>不入库</option>");
									out.println("<option value=\"1\">入库</option>");
									out.println("<select>");
									break;
								}
								case 1: {
									out.println("<select name=\"intodb\" style=\"width:150px\">");
									out.println("<option value=\"0\">不入库</option>");
									out.println("<option value=\"1\" selected>入库</option>");
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