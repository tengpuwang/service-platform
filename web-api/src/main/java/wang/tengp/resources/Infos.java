package wang.tengp.resources;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.CriteriaDefinition;
import org.springframework.data.mongodb.core.query.Query;
import wang.tengp.common.InfoConverter;
import wang.tengp.common.Pagination;
import wang.tengp.common.util.ApplicationConextUtils;
import wang.tengp.core.annotation.Resource;
import wang.tengp.enums.EstateType;
import wang.tengp.model.File;
import wang.tengp.model.Info;
import wang.tengp.model.Site;
import wang.tengp.model.estate.Store;
import wang.tengp.qiniu.QiniuUploader;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import static spark.Spark.*;

/**
 * Created by shumin on 16-11-4.
 */
@Resource
public class Infos {
    static {
        init();
    }

    private static synchronized void init() {
        /**
         * 查询信息
         */
        get("/infos", (req, rep) -> {
            MongoTemplate mongoTemplate = (MongoTemplate) ApplicationConextUtils.getBean("mongoTemplate");
            List infos = new ArrayList<Info>();
            String queryString = req.queryParams("q");
            if (queryString == null) {
                queryString = req.queryString();
            }
            if (!Strings.isNullOrEmpty(queryString)) {
                JSONObject q_json = JSON.parseObject(queryString);
                JSONObject q = q_json.getJSONObject("query");
                // 处理 Site的关联
                String siteId = q.getString("site");
                ObjectId sid = new ObjectId(siteId);
                q.remove("site");
                q.put("site.$id", sid);
                // 未成交
                q.put("isTraded", false);
                // 未删除
                q.put("isDeleted", false);

                // 处理title模糊匹配
                String title = q.getString("title");
                if (title != null) {
                    Pattern pattern = Pattern.compile("^.*" + title + ".*$", Pattern.CASE_INSENSITIVE);
                    q.put("title", pattern);
                }

                JSONObject sort = q_json.getJSONObject("sort");
                JSONObject page = q_json.getJSONObject("page");
                int pageIndex = page.getInteger("index");
                int pageSize = page.getInteger("size");
                DBObject query = new BasicDBObject(q);
                DBObject orderBy = new BasicDBObject(sort);

                Pagination<Infos> pagination = new Pagination<Infos>(pageIndex, pageSize);
                // 查询分页总数
                long total_count = mongoTemplate.getCollection("info").find(query).count();
                pagination.getPageInfo().setTotalCount(total_count);
                int skip = (pageIndex - 1) * pageSize;
                List<DBObject> dbObjectList = mongoTemplate.getCollection("info").find(query).sort(orderBy).skip(skip).limit(pageSize).toArray();

                infos = InfoConverter.convert(dbObjectList);

                pagination.setPageDatas(infos);

                return pagination;
            } else {
                List<DBObject> dbObjectList = mongoTemplate.getCollection("info").find().toArray();

                infos = InfoConverter.convert(dbObjectList);

            }
            return infos;
        }, JSON::toJSONString);


        get("/infos/:id", (req, rep) -> {
            String id = req.params(":id");
            ObjectId info_id = new ObjectId(id);
            MongoTemplate mongoTemplate = (MongoTemplate) ApplicationConextUtils.getBean("mongoTemplate");
            DBObject query = Query.query(Criteria.where("_id").is(info_id)).getQueryObject();
            DBObject obj = mongoTemplate.getCollection("info").findOne(query);
            Info info = InfoConverter.convert(obj);
            return info;
        }, JSON::toJSONString);

        /**
         * 添加转让信息
         */
        post("/infos", (req, rep) -> {
            String request_playload = req.body();
            JSONObject json_info = JSON.parseObject(request_playload);
            Info info = JSON.parseObject(request_playload, Info.class);
            // 图片 处理
//            JSONArray json_images = json_info.getJSONArray("images");
//            if (json_images != null && json_images.size() > 0) {
//                List<File> images = File.findByIds(json_images, File.class);
//                json_info.put("images", images);
//            }
            // 物业实体 处理
            EstateType estateType = info.getEstateType();
            switch (estateType) {
                case Store: {
                    Store store = json_info.getJSONObject("estate").toJavaObject(Store.class);
                    info.setEstate(store);
                }
            }
            info.setSite(Site.findById(info.getSiteId(), Site.class));
            info.insert();
            return info;
        }, JSON::toJSONString);


        // 查看信息
        put("/infos/:id/read", (request, response) -> {
            String info_id = request.params(":id");

            Info info = new Info().findById(new ObjectId(info_id));
            info.setViewCount(info.getViewCount() + 1);
            info.save();
            return info;
        });

        // 成交
        put("/infos/:id/complete", (request, response) -> {
            String info_id = request.params(":id");
            Info info = new Info().findById(new ObjectId(info_id));
            info.setTraded(true);
            info.save();
            return info;
        }, JSON::toJSONString);

        // 删除信息
        delete("/infos/:id", (request, response) -> {
            String info_id = request.params(":id");
            Info info = new Info().findById(info_id);
            List<String> images = info.getImages();
            String[] keys = new String[images.size()];
            for (int i = 0; i < images.size(); i++) {
                String image = images.get(i);
                String key = image.substring(image.lastIndexOf('/') + 1);
                keys[i] = key;
            }
            File.remove(Query.query(Criteria.where("key").in(keys)), File.class);
            info.remove();
            QiniuUploader.batchDelete(keys);
            return response;
        });

        // 批量删除信息
        delete("/infos", (request, response) -> {
            String req_body = request.body();
            JSONArray ids = JSON.parseObject(req_body).getJSONArray("ids");
            List<Info> infos = new Info().findByIds(ids);
            // 需要删除的图片
            List<String> keys = Lists.newArrayList();
            infos.parallelStream().forEach(info -> {
                List<String> images = info.getImages();
                images.parallelStream().forEach(image -> {
                    String key = image.substring(image.lastIndexOf('/'));
                    keys.add(key);
                });
            });
            File.remove(Query.query(Criteria.where("key").in(keys)), File.class);
            Info.removeByIds(ids, Info.class);
            QiniuUploader.batchDelete((String[]) keys.toArray());
            return null;
        });
    }
}