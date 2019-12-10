package br.com.ltoscano.dfs.core.filesystem;

import br.com.ltoscano.dfs.core.exception.DfsInvalidPathException;
import br.com.ltoscano.dfs.core.id.DfsID;
import br.com.ltoscano.dfs.core.path.DfsPathHelper;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author ltosc
 */
public class DfsDirectory implements Cloneable
{
    private String id;
    private String parentId;
    private String path;
    
    private List<String> directoryList;
    private List<String> fileList;
    
    private long creationTimestamp;
    private long modificationTimestamp;
    
    public DfsDirectory()
    {
        this.id = null;
        this.parentId = null;
        this.path = null;
        
        this.directoryList = null;
        this.fileList = null;
        
        this.creationTimestamp = -1;
        this.modificationTimestamp = -1;
    }
    
    public DfsDirectory(String path) throws DfsInvalidPathException
    {
        this.id = DfsID.generate(path);
        this.parentId = DfsID.generate(DfsPathHelper.getBasePath(path));
        this.path = path;
        
        this.directoryList = new ArrayList<>();
        this.fileList = new ArrayList<>();
        
        this.creationTimestamp = System.currentTimeMillis();
        this.modificationTimestamp = creationTimestamp;
    }
    
    @Override
    public DfsDirectory clone() throws CloneNotSupportedException
    {
        DfsDirectory cloneDirectory = new DfsDirectory();
        
        cloneDirectory.id = this.id;
        cloneDirectory.parentId = this.parentId;
        
        cloneDirectory.directoryList = new ArrayList<>(this.directoryList);
        cloneDirectory.fileList = new ArrayList<>(this.fileList);
        
        cloneDirectory.creationTimestamp = this.creationTimestamp;
        cloneDirectory.modificationTimestamp = this.modificationTimestamp;
        
        return cloneDirectory;
    }
    
    public void addDirectory(String directoryId)
    {
        if(!containsDirectory(directoryId))
        {
            directoryList.add(directoryId);
            updateModificationTimestamp();
        }
    }
    
    public void removeDirectory(String directoryId)
    {
        if(containsDirectory(directoryId))
        {
            directoryList.remove(directoryId);
            updateModificationTimestamp();
        }
    }
    
    public List<String> getDirectoryList()
    {
        return directoryList;
    }
    
    public boolean containsDirectory(String directoryId)
    {
        return directoryList.contains(directoryId);
    }
    
    public void addFile(String fileId)
    {
        if(!containsFile(fileId))
        {
            fileList.add(fileId);
        }
        
        updateModificationTimestamp();
    }
    
    public void removeFile(String fileId)
    {
        if(containsFile(fileId))
        {
            fileList.remove(fileId);
            updateModificationTimestamp();
        }
    }
    
    public List<String> getFiles()
    {
        return fileList;
    }
    
    public boolean containsFile(String fileId)
    {
        return fileList.contains(fileId);
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
}
