package org.netty.test;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.timeout.IdleStateHandler;

public class server {
    public static void main(String[] args) {
        EventLoopGroup bossGroup = new NioEventLoopGroup(1);
        EventLoopGroup workerGroup = new NioEventLoopGroup(4);
        try {
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            serverBootstrap.group(bossGroup, workerGroup)
            .channel(NioServerSocketChannel.class)
            .localAddress(8081)
            .childHandler(new ChannelInitializer<Channel>() {

                @Override
                protected void initChannel(Channel ch) throws Exception {
                    // TODO Auto-generated method stub
                    ChannelPipeline pipeline = ch.pipeline();
                    /**通过netty中的 IdleStateHandler 超时机制来实现心跳和重连
                    readerIdleTime：为读超时时间（即多长时间没有接受到客户端发送数据）
                    writerIdleTime：为写超时时间（即多长时间没有向客户端发送数据）
                    allIdleTime：所有类型的超时时间
                     */
                    pipeline.addLast(new IdleStateHandler(10,0,0));
                    pipeline.addLast(new MsgPckDecode());
                    pipeline.addLast(new MsgPckEncode());
                    pipeline.addLast(new serverHandler());
                }
            });         
            System.out.println("start server 8081 --");
            ChannelFuture sync = serverBootstrap.bind().sync();
            sync.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }finally{
            //优雅的关闭资源
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }
}