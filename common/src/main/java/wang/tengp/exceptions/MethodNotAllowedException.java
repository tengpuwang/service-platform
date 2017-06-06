package wang.tengp.exceptions;

/**
 * An exception which corresponds to an HTTP 405 error.
 */
public final class MethodNotAllowedException extends PlatformException {
  private static final long serialVersionUID = 1L;

  public MethodNotAllowedException() {
    super();
  }
  
  public MethodNotAllowedException(String e) {
    super(e);
  }
  
  public MethodNotAllowedException(Exception e) {
    super(e);
  }
}