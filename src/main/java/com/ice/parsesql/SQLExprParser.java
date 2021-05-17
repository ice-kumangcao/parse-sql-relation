package com.ice.parsesql;

import com.alibaba.druid.sql.ast.SQLExpr;
import com.alibaba.druid.sql.ast.expr.*;

import java.util.*;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * @author ice
 * @date 5/7/21
 */
public class SQLExprParser {

    private Table fromTable;

    private SQLExpr expr;

    SQLExprParser(SQLExpr expr, Table fromTable) {
        this.expr = expr;
        this.fromTable = fromTable;
    }

    static List<SQLExpr> getBaseSQLExpr(SQLExpr sqlExpr) {
        List<SQLExpr> finalResult = new ArrayList<>();
        Queue<SQLExpr> temResult = new LinkedBlockingQueue<>();
        temResult.add(sqlExpr);
        while (true) {
            SQLExpr expr = temResult.poll();
            if (expr == null) {
                break;
            }
            if (isBaseSQLExpr(expr)) {
                finalResult.add(expr);
            } else {
                temResult.addAll(getChildrenSQLExpr(expr));
            }
        }
        return finalResult;
    }

    static List<SQLExpr> getChildrenSQLExpr(SQLExpr expr) {
        if (expr instanceof SQLMethodInvokeExpr) {
            return ((SQLMethodInvokeExpr) expr).getArguments();
        } else if (expr instanceof SQLCastExpr) {
            return Collections.singletonList(((SQLCastExpr) expr).getExpr());
        } else if (expr instanceof SQLBinaryOpExpr) {
            SQLBinaryOpExpr binaryOpExpr = (SQLBinaryOpExpr) expr;
            return Arrays.asList(binaryOpExpr.getLeft(), binaryOpExpr.getRight());
        }
        throw new IllegalArgumentException(expr.getClass().toString());
    }

    static boolean isBaseSQLExpr(SQLExpr expr) {
        return expr instanceof SQLValuableExpr
                || expr instanceof SQLIdentifierExpr
                || expr instanceof SQLPropertyExpr
                || expr instanceof SQLAllColumnExpr
                || expr instanceof SQLVariantRefExpr
                || expr instanceof SQLQueryExpr
                || expr instanceof SQLNCharExpr;
    }

    static List<Column> parseSQLExpr(SQLExpr expr, Table fromTable) {
        SQLExprParser parser = new SQLExprParser(expr, fromTable);
        return parser.parseSQLExpr0();
    }

    private List<Column> parseSQLExpr0() {
        if (isBaseSQLExpr(expr)) {
            if (expr instanceof SQLIdentifierExpr) {
                return parseSQLIdentifierExpr((SQLIdentifierExpr) expr);
            } else if (expr instanceof SQLPropertyExpr) {
                return parseSQLPropertyExpr((SQLPropertyExpr) expr);
            } else if (expr instanceof SQLAllColumnExpr) {
                return fromTable.getColumns();
            } else if (expr instanceof SQLIntegerExpr
                    || expr instanceof SQLCharExpr
                    || expr instanceof SQLNumberExpr) {
                return Collections.emptyList();
            }
            throw new ParseSQLException(expr.getClass().toString());
        } else {
            List<SQLExpr> sqlExprs = getBaseSQLExpr(expr);
            List<Column> sourceColumns = new ArrayList<>();
            sqlExprs.forEach(sqlExpr -> sourceColumns.addAll(parseSQLExpr(sqlExpr, fromTable)));
            return Collections.singletonList(new Column(null, sourceColumns));
        }
    }

    private List<Column> parseSQLIdentifierExpr(SQLIdentifierExpr expr) {
        String columnName = StringUtils.removeNameQuotes(expr.getName());
        Column column = fromTable.getColumn(columnName);
        return Collections.singletonList(new Column(columnName, column));
    }

    private List<Column> parseSQLPropertyExpr(SQLPropertyExpr expr) {
        String columnName = StringUtils.removeNameQuotes(expr.getName());
        String tableName = expr.getOwnerName();
        Column column = fromTable.getColumn(tableName, columnName);
        return Collections.singletonList(new Column(columnName, column));
    }
}
