package laura.dxc;

import java.net.InetSocketAddress;
import java.nio.channels.AsynchronousChannelGroup;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.nio.channels.AsynchronousSocketChannel;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class AIOServer {

	public static final int PORT=30000;
	final static String UTF_8="utf-8";
	static List<AsynchronousSocketChannel> channelList=new ArrayList<>();
	public AIOServer() {
		// TODO Auto-generated constructor stub
	}
	
	public void startListen()throws InterruptedException, Exception
	{
		ExecutorService exector=Executors.newFixedThreadPool(20);
		AsynchronousChannelGroup channelGroup=AsynchronousChannelGroup.withThreadPool(exector);
		AsynchronousServerSocketChannel serverChannel=AsynchronousServerSocketChannel.open(channelGroup);
		serverChannel.bind(new InetSocketAddress(PORT));
		serverChannel.accept(null, new AcceptHandle(serverChannel));
	}

	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		AIOServer server=new AIOServer();
		
		server.startListen();
		System.out.print("server starts");
		while(true)
		{
			
		}

	}

}
