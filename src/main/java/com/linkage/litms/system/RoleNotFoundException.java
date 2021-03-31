/*
 * 
 * 创建日期 2006-1-23
 * Administrator suzr
 * TODO 要更改此生成的文件的模板，请转至
 * 窗口 － 首选项 － Java － 代码样式 － 代码模板
 */
package com.linkage.litms.system;

public class RoleNotFoundException extends Exception {
    private static final long serialVersionUID = 1L;

    public RoleNotFoundException() {
        super();
        // TODO 自动生成构造函数存根
    }

    /**
     * 带错误信息参数的构造函数
     * 
     * @param msg
     *            错误信息
     */
    public RoleNotFoundException(String msg) {
        super(msg);
    }
}
