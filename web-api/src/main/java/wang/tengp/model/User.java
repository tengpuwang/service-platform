package wang.tengp.model;

import wang.tengp.core.BaseDocument;
import org.springframework.data.annotation.PersistenceConstructor;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.DBRef;

/**
 * 会员
 * Created by shumin on 16-7-18.
 */
public class User extends BaseDocument<User> {

    private String email;

    private String password;

    private String username;

    @Transient
    private String roleId;

    @DBRef(db = "system-roles")
    private Role role;

    @PersistenceConstructor
    public User() {
    }

    public String getEmail() {
        return email;
    }

    public User setEmail(String email) {
        this.email = email;
        return this;
    }

    public String getPassword() {
        return password;
    }

    public User setPassword(String password) {
        this.password = password;
        return this;
    }

    public String getUsername() {
        return username;
    }

    public User setUsername(String username) {
        this.username = username;
        return this;
    }

    public String getRoleId() {
        if (roleId == null && role != null) {
            roleId = role.getId().toString();
        }
        return roleId;
    }

    public User setRoleId(String roleId) {
        this.roleId = roleId;
        return this;
    }

    public Role getRole() {
        return role;
    }

    public User setRole(Role role) {
        this.role = role;
        return this;
    }

    public User addRole(Role role) {
        if (this.role == null) {
//            this.roles = Lists.newArrayList();
            this.role = role;
        }
//        this.roles.add(role);
        return this;
    }

//    public User addRoles(Collection<Role> roles) {
//        if (this.roles == null) {
//            this.roles = Lists.newArrayList();
//        }
//        this.roles.addAll(roles);
//        return this;
//    }
}