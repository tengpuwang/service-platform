package wang.tengp.common;

import com.google.common.collect.Lists;
import org.apache.commons.collections.CollectionUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by shumin on 16-10-22.
 */
public final class TreeBuilder {

    /**
     * list to tree
     *
     * @param allTreeNodes
     * @param <T>
     * @return
     */
    public static <T> List<TreeNode<T>> buildListToTree(final List<TreeNode<T>> allTreeNodes) {
        // 查找根节点
        List<TreeNode<T>> roots = findRoots(allTreeNodes);
        // 非根节点
        List<TreeNode<T>> notRoots = (List<TreeNode<T>>) CollectionUtils.subtract(allTreeNodes, roots);

        // 遍历根节点。为每个根节点设置子节点
        roots.parallelStream().forEach(root -> {
            root.setChildren(findChildren(root, notRoots));
        });

        return roots;
    }


    public static <T> List<TreeNode<T>> buildTreeList(final List<TreeNode<T>> allTreeNodes) {

        List<TreeNode<T>> result = Lists.newArrayList();

        // 查找根节点
        List<TreeNode<T>> roots = findRoots(allTreeNodes);
        // 非根节点
        List<TreeNode<T>> notRoots = (List<TreeNode<T>>) CollectionUtils.subtract(allTreeNodes, roots);

        // 遍历根节点。为每个根节点设置子节点
        roots.parallelStream().forEachOrdered(root -> {
            result.add(root);
            result.addAll(findChildren(root, notRoots));
        });
        return result;
    }

    /**
     * 查找根节点
     *
     * @param allTreeNodes
     * @param <T>
     * @return
     */
    private static <T> List<TreeNode<T>> findRoots(final List<TreeNode<T>> allTreeNodes) {
        // 根节点列表
        List<TreeNode<T>> roots = new ArrayList<TreeNode<T>>();
        // 查找根节点
        allTreeNodes.parallelStream().forEach(treeNode -> {
            // parent 为空
            if (treeNode.getParentid() == null) {
                treeNode.setLevel(0);
                treeNode.setRoot(true);
                roots.add(treeNode);
            }
        });
        // 排序
        Collections.sort(roots);
        return roots;
    }

    /**
     * 查找字子节点
     *
     * @param root
     * @param allTreeNodes
     * @param <T>
     * @return
     */
    private static <T> List<TreeNode<T>> findChildren(final TreeNode root, final List<TreeNode<T>> allTreeNodes) {
        // 子节点列表
        List<TreeNode<T>> children = new ArrayList<TreeNode<T>>();
        // 查找子节点
        allTreeNodes.parallelStream().forEach(treeNode -> {
            if (treeNode.getParentid().equals(root.getId())) {
                treeNode.setParent(root);
                treeNode.setLevel(root.getLevel() + 1);
                children.add(treeNode);
            }
        });

        List<TreeNode<T>> notChildren = (List<TreeNode<T>>) CollectionUtils.subtract(allTreeNodes, children);

        children.parallelStream().forEach(child -> {
            // 递归查询子节点
            List<TreeNode<T>> tmpChildren = findChildren(child, notChildren);
            // 没有子节点则为叶子节点
            if (tmpChildren == null || tmpChildren.size() < 1) {
                child.setLeaf(true);
            } else {
                child.setLeaf(false);
            }
            child.setChildren(tmpChildren);
        });

        // 排序
        Collections.sort(children);

        return children;
    }
}