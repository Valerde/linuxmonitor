package com.sovava.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

/**
 * <p>Title:CtrCommond </p>
 * <p>Description: 链接服务器的工具类</p>
 */
@Slf4j
public class CtrCommond {


    /**
     * 执行命令
     *
     * @param cmd
     * @return
     */
    public static String doCommond(String cmd) {
        String result = "";
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
