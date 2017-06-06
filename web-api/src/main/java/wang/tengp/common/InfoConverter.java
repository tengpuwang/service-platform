package wang.tengp.common;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.mongodb.DBObject;
import com.mongodb.DBRef;
import wang.tengp.enums.EstateType;
import wang.tengp.model.Info;
import wang.tengp.model.estate.Store;

import java.util.List;

/**
 * Info 转换工具
 * Created by shumin on 2017/3/8.
 */
public class InfoConverter {

    // 将Map列表转换成Info列表
    public static final List<Info> convert(List<DBObject> dbObjectList) {
        List<Info> infos = Lists.newArrayList();
        for (DBObject object : dbObjectList) {
            Info info = convert(object);
            infos.add(info);
        }
        return infos;
    }

    // 将DBObject转换成InfoØ
    public static final Info convert(DBObject dbObject) {
        // 处理site
        DBRef site_ref = (DBRef) dbObject.get("site");
        if (site_ref != null)
            dbObject.put("site", site_ref.getId());
        String json_str = JSON.toJSONString(dbObject);
        JSONObject json_info = JSON.parseObject(json_str);
        Info info = JSON.parseObject(json_str, Info.class);
        EstateType estateType = info.getEstateType();
        switch (estateType) {
            case Store: {
                Store store = json_info.getJSONObject("estate").toJavaObject(Store.class);
                info.setEstate(store);
            }
        }
        return info;
    }
}