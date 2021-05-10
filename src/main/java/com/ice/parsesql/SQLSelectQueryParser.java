package com.ice.parsesql;

import com.alibaba.druid.sql.ast.SQLExpr;
import com.alibaba.druid.sql.ast.expr.SQLAllColumnExpr;
import com.alibaba.druid.sql.ast.expr.SQLIdentifierExpr;
import com.alibaba.druid.sql.ast.expr.SQLPropertyExpr;
import com.alibaba.druid.sql.ast.statement.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author ice
 * @date 5/7/21
 */
public interface SQLSelectQueryParser {

    static List<Column> parse(SQLSelectQuery sqlSelectQuery) {
        if (sqlSelectQuery instanceof SQLSelectQueryBlock) {
            return parseSQLSelectQueryBlock((SQLSelectQueryBlock) sqlSelectQuery);
        } else if (sqlSelectQuery instanceof SQLUnionQuery) {
            return parseSQLUnionQuery((SQLUnionQuery) sqlSelectQuery);
        }
        throw new ParseSQLException(sqlSelectQuery.getClass().toString());
    }

    static List<Column> parseSQLUnionQuery(SQLUnionQuery sqlUnionQuery) {
        List<Column> leftColumns = SQLSelectQueryParser.parse(sqlUnionQuery.getLeft());
        List<Column> rightColumns = SQLSelectQueryParser.parse(sqlUnionQuery.getRight());
        if (leftColumns.size() != rightColumns.size()) {
            throw new ParseSQLException();
        }
        int columnSize = leftColumns.size();
        List<Column> columns = new ArrayList<>();
        for (int i = 0; i < columnSize; i++) {
            columns.add(new Column(leftColumns.get(i).columnName, leftColumns.get(i), rightColumns.get(i)));
        }
        return columns;
    }

    static List<Column> parseSQLSelectQueryBlock(SQLSelectQueryBlock sqlSelectQueryBlock) {
        List<Column> columns = new ArrayList<>();
        List<SQLSelectItem> items = sqlSelectQueryBlock.getSelectList();
        SQLTableSource tableSource = sqlSelectQueryBlock.getFrom();
        TableSource fromTables = SQLTableSourceParser.parse(tableSource);
        items.forEach(item -> columns.addAll(parseSQLSelectItem(item, fromTables)));
        return columns;
    }

    static List<Column> parseSQLSelectItem(SQLSelectItem item, TableSource tableSource) {
//        List<SQLExpr> exprs = SQLExprParser.getBaseSQLExpr(item.getExpr());
        SQLExpr expr = item.getExpr();
        String alias = item.getAlias();
        if (expr instanceof SQLIdentifierExpr) {
            String columnName = ((SQLIdentifierExpr) expr).getName();
            Column column = tableSource.getColumn(columnName);
            if (alias == null) {
                alias = columnName;
            }
            return Collections.singletonList(new Column(alias, column));
        } else if (expr instanceof SQLPropertyExpr) {
            SQLPropertyExpr sqlPropertyExpr = (SQLPropertyExpr) expr;
            String columnName = sqlPropertyExpr.getName();
            String tableName = sqlPropertyExpr.getOwnerName();
            Column column = tableSource.getColumn(tableName, columnName);
            if (alias == null) {
                alias = columnName;
            }
            return Collections.singletonList(new Column(alias, column));
        } else if (expr instanceof SQLAllColumnExpr) {
            return tableSource.getAllColumn();
        }
        throw new ParseSQLException(expr.getClass().toString());
    }
}
