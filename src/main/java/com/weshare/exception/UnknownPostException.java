package com.weshare.exception;

public class UnknownPostException extends RuntimeException {

	public UnknownPostException() {
		super("不存在的推文");
	}
}
