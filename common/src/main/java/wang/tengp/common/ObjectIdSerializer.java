package wang.tengp.common;

import com.alibaba.fastjson.serializer.JSONSerializer;
import com.alibaba.fastjson.serializer.ObjectSerializer;
import com.alibaba.fastjson.serializer.SerializeWriter;
import com.alibaba.fastjson.serializer.SerializerFeature;

import java.io.IOException;
import java.lang.reflect.Type;

/**
 * 自定义 ObjectId 序列化接口
 * Created by shumin on 16/6/16.
 */
public class ObjectIdSerializer implements ObjectSerializer {

    //   注册 ObjectId 序列化
//    static {
//        System.out.println("注册 ObjectId 序列化");
//        SerializeConfig.getGlobalInstance().put(ObjectId.class, ObjectIdSerializer.getInstance());
//    }

    private ObjectIdSerializer() {
    }

    private static class SingletonHolder {
        private static final ObjectIdSerializer INSTANCE = new ObjectIdSerializer();
    }

    public static final ObjectIdSerializer getInstance() {
        return SingletonHolder.INSTANCE;
    }

    @Override
    public void write(JSONSerializer serializer, Object object, Object fieldName, Type fieldType, int features) throws IOException {
        SerializeWriter out = serializer.getWriter();

        if (object == null) {
            if (out.isEnabled(SerializerFeature.WriteNullStringAsEmpty)) {
                out.writeString("");
            } else {
                out.writeNull();
            }
            return;
        }
        out.writeString(object.toString());
    }

}