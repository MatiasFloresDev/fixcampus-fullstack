package pe.edu.upc.fixcampus;

public class ApiException extends RuntimeException {
    final int status;
    final String code;
    public ApiException(int status, String code) {
        super(code);
        this.status = status;
        this.code = code;
    }
    static ApiException missing() { return new ApiException(404, "notFound"); }
    static ApiException forbidden() { return new ApiException(403, "forbidden"); }
}
