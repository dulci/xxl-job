package com.xxl.job.lock;

import com.xxl.job.admin.core.util.ZkDistributeLock;
import com.xxl.job.core.util.DateUtil;
import org.junit.Test;

import java.util.Date;
import java.util.concurrent.locks.Lock;

/**
 * Created by zhangjianlong on 2018/12/27.
 */
public class ZkDistributeLockTest {
    @Test
    public void test(){

        Lock lock = new ZkDistributeLock("testerte22");
        try {
            if (lock.tryLock()) {
                System.out.print("dddd");
                System.out.print("dddd");
            }
        }catch(Exception e){
            e.printStackTrace();

        }finally {
            lock.unlock();
        }

    }
    @Test
    public void test2(){

    Date now  =new Date();
    System.out.println(DateUtil.format(now));
    Date hDate = DateUtil.calSeconds(now,3);
        System.out.println(DateUtil.format(hDate));
    }



}
