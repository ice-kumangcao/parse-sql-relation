package com.ice.parsesql;

import com.alibaba.druid.sql.ast.statement.*;

import java.util.List;

/**
 * @author ice
 * @date 5/7/21
 */
public interface SQLTableSourceParser {

    static TableSource parse(SqlContext context, SQLTableSource tableSource) {
        if (tableSource instanceof SQLExprTableSource) {
            return parseSQLExprTableSource(context, (SQLExprTableSource) tableSource);
        } else if (tableSource instanceof SQLJoinTableSource) {
            return parseSQLJoinTableSource(context, (SQLJoinTableSource) tableSource);
        } else if (tableSource instanceof SQLSubqueryTableSource) {
            parseSQLSubqueryTableSource(context, (SQLSubqueryTableSource) tableSource);
        }
        throw new ParseSQLException(tableSource.getClass().toString());
    }

    static TableSource parseSQLSubqueryTableSource(SqlContext context, SQLSubqueryTableSource tableSource) {
        SQLSelect sqlSelect = tableSource.getSelect();
        List<Column> columns = SQLSelectParser.parse(context, sqlSelect);
        String alis = tableSource.getAlias();
        TableSource tables = new TableSource();
        Table table = new Table(null, null, alis);
        table.columns.addAll(columns);
        tables.tableNames.put(alis, table);
        return tables;
    }

    static TableSource parseSQLJoinTableSource(SqlContext context, SQLJoinTableSource tableSource) {
        TableSource leftTable = SQLTableSourceParser.parse(context, tableSource.getLeft());
        TableSource rightTable = SQLTableSourceParser.parse(context, tableSource.getRight());
        return leftTable;
    }

    static TableSource parseSQLExprTableSource(SqlContext context, SQLExprTableSource tableSource) {
        Table table = new Table(tableSource.getCatalog(), tableSource.getSchema(), tableSource.getTableName());
        TableSource tables = new TableSource();
        tables.tableNames.put(tableSource.computeAlias(), table);
        return tables;
    }
}
