package xyz.qingmumu.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;

/**
 * @Date:2023/8/27 21:02
 * @Created by Muqing
 */
@Configuration
public class WebSocketConfig {

    public ServerEndpointExporter serverEndpointExporter() {
        return new ServerEndpointExporter();
    }
}