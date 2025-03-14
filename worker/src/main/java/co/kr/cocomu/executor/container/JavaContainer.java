    package co.kr.cocomu.executor.container;

    import co.kr.cocomu.executor.docker.DockerCommander;
    import co.kr.cocomu.executor.docker.DockerConstant;
    import java.nio.file.Path;
    import java.util.List;

    public class JavaContainer {

        private static final String JAVA_IMAGE = "java-code-executor";
        private static final String JAVA_COMMAND_FORMAT = """
            compilation_output=$(javac -J-Xms64m -J-Xmx128m -J-Xss256m -encoding UTF-8 Main.java 2>&1); \
            compilation_status=$?; \
            echo "$compilation_output" > compile.log; \
            if [ $compilation_status -ne 0 ]; then \
                exit 1; \
            fi && \
            
            { \
                output=$(echo '%s' | timeout 2 /usr/bin/time -f "%%e\\n%%M" java -Xms64m -Xmx128m -Xss256m -Dfile.encoding=UTF-8 -XX:+UseSerialGC Main 2>&1); \
                exit_code=$?; \
                echo "$output" > output.log; \
                if [ $exit_code -eq 124 ]; then \
                    exit 124; \
                elif [ $exit_code -ne 0 ]; then \
                    exit 2; \
                fi \
            }
            """;

        public static List<String> generate(final String containerId, final Path tempDir, final String input) {
            return DockerCommander.builder()
                .withRun()
                .withRemove()
                .withLimits(DockerConstant.DEFAULT_MEMORY, DockerConstant.DEFAULT_CPUS)
                .withSecurity()
                .withName(containerId)
                .withVolume(tempDir)
                .withWorkDir(DockerConstant.DEFAULT_WORK_DIR)
                .withImage(JAVA_IMAGE)
                .withCommand(String.format(JAVA_COMMAND_FORMAT, input))
                .build();
        }

    }
