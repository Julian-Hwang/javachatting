package com.aaa.server;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.TextArea;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class Servergui extends JFrame implements ActionListener{
	//채팅창을 선언한다.
	private TextArea chats;
	//String은 id가 들어가고 printwriter는 출력 스트림 객체를 저장한다.
	private Map<String, PrintWriter> clientMap;
	// 포트번호 선언
	private int portnum = 10000;
	//생성자를 선언한다.
	public Servergui() {
		JPanel panel = new JPanel();
		FlowLayout flowLayout = new FlowLayout();
		
		//채팅상환을 표시하는 textarea이다.
		chats = new TextArea();
		chats.setPreferredSize(new Dimension(450,450));
		chats.setEditable(false);
		
		panel.setLayout(flowLayout);
		panel.add(chats);
		
		add(panel,BorderLayout.CENTER);
		
		setSize(500,500);
		setVisible(true);
	}
	
	// 동작하는 동안 포트번호를 받아들이고 접속했다는 메시지를 출력한다.
	public void init() {
		ServerSocket serverSocket = null;
		try {
			//연결된 클라이언트를 저장할 Map<client_id, output_stream>를 생성한다. 생성 후 동기화를 설정한다.
			serverSocket = new ServerSocket(portnum);
			clientMap = new HashMap<String, PrintWriter>();
			Collections.synchronizedMap(clientMap);
			
			//serversocket에 연결이 이루어지면 해당 socket를 반환하고
			//채팅서버를 담당하는 ServerNetwork객체의 생성자에 매개변수를 넘겨 객체를 생성한다.
			while (true) {
				chats.append("연결중\n");
				Socket socket = serverSocket.accept();
				ServerNetwork serverNetwork = new ServerNetwork(clientMap, this, socket);
				Thread thread = new Thread(serverNetwork);
				thread.start();
			}
					
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	
	public static void main(String[] args) {
		Servergui servergui = new Servergui();
		servergui.init();
	}
	@Override
	public void actionPerformed(ActionEvent e) {}
	
	public void appendMsg(String msg) {
		chats.append(msg);
	}
	public TextArea getChats() {
		return chats;
	}
	public void setChats(TextArea chats) {
		this.chats = chats;
	}
	
	
}
