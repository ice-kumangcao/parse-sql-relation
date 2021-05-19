package com.ice.parsesql;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author ice
 * @date 5/7/21
 */
public class Table {

    private String database;

    private String schema;

    private String tableName;

    private List<Column> columns = new ArrayList<>();

    private Map<String, Table> fromTables = new HashMap<>();

    Table(String database, String schema, String table) {
        this.database = database;
        this.schema = schema;
        this.tableName = table;
    }

    Table(String alias) {
        this.tableName = alias;
    }

    private Table addColumn(Column column) {
        columns.add(column);
        return this;
    }

    public Table fromColumns(List<Column> columns) {
        columns.forEach(column -> {
            this.columns.add(new Column(this, column.getColumnName(), column));
        });
        return this;
    }

    public Table fromTable(Table... tables) {
        for (Table table : tables) {
            fromColumns(table.getColumns());
            this.fromTables.put(table.getTableName().toLowerCase(), table);
        }
        return this;
    }

    public void mergeTable(Table table) {
        this.columns.addAll(table.columns);
    }

    public String getTableName() {
        return tableName;
    }

    public List<Column> getColumns() {
        return columns;
    }

    public Column getColumn(String columnName) {
        List<Column> resultColumns = this.columns.stream().filter(column -> column.getColumnName().equalsIgnoreCase(columnName))
                .collect(Collectors.toList());
        if (resultColumns.size() == 1) {
            return resultColumns.get(0);
        }
        throw new ParseSQLException(columnName + "冲突 size:"+resultColumns.size());
    }

    public Column getColumn(String tableName, String columnName) {
        List<Column> resultColumns = this.columns.stream().filter(column ->
                column.getColumnName().equalsIgnoreCase(columnName)
                        && column.getTable().getTableName().equalsIgnoreCase(tableName))
                .collect(Collectors.toList());
        if (resultColumns.size() == 1) {
            return resultColumns.get(0);
        }
        throw new ParseSQLException(tableName + "." + columnName + "冲突 size:"+resultColumns.size());
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
        stringBuilder.append(tableName);
        return stringBuilder.toString();
    }
}
