package wang.tengp.common;

import com.alibaba.fastjson.annotation.JSONField;
import org.bson.types.ObjectId;

import java.util.List;

/**
 * 树节点
 * Created by shumin on 16-10-22.
 */
public class TreeNode<T> implements Comparable<TreeNode<T>> {

    private ObjectId id;

    private ObjectId parentid;

    @JSONField(serialize = false)
    private TreeNode<T> parent;

    private List<TreeNode<T>> children;

    private String name;

    private String description;

    private T data;

    // 排序
    private int order;

    // 层级
    private int level;

    // 是否叶子节点
    private boolean isLeaf;

    // 是否根节点
    @JSONField(serialize = false)
    private boolean isRoot;

    public TreeNode() {
    }

    public TreeNode(ObjectId id, ObjectId parentid, T data) {
        this.id = id;
        this.parentid = parentid;
        this.data = data;
    }

    public ObjectId getId() {
        return id;
    }

    public TreeNode setId(ObjectId id) {
        this.id = id;
        return this;
    }

    public ObjectId getParentid() {
        return parentid;
    }

    public TreeNode setParentid(ObjectId parentid) {
        this.parentid = parentid;
        return this;
    }

    public TreeNode<T> getParent() {
        return parent;
    }

    public TreeNode setParent(TreeNode<T> parent) {
        this.parent = parent;
        return this;
    }

    public List<TreeNode<T>> getChildren() {
        return children;
    }

    public TreeNode setChildren(List<TreeNode<T>> children) {
        this.children = children;
        return this;
    }

    public String getName() {
        return name;
    }

    public TreeNode setName(String name) {
        this.name = name;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public TreeNode setDescription(String description) {
        this.description = description;
        return this;
    }

    public T getData() {
        return data;
    }

    public TreeNode setData(T data) {
        this.data = data;
        return this;
    }

    public int getOrder() {
        return order;
    }

    public TreeNode setOrder(int order) {
        this.order = order;
        return this;
    }

    public int getLevel() {
        return level;
    }

    public TreeNode setLevel(int level) {
        this.level = level;
        return this;
    }

    public boolean isLeaf() {
        return isLeaf;
    }

    public TreeNode setLeaf(boolean leaf) {
        isLeaf = leaf;
        return this;
    }

    public boolean isRoot() {
        return isRoot;
    }

    public TreeNode setRoot(boolean root) {
        isRoot = root;
        return this;
    }

    @Override
    public int hashCode() {
        return this.id.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        TreeNode other = (TreeNode) obj;
        if (!id.equals(other.id))
            return false;
        if (!parentid.equals(other.id))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "TreeNode {id=" + id + ", parentid=" + parentid + ", name=" + name + ", children=" + children + ", level =" + level + "}";
    }

    @Override
    public int compareTo(TreeNode<T> treeNode) {
        int resule = this.getOrder() - treeNode.getOrder();
        return resule;
    }
}