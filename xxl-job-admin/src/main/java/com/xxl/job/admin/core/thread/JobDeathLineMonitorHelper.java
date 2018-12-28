package com.xxl.job.admin.core.thread;

import com.xxl.job.admin.core.conf.XxlJobAdminConfig;
import com.xxl.job.admin.core.model.XxlJobGroup;
import com.xxl.job.admin.core.model.XxlJobInfo;
import com.xxl.job.admin.core.model.XxlJobLog;
import com.xxl.job.admin.core.trigger.TriggerTypeEnum;
import com.xxl.job.admin.core.util.I18nUtil;
import com.xxl.job.admin.core.util.ZkDistributeLock;
import com.xxl.job.core.biz.model.ReturnT;
import glodon.gcj.member.center.utils.util.SMSSendUtil;
import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.javamail.MimeMessageHelper;

import javax.mail.internet.MimeMessage;
import java.text.MessageFormat;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;

/**
 * job monitor instance
 *
 * @author xuxueli 2015-9-1 18:05:56
 */
public class JobDeathLineMonitorHelper {
    private static Logger logger = LoggerFactory.getLogger(JobDeathLineMonitorHelper.class);

    private static JobDeathLineMonitorHelper instance = new JobDeathLineMonitorHelper();

    public static JobDeathLineMonitorHelper getInstance() {
        return instance;
    }

    // ---------------------- monitor ----------------------

    private Thread monitorThread;
    private volatile boolean toStop = false;

    public void start() {
        monitorThread = new Thread(new Runnable() {

            @Override
            public void run() {

                // monitor
                while (!toStop) {
                    Lock lock = new ZkDistributeLock("JobDeathLineMonitorHelper_lock");
                    try {
                        if (lock.tryLock()) {
                            List<Integer> failLogIds = XxlJobAdminConfig.getAdminConfig().getXxlJobLogDao().findDealthJobLogIds(1000);
                            if (CollectionUtils.isNotEmpty(failLogIds)) {
                                for (int failLogId : failLogIds) {

                                    // lock log
                                    int lockRet = XxlJobAdminConfig.getAdminConfig().getXxlJobLogDao().updateAlarmStatus(failLogId, 0, -1);
                                    if (lockRet < 1) {
                                        continue;
                                    }
                                    XxlJobLog log = XxlJobAdminConfig.getAdminConfig().getXxlJobLogDao().load(failLogId);
                                    XxlJobInfo info = XxlJobAdminConfig.getAdminConfig().getXxlJobInfoDao().loadById(log.getJobId());


                                    // 2、fail alarm monitor
                                    int newAlarmStatus = 0;        // 告警状态：0-默认、-1=锁定状态、1-无需告警、2-告警成功、3-告警失败
                                    boolean alarmFlag =XxlJobAdminConfig.getAdminConfig().getAlarmFlag();
                                    if (alarmFlag && info != null &&
                                            (
                                                    (info.getAlarmEmail() != null && info.getAlarmEmail().trim().length() > 0)
                                                            ||
                                                            (info.getAlarmTel() != null && info.getAlarmTel().trim().length() > 0)

                                            )) {
                                        boolean alarmResult = true;
                                        try {
                                            if (info.getAlarmEmail() != null && info.getAlarmEmail().trim().length() > 0) {
                                                alarmResult = failAlarm(info, log);
                                            }
                                            if (info.getAlarmTel() != null && info.getAlarmTel().trim().length() > 0 && log.getExecutorFailRetryCount() <= 0) {
                                                alarmResult = failSendSms(info, log);
                                            }

                                        } catch (Exception e) {
                                            alarmResult = false;
                                            logger.error(e.getMessage(), e);
                                        }
                                        newAlarmStatus = alarmResult ? 2 : 3;
                                    } else {
                                        newAlarmStatus = 1;
                                    }

                                    XxlJobAdminConfig.getAdminConfig().getXxlJobLogDao().updateAlarmStatus(failLogId, -1, newAlarmStatus);
                                }
                            }
                        }
                        TimeUnit.SECONDS.sleep(10);
                    } catch (Exception e) {
                        logger.error("job dealth line monitor error:{}", e);
                    } finally {
                        lock.unlock();
                    }
                }

            }
        });
        monitorThread.setDaemon(true);
        monitorThread.start();
    }

    public void toStop() {
        toStop = true;
        // interrupt and wait
        monitorThread.interrupt();
        try {
            monitorThread.join();
        } catch (InterruptedException e) {
            logger.error(e.getMessage(), e);
        }
    }


    // ---------------------- alarm ----------------------

    // email alarm template
    private static final String mailBodyTemplate = "<h5>" + I18nUtil.getString("jobconf_monitor_detail") + "：</span>" +
            "<table border=\"1\" cellpadding=\"3\" style=\"border-collapse:collapse; width:80%;\" >\n" +
            "   <thead style=\"font-weight: bold;color: #ffffff;background-color: #ff8c00;\" >" +
            "      <tr>\n" +
            "         <td width=\"20%\" >" + I18nUtil.getString("jobinfo_field_jobgroup") + "</td>\n" +
            "         <td width=\"10%\" >" + I18nUtil.getString("jobinfo_field_id") + "</td>\n" +
            "         <td width=\"20%\" >" + I18nUtil.getString("jobinfo_field_jobdesc") + "</td>\n" +
            "         <td width=\"10%\" >" + I18nUtil.getString("jobconf_monitor_alarm_title") + "</td>\n" +
            "         <td width=\"40%\" >" + I18nUtil.getString("jobconf_monitor_alarm_content") + "</td>\n" +
            "      </tr>\n" +
            "   </thead>\n" +
            "   <tbody>\n" +
            "      <tr>\n" +
            "         <td>{0}</td>\n" +
            "         <td>{1}</td>\n" +
            "         <td>{2}</td>\n" +
            "         <td>" + I18nUtil.getString("jobconf_monitor_alarm_type") + "</td>\n" +
            "         <td>{3}</td>\n" +
            "      </tr>\n" +
            "   </tbody>\n" +
            "</table>";

    private static final String smsBodyTemplate = "任务[{0}][{1}][{2}]执行超时";


    /**
     * fail alarm
     *
     * @param jobLog
     */
    private boolean failAlarm(XxlJobInfo info, XxlJobLog jobLog) {
        boolean alarmResult = true;

        // send monitor email
        if (info != null && info.getAlarmEmail() != null && info.getAlarmEmail().trim().length() > 0) {


            Set<String> emailSet = new HashSet<String>(Arrays.asList(info.getAlarmEmail().split(",")));
            for (String email : emailSet) {
                XxlJobGroup group = XxlJobAdminConfig.getAdminConfig().getXxlJobGroupDao().load(Integer.valueOf(info.getJobGroup()));

                String personal = I18nUtil.getString("admin_name_full");
                String title = I18nUtil.getString("jobconf_monitor");
                String content = MessageFormat.format(mailBodyTemplate,
                        group != null ? group.getTitle() : "null",
                        info.getId(),
                        info.getJobDesc(),
                        "任务超时");


                // make mail
                try {
                    MimeMessage mimeMessage = XxlJobAdminConfig.getAdminConfig().getMailSender().createMimeMessage();

                    MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
                    helper.setFrom(XxlJobAdminConfig.getAdminConfig().getEmailUserName(), personal);
                    helper.setTo(email);
                    helper.setSubject(title);
                    helper.setText(content, true);

                    XxlJobAdminConfig.getAdminConfig().getMailSender().send(mimeMessage);
                } catch (Exception e) {
                    logger.error(">>>>>>>>>>> job monitor alarm email send error, JobLogId:{}", jobLog.getId(), e);

                    alarmResult = false;
                }

            }
        }

        // TODO, custom alarm strategy, such as sms


        return alarmResult;
    }


    private boolean failSendSms(XxlJobInfo info, XxlJobLog jobLog) {
        boolean alarmResult = true;

        // send monitor email
        if (info != null && info.getAlarmTel() != null && info.getAlarmTel().trim().length() > 0) {


            Set<String> telSet = new HashSet<String>(Arrays.asList(info.getAlarmTel().split(",")));
            XxlJobGroup group = XxlJobAdminConfig.getAdminConfig().getXxlJobGroupDao().load(Integer.valueOf(info.getJobGroup()));
            String content = MessageFormat.format(smsBodyTemplate,
                    info.getId(),
                    info.getJobDesc(),
                    jobLog.getId());

            for (String tel : telSet) {

                // make mail
                try {
                    XxlJobAdminConfig xxlJobAdminConfig = XxlJobAdminConfig.getAdminConfig();

                    SMSSendUtil.sendISMS(xxlJobAdminConfig.getSmsUserName(), xxlJobAdminConfig.getSmsPassword(), tel, content);
                    //XxlJobAdminConfig.getAdminConfig().getMailSender().send(mimeMessage);
                } catch (Exception e) {
                    logger.error(">>>>>>>>>>> job monitor alarm tel send error, JobLogId:{}", jobLog.getId(), e);

                    alarmResult = false;
                }

            }
        }


        return alarmResult;
    }


}
