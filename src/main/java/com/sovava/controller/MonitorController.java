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
        DeskState deskState = monitorService.getDeskState();
        return R.ok().setData(deskState);
    }

    @GetMapping("/disk")
    public R getDisk() {
        DiskIoState diskState = monitorService.getDiskState();
        return R.ok().setData(diskState);
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


        AllState allState =monitorService.getAll();
        return R.ok().setData(allState);
    }

    @GetMapping("/sysinfo")
    public R getSystemInfo() {
        SystemInfo systemInfo = monitorService.getSystemInfo();
        return R.ok().put("sys", systemInfo);

    }
}
