/*
 * Created on 2004-3-22
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package com.linkage.litms.common.util;

import java.util.Map;

import com.linkage.litms.common.database.Cursor;

/**
 * @author yuht
 * 
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 * add by hemc 2006-10-19
 * 根据Cursor,创建下拉框
 * @param cursor 数据源
 * @param name 用于生成下拉框时的value值
 * @param cast 用于生成下拉框时的展示值(呈现给用户看的)
 * @param hasChild 下拉框的值改变时,是否需要发生js事件
 * @param pos 初始化下拉框的初始值
 * @param aliasName 下拉框select的名字name(如果为空字符串,则使用name作为下拉框的名字)
 * @param hasDefault 
 * @return 生成下拉框的html代码后返回.
 */
public class FormUtil {
    public static String createListBox(Cursor cursor, String name, String cast, boolean hasChild, String pos, String aliasName) {
        return createListBox(cursor, name, cast, hasChild, pos, aliasName, true);
    }
    /**
     * 根据Cursor,创建下拉框
     * add by hemc 2006-10-19
     * @param cursor 数据源
     * @param name 用于生成下拉框时的value值
     * @param cast 用于生成下拉框时的展示值(呈现给用户看的)
     * @param hasChild 下拉框的值改变时,是否需要发生js事件
     * @param pos 初始化下拉框的初始值
     * @param aliasName 下拉框select的名字name(如果为空字符串,则使用name作为下拉框的名字)
     * @param hasDefault 
     * @return 生成下拉框的html代码后返回.
     */
    public static String createListBox(Cursor cursor, String name, String cast, boolean hasChild, String pos, String aliasName, boolean hasDefault) {
        String htmlStr;
        if (hasChild) {
            if (!aliasName.equals(""))
                htmlStr = "<SELECT NAME=" + aliasName + " CLASS=bk onchange=showChild('" + aliasName + "',event) style= 'width:225px'>";
            else
                htmlStr = "<SELECT NAME=" + name + " CLASS=bk onchange=showChild('" + name + "',event) style= 'width:225px'>";
        } else {
            if (!aliasName.equals(""))
                htmlStr = "<SELECT NAME=" + aliasName + " CLASS=bk style= 'width:225px'>";
            else
                htmlStr = "<SELECT NAME=" + name + " CLASS=bk style= 'width:225px'>";
        }
        Map fields = cursor.getNext();

        if (fields == null) {
            if (hasDefault)
                htmlStr += "<OPTION VALUE=-1>==此项没有记录==</OPTION>";
        } else {
            String tmp;
            htmlStr += "<OPTION VALUE=-1>==请选择==</OPTION>";
            while (fields != null) {
                tmp = (String) fields.get(name);
                if (tmp != null)
                    tmp = tmp.trim();
                htmlStr += "<OPTION VALUE='" + tmp;
                if (pos.trim().equals(tmp))
                    htmlStr += "' selected>--";
                else
                    htmlStr += "'>--";

                htmlStr += (String) fields.get(cast) + "--</OPTION>";
                fields = cursor.getNext();
            }
        }

        htmlStr += "</SELECT>";
        return htmlStr;
    }
    
    /**
     * 
     * @param cursor
     * @param name
     * @param cast
     * @param hasChild
     * @param pos
     * @param aliasName
     * @param hasDefault
     * @param disabled
     * @return
     */
    public static String createListBox(Cursor cursor, String name, String cast, boolean hasChild, String pos, String aliasName, boolean hasDefault, boolean disabled) {
    	String disabledStr = "";
        String htmlStr;
        if (disabled) {
        	disabledStr = "disabled";
        }
        if (hasChild) {
            if (!aliasName.equals(""))
                htmlStr = "<SELECT NAME=" + aliasName + " CLASS=bk onchange=showChild('" + aliasName + "' ) "+disabledStr+">";
            else
                htmlStr = "<SELECT NAME=" + name + " CLASS=bk onchange=showChild('" + name + "' ) "+disabledStr+">";
        } else {
            if (!aliasName.equals(""))
                htmlStr = "<SELECT NAME=" + aliasName + " CLASS=bk "+disabledStr+">";
            else
                htmlStr = "<SELECT NAME=" + name + " CLASS=bk "+disabledStr+">";
        }
        Map fields = cursor.getNext();

        if (fields == null) {
            if (hasDefault)
                htmlStr += "<OPTION VALUE=-1>==此项没有记录==</OPTION>";
        } else {
            String tmp;
            htmlStr += "<OPTION VALUE=-1>==请选择==</OPTION>";
            while (fields != null) {
                tmp = (String) fields.get(name);
                if (tmp != null)
                    tmp = tmp.trim();
                htmlStr += "<OPTION VALUE='" + tmp;
                if (pos.trim().equals(tmp))
                    htmlStr += "' selected>--";
                else
                    htmlStr += "'>--";

                htmlStr += (String) fields.get(cast) + "--</OPTION>";
                fields = cursor.getNext();
            }
        }

        htmlStr += "</SELECT>";
        return htmlStr;
    }
    
    
/*
    public static String createListBox1(Cursor cursor, String name, String cast, boolean hasChild, String pos, String aliasName) {
        return createListBox1(cursor, name, cast, hasChild, pos, aliasName, true);
    }
*/
    public static String createListBox1(Cursor cursor, String name, String cast, boolean hasChild, String pos, String aliasName, boolean hasDefault) {
        String htmlStr;
        if (hasChild) {
            if (!aliasName.equals(""))
                htmlStr = "<SELECT NAME=" + aliasName + " CLASS=bk onchange=showChild('" + aliasName + "')>";
            else
                htmlStr = "<SELECT NAME=" + name + " CLASS=bk onchange=showChild('" + name + "')>";
        } else {
            if (!aliasName.equals(""))
                htmlStr = "<SELECT NAME=" + aliasName + " CLASS=bk>";
            else
                htmlStr = "<SELECT NAME=" + name + " CLASS=bk>";
        }
        Map fields = cursor.getNext();

        if (fields == null) {
            if (hasDefault)
                htmlStr += "<OPTION VALUE=-1>==此项没有记录==</OPTION>";
        } else {
            htmlStr += "<OPTION VALUE=-1>==请选择==</OPTION>";
            while (fields != null) {
                htmlStr += "<OPTION VALUE='" + fields.get(pos);
                if (pos.equals((String) fields.get(pos)))
                    htmlStr += "' selected>--";
                else
                    htmlStr += "'>--";

                htmlStr += (String) fields.get(cast) + "/" + fields.get(name) + "--</OPTION>";
                fields = cursor.getNext();
            }
        }

        htmlStr += "</SELECT>";
        return htmlStr;
    }

    /**
     * 
     * 
     */
    public static String createListBox2(Cursor cursor, String name, String[] cast, boolean hasChild, String pos, String aliasName) {
        return createListBox2(cursor, name, cast, hasChild, pos, aliasName, true);
    }

    public static String createListBox2(Cursor cursor, String name, String[] cast, boolean hasChild, String pos, String aliasName, boolean hasDefault) {
        String htmlStr;
        // length
        int arrayLength = cast.length;

        if (hasChild) {
            if (!aliasName.equals(""))
                htmlStr = "<SELECT NAME=" + aliasName + " CLASS=bk onchange=showChild('" + aliasName + "')>";
            else
                htmlStr = "<SELECT NAME=" + name + " CLASS=bk onchange=showChild('" + name + "')>";
        } else {
            if (!aliasName.equals(""))
                htmlStr = "<SELECT NAME=" + aliasName + " CLASS=bk>";
            else
                htmlStr = "<SELECT NAME=" + name + " CLASS=bk>";
        }
        Map fields = cursor.getNext();

        if (fields == null) {
            if (hasDefault)
                htmlStr += "<OPTION VALUE=-1>==此项没有记录==</OPTION>";
        } else {
            htmlStr += "<OPTION VALUE=-1>==请选择==</OPTION>";
            while (fields != null) {
                htmlStr += "<OPTION VALUE='" + fields.get(name);
                if (pos.equals((String) fields.get(name)))
                    htmlStr += "' selected>--";
                else
                    htmlStr += "'>--";

                htmlStr += (String) fields.get(cast[0]);
                for (int c = 1; c < arrayLength; c++) {
                    htmlStr += "/" + (String) fields.get(cast[c]);
                }

                htmlStr += "--</OPTION>";

                fields = cursor.getNext();
            }
        }

        htmlStr += "</SELECT>";
        return htmlStr;
    }

    public static String createListBoxForm(Cursor cursor, String name, String cast, boolean hasChild, String pos, String aliasName, String insert) {
        String htmlStr;
        if (hasChild) {
            if (!aliasName.equals(""))
                htmlStr = "<SELECT NAME=" + aliasName + " CLASS=bk onchange=showChild('" + aliasName + "')>";
            else
                htmlStr = "<SELECT NAME=" + name + " CLASS=bk onchange=showChild('" + name + "')>";
        } else if (!aliasName.equals(""))
            htmlStr = "<SELECT NAME=" + aliasName + " CLASS=bk>";
        else
            htmlStr = "<SELECT NAME=" + name + " CLASS=bk>";
        Map fields = cursor.getNext();
        if (fields == null) {
            htmlStr = htmlStr + "<OPTION VALUE=-1>====此项没有记录====</OPTION>";
        } else {
            htmlStr = htmlStr + "<OPTION VALUE=-1>====请选择====</OPTION>";
            for (; fields != null; fields = cursor.getNext()) {
                htmlStr = htmlStr + "<OPTION VALUE=\"" + fields.get(name) + insert;
                if (pos.equals((String) fields.get(name)))
                    htmlStr = htmlStr + "\" selected>--";
                else
                    htmlStr = htmlStr + "\">--";
                htmlStr = htmlStr + (String) fields.get(cast) + "--</OPTION>";
            }
        }
        htmlStr = htmlStr + "</SELECT>";
        return htmlStr;
    }

    public static String createListBox(Cursor cursor, String name, String cast, boolean hasChild, String pos, String aliasName, String firstName) {
        return createListBox(cursor, name, cast, hasChild, pos, aliasName, true, firstName);
    }

    public static String createListBox(Cursor cursor, String name, String cast, boolean hasChild, String pos, String aliasName, boolean hasDefault,
            String firstName) {
        String htmlStr;
        if (hasChild) {
            if (!aliasName.equals(""))
                htmlStr = "<SELECT NAME=" + aliasName + " CLASS=bk onchange=showChild('" + aliasName + "')>";
            else
                htmlStr = "<SELECT NAME=" + name + " CLASS=bk onchange=showChild('" + name + "')>";
        } else {
            if (!aliasName.equals(""))
                htmlStr = "<SELECT NAME=" + aliasName + " CLASS=bk>";
            else
                htmlStr = "<SELECT NAME=" + name + " CLASS=bk>";
        }
        Map fields = cursor.getNext();

        if (fields == null) {
            if (hasDefault)
                htmlStr += "<OPTION VALUE=-1>==此项没有记录==</OPTION>";
        } else {
            if (firstName != null)
                htmlStr += "<OPTION VALUE=-1>==" + firstName + "==</OPTION>";
            else
                htmlStr += "<OPTION VALUE=-1>==请选择==</OPTION>";
            while (fields != null) {
                htmlStr += "<OPTION VALUE='" + fields.get(name);
                if (pos.equals((String) fields.get(name)))
                    htmlStr += "' selected>--";
                else
                    htmlStr += "'>--";

                htmlStr += (String) fields.get(cast) + "--</OPTION>";
                fields = cursor.getNext();
            }
        }

        htmlStr += "</SELECT>";

        return htmlStr;
    }
    
    /**
     * 根据数据库中的搜索数据拼接下拉框的option
     * @param cursor  搜索结果
     * @param key     数据库中key对应的值作为下拉框的value值
     * @param value   数据库中value对应的值作为下拉框的展示值
     * @return   下拉框的option的字符串
     */
    public static String creatSelectOption(Cursor cursor,String key ,String value)
    {
    	StringBuffer str = new StringBuffer(250);
    	Map fields = cursor.getNext();
    	if( null == fields)
    	{
    		str.append("<OPTION VALUE=-1>====此项没有记录====</OPTION>");
    	}
    	else 
    	{
    		str.append("<OPTION VALUE=-1 selected>==请选择==</OPTION>");
    		while(null!=fields)
        	{ 
    			str.append("<OPTION VALUE='").append(fields.get(key)).append("'>--");
        		str.append(fields.get(value)).append("--</OPTION>");
        		fields = cursor.getNext();
        	}
    	}    	
    	return str.toString();    	
    }
    /**
     *  生成下拉框，显示内容为"-"分割
     * @param cursor
     * @param name
     * @param cast
     * @param hasChild
     * @param pos
     * @param aliasName
     * @param hasDefault
     * @return
     */
    
    public static String createListBox_link(Cursor cursor, String name, String[] cast, boolean hasChild, String pos, String aliasName, boolean hasDefault) {
        String htmlStr;
        // length
        int arrayLength = cast.length;

        if (hasChild) {
            if (!aliasName.equals(""))
                htmlStr = "<SELECT NAME=" + aliasName + " CLASS=bk onchange=showChild('" + aliasName + "')>";
            else
                htmlStr = "<SELECT NAME=" + name + " CLASS=bk onchange=showChild('" + name + "')>";
        } else {
            if (!aliasName.equals(""))
                htmlStr = "<SELECT NAME=" + aliasName + " CLASS=bk>";
            else
                htmlStr = "<SELECT NAME=" + name + " CLASS=bk>";
        }
        Map fields = cursor.getNext();

        if (fields == null) {
            if (hasDefault)
                htmlStr += "<OPTION VALUE=-1>==此项没有记录==</OPTION>";
        } else {
            htmlStr += "<OPTION VALUE=-1>==请选择==</OPTION>";
            while (fields != null) {
                htmlStr += "<OPTION VALUE='" + fields.get(name);
                if (pos.equals((String) fields.get(name)))
                    htmlStr += "' selected>--";
                else
                    htmlStr += "'>--";

                htmlStr += (String) fields.get(cast[0]);
                for (int c = 1; c < arrayLength; c++) {
                    htmlStr += "-" + (String) fields.get(cast[c]);
                }

                htmlStr += "--</OPTION>";

                fields = cursor.getNext();
            }
        }

        htmlStr += "</SELECT>";
        return htmlStr;
    }
    
    /**
     * 根据Cursor,创建下拉框
     * add by hemc 2006-10-19
     * @param cursor 数据源
     * @param name 用于生成下拉框时的value值
     * @param cast 用于生成下拉框时的展示值(呈现给用户看的)
     * @param cast_relace 当cast里的值为空时显示该值
     * @param cast_add 显示值中额外附加的部分
     * @param hasChild 下拉框的值改变时,是否需要发生js事件
     * @param pos 初始化下拉框的初始值
     * @param aliasName 下拉框select的名字name(如果为空字符串,则使用name作为下拉框的名字)
     * @param hasDefault 
     * @return 生成下拉框的html代码后返回.
     */
    public static String createListBox_replace(Cursor cursor, String name, String cast, String cast_relace, String cast_add, boolean hasChild, String pos, String aliasName, boolean hasDefault) {
        String htmlStr;
        if (hasChild) {
            if (!aliasName.equals(""))
                htmlStr = "<SELECT NAME=" + aliasName + " CLASS=bk onchange=showChild('" + aliasName + "')>";
            else
                htmlStr = "<SELECT NAME=" + name + " CLASS=bk onchange=showChild('" + name + "')>";
        } else {
            if (!aliasName.equals(""))
                htmlStr = "<SELECT NAME=" + aliasName + " CLASS=bk>";
            else
                htmlStr = "<SELECT NAME=" + name + " CLASS=bk>";
        }
        Map fields = cursor.getNext();

        if (fields == null) {
            if (hasDefault)
                htmlStr += "<OPTION VALUE=-1>==此项没有记录==</OPTION>";
        } else {
            String tmp;
            htmlStr += "<OPTION VALUE=-1>==请选择==</OPTION>";
            while (fields != null) {
                tmp = (String) fields.get(name);
                if (tmp != null)
                    tmp = tmp.trim();
                htmlStr += "<OPTION VALUE='" + tmp;
                if (pos.trim().equals(tmp))
                    htmlStr += "' selected>--";
                else
                    htmlStr += "'>--";

                String castValue = (String) fields.get(cast);
                String cast_addValue = (String) fields.get(cast_add);
                
                if (castValue != null && !"".equals(castValue)){
                	htmlStr += castValue + "(" + cast_addValue + ")--</OPTION>";
                }
                else {
                	String cast_relaceValue =  (String) fields.get(cast_relace);
                	htmlStr += cast_relaceValue + "(" + cast_addValue + ")--</OPTION>";
                }
                
                fields = cursor.getNext();
            }
        }

        htmlStr += "</SELECT>";
        return htmlStr;
    }

}
