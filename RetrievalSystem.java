/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */




/**
 *
 * @author Divya
 */
import java.io.*;
import java.util.*;
import org.jsoup.Jsoup;
public class RetrievalSystem {
    
    
 static ArrayList<String> queryArray = new ArrayList<String>();
 static List invertedIndexStem   = new ArrayList();
 static Set<String> stopwords = new HashSet<String>();
 static HashMap<Long,String> MAXTFStem = new HashMap<Long,String>();
 static HashMap<Integer,Integer> DoclenStem = new HashMap<Integer,Integer>();
 static HashMap<String,Long> TFMapStem = new HashMap<String,Long>();
 static HashMap<Integer, ArrayList> queryMap = new HashMap<Integer, ArrayList>();
 static int queryCount = 0;
 static int collectionSize = 0;
 static int totalDoclen = 0;
 static TreeMap<Integer, Double> W1Map = new TreeMap<Integer, Double>();
 static TreeMap<Integer, Double> W2Map = new TreeMap<Integer, Double>();
 
 //change
 static HashMap<String, Integer> WDocLength = new HashMap<String, Integer>();
 static HashMap<Integer, Integer> timesMap = new HashMap<Integer, Integer>();
 static HashMap<String, HashMap> wTFStemMap = new HashMap<String, HashMap>();
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws FileNotFoundException, IOException {
      String filePath = args[0];

        String query = "";
        boolean nextQn = false;
         String fullQuery = "";
   // String filePath = "C:/Users/Divya/Desktop/Semester4/Information Retrieval/Assignment/Assignment1/Cranfield/";
    ListStopWords("stopwords");
     //ListStopWords("C:/Users/Divya/Desktop/Semester4/Information Retrieval/Assignment/Assignment1/stopwords");
     
     
     
      long startIndexVersion2 =  System.currentTimeMillis();
                StemIndexGeneration(filePath);
                long endIndexVersion2 = System.currentTimeMillis();
                long Index2_time = endIndexVersion2-startIndexVersion2;
                String LastQuery = "";
                int justCount =0;
               BufferedReader reader = new BufferedReader(new FileReader("hw3.queries"));
                // BufferedReader reader = new BufferedReader(new FileReader("C:/Users/Divya/Desktop/Semester4/Information Retrieval/Assignment/Assignment_3/hw3.queries"));
    			 query = reader.readLine();
                          while(query != null){
                             
                              if(!query.startsWith("Q")){
                                  justCount++;
                                   nextQn = false;
                                  if(nextQn==false){
                                  fullQuery = fullQuery+ " "+query;
                                  LastQuery= fullQuery;
                                  
                                  }
                                  
                              }
                              else{
                                 
                                  if(justCount !=0)
                                indexQuery(fullQuery);
                                nextQn = true;
                                fullQuery = "";
                              }   
                             
                               query = reader.readLine(); 
                              
                          }
                          indexQuery(LastQuery);
                          
                          weightFunctions(filePath);
    }
    
  public static void indexQuery(String fullQuery){
      queryCount++;
      Stemmer porter = new Stemmer(); 
       StringTokenizer query = new StringTokenizer(fullQuery," ");
        while (query.hasMoreTokens())
            {
             String token = query.nextToken().replaceAll("[-/()==?'+.^:,]","").toLowerCase(); 
              if (stopwords.contains(token) == false){
                  int j = token.length();
  char[] w = token.toCharArray();
  for (int c = 0; c < j; c++) porter.add(w[c]);
  porter.stem();
                    {  String u;
                       u = porter.toString();
queryArray.add(u);
                    }
              }
            }
       // System.out.println(queryArray);
        queryMap.put(queryCount, queryArray);
        //queryArray.clear();
     queryArray = new ArrayList<String>();
       
  }
  
  //implementing weight functions W1 and w2
  public static void weightFunctions(String filePath) throws FileNotFoundException, IOException{
      int QueryCount = 0;
      //System.out.println(queryMap.size());
      Iterator<Integer> itemQuerymap = queryMap.keySet().iterator(); 
       while(itemQuerymap.hasNext()){
            W1Map .clear();
	W2Map.clear();
           QueryCount++;
  int  QueryId = itemQuerymap.next();
  ArrayList<String> SinglequeryArray = new ArrayList<String>();
          ArrayList SinglequeryArray1 = queryMap.get(QueryId);
          System.out.println("Query No: " + QueryId + " Indexed form of Query " + SinglequeryArray1 );
          System.out.println("..............................................................................");
   ListIterator<?> tokenListing = SinglequeryArray1.listIterator();
   while (tokenListing.hasNext()){
       String token = tokenListing.next().toString();
 
   
HashMap<?, ?> storage = new HashMap<Object, Object>();
			
Iterator<?> iter = wTFStemMap.entrySet().iterator();
while (iter.hasNext()) {
Map.Entry entries = (Map.Entry) iter.next();
String convert_tf = entries.getKey().toString();
if (convert_tf.equals(token)) {
storage = (HashMap<?, ?>) entries.getValue();
Iterator<?> iter1 = storage.entrySet().iterator();
while (iter1.hasNext()) {
Map.Entry val = (Map.Entry) iter1.next();
						
Long num = Long.parseLong(val.getKey().toString());
 //  System.out.println("docNoInTf" + docNoInTf);
Integer termfreq = Integer.parseInt(val.getValue().toString());
 
String[] maxtf = ( MAXTFStem.get(num)).split("-");
 //  System.out.println("MAXTF VALUE" + MAXTFStem.get(docNoInTf));
Integer maxtfInt = Integer.parseInt(maxtf[1]);

Integer docfreq = Integer.parseInt(WDocLength.get(convert_tf).toString());

 Integer documentNo = Integer.valueOf(num.intValue());
// System.out.println("docLength" + DoclenStem.get(documentNo));
Integer doclength = Integer.parseInt(DoclenStem.get(documentNo).toString());
Double w11 = W1Calculator(termfreq, maxtfInt, docfreq, doclength);
Double w22 = W2Calculator(termfreq, maxtfInt, docfreq, doclength);
 if (!W1Map.containsKey((int)(long)num))
{
W1Map.put((int)(long)num, w11);
}
else {
Double w1 = W1Map.get((int)(long)num);
w1 = w1 + w11;
W1Map.put((int)(long)num, w1);
}
if (!W2Map.containsKey((int)(long)num)){
W2Map.put((int)(long)num, w22);
}
else {
Double w2 = W2Map.get((int)(long)num);
w2 = w2 + w22;
W2Map.put((int)(long)num, w2);
}
 }
  }
 }
  }
   
 
   // System.out.println("\n");
   int rank = 0;
   System.out.println("SEARCH RESULTS BY W1 WEIGHTING FUNCTION");
   System.out.println("QueryNo"+QueryId);
   for(int i =0 ;i<10; i++){
       rank++;
    Map.Entry<Integer,Double> maxEntry = null;
for(Map.Entry<Integer,Double> entry : W1Map.entrySet()) {
    if (maxEntry == null || entry.getValue() > maxEntry.getValue()) {
        maxEntry = entry;
        
    }
}
System.out.println("Rank."+rank+"  External DocumentID:  " + DocnameFinder(maxEntry.getKey()) + "        Score " +maxEntry.getValue());
String titleLine = "";
              // String filePath;
            BufferedReader scanner1 = new BufferedReader(new FileReader(filePath+DocnameFinder(maxEntry.getKey())));
            String lineParser=scanner1.readLine();
        while (lineParser!= null){
            if(lineParser.startsWith("<TITLE>")){
                 while (!lineParser.equals("</TITLE>")){
                titleLine = titleLine + " "+lineParser;
                lineParser = scanner1.readLine();
                 }
            }
            lineParser = scanner1.readLine();
            
        }
 org.jsoup.nodes.Document doc = Jsoup.parse(titleLine);
String text = doc.title().toString();
System.out.println("HeadLine:  " + text);
W1Map.remove(maxEntry.getKey()); 
   }  
   System.out.println("\n");
   System.out.println("SEARCH RESULTS BY W2 WEIGHTING FUNCTION");
   System.out.println("QueryNo "+QueryId);
   rank = 0;
   for(int i =0 ;i<10; i++){
       rank++;
  // System.out.println("Checking");
    Map.Entry<Integer,Double> maxEntry = null;
for(Map.Entry<Integer,Double> entry : W2Map.entrySet()) {
    if (maxEntry == null || entry.getValue() > maxEntry.getValue()) {
        maxEntry = entry;
        
    }
}
System.out.println("Rank."+rank+"  External DocumentID:   " + DocnameFinder(maxEntry.getKey()) + "        Score " +maxEntry.getValue());

String titleLine = "";
 BufferedReader scanner1 = new BufferedReader(new FileReader(filePath+DocnameFinder(maxEntry.getKey())));
            //BufferedReader scanner1 = new BufferedReader(new FileReader("C:/Users/Divya/Desktop/Semester4/Information Retrieval/Assignment/Assignment1/Cranfield/"+DocnameFinder(maxEntry.getKey())));
            String lineParser=scanner1.readLine();
        while (lineParser!= null){
            if(lineParser.startsWith("<TITLE>")){
                 while (!lineParser.equals("</TITLE>")){
                titleLine = titleLine + " "+lineParser;
                lineParser = scanner1.readLine();
                 }
            }
            lineParser = scanner1.readLine();
            
        }
 org.jsoup.nodes.Document doc = Jsoup.parse(titleLine);
String text = doc.title().toString();
System.out.println("HeadLine:  " + text);
        
W2Map.remove(maxEntry.getKey()); 
   } 
   
   System.out.println("*******************************************************************************************************");
       }
      
  }
  
  public static String DocnameFinder(int DocId){
      String docName = null;
      String org = Integer.toString(DocId);
      if(org.length()==1){
          docName = "cranfield000"+org;
      }
      else if (org.length()==2){
          docName = "cranfield00"+org;}
       else if (org.length()==3){
          docName = "cranfield0"+org;}
       else if (org.length()==4){
          docName = "cranfield"+org;}
      return docName; 
  }
  
  
  public static Double W1Calculator(Integer tf, Integer maxtf, Integer df, Integer doclen) {
    
		Double W1 = 0.0;
		W1 = (0.4 + 0.6 * Math.log((double)tf + 0.5) / Math.log((double)maxtf + 1.0))
				* (Math.log((double)collectionSize / df) / Math.log((double)collectionSize));
		return W1;
	}

	public static Double W2Calculator(Integer tf, Integer maxtf, Integer df, Integer doclen) {
              int avgDoclen = (totalDoclen/collectionSize);
		Double W2 = 0.0;
		W2 = (0.4 + 0.6 * (tf / (tf + 0.5 + 1.5 * (doclen / (avgDoclen ))))
				* Math.log((double)collectionSize / df) / Math.log((double)collectionSize));
		return W2;
	}
  
    //reading stopwords from file and storing in hash set
  private static void ListStopWords(String filePath) throws FileNotFoundException, IOException {	
          FileReader stopWords = new FileReader(filePath);
          BufferedReader bufferRead = new BufferedReader(stopWords);
          String nextLine;
          while((nextLine = bufferRead.readLine())!= null)
          stopwords.add(nextLine);
	 	
	}
  
  
   public static void StemIndexGeneration(String filepath) throws FileNotFoundException, IOException{
      Long initialize = 1L;
     int  docIdPresent = 0;
     int listIndex = 0;
     int docIndex = 0;
     int filecount = 0;
     int stemPresent = 0;
     Stemmer porter = new Stemmer(); 
   File folder = new File(filepath);
   File[] listOfFiles = folder.listFiles(); 
   for (File file : listOfFiles) {
       TFMapStem.clear();
        int count_word_total = 0;
        filecount++;
        int count_words = 0;
    if (file.isFile()) {
        collectionSize++;
        //reading from each file inside cranfield
        BufferedReader scanner = new BufferedReader(new FileReader(filepath+"/"+file.getName()));
        String fileName=null;
        String line=scanner.readLine();
        while (line!= null){
        
        if(!line.startsWith("<")){
        StringTokenizer token1 = new StringTokenizer(line,"-// ");
         while (token1.hasMoreTokens())
            {
                 
                String token = token1.nextToken().replaceAll("[-/()==?'+.^:,]","").toLowerCase();
                String toStem = token.toLowerCase();
                count_word_total++;
                if (stopwords.contains(toStem) == false){// && !("*".equals(toStem)) && !("".equals(toStem))&& !("$".equals(toStem))&& ("'".equals(toStem))  ){
  int j = toStem.length();
  char[] w = toStem.toCharArray();
  for (int c = 0; c < j; c++) porter.add(w[c]);
//calling stem function to stem each string.
  porter.stem();
                    {  String u;

                       /* and now, to test toString() : */
                       u = porter.toString();
                       /* to test getResultBuffer(), getResultLength() : */
                       /* u = new String(s.getResultBuffer(), 0, s.getResultLength()); */
             
//System.out.println(u);
if(TFMapStem.containsKey(u))
 {
 Long  tf = TFMapStem.get(u);
TFMapStem.put(u, tf+1);
 }
 else
 TFMapStem.put(u, initialize);


 for(int invIndex=0;invIndex<invertedIndexStem.size();invIndex++)
{
 IndexValues stemDetails = (IndexValues)invertedIndexStem.get(invIndex);
 if(stemDetails.term.equals(u))
{
 stemPresent = 1;
listIndex = invIndex;
 break;
 }
 }
if(stopwords.contains(u) == false){
 if (stemPresent == 1){
IndexValues val = (IndexValues)invertedIndexStem.get(listIndex);
for(int docIdIndex=0;docIdIndex<val.docIds.size();docIdIndex++)
{
if(val.docIds.get(docIdIndex) == filecount){
docIdPresent=1;
docIndex = docIdIndex;
 break;
}
}
 if(docIdPresent == 0){
 val.docIds.add((long)filecount);
 val.termFreq.add(1L);
 }
else {					   
Long temp = Long.parseLong(val.termFreq.get(docIndex).toString());
temp++;
val.termFreq.remove(docIndex);
val.termFreq.add(docIndex,temp);
docIdPresent = 0;
}
invertedIndexStem.remove(listIndex);
 invertedIndexStem.add(listIndex,val);
										   
stemPresent = 0;            
 }
else
 {
 IndexValues newEntry = new IndexValues();
 newEntry.term = u;
 newEntry.docIds.add((long)filecount);
newEntry.termFreq.add(1L);
 invertedIndexStem.add(newEntry);
			                            
 }
  }
}
}
}
 }
         
    ListIterator invIndexIterator = invertedIndexStem.listIterator();
                     
for(int invIndexIt=0;invIndexIt<invertedIndexStem.size();invIndexIt++){
 IndexValues iv = (IndexValues)invIndexIterator.next();                                                            
 int docfreq = iv.docIds.size();
 iv.docFreq = docfreq;
					}
      
        
    line = scanner.readLine();
    
                                          }
        
      



    }
    totalDoclen = totalDoclen + count_word_total;
    DoclenStem.put(filecount, count_word_total);
    TFMapStem.remove("");
    
    
   
    
   // System.out.println("Display entries in a Doclen");
   Iterator<Integer> iterTree = DoclenStem.keySet().iterator(); 
   while(iterTree.hasNext()){
  int TreeNamekey = iterTree.next();
 // System.out.println("key: " + TreeNamekey + " value: " + DoclenStem.get(TreeNamekey));
 
 
}
    
    //
    String maxval= null;
    Map.Entry<String,Long> maxEntry = null;
    long freq = 0L;
  Iterator<Map.Entry<String, Long>> it = TFMapStem.entrySet().iterator();
			    while (it.hasNext()) {
			        Map.Entry pairs = (Map.Entry)it.next();
			        if ((Long)pairs.getValue() > freq)
			        {
			        	freq = (Long)pairs.getValue();
			        	maxval = pairs.getKey().toString();
			        }
			       
			        it.remove(); 
				
		     }
                         
			    MAXTFStem.put((long)filecount, maxval+"-"+freq);
    //System.out.println("MAXTFStem  " + MAXTFStem.get(filecount));
                          }
   
   
   
  

 for (int loop = 0;loop<invertedIndexStem.size();loop++)  {
		   
IndexValues iv = (IndexValues)invertedIndexStem.get(loop);
String insertVal = iv.term;
int mydocfrq = (int) iv.docFreq;
WDocLength.put(insertVal, mydocfrq);
ListIterator<?> iter5 = iv.docIds.listIterator();
while (iter5.hasNext()) {
Integer insertId = Integer.parseInt(iter5.next().toString());
int docval = iter5.previousIndex();
ListIterator<?> tfiter = iv.termFreq.listIterator();
while (tfiter.hasNext()) {
Integer times = Integer.parseInt(tfiter.next().toString());
int freqItr = tfiter.previousIndex();
if (docval == freqItr) {
timesMap.put(insertId, times);
break;
}
}
}
wTFStemMap.put(insertVal, timesMap);
timesMap = new HashMap<Integer, Integer>();
} 
} 
  
}



class IndexValues {
    public String term;
    public int docFreq;
    public ArrayList<Long> termFreq;
    public ArrayList<Long> docIds;
    
    public IndexValues()
    {
        term = "";
        docFreq = 0;
        termFreq = new ArrayList<Long>();
        docIds = new ArrayList<Long>();
        
    }
    
}


//porter stemmer class
 class Stemmer
{  private char[] b;
private int i,     /* offset into b */
            i_end, /* offset to end of stemmed word */
            j, k;
private static final int INC = 50;
                  /* unit of size whereby b is increased */
public Stemmer()
{  b = new char[INC];
   i = 0;
   i_end = 0;
}

/**
 * Add a character to the word being stemmed.  When you are finished
 * adding characters, you can call stem(void) to stem the word.
 */

public void add(char ch)
{  if (i == b.length)
   {  char[] new_b = new char[i+INC];
      for (int c = 0; c < i; c++) new_b[c] = b[c];
      b = new_b;
   }
   b[i++] = ch;
}


/** Adds wLen characters to the word being stemmed contained in a portion
 * of a char[] array. This is like repeated calls of add(char ch), but
 * faster.
 */

public void add(char[] w, int wLen)
{  if (i+wLen >= b.length)
   {  char[] new_b = new char[i+wLen+INC];
      for (int c = 0; c < i; c++) new_b[c] = b[c];
      b = new_b;
   }
   for (int c = 0; c < wLen; c++) b[i++] = w[c];
}

/**
 * After a word has been stemmed, it can be retrieved by toString(),
 * or a reference to the internal buffer can be retrieved by getResultBuffer
 * and getResultLength (which is generally more efficient.)
 */
public String toString() { return new String(b,0,i_end); }

/**
 * Returns the length of the word resulting from the stemming process.
 */
public int getResultLength() { return i_end; }

/**
 * Returns a reference to a character buffer containing the results of
 * the stemming process.  You also need to consult getResultLength()
 * to determine the length of the result.
 */
public char[] getResultBuffer() { return b; }

/* cons(i) is true <=> b[i] is a consonant. */

private final boolean cons(int i)
{  switch (b[i])
   {  case 'a': case 'e': case 'i': case 'o': case 'u': return false;
      case 'y': return (i==0) ? true : !cons(i-1);
      default: return true;
   }
}

/* m() measures the number of consonant sequences between 0 and j. if c is
   a consonant sequence and v a vowel sequence, and <..> indicates arbitrary
   presence,

      <c><v>       gives 0
      <c>vc<v>     gives 1
      <c>vcvc<v>   gives 2
      <c>vcvcvc<v> gives 3
      ....
*/

private final int m()
{  int n = 0;
   int i = 0;
   while(true)
   {  if (i > j) return n;
      if (! cons(i)) break; i++;
   }
   i++;
   while(true)
   {  while(true)
      {  if (i > j) return n;
            if (cons(i)) break;
            i++;
      }
      i++;
      n++;
      while(true)
      {  if (i > j) return n;
         if (! cons(i)) break;
         i++;
      }
      i++;
    }
}

/* vowelinstem() is true <=> 0,...j contains a vowel */

private final boolean vowelinstem()
{  int i; for (i = 0; i <= j; i++) if (! cons(i)) return true;
   return false;
}

/* doublec(j) is true <=> j,(j-1) contain a double consonant. */

private final boolean doublec(int j)
{  if (j < 1) return false;
   if (b[j] != b[j-1]) return false;
   return cons(j);
}

/* cvc(i) is true <=> i-2,i-1,i has the form consonant - vowel - consonant
   and also if the second c is not w,x or y. this is used when trying to
   restore an e at the end of a short word. e.g.

      cav(e), lov(e), hop(e), crim(e), but
      snow, box, tray.

*/

private final boolean cvc(int i)
{  if (i < 2 || !cons(i) || cons(i-1) || !cons(i-2)) return false;
   {  int ch = b[i];
      if (ch == 'w' || ch == 'x' || ch == 'y') return false;
   }
   return true;
}

private final boolean ends(String s)
{  int l = s.length();
   int o = k-l+1;
   if (o < 0) return false;
   for (int i = 0; i < l; i++) if (b[o+i] != s.charAt(i)) return false;
   j = k-l;
   return true;
}

/* setto(s) sets (j+1),...k to the characters in the string s, readjusting
   k. */

private final void setto(String s)
{  int l = s.length();
   int o = j+1;
   for (int i = 0; i < l; i++) b[o+i] = s.charAt(i);
   k = j+l;
}

/* r(s) is used further down. */

private final void r(String s) { if (m() > 0) setto(s); }

/* step1() gets rid of plurals and -ed or -ing. e.g.

       caresses  ->  caress
       ponies    ->  poni
       ties      ->  ti
       caress    ->  caress
       cats      ->  cat

       feed      ->  feed
       agreed    ->  agree
       disabled  ->  disable

       matting   ->  mat
       mating    ->  mate
       meeting   ->  meet
       milling   ->  mill
       messing   ->  mess

       meetings  ->  meet

*/

private final void step1()
{  if (b[k] == 's')
   {  if (ends("sses")) k -= 2; else
      if (ends("ies")) setto("i"); else
      if (b[k-1] != 's') k--;
   }
   if (ends("eed")) { if (m() > 0) k--; } else
   if ((ends("ed") || ends("ing")) && vowelinstem())
   {  k = j;
      if (ends("at")) setto("ate"); else
      if (ends("bl")) setto("ble"); else
      if (ends("iz")) setto("ize"); else
      if (doublec(k))
      {  k--;
         {  int ch = b[k];
            if (ch == 'l' || ch == 's' || ch == 'z') k++;
         }
      }
      else if (m() == 1 && cvc(k)) setto("e");
  }
}

/* step2() turns terminal y to i when there is another vowel in the stem. */

private final void step2() { if (ends("y") && vowelinstem()) b[k] = 'i'; }

/* step3() maps double suffices to single ones. so -ization ( = -ize plus
   -ation) maps to -ize etc. note that the string before the suffix must give
   m() > 0. */

private final void step3() { if (k == 0) return; /* For Bug 1 */ switch (b[k-1])
{
    case 'a': if (ends("ational")) { r("ate"); break; }
              if (ends("tional")) { r("tion"); break; }
              break;
    case 'c': if (ends("enci")) { r("ence"); break; }
              if (ends("anci")) { r("ance"); break; }
              break;
    case 'e': if (ends("izer")) { r("ize"); break; }
              break;
    case 'l': if (ends("bli")) { r("ble"); break; }
              if (ends("alli")) { r("al"); break; }
              if (ends("entli")) { r("ent"); break; }
              if (ends("eli")) { r("e"); break; }
              if (ends("ousli")) { r("ous"); break; }
              break;
    case 'o': if (ends("ization")) { r("ize"); break; }
              if (ends("ation")) { r("ate"); break; }
              if (ends("ator")) { r("ate"); break; }
              break;
    case 's': if (ends("alism")) { r("al"); break; }
              if (ends("iveness")) { r("ive"); break; }
              if (ends("fulness")) { r("ful"); break; }
              if (ends("ousness")) { r("ous"); break; }
              break;
    case 't': if (ends("aliti")) { r("al"); break; }
              if (ends("iviti")) { r("ive"); break; }
              if (ends("biliti")) { r("ble"); break; }
              break;
    case 'g': if (ends("logi")) { r("log"); break; }
} }

/* step4() deals with -ic-, -full, -ness etc. similar strategy to step3. */

private final void step4() { switch (b[k])
{
    case 'e': if (ends("icate")) { r("ic"); break; }
              if (ends("ative")) { r(""); break; }
              if (ends("alize")) { r("al"); break; }
              break;
    case 'i': if (ends("iciti")) { r("ic"); break; }
              break;
    case 'l': if (ends("ical")) { r("ic"); break; }
              if (ends("ful")) { r(""); break; }
              break;
    case 's': if (ends("ness")) { r(""); break; }
              break;
} }

/* step5() takes off -ant, -ence etc., in context <c>vcvc<v>. */

private final void step5()
{   if (k == 0) return; /* for Bug 1 */ switch (b[k-1])
    {  case 'a': if (ends("al")) break; return;
       case 'c': if (ends("ance")) break;
                 if (ends("ence")) break; return;
       case 'e': if (ends("er")) break; return;
       case 'i': if (ends("ic")) break; return;
       case 'l': if (ends("able")) break;
                 if (ends("ible")) break; return;
       case 'n': if (ends("ant")) break;
                 if (ends("ement")) break;
                 if (ends("ment")) break;
                 /* element etc. not stripped before the m */
                 if (ends("ent")) break; return;
       case 'o': if (ends("ion") && j >= 0 && (b[j] == 's' || b[j] == 't')) break;
                                 /* j >= 0 fixes Bug 2 */
                 if (ends("ou")) break; return;
                 /* takes care of -ous */
       case 's': if (ends("ism")) break; return;
       case 't': if (ends("ate")) break;
                 if (ends("iti")) break; return;
       case 'u': if (ends("ous")) break; return;
       case 'v': if (ends("ive")) break; return;
       case 'z': if (ends("ize")) break; return;
       default: return;
    }
    if (m() > 1) k = j;
}

/* step6() removes a final -e if m() > 1. */

private final void step6()
{  j = k;
   if (b[k] == 'e')
   {  int a = m();
      if (a > 1 || a == 1 && !cvc(k-1)) k--;
   }
   if (b[k] == 'l' && doublec(k) && m() > 1) k--;
}

/** Stem the word placed into the Stemmer buffer through calls to add().
 * Returns true if the stemming process resulted in a word different
 * from the input.  You can retrieve the result with
 * getResultLength()/getResultBuffer() or toString().
 */
public void stem()
{  k = i - 1;
   if (k > 1) { step1(); step2(); step3(); step4(); step5(); step6(); }
   i_end = k+1; i = 0;
}

/** Test program for demonstrating the Stemmer.  It reads text from a
 * a list of files, stems each word, and writes the result to standard
 * output. Note that the word stemmed is expected to be in lower case:
 * forcing lower case must be done outside the Stemmer class.
 * Usage: Stemmer file-name file-name ...
 */
}

