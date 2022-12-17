package com.sovava.pojo;

import lombok.Data;

/**
 * 系统相关信息
 */
@Data
public class SystemInfo {

    /**
     * CPU型号信息
     */
    private String cpuModel;

    /**
     * 物理CPU个数
     */
    private String cpuNum;
    /**
     * 每个CPU核数量
     */
    private String cpuPerCore;

    /**
     * 系统运行天数
     */
    private String systemDays;
    /**
     * 系统版本信息
     */
    private String systemRelease;
    /**
     * 系统版本详细信息
     */
    private String systemVersion;
    /**
     * 系统密码文件修改时间
     */
    private String passFileInfo;
}
