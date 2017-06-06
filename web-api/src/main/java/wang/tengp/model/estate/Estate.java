package wang.tengp.model.estate;

import wang.tengp.core.BaseDocument;
import wang.tengp.enums.EstateType;

/**
 * 物业实体
 * Created by shumin on 16-7-23.
 */
public abstract class Estate {

    // 面积
    private double area;
    // 租金
    private double rental;
    // 物业费
    private double propertyFee;
    // 详细地址
    private String location;
    // 行政省
    private String province;
    // 行政市
    private String city;
    // 行政区
    private String district;
    // 街道
    private String street;
    // 商圈
    private String shopArea;
    // 地铁
    private String subway;
    // 站点
    private String station;


    public Estate() {
    }

    public double getArea() {
        return area;
    }

    public Estate setArea(double area) {
        this.area = area;
        return this;
    }

    public double getRental() {
        return rental;
    }

    public Estate setRental(double rental) {
        this.rental = rental;
        return this;
    }

    public double getPropertyFee() {
        return propertyFee;
    }

    public Estate setPropertyFee(double propertyFee) {
        this.propertyFee = propertyFee;
        return this;
    }

    public String getLocation() {
        return location;
    }

    public Estate setLocation(String location) {
        this.location = location;
        return this;
    }

    public String getDistrict() {
        return district;
    }

    public Estate setDistrict(String district) {
        this.district = district;
        return this;
    }

    public String getStreet() {
        return street;
    }

    public Estate setStreet(String street) {
        this.street = street;
        return this;
    }

    public String getShopArea() {
        return shopArea;
    }

    public Estate setShopArea(String shopArea) {
        this.shopArea = shopArea;
        return this;
    }

    public String getSubway() {
        return subway;
    }

    public Estate setSubway(String subway) {
        this.subway = subway;
        return this;
    }

    public String getStation() {
        return station;
    }

    public Estate setStation(String station) {
        this.station = station;
        return this;
    }

    public String getProvince() {
        return province;
    }

    public Estate setProvince(String province) {
        this.province = province;
        return this;
    }

    public String getCity() {
        return city;
    }

    public Estate setCity(String city) {
        this.city = city;
        return this;
    }
}