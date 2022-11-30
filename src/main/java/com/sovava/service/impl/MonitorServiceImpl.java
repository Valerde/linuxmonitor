package com.sovava.service.impl;

import com.sovava.pojo.*;
import com.sovava.service.MonitorService;
import com.sovava.utils.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ThreadPoolExecutor;

@Service
public class MonitorServiceImpl implements MonitorService {

    @Autowired
    private ThreadPoolExecutor executor;

    @Override
    public CpuState getCpuState() {
        return LinuxStrUtil.setCpuState(CtrCommond.doCommond(LinuxCmd.VMSTAT));
    }

    @Override
    public DeskState getDeskState() {
        return LinuxStrUtil.setDfHl(CtrCommond.doCommond(LinuxCmd.DF_HL));
    }

    @Override
    public DiskIoState getDiskState() {
        return LinuxStrUtil.setDiskIo(CtrCommond.doCommond(LinuxCmd.DISK_IO));
    }

    @Override
    public MemState getMemState() {
        return LinuxStrUtil.setViewMem(CtrCommond.doCommond(LinuxCmd.VIEW_MEM));
    }

    @Override
    public NetIoState getNetIoState() {
        return LinuxStrUtil.setNetIo(CtrCommond.doCommond(LinuxCmd.SAR_DEV_1));
    }

    @Override
    public ProcessState getProcessState(String pid) {
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

    @Override
    public TcpState getTcpState() {
        return LinuxStrUtil.setTcp(CtrCommond.doCommond(LinuxCmd.SAR_TCP_1));
    }

    @Override
    public SysLoadState getSysLoadState() {
        return LinuxStrUtil.setSysLoad(CtrCommond.doCommond(LinuxCmd.UPTIME));
    }

    @Override
    public SystemInfo getSystemInfo() {

        SystemInfo systemInfo = new SystemInfo();
        systemInfo.setSystemDays(HostUtils.getSystemDays());
        systemInfo.setSystemRelease(HostUtils.getSystemRelease());
        systemInfo.setSystemVersion(HostUtils.getSystemUname());
        systemInfo.setPassFileInfo(HostUtils.getPasswdFileInfo());
        systemInfo.setCpuNum(HostUtils.getCpuNum());
        systemInfo.setCpuModel(HostUtils.getCpuModel());
        systemInfo.setCpuPerCore(HostUtils.getCpuPerCores());
        return systemInfo;
    }

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
            CompletableFuture.allOf(cpuFuture,deskFuture,diskFuture,memFuture,netFuture,sysLoadFuture,tcpFuture).get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }

        return allState;
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
}
