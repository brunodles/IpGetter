package com.brunodles.ipgetter

class PropertiesProvider implements Provider<Properties> {

    private Properties properties

    PropertiesProvider(Properties properties) {
        this.properties = properties
    }

    @Override
    boolean contains(String key) {
        return properties.containsKey(key)
    }

    @Override
    String get(String key) {
        return properties.getProperty(key)
    }
}
