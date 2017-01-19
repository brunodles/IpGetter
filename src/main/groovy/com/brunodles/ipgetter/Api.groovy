package com.brunodles.ipgetter

import org.gradle.api.Project

class Api {

    public static final String USE_MOCK = 'api.use_mock'
    public static final String LOCAL_IP_INTERFACE = 'api.local_ip_interface'
    public static final String LOCAL_PORT = 'api.local_port'

    private final ConfigProvider config
    private NetworkInterfaceProvider networkInterfaceProvider = new DefaultNetworkInterfaceProvider()

    private Api(ConfigProvider config) {
        this.config = config
    }

    static Api from(Properties properties) {
        return new Api(new PropertiesProvider(properties))
    }

    static Api from(Project project) {
        return new Api(new ProjectConfigProvider(project))
    }

    String localIpOr(String defaultUrl) {
        if (!validProperties()) return defaultUrl

        String propInterface = config.get(LOCAL_IP_INTERFACE)
        def port = config.get(LOCAL_PORT)
        def ip = ipFrom(propInterface)
        if (!ip) return defaultUrl
        return "http://${ip}:${port}"
    }

    boolean validProperties() {
        return (config.contains(USE_MOCK)
                && config.get(USE_MOCK).toBoolean()
                && config.contains(LOCAL_IP_INTERFACE)
                && config.contains(LOCAL_PORT))
    }

    String localUrl() {
        if (!config.contains(LOCAL_IP_INTERFACE)) return null
        String propInterface = config.get(LOCAL_IP_INTERFACE)
        def port = config.get(LOCAL_PORT)
        def ip = ipFrom(propInterface)
        if (!ip) return null
        return "http://${ip}:${port}"
    }

    void printProperties() {
        printProp config, USE_MOCK
        printProp config, LOCAL_IP_INTERFACE
        printProp config, LOCAL_PORT
        println localIpOr('Failed to get ip')
    }

    String ipFrom(String propInterface) {
        for (ipInterface in [propInterface, 'wlan0', 'eth0']) {
            if (ipInterface == null) continue
            String ip = networkInterfaceProvider.getNetworkInterfaceIp(ipInterface)
            if (ip != null) {
                return ip
            }
        }
        return null
    }

    void setNetworkInterfaceProvider(NetworkInterfaceProvider networkInterfaceProvider) {
        this.networkInterfaceProvider = networkInterfaceProvider
    }

    static def printProp(ConfigProvider provider, String name) {
        if (provider.contains(name))
            println "$name = ${provider.get(name)}"
        else
            println "$name does not exists"
    }
}
