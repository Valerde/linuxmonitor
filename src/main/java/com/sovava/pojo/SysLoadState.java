package com.sovava.pojo;

import lombok.Data;

import java.io.Serializable;

/**
 * Title:SysLoadState <br>
 * Description: uptime查看系统负载状态
 */
@Data
public class SysLoadState implements Serializable {




    /**
     * 1分钟之前到现在的负载
     */
    private String oneLoad;

    /**
     * 5分钟之前到现在的负载
     */
    private String fiveLoad;

    /**
     * 15分钟之前到现在的负载
     */
    private String fifteenLoad;

    /**
     * 登录用户数量
     */
    private String users;
}
