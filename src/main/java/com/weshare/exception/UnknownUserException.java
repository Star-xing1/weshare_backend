package com.weshare.exception;

public class UnknownUserException extends RuntimeException {

	public UnknownUserException() {
		super("未知的用户");
	}
}
