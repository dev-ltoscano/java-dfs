package br.com.ltoscano.dfs.core.filesystem;

import br.com.ltoscano.dfs.core.exception.DfsInvalidPathException;
import br.com.ltoscano.dfs.core.exception.DfsNotFoundException;
import br.com.ltoscano.dfs.core.filesystem.descriptor.DfsFileDescriptor;
import br.com.ltoscano.dfs.core.id.DfsID;
import br.com.ltoscano.dfs.core.path.DfsPathHelper;
import java.util.Collection;
import java.util.HashMap;

/**
 *
 * @author ltosc
 */
public class DfsFile implements Cloneable
{
    private String id;
    private String parentId;
    private String path;
    
    private HashMap<String, DfsFileDescriptor> fileDescriptorList;
    private String currentVersion;
    
    private long creationTimestamp;
    private long modificationTimestamp;
    
    private boolean available;
    
    public DfsFile()
    {
        this.id = null;
        this.parentId = null;
        this.path = null;
        
        this.fileDescriptorList = null;
        this.currentVersion = null;
        
        this.creationTimestamp = -1;
        this.modificationTimestamp = -1;
    }
    
    public DfsFile(String path, DfsFileDescriptor fileDescriptor) throws DfsInvalidPathException
    {
        this.id = DfsID.generate(path);
        this.parentId = DfsID.generate(DfsPathHelper.getBasePath(path));
        this.path = path;
        
        this.fileDescriptorList = new HashMap<>();
        this.fileDescriptorList.put(fileDescriptor.getHash(), fileDescriptor);
        this.currentVersion = fileDescriptor.getHash();
        
        this.creationTimestamp = System.currentTimeMillis();
        this.modificationTimestamp = creationTimestamp;
        
        this.available = false;
    }
    
    @Override
    public DfsFile clone() throws CloneNotSupportedException
    {
        DfsFile cloneFile = new DfsFile();
        
        cloneFile.id = this.id;
        cloneFile.parentId = this.parentId;
        cloneFile.fileDescriptorList = new HashMap<>(this.fileDescriptorList);
        cloneFile.currentVersion = this.currentVersion;
        cloneFile.creationTimestamp = this.creationTimestamp;
        cloneFile.modificationTimestamp = this.modificationTimestamp;
        cloneFile.available = this.available;
        
        return cloneFile;
    }
    
    public void addFileVersion(DfsFileDescriptor fileDescriptor)
    {
        if(!containsFileVersion(fileDescriptor.getHash()))
        {
            fileDescriptorList.put(fileDescriptor.getHash(), fileDescriptor);
            currentVersion = fileDescriptor.getHash();
            
            updateModificationTimestamp();
        }
    }
    
    public DfsFileDescriptor getFileVersion(String versionId) throws DfsNotFoundException
    {
        if(!containsFileVersion(versionId))
        {
            throw new DfsNotFoundException("The file version not found");
        }
        
        return fileDescriptorList.get(versionId);
    }
    
    public boolean containsFileVersion(String versionId)
    {
        return fileDescriptorList.containsKey(versionId);
    }   

    public String getId() 
    {
        return id;
    }

    public String getParentId() 
    {
        return parentId;
    }

    public String getPath()
    {
        return path;
    }

    public void setPath(String path) throws DfsInvalidPathException 
    {
        this.parentId = DfsID.generate(DfsPathHelper.getBasePath(path));
        this.path = path;
    }

    public Collection<DfsFileDescriptor> getFileDescriptorList()
    {
        return fileDescriptorList.values();
    }

    public String getCurrentVersion()
    {
        return currentVersion;
    }

    public long getCreationTimestamp() 
    {
        return creationTimestamp;
    }

    public long getModificationTimestamp() 
    {
        return modificationTimestamp;
    }

    private void updateModificationTimestamp()
    {
        this.modificationTimestamp = System.currentTimeMillis();
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
