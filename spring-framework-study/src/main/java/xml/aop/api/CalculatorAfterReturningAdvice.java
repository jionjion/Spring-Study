package xml.aop.api;

import org.springframework.aop.AfterReturningAdvice;

import java.lang.reflect.Method;
import java.util.Arrays;

/**
 * @author Jion
 * 返回通知
 */
public class CalculatorAfterReturningAdvice implements AfterReturningAdvice {

    /**
     * @param returnValue 返回值
     * @param method      调用方法
     * @param args        调用方法时传入参数
     * @param target      调用对象的代理
     * @throws Throwable 各种异常
     */
    @Override
    public void afterReturning(Object returnValue, Method method, Object[] args, Object target) throws Throwable {

        System.out.println("[返回通知:] 对象 " + target.hashCode() + " 的方法" + method.getName()
                                 + " 正常结束执行,参数为:" + Arrays.asList(args) + "结果为: " + returnValue);
    }
}
