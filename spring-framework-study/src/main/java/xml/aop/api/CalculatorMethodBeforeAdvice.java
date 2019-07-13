package xml.aop.api;

import org.springframework.aop.MethodBeforeAdvice;

import java.lang.reflect.Method;
import java.util.Arrays;

/**
 * @author Jion
 *  前置通知
 */
public class CalculatorMethodBeforeAdvice implements MethodBeforeAdvice {

    @Override
    public void before(Method method, Object[] args, Object target) throws Throwable {

        System.out.println("[返回通知:] 对象 " + target.hashCode() + " 的方法" + method.getName()
                + " 正常结束执行,参数为:" + Arrays.asList(args) );

    }
}