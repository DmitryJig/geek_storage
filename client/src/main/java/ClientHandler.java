import common.Message;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.SimpleChannelInboundHandler;

import java.util.function.Consumer;

public class ClientHandler extends SimpleChannelInboundHandler<String> {

    private final Message message;
    private final Consumer<String> callback;


    public ClientHandler(Message message, Consumer<String> callback) {
        this.message = message;
        this.callback = callback;
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        ctx.writeAndFlush(message);
    }

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, String msg) throws Exception {

        callback.accept(msg); // метод вызовется только когда ответит сервер
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace(); // обрабатываем возможные ошибки
    }


}
