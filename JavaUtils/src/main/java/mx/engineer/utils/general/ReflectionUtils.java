package mx.engineer.utils.general;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import mx.engineer.utils.general.exceptions.InvalidFieldException;

public final class ReflectionUtils {
    
    private ReflectionUtils() { }

    /*public static Boolean isObjectValuesEqual(final Object expected, final Object given,
            final Boolean isIgnoreNulls) throws IllegalAccessException,
    IllegalArgumentException, InvocationTargetException {
        final List<Method> getMethods = getPrefixedMethods(expected, "get");
        for (Method getMethod : getMethods) {
            if (!(isIgnoreNulls && getMethod.invoke(expected) == null))
                if (!isValueEqual(getMethod.invoke(expected), getMethod.invoke(given)))
                    return false;
        }
        return true;
    }

    public static List<Method> getPrefixedMethods(final Object givenObject, final String methodPrefix) {
        final List<Method> getMethods = new ArrayList<Method>();
        for (Method method : givenObject.getClass().getDeclaredMethods())
            if (method.getName().contains(methodPrefix))
                getMethods.add(method);
        return getMethods;
    }

    private static Boolean isValueEqual(final Object expected, final Object given) {
        return expected.toString().equals(given.toString());
    }*/
    
    public static Object getValue(final Object givenObject, final String fieldName)
            throws InvalidFieldException {
        final String camelCasedFieldName =
                fieldName.substring(0, 1).toUpperCase().concat(fieldName.substring(1));
        try {
            final Method method = givenObject.getClass().getDeclaredMethod("get".concat(camelCasedFieldName));
            return method.invoke(givenObject);
        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException
                | SecurityException getValueException) {
            throw new InvalidFieldException(getValueException);
        }
    }
}
