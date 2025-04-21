package com.bocchi.bocchiweb.util.response;

import org.springframework.http.MediaType;
import org.springframework.core.MethodParameter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import jakarta.servlet.http.HttpServletResponse;

@RestControllerAdvice
public class ApiResponseAdvice implements ResponseBodyAdvice<Object> {

    @Override
    public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
        return true;
    }

    @Override
    public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType,
            Class<? extends HttpMessageConverter<?>> selectedConverterType, ServerHttpRequest request,
            ServerHttpResponse response) {

        if (body instanceof ApiResponse<?>) {
            return body;
        }

        String message = "";
        ApiMessage apiMessage = returnType.getMethodAnnotation(ApiMessage.class);
        if (apiMessage != null) {
            message = apiMessage.value();
        }

        HttpServletResponse servletResponse = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes())
                .getResponse();

        int statusCode = (servletResponse != null) ? servletResponse.getStatus() : 200;

        return new ApiResponse<>(statusCode, null, message, body);
    }

}