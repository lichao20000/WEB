package com.linkage.module.gwms.report.thread;

import com.linkage.module.gwms.report.dao.MulticastDownReportDAO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

/**
 * @author songxq
 * @version 1.0
 * @category
 * @copyright Ailk NBS-Network Mgt. RD Dept.
 * @since 2020/6/23.
 */
public class MulticastDownThread implements Runnable {

    Logger logger = LoggerFactory.getLogger(MulticastDownThread.class);
    // 持久层

    private String loid;

    private String type;

    private Vector vector;

    public MulticastDownThread(String loid, String type, Vector<Map> vector)
    {
        this.loid = loid;
        this.type = type;
        this.vector = vector;
    }

    @Override
    public void run() {
        Map map = new HashMap();
        MulticastDownReportDAO dao = new MulticastDownReportDAO();
        if("1".equals(type))
        {
            map = dao.queryDevice(loid);
        }
        else if("2".equals(type))
        {
            map = dao.queryBusiness(loid);
        }
        vector.add(map);
    }
}
