package com.sovava.utils;

import com.sovava.pojo.*;


import org.springframework.util.StringUtils;


/**
 * <p>Title:HostUtils </p>
 * <p>Description: 获取服务器信息的工具类</p>
 *
 * @author yxz
 * @date Jul 24, 2017
 */
public class HostUtils {

    /**
     * 获取系统版本信息
     *
     * @param conn
     * @return
     */
    public static String getSystemRelease() {
        return CtrCommond.doCommond(LinuxCmd.SYSTEM_RELEASE).replace(" \\n \\l</br>", "");
    }


    /**
     * 获取系统版本详细信息
     *
     * @return
     */
    public static String getSystemUname() {
        return CtrCommond.doCommond(LinuxCmd.UNAME_A);
    }

    /**
     * 获取物理CPU个数
     *
     * @return
     */
    public static String getCpuNum() {
        return CtrCommond.doCommond(LinuxCmd.WULI_CPU_NUM);
    }


    /**
     * 获取每个CPU核数量
     *
     * @return
     */
    public static String getCpuPerCores() {
        String str = CtrCommond.doCommond(LinuxCmd.WULI_CPU_CORE_NUM);
        if (!StringUtils.isEmpty(str)) {
            return str.substring(str.length() - 1);
        }
        return "";
    }


    /**
     * 获取系统已经运行天数
     *
     * @return
     */
    public static String getSystemDays() {
        return LinuxStrUtil.getYxDays(CtrCommond.doCommond(LinuxCmd.UPTIME));
    }


    /**
     * 获取磁盘使用信息
     *
     * @return
     */
    public static DeskState getDfInfo() {
        return LinuxStrUtil.setDfHl(CtrCommond.doCommond(LinuxCmd.DF_HL));
    }

    /**
     * 获取CPU型号信息
     *
     * @return
     */
    public static String getCpuModel() {
        String result = CtrCommond.doCommond(LinuxCmd.CPU_XINGHAO);
        if (!StringUtils.isEmpty(result)) {
            return result.trim();
        } else {
            return "";
        }
    }

    /**
     * 获取内存使用情况
     *
     * @return
     */
    public static MemState getMemState() {
        return LinuxStrUtil.setViewMem(CtrCommond.doCommond(LinuxCmd.VIEW_MEM));
    }


    /**
     * 获取cpu使用情况
     *
     * @return
     */
    public static CpuState getCpuState() {
        return LinuxStrUtil.setCpuState(CtrCommond.doCommond(LinuxCmd.VMSTAT));
    }


    /**
     * 获取磁盘IO使用情况
     *
     * @return
     */
    public static DiskIoState getDiskIoState() {
        return LinuxStrUtil.setDiskIo(CtrCommond.doCommond(LinuxCmd.DISK_IO));
    }


    /**
     * 获取网络吞吐率
     *
     * @return
     */
    public static NetIoState getNetIoState() {
        return LinuxStrUtil.setNetIo(CtrCommond.doCommond(LinuxCmd.SAR_DEV_1));
    }

    /**
     * 获取tcp状态
     *
     * @return
     */
    public static TcpState getTcpState() {
        return LinuxStrUtil.setTcp(CtrCommond.doCommond(LinuxCmd.SAR_TCP_1));
    }

    /**
     * 获取系统负载状态
     *
     * @return
     */
    public static SysLoadState getSysLoadState() {
        return LinuxStrUtil.setSysLoad(CtrCommond.doCommond(LinuxCmd.UPTIME));
    }


    /**
     * 获取进程使用状态
     *
     * @param pid 进程ID
     * @return
     */
    public static ProcessState getProcessState(String pid) {
        String processStr = CtrCommond.doCommond(LinuxCmd.dd.replace("{pid}", pid));
        processStr = conProcessStr(processStr, pid);
        String[] appStateStr = null;
        if (!StringUtils.isEmpty(processStr)) {
            appStateStr = processStr.split(StaticKeys.SPLIT_KG);
            if (appStateStr.length > 1) {
                ProcessState processState = new ProcessState();
                processState.setCpuPer(appStateStr[0]);
                processState.setMemPer(appStateStr[1]);
                return processState;
            }
        }
        return null;
    }

    /**
     * 检测系统已加载内核模块
     *
     * @return
     */
    public static String getLsmodInfo() {
        String lsmod = CtrCommond.doCommond(LinuxCmd.lsmod);
        return lsmod;
    }

    /**
     * 检测系统密码文件修改时间
     *
     * @return
     */
    public static String getPasswdFileInfo() {
        String passwdFile = CtrCommond.doCommond(LinuxCmd.passwd_update_time);
        passwdFile = passwdFile.substring(23, 23 + 18);
        return passwdFile;
    }

    /**
     * 检测系统rpc服务开放状态
     *
     * @return
     */
    public static String getRpcinfo() {
        String rpcinfo = CtrCommond.doCommond(LinuxCmd.rpcinfo);
        return rpcinfo;
    }


    /**
     * 检测系统任务计划
     *
     * @return
     */
    public static String getCrontab() {
        String crontab = CtrCommond.doCommond(LinuxCmd.crontab);
        return crontab;
    }


    /**
     * 对进行占用的CPU和mem字符串，进行预处理
     *
     * @param gg
     * @return
     */
    private static String conProcessStr(String processStr, String pid) {
        if (StringUtils.isEmpty(processStr)) {
            return "";
        }
        processStr = processStr.trim();
        String[] rows = processStr.split(StaticKeys.SPLIT_BR);
        String[] cols = null;
        String row = "";
        for (int i = 1; i < rows.length; i++) {
            row = FormatUtil.replaceKg(rows[i]);
            cols = row.split(StaticKeys.SPLIT_KG);
            if (pid.equals(cols[1])) {
                return cols[2] + " " + cols[3];
            }
        }
        return "";
    }


}
