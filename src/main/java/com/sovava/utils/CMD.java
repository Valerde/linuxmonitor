package com.sovava.utils;


/**
 * <p>Title:LinuxCmd </p>
 * <p>Description: LINUX命令</p>
 */
public class CMD {

    public static final String VIEW_MEM = "free -m";//查看内存状态

    public static final String SYSTEM_RELEASE = "cat /etc/issue";//查看系统版本

    public static final String UNAME_A = "uname -a";//查看系统详细信息

    public static final String DF_HL = "df -hlP";//查看磁盘空间

    //物理cpu个数
    public static final String WULI_CPU_NUM = "cat /proc/cpuinfo| grep \"physical id\"| sort| uniq| wc -l";

    //每个cpu的核数
    public static final String WULI_CPU_CORE_NUM = "cat /proc/cpuinfo| grep \"cpu cores\"| uniq";

    //cpu型号信息
    public static final String CPU_XINGHAO = "cat /proc/cpuinfo | grep name | cut -f2 -d: | uniq -c";

    //cpu使用情况
    public static final String VMSTAT = "mpstat -P ALL 1 3";

    //查看服务器网络吞吐率
    public static final String SAR_DEV_1 = "sar -n DEV 1 3";

    //查看磁盘IO使用情况
    public static final String DISK_IO = "iostat -xkz 1 1";

    //tcp状态
    public static final String SAR_TCP_1 = "sar -n TCP 1 3";

    //查看系统负载状态
    public static final String UPTIME = "uptime";

    //查看passwd文件修改时间
    public static final String passwd_update_time = "ls -l /etc/passwd";


}
