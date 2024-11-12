package rahulstech.jfx.balancesheet.core.util;

import java.lang.reflect.Constructor;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ReflectionUtil {

    private ReflectionUtil() {}

    public static Object newInstance(Class<?> clazz, Object[] args) {
        Constructor<?> constructor = findSuitableConstructor(clazz,args);
        if (null == constructor) {
            throw new IllegalArgumentException("no suitable constructor found for class "+clazz.getName()+
                    " with argument types ["+toClassNameListString(args)+"]");
        }
        try {
            return constructor.newInstance(args);
        }
        catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    private static Constructor<?> findSuitableConstructor(Class<?> clazz, Object[] args) {
        int aCount = args.length;
        Constructor<?>[] constructors = clazz.getDeclaredConstructors();
        for (Constructor<?> ctor : constructors) {
            int pCount = ctor.getParameterCount();
            if (aCount != pCount) {
                continue;
            }

            Class<?>[] pTypes = ctor.getParameterTypes();
            boolean found = true;
            for (int i = 0; i < pCount; i++) {
                Object arg = args[i];
                if (null == arg) {
                    continue;
                }
                Class<?> pType = pTypes[i];
                Class<?> aType = arg.getClass();
                if (!pType.isAssignableFrom(aType)) {
                    found = false;
                    break;
                }
            }

            if (found) {
                return ctor;
            }
        }
        return null;
    }

    private static String toClassNameListString(Object[] array) {
        return Stream.of(array).map(o -> null == o ? null : o.getClass().getName()).collect(Collectors.joining(","));
    }
}
