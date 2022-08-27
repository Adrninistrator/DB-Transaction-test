package test.db.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Adrninistrator
 * @date 2022/6/16
 * @description:
 */
public class CommonUtil {
    private static final Logger logger = LoggerFactory.getLogger(CommonUtil.class);

    private static final String COMMON_UTIL_CLASS_NAME = CommonUtil.class.getName();

    public static String getObjHashCodeStr(Object obj) {
        return String.format("%x", System.identityHashCode(obj));
    }

    public static String getStackTraceElementString(StackTraceElement stackTraceElement) {
        String fileNameWithOutExt = null;
        if (stackTraceElement.getFileName() != null) {
            int lastPointIndex = stackTraceElement.getFileName().lastIndexOf(".");
            if (lastPointIndex == -1) {
                fileNameWithOutExt = stackTraceElement.getFileName();
            } else {
                fileNameWithOutExt = stackTraceElement.getFileName().substring(0, lastPointIndex);
            }
        }

        return stackTraceElement.getClassName() + "." + stackTraceElement.getMethodName() +
                (stackTraceElement.isNativeMethod() ? "(Native Method)" :
                        (fileNameWithOutExt != null && stackTraceElement.getLineNumber() >= 0 ?
                                "(" + fileNameWithOutExt + ":" + stackTraceElement.getLineNumber() + ")" :
                                (fileNameWithOutExt != null ? "(" + fileNameWithOutExt + ")" : "(Unknown Source)")));
    }

    public static void printStackTrace() {
        if (!PrintStackTraceSwitchUtil.checkSwitch()) {
            return;
        }

        StackTraceElement[] stackTraceElements = Thread.currentThread().getStackTrace();
        StringBuilder stringBuilder = new StringBuilder();
        boolean findCurrentMethod = false;

        for (StackTraceElement stackTraceElement : stackTraceElements) {
            if (!findCurrentMethod) {
                if (COMMON_UTIL_CLASS_NAME.equals(stackTraceElement.getClassName()) && "printStackTrace".equals(stackTraceElement.getMethodName())) {
                    // 对于当前方法，及当前方法调用Thread.currentThread().getStackTrace()方法，均不打印
                    findCurrentMethod = true;
                }
                continue;
            }

            stringBuilder.insert(0, getStackTraceElementString(stackTraceElement) + "\n");
        }

        logger.info("stack trace\n{}", stringBuilder);
    }

    private CommonUtil() {
        throw new IllegalStateException("illegal");
    }
}
