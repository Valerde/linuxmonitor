package com.sovava.service;

import com.sovava.pojo.*;

public interface MonitorService {
    CpuState getCpuState();

    DiskState getDiskState();

    DiskIoState getDiskIoState();

    MemState getMemState();

    NetIoState getNetIoState();


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
