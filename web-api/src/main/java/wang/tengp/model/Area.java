package wang.tengp.model;

import org.springframework.context.annotation.Scope;
import org.springframework.data.mongodb.core.mapping.Document;
import wang.tengp.core.BaseDocument;
import wang.tengp.core.annotation.ActiveRecord;

/**
 * 行政区域
 * Created by shumin on 16-8-22.
 */

@ActiveRecord
@Scope("prototype")
@Document(collection = "area")
public class Area extends BaseDocument<Area> {

    private String id;

    private String parent_id;

    private String name;

    private String short_name;

    private int level;

    private int sort;

    public Area() {
    }

    public String getParent_id() {
        return parent_id;
    }

    public Area setParent_id(String parent_id) {
        this.parent_id = parent_id;
        return this;
    }

    public String getName() {
        return name;
    }

    public Area setName(String name) {
        this.name = name;
        return this;
    }

    public String getShort_name() {
        return short_name;
    }

    public Area setShort_name(String short_name) {
        this.short_name = short_name;
        return this;
    }

    public int getLevel() {
        return level;
    }

    public Area setLevel(int level) {
        this.level = level;
        return this;
    }

    public int getSort() {
        return sort;
    }

    public Area setSort(int sort) {
        this.sort = sort;
        return this;
    }
}