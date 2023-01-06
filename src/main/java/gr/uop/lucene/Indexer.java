package gr.uop.lucene;

import gr.uop.BibFileFields;
import gr.uop.asteras.AddingBib;

import org.apache.lucene.analysis.en.EnglishAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

import java.io.File;
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
        Field annotation = new Field(BibFileFields.ANNOTATION, AddingBib.getBibAnnotationData(file),
                TextField.TYPE_STORED);
        Field author = getField(BibFileFields.AUTHOR, file);
        Field title = getField(BibFileFields.TITLE, file);
        Field booktitle = getField(BibFileFields.BOOKTITLE, file);
        Field publisher = getField(BibFileFields.PUBLISHER, file);
        Field year = getField(BibFileFields.YEAR, file);

        Field fileNameField = new Field(BibFileFields.FILE_NAME, file.getName(), StringField.TYPE_STORED);
        Field filePathField = new Field(BibFileFields.FILE_PATH, file.getAbsolutePath(), StringField.TYPE_STORED);

        document.add(annotation);
        document.add(author);
        document.add(title);
        document.add(booktitle);
        document.add(publisher);
        document.add(year);
        document.add(fileNameField);
        document.add(filePathField);
        /* Stemmer
        EnglishStemmer stemmer = new EnglishStemmer();
        String[] words = articleData.getBody().split("[ .{},]+");
        stringBuilder = new StringBuilder();
        for (String word : words)
        {
            stemmer.setCurrent(word);
            stemmer.stem();
            stringBuilder.append(stemmer.getCurrent()).append(" ");
        }
        */
        return document;
    }

    private Field getField(String bibFileField, File file)
    {
        return new Field(bibFileField, AddingBib.getBibFieldData(bibFileField, file), TextField.TYPE_STORED);
    }

    private void indexFile(File file)
    {
        System.out.println("Indexing: " + file.getAbsolutePath());
        try
        {
            writer.addDocument(getDocument(file));
        }
        catch (IOException e)
        {
            throw new RuntimeException(e);
        }
    }

    public int createIndex(String dataDirPath) throws IOException
    {
        File[] files = new File(dataDirPath).listFiles(new BibFileFilter());
        if (files == null)
        {
            return -1;
        }

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