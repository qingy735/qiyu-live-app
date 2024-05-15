package org.qiyu.live.framework.web.starter.error;

import jakarta.servlet.http.HttpServletRequest;
import org.qiyu.live.common.interfaces.vo.WebResponseVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @Author: QingY
 * @Date: Created in 22:02 2024-05-11
 * @Description:
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(value = Exception.class)
    @ResponseBody
    public WebResponseVO errorHandler(HttpServletRequest request, Exception e) {
        LOGGER.error(request.getRequestURI() + ", error is ", e);
        return WebResponseVO.sysError("系统异常");
    }

    /**
     * 异常捕获 监听自定义异常
     *
     * @param request
     * @param e
     * @return
     */
    @ExceptionHandler(value = QiyuErrorException.class)
    @ResponseBody
    public WebResponseVO sysErrorHandler(HttpServletRequest request, QiyuErrorException e) {
        LOGGER.error(request.getRequestURI() + ", error is ", e);
        return WebResponseVO.bizError(e.getErrorMsg(), e.getErrorCode());
    }

}
