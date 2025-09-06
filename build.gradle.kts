//import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
//	kotlin("jvm") version "2.2.0"
	`java-library`
}

group = "dev.progames723.esll"
version = "1.0"

repositories {
	mavenCentral()
}

dependencies {

}

java {

}

tasks.compileJava {
	options.release.set(8)
}
/*
kotlin {
	this.target.compilerOptions.jvmTarget = JvmTarget.JVM_9
	jvmToolchain(21)
}

tasks.compileKotlin {
	this.compilerOptions.jvmTarget = JvmTarget.JVM_9
}*/