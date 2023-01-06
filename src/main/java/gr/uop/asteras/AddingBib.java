package gr.uop.asteras;

import java.io.*;
import java.util.Scanner;

public class AddingBib
{
    public static String getBibAnnotationData(File file)
    {
        StringBuilder data = new StringBuilder();
        try
        {
            Scanner input = new Scanner(file);
            String annotetiondata;
            while (input.hasNextLine())
            {
                annotetiondata = input.nextLine().trim();
                if (annotetiondata.contains("@"))
                {
                    var elements = annotetiondata.split("\\{");
                    data.append(elements[1]).append("\n");
                }
            }
        }
        catch (FileNotFoundException exception)
        {
            System.out.println(exception.getMessage());
        }
        return data.toString();
    }

    public static String getBibFieldData(String field, File file)
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
