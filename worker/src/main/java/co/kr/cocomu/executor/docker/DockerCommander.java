package co.kr.cocomu.executor.docker;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import lombok.experimental.Accessors;

@Accessors(fluent = true)
public class DockerCommander {

    private final List<String> commands = new ArrayList<>();

    public static DockerCommander builder() {
        return new DockerCommander().add("docker");
    }

    public DockerCommander add(final String... args) {
        commands.addAll(List.of(args));
        return this;
    }

    public DockerCommander withRun() {
        return add("run");
    }

    public DockerCommander withVolume(final Path tempDir) {
        return add("-v", tempDir.toAbsolutePath() + ":/code");
    }

    public DockerCommander withWorkDir(final String workDir) {
        return add("-w", workDir);
    }

    public DockerCommander withLimits(final String memory, final String cpus) {
        return add("--memory=" + memory, "--cpus=" + cpus);
    }

    public DockerCommander withSecurity() {
        return add("--network=none");
    }

    public DockerCommander withName(final String containerId) {
        return add("--name", containerId);
    }

    public DockerCommander withImage(final String image) {
        return add(image);
    }

    public DockerCommander withCommand(final String command) {
        return add("sh", "-c", command);
    }

    public DockerCommander withRemove() {
        return add("--rm");
    }

    public List<String> build() {
        return commands;
    }

}
