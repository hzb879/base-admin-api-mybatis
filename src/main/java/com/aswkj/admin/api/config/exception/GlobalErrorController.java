package com.aswkj.admin.api.config.exception;


import com.aswkj.admin.api.common.response.ResponseData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.ServletWebRequest;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;
import java.util.Objects;

@Controller
public class GlobalErrorController implements ErrorController {

//	private static final String ERROR_VIEW = "common/error";

  private final static String ERROR_PATH = "/error";

  private static final Logger logger = LoggerFactory
          .getLogger(GlobalErrorController.class);

  /**
   * Error Attributes in the Application
   */
  @Autowired
  private ErrorAttributes errorAttributes;

//    /**
//     * Supports the HTML Error View
//     *
//     * @param request
//     */
//    @RequestMapping(value = ERROR_PATH,produces=MediaType.TEXT_HTML_VALUE)
//    public ModelAndView errorHtml(HttpServletRequest request) {
//    		return new ModelAndView(ERROR_VIEW, this.getErrorAttributes(request, false));
//    }

  /**
   * Supports other formats like JSON, XML
   *
   * @param request
   */
  @RequestMapping(value = ERROR_PATH)
  @ResponseBody
  public ResponseData<Map<String, Object>> error(
          HttpServletRequest request) {
    Map<String, Object> body = this.getErrorAttributes(request,
            this.getTraceParameter(request));
    return new ResponseData<Map<String, Object>>(this.getStatus(request).value(), body.get("message").toString(), body);
  }

  @Override
  public String getErrorPath() {
    return ERROR_PATH;
  }

  private boolean getTraceParameter(HttpServletRequest request) {
    String parameter = request.getParameter("trace");
    if (Objects.isNull(parameter)) {
      return false;
    }
    return !"false".equals(parameter.toLowerCase());
  }

  private Map<String, Object> getErrorAttributes(HttpServletRequest request,
                                                 boolean includeStackTrace) {
    Map<String, Object> map = this.errorAttributes
            .getErrorAttributes(new ServletWebRequest(request), includeStackTrace);
    logger.error("map is [{}]", map);
    return map;
  }

  private HttpStatus getStatus(HttpServletRequest request) {
    Integer statusCode = (Integer) request
            .getAttribute("javax.servlet.error.status_code");
    if (statusCode != null) {
      return HttpStatus.valueOf(statusCode);
    }
    return HttpStatus.INTERNAL_SERVER_ERROR;
  }
}
