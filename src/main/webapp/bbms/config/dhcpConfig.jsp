<%@ page contentType="text/html;charset=gbk"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<link rel="stylesheet" href="../../css/css_green.css" type="text/css">
<SCRIPT LANGUAGE="JavaScript" SRC="../../Js/jquery.js"></SCRIPT>
<SCRIPT LANGUAGE="JavaScript" SRC="../../Js/CheckFormForm.js"></SCRIPT>
<script type="text/javascript" src="../../Js/jsDate/WdatePicker.js"></script>
<SCRIPT LANGUAGE="JavaScript">
var deviceId = '<s:property value="deviceId"/>';
parent.unblock();
 
function dhcpEnableChange(){
    var dhcpEnable = $("input[@name='dhcpEnable'][@checked]").val();
	if(dhcpEnable=="1"){
		$("tr[@name='dhcpno']").show();	
		parent.dyniframesize();	
       	
	}else{
		$("tr[@name='dhcpno']").hide();	
     	parent.dyniframesize();
	}
}

function showDHCP(deviceId,vlanI,ipAddress,ipMask,dhcpEnable,dhcpMinAddr,dhcpMaxAddr,dhcpGateway,dhcpLeaseTime,dhcpDns,dhcpDomain){
    $("tr[@name='dhcp']").show();
    $("div[@id='ipAddress']").html("");
    $("div[@id='ipAddress']").append(ipAddress);	
    $("div[@id='ipMask']").html("");
    $("div[@id='ipMask']").append(ipMask);	
    $("input[@name='ipAddress']").val(ipAddress);
    
	$("input[@name='dhcpMinAddr']").val(dhcpMinAddr);
	$("input[@name='dhcpMaxAddr']").val(dhcpMaxAddr);
	$("input[@name='dhcpGateway']").val(dhcpGateway);
	$("input[@name='dhcpDns']").val(dhcpDns);
	$("input[@name='dhcpDomain']").val(dhcpDomain);
	var t = dhcpLeaseTime.split("/");
	$("input[@name='day']").val(t[0]);
	$("input[@name='hour']").val(t[1]);
	$("input[@name='minute']").val(t[2]);
	$("input[@name='deviceId']").val(deviceId);
	$("input[@name='vlanI']").val(vlanI);
	$("input[@name='dhcpEnable'][@value='"+dhcpEnable+"']").attr("checked",true);
	$("input[@name='dhcpEnable_h']").val(dhcpEnable);
	dhcpEnableChange();
	parent.dyniframesize();
   
	//if(dhcpEnable=="1"){
	  //  $("input[@name='dhcpEnable'][@value=1]").attr("checked",true);
	   /// dhcpEnableChange();
	//}
	//if(dhcpEnable=="0"){
	  //  $("input[@name='dhcpEnable'][@value=0]").attr("checked",true);
	    //dhcpEnableChange();
	//}	

}

function checkForm(){
          var v = $("input[@name='dhcpEnable'][@checked]").val()
          if(v == "1"){
                var dhcpMinAddr = document.frm.dhcpMinAddr.value;
                var dhcpMaxAddr = document.frm.dhcpMaxAddr.value;
                var dhcpGateway = document.frm.dhcpGateway.value;
                var dhcpDns = document.frm.dhcpDns.value;
                var dhcpDomain = document.frm.dhcpDomain.value;
                var ipAddress = document.frm.ipAddress.value;
                //�����ж�
                        if(dhcpMinAddr == ""){
                                alert("�������ַ����ʼ��ַ");
                                document.frm.dhcpMinAddr.focus();
                                return false;
                        }
                        if(dhcpMaxAddr == ""){
                                alert("�������ַ�ؽ�����ַ");
                                document.frm.dhcpMaxAddr.focus();
                                return false;
                        }
                        if(dhcpGateway == ""){
                                alert("������Ĭ������");
                                document.frm.dhcpGateway.focus();
                                return false;
                        }
                        if(dhcpDns == ""){
                                alert("������DNS��������ַ");
                                document.frm.dhcpDns.focus();
                                return false;
                        }
                       // if(dhcpDomain == ""){
                       //         alert("������DHCP������ǿ������");
                       //         document.frm.dhcpDomain.focus();
                       //         return false;
                       // }
                        //IP��ʽ�ж�
                        if(!IsIPAddr2(dhcpMinAddr,"��ַ����ʼ��ַ")){
                            document.frm.dhcpMinAddr.focus();
                            return false;
                        }
                        if(!IsIPAddr2(dhcpMaxAddr,"��ַ�ؽ�����ַ")){
                            document.frm.dhcpMaxAddr.focus();
                            return false;
                        }
                        if(!IsIPAddr2(dhcpGateway,"Ĭ������")){
                            document.frm.dhcpGateway.focus();
                            return false;
                        }
                        var g = dhcpDns.split(",");
                        for(i = 0;i<g.length;i++){
                            if(!IsIPAddr2(g[i],"DNS��������ַ")){
                                document.frm.dhcpDns.focus();
                                return false;
                            }
                        }
                     //   if(!IsIPAddr2(dhcpDomain,"DHCP������ǿ������")){
                     //       document.frm.dhcpDomain.focus();
                     //       return false;
                     //   }
                        //��ַ����ʼ��ַ,��ַ�ؽ�����ַ�����ж�
                        var mi = dhcpMinAddr.split(".");
                        var ma = dhcpMaxAddr.split(".");
                        if(ipAddress!=""){                            
                            if(!checkIpAd(dhcpMinAddr,ipAddress)){
                                alert("��ַ����ʼ��ַ����ӿ�IP��ַ����ͬһ����");
                                document.frm.dhcpMinAddr.focus();
                                return false;
                            }
                            if(!checkIpAd(dhcpMaxAddr,ipAddress)){
                                alert("��ַ�ؽ�����ַ����ӿ�IP��ַ����ͬһ����");
                                document.frm.dhcpMaxAddr.focus();
                                return false;
                            }
                        }else{
                            if(!checkIpAd(dhcpMinAddr,dhcpMaxAddr)){
                                alert("��ַ����ʼ��ַ,��ַ�ؽ�����ַ����ͬһ����");
                                document.frm.dhcpMinAddr.focus();
                                return false;
                            }
                        }                      
                        if(parseInt(mi[3])>parseInt(ma[3])){
                            alert("��ַ����ʼ��ַ,��ַ�ؽ�����ַ������ȷ");
                            document.frm.dhcpMinAddr.focus();
                            return false;
                        }
                        if(ma[3]>254){
                            alert("��ַ�ؽ�����ַ������ȷ");
                            document.frm.dhcpMaxAddr.focus();
                            return false;
                        }
                        //�ж�����
                        var day = document.frm.day.value;
                        var hour = document.frm.hour.value;
                        var minute = document.frm.minute.value;
                        if(hour>23){
                            alert("���ڵ�СʱҪС�ڵ���23");
                            document.frm.hour.focus();
                            return false;
                        }
                        if(minute>59){
                            alert("���ڵķ���ҪС�ڵ���59");
                            document.frm.minute.focus();
                            return false;
                        }
                        
                }
                return true;
}
        
function checkIpAd(ip1,ip2){
    var a = ip1.split(".");
    var b = ip2.split(".");
    if(a[0]!=b[0]||a[1]!=b[1]||a[2]!=b[2]){
    //alert("ip1:"+a[0]+"."+a[1]+"."+a[2]+"."+a[3]);
    //alert("ip2:"+b[0]+"."+b[1]+"."+b[2]+"."+b[3]);
   // alert("ip1������ip2");
                return false;
        }
        return true;
}

 function ExecMod(){
     			if(checkForm() == false) return false;
   			    var dhcpEnable = $("input[@name='dhcpEnable'][@checked]").val();
   			    var dhcpEnable_h = $("input[@name='dhcpEnable_h']").val();
                if(dhcpEnable=="0"&&dhcpEnable_h=="0"){
                    alert("�豸DHCP�Ѿ��ǹرյģ������������ùر�");
                    return false;
                }
 			    parent.block();
                var dhcpMinAddr = $("input[@name='dhcpMinAddr']").val();
	            var dhcpMaxAddr = $("input[@name='dhcpMaxAddr']").val();
	            var dhcpDomain = $("input[@name='dhcpDomain']").val();
	            var dhcpDns = $("input[@name='dhcpDns']").val();
	            var dhcpGateway = $("input[@name='dhcpGateway']").val();
	            var deviceId = $("input[@name='deviceId']").val();
	            var vlanI = $("input[@name='vlanI']").val();
	            
	            var day = $("input[@name='day']").val();
	            var hour = $("input[@name='hour']").val();
	            var minute = $("input[@name='minute']").val();
	            var dhcpLeaseTime = day*24*60*60 +hour*60*60+minute*60;	            
				document.all("div_config").style.display = "";                          
				document.all("div_config").innerHTML = "<font size=2>���������豸DHCP����������Ϣ�������ĵȴ�....</font>";
				var url = "<s:url value='/bbms/config/vlanDhcpConfig!configDHCP.action'/>";
				$.post(url,{
	               deviceId:deviceId,
	               vlanI:vlanI,
	               dhcpEnable:dhcpEnable,
	               dhcpMinAddr:dhcpMinAddr,
	               dhcpMaxAddr:dhcpMaxAddr,
	               dhcpDomain:dhcpDomain,
	               dhcpDns:dhcpDns,
	               dhcpGateway:dhcpGateway,
	               dhcpLeaseTime:dhcpLeaseTime
	            },function(ajax){
	            var s = ajax.split(";");
			    if(s[0]=="-1"){
					$("div[@id='div_config']").css("background-color","red");
					document.all("div_config").innerHTML = "<font size=2>"+s[1]+"</font>";
					parent.dyniframesize();
				}
				if(s[0]=="1"){
   	  		     	$("div[@id='div_config']").css("background-color","#33CC00");
					document.all("div_config").innerHTML = "<font size=2>"+s[1]+"</font>";
					
					var url = "<s:url value='/servStrategy/ServStrategy!getStrategy.action'/>";
					var strategyId = s[2];
					$.post(url,{
	               		strategyId:strategyId
	                },function(ajax){
		                $("div[@id='div_strategy']").html("");
						$("div[@id='div_strategy']").append(ajax);
						parent.dyniframesize();
	                });				
					parent.dyniframesize();
		 			parent.unblock();
				}
	             });
				document.getElementById("tr002").style.display = "";
				parent.dyniframesize();
				setTimeout("clearResult()", 5000);
				
    }
    
function clearResult() {
	document.all("div_config").style.display = "none";
}
    
function reFresh(strategyId){
    var number = Math.random(); 
	var url = "/bbms/servStrategy/ServStrategy!getStrategy.action?number="+number+"&strategyId=";
	document.all("myiframe").src = url+strategyId;
	parent.dyniframesize();
	parent.unblock();
}

parent.unblock();
</SCRIPT>
<TABLE border=0 cellspacing=0 cellpadding=0 width="100%">
	<TR>
		<TD align=right>
			<div id="div_config"
				style="width: 23%; display: none; background-color: #33CC00">
			</div>
		</TD>
	</TR>
	<TR>
		<TD>
			<FORM NAME="frm" METHOD="post" onsubmit="return CheckForm()">
				<table width="100%" border="0" align="center" cellpadding="0"
					cellspacing="0" class="text">
					<!-- <tr>
						<td>
							<table width="100%" height="30" border="0" cellspacing="0"
								cellpadding="0" class="green_gargtd">
								<tr>
									<td width="162" align="center" class="title_bigwhite" nowrap>
										����ʵ������
									</td>
									<td nowrap>
										<img src="../../images/attention_2.gif" width="15" height="12">
										����DHCP��������
									</td>
								</tr>
							</table>
						</td>
					</tr>
					<TR>
						<TH colspan="4" align="center">
							�豸��ѯ
						</TH>
					</TR>
					<TR bgcolor="#FFFFFF" id="tr1" STYLE="display: ">
						<td colspan="4">
							<div id="selectDevice">
								<span>������....</span>
							</div>
						</td>
					</TR> -->
					<TH colspan="4" align="center">
						�豸��DHCP��Ϣ
					</TH>
					<tr align="left" id="trnet" STYLE="display: ">
						<td colspan="4" bgcolor=#999999>
							<TABLE border=0 cellspacing=1 cellpadding=2 width="100%">
								<tr align="center" bgcolor="#FFFFFF">
									<TD class=column5 align="center">
										VLANID
									</TD>
									<TD class=column5 align="center">
										DHCPʹ��״̬
									</TD>
									<TD class=column5 align="center">
										��ַ����ʼ��ַ
									</TD>
									<TD class=column5 align="center">
										��ַ�ؽ�����ַ
									</TD>
									<!-- <TD class=column5 align="center">������ַ�б�</TD> -->
									<!-- <TD class=column5 align="center">��ַ����</TD> -->
									<TD class=column5 align="center">
										DNS��������ַ
									</TD>
									<TD class=column5 align="center">
										Ĭ������
									</TD>
									<TD class=column5 align="center">
										����(��/ʱ/��)
									</TD>
									<TD class=column5 align="center">
										����
									</TD>
								</tr>
								<s:if test="lanVlanList!=null">
									<s:iterator value="lanVlanList">
										<tr align="center" bgcolor="#FFFFFF">
											<TD align="center">
												<s:property value="vlanId" />
											</TD>
											<TD align="center">

												<s:if test="dhcpEnable==1">����</s:if>
												<s:elseif test="dhcpEnable==0">�ر�</s:elseif>
												<s:else>-</s:else>

											</TD>
											<TD align="center">
												<s:if test="dhcpMinAddr=='N/A' || dhcpMinAddr=='null' ">-</s:if>
												<s:else>
													<s:property value="dhcpMinAddr" />
												</s:else>
											</TD>
											<TD align="center">
												<s:if test="dhcpMaxAddr=='N/A' || dhcpMaxAddr=='null' ">-</s:if>
												<s:else>
													<s:property value="dhcpMaxAddr" />
												</s:else>
											</TD>
											<TD align="center">
												<s:if test="dhcpDns=='N/A' || dhcpDns=='null' ">-</s:if>
												<s:else>
													<s:property value="dhcpDns" />
												</s:else>
											</TD>
											<TD align="center">
												<s:if test="dhcpGateway=='N/A' || dhcpGateway=='null' ">-</s:if>
												<s:else>
													<s:property value="dhcpGateway" />
												</s:else>
											</TD>
											<TD align="center">
												<s:if test="dhcpLeaseTime=='N/A' || dhcpLeaseTime=='null' ">-</s:if>
												<s:else>
													<s:property value="dhcpLeaseTime" />
												</s:else>
											</TD>
											<TD align="center">
												<a href=javascript:
													//
														onclick="showDHCP('<s:property value="deviceId"/>','<s:property value="vlanI"/>',
                                                                            '<s:property value="ipAddress"/>','<s:property value="ipMask"/>', 
                                                                            '<s:property value="dhcpEnable"/>','<s:property value="dhcpMinAddr"/>',
                                                                            '<s:property value="dhcpMaxAddr"/>','<s:property value="dhcpGateway"/>',
                                                                            '<s:property value="dhcpLeaseTime"/>','<s:property value="dhcpDns"/>',
                                                                            '<s:property value="dhcpDomain"/>')">
													<IMG SRC='../../images/edit.gif' BORDER='0' ALT='�༭'
														style='cursor: hand'> </a>
											</TD>
										</TR>
									</s:iterator>
								</s:if>
								<s:else>
									<tr align="center" bgcolor="#FFFFFF">
										<TD colspan="13" align="center">
											<FONT color="red"><s:property value="massage" /> </FONT>
										</TD>
									</tr>
								</s:else>
								<tr align="center" bgcolor="#FFFFFF">
									<TD colspan="13" align="center">
										<s:property value="corbaMsg" />
									</TD>
								</tr>
							</TABLE>
					<tr name="dhcp" style="display: none">
						<td>
							<TABLE width="100%" border=0 cellspacing=0 cellpadding=0
								align="center">
								<TR>
									<TD bgcolor=#999999>
										<TABLE border=0 cellspacing=1 cellpadding=2 width="100%">
											<TR>
												<TH colspan="4" align="center">
													�豸��DHCP��Ϣ
												</TH>
											</TR>
											<TR bgcolor="#FFFFFF" name="dhcp_enable" style="display: ">
												<TD align="right" class="column" width="20%" height="25">
													��ӿ�IP��ַ
												</TD>
												<TD width="30%">
													<input type="hidden" name="ipAddress" value="">
													<DIV id="ipAddress"></DIV>
												</TD>
												<TD align="right" class=column width="20%">
													��ӿڵ�ַ����
												</TD>
												<TD width="30%">
													<DIV id="ipMask"></DIV>
												</TD>
											</TR>
											<TR bgcolor="#FFFFFF" name="dhcp_enable" style="display: ">
												<TD class=column align="right">
													DHCPʹ��
												</TD>
												<TD colspan=3>
													<input type="radio" name="dhcpEnable" checked value="1"
														onclick="dhcpEnableChange();">
													����
													<input type="radio" name="dhcpEnable" value="0"
														onclick="dhcpEnableChange();">
													�ر�
													<input type="hidden" name="dhcpEnable_h" value="">
													<input type="hidden" name="deviceId" id="deviceId" value="">
													<input type="hidden" name="vlanI" id="vlanI" value="">
												</TD>
											</TR>
											<!-- SNMPVersion == 1 (V3) -->
											<TR bgcolor="#FFFFFF" name="dhcpno" style="display: ">
												<TD align="right" class="column" width="20%">
													��ַ����ʼ��ַ
												</TD>
												<TD width="30%">
													<input type="text" name="dhcpMinAddr" id="dhcpMinAddr"
														class="bk" value="">
												</TD>
												<TD align="right" class=column width="20%">
													��ַ�ؽ�����ַ
												</TD>
												<TD width="30%">
													<input type="text" name="dhcpMaxAddr" id="dhcpMaxAddr"
														class="bk" value="">
												</TD>
											</TR>
											<!--
											<TR bgcolor="#FFFFFF" name="snmpv3" style="display:" class=column>
												<TD class=column align="right">������ַ�б�</TD>
												<TD >
													<input type="text" name="auth_passwd" id="auth_passwd" class="bk" value="">
												</TD>
												<TD class=column align="right">��ַ����</TD>
												<TD >
													<input type="text" name="auth_passwd" id="auth_passwd" class="bk" value="">
													
												</TD>
											</TR>
											-->
											<TR bgcolor="#FFFFFF" name="dhcpno" style="display: ">
												<TD align="right" class=column>
													Ĭ������
												</TD>
												<TD>
													<input type="text" name="dhcpGateway" id="dhcpGateway"
														class="bk" value="">
												</TD>
												<TD class=column align="right">
													����
												</TD>
												<TD>
													<input type="text" name="day" class="bk" value="" size="6"
														maxlength="6">
													&nbsp;��&nbsp;
													<input type="text" name="hour" class="bk" value="" size="4"
														maxlength="2">
													&nbsp;ʱ&nbsp;
													<input type="text" name="minute" class="bk" value=""
														size="4" maxlength="2">
													&nbsp;��
												</TD>
											</TR>
											<TR bgcolor="#FFFFFF" name="dhcpno" style="display: "
												class=column>
												<TD align="right" class=column>
													DNS��������ַ
												</TD>
												<TD>
													<input type="text" name="dhcpDns" id="dhcpDns" class="bk"
														value="">
													�Զ��ŷָ�
												</TD>
												<TD align="right" class=column>
													<!-- DHCP������ǿ������ -->
												</TD>
												<TD>
													<input type="hidden" name="dhcpDomain" id="dhcpDomain"
														class="bk" value="">

												</TD>
											</TR>
											<TR bgcolor="#FFFFFF">
												<TD colspan="4" align="right" class="green_foot">
													<!-- <button onclick="ExecMod();">
														�� ȡ 
													</button>
													&nbsp;&nbsp;-->
													<button onclick="ExecMod();">
														�� ��
													</button>
													&nbsp;&nbsp;
												</TD>
											</TR>
											<TR bgcolor="#FFFFFF" id="tr002" style="display: none">
												<td colspan="4" valign="top" class=column>
													<div id="div_strategy"
														style="width: 100%; z-index: 1; top: 100px">
													</div>
												</td>
											</TR>
										</TABLE>
									</TD>
								</TR>
							</TABLE>
						</td>
					</tr>
				</table>
			</FORM>
		</TD>
	</TR>
	<TR>
		<!--<TD HEIGHT=20>
			&nbsp;
			<IFRAME ID=childFrm SRC="" STYLE="display: none"></IFRAME>
			<IFRAME ID=childFrm1 SRC="" STYLE="display: none"></IFRAME>
			<IFRAME ID=childFrm2 SRC="" STYLE="display: none"></IFRAME>
		</TD>
	</TR>-->
</TABLE>
