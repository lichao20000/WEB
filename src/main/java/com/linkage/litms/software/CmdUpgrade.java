/**
 * 命令备份软件升级
 */
package com.linkage.litms.software;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.linkage.commons.db.PrepareSQL;
import com.linkage.litms.common.database.Cursor;
import com.linkage.litms.common.database.DataSetBean;
import com.linkage.litms.common.util.RPCUtilHandler;
import com.linkage.litms.common.util.StringUtils;

public class CmdUpgrade {
    private HttpServletRequest request = null;

    public CmdUpgrade(HttpServletRequest req) {
        request = req;
    }

    /**
     * 先将多个设备的多个参数属性值获取到,返回字符串
     * @param gather_id
     * @param ior
     * @param device_ids
     * @param param
     */
    
    public Map getParamValue(String[] deviceArr, String[] cmdParamArr) {
        //存放设备对应的 属性和Value组合的map对象
        Map mapDeviceParamValue = new HashMap();

        for (int i = 0; i < deviceArr.length; i++) {
            //获取当前设备的参数值
            Map mapParamValue = RPCUtilHandler.getParamValueMap(cmdParamArr,  deviceArr[i]
                    );
            //如果没有取到值，则不放入map中
            if(mapParamValue != null && mapParamValue.size() != 0)
                mapDeviceParamValue.put(deviceArr[i], mapParamValue);
        }
        
        return mapDeviceParamValue;
    }
    /**
     * 再将多个设备的多个参数属性值设置到设备中,返回成功失败结果。
     * @param cmdDeviceParamValue key：为设备id，value：设备对应的三个参数属性和值的组成的二维数组
     * @param deviceArr 设备id数组
     *
     * @return 执行成功的设备id，以数组封装后返回
     */
    
    public boolean setParamValue(String device_id, Map mapParamValue){
        if(device_id == null)
            return false;
        String[][] cmdParamValueArr = new String[mapParamValue.size()][2];
        Iterator iterator = mapParamValue.entrySet().iterator();
        int i = 0;
        Map.Entry entry = null;
        while(iterator.hasNext()){
            entry = (Map.Entry)iterator.next();
            cmdParamValueArr[i][0] = (String)entry.getKey();
            cmdParamValueArr[i][1] = (String)entry.getValue();
            i ++;
        }
        
        mapParamValue = null;
        
        return RPCUtilHandler.setParamValueMap(cmdParamValueArr, device_id);
    }
    
    /**
     * 返回已经执行完成的工单对应的设备编号数组
     * @param sheetArr 工单数组
     * @return 返回已经执行完成的工单对应的设备编号数组
     */
    public static String getStatusOfSheet(String[] sheetArr){
        String arr_sheetid = null;
        for (int i = 0; i < sheetArr.length; i++) {
            if (i == 0) {
                arr_sheetid = "'" + sheetArr[i] + "'";
            } else {
                arr_sheetid += ",'" + sheetArr[i] + "'";
            }
        }
        //查询已经完成的工单的设备id
        String sql = "select sheet_id,device_id,exec_status,fault_code,receive_time from tab_sheet_report where sheet_id in(" + arr_sheetid + ")";
        PrepareSQL psql = new PrepareSQL(sql);
        Cursor cursor = DataSetBean.getCursor(psql.getSQL());
        Map fields = cursor.getNext();
        StringBuffer sb = new StringBuffer();
        sb.append("result=[");
        while(fields != null){
            //listDevice.add(fields.get("device_id") + "$" + fields.get("sheet_id"));
            sb.append(StringUtils.formateJSON(fields));
            fields = cursor.getNext();
            if(fields != null){
                sb.append(",");
            }
        }
        sb.append("]");
        fields = null;
        cursor = null;
        //logger.debug(sb.toString());
        return sb.toString();
    }
}
