package com.ice.parsesql;

import org.junit.Assert;
import org.junit.Test;

public class StringUtilsTest {

    @Test
    public void test() {
        String str = StringUtils.removeNameQuotes("[test]");
        Assert.assertNotNull(str);
    }
}
