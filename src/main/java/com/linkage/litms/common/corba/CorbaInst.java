package com.linkage.litms.common.corba;

import java.util.Map;

import com.linkage.commons.db.PrepareSQL;
import com.linkage.litms.common.database.DataSetBean;

public abstract class CorbaInst {
//    private NamingContext ncRef = null;

    protected String getNameIor() {
        String object_name = "NameService";
        String object_poa = "/rootPoa";
        return getNameIor(object_name, object_poa);
    }

    protected String getNameIor(String object_name, String object_poa) {
        String ior = null;
        String strSQL = "select ior from tab_ior where object_name='"
                + object_name + "' " + "and object_poa='" + object_poa
                + "' and object_port=0";
        PrepareSQL psql = new PrepareSQL(strSQL);
        Map fields = DataSetBean.getRecord(psql.getSQL());
        if (fields != null) {
            ior = (String) fields.get("ior");
        }

        return ior;
    }


    public abstract boolean bind();



    public abstract org.omg.CORBA.Object getInstance();
}
