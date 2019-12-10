package br.com.ltoscano.dfs.core.config;

import br.com.ltoscano.dfs.core.id.DfsUUID;

/**
 *
 * @author ltosc
 */
public final class DfsConfig
{
    private final String peerName;
    
    private String multicastIP;
    private int multicastPort;
    
    private String tcpIP;
    private int tcpPort;
    
    private int waitTimeForServerToStop;
    
    private int minTimeForPeerAnnouncement;
    private int maxTimeForPeerAnnouncement;
    
    private int timeToCheckPeerStatus;
    private int peerDowntime;
    
    private int minTimeToGetMetadata;
    private int maxTimeToGetMetadata;
    
    private int minTimeToGetData;
    private int maxTimeToGetData;
    
    public DfsConfig()
    {
        this.peerName = DfsUUID.generate();
        
        this.multicastIP = "230.0.0.0";
        this.multicastPort = 8080;
        
        this.tcpIP = "127.0.0.1";
        this.tcpPort = 8888;
        
        this.waitTimeForServerToStop = 10000;
        
        this.minTimeForPeerAnnouncement = 5000;
        this.maxTimeForPeerAnnouncement = 10000;
        
        this.timeToCheckPeerStatus = minTimeForPeerAnnouncement;
        this.peerDowntime = (2 * maxTimeForPeerAnnouncement);
        
        this.minTimeToGetMetadata = 30000;
        this.maxTimeToGetMetadata = 60000;
        
        this.minTimeToGetData = 1000;
        this.maxTimeToGetData = 10000;
    }
    
    /**
     * @return the peerName
     */
    public String getPeerName() {
        return peerName;
    }

    /**
     * @return the minTimeForPeerAnnouncement
     */
    public int getMinTimeForPeerAnnouncement() {
        return minTimeForPeerAnnouncement;
    }

    /**
     * @param minTimeForPeerAnnouncement the minTimeForPeerAnnouncement to set
     */
    public void setMinTimeForPeerAnnouncement(int minTimeForPeerAnnouncement) {
        this.minTimeForPeerAnnouncement = minTimeForPeerAnnouncement;
    }

    /**
     * @return the maxTimeForPeerAnnouncement
     */
    public int getMaxTimeForPeerAnnouncement() {
        return maxTimeForPeerAnnouncement;
    }

    /**
     * @param maxTimeForPeerAnnouncement the maxTimeForPeerAnnouncement to set
     */
    public void setMaxTimeForPeerAnnouncement(int maxTimeForPeerAnnouncement) {
        this.maxTimeForPeerAnnouncement = maxTimeForPeerAnnouncement;
    }

    /**
     * @return the timeToCheckPeerStatus
     */
    public int getTimeToCheckPeerStatus() {
        return timeToCheckPeerStatus;
    }

    /**
     * @param timeToCheckPeerStatus the timeToCheckPeerStatus to set
     */
    public void setTimeToCheckPeerStatus(int timeToCheckPeerStatus) {
        this.timeToCheckPeerStatus = timeToCheckPeerStatus;
    }

    /**
     * @return the peerDowntime
     */
    public int getPeerDowntime() {
        return peerDowntime;
    }

    /**
     * @param peerDowntime the peerDowntime to set
     */
    public void setPeerDowntime(int peerDowntime) {
        this.peerDowntime = peerDowntime;
    }

    /**
     * @return the minTimeToGetMetadata
     */
    public int getMinTimeToGetMetadata() {
        return minTimeToGetMetadata;
    }

    /**
     * @param minTimeToGetMetadata the minTimeToGetMetadata to set
     */
    public void setMinTimeToGetMetadata(int minTimeToGetMetadata) {
        this.minTimeToGetMetadata = minTimeToGetMetadata;
    }

    /**
     * @return the maxTimeToGetMetadata
     */
    public int getMaxTimeToGetMetadata() {
        return maxTimeToGetMetadata;
    }

    /**
     * @param maxTimeToGetMetadata the maxTimeToGetMetadata to set
     */
    public void setMaxTimeToGetMetadata(int maxTimeToGetMetadata) {
        this.maxTimeToGetMetadata = maxTimeToGetMetadata;
    }

    /**
     * @return the waitTimeForServerToStop
     */
    public int getWaitTimeForServerToStop() {
        return waitTimeForServerToStop;
    }

    /**
     * @param waitTimeForServerToStop the waitTimeForServerToStop to set
     */
    public void setWaitTimeForServerToStop(int waitTimeForServerToStop) {
        this.waitTimeForServerToStop = waitTimeForServerToStop;
    }

    /**
     * @return the multicastIP
     */
    public String getMulticastIP() {
        return multicastIP;
    }

    /**
     * @param multicastIP the multicastIP to set
     */
    public void setMulticastIP(String multicastIP) {
        this.multicastIP = multicastIP;
    }

    /**
     * @return the multicastPort
     */
    public int getMulticastPort() {
        return multicastPort;
    }

    /**
     * @param multicastPort the multicastPort to set
     */
    public void setMulticastPort(int multicastPort) {
        this.multicastPort = multicastPort;
    }

    /**
     * @return the tcpIP
     */
    public String getTcpIP() {
        return tcpIP;
    }

    /**
     * @param tcpIP the tcpIP to set
     */
    public void setTcpIP(String tcpIP) {
        this.tcpIP = tcpIP;
    }

    /**
     * @return the tcpPort
     */
    public int getTcpPort() {
        return tcpPort;
    }

    /**
     * @param tcpPort the tcpPort to set
     */
    public void setTcpPort(int tcpPort) {
        this.tcpPort = tcpPort;
    }

    /**
     * @return the minTimeToGetData
     */
    public int getMinTimeToGetData() {
        return minTimeToGetData;
    }

    /**
     * @param minTimeToGetData the minTimeToGetData to set
     */
    public void setMinTimeToGetData(int minTimeToGetData) {
        this.minTimeToGetData = minTimeToGetData;
    }

    /**
     * @return the maxTimeToGetData
     */
    public int getMaxTimeToGetData() {
        return maxTimeToGetData;
    }

    /**
     * @param maxTimeToGetData the maxTimeToGetData to set
     */
    public void setMaxTimeToGetData(int maxTimeToGetData) {
        this.maxTimeToGetData = maxTimeToGetData;
    }
}
