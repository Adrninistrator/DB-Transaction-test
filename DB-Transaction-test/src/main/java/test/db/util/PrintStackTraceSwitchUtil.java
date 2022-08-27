package test.db.util;

import test.db.common.CommonConstants;

/**
 * @author adrninistrator
 * @date 2022/7/9
 * @description:
 */
public class PrintStackTraceSwitchUtil {
    private static boolean STACK_TRACE_SWITCH = false;

    public static void setSwitchOn() {
        System.setProperty(CommonConstants.PRINT_STACK_TRACE_KEY, CommonConstants.PRINT_STACK_TRACE_ON);
        STACK_TRACE_SWITCH = true;
    }

    public static void setSwitchOff() {
        System.setProperty(CommonConstants.PRINT_STACK_TRACE_KEY, CommonConstants.PRINT_STACK_TRACE_OFF);
        STACK_TRACE_SWITCH = false;
    }

    public static boolean checkSwitch() {
        return STACK_TRACE_SWITCH;
    }

    private PrintStackTraceSwitchUtil() {
        throw new IllegalStateException("illegal");
    }
}
