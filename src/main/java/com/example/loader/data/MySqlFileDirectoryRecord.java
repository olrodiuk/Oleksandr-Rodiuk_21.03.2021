package com.example.loader.data;

public class MySqlFileDirectoryRecord {

  private final long contentID;
  private final String fileName;
  private final Long parentID;
  private final boolean isDirectory;

  public MySqlFileDirectoryRecord(long contentID, String fileName, Long parentID, boolean isDirectory) {

    this.contentID = contentID;
    this.fileName = fileName;
    this.parentID = parentID;
    this.isDirectory = isDirectory;
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

  @Override
  public String toString() {
    return fileName;
  }
}
