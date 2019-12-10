package br.com.ltoscano.dfs.core.file;

import br.com.ltoscano.dfs.core.enums.DfsEnums.DfsPartitionSize;
import br.com.ltoscano.dfs.core.exception.DfsNotFoundException;
import br.com.ltoscano.dfs.core.filesystem.descriptor.DfsBlockDescriptor;
import br.com.ltoscano.dfs.core.filesystem.descriptor.DfsFileDescriptor;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.Iterator;

/**
 *
 * @author ltosc
 */
public final class DfsFileHelper 
{
    private DfsFileHelper()
    {
        
    }
    
    public static DfsFileDescriptor split(String inputFilePath, String outputDirPath, DfsPartitionSize partitionSize) throws IOException
    {
        File inputFile = new File(inputFilePath);
        
        if(!inputFile.exists())
        {
            throw new FileNotFoundException("File '" + inputFilePath + "' does not exist");
        }
        
        DfsFileDescriptor fileDescriptor = DfsFileDescriptor.buildFileDescriptor(inputFilePath, partitionSize);
        
        File outputDir = new File(Paths.get(outputDirPath, fileDescriptor.getHash()).toString());
        
        if(!outputDir.exists())
        {
            outputDir.mkdirs();
        }
        
        Collection<DfsBlockDescriptor> blockDescriptorList = fileDescriptor.getBlockDescriptorList();
        
        try(RandomAccessFile randomAccessFile = new RandomAccessFile(inputFile, "r"))
        {
            byte[] buffer;
            Iterator<DfsBlockDescriptor> blockIt = blockDescriptorList.iterator();
            
            while(blockIt.hasNext())
            {
                DfsBlockDescriptor blockDescriptor = blockIt.next();
                
                String blockFilePath = Paths.get(outputDir.toString(), blockDescriptor.getHash()).toString();
                buffer = new byte[Math.toIntExact(blockDescriptor.getSize())];
                
                randomAccessFile.seek(blockDescriptor.getOffset());
                randomAccessFile.read(buffer);
                
                try(FileOutputStream fileOutputStream = new FileOutputStream(blockFilePath))
                {
                    fileOutputStream.write(buffer);
                }
            }
        }
        
        return fileDescriptor;
    }
    
    public static void merge(String inputDirPath, String outputFilePath, DfsFileDescriptor fileDescriptor) throws FileNotFoundException, IOException, DfsNotFoundException
    {
        File inputDir = new File(inputDirPath);
        
        if(!inputDir.exists())
        {
            throw new FileNotFoundException("Directory '" + inputDirPath + "' does not exist");
        }
        
        try(RandomAccessFile randomAccessFile = new RandomAccessFile(outputFilePath, "rw"))
        {
            File[] fileList = inputDir.listFiles();
            
            for (File file : fileList) 
            {
                DfsBlockDescriptor blockDescriptor = fileDescriptor.getBlockDescriptor(file.getName());
                
                try(FileInputStream fileInputStream = new FileInputStream(file))
                {
                    randomAccessFile.seek(blockDescriptor.getOffset());
                    randomAccessFile.write(fileInputStream.readAllBytes());
                }
            }
        }
    }
    
    public static String readTextFile(String filePath) throws FileNotFoundException, IOException
    {
        StringBuilder strBuilder = new StringBuilder();
        String tmpLine;
        
        try(BufferedReader bufferedReader = new BufferedReader(new FileReader(filePath)))
        {
            while((tmpLine = bufferedReader.readLine()) != null)
            {
                strBuilder.append(tmpLine);
            }
        }
        
        return strBuilder.toString();
    }
    
    public static void writeTextFile(String path, String content) throws IOException
    {
        try(FileWriter fileWriter = new FileWriter(path))
        {
            fileWriter.write(content);
        }
    }
}
