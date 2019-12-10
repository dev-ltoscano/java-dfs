package br.com.ltoscano.dfs.core.network.tcp;

import br.com.ltoscano.dfs.core.event.DfsEventMessage;
import br.com.ltoscano.dfs.core.exception.DfsException;
import br.com.ltoscano.dfs.core.network.DfsNetworkAddress;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;

/**
 *
 * @author ltosc
 */
public class DfsTcpConnection 
{
    private final Socket clientSocket;
    private final BufferedReader bufferedReader;
    private final BufferedWriter bufferedWriter;
    
    public DfsTcpConnection(DfsNetworkAddress networkAddress) throws IOException
    {
        this(new Socket(networkAddress.getStringIP(), networkAddress.getPort()));
    }
    
    public DfsTcpConnection(Socket clientSocket) throws IOException
    {
        this.clientSocket = clientSocket;
        this.bufferedReader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream(), "UTF-8"));
        this.bufferedWriter = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream(), "UTF-8"));
    }
    
    public Socket getClientSocket()
    {
        return clientSocket;
    }
    
    public BufferedReader getBufferedReader()
    {
        return bufferedReader;
    }
    
    public BufferedWriter getBufferedWriter()
    {
        return bufferedWriter;
    }
    
    public String readString() throws IOException
    {
        return bufferedReader.readLine();
    }
    
    public void writeString(String msg) throws IOException
    {
        bufferedWriter.write(msg);
        bufferedWriter.newLine();
        bufferedWriter.flush();
    }
    
    public DfsEventMessage readEvent() throws IOException, DfsException
    {
        StringBuilder jsonBuilder = new StringBuilder();
        String tmpLine;

        do
        {
            tmpLine = bufferedReader.readLine();
            jsonBuilder.append(tmpLine);
        }
        while (clientSocket.getInputStream().available() != 0);
        
        return DfsEventMessage.fromJson(jsonBuilder.toString());
    }
    
    public void writeEvent(DfsEventMessage eventArgs) throws IOException
    {
        writeString(eventArgs.toString());
    }
    
    public void close() throws IOException
    {
        bufferedWriter.close();
        bufferedReader.close();
        clientSocket.close();
    }
}
