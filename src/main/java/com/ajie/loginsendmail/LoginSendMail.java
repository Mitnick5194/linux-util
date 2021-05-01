package com.ajie.loginsendmail;

import com.ajie.api.ip.IpQueryVo;
import com.ajie.api.ip.impl.IpQueryApiImpl;
import com.ajie.loginsendmail.utils.ExecuteUtil;
import com.ajie.loginsendmail.utils.IpUtil;
import com.ajie.loginsendmail.utils.SendMailUtil;
import org.apache.commons.lang3.StringUtils;

import java.util.Properties;

/**
 * linux登录邮件报告登录信息
 */
public class LoginSendMail {

    public static IpQueryApiImpl ipQueryApi = new IpQueryApiImpl();

    static {
        Config config = Config.getConfig();
        ipQueryApi.setGaodeKey(config.getProperties().getProperty("gaode_key"));
        ipQueryApi.setIpstackAccessKey(config.getProperties().getProperty("ip_stack_key"));
    }

    public static void run(String cmd) {
        System.out.println(cmd);
        boolean flag = false;
        try {
            String msg = ExecuteUtil.execute(cmd);
            //提取ip地址
            String ip = IpUtil.pickupIp(msg);
            String province = null;
            if (StringUtils.isNoneBlank(ip)) {
                //查询ip地址
                try {
                    IpQueryVo vo = ipQueryApi.queryByGaode(ip);
                    province = vo.getProvince();
                    if (StringUtils.isBlank(province) || province.equals("[]")) {
                        //改个供应商查
                        vo = ipQueryApi.queryByIpstack(ip);
                        province = vo.getProvince();
                    }
                    if (null != province && !province.endsWith("省")) {
                        province += "省";
                    }
                    msg += " " + province + vo.getCity();
                } catch (Exception e) {
                    //忽略异常
                }
            }
            Properties properties = Config.getConfig().getProperties();
            String isIgnoreGd = properties.getProperty("is_ignore_gd");
            if ("true".equals(isIgnoreGd) && null != province && province.contains("广东")) {
                System.out.println("忽略广东");
                return;
            }
            flag = true;//标记命令执行成功，如果异常 ，那也是发送邮件异常
            System.out.println(msg);
            SendMailUtil.sendMail(msg);
        } catch (Exception e) {
            e.printStackTrace();
            if (!flag) {
                //发送邮件通知命令执行失败
                try {
                    SendMailUtil.sendMail("命令执行过程失败：" + e.getMessage());
                } catch (Exception ex) {
                    //忽略
                }
            }
        }
    }

}
