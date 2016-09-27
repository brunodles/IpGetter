package com.github.brunodles.IpGetter

import org.gradle.api.Plugin
import org.gradle.api.Project

class IpGetter implements Plugin<Project> {
    void apply(Project project) {
        project.extensions.add("getApiUrl", getApiUrl)
    }

    def getApiUrl = { ->
        def port = 80;
        def ip = "fail.com";
        def properties = new Properties();
        def file = new File("./ip.properties");
        def String propInterface = null;
        if (file.exists()) {
            properties.load(new FileInputStream(file));
            propInterface = properties.getProperty("api.local_ip_interface");
            port = properties.getProperty("api.local_port");
        }
        for (ipInterface in [propInterface, 'wlan0', 'eth0']) {
            if (ipInterface == null) continue;
            def temp = getNetworkInterfaceIp(ipInterface);
            if (temp != null) {
                ip = temp;
                break;
            }
        }
        return "http://${ip}:${port}"
    }

    // Get the ip address by interface name
    static def getNetworkInterfaceIp(String interfaceName) {
        NetworkInterface iface = NetworkInterface.getByName(interfaceName);
        if (iface == null) return null;
        for (InterfaceAddress address : iface.getInterfaceAddresses()) {
            String ip = address.getAddress().getHostAddress()
            if (ip.length() <= 15) {
                return ip;
            }
        }
    }
}