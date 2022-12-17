package com.sovava.pojo;

import lombok.Data;

import java.io.Serializable;

/**
 * 查看CPU情况
 */
@Data
public class CpuState implements Serializable{


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
