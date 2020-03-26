package com.kakaointerntask.bank.common;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.PrintWriter;
import java.io.StringWriter;

public class ExceptionUtil {
    public static String getStackTrace(Throwable t) {
        StringWriter sw = new StringWriter();
        t.printStackTrace(new PrintWriter(sw));
        return sw.toString();
    }

    public static String getMessageWithLine(Throwable t) {
        return String.format(
                "Exception occurred : %s\n%s.%s in %s:%d",
                t.getMessage(),
                t.getStackTrace()[0].getClassName(),
                t.getStackTrace()[0].getMethodName(),
                t.getStackTrace()[0].getFileName(),
                t.getStackTrace()[0].getLineNumber());
    }
}
