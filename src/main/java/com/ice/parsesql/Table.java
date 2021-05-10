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

    public void addColumn(Column column) {
        columns.add(column);
    }
}
