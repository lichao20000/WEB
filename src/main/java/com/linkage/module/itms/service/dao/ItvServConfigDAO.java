package com.linkage.module.itms.service.dao;

import com.linkage.commons.db.DBOperation;
import com.linkage.commons.db.PrepareSQL;
import com.linkage.module.gwms.dao.SuperDAO;

import java.util.ArrayList;
import java.util.HashMap;

public class ItvServConfigDAO extends SuperDAO {

    public ArrayList<HashMap<String, String>> query(String loid) {
        PrepareSQL psql = new PrepareSQL();
        psql.append("select a.username loid,b.vlanid,b.username itvaccount from tab_hgwcustomer a,hgwcust_serv_info b where a.user_id = b.user_id " +
                " and a.username = ? and b.serv_type_id = 11 and b.open_status = 1 ");
        psql.setString(1,loid);
        return DBOperation.getRecords(psql.getSQL());
    }
}
