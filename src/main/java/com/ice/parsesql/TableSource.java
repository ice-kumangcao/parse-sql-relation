package com.ice.parsesql;

import java.util.*;

/**
 * @author ice
 * @date 5/9/21
 */
@Deprecated
public class TableSource {

    private Map<String, Table> tableNames = new HashMap<>();

    public void addTable(Table table) {
        tableNames.put(table.getTableName().toLowerCase(), table);
    }

    public void addTable(String tableName, Table table) {
        tableNames.put(tableName.toLowerCase(), table);
    }

    public Column getColumn(String tableName, String columnName, boolean distinct) {
        tableName = tableName.toLowerCase();
        if (!tableNames.containsKey(tableName)) {
            throw new ParseSQLException();
        }
        List<Column> columns = tableNames.get(tableName).getColumns();
        if (columns.isEmpty()) {
            //TODO: 查询table字段元数据
            if (distinct) {
                return new Column(tableNames.get(tableName), columnName);
            }
            return null;
        }
        for (Column column : columns) {
            if (column.getColumnName().equalsIgnoreCase(columnName)) {
                return column;
            }
        }
        return null;
    }

    public Column getColumn(String columnName) {
        Set<String> keys = tableNames.keySet();
        for (String key : keys) {
            Column column = getColumn(key, columnName, false);
            if (column != null) {
                return column;
            }
        }
        return new Column(getOneEmptyTable(), columnName);
    }

    public List<Column> getAllColumn() {
        List<Column> columns = new ArrayList<>();
        tableNames.forEach((key, table) -> {
            columns.addAll(table.getColumns());
        });
        return columns;
    }

    private Table getOneEmptyTable() {
        for (Map.Entry<String, Table> value : tableNames.entrySet()) {
            if (value.getValue().getColumns().isEmpty()) {
                return value.getValue();
            }
        }
        throw new ParseSQLException("not have empty table");
    }

    public void mergeTableSource(TableSource tableSource) {
        this.tableNames.putAll(tableSource.tableNames);
    }
}
