package br.com.ltoscano.dfs.core.network.multicast.sender;

import br.com.ltoscano.dfs.core.event.DfsEventMessage;
import br.com.ltoscano.dfs.core.network.DfsNetworkAddress;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.nio.charset.StandardCharsets;

/**
 *
 * @author ltosc
 */
public final class DfsMulticastSender
{
    private DfsMulticastSender()
    {
        
    }
    
    public static void send(DfsNetworkAddress networkAddress, DfsEventMessage eventArgs) throws SocketException, IOException
    {
        byte[] sendBuffer = eventArgs.toString().getBytes(StandardCharsets.UTF_8);
        DatagramPacket packet = new DatagramPacket(sendBuffer, sendBuffer.length, networkAddress.getIP(), networkAddress.getPort());

        DatagramSocket udpSocket = new DatagramSocket();
        udpSocket.send(packet);
    }
}
