package com.sovava.pojo;

import lombok.Data;

import java.io.Serializable;

/**
 * <p>Title:TcpState </p>
 * <p>Description: 查看TCP连接状态</p>
 *
 * @author yxz
 * @date Jul 24, 2017
 */
@Data
public class TcpState implements Serializable {

    private static final long serialVersionUID = -299667815095138020L;


    /**
     * 每秒本地发起的TCP连接数，既通过connect调用创建的TCP连接；,active/s
     */
    private String active;

    /**
     * 每秒远程发起的TCP连接数，即通过accept调用创建的TCP连接,passive/s
     */
    private String passive;
    private String isegs;//接受的段 TODO 设置这个
    private String oseg;//输出的段
}
