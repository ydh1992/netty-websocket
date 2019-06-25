package org.netty.autoconfigure;

import org.netty.standard.ServerEndpointExporter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.netty.annotation.EnableWebSocket;

@EnableWebSocket
@ConditionalOnMissingBean(ServerEndpointExporter.class)
public class NettyWebSocketAutoConfigure {
}
