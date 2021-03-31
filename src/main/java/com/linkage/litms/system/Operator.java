/*
 * Created on 2004-3-22
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package com.linkage.litms.system;

/**
 * @author yuht
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class Operator {
	public int _oid;
	public int _poid;
	public int _layer;
	public String _name;
	public String _remark;
	
    /**
     * 结构构造
     * @param oid
     * @param poid
     * @param layer
     * @param name
     * @param remark
     */
	public Operator(int oid, int poid, int layer, String name, String remark){
		_oid 	= oid;
		_poid 	= poid;
		_layer  = layer;
		_name	= name;
		_remark = remark;
	}
}
