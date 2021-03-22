package com.example.loader.data;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TreeNode {

    private TreeNode parent;

    private SortCategory category;

    private final List<TreeNode> children;

    private final MySqlFileDirectoryRecord value;

    public TreeNode(MySqlFileDirectoryRecord value) {
        this.children = new ArrayList<>();
        this.value = value;
        this.category = SortCategory.UNKNOWN;
    }

    public TreeNode getParent() {
        return parent;
    }

    public List<TreeNode> getChildren() {
        return Collections.unmodifiableList(children);
    }

    public void addChild(TreeNode value) {
        children.add(value);
        value.parent = this;
    }

    public SortCategory getCategory() {
        return category;
    }

    public void setToEncode() {
        category = SortCategory.TO_ENCODE;
    }

    public void setToCopy() {
        category = SortCategory.TO_COPY;
    }

    public MySqlFileDirectoryRecord getValue() {
        return value;
    }

    public Long getParentId() {
        return getValue().getParentID();
    }
}
