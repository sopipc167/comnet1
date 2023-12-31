package comnet;
import java.io.*; 
import java.net.*;
import java.util.Scanner;

import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;

public class Http
{ 
    public static void main(String argv[])throws Exception
    {
    	//사용자로부터 받은 url을 파싱함
    	System.out.println("URL을 입력하세요:");
    	Scanner sc = new Scanner(System.in);
    	String url = sc.nextLine();
    	URL obj = new URL(url);
    	String host = obj.getHost();
    	System.out.println(host);
    	String path = (obj.getPath()!="") ? obj.getPath() : "/";
    	System.out.println(path);
    	String protocol = obj.getProtocol();
    	
        /*와어어샤크 때 사용하던 DNS와 포트 80번 (Deprecated : HTTP용)
        Socket clientSocket = new Socket(host, 80); */
    	
    	 // SSL 소켓 팩토리 생성
        SSLSocketFactory factory = (SSLSocketFactory) SSLSocketFactory.getDefault();
        SSLSocket clientSocket = (SSLSocket) factory.createSocket(host, 443);
    	
        //Chapter02 마지막에 있는 Java 예제
        DataOutputStream outToServer = new DataOutputStream(clientSocket.getOutputStream());
        BufferedReader inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream())); 

        //간단하게 HTTP GET Request 메시지 (파싱한 url을 바탕으로, 봇 감지를 피하기 위해 user-agent 추가)
        String HTTPGETRequestMsg = "GET "+path+" HTTP/1.1\r\n"
            + "Host: "+host+"\r\n"
            + "Accept-Language: ko,en,q=0.9,en-US,q=0.8\r\n"
            + "User-Agent: Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/118.0.0.0 Safari/537.36\r\n"
            + "\r\n";

        //소켓을 통해서
        outToServer.writeBytes(HTTPGETRequestMsg);
        while(true)
        {
            String line = inFromServer.readLine();
            
            if(line == null)
                break;

            System.out.println(line);
        }

        //소켓 종료
        clientSocket.close(); 
    } 
}