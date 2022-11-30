package com.sovava.pojo;

import lombok.Data;

import java.io.Serializable;

/**
 * 
* <p>Title:CpuState </p>
* <p>Description: 查看CPU使用情况</p>
* @author yxz
* @date Jul 24, 2017
 */
@Data
public class CpuState implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -2913111613773445949L;

	/**
	 * 用户态的CPU时间（%）
	 */
    private String user;

    /**
	 * 系统（内核）时间（%）
	 */
    private String sys;
    
    /**
	 * 空闲时间（idle）（%）
	 */
    private String idle;
    
    /**
   	 * IO等待时间（wait）（%）
   	 */
    private String iowait;
    
    /**
   	 * 硬中断时间（%）
   	 */
    private String irq;
    
    /**
     * 软中断时间（%）
     */
    private String soft;

}
