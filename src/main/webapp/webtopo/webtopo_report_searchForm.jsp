<%@ include file="../timelater.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>
<%@ page import="com.linkage.litms.flux.FluxComm,
				 com.linkage.litms.common.database.Cursor,
				 java.util.*,
                 com.linkage.litms.common.util.*"%>
<%
request.setCharacterEncoding("GBK");
String device_id = request.getParameter("dev_id");

FluxComm flux = new FluxComm(request);
Cursor cursor = flux.getDeviceType();

SimpleDateFormat  sdf=new SimpleDateFormat("yyyy-MM-dd");
String dateValue=sdf.format(new Date((new Date()).getTime()-86400000));
long longValue=(new Date().getTime()-86400000)/1000;


%>
<% out.println("<div id='idLayerTable'>");%>
<TABLE border=0 cellspacing=0 cellpadding=0 width="100%">
    <TR>
    <TD align="center" bgcolor="#FFFFFF"> 
        <FORM method=POST action="" NAME="frm" onsubmit="return checkForm()" target="childFrm">
            <TABLE width="95%" border=0 align="center" cellpadding=0 cellspacing=0 bgcolor="#999999" class="text">
                <TR><TD ><input type="hidden" name="dev_id" value="<%=device_id%>">
                    <TABLE border=0 cellspacing=1 cellpadding=2 width="100%">
                <TR bgcolor="#FFFFFF"> 
                  <td colspan=2 class="blue_title"> <div align="center" >�豸�˿�����ͳ��--<span id="title">���豸�˿�������ֵ��</span></div></td>
                </TR>
                <TR bgcolor="#FFFFFF"> 
                  <TD width="180" >&nbsp;��ѡ��Ҫ�鿴��������ͣ�</TD>
                  <TD > <input name="SearchType" type="radio" value="1" checked  onClick="javascript:searchType();">
                    �ձ��� 
                    <input type="radio" name="SearchType" value="2" onClick="javascript:searchType();">
                    �ܱ��� 
                    <input type="radio" name="SearchType" value="3" onClick="javascript:searchType();">
                    �±��� 
                    <input type="radio" name="SearchType" value="4" onClick="javascript:searchType();">
                    �걨�� 
                    <input type="radio" name="SearchType" value="5" onClick="javascript:searchType();"  disabled>
                    �׶α���</TD>
                </TR>
                <TR bgcolor="#FFFFFF" id="r1"  style="display:none"> 
                  <TD >&nbsp;��ѡ��׶α���Ĳ�ѯ��ʽ��</TD>
                  <TD><select name="dayType" onchange="javascript:typeChange();">
                      <option value="0">������β�ѯ</option>
                      <option value="1">���ռ����ѯ</option>
                    </select></TD>
                </TR>
                <TR bgcolor="#FFFFFF" id="r2"  style="display:none"> 
                  <TD >&nbsp;��ѡ��ʱ��Σ�</TD>
                  <TD >��ʼ���ڣ� 
                    <input type="text" name="s_day" class=bk> <input type="button" value="��" class=btn onClick="showCalendar('day',event)" name="button"> 
                    &nbsp;�������ڣ� 
                    <input type="text" name="e_day" class=bk> <input type="button" value="��" class=btn onClick="showCalendar('day',event)" name="button"> 
                  </TD>
                </TR>
                <TR bgcolor="#FFFFFF" id="r3"  style="display:none"> 
                  <TD >&nbsp;��ѡ��ʱ�䣺</TD>
                  <TD ><input type="text" name="allday" class=bk size="70"> <input type="button" value="����" class=btn onClick="showCalendar('addday');" name="button"> 
                    <input type="button" value="���" class=btn onclick="javascript:frm.allday.value='';" name="clear"> 
                  </TD>
                </TR>
                <TR bgcolor="#FFFFFF" id="r4" style="display:"> 
                  <TD >&nbsp;��ѡ����Ҫ�鿴�����<FONT COLOR="red"><span id="strdata">�գ�</span></FONT></TD>
                  <TD > <span id="changeStart"> <INPUT TYPE="text" NAME="day" class=bk value=<%=dateValue%>> <INPUT TYPE="button" class=btn onclick="showCalendar('day',event)" value="��"> 
                    <INPUT TYPE="hidden" name="hidday"> </span></TD>
                </TR>
                
                <TR bgcolor="#FFFFFF" > 
                  <TD >&nbsp;ѡ���������ͣ�<br> <INPUT name="checkbox" TYPE="checkbox"  id="checkbox" onclick="selectAll('kind')"> 
                    &nbsp;ȫѡ</TD>
                  <TD > <table width="100%" border="0" cellpadding="0" cellspacing="0">
                      <tr> 
                        <td><span> 
                          <input name="kind" type="checkbox"  id="kind" value="ifinoctetsbps" checked>
                          ��������(bps)&nbsp;</span> </td>
                        <td> <input name="kind" type="checkbox" id="kind" value="ifindiscardspps">
                          ���붪����&nbsp;</td>
                        <td> <input name="kind" type="checkbox"  id="kind" value="ifinerrors">
                          ����������&nbsp; </td>
                        <td> <input name="kind" type="checkbox"  id="kind" value="ifinoctetsbpsmax">
                          �����ֵ</td>
                        <td> <input name="kind" type="checkbox" id="kind" value="ifinucastpktspps">
                          ÿ�����뵥������ </td>
                        <td> <input name="kind" type="checkbox" id="kind" value="u1">
                          �������������</td>
                      </tr>
                      <tr> 
                        <td><input name="kind" type="checkbox" id="kind" value="ifoutoctetsbps" checked>
                          ��������(bps)&nbsp;</td>
                        <td><input name="kind" type="checkbox"  id="kind" value="ifoutdiscardspps">
                          ����������&nbsp;</td>
                        <td><input name="kind" type="checkbox"  id="kind" value="ifouterrors">
                          �����������</td>
                        <td><input name="kind" type="checkbox"  id="kind" value="ifoutoctetsbpsmax">
                          ������ֵ</td>
                        <td><input name="kind" type="checkbox" id="kind" value="ifinnucastpktspps">
                          ÿ������ǵ�������</td>
                        <td><input type="checkbox" name="kind" value="u2" >
                          ��������������</td>
                      </tr>
                      <tr> 
                        <td><input name="kind" type="checkbox" id="kind" value="ifinerrorspps">
                          ��������</td>
                        <td><input name="kind" type="checkbox"  id="kind" value="ifinoctets">
                          �����ֽ���</td>
                        <td><input name="kind" type="checkbox"  id="kind" value="ifindiscards">
                          ���붪������&nbsp;</td>
                        <td><input name="kind" type="checkbox"  id="kind" value="u3">
                          �������������</td>
                        <td><input name="kind" type="checkbox"  id="kind" value="ifoutucastpktspps">
                          ÿ��������������&nbsp;</td>
                        <td>&nbsp;</td>
                      </tr>
                      <tr> 
                        <td><input name="kind" type="checkbox"  id="kind" value="ifouterrorspps">
                          ���������</td>
                        <td><input name="kind" type="checkbox"id="kind" value="ifoutoctets">
                          �����ֽ���&nbsp;</td>
                        <td><input name="kind" type="checkbox" id="kind" value="ifoutdiscards">
                          ������������&nbsp;</td>
                        <td><input name="kind" type="checkbox"  id="kind" value="u4">
                          �������������</td>
                        <td><input name="kind" type="checkbox"  id="kind" value="ifoutnucastpktspps">
                          ÿ�������ǵ�������</td>
                        <td>&nbsp;</td>
                      </tr>
                      <tr>
                        <td><input name="kind" type="checkbox"  id="kind" value="ifinunknownprotospps">
                          ÿ������δ֪Э�����</td>
                        <td><input name="kind" type="checkbox"  id="kind" value="ifoutqlenpps">
                          ÿ���������д�С</td>
                        <td>&nbsp;</td>
                        <td>&nbsp;</td>
                      </tr>
                    </table></TD>
                </TR>
                <TR bgcolor="#FFFFFF"> 
                  <TD bgcolor="#FFFFFF" >&nbsp;��ѡ���豸�˿ڣ�<br> <INPUT TYPE="checkbox" onclick="selectAll('port')"> 
                    &nbsp;ȫѡ</TD>
                  <TD><SPAN id=device_port>����ѡ���豸</SPAN></TD>
                </TR>
                <TR bgcolor="#FFFFFF"  onmouseout="className='blue_trOut'"> 
                  <TD colspan=2 align=right class="blue_foot"> <div align="right"> 
                      <input type="hidden" name="queryTime" >
                      <input type="hidden" name="queryMonth">
					  
                      <INPUT TYPE="submit" name="cmdOK" value=" ͳ �� "  class="jianbian">
                      &nbsp;&nbsp;&nbsp;&nbsp; </div></TD>
                </TR>
              </TABLE>
                </TD></TR>
            </TABLE>
        </FORM>
        <DIV id="idLayerView1" style="overflow:auto;width:1200px;display:none"></div>
    </TD></TR>
    <TR><TD HEIGHT=20>&nbsp;</TD></TR>
</TABLE>
<%out.println("</div>");%>
<SCRIPT LANGUAGE="JavaScript">
    <!--
	if(typeof(parent.maxspeed) == "object"){
		parent.maxspeed.innerHTML = idLayerTable.innerHTML;	
		parent.showport();
		parent.frm.hidday.value=<%=longValue%>;
		//alert(parent.frm.hidday.value);
    }
    //-->
</SCRIPT>