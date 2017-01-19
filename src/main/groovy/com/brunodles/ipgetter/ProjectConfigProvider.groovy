package com.brunodles.ipgetter

import org.gradle.api.Project

class ProjectConfigProvider implements ConfigProvider<Project> {
    private Project project

    ProjectConfigProvider(Project project) {
        this.project = project
    }

    @Override
    boolean contains(String key) {
        return project.hasProperty(key)
    }

    @Override
    String get(String key) {
        return project.property(key)
    }
}
