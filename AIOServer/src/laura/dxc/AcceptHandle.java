package laura.dxc;

import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.nio.charset.StandardCharsets;

public class AcceptHandle implements CompletionHandler<AsynchronousSocketChannel, Object> {

	private AsynchronousServerSocketChannel serverChannel;
	public AcceptHandle(AsynchronousServerSocketChannel serverChannel)
	{
		this.serverChannel=serverChannel;
		
	}
	
	private ByteBuffer buff=ByteBuffer.allocate(2014);
	
	@Override
	public void completed(final AsynchronousSocketChannel sc, Object arg1) {
		// TODO Auto-generated method stub
		AIOServer.channelList.add(sc);
		serverChannel.accept(null,this);
		sc.read(buff, null, new CompletionHandler<Integer, Object>(){

			@Override
			public void completed(Integer result, Object attachment) {
				// TODO Auto-generated method stub
				System.out.println("completed method");
				buff.flip();
				String content=StandardCharsets.UTF_8.decode(buff).toString();
				for(AsynchronousSocketChannel c:AIOServer.channelList)
				{
					System.out.println("ChannelListSize"+AIOServer.channelList.size());
					try {
						c.write(ByteBuffer.wrap(content.getBytes(AIOServer.UTF_8))).get();
						
					}
					catch(Exception ex)
					{
						ex.printStackTrace();
						
					}
					buff.clear();
					sc.read(buff, null, this);
					
					
				}
			}

			@Override
			public void failed(Throwable exc, Object attachment) {
				// TODO Auto-generated method stub
				System.out.println("fail to read data"+exc);
				AIOServer.channelList.remove(sc);
			}
			
			
		});
	}

	@Override
	public void failed(Throwable arg0, Object arg1) {
		// TODO Auto-generated method stub
		System.out.println("fail to connect"+arg0);
	}

}
