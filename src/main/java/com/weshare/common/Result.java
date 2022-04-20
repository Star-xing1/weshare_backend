package com.weshare.common;

import java.util.HashMap;
import java.util.Map;
import lombok.Getter;

/**
 * 结果实体：用于统一后端向前端返回数据的格式
 */
@Getter
public class Result {

	// 是否成功获取数据
	private Boolean success;
	// 返回状态码
	private Integer code;
	// 返回信息
	private String message;
	// 返回数据集
	private final Map<String, Object> data = new HashMap<>();

	private Result() {
	}

	// 获取成功结果
	public static Result ok() {
		Result result = new Result();
		result.setSuccess(true);
		result.setCode(ResultCode.SUCCESS.code);
		return result;
	}

	// 获取失败结果
	public static Result error() {
		Result result = new Result();
		result.setSuccess(false);
		result.setCode(ResultCode.ERROR.code);
		return result;
	}

	/**
	 * 下列方法用于链式编程
	 */
	public Result success(Boolean success) {
		this.setSuccess(success);
		return this;
	}

	public Result code(Integer code) {
		this.setCode(code);
		return this;
	}

	public Result message(String message) {
		this.setMessage(message);
		return this;
	}

	public Result data(String key, Object value) {
		this.data.put(key, value);
		return this;
	}

	private void setSuccess(Boolean success) {
		this.success = success;
	}

	private void setCode(Integer code) {
		this.code = code;
	}

	private void setMessage(String message) {
		this.message = message;
	}
}