package gr.uop;

import java.io.File;
import java.io.FileFilter;

public class BibFileFilter implements FileFilter
{

    @Override
    public boolean accept(File file)
    {
        return file.getName().toLowerCase().endsWith(".bib");
    }
}
