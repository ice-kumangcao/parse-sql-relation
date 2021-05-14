package com.ice.parsesql;

import com.alibaba.druid.sql.ast.statement.*;

import java.util.List;

/**
 * @author ice
 * @date 5/7/21
 */
public interface SQLTableSourceParser {

    static TableSource parse(SQLTableSource tableSource) {
        if (tableSource instanceof SQLExprTableSource) {
            return parseSQLExprTableSource((SQLExprTableSource) tableSource);
        } else if (tableSource instanceof SQLJoinTableSource) {
            return parseSQLJoinTableSource((SQLJoinTableSource) tableSource);
        } else if (tableSource instanceof SQLSubqueryTableSource) {
            return parseSQLSubqueryTableSource((SQLSubqueryTableSource) tableSource);
        }
        throw new ParseSQLException(tableSource.getClass().toString());
    }

    static TableSource parseSQLSubqueryTableSource(SQLSubqueryTableSource tableSource) {
        SQLSelect sqlSelect = tableSource.getSelect();
        List<Column> columns = SQLSelectParser.parse(sqlSelect);
        String alis = tableSource.getAlias();
        TableSource tables = new TableSource();
        Table table = new Table(null, null, alis);
        table.columns.addAll(columns);
        tables.addTable(alis, table);
        return tables;
    }

    static TableSource parseSQLJoinTableSource(SQLJoinTableSource tableSource) {
        TableSource leftTable = SQLTableSourceParser.parse(tableSource.getLeft());
        TableSource rightTable = SQLTableSourceParser.parse(tableSource.getRight());
        leftTable.mergeTableSource(rightTable);
        return leftTable;
    }

    static TableSource parseSQLExprTableSource(SQLExprTableSource tableSource) {
        Table table = new Table(tableSource.getCatalog(), tableSource.getSchema(), tableSource.getTableName());
        TableSource tables = new TableSource();
        tables.addTable(tableSource.computeAlias(), table);
        return tables;
    }
}
