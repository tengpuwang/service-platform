/**
 * Project:platform-paas
 * <p>
 * File:WebContextUtils.java
 * <p>
 * Package:cn.dceast.platform.paas.core.util
 * <p>
 * Date:15/8/2下午4:48
 * <p>
 * Copyright (c) 2015, shumin(shumin_1027@126.com) All Rights Reserved.
 * <p>
 * <p>
 * Modification History:
 * <p>
 * Date         			Author		Version		Description
 * -----------------------------------------------------------------
 * 15/8/2下午4:48		   shumin		1.0			1.0 Version
 */
package wang.tengp.common.util;

import com.google.common.base.Strings;

import javax.servlet.http.HttpServletRequest;

/**
 * WEB上下文工具类
 *
 * @ClassName: WebContextUtils
 * @Description: ${todo}
 * @Author shumin
 * @Date 16/5/18 下午4:48
 * @Email shumin_1027@126.com
 */
public class WebContextUtils {

    private static final String ACCEPT_TYPE_REQUEST_MIME_HEADER = "Accept";
    private static final String HTTP_METHOD_OVERRIDE_HEADER = "X-HTTP-Method-Override";

    private static String webRootPath;
    private static String webRootUrl;

    /**
     * 获取上下文URL根路径
     *
     * @param request
     * @return
     */
    public static final String getWebRootUrl(HttpServletRequest request) {
        if (webRootUrl != null && webRootUrl.length() > 0) {
            return webRootPath;
        }
        StringBuilder sb = new StringBuilder();
        sb.append(request.getScheme()).append("://").append(request.getServerName());
        if (request.getServerPort() != 80) {
            sb.append(":").append(request.getServerPort());
        }
        String contextPath = request.getContextPath();
        if (!Strings.isNullOrEmpty(contextPath)) {
            sb.append(contextPath);
        }
        webRootUrl = sb.toString();
        sb = null;
        return webRootUrl;
    }

    public static final String getWebRootPath(HttpServletRequest request) {
        if (webRootPath != null && webRootPath.length() > 0) {
            return webRootPath;
        }
        webRootPath = request.getServletContext().getRealPath("/");
        return webRootPath;
    }


    public static final String getHttpMethod(HttpServletRequest httpRequest) {
        String method = httpRequest.getHeader(HTTP_METHOD_OVERRIDE_HEADER);
        if (method == null) {
            method = httpRequest.getMethod();
        }
        return method;
    }

    public static final String getAcceptType(HttpServletRequest httpRequest) {
        return httpRequest.getHeader(ACCEPT_TYPE_REQUEST_MIME_HEADER);
    }

    public static final String getContextPath(HttpServletRequest httpRequest) {
        return httpRequest.getContextPath();
    }


    public static final String getRemoteAddr(HttpServletRequest request) {
        // 通过请求头获取ip地址
        String ip = request.getHeader("x-forwarded-for");
        // 判断ip地址是否是代理地址
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        // 判断ip地址是否是代理地址
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        // 判断ip地址是否是代理地址
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }
}