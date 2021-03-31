<%@ page contentType="text/html;charset=GBK" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<link rel="stylesheet" href="<s:url value="/css3/c_table.css"/>"
      type="text/css">
<link rel="stylesheet" href="<s:url value="/css3/global.css"/>"
      type="text/css">

<table class="listtable">
    <caption>
        	查询结果
    </caption>
    <s:if test="dataList.size()>0">
        <thead>
	        <tr>
	            <th class="title_1" rowspan="2">本地网</th>
	            <th colspan="3">设备是否确认</th>
	            <th colspan="4">设备是否在线</th>
	        </tr>
	        <tr>
	            <th>已确认</th>
	            <th>未确认</th>
	            <th>设备总数</th>
	            <th>设备在线数</th>
	            <th>设备下线数</th>
	            <th>设备未知状态</th>
	            <th>设备总数</th>
	        </tr>
	    </thead>

        <tbody>
        <s:iterator value="dataList">
            <tr>
                <td align="center">
                    <s:property value="city_name"/>
                </td>
                <td align="center">
                	<a href='DeviceList4Sxlt.jsp?stat_type=confirmed&gwType=<s:property value="gwType"/>&city_id=<s:property value="city_id"/>&starttime=<s:property value="starttime"/>&endtime=<s:property value="endtime"/>' 
                		target='_blank'>
                		<s:property value="confirmed"/>
                	</a>
                </td>
                <td align="center">
                	<a href='DeviceList4Sxlt.jsp?stat_type=unconfirmed&gwType=<s:property value="gwType"/>&city_id=<s:property value="city_id"/>&starttime=<s:property value="starttime"/>&endtime=<s:property value="endtime"/>'
                		target='_blank'>
                		<s:property value="unconfirmed"/>
                	</a>
                </td>
                <td align="center">
                	<a href='DeviceList4Sxlt.jsp?stat_type=isconfirmed&gwType=<s:property value="gwType"/>&city_id=<s:property value="city_id"/>&starttime=<s:property value="starttime"/>&endtime=<s:property value="endtime"/>'
                		target='_blank'>
                		<s:property value="isconfirmed"/>
                	</a>
                </td>
                <td align="center">
                	<a href='DeviceList4Sxlt.jsp?stat_type=online&gwType=<s:property value="gwType"/>&city_id=<s:property value="city_id"/>&starttime=<s:property value="starttime"/>&endtime=<s:property value="endtime"/>' 
                		target='_blank'>
                		<s:property value="online"/>
                	</a>
                </td>
                <td align="center">
                	<a href='DeviceList4Sxlt.jsp?stat_type=offline&gwType=<s:property value="gwType"/>&city_id=<s:property value="city_id"/>&starttime=<s:property value="starttime"/>&endtime=<s:property value="endtime"/>'
                		target='_blank'>
                		<s:property value="offline"/>
                	</a>
                </td>
                <td align="center">
                	<a href='DeviceList4Sxlt.jsp?stat_type=unknow&gwType=<s:property value="gwType"/>&city_id=<s:property value="city_id"/>&starttime=<s:property value="starttime"/>&endtime=<s:property value="endtime"/>'
                		target='_blank'>
                		<s:property value="unknow"/>
                	</a>
                </td>
                <td align="center">
                	<a href='DeviceList4Sxlt.jsp?stat_type=isonline&gwType=<s:property value="gwType"/>&city_id=<s:property value="city_id"/>&starttime=<s:property value="starttime"/>&endtime=<s:property value="endtime"/>'
                		target='_blank'>
                		<s:property value="isonline"/>
                	</a>
                </td>
            </tr>
        </s:iterator>
        </tbody>
        <tfoot>
        <tr>
            <td colspan='17'>
                <IMG SRC="<s:url value="/images/excel.gif"/>" BORDER='0' ALT='导出列表'
                     style='cursor: hand'
                     onclick="ToExcel('<s:property value="cityId"/>','<s:property value="starttime"/>',
                             '<s:property value="endtime"/>','<s:property value="gwType"/>')">
            </td>
        </tr>
        </tfoot>
    </s:if>
    <s:else>
        <tfoot>
        <tr>
            <td align="left">
				没有相关信息
            </td>
        </tr>
        </tfoot>
    </s:else>
    <tr STYLE="display: none">
        <td colspan="9">
            <iframe id="childFrm" src=""></iframe>
        </td>
    </tr>
</table>


