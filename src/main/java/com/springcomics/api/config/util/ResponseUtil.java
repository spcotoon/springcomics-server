package com.springcomics.api.config.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.springcomics.api.response.ErrorResponse;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

public class ResponseUtil {
    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static void sendErrorResponse(HttpServletResponse response, int status, String message) throws IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.setStatus(status);

        ErrorResponse errorResponse = ErrorResponse.builder()
                .code(String.valueOf(status))
                .message(message)
                .build();

        response.getWriter().write(objectMapper.writeValueAsString(errorResponse));
        response.getWriter().flush();
    }
}
