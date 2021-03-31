
package com.linkage.module.gwms.report.bio;

import com.linkage.module.gwms.report.dao.MultiBusEquipmentDAO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MultiBusEquipmentBIO {
    // 日志记录
    private static Logger logger = LoggerFactory.getLogger(MultiBusEquipmentBIO.class);

    private MultiBusEquipmentDAO multiBusEquipmentDAO;


    /**
     * 多个业务统计设备资源报表
     * @param servTypeIdString
     * @return
     */
    public List<Map> getMultiBusEquipmentList(String servTypeIdString) {
        return multiBusEquipmentDAO.getMultiBusEquipmentList(servTypeIdString);
    }


    /**
     * 多个业务统计设备资源报表导出Excel
     * @return
     */
    public List<Map> getMultiBusEquipmentExcel(String servTypeIdString) {
        return multiBusEquipmentDAO.getMultiBusEquipmentList(servTypeIdString);
    }

    /**
     * 多个业务统计设备资源报表详情
     * @param cityId
     * @param curPage_splitPage
     * @param num_splitPage
     * @return
     */
    public Map<String, Object> queryMultiBusEquipmentDetailList(String cityId, int curPage_splitPage, int num_splitPage, String servTypeIdString) {
        Map<String, Object> result = new HashMap<String, Object>();
        //1、获取老旧终端未完成的设备总数
        int total = multiBusEquipmentDAO.getMultiBusEquipmentDetailCount(cityId, servTypeIdString);
        result.put("total", total);
        if (total == 0) {
            result.put("list", null);
            logger.warn("MultiBusEquipmentBIO.queryMultiBusEquipmentDetailList this cityId has no MultiBusEquipmentDetailCount, cityId:{}", cityId);
            return result;
        }
        List<Map> deviceList = multiBusEquipmentDAO.queryMultiBusEquipmentDetailList(cityId, curPage_splitPage, num_splitPage, servTypeIdString);
        result.put("list", deviceList);
        return result;
    }


    /**
     * 多个业务统计设备资源报表详情导出Excel
     * @param cityId
     * @param servTypeIdString
     * @return
     */
    public List<Map> getMultiBusEquipmentDetailExcel(String cityId, String servTypeIdString) {
        return multiBusEquipmentDAO.getMultiBusEquipmentDetailExcel(cityId, servTypeIdString);
    }

    public MultiBusEquipmentDAO getMultiBusEquipmentDAO() {
        return multiBusEquipmentDAO;
    }

    public void setMultiBusEquipmentDAO(MultiBusEquipmentDAO multiBusEquipmentDAO) {
        this.multiBusEquipmentDAO = multiBusEquipmentDAO;
    }
}
