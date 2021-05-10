package com.ice.parsesql;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author ice
 * @date 5/9/21
 */
public class TableSource {

    Map<String, Table> tableNames = new HashMap<>();


    public Column getColumn(String tableName, String columnName) {
        if (!tableNames.containsKey(tableName)) {
            throw new ParseSQLException();
        }
        List<Column> columns = tableNames.get(tableName).columns;
        if (columns.isEmpty()) {
            //TODO: 查询table字段元数据
            return new Column(tableNames.get(tableName), columnName);
        }
        for (Column column : columns) {
            if (column.columnName.equalsIgnoreCase(columnName)) {
                return column;
            }
        }
        return null;
    }

    public Column getColumn(String columnName) {
        Set<String> keys = tableNames.keySet();
        for (String key : keys) {
            Column column = getColumn(key, columnName);
            if (column != null) {
                return column;
            }
        }
        return null;
    }
}
