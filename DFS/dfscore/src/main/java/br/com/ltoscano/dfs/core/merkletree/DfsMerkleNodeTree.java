package br.com.ltoscano.dfs.core.merkletree;

/**
 *
 * @author ltosc
 */
public class DfsMerkleNodeTree {

    private final String hash;
    private DfsMerkleNodeTree leftNode;
    private DfsMerkleNodeTree rightNode;

    public DfsMerkleNodeTree(String hash) {
        this.hash = hash;
        this.leftNode = null;
        this.rightNode = null;
    }

    public DfsMerkleNodeTree(String hash, DfsMerkleNodeTree leftNode, DfsMerkleNodeTree rightNode) {
        this.hash = hash;
        this.leftNode = leftNode;
        this.rightNode = rightNode;
    }

    public String getHash() {
        return hash;
    }

    public DfsMerkleNodeTree getLeftNode() {
        return leftNode;
    }

    public void setLeftNode(DfsMerkleNodeTree leftNode) {
        this.leftNode = leftNode;
    }

    public DfsMerkleNodeTree getRightNode() {
        return rightNode;
    }

    public void setRightNode(DfsMerkleNodeTree rightNode) {
        this.rightNode = rightNode;
    }
}