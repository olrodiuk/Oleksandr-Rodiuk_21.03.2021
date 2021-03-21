package com.example.sorter;

import com.example.Application;
import com.example.loader.data.MySqlFileDirectoryRecord;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class EncodeCopyDataSorterTest {

  @Test
  void test1() {

        /*d1/
        d1/f1.mov
        d1/d2
        d1/d2/f2.tif
        d1/d2/f-n.tif*/
    MySqlFileDirectoryRecord d1 = new MySqlFileDirectoryRecord(0, "d1/", null, true);
    MySqlFileDirectoryRecord f1 = new MySqlFileDirectoryRecord(1, "d1/f1.mov", 0L, false);
    MySqlFileDirectoryRecord d2 = new MySqlFileDirectoryRecord(2, "d1/d2/", 0L, true);
    MySqlFileDirectoryRecord f2 = new MySqlFileDirectoryRecord(3, "d1/d2/f2.tif", 2L, false);
    MySqlFileDirectoryRecord fn = new MySqlFileDirectoryRecord(4, "d1/d2/f-n.tif", 3L, false);

    List<MySqlFileDirectoryRecord> data = Application.setRelationShips(Arrays.asList(d1, f1, d2, f2, fn));

    Map<Integer, Iterable<MySqlFileDirectoryRecord>> result = new EncodeCopyDataSorter().sort(data);

    Application.printResult(result);

    // f1, d2
    assertIterableEquals(
        Arrays.asList(f1, d2),
        result.get(EncodeCopyDataSorter.TO_ENCODE_KEY)
    );

    // TO_COPY_KEY list is empty
    assertFalse(result.get(EncodeCopyDataSorter.TO_COPY_KEY).iterator().hasNext());
  }

  @Test
  void test2() {

        /*d1/
            d1/f1.mov
            d1/f3.txt
            d1/d2
            d1/d2/f2.tif
            d1/d2/f-n.tif
            d1/d2/d3
            d1/d2/d3/f4.pdf
            */

    MySqlFileDirectoryRecord d1 = new MySqlFileDirectoryRecord(0, "d1/", null, true);
    MySqlFileDirectoryRecord f1 = new MySqlFileDirectoryRecord(1, "d1/f1.mov", 0L, false);
    MySqlFileDirectoryRecord f3 = new MySqlFileDirectoryRecord(2, "d1/f3.txt", 0L, false);
    MySqlFileDirectoryRecord d2 = new MySqlFileDirectoryRecord(3, "d1/d2/", 0L, true);
    MySqlFileDirectoryRecord f2 = new MySqlFileDirectoryRecord(4, "d1/d2/f2.tif", 3L, false);
    MySqlFileDirectoryRecord fn = new MySqlFileDirectoryRecord(5, "d1/d2/f-n.tif", 3L, false);
    MySqlFileDirectoryRecord d3 = new MySqlFileDirectoryRecord(6, "d1/d2/d3", 3L, true);
    MySqlFileDirectoryRecord f4 = new MySqlFileDirectoryRecord(7, "d1/d2/d3/f4.pdf", 6L, false);

    List<MySqlFileDirectoryRecord> data = Application.setRelationShips(Arrays.asList(d1, f1, f3, d2, f2, fn, d3, f4));

    Map<Integer, Iterable<MySqlFileDirectoryRecord>> result = new EncodeCopyDataSorter().sort(data);

    Application.printResult(result);

    // f1, d2
    assertIterableEquals(
        Arrays.asList(f1, d2),
        result.get(EncodeCopyDataSorter.TO_ENCODE_KEY)
    );

    // f3
    assertIterableEquals(
        Collections.singletonList(f3),
        result.get(EncodeCopyDataSorter.TO_COPY_KEY)
    );

  }


  @Test
  void test3() {
        /*
        d1/
        d1/f3.txt
        d1/d2
        d1/d2/d3
        d1/d2/d3/f4.pdf
        */

    MySqlFileDirectoryRecord d1 = new MySqlFileDirectoryRecord(0, "d1/", null, true);
    MySqlFileDirectoryRecord f3 = new MySqlFileDirectoryRecord(2, "d1/f3.txt", 0L, false);
    MySqlFileDirectoryRecord d2 = new MySqlFileDirectoryRecord(3, "d1/d2/", 0L, true);
    MySqlFileDirectoryRecord d3 = new MySqlFileDirectoryRecord(6, "d1/d2/d3", 3L, true);
    MySqlFileDirectoryRecord f4 = new MySqlFileDirectoryRecord(7, "d1/d2/d3/f4.pdf", 6L, false);

    List<MySqlFileDirectoryRecord> data = Application.setRelationShips(Arrays.asList(d1, f3, d2, d3, f4));

    Map<Integer, Iterable<MySqlFileDirectoryRecord>> result = new EncodeCopyDataSorter().sort(data);

    Application.printResult(result);

    // TO_COPY_KEY list is empty
    assertFalse(result.get(EncodeCopyDataSorter.TO_ENCODE_KEY).iterator().hasNext());

    // d1
    assertIterableEquals(
        Collections.singletonList(d1),
        result.get(EncodeCopyDataSorter.TO_COPY_KEY)
    );
  }


  @Test
  void test4() {
        /*
        d0/d1/
        d0/d1/f1.mov
        d0/d1/f3.txt
        d0/d1/d2/f2.tif
        d0/d1/d2/fn.tif
        d0/d1/d2/d3/f4.pdf
        d0/d4
        d0/d4/f5.xml
        d0/d4/d5
        d0/d4/d5/d6
        d0/d4/d5/d6/f6.xml
        d0/d7
        d0/d7/d8
        d0/d7/d8/f7.xml
        */

    MySqlFileDirectoryRecord d0 = new MySqlFileDirectoryRecord(0, "d0/", null, true);
    MySqlFileDirectoryRecord d1 = new MySqlFileDirectoryRecord(1, "d0/d1/", 0L, true);
    MySqlFileDirectoryRecord f1 = new MySqlFileDirectoryRecord(2, "d0/d1/f1.mov", 1L, false);
    MySqlFileDirectoryRecord f3 = new MySqlFileDirectoryRecord(3, "d0/d1/f3.txt", 1L, false);
    MySqlFileDirectoryRecord d2 = new MySqlFileDirectoryRecord(4, "d0/d1/d2/", 1L, true);
    MySqlFileDirectoryRecord f2 = new MySqlFileDirectoryRecord(5, "d0/d1/d2/f2.tif", 4L, false);
    MySqlFileDirectoryRecord fn = new MySqlFileDirectoryRecord(6, "d0/d1/d2/fn.tif", 4L, false);
    MySqlFileDirectoryRecord d3 = new MySqlFileDirectoryRecord(7, "d0/d1/d2/d3", 4L, true);
    MySqlFileDirectoryRecord f4 = new MySqlFileDirectoryRecord(8, "d0/d1/d2/d3/f4.pdf", 7L, false);
    MySqlFileDirectoryRecord d4 = new MySqlFileDirectoryRecord(9, "d0/d4", 0L, true);
    MySqlFileDirectoryRecord f5 = new MySqlFileDirectoryRecord(10, "d0/d4/f5.xml", 9L, false);
    MySqlFileDirectoryRecord d5 = new MySqlFileDirectoryRecord(11, "d0/d4/d5", 9L, true);
    MySqlFileDirectoryRecord d6 = new MySqlFileDirectoryRecord(12, "d0/d4/d5/d6", 11L, true);
    MySqlFileDirectoryRecord f6 = new MySqlFileDirectoryRecord(13, "d0/d4/d5/d6/f6.xml", 12L, false);
    MySqlFileDirectoryRecord d7 = new MySqlFileDirectoryRecord(14, "d0/d7", 0L, true);
    MySqlFileDirectoryRecord d8 = new MySqlFileDirectoryRecord(15, "d0/d7/d8", 14L, true);
    MySqlFileDirectoryRecord f7 = new MySqlFileDirectoryRecord(16, "d0/d7/d8/f7.xml", 15L, false);

    List<MySqlFileDirectoryRecord> data = Application.setRelationShips(
        Arrays.asList(d0, d1, f1, f3, d2, f2, fn, d3, f4, d4, f5, d5, d6, f6, d7, d8, f7));

    Map<Integer, Iterable<MySqlFileDirectoryRecord>> result = new EncodeCopyDataSorter().sort(data);

    Application.printResult(result);

    // f1, d2
    assertIterableEquals(
        Arrays.asList(f1, d2),
        result.get(EncodeCopyDataSorter.TO_ENCODE_KEY)
    );

    // f3
    assertIterableEquals(
        Arrays.asList(f3, d4, d7),
        result.get(EncodeCopyDataSorter.TO_COPY_KEY)
    );
  }

  @Test
  void test5() {
        /*
        d0/d1/
        d0/d1/f1.mov
        d0/d1/f3.txt
        d0/d1/d2/f2.tif
        d0/d1/d2/fn.tif
        d0/d1/d2/d3/f4.pdf
        d0/d4
        d0/d4/f5.xml
        d0/d4/d5
        d0/d4/d5/d6
        d0/d4/d5/d6/f6.xml
        d0/d7
        d0/d7/d8
        d0/d7/d8/f7.xml
        */

    MySqlFileDirectoryRecord d0 = new MySqlFileDirectoryRecord(0, "d0/", null, true);
    MySqlFileDirectoryRecord d1 = new MySqlFileDirectoryRecord(1, "d0/d1/", 0L, true);
    MySqlFileDirectoryRecord f1 = new MySqlFileDirectoryRecord(2, "d0/d1/f1.mov", 1L, false);
    MySqlFileDirectoryRecord f3 = new MySqlFileDirectoryRecord(3, "d0/d1/f3.txt", 1L, false);
    MySqlFileDirectoryRecord d2 = new MySqlFileDirectoryRecord(4, "d0/d1/d2/", 1L, true);
    MySqlFileDirectoryRecord f2 = new MySqlFileDirectoryRecord(5, "d0/d1/d2/f2.tif", 4L, false);
    MySqlFileDirectoryRecord fn = new MySqlFileDirectoryRecord(6, "d0/d1/d2/fn.tif", 4L, false);
    MySqlFileDirectoryRecord d3 = new MySqlFileDirectoryRecord(7, "d0/d1/d2/d3", 4L, true);
    MySqlFileDirectoryRecord f4 = new MySqlFileDirectoryRecord(8, "d0/d1/d2/d3/f4.pdf", 7L, false);
    MySqlFileDirectoryRecord d4 = new MySqlFileDirectoryRecord(9, "d0/d4", 0L, true);
    MySqlFileDirectoryRecord f5 = new MySqlFileDirectoryRecord(10, "d0/d4/f5.xml", 9L, false);
    MySqlFileDirectoryRecord d5 = new MySqlFileDirectoryRecord(11, "d0/d4/d5", 9L, true);
    MySqlFileDirectoryRecord d6 = new MySqlFileDirectoryRecord(12, "d0/d4/d5/d6", 11L, true);
    MySqlFileDirectoryRecord f6 = new MySqlFileDirectoryRecord(13, "d0/d4/d5/d6/f6.xml", 12L, false);
    MySqlFileDirectoryRecord d7 = new MySqlFileDirectoryRecord(14, "d0/d7", 0L, true);
    MySqlFileDirectoryRecord d8 = new MySqlFileDirectoryRecord(15, "d0/d7/d8", 14L, true);
    MySqlFileDirectoryRecord f7 = new MySqlFileDirectoryRecord(16, "d0/d7/d8/f7.xml", 15L, false);
    MySqlFileDirectoryRecord d9 = new MySqlFileDirectoryRecord(17, "d0/d7/d8/d9", 16L, true);
    MySqlFileDirectoryRecord d10 = new MySqlFileDirectoryRecord(18, "d0/d7/d8/d9/d10", 17L, true);
    MySqlFileDirectoryRecord f8 = new MySqlFileDirectoryRecord(19, "d0/d7/d8/d9/d10/f8.xml", 18L, false);

    List<MySqlFileDirectoryRecord> data = Application.setRelationShips(
        Arrays.asList(d0, d1, f1, f3, d2, f2, fn, d3, f4, d4, f5, d5, d6, f6, d7, d8, f7, d9, d10, f8));

    Map<Integer, Iterable<MySqlFileDirectoryRecord>> result = new EncodeCopyDataSorter().sort(data);

    Application.printResult(result);

    // f1, d2
    assertIterableEquals(
        Arrays.asList(f1, d2),
        result.get(EncodeCopyDataSorter.TO_ENCODE_KEY)
    );

    // f3
    assertIterableEquals(
        Arrays.asList(f3, d4, d7),
        result.get(EncodeCopyDataSorter.TO_COPY_KEY)
    );
  }
}