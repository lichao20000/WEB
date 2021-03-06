package com.linkage.litms.system.dbimpl;

import java.util.ArrayList;
import java.util.HashMap;

import com.linkage.commons.db.PrepareSQL;
import com.linkage.litms.common.database.Cursor;
import com.linkage.litms.common.database.DataSetBean;

public class DbMainMenu
{
    private PrepareSQL pSQL = null;
    public DbMainMenu()
    {
        if (pSQL == null)
        {
            pSQL = new PrepareSQL();
        }
    }
    
    private String getLayer1MenuSql = "select keyword, linkid, id, label, url, target, submenu, icon from tab_mainmenu where layer = 1 order by linkid";
    private String getLayer2MenuSql = "select keyword, linkid, id, label, url, target, submenu, icon from tab_mainmenu where layer = 2 and id = ? order by linkid";
    private String getParamByKeySql = "select paramtype, paramname, paramvalue from tab_menuparam where keyword = ?";
    private String updateMenunameAndURLSql = "update tab_mainmenu set label = ?, url = ? where keyword = ?";
    private String deleteParamByKeySql = "delete from tab_menuparam where keyword = ?";
    private String addByKeySql = "insert into tab_menuparam (keyword, paramtype, paramname, paramvalue) values (?, ?, ?, ?)";
    private String deleteMenuByKeySql = "delete from tab_mainmenu where keyword = ?";
    private String deleteFromTab_role_operatorSql = "delete from tab_role_operator where operator_id = (select operator_id from tab_operator where operator_name = ?)";
    private String deleteFromTab_operatorSql = "delete from tab_operator where operator_name = ?";
    private String getMaxLinkidSql = "select max(linkid) as linkid from tab_mainmenu where id = ?";
    private String addMainmenuSql = "insert into tab_mainmenu (keyword, layer, linkid, id, label, url, target, submenu, icon) values (?, ?, ?, ?, ?, ?, ?, ?, ?)";
    private String addMenuparamSql = "insert into tab_menuparam (keyword, paramtype, paramname, paramvalue) values (?, ?, ?, ?)";
    private String getOperator_idByOperator_nameSql = "select operator_id from tab_operator where operator_name = ?";
    private String getMaxOperator_idSql = "select max(operator_id) as operator_id from tab_operator";
    private String addIntoTab_operatorSql = "insert into tab_operator (operator_id, operator_pid, operator_layer, operator_type, operator_name, remark) values (?, ?, ?, ?, ?, ?)";
    private String addIntoTab_role_operatorSql = "insert into tab_role_operator (role_id, operator_id) values (?, ?)";
    
    //?????????sql
    public void doBatchSql(ArrayList list)
    {
        DataSetBean.doBatch(list);
    }
    
    //???????????????????????????
    public Cursor getLayer1Menu()
    {
        pSQL.setSQL(getLayer1MenuSql);
        return DataSetBean.getCursor(pSQL.getSQL());
    }
    
    //??????id???????????????????????????
    public Cursor getLayer2MenuById(String id)
    {
        pSQL.setSQL(getLayer2MenuSql);
        pSQL.setStringExt(1, id, true);
        return DataSetBean.getCursor(pSQL.getSQL());
    }
    
    //??????key??????tab_menuparam???????????????
    public Cursor getParamByKey(String key)
    {
        pSQL.setSQL(getParamByKeySql);
        pSQL.setStringExt(1, key, true);
        return DataSetBean.getCursor(pSQL.getSQL());
    }
    
    //??????????????????URL??????tab_mainmenu
    public String updateMenunameAndURL(String key, String menuname, String url)
    {
        pSQL.setSQL(updateMenunameAndURLSql);
        pSQL.setStringExt(1, menuname, true);
        pSQL.setStringExt(2, url, true);
        pSQL.setStringExt(3, key, true);
        return pSQL.getSQL();
    }
    
    //??????key??????tab_menuparam???????????????
    public String deleteParamByKey(String key)
    {
        pSQL.setSQL(deleteParamByKeySql);
        pSQL.setStringExt(1, key, true);
        return pSQL.getSQL();
    }
    
    //??????tab_menuparam???????????????
    public void addByKey(String key, String[] paramArray)
    {
        ArrayList sqlList = new ArrayList();
        String[] param = null;
        pSQL.setSQL(addByKeySql);
        for (int i = 0; i < paramArray.length; i++)
        {
            param = paramArray[i].split(",");
            pSQL.setStringExt(1, key, true);
            pSQL.setStringExt(2, param[0], true);
            pSQL.setStringExt(3, param[1], true);
            pSQL.setStringExt(4, param[2], true);
            sqlList.add(pSQL.getSQL());
        }
        DataSetBean.doBatch(sqlList);
    }
    
    //??????key??????tab_mainmenu???????????????
    public String deleteMenuByKey(String key)
    {
        pSQL.setSQL(deleteMenuByKeySql);
        pSQL.setStringExt(1, key, true);
        return pSQL.getSQL();
    }
    
    //??????key????????????tab_role_operator
    public String deleteFromTab_role_operatorByKey(String key)
    {
        pSQL.setSQL(deleteFromTab_role_operatorSql);
        pSQL.setStringExt(1, key, true);
        return pSQL.getSQL();
    }
    
    //??????key????????????tab_operator
    public String deleteFromTab_operatorByKey(String key)
    {
        pSQL.setSQL(deleteFromTab_operatorSql);
        pSQL.setStringExt(1, key, true);
        return pSQL.getSQL();
    }
    
    //????????????????????????????????????linkid??????????????????linkid???
    public String getMaxLinkid(String submenu)
    {
        HashMap map = new HashMap();
        pSQL.setSQL(getMaxLinkidSql);
        pSQL.setStringExt(1, submenu, true);
        map = DataSetBean.getRecord(pSQL.getSQL());
        return (String)map.get("linkid");
    }
    
    //?????????????????????tab_mainmenu????????????
    public String addMainmenu(String keyword, String layer, String linkid, String id, String label, String url, String target, String submenu, String icon)
    {
        pSQL.setSQL(addMainmenuSql);
        pSQL.setStringExt(1, keyword, true);
        pSQL.setStringExt(2, layer, false);
        pSQL.setStringExt(3, linkid, true);
        pSQL.setStringExt(4, id, true);
        pSQL.setStringExt(5, label, true);
        pSQL.setStringExt(6, url, true);
        pSQL.setStringExt(7, target, true);
        pSQL.setStringExt(8, submenu, true);
        pSQL.setStringExt(9, icon, true);
        //DataSetBean.executeUpdate(pSQL.getSQL());
        return pSQL.getSQL();
    }
    
    //??????????????????????????????tab_menuparam????????????
    public ArrayList addMenuparam(String keyword, String[] paramArray)
    {
        ArrayList sqlList = new ArrayList();
        String[] param = null;
        pSQL.setSQL(addMenuparamSql);
        for (int i = 0; i < paramArray.length; i++)
        {
            param = paramArray[i].split(",");
            pSQL.setStringExt(1, keyword, true);
            pSQL.setStringExt(2, param[0], true);
            pSQL.setStringExt(3, param[1], true);
            pSQL.setStringExt(4, param[2], true);
            sqlList.add(pSQL.getSQL());
        }
        return sqlList;
    }
    
    //??????key??????tab_operator?????????operator_id
    public String getOperator_idByOperator_name(String operator_name)
    {
        HashMap map = new HashMap();
        pSQL.setSQL(getOperator_idByOperator_nameSql);
        pSQL.setStringExt(1, operator_name, true);
        map = DataSetBean.getRecord(pSQL.getSQL());
        return (String)map.get("operator_id");
    }
    
    //??????tab_operator??????operator_id????????????
    public String getMaxOperator_id()
    {
        HashMap map = new HashMap();
        pSQL.setSQL(getMaxOperator_idSql);
        map = DataSetBean.getRecord(pSQL.getSQL());
        return (String)map.get("operator_id");
    }
    
    //??????????????????tab_operator
    public String addIntoTab_operator(String operator_id, String operator_pid, String operator_layer, String operator_type, String operator_name, String remark)
    {
        pSQL.setSQL(addIntoTab_operatorSql);
        pSQL.setStringExt(1, operator_id, false);
        pSQL.setStringExt(2, operator_pid, false);
        pSQL.setStringExt(3, operator_layer, false);
        pSQL.setStringExt(4, operator_type, false);
        pSQL.setStringExt(5, operator_name, true);
        pSQL.setStringExt(6, remark, true);
        return pSQL.getSQL();
    }
    
    //??????????????????tab_role_operator
    public String addIntoTab_role_operator(String role_id, String operator_id)
    {
        pSQL.setSQL(addIntoTab_role_operatorSql);
        pSQL.setStringExt(1, role_id, false);
        pSQL.setStringExt(2, operator_id, false);
        return pSQL.getSQL();
    }
}