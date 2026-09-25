package pe.edu.upc.fixcampus.exception;

public class ApiException extends RuntimeException {
    final int status;
    final String code;
    public ApiException(int status, String code) {
        super(code);
        this.status = status;
        this.code = code;
    }
    public static ApiException missing() { return new ApiException(404, "notFound"); }
    public static ApiException forbidden() { return new ApiException(403, "forbidden"); }
}
