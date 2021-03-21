package com.example.loader.data;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class MySqlFileDirectoryRecord {

  private final long contentID;
  private final String fileName;
  private final Long parentID;
  private final boolean isDirectory;

  private MySqlFileDirectoryRecord parent;

  private List<MySqlFileDirectoryRecord> children;

  public MySqlFileDirectoryRecord(long contentID, String fileName, Long parentID, boolean isDirectory) {

    this.contentID = contentID;
    this.fileName = fileName;
    this.parentID = parentID;
    this.isDirectory = isDirectory;
    this.children = new ArrayList<>();
  }

  public long getContentID() {
    return contentID;
  }

  public String getFileName() {
    return fileName;
  }

  public Long getParentID() {
    return parentID;
  }

  public boolean isDirectory() {
    return isDirectory;
  }

  public MySqlFileDirectoryRecord getParent() {
    return parent;
  }

  public MySqlFileDirectoryRecord setParent(MySqlFileDirectoryRecord parent) {
    this.parent = parent;
    return this;
  }

  public MySqlFileDirectoryRecord setChildren(List<MySqlFileDirectoryRecord> children) {
    this.children = children;
    return this;
  }

  public List<MySqlFileDirectoryRecord> getChildren() {
    return Collections.unmodifiableList(children);
  }

  public void addChild(MySqlFileDirectoryRecord value) {
    children.add(value);
    value.parent = this;
  }

  @Override
  public String toString() {
    return fileName;
  }

}
