package com.sovava.service;

import com.sovava.pojo.*;

public interface MonitorService {
    CpuState getCpuState();

    DeskState getDeskState();

    DiskIoState getDiskState();

    MemState getMemState();

    NetIoState getNetIoState();

    ProcessState getProcessState(String pid);

    TcpState getTcpState();

    SysLoadState getSysLoadState();

    SystemInfo getSystemInfo();

    AllState getAll();

    String getSystemRelease();

    String getSystemUname();

    String getCpuNum();

    String getCpuPerCores();

    String getSystemDays();

    String getCpuModel();

    String getPasswdFileInfo();
}
