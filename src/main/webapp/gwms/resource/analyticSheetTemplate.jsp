<%
response.setContentType("text/plain");
response.setHeader("Content-disposition","attachment; filename=example.csv" );
out.println("工单受理时间,逻辑ID,接入方式,用户姓名,家庭住址,属地,宽带账号,宽带密码,宽带vlanID,IP获取方式,语音WAN连接IP地址,语音WAN连接网关,语音WAN连接子网掩码,主MGC地址,主MGC端口,备MGC地址	,备MGC端口,语音注册域名（软交换）,终端物理标识,电话号码,iptv账号,IPTV个数");
%>
<%@ page contentType="text/html;charset=GBK"%>



