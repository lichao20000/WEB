package com.linkage.module.gwms.report.bio;

import com.linkage.module.gwms.report.dao.LanGatherInfoDAO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author songxq
 * @version 1.0
 * @category
 * @copyright Ailk NBS-Network Mgt. RD Dept.
 * @since 2020/8/4.
 */
public class LanGatherInfoBIO {
    Logger logger = LoggerFactory.getLogger(LanGatherInfoBIO.class);
    private LanGatherInfoDAO dao ;


    public LanGatherInfoDAO getDao() {
        return dao;
    }

    public void setDao(LanGatherInfoDAO dao) {
        this.dao = dao;
    }

    public List<Map> query(String cityId, String startOpenDate, String endOpenDate) {
        List<Map> list = new ArrayList<Map>();
        if("00".equals(cityId) || "".equals(cityId) || "-1".equals(cityId))
        {
            list = dao.queryAllCity(cityId,startOpenDate,endOpenDate);
        }
        else
        {
            Map map = dao.query(cityId,startOpenDate,endOpenDate);
            list.add(map);
        }
        return list;
    }

    public List<Map> queryDetail(int curPage_splitPage, int num_splitPage, String cityId, String startOpenDate, String endOpenDate) {
        return dao.queryDetail(curPage_splitPage,num_splitPage,cityId,startOpenDate,endOpenDate);
    }

    public int queryDetailCount(int curPage_splitPage, int num_splitPage, String cityId, String startOpenDate, String endOpenDate) {
        return dao.queryDetailCount(curPage_splitPage,num_splitPage,cityId,startOpenDate,endOpenDate);
    }

    public ArrayList<Map> exportDetail(String cityId, String startOpenDate, String endOpenDate) {
        return dao.exportDetail(cityId,startOpenDate,endOpenDate);
    }

    /**
     * 根据area_id查询city_id
     * @param area_id
     * @return
     */
    public Map queryCityIdByAreaId(String area_id) {
        return dao.queryCityIdByAreaId(area_id);
    }
}
