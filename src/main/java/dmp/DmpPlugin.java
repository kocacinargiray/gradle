/*
 * This Java source file was generated by the Gradle 'init' task.
 */
package dmp;

import org.gradle.api.Action;
import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.gradle.api.artifacts.dsl.DependencyHandler;
import org.gradle.api.internal.provider.DefaultProperty;
import org.gradle.api.internal.provider.PropertyHost;
import org.gradle.api.plugins.JavaPlugin;
import org.gradle.api.plugins.PluginContainer;
import org.gradle.api.plugins.PluginManager;
import org.gradle.api.provider.Property;
import org.gradle.api.tasks.InputFile;

import javax.swing.*;

/**
 * A simple 'hello world' plugin.
 */
public class DmpPlugin implements Plugin<Project> {

	public void apply(Project project) {
		PublishingTask publishingTask =project.getTasks().register("hello", PublishingTask.class).get();
		Property<String> stringProperty = new DefaultProperty<>(PropertyHost.NO_OP,String.class);
		stringProperty.set("nexusUrl");
		publishingTask.setServerUrl(stringProperty);

		project.getTasks().register("hello", task -> {
			task.doLast(s -> System.out.println("Hello from plugin 'dmpDen.greeting'"));

		});
		project.getTasks().register("merhaba", task -> {
			task.doLast(s -> System.out.println("Merhabaaa'"));
		});
		PluginContainer container = project.getPlugins();
		PluginManager pluginManager = project.getPluginManager();
		pluginManager.apply(JavaPlugin.class);
		project.getPlugins().apply("org.springframework.boot:2.6.2");
		DependencyHandler dependencies = project.getDependencies();
		dependencies.add(JavaPlugin.IMPLEMENTATION_CONFIGURATION_NAME,
				"org.springframework.boot:spring-boot-starter-web:2.6.2");
		dependencies.add(JavaPlugin.TEST_IMPLEMENTATION_CONFIGURATION_NAME, "org.junit.jupiter:junit-jupiter-engine");
		// dependencies.add(JavaPlugin.TEST_IMPLEMENTATION_CONFIGURATION_NAME,
		// springBootStarterTest(dependencies));

	}

}