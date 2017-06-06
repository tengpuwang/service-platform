package wang.tengp.model;

import com.alibaba.fastjson.annotation.JSONField;
import wang.tengp.core.BaseDocument;
import wang.tengp.core.annotation.ActiveRecord;
import org.bson.types.ObjectId;
import org.springframework.context.annotation.Scope;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import wang.tengp.qiniu.QiniuConfig;

/**
 * 站点
 * Created by shumin on 16-9-9.
 */
@ActiveRecord
@Scope("prototype")
@Document(collection = "site")
public class Site extends BaseDocument<Site> {

    // 站点名称
    private String name;

    // 站点标题
    private String title;

    // 站点联系联系电话
    private String phone;

    private String email;

    // 站点简介
    private String intro;

    // 子域名
    private String subDomain;

    //七牛云存储配置
    private QiniuConfig qiniuConfig;

    @JSONField(name = "city", serialize = false)
    @Transient
    private ObjectId cityId;

    @JSONField(deserialize = false)
    @DBRef(db = "city", lazy = true)
    private City city;


    public Site() {
    }

    public String getName() {
        return name;
    }

    public Site setName(String name) {
        this.name = name;
        return this;
    }

    public String getTitle() {
        return title;
    }

    public Site setTitle(String title) {
        this.title = title;
        return this;
    }

    public String getPhone() {
        return phone;
    }

    public Site setPhone(String phone) {
        this.phone = phone;
        return this;
    }

    public String getIntro() {
        return intro;
    }

    public Site setIntro(String intro) {
        this.intro = intro;
        return this;
    }

    public String getEmail() {
        return email;
    }

    public Site setEmail(String email) {
        this.email = email;
        return this;
    }

    public ObjectId getCityId() {
        return cityId;
    }

    public Site setCityId(ObjectId cityId) {
        this.cityId = cityId;
        return this;
    }

    public City getCity() {
        return city;
    }

    public Site setCity(City city) {
        this.city = city;
        return this;
    }

    public String getSubDomain() {
        return subDomain;
    }

    public Site setSubDomain(String subDomain) {
        this.subDomain = subDomain;
        return this;
    }
}