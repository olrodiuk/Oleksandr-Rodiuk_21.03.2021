package com.example;

import com.example.loader.data.MySqlFileDirectoryRecord;
import com.example.loader.data.SortCategory;
import com.example.loader.data.TreeNode;
import com.example.loader.dataLoader.MySqlDataLoader;
import com.example.sorter.EncodeCopyDataSorter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.example.loader.data.SortCategory.TO_COPY;
import static com.example.loader.data.SortCategory.TO_ENCODE;

public class Application {

  public static void main(String[] args) {

    MySqlDataLoader mySqlDataLoader = new MySqlDataLoader();
    List<MySqlFileDirectoryRecord> load = mySqlDataLoader.load();
    List<TreeNode> mySqlFileDirectoryRecords = buildFSTree(load);

    Map<SortCategory, List<MySqlFileDirectoryRecord>> sort = new EncodeCopyDataSorter().sort(mySqlFileDirectoryRecords);
    printResult(sort);
  }

  public static void printResult(Map<SortCategory, List<MySqlFileDirectoryRecord>> result) {
    System.out.println("Список файлов\\директорий на кодирование: ");
    result.get(TO_ENCODE)
            .forEach(System.out::println);

    System.out.println("Список файлов\\директорий на копирование: ");
    result.get(TO_COPY)
            .forEach(System.out::println);
  }

  public static List<TreeNode> buildFSTree(List<MySqlFileDirectoryRecord> data) {
    List<TreeNode> rootList = new ArrayList<>();
    Map<Long, TreeNode> allDataMap = data.stream()
            .collect(Collectors.toMap(MySqlFileDirectoryRecord::getContentID, TreeNode::new));
    for (TreeNode entry : allDataMap.values()) {
      if (entry.getParentId() == null) {
        rootList.add(entry);
      } else {
        allDataMap.get(entry.getParentId()).addChild(entry);
      }
    }
    return rootList;
  }
}
