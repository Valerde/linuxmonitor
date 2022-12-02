package com.sovava.service.impl;

import com.sovava.pojo.*;
import com.sovava.service.MonitorService;
import com.sovava.utils.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ThreadPoolExecutor;

@Service
public class MonitorServiceImpl implements MonitorService {

    @Autowired
    private ThreadPoolExecutor executor;

    /**
     * 获取系统信息
     *
     * @return
     */
    @Override
    public SystemInfo getSystemInfo() {

        SystemInfo systemInfo = new SystemInfo();
        systemInfo.setSystemDays(getSystemDays());
        systemInfo.setSystemRelease(getSystemRelease());
        systemInfo.setSystemVersion(getSystemUname());
        systemInfo.setPassFileInfo(getPasswdFileInfo());
        systemInfo.setCpuNum(getCpuNum());
        systemInfo.setCpuModel(getCpuModel());
        systemInfo.setCpuPerCore(getCpuPerCores());
        return systemInfo;
    }

    /**
     * 获取系统资源信息
     *
     * @return
     */
    @Override
    public AllState getAll() {
        AllState allState = new AllState();
        CompletableFuture<Void> cpuFuture = CompletableFuture.runAsync(() -> {
            allState.setCpuState(getCpuState());
        }, executor);
        CompletableFuture<Void> deskFuture = CompletableFuture.runAsync(() -> {
            allState.setDeskState(getDeskState());
        }, executor);
        CompletableFuture<Void> diskFuture = CompletableFuture.runAsync(() -> {
            allState.setDiskIoState(getDiskState());
        }, executor);
        CompletableFuture<Void> memFuture = CompletableFuture.runAsync(() -> {
            allState.setMemState(getMemState());
        }, executor);
        CompletableFuture<Void> netFuture = CompletableFuture.runAsync(() -> {
            allState.setNetIoState(getNetIoState());
        }, executor);
        CompletableFuture<Void> sysLoadFuture = CompletableFuture.runAsync(() -> {
            allState.setSysLoadState(getSysLoadState());
        }, executor);
        CompletableFuture<Void> tcpFuture = CompletableFuture.runAsync(() -> {
            allState.setTcpState(getTcpState());
        }, executor);
        try {
            CompletableFuture.allOf(cpuFuture, deskFuture, diskFuture, memFuture, netFuture, sysLoadFuture, tcpFuture).get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }

        return allState;
    }


    @Override
    public CpuState getCpuState() {
        return LinuxStrUtil.setCpuState(doCommond(LinuxCmd.VMSTAT));
    }

    @Override
    public DeskState getDeskState() {
        return LinuxStrUtil.setDfHl(doCommond(LinuxCmd.DF_HL));
    }

    @Override
    public DiskIoState getDiskState() {
        return LinuxStrUtil.setDiskIo(doCommond(LinuxCmd.DISK_IO));
    }

    @Override
    public MemState getMemState() {
        return LinuxStrUtil.setViewMem(doCommond(LinuxCmd.VIEW_MEM));
    }

    @Override
    public NetIoState getNetIoState() {
        return LinuxStrUtil.setNetIo(doCommond(LinuxCmd.SAR_DEV_1));
    }

    @Override
    public ProcessState getProcessState(String pid) {
        String processStr = doCommond(LinuxCmd.dd.replace("{pid}", pid));
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

    @Override
    public TcpState getTcpState() {
        return LinuxStrUtil.setTcp(doCommond(LinuxCmd.SAR_TCP_1));
    }

    @Override
    public SysLoadState getSysLoadState() {
        return LinuxStrUtil.setSysLoad(doCommond(LinuxCmd.UPTIME));
    }


    /**
     * 获取系统版本信息
     *
     * @return
     */
    @Override
    public String getSystemRelease() {
        return doCommond(LinuxCmd.SYSTEM_RELEASE).replace(" \\n \\l</br>", "");
    }


    /**
     * 获取系统版本详细信息
     *
     * @return
     */
    @Override
    public String getSystemUname() {
        return doCommond(LinuxCmd.UNAME_A);
    }

    /**
     * 获取物理CPU个数
     *
     * @return
     */
    @Override
    public String getCpuNum() {
        return doCommond(LinuxCmd.WULI_CPU_NUM);
    }


    /**
     * 获取每个CPU核数量
     *
     * @return
     */
    @Override
    public String getCpuPerCores() {
        String str = doCommond(LinuxCmd.WULI_CPU_CORE_NUM);
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
    @Override
    public String getSystemDays() {
        return LinuxStrUtil.getYxDays(doCommond(LinuxCmd.UPTIME));
    }

    /**
     * 获取CPU型号信息
     *
     * @return
     */
    @Override
    public String getCpuModel() {
        String result = doCommond(LinuxCmd.CPU_XINGHAO);
        if (!StringUtils.isEmpty(result)) {
            return result.trim();
        } else {
            return "";
        }
    }

    /**
     * 检测系统密码文件修改时间
     *
     * @return
     */
    @Override
    public String getPasswdFileInfo() {
        String passwdFile = doCommond(LinuxCmd.passwd_update_time);
        passwdFile = passwdFile.substring(23, 23 + 18);
        return passwdFile;
    }

    private String conProcessStr(String processStr, String pid) {
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

    /**
     * 命令执行器
     *
     * @param cmd
     * @return
     */
    private String doCommond(String cmd) {
        String result = "";
        //执行命令并获取输出流
        try {

            String[] cmds = {"/bin/sh", "-c", cmd};
            Process pro = Runtime.getRuntime().exec(cmds);
            pro.waitFor();
            InputStream in = pro.getInputStream();
            BufferedReader read = new BufferedReader(new InputStreamReader(in));
            String line = null;
            while ((line = read.readLine()) != null) {
                result += line + StaticKeys.SPLIT_BR;
            }

        } catch (IOException e) {
            System.out.println("执行linux命令错误：" + e.toString());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        //输出流整形
        if (result.endsWith(StaticKeys.SPLIT_BR)) {
            result = result.substring(0, result.length() - StaticKeys.SPLIT_BR.length());
        }

        if (!StringUtils.isEmpty(result)) {
            if (cmd.contains("DEV") || cmd.contains("iostat")) {
                if (result.contains("</br></br>")) {
//                    log.debug("iostat的结果是：{}",result);
//                    log.debug("result.lastIndexOf(\"</br></br>\")=={}",result.lastIndexOf("</br></br>"));
                    result = result.substring(result.indexOf("</br></br>") + 10);
                }
            }
            if (cmd.contains("mpstat")) {
                if (result.contains("</br></br>")) {
                    result = result.substring(result.lastIndexOf("</br></br>") + 10);
                    int s = result.indexOf("</br>") + 5;
                    s = result.indexOf("</br>", s);
                    result = result.substring(0, s);
                }
            }
        }
        return result;
    }
}
