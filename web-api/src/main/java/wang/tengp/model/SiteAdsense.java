package wang.tengp.model;

import com.alibaba.fastjson.annotation.JSONField;
import org.bson.types.ObjectId;
import org.springframework.context.annotation.Scope;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import wang.tengp.core.BaseDocument;
import wang.tengp.core.annotation.ActiveRecord;

import java.util.List;

/**
 * 站点广告
 * Created by shumin on 16-9-9.
 */

@ActiveRecord
@Scope("prototype")
@Document(collection = "site-adsense")
public class SiteAdsense extends BaseDocument<SiteAdsense> {

    // 信息所属站点
    @DBRef(db = "site", lazy = true)
    @JSONField(deserialize = false, serialize = false)
    private Site site;

    @JSONField(name = "site", deserialize = true, serialize = true)
    @Transient
    private ObjectId siteId;

    // 首页-最新爆料-大图
    @DBRef(db = "info", lazy = true)
    private List<Info> adsense1;

    // 首页-最新爆料-小图
    @DBRef(db = "info", lazy = true)
    private List<Info> adsense2;

    // 首页-腾铺优选-大图
    @DBRef(db = "info", lazy = true)
    private List<Info> adsense3;

    // 首页-腾铺优选-小图
    @DBRef(db = "info", lazy = true)
    private List<Info> adsense4;

    // 首页-腾铺优选-无图
    @DBRef(db = "info", lazy = true)
    private List<Info> adsense5;

    // 首页-一周快转
    @DBRef(db = "info", lazy = true)
    private List<Info> adsense6;

    // 搜索页
    @DBRef(db = "info", lazy = true)
    private List<Info> adsense7;

    // 详情页-大图
    @DBRef(db = "info", lazy = true)
    private List<Info> adsense8;

    // 详情页-小图
    @DBRef(db = "info", lazy = true)
    private List<Info> adsense9;

    // 详情页-无图
    @DBRef(db = "info", lazy = true)
    private List<Info> adsense10;

    public SiteAdsense() {
    }

    public Site getSite() {
        return site;
    }

    public SiteAdsense setSite(Site site) {
        this.site = site;
        return this;
    }

    public ObjectId getSiteId() {
        return siteId;
    }

    public SiteAdsense setSiteId(ObjectId siteId) {
        this.siteId = siteId;
        return this;
    }

    public List<Info> getAdsense1() {
        return adsense1;
    }

    public SiteAdsense setAdsense1(List<Info> adsense1) {
        this.adsense1 = adsense1;
        return this;
    }

    public List<Info> getAdsense2() {
        return adsense2;
    }

    public SiteAdsense setAdsense2(List<Info> adsense2) {
        this.adsense2 = adsense2;
        return this;
    }

    public List<Info> getAdsense3() {
        return adsense3;
    }

    public SiteAdsense setAdsense3(List<Info> adsense3) {
        this.adsense3 = adsense3;
        return this;
    }

    public List<Info> getAdsense4() {
        return adsense4;
    }

    public SiteAdsense setAdsense4(List<Info> adsense4) {
        this.adsense4 = adsense4;
        return this;
    }

    public List<Info> getAdsense5() {
        return adsense5;
    }

    public SiteAdsense setAdsense5(List<Info> adsense5) {
        this.adsense5 = adsense5;
        return this;
    }

    public List<Info> getAdsense6() {
        return adsense6;
    }

    public SiteAdsense setAdsense6(List<Info> adsense6) {
        this.adsense6 = adsense6;
        return this;
    }

    public List<Info> getAdsense7() {
        return adsense7;
    }

    public SiteAdsense setAdsense7(List<Info> adsense7) {
        this.adsense7 = adsense7;
        return this;
    }

    public List<Info> getAdsense8() {
        return adsense8;
    }

    public SiteAdsense setAdsense8(List<Info> adsense8) {
        this.adsense8 = adsense8;
        return this;
    }

    public List<Info> getAdsense9() {
        return adsense9;
    }

    public SiteAdsense setAdsense9(List<Info> adsense9) {
        this.adsense9 = adsense9;
        return this;
    }

    public List<Info> getAdsense10() {
        return adsense10;
    }

    public SiteAdsense setAdsense10(List<Info> adsense10) {
        this.adsense10 = adsense10;
        return this;
    }
}