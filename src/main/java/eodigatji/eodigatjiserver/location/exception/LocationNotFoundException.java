package eodigatji.eodigatjiserver.location.exception;

public class LocationNotFoundException extends RuntimeException {

    public LocationNotFoundException(Long id) {
        super("보관장소를 찾을 수 없습니다. id=" + id);
    }
}
