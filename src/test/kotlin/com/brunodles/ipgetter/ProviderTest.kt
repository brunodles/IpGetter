package com.brunodles.ipgetter

import com.brunodles.oleaster.suiterunner.OleasterSuiteRunner
import com.brunodles.test.helper.given
import com.brunodles.test.helper.on
import com.brunodles.test.helper.once
import com.brunodles.test.helper.with
import com.mscharhag.oleaster.runner.StaticRunnerSupport.*
import com.nhaarman.mockito_kotlin.mock
import org.assertj.core.api.Assertions.assertThat
import org.gradle.api.Project
import org.junit.runner.RunWith
import org.mockito.ArgumentMatchers.eq
import java.lang.reflect.ParameterizedType
import java.util.*


private val TEST_KEY: String = "this.is.my.key"
private val TEST_VALUE: String = "Wow, this is a property"

abstract class ProviderTest<PROVIDER : Provider<SOURCE>, SOURCE>(
        providerBuilder: (SOURCE) -> PROVIDER,
        sourceBuilderGood: () -> SOURCE,
        sourceBuilderBad: () -> SOURCE) {

    var source: SOURCE? = null
    var provider: PROVIDER? = null
    var containsResult = false
    var getResult: String? = null

    init {
        given(aProviderImplementation()) {
            beforeEach { provider = providerBuilder(source!!) }
            on("#contains") {
                beforeEach { containsResult = provider!!.contains(TEST_KEY) }
                with("empty content") {
                    before { source = sourceBuilderBad() }
                    it("should return false") {
                        assertThat(containsResult).isFalse()
                    }
                }
                with("content") {
                    before { source = sourceBuilderGood() }
                    it("should return true") {
                        assertThat(containsResult).isTrue()
                    }
                }
            }
            on("#get") {
                beforeEach { getResult = provider!!.get(TEST_KEY) }
                with("empty content") {
                    before { source = sourceBuilderBad() }
                    it("should return null") {
                        assertThat(getResult).isNull()
                    }
                }
                with("content") {
                    before { source = sourceBuilderGood() }
                    it("should return the value") {
                        assertThat(getResult).isEqualTo(TEST_VALUE)
                    }
                }
            }
        }
    }

    @Suppress("UNCHECKED_CAST")
    private fun aProviderImplementation(): String {
        return ((this.javaClass.genericSuperclass as ParameterizedType).actualTypeArguments[0] as Class<PROVIDER>).simpleName
    }
}

@RunWith(OleasterSuiteRunner::class)
class ProjectProviderTest : ProviderTest<ProjectProvider, Project>(
        ::ProjectProvider,
        {
            val source: Project = mock()
            once(source.hasProperty(eq(TEST_KEY))).thenReturn(true)
            once(source.property(eq(TEST_KEY))).thenReturn(TEST_VALUE)
            source
        },
        {
            val source: Project = mock()
            once(source.hasProperty(eq(TEST_KEY))).thenReturn(false)
            once(source.property(eq(TEST_KEY))).thenReturn(null)
            source
        })

@RunWith(OleasterSuiteRunner::class)
class PropertiesProviderTest : ProviderTest<PropertiesProvider, Properties>(
        ::PropertiesProvider,
        {
            val source: Properties = mock()
            once(source.containsKey(eq(TEST_KEY))).thenReturn(true)
            once(source.getProperty(eq(TEST_KEY))).thenReturn(TEST_VALUE)
            source
        },
        {
            val source: Properties = mock()
            once(source.containsKey(eq(TEST_KEY))).thenReturn(false)
            once(source.getProperty(eq(TEST_KEY))).thenReturn(null)
            source
        })