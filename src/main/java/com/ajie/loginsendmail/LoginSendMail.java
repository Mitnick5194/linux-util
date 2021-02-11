package com.ajie.loginsendmail;

import com.ajie.api.ip.IpQueryVo;
import com.ajie.api.ip.impl.IpQueryApiImpl;
import com.ajie.loginsendmail.utils.ExecuteUtil;
import com.ajie.loginsendmail.utils.IpUtil;
import com.ajie.loginsendmail.utils.SendMailUtil;
import org.apache.commons.lang3.StringUtils;

/**
 * linux登录邮件报告登录信息
 */
public class LoginSendMail {

    public static IpQueryApiImpl ipQueryApi = new IpQueryApiImpl();

    static {
        ipQueryApi.setGaodeKey("ad3d96f353c59486c2272898c3d30faf");
        ipQueryApi.setIpstackAccessKey("4b09d9dbaba703ee541d677f9c216b5c");
    }

    public static void run() {
        boolean flag = false;
        try {
            String msg = ExecuteUtil.execute(" who -a | grep tl");
            //提取ip地址
            String ip = IpUtil.pickupIp(msg);
            if (StringUtils.isNoneBlank(ip)) {
                //查询ip地址
                try {
                    IpQueryVo vo = ipQueryApi.queryByGaode(ip);
                    String province = vo.getProvince();
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
            flag = true;//标记命令执行成功，如果异常 ，那也是发送邮件异常
            System.out.println(msg);
            SendMailUtil.sendMail(msg);
        } catch (Exception e) {
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
