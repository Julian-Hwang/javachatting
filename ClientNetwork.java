package com.aaa.client;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;

public class ClientNetwork extends Thread {
	//채팅 clientgui 객체이다.
	private Clientgui clientgui;
	
	//서버와 연결된 socket을 담당한다.
	private Socket socket;
	
	private BufferedReader br;
	private PrintWriter pw;
	
	private String id;
	
	//serversocket과 연결된 socket을 멤버변수에 저장하고 출력스트림 객체를 생성한다.
	public ClientNetwork(Clientgui clientgui, String ip, int port){
		this.clientgui=clientgui;
		
		id=clientgui.getIdname();
		
		try {
			socket = new Socket(ip,port);
			pw = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	//Thread 실행을 위한 run함수 이다.
	@Override
	public void run() {
		String message = null;
		try {
			br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			
			//서버에 접속한 후 채팅입장여부를 확인하는 문장을 전송한다.
			message = "id"+":"+id;
			pw.println(message);
			pw.flush();
			
			//무한루프를 돌며 서버로부터 수신되는 메시지를 표시한다.
			//만약 socket와의 연결이 끊어지면 thread가 정지된다.
			while (true) {
				message=br.readLine();
				clientgui.getChatviewer().append(message+"\n");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public PrintWriter getPw() {
		return pw;
	}
	public void setPw(PrintWriter pw) {
		this.pw = pw;
	}
}
