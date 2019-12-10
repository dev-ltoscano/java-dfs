package br.com.ltoscano.dfs.core.event;

import br.com.ltoscano.dfs.core.exception.DfsException;
import br.com.ltoscano.dfs.core.exception.DfsNotFoundException;
import br.com.ltoscano.dfs.core.json.DfsJsonHelper;
import br.com.ltoscano.dfs.core.string.DfsStringHelper;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Set;

/**
 *
 * @author ltosc
 */
public class DfsEventMessage 
{
    private final HashMap<String, String> args;
    
    public DfsEventMessage(String eventName)
    {
        this.args = new HashMap<>();
        this.args.put("EVENT_NAME", DfsStringHelper.capitalize(eventName));
    }
    
    public String getEventName()
    {
        return args.get("EVENT_NAME");
    }
    
    public String getArgs(String argKey) throws DfsNotFoundException
    {
        argKey = DfsStringHelper.capitalize(argKey);
        
        if(!args.containsKey(argKey))
        {
            throw new DfsNotFoundException("The argument '" + argKey + "' not found");
        }
        
        return args.get(argKey);
    }
    
    public void setArgs(String argKey, String argValue)
    {
        args.put(DfsStringHelper.capitalize(argKey), argValue);
    }
    
    public Set<Entry<String, String>> getArgs() 
    {
        return args.entrySet();
    }
    
    public static DfsEventMessage fromJson(String json) throws DfsException 
    {
        return DfsJsonHelper.jsonToObj(json, DfsEventMessage.class);
    }
    
    @Override
    public String toString()
    {
        return DfsJsonHelper.objToJson(this);
    }
}
