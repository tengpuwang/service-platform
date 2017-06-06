package wang.tengp.common;

import com.alibaba.fastjson.parser.DefaultJSONParser;
import com.alibaba.fastjson.parser.JSONToken;
import com.alibaba.fastjson.parser.deserializer.ObjectDeserializer;
import com.google.common.base.Strings;
import org.bson.types.ObjectId;

import java.lang.reflect.Type;

/**
 * 自定义 ObjectId 反序列化接口
 * Created by shumin on 16-7-7.
 */
public class ObjectIdDeserializer implements ObjectDeserializer {

    //   注册 ObjectId 反序列化
//    static {
//        System.out.println("注册 ObjectId 反序列化");
//        ParserConfig.getGlobalInstance().putDeserializer(ObjectId.class, ObjectIdDeserializer.getInstance());
//    }

    private ObjectIdDeserializer() {
    }

    private static class SingletonHolder {
        private static final ObjectIdDeserializer INSTANCE = new ObjectIdDeserializer();
    }

    public static final ObjectIdDeserializer getInstance() {
        return SingletonHolder.INSTANCE;
    }

    @Override
    public ObjectId deserialze(DefaultJSONParser defaultJSONParser, Type type, Object fieldName) {
        String strValue = (String) defaultJSONParser.parse(fieldName);
        if (Strings.isNullOrEmpty(strValue)){
            return null;
        }
        return new ObjectId(strValue);

//        throw new JSONException("parse error");
    }

    @Override
    public int getFastMatchToken() {
        return JSONToken.LITERAL_STRING;
    }
}