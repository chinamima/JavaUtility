package com.gjj.java.utility;


import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * Created by guojinjun on 2018/06/12.
 */
public class ReflectUtil {


    public static Object getFieldValue(final Object object, final String fieldName, final Class<?> fieldClass) {
        Field field = getField(object.getClass(), fieldName, fieldClass);
        if (field == null) {
            return null;
//            throw new IllegalArgumentException("Could not find field [" + fieldName + "] on target [" + object + "]");
        }
        makeAccessible(field);
        Object result = null;
        try {
            result = field.get(object);
        } catch (IllegalAccessException e) {
            LogUtil.e("不可能抛出的异常{}", e);
        }
        return result;
    }

    private static Field getField(Class<?> clazz, String fieldName, Class<?> fieldClass) {
        Field found = null;
        for (Class superClass = clazz; superClass != Object.class; superClass = superClass.getSuperclass()) {
            Field[] fields = superClass.getDeclaredFields();
            for (int i = 0; i < fields.length; i++) {
                String name = fields[i].getName();
                if (!name.equals(fieldName)) {
                    continue;
                }
                String type = fields[i].getType().getName();
                if (!type.equals(fieldClass.getName())) {
                    continue;
                }
                found = fields[i];
                break;
            }
            if (found != null) {
                break;
            }
        }
        return found;
    }

    /**
     * 直接读取对象属性值,无视private/protected修饰符,不经过getter函数.
     */
    public static Object getFieldValue(final Object object, final String fieldName) {
        Field field = getDeclaredField(object, fieldName);
        if (field == null) {
            return null;
//            throw new IllegalArgumentException("Could not find field [" + fieldName + "] on target [" + object + "]");
        }
        makeAccessible(field);
        Object result = null;
        try {
            result = field.get(object);
        } catch (IllegalAccessException e) {
            LogUtil.e("不可能抛出的异常{}", e);
        }
        return result;
    }

    /**
     * 直接设置对象属性值,无视private/protected修饰符,不经过setter函数.
     */
    public static void setFieldValue(final Object object, final String fieldName, final Object value) {
        Field field = getDeclaredField(object, fieldName);
        if (field == null) {
            return;
//            throw new IllegalArgumentException("Could not find field [" + fieldName + "] on target [" + object + "]");
        }
        makeAccessible(field);
        try {
            field.set(object, value);
        } catch (IllegalAccessException e) {
            LogUtil.e("不可能抛出的异常:{}", e);
        }
    }

    /**
     * 循环向上转型,获取对象的DeclaredField.
     */
    protected static Field getDeclaredField(final Object object, final String fieldName) {
        return getDeclaredField(object.getClass(), fieldName);
    }

    /**
     * 循环向上转型,获取类的DeclaredField.
     */
    @SuppressWarnings("unchecked")
    protected static Field getDeclaredField(final Class clazz, final String fieldName) {
        for (Class superClass = clazz; superClass != Object.class; superClass = superClass.getSuperclass()) {
            try {
                return superClass.getDeclaredField(fieldName);
            } catch (NoSuchFieldException e) {
                // Field不在当前类定义,继续向上转型
            }
        }
        return null;
    }

    /**
     * 强制转换field可访问.
     */
    protected static void makeAccessible(final Field field) {
        if (!Modifier.isPublic(field.getModifiers()) || !Modifier.isPublic(field.getDeclaringClass().getModifiers())) {
            field.setAccessible(true);
        }
    }

    /**
     * 通过反射,获得定义Class时声明的父类的泛型参数的类型. 如public UserDao extends HibernateDao<User>
     *
     * @param clazz The class to introspect
     * @return the first generic declaration, or Object.class if cannot be
     * determined
     */
    @SuppressWarnings("unchecked")
    public static Class getSuperClassGenericType(final Class clazz) {
        return getSuperClassGenericType(clazz, 0);
    }

    /**
     * 通过反射,获得定义Class时声明的父类的泛型参数的类型. 如public UserDao extends
     * HibernateDao<User,Long>
     *
     * @param clazz clazz The class to introspect
     * @param index the Index of the generic ddeclaration,start from 0.
     * @return the index generic declaration, or Object.class if cannot be
     * determined
     */
    @SuppressWarnings("unchecked")
    public static Class getSuperClassGenericType(final Class clazz, final int index) {
        Type genType = clazz.getGenericSuperclass();
        if (!(genType instanceof ParameterizedType)) {
            LogUtil.w(clazz.getSimpleName() + "'s superclass not ParameterizedType");
            return Object.class;
        }
        Type[] params = ((ParameterizedType) genType).getActualTypeArguments();
        if (index >= params.length || index < 0) {
            LogUtil.w("Index: " + index + ", Size of " + clazz.getSimpleName() + "'s Parameterized Type: " + params.length);
            return Object.class;
        }
        if (!(params[index] instanceof Class)) {
            LogUtil.w(clazz.getSimpleName() + " not set the actual class on superclass generic parameter");
            return Object.class;
        }
        return (Class) params[index];
    }

    public static Method getMethod(Class<?> clazz, String methodName, Class<?>[] parameterTypes) {
        try {
            Method method = clazz.getDeclaredMethod(methodName, parameterTypes);
            return method;
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 使用反射调用方法
     *
     * @param o          被调用对象
     * @param methodName 被调用对象的方法名称
     * @param args       被调用方法所需传入的参数列表
     */
    public static Object callMethod(Object o, String methodName, Object... args) {
        Object result = null;
        try {
            Class c = o.getClass();
            Method m = getMethod(c, methodName, args);
            result = m.invoke(o, args);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return result;
    }

    public static Object callStaticMethod(String className, String methodName, Object... args) {
        return callStaticMethod(className.getClass().getClassLoader(), className, methodName, args);
    }

    public static Object callStaticMethod(ClassLoader classLoader, String className, String methodName, Object... args) {
        return callStaticMethod(classLoader, className, methodName, null, args);
    }


    public static Object callStaticMethod(ClassLoader classLoader, String className, String methodName, Class<?> clazzReturn, Object... args) {
        try {
            Class clazz = classLoader.loadClass(className);
            Method m = getMethod(clazz, methodName, clazzReturn, args);
            if (m == null) {
                return null;
            }
            Object result = m.invoke(null, args);
            return result;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Method getMethod(Class c, String methodName, Object[] args) throws NoSuchMethodException {
        return getMethod(c, methodName, null, args);
    }

    public static Method getMethod(Class c, String methodName, Class<?> clazzReturn, Object[] args) {
        Method m = null;
        Class<?>[] clazzArgs = getParameterTypes(args);
        Method[] methodes = c.getDeclaredMethods();
        for (Method method : methodes) {
            Class<?>[] parameterTypes = method.getParameterTypes();
            if (!method.getName().equals(methodName) || parameterTypes.length != args.length) {
                continue;
            }
            boolean match = true;
            for (int i = 0; i < args.length; i++) {
                if (clazzArgs[i] == parameterTypes[i]) {
                    continue;
                }
                match = false;
                break;
            }
            if (match) {
                Class<?> returnType = method.getReturnType();
                if (clazzReturn != null && !returnType.getCanonicalName().equals(clazzReturn.getCanonicalName())) {
                    continue;
                }
                m = method;
                m.setAccessible(true);
                break;
            }
        }
        return m;
    }



    protected static Class<?>[] getParameterTypes(Object[] args) {
        Class<?> argsTypes[] = new Class<?>[args.length];
        //基本类型在参数传递过程中会自动封住成对象类型，这里还原成基本类型的Class
        for (int i = 0; i < args.length; i++) {
            argsTypes[i] = args[i].getClass();
            if (argsTypes[i] == Integer.class) {
                argsTypes[i] = int.class;
            }
            if (argsTypes[i] == Float.class) {
                argsTypes[i] = float.class;
            }
            if (argsTypes[i] == Long.class) {
                argsTypes[i] = long.class;
            }
            if (argsTypes[i] == Byte.class) {
                argsTypes[i] = byte.class;
            }
            if (argsTypes[i] == Double.class) {
                argsTypes[i] = double.class;
            }
            if (argsTypes[i] == Boolean.class) {
                argsTypes[i] = boolean.class;
            }
        }
        return argsTypes;
    }

    /**
     * 使用反射设置变量值
     *
     * @param target    被调用对象
     * @param fieldName 被调用对象的字段，一般是成员变量或静态变量，不可是常量！
     * @param value     值
     * @param <T>       value类型，泛型
     */
    public static <T> void setValue(Object target, String fieldName, T value) {
        try {
            Class c = target.getClass();
            Field f = c.getDeclaredField(fieldName);
            f.setAccessible(true);
            f.set(target, value);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    /**
     * 使用反射获取变量值
     *
     * @param target    被调用对象
     * @param fieldName 被调用对象的字段，一般是成员变量或静态变量，不可以是常量
     * @param <T>       返回类型，泛型
     * @return 值
     */
    public static <T> T getValue(Object target, String fieldName) {
        T value = null;
        try {
            Class c = target.getClass();
            Field f = c.getDeclaredField(fieldName);
            f.setAccessible(true);
            value = (T) f.get(target);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return value;
    }
}
