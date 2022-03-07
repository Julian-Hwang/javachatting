package com.aaa.server;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;


public class ServerNetwork extends Thread{
	//포트번호가 들러올 수 있는 소켓과 
	//데이터가 다녀갈 수 있는 input과 output, Severgui클래스를 선언한다.
	//채팅서버의 gui를 담당하는 Servergui 객체와 
	//채팅클라이언트를 저장하는 Map<클라이언트ID, 출력스트림객체> 객체를 지정한다.
	private Map<String, PrintWriter> clientMap;
	private Servergui servergui;
	private Socket socket;
	
	private BufferedReader br;
	private String id;
	
	private boolean condition=true;
	
	public ServerNetwork(Map<String, PrintWriter>clientMap, Servergui servergui, Socket socket) {
		this.clientMap=clientMap;
		this.servergui=servergui;
		this.socket=socket;
	}
	
	//Thread 실행을 위한 run함수
	@Override
	public void run() {
		String message = null;
		String receivedMsg[] = null;
		
		try {
			//ServerSocket에서 반환되어 온 Socket 객체에서 입출력 스트림 객체를 불러온다.
			br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			PrintWriter pw = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));
			
			//receivedMsg[0]	receivedMsg[1]		receivedMsg[2]
			//id				<채팅 아이디>							//채팅방에서 입장할 때 전달된다.
			//contents			<채팅 아이디>		<채팅 내용>			//채팅방에서 진행되는 채팅내용이 전달된다.
			//end				<채팅 아이디>							//채팅방에서 탈퇴시 전달된다.
			while (condition) {
				message = br.readLine();
				receivedMsg = message.split(":");
				if (receivedMsg[0].equals("id")) {
					//채팅 클라이언트를 저장하는 Map 객체에 id와 출력 스티림 객체를 저장한다.
					clientMap.put(receivedMsg[1], pw);
					//servergui로 출력한다.
					servergui.getChats().append(receivedMsg[1]+" 님이 연결되었습니다.\n");
					//다른 클라이언트로 메시지를 발송한다.
					broadcast(receivedMsg[1] + " 님이 연결되었습니다.");
				} else if (receivedMsg[0].equals("contents")) {
					//servergui로 출력한다.
					servergui.getChats().append("["+receivedMsg[1]+"]"+": "+receivedMsg[2]+"\n");
					//다른 클라이언트로 메시지를 발송한다.
					broadcast("["+receivedMsg[1]+"]"+": "+receivedMsg[2]);
				} else if(receivedMsg[0].equals("end")){
					//채팅 클라이언트를 저장하는 Map 객체에 id와 출력 스티림 객체를 삭제한다.
					clientMap.remove(receivedMsg[1]);
					//servergui로 출력한다.
					servergui.getChats().append(receivedMsg[1]+" 님이 퇴장하셨습니다."+"\n");
					//다른 클라이언트로 메시지를 발송한다.
					broadcast(receivedMsg[1]+" 님이 퇴장하셨습니다."+"\n");
					//조건을 false로 바꾸어 while을 종료한다.
					condition = false;
				} else {
					System.out.println("ServerNetwork: Error");
				}
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	//보유하고 있는 모든  Map객체로 메시지를 발송한다.
	private void broadcast(String msg) {
		Collection<PrintWriter> collection = clientMap.values();
		Iterator<PrintWriter> iterator = collection.iterator();
		
		while (iterator.hasNext()) {
			PrintWriter pw = iterator.next();
			System.out.println(msg);
			pw.println(msg);
			pw.flush();
		}
	}

}
