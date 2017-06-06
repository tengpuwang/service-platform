package wang.tengp.model.estate;

import wang.tengp.enums.EstateType;

/**
 * 商铺
 * Created by shumin on 16-7-18.
 */

public class Store extends Estate {

    private final static EstateType estateType = EstateType.Store;

    // 店铺类型
    private String type;

    // 是否营业中
    private boolean isOperating;

    // 转让费
    private double transferFee;

    // （转让费）上下浮动价格
    private double offset;

    // 可否议价（转让费）
    private Boolean canBargaining;

    // 可否空转
    private Boolean canEmptyAttorn;

    public Store() {
    }

    public static EstateType getEstateType() {
        return estateType;
    }

    public String getType() {
        return type;
    }

    public Store setType(String type) {
        this.type = type;
        return this;
    }

    public boolean isOperating() {
        return isOperating;
    }

    public Store setOperating(boolean operating) {
        isOperating = operating;
        return this;
    }

    public double getTransferFee() {
        return transferFee;
    }

    public Store setTransferFee(double transferFee) {
        this.transferFee = transferFee;
        return this;
    }

    public Boolean getCanBargaining() {
        return canBargaining;
    }

    public Store setCanBargaining(Boolean canBargaining) {
        this.canBargaining = canBargaining;
        return this;
    }

    public Boolean getCanEmptyAttorn() {
        return canEmptyAttorn;
    }

    public Store setCanEmptyAttorn(Boolean canEmptyAttorn) {
        this.canEmptyAttorn = canEmptyAttorn;
        return this;
    }

    public double getOffset() {
        return offset;
    }

    public Store setOffset(double offset) {
        this.offset = offset;
        return this;
    }
}