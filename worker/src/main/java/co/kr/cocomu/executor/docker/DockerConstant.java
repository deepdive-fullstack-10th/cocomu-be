package co.kr.cocomu.executor.docker;

public final class DockerConstant {

    public static final String DEFAULT_MEMORY = "128m";
    public static final String DEFAULT_CPUS = "1";
    public static final String DEFAULT_WORK_DIR = "/code";

    public static final String COMPILE_EXECUTION_LOG = "compile.log";
    public static final String EXECUTION_RESULT_LOG = "output.log";
    public static final int SUCCESS_OUT_CODE = 0;
    public static final int COMPILE_ERROR_EXIT_CODE = 1;
    public static final int RUNTIME_ERROR_EXIT_CODE = 2;
    public static final int TIMEOUT_EXIT_CODE = 124;

}
