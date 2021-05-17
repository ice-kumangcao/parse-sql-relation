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

    private Table table;

    private String columnName;

    List<Column> fromColumn = new ArrayList<>();

    Column(Table table, String columnName) {
        this.table = table;
        this.columnName = columnName;
    }

    Column(Table table, String columnName, Column... sourceColumns) {
        this.table = table;
        this.columnName = columnName;
        fromColumn.addAll(new HashSet<>(Arrays.asList(sourceColumns)));
    }

    Column(String columnName, Column... sourceColumns) {
        this.columnName = columnName;
        fromColumn.addAll(new HashSet<>(Arrays.asList(sourceColumns)));
    }

    Column(String columnName, List<Column> columns) {
        this.columnName = columnName;
        this.fromColumn.addAll(columns);
    }

    public Table getTable() {
        return table;
    }

    public String getColumnName() {
        return columnName;
    }

    public void setColumnName(String columnName) {
        this.columnName = columnName;
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
