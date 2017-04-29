package org.suze.util;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.util.UUID;

import com.alibaba.fastjson.JSON;


/**
 * Description: 字符串工具类<br>
 * @version V1.0  2017/4/29 17:45  by 石冬冬-Seig Heil（dd.shi02@zuche.com）创建
 */
public final class StringTools {
	/**
	 * 字符串分隔符
	 */
	public static final String SYMBOL_SPLITOR = ",";
	public static final String SYMBOL_ELLIPSIS = "...";
	public static final int MAX_LENGTH = 1000;
    /**
     * 获取异常打印信息
     *
     * @param e
     * @return
     */
    public static String toString(Throwable e) {
        StringWriter sw = new StringWriter(1024);
        e.printStackTrace(new PrintWriter(sw));
        return sw.getBuffer().toString();
    }

    /**
     * build如果已经有数据则添加 逗号
     *
     * @param builder
     * @param value
     */
    public static void appendAndPreComma(StringBuilder builder, String value) {
        if (builder.length() > 1) {
            builder.append(",");
        }
        builder.append(value);
    }
    
    /**
     * build如果字符添加一个 逗号，用于多条件SQL查询使用
     * @param builder
     * @param value
     */
    public static void appendAndPreSplit(StringBuilder builder, String value) {
    	if(isNotEmpty(value)){
    		builder.append(value).append(",");
    	}
    }

    public static String formatJSON(String template, Object... objects) {
        return format(template, FormatType.JSON, objects);
    }

    public static String formatString(String template, Object... objects) {
        return format(template, FormatType.STRING, objects);
    }

    /**
     * 针对模板内容和后续参数进行格式化
     *
     * @param template
     * @param objects
     * @return
     */
    public static String format(String template, FormatType type, Object... objects) {
        StringBuilder builder = new StringBuilder(template.length() * 2);
        for (int i = 0; i < template.length(); i++) {
            char c = template.charAt(i);
            int next = i + 1;
            //如果是特殊字符并且后续还有数值那么直接输出后面的字符 并且索引向后+1
            if (c == '\\' && next < template.length()) {
                char nextC = template.charAt(next);
                builder.append(nextC);
                i = next;
                continue;
            } else if (c == '[') {//如果是遇到[ 开始那么则是需要进行后续参数的传入
                int index = template.indexOf(']', next);//必须后面有] 才是有值的范围
                if (index != -1) {
                    String temp = template.substring(next, index);
                    Integer objIndex = NumberUtil.parseInt(temp);
                    if (objIndex != null) {
                        builder.append(toString(objIndex - 1, type, objects));
                        i = index;
                        continue;
                    }
                }
            }
            builder.append(c);
        }
        return builder.toString();
    }

    public static boolean isNotEmpty(String s) {
        return s != null && s.length() > 0;
    }

    public static boolean isEmpty(String s) {
        return !isNotEmpty(s);
    }
    public static String valueOf(Object o){
    	return null==o?null:o.toString();
    }

    public static String valueOf(Object o,Object defaultValue){
        return null == o || isEmpty(o.toString()) ? defaultValue.toString() : o.toString();
    }

    private static String toString(int i, FormatType type, Object... objects) {
        String s = null;
        if (objects != null && i >= 0 && i < objects.length) {
            if (type == FormatType.JSON) {
                s = JSON.toJSONString(objects[i]);
            } else if (type == FormatType.STRING) {
                s = String.valueOf(objects[i]);
            }
        }
        return String.valueOf(s);
    }
    /**
     * Description:删除字符串最后一个分隔符 
     * @Version1.0 2016-3-17 上午8:42:01 by 石冬冬-Chris Suk（dd.shi02@zuche.com）创建
     * @param v 带分隔符的字符串
     * @param s 分隔符
     * @return
     */
    public static String removeLastSymb(String v,String s){
    	if(isNotEmpty(v)){
    		if(v.endsWith(s)){
				v = v.substring(0, v.length()-1);
			}
    		return v;
    	}else{
    		return null;
    	}
    }
    
    public static String removeLastSymb(String v){
    	return removeLastSymb(v, SYMBOL_SPLITOR);
    }
    /**
     * Description:产生32位ID 
     * @Version1.0 2016-3-31 下午7:37:32 by 石冬冬-Chris Suk（dd.shi02@zuche.com）创建
     * @return
     */
    public static String getUUID(){
    	return UUID.randomUUID().toString().replace("-", "");
    }
    /**
     * Description:截断字符串拼加省略号 
     * @Version1.0 2016-4-11 上午10:48:54 by 石冬冬-Chris Suk（dd.shi02@zuche.com）创建
     * @param value 待处理字符串
     * @param length 保留字符串最大长度
     * @return
     */
    public static String truncate(String value,int length){
    	if(isNotEmpty(value)){
    		if(value.length()>length){
    			value = value.substring(0, length).concat(SYMBOL_ELLIPSIS);
    		}
    		return value;
    	}else{
    		return null;
    	}
    }
    public static String getNotNullString(Object obj){
        if(null == obj){
            return "";
        }
        return obj.toString();
    }

    /**
     * 过滤emoji表情
     * 本方法请教于架构-冯东宝
     * @param text
     * @return 过滤后的字符串
     * @throws UnsupportedEncodingException
     */
    public static  String filterOffUtf8Mb4(String text) throws UnsupportedEncodingException {
        if(isEmpty(text)){
            return null;
        }
        byte[] bytes = text.getBytes("utf-8");
        ByteBuffer buffer = ByteBuffer.allocate(bytes.length);
        int i = 0;
        while (i < bytes.length) {
            short b = bytes[i];
            if (b > 0) {
                buffer.put(bytes[i++]);
                continue;
            }
            b += 256;
            if ((b ^ 0xC0) >> 4 == 0) {
                buffer.put(bytes, i, 2);
                i += 2;
            }
            else if ((b ^ 0xE0) >> 4 == 0) {
                buffer.put(bytes, i, 3);
                i += 3;
            }
            else if ((b ^ 0xF0) >> 4 == 0) {
                i += 4;
            }else{
                i++;
            }
        }
        buffer.flip();
        String str=new String(buffer.array(), "utf-8");
        buffer.clear();
        return str;
    }


    public static enum FormatType {
        JSON, STRING;
    }

    private StringTools() {

    }
    
    public static void main(String[] args) {
		System.out.println(truncate("年号放大范德萨范德萨分士大夫倒萨", 3));
	}
}
