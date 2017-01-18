package com.brunodles.ipgetter

/**
 * Created by bruno on 16/01/17.
 */
interface Provider<T> {
    boolean contains(String key)
    String get(String key)
}
