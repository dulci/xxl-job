package com.xxl.job.worker.util;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * Created by dul-c on 2018-12-17.
 */
public class IpUtil {
	public static String getIp() {
		String ip = null;
		try {
			InetAddress addr = InetAddress.getLocalHost();
			ip = addr.getHostAddress().toString();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		return ip;
	}
}
