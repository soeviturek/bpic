package com.bpic.common.utils.reponse;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;

@Data
public class Response<T> implements Serializable {
	private static final Logger logger = LoggerFactory.getLogger(Response.class);

	private static final long serialVersionUID = 1L;

	public static final Integer SUCCESS = 200;
	public static final String SUCCESS_MESSAGE = "操作成功！";
	public static final Integer ERROR = 500;
	public static final String ERROR_MESSAGE = "服务异常！";

	private Integer code = 200;
	private String msg;
	private transient T data;

	public Response(Integer resultCode, String resultMsg) {
		super();
		this.setCode(resultCode);
		this.setMsg(resultMsg);
	}

	public T getData() {
		return data;
	}

	public void setData(T data) {
		this.data = data;
	}

	public Response() {

	}


	public Response<Object> resultData(String res, Response<Object> response) {
		Response<Object> bean = new Response<Object>(SUCCESS, SUCCESS_MESSAGE);
		try {
			JSONObject resObj = JSON.parseObject(res);
			if (resObj != null) {
				bean.setCode(resObj.getInteger("code"));
				bean.setMsg(StringUtils.isNotBlank(resObj.getString("msg")) ? resObj.getString("msg") : "");
				bean.setData(resObj.getJSONObject("responseBody"));
				return bean;
			}
			return response;
		} catch (Exception e) {
			logger.error("", e);
			bean.setCode(ERROR);
			bean.setMsg(ERROR_MESSAGE);
			bean.setData(e.getLocalizedMessage());
			return bean;
		}
		
	}

	public Integer getCode() {
		return code;
	}

	public void setCode(Integer code) {
		this.code = code;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}
}
