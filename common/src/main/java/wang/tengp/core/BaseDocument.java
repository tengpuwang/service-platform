/*
 * Copyright 2012 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package wang.tengp.core;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.annotation.JSONField;
import com.mongodb.WriteResult;
import org.bson.BasicBSONObject;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.data.repository.NoRepositoryBean;
import wang.tengp.common.Pagination;
import wang.tengp.common.util.ApplicationConextUtils;
import wang.tengp.common.util.ReflectionUtils;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.*;

/**
 * Base class for document classes.
 *
 * @param <T> Created by shumin on 16-5-6.
 */
@NoRepositoryBean
public abstract class BaseDocument<T> implements Serializable {

    private static final Logger logger = LoggerFactory.getLogger(BaseDocument.class);

    @Transient
    private final Class<T> _class = (Class<T>) this.getClass();

    /**
     * spring mongodb　集成操作类
     */
    @Transient
    protected static MongoTemplate mongoTemplate;

    /**
     * 初始化MongoTemplate
     */
    public synchronized static void initMongoTemplate() {
        if (mongoTemplate == null) {
            mongoTemplate = (MongoTemplate) ApplicationConextUtils.getBean("mongoTemplate");
        }
    }

    @Id
    public ObjectId id;

    // 创建时间
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    public Date createdAt;

    // 最后修改时间
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    public Date lastModifiedAt;

    //  是否删除
    public boolean isDeleted;

    //  删除时间
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    public Date deletedAt;

    protected BaseDocument() {
    }

    /**
     * Returns the identifier of the document.
     *
     * @return the id
     */
    public ObjectId getId() {
        return id;
    }

    public T setId(ObjectId id) {
        this.id = id;
        return (T) this;
    }

    public T setId(String hexString) {
        this.id = new ObjectId(hexString);
        return (T) this;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public T setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
        return (T) this;
    }

    public Date getLastModifiedAt() {
        return lastModifiedAt;
    }

    public T setLastModifiedAt(Date lastModifiedAt) {
        this.lastModifiedAt = lastModifiedAt;
        return (T) this;
    }

    public boolean isDeleted() {
        return isDeleted;
    }

    public T setDeleted(boolean deleted) {
        isDeleted = deleted;
        return (T) this;
    }

    public Date getDeletedAt() {
        return deletedAt;
    }

    public T setDeleteddAt(Date deleteddAt) {
        this.deletedAt = deleteddAt;
        return (T) this;
    }


    // 领域模型 ActiveRecord

    //--------------------------------- 查询 ： start------------------------------

    public T findById(ObjectId id) {
        return this.findById(id, this._class);
    }

    public static <T> T findById(ObjectId id, Class<T> _class) {
        return mongoTemplate.findById(id, _class);
    }

    public T findById(String id) {
        return this.findById(id, this._class);
    }

    public static <T> T findById(String id, Class<T> _class) {
        return mongoTemplate.findById(id, _class);
    }

    public static <T> List<T> findByIds(Collection ids, Class<T> _class) {
        Query query = new Query();
        Criteria criteria = Criteria.where("_id").in(ids);
        query.addCriteria(criteria);
        return mongoTemplate.find(query, _class);
    }

    public static <T> List<T> findByIds(String[] ids, Class<T> _class) {
        Query query = new Query();
        Criteria criteria = Criteria.where("_id").in(ids);
        query.addCriteria(criteria);
        return mongoTemplate.find(query, _class);
    }

    public static <T> List<T> findByIds(ObjectId[] ids, Class<T> _class) {
        Query query = new Query();
        Criteria criteria = Criteria.where("_id").in(ids);
        query.addCriteria(criteria);
        return mongoTemplate.find(query, _class);
    }

    public List<T> findAll() {
        return this.findAll(this._class);
    }

    public static <T> List<T> findAll(Class _class) {
        return mongoTemplate.findAll(_class);
    }

    public T findOne(Query query) {
        return this.findOne(query, this._class);
    }

    public static <T> T findOne(Query query, Class _class) {
        return (T) mongoTemplate.findOne(query, _class);
    }

    public List<T> find(Query query) {
        return this.find(query, this._class);
    }

    public static <T> List<T> find(Query query, Class _class) {
        return (List<T>) mongoTemplate.find(query, _class);
    }

    //--------------------------------- 查询 ： end------------------------------


    //--------------------------------- 插入&保存 ： start------------------------------

    public T save() {
        this.lastModifiedAt = new Date();
        return this.save(this);
    }

    public static <T> T save(BaseDocument<T> object) {
        object.lastModifiedAt = new Date();
        mongoTemplate.save(object);
        return (T) object;
    }

    public T insert() {
        return this.insert(this);
    }

    public static <T> T insert(BaseDocument<T> object) {
        object.createdAt = new Date();
        mongoTemplate.insert(object);
        return (T) object;
    }

    // 批量插入
    public static List<? extends Serializable> insertAll(List<? extends Serializable> entities) {
        for (int i = 0, len = entities.size(); i < len; i++) {
            ((BaseDocument) entities.get(i)).createdAt = new Date();
        }
        mongoTemplate.insertAll(entities);

        return entities;
    }

    //--------------------------------- 插入&保存 ： end------------------------------


    //--------------------------------- 删除 ： start------------------------------

    /**
     * 物理删除
     *
     * @return
     */
    public WriteResult remove() {
        return remove(this);
    }


    /**
     * 逻辑删除
     *
     * @return
     */
    public boolean delete() {
        return delete(this);
    }

    /**
     * 物理删除
     *
     * @return
     */
    public static WriteResult remove(Serializable object) {
        WriteResult writeResult = mongoTemplate.remove(object);
        return writeResult;
    }

    /**
     * 逻辑删除
     *
     * @return
     */
    public static boolean delete(BaseDocument object) {
        try {
            object.setDeleted(true);
            object.setDeleteddAt(new Date());
            save(object);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public static WriteResult remove(Query query, Class _class) {
        WriteResult writeResult = mongoTemplate.remove(query, _class);
        return writeResult;
    }

    public static WriteResult delete(Query query, Class _class) {
        Update delete = new Update();
        delete.set("isDeleted", true);
        delete.set("deletedAt", new Date());
        WriteResult writeResult = mongoTemplate.updateMulti(query, delete, _class);
        return writeResult;
    }

    public static WriteResult removeById(ObjectId id, Class _class) {
        return removeById(id.toHexString(), _class);
    }

    public static WriteResult deleteById(ObjectId id, Class _class) {
        return deleteById(id.toHexString(), _class);
    }

    public static WriteResult removeById(String id, Class _class) {
        Query query = new Query();
        Criteria criteria = Criteria.where("_id").is(id);
        query.addCriteria(criteria);
        WriteResult writeResult = mongoTemplate.remove(query, _class);
        return writeResult;
    }

    public static WriteResult deleteById(String id, Class _class) {
        Query query = new Query();
        Criteria criteria = Criteria.where("_id").is(id);
        query.addCriteria(criteria);
        Update delete = new Update();
        delete.set("isDeleted", true);
        delete.set("deletedAt", new Date());
        WriteResult writeResult = mongoTemplate.updateFirst(query, delete, _class);
        return writeResult;
    }

    public static WriteResult removeByIds(Collection ids, Class _class) {
        Query query = new Query();
        Criteria criteria = Criteria.where("_id").in(ids);
        query.addCriteria(criteria);
        WriteResult writeResult = mongoTemplate.remove(query, _class);
        return writeResult;
    }

    public static WriteResult deleteByIds(Collection ids, Class _class) {
        Query query = new Query();
        Criteria criteria = Criteria.where("_id").in(ids);
        query.addCriteria(criteria);
        Update delete = new Update();
        delete.set("isDeleted", true);
        delete.set("deletedAt", new Date());
        WriteResult writeResult = mongoTemplate.updateMulti(query, delete, _class);
        return writeResult;
    }

    //--------------------------------- 删除 ： end------------------------------

//    public static WriteResult removeByIds(String[] ids, Class _class) {
//        Query query = new Query();
//        Criteria criteria = Criteria.where("_id").in(ids);
//        query.addCriteria(criteria);
//        WriteResult writeResult = mongoTemplate.remove(query, _class);
//        return writeResult;
//    }

//    public T update() {
//        return (T) this.update(this);
//    }
//
//    public static <T> T update(BaseDocument<T> object) {
//        mongoTemplate.save(object);
//        return (T) object;
//    }

    // ----------------------------- 更新 ： start ----------------------------------

    public void findAndModify(Query query, Update update) {
        findAndModify(query, update, this._class);
    }

    /**
     * 返回更新后的文档
     * zhengm
     *
     * @param query
     * @param update
     * @param options
     * @param _class
     */
    public static <T> T findAndModify(Query query, Update update, FindAndModifyOptions options, Class _class) {
        return (T) mongoTemplate.findAndModify(query, update, options, _class);
    }

    public static void findAndModify(Query query, Update update, Class _class) {
        mongoTemplate.findAndModify(query, update, _class);
    }

    public void updateMulti(Query query, Update update) {
        updateMulti(query, update, this._class);
    }

    public static void updateMulti(Query query, Update update, Class _class) {
        mongoTemplate.updateMulti(query, update, _class);
    }

    public void updateFirst(Query query, Update update) {
        updateFirst(query, update, this._class);
    }

    public static void updateFirst(Query query, Update update, Class _class) {
        mongoTemplate.updateFirst(query, update, _class);
    }

    // ----------------------------- 更新 ： end ----------------------------------


    // ----------------------------- 分页查询 ： start ----------------------------------

    public Pagination<T> findByPage(Pagination<T> pagination, Query query) {
        return findByPage(pagination, query, _class);
    }

    public static <T> Pagination<T> findByPage(Pagination<T> pagination, Query query, Class _class) {

        // 总记录数
        long count = count(query, _class);

        pagination.getPageInfo().setTotalCount(count);

        int pageNumber = pagination.getPageInfo().getPageNo();
        int pageSize = pagination.getPageInfo().getPageSize();

        query.skip((pageNumber - 1) * pageSize).limit(pageSize);

        List<T> rows = find(query, _class);

        pagination.setPageDatas(rows);

        return pagination;
    }

    // ----------------------------- 分页查询 ： end ----------------------------------

    // ----------------------------- 计数 ： start ----------------------------------
    public long count() {
        return count(this._class);
    }

    public static long count(Class _class) {
        Query query = new Query();
        return count(query, _class);
    }

    public long count(Query query) {
        return count(query, _class);
    }

    public static long count(Query query, Class _class) {
        return mongoTemplate.count(query, _class);
    }
    // ----------------------------- 计数 ： end ----------------------------------

    /**
     * 获取需要操作的实体类class
     *
     * @return
     */
    private Class<T> getEntityClass() {
        return ReflectionUtils.getSuperClassGenricType(this.getClass());
    }

    /*
     * (non-Javadoc)
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object obj) {

        if (this == obj) {
            return true;
        }

        if (this.id == null || obj == null || !(this.getClass().equals(obj.getClass()))) {
            return false;
        }

        BaseDocument that = (BaseDocument) obj;

        return this.id.equals(that.getId());
    }

    /*
     * (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        return id == null ? 0 : id.hashCode();
    }


    public Map<String, Object> toMap() {
        return toMap(this);
    }

    public static Map<String, Object> toMap(BaseDocument obj) {
        if (obj == null) {
            return null;
        }
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            BeanInfo beanInfo = Introspector.getBeanInfo(obj._class);
            PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
            for (PropertyDescriptor property : propertyDescriptors) {
                String key = property.getName();
                // 过滤class属性
                if (!key.equals("class")) {
                    // 得到property对应的getter方法
                    Method getter = property.getReadMethod();
                    Object value = getter.invoke(obj);
                    map.put(key, value);
                }
            }
        } catch (Exception e) {
            return null;
        }
        return map;
    }

    public BasicBSONObject toBson() {
        return toBson(this);
    }

    public static BasicBSONObject toBson(BaseDocument obj) {

        return new BasicBSONObject(toMap(obj));
    }

    public String toJson() {
        return toJson(this);
    }

    public static String toJson(BaseDocument obj) {

        return JSON.toJSONString(obj);
    }
}