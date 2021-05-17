package com.ice.parsesql;

import com.alibaba.druid.sql.ast.SQLExpr;
import com.alibaba.druid.sql.ast.expr.SQLAllColumnExpr;
import com.alibaba.druid.sql.ast.expr.SQLIdentifierExpr;
import com.alibaba.druid.sql.ast.expr.SQLMethodInvokeExpr;
import com.alibaba.druid.sql.ast.expr.SQLPropertyExpr;
import com.alibaba.druid.sql.ast.statement.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author ice
 * @date 5/7/21
 */
public class SQLSelectQueryParser {

    private SQLSelectQuery sqlSelectQuery;

    private TableSource tableSource;

    SQLSelectQueryParser(SQLSelectQuery sqlSelectQuery) {
        this.sqlSelectQuery = sqlSelectQuery;
    }

    SQLSelectQueryParser(SQLSelectQuery sqlSelectQuery, TableSource tableSource) {
        this.sqlSelectQuery = sqlSelectQuery;
        this.tableSource = tableSource;
    }

    static List<Column> parse(SQLSelectQuery sqlSelectQuery, TableSource tableSource) {
        SQLSelectQueryParser parser = new SQLSelectQueryParser(sqlSelectQuery, tableSource);
        return parser.parse0();
    }

    private List<Column> parse0() {
        if (sqlSelectQuery instanceof SQLSelectQueryBlock) {
            return parseSQLSelectQueryBlock((SQLSelectQueryBlock) sqlSelectQuery);
        } else if (sqlSelectQuery instanceof SQLUnionQuery) {
            return parseSQLUnionQuery((SQLUnionQuery) sqlSelectQuery);
        }
        throw new ParseSQLException(sqlSelectQuery.getClass().toString());
    }

    private List<Column> parseSQLUnionQuery(SQLUnionQuery sqlUnionQuery) {
        List<Column> leftColumns = SQLSelectQueryParser.parse(sqlUnionQuery.getLeft(), tableSource);
        List<Column> rightColumns = SQLSelectQueryParser.parse(sqlUnionQuery.getRight(), tableSource);
        if (leftColumns.size() != rightColumns.size()) {
            throw new ParseSQLException();
        }
        int columnSize = leftColumns.size();
        List<Column> columns = new ArrayList<>();
        for (int i = 0; i < columnSize; i++) {
            columns.add(new Column(leftColumns.get(i).getColumnName(), leftColumns.get(i), rightColumns.get(i)));
        }
        return columns;
    }

    private List<Column> parseSQLSelectQueryBlock(SQLSelectQueryBlock sqlSelectQueryBlock) {
        List<Column> columns = new ArrayList<>();
        List<SQLSelectItem> items = sqlSelectQueryBlock.getSelectList();
        SQLTableSource tableSource = sqlSelectQueryBlock.getFrom();
        Table fromTable = SQLTableSourceParser.parse(tableSource);
        items.forEach(item -> columns.addAll(parseSQLSelectItem(item, fromTable)));
        return columns;
    }

    private List<Column> parseSQLSelectItem(SQLSelectItem item, Table table) {
        SQLExpr expr = item.getExpr();
        String alias = item.getAlias();
        List<Column> columns = SQLExprParser.parseSQLExpr(expr, table);
        if (expr instanceof SQLAllColumnExpr) {
            return columns;
        } else {
            if (alias != null && columns.size() == 1) {
                columns.get(0).setColumnName(alias);
            }
            if (columns.isEmpty()) {
                return Collections.singletonList(new Column(alias));
            } else if (columns.size() == 1) {
                return columns;
            }
            throw new ParseSQLException("columns size error");
        }
    }
}
