package com.ice.parsesql;

import com.alibaba.druid.DbType;
import com.alibaba.druid.sql.SQLUtils;
import com.alibaba.druid.sql.ast.SQLStatement;

import java.util.List;

/**
 * @author ice
 * @date 4/29/21
 */
public class ParseSQL {

    static String sql1 = "select * from (select id from test) union all select id from test";

    static String sql2 = "select t1.c1, t2.c1 from test1 t1 left join test2 t2 on test1.id = test2.id";

    public static void main(String[] args) {
        List<SQLStatement> sqlStatements = SQLUtils.parseStatements(sql1, DbType.mysql);
        for (SQLStatement statement : sqlStatements) {
            SqlContext context = new SqlContext();
            SQLStatementParser.parse(context, statement);
            System.out.println();
        }
    }
}
