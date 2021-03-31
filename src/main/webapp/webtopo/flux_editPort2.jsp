
<table width="100%" border="0" cellspacing="0" cellpadding="0">
	<tr>
		<td>
			<table width="100%" border="0" align="center" cellpadding="0" cellspacing="0" valign="middle">
				<tr> 
				  <TH width="159" height="25" class="button_onblue" id="td1" onClick="location.href='javascript:showpage(1);'">固定阀值告警配置</TH>
				  <TH width="159" height="25" class="button_outblue" id="td2" onClick="location.href='javascript:showpage(2);'">动态阀值告警配置</TH>
				  <TH width="159" height="25" class="button_outblue" id="td3" onClick="location.href='javascript:showpage(3);'">端口流量突变告警配置</TH>
				  <td align="left"></td>
				</tr>
				<tr> 
				  <td height="3" colspan="4" align="center" class="blue_tag_line"></td>
				</tr>
		  </table>
		</td>
	</tr>
	<tr>
		<td id="test1" style="display:" bgcolor=#000000>
			<table width="100%" border="0" cellspacing="0" cellpadding="0">
				<tr>
					<td class="blue_title">
						<table width="100%" border="0" cellspacing="0" cellpadding="0">
							<tr>
								<td class="blue_title" width="8%">参数配置</td>
								<td>&nbsp;</td>
							</tr>
						</table>
					</td>
				</tr>
				<tr>
					<td class="column">
						<table width="100%" border="0" cellspacing="0" cellpadding="0">
							<tr><td HEIGHT="20"></td></tr>
							<tr>
								<td width="2%">&nbsp;</td>
								<%
									if (num_ifinoctetsbps_max < 0) {
										out.println("<td><input name=\"checkbox_1\" type=\"checkbox\" onclick=\"javascript:CheckChange('1');\">端口流入带宽利用率阀值(%)</td>");
										out.println("<td><input name=\"text_1\" type=\"text\" class=\"bk\" style=\"width:150px\" value=\"-1\" disabled></td>");
										out.println("<input type=\"hidden\" name=\"ifinoctetsbps_max\" value=\"\">");
									}
									else {
										out.println("<td><input name=\"checkbox_1\" type=\"checkbox\" onclick=\"javascript:CheckChange('1');\" checked>端口流入带宽利用率阀值(%)</td>");
										out.println("<td><input name=\"text_1\" type=\"text\" style=\"width:150px\" value=\"" + ifinoctetsbps_max + "\"></td>");
										out.println("<input type=\"hidden\" name=\"ifinoctetsbps_max\" value=\"" + ifinoctetsbps_max + "\">");
									}
								%>
								<td width="2%">&nbsp;</td>
								<%
									if (num_ifoutoctetsbps_max < 0) {
										out.println("<td><input name=\"checkbox_2\" type=\"checkbox\" onclick=\"javascript:CheckChange('2');\">端口流出带宽利用率阀值(%)</td>");
										out.println("<td><input name=\"text_2\" type=\"text\" class=\"bk\" style=\"width:150px\" value=\"-1\" disabled></td>");
										out.println("<input type=\"hidden\" name=\"ifoutoctetsbps_max\" value=\"\">");
									}
									else {
										out.println("<td><input name=\"checkbox_2\" type=\"checkbox\" onclick=\"javascript:CheckChange('2');\" checked>端口流出带宽利用率阀值(%)</td>");
										out.println("<td><input name=\"text_2\" type=\"text\" style=\"width:150px\" value=\"" + ifoutoctetsbps_max + "\"></td>");
										out.println("<input type=\"hidden\" name=\"ifoutoctetsbps_max\" value=\"" + ifoutoctetsbps_max + "\">");
									}
								%>
							</tr>

							<tr><td HEIGHT="15"></td></tr>
							<tr>
								<td>&nbsp;</td>
								<%
									if (num_ifindiscardspps_max < 0) {
										out.println("<td><input name=\"checkbox_3\" type=\"checkbox\" onclick=\"javascript:CheckChange('3');\">端口流入丢包率阀值(%)</td>");
										out.println("<td><input name=\"text_3\" type=\"text\" class=\"bk\" style=\"width:150px\" value=\"-1\" disabled></td>");
										out.println("<input type=\"hidden\" name=\"ifindiscardspps_max\" value=\"\">");
									}
									else {
										out.println("<td><input name=\"checkbox_3\" type=\"checkbox\" onclick=\"javascript:CheckChange('3');\" checked>端口流入丢包率阀值(%)</td>");
										out.println("<td><input name=\"text_3\" type=\"text\" style=\"width:150px\" value=\"" + ifindiscardspps_max + "\"></td>");
										out.println("<input type=\"hidden\" name=\"ifindiscardspps_max\" value=\"" + ifindiscardspps_max + "\">");
									}
								%>
								<td>&nbsp;</td>
								<%
									if (num_ifoutdiscardspps_max < 0) {
										out.println("<td><input name=\"checkbox_4\" type=\"checkbox\" onclick=\"javascript:CheckChange('4');\">端口流出丢包率阀值(%)</td>");
										out.println("<td><input name=\"text_4\" type=\"text\" class=\"bk\" style=\"width:150px\" value=\"-1\" disabled></td>");
										out.println("<input type=\"hidden\" name=\"ifoutdiscardspps_max\" value=\"\">");
									}
									else {
										out.println("<td><input name=\"checkbox_4\" type=\"checkbox\" onclick=\"javascript:CheckChange('4');\" checked>端口流出丢包率阀值(%)</td>");
										out.println("<td><input name=\"text_4\" type=\"text\" style=\"width:150px\" value=\"" + ifoutdiscardspps_max + "\"></td>");
										out.println("<input type=\"hidden\" name=\"ifoutdiscardspps_max\" value=\"" + ifoutdiscardspps_max + "\">");
									}
								%>
							</tr>

							<tr><td HEIGHT="15"></td></tr>
							<tr>
								<td>&nbsp;</td>
								<%
									if (num_ifinerrorspps_max < 0) {
										out.println("<td><input name=\"checkbox_5\" type=\"checkbox\" onclick=\"javascript:CheckChange('5');\">端口流入错包率阀值(%)</td>");
										out.println("<td><input name=\"text_5\" type=\"text\" class=\"bk\" style=\"width:150px\" value=\"-1\" disabled></td>");
										out.println("<input type=\"hidden\" name=\"ifinerrorspps_max\" value=\"\">");
									}
									else {
										out.println("<td><input name=\"checkbox_5\" type=\"checkbox\" onclick=\"javascript:CheckChange('5');\" checked>端口流入错包率阀值(%)</td>");
										out.println("<td><input name=\"text_5\" type=\"text\" style=\"width:150px\" value=\"" + ifinerrorspps_max + "\"></td>");
										out.println("<input type=\"hidden\" name=\"ifinerrorspps_max\" value=\"" + ifinerrorspps_max + "\">");
									}
								%>
								<td>&nbsp;</td>
								<%
									if (num_ifouterrorspps_max < 0) {
										out.println("<td><input name=\"checkbox_6\" type=\"checkbox\" onclick=\"javascript:CheckChange('6');\">端口流出错包率阀值(%)</td>");
										out.println("<td><input name=\"text_6\" type=\"text\" class=\"bk\" style=\"width:150px\" value=\"-1\" disabled></td>");
										out.println("<input type=\"hidden\" name=\"ifouterrorspps_max\" value=\"\">");
									}
									else {
										out.println("<td><input name=\"checkbox_6\" type=\"checkbox\" onclick=\"javascript:CheckChange('6');\" checked>端口流出错包率阀值(%)</td>");
										out.println("<td><input name=\"text_6\" type=\"text\" style=\"width:150px\" value=\"" + ifouterrorspps_max + "\"></td>");
										out.println("<input type=\"hidden\" name=\"ifouterrorspps_max\" value=\"" + ifouterrorspps_max + "\">");
									}
								%>
							</tr>

							<tr><td HEIGHT="15"></td></tr>
							<tr>
								<td>&nbsp;</td>
								<td>超出阀值的次数(发告警)</td>
								<td><input name="text_7" type="text" style="width:150px" value="<%= warningnum%>"></td>
								<td colspan="2">&nbsp;</td>
							</tr>
							<tr><td HEIGHT="15"></td></tr>
							<tr>
								<td>&nbsp;</td>
								<td>发出阀值告警时的告警级别</td>
								<td>
									<select name="select_1" style="width:150px">
									<%
										switch (num_warninglevel) {
											case 1: {
												out.println("<option value=\"1\" selected>正常日志</option>");
												out.println("<option value=\"2\">提示告警</option>");
												out.println("<option value=\"3\">一般告警</option>");
												out.println("<option value=\"4\">严重告警</option>");
												out.println("<option value=\"5\">紧急告警</option>");
												break;
											}
											case 2: {
												out.println("<option value=\"1\">正常日志</option>");
												out.println("<option value=\"2\" selected>提示告警</option>");
												out.println("<option value=\"3\">一般告警</option>");
												out.println("<option value=\"4\">严重告警</option>");
												out.println("<option value=\"5\">紧急告警</option>");
												break;
											}
											case 3: {
												out.println("<option value=\"1\">正常日志</option>");
												out.println("<option value=\"2\">提示告警</option>");
												out.println("<option value=\"3\" selected>一般告警</option>");
												out.println("<option value=\"4\">严重告警</option>");
												out.println("<option value=\"5\">紧急告警</option>");
												break;
											}
											case 4: {
												out.println("<option value=\"1\">正常日志</option>");
												out.println("<option value=\"2\">提示告警</option>");
												out.println("<option value=\"3\">一般告警</option>");
												out.println("<option value=\"4\" selected>严重告警</option>");
												out.println("<option value=\"5\">紧急告警</option>");
												break;
											}
											case 5: {
												out.println("<option value=\"1\">正常日志</option>");
												out.println("<option value=\"2\">提示告警</option>");
												out.println("<option value=\"3\">一般告警</option>");
												out.println("<option value=\"4\">严重告警</option>");
												out.println("<option value=\"5\" selected>紧急告警</option>");
												break;
											}
										}
									%>
									</select>
								</td>
								<td>&nbsp;</td>
								<td>发出恢复告警时的告警级别</td>
								<td>
									<select name="select_2" style="width:150px">
									<%
										switch (num_reinstatelevel) {
			
											case 1: {
												out.println("<option value=\"1\" selected>正常日志</option>");
												out.println("<option value=\"2\">提示告警</option>");
												out.println("<option value=\"3\">一般告警</option>");
												out.println("<option value=\"4\">严重告警</option>");
												out.println("<option value=\"5\">紧急告警</option>");
												break;
											}
											case 2: {
												out.println("<option value=\"1\">正常日志</option>");
												out.println("<option value=\"2\" selected>提示告警</option>");
												out.println("<option value=\"3\">一般告警</option>");
												out.println("<option value=\"4\">严重告警</option>");
												out.println("<option value=\"5\">紧急告警</option>");
												break;
											}
											case 3: {
												out.println("<option value=\"1\">正常日志</option>");
												out.println("<option value=\"2\">提示告警</option>");
												out.println("<option value=\"3\" selected>一般告警</option>");
												out.println("<option value=\"4\">严重告警</option>");
												out.println("<option value=\"5\">紧急告警</option>");
												break;
											}
											case 4: {
												out.println("<option value=\"1\">正常日志</option>");
												out.println("<option value=\"2\">提示告警</option>");
												out.println("<option value=\"3\">一般告警</option>");
												out.println("<option value=\"4\" selected>严重告警</option>");
												out.println("<option value=\"5\">紧急告警</option>");
												break;
											}
											case 5: {
												out.println("<option value=\"1\">正常日志</option>");
												out.println("<option value=\"2\">提示告警</option>");
												out.println("<option value=\"3\">一般告警</option>");
												out.println("<option value=\"4\">严重告警</option>");
												out.println("<option value=\"5\" selected>紧急告警</option>");
												break;
											}
										}
									%>
									</select>
								</td>
							</tr>
							<tr><td HEIGHT="15"></td></tr>
						</table>
					</td>
				</tr>
			</table>
		</td>
	</tr>
	<tr>
		<td id="test2" style="display:none" bgcolor=#000000>
			<table width="100%" border="0" cellspacing="0" cellpadding="0">
				<tr>
					<td class="blue_title">
						<table width="100%" border="0" cellspacing="0" cellpadding="0">
							<tr>
								<td class="blue_title" width="8%">参数配置</td>
								<td>&nbsp;</td>
							</tr>
						</table>
					</td>
				</tr>
				<tr>
					<td class="column">
						<table width="100%" border="0" cellspacing="0" cellpadding="0">
							<tr><td HEIGHT="30"></td></tr>
							<tr>
								<td>
									<table width="100%" border="0" cellspacing="0" cellpadding="0">
										<tr>
											<td width="5%"></td>
											<%
												if (overper.equals("-1")){
													out.println("<td align=\"left\"><input name=\"checkbox_7\" type=\"checkbox\" onclick=\"javascript:CheckChange('7');\">启用动态阀值告警</td>");
												}
												else {
													out.println("<td align=\"left\"><input name=\"checkbox_7\" type=\"checkbox\" onclick=\"javascript:CheckChange('7');\" checked>启用动态阀值告警</td>");
												}
											%>
										</tr>
									</table>
								</td>
							</tr>
							<tr>
								<td>
									<table width="100%" border="0" cellspacing="0" cellpadding="0">
										<tr>
											<td height="20"></td>
										</tr>
										<tr>
											<td width="2%">&nbsp;</td>
											<td width="22%">超出动态阀值的百分比(%)</td>
											<td width="19%">
											<%
												if (overper.equals("-1")) {
													out.println("<input name=\"text_8\" type=\"text\" style=\"width:150px\" class=\"bk\" value=\"-1\" disabled>");
													out.println("<input type=\"hidden\" name=\"overper\" value=\"\">");
												}
												else {
													out.println("<input name=\"text_8\" type=\"text\" style=\"width:150px\" value=\"" + overper + "\"></td>");
													out.println("<input type=\"hidden\" name=\"overper\" value=\"" + overper + "\">");
												}
											%>
											</td>
											<td width="9%" align="left">(0-100%)</td>
											<td width="2%">&nbsp;</td>
											<td width="20%">超出百分比次数(发告警)</td>
											<td width="26%">
											<%
												if (overper.equals("-1")) {
													out.println("<input name=\"text_9\" type=\"text\" style=\"width:150px\" class=\"bk\" value=\"-1\" disabled>");
													out.println("<input type=\"hidden\" name=\"overnum\" value=\"\">");
												}
												else {
													out.println("<input name=\"text_9\" type=\"text\" style=\"width:150px\" value=\"" + overnum + "\">");
													out.println("<input type=\"hidden\" name=\"overnum\" value=\"" + overnum + "\">");
												}
											%>
											</td>
										</tr>
										<tr>
											<td height="15"></td>
										</tr>
										<tr>
											<td>&nbsp;</td>
											<td>生成动态阀值的天数(天)</td>
											<td>
											<%
												if (overper.equals("-1")) {
													out.println("<input name=\"text_10\" type=\"text\" style=\"width:150px\" class=\"bk\" value=\"-1\" disabled>");
													out.println("<input type=\"hidden\" name=\"com_day\" value=\"\">");
												}
												else {
													out.println("<input name=\"text_10\" type=\"text\" style=\"width:150px\" value=\"" + com_day + "\">");
													out.println("<input type=\"hidden\" name=\"com_day\" value=\"" + com_day + "\">");
												}
											%>
											</td>
											<td colspan="4" align="left">(用来做为比较标准阀值的天数,最大为3天)</td>
										</tr>
										<tr>
											<td height="15"></td>
										</tr>
										<tr>
											<td>&nbsp;</td>
											<td>发出动态阀值告警时的级别</td>
											<td>
											<%
												if (overper.equals("-1")) {
													out.println("<select name=\"select_3\" style=\"width:150px\" disabled>");
													out.println("<option value=\"1\">正常日志</option>");
													out.println("<option value=\"2\">提示告警</option>");
													out.println("<option value=\"3\">一般告警</option>");
													out.println("<option value=\"4\">严重告警</option>");
													out.println("<option value=\"5\">紧急告警</option>");
													out.println("</select>");
												}
												else {
													out.println("<select name=\"select_3\" style=\"width:150px\">");
													switch (num_overlevel) {
														case 1:{
															out.println("<option value=\"1\" selected>正常日志</option>");
															out.println("<option value=\"2\">提示告警</option>");
															out.println("<option value=\"3\">一般告警</option>");
															out.println("<option value=\"4\">严重告警</option>");
															out.println("<option value=\"5\">紧急告警</option>");
															break;
														}
														case 2:{
															out.println("<option value=\"1\">正常日志</option>");
															out.println("<option value=\"2\" selected>提示告警</option>");
															out.println("<option value=\"3\">一般告警</option>");
															out.println("<option value=\"4\">严重告警</option>");
															out.println("<option value=\"5\">紧急告警</option>");
															break;
														}
														case 3:{
															out.println("<option value=\"1\">正常日志</option>");
															out.println("<option value=\"2\">提示告警</option>");
															out.println("<option value=\"3\" selected>一般告警</option>");
															out.println("<option value=\"4\">严重告警</option>");
															out.println("<option value=\"5\">紧急告警</option>");
															break;
														}
														case 4:{
															out.println("<option value=\"1\">正常日志</option>");
															out.println("<option value=\"2\">提示告警</option>");
															out.println("<option value=\"3\">一般告警</option>");
															out.println("<option value=\"4\" selected>严重告警</option>");
															out.println("<option value=\"5\">紧急告警</option>");
															break;
														}
														case 5:{
															out.println("<option value=\"1\">正常日志</option>");
															out.println("<option value=\"2\">提示告警</option>");
															out.println("<option value=\"3\">一般告警</option>");
															out.println("<option value=\"4\">严重告警</option>");
															out.println("<option value=\"5\" selected>紧急告警</option>");
															break;
														}
													}
													out.println("</select>");
												}
											%>
											</td>
											<td>&nbsp;</td>
											<td>&nbsp;</td>
											<td>发出恢复告警时的级别</td>
											<td>
											<%
												if (overper.equals("-1")) {
													out.println("<select name=\"select_4\" style=\"width:150px\" disabled>");
													out.println("<option value=\"1\">正常日志</option>");
													out.println("<option value=\"2\">提示告警</option>");
													out.println("<option value=\"3\">一般告警</option>");
													out.println("<option value=\"4\">严重告警</option>");
													out.println("<option value=\"5\">紧急告警</option>");
													out.println("</select>");
												}
												else {
													out.println("<select name=\"select_4\" style=\"width:150px\">");
													switch (num_reinoverlevel) {
														case 1:{
															out.println("<option value=\"1\" selected>正常日志</option>");
															out.println("<option value=\"2\">提示告警</option>");
															out.println("<option value=\"3\">一般告警</option>");
															out.println("<option value=\"4\">严重告警</option>");
															out.println("<option value=\"5\">紧急告警</option>");
															break;
														}
														case 2:{
															out.println("<option value=\"1\">正常日志</option>");
															out.println("<option value=\"2\" selected>提示告警</option>");
															out.println("<option value=\"3\">一般告警</option>");
															out.println("<option value=\"4\">严重告警</option>");
															out.println("<option value=\"5\">紧急告警</option>");
															break;
														}
														case 3:{
															out.println("<option value=\"1\">正常日志</option>");
															out.println("<option value=\"2\">提示告警</option>");
															out.println("<option value=\"3\" selected>一般告警</option>");
															out.println("<option value=\"4\">严重告警</option>");
															out.println("<option value=\"5\">紧急告警</option>");
															break;
														}
														case 4:{
															out.println("<option value=\"1\">正常日志</option>");
															out.println("<option value=\"2\">提示告警</option>");
															out.println("<option value=\"3\">一般告警</option>");
															out.println("<option value=\"4\" selected>严重告警</option>");
															out.println("<option value=\"5\">紧急告警</option>");
															break;
														}
														case 5:{
															out.println("<option value=\"1\">正常日志</option>");
															out.println("<option value=\"2\">提示告警</option>");
															out.println("<option value=\"3\">一般告警</option>");
															out.println("<option value=\"4\">严重告警</option>");
															out.println("<option value=\"5\" selected>紧急告警</option>");
															break;
														}
													}
													out.println("</select>");
												}
											%>
											</td>
										</tr>
										<tr>
											<td height="15"></td>
										</tr>
									</table></td>
								</tr>
							</table>
						</td>
					</tr>
				</table>
			</td>
		</tr>
		<tr>
			<td id="test3" style="display:none" bgcolor=#000000>
				<table width="100%" border="0" cellspacing="0" cellpadding="0">
					<tr>
						<td class="blue_title">
							<table width="100%" border="0" cellspacing="0" cellpadding="0">
								<tr>
									<td class="blue_title" width="8%">参数配置</td>
									<td>&nbsp;</td>
								</tr>
							</table>
						</td>
					</tr>
					<tr>
						<td class="column">
							<table width="100%" border="0" cellspacing="0" cellpadding="0">
								<tr><td height="20"></td></tr>
								<tr>
									<td>
										<table width="100%" border="0" cellspacing="0" cellpadding="0">
											<tr>
												<td width="2%">&nbsp;</td>
												<td>
												<%
													if (intbflag.equals("0")) {
														out.println("<input name=\"checkbox_8\" type=\"checkbox\" onclick=\"javascript:CheckChange('8');\">");
													}
													else {
														out.println("<input name=\"checkbox_8\" type=\"checkbox\" onclick=\"javascript:CheckChange('8');\" value=\"1\" checked>");
													}
												%>
												&nbsp;启用流入流量突变告警配置
												</td>
											</tr>
										</table>
									</td>
								</tr>
								<tr>
									<td>
										<table>
											<tr><td height="15"></td></tr>
											<tr>
												<td width="2%">&nbsp;</td>
												<td width="23%">流入速率变化率阀值(%)</td>
												<td width="23%">
												<%
													if (intbflag.equals("0")) {
														out.println("<input name=\"text_11\" type=\"text\" style=\"width:150px\" class=\"bk\" value=\"-1\" disabled>");
														out.println("<input type=\"hidden\" name=\"ifinoctets\" value=\"\">");
													}
													else {
														out.println("<input name=\"text_11\" type=\"text\" style=\"width:150px\" value=\"" + ifinoctets + "\">");
														out.println("<input type=\"hidden\" name=\"ifinoctets\" value=\"" + ifinoctets + "\">");
													}
												%>
												</td>
												<td width="4%">&nbsp;</td>
												<td width="23%">流入速率突变告警操作符</td>
												<td width="23%">
												<%
													if (intbflag.equals("0")) {
														out.println("<select name=\"select_5\" style=\"width:50px\" disabled>");
														out.println("<option value=\"1\">></option>");
														out.println("<option value=\"2\">=</option>");
														out.println("<option value=\"3\"><</option>");
														out.println("</select>");
													}
													else {
														out.println("<select name=\"select_5\" style=\"width:50px\">");
														switch (num_inoperation) {
															case 1:{
																out.println("<option value=\"1\" selected>></option>");
																out.println("<option value=\"2\">=</option>");
																out.println("<option value=\"3\"><</option>");
																break;
															}
															case 2:{
																out.println("<option value=\"1\">></option>");
																out.println("<option value=\"2\" selected>=</option>");
																out.println("<option value=\"3\"><</option>");
																break;
															}
															case 3:{
																out.println("<option value=\"1\">></option>");
																out.println("<option value=\"2\">=</option>");
																out.println("<option value=\"3\" selected><</option>");
																break;
															}
														}
														out.println("</select>");
													}
												%>
												</td>
											</tr>
											<tr><td height="15"></td></tr>
											<tr>
												<td>&nbsp;</td>
												<td>流入速率突变告警级别</td>
												<td>
												<%
													if (intbflag.equals("0")) {
														out.println("<select name=\"select_6\" style=\"width:150px\" disabled>");
														out.println("<option value=\"1\">正常日志</option>");
														out.println("<option value=\"2\">提示告警</option>");
														out.println("<option value=\"3\">一般告警</option>");
														out.println("<option value=\"4\">严重告警</option>");
														out.println("<option value=\"5\">紧急告警</option>");
														out.println("</select>");
													}
													else {
														out.println("<select name=\"select_6\" style=\"width:150px\">");
														switch (num_inwarninglevel) {
															case 1:{
																out.println("<option value=\"1\" selected>正常日志</option>");
																out.println("<option value=\"2\">提示告警</option>");
																out.println("<option value=\"3\">一般告警</option>");
																out.println("<option value=\"4\">严重告警</option>");
																out.println("<option value=\"5\">紧急告警</option>");
																break;
															}
															case 2:{
																out.println("<option value=\"1\">正常日志</option>");
																out.println("<option value=\"2\" selected>提示告警</option>");
																out.println("<option value=\"3\">一般告警</option>");
																out.println("<option value=\"4\">严重告警</option>");
																out.println("<option value=\"5\">紧急告警</option>");
																break;
															}
															case 3:{
																out.println("<option value=\"1\">正常日志</option>");
																out.println("<option value=\"2\">提示告警</option>");
																out.println("<option value=\"3\" selected>一般告警</option>");
																out.println("<option value=\"4\">严重告警</option>");
																out.println("<option value=\"5\">紧急告警</option>");
																break;
															}
															case 4:{
																out.println("<option value=\"1\">正常日志</option>");
																out.println("<option value=\"2\">提示告警</option>");
																out.println("<option value=\"3\">一般告警</option>");
																out.println("<option value=\"4\" selected>严重告警</option>");
																out.println("<option value=\"5\">紧急告警</option>");
																break;
															}
															case 5:{
																out.println("<option value=\"1\">正常日志</option>");
																out.println("<option value=\"2\">提示告警</option>");
																out.println("<option value=\"3\">一般告警</option>");
																out.println("<option value=\"4\">严重告警</option>");
																out.println("<option value=\"5\" selected>紧急告警</option>");
																break;
															}
														}
														out.println("</select>");
													}
												%>
												</td>
												<td>&nbsp;</td>
												<td>流入速率恢复突变告警级别</td>
												<td>
												<%
													if (intbflag.equals("0")) {
														out.println("<select name=\"select_7\" style=\"width:150px\" disabled>");
														out.println("<option value=\"1\">正常日志</option>");
														out.println("<option value=\"2\">提示告警</option>");
														out.println("<option value=\"3\">一般告警</option>");
														out.println("<option value=\"4\">严重告警</option>");
														out.println("<option value=\"5\">紧急告警</option>");
														out.println("</select>");
													}
													else {
														out.println("<select name=\"select_7\" style=\"width:150px\">");
														switch (num_inreinstatelevel) {
															case 1:{
																out.println("<option value=\"1\" selected>正常日志</option>");
																out.println("<option value=\"2\">提示告警</option>");
																out.println("<option value=\"3\">一般告警</option>");
																out.println("<option value=\"4\">严重告警</option>");
																out.println("<option value=\"5\">紧急告警</option>");
																break;
															}
															case 2:{
																out.println("<option value=\"1\">正常日志</option>");
																out.println("<option value=\"2\" selected>提示告警</option>");
																out.println("<option value=\"3\">一般告警</option>");
																out.println("<option value=\"4\">严重告警</option>");
																out.println("<option value=\"5\">紧急告警</option>");
																break;
															}
															case 3:{
																out.println("<option value=\"1\">正常日志</option>");
																out.println("<option value=\"2\">提示告警</option>");
																out.println("<option value=\"3\" selected>一般告警</option>");
																out.println("<option value=\"4\">严重告警</option>");
																out.println("<option value=\"5\">紧急告警</option>");
																break;
															}
															case 4:{
																out.println("<option value=\"1\">正常日志</option>");
																out.println("<option value=\"2\">提示告警</option>");
																out.println("<option value=\"3\">一般告警</option>");
																out.println("<option value=\"4\" selected>严重告警</option>");
																out.println("<option value=\"5\">紧急告警</option>");
																break;
															}
															case 5:{
																out.println("<option value=\"1\">正常日志</option>");
																out.println("<option value=\"2\">提示告警</option>");
																out.println("<option value=\"3\">一般告警</option>");
																out.println("<option value=\"4\">严重告警</option>");
																out.println("<option value=\"5\" selected>紧急告警</option>");
																break;
															}
														}
														out.println("</select>");
													}
												%>
												</td>
											</tr>
										</table>
									</td>
								</tr>
								<tr><td height="20"></td></tr>
								<tr>
									<td>
										<table width="100%" border="0" cellspacing="0" cellpadding="0">
											<tr>
												<td width="2%">&nbsp;</td>
												<td>
												<%
													if (outtbflag.equals("0")) {
														out.println("<input name=\"checkbox_9\" type=\"checkbox\" onclick=\"javascript:CheckChange('9');\">");
													}
													else {
														out.println("<input name=\"checkbox_9\" type=\"checkbox\" onclick=\"javascript:CheckChange('9');\" checked value=\"1\">");
													}
												%>
												&nbsp;启用流出流量突变告警配置</td>
											</tr>
										</table>
									</td>
								</tr>
								<tr><td height="15"></td></tr>
								<tr>
									<td>
										<table width="100%" border="0" cellspacing="0" cellpadding="0">
											<tr>
												<td width="2%">&nbsp;</td>
												<td width="23%">流出速率变化率阀值(%)</td>
												<td width="23%">
												<%
													if (outtbflag.equals("0")) {
														out.println("<input name=\"text_12\" type=\"text\" style=\"width:150px\" class=\"bk\" value=\"-1\" disabled>");
														out.println("<input type=\"hidden\" name=\"ifoutoctets\" value=\"\">");
													}
													else {
														out.println("<input name=\"text_12\" type=\"text\" style=\"width:150px\" value=\"" + ifoutoctets + "\">");
														out.println("<input type=\"hidden\" name=\"ifoutoctets\" value=\"" + ifoutoctets + "\">");
													}
												%>
												</td>
												<td width="4%">&nbsp;</td>
												<td width="23%">流出速率突变告警操作符</td>
												<td width="23%">
												<%
													if (outtbflag.equals("0")) {
														out.println("<select name=\"select_8\" style=\"width:50px\" disabled>");
														out.println("<option value=\"1\">></option>");
														out.println("<option value=\"2\">=</option>");
														out.println("<option value=\"3\"><</option>");
														out.println("</select>");
														}
														else {
															out.println("<select name=\"select_8\" style=\"width:50px\">");
															switch (num_outoperation) {
																case 1:{
																	out.println("<option value=\"1\" selected>></option>");
																	out.println("<option value=\"2\">=</option>");
																	out.println("<option value=\"3\"><</option>");
																	break;
																}
																case 2:{
																	out.println("<option value=\"1\">></option>");
																	out.println("<option value=\"2\" selected>=</option>");
																	out.println("<option value=\"3\"><</option>");
																	break;
																}
																case 3:{
																	out.println("<option value=\"1\">></option>");
																	out.println("<option value=\"2\">=</option>");
																	out.println("<option value=\"3\" selected><</option>");
																	break;
																}
															}
															out.println("</select>");
														}
													%>
												</td>
											</tr>
											<tr><td height="15"></td></tr>
											<tr>
												<td>&nbsp;</td>
												<td>流出速率突变告警级别</td>
												<td>
												<%
													if (outtbflag.equals("0")) {
														out.println("<select name=\"select_9\" style=\"width:150px\" disabled>");
														out.println("<option value=\"1\">正常日志</option>");
														out.println("<option value=\"2\">提示告警</option>");
														out.println("<option value=\"3\">一般告警</option>");
														out.println("<option value=\"4\">严重告警</option>");
														out.println("<option value=\"5\">紧急告警</option>");
														out.println("</select>");
													}
													else {
														out.println("<select name=\"select_9\" style=\"width:150px\">");
														switch (num_outwarninglevel) {
															case 1:{
																out.println("<option value=\"1\" selected>正常日志</option>");
																out.println("<option value=\"2\">提示告警</option>");
																out.println("<option value=\"3\">一般告警</option>");
																out.println("<option value=\"4\">严重告警</option>");
																out.println("<option value=\"5\">紧急告警</option>");
																break;
															}
															case 2:{
																out.println("<option value=\"1\">正常日志</option>");
																out.println("<option value=\"2\" selected>提示告警</option>");
																out.println("<option value=\"3\">一般告警</option>");
																out.println("<option value=\"4\">严重告警</option>");
																out.println("<option value=\"5\">紧急告警</option>");
																break;
															}
															case 3:{
																out.println("<option value=\"1\">正常日志</option>");
																out.println("<option value=\"2\">提示告警</option>");
																out.println("<option value=\"3\" selected>一般告警</option>");
																out.println("<option value=\"4\">严重告警</option>");
																out.println("<option value=\"5\">紧急告警</option>");
																break;
															}
															case 4:{
																out.println("<option value=\"1\">正常日志</option>");
																out.println("<option value=\"2\">提示告警</option>");
																out.println("<option value=\"3\">一般告警</option>");
																out.println("<option value=\"4\" selected>严重告警</option>");
																out.println("<option value=\"5\">紧急告警</option>");
																break;
															}
															case 5:{
																out.println("<option value=\"1\">正常日志</option>");
																out.println("<option value=\"2\">提示告警</option>");
																out.println("<option value=\"3\">一般告警</option>");
																out.println("<option value=\"4\">严重告警</option>");
																out.println("<option value=\"5\" selected>紧急告警</option>");
																break;
															}
														}
														out.println("</select>");
													}
												%>
												</td>
												<td>&nbsp;</td>
												<td>流出速率恢复突变告警级别</td>
												<td>
												<%
													if (outtbflag.equals("0")) {
														out.println("<select name=\"select_10\" style=\"width:150px\" disabled>");
														out.println("<option value=\"1\">正常日志</option>");
														out.println("<option value=\"2\">提示告警</option>");
														out.println("<option value=\"3\">一般告警</option>");
														out.println("<option value=\"4\">严重告警</option>");
														out.println("<option value=\"5\">紧急告警</option>");
														out.println("</select>");
													}
													else {
														out.println("<select name=\"select_10\" style=\"width:150px\">");
														switch (num_outreinstatelevel) {
															case 1:{
																out.println("<option value=\"1\" selected>正常日志</option>");
																out.println("<option value=\"2\">提示告警</option>");
																out.println("<option value=\"3\">一般告警</option>");
																out.println("<option value=\"4\">严重告警</option>");
																out.println("<option value=\"5\">紧急告警</option>");
																break;
															}
															case 2:{
																out.println("<option value=\"1\">正常日志</option>");
																out.println("<option value=\"2\" selected>提示告警</option>");
																out.println("<option value=\"3\">一般告警</option>");
																out.println("<option value=\"4\">严重告警</option>");
																out.println("<option value=\"5\">紧急告警</option>");
																break;
															}
															case 3:{
																out.println("<option value=\"1\">正常日志</option>");
																out.println("<option value=\"2\">提示告警</option>");
																out.println("<option value=\"3\" selected>一般告警</option>");
																out.println("<option value=\"4\">严重告警</option>");
																out.println("<option value=\"5\">紧急告警</option>");
																break;
															}
															case 4:{
																out.println("<option value=\"1\">正常日志</option>");
																out.println("<option value=\"2\">提示告警</option>");
																out.println("<option value=\"3\">一般告警</option>");
																out.println("<option value=\"4\" selected>严重告警</option>");
																out.println("<option value=\"5\">紧急告警</option>");
																break;
															}
															case 5:{
																out.println("<option value=\"1\">正常日志</option>");
																out.println("<option value=\"2\">提示告警</option>");
																out.println("<option value=\"3\">一般告警</option>");
																out.println("<option value=\"4\">严重告警</option>");
																out.println("<option value=\"5\" selected>紧急告警</option>");
																break;
															}
														}
														out.println("</select>");
													}
												%>
												</td>
											</tr>
											<tr><td height="15"></td></tr>
										</table>
									</td>
								</tr>
							</table>
						</td>
					</tr>
				</table>
			</td>
		</tr>
	</table>