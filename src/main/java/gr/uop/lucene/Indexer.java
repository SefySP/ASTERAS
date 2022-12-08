package gr.uop.lucene;

import org.apache.lucene.analysis.en.EnglishAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import gr.uop.BibFileFilter;

public class Indexer implements AutoCloseable
{
    private final IndexWriter writer;

    public Indexer(String indexDirectoryPath) throws IOException
    {
        Path indexPath = Paths.get(indexDirectoryPath);

        Directory indexDirectory = FSDirectory.open(indexPath);
        //create the indexer
        IndexWriterConfig config = new IndexWriterConfig(new EnglishAnalyzer(EnglishAnalyzer.ENGLISH_STOP_WORDS_SET));
        writer = new IndexWriter(indexDirectory, config);
    }

    public void close() throws IOException
    {
        writer.commit();
        writer.close();
    }

    private Document getDocument(File file)
    {
        Document document = new Document();
        //index file contents
        try (BufferedReader br = new BufferedReader(new FileReader(file)))
        {
            String currentLine;
            StringBuilder stringBuilder = new StringBuilder();
            while ((currentLine = br.readLine()) != null)
            {
                stringBuilder.append(currentLine);
            }

            /* Stemmer
            EnglishStemmer stemmer = new EnglishStemmer();
            String[] words = articleData.getBody().split("[ .,]+");
            stringBuilder = new StringBuilder();
            for (String word : words)
            {
                stemmer.setCurrent(word);
                stemmer.stem();
                stringBuilder.append(stemmer.getCurrent()).append(" ");
            }
            */
        }
        catch (IOException ioException)
        {
            System.out.println(ioException.getMessage());
        }
        return document;
    }

    private void indexFile(File file) throws IOException
    {
//        System.out.println("Indexing: " + file.getCanonicalPath());
        writer.addDocument(getDocument(file));
     }

    public int createIndex(String dataDirPath) throws IOException
    {
        File[] files = new File(dataDirPath).listFiles(new BibFileFilter());
        if (files == null)
            return -1;

        for (File file : files)
        {
            if (checkFile(file))
            {
                indexFile(file);
            }
        }
        return writer.numRamDocs();
    }

    private boolean checkFile(File file)
    {
        return !file.isHidden()
                && file.canRead();
    }
}