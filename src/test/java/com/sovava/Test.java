package com.sovava;


import com.sovava.utils.HostUtils;


public class Test {

    /**
     * @param args
     */
    public static void main(String[] args) {

//        System.out.println("CPU型号信息：" + HostUtils.getCpuModel());
//        System.out.println("物理CPU个数：" + HostUtils.getCpuNum());
//        System.out.println("每个CPU核数量：" + HostUtils.getCpuPerCores());
        System.out.println("系统运行天数：" + HostUtils.getSystemDays());
        System.out.println("系统版本信息：" + HostUtils.getSystemRelease());
//        System.out.println("系统版本详细信息：" + HostUtils.getSystemUname());
//        System.out.println("cpu Idle使用率：" + HostUtils.getCpuState().getIdle());
        System.out.println("磁盘已使用G：" + HostUtils.getDfInfo().getUsed());
//        System.out.println("磁盘IO状态：" + HostUtils.getDiskIoState().getRkBS());
        System.out.println("内存已使用百分比：" + HostUtils.getMemState().getUsePer());
//        System.out.println("网络吞吐率rxbyt：" + HostUtils.getNetIoState().getRxbyt());
//        System.out.println("系统一分钟负载：" + HostUtils.getSysLoadState().getOneLoad());
//        System.out.println("系统TCP active：" + HostUtils.getTcpState().getActive());
////        System.out.println("进程2696内存使用率："+HostUtils.getProcessState("1217").getMemPer());
////        System.out.println("系统已加载内核模块：" + HostUtils.getLsmodInfo());
        System.out.println("系统密码文件修改时间：" + HostUtils.getPasswdFileInfo());
//        System.out.println("系统rpc服务开放状态：" + HostUtils.getRpcinfo());
//        System.out.println("当前系统任务计划：" + HostUtils.getCrontab());
    }

}
