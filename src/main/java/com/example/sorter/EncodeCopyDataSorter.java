package com.example.sorter;

import com.example.loader.data.MySqlFileDirectoryRecord;
import com.example.loader.data.SortCategory;
import com.example.loader.data.TreeNode;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.example.loader.data.SortCategory.TO_COPY;
import static com.example.loader.data.SortCategory.TO_ENCODE;

public class EncodeCopyDataSorter {

  public Map<SortCategory, List<MySqlFileDirectoryRecord>> sort(List<TreeNode> rootList) {
    Map<SortCategory, List<MySqlFileDirectoryRecord>> result = Map.of(
            TO_ENCODE, new ArrayList<>(),
            TO_COPY, new ArrayList<>());


    for (TreeNode rootNode : rootList) {
      markTreeNodesToEncode(rootNode);
      markTreeNodesToCopy(rootNode);
    }

    for (TreeNode treeNode : rootList) {
      sort(treeNode, result.get(TO_COPY), result.get(TO_ENCODE));
    }

    return result;
  }

  private void markTreeNodesToCopy(TreeNode rootNode) {
    if (rootNode == null) return;
    if (TO_COPY == rootNode.getCategory()) return;
    if (TO_ENCODE == rootNode.getCategory()) return;

    if (!rootNode.getValue().isDirectory() && !isToEncodeList(rootNode.getValue())) {
      rootNode.setToCopy();
      return;
    }

    for (TreeNode child : rootNode.getChildren()) {
      markTreeNodesToCopy(child);
    }

    if (rootNode.getChildren().stream().map(TreeNode::getCategory).allMatch(TO_COPY::equals)) {
      // all child files is mapped to as 'to_copy' -> mark whole dir 'to_copy'
      rootNode.setToCopy();
    }
  }

  private void markTreeNodesToEncode(TreeNode rootNode) {
    if (rootNode == null) return;
    if (TO_COPY == rootNode.getCategory()) return;
    if (TO_ENCODE == rootNode.getCategory()) return;

    if (hasTifExtension(rootNode.getValue())) {
      rootNode.getParent().setToEncode();
      return;
    }

    if (isToEncodeList(rootNode.getValue())) {
      rootNode.setToEncode();
      return;
    }

    for (TreeNode child : rootNode.getChildren()) {
      markTreeNodesToEncode(child);
    }
  }

  public void sort(TreeNode rootNode, List<MySqlFileDirectoryRecord> toCopyList,
                   List<MySqlFileDirectoryRecord> toEncodeList) {
    if (rootNode == null) return;

    if (TO_ENCODE == rootNode.getCategory()) {
      toEncodeList.add(rootNode.getValue());
    } else if (TO_COPY == rootNode.getCategory()) {
      toCopyList.add(rootNode.getValue());
    } else {
      for (TreeNode child : rootNode.getChildren()) {
        sort(child, toCopyList, toEncodeList);
      }
    }
  }

  public static boolean isToEncodeList(MySqlFileDirectoryRecord record) {
    return record.getFileName().matches(".*\\.mov$|.*\\.avi$|.*\\.tif$");
  }

  public static boolean hasTifExtension(MySqlFileDirectoryRecord record) {
    return record.getFileName().matches(".*\\.tif$");
  }
}
