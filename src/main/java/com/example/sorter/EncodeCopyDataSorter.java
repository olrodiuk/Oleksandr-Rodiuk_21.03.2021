package com.example.sorter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.example.loader.data.MySqlFileDirectoryRecord;

public class EncodeCopyDataSorter {

  public static final int TO_COPY_KEY = 1;
  public static final int TO_ENCODE_KEY = 0;

  public static boolean isToEncodeList(MySqlFileDirectoryRecord record) {
    return record.getFileName().matches(".*\\.mov$|.*\\.avi$|.*\\.tif$");
  }

  public static boolean hasTifExtension(MySqlFileDirectoryRecord record) {
    return record.getFileName().matches(".*\\.tif$");
  }

  public static List<MySqlFileDirectoryRecord> getAllParents(MySqlFileDirectoryRecord record) {
    if (record.getParent() == null) return Collections.emptyList();
    List<MySqlFileDirectoryRecord> result = new ArrayList<>();
    getAllParents(record, result);
    return result;
  }

  private static void getAllParents(MySqlFileDirectoryRecord record, List<MySqlFileDirectoryRecord> parents) {
    if (record == null) return;

    MySqlFileDirectoryRecord parent = record.getParent();
    getAllParents(parent, parents);

    if (parent != null) {
      parents.add(parent);
    }
  }

  public Map<Integer, Iterable<MySqlFileDirectoryRecord>> sort(List<MySqlFileDirectoryRecord> rootList) {
    Map<Integer, Map<Long, MySqlFileDirectoryRecord>> result = Map.of(
        TO_ENCODE_KEY, new HashMap<>(),
        TO_COPY_KEY, new HashMap<>());


    for (MySqlFileDirectoryRecord rootNode : rootList) {
      sort(rootNode, result);
    }

    List<MySqlFileDirectoryRecord> values = new ArrayList<>(result.get(TO_COPY_KEY).values());
    for (MySqlFileDirectoryRecord toCopyRecord : values) {
      normalizeToCopyMap(toCopyRecord, result.get(TO_COPY_KEY), result.get(TO_ENCODE_KEY));
    }

    Map<Integer, Iterable<MySqlFileDirectoryRecord>> res = new HashMap<>();
    for (Map.Entry<Integer, Map<Long, MySqlFileDirectoryRecord>> entry : result.entrySet()) {
      res.put(entry.getKey(), entry.getValue().values());
    }

    return res;
  }

  private boolean normalizeToCopyMap(MySqlFileDirectoryRecord rootNode,
      Map<Long, MySqlFileDirectoryRecord> toCopyMap,
      Map<Long, MySqlFileDirectoryRecord> toEncodeMap) {
    if (rootNode == null) return false;

    MySqlFileDirectoryRecord parent = rootNode.getParent();

    //проверяем есть ли запись в списке на кодирования
    if (isNotInEncodeMap(rootNode, toEncodeMap)) {

      //если список уже содержит папку не делаем ничего
      if (!isMapNotContainsParent(rootNode, toCopyMap)) {
        return false;
      }

      //рекурсия для схлопывания к самой верхней папке
      if (normalizeToCopyMap(parent, toCopyMap, toEncodeMap)) {
        //удаляем дочерние елементы
        removeAllChildren(parent, toCopyMap);

        //добавляем самый верхний уровень, если нету еще в списке
        if (isMapNotContainsParent(rootNode, toCopyMap)) {
          toCopyMap.put(parent.getContentID(), parent);
        }
        return false;

      } else {

        //только для папок если не содержится папка в списке на кодированиеб не самый верхний уровень
        if (parent != null && isMapNotContainsParent(rootNode, toCopyMap)) {
          toCopyMap.put(parent.getContentID(), rootNode);
        }

      }
    }
    return true;
  }

  private boolean isMapNotContainsParent(MySqlFileDirectoryRecord rootNode, Map<Long, MySqlFileDirectoryRecord> toCopyMap) {
    return getAllParents(rootNode).stream()
        .map(MySqlFileDirectoryRecord::getContentID)
        .noneMatch(toCopyMap::containsKey);
  }

  private boolean isNotInEncodeMap(MySqlFileDirectoryRecord rootNode, Map<Long, MySqlFileDirectoryRecord> toEncodeMap) {
    return toEncodeMap.values()
        .stream()
        .flatMap(e -> getAllParents(e).stream())
        .distinct()
        .map(MySqlFileDirectoryRecord::getContentID)
        .noneMatch(e -> e.equals(rootNode.getParentID()));
  }

  private void sort(MySqlFileDirectoryRecord rootNode,
      Map<Integer, Map<Long, MySqlFileDirectoryRecord>> result) {
    if (rootNode == null) return;

    for (MySqlFileDirectoryRecord child : rootNode.getChildren()) {
      sort(child, result);
    }

    Map<Long, MySqlFileDirectoryRecord> toCopyMap = result.get(TO_COPY_KEY);
    Map<Long, MySqlFileDirectoryRecord> toEncodeMap = result.get(TO_ENCODE_KEY);
    if (hasTifExtension(rootNode)) {
      if (!toEncodeMap.containsKey(rootNode.getParentID())) {
        toCopyMap.remove(rootNode.getParentID());
        removeAllChildren(rootNode, toEncodeMap);
        toEncodeMap.put(rootNode.getParentID(), rootNode.getParent());
      }
    } else if (isToEncodeList(rootNode)) {
      toEncodeMap.put(rootNode.getContentID(), rootNode);
    } else if (!rootNode.isDirectory() && isNotInEncodeResult(rootNode, toEncodeMap)) {
      toCopyMap.put(rootNode.getContentID(), rootNode);
    }
  }

  private boolean isNotInEncodeResult(MySqlFileDirectoryRecord rootNode,
      Map<Long, MySqlFileDirectoryRecord> toEncodeMap) {
    return isMapNotContainsParent(rootNode, toEncodeMap);
  }

  private void removeAllChildren(MySqlFileDirectoryRecord rootNode, Map<Long, MySqlFileDirectoryRecord> map) {
    if (rootNode == null) return;
    for (MySqlFileDirectoryRecord child : rootNode.getChildren()) {
      removeAllChildren(child, map);
    }
    map.remove(rootNode.getContentID());
  }

}
