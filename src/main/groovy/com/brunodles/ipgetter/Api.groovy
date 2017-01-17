package com.brunodles.ipgetter

import org.gradle.api.Project

class Api {

    public static final String USE_MOCK = 'api.use_mock'
    public static final String LOCAL_IP_INTERFACE = 'api.local_ip_interface'
    public static final String LOCAL_PORT = 'api.local_port'

    private final Provider provider

    private Api(Provider provider) {
        this.provider = provider
    }

    static Api from(Properties properties) {
        return new Api(new PropertiesProvider(properties))
    }

    static Api from(Project project) {
        return new Api(new ProjectProvider(project))
    }

    String localIpOr(String defaultUrl) {
        if (!validProperties()) return defaultUrl

        String propInterface = provider.get(LOCAL_IP_INTERFACE)
        def port = provider.get(LOCAL_PORT)
        def ip = ipFrom(propInterface)
        if (!ip) return defaultUrl
        return "http://${ip}:${port}"
    }

    boolean validProperties() {
        return (provider.contains(USE_MOCK)
                && provider.get(USE_MOCK).toBoolean()
                && provider.contains(LOCAL_IP_INTERFACE)
                && provider.contains(LOCAL_PORT))
    }

    String localUrl() {
        if (!provider.contains(LOCAL_IP_INTERFACE)) return null
        String propInterface = provider.get(LOCAL_IP_INTERFACE)
        def port = provider.get(LOCAL_PORT)
        def ip = ipFrom(propInterface)
        if (!ip) return null
        return "http://${ip}:${port}"
    }

    void printProperties() {
        printProp provider, USE_MOCK
        printProp provider, LOCAL_IP_INTERFACE
        printProp provider, LOCAL_PORT
        println localIpOr('Failed to get ip')
    }

    static String ipFrom(String propInterface) {
        for (ipInterface in [propInterface, 'wlan0', 'eth0']) {
            if (ipInterface == null) continue
            String ip = getNetworkInterfaceIp(ipInterface)
            if (ip != null) {
                return ip
            }
        }
        return null
    }

    static String getNetworkInterfaceIp(String interfaceName) {
        NetworkInterface iface = NetworkInterface.getByName(interfaceName)
        if (iface == null) return null
        for (InterfaceAddress address : iface.getInterfaceAddresses()) {
            String ip = address.getAddress().getHostAddress()
            if (ip.length() <= 15) {
                return ip
            }
        }
        return null
    }

    static def printProp(Provider provider, String name) {
        if (provider.contains(name))
            println "$name = ${provider.get(name)}"
        else
            println "$name does not exists"
    }
}
