package com.sony.atsc;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Map;

import com.sony.atsc.NanoHTTPD.IHTTPSession;
import com.sony.atsc.NanoHTTPD.Response;

public class HTMLPlugIn implements WebServerPlugin{


		@Override
		public boolean canServeUri(String uri, File rootDir) {
			// TODO Auto-generated method stub
			return true;
		}

		@Override
		public void initialize(Map<String, String> commandLineOptions) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public Response serveFile(String uri, Map<String, String> headers,
				IHTTPSession session, File file, String mimeType) {
			// TODO Auto-generated method stub
			
	        FileInputStream data;
			try {
				data = new FileInputStream (file.getAbsolutePath());
		        return new Response(Response.Status.OK, "HTML",  data);
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	        return null;

		}
    	
    
}
