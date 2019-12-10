package br.com.ltoscano.dfs.core.filesystem.descriptor;

/**
 *
 * @author ltosc
 */
public class DfsBlockDescriptor
{
    private final String hash;
    private final long size;
    private final int offset;
    private boolean available;
    
    public DfsBlockDescriptor()
    {
        this.hash = null;
        this.size = -1;
        this.offset = -1;
        this.available = false;
    }
    
    public DfsBlockDescriptor(String hash, int size, int offset)
    {
        this.hash = hash;
        this.size = size;
        this.offset = offset;
        this.available = false;
    }

    /**
     * @return the hash
     */
    public String getHash() {
        return hash;
    }

    /**
     * @return the size
     */
    public long getSize() {
        return size;
    }

    /**
     * @return the offset
     */
    public int getOffset() {
        return offset;
    }

    /**
     * @return the available
     */
    public boolean isAvailable() {
        return available;
    }

    /**
     * @param available the available to set
     */
    public void setAvailable(boolean available) {
        this.available = available;
    }
}
