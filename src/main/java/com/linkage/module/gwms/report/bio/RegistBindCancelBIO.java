
package com.linkage.module.gwms.report.bio;

import com.linkage.module.gwms.report.dao.RegistBindCancelDAO;

import java.util.List;
import java.util.Map;

public class RegistBindCancelBIO {

    private RegistBindCancelDAO registBindCancelDAO;


    /**
     * 终端注册、绑定和注销状态的统计报表
     * @param starttime1
     * @param endtime1
     * @return
     */
    public List<Map> getRegistBindCancelList(String starttime1, String endtime1) {
        return registBindCancelDAO.getRegistBindCancelList(starttime1, endtime1);
    }


    /**
     * 终端注册、绑定和注销状态的统计报表 导出Excel
     * @param starttime1
     * @param endtime1
     * @return
     */
    public List<Map> getRegistBindCancelExcel(String starttime1, String endtime1) {
        return registBindCancelDAO.getRegistBindCancelList(starttime1, endtime1);
    }


    public RegistBindCancelDAO getRegistBindCancelDAO() {
        return registBindCancelDAO;
    }

    public void setRegistBindCancelDAO(RegistBindCancelDAO registBindCancelDAO) {
        this.registBindCancelDAO = registBindCancelDAO;
    }
}
