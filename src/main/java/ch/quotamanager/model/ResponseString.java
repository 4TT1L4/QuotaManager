package ch.quotamanager.model;

public class ResponseString {
	
	private final String response;

	private ResponseString(String response) {
		this.response = response;
	}

	public String getResponse(){
		return response;
	}
	
	public static ResponseString create(Object response) {
		if (response == null){
			return null;
		}
		
		return new ResponseString(response.toString());
	}
}
