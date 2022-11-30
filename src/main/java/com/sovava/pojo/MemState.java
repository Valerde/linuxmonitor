package com.sovava.pojo;

import lombok.Data;

import java.io.Serializable;

/**
 * 
* <p>Title:MemState </p>
* <p>Description: 查看内存使用情况</p>
* @author yxz
* @date Jul 24, 2017
 */
@Data
public class MemState implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -1412473355088780549L;


	/**
	 * 总计内存，M
	 */
    private String total;

    /**
	 *已使用多少，M
	 */
    private String used;
    
    /**
	 * 未使用，M
	 */
    private String free;
    
    /**
     * 已使用百分比%
     */
    private String usePer;

}
