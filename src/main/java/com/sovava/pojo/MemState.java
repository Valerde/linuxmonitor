package com.sovava.pojo;

import lombok.Data;

import java.io.Serializable;

/**
 * 查看内存情况
 */
@Data
public class MemState implements Serializable {
    /**
     *
     */
    private static final long serialVersionUID = -1412473355088780549L;


    /**
     * 总计内存，M
     */
    private String total;

    /**
     * 已使用多少，M
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
