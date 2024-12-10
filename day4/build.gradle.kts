plugins {
    id("buildsrc.convention.kotlin-jvm")
    application
}

dependencies {
    implementation(project(":utils"))
}

application {
    mainClass = "org.nobody.day4.AppKt"
}
