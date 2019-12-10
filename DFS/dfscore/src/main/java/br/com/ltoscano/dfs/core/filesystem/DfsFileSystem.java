package br.com.ltoscano.dfs.core.filesystem;

import br.com.ltoscano.dfs.core.exception.DfsAlreadyExistsException;
import br.com.ltoscano.dfs.core.exception.DfsException;
import br.com.ltoscano.dfs.core.exception.DfsInvalidPathException;
import br.com.ltoscano.dfs.core.exception.DfsNotFoundException;
import br.com.ltoscano.dfs.core.exception.DfsRuntimeException;
import br.com.ltoscano.dfs.core.filesystem.descriptor.DfsBlockDescriptor;
import br.com.ltoscano.dfs.core.filesystem.descriptor.DfsFileDescriptor;
import br.com.ltoscano.dfs.core.id.DfsID;
import br.com.ltoscano.dfs.core.json.DfsJsonHelper;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;

/**
 *
 * @author ltosc
 */
public class DfsFileSystem implements Cloneable
{
    private HashMap<String, DfsDirectory> directoryList;
    private HashMap<String, DfsFile> fileList;
    
    private long modificationTimestamp;
    
    public DfsFileSystem()
    {
        this.directoryList = new HashMap<>();
        this.fileList = new HashMap<>();
        
        this.modificationTimestamp = -1;
        
        this.createRootDirectory();
    }
    
    private void createRootDirectory()
    {
        try 
        {
            directoryList.put(DfsID.generate("/"), new DfsDirectory("/"));
        }
        catch (DfsInvalidPathException ex)
        {
            throw new DfsRuntimeException("Could not create root directory");
        }
    }
    
    @Override
    public synchronized DfsFileSystem clone() throws CloneNotSupportedException
    {
        DfsFileSystem cloneFileSystem = new DfsFileSystem();
        
        cloneFileSystem.directoryList = new HashMap<>(this.directoryList);
        cloneFileSystem.fileList = new HashMap<>(this.fileList);
        cloneFileSystem.modificationTimestamp = this.modificationTimestamp;
        
        return cloneFileSystem;
    }
    
    public synchronized DfsDirectory getRootDirectory()
    {
        return directoryList.get(DfsID.generate("/"));
    }
    
    public synchronized void addDirectory(DfsDirectory directory) throws DfsAlreadyExistsException, DfsNotFoundException
    {
        if(!containsDirectory(directory.getParentId()))
        {
            throw new DfsNotFoundException("The parent directory does not exist");
        }
        
        directoryList.get(directory.getParentId()).addDirectory(directory.getId());
        directoryList.put(directory.getId(), directory);
        
        updateModificationTimestamp();
    }
    
    public synchronized void removeDirectory(String directoryId)
    {
        if(containsDirectory(directoryId))
        {
            String parentId = directoryList.get(directoryId).getParentId();
            directoryList.get(parentId).removeDirectory(directoryId);
            
            directoryList.remove(directoryId);
            updateModificationTimestamp();
        }
    }
    
    public synchronized DfsDirectory getDirectory(String directoryId) throws DfsNotFoundException
    {
        if(!containsDirectory(directoryId))
        {
            throw new DfsNotFoundException("The directory was not found");
        }
        
        return directoryList.get(directoryId);
    }
    
    public synchronized Collection<DfsDirectory> getDirectories()
    {
        return directoryList.values();
    }
    
    public synchronized boolean containsDirectory(String directoryId)
    {
        return directoryList.containsKey(directoryId);
    }
    
    public synchronized void addFile(DfsFile file) throws DfsAlreadyExistsException, DfsNotFoundException
    {
        if(!containsDirectory(file.getParentId()))
        {
            throw new DfsNotFoundException("The parent directory does not exist");
        }
        
        directoryList.get(file.getParentId()).addFile(file.getId());
        fileList.put(file.getId(), file);
        
        updateModificationTimestamp();
    }
    
    public synchronized void removeFile(String fileId)
    {
        if(containsFile(fileId))
        {
            String parentId = fileList.get(fileId).getParentId();
            directoryList.get(parentId).removeFile(fileId);
            fileList.remove(fileId);
            
            updateModificationTimestamp();
        }
    }
    
    public synchronized DfsFile getFile(String fileId) throws DfsNotFoundException
    {
        if(!containsFile(fileId))
        {
            throw new DfsNotFoundException("The file was not found");
        }
        
        return fileList.get(fileId);
    }
    
    public synchronized Collection<DfsFile> getFiles()
    {
        return fileList.values();
    }
    
    public synchronized boolean containsFile(String fileId)
    {
        return fileList.containsKey(fileId);
    }
    
    public static DfsFileSystem load(String filePath)
    {
        try(BufferedReader bufferedReader = new BufferedReader(new FileReader(filePath)))
        {
            StringBuilder jsonBuilder = new StringBuilder();
            String tmpLine;
            
            while((tmpLine = bufferedReader.readLine()) != null)
            {
                jsonBuilder.append(tmpLine);
            }
            
            return DfsJsonHelper.jsonToObj(jsonBuilder.toString(), DfsFileSystem.class);
        } 
        catch (FileNotFoundException ex) 
        {
            return new DfsFileSystem();
        } 
        catch (IOException | DfsException ex)
        {
            return new DfsFileSystem();
        }
    }
    
    public synchronized void save(String filePath) throws IOException
    {
        try(BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(filePath)))
        {
            bufferedWriter.write(toString());
        }
    }
    
    public synchronized void mergeFileSystem(DfsFileSystem neighborFileSystem) throws DfsAlreadyExistsException, DfsNotFoundException
    {
        if (getModificationTimestamp() < neighborFileSystem.getModificationTimestamp()) 
        {
            directoryList.putAll(neighborFileSystem.directoryList);
            fileList.putAll(neighborFileSystem.fileList);
            
            for(DfsFile file : fileList.values())
            {
                for(DfsFileDescriptor version : file.getFileDescriptorList())
                {
                    for(DfsBlockDescriptor block : version.getBlockDescriptorList())
                    {
                        block.setAvailable(false);
                    }
                    
                    version.setAvailable(false);
                }
                
                file.setAvailable(false);
            }
            
            modificationTimestamp = neighborFileSystem.modificationTimestamp;
        }
    }
    
    public synchronized long getModificationTimestamp() 
    {
        return modificationTimestamp;
    }

    public synchronized void updateModificationTimestamp() 
    {
        this.modificationTimestamp = System.currentTimeMillis();
    }
    
    @Override
    public synchronized String toString()
    {
        return DfsJsonHelper.objToJson(this);
    }
}
