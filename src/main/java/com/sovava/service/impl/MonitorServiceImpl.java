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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.lang.Double.parseDouble;

@Service
public class MonitorServiceImpl implements MonitorService {

    private static final String COMMAND_NOT = "command not found";


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
            allState.setDiskState(getDiskState());
        }, executor);
        CompletableFuture<Void> diskFuture = CompletableFuture.runAsync(() -> {
            allState.setDiskIoState(getDiskIoState());
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

        String cpuResult = doCommond(CMD.VMSTAT);

        if (StringUtils.isEmpty(cpuResult) || cpuResult.contains(COMMAND_NOT)) {
            return null;
        }

        cpuResult = cpuResult.trim();
        CpuState cpuState = new CpuState();
        String[] rows = cpuResult.split(StaticKeys.SPLIT_BR);
        String row = "";
        String[] cols = null;
        for (int i = 1; i < rows.length; i++) {
            row = FormatUtil.replaceKg(rows[i]);
            cols = row.split(StaticKeys.SPLIT_SPACE);
            if (cols[0].contains(":")) {
                row = FormatUtil.replaceKg(rows[i]);
                cols = row.split(StaticKeys.SPLIT_SPACE);
                cpuState.setUser(cols[2]);
                cpuState.setSys(cols[4]);
                cpuState.setIowait(cols[5]);
                cpuState.setIrq(cols[6]);
                cpuState.setSoft(cols[7]);
                cpuState.setIdle(cols[11]);
                break;
            }
        }

        return cpuState;
    }

    @Override
    public DiskState getDiskState() {
        String diskResult = doCommond(CMD.DF_HL);
        if (StringUtils.isEmpty(diskResult)) {
            return null;
        }
        diskResult = diskResult.trim();
        DiskState diskState = new DiskState();
        String[] rows = diskResult.split(StaticKeys.SPLIT_BR);
        String row = "";
        double sumPer = 0;
        double sumCon = 0;
        double sumUsed = 0;
        //TO DO 此地的计算方法有误
        for (int i = 1; i < rows.length; i++) {
            row = FormatUtil.replaceKg(rows[i]);
            if (row.contains("/dev/")) {
                sumCon += FormatUtil.mToG(row.split(StaticKeys.SPLIT_SPACE)[1]);
                sumUsed += FormatUtil.mToG(row.split(StaticKeys.SPLIT_SPACE)[2]);
            }
        }
        sumPer = (sumUsed / sumCon) * 100;
        diskState.setUsePer(FormatUtil.formatDouble(sumPer, 2) + "");
        diskState.setSize(FormatUtil.formatDouble(sumCon, 2) + "");
        diskState.setUsed(FormatUtil.formatDouble(sumUsed, 2) + "");

        return diskState;
    }

    @Override
    public DiskIoState getDiskIoState() {
        String diskIoResult = doCommond(CMD.DISK_IO);
        if (StringUtils.isEmpty(diskIoResult) || diskIoResult.contains(COMMAND_NOT)) {
            return null;
        }
        diskIoResult = diskIoResult.trim();
        DiskIoState diskIoState = new DiskIoState();
        String[] rows = diskIoResult.split(StaticKeys.SPLIT_BR);
        String[] cols = null;
        String row = "";
        double rs = 0;
        double ws = 0;
        double rkBS = 0;
        double wkBS = 0;
        double await = 0;
        double avgquSz = 0;
        double util = 0;
        for (int i = 4; i < rows.length; i++) {
            row = FormatUtil.replaceKg(rows[i]);
            cols = row.split(StaticKeys.SPLIT_SPACE);
            rs = Double.parseDouble(cols[3].trim()) + rs;
            ws = Double.parseDouble(cols[4].trim()) + ws;
            rkBS = Double.parseDouble(cols[5].trim()) + rkBS;
            wkBS = Double.parseDouble(cols[6].trim()) + wkBS;
            avgquSz = Double.parseDouble(cols[8].trim()) + avgquSz;
            await = Double.parseDouble(cols[9].trim()) + await;
            util = Double.parseDouble(cols[cols.length - 1].trim()) + util;
        }
        rs = rs / (rows.length - 1);
        ws = ws / (rows.length - 1);
        rkBS = rkBS / (rows.length - 1);
        wkBS = wkBS / (rows.length - 1);
        avgquSz = avgquSz / (rows.length - 1);
        await = await / (rows.length - 1);
        util = util / (rows.length - 1);
        diskIoState.setRs(FormatUtil.formatDouble(rs, 2) + "");
        diskIoState.setWs(FormatUtil.formatDouble(ws, 2) + "");
        diskIoState.setRkBS(FormatUtil.formatDouble(rkBS, 2) + "");
        diskIoState.setWkBS(FormatUtil.formatDouble(wkBS, 2) + "");
        diskIoState.setAvgquSz(FormatUtil.formatDouble(avgquSz, 2) + "");
        diskIoState.setAwait(FormatUtil.formatDouble(await, 2) + "");
        diskIoState.setUtil(FormatUtil.formatDouble(util, 2) + "");

        return diskIoState;
    }

    @Override
    public MemState getMemState() {
        String memResult = doCommond(CMD.VIEW_MEM);
        if (StringUtils.isEmpty(memResult)) {
            return null;
        }
        memResult = memResult.trim();
        MemState memState = new MemState();
        String[] rows = memResult.split(StaticKeys.SPLIT_BR);
        String row = "";
        for (int i = 1; i < 2; i++) {
            row = FormatUtil.replaceKg(rows[i]);
            memState.setTotal(row.split(StaticKeys.SPLIT_SPACE)[1]);
            memState.setUsed(row.split(StaticKeys.SPLIT_SPACE)[2]);
            memState.setFree(row.split(StaticKeys.SPLIT_SPACE)[6]);
        }
        try {
            double totalMem = parseDouble(memState.getTotal());
            double usedMem = parseDouble(memState.getUsed());
            double usedPer = (usedMem / totalMem) * 100;
            memState.setUsePer(FormatUtil.formatDouble(usedPer, 1) + "");
        } catch (Exception e) {
            e.printStackTrace();
        }

        return memState;
    }

    @Override
    public NetIoState getNetIoState() {
        String netIoResult = doCommond(CMD.SAR_DEV_1);

        if (StringUtils.isEmpty(netIoResult) || netIoResult.contains(COMMAND_NOT)) {
            return null;
        }

        netIoResult = netIoResult.trim();
        NetIoState netIoState = new NetIoState();
        String[] rows = netIoResult.split(StaticKeys.SPLIT_BR);
        String row = "";
        String[] cols = null;
        double rxpck = 0;
        double txpck = 0;
        double rxbyt = 0;//rxkB/s
        double txbyt = 0;//txkB/s
        double rxcmp = 0;
        double txcmp = 0;
        for (int i = 1; i < rows.length; i++) {
            row = FormatUtil.replaceKg(rows[i]);
            cols = row.split(StaticKeys.SPLIT_SPACE);
            if (cols[0].contains(":") && (cols[1].contains("ens") || cols[1].contains("eth"))) {
                rxpck = parseDouble(cols[2].trim()) + rxpck;
                txpck = parseDouble(cols[3].trim()) + txpck;
                rxbyt = parseDouble(cols[4].trim()) + rxbyt;
                txbyt = Double.parseDouble(cols[5].trim()) + txbyt;
                rxcmp = Double.parseDouble(cols[6].trim()) + rxcmp;
                txcmp = Double.parseDouble(cols[7].trim()) + txcmp;
            }
        }
        netIoState.setRxpck(FormatUtil.formatDouble(rxpck, 2) + "");
        netIoState.setTxpck(FormatUtil.formatDouble(txpck, 2) + "");
        netIoState.setRxbyt(FormatUtil.formatDouble(rxbyt, 2) + "");//rxkB/s
        netIoState.setTxbyt(FormatUtil.formatDouble(txbyt, 2) + "");//txkB/s
        netIoState.setRxcmp(FormatUtil.formatDouble(rxcmp, 2) + "");
        netIoState.setTxcmp(FormatUtil.formatDouble(txcmp, 2) + "");
        return netIoState;
    }


    @Override
    public TcpState getTcpState() {
        String tcpResult = doCommond(CMD.SAR_TCP_1);

        if (StringUtils.isEmpty(tcpResult) || tcpResult.contains(COMMAND_NOT)) {
            return null;
        }

        tcpResult = tcpResult.trim();
        TcpState tcpState = new TcpState();
        String[] rows = tcpResult.split(StaticKeys.SPLIT_BR);
        String row = "";
        String[] cols = null;
        if (rows.length > 1) {
            row = FormatUtil.replaceKg(rows[rows.length - 1]);
            cols = row.split(StaticKeys.SPLIT_SPACE);
            if (cols[0].contains(":")) {
                tcpState.setActive(cols[1]);
                tcpState.setPassive(cols[2]);
                tcpState.setIsegs(cols[3]);
                tcpState.setOseg(cols[4]);
            }
        }
        return tcpState;
    }

    @Override
    public SysLoadState getSysLoadState() {
        String loadResult = doCommond(CMD.UPTIME);
        if (StringUtils.isEmpty(loadResult)) {
            return null;
        }
        String users = "0";
        String average = "average:";
        int userIndex = loadResult.indexOf("user");
        users = loadResult.substring(0, userIndex).substring(userIndex - 2);
        SysLoadState loadState = new SysLoadState();
        String minLoad = loadResult.substring(loadResult.indexOf(average) + average.length());
        String[] cols = minLoad.split(StaticKeys.SPLIT_COMMA);
        loadState.setUsers(users.replace(StaticKeys.SPLIT_SPACE, ""));
        loadState.setOneLoad(cols[0].replace(StaticKeys.SPLIT_COMMA, "").replace(StaticKeys.SPLIT_SPACE, ""));
        loadState.setFiveLoad(cols[1].replace(StaticKeys.SPLIT_COMMA, "").replace(StaticKeys.SPLIT_SPACE, ""));
        loadState.setFifteenLoad(cols[2].replace(StaticKeys.SPLIT_COMMA, "").replace(StaticKeys.SPLIT_SPACE, ""));
        return loadState;
    }


    /**
     * 获取系统版本信息
     *
     * @return
     */
    @Override
    public String getSystemRelease() {
        return doCommond(CMD.SYSTEM_RELEASE).replace(" \\n \\l</br>", "");
    }


    /**
     * 获取系统版本详细信息
     *
     * @return
     */
    @Override
    public String getSystemUname() {
        return doCommond(CMD.UNAME_A);
    }

    /**
     * 获取物理CPU个数
     *
     * @return
     */
    @Override
    public String getCpuNum() {
        return doCommond(CMD.WULI_CPU_NUM);
    }


    /**
     * 获取每个CPU核数量
     *
     * @return
     */
    @Override
    public String getCpuPerCores() {
        String str = doCommond(CMD.WULI_CPU_CORE_NUM);
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
        String runDaysResult = doCommond(CMD.UPTIME);

        if (StringUtils.isEmpty(runDaysResult)) {
            return null;
        }
        String days = "";
        Pattern pattern = Pattern.compile("up.*days");
        Matcher matchs = pattern.matcher(runDaysResult.toLowerCase());
        Pattern pattern2 = Pattern.compile("up.*day");
        Matcher match = pattern2.matcher(runDaysResult.toLowerCase());
        if (matchs.find()) {
            days = matchs.group();
            days = days.replace("up", "").replace("days", "");
            days += "days";
        } else if (match.find()) {
            days = match.group();
            days = days.replace("up", "").replace("day", "");
            days += "day";
        } else {
            int upIndex = runDaysResult.indexOf("up");
            int dzhcIndex = runDaysResult.indexOf(",", upIndex);
            days = runDaysResult.substring(upIndex + 2, dzhcIndex) + "hours";
        }

        return days.replace(StaticKeys.SPLIT_SPACE, "");
    }

    /**
     * 获取CPU型号信息
     *
     * @return
     */
    @Override
    public String getCpuModel() {
        String result = doCommond(CMD.CPU_XINGHAO);
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
        String passwdFile = doCommond(CMD.passwd_update_time);
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
            cols = row.split(StaticKeys.SPLIT_SPACE);
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
