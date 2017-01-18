package com.brunodles.ipgetter

import com.brunodles.oleaster.suiterunner.OleasterSuiteRunner
import com.brunodles.test.helper.*
import com.mscharhag.oleaster.runner.StaticRunnerSupport.*
import com.nhaarman.mockito_kotlin.eq
import com.nhaarman.mockito_kotlin.mock
import org.assertj.core.api.Assertions.assertThat
import org.junit.runner.RunWith
import org.mockito.Mockito
import java.io.ByteArrayOutputStream
import java.io.PrintStream
import java.net.InetAddress
import java.net.InterfaceAddress
import java.net.NetworkInterface
import java.net.networkInterface
import java.util.*

@RunWith(OleasterSuiteRunner::class)
class ApiTest {

    val EXPECTED_ADDRESS = "http://10.0.0.42:8080"

    var api: Api? = null
    var properties: Properties? = null
    var outputStream: ByteArrayOutputStream? = null

    init {
        given(Api::class) {
            beforeEach { api = Api.from(properties) }
            after { api = null }
            with("empty properties file") {
                before { properties = Properties() }
                includeDisabledMock()

                on("#printProperties") {
                    beforeEach {
                        outputStream = ByteArrayOutputStream()
                        System.setOut(Mockito.spy(PrintStream(outputStream)))
                    }
                    it("should print the properties") {
                        api!!.printProperties()
                        assertThat(outputStream!!.toString()).isEqualTo(resAsSttring("properties_output_empty"))
                    }
                }
            }
            with("properties file that does not enables mock") {
                before { properties = Properties().loadRes("mock_api_disable.properties") }
                includeDisabledMock()

                on("#printProperties") {
                    beforeEach {
                        outputStream = ByteArrayOutputStream()
                        System.setOut(Mockito.spy(PrintStream(outputStream)))
                    }
                    it("should print the properties") {
                        api!!.printProperties()
                        assertThat(outputStream!!.toString()).isEqualTo(resAsSttring("properties_output_disable"))
                    }
                }
            }
            with("properties file that enables mock") {
                before { properties = Properties().loadRes("mock_api_enable.properties") }
                beforeEach {
                    val provider: NetworkInterfaceProvider = mock()
                    once(provider.getNetworkInterfaceIp(eq("wlan0"))).thenReturn("10.0.0.42")

                    api!!.setNetworkInterfaceProvider(provider)
                }
                on("#validProperties") {
                    it("should return true") {
                        assertThat(api!!.validProperties()).isTrue()
                    }
                }
                on("#localIpOr, from a parametrized ip") {
                    it("should return the local") {
                        assertThat(api!!.localIpOr("http://my-api.com")).isEqualTo(EXPECTED_ADDRESS)
                    }
                }
                on("#localUrl") {
                    it("should build the url based on properties file") {
                        assertThat(api!!.localUrl()).isEqualTo(EXPECTED_ADDRESS)
                    }
                }
                on("#printProperties") {
                    beforeEach {
                        outputStream = ByteArrayOutputStream()
                        System.setOut(Mockito.spy(PrintStream(outputStream)))
                    }
                    it("should print the properties") {
                        api!!.printProperties()
                        assertThat(outputStream!!.toString()).isEqualTo(resAsSttring("properties_output_enable"))
                    }
                }
            }
        }
    }

    private fun includeDisabledMock() {
        on("#validProperties") {
            it("should return false") {
                assertThat(api!!.validProperties()).isFalse()
            }
        }
        on("#localIpOr, from a parametrized address") {
            it("should return the parametrized address") {
                assertThat(api!!.localIpOr("http://my-api.com")).isEqualTo("http://my-api.com")
            }
        }
        on("#localUrl") {
            it("should return null") {
                assertThat(api!!.localUrl()).isNull()
            }
        }
    }
}