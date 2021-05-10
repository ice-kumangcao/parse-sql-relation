package com.ice.parsesql;

import com.alibaba.druid.sql.ast.SQLStatement;
import com.alibaba.druid.sql.ast.statement.SQLSelectStatement;

import java.util.List;

public interface SQLStatementParser {

    static boolean parse(SqlContext context, SQLStatement statement) {
        if (statement instanceof SQLSelectStatement) {
            List<Column>  columns = parseSQLSelectStatement(context, (SQLSelectStatement) statement);
            return true;
        }
        return false;
    }

    static List<Column> parseSQLSelectStatement(SqlContext context, SQLSelectStatement statement) {
        return SQLSelectParser.parse(context, statement.getSelect());
    }
}
