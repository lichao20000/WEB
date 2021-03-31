/*
 * 
 * 创建日期 2006-1-23
 * Administrator suzr
 * TODO 要更改此生成的文件的模板，请转至
 * 窗口 － 首选项 － Java － 代码样式 － 代码模板
 */
package com.linkage.litms.system.dbimpl;

import java.util.ArrayList;
import java.util.Map;

import com.linkage.commons.db.DBUtil;
import com.linkage.commons.db.PrepareSQL;
import com.linkage.litms.common.database.Cursor;
import com.linkage.litms.common.database.DataSetBean;
import com.linkage.litms.system.Role;
import com.linkage.litms.system.RoleNotFoundException;
import com.linkage.module.gwms.Global;

/**
 * 用数据库的方式实现Role接口
 * 
 * @author suzr
 * @version 1.00, 1/23/2006
 * @since Liposs 2.1
 */
public class RoleSyb implements Role {
    private static final int INSERT_OPER = 1;// 新增

    private static final int UPDATE_OPER = 2;// 更新

    /**
     * 角色ID
     */
    private int m_RoleId;

    /**
     * 角色名称
     */
    private String m_RoleName;

    /**
     * 角色描述
     */
    private String m_RoleDesc;

    /**
     * 角色PID
     */
    private int m_Role_Pid;

    /**
     * 操作人员ID
     */
    private int m_Acc_Oid;

    /**
     * 游标
     */
    private Cursor cursor = null;

    private Map fields = null;

//    private Map map = null;

    /**
     * 角色详细信息
     */
    private Map m_RoleInfo = null;

    private PrepareSQL pSQL = null;

    /**
     * 角色权限
     */
    private ArrayList m_RoleQuora = null;

    /**
     * 增加角色对象
     */
    private String m_RoleInfoAdd_SQL = "insert into tab_role (role_id,role_name,role_desc,role_pid,acc_oid) values (?,?,?,?,?)";

    /**
     * 删除角色对象
     */
    private String m_DelRole_SQL = "delete from tab_role where role_id =?";

    /**
     * 更新角色对象
     */
    private String m_UpdateRole_SQL = "update tab_role set role_name=?,role_desc=?,role_pid=?,acc_oid=? where role_id=?";

    /**
     * 根据角色ID获得角色信息
     */
    private String m_RoleInfo_By_RoleId_SQL = "select * from tab_role where role_id=?";

    /**
     * 根据角色ID获取角色操作权限
     */
    private String m_RoleQuora_By_RoleId_SQL = "select operator_id from tab_role_operator where role_id=?";

    /**
     * 写入角色权限
     */
    private String m_RoleQuoraAdd_SQL = "insert into tab_role_operator (role_id,operator_id) values (?,?)";

    /**
     * 删除角色权限
     */
    private String m_RoleQuoraDel_SQL = "delete from tab_role_operator where role_id=?";

    public RoleSyb() {
        if (pSQL == null) {
            pSQL = new PrepareSQL();
        }
    }

    /**
     * 构造带参数角色对象
     * 
     * @param m_RoleId
     */
    public RoleSyb(int m_RoleId) {
        this.m_RoleId = m_RoleId;

        if (pSQL == null) {
            pSQL = new PrepareSQL();
        }

        loadRoleInfo();
    }

    /**
     * 带参数构造，创建角色和权限
     * 
     * @param m_RoleId
     * @param m_RoleName
     * @param m_RoleDesc
     * @param m_Role_Pid
     * @param m_Acc_Oid
     * @param m_RoleQuora
     */
    public RoleSyb(int m_RoleId, String m_RoleName, String m_RoleDesc, int m_Role_Pid, int m_Acc_Oid, ArrayList m_RoleQuora) {
        if (pSQL == null) {
            pSQL = new PrepareSQL();
        }

        this.m_RoleId = m_RoleId;
        this.m_RoleQuora = m_RoleQuora;
        this.m_Acc_Oid = m_Acc_Oid;
        this.m_Role_Pid = m_Role_Pid;
        this.m_RoleName = m_RoleName;
        this.m_RoleDesc = m_RoleDesc;

        // insertIntoDb();
    }

    /**
     * 构造包含Role对象的对象实体
     * 
     * @param m_Role
     */
    public RoleSyb(Role m_Role) {
        if (pSQL == null) {
            pSQL = new PrepareSQL();
        }

        this.m_RoleId = m_Role.getRoleId();
        this.m_RoleQuora = m_Role.getRoleQuora();
        this.m_Acc_Oid = m_Role.getAccoid();
        this.m_Role_Pid = m_Role.getRolePid();
        this.m_RoleName = m_Role.getRoleName();
        this.m_RoleDesc = m_Role.getRoleDesc();
    }

    /**
     * 带参数构造，创建角色对象
     * 
     * @param m_RoleId
     * @param m_RoleName
     * @param m_RoleDesc
     * @param m_Role_Pid
     * @param m_Acc_Oid
     */
    public RoleSyb(int m_RoleId, String m_RoleName, String m_RoleDesc, int m_Role_Pid, int m_Acc_Oid) {
        if (pSQL == null) {
            pSQL = new PrepareSQL();
        }
        
        this.m_RoleId = m_RoleId;
        this.m_Acc_Oid = m_Acc_Oid;
        this.m_Role_Pid = m_Role_Pid;
        this.m_RoleName = m_RoleName;
        this.m_RoleDesc = m_RoleDesc;

        // insertIntoDb();
    }

    /**
     * 角色信息入库
     * 
     * @return boolean
     */
    public boolean insertIntoDb() {
        ArrayList list = new ArrayList();
        list.clear();
        pSQL.setSQL(m_RoleInfoAdd_SQL);
        pSQL.setInt(1, m_RoleId);
        pSQL.setString(2, m_RoleName);
        pSQL.setString(3, m_RoleDesc);
        pSQL.setInt(4, m_Role_Pid);
        pSQL.setInt(5, m_Acc_Oid);
        list.add(pSQL.getSQL());
//        list.add(getInsertMapOfSQL());
        list.addAll(getInsertRoleQuoraOfSQL());

        String[] arr_SQL = (String[]) list.toArray();
        list.clear();
        int[] iCode = DataSetBean.doBatch(arr_SQL);

        return (iCode != null) ? true : false;
    }

    /**
     * 更新角色资料
     * 
     * @return boolean
     */
    public boolean updateRoleInfoToDb() {
        pSQL.setSQL(m_UpdateRole_SQL);
        pSQL.setString(1, m_RoleName);
        pSQL.setString(2, m_RoleDesc);
        pSQL.setInt(3, m_Role_Pid);
        pSQL.setInt(4, m_Acc_Oid);
        pSQL.setInt(5, m_RoleId);
        ArrayList list = new ArrayList();
        list.add(pSQL.getSQL());
        list.add(getSaveRoleQuoraOfSQL(UPDATE_OPER));

        String[] arr_SQL = (String[]) list.toArray();
        list.clear();
        int[] iCode = DataSetBean.doBatch(arr_SQL);

        return (iCode != null) ? true : false;
    }
    
    /**
     * 删除角色对象及权限
     * 
     * @param m_RoleId
     * @return boolean
     */
    public boolean delRole(int m_RoleId) {
        ArrayList list = new ArrayList();
        list.clear();

        pSQL.setSQL(m_DelRole_SQL);
        pSQL.setInt(1, m_RoleId);
        list.add(pSQL.getSQL());

        pSQL.setSQL(m_RoleQuoraDel_SQL);
        pSQL.setInt(1, m_RoleId);
        list.add(pSQL.getSQL());

        String[] arr_SQL = (String[]) list.toArray();
        list.clear();
        int[] iCode = DataSetBean.doBatch(arr_SQL);

        return (iCode != null) ? true : false;
    }

    /**
     * 获取角色ID最大值的下一个
     * 
     * @return int
     */
    public int getRoleUuidId() {
        return Integer.parseInt(String.valueOf(DataSetBean.getMaxId("tab_role", "role_id")));
    }

    /**
     * 保存编辑之后或者是创建新的角色权限
     * 
     * @param type
     * @return ArrayList
     */
    private ArrayList getSaveRoleQuoraOfSQL(int type) {
        ArrayList list = new ArrayList();
        list.clear();

        if (m_RoleQuora == null)
            return null;

        if (type == INSERT_OPER) {
            for (int k = 0; k < m_RoleQuora.size(); k++) {
                pSQL.setSQL(m_RoleQuoraAdd_SQL);

                pSQL.setInt(1, m_RoleId);
                pSQL.setInt(2, Integer.parseInt(String.valueOf(m_RoleQuora.get(k))));

                list.add(pSQL.getSQL());
            }
        } else if (type == UPDATE_OPER) {
            // 删除角色动作表中特定角色动作
            pSQL.setSQL(m_RoleQuoraDel_SQL);
            pSQL.setInt(1, m_RoleId);
            list.add(pSQL.getSQL());
            // 插入角色权限
            pSQL.setSQL(m_RoleQuoraAdd_SQL);
            for (int k = 0; k < m_RoleQuora.size(); k++) {
                pSQL.setSQL(m_RoleQuoraAdd_SQL);

                pSQL.setInt(1, m_RoleId);
                pSQL.setInt(2, Integer.parseInt(String.valueOf(m_RoleQuora.get(k))));

                list.add(pSQL.getSQL());
            }
        }

        return list;
    }

    /**
     * 获得角色权限创建SQL
     * 
     * @return ArrayList
     */
    private ArrayList getInsertRoleQuoraOfSQL() {
        ArrayList list = new ArrayList();
        list.clear();

        if (m_RoleQuora == null)
            return null;

        for (int k = 0; k < m_RoleQuora.size(); k++) {
            pSQL.setSQL(m_RoleQuoraAdd_SQL);

            pSQL.setInt(1, m_RoleId);
            pSQL.setInt(2, Integer.parseInt(String.valueOf(m_RoleQuora.get(k))));

            list.add(pSQL.getSQL());
        }

        return list;
    }

    /**
     * 获得插入角色信息SQL
     * 
     * @return String
     */
//    private String getInsertMapOfSQL() {
//        if (m_RoleInfo == null)
//            return "";
//        pSQL.setSQL(m_RoleInfoAdd_SQL);
//
//        pSQL.setInt(1, m_RoleId);
//        pSQL.setString(2, (String) m_RoleInfo.get("role_name"));
//        pSQL.setString(3, (String) m_RoleInfo.get("role_desc"));
//        pSQL.setInt(4, Integer.parseInt((String) m_RoleInfo.get("role_pid")));
//        pSQL.setInt(5, Integer.parseInt((String) m_RoleInfo.get("acc_oid")));
//
//        return pSQL.getSQL();
//    }

    /**
     * 载入角色信息
     * 
     */
    private void loadRoleInfo() {
        // teledb
        if (DBUtil.GetDB() == Global.DB_MYSQL) {
            m_RoleInfo_By_RoleId_SQL = "select role_id, role_name, role_pid, role_desc, acc_oid from tab_role where role_id=?";
        }

        pSQL.setSQL(m_RoleInfo_By_RoleId_SQL);
        pSQL.setInt(1, m_RoleId);
        Map fields = DataSetBean.getRecord(pSQL.getSQL());
        if (fields != null) {
            this.m_RoleId = Integer.parseInt((String) fields.get("role_id"));
            this.m_RoleName = (String) fields.get("role_name");
            this.m_Role_Pid = Integer.parseInt((String) fields.get("role_pid"));
            this.m_RoleDesc = (String) fields.get("role_desc");
            this.m_Acc_Oid = Integer.parseInt((String) fields.get("acc_oid"));

            // 获得角色对应权限
            loadRoleQuoraFromDb();
        } else {
            try {
                throw new RoleNotFoundException("角色ID：" + m_RoleId + " 在表（tab_role）中未发现");
            } catch (RoleNotFoundException e) {

            }
        }

        fields.clear();
        fields = null;
    }

    /**
     * 载入角色权限
     */
    private void loadRoleQuoraFromDb() {
        ArrayList list = new ArrayList();
        list.clear();

        pSQL.setSQL(m_RoleQuora_By_RoleId_SQL);
        pSQL.setInt(1, m_RoleId);
        cursor = DataSetBean.getCursor(pSQL.getSQL());

        fields = cursor.getNext();
        while (fields != null) {
            list.add(fields.get("operator_id".toUpperCase()));
            fields = cursor.getNext();
        }

        //角色权限
        m_RoleQuora = list;

        fields = null;
        cursor = null;
    }

    public void setRoleName(String m_RoleName) {
        this.m_RoleName = m_RoleName;
    }

    public void setRoleDesc(String m_RoleDesc) {
        this.m_RoleDesc = m_RoleDesc;
    }

    public void setRolePid(int m_Role_Pid) {
        this.m_Role_Pid = m_Role_Pid;
    }

    public void setAccOid(int m_Acc_Oid) {
        this.m_Acc_Oid = m_Acc_Oid;
    }

    public String getRoleName() {
        return m_RoleName;
    }

    public String getRoleDesc() {
        return m_RoleDesc;
    }

    public int getRolePid() {
        return m_Role_Pid;
    }

    public int getAccoid() {
        return m_Acc_Oid;
    }

    public Map getRoleInfo() {
        return m_RoleInfo;
    }

    public void setRoleInfo(Map m_RoleInfo) {
        this.m_RoleInfo = m_RoleInfo;
    }

    /**
     * @param args
     */
    public static void main(String[] args) {

    }

    public void setRoleId(int m_RoleId) {
        this.m_RoleId = m_RoleId;
    }

    public int getRoleId() {
        return m_RoleId;
    }

    public ArrayList getRoleQuora() {
        return m_RoleQuora;
    }

    public void setRoleQuora(ArrayList m_RoleQuora) {
        this.m_RoleQuora = m_RoleQuora;
    }

}
