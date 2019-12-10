package br.com.ltoscano.dfs.core.config;

import br.com.ltoscano.dfs.core.exception.DfsException;
import br.com.ltoscano.dfs.core.file.DfsFileHelper;
import br.com.ltoscano.dfs.core.json.DfsJsonHelper;
import java.io.IOException;

/**
 *
 * @author ltosc
 */
public final class DfsConfigurator 
{
    private static DfsConfig config;
    
    private DfsConfigurator()
    {
        
    }

    public static DfsConfig getConfig()
    {
        if(config == null)
        {
            load("dfs-config.json");
        }
        
        return config;
    }
    
    public static void load(String filePath)
    {
        try 
        {
            config = DfsJsonHelper.jsonToObj(DfsFileHelper.readTextFile(filePath), DfsConfig.class);
        } 
        catch (IOException | DfsException ex)
        {
            config = new DfsConfig();
        }
    }
    
    public static void save(String filePath) throws IOException
    {
        DfsFileHelper.writeTextFile(filePath, DfsJsonHelper.objToJson(config));
    }
}
