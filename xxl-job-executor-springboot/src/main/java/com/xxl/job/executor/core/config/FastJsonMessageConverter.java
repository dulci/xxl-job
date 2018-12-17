/***********************************************************
 *(该类的功能及其特点描述)
 *
 *@see(与该类相关联的类)
 *
 *广联达
 *
 *版权：本文件版权归属北京广联达科技股份有限公司
 *
 *
 *@author:郭冲
 *
 *@since:jdk1.5
 *
 *
 *@version:1.0
 *
 *@date:2015-4-21
 *
 *最后更改日期：
 *
 *
 *修改人：
 *
 *
 ********************************************************/

package com.xxl.job.executor.core.config;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.support.converter.AbstractMessageConverter;
import org.springframework.amqp.support.converter.MessageConversionException;

import java.io.UnsupportedEncodingException;


public class FastJsonMessageConverter extends AbstractMessageConverter {

	private static Log log = LogFactory.getLog(FastJsonMessageConverter.class);

	private static final String DEFAULT_CHARSET = "UTF-8";

	private volatile String defaultCharset = DEFAULT_CHARSET;

	public FastJsonMessageConverter() {
		super();
	}

	public void setDefaultCharset(String defaultCharset) {
		this.defaultCharset = (defaultCharset != null) ? defaultCharset
				: DEFAULT_CHARSET;
	}

	public Object fromMessage(Message message)
			throws MessageConversionException {
		 String json = "";
		try {
			json = new String(message.getBody(), "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return JSON.parseObject(json);
	}

	@SuppressWarnings("unchecked")
	public <T> T fromMessage(Message message, T t) {
		String json = "";
		try {
			json = new String(message.getBody(), "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return (T) JSON.parseObject(json, t.getClass());
	}

	public JSONObject fromMessage2Json(Message message) {
		String json = "{}";
		try {
			json = new String(message.getBody(), "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return JSONObject.parseObject(json);
	}

	protected Message createMessage(Object objectToConvert, MessageProperties messageProperties)
			throws MessageConversionException {
		byte[] bytes;
		try {
			String jsonString = JSON.toJSONString(objectToConvert);
			bytes = jsonString.getBytes(this.defaultCharset);
		} catch (UnsupportedEncodingException e) {
			throw new MessageConversionException(
					"Failed to convert Message content", e);
		}
		messageProperties.setContentType(MessageProperties.CONTENT_TYPE_JSON);
		messageProperties.setContentEncoding(this.defaultCharset);
		if (bytes != null) {
			messageProperties.setContentLength(bytes.length);
		}
		return new Message(bytes, messageProperties);

	}

}
