package com.ice.parsesql;

import com.alibaba.druid.sql.ast.SQLExpr;
import com.alibaba.druid.sql.ast.expr.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * @author ice
 * @date 5/7/21
 */
public class SQLExprParser {

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
                finalResult.addAll(getBaseExpr(expr));
            }
        }
        return finalResult;
    }

    static List<SQLExpr> getBaseExpr(SQLExpr expr) {
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
}
