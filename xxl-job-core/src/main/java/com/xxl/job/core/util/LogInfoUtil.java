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



    public static class LogInfoVO {

        private int jobId;
        private int logId;

        public LogInfoVO(int jobId, int logId) {
            this.jobId = jobId;
            this.logId = logId;
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
    }


}
