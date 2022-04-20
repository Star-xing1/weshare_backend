package com.weshare.common;

/**
 * 结果响应码：用于定义后端向前端返回数据时的响应码
 */
public enum ResultCode {
	SUCCESS(20000),
	ERROR(20001);

	int code;

	ResultCode(int code) {
		this.code = code;
	}
}
