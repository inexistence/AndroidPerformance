plugins {
    `kotlin-dsl`
    `groovy`
}
repositories {
    google()
    mavenCentral()
    jcenter()
}

dependencies {
//    testImplementation("junit:junit:4.13")
    implementation(gradleApi())
    implementation(localGroovy())
    implementation(kotlin("stdlib"))
    // 需要同步修改rootProject/build.gradle中的版本号
    implementation("com.android.tools.build:gradle:8.2.0-alpha07")
    implementation("org.ow2.asm:asm-util:9.2")
    implementation("org.ow2.asm:asm-commons:9.2")
}