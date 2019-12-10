package br.com.ltoscano.dfs.core.filesystem.descriptor;

import br.com.ltoscano.dfs.core.enums.DfsEnums.DfsPartitionSize;
import br.com.ltoscano.dfs.core.exception.DfsNotFoundException;
import br.com.ltoscano.dfs.core.file.DfsFileHelper;
import br.com.ltoscano.dfs.core.json.DfsJsonHelper;
import br.com.ltoscano.dfs.core.merkletree.DfsMerkleTree;
import br.com.ltoscano.dfs.core.sha.DfsSHA;
import br.com.ltoscano.dfs.core.sha.DfsSHA.DfsSHAVersion;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

/**
 *
 * @author ltosc
 */
public class DfsFileDescriptor
{
    private final String hash;
    private final long size;
    private boolean available;
    
    private final HashMap<String, DfsBlockDescriptor> blockDescriptorList;
    
    public DfsFileDescriptor()
    {
        this.blockDescriptorList = null;
        
        this.hash = null;
        this.size = -1;
        
        this.available = false;
    }
   
    public DfsFileDescriptor(List<DfsBlockDescriptor> blockDescriptorList) throws IOException
    {
        this.blockDescriptorList = new HashMap<>();
        
        long totalFileSize = 0;
        List<String> blockHashList = new ArrayList<>();
        
        for(DfsBlockDescriptor blockDescriptor : blockDescriptorList)
        {
            this.blockDescriptorList.put(blockDescriptor.getHash(), blockDescriptor);
            
            totalFileSize += blockDescriptor.getSize();
            blockHashList.add(blockDescriptor.getHash());
        }
        
        this.hash = DfsMerkleTree.buildHash(blockHashList);
        this.size = totalFileSize;
        this.available = false;
    }
    
    public static DfsFileDescriptor buildFileDescriptor(String localFilePath, DfsPartitionSize partitionSize) throws FileNotFoundException, IOException
    {
        File localFile = new File(localFilePath);
        
        if(!localFile.exists())
        {
            throw new FileNotFoundException("File '" + localFilePath + "' does not exist");
        }
        
        List<DfsBlockDescriptor> blockDescriptorList = new ArrayList<>();
        
        byte[] buffer;
        
        switch(partitionSize)
        {
            case _4KB:
            {
                buffer = new byte[4096];
                break;
            }
            case _8KB:
            {
                buffer = new byte[8192];
                break;
            }
            case _16KB:
            {
                buffer = new byte[16384];
                break;
            }
            case _32KB:
            {
                buffer = new byte[32768];
                break;
            }
            case _64KB:
            {
                buffer = new byte[65536];
                break;
            }
            case _128KB:
            {
                buffer = new byte[131072];
                break;
            }
            case _256KB:
            {
                buffer = new byte[262144];
                break;
            }
            case _512KB:
            {
                buffer = new byte[524288];
                break;
            }
            default:
            {
                buffer = new byte[4096];
            }
        }
        
        int size;
        int offset = 0;
        
        try(BufferedInputStream bufferedInputStream = new BufferedInputStream(new FileInputStream(localFile)))
        {
            while((size = bufferedInputStream.read(buffer)) != -1)
            {
                blockDescriptorList.add(
                        new DfsBlockDescriptor(
                                DfsSHA.generate(buffer, DfsSHAVersion.SHA1), 
                                size,
                                offset));
                
                offset += size;
            }
        }
        
        return new DfsFileDescriptor(blockDescriptorList);
    }
    
    public DfsBlockDescriptor getBlockDescriptor(String blockId) throws DfsNotFoundException
    {
        if(!containsBlock(blockId))
        {
            throw new DfsNotFoundException("The block not found");
        }
        
        return blockDescriptorList.get(blockId);
    }
    
    public Collection<DfsBlockDescriptor> getBlockDescriptorList()
    {
        return blockDescriptorList.values();
    }
    
    public boolean containsBlock(String blockId)
    {
        return blockDescriptorList.containsKey(blockId);
    }
    
    public void save(String path) throws IOException
    {
        DfsFileHelper.writeTextFile(path, toString());
    }

    public String getHash() {
        return hash;
    }

    public long getSize() {
        return size;
    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }
    
    @Override
    public synchronized String toString()
    {
        return DfsJsonHelper.objToJson(this);
    }
}
