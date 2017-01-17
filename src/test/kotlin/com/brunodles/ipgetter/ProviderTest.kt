package com.brunodles.ipgetter

import com.brunodles.oleaster.suiterunner.OleasterSuiteRunner
import com.brunodles.test.helper.*
import com.mscharhag.oleaster.runner.StaticRunnerSupport.*
import com.nhaarman.mockito_kotlin.mock
import org.assertj.core.api.Assertions.assertThat
import org.gradle.api.Project
import org.junit.runner.RunWith
import org.mockito.ArgumentMatchers.eq

@RunWith(OleasterSuiteRunner::class)
class ProviderTest {

    private val TEST_KEY: String = "this.is.my.key"
    private val TEST_VALUE: String = "Wow, this is a property"

    var project: Project? = null
    var provider: Provider? = null
    var containsResult = false
    var getResult: String? = null

    init {
        given(ProjectProvider::class) {
            beforeEach { provider = ProjectProvider(project) }
            on("#contains") {
                beforeEach { containsResult = provider!!.contains(TEST_KEY) }
                with("empty content") {
                    mockProject_bad()
                    it("should return false") {
                        assertThat(containsResult).isFalse()
                    }
                }
                with("content") {
                    mockProject_good()
                    it("should return true") {
                        assertThat(containsResult).isTrue()
                    }
                }
            }
           on("#get") {
                beforeEach { getResult = provider!!.get(TEST_KEY) }
                with("empty content") {
                    mockProject_bad()
                    it("should return null") {
                        assertThat(getResult).isNull()
                    }
                }
                with("content") {
                    mockProject_good()
                    it("should return the value") {
                        assertThat(getResult).isEqualTo(TEST_VALUE)
                    }
                }
            }
        }
    }

    private fun mockProject_good() {
        before {
            project = mock()
            once(project!!.hasProperty(eq(TEST_KEY))).thenReturn(true)
            once(project!!.property(eq(TEST_KEY))).thenReturn(TEST_VALUE)
        }
    }

    private fun mockProject_bad() {
        before {
            project = mock()
            once(project!!.hasProperty(eq(TEST_KEY))).thenReturn(false)
            once(project!!.property(eq(TEST_KEY))).thenReturn(null)
        }
    }
}