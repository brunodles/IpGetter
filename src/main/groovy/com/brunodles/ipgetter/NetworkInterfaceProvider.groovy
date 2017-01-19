package com.brunodles.ipgetter

interface NetworkInterfaceProvider {
    String getNetworkInterfaceIp(String interfaceName)
}