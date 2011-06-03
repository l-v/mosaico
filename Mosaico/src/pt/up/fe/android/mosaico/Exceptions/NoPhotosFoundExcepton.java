package pt.up.fe.android.mosaico.Exceptions;

public class NoPhotosFoundExcepton extends Exception {

	private static final long serialVersionUID = -5548245773296495823L;

	private String message;
	public NoPhotosFoundExcepton(String message)
	{
		this.message = message;
	}
	
	public NoPhotosFoundExcepton()
	{
		this.message = "No Photos were found. Try again";
	}
	public String getMessage()
	{
		return message;
	}
}
