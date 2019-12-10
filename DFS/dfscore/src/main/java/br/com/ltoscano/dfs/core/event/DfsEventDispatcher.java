package br.com.ltoscano.dfs.core.event;

import br.com.ltoscano.dfs.core.base.DfsBase;
import br.com.ltoscano.dfs.core.exception.DfsNotFoundException;
import br.com.ltoscano.dfs.core.network.tcp.DfsTcpConnection;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 *
 * @author ltosc
 */
public class DfsEventDispatcher extends DfsBase
{
    private final HashMap<String, List<DfsEventListener>> eventListenerMap;
    
    public DfsEventDispatcher()
    {
        super(DfsEventDispatcher.class);
        this.eventListenerMap = new HashMap<>();
    }
    
    private List<DfsEventListener> getEventListenerList(String eventName) throws DfsNotFoundException
    {
        if(!eventListenerMap.containsKey(eventName))
        {
            throw new DfsNotFoundException("The event '" + eventName + "' was not found");
        }
        
        return eventListenerMap.get(eventName);
    }
    
    public void registerEventListener(DfsEventListener eventListener)
    {
        if(!eventListenerMap.containsKey(eventListener.getEventName()))
        {
            List<DfsEventListener> eventListenerList = new ArrayList<>();
            eventListenerList.add(eventListener);
            
            eventListenerMap.put(eventListener.getEventName(), eventListenerList);
        }
        else
        {
            eventListenerMap.get(eventListener.getEventName()).add(eventListener);
        }
    }
    
    public void unregisterEventListener(DfsEventListener eventListener)
    {
        if(eventListenerMap.containsKey(eventListener.getEventName()))
        {
            eventListenerMap.get(eventListener.getEventName()).remove(eventListener);
        }
    }
    
    public void fireEvent(DfsEventMessage eventMsg) throws DfsNotFoundException
    {
        final List<DfsEventListener> listenerList =  getEventListenerList(eventMsg.getEventName());
        
        Thread fireThread = new Thread(new Runnable() 
        {
            @Override
            public void run() 
            {
                for(DfsEventListener listener : listenerList)
                {
                    try 
                    {
                        listener.onEvent(null, eventMsg);
                    } 
                    catch (Exception ex) 
                    {
                        handleError(ex);
                    }
                }
            }
        });
        
        fireThread.start();
    }
    
    public void fireEvent(Socket clientSocket)
    {
        Thread fireThread = new Thread(new Runnable() 
        {
            @Override
            public void run() 
            {
                try 
                {
                    Thread.sleep(100);
                }
                catch (InterruptedException ex) 
                {
                    handleError(ex);
                }
                
                DfsTcpConnection connection = null;
                
                try 
                {
                    connection = new DfsTcpConnection(clientSocket);
                    
                    DfsEventMessage eventMsg = connection.readEvent();
                    List<DfsEventListener> listenerList =  getEventListenerList(eventMsg.getEventName());
                    
                    for (DfsEventListener listener : listenerList)
                    {
                        try 
                        {
                            listener.onEvent(connection, eventMsg);
                        } 
                        catch (Exception ex)
                        {
                            handleError(ex);
                        }
                    }
                } 
                catch (IOException ex) 
                {
                    handleError(ex);
                }
                catch (Exception ex)
                {
                    handleError("An error occurred while firing event", ex);
                }
                finally
                {
                    if(connection != null)
                    {
                        try
                        {
                            connection.close();
                        } 
                        catch (IOException ex) 
                        {
                            handleError(ex);
                        }
                    }
                }
            }
        });
        
        fireThread.start();
    }
}
