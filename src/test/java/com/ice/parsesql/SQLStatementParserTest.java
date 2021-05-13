package com.ice.parsesql;

import com.alibaba.druid.DbType;
import com.alibaba.druid.sql.SQLUtils;
import com.alibaba.druid.sql.ast.SQLStatement;
import org.apache.commons.io.FileUtils;
import org.junit.Test;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class SQLStatementParserTest {

    String filePath = "C:\\Users\\ice\\Desktop\\select-sqls";

    String fileName;

    public void parseSQL(String sql) {
        List<SQLStatement> sqlStatements = SQLUtils.parseStatements(sql, DbType.sqlserver);
        for (SQLStatement statement : sqlStatements) {
            Table table = SQLStatementParser.parse(statement);
            System.out.println();
        }
    }

    @Test
    public void parseFileByName() throws Exception {
        String fileName = "1002.sql";
        parseFile(new File(filePath + "\\" + fileName));
    }

    @Test
    public void parseFiles() throws Exception {
        for (int i = 4; i < 1948; i++) {
            parseFile(new File(filePath + "\\" + i + ".sql"));
        }
    }

    public void parseFile(File file) throws Exception {
        fileName = file.getName();
        String sql = FileUtils.readFileToString(file, StandardCharsets.UTF_8);
        System.out.println(fileName);
        parseSQL(sql);
    }
}
