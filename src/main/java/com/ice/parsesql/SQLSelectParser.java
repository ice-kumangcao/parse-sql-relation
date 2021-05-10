package com.ice.parsesql;

import com.alibaba.druid.sql.ast.statement.SQLSelect;

import java.util.List;

/**
 * @author ice
 * @date 5/7/21
 */
public interface SQLSelectParser {

    static List<Column> parse(SqlContext context, SQLSelect sqlSelect) {
        List<Column> columns = SQLSelectQueryParser.parse(context, sqlSelect.getQuery());
        return columns;
    }
}
