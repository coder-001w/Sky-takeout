package com.sky.aspect;

import com.sky.annotation.AutoFill;
import com.sky.constant.AutoFillConstant;
import com.sky.context.BaseContext;
import com.sky.enumeration.OperationType;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.time.LocalDateTime;

@Aspect
@Component
@Slf4j
public class AutoFillAspect {
    /**
     * 切入点
     */
    @Pointcut("execution(* com.sky.mapper.*.*(..)) && @annotation(com.sky.annotation.AutoFill)")
    public void autoFillPointcut(){}

    @Before("autoFillPointcut()")
    public void before(JoinPoint joinPoint) {
        log.info("自动填充切面执行");
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        AutoFill autoFill = signature.getMethod().getAnnotation(AutoFill.class);
        OperationType operationType = autoFill.value();

        Object[] objects = joinPoint.getArgs();
        if (objects.length == 0 && objects == null){
            return;
        }
        Object object = objects[0];
        if (operationType == OperationType.INSERT) {
            //新增操作
            try {
                Method setCreateTime = object.getClass().getMethod(AutoFillConstant.SET_CREATE_TIME, LocalDateTime.class);
                Method setUpdateTime = object.getClass().getMethod(AutoFillConstant.SET_UPDATE_TIME, LocalDateTime.class);
                Method setCreateUser = object.getClass().getMethod(AutoFillConstant.SET_CREATE_USER, Long.class);
                Method setUpdateUser = object.getClass().getMethod(AutoFillConstant.SET_UPDATE_USER, Long.class);
                setCreateTime.invoke(object, LocalDateTime.now());
                setUpdateTime.invoke(object, LocalDateTime.now());
                setCreateUser.invoke(object, BaseContext.getCurrentId());
                setUpdateUser.invoke(object, BaseContext.getCurrentId());
            } catch (Exception e) {
                log.error("自动填充失败", e);
            }
        } else if (operationType == OperationType.UPDATE) {
            //修改操作
            try {
                Method setUpdateTime = object.getClass().getMethod(AutoFillConstant.SET_UPDATE_TIME, LocalDateTime.class);
                Method setUpdateUser = object.getClass().getMethod(AutoFillConstant.SET_UPDATE_USER, Long.class);
                setUpdateTime.invoke(object, LocalDateTime.now());
                setUpdateUser.invoke(object, BaseContext.getCurrentId());
            } catch (Exception e) {
                log.error("自动填充失败", e);
            }
        }


    }
}
