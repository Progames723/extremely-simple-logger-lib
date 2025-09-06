plugins {
	`java-library`
	`maven-publish`
	publishing
}

group = "dev.progames723.esll"
version = "1.0"

repositories {
	mavenCentral()
}

dependencies {

}

java {
	withSourcesJar()
}

publishing {
	publications {
		create<MavenPublication>("maven") {
			groupId = group as String
			artifactId = "esll"
			version = "1.0"

			from(components["java"])
		}
	}
	this.repositories.mavenLocal()
}

tasks.compileJava {
	options.release.set(8)
}