package com.sovava.pojo;

import lombok.Data;

import java.io.Serializable;

/**
 * 查看IO情况
 */
@Data
public class DiskIoState implements Serializable{
	



	/**
	 * 每秒读次数
	 */
    private String rs;

    /**
	 * 每秒写次数
	 */
    private String ws;
    
    /**
	 *读数据量 kb/s
	 */
    private String rkBS;
    
    /**
   	 * 写数据量（kb/s
   	 */
    private String wkBS;
    
    /**
   	 * IO操作的平均等待时间，单位是毫秒。这是应用程序在和磁盘交互时，需要消耗的时间，包括IO等待和实际操作的耗时。如果这个数值过大，可能是硬件设备遇到了瓶颈或者出现故障
   	 */
    private String await;
    
    /**
     * 向设备发出的请求平均数量。如果这个数值大于1，可能是硬件设备已经饱和（部分前端硬件设备支持并行写入）
     */
    private String avgquSz;
    
    /**
     * 设备利用率。这个数值表示设备的繁忙程度，经验值是如果超过60，可能会影响IO性能（可以参照IO操作平均等待时间）。如果到达100%，说明硬件设备已经饱和
     */
    private String util;

   
}
