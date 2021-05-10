package com.ice.parsesql;

import com.alibaba.druid.sql.ast.statement.SQLSelect;

import java.util.List;

/**
 * @author ice
 * @date 5/7/21
 */
public interface SQLSelectParser {

    static List<Column> parse(SQLSelect sqlSelect) {
        return SQLSelectQueryParser.parse(sqlSelect.getQuery());
    }
}
