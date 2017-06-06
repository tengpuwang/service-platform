package wang.tengp.model;

import wang.tengp.core.BaseDocument;
import wang.tengp.core.annotation.ActiveRecord;
import com.google.common.collect.Lists;
import org.springframework.context.annotation.Scope;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

/**
 * 城市
 * Created by shumin on 16-8-19.
 */
@ActiveRecord
@Scope("prototype")
@Document(collection = "city")
public class City extends BaseDocument<City> {

    // 城市名
    private String name;

    // 拼音
    private String pinyin;

    // 区域
    private List<Area> areas;

    // 地铁
    private List<Subway> subways;

    public City() {
    }

    public String getName() {
        return name;
    }

    public City setName(String name) {
        this.name = name;
        return this;
    }

    public String getPinyin() {
        return pinyin;
    }

    public City setPinyin(String pinyin) {
        this.pinyin = pinyin;
        return this;
    }

    public List<Area> getAreas() {
        return areas;
    }

    public City setAreas(List<Area> areas) {
        this.areas = areas;
        return this;
    }

    public List<Subway> getSubways() {
        return subways;
    }

    public City setSubways(List<Subway> subways) {
        this.subways = subways;
        return this;
    }


    /**
     * 区域
     */
    public static class Area {

        // 区域名
        public String name;

        // 商圈
        public List<ShopArea> shopAreas;

        public Area() {
        }

        public String getName() {
            return name;
        }

        public Area setName(String name) {
            this.name = name;
            return this;
        }

        public List<ShopArea> getShopAreas() {
            return shopAreas;
        }

        public Area setShopAreas(List<ShopArea> shopAreas) {
            this.shopAreas = shopAreas;
            return this;
        }
    }

    /**
     * 商圈
     */
    public static class ShopArea {

        // 商圈
        public String name;

        public ShopArea() {
        }

        public String getName() {
            return name;
        }

        public ShopArea setName(String name) {
            this.name = name;
            return this;
        }
    }

    /**
     * 地铁
     */
    public static class Subway {

        public String name;

        public List<String> stations = Lists.newArrayList();

        public Subway() {
        }

        public String getName() {
            return name;
        }

        public Subway setName(String name) {
            this.name = name;
            return this;
        }

        public List<String> getStations() {
            return stations;
        }

        public Subway setStations(List<String> stations) {
            this.stations = stations;
            return this;
        }
    }

}

