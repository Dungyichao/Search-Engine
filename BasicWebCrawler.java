package com.mkyong.basicwebcrawler;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.HashSet;
import java.util.Scanner;

import java.net.*;

/**
 * Created by famou on 6/30/2017.
 */
public class BasicWebCrawler {
    //private static int Max_depth = 0;
    private int count = 0;
    private HashSet<String> links;
    //private String searchword;

    public BasicWebCrawler(){
        links = new HashSet<>();
    }

    public HashSet<String> getPageLinks(String URL, int depth, int finaldepth){
        //this.searchword = searchword;
        int Max_depth = finaldepth;
        if ((!links.contains(URL)) && (depth<Max_depth)) {
            try{
                //System.out.println("Depth: " + depth);
                links.add(URL);
                //System.out.println(URL);
                Document document = Jsoup.connect(URL).get();
                Elements linksOnPage = document.select("a[href]");
                depth++;
                for (Element page : linksOnPage){
                    getPageLinks(page.attr("abs:href"), depth, Max_depth);
                }
            } catch (IOException e){
                //System.out.println("For '" + URL + "': " + e.getMessage());
            }
            count = count+1;
        }
        //System.out.println("#####################################################");
        return links;
    }

    public static String readURLContent(String urlString) throws IOException
    {
        URL url = new URL(urlString);
        Scanner scan = new Scanner(url.openStream());

        String content = new String();
        while (scan.hasNext())
            content += scan.nextLine();
        scan.close();
        return content;
    }

    public static String findTitle(String str)
    {
        String tagOpen = "<title>";
        String tagClose = "</title>";

        int begin = str.indexOf(tagOpen) + tagOpen.length();
        int end = str.indexOf(tagClose);
        return str.substring(begin, end);
    }
    public int URLwithsearchword(HashSet<String> links, String[] searchword){
        int count = 0;
        for (String j: links){
            String content = null;
            try {
                content = readURLContent(j);
            } catch (IOException e) {
                e.getMessage();
            }
            int counter = 0;
            for (String searchwords: searchword){
                if (content != null && content.contains(searchwords)) {
                    counter = counter + 1;
                }
            }
            if (counter == searchword.length) {
                String title = findTitle(content);
                System.out.println(title + "[" + j + "]");
                int i = showcontentsearch(content, searchword);
                //System.out.println("     ................." + words + "  ......................");
                //System.out.println("Title: " + title);
                count = count + i;
                System.out.println("............." + i + "  Search Match");
            }
        }
        return count;

    }

    public int showcontentsearch(String str, String[] Searchword){
        int i = Searchword[0].length();
        int j = str.indexOf(Searchword[0]);
        //String contentword = Searchword;
        int k = 0;
        while(j>=0) {
            int counter = 0;
            for (String Searchwords: Searchword){
                if (str.substring(j-20,j+i+60).contains(Searchwords)){
                    counter = counter + 1;
                }
            }
            if (counter == Searchword.length){
                System.out.println("........" + str.substring(j-20,j+i+60));
                k++;
            }
            j = str.indexOf(Searchword[0], j+i+60);
        }
        //int begin = str.indexOf(contentword);
        //int end = begin + contentword.length();
        //if(begin >= 10){return str.substring(begin-10, end + 30);}
        //else{return str.substring(begin, end + 40);}
        return k;
    }
    public static void main(String[] args){
        Scanner s = new Scanner(System.in);
        System.out.println("Enter URL: ");
        String urlname = s.nextLine();
        Scanner w = new Scanner(System.in);
        //w.useDelimiter(",");
        System.out.println("Search Words: ");
        String searchword[] = w.nextLine().split(",");
        System.out.println("search word: "+searchword.length);
        Scanner i = new Scanner(System.in);
        System.out.println("Depth: ");
        int finaldepth = i.nextInt();
        HashSet<String> links;
        links = new BasicWebCrawler().getPageLinks(urlname, 0, finaldepth);
        //System.out.println("#######################################################");
        //System.out.println(links);
        //System.out.println("#######################################################");
        int count = new BasicWebCrawler().URLwithsearchword(links, searchword);
        System.out.println("There are " + count + " results.");
    }
}
