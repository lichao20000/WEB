
<table width="100%" border="0" cellspacing="0" cellpadding="0">
	<tr>
		<td>
			<table width="100%" border="0" align="center" cellpadding="0" cellspacing="0" valign="middle">
				<tr> 
				  <TH width="159" height="25" class="button_onblue" id="td1" onClick="location.href='javascript:showpage(1);'">�̶���ֵ�澯����</TH>
				  <TH width="159" height="25" class="button_outblue" id="td2" onClick="location.href='javascript:showpage(2);'">��̬��ֵ�澯����</TH>
				  <TH width="159" height="25" class="button_outblue" id="td3" onClick="location.href='javascript:showpage(3);'">�˿�����ͻ��澯����</TH>
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
								<td class="blue_title" width="8%">��������</td>
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
										out.println("<td><input name=\"checkbox_1\" type=\"checkbox\" onclick=\"javascript:CheckChange('1');\">�˿�������������ʷ�ֵ(%)</td>");
										out.println("<td><input name=\"text_1\" type=\"text\" class=\"bk\" style=\"width:150px\" value=\"-1\" disabled></td>");
										out.println("<input type=\"hidden\" name=\"ifinoctetsbps_max\" value=\"\">");
									}
									else {
										out.println("<td><input name=\"checkbox_1\" type=\"checkbox\" onclick=\"javascript:CheckChange('1');\" checked>�˿�������������ʷ�ֵ(%)</td>");
										out.println("<td><input name=\"text_1\" type=\"text\" style=\"width:150px\" value=\"" + ifinoctetsbps_max + "\"></td>");
										out.println("<input type=\"hidden\" name=\"ifinoctetsbps_max\" value=\"" + ifinoctetsbps_max + "\">");
									}
								%>
								<td width="2%">&nbsp;</td>
								<%
									if (num_ifoutoctetsbps_max < 0) {
										out.println("<td><input name=\"checkbox_2\" type=\"checkbox\" onclick=\"javascript:CheckChange('2');\">�˿��������������ʷ�ֵ(%)</td>");
										out.println("<td><input name=\"text_2\" type=\"text\" class=\"bk\" style=\"width:150px\" value=\"-1\" disabled></td>");
										out.println("<input type=\"hidden\" name=\"ifoutoctetsbps_max\" value=\"\">");
									}
									else {
										out.println("<td><input name=\"checkbox_2\" type=\"checkbox\" onclick=\"javascript:CheckChange('2');\" checked>�˿��������������ʷ�ֵ(%)</td>");
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
										out.println("<td><input name=\"checkbox_3\" type=\"checkbox\" onclick=\"javascript:CheckChange('3');\">�˿����붪���ʷ�ֵ(%)</td>");
										out.println("<td><input name=\"text_3\" type=\"text\" class=\"bk\" style=\"width:150px\" value=\"-1\" disabled></td>");
										out.println("<input type=\"hidden\" name=\"ifindiscardspps_max\" value=\"\">");
									}
									else {
										out.println("<td><input name=\"checkbox_3\" type=\"checkbox\" onclick=\"javascript:CheckChange('3');\" checked>�˿����붪���ʷ�ֵ(%)</td>");
										out.println("<td><input name=\"text_3\" type=\"text\" style=\"width:150px\" value=\"" + ifindiscardspps_max + "\"></td>");
										out.println("<input type=\"hidden\" name=\"ifindiscardspps_max\" value=\"" + ifindiscardspps_max + "\">");
									}
								%>
								<td>&nbsp;</td>
								<%
									if (num_ifoutdiscardspps_max < 0) {
										out.println("<td><input name=\"checkbox_4\" type=\"checkbox\" onclick=\"javascript:CheckChange('4');\">�˿����������ʷ�ֵ(%)</td>");
										out.println("<td><input name=\"text_4\" type=\"text\" class=\"bk\" style=\"width:150px\" value=\"-1\" disabled></td>");
										out.println("<input type=\"hidden\" name=\"ifoutdiscardspps_max\" value=\"\">");
									}
									else {
										out.println("<td><input name=\"checkbox_4\" type=\"checkbox\" onclick=\"javascript:CheckChange('4');\" checked>�˿����������ʷ�ֵ(%)</td>");
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
										out.println("<td><input name=\"checkbox_5\" type=\"checkbox\" onclick=\"javascript:CheckChange('5');\">�˿��������ʷ�ֵ(%)</td>");
										out.println("<td><input name=\"text_5\" type=\"text\" class=\"bk\" style=\"width:150px\" value=\"-1\" disabled></td>");
										out.println("<input type=\"hidden\" name=\"ifinerrorspps_max\" value=\"\">");
									}
									else {
										out.println("<td><input name=\"checkbox_5\" type=\"checkbox\" onclick=\"javascript:CheckChange('5');\" checked>�˿��������ʷ�ֵ(%)</td>");
										out.println("<td><input name=\"text_5\" type=\"text\" style=\"width:150px\" value=\"" + ifinerrorspps_max + "\"></td>");
										out.println("<input type=\"hidden\" name=\"ifinerrorspps_max\" value=\"" + ifinerrorspps_max + "\">");
									}
								%>
								<td>&nbsp;</td>
								<%
									if (num_ifouterrorspps_max < 0) {
										out.println("<td><input name=\"checkbox_6\" type=\"checkbox\" onclick=\"javascript:CheckChange('6');\">�˿���������ʷ�ֵ(%)</td>");
										out.println("<td><input name=\"text_6\" type=\"text\" class=\"bk\" style=\"width:150px\" value=\"-1\" disabled></td>");
										out.println("<input type=\"hidden\" name=\"ifouterrorspps_max\" value=\"\">");
									}
									else {
										out.println("<td><input name=\"checkbox_6\" type=\"checkbox\" onclick=\"javascript:CheckChange('6');\" checked>�˿���������ʷ�ֵ(%)</td>");
										out.println("<td><input name=\"text_6\" type=\"text\" style=\"width:150px\" value=\"" + ifouterrorspps_max + "\"></td>");
										out.println("<input type=\"hidden\" name=\"ifouterrorspps_max\" value=\"" + ifouterrorspps_max + "\">");
									}
								%>
							</tr>

							<tr><td HEIGHT="15"></td></tr>
							<tr>
								<td>&nbsp;</td>
								<td>������ֵ�Ĵ���(���澯)</td>
								<td><input name="text_7" type="text" style="width:150px" value="<%= warningnum%>"></td>
								<td colspan="2">&nbsp;</td>
							</tr>
							<tr><td HEIGHT="15"></td></tr>
							<tr>
								<td>&nbsp;</td>
								<td>������ֵ�澯ʱ�ĸ澯����</td>
								<td>
									<select name="select_1" style="width:150px">
									<%
										switch (num_warninglevel) {
											case 1: {
												out.println("<option value=\"1\" selected>������־</option>");
												out.println("<option value=\"2\">��ʾ�澯</option>");
												out.println("<option value=\"3\">һ��澯</option>");
												out.println("<option value=\"4\">���ظ澯</option>");
												out.println("<option value=\"5\">�����澯</option>");
												break;
											}
											case 2: {
												out.println("<option value=\"1\">������־</option>");
												out.println("<option value=\"2\" selected>��ʾ�澯</option>");
												out.println("<option value=\"3\">һ��澯</option>");
												out.println("<option value=\"4\">���ظ澯</option>");
												out.println("<option value=\"5\">�����澯</option>");
												break;
											}
											case 3: {
												out.println("<option value=\"1\">������־</option>");
												out.println("<option value=\"2\">��ʾ�澯</option>");
												out.println("<option value=\"3\" selected>һ��澯</option>");
												out.println("<option value=\"4\">���ظ澯</option>");
												out.println("<option value=\"5\">�����澯</option>");
												break;
											}
											case 4: {
												out.println("<option value=\"1\">������־</option>");
												out.println("<option value=\"2\">��ʾ�澯</option>");
												out.println("<option value=\"3\">һ��澯</option>");
												out.println("<option value=\"4\" selected>���ظ澯</option>");
												out.println("<option value=\"5\">�����澯</option>");
												break;
											}
											case 5: {
												out.println("<option value=\"1\">������־</option>");
												out.println("<option value=\"2\">��ʾ�澯</option>");
												out.println("<option value=\"3\">һ��澯</option>");
												out.println("<option value=\"4\">���ظ澯</option>");
												out.println("<option value=\"5\" selected>�����澯</option>");
												break;
											}
										}
									%>
									</select>
								</td>
								<td>&nbsp;</td>
								<td>�����ָ��澯ʱ�ĸ澯����</td>
								<td>
									<select name="select_2" style="width:150px">
									<%
										switch (num_reinstatelevel) {
			
											case 1: {
												out.println("<option value=\"1\" selected>������־</option>");
												out.println("<option value=\"2\">��ʾ�澯</option>");
												out.println("<option value=\"3\">һ��澯</option>");
												out.println("<option value=\"4\">���ظ澯</option>");
												out.println("<option value=\"5\">�����澯</option>");
												break;
											}
											case 2: {
												out.println("<option value=\"1\">������־</option>");
												out.println("<option value=\"2\" selected>��ʾ�澯</option>");
												out.println("<option value=\"3\">һ��澯</option>");
												out.println("<option value=\"4\">���ظ澯</option>");
												out.println("<option value=\"5\">�����澯</option>");
												break;
											}
											case 3: {
												out.println("<option value=\"1\">������־</option>");
												out.println("<option value=\"2\">��ʾ�澯</option>");
												out.println("<option value=\"3\" selected>һ��澯</option>");
												out.println("<option value=\"4\">���ظ澯</option>");
												out.println("<option value=\"5\">�����澯</option>");
												break;
											}
											case 4: {
												out.println("<option value=\"1\">������־</option>");
												out.println("<option value=\"2\">��ʾ�澯</option>");
												out.println("<option value=\"3\">һ��澯</option>");
												out.println("<option value=\"4\" selected>���ظ澯</option>");
												out.println("<option value=\"5\">�����澯</option>");
												break;
											}
											case 5: {
												out.println("<option value=\"1\">������־</option>");
												out.println("<option value=\"2\">��ʾ�澯</option>");
												out.println("<option value=\"3\">һ��澯</option>");
												out.println("<option value=\"4\">���ظ澯</option>");
												out.println("<option value=\"5\" selected>�����澯</option>");
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
								<td class="blue_title" width="8%">��������</td>
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
													out.println("<td align=\"left\"><input name=\"checkbox_7\" type=\"checkbox\" onclick=\"javascript:CheckChange('7');\">���ö�̬��ֵ�澯</td>");
												}
												else {
													out.println("<td align=\"left\"><input name=\"checkbox_7\" type=\"checkbox\" onclick=\"javascript:CheckChange('7');\" checked>���ö�̬��ֵ�澯</td>");
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
											<td width="22%">������̬��ֵ�İٷֱ�(%)</td>
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
											<td width="20%">�����ٷֱȴ���(���澯)</td>
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
											<td>���ɶ�̬��ֵ������(��)</td>
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
											<td colspan="4" align="left">(������Ϊ�Ƚϱ�׼��ֵ������,���Ϊ3��)</td>
										</tr>
										<tr>
											<td height="15"></td>
										</tr>
										<tr>
											<td>&nbsp;</td>
											<td>������̬��ֵ�澯ʱ�ļ���</td>
											<td>
											<%
												if (overper.equals("-1")) {
													out.println("<select name=\"select_3\" style=\"width:150px\" disabled>");
													out.println("<option value=\"1\">������־</option>");
													out.println("<option value=\"2\">��ʾ�澯</option>");
													out.println("<option value=\"3\">һ��澯</option>");
													out.println("<option value=\"4\">���ظ澯</option>");
													out.println("<option value=\"5\">�����澯</option>");
													out.println("</select>");
												}
												else {
													out.println("<select name=\"select_3\" style=\"width:150px\">");
													switch (num_overlevel) {
														case 1:{
															out.println("<option value=\"1\" selected>������־</option>");
															out.println("<option value=\"2\">��ʾ�澯</option>");
															out.println("<option value=\"3\">һ��澯</option>");
															out.println("<option value=\"4\">���ظ澯</option>");
															out.println("<option value=\"5\">�����澯</option>");
															break;
														}
														case 2:{
															out.println("<option value=\"1\">������־</option>");
															out.println("<option value=\"2\" selected>��ʾ�澯</option>");
															out.println("<option value=\"3\">һ��澯</option>");
															out.println("<option value=\"4\">���ظ澯</option>");
															out.println("<option value=\"5\">�����澯</option>");
															break;
														}
														case 3:{
															out.println("<option value=\"1\">������־</option>");
															out.println("<option value=\"2\">��ʾ�澯</option>");
															out.println("<option value=\"3\" selected>һ��澯</option>");
															out.println("<option value=\"4\">���ظ澯</option>");
															out.println("<option value=\"5\">�����澯</option>");
															break;
														}
														case 4:{
															out.println("<option value=\"1\">������־</option>");
															out.println("<option value=\"2\">��ʾ�澯</option>");
															out.println("<option value=\"3\">һ��澯</option>");
															out.println("<option value=\"4\" selected>���ظ澯</option>");
															out.println("<option value=\"5\">�����澯</option>");
															break;
														}
														case 5:{
															out.println("<option value=\"1\">������־</option>");
															out.println("<option value=\"2\">��ʾ�澯</option>");
															out.println("<option value=\"3\">һ��澯</option>");
															out.println("<option value=\"4\">���ظ澯</option>");
															out.println("<option value=\"5\" selected>�����澯</option>");
															break;
														}
													}
													out.println("</select>");
												}
											%>
											</td>
											<td>&nbsp;</td>
											<td>&nbsp;</td>
											<td>�����ָ��澯ʱ�ļ���</td>
											<td>
											<%
												if (overper.equals("-1")) {
													out.println("<select name=\"select_4\" style=\"width:150px\" disabled>");
													out.println("<option value=\"1\">������־</option>");
													out.println("<option value=\"2\">��ʾ�澯</option>");
													out.println("<option value=\"3\">һ��澯</option>");
													out.println("<option value=\"4\">���ظ澯</option>");
													out.println("<option value=\"5\">�����澯</option>");
													out.println("</select>");
												}
												else {
													out.println("<select name=\"select_4\" style=\"width:150px\">");
													switch (num_reinoverlevel) {
														case 1:{
															out.println("<option value=\"1\" selected>������־</option>");
															out.println("<option value=\"2\">��ʾ�澯</option>");
															out.println("<option value=\"3\">һ��澯</option>");
															out.println("<option value=\"4\">���ظ澯</option>");
															out.println("<option value=\"5\">�����澯</option>");
															break;
														}
														case 2:{
															out.println("<option value=\"1\">������־</option>");
															out.println("<option value=\"2\" selected>��ʾ�澯</option>");
															out.println("<option value=\"3\">һ��澯</option>");
															out.println("<option value=\"4\">���ظ澯</option>");
															out.println("<option value=\"5\">�����澯</option>");
															break;
														}
														case 3:{
															out.println("<option value=\"1\">������־</option>");
															out.println("<option value=\"2\">��ʾ�澯</option>");
															out.println("<option value=\"3\" selected>һ��澯</option>");
															out.println("<option value=\"4\">���ظ澯</option>");
															out.println("<option value=\"5\">�����澯</option>");
															break;
														}
														case 4:{
															out.println("<option value=\"1\">������־</option>");
															out.println("<option value=\"2\">��ʾ�澯</option>");
															out.println("<option value=\"3\">һ��澯</option>");
															out.println("<option value=\"4\" selected>���ظ澯</option>");
															out.println("<option value=\"5\">�����澯</option>");
															break;
														}
														case 5:{
															out.println("<option value=\"1\">������־</option>");
															out.println("<option value=\"2\">��ʾ�澯</option>");
															out.println("<option value=\"3\">һ��澯</option>");
															out.println("<option value=\"4\">���ظ澯</option>");
															out.println("<option value=\"5\" selected>�����澯</option>");
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
									<td class="blue_title" width="8%">��������</td>
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
												&nbsp;������������ͻ��澯����
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
												<td width="23%">�������ʱ仯�ʷ�ֵ(%)</td>
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
												<td width="23%">��������ͻ��澯������</td>
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
												<td>��������ͻ��澯����</td>
												<td>
												<%
													if (intbflag.equals("0")) {
														out.println("<select name=\"select_6\" style=\"width:150px\" disabled>");
														out.println("<option value=\"1\">������־</option>");
														out.println("<option value=\"2\">��ʾ�澯</option>");
														out.println("<option value=\"3\">һ��澯</option>");
														out.println("<option value=\"4\">���ظ澯</option>");
														out.println("<option value=\"5\">�����澯</option>");
														out.println("</select>");
													}
													else {
														out.println("<select name=\"select_6\" style=\"width:150px\">");
														switch (num_inwarninglevel) {
															case 1:{
																out.println("<option value=\"1\" selected>������־</option>");
																out.println("<option value=\"2\">��ʾ�澯</option>");
																out.println("<option value=\"3\">һ��澯</option>");
																out.println("<option value=\"4\">���ظ澯</option>");
																out.println("<option value=\"5\">�����澯</option>");
																break;
															}
															case 2:{
																out.println("<option value=\"1\">������־</option>");
																out.println("<option value=\"2\" selected>��ʾ�澯</option>");
																out.println("<option value=\"3\">һ��澯</option>");
																out.println("<option value=\"4\">���ظ澯</option>");
																out.println("<option value=\"5\">�����澯</option>");
																break;
															}
															case 3:{
																out.println("<option value=\"1\">������־</option>");
																out.println("<option value=\"2\">��ʾ�澯</option>");
																out.println("<option value=\"3\" selected>һ��澯</option>");
																out.println("<option value=\"4\">���ظ澯</option>");
																out.println("<option value=\"5\">�����澯</option>");
																break;
															}
															case 4:{
																out.println("<option value=\"1\">������־</option>");
																out.println("<option value=\"2\">��ʾ�澯</option>");
																out.println("<option value=\"3\">һ��澯</option>");
																out.println("<option value=\"4\" selected>���ظ澯</option>");
																out.println("<option value=\"5\">�����澯</option>");
																break;
															}
															case 5:{
																out.println("<option value=\"1\">������־</option>");
																out.println("<option value=\"2\">��ʾ�澯</option>");
																out.println("<option value=\"3\">һ��澯</option>");
																out.println("<option value=\"4\">���ظ澯</option>");
																out.println("<option value=\"5\" selected>�����澯</option>");
																break;
															}
														}
														out.println("</select>");
													}
												%>
												</td>
												<td>&nbsp;</td>
												<td>�������ʻָ�ͻ��澯����</td>
												<td>
												<%
													if (intbflag.equals("0")) {
														out.println("<select name=\"select_7\" style=\"width:150px\" disabled>");
														out.println("<option value=\"1\">������־</option>");
														out.println("<option value=\"2\">��ʾ�澯</option>");
														out.println("<option value=\"3\">һ��澯</option>");
														out.println("<option value=\"4\">���ظ澯</option>");
														out.println("<option value=\"5\">�����澯</option>");
														out.println("</select>");
													}
													else {
														out.println("<select name=\"select_7\" style=\"width:150px\">");
														switch (num_inreinstatelevel) {
															case 1:{
																out.println("<option value=\"1\" selected>������־</option>");
																out.println("<option value=\"2\">��ʾ�澯</option>");
																out.println("<option value=\"3\">һ��澯</option>");
																out.println("<option value=\"4\">���ظ澯</option>");
																out.println("<option value=\"5\">�����澯</option>");
																break;
															}
															case 2:{
																out.println("<option value=\"1\">������־</option>");
																out.println("<option value=\"2\" selected>��ʾ�澯</option>");
																out.println("<option value=\"3\">һ��澯</option>");
																out.println("<option value=\"4\">���ظ澯</option>");
																out.println("<option value=\"5\">�����澯</option>");
																break;
															}
															case 3:{
																out.println("<option value=\"1\">������־</option>");
																out.println("<option value=\"2\">��ʾ�澯</option>");
																out.println("<option value=\"3\" selected>һ��澯</option>");
																out.println("<option value=\"4\">���ظ澯</option>");
																out.println("<option value=\"5\">�����澯</option>");
																break;
															}
															case 4:{
																out.println("<option value=\"1\">������־</option>");
																out.println("<option value=\"2\">��ʾ�澯</option>");
																out.println("<option value=\"3\">һ��澯</option>");
																out.println("<option value=\"4\" selected>���ظ澯</option>");
																out.println("<option value=\"5\">�����澯</option>");
																break;
															}
															case 5:{
																out.println("<option value=\"1\">������־</option>");
																out.println("<option value=\"2\">��ʾ�澯</option>");
																out.println("<option value=\"3\">һ��澯</option>");
																out.println("<option value=\"4\">���ظ澯</option>");
																out.println("<option value=\"5\" selected>�����澯</option>");
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
												&nbsp;������������ͻ��澯����</td>
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
												<td width="23%">�������ʱ仯�ʷ�ֵ(%)</td>
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
												<td width="23%">��������ͻ��澯������</td>
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
												<td>��������ͻ��澯����</td>
												<td>
												<%
													if (outtbflag.equals("0")) {
														out.println("<select name=\"select_9\" style=\"width:150px\" disabled>");
														out.println("<option value=\"1\">������־</option>");
														out.println("<option value=\"2\">��ʾ�澯</option>");
														out.println("<option value=\"3\">һ��澯</option>");
														out.println("<option value=\"4\">���ظ澯</option>");
														out.println("<option value=\"5\">�����澯</option>");
														out.println("</select>");
													}
													else {
														out.println("<select name=\"select_9\" style=\"width:150px\">");
														switch (num_outwarninglevel) {
															case 1:{
																out.println("<option value=\"1\" selected>������־</option>");
																out.println("<option value=\"2\">��ʾ�澯</option>");
																out.println("<option value=\"3\">һ��澯</option>");
																out.println("<option value=\"4\">���ظ澯</option>");
																out.println("<option value=\"5\">�����澯</option>");
																break;
															}
															case 2:{
																out.println("<option value=\"1\">������־</option>");
																out.println("<option value=\"2\" selected>��ʾ�澯</option>");
																out.println("<option value=\"3\">һ��澯</option>");
																out.println("<option value=\"4\">���ظ澯</option>");
																out.println("<option value=\"5\">�����澯</option>");
																break;
															}
															case 3:{
																out.println("<option value=\"1\">������־</option>");
																out.println("<option value=\"2\">��ʾ�澯</option>");
																out.println("<option value=\"3\" selected>һ��澯</option>");
																out.println("<option value=\"4\">���ظ澯</option>");
																out.println("<option value=\"5\">�����澯</option>");
																break;
															}
															case 4:{
																out.println("<option value=\"1\">������־</option>");
																out.println("<option value=\"2\">��ʾ�澯</option>");
																out.println("<option value=\"3\">һ��澯</option>");
																out.println("<option value=\"4\" selected>���ظ澯</option>");
																out.println("<option value=\"5\">�����澯</option>");
																break;
															}
															case 5:{
																out.println("<option value=\"1\">������־</option>");
																out.println("<option value=\"2\">��ʾ�澯</option>");
																out.println("<option value=\"3\">һ��澯</option>");
																out.println("<option value=\"4\">���ظ澯</option>");
																out.println("<option value=\"5\" selected>�����澯</option>");
																break;
															}
														}
														out.println("</select>");
													}
												%>
												</td>
												<td>&nbsp;</td>
												<td>�������ʻָ�ͻ��澯����</td>
												<td>
												<%
													if (outtbflag.equals("0")) {
														out.println("<select name=\"select_10\" style=\"width:150px\" disabled>");
														out.println("<option value=\"1\">������־</option>");
														out.println("<option value=\"2\">��ʾ�澯</option>");
														out.println("<option value=\"3\">һ��澯</option>");
														out.println("<option value=\"4\">���ظ澯</option>");
														out.println("<option value=\"5\">�����澯</option>");
														out.println("</select>");
													}
													else {
														out.println("<select name=\"select_10\" style=\"width:150px\">");
														switch (num_outreinstatelevel) {
															case 1:{
																out.println("<option value=\"1\" selected>������־</option>");
																out.println("<option value=\"2\">��ʾ�澯</option>");
																out.println("<option value=\"3\">һ��澯</option>");
																out.println("<option value=\"4\">���ظ澯</option>");
																out.println("<option value=\"5\">�����澯</option>");
																break;
															}
															case 2:{
																out.println("<option value=\"1\">������־</option>");
																out.println("<option value=\"2\" selected>��ʾ�澯</option>");
																out.println("<option value=\"3\">һ��澯</option>");
																out.println("<option value=\"4\">���ظ澯</option>");
																out.println("<option value=\"5\">�����澯</option>");
																break;
															}
															case 3:{
																out.println("<option value=\"1\">������־</option>");
																out.println("<option value=\"2\">��ʾ�澯</option>");
																out.println("<option value=\"3\" selected>һ��澯</option>");
																out.println("<option value=\"4\">���ظ澯</option>");
																out.println("<option value=\"5\">�����澯</option>");
																break;
															}
															case 4:{
																out.println("<option value=\"1\">������־</option>");
																out.println("<option value=\"2\">��ʾ�澯</option>");
																out.println("<option value=\"3\">һ��澯</option>");
																out.println("<option value=\"4\" selected>���ظ澯</option>");
																out.println("<option value=\"5\">�����澯</option>");
																break;
															}
															case 5:{
																out.println("<option value=\"1\">������־</option>");
																out.println("<option value=\"2\">��ʾ�澯</option>");
																out.println("<option value=\"3\">һ��澯</option>");
																out.println("<option value=\"4\">���ظ澯</option>");
																out.println("<option value=\"5\" selected>�����澯</option>");
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