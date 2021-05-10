package com.ice.parsesql;

import com.alibaba.druid.sql.ast.SQLExpr;
import com.alibaba.druid.sql.ast.expr.SQLIdentifierExpr;
import com.alibaba.druid.sql.ast.expr.SQLPropertyExpr;
import com.alibaba.druid.sql.ast.statement.*;

import java.util.ArrayList;
import java.util.List;

/**
 * @author ice
 * @date 5/7/21
 */
public interface SQLSelectQueryParser {

    static List<Column> parse(SqlContext context, SQLSelectQuery sqlSelectQuery) {
        if (sqlSelectQuery instanceof SQLSelectQueryBlock) {
            return parseSQLSelectQueryBlock(context, (SQLSelectQueryBlock) sqlSelectQuery);
        } else if (sqlSelectQuery instanceof SQLUnionQuery) {
            return parseSQLUnionQuery(context, (SQLUnionQuery) sqlSelectQuery);
        }
        return new ArrayList<>();
    }

    static List<Column> parseSQLUnionQuery(SqlContext context, SQLUnionQuery sqlUnionQuery) {
        List<Column> leftColumns = SQLSelectQueryParser.parse(context, sqlUnionQuery.getLeft());
        List<Column> rightColumns = SQLSelectQueryParser.parse(context, sqlUnionQuery.getRight());
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

    static List<Column> parseSQLSelectQueryBlock(SqlContext context, SQLSelectQueryBlock sqlSelectQueryBlock) {
        List<Column> columns = new ArrayList<>();
        List<SQLSelectItem> items = sqlSelectQueryBlock.getSelectList();
        SQLTableSource tableSource = sqlSelectQueryBlock.getFrom();
        SQLTableSourceParser.parse(context, tableSource);
        items.forEach(item -> columns.add(parseSQLSelectItem(item, context)));
        return columns;
    }

    static Column parseSQLSelectItem(SQLSelectItem item, SqlContext context) {
//        List<SQLExpr> exprs = SQLExprParser.getBaseSQLExpr(item.getExpr());
        SQLExpr expr = item.getExpr();
        String alias = item.getAlias();
        if (expr instanceof SQLIdentifierExpr) {
            String columnName = ((SQLIdentifierExpr) expr).getName();
            Column column = context.getColumn(columnName);
            if (alias == null) {
                alias = columnName;
            }
            return new Column(alias, column);
        } else if (expr instanceof SQLPropertyExpr) {
            SQLPropertyExpr sqlPropertyExpr = (SQLPropertyExpr) expr;
            String columnName = sqlPropertyExpr.getName();
            String tableName = sqlPropertyExpr.getOwnerName();
            Column column = context.getColumn(tableName, columnName);
            if (alias == null) {
                alias = columnName;
            }
            return new Column(alias, column);
        }
        return null;
    }
}
