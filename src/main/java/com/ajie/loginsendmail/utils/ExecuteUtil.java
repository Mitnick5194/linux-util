package com.ajie.loginsendmail.utils;

import org.apache.commons.lang3.StringUtils;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * 命令执行工具
 */
public class ExecuteUtil {

    private ExecuteUtil() {

    }

    public static String execute(String cmd) throws Exception {
        if (StringUtils.isBlank(cmd)) {
            throw new Exception("无效命令");
        }
        String[] cmdArr = new String[3];
       cmdArr[0] = "/bin/sh";
        cmdArr[1] = "-c";
        cmdArr[2] = cmd;
        Process exec = Runtime.getRuntime().exec(cmdArr);
        InputStream inputStream = exec.getInputStream();
        BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
        String line;
        StringBuilder sb = new StringBuilder();
        while ((line = br.readLine()) != null) {
            sb.append(line);
            sb.append("\r\n");
        }
        int i = exec.waitFor();
        if (0 != i) {
            //有错误
            line = null;
            sb = new StringBuilder();
            //读取错误信息
            br = new BufferedReader(new InputStreamReader(inputStream));
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
            throw new Exception(sb.toString());
        }
        return sb.toString();


    }
}
