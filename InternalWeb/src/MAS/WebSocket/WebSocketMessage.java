package MAS.WebSocket;

import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@ServerEndpoint("/websocket/{channel-id}")
public class WebSocketMessage {
    private static Set<Session> peers = Collections.synchronizedSet(new HashSet<Session>());
    private static ConcurrentHashMap<String, Set<Session>> channels = new ConcurrentHashMap<>();

    @OnOpen
    public void onOpen(Session peer, @PathParam("channel-id") String channelId) throws IOException {
        if (channels.get(channelId) == null) {
            channels.put(channelId, Collections.synchronizedSet(new HashSet<Session>()));
        }
        channels.get(channelId).add(peer);
        peers.add(peer);
    }

    @OnClose
    public void onClose(Session peer, @PathParam("channel-id") String channelId) {
        peers.remove(peer);
        channels.get(channelId).remove(peer);
    }

    @OnMessage
    public void onMessage(String message, Session session, @PathParam("channel-id") String channelId) throws IOException {
        for (Session peer : channels.get(channelId)) {
            if (peer.equals(session)) continue;
            peer.getBasicRemote().sendText(message);
        }
    }
}
