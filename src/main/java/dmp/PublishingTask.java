package dmp;

import org.gradle.api.Action;
import org.gradle.api.DefaultTask;
import org.gradle.api.Project;
import org.gradle.api.artifacts.repositories.AuthenticationContainer;
import org.gradle.api.artifacts.repositories.PasswordCredentials;
import org.gradle.api.plugins.JavaPluginExtension;
import org.gradle.api.provider.Property;
import org.gradle.api.publish.PublishingExtension;
import org.gradle.api.publish.maven.MavenPublication;
import org.gradle.api.publish.maven.plugins.MavenPublishPlugin;
import org.gradle.api.tasks.Input;
import org.gradle.api.tasks.TaskAction;
import org.gradle.api.tasks.TaskContainer;
import org.gradle.api.tasks.TaskProvider;
import org.gradle.jvm.tasks.Jar;

import java.net.URI;

public class PublishingTask extends DefaultTask {

    @Input
    private Property<String> serverUrl;

    public Property<String> getServerUrl() {
        return serverUrl;
    }

    public void setServerUrl(Property<String> serverUrl) {
        this.serverUrl = serverUrl;
    }

    @TaskAction
    public void addPublication() {
        StringBuilder urlBuilder = new StringBuilder(this.getServerUrl().get());//"http://nexusrepo.fonetyazilim.com/repository/sbs-repo-");
        urlBuilder.append(super.getProject().getVersion().toString().endsWith("-SNAPSHOT") ? "snapshots": "releases");

        JavaPluginExtension java = super.getProject().getExtensions().getByType(JavaPluginExtension.class);

        super.getProject().getPluginManager().apply( MavenPublishPlugin.class );
        TaskContainer tasks = super.getProject().getTasks();

        TaskProvider<Jar> sourceJar = tasks.register("sourceJar", Jar.class, jar -> {
            jar.from(java.getSourceSets().getByName("main").getAllSource());
        });

        Action<PublishingExtension> publishingExtensionAction =
                publishingExtension -> {
                    publishingExtension.repositories( repos -> {
                        repos.maven( repo -> {
                            repo.setUrl( URI.create(urlBuilder.toString()));
                            repo.credentials( PasswordCredentials.class, creds -> {
                                creds.setUsername("admin");
                                creds.setPassword("Ankara1234");
                            });
                        });
                    });
                    publishingExtension.getPublications().create("mavenJava", MavenPublication.class,
                            mavenPublication -> {
                                mavenPublication.from(super.getProject().getComponents().getByName("java"));
                                mavenPublication.artifact(sourceJar.get());
                            });
                };

        super.getProject().getExtensions().configure(PublishingExtension.class,publishingExtensionAction);
    }
}
