<%@ include file="../timelater.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>
<%
request.setCharacterEncoding("GBK");
%>
<%@ include file="../head.jsp"%>
<%@ include file="../toolbar.jsp"%>
<TABLE border=0 cellspacing=0 cellpadding=0 width="100%">

<tr>
    <td height="303" align="center" valign="top">
      <table width="90%"  border="0" cellspacing="0" cellpadding="0">
        <tr>
          <td height="20">&nbsp;</td>
        </tr>
        <tr>
          <td height="20"><table width="90%"  border="0" align="center" cellpadding="2" cellspacing="1" bgcolor="#666666">
              <tr>
                <td height="19" background="../images/table_title.jpg"><div align="center"><strong>  <strong>统一配置考核指标</strong></strong></div></td>
              </tr>
              <tr>
                <td bgcolor="#FFFFFF"><br>
                  <table width="100%"  border="0" cellspacing="0" cellpadding="0">
                  <tr>
                    <td height="20" valign="top" align="center"><span class="text"><strong>请选择指标参数:</strong></span><br>                      <table width="159"  border="0" cellpadding="0" cellspacing="5">
                      <tr>
                        <td height="20" background="../images/button_back1.gif"><div align="center"><span class="text"> <a href="ConfigAdsl.jsp"  class="black">通道达标率</a></span></div></td>
                      </tr>
                      <tr>
                        <td height="20" background="../images/button_back1.gif"><div align="center"><span class="text"> <a href="ConfigPPOE.jsp" class="black">PPPOE 接通率</a></span></div></td>
                      </tr>
                      <tr>
                        <td height="20" background="../images/button_back1.gif"><div align="center"><span class="text"> <a href="ConfigOSS.jsp" class="black">网管可用率</a></span></div></td>
                      </tr>
                    </table></td>
                    </tr>
                </table>                
                <br></td>
              </tr>
              <tr>
                <td height="19" background="../images/table_title.jpg"><div align="center"><strong>
                  </strong></div></td>
              </tr>
          </table></td>
        </tr>
      </table></td>
  </tr>
</TABLE>
<%@ include file="../foot.jsp"%>