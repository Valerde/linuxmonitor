package com.sovava.pojo;

import lombok.Data;

import java.io.Serializable;

/**
 * 查看内存情况
 */
@Data
public class MemState implements Serializable {

    /**
     * 总计内存，MB
     */
    private String total;

    /**
     * 已使用多少，MB
     */
    private String used;

    /**
     * 未使用，MB
     */
    private String free;

    /**
     * 已使用百分比%
     */
    private String usePer;

}
