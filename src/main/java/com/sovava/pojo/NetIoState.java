package com.sovava.pojo;

import lombok.Data;

import java.io.Serializable;

/**
 * Title:NetIoState <br>
 * Description:网络设备的吞吐率
 */
@Data
public class NetIoState implements Serializable {



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
