package com.brunodles.ipgetter

interface NetworkInterfaceProvider {
    NetworkInterface getByName(String name)
}