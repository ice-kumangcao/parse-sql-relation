package com.ice.parsesql;

import java.util.ArrayList;
import java.util.List;

/**
 * @author ice
 * @date 5/7/21
 */
public class Table {

    String database;

    String schema;

    String table;

    List<Column> columns = new ArrayList<>();

    Table(String database, String schema, String table) {
        this.database = database;
        this.schema = schema;
        this.table = table;
    }

    public Table addColumn(Column column) {
        columns.add(column);
        return this;
    }

    public Table addColumns(List<Column> columns) {
        this.columns.addAll(columns);
        return this;
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        if (database != null) {
            stringBuilder.append(database).append(".");
        }
        if (schema != null) {
            stringBuilder.append(schema).append(".");
        }
        stringBuilder.append(table);
        return stringBuilder.toString();
    }
}
