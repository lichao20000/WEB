
package com.linkage.module.gwms.report.bio;

import com.linkage.module.gwms.report.dao.SingleBusEquipmentDAO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SingleBusEquipmentBIO {
    // 日志记录
    private static Logger logger = LoggerFactory.getLogger(SingleBusEquipmentBIO.class);

    private SingleBusEquipmentDAO singleBusEquipmentDAO;


    /**
     * 单个业务统计设备资源报表
     * @param starttime1
     * @param endtime1
     * @return
     */
    public List<Map> getSingleBusEquipmentList(String starttime1, String endtime1) {
        return singleBusEquipmentDAO.getSingleBusEquipmentList(starttime1, endtime1);
    }


    /**
     * 单个业务统计设备资源报表导出Excel
     * @param starttime1
     * @param endtime1
     * @return
     */
    public List<Map> getSingleBusEquipmentExcel(String starttime1, String endtime1) {
        return singleBusEquipmentDAO.getSingleBusEquipmentList(starttime1, endtime1);
    }

    /**
     * 单个业务统计设备资源报表详情
     * @param cityId
     * @param curPage_splitPage
     * @param num_splitPage
     * @param starttime1
     * @param endtime1
     * @return
     */
    public Map<String, Object> querySingleBusEquipmentDetailList(String cityId, int curPage_splitPage, int num_splitPage, String starttime1, String endtime1, String servTypeId) {
        Map<String, Object> result = new HashMap<String, Object>();
        //1、获取老旧终端未完成的设备总数
        int total = singleBusEquipmentDAO.getSingleBusEquipmentDetailCount(cityId, starttime1, endtime1, servTypeId);
        result.put("total", total);
        if (total == 0) {
            result.put("list", null);
            logger.warn("SingleBusEquipmentBIO.querySingleBusEquipmentDetailList this cityId has no SingleBusEquipmentDetailCount, cityId:{}", cityId);
            return result;
        }
        List<Map> deviceList = singleBusEquipmentDAO.querySingleBusEquipmentDetailList(cityId, curPage_splitPage, num_splitPage, starttime1, endtime1, servTypeId);
        result.put("list", deviceList);
        return result;
    }


    /**
     * 单个业务统计设备资源报表详情导出Excel
     * @param cityId
     * @param starttime1
     * @param endtime1
     * @return
     */
    public List<Map> getSingleBusEquipmentDetailExcel(String cityId, String starttime1, String endtime1, String servTypeId) {
        return singleBusEquipmentDAO.getSingleBusEquipmentDetailExcel(cityId, starttime1, endtime1, servTypeId);
    }



    public SingleBusEquipmentDAO getSingleBusEquipmentDAO() {
        return singleBusEquipmentDAO;
    }

    public void setSingleBusEquipmentDAO(SingleBusEquipmentDAO singleBusEquipmentDAO) {
        this.singleBusEquipmentDAO = singleBusEquipmentDAO;
    }
}
