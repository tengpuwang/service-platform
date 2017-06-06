package wang.tengp.model;

import wang.tengp.vo.Contact;

import java.util.Date;

/**
 * 客户预约
 * Created by shumin on 16-10-8.
 */
public class Booking {

    // 预约时间
    private Date time;

    // 预约地点
    private String address;

    // 联系人
    private Contact contact;


    public Booking() {
    }

    public Date getTime() {
        return time;
    }

    public Booking setTime(Date time) {
        this.time = time;
        return this;
    }

    public String getAddress() {
        return address;
    }

    public Booking setAddress(String address) {
        this.address = address;
        return this;
    }

    public Contact getContact() {
        return contact;
    }

    public Booking setContact(Contact contact) {
        this.contact = contact;
        return this;
    }
}