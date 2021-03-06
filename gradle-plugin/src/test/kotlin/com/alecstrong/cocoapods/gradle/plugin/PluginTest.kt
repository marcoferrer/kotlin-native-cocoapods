package com.alecstrong.cocoapods.gradle.plugin

import com.google.common.truth.Truth.assertThat
import org.gradle.testkit.runner.GradleRunner
import org.junit.Test
import java.io.File

class PluginTest {
  @Test
  fun `running generatePodspec in an empty repo works correctly`() {
    val fixtureName = "sample"
    val fixtureRoot = File("src/test/$fixtureName")
    val runner = GradleRunner.create()
        .withProjectDir(fixtureRoot)
        .withPluginClasspath()

    val podspec = File(fixtureRoot, "$fixtureName.podspec")
    assertThat(podspec.exists()).isFalse()

    runner.withArguments("generatePodspec", "--stacktrace").build()

    assertThat(podspec.exists()).isTrue()
    podspec.delete()
  }

  @Test
  fun `running initializeFramework successfully copies the dummy framework over`() {
    val fixtureName = "sample"
    val fixtureRoot = File("src/test/$fixtureName")
    val runner = GradleRunner.create()
        .withProjectDir(fixtureRoot)
        .withPluginClasspath()


    val framework = File(fixtureRoot, "build/sample.framework").apply { deleteRecursively() }
    val dsym = File(fixtureRoot, "build/sample.framework.dSYM").apply { deleteRecursively() }

    runner.withArguments(
        "-P${InitializeFrameworkTask.FRAMEWORK_PROPERTY}=$fixtureName.framework", "initializeFramework", "--stacktrace"
    ).build()

    assertThat(framework.exists()).isTrue()
    assertThat(dsym.exists()).isTrue()

    framework.deleteRecursively()
    dsym.deleteRecursively()
  }

  @Test
  fun `run createIosDebugArtifacts`() {
    val fixtureName = "sample"
    val fixtureRoot = File("src/test/$fixtureName")
    val runner = GradleRunner.create()
        .withProjectDir(fixtureRoot)
        .withPluginClasspath()

    val framework = File(fixtureRoot, "build/sample.framework").apply { deleteRecursively() }
    val dsym = File(fixtureRoot, "build/sample.framework.dSYM").apply { deleteRecursively() }

    runner.withArguments(
        "-P${CocoapodsPlugin.POD_FRAMEWORK_DIR_ENV}=${fixtureRoot.absolutePath}/build", "createIosDebugArtifacts", "--stacktrace"
    ).build()

    assertThat(framework.exists()).isTrue()
    assertThat(dsym.exists()).isTrue()

    framework.deleteRecursively()
    dsym.deleteRecursively()
  }
}
