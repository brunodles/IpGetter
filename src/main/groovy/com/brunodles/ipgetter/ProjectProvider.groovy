package com.brunodles.ipgetter

import org.gradle.api.Project

class ProjectProvider implements Provider {
    private Project project

    ProjectProvider(Project project) {
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
