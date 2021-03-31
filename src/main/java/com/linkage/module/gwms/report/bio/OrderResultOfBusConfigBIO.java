
package com.linkage.module.gwms.report.bio;

import com.linkage.module.gwms.report.dao.OrderResultOfBusConfigDAO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OrderResultOfBusConfigBIO {
    // 日志记录
    private static Logger logger = LoggerFactory.getLogger(OrderResultOfBusConfigBIO.class);

    private OrderResultOfBusConfigDAO orderResultOfBusConfigDAO;


    /**
     * 业务配置工单结果的统计报表
     * @param starttime1
     * @param endtime1
     * @return
     */
    public List<Map> getOrderResultOfBusConfigList(String starttime1, String endtime1) {
        return orderResultOfBusConfigDAO.getOrderResultOfBusConfigList(starttime1, endtime1);
    }


    /**
     * 业务配置工单结果的统计报表导出Excel
     * @param starttime1
     * @param endtime1
     * @return
     */
    public List<Map> getOrderResultOfBusConfigExcel(String starttime1, String endtime1) {
        return orderResultOfBusConfigDAO.getOrderResultOfBusConfigList(starttime1, endtime1);
    }

    /**
     * 业务配置工单结果的统计报表详情
     * @param cityId
     * @param curPage_splitPage
     * @param num_splitPage
     * @param starttime1
     * @param endtime1
     * @return
     */
    public Map<String, Object> queryOrderResultOfBusConfigDetailList(String cityId, int curPage_splitPage, int num_splitPage, String starttime1, String endtime1, String servTypeId, String numType) {
        Map<String, Object> result = new HashMap<String, Object>();
        //1、获取老旧终端未完成的设备总数
        int total = orderResultOfBusConfigDAO.getOrderResultOfBusConfigDetailCount(cityId, starttime1, endtime1, servTypeId, numType);
        result.put("total", total);
        if (total == 0) {
            result.put("list", null);
            logger.warn("OrderResultOfBusConfigBIO.queryOrderResultOfBusConfigDetailList this cityId has no OrderResultOfBusConfigDetailCount, cityId:{}", cityId);
            return result;
        }
        List<Map> deviceList = orderResultOfBusConfigDAO.queryOrderResultOfBusConfigDetailList(cityId, curPage_splitPage, num_splitPage, starttime1, endtime1, servTypeId, numType);
        result.put("list", deviceList);
        return result;
    }


    /**
     * 业务配置工单结果的统计报表详情导出Excel
     * @param cityId
     * @param starttime1
     * @param endtime1
     * @return
     */
    public List<Map> getOrderResultOfBusConfigDetailExcel(String cityId, String starttime1, String endtime1, String servTypeId, String numType) {
        return orderResultOfBusConfigDAO.getOrderResultOfBusConfigDetailExcel(cityId, starttime1, endtime1, servTypeId, numType);
    }



    public OrderResultOfBusConfigDAO getOrderResultOfBusConfigDAO() {
        return orderResultOfBusConfigDAO;
    }

    public void setOrderResultOfBusConfigDAO(OrderResultOfBusConfigDAO orderResultOfBusConfigDAO) {
        this.orderResultOfBusConfigDAO = orderResultOfBusConfigDAO;
    }
}
