package wang.tengp.resources;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import wang.tengp.common.util.ApplicationConextUtils;
import wang.tengp.core.annotation.Resource;

import java.util.Map;

import static spark.Spark.get;
import static spark.Spark.put;

/**
 * 系统配置
 * Created by shumin on 16-11-3.
 */
@Resource
public class Profiles {

    static {
        init();
    }

    private synchronized static void init() {

        /**
         * 获取系统配置
         */
        get("/system/profiles", (request, response) -> {
            String collection_name = "system-profile";
            MongoTemplate mongoTemplate = (MongoTemplate) ApplicationConextUtils.getBean("mongoTemplate");
            Map profiles = mongoTemplate.findOne(new Query(), Map.class, collection_name);
            return profiles;
        }, JSON::toJSONString);

        /**
         * 修改系统配置
         */
        put("/system/profiles", (request, response) -> {
            String collection_name = "system-profile";
            String request_playload = request.body();
            JSONObject object = JSON.parseObject(request_playload);
            MongoTemplate mongoTemplate = (MongoTemplate) ApplicationConextUtils.getBean("mongoTemplate");
            Query query = new Query();
            mongoTemplate.findAndRemove(query, Map.class, collection_name);
            if (object.get("_id") != null) {
                String id = object.getString("_id");
                object.put("_id", new ObjectId(id));
            }
            mongoTemplate.save(object, collection_name);
            response.status(201);
            return object;
        }, JSON::toJSONString);
    }
}