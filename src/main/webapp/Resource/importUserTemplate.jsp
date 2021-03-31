<%
response.setContentType("text/plain");
response.setHeader("Content-disposition","attachment; filename=example.csv" );
out.print("oui,设备序列号,宽带帐号（必填）,宽带密码（必填）,Adsl绑定电话");
out.print(",开通时间(格式：2008-01-15 12:00:00),最大下行可达速率,上行承诺速率");
out.print(",允许用户上网数,VlanID号,VpiID号（必填）,VciID号（必填）,属地名称（必填 中文描述，请保证名称和系统中一致）");
out.print(",局向标识（中文描述，请保证名称和系统中一致）,小区标识（中文描述，请保证名称和系统中一致）,联系人,业务类型（id 必填）");
out.print(",联系电话,用户类型（必填 0、公司客户　1、网吧客户　2、个人客户）,用户状态（必填 1:开户 2:暂停 3:销户 4:更换设备）");
out.println();
%>
<%@ page contentType="text/html;charset=GBK"%>

