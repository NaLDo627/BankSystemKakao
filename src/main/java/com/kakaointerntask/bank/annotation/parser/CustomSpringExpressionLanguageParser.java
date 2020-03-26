package com.kakaointerntask.bank.annotation.parser;

import org.springframework.expression.ExpressionParser;
import org.springframework.expression.ParserContext;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

public class CustomSpringExpressionLanguageParser {
    private static ExpressionParser PARSER = new SpelExpressionParser();

    public static String getDynamicStringValue(String[] parameterNames, Object[] args, String key) {
        return getDynamicValue(parameterNames, args, key, String.class);
    }

    public static Integer getDynamicIntegerValue(String[] parameterNames, Object[] args, String key) {
        return getDynamicValue(parameterNames, args, key, Integer.class);
    }

    public static Long getDynamicLongValue(String[] parameterNames, Object[] args, String key) {
        return getDynamicValue(parameterNames, args, key, Long.class);
    }

    public static <T> T getDynamicValue(String[] parameterNames, Object[] args, String key, Class<T> tClass) {
        StandardEvaluationContext context = new StandardEvaluationContext();
        for (int i = 0; i < parameterNames.length; i++) {
            context.setVariable(parameterNames[i], args[i]);
        }
        return PARSER.parseExpression(key).getValue(context, tClass);
    }
}