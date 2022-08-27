package test.db.common;

/**
 * @author adrninistrator
 * @date 2022/7/9
 * @description:
 */
public class CommonConstants {
    public static final String TEST_MODE_KEY = "test.mode";

    public static final String PRINT_STACK_TRACE_KEY = "print.stack.trace";

    public static final String PRINT_STACK_TRACE_ON = "on";
    public static final String PRINT_STACK_TRACE_OFF = "off";

    private CommonConstants() {
        throw new IllegalStateException("illegal");
    }
}
