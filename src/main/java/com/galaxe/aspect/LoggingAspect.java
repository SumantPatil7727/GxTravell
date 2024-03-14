package com.galaxe.aspect;

import java.util.Arrays;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Aspect
@Slf4j
@Component
public class LoggingAspect {
  // Around advice for logging method execution
  @Around("execution(* com.galaxe.*.*.*(..))")
  public Object loggingMethodAspect(ProceedingJoinPoint pjp) throws Throwable {
    log.debug(pjp.getSignature() + " is started with arguments " + Arrays.toString(pjp.getArgs()));
    Object retVal = pjp.proceed();
    log.debug(pjp.getSignature() + " is ended");
    return retVal;
  }

  // After Throwing advice for logging exceptions
  @AfterThrowing(value = "execution(* com.galaxe.*.*.*(..))", throwing = "ex")
  public void loggingExceptionAspect(JoinPoint jp, Exception ex) {
    log.error(
        "Some Exception is raised in "
            + jp.getSignature()
            + "with msg:: "
            + ex.getLocalizedMessage());
  }
}
