package com.example.loader.dataLoader;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.example.loader.data.MySqlFileDirectoryRecord;

public class MySqlDataLoader {

  static final String DB_URL = "jdbc:mysql://localhost:3306/progforce";
  static final String USER = "root";
  static final String PASS = "";

  public List<MySqlFileDirectoryRecord> load() {
    ArrayList<MySqlFileDirectoryRecord> records = new ArrayList<>();
    try (Connection connection = DriverManager.getConnection(DB_URL, USER, PASS)) {
      try (Statement statement = connection.createStatement()) {
        try (ResultSet resultSet = statement.executeQuery("select * from san_content_999_calculated")) {
          while (resultSet.next()) {
            long contentID = resultSet.getLong("ContentID");
            String fileName = resultSet.getString("FileName");
            Long parentID = (Long) resultSet.getObject("ParentID");
            boolean isDirectory = resultSet.getBoolean("isDirectory");

            MySqlFileDirectoryRecord mySqlD = new MySqlFileDirectoryRecord(contentID, fileName, parentID, isDirectory);

            records.add(mySqlD);
          }
        }
      }
    } catch (SQLException throwables) {
      throwables.printStackTrace();
    }
    return records;
  }

}
