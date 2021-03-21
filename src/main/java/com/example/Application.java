package com.example;

import com.example.loader.data.MySqlFileDirectoryRecord;
import com.example.loader.dataLoader.MySqlDataLoader;
import com.example.sorter.EncodeCopyDataSorter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class Application {

  public static void main(String[] args) {

    MySqlDataLoader mySqlDataLoader = new MySqlDataLoader();
    List<MySqlFileDirectoryRecord> load = mySqlDataLoader.load();
    List<MySqlFileDirectoryRecord> mySqlFileDirectoryRecords = setRelationShips(load);

    Map<Integer, Iterable<MySqlFileDirectoryRecord>> sort = new EncodeCopyDataSorter().sort(mySqlFileDirectoryRecords);
    printResult(sort);
  }

  public static void printResult(Map<Integer, Iterable<MySqlFileDirectoryRecord>> result) {
    System.out.println("Список файлов\\директорий на кодирование: ");
    result.get(EncodeCopyDataSorter.TO_ENCODE_KEY)
        .forEach(System.out::println);

    System.out.println("Список файлов\\директорий на копирование:  ");
    result.get(EncodeCopyDataSorter.TO_COPY_KEY)
        .forEach(System.out::println);
  }

  public static List<MySqlFileDirectoryRecord> setRelationShips(List<MySqlFileDirectoryRecord> data) {
    List<MySqlFileDirectoryRecord> rootList = new ArrayList<>();
    // set parent-child relationship
    Map<Long, MySqlFileDirectoryRecord> allDataMap = data.stream()
        .collect(Collectors.toMap(MySqlFileDirectoryRecord::getContentID, Function.identity()));
    for (MySqlFileDirectoryRecord entry : allDataMap.values()) {
      if (entry.getParentID() == null) {
        rootList.add(entry);
      } else {
        allDataMap.get(entry.getParentID()).addChild(entry);
      }
    }
    return rootList;
  }
}
