package com.sovava.controller;

import com.sovava.pojo.*;
import com.sovava.service.MonitorService;
import com.sovava.utils.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/monitor")
@Slf4j
public class MonitorController {
    @Autowired
    private MonitorService monitorService;

    @GetMapping("/cpu")
    public R getCpu() {
        CpuState cpuState = monitorService.getCpuState();
        log.debug("CPU的信息为：{}", cpuState.toString());
        return R.ok().setData(cpuState);
    }

    @GetMapping("/desk")
    public R getDesk() {
        DiskState diskState = monitorService.getDiskState();
        return R.ok().setData(diskState);
    }

    @GetMapping("/diskIo")
    public R getDiskIo() {
        DiskIoState diskIoState = monitorService.getDiskIoState();
        return R.ok().setData(diskIoState);
    }

    @GetMapping("/mem")
    public R getMem() {
        log.debug("进入mem");
        MemState memState = monitorService.getMemState();
        log.debug("mem的信息为：{}", memState);
        return R.ok().setData(memState);
    }

    @GetMapping("/net")
    public R getNet() {
        NetIoState netIoState = monitorService.getNetIoState();
        return R.ok().setData(netIoState);
    }

    @GetMapping("/sys")
    public R getSysLoad() {
        SysLoadState sysLoadState = monitorService.getSysLoadState();
        return R.ok().setData(sysLoadState);
    }

    @GetMapping("/tcp")
    public R getTcp() {
        TcpState tcpState = monitorService.getTcpState();
        return R.ok().setData(tcpState);
    }

    @GetMapping("/all")
    public R getAll() {


        AllState allState = monitorService.getAll();
        log.debug("所有的资源信息为：{}", allState.toString());
        return R.ok().setData(allState);
    }

    @GetMapping("/sysinfo")
    public R getSystemInfo() {
        SystemInfo systemInfo = monitorService.getSystemInfo();
        log.debug("所有的系统信息为：{}", systemInfo.toString());
        return R.ok().put("sys", systemInfo);

    }
}
