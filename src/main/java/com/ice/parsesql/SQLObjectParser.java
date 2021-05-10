package com.ice.parsesql;

/**
 * @author ice
 * @date 5/7/21
 */
public interface SQLObjectParser {

    default boolean parse() {
        return false;
    }
}
