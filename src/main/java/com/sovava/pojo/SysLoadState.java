package com.sovava.pojo;

import lombok.Data;

import java.io.Serializable;

/**
 * 
* <p>Title:SysLoadState </p>
* <p>Description: uptime查看系统负载状态</p>
* @author yxz
* @date Jul 24, 2017
 */
@Data
public class SysLoadState implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -4863071148000213553L;


	/**
	 * 1分钟之前到现在的负载
	 */
    private String oneLoad;

    /**
	 *5分钟之前到现在的负载
	 */
    private String fiveLoad;
    
    /**
   	 *15分钟之前到现在的负载
   	 */
    private String fifteenLoad;
    
    /**
     * 登录用户数量
     */
    private String users;
}
