package com.brunodles.ipgetter

interface ConfigProvider<T> {
    boolean contains(String key)
    String get(String key)
}
