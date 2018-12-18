package com.xxl.job.core.util;

/**
 * LogInfoUtil vo
 * @author xuxueli 2017-07-25 21:26:38
 */
public class LogInfoUtil {

    private static InheritableThreadLocal<LogInfoVO> logContext = new InheritableThreadLocal<LogInfoVO>();

    public static void setLogInfo(LogInfoVO logInfoVO){
        logContext.set(logInfoVO);
    }

    public static Integer getLogId(){
        LogInfoVO logInfoVO =  logContext.get();
        return logInfoVO.getLogId();
    }
    public static String getMqKey(){
        LogInfoVO logInfoVO =  logContext.get();
        return logInfoVO.getMqKey();
    }
    public static int getJobId(){
        LogInfoVO logInfoVO =  logContext.get();
        return logInfoVO.getJobId();
    }
    public static String getJobDesc(){
        LogInfoVO logInfoVO =  logContext.get();
        return logInfoVO.getJobDesc();
    }




    public static class LogInfoVO {

        private int jobId;
        private int logId;
        private String jobDesc;
        private String mqKey;

        public LogInfoVO(int jobId, int logId,String jobDesc,String mqKey) {
            this.jobId = jobId;
            this.logId = logId;
            this.jobDesc=jobDesc;
            this.mqKey=mqKey;

        }

        public int getJobId() {
            return jobId;
        }

        public void setJobId(int jobId) {
            this.jobId = jobId;
        }

        public int getLogId() {
            return logId;
        }

        public void setLogId(int logId) {
            this.logId = logId;
        }

        public String getMqKey() {
            return mqKey;
        }

        public void setMqKey(String mqKey) {
            this.mqKey = mqKey;
        }

        public String getJobDesc() {
            return jobDesc;
        }

        public void setJobDesc(String jobDesc) {
            this.jobDesc = jobDesc;
        }
    }


}
