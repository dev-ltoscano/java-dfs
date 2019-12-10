package br.com.ltoscano.dfs.core.enums;

/**
 *
 * @author ltosc
 */
public final class DfsEnums 
{
    private DfsEnums()
    {
        
    }
    
    public enum DfsServerType { Tcp, Multicast };
    
    public enum DfsEventSource { Tcp, Multicast, Any };
    
    public enum DfsPartitionSize { _4KB, _8KB, _16KB, _32KB, _64KB, _128KB, _256KB, _512KB }
}
