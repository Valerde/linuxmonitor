package com.sovava.pojo;

import lombok.Data;

/**
 * 所有系統资源情況
 */
@Data
public class AllState {
    private CpuState cpuState;
    private DiskState diskState;
    private DiskIoState diskIoState;
    private MemState memState;
    private NetIoState netIoState;
    private SysLoadState sysLoadState;
    private TcpState tcpState;
}
