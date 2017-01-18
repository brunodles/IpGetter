package com.brunodles.ipgetter

class DefaultNetworkInterfaceProvider implements NetworkInterfaceProvider {

    @Override
    String getNetworkInterfaceIp(String interfaceName) {
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
}
