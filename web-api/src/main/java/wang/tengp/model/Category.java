package wang.tengp.model;

import org.springframework.context.annotation.Scope;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import wang.tengp.core.BaseDocument;
import wang.tengp.core.annotation.ActiveRecord;

import java.util.List;

/**
 * 行业（信息）分类
 * Created by shumin on 16-8-18.
 */
@ActiveRecord
@Scope("prototype")
@Document(collection = "category")
public class Category extends BaseDocument<Category> implements Comparable<Category> {

    private String name;
    private String code;
    private String description;
    private List<String> tags;

    // 排序
    private int order;
    
    // 上级分类
    @DBRef(db = "category", lazy = true)
    private Category supCategory;

    // 子类
    @DBRef(db = "category", lazy = true)
    private List<Category> subCategories;

    public Category() {
    }

    public String getName() {
        return name;
    }

    public Category setName(String name) {
        this.name = name;
        return this;
    }

    public String getCode() {
        return code;
    }

    public Category setCode(String code) {
        this.code = code;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public Category setDescription(String description) {
        this.description = description;
        return this;
    }

    public List<String> getTags() {
        return tags;
    }

    public Category setTags(List<String> tags) {
        this.tags = tags;
        return this;
    }

    public Category getSupCategory() {
        return supCategory;
    }

    public Category setSupCategory(Category supCategory) {
        this.supCategory = supCategory;
        return this;
    }

    public List<Category> getSubCategories() {
        return subCategories;
    }

    public Category setSubCategories(List<Category> subCategories) {
        this.subCategories = subCategories;
        return this;
    }

    public int getOrder() {
        return order;
    }

    public Category setOrder(int order) {
        this.order = order;
        return this;
    }

    @Override
    public int compareTo(Category category) {
        return this.getOrder() - category.getOrder();
    }
}