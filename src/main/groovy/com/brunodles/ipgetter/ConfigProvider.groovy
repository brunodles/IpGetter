package com.brunodles.ipgetter

/**
 * Created by bruno on 16/01/17.
 */
interface ConfigProvider<T> {
    boolean contains(String key)
    String get(String key)
}
