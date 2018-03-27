package laura.dxc;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousChannelGroup;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class AIOClient {

	final static String UTF_8="utf-8";
	final static int PORT=30000;
	AsynchronousSocketChannel clientChannel;
	JFrame mainWin=new JFrame("mutiple Chat");
	JTextArea jta=new JTextArea(16,48);
	JTextField jtf=new JTextField(40);
	JButton sendBn=new JButton("send");
	
	public void init()
	{
		mainWin.setLayout(new BorderLayout());
		jta.setEditable(false);
		mainWin.add(new JScrollPane(jta), BorderLayout.CENTER);
		JPanel jp=new JPanel();
		jp.add(jtf);
		jp.add(sendBn);
		Action sendAction=new AbstractAction()
		{
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			public void actionPerformed(ActionEvent e)
			{
				String content=jtf.getText();
				if(content.trim().length()>0)
				{
					try {
						
						clientChannel.write(ByteBuffer.wrap(content.trim().getBytes(UTF_8))).get();
					}
					catch(Exception ex)
					{
						ex.printStackTrace();
					}
				}
				jtf.setText("");
			}
			
		};
		sendBn.addActionListener(sendAction);
		mainWin.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		mainWin.add(jp, BorderLayout.SOUTH);
		mainWin.pack();
		mainWin.setVisible(true);
	}
	
	public void connect()throws Exception
	{
		final ByteBuffer buff=ByteBuffer.allocate(1024);
		ExecutorService exector=Executors.newFixedThreadPool(80);
		AsynchronousChannelGroup channelGroup=AsynchronousChannelGroup.withThreadPool(exector);
		clientChannel=AsynchronousSocketChannel.open(channelGroup);
		clientChannel.connect(new InetSocketAddress("127.0.0.1", PORT)).get();
		jta.append("connect successfully");
		buff.clear();
		clientChannel.read(buff,null, new CompletionHandler<Integer, Object>()
		{

			@Override
			public void completed(Integer arg0, Object arg1) {
				// TODO Auto-generated method stub
				buff.flip();
				String content=StandardCharsets.UTF_8.decode(buff).toString();
				jta.append("someone said "+content+"\n");
				buff.clear();
				clientChannel.read(buff, null, this);
			}

			@Override
			public void failed(Throwable arg0, Object arg1) {
				// TODO Auto-generated method stub
				System.out.println("read failed "+arg0);
			}
			
			
			
		});
		
		
	}
	
	public static void main(String[] args)throws Exception {
		// TODO Auto-generated method stub
		AIOClient client=new AIOClient();
		client.init();
		client.connect();
	}

}
