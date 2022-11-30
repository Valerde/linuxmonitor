package com.sovava.pojo;

import lombok.Data;

import java.io.Serializable;

/**
 * 
* <p>Title:ProcessState </p>
* <p>Description: 进程状态</p>
* @author yxz
* @date Jul 24, 2017
 */
@Data
public class ProcessState implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -2913111613773445949L;

	/**
	 * %CPU
	 */
    private String cpuPer;

    /**
	 * %MEM
	 */
    private String memPer;

}
