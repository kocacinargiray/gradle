package dmp;

import dmp.util.ReadPropertyFile;
import org.gradle.api.Action;
import org.gradle.api.JavaVersion;
import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.gradle.api.artifacts.repositories.MavenArtifactRepository;
import org.gradle.api.artifacts.repositories.PasswordCredentials;
import org.gradle.api.plugins.JavaPlugin;
import org.gradle.api.plugins.JavaPluginConvention;
import org.gradle.api.plugins.JavaPluginExtension;
import org.gradle.api.publish.PublishingExtension;
import org.gradle.api.publish.maven.MavenPublication;
import org.gradle.api.publish.maven.plugins.MavenPublishPlugin;
import org.gradle.api.tasks.TaskContainer;
import org.gradle.api.tasks.TaskProvider;
import org.gradle.jvm.tasks.Jar;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URI;
import java.util.HashMap;

public class GradleParentPlugin implements Plugin<Project> {
    @Override
    public void apply(Project project) {
        try {
            addConfiguration(project);
            addRepositories(project);
            addPublication(project);
            addDependencies(project);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    void addConfiguration(Project project) throws IOException {
        ReadPropertyFile readPropertyFile = new ReadPropertyFile();
        HashMap<String, String> configurationMap = readPropertyFile.readFileProperty("configuration.properties");
        configurationMap.entrySet().stream().forEach(e -> {
            if(e.getKey().equals("javaVersion")) {
                project.getExtensions().getByType(JavaPluginExtension.class).
                        setTargetCompatibility(JavaVersion.toVersion(Integer.valueOf(e.getValue()).intValue()));
            }
            else {
                project.getPlugins().apply(e.getValue());
            }
        });
    }


    void addRepositories(Project project) throws IOException {
        project.getRepositories().mavenLocal();

        ReadPropertyFile readPropertyFile = new ReadPropertyFile();
        HashMap<String, String> repositoryMap = readPropertyFile.readFileProperty("repository.properties");

        repositoryMap.entrySet().stream().forEach(e -> {
            Action<MavenArtifactRepository> mavenNexus = mavenArtifactRepository -> {
                mavenArtifactRepository.setName(e.getKey());
                mavenArtifactRepository.setAllowInsecureProtocol(true);
                mavenArtifactRepository.setUrl(e.getValue());
            };
            project.getRepositories().maven(mavenNexus);
        });
    }

    void addDependencies(Project project) throws IOException {

        ReadPropertyFile readPropertyFile = new ReadPropertyFile();
        HashMap<String, String> dependencyMap = readPropertyFile.readFileProperty("dependency.properties");

        dependencyMap.entrySet().stream().forEach(e ->
                project.getDependencies().
                        add(JavaPlugin.IMPLEMENTATION_CONFIGURATION_NAME,
                                e.getKey().trim() +":"+e.getValue())
        );
    }

    public void addPublication(Project project) {

        ReadPropertyFile readPropertyFile = new ReadPropertyFile();
        HashMap<String, String> publishRepositoryMap =
                readPropertyFile.readFileProperty("publishRepository.properties");


        Action<PublishingExtension> publishingExtensionAction =
                publishingExtension -> {
                    publishingExtension.repositories( repos ->
                            publishRepositoryMap.entrySet().stream().forEach(e -> repos.maven(repo -> {
                        repo.setName(e.getKey());
                        repo.setUrl( URI.create(e.getValue()));
                        repo.credentials( PasswordCredentials.class);
                    })));
                    publishingExtension.getPublications().create("mavenJava", MavenPublication.class,
                            mavenPublication -> {
                                mavenPublication.from(project.getComponents().getByName("java"));
                            });
        };
        project.getExtensions().configure(PublishingExtension.class,publishingExtensionAction);
    }
}
