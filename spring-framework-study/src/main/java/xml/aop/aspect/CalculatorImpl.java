package xml.aop.aspect;

/**
 * @author Jion
 *  代理接口的实现类
 */
public class CalculatorImpl implements Calculator {

    /** 实现除法 */
    @Override
    public int div(int a, int b) {
        return a / b;
    }
}
