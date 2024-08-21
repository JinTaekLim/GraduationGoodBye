package com.ggb.graduationgoodbye.global.error;

import com.ggb.graduationgoodbye.global.error.exception.BusinessException;
import com.ggb.graduationgoodbye.global.error.exception.ForbiddenException;
import com.ggb.graduationgoodbye.global.error.exception.ServerException;
import com.ggb.graduationgoodbye.global.error.exception.UnAuthenticatedException;
import com.ggb.graduationgoodbye.global.error.type.ApiErrorType;
import com.ggb.graduationgoodbye.global.response.ApiResponse;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

  /**
   * Bean Validation 예외 처리
   */
  @ExceptionHandler(BindException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public ApiResponse<?> handler(BindException e) {
    List<String> errorMessageList = e.getFieldErrors().stream().map(
        b -> b.getField() + " : " + b.getDefaultMessage()
    ).toList();
    return ApiResponse.error(ApiErrorType.BAD_REQUEST, errorMessageList);
  }

  /**
   * 잘못된 요청 경우 예외 처리
   */
  @ExceptionHandler(BusinessException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public ApiResponse<?> handler(BusinessException e) {
    return ApiResponse.error(e.getCode(), e.getMessage());
  }

  /**
   * 인증을 실패 했을 경우 예외 처리
   */
  @ExceptionHandler(UnAuthenticatedException.class)
  @ResponseStatus(HttpStatus.UNAUTHORIZED)
  public ApiResponse<?> handler(UnAuthenticatedException e) {
    return ApiResponse.error(e.getCode(), e.getMessage());
  }

  /**
   * 권한이 없을 경우 예외 처리
   */
  @ExceptionHandler(ForbiddenException.class)
  @ResponseStatus(HttpStatus.FORBIDDEN)
  public ApiResponse<?> handler(ForbiddenException e) {
    return ApiResponse.error(e.getCode(), e.getMessage());
  }

  /**
   * 서버에서 에러가 발생 했을 경우 예외 처리
   */
  @ExceptionHandler(ServerException.class)
  @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
  public ApiResponse<?> handler(ServerException e) {
    return ApiResponse.error(e.getCode(), e.getMessage());
  }

  @ExceptionHandler(Exception.class)
  @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
  public ApiResponse<?> handler(Exception e) {
    log.error(e.getMessage());
    log.error("Exception : ", e);
    return ApiResponse.error(ApiErrorType.INTERNAL_SERVER_ERROR);
  }
}
