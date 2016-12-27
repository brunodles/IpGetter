package com.brunodles.ipgetter

import org.gradle.api.Project

class Api {

    public static final String USE_MOCK = 'api.use_mock'
    public static final String LOCAL_IP_INTERFACE = 'api.local_ip_interface'
    public static final String LOCAL_PORT = 'api.local_port'

    private final def project

    Api(Project project) {
        this.project = project
    }

    Api(Properties properties) {
        this.project = properties
    }

    String localIpOr(String defaultUrl) {
        if (!validProperties()) return defaultUrl

        String propInterface = project.property(LOCAL_IP_INTERFACE)
        def port = project.property(LOCAL_PORT)
        def ip = ipFrom(propInterface)
        if (!ip) return defaultUrl
        return "http://${ip}:${port}"
    }

    boolean validProperties() {
        return (project.hasProperty(USE_MOCK)
                && project.property(USE_MOCK).toBoolean()
                && project.hasProperty(LOCAL_IP_INTERFACE)
                && project.hasProperty(LOCAL_PORT))
    }

    def localUrl() {
        if (project.hasProperty(LOCAL_IP_INTERFACE)) {
            String propInterface = project.property(LOCAL_IP_INTERFACE)
            def port = project.property(LOCAL_PORT)
            def ip = ipFrom(propInterface)
            if (!ip) return null
            return "http://${ip}:${port}"
        }
        return null
    }

    void printProperties() {
        printProp project, USE_MOCK
        printProp project, LOCAL_IP_INTERFACE
        printProp project, LOCAL_PORT
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

    static def printProp(def project, String name) {
        if (project.hasProperty(name))
            println name + "=" + project.property(name)
        else
            println "$name does not exists"
    }
}
