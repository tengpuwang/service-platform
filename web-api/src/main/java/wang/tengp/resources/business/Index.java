package wang.tengp.resources.business;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.mongodb.BasicDBList;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.DBRef;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import wang.tengp.common.InfoConverter;
import wang.tengp.common.util.ApplicationConextUtils;
import wang.tengp.core.annotation.Resource;
import wang.tengp.model.Info;

import java.util.List;
import java.util.Map;

import static spark.Spark.get;

/**
 * Created by shumin on 2017/3/18.
 */
@Resource
public class Index {

    static {
        init();
    }

    private synchronized static void init() {

        get("/index", (request, response) -> {
            MongoTemplate mongoTemplate = (MongoTemplate) ApplicationConextUtils.getBean("mongoTemplate");
            DBCollection info_collection = mongoTemplate.getCollection("info");
            DBCollection site_adsense_collection = mongoTemplate.getCollection("site-adsense");
            Map result = Maps.newHashMap();

            // 当前站点id
            String site_id = request.queryParams("site_id");

            // 查询 广告位1-6;
            DBObject query = Query.query(Criteria.where("site.$id").is(new ObjectId(site_id))).getQueryObject();
            DBObject data = site_adsense_collection.findOne(query);
            if (data != null) {
                for (int i = 1; i <= 6; i++) {
                    String key = "adsense" + i;
                    BasicDBList list = (BasicDBList) data.get(key);
                    List<ObjectId> info_objIds = Lists.newArrayList();
                    if (list == null) {
                        result.put(key, Lists.newArrayList());
                        continue;
                    }
                    ;
                    for (Object obj : list) {
                        DBRef ref = (DBRef) obj;
                        info_objIds.add(new ObjectId(ref.getId().toString()));
                    }

                    DBObject query_obj = Query.query(new Criteria("_id").in(info_objIds)).getQueryObject();
                    List<DBObject> datas = mongoTemplate.getCollection("info").find(query_obj).toArray();
                    List<Info> _adsense = InfoConverter.convert(datas);

                    result.put(key, _adsense);
                }
                // 最新成交
                List<DBObject> datas = info_collection.find(Query.query(Criteria.where("site.$id").is(new ObjectId(site_id)).and("isTraded").is(true)).getQueryObject()).toArray();
                List<Info> newest_trade = InfoConverter.convert(datas);
                result.put("newest_trade", newest_trade);
            }

            return result;
        }, JSON::toJSONString);

    }
}
