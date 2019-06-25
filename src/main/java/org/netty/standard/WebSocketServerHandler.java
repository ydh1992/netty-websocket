package org.netty.standard;

import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.websocketx.*;
import org.netty.pojo.PojoEndpointServer;

class WebSocketServerHandler extends SimpleChannelInboundHandler<WebSocketFrame> {

    private final PojoEndpointServer pojoEndpointServer;

    public WebSocketServerHandler(PojoEndpointServer pojoEndpointServer) {
        this.pojoEndpointServer = pojoEndpointServer;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, WebSocketFrame msg) throws Exception {
        //ChannelHandlerContext：上下文对象
        handleWebSocketFrame(ctx, msg);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        //channel发生异常，若不关闭，随着异常channel的逐渐增多，性能也就随之下降
        pojoEndpointServer.doOnError(ctx, cause);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        //客户端与服务端断开连接之后
        pojoEndpointServer.doOnClose(ctx);
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        //用户事件触发
        pojoEndpointServer.doOnEvent(ctx, evt);
    }

    private void handleWebSocketFrame(ChannelHandlerContext ctx, WebSocketFrame frame) {
        if (frame instanceof TextWebSocketFrame) {
            //region 纯文本消息
            pojoEndpointServer.doOnMessage(ctx, frame);
            return;
        }
        if (frame instanceof PingWebSocketFrame) {
            //region 判断是否是ping消息
            ctx.writeAndFlush(new PongWebSocketFrame(frame.content().retain()));
            return;
        }
        if (frame instanceof CloseWebSocketFrame) {
            //region 判断是否是关闭链路的指令
            ctx.writeAndFlush(frame.retainedDuplicate()).addListener(ChannelFutureListener.CLOSE);
            return;
        }
        if (frame instanceof BinaryWebSocketFrame) {
            //region 二进制消息
            pojoEndpointServer.doOnBinary(ctx, frame);
            return;
        }
        if (frame instanceof PongWebSocketFrame) {
            return;
        }
    }
    @Override
    public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
        //channel注册
        super.channelRegistered(ctx);
    }

    @Override
    public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
        //channel注册
        super.channelUnregistered(ctx);
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        //channel活跃状态
        super.channelActive(ctx);
    }
    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        //channel读取数据完毕
        super.channelReadComplete(ctx);
    }

    @Override
    public void channelWritabilityChanged(ChannelHandlerContext ctx) throws Exception {
        //channel可写事件更改
        super.channelWritabilityChanged(ctx);
    }

    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        //助手类添加
        super.handlerAdded(ctx);
    }

    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        //助手类移除
        super.handlerRemoved(ctx);
    }
}