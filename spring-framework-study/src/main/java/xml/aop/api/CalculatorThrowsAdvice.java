package xml.aop.api;

import org.springframework.aop.ThrowsAdvice;

import java.lang.reflect.Method;
import java.util.Arrays;

/**
 * @author Jion
 *  异常通知
 */
public class CalculatorThrowsAdvice implements ThrowsAdvice {

    public void afterThrowing(Method method, Object[] args, Object target, Exception ex) throws Throwable {
        System.out.println("[异常通知:] 对象 " + target.hashCode() + " 的方法" + method.getName()
                + " 执行异常,参数为:" + Arrays.asList(args) + "异常名称" + ex.getCause());
    }
}
