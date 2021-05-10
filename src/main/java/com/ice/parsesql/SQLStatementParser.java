package com.ice.parsesql;

import com.alibaba.druid.sql.ast.SQLStatement;
import com.alibaba.druid.sql.ast.statement.SQLSelectStatement;

import java.util.List;

public interface SQLStatementParser {

    static Table parse(SQLStatement statement) {
        if (statement instanceof SQLSelectStatement) {
            List<Column>  columns = parseSQLSelectStatement((SQLSelectStatement) statement);
            return new Table(null, null, null).addColumns(columns);
        }
        throw new ParseSQLException(statement.getClass().toString());
    }

    static List<Column> parseSQLSelectStatement(SQLSelectStatement statement) {
        return SQLSelectParser.parse(statement.getSelect());
    }
}
