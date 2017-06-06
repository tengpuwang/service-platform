package wang.tengp.enums;

/**
 * 信息来源
 * Created by shumin on 16-10-22.
 */
public enum InfoSource {
    TENGPU("腾铺认证", 1),
    PERSONAL("个人信息", 2);

    private String name;
    private int index;

    // 构造方法
    private InfoSource(String name, int index) {
        this.name = name;
        this.index = index;
    }

    // 普通方法
    public static String getName(int index) {
        for (InfoSource c : InfoSource.values()) {
            if (c.getIndex() == index) {
                return c.name;
            }
        }
        return null;
    }

    // get set 方法
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }
}
