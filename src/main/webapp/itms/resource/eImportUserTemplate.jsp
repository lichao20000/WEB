<%
response.setContentType("text/plain");
response.setHeader("Content-disposition","attachment; filename=workMemorandumTemplete.csv" );
out.print("终端厂家,终端类型,终端型号,硬件版本");
out.print(",软件版本,工作事项");
out.print(",厂家联系人,厂家联系方式,接待人员,开始时间(yyyy-MM-dd HH:mm:ss),结束时间(yyyy-MM-dd HH:mm:ss)");
out.print(",工作内容,完成状态(0:未完成1进行中2:已完成),备注");
out.println();
%>
<%@ page contentType="text/html;charset=GBK"%>

