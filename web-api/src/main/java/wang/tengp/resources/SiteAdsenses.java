package wang.tengp.resources;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
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
import wang.tengp.model.Site;
import wang.tengp.model.SiteAdsense;

import java.util.List;

import static spark.Spark.get;
import static spark.Spark.put;

/**
 * Created by shumin on 2017/2/21.
 */
@Resource
public class SiteAdsenses {
    static {
        init();
    }

    private synchronized static void init() {
        // 获取站点广告信息
        get("/site/:id/adsenses", (request, response) -> {
            return null;
        }, JSON::toJSONString);

        // 获取站点广告信息
        get("/site/:id/adsenses/:index", (request, response) -> {
            MongoTemplate mongoTemplate = (MongoTemplate) ApplicationConextUtils.getBean("mongoTemplate");
            DBCollection info_collection = mongoTemplate.getCollection("info");
            DBCollection site_adsense_collection = mongoTemplate.getCollection("site-adsense");
            String site_id = request.params(":id");
            String index = request.params(":index");
            Criteria criteria = new Criteria();
            criteria.where("site.$id").is(new ObjectId(site_id));
            Query query = new Query();
            query.addCriteria(criteria);
            DBObject data = site_adsense_collection.findOne(query.getQueryObject());
            String key = "adsense" + index;
            BasicDBList list = (BasicDBList) data.get(key);
            List<ObjectId> info_objIds = Lists.newArrayList();
            for (Object obj : list) {
                DBRef ref = (DBRef) obj;
                info_objIds.add(new ObjectId(ref.getId().toString()));
            }

            DBObject query_obj = Query.query(new Criteria("_id").in(info_objIds)).getQueryObject();
            List<DBObject> datas = info_collection.find(query_obj).toArray();
            List _adsense = InfoConverter.convert(datas);

            return _adsense;
        }, JSON::toJSONString);


        // 跟新站点广告信息
        put("/site/:id/adsenses", (request, response) -> {
            MongoTemplate mongoTemplate = (MongoTemplate) ApplicationConextUtils.getBean("mongoTemplate");
            DBCollection info_collection = mongoTemplate.getCollection("info");
            String site_id = request.params(":id");
            Site site = Site.findById(site_id, Site.class);
            Query query = new Query();
            Criteria criteria = Criteria.where("site.$id").is(site.getId());
            query.addCriteria(criteria);
            SiteAdsense siteAdsense = SiteAdsense.findOne(query, SiteAdsense.class);
            if (siteAdsense == null) {
                siteAdsense = new SiteAdsense().setSite(site);
            }

            JSONObject adsenses = JSON.parseObject(request.body());

            for (int i = 1; i <= 10; i++) {
                String key = "adsense" + i;
                JSONArray info_ids = adsenses.getJSONArray(key);
                if (info_ids == null || info_ids.size() == 0) {
                    continue;
                }
                List<ObjectId> info_objIds = Lists.newArrayList();
                for (Object object : info_ids) {
                    info_objIds.add(new ObjectId(object.toString()));
                }

                DBObject query_obj = Query.query(new Criteria("_id").in(info_objIds)).getQueryObject();
                List<DBObject> datas = info_collection.find(query_obj).toArray();
                List<Info> _adsense = InfoConverter.convert(datas);
                switch (i) {
                    case 1: {
                        siteAdsense.setAdsense1(_adsense);
                        break;
                    }
                    case 2: {
                        siteAdsense.setAdsense2(_adsense);
                        break;
                    }
                    case 3: {
                        siteAdsense.setAdsense3(_adsense);
                        break;
                    }
                    case 4: {
                        siteAdsense.setAdsense4(_adsense);
                        break;
                    }
                    case 5: {
                        siteAdsense.setAdsense5(_adsense);
                        break;
                    }
                    case 6: {
                        siteAdsense.setAdsense6(_adsense);
                        break;
                    }
                    case 7: {
                        siteAdsense.setAdsense7(_adsense);
                        break;
                    }
                    case 8: {
                        siteAdsense.setAdsense8(_adsense);
                        break;
                    }
                    case 9: {
                        siteAdsense.setAdsense9(_adsense);
                        break;
                    }
                    case 10: {
                        siteAdsense.setAdsense10(_adsense);
                        break;
                    }
                }
            }
            siteAdsense.save();
            return siteAdsense;
        }, JSON::toJSONString);
    }
}