
package com.linkage.module.gwms.report.bio;

import com.linkage.module.gwms.report.dao.QueryBatchSpeedMeasureDAO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class QueryBatchSpeedMeasureBIO {
    // 日志记录
    private static Logger logger = LoggerFactory.getLogger(QueryBatchSpeedMeasureBIO.class);

    private QueryBatchSpeedMeasureDAO queryBatchSpeedMeasureDAO;


    /**
     * 批量测速结果查询列表
     * @param starttime1
     * @param endtime1
     * @return
     */
    public List<Map> getBatchSpeedMeasureList(String starttime1, String endtime1, String taskName) {
        return queryBatchSpeedMeasureDAO.getBatchSpeedMeasureList(starttime1, endtime1, taskName);
    }


    /**
     * 批量测速结果查询导出Excel
     * @param starttime1
     * @param endtime1
     * @return
     */
    public List<Map> getBatchSpeedMeasureExcel(String starttime1, String endtime1, String taskName) {
        return queryBatchSpeedMeasureDAO.getBatchSpeedMeasureList(starttime1, endtime1, taskName);
    }

    /**
     * 批量测速结果查询详情
     * @param cityId
     * @param curPage_splitPage
     * @param num_splitPage
     * @param starttime1
     * @param endtime1
     * @param downlink
     * @return
     */
    public Map<String, Object> queryBatchSpeedMeasureDetailList(String cityId, int curPage_splitPage, int num_splitPage, String starttime1, String endtime1, String downlink, String taskName, String numType) {
        Map<String, Object> result = new HashMap<String, Object>();
        //1、获取老旧终端未完成的设备总数
        int total = queryBatchSpeedMeasureDAO.getBatchSpeedMeasureDetailCount(cityId, starttime1, endtime1, downlink, taskName, numType);
        result.put("total", total);
        if (total == 0) {
            result.put("list", null);
            logger.warn("QueryBatchSpeedMeasureBIO.queryBatchSpeedMeasureDetailList this cityId has no BatchSpeedMeasureDetailCount, cityId:{}", cityId);
            return result;
        }
        List<Map> deviceList = queryBatchSpeedMeasureDAO.queryBatchSpeedMeasureDetailList(cityId, curPage_splitPage, num_splitPage, starttime1, endtime1, downlink, taskName, numType);
        result.put("list", deviceList);
        return result;
    }




    /**
     * 批量测速结果查询详情导出Excel
     * @param cityId
     * @param downlink
     * @param starttime1
     * @param endtime1
     * @return
     */
    public List<Map> getBatchSpeedMeasureDetailExcel(String cityId, String downlink, String starttime1, String endtime1, String taskName, String numType) {
        return queryBatchSpeedMeasureDAO.getBatchSpeedMeasureDetailExcel(cityId, downlink, starttime1, endtime1, taskName, numType);
    }

    public QueryBatchSpeedMeasureDAO getQueryBatchSpeedMeasureDAO() {
        return queryBatchSpeedMeasureDAO;
    }

    public void setQueryBatchSpeedMeasureDAO(QueryBatchSpeedMeasureDAO queryBatchSpeedMeasureDAO) {
        this.queryBatchSpeedMeasureDAO = queryBatchSpeedMeasureDAO;
    }
}
