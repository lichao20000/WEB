/*
 * 
 * 创建日期 2007-3-5
 * Administrator yys
 * TODO 要更改此生成的文件的模板，请转至
 * 窗口 － 首选项 － Java － 代码样式 － 代码模板
 */
package com.linkage.litms.tree;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

import com.linkage.litms.common.database.Cursor;

public class TreeMap {

	public TreeMap() {
		super();
		// TODO 自动生成构造函数存根
	}
	
    /**
     * 根据结果集及关联赋值关联字段名格式化树状结构关系
     *
     * 调用前需确认确认_c中的记录已按照_pid升序排序
     * 如未排序可通过调用Cursor.sort()方法进行排序
     *
     * @param	_c		需格式化的结果集
     * @param	_id		树状结构中的id编号字段名
     * @param	_pid	树状结构中的父id编号字段名
     * @param	_starid	树状结构中的起点id编号
     * @param	desc	树状结构中节点呈现字段名
     * @param	flag	是否严格按照起点id编号（_starid）开始树状结构
     * @return Map		更具树状层次分成存放的Map 每一层下都会包含一个新的Map
     */
    public static LinkedHashMap getTreeFormateMap(Cursor _c,String _id,String _pid,String _starid,boolean flag) {
    	
    	LinkedHashMap _treeMap = new LinkedHashMap();
    	
    	String tem_id = "";									//节点编号
    	String tem_pid = "";								//父节点编号
    	ArrayList tem_pidNotFineNods = new ArrayList();		//记录在遍历过程中没有找到父节点的节点
    	Map _h = _c.getNext();
    	int count = 1;
    	while(_h != null){
    		tem_id = (String)_h.get(_id);
    		tem_pid = (String)_h.get(_pid);
    		
    		
    		//放入“顶层”节点编号
    		if(_treeMap.isEmpty()) {
    			//当必须以_starid开始树状结构时
    			//在_treeMap为空之前所有编号不为_starid的节点将丢弃
    			if(flag) {
    				if(tem_pid.equals(_starid)) {
    					_treeMap.put(tem_id, new LinkedHashMap());
    				}    					
    				else {
    					//不做任何处理 节点直接丢弃
    					//_treeMap继续为空
    				}    					
    			}
    			//当不必以_starid开始树状结构时
    			//在_treeMap为空时将直接把_c中的第一条记录放入 
    			else {
    				_treeMap.put(tem_id, new LinkedHashMap());
    			}
    			
    		}
    		//遍历追加下层节点
    		else {
    			boolean _f = interativeAdd(_treeMap,tem_id,tem_pid);

    			if(!_f){
        			//未成功加入则追加到tem_pidNotFineNods中 待重新追加
        			//未成功加入的节点既没有找到父节点的节点
        			//那么此节点的父节点只可能在_c中排在他的后面
        			//否则此节点在此树中将找不到父节点既非法节点 应丢弃	
    				tem_pidNotFineNods = getFormateArray(tem_pidNotFineNods,tem_id,tem_pid,0);
    			}else {
    				//添加成功
    			}
    		}
    		
    		count++;
    		_h = _c.getNext();
    	}
    	tem_id = "";
    	tem_pid = "";
    	
    	
//    	for (int i = 0; i < tem_pidNotFineNods.size(); i++) {
//		}
    	
    	//补加未找到父节点的节点
    	if(tem_pidNotFineNods.size()>0) {
    		String trycount = "";
    		//当所有节点都被移出后 循环结束
    		while(tem_pidNotFineNods.size()>0) {
        		//解析节点的id与pid
        		tem_id = ((String)tem_pidNotFineNods.get(0)).split(",")[0];
        		tem_pid = ((String)tem_pidNotFineNods.get(0)).split(",")[1];
        		trycount = ((String)tem_pidNotFineNods.get(0)).split(",")[2];
        		if(interativeAdd(_treeMap,tem_id,tem_pid)) {
        			//添加成功则在tem_pidNotFineNods中删除此节点
        			tem_pidNotFineNods.remove(0);
        		}else {
        			//如果已第二次尝试还不能成功加入 则不再加入tem_pidNotFineNods 直接移出列表
        			if(trycount.equals("1"))
        				tem_pidNotFineNods.remove(0);
        			else
        				//如果第一次尝试未添加成功则加入tem_pidNotFineNods中等待第二次追加        			
        				tem_pidNotFineNods = getFormateArray(tem_pidNotFineNods,tem_id,tem_pid,1);
        		}
				
			}
    	}
    	
    	
    	return _treeMap;
    }
    
    /**
     * 根据_pid遍历Map将_id加入
     * @param _m	需遍历的Map
     * @param _id	需加入的id
     * @param _pid	所要加入在谁的下面
     * @return		找到并加入则返回true 否则false
     */
    public static boolean interativeAdd(LinkedHashMap _m,String _id,String _pid) {
    	
    	boolean flag = false;
    	
    	String tem_k = "";
    	LinkedHashMap tem_v = new LinkedHashMap();
    	java.util.Iterator keyValuePairs = _m.entrySet().iterator();
    	for(int j =0;j<_m.size();j++){
    		Map.Entry entry = (Map.Entry) keyValuePairs.next();
    		
    		tem_k = (String)entry.getKey();
    		tem_v = (LinkedHashMap)entry.getValue();
    		
    		//找到要追加的父对象
    		if(tem_k.equals(_pid)) {
    			tem_v.put(_id, new LinkedHashMap());
    			flag = true;
    			break;
    		}
    		
    		//向此分支的下一层遍历
    		flag = interativeAdd(tem_v,_id,_pid);
    		//如果在下一层中找到则不在遍历其他分支 遍历结束
    		if(flag)
    			break;

    	}
    	
    	return flag;
    }
    
    /**
     * 根据列表中的父子关系组织列表
     * 
     * 加入规则：遍历列表在列表中找到要加入节点的子节点
     * 			加入后保证tem_pidNotFineNods中存在父子关系的节点之间相邻
     * 			并让“父”节点在前，“子”节点在后 保证补加时不会有节点遗漏
     * 			在列表中未找到子节点的节点追加在列表的最后
     * @param _a		要追加的列表
     * @param _id		追加节点的编号
     * @param _pid		追加节点的父编号
     * @param trycount	加入次数
     * @return	_a		组织后的列表
     */
    public static ArrayList getFormateArray(ArrayList _a,String _id,String _pid,int trycount) {		
		
    	if(_a.size()==0)
			_a.add(_id+","+_pid+","+trycount);
		else {
			boolean flag = true;
			for (int i = 0; i < _a.size(); i++) {
				//查找当前节点的子节点 有则追加在其前 列表中其他节点后移
				if(((String)_a.get(i)).split(",")[1].equals(_id)) {
					_a.add(i,_id+","+_pid+","+trycount);
					flag = false;
					break;
				}    						
			}
			
			//未找到其子节点则追加在列表最后
			if(flag)
				_a.add(_id+","+_pid+","+trycount);
		}
		
		return _a;
    }

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO 自动生成方法存根

	}

}
