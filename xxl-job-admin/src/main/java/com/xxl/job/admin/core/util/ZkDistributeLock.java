package com.xxl.job.admin.core.util;

import com.xxl.job.admin.core.conf.XxlJobAdminConfig;
import org.I0Itec.zkclient.IZkDataListener;
import org.I0Itec.zkclient.ZkClient;
import org.I0Itec.zkclient.exception.ZkNodeExistsException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

public class ZkDistributeLock implements Lock  {
	private static Logger logger = LoggerFactory.getLogger(ZkDistributeLock.class);

	private static final String ZK_IP_PORT = XxlJobAdminConfig.getAdminConfig().getZookeeperAddress();


	private  static  ZkClient client = new ZkClient(ZK_IP_PORT);

	private CountDownLatch cdl = null;
	// 锁的名字
	protected String lockKey;

	public ZkDistributeLock(String lockKey) {
		this.lockKey = lockKey;
	}

	// 实现阻塞式的加锁
	@Override
	public void lock() {
		if (tryLock()) {
			return;
		}
		waitForLock();
		lock();
	}

	// 阻塞时的实现
	private void waitForLock() {
		// 给节点加监听
		IZkDataListener listener = new IZkDataListener() {
			@Override
			public void handleDataDeleted(String dataPath) throws Exception {
				logger.info("-------get data delete event---------------");
				if (cdl != null) {
					cdl.countDown();
				}
			}

			@Override
			public void handleDataChange(String dataPath, Object data) throws Exception {
			}
		};

		client.subscribeDataChanges(getKey(), listener);
		if (client.exists(getKey())) {
			try {
				cdl = new CountDownLatch(1);
				cdl.await();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

		client.unsubscribeDataChanges(getKey(), listener);
	}

	// 实现非阻塞式的加锁
	@Override
	public boolean tryLock() {
		try {
			client.createEphemeral(getKey());
			return true;
		} catch (ZkNodeExistsException e) {
			return false;
		}
	}

	@Override
	public void unlock() {
		client.delete(getKey());
	}

	// -------------------------------

	@Override
	public boolean tryLock(long time, TimeUnit unit) throws InterruptedException {
		return false;
	}

	@Override
	public Condition newCondition() {
		return null;
	}

	@Override
	public void lockInterruptibly() throws InterruptedException {
	}

	private String getKey(){
		return "/"+this.lockKey;
	}


}
