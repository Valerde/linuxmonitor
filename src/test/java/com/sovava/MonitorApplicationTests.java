package com.sovava;

import com.sovava.service.MonitorService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@Slf4j
class MonitorApplicationTests {

    @Test
    void contextLoads() {
    }

    @Autowired
    private MonitorService monitorService;

    @Test
    public void testMonitor() {
        log.debug("CPU型号信息：{}" , monitorService.getCpuModel());
        log.debug("物理CPU个数：{}" , monitorService.getCpuNum());
        log.debug("每个CPU核数量：{}" , monitorService.getCpuPerCores());
        log.debug("系统运行天数：{}" , monitorService.getSystemDays());
        log.debug("系统版本信息：{}" , monitorService.getSystemRelease());
        log.debug("系统版本详细信息：{}" , monitorService.getSystemUname());
        log.debug("cpu Idle使用率：{}" , monitorService.getCpuState());
        log.debug("磁盘已使用G：{}" , monitorService.getDiskState());
        log.debug("磁盘IO状态：{}" , monitorService.getDiskIoState());
        log.debug("内存已使用百分比：{}" , monitorService.getMemState());
        log.debug("网络吞吐率rxbyt：{}" , monitorService.getNetIoState());
        log.debug("系统一分钟负载：{}" , monitorService.getSysLoadState());
        log.debug("系统TCP active：{}" , monitorService.getTcpState());
        log.debug("系统密码文件修改时间：{}" , monitorService.getPasswdFileInfo());

    }

}
