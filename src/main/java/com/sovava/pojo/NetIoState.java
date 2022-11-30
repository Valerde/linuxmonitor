package com.sovava.pojo;

import lombok.Data;

import java.io.Serializable;

/**
 * 
* <p>Title:NetIoState </p>
* <p>Description:网络设备的吞吐率 </p>
* @author yxz
* @date Jul 24, 2017
 */

@Data
public class NetIoState implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -8314012397341825158L;

	
	/**
	 * 每秒钟接收的数据包,rxpck/s
	 */
    private String rxpck;

    /**
	 * 每秒钟发送的数据包,txpck/s
	 */
    private String txpck;
    
    
    /**
	 * 每秒钟接收的KB数,rxkB/s
	 */
    private String rxbyt;

    /**
	 * 每秒钟发送的KB数,txkB/s
	 */
    private String txbyt;
    
    /**
	 * 每秒钟接收的压缩数据包,rxcmp/s
	 */
    private String rxcmp;
    
    
    /**
	 * 每秒钟发送的压缩数据包,txcmp/s
	 */
    private String txcmp;
}
