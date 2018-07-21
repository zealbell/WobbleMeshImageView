package linkersoft.blackpanther.wobble.utils;


import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class util {

    public static class StringIO{
        public static String getStringsInBetween(String Sentence, String start, String end, boolean inclusive, boolean multilineSentence){
            //fix this:: the regex results in a stackoverflow error when the lines r too much
            String inbetween=null;
            Pattern pattern=multilineSentence? Pattern.compile(start+"(((.|\\n)||(.|\\r)||(.|\\r\\n))*?)"+end): Pattern.compile(start+"(.*?)"+end);
            Matcher matchsticks= pattern.matcher(Sentence);
            while (matchsticks.find())inbetween=matchsticks.group();
            if(!inclusive && inbetween!=null){
                inbetween=inbetween.replaceAll(start, "");
                inbetween=inbetween.replaceAll(end, "");
            }return inbetween;

        }
    }

}