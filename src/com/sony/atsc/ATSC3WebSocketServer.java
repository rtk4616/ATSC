package com.sony.atsc;


import java.io.IOException;

import com.sony.atsc.Starter.Listener;
import com.sony.atsc.NanoWSD.WebSocketFrame.CloseCode;


public class ATSC3WebSocketServer extends NanoWSD{
	int localPort;

    protected Listener listener; 
    public ATSC3WebSocketServer(String host, int port){
    	super(host, port);
    	localPort=port;

    }

	
	ATSCWebSocket ws;


	

	@Override
	protected WebSocket openWebSocket(IHTTPSession handshake) {
		System.out.println( "OPEN WEBSOCKET");
			ws=new ATSCWebSocket(handshake,localPort, listener);
			if (listener!=null)
				listener.localws=ws;
			return ws;

	}
	
	
	public static class ATSCWebSocket extends WebSocket{
		int localPort;
		public Listener listener;

		public ATSCWebSocket(IHTTPSession handshakeRequest) {
			super(handshakeRequest);

			// TODO Auto-generated constructor stub
		}

		public ATSCWebSocket(IHTTPSession handshakeRequest, int port, Listener l) {
			super(handshakeRequest);
			localPort=port;
			if (l!=null)
				listener=l;
			
			// TODO Auto-generated constructor stub
		}
		@Override
		protected void onOpen() {
			// TODO Auto-generated method stub
			System.out.println(  "OPEN");
			
		}

		@Override
		protected void onClose(CloseCode code, String reason,
				boolean initiatedByRemote) {
			System.out.println(  "CLOSE");
			// TODO Auto-generated method stub
			
		}
		

		@Override
		protected void onMessage(WebSocketFrame message) {
			System.out.println(  "MESSAGE RECEIVED from Port: "+ localPort+ " that says: "+ message.getTextPayload());
			if (listener!=null){
				listener.out(message.getTextPayload());  //send to the reflected websocket
			}
				
			
		}

		@Override
		protected void onPong(WebSocketFrame pong) {
			// TODO Auto-generated method stub
			
		}

		@Override
		protected void onException(IOException exception) {
			// TODO Auto-generated method stub
			
		}
		
	}


}
