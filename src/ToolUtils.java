
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;


/**
 * 补充基础工具类，
 * 包含日期辅助工具类
 *
 * @author ln
 * @version 1.0
 * @date 2020/9/8 11:22
 * @since 1.0
 */
public class ToolUtils<T> {


    /**
     * 获取当前日期 返回字符串格式
     *
     * @return
     */
    public static Integer getNowDateString() {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
        String dateString = formatter.format(new Date());
        return Integer.parseInt(dateString);
    }


    /**
     * 获取当前日期 返回字符串格式
     *
     * @return
     */
    public static String getNowYearString() {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy");
        String dateString = formatter.format(new Date());
        return dateString;
    }


    /**
     * 效验 时间格式（YYYYMM） 对象
     */
    public static boolean isValidYYYYMM(String str) {
        boolean convertSuccess = true;
        // 指定日期格式为四位年/两位月份/两位日期，注意yyyy/MM/dd区分大小写；
        SimpleDateFormat format = new SimpleDateFormat("yyyyMM");
        try {
            // 设置lenient为false. 否则SimpleDateFormat会比较宽松地验证日期，比如2007/02/29会被接受，并转换成2007/03/01
            format.setLenient(false);
            format.parse(str);
        } catch (ParseException e) {
            convertSuccess = false;
        }
        return convertSuccess;
    }

    /**
     * 根据开始时间，结束时间，求出相差集合 (yyyyMM) ln
     * 测试入参 202001 ,202003 返回集合：202001 202002 202003
     *
     * @throws ParseException
     */
    public static List<Integer> getMonthsBetween(Integer minDate, Integer maxDate) {
        String minDateStr = minDate.toString();
        String maxDateStr = maxDate.toString();
        ArrayList<Integer> result = new ArrayList<>();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMM");
        Calendar min = Calendar.getInstance();
        Calendar max = Calendar.getInstance();
        try {
            min.setTime(sdf.parse(minDateStr));
            min.set(min.get(Calendar.YEAR), min.get(Calendar.MONTH), 1);
            max.setTime(sdf.parse(maxDateStr));
            max.set(max.get(Calendar.YEAR), max.get(Calendar.MONTH), 2);
            Calendar curr = min;
            while (curr.before(max)) {
                result.add(Integer.parseInt(sdf.format(curr.getTime())));
                curr.add(Calendar.MONTH, 1);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return result;
    }

    public static List<Integer> getYearsBetween(Integer minDate, Integer maxDate) {
        String minDateStr = minDate.toString();
        String maxDateStr = maxDate.toString();
        ArrayList<Integer> result = new ArrayList<>();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMM");
        Calendar min = Calendar.getInstance();
        Calendar max = Calendar.getInstance();
        try {
            min.setTime(sdf.parse(minDateStr));
            min.set(min.get(Calendar.YEAR), min.get(Calendar.MONTH), 1);
            max.setTime(sdf.parse(maxDateStr));
            max.set(max.get(Calendar.YEAR), max.get(Calendar.MONTH), 2);
            Calendar curr = min;
            result.add(Integer.parseInt(sdf.format(curr.getTime())));
            while (curr.before(max)) {
                curr.add(Calendar.MONTH, 1);
                if (curr.get(Calendar.MONTH) == Calendar.DECEMBER || curr.get(Calendar.MONTH) == Calendar.JANUARY) {
                    result.add(Integer.parseInt(sdf.format(curr.getTime())));
                }
            }
        } catch (ParseException e) {

            e.printStackTrace();
        }
        return result;
    }


    public static Integer getYearByDate(Integer dateString) {
        return Integer.parseInt(String.valueOf(dateString).substring(0, 4));
    }


    /**
     * 获取四舍五入后的数据
     *
     * @param source
     * @return
     */
    public static BigDecimal getRoundBidDecimal(BigDecimal source) {
        return source.setScale(2, BigDecimal.ROUND_CEILING);
    }


    /**
     * 求相差月份
     * 测试： 202009  202008  结果 2个月
     *
     * @param
     * @return
     */
    public static Integer getDiffBetween(Integer start, Integer end) {
        String startYm = String.valueOf(start);
        String endYm = String.valueOf(end);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMM");
        Calendar bef = Calendar.getInstance();
        Calendar aft = Calendar.getInstance();
        try {
            bef.setTime(sdf.parse(startYm));
            aft.setTime(sdf.parse(endYm));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        int result = aft.get(Calendar.MONTH) - bef.get(Calendar.MONTH);
        int month = (aft.get(Calendar.YEAR) - bef.get(Calendar.YEAR)) * 12;
        return Math.abs(month + result) + 1;
    }


    public static String getPreYearLast(String year) {
        Calendar curr = Calendar.getInstance();
        curr.clear();
        curr.set(Calendar.YEAR, Integer.parseInt(year) - 1);
        curr.roll(Calendar.DAY_OF_YEAR, -1);
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
        return format.format(curr.getTime());
    }

    /**
     * 获取上衣年度
     *
     * @param year
     * @return
     */
    public static String getPreYear(String year) {
        Calendar curr = Calendar.getInstance();
        curr.clear();
        curr.set(Calendar.YEAR, Integer.parseInt(year) - 1);
        curr.roll(Calendar.DAY_OF_YEAR, -1);
        SimpleDateFormat format = new SimpleDateFormat("yyyy");
        return format.format(curr.getTime());
    }


//    public static <E> Object mapToModel(Map<String, Object> map, Class<E> cls) {
//        Set<String> set = map.keySet();
//        E e = null;
//        try {
//            e = cls.newInstance();
//            for (String s : set) {
//                if (!ObjectUtils.isEmpty(map.get(s)) && !"null".equals(map.get(s)) && !""
//                        .equals(map.get(s))) {
//                    Field field = cls.getDeclaredField(s);
//                    field.setAccessible(true);
//                    if ("java.lang.Long".equals(field.getType().getName())) {
//                        field.set(e, Long.valueOf(map.get(s).toString()).longValue());
//                    } else if ("java.math.BigDecimal".equals(field.getType().getName())) {
//                        field.set(e, new BigDecimal(map.get(s).toString()));
//                    } else {
//                        field.set(e, map.get(s));
//                    }
//                }
//            }
//        } catch (Exception ex) {
//            System.out.println(ex.getMessage());
//        }
//        return e;
//    }

    public static String getDate(String nowDate) {
        return (Integer.valueOf(nowDate.substring(0, 4)) - 60) + nowDate.substring(4);
    }


    /**
     * 截取list集合，返回list集合
     *
     * @param tList  (需要截取的集合)
     * @param subNum (每次截取的数量)
     * @return
     */
    public static <T> List<List<T>> subList(List<T> tList, Integer subNum) {
        // 新的截取到的list集合
        List<List<T>> tNewList = new ArrayList<List<T>>();
        // 要截取的下标上限
        Integer priIndex = 0;
        // 要截取的下标下限
        Integer lastIndex = 0;
        // 每次插入list的数量
        // Integer subNum = 500;
        // 查询出来list的总数目
        Integer totalNum = tList.size();
        // 总共需要插入的次数
        Integer insertTimes = totalNum / subNum;
        List<T> subNewList = new ArrayList<T>();
        for (int i = 0; i <= insertTimes; i++) {
            // [0--20) [20 --40) [40---60) [60---80) [80---100)
            priIndex = subNum * i;
            lastIndex = priIndex + subNum;
            // 判断是否是最后一次
            if (i == insertTimes) {
                subNewList = tList.subList(priIndex, tList.size());
            } else {
                // 非最后一次
                subNewList = tList.subList(priIndex, lastIndex);

            }
            if (subNewList.size() > 0) {
                tNewList.add(subNewList);
            }
        }
        return tNewList;
    }


    public static Integer getNowYear() {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy");
        String dateString = formatter.format(new Date());
        return Integer.parseInt(dateString);
    }

    /**
     * 反射机制 返回单条记录 T
     *
     * @param resultSet
     * @param clazz
     * @return
     * @throws Exception
     */
    public static <T> T selectSimpleResult(ResultSet resultSet, Class<T> clazz) throws Exception {
        T resultObject = null;
        ResultSetMetaData metaData = resultSet.getMetaData();
        int couLength = metaData.getColumnCount();
        while (resultSet.next()) {
            // 通过反射的到一个泛型对象
            resultObject = clazz.newInstance();
            for (int i = 0; i < couLength; i++) {
                String colName = metaData.getColumnName(i + 1);
                Object colValue = resultSet.getObject(colName);
                if (colValue == null) {
                    colValue = "";
                }
                Field field = clazz.getDeclaredField(colName);
                field.setAccessible(true); // 取消Java检查机制 打开访问权限
                field.set(resultObject, colValue);
            }
        }
        return resultObject;
    }

    /**
     * jdbc查询结果集转换成map
     *
     * @param rs
     * @param rsMeta
     * @return
     * @throws Exception
     */
    public static Map<String, Object> JdbcResultToMap(ResultSet rs, ResultSetMetaData rsMeta) throws Exception {
        Map<String, Object> row = new HashMap<>();
        int i = 0;
        for (int size = rsMeta.getColumnCount(); i < size; ++i) {
            String columName = rsMeta.getColumnLabel(i + 1);
            Object value = rs.getObject(i + 1);
            row.put(columName.toLowerCase(), value);
        }
        return row;
    }


    /**
     * 拼接sql字符串
     *
     * @param list
     * @return
     */
    public static StringBuffer SplicingSqlStr(List<String> list) {
        StringBuffer stringBuffer = new StringBuffer();
        for (int i = 0; i < list.size(); i++) {
            stringBuffer.append("?, ");
        }
        stringBuffer.deleteCharAt(stringBuffer.length() - 1);
        stringBuffer.deleteCharAt(stringBuffer.length() - 1);
        return stringBuffer;
    }


}
