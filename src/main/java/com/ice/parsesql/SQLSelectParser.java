package com.ice.parsesql;

import com.alibaba.druid.sql.ast.statement.SQLSelect;
import com.alibaba.druid.sql.ast.statement.SQLWithSubqueryClause;

import java.util.List;

/**
 * @author ice
 * @date 5/7/21
 */
public interface SQLSelectParser {

    static List<Column> parse(SQLSelect sqlSelect) {
        //TODO: 解析with语句
        SQLWithSubqueryClause sqlWithSubquery  = sqlSelect.getWithSubQuery();
        return SQLSelectQueryParser.parse(sqlSelect.getQuery());
    }
}
