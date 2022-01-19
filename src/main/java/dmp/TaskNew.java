//package dmp;
//
//import org.gradle.api.Action;
//import org.gradle.api.DefaultTask;
//import org.gradle.api.Project;
//import org.gradle.api.artifacts.repositories.MavenArtifactRepository;
//import org.gradle.api.artifacts.repositories.PasswordCredentials;
//import org.gradle.api.file.RegularFileProperty;
//import org.gradle.api.plugins.JavaPlugin;
//import org.gradle.api.plugins.JavaPluginExtension;
//import org.gradle.api.publish.PublishingExtension;
//import org.gradle.api.publish.maven.MavenPublication;
//import org.gradle.api.publish.maven.plugins.MavenPublishPlugin;
//import org.gradle.api.tasks.InputFile;
//import org.gradle.api.tasks.TaskAction;
//import org.gradle.api.tasks.TaskContainer;
//import org.gradle.api.tasks.TaskProvider;
//import org.gradle.jvm.tasks.Jar;
//
//import java.io.BufferedReader;
//import java.io.File;
//import java.io.FileReader;
//import java.io.IOException;
//import java.net.URI;
//
//public abstract class TaskNew extends DefaultTask {
//
//	@InputFile
//	final RegularFileProperty dependenciesFile = super.getProject().getObjects().fileProperty().convention
//			(super.getProject().getLayout().getProjectDirectory().file("dependencies.txt"));
//
//	@InputFile
//	final RegularFileProperty otherFile = super.getProject().getObjects().fileProperty().convention
//			(super.getProject().getLayout().getProjectDirectory().file("dependencies.txt"));;
//
//	@InputFile
//	final RegularFileProperty repositoryFile = super.getProject().getObjects().fileProperty().convention
//			(super.getProject().getLayout().getProjectDirectory().file("repository.txt"));
//
//	@TaskAction
//	void addDependencies(Project project) throws IOException {
//		System.out.println("From my TaskNew");
//
//		File file = dependenciesFile.getAsFile().get();
//		FileReader fr = new FileReader(file);
//		BufferedReader br = new BufferedReader(fr);
//		String line;
//
//		while ((line=br.readLine()) != null) {
//			String[] splitedStrings = line.split("=");
//			String dependencies = splitedStrings[0].trim() +":"+ splitedStrings[1].trim();
//			project.getDependencies().add(JavaPlugin.IMPLEMENTATION_CONFIGURATION_NAME, dependencies);
//		}
//
//		/*TaskContainer tasks = project.getTasks();
//		project.getExtensions().configure( PublishingExtension.class, ext -> {
//			ext.repositories( repos -> {
//				repos.maven( repo -> {
//					repo.setUrl( URI.create( s3 ) );
//				} );
//			} );
//			ext.publications( pubs -> {
//				TaskProvider<Jar> provider = tasks.register( "sources", Jar.class, jar -> {
//					jar.getArchiveClassifier( jar.getName() );
//				});
//			});
//		});
//
//		final Jar sourcesJar = (Jar) project.getTasks().getByName(JavaLibraryPlugin.);
//		Task sourceJar = project.getTasks().create("sourceJar", Jar.class, jar -> {
//			jar.from(java.getSourceSets().getByName("main").getAllSource());
//		});
//
//		Action< PublishingExtension > publishingExtensionAction =
//				publishingExtension -> {
//					publishingExtension.getPublications().withType(MavenPublication.class,
//							new Action<MavenPublication>() {
//								@Override
//								public void execute(MavenPublication mavenPublication) {
//
//									mavenPublication.artifact(sourceJar, mavenArtifact -> {
//										mavenArtifact.setClassifier("sources");
//									});
//
//								}
//							}
//							);};
//
//		project.getExtensions().configure(PublishingExtension.class,publishingExtensionAction);*/
//	}
//
//	@TaskAction
//	void configureRepositories(Project project) throws IOException {
//		System.out.println("From my TaskNew");
//		project.getRepositories().mavenLocal();
//
//		File file = repositoryFile.getAsFile().get();
//		FileReader fr = new FileReader(file);
//		BufferedReader br = new BufferedReader(fr);
//		String line;
//
//		while ((line=br.readLine()) != null) {
//			String[] splitedStrings = line.split("=");
//			Action<MavenArtifactRepository> mavenNexus = mavenArtifactRepository -> {
//				mavenArtifactRepository.setName(splitedStrings[0]);
//				mavenArtifactRepository.setAllowInsecureProtocol(true);
//				mavenArtifactRepository.setUrl(splitedStrings[1]);
//			};
//			project.getRepositories().maven(mavenNexus);
//		}
//	}
//
//	@TaskAction
//	void addOtherField(Project project) throws IOException {
//		System.out.println("From my TaskNew");
//
//		File file = dependenciesFile.getAsFile().get();
//		FileReader fr = new FileReader(file);
//		BufferedReader br = new BufferedReader(fr);
//		String line;
//
//		while ((line=br.readLine()) != null) {
//			String[] splitedStrings = line.split("=");
//			String dependencies = splitedStrings[0].trim() +":"+ splitedStrings[1].trim();
//			project.getDependencies().add(JavaPlugin.IMPLEMENTATION_CONFIGURATION_NAME, dependencies);
//		}
//	}
//
//	public void appPublication(Project project) {
//		StringBuilder urlBuilder = new StringBuilder("http://nexusrepo.fonetyazilim.com/repository/sbs-repo-");
//		urlBuilder.append(project.getVersion().toString().endsWith("-SNAPSHOT") ? "snapshots": "releases");
//
//		JavaPluginExtension java = project.getExtensions().getByType(JavaPluginExtension.class);
//
//		project.getPluginManager().apply( MavenPublishPlugin.class );
//		TaskContainer tasks = project.getTasks();
//
//		TaskProvider<Jar> sourceJar = tasks.register("sourceJar", Jar.class, jar -> {
//			jar.from(java.getSourceSets().getByName("main").getAllSource());
//		});
//		/*Action<PublicationContainer> publicationContainerAction =
//				publications ->
//				{MavenPublication p = publications.create("", MavenPublication.class,
//						publication -> {
//							publication.from(project.getComponents().getByName("java"));
//							publication.artifact(sourceJar.get());
//						});
//				};
//
//		GradleDSLHelper.publications(project, publicationContainerAction);*/
//
//
//		Action<PublishingExtension> publishingExtensionAction =
//				publishingExtension -> {
//					publishingExtension.repositories( repos -> {
//						repos.maven( repo -> {
//							repo.setUrl( URI.create(urlBuilder.toString()));
//							repo.credentials( PasswordCredentials.class, creds -> {
//								creds.setUsername("admin");
//								creds.setPassword("Ankara1234");
//							} );
//						} );
//					} );
//					publishingExtension.getPublications().create("publish",MavenPublication.class,
//							mavenPublication -> {
//								mavenPublication.from(project.getComponents().getByName("java"));
//								mavenPublication.artifact(sourceJar.get());
//							});
//		};
//
//		project.getExtensions().configure(PublishingExtension.class,publishingExtensionAction);
//	}
//}
