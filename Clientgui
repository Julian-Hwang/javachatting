package com.aaa.client;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.TextArea;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.PrintWriter;

import javax.swing.JButton;
import javax.swing.JPanel;

public class Clientgui extends Frame implements ActionListener{
	private TextArea chatviewer; //채팅 대화창 만들기
	
	private TextField tfname; //이름 입력칸 만들기
	private TextField tfport; //포트번호 입력칸 만들기
	private TextField tfip; //ip 입력칸 만들기
	private JButton login; //로그인버튼으로 서버와 연결한다.
	
	private TextField tfinput; //대화입력칸 만들기
	private JButton tfenter; //엔터 버튼을 올려 대화내용을 공유한다.
	
	private PrintWriter pw; //데이터를 서버로 보내는 스트림이다.
	private String idname; //tfname에서 아이디를 가져올때 사용한다.
	private String serverip; //tfip에서 ip를 가져올때 사용한다.
	private int portnum = 10000;//지정된 포트번호
	
	public Clientgui() {
		//패널선언
		JPanel up = new JPanel();
		JPanel center = new JPanel();
		JPanel down = new JPanel();
		
		//up
		tfname = new TextField("이름",5);// 이름입력칸구성하기
		tfname.setText("이름 입력");
		tfname.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				tfname.setText("");
			}
		});
		
		tfport = new TextField(Integer.toString(portnum),5);// 포트번호 입력칸 구성하기
		tfport.setText("10000");
		
		tfip = new TextField("127.0.0.1",5);//ip입력칸 구성하기.
		tfip.setText("127.0.0.1");
		
		login = new JButton("시작"); //로그인 버튼 구성하기
		login.setPreferredSize(new Dimension(67,23));
		login.addActionListener(this);
		
		//center
		chatviewer = new TextArea(""); //채팅창 구성하기
		chatviewer.setPreferredSize(new Dimension(278,285));
		chatviewer.setEnabled(false);
		
		//down
		tfinput = new TextField(""); //입력창 구성하기
		tfinput.setPreferredSize(new Dimension(210,25));
		tfinput.addActionListener(this);
		
		tfenter = new JButton("엔터");//엔터 버튼 구성하기
		tfenter.setPreferredSize(new Dimension(60,25));
		tfenter.addActionListener(this);
		
		// 해당되는 패널에 각 요소들을 넣어준다.
		up.add(tfname);
		up.add(tfport);
		up.add(tfip);
		up.add(login);
		
		center.add(chatviewer);
		
		down.add(tfinput);
		down.add(tfenter);
		
		//Borderlayout을 이용해 위치를 지정한다.
		add(up,BorderLayout.NORTH);
		add(center,BorderLayout.CENTER);
		add(down, BorderLayout.SOUTH);
		
		//프레임 크기 설정
		setSize(300,400);
		setVisible(true);
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		//Event 객체를 반환한다.
		Object obj = e.getSource();
		if (obj == login) {
			// 로그인 버튼을 누르면 init()의 실행을 통해 Event가 서버와 연결한다.
			idname = tfname.getText();
			serverip = tfip.getText();
			portnum = Integer.parseInt(tfport.getText());
			
			init();
			
			chatviewer.setText("채팅 시작\n");
			
			login.setEnabled(false);
		} else if (obj == tfinput || obj == tfenter) {
			// Event가 textfield의 문장을 엔터버튼이나 
			//키보드의 엔터키를 통해 받으면 서버와 연결된다.
			String msg = tfinput.getText();
			
			//채팅내용을 다음 코드문에 따라 가공해서 전송한다.
			if (!msg.trim().equals("")) {
				try {
					String str = "contents"+":"+ idname + ":" + msg;
					pw.println(str);
					pw.flush();
					
					tfinput.setText("");
					tfinput.setFocusable(true);
				} catch (Exception e2) {
					e2.printStackTrace();
				}
			} else {
				System.out.println("Error");
			}
		}
	}
	
	//Clientgui의 네트워크를 구현한다.
	public void init() {
		ClientNetwork clientNetwork = new ClientNetwork(this,serverip,portnum); 
		//클라이언트 네트워크 클래스 선언
		Thread thread = new Thread(clientNetwork);
		thread.start();
		
		try {
			//Thread 실행후 Socket에 대한 출력 스트림 객체를 반환하여 멤버변수에 저장한다.
			//다음 출력스트림은 서버로 채팅메시지 전송에 사용된다.
			pw = clientNetwork.getPw();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		Clientgui clientgui = new Clientgui();
		clientgui.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				//채팅창을 나가면 다음 메시지가 나간다.
				String str = "end"+":"+clientgui.getIdname();
				clientgui.getPw().println(str);
				clientgui.getPw().flush();
				System.exit(0);				
			}
		});
	}
	
	public PrintWriter getPw() {
		return pw;
	}
	public TextArea getChatviewer() {
		return chatviewer;
	}
	public String getIdname() {
		return idname;
	}
	
	//ClientNetwork 클래스에서 쓰일 메소드이다.
	// 채팅창에 메시지 전달을 담당한다.
	public void appendMsg(String msg) {
		chatviewer.append(msg);
	}
	

}
