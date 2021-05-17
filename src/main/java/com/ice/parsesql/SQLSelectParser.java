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
        TableSource tableSource = null;
        if (sqlSelect.getWithSubQuery() != null) {
            tableSource = parseSQLWithSubQuery(sqlSelect.getWithSubQuery());
        }
        return SQLSelectQueryParser.parse(sqlSelect.getQuery(), tableSource);
    }

    static TableSource parseSQLWithSubQuery(SQLWithSubqueryClause sqlWithSubquerys) {
        List<SQLWithSubqueryClause.Entry> entries = sqlWithSubquerys.getEntries();
        TableSource tableSource = new TableSource();
        entries.forEach(entry -> tableSource.addTable(parseSQLWithSubqueryEntry(entry)));
        return tableSource;
    }

    static Table parseSQLWithSubqueryEntry(SQLWithSubqueryClause.Entry entry) {
        String tableName = entry.getAlias();
        List<Column> columns = SQLSelectParser.parse(entry.getSubQuery());
        return new Table(null, null, tableName).fromColumns(columns);
    }

}
