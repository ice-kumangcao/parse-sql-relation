package com.ice.parsesql;

import com.alibaba.druid.sql.ast.statement.*;

import java.util.List;

/**
 * @author ice
 * @date 5/7/21
 */
public class SQLTableSourceParser {

    private SQLTableSource tableSource;

    SQLTableSourceParser(SQLTableSource tableSource) {
        this.tableSource = tableSource;
    }

    static Table parse(SQLTableSource tableSource) {
        SQLTableSourceParser parser = new SQLTableSourceParser(tableSource);
        return parser.parse0();
    }

    private Table parse0() {
        if (tableSource instanceof SQLExprTableSource) {
            return parseSQLExprTableSource((SQLExprTableSource) tableSource);
        } else if (tableSource instanceof SQLJoinTableSource) {
            return parseSQLJoinTableSource((SQLJoinTableSource) tableSource);
        } else if (tableSource instanceof SQLSubqueryTableSource) {
            return parseSQLSubqueryTableSource((SQLSubqueryTableSource) tableSource);
        }
        throw new ParseSQLException(tableSource.getClass().toString());
    }

    private Table parseSQLSubqueryTableSource(SQLSubqueryTableSource tableSource) {
        SQLSelect sqlSelect = tableSource.getSelect();
        List<Column> columns = SQLSelectParser.parse(sqlSelect);
        String alis = tableSource.getAlias();
        Table table = new Table(null, null, alis);
        table.getColumns().addAll(columns);
        return table;
    }

    private Table parseSQLJoinTableSource(SQLJoinTableSource tableSource) {
        Table leftTable = SQLTableSourceParser.parse(tableSource.getLeft());
        Table rightTable = SQLTableSourceParser.parse(tableSource.getRight());
        Table table = new Table(null);
        table.fromTable(leftTable, rightTable);
        return table;
    }

    private Table parseSQLExprTableSource(SQLExprTableSource tableSource) {
        Table table = new Table(tableSource.getCatalog(), tableSource.getSchema(), tableSource.getTableName());
        Table alisTable = new Table(tableSource.computeAlias());
        alisTable.fromTable(table);
        return alisTable;
    }
}
