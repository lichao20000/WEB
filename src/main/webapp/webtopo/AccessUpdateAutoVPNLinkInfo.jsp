<%@ page contentType="text/html;charset=GBK"%>
<%@ page import ="com.linkage.litms.common.database.Cursor, com.linkage.litms.common.database.DataSetBean,java.util.*"%>

<%@ include file="../timelater.jsp"%>
<SCRIPT LANGUAGE="JavaScript" id="idParentFun">

</SCRIPT>


<%@ include file="../head.jsp"%>
<%@ include file="../toolbar.jsp"%>

<%
	//String vpn_id = request.getParameter("vpn_id");
	String link_auto_id = request.getParameter("link_auto_id");
	//String vpn_id = request.getParameter("vpn_id");
	//String vpn_link_id = request.getParameter("vpn_link_id");
	String strsql = "";
	String vpn_link_id = "";
	String vpn_auto_id = "";
	String vrf_name = "";
	String vrf_desc = "";
	String rd = "";
	String export_rt = "";
	String import_rt = "";
	String export_map = "";
	String import_map = "";
	String maxroute = "0";
	String maxroutethred = "";
	
	String pe_device_id = "";
	String pe_port = "";
	String pe_port_index = "";
	String pe_port_fsp = "";
	String pe_port_proto = "";
	String peportip = "";
	String peport_bandwidth = "0";
	String pe_vlan_id = "0";
	String pe_vcd = "0";
	String pe_vpi = "0";
	String pe_vci = "0";
	String pe_dlci = "0";

	String pe_name_bak = "";
	String perouterip_bak = "";
	String pe_port_bak = "";
	String pe_port_fsp_bak = "";
	String pe_port_proto_bak = "";
	String pe_device_id_bak = "";

	String peport_bandwidth_bak = "0";
	String pe_vlan_id_bak = "0";
	String pe_vcd_bak = "0";
	String pe_vpi_bak = "0";
	String pe_vci_bak = "0";
	String pe_dlci_bak = "0";
	String ce_managed = "-1";
	String ce_name = "";
	String ce_ip = "";
	String ce_port = "";
	String ce_port_fsp = "";
	
	String ce_port_proto = "";
	String ce_port_ip = "";
	String ce_vlan_id = "0";
	String ce_vcd = "0";
	String ce_vpi = "0";
	String ce_vci = "0";
	String ce_dlci = "0";
	String pe_ce_routepro = "";
	String routepro_attr = "";
	String bridgegroup = "0";
	String update_flag = "0";
	String remark1 = "";

	strsql = "select * from vpn_auto_link where link_auto_id="+ link_auto_id;
	Map fields = DataSetBean.getRecord(strsql);
	if (fields != null) {
	 vpn_link_id = (String)fields.get("vpn_link_id");
	 vpn_auto_id = (String)fields.get("vpn_auto_id");
	 vrf_name  = (String)fields.get("vrf_name");
	 vrf_desc = (String)fields.get("vrf_desc");
	 rd = (String)fields.get("rd");
	 export_rt = (String)fields.get("export_rt");
	 import_rt = (String)fields.get("import_rt");
	 export_map = (String)fields.get("export_map");
	 import_map = (String)fields.get("import_map");
	 maxroute = (String)fields.get("maxroute");
	 maxroutethred = (String)fields.get("maxroutethred");
	 pe_device_id = (String)fields.get("pe_device_id");
	
	 pe_port = (String)fields.get("pe_port");
	 pe_port_index = (String)fields.get("pe_port_index");
	 pe_port_fsp = (String)fields.get("pe_port_fsp");
	 pe_port_proto = (String)fields.get("pe_port_proto");
	 peportip = (String)fields.get("peportip");
	 peport_bandwidth = (String)fields.get("peport_bandwidth");
	 pe_vlan_id = (String)fields.get("pe_vlan_id");
	 pe_vcd = (String)fields.get("pe_vcd");
	 pe_vpi = (String)fields.get("pe_vpi");
	 pe_vci = (String)fields.get("pe_vci");
	 pe_dlci = (String)fields.get("pe_dlci");

	 pe_name_bak = (String)fields.get("pe_name_bak");
	 perouterip_bak = (String)fields.get("perouterip_bak");
	 pe_port_bak = (String)fields.get("pe_port_bak");
	 pe_port_fsp_bak = (String)fields.get("pe_port_fsp_bak");
	 pe_port_proto_bak = (String)fields.get("pe_port_proto_bak");
	 pe_device_id_bak = (String)fields.get("pe_device_id_bak");
	 peport_bandwidth_bak = (String)fields.get("peport_bandwidth_bak");
	 pe_vlan_id_bak = (String)fields.get("pe_vlan_id_bak");
	 pe_vcd_bak = (String)fields.get("pe_vcd_bak");
	 pe_vpi_bak = (String)fields.get("pe_vpi_bak");
	 pe_vci_bak = (String)fields.get("pe_vci_bak");
	 pe_dlci_bak = (String)fields.get("pe_dlci_bak");
	 ce_managed = (String)fields.get("ce_managed");
	 ce_name = (String)fields.get("ce_name");
	 ce_ip = (String)fields.get("ce_ip");
	 ce_port = (String)fields.get("ce_port");
	 ce_port_fsp = (String)fields.get("ce_port_fsp");
	
	 ce_port_proto = (String)fields.get("ce_port_proto");
	 ce_port_ip = (String)fields.get("ce_port_ip");
	 ce_vlan_id = (String)fields.get("ce_vlan_id");
	 ce_vcd = (String)fields.get("ce_vcd");
	 ce_vpi = (String)fields.get("ce_vpi");
	 ce_vci = (String)fields.get("ce_vci");
	 ce_dlci = (String)fields.get("ce_dlci");

	 pe_ce_routepro = (String)fields.get("pe_ce_routepro");
	 routepro_attr = (String)fields.get("routepro_attr");
	 bridgegroup = (String)fields.get("bridgegroup");
	 update_flag = (String)fields.get("update_flag");
	 remark1 = (String)fields.get("remark1");
}


%>
<SCRIPT LANGUAGE="JavaScript">
<!--
	
//-->
</SCRIPT>


<TABLE border=0 cellspacing=0 cellpadding=0 width="100%" id="myTable">
<TR><TD>
    <FORM NAME="frm3" METHOD="post" ACTION="AccessVPNAutoLinkInfoIsSuccess.jsp" onsubmit="return CheckForm3()" target="childfrm">
	<TABLE width="100%" border=0 cellspacing=0 cellpadding=0 align="center" >
		<TR>
			<TD bgcolor=#000000>
              <TABLE border=0 cellspacing=1 cellpadding=2 width="100%">
                <TR> 
                  <TD bgcolor="#ffffff" colspan="4">带'<font color="#FF0000">*</font>'的表单必须填写或选择</TD>
                </TR>
                <TR>
                  <TH colspan="4" align="center">编辑链路自动发现信息表</TH>
                </TR>
				
                <TR bgcolor="#FFFFFF"> 
				  <TD class=column align="left">链路标识</TD>
                  <TD>
                    <INPUT TYPE="text" NAME="vpn_link_id" maxlength=100 class=bk readonly value="<%=vpn_link_id%>">
                    <font color="#FF0000">*</font></TD>
					<TD class=column align="left">用户自动标识</TD>
                  <TD>
                    <INPUT TYPE="text" NAME="vpn_auto_id" maxlength=100 class=bk readonly value="<%=vpn_auto_id%>">
                    <font color="#FF0000">*</font></TD>
                </TR>
				<TR bgcolor="#FFFFFF">
				 <TD class=column align="left">Vrf name</TD>
                  <TD>
                    <INPUT TYPE="text" NAME="vrf_name" maxlength=100 class=bk value="<%=vrf_name%>">
                    <font color="#FF0000">*</font></TD>

				  <TD class=column align="left">Vrf 描述</TD>
					<TD> 
                    <INPUT TYPE="text" NAME="vrf_desc" maxlength=100 class=bk value="<%=vrf_desc%>">
                    </TD>
					
				</TR>
				<TR bgcolor="#FFFFFF"> 
					<TD class=column align="left">RD</TD>
					<TD><INPUT TYPE="text" NAME="rd" maxlength=20 class=bk value="<%=rd%>">&nbsp;<font color="#FF0000">*</font></TD>
					<TD class=column align="left">Export RT</TD>
					<TD><INPUT TYPE="text" NAME="export_rt" maxlength=20 class=bk value="<%=export_rt%>">&nbsp;<font color="#FF0000">*</font>
					</TD>
				
                </TR>
				<TR bgcolor="#FFFFFF">
					<TD class=column align="left">Import RT</TD>
				  <TD><INPUT TYPE="text" NAME="import_rt" maxlength=20 class=bk value="<%=import_rt%>">&nbsp;<font color="#FF0000">*</font>
				  </TD>
					<TD class=column align="left">Export map</TD>
					<TD><INPUT TYPE="text" NAME="export_map" maxlength=20 class=bk value="<%=export_map%>">&nbsp;</TD>
					
				</TR>
				<TR bgcolor="#FFFFFF">
					<TD class=column align="left">Import map</TD>
					<TD><INPUT TYPE="text" NAME="import_map" maxlength=20 class=bk value="<%=import_map%>">&nbsp;</TD>
					<TD class=column align="left">maxroute</TD>
					<TD><INPUT TYPE="text" NAME="maxroute" maxlength=100 class=bk value="<%=maxroute%>">&nbsp;</TD>

				</TR>
				<TR bgcolor="#FFFFFF">
					<TD class=column align="left">maxroutethred</TD>
					<TD><INPUT TYPE="text" NAME="maxroutethred" maxlength=20 class=bk value="<%=maxroutethred%>"></TD>
					<TD class=column align="left">PE路由器设备编码</TD>
					<TD><INPUT TYPE="text" NAME="pe_device_id" maxlength=30 class=bk value="<%=pe_device_id%>">&nbsp;<font color="#FF0000">*</font></TD>
				
				</TR>
				<TR bgcolor="#FFFFFF">
					<TD class=column align="left">接口数据</TD>
					<TD><INPUT TYPE="text" NAME="pe_port" maxlength=15 class=bk value="<%=pe_port%>">&nbsp;<font color="#FF0000">*</font></TD>
					<TD class=column align="left">端口索引</TD>
					<TD><INPUT TYPE="text" NAME="pe_port_index" maxlength=15 class=bk value="<%=pe_port_index%>">&nbsp;</TD>
				</TR>
				<TR bgcolor="#FFFFFF">
					<TD class=column align="left">PE(子)接口所在槽位号</TD>
					<TD><INPUT TYPE="text" NAME="pe_port_fsp" maxlength=15 class=bk value="<%=pe_port_fsp%>">&nbsp;<font color="#FF0000">*</font></TD>
					<TD class=column align="left"></TD>
					<TD></TD>
					

				</TR>
				<TR bgcolor="#FFFFFF">
					<TD class=column align="left">PE(子)接口封装协议</TD>
					<TD>
					<SELECT name="pe_port_proto" onChange="selectObjectCmd_pe('1')" class=bk>
					<option value="0" <% if (pe_port_proto.equals("0")) {%>selected<%}%>>==请选择==</option>
					<option value="1" <% if (pe_port_proto.equals("1")) {%>selected<%}%>>DOT1Q</option>
					<option value="2" <% if (pe_port_proto.equals("2")) {%>selected<%}%>>ISL</option>
					<option value="3" <% if (pe_port_proto.equals("3")) {%>selected<%}%>>FRAME_RELAY</option>
					<option value="4" <% if (pe_port_proto.equals("4")) {%>selected<%}%>>FRAME_RELAY_IETF</option>
					<option value="5" <% if (pe_port_proto.equals("5")) {%>selected<%}%>>HDLC</option>
					<option value="6" <% if (pe_port_proto.equals("6")) {%>selected<%}%>>PPP</option>
					<option value="7" <% if (pe_port_proto.equals("7")) {%>selected<%}%>>AAAL5SNAP</option>
					<option value="8" <% if (pe_port_proto.equals("8")) {%>selected<%}%>>DEFAULT</option>
					</SELECT>&nbsp;
					</TD>
					<TD class=column align="left">PE(子)接口ip</TD>
					<TD><INPUT TYPE="text" NAME="peportip" maxlength=15 class=bk value="<%=peportip%>">&nbsp;</TD>
					
				</TR>
				<TR bgcolor="#FFFFFF">
					<TD class=column align="left">PE(子)接口带宽(bps)</TD>
					<TD><INPUT TYPE="text" NAME="peport_bandwidth" maxlength=15 class=bk value="<%=peport_bandwidth%>">&nbsp;</TD>
					<TD class=column align="left">Pe_vlan_id</TD>
					<TD><INPUT TYPE="text" NAME="pe_vlan_id" maxlength=15 class=bk value="<%=pe_vlan_id%>"><span id='Dot1Q_ISL' name='Dot1Q_ISL' style="display:none;"><font color="#FF0000">*</font></span></TD>
					
				</TR>
				<TR bgcolor="#FFFFFF">
					<TD class=column align="left">Pe_vcd</TD>
					<TD><INPUT TYPE="text" NAME="pe_vcd" maxlength=15 class=bk value="<%=pe_vcd%>"><span id='ATM_aal5_1' style="display:none;"></span></TD>
					<TD class=column align="left">Pe_vpi</TD>
					<TD><INPUT TYPE="text" NAME="pe_vpi" maxlength=15 class=bk value="<%=pe_vpi%>"><span id='ATM_aal5_2' style="display:none;"><font color="#FF0000">*</font></span></TD>
				</TR>
				<TR bgcolor="#FFFFFF">
					<TD class=column align="left">Pe_vci</TD>
					<TD><INPUT TYPE="text" NAME="pe_vci" maxlength=15 class=bk value="<%=pe_vci%>"><span id='ATM_aal5_3' style="display:none;"><font color="#FF0000">*</font></span></TD>
					<TD class=column align="left">Pe_dlci</TD>
					<TD><INPUT TYPE="text" NAME="pe_dlci" maxlength=15 class=bk value="<%=pe_dlci%>"><span id='FR' style="display:none;"><font color="#FF0000">*</font></span></TD>
				</TR>

				<TR style="cursor:hand;background-color:#cccccc" onclick="EC('pe',this);">
					<TD colspan="4">
						<TABLE CELLPADDING="0" CELLSPACING="0" WIDTH="100%" BORDER="0">
						<TR><TD>
							备Pe设备(有备份链路时必填)
						</TD>
						<TD align="right">
							<IMG SRC="images/down_enabled.gif" WIDTH="7" HEIGHT="9" BORDER="0" ALT="">&nbsp;
						</TD>
						</TR>
						</TABLE>
					</TD>
				</TR>
				<TR bgcolor="#FFFFFF" leaf="pe" style="display:none">
					<TD class=column align="left">备Pe设备名称</TD>
					<TD><INPUT TYPE="text" NAME="pe_name_bak" maxlength=15 class=bk value="<%=pe_name_bak%>">&nbsp;<font color="#FF0000">*</font></TD>
					<TD class=column align="left">备PE路由器管理ip</TD>
					<TD><INPUT TYPE="text" NAME="perouterip_bak" maxlength=15 class=bk value="<%=perouterip_bak%>">&nbsp;<font color="#FF0000">*</font></TD>
				</TR>
				<TR bgcolor="#FFFFFF" leaf="pe" style="display:none">
					<TD class=column align="left">备pe(子)接口名称</TD>
					<TD><INPUT TYPE="text" NAME="pe_port_bak" maxlength=15 class=bk value="<%=pe_port_bak%>">&nbsp;<font color="#FF0000">*</font></TD>
					<TD class=column align="left">备PE(子)接口所在槽位号</TD>
					<TD><INPUT TYPE="text" NAME="pe_port_fsp_bak" maxlength=15 class=bk value="<%=pe_port_fsp_bak%>">&nbsp;<font color="#FF0000">*</font></TD>
				</TR>
				<TR bgcolor="#FFFFFF" leaf="pe" style="display:none">
				
					<TD class=column align="left">备PE(子)接口封装协议</TD>
					<TD><INPUT TYPE="text" NAME="pe_port_proto_bak" maxlength=15 class=bk value="<%=pe_port_proto_bak%>">&nbsp;<font color="#FF0000">*</font></TD>
					<TD class=column align="left">备PE(子)界面ip</TD>
					<TD><INPUT TYPE="text" NAME="pe_device_id_bak" maxlength=15 class=bk value="<%=pe_device_id_bak%>">&nbsp;<font color="#FF0000">*</font></TD>
				</TR>
				<TR bgcolor="#FFFFFF" leaf="pe" style="display:none">
				
					<TD class=column align="left">备PE(子)界面带宽(bps)</TD>
					<TD><INPUT TYPE="text" NAME="peport_bandwidth_bak" maxlength=15 class=bk value="<%=peport_bandwidth_bak%>">&nbsp;<font color="#FF0000">*</font></TD>
					<TD class=column align="left">Pe_vlan_id_bak</TD>
					<TD><INPUT TYPE="text" NAME="pe_vlan_id_bak" maxlength=15 class=bk value="<%=pe_vlan_id_bak%>">&nbsp;<font color="#FF0000">*</font></TD>
				</TR>
				<TR bgcolor="#FFFFFF" leaf="pe" style="display:none">
				
					<TD class=column align="left">Pe_vcd_bak</TD>
					<TD><INPUT TYPE="text" NAME="pe_vcd_bak" maxlength=15 class=bk value="<%=pe_vcd_bak%>">&nbsp;<font color="#FF0000">*</font></TD>
					<TD class=column align="left">Pe_vpi_bak</TD>
					<TD><INPUT TYPE="text" NAME="pe_vpi_bak" maxlength=15 class=bk value="<%=pe_vpi_bak%>">&nbsp;<font color="#FF0000">*</font></TD>
				</TR>
				<TR bgcolor="#FFFFFF" leaf="pe" style="display:none">
					<TD class=column align="left">Pe_vci_bak</TD>
					<TD><INPUT TYPE="text" NAME="pe_vci_bak" maxlength=15 class=bk value="<%=pe_vci_bak%>">&nbsp;<font color="#FF0000">*</font></TD>
					<TD class=column align="left">Pe_dlci_bak</TD>
					<TD><INPUT TYPE="text" NAME="pe_dlci_bak" maxlength=15 class=bk value="<%=pe_dlci_bak%>">&nbsp;<font color="#FF0000">*</font></TD>
				</TR>
				<TR style="cursor:hand;background-color:#cccccc" onclick="EC('ce',this);">
					<TD colspan="4">
						<TABLE CELLPADDING="0" CELLSPACING="0" WIDTH="100%" BORDER="0">
						<TR><TD>
							Ce设备(CE可管时必填)
						</TD>
						<TD align="right">
							<IMG SRC="images/down_enabled.gif" WIDTH="7" HEIGHT="9" BORDER="0" ALT="">&nbsp;
						</TD>
						</TR>
						</TABLE>
					</TD>
				</TR>

				<TR bgcolor="#FFFFFF" leaf="ce" style="display:none">
					<TD class=column align="left">CE是否可管</TD>
					<TD>
					<SELECT name="ce_managed" onChange="ce_managed_Selected('1')" class=bk>
					<option value="-1" <% if (ce_managed.equals("-1")) {%>selected<%}%>>==请选择==</option>
					<option value="0" <% if (ce_managed.equals("0")) {%>selected<%}%>>不可管</option>
					<option value="1" <% if (ce_managed.equals("1")) {%>selected<%}%>>可管</option>
					</SELECT>&nbsp;<font color="#FF0000">*</font>
					</TD>
					<TD class=column align="left">CE名称</TD>
					<TD><INPUT TYPE="text" NAME="ce_name" maxlength=15 class=bk value="<%=ce_name%>">&nbsp;<font color="#FF0000">*</font></TD>
					
				</TR>
				<TR bgcolor="#FFFFFF" leaf="ce" style="display:none">
					<TD class=column align="left">CE管理地址</TD>
					<TD><INPUT TYPE="text" NAME="ce_ip" maxlength=15 class=bk value="<%=ce_ip%>">&nbsp;<font color="#FF0000">*</font></TD>
					<TD class=column align="left">CE（子）接口名称</TD>
					<TD><INPUT TYPE="text" NAME="ce_port" maxlength=15 class=bk value="<%=ce_port%>">&nbsp;<font color="#FF0000">*</font></TD>
					
				</TR>
				<TR bgcolor="#FFFFFF" leaf="ce" style="display:none">
					<TD class=column align="left">CE（子）接口所在槽位号</TD>
					<TD><INPUT TYPE="text" NAME="ce_port_fsp" maxlength=15 class=bk value="<%=ce_port_fsp%>">&nbsp;<font color="#FF0000">*</font></TD>
					<TD class=column align="left">CE（子）接口封装协议</TD>
					<TD>
					<SELECT name="ce_port_proto" onChange="selectObjectCmd_ce('1')" class=bk>
					<option value="0" <% if (ce_port_proto.equals("0")) {%>selected<%}%>>==请选择==</option>
					<option value="1" <% if (ce_port_proto.equals("1")) {%>selected<%}%>>DOT1Q</option>
					<option value="2" <% if (ce_port_proto.equals("2")) {%>selected<%}%>>ISL</option>
					<option value="3" <% if (ce_port_proto.equals("3")) {%>selected<%}%>>FRAME_RELAY</option>
					<option value="4" <% if (ce_port_proto.equals("4")) {%>selected<%}%>>FRAME_RELAY_IETF</option>
					<option value="5" <% if (ce_port_proto.equals("5")) {%>selected<%}%>>HDLC</option>
					<option value="6" <% if (ce_port_proto.equals("6")) {%>selected<%}%>>PPP</option>
					<option value="7" <% if (ce_port_proto.equals("7")) {%>selected<%}%>>AAL5SNAP</option>
					<option value="8" <% if (ce_port_proto.equals("8")) {%>selected<%}%>>DEFAULT</option>
					</SELECT>&nbsp;<font color="#FF0000">*</font>
					</TD>
					
				</TR>
				<TR bgcolor="#FFFFFF" leaf="ce" style="display:none">
					<TD class=column align="left">CE（子）接口地址</TD>
					<TD><INPUT TYPE="text" NAME="ce_port_ip" maxlength=15 class=bk value="<%=ce_port_ip%>">&nbsp;<font color="#FF0000">*</font></TD>
					<TD class=column align="left">Ce_vlan_id</TD>
					<TD><INPUT TYPE="text" NAME="ce_vlan_id" maxlength=15 class=bk value="<%=ce_vlan_id%>"><span id='Dot1Q_ISL_CE' style="display:none;"><font color="#FF0000">*</font></span></TD>
				</TR>
				<TR bgcolor="#FFFFFF" leaf="ce" style="display:none">
					<TD class=column align="left">Ce_vcd</TD>
					<TD><INPUT TYPE="text" NAME="ce_vcd" maxlength=15 class=bk value="<%=ce_vcd%>"><span id='ATM_aal5_1_CE' style="display:none;"><font color="#FF0000">*</font></span></TD>
					<TD class=column align="left">Ce_vpi</TD>
					<TD><INPUT TYPE="text" NAME="ce_vpi" maxlength=15 class=bk value="<%=ce_vpi%>"><span id='ATM_aal5_2_CE' style="display:none;"><font color="#FF0000">*</font></span></TD>
				</TR>
				<TR bgcolor="#FFFFFF" leaf="ce" style="display:none">
					<TD class=column align="left">Ce_vci</TD>
					<TD><INPUT TYPE="text" NAME="ce_vci" maxlength=15 class=bk value="<%=ce_vci%>"><span id='ATM_aal5_3_CE' style="display:none;"><font color="#FF0000">*</font></span></TD>
					<TD class=column align="left">Ce_dlci</TD>
					<TD><INPUT TYPE="text" NAME="ce_dlci" maxlength=15 class=bk value="<%=ce_dlci%>"><span id='FR_CE' style="display:none;"><font color="#FF0000">*</font></span></TD>
				</TR>
				<TR bgcolor="#FFFFFF">
					<TD class=column align="left">Pe-ce路由协议</TD>
					<TD><INPUT TYPE="text" NAME="pe_ce_routepro" maxlength=15 class=bk value="<%=pe_ce_routepro%>"></TD>

					<TD class=column align="left">路由协议属性</TD>
					<TD><INPUT TYPE="text" NAME="routepro_attr" maxlength=15 class=bk value="<%=routepro_attr%>"></TD>
					
				</TR>
				<TR bgcolor="#FFFFFF">
					<TD class=column align="left">桥接组</TD>
					<TD><INPUT TYPE="text" NAME="bridgegroup" maxlength=15 class=bk value="<%=bridgegroup%>"></TD>
					<TD class=column align="left">备注</TD>
					<TD><INPUT TYPE="text" NAME="remark1" maxlength=50 size=50 class=bk value="<%=remark1%>"></TD>
				</TR>
                <TR> 
                  <TD colspan="4" align="center" class=foot>
				    <INPUT TYPE="submit" value=" 修 改 " class=btn>&nbsp;&nbsp;
					<INPUT TYPE="button" value=" 关 闭 " class=btn onclick="javascript:window.close();">&nbsp;&nbsp;
					<INPUT TYPE="hidden" name="action" value="edit">
					<INPUT TYPE="hidden" name="link_auto_id" value="<%=link_auto_id%>">
                 </TD>
                </TR>
              </TABLE>
			</TD>
		</TR>
	</TABLE>
    </FORM>
  </TD></TR>
</TABLE>
<iframe id="childfrm" name="childfrm" align="center" style="display:none"></iframe>
<SCRIPT LANGUAGE="JavaScript">
<!--
parent.document.all("autolinkinfo").style.display="";
parent.document.all("autolinkinfo").innerHTML=document.all.myTable.outerHTML;
//alert(document.all.myTable.outerHTML);
//-->
</SCRIPT>