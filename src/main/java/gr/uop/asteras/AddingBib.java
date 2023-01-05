package gr.uop.asteras;

import java.io.*;
import java.util.Scanner;

public class AddingBib
{

    private final File file;

    public AddingBib(File file)
    {
        this.file = file;
    }

    public String getBibFieldData(String field)
    {
        StringBuilder data = new StringBuilder();
        try
        {
            boolean isFieldFound = false;
            Scanner input = new Scanner(file);
            String fieldData;

            while (input.hasNextLine())
            {
                fieldData = input.nextLine().trim();
                if (isContains(fieldData, field))
                {
                    var elements = fieldData.split("=");
                    if (!isEquals(field, elements))
                        continue;
                    if (isContains(elements[1].trim(), "},"))
                    {
                        data.append(elements[1].trim()).append("\n");
                        continue;
                    }
                    data.append(elements[1].trim());
                    isFieldFound = true;
                    continue;
                }
                if (isContains(fieldData, "},") && isFieldFound)
                {
                    data.append(" ").append(fieldData.trim()).append("\n").trimToSize();
                    isFieldFound = false;
                }
            }
        }
        catch (FileNotFoundException exception)
        {
            System.out.println(exception.getMessage());
        }
        return data.toString().trim();
    }

    private static boolean isContains(String elements, String s)
    {
        return elements.contains(s);
    }

    private static boolean isEquals(String field, String[] elements)
    {
        return elements[0].trim().equals(field);
    }
}
