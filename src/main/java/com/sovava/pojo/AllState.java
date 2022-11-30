package com.sovava.pojo;

import lombok.Data;

@Data
public class AllState {
    private CpuState cpuState;
    private DeskState deskState;
    private DiskIoState diskIoState;
    private MemState memState;
    private NetIoState netIoState;
    private SysLoadState sysLoadState;
    private TcpState tcpState;
}
