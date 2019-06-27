package xml.aop.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;

import java.util.Arrays;

/**
 * @author Jion
 *  切面类,各种切面编程
 */
public class LoggingAspect {

    /** 前置通知,方法执行前调用 */
    public void beforeMethod(JoinPoint joinPoint){
        String methodName = joinPoint.getSignature().getName();
        Object [] args = joinPoint.getArgs();

        System.out.println("[前置通知:] 方法 " + methodName + " 开始执行,参数为: " + Arrays.asList(args));
    }

    /** 返回通知,方法顺利执行后调用 */
    public void afterReturning(JoinPoint joinPoint, Object result){
        String methodName = joinPoint.getSignature().getName();
        System.out.println("[返回通知:] 方法 " + methodName + " 正常结束执行,结果为: " + result);
    }

    /** 后置通知,无论方法是否正常执行后调用 */
    public void afterMethod(JoinPoint joinPoint){
        String methodName = joinPoint.getSignature().getName();
        System.out.println("[后置通知] 方法 " + methodName + " 执行结束." );
    }

    /** 异常通知,方法发生异常时调用 */
    public void afterThrowing(JoinPoint joinPoint, Exception e){
        String methodName = joinPoint.getSignature().getName();
        System.out.println("[异常通知:] 方法 " + methodName + " 结束执行,出现异常: " + e.getLocalizedMessage());
    }

    /** 环绕通知 */
    public Object aroundMethod(ProceedingJoinPoint joinPoint){

        Object result = null;
        String methodName = joinPoint.getSignature().getName();

        try {
            // 前置通知
            System.out.println("[环绕前置通知:] 方法 " + methodName + " 开始执行,参数为: " + Arrays.asList(joinPoint.getArgs()));
            // 执行目标方法
            result = joinPoint.proceed();
            // 返回通知
            System.out.println("[环绕返回通知:] 方法 " + methodName + " 正常结束执行,结果为: " + result);
        } catch (Throwable e) {
            // 异常通知
            System.out.println("[环绕异常通知:] 方法 " + methodName + " 结束执行,出现异常: " + e.getLocalizedMessage());
            e.printStackTrace();
        }
        // 后置通知
        System.out.println("[环绕后置通知] 方法 " + methodName + " 执行结束." );
        // 返回值
        return result;
    }
}
