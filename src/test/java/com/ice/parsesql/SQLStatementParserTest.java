package com.ice.parsesql;

import com.alibaba.druid.DbType;
import com.alibaba.druid.sql.SQLUtils;
import com.alibaba.druid.sql.ast.SQLStatement;
import org.junit.Test;

import java.util.List;

public class SQLStatementParserTest {

    static String sql1 = "select * from (select id from test) union all select id from test";

    static String sql2 = "select t1.c1, t2.c1 from test1 t1 left join test2 t2 on test1.id = test2.id";

    @Test
    public void test() {
        List<SQLStatement> sqlStatements = SQLUtils.parseStatements(sql2, DbType.mysql);
        for (SQLStatement statement : sqlStatements) {
            Table table = SQLStatementParser.parse(statement);
            System.out.println();
        }
    }
}
