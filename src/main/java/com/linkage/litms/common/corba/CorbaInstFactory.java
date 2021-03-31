package com.linkage.litms.common.corba;


import javax.servlet.http.HttpSession;

import RemoteDB.DB;
import RemoteDB.SQLException;

// Referenced classes of package com.linkage.litms.common.corba:
//            WebTopoCorbaInst, CorbaInst, AdslCorbaInst

public class CorbaInstFactory
{
//	private Logger log = Logger.getLogger(CorbaInstFactory.class);
    public CorbaInstFactory()
    {
    }

    public static org.omg.CORBA.Object factory(String name,HttpSession session)
    {
        if(name.equals("webtopo"))
        {
            CorbaInst corba = new WebTopoCorbaInst();

            DB dbInstance = (DB)corba.getInstance();
            try
            {
                return dbInstance.createWebTopoManager("");
            }
            catch(SQLException e)
            {
                e.printStackTrace();
            }
            return null;
        }

        if(name.equals("db"))
        {
            CorbaInst corba = new WebTopoCorbaInst();
            DB dbInstance = (DB)corba.getInstance();
            return dbInstance;
        } else
        {
            return null;
        }
    }


    public static org.omg.CORBA.Object factory(String name)
    {
        if(name.equals("webtopo"))
        {
            CorbaInst corba = new WebTopoCorbaInst();
            DB dbInstance = (DB)corba.getInstance();
//            log.debug("corba instance = "+dbInstance);
            try
            {
                return dbInstance.createWebTopoManager("");
            }
            catch(SQLException e)
            {
                e.printStackTrace();
            }
            return null;
        }


        if(name.equals("db"))
        {
            CorbaInst corba = new WebTopoCorbaInst();
            DB dbInstance = (DB)corba.getInstance();
            return dbInstance;
        } else
        {
            return null;
        }
    }
}
