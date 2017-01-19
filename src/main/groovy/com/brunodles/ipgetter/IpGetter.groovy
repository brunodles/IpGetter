package com.brunodles.ipgetter

import com.brunodles.auto.gradleplugin.AutoPlugin
import org.gradle.api.Plugin
import org.gradle.api.Project

@AutoPlugin
class IpGetter implements Plugin<Project> {
    void apply(Project project) {
        project.extensions.add("localIpOr", localIpOr(project))
        project.task('printIpProps') {
            doLast {
                getProperties(project, './ip.properties').printProperties()
            }
        }
    }

    static def localIpOr(Project project) {
        return { String defaultUrl ->
            return getProperties(project, './ip.properties').localIpOr(defaultUrl)
        }
    }

    static def getProperties(Project project, String propertiesFilePath) {
        File file = new File(propertiesFilePath)
        if (file.exists()) {
            Properties properties = new Properties()
            properties.load(new FileReader(file))
            return Api.from(properties)
        }
        return Api.from(project)
    }

}
