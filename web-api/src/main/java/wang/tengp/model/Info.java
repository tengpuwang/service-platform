package wang.tengp.model;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.annotation.JSONField;
import com.google.common.collect.Lists;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import wang.tengp.common.InfoConverter;
import wang.tengp.common.util.ApplicationConextUtils;
import wang.tengp.core.BaseDocument;
import wang.tengp.enums.EstateType;
import wang.tengp.enums.InfoSource;
import wang.tengp.enums.InfoStatus;
import wang.tengp.enums.InfoType;
import wang.tengp.model.estate.Estate;
import wang.tengp.vo.Contact;

import java.util.Collection;
import java.util.List;

/**
 * 信息
 * Created by shumin on 16-7-23.
 */
public class Info extends BaseDocument<Info> {

    // 信息所属站点
    @DBRef(db = "site", lazy = true)
    @JSONField(deserialize = false, serialize = false)
    private Site site;

    @JSONField(name = "site", deserialize = true, serialize = true)
    @Transient
    private ObjectId siteId;
    // 标题
    private String title;

    // 内容
    private String content;

    // 所属行业分类
    private String category;

    // 标签
    private String[] tags;

    // 图片
    private List<String> images;

    // 供求类型
    private InfoType infoType;

    // 物业类型
    private EstateType estateType;

    // 物业实体信息
    private Estate estate;

    // 信息状态（上线/下线）
    private InfoStatus infoStatus;

    // 信息来源（腾铺认证/个人自发）
    private InfoSource infoSource;

    // 是否腾铺优选
    private boolean isPreference;

    // 是否成交
    private boolean isTraded;

    // 被查看数量
    private long viewCount;

    // 被关注数量
    private long watchCount;

    // 信息联系人
    private Contact contact;

    // 级别（1-10）
    private int level;

    public Info() {
    }

    public Site getSite() {
        return site;
    }

    public Info setSite(Site site) {
        this.site = site;
        return this;
    }

    public ObjectId getSiteId() {
        if (this.site != null) {
            return this.site.getId();
        }
        return siteId;
    }

    public Info setSiteId(ObjectId siteId) {
        this.siteId = siteId;
        return this;
    }

    public String getCategory() {
        return category;
    }

    public Info setCategory(String category) {
        this.category = category;
        return this;
    }

    public boolean isTraded() {
        return isTraded;
    }

    public Info setTraded(boolean traded) {
        isTraded = traded;
        return this;
    }

    public String getTitle() {
        return title;
    }

    public Info setTitle(String title) {
        this.title = title;
        return this;
    }

    public String getContent() {
        return content;
    }

    public Info setContent(String content) {
        this.content = content;
        return this;
    }

    public String[] getTags() {
        return tags;
    }

    public Info setTags(String[] tags) {
        this.tags = tags;
        return this;
    }

    public List<String> getImages() {
        return images;
    }

    public Info setImages(List<String> images) {
        this.images = images;
        return this;
    }

    public InfoType getInfoType() {
        return infoType;
    }

    public Info setInfoType(InfoType infoType) {
        this.infoType = infoType;
        return this;
    }

    public EstateType getEstateType() {
        return estateType;
    }

    public Info setEstateType(EstateType estateType) {
        this.estateType = estateType;
        return this;
    }

    public Estate getEstate() {
        return estate;
    }

    public Info setEstate(Estate estate) {
        this.estate = estate;
        return this;
    }

    public InfoStatus getInfoStatus() {
        return infoStatus;
    }

    public Info setInfoStatus(InfoStatus infoStatus) {
        this.infoStatus = infoStatus;
        return this;
    }

    public InfoSource getInfoSource() {
        return infoSource;
    }

    public Info setInfoSource(InfoSource infoSource) {
        this.infoSource = infoSource;
        return this;
    }

    public boolean isPreference() {
        return isPreference;
    }

    public Info setPreference(boolean preference) {
        isPreference = preference;
        return this;
    }

    public long getViewCount() {
        return viewCount;
    }

    public Info setViewCount(long viewCount) {
        this.viewCount = viewCount;
        return this;
    }

    public long getWatchCount() {
        return watchCount;
    }

    public Info setWatchCount(long watchCount) {
        this.watchCount = watchCount;
        return this;
    }

    public Contact getContact() {
        return contact;
    }

    public Info setContact(Contact contact) {
        this.contact = contact;
        return this;
    }

    public int getLevel() {
        return level;
    }

    public Info setLevel(int level) {
        this.level = level;
        return this;
    }


    @Override
    public Info findById(String id) {
        MongoTemplate mongoTemplate = (MongoTemplate) ApplicationConextUtils.getBean("mongoTemplate");
        DBCollection info_collection = mongoTemplate.getCollection("info");
        DBObject data = info_collection.findOne(Query.query(Criteria.where("_id").is(new ObjectId(id))).getQueryObject());
        Info info = InfoConverter.convert(data);
        return info;
    }

    @Override
    public Info findById(ObjectId id) {
        MongoTemplate mongoTemplate = (MongoTemplate) ApplicationConextUtils.getBean("mongoTemplate");
        DBCollection info_collection = mongoTemplate.getCollection("info");
        DBObject data = info_collection.findOne(Query.query(Criteria.where("_id").is(id)).getQueryObject());
        Info info = InfoConverter.convert(data);
        return info;
    }

    public List<Info> findByIds(Collection ids) {
        List<ObjectId> obj_ids = Lists.newArrayList();
        ids.parallelStream().forEach(id -> {
            obj_ids.add(new ObjectId(id.toString()));
        });
        List<Info> infos = Lists.newArrayList();
        MongoTemplate mongoTemplate = (MongoTemplate) ApplicationConextUtils.getBean("mongoTemplate");
        DBCollection info_collection = mongoTemplate.getCollection("info");
        List<DBObject> datas = info_collection.find(Query.query(Criteria.where("_id").in(obj_ids)).getQueryObject()).toArray();
        infos = InfoConverter.convert(datas);
        return infos;
    }
}