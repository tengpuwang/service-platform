package wang.tengp.vo;

/**
 * 联系人
 * Created by shumin on 16-10-27.
 */
public class Contact {

    private String name;

    private String phone;

    private String qq;

    private String weixin;

    public Contact() {
    }

    public String getName() {
        return name;
    }

    public Contact setName(String name) {
        this.name = name;
        return this;
    }

    public String getPhone() {
        return phone;
    }

    public Contact setPhone(String phone) {
        this.phone = phone;
        return this;
    }

    public String getQq() {
        return qq;
    }

    public Contact setQq(String qq) {
        this.qq = qq;
        return this;
    }

    public String getWeixin() {
        return weixin;
    }

    public Contact setWeixin(String weixin) {
        this.weixin = weixin;
        return this;
    }
}