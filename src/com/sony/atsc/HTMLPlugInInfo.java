package com.sony.atsc;


public class HTMLPlugInInfo implements WebServerPluginInfo {
	
    private static final String[] MIME_TYPES = {"text/html","application/javascript"};


	@Override
	public String[] getIndexFilesForMimeType(String mime) {
		// TODO Auto-generated method stub
		if (mime=="text/html"){
		String[] s={"index.html"};
		
		return s;
		}
		return null;
	}

	@Override
	public String[] getMimeTypes() {
		// TODO Auto-generated method stub
		
	
		return MIME_TYPES;
	}

	@Override
	public WebServerPlugin getWebServerPlugin(String mimeType) {
		// TODO Auto-generated method stub
		for (String m :MIME_TYPES){
			if (mimeType==m) {
				System.out.println("html plug in created");
				return new HTMLPlugIn();
			}
		}
		return null;
	}

}
