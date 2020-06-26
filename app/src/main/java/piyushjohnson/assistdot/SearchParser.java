package piyushjohnson.assistdot;

import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SearchParser {


    private HashMap<String,String> result;
    private String symbol = null,sub = null,str = null;

    SearchParser(String str)
    {
        this.str = str;
    }

    public HashMap<String,String> parse()
    {
        Pattern pattern = Pattern.compile("[#!^@*.]*[#!^@*.]");
        Matcher matcher = pattern.matcher(str);
        result = new HashMap<>();
        /*if (!str.matches("[#:!^*.].*"))
        {
            result.put("Keyword",str);
            return result;
        }*/
        int count = -1;
        int start = -1,end = -1,pend = -1,pstart = -1;
        while(matcher.find())
        {
            start = matcher.start();
            end = matcher.end();
            /*Log.d("Start : " + start + " End :" + end);
            Log.d("Previous End : " + pend + " Previous Start : " + pstart);
            Log.d("Count : " + count++);*/
            if(pend == -1)
            {
                symbol = matcher.group();
            }
            else
            {
                sub = str.substring(pend,start);
                setResult(symbol);
                symbol = matcher.group();
                /*Log.d(Value : " + str.substring(pend,start));*/
//                Log.d("App:","Symbol - "+);

            }

            pend = end;
            pstart = start;
        }

        return result;

    }

    public void setResult(String symbol)
    {
        switch (symbol)
        {
            case "#":
                result.put("Tag",sub);
                break;

            case "!":
                result.put("Reminder",sub);

                break;

            case "^":
                result.put("Date",sub);

                break;

            case "@":
                result.put("Category",sub);

                break;

            case "*":
                result.put("State",sub);

                break;

            default:

        }
    }

  
}
