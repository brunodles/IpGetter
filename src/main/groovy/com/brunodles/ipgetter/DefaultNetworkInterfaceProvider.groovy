package com.brunodles.ipgetter

class DefaultNetworkInterfaceProvider implements NetworkInterfaceProvider {

    @Override
    NetworkInterface getByName(String name) {
        return NetworkInterface.getByName(name)
    }
}
