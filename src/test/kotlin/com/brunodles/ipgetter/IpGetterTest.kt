package com.brunodles.ipgetter

import com.brunodles.oleaster.suiterunner.OleasterSuiteRunner
import com.brunodles.test.helper.context
import com.brunodles.test.helper.given
import com.brunodles.test.helper.xgiven
import com.mscharhag.oleaster.runner.StaticRunnerSupport.beforeEach
import com.mscharhag.oleaster.runner.StaticRunnerSupport.it
import org.assertj.core.api.Assertions.assertThat
import org.gradle.api.Project
import org.gradle.api.Task
import org.gradle.testfixtures.ProjectBuilder
import org.junit.runner.RunWith

@RunWith(OleasterSuiteRunner::class)
class IpGetterTest {
    var project: Project? = null

    init {
        given(Project::class) {
            beforeEach { project = ProjectBuilder.builder().build() }
            context("apply ${IpGetter::class.java.simpleName} plugin") {
                beforeEach { project!!.pluginManager.apply("ipgetter") }
                it("should add an extension function to get the IP") {
                    assertThat(project!!.extensions.getByName("localIpOr")).isInstanceOf(String::class.java)
                }
                it("should add a task to print properties") {
                    assertThat(project!!.task("printIpProps")).isInstanceOf(Task::class.java)
                }
            }
        }
    }
}