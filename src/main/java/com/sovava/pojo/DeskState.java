package com.sovava.pojo;

import lombok.Data;

import java.io.Serializable;

/**
 * 查看磁盘情况
 */
@Data
public class DeskState implements Serializable {


    /**
     *
     */
    private static final long serialVersionUID = 879979812204191283L;

    /**
     * 分区大小
     */
    private String size;

    /**
     * 已使用
     */
    private String used;

    /**
     * 已使用百分比
     */
    private String usePer;

}
