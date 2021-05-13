package com.ice.parsesql;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

/**
 * @author ice
 * @date 5/8/21
 */
public class Column {

    Table table;

    String columnName;

    List<Column> fromColumn = new ArrayList<>();

    Column(Table table, String columnName) {
        this.table = table;
        this.columnName = columnName;
    }

    Column(String columnName, Column... sourceColumns) {
        this.columnName = columnName;
        fromColumn.addAll(new HashSet<>(Arrays.asList(sourceColumns)));
    }

    Column(String columnName, List<Column> columns) {
        this.columnName = columnName;
        this.fromColumn.addAll(columns);
    }

    @Override
    public String toString() {
        if (fromColumn != null && fromColumn.size() > 0) {
            return columnName + " -> " + fromColumn;
        } else {
            return table + "." + columnName;
        }
    }
}
