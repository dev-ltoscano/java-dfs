package br.com.ltoscano.dfs.core.json;

import br.com.ltoscano.dfs.core.exception.DfsException;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

/**
 *
 * @author ltosc
 */
public class DfsJsonHelper 
{
    private static final Gson parser = new Gson();
    
    private DfsJsonHelper()
    {
        
    }
    
    public static String objToJson(Object obj)
    {
        return parser.toJson(obj);
    }
    
    public static <T> T jsonToObj(String json, Class<T> objClass) throws DfsException
    {
        return jsonToObj(parser, json, objClass);
    }
    
    public static <T> T jsonToObj(Gson jsonParser, String json, Class<T> objClass) throws DfsException
    {
        try
        {
            return jsonParser.fromJson(json, objClass);
        }
        catch(JsonSyntaxException ex)
        {
            throw new DfsException(ex.getMessage());
        }
    }
}
