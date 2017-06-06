package wang.tengp.model;

import wang.tengp.core.BaseDocument;
import com.google.common.collect.Lists;
import org.springframework.data.annotation.PersistenceConstructor;
import org.springframework.data.mongodb.core.mapping.DBRef;

import java.util.Collection;
import java.util.List;

/**
 * Created by shumin on 16-7-18.
 */

public class Role extends BaseDocument<Role> {

    // 名称
    private String name;

    // 描述
    private String discription;

    // 标记
    private String[] tags;

    @DBRef(db = "system-users")
    private List<User> users;

    @PersistenceConstructor
    public Role() {
    }

    public String getName() {
        return name;
    }

    public Role setName(String name) {
        this.name = name;
        return this;
    }

    public String getDiscription() {
        return discription;
    }

    public Role setDiscription(String discription) {
        this.discription = discription;
        return this;
    }

    public String[] getTags() {
        return tags;
    }

    public Role setTags(String[] tags) {
        this.tags = tags;
        return this;
    }

    public List<User> getUsers() {
        return users;
    }

    public Role setUsers(List<User> users) {
        this.users = users;
        return this;
    }

    public Role addUser(User user) {
        if (this.users == null) {
            this.users = Lists.newArrayList();
        }
        this.users.add(user);
        return this;
    }

    public Role addUsers(Collection<User> users) {
        if (this.users == null) {
            this.users = Lists.newArrayList();
        }
        this.users.addAll(users);
        return this;
    }
}