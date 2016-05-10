package com.sony.atsc;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.sony.atsc.ATSC3WebSocketServer.ATSCWebSocket;

public class Starter {
    private static final String LICENCE =
            "Copyright (c) 2012-2013 by Paul S. Hawke, 2001,2005-2013 by Jarno Elonen, 2010 by Konstantinos Togias\n"
                + "\n"
                + "Redistribution and use in source and binary forms, with or without\n"
                + "modification, are permitted provided that the following conditions\n"
                + "are met:\n"
                + "\n"
                + "Redistributions of source code must retain the above copyright notice,\n"
                + "this list of conditions and the following disclaimer. Redistributions in\n"
                + "binary form must reproduce the above copyright notice, this list of\n"
                + "conditions and the following disclaimer in the documentation and/or other\n"
                + "materials provided with the distribution. The name of the author may not\n"
                + "be used to endorse or promote products derived from this software without\n"
                + "specific prior written permission. \n"
                + " \n"
                + "THIS SOFTWARE IS PROVIDED BY THE AUTHOR ``AS IS'' AND ANY EXPRESS OR\n"
                + "IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES\n"
                + "OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED.\n"
                + "IN NO EVENT SHALL THE AUTHOR BE LIABLE FOR ANY DIRECT, INDIRECT,\n"
                + "INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT\n"
                + "NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,\n"
                + "DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY\n"
                + "THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT\n"
                + "(INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE\n"
                + "OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.";
	
    /**
     * Starts as a standalone file server and waits for Enter.
     */
    static Listener l1;
    static Listener l2;
    static String queuel1l2;
    static String queuel2l1;    
    
    public static void main(String[] args) {
        // Defaults
    	
    	 //l1=new Listener();
    	
    	new Starter();
    	
        int port1 = 9000;
        int port2= 9001;

        String host1 = "127.0.0.1";
        String host2 = "127.0.0.1";
        
        List<File> rootDirs = new ArrayList<File>();
        boolean quiet = false;
        Map<String, String> options = new HashMap<String, String>();

        // Parse command-line, with short and long versions of the options.
        for (int i = 0; i < args.length; ++i) {
            if (args[i].equalsIgnoreCase("-h1") || args[i].equalsIgnoreCase("--host1")) {
                host1 = args[i + 1];
            } else if (args[i].equalsIgnoreCase("-p1") || args[i].equalsIgnoreCase("--port1")) {
                port1 = Integer.parseInt(args[i + 1]);
            } else if (args[i].equalsIgnoreCase("-h2") || args[i].equalsIgnoreCase("--host2")) {
                host2 = args[i + 1];
            } else if (args[i].equalsIgnoreCase("-p2") || args[i].equalsIgnoreCase("--port2")) {
                port2 = Integer.parseInt(args[i + 1]);
            } else if (args[i].equalsIgnoreCase("-q") || args[i].equalsIgnoreCase("--quiet")) {
                quiet = true;
            } else if (args[i].equalsIgnoreCase("-d1") || args[i].equalsIgnoreCase("--dir")) {
                rootDirs.add(new File(args[i + 1]).getAbsoluteFile());
            } else if (args[i].equalsIgnoreCase("--licence")) {
                System.out.println(LICENCE + "\n");
            } else if (args[i].startsWith("-X:")) {
                int dot = args[i].indexOf('=');
                if (dot > 0) {
                    String name = args[i].substring(0, dot);
                    String value = args[i].substring(dot + 1, args[i].length());
                    options.put(name, value);
                }
            }
        }

        if (rootDirs.isEmpty()) {
            rootDirs.add(new File("apps/").getAbsoluteFile());
        }
        System.out.println("Root Dir: "+rootDirs.get(0).getAbsolutePath());

        options.put("host", host1);
        options.put("port", ""+port1);
        options.put("quiet", String.valueOf(quiet));
        StringBuilder sb = new StringBuilder();
        for (File dir : rootDirs) {
            if (sb.length() > 0) {
                sb.append(":");
            }
            try {
                sb.append(dir.getCanonicalPath());
            } catch (IOException ignored) {}
        }
        options.put("home", sb.toString());

//        ServiceLoader<WebServerPluginInfo> serviceLoader = ServiceLoader.load(WebServerPluginInfo.class);
//        for (WebServerPluginInfo info : serviceLoader) {
        HTMLPlugInInfo info=new HTMLPlugInInfo();
            String[] mimeTypes = info.getMimeTypes();
            for (String mime : mimeTypes) {
                String[] indexFiles = info.getIndexFilesForMimeType(mime);
                if (!quiet) {
                    System.out.print("# Found plugin for Mime type: \"" + mime + "\"");
                    if (indexFiles != null) {
                        System.out.print(" (serving index files: ");
                        for (String indexFile : indexFiles) {
                            System.out.print(indexFile + " ");
                        }
                    }
                    System.out.println(").");
                }
                SimpleWebServer.registerPluginForMimeType(indexFiles, mime, info.getWebServerPlugin(mime), options);
            }
        //}
            SimpleWebServer s1=new SimpleWebServer(host1, port1, rootDirs, quiet,l1);

            SimpleWebServer s2=new SimpleWebServer(host2, port2, rootDirs, quiet,l2);  

        ServerRunner.executeInstance(s1);
        ServerRunner.executeInstance(s2);
        
        try {
            System.in.read();
        } catch (Throwable ignored) {
        }

        s1.stop();
        
        s2.stop();
        
        System.out.println("Servers stopped");

    }
    
    public Starter(){
    	l1=new Listener();
    	
    	l2=new Listener();

    	l1.reflect=l2;
    	l2.reflect=l1;    	
    }
    
    public class Listener {

    	public ATSCWebSocket localws;
    	public Listener reflect;
    	
    	public Listener(){

    	}
    	

    	
    	public void out(String m){
    		try {
    			if (reflect.localws!=null) reflect.localws.send(m);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    		
    	}
    	
    }

}
