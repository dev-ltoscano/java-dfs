package br.com.ltoscano.dfs.core.merkletree;

import br.com.ltoscano.dfs.core.array.DfsArrayHelper;
import br.com.ltoscano.dfs.core.exception.DfsRuntimeException;
import br.com.ltoscano.dfs.core.sha.DfsSHA;
import br.com.ltoscano.dfs.core.string.DfsStringHelper;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author ltosc
 */
public class DfsMerkleTree 
{
    private final DfsMerkleNodeTree rootNode;
    private final String hash;
    
    private DfsMerkleTree(DfsMerkleNodeTree rootNode)
    {
        this.rootNode = rootNode;
        this.hash = rootNode.getHash();
    }
    
    private static DfsMerkleNodeTree recursivelyBuildTree(List<DfsMerkleNodeTree> childNodeList) throws IOException
    {
        if(childNodeList.size() == 1)
        {
            return childNodeList.get(0);
        }
        
        List<DfsMerkleNodeTree> parentNodeList = new ArrayList<>();
        int lastIndex = (childNodeList.size() - 1);
        
        for(int i = 0; i <= lastIndex; i += 2)
        {
            if(i == lastIndex)
            {
                parentNodeList.add(childNodeList.get(i));
            }
            else
            {
                DfsMerkleNodeTree firstChidlNode = childNodeList.get(i);
                DfsMerkleNodeTree secondChildNode = childNodeList.get(i + 1);
                
                String parentHash = DfsSHA.generate(DfsArrayHelper.concat(
                            firstChidlNode.getHash().getBytes(StandardCharsets.UTF_8), 
                            secondChildNode.getHash().getBytes(StandardCharsets.UTF_8)),
                    DfsSHA.DfsSHAVersion.SHA1);
                
                parentNodeList.add(new DfsMerkleNodeTree(parentHash, firstChidlNode, secondChildNode));
            }
        }
        
        return recursivelyBuildTree(parentNodeList);
    }
    
    public static DfsMerkleTree build(List<String> leavesHashList) throws IOException
    {
        List<DfsMerkleNodeTree> leavesNodeList = new ArrayList<>();
        
        for(String leafHash : leavesHashList)
        {
            if(DfsStringHelper.isNullOrEmpty(leafHash))
            {
                throw new DfsRuntimeException("The hash is null or empty");
            }
            
            leavesNodeList.add(new DfsMerkleNodeTree(leafHash));
        }
        
        return new DfsMerkleTree(recursivelyBuildTree(leavesNodeList));
    }
    
    public static String buildHash(List<String> leavesHashList) throws IOException
    {
        List<DfsMerkleNodeTree> leavesNodeList = new ArrayList<>();
        
        for(String leafHash : leavesHashList)
        {
            if(DfsStringHelper.isNullOrEmpty(leafHash))
            {
                throw new DfsRuntimeException("The hash is null or empty");
            }
            
            leavesNodeList.add(new DfsMerkleNodeTree(leafHash));
        }
        
        return recursivelyBuildTree(leavesNodeList).getHash();
    }
    
    public boolean isEqualTo(DfsMerkleTree otherTree)
    {
        if(otherTree == null)
        {
            throw new IllegalArgumentException("The parameter is null");
        }
        
        return getHash().equalsIgnoreCase(otherTree.getHash());
    }

    public DfsMerkleNodeTree getRootNode() {
        return rootNode;
    }

    public String getHash() {
        return hash;
    }
}
