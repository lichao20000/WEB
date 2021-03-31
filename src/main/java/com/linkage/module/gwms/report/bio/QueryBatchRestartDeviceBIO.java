
package com.linkage.module.gwms.report.bio;

import com.linkage.module.gwms.report.dao.QueryBatchRestartDeviceDAO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class QueryBatchRestartDeviceBIO {
    // 日志记录
    private static Logger logger = LoggerFactory.getLogger(QueryBatchRestartDeviceBIO.class);

    private QueryBatchRestartDeviceDAO queryBatchRestartDeviceDAO;


    /**
     * 设备批量重启列表
     * @param starttime1
     * @param endtime1
     * @return
     */
    public List<Map> getBatchRestartDeviceList(String starttime1, String endtime1) {
        return queryBatchRestartDeviceDAO.getBatchRestartDeviceList(starttime1, endtime1);
    }


    /**
     * 设备批量重启详情
     * @param cityId
     * @param curPage_splitPage
     * @param num_splitPage
     * @param starttime1
     * @param endtime1
     * @param numType
     * @return
     */
    public Map<String, Object> queryBatchRestartDevDetailList(String cityId, int curPage_splitPage, int num_splitPage, String starttime1, String endtime1, String numType) {
        Map<String, Object> result = new HashMap<String, Object>();
        //1、获取老旧终端未完成的设备总数
        int total = queryBatchRestartDeviceDAO.getBatchRestartDevDetailCount(cityId, starttime1, endtime1, numType);
        result.put("total", total);
        if (total == 0) {
            result.put("list", null);
            logger.warn("QueryBatchRestartDeviceBIO.queryBatchRestartDevDetailList this cityId has no BatchRestartDevDetailCount, cityId:{}", cityId);
            return result;
        }
        List<Map> deviceList = queryBatchRestartDeviceDAO.queryBatchRestartDevDetailList(cityId, curPage_splitPage, num_splitPage, starttime1, endtime1, numType);
        result.put("list", deviceList);
        return result;
    }


    /**
     * 光宽批量重启查询导出Excel
     * @param starttime1
     * @param endtime1
     * @return
     */
    public List<Map> getBatchRestartDevExcel(String starttime1, String endtime1) {
        return queryBatchRestartDeviceDAO.getBatchRestartDeviceList(starttime1, endtime1);
    }

    /**
     * 光宽批量重启设备详情导出Excel
     * @param cityId
     * @param numType
     * @param starttime1
     * @param endtime1
     * @return
     */
    public List<Map> getBatchRestartDevDetailExcel(String cityId, String numType, String starttime1, String endtime1) {
        return queryBatchRestartDeviceDAO.getBatchRestartDevDetailExcel(cityId, numType, starttime1, endtime1);
    }

    public QueryBatchRestartDeviceDAO getQueryBatchRestartDeviceDAO() {
        return queryBatchRestartDeviceDAO;
    }

    public void setQueryBatchRestartDeviceDAO(QueryBatchRestartDeviceDAO queryBatchRestartDeviceDAO) {
        this.queryBatchRestartDeviceDAO = queryBatchRestartDeviceDAO;
    }
}
