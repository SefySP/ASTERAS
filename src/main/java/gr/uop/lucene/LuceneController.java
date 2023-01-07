package gr.uop.lucene;

import org.apache.lucene.document.Document;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import gr.uop.BibFileFields;

public class LuceneController
{
    public static final String INDEX_DIR = "src/main/resources/gr/uop/asteras/index";
    public static final String DATA_DIR = "src/main/resources/gr/uop/asteras/data";

    private long time;

    private final Indexer indexer;

    public LuceneController()
    {
        try
        {
            this.indexer = new Indexer(INDEX_DIR);
        }
        catch (IOException e)
        {
            throw new RuntimeException(e);
        }
    }

    public void addFileToIndex(File file)
    {
        long startTime = System.currentTimeMillis();
        indexer.indexFile(file);
        long endTime = System.currentTimeMillis();
        System.out.println(file.getName() + " File indexed, time taken: " + (endTime - startTime) + " ms");
        indexer.merge();
        indexer.commit();
    }

    public void createIndex()
    {
        int numIndexed;
        long startTime = System.currentTimeMillis();
        numIndexed = indexer.createIndex(DATA_DIR);
        long endTime = System.currentTimeMillis();
        System.out.println(numIndexed + " File(s) indexed, time taken: " + (endTime - startTime) + " ms");
        indexer.commit();
    }

    public List<File> search(String searchQuery, String[] fields) throws IOException, ParseException
    {
        Searcher searcher = new Searcher(INDEX_DIR, fields);

        long startTime = System.currentTimeMillis();
        TopDocs hits = searcher.search(searchQuery);
        long endTime = System.currentTimeMillis();


        setTime(endTime - startTime);

        return getTopResults(searcher, hits);
    }

    private List<File> getTopResults(Searcher searcher, TopDocs hits) throws IOException
    {
        List<File> topDocsFileList = new ArrayList<>();
        for (ScoreDoc scoreDoc: hits.scoreDocs)
        {
            Document doc = searcher.getDocument(scoreDoc);
            topDocsFileList.add(new File(doc.get(BibFileFields.FILE_PATH)));
        }
        searcher.close();
        return topDocsFileList;
    }

    public long getTime()
    {
        return time;
    }

    public void setTime(long time)
    {
        this.time = time;
    }
}
