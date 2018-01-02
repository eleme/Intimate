package me.ele.intimate.plugin

import com.android.build.gradle.AppExtension
import org.gradle.api.Plugin
import org.gradle.api.Project

class IntimatePlugin implements Plugin<Project> {
    void apply(Project project) {
        def android = project.extensions.findByType(AppExtension)
        android.registerTransform(new IntimateTransform(project))
    }
}