package wang.tengp.common.util;

import org.apache.commons.beanutils.PropertyUtils;

import java.util.Iterator;
import java.util.Locale;
import java.util.Map;

/**
 * Created by shumin on 16-10-27.
 */
public class BeanUtils extends org.apache.commons.beanutils.BeanUtils {

    /**
     * 将源对象中的值覆盖到目标对象中，仅覆盖源对象中不为NULL值的属性
     *
     * @param dest 目标对象，标准的JavaBean
     * @param orig 源对象，可为Map、标准的JavaBean
     */
    @SuppressWarnings("rawtypes")
    public static void applyIf(Object dest, Object orig) throws Exception {
        try {
            if (orig instanceof Map) {
                Iterator names = ((Map) orig).keySet().iterator();
                while (names.hasNext()) {
                    String name = (String) names.next();
                    if (PropertyUtils.isWriteable(dest, name)) {
                        Object value = ((Map) orig).get(name);
                        if (value != null) {
                            PropertyUtils.setSimpleProperty(dest, name, value);
                        }
                    }
                }
            } else {
                java.lang.reflect.Field[] fields = orig.getClass().getDeclaredFields();
                for (int i = 0; i < fields.length; i++) {
                    String name = fields[i].getName();
                    if (PropertyUtils.isReadable(orig, name) && PropertyUtils.isWriteable(dest, name)) {
                        Object value = PropertyUtils.getSimpleProperty(orig, name);
                        if (value != null) {
                            PropertyUtils.setSimpleProperty(dest, name, value);
                        }
                    }
                }
            }
        } catch (Exception e) {
            throw new Exception("将源对象中的值覆盖到目标对象中，仅覆盖源对象中不为NULL值的属性", e);
        }
    }

    /**
     * 将源对象中的值覆盖到目标对象中，仅覆盖源对象中不为NULL值的属性
     *
     * @param orig 源对象，标准的JavaBean
     * @param dest 排除检查的属性，Map
     */
    @SuppressWarnings("rawtypes")
    public static boolean checkObjProperty(Object orig, Map dest) throws Exception {
        try {
            java.lang.reflect.Field[] fields = orig.getClass().getDeclaredFields();
            for (int i = 0; i < fields.length; i++) {
                String name = fields[i].getName();
                if (!dest.containsKey(name)) {
                    if (PropertyUtils.isReadable(orig, name)) {
                        Object value = PropertyUtils.getSimpleProperty(orig, name);
                        if (value == null) {
                            return true;
                        }
                    }
                }
            }
            return false;
        } catch (Exception e) {
            throw new Exception("将源对象中的值覆盖到目标对象中，仅覆盖源对象中不为NULL值的属性", e);
        }
    }

    private static final char SEPARATOR = '_';

    /**
     * 将属性样式字符串转成驼峰样式字符串<br>
     * (例:branchNo -> branch_no)<br>
     *
     * @param inputString
     * @return
     */
    public static String toUnderlineString(String inputString) {
        if (inputString == null)
            return null;
        StringBuilder sb = new StringBuilder();
        boolean upperCase = false;
        for (int i = 0; i < inputString.length(); i++) {
            char c = inputString.charAt(i);

            boolean nextUpperCase = true;

            if (i < (inputString.length() - 1)) {
                nextUpperCase = Character.isUpperCase(inputString.charAt(i + 1));
            }

            if ((i >= 0) && Character.isUpperCase(c)) {
                if (!upperCase || !nextUpperCase) {
                    if (i > 0)
                        sb.append(SEPARATOR);
                }
                upperCase = true;
            } else {
                upperCase = false;
            }

            sb.append(Character.toLowerCase(c));
        }

        return sb.toString();
    }

    /**
     * 将驼峰字段转成属性字符串<br>
     * (例:branch_no -> branchNo )<br>
     *
     * @param inputString 输入字符串
     * @return
     */
    public static String toCamelCaseString(String inputString) {
        return toCamelCaseString(inputString, false);
    }

    /**
     * 将驼峰字段转成属性字符串<br>
     * (例:branch_no -> branchNo )<br>
     *
     * @param inputString             输入字符串
     * @param firstCharacterUppercase 是否首字母大写
     * @return
     */
    public static String toCamelCaseString(String inputString, boolean firstCharacterUppercase) {
        if (inputString == null)
            return null;
        StringBuilder sb = new StringBuilder();
        boolean nextUpperCase = false;
        for (int i = 0; i < inputString.length(); i++) {
            char c = inputString.charAt(i);

            switch (c) {
                case '_':
                case '-':
                case '@':
                case '$':
                case '#':
                case ' ':
                case '/':
                case '&':
                    if (sb.length() > 0) {
                        nextUpperCase = true;
                    }
                    break;

                default:
                    if (nextUpperCase) {
                        sb.append(Character.toUpperCase(c));
                        nextUpperCase = false;
                    } else {
                        sb.append(Character.toLowerCase(c));
                    }
                    break;
            }
        }

        if (firstCharacterUppercase) {
            sb.setCharAt(0, Character.toUpperCase(sb.charAt(0)));
        }

        return sb.toString();
    }

    /**
     * 得到标准字段名称
     *
     * @param inputString 输入字符串
     * @return
     */
    public static String getValidPropertyName(String inputString) {
        String answer;
        if (inputString == null) {
            answer = null;
        } else if (inputString.length() < 2) {
            answer = inputString.toLowerCase(Locale.US);
        } else {
            if (Character.isUpperCase(inputString.charAt(0)) && !Character.isUpperCase(inputString.charAt(1))) {
                answer = inputString.substring(0, 1).toLowerCase(Locale.US) + inputString.substring(1);
            } else {
                answer = inputString;
            }
        }
        return answer;
    }

    /**
     * 将属性转换成标准set方法名字符串<br>
     *
     * @param property
     * @return
     */
    public static String getSetterMethodName(String property) {
        StringBuilder sb = new StringBuilder();

        sb.append(property);
        if (Character.isLowerCase(sb.charAt(0))) {
            if (sb.length() == 1 || !Character.isUpperCase(sb.charAt(1))) {
                sb.setCharAt(0, Character.toUpperCase(sb.charAt(0)));
            }
        }
        sb.insert(0, "set");
        return sb.toString();
    }

    /**
     * 将属性转换成标准get方法名字符串<br>
     *
     * @param property
     * @return
     */
    public static String getGetterMethodName(String property) {
        StringBuilder sb = new StringBuilder();

        sb.append(property);
        if (Character.isLowerCase(sb.charAt(0))) {
            if (sb.length() == 1 || !Character.isUpperCase(sb.charAt(1))) {
                sb.setCharAt(0, Character.toUpperCase(sb.charAt(0)));
            }
        }
        sb.insert(0, "get");
        return sb.toString();
    }

    public static void main(String[] args) {
        System.out.println(BeanUtils.toUnderlineString("ISOCertifiedStaff"));
        System.out.println(BeanUtils.getValidPropertyName("CertifiedStaff"));
        System.out.println(BeanUtils.getSetterMethodName("userID"));
        System.out.println(BeanUtils.getGetterMethodName("userID"));
        System.out.println(BeanUtils.toCamelCaseString("iso_certified_staff", true));
        System.out.println(BeanUtils.getValidPropertyName("certified_staff"));
        System.out.println(BeanUtils.toCamelCaseString("site_Id"));
    }

}