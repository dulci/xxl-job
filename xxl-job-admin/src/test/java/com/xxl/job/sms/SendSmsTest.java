package com.xxl.job.sms;

import com.xxl.job.admin.service.impl.AdminBizImpl;
import glodon.gcj.member.center.utils.util.SMSSendUtil;
import org.junit.Test;

import java.text.MessageFormat;

/**
 * Created by zhangjianlong on 2018/12/21.
 */
public class SendSmsTest {
    @Test
    public void sendSms() {
        SMSSendUtil.sendISMS("caijia-cb-sms", "123qwe!@#", "15313254186", "test");
    }

    @Test
    public void sendSms1() {
        System.out.println(SMSSendUtil.sendSMSYM("15313254186", "test"));
    }

    @Test
    public void sendSms2() {

        System.out.println(SMSSendUtil.sendSMS("15313254186", "test"));
    }

    @Test
    public void sendSms3() {
        System.out.println(MessageFormat.format(AdminBizImpl.CHILD_JOBID_REGXP,5) );
    }

}
