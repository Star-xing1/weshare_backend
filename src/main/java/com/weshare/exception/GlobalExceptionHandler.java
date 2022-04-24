package com.weshare.exception;

import com.weshare.common.Result;
import java.util.HashMap;
import java.util.Map;
import javax.persistence.EntityNotFoundException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 全局异常处理器
 */
@RestControllerAdvice(basePackages = "com.weshare.controller")
public class GlobalExceptionHandler {

	/**
	 * JSR303异常处理器
	 */
	@ExceptionHandler(value = MethodArgumentNotValidException.class)
	public Result handleException(MethodArgumentNotValidException e) {
		BindingResult bindingResult = e.getBindingResult();
		Map<String,String> map = new HashMap<>();
		bindingResult.getFieldErrors().forEach(fieldError->{
			map.put(fieldError.getField(),fieldError.getDefaultMessage());
		});
		return Result.error().data("resultMap",map);
	}

	/**
	 * JPA不存在的主键查找
	 */
	@ExceptionHandler(value = EntityNotFoundException.class)
	public Result handleException(EntityNotFoundException e) {
		e.printStackTrace();
		return Result.error().message(e.getMessage());
	}

	/**
	 * 全局异常处理器
	 */
	@ExceptionHandler(value = Exception.class)
	public Result handleException(Exception e) {
		e.printStackTrace();
		return Result.error().message(e.getMessage());
	}
}