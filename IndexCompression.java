
//package irhomework_2;
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

import java.util.*; 
import edu.stanford.nlp.pipeline.*;
import edu.stanford.nlp.ling.*; 
import edu.stanford.nlp.ling.CoreAnnotations.*;  
import edu.stanford.nlp.util.CoreMap;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Writer;




  
public class IndexCompression
 {
    static HashMap<Integer,String> MAXTF = new HashMap<Integer,String>();
    static HashMap<Integer,Integer> Doclen = new HashMap<Integer,Integer>();
    static HashMap<Integer,String> MAXTFStem = new HashMap<Integer,String>();
    static HashMap<Integer,Integer> DoclenStem = new HashMap<Integer,Integer>();
    static HashMap<String,Long> TFMapStem = new HashMap<String,Long>();
    static List invertedIndexStem   = new ArrayList();
    static List invertedIndex   = new ArrayList();
    static Set<String> stopwords = new HashSet<String>();
    static HashMap<Long, Long> FileWordCountMap = new HashMap<Long,Long>();
    static HashMap<String,Long> TermFrequencyMap = new HashMap<String,Long>();
    public static void main(String[] args) throws FileNotFoundException, IOException
  {
  String filePath = args[0];
 String outputpath = args[1];

//Scanner filepath = new Scanner(System.in);
//Scanner filepath = new Scanner(System.in);

		//System.out.println("Enter the path where Cranfield file collection is located:");
		//String filePath = scanner.nextLine();
		
		
		//ListStopWords("C:/Users/Divya/Desktop/Semester4/Information Retrieval/Assignment/Assignment2/stopwords");
               ListStopWords("stopwords");
                long startIndexVersion1 =  System.currentTimeMillis();
                LemmaIndexGeneration(filePath);
                long endIndexVersion1 = System.currentTimeMillis();
                long Index1_time = endIndexVersion1-startIndexVersion1;
                
                
                long startIndexVersion2 =  System.currentTimeMillis();
                StemIndexGeneration(filePath);
                long endIndexVersion2 = System.currentTimeMillis();
                long Index2_time = endIndexVersion2-startIndexVersion2;
                
		
               // System.out.println("Enter the path where output file is created:");
                //String outputpath = scanner.nextLine();
                long startFileWrite =  System.currentTimeMillis();
                WriteFiles(outputpath);
                long endFilewrite = System.currentTimeMillis();
                long FileWrite_time = endFilewrite-startFileWrite;
                long FileWrite = FileWrite_time/2;
                
            
         int unCompressedSizeVersion1 = 0;
         System.out.println("IndexVersion1");
         System.out.println("************************************************************************************");
         for (int i=0;i<invertedIndex.size();i++) {
             int ListSize = 0;    
         IndexValues ind = (IndexValues)invertedIndex.get(i);
         unCompressedSizeVersion1 +=  ind.docIds.size();
         if ((ind.term.equals("reynold")) || (ind.term.equals("nasa"))|| (ind.term.equals("prandtl"))|| (ind.term.equals("flow")) ||(ind.term.equals("pressure"))
	    || (ind.term.equals("boundary")) ||(ind.term.equals("shock")) ){
	    			 
	    	
	      int total = 0;
	      for(int j = 0; j < ind.termFreq.size(); j++)
	                 total += ind.termFreq.get(j);
                  ListSize += 2 * ind.termFreq.size();
	         
	          
	    	  System.out.print("Term:"+ind.term+"       ");
	    	  System.out.print("Document Frequency df:"+ind.docFreq+"      ");
	    	  System.out.print("Term Frequency tf:"+ total+"      ");
	    	  System.out.println("Inverted list Size:"+ListSize*Long.SIZE+"       ");
	    	 }
	    	}
         System.out.println("************************************************************************************");
         
         System.out.println("IndexVersion2"); 
         int unCompressedSizeVersion2 = 0;
        System.out.println("************************************************************************************");
         for (int i=0;i<invertedIndexStem.size();i++) {	 
             int ListSize2 = 0;
         IndexValues ind = (IndexValues)invertedIndexStem.get(i);
         unCompressedSizeVersion2 +=  ind.docIds.size();
         if ((ind.term.equals("reynold")) || (ind.term.equals("nasa"))|| (ind.term.equals("prandtl"))|| (ind.term.equals("flow")) ||(ind.term.equals("pressur"))
	    || (ind.term.equals("boundari")) ||(ind.term.equals("shock")) 
	    			  )
	    	 {
	      int total = 0;
              for(int j = 0; j < ind.termFreq.size(); j++){
	                 total += ind.termFreq.get(j);}
               ListSize2 += 2 * ind.termFreq.size();
	      
	          
	    	  System.out.print("Term:"+ind.term+ "    ");
	    	  System.out.print("Document Frequency df:"+ind.docFreq+ "      ");
	    	  System.out.print("Term Frequency tf:"+ total+ "      ");
	    	  System.out.println("Inverted list Size:"+ListSize2*Long.SIZE+" ");
	    	 }
	    	}         
           System.out.println("************************************************************************************");
                
                
                
                
        char measure = '0';
        int Charac = 0,Bits = 0;
        FileReader reader = new FileReader(outputpath+"/IndexVersion1_Compress");
        while (measure != (char)-1)  
        {
         Charac++;
         measure = (char)reader.read();
         if(measure=='0' || measure=='1' ){
              Bits++;
          }
        }
        
       
        char measure2 = '0';
        int Charac2 = 0,Bits2 = 0;
        FileReader reader2 = new FileReader(outputpath+"/IndexVersion2_Compress");
        while (measure2 != (char)-1)  
        {
         Charac2++;
         measure2 = (char)reader2.read();
         if(measure2=='0' || measure2=='1' ){
              Bits2++;
          }
        }
        reader.close();
                System.out.println("************************************************************************************");
                System.out.println("The elapsed time  required to build version1 of the index  "+ Index1_time+FileWrite);
                System.out.println("The elapsed time  required to build  version2 of the index  "+ Index2_time+FileWrite);
                System.out.println("The number of inverted lists in version1 of the index  "+invertedIndex.size());
                System.out.println("The number of inverted lists in version2 of the index  "+invertedIndexStem.size());
                System.out.println("The  size of the index Version 1 uncompressed " +unCompressedSizeVersion1*Long.SIZE + " bytes");
                System.out.println("The  size of the index Version 1 compressed " +((Charac-Bits)+(Bits/Long.SIZE))+" bytes");
                System.out.println("The  size of the index Version 2 uncompressed " +unCompressedSizeVersion2*Long.SIZE + " bytes");
                System.out.println("The  size of the index Version 2 compressed " +((Charac2-Bits2)+(Bits2/Long.SIZE))+" bytes"); 
                System.out.println("************************************************************************************");
  }


   
  
  private static void LemmaIndexGeneration(String filepath) throws FileNotFoundException, IOException{
    long initial_count = 1;
     int filecount = 0;
     int stopword_count = 0;
     File folder = new File(filepath);
     File[] listOfFiles = folder.listFiles(); 
     
   Properties props = new Properties(); 
    props.put("annotators", "tokenize, ssplit, pos, lemma"); 
    StanfordCoreNLP pipeline = new StanfordCoreNLP(props, false);
    for (File file : listOfFiles) {
        TermFrequencyMap.clear();
        int count_word_total = 0;
        filecount++;
        int count_words = 0;
    if (file.isFile()) {
        
        //reading from each file inside cranfield
        BufferedReader reader = new BufferedReader(new FileReader(filepath+"/"+file.getName()));
        String line = reader.readLine();
        while (line!= null){
            int lemmaPresent = 0, docIdPresent = 0,listIndex = 0,docIndex = 0;
        if(!line.startsWith("<")){
            
            
            String text = line;
  Annotation document = pipeline.process(text);   
  for(CoreMap sentence: document.get(SentencesAnnotation.class)) {    
  for(CoreLabel token: sentence.get(TokensAnnotation.class)) {       
  String word = token.get(TextAnnotation.class); 
    String lemmas = token.get(LemmaAnnotation.class); 
    String lemma = lemmas.replaceAll("[-/()==?'+.^:,$]","").toLowerCase();
      
    
     if (stopwords.contains(lemma) == true || ("?".equals(lemma))||("/".equals(lemma))||("=".equals(lemma))||("*".equals(lemma)) || ("".equals(lemma))||("$".equals(lemma))|| ("'".equals(lemma))|| (",".equals(lemma)) ){
         if(stopwords.contains(lemma)){
           count_words ++;  
         }
         
        stopword_count++; 
     }else{
         
         if(!stopwords.contains(lemma)){
                count_words ++;
                if(TermFrequencyMap.containsKey(lemma))
			 {
		long tf = TermFrequencyMap.get(lemma);
		TermFrequencyMap.put(lemma, tf+1);
				 }
		else
		TermFrequencyMap.put(lemma, initial_count);
                     }
     }
    
      for(int invIndex=0;invIndex<invertedIndex.size();invIndex++){
       
	IndexValues lemmaValues = (IndexValues)invertedIndex.get(invIndex);
	if(lemmaValues.term.equals(lemma)){
	lemmaPresent = 1;
	listIndex = invIndex;
	 break;
	 }
	 
     }
      
      if(stopwords.contains(lemma) == false){
          
      if (lemmaPresent == 1)
	   {
         IndexValues val = (IndexValues)invertedIndex.get(listIndex);
          for(int docIdIndex=0;docIdIndex<val.docIds.size();docIdIndex++){
	   if(val.docIds.get(docIdIndex) == filecount)
	 {
	 docIdPresent=1;
	docIndex = docIdIndex;
	 break;
	 }
               }
	 if(docIdPresent == 0)
	 {
	 val.docIds.add((long)filecount);
         val.termFreq.add(initial_count);
                         }
	else
	 {
										   
        Long temp = Long.parseLong(val.termFreq.get(docIndex).toString());
        temp++;
        val.termFreq.remove(docIndex);
        val.termFreq.add(docIndex,temp);
        docIdPresent = 0;
	  }
        invertedIndex.remove(listIndex);
	invertedIndex.add(listIndex,val);
										   
	lemmaPresent = 0;            
          }
       else
	  {
        IndexValues newEntry = new IndexValues();
         newEntry.term = lemma;
        newEntry.docIds.add((long)filecount);
        newEntry.termFreq.add(initial_count);
        invertedIndex.add(newEntry);
			                            
} 
  }
      
   } 
                }
         }
         line = reader.readLine();
                                          }//while linenot null
                           
      
      count_word_total = count_words + count_word_total;
      
      ListIterator invIndexIterator = invertedIndex.listIterator();
                     
					for(int invIndexIt=0;invIndexIt<invertedIndex.size();invIndexIt++)
					{
						 IndexValues iv = (IndexValues)invIndexIterator.next();                                                            
                         int docfreq = iv.docIds.size();
                         iv.docFreq = docfreq;
					}
      
        
    }
   
    Doclen.put(filecount, count_word_total);
    
    
    Map.Entry<String,Long> maxEntry = null;
    long freq = 0;
    String maxval = null;
  for(Map.Entry<String,Long> entry : TermFrequencyMap.entrySet()) {
    if (maxEntry == null || entry.getValue() > maxEntry.getValue()) {
        maxEntry = entry; 
        maxval = entry.getKey();
        freq = entry.getValue();
    }
  }
    MAXTF.put(filecount, maxval +" "+freq );
    
                          }//for file
    
    
   
   
 

  }//main method
  
  public static void StemIndexGeneration(String filepath) throws FileNotFoundException, IOException{
     
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
 TFMapStem.put(u, 1L);


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
         
         
        
    line = scanner.readLine();
                                          }
        
      
 ListIterator invIndexIterator = invertedIndexStem.listIterator();
                     
for(int invIndexIt=0;invIndexIt<invertedIndexStem.size();invIndexIt++){
 IndexValues iv = (IndexValues)invIndexIterator.next();                                                            
 int docfreq = iv.docIds.size();
 iv.docFreq = docfreq;
					}







    }
    DoclenStem.put(filecount, count_word_total);
    
    String maxval= null;
    Map.Entry<String,Long> maxEntry = null;
    long freq = 0;
  for(Map.Entry<String,Long> entry : TFMapStem.entrySet()) {
    if (maxEntry == null || entry.getValue() > maxEntry.getValue()) {
        maxEntry = entry; 
         maxval = entry.getKey();
        freq = entry.getValue();
    }
  }
    MAXTFStem.put(filecount, maxval +" "+freq );
                          }
   
   
}

   
  
  
  
  
  
  private static void  printMap(HashMap tm){
       Iterator<String> iterTree = tm.keySet().iterator(); 
   while(iterTree.hasNext()){
  String TreeNamekey = iterTree.next();
  System.out.println("key: " + TreeNamekey + " value: " + tm.get(TreeNamekey));
  }
  }
  
  
  //reading stopwords from file and storing in hash set
  private static void ListStopWords(String filePath) throws FileNotFoundException, IOException {	
          FileReader stopWords = new FileReader(filePath);
          BufferedReader bufferRead = new BufferedReader(stopWords);
          String nextLine;
          while((nextLine = bufferRead.readLine())!= null)
          stopwords.add(nextLine);
	 	
	}
  
  public static void WriteFiles(String filepath) throws IOException{
      
      FileOutputStream file_ver_1_uncompress_binary = new FileOutputStream(filepath+"/"+"IndexVersion1_Uncompress");
      ObjectOutputStream os_ver_1_uncompress_binary = new ObjectOutputStream(file_ver_1_uncompress_binary);
      FileOutputStream file_ver_1_compress_binary = new FileOutputStream(filepath+"/"+"IndexVersion1_Compress");
      ObjectOutputStream os_ver_1_compress_binary = new ObjectOutputStream(file_ver_1_compress_binary);
      
      
      FileOutputStream file_ver_2_uncompress_binary = new FileOutputStream(filepath+"/"+"IndexVersion2_Uncompress");
      ObjectOutputStream os_ver_2_uncompress_binary = new ObjectOutputStream(file_ver_2_uncompress_binary);
      FileOutputStream file_ver_2_compress_binary = new FileOutputStream(filepath+"/"+"IndexVersion2_Compress");
      ObjectOutputStream os_ver_2_compress_binary = new ObjectOutputStream(file_ver_2_compress_binary);
      
      /*File file_ver_1_uncompress = new File(filepath+"/"+"IndexVersion1_Uncompress.txt");//
      file_ver_1_uncompress.createNewFile();
      File file_ver_1_compress = new File(filepath+"/"+"IndexVersion1_Compress.txt");//
      file_ver_1_compress.createNewFile();
      Writer write1 = new BufferedWriter(new FileWriter(file_ver_1_uncompress));//
      Writer write2 = new BufferedWriter(new FileWriter(file_ver_1_compress));//
      
      File file_ver_2_uncompress = new File(filepath+"/"+"IndexVersion2_Uncompress.txt");//
      file_ver_2_uncompress.createNewFile();//
      File file_ver_2_compress = new File(filepath+"/"+"IndexVersion2_Compress.txt");//
      file_ver_2_compress.createNewFile();//
      Writer write3 = new BufferedWriter(new FileWriter(file_ver_2_uncompress));//
      Writer write4 = new BufferedWriter(new FileWriter(file_ver_2_compress));//*/
      
      
      
      
      for(int i=0;i<invertedIndex.size();i++)
        {
           
            IndexValues result = (IndexValues)invertedIndex.get(i);
            //ds.writeUTF(result.term);
            os_ver_1_uncompress_binary.writeUTF(result.term);
            os_ver_1_uncompress_binary.write((int)result.docFreq);
            os_ver_1_compress_binary.writeUTF(result.term);
            os_ver_1_compress_binary.writeUTF(GammaEncoding(result.docFreq));
            
             
            // write1.write(result.term+" "+(int)result.docFreq+" {");//
             //write2.write(result.term+" "+(int)result.docFreq+"{");//
            ListIterator<Long> documentIds = result.docIds.listIterator();
            while(documentIds.hasNext())
            {
                Integer docIdList = Integer.parseInt(documentIds.next().toString());
                int di = documentIds.previousIndex();
                os_ver_1_uncompress_binary.write(docIdList);
                os_ver_1_compress_binary.writeUTF(DeltaEncoding(docIdList));
                
                // write1.write(" "+docIdList+": ");//
                // write2.write(""+DeltaEncoding(docIdList)+":");//
                //System.out.println(docIdList);
                ListIterator<Long> frequency = result.termFreq.listIterator();
                
                while(frequency.hasNext())
                {
                    Integer inte = Integer.parseInt(frequency.next().toString());
                    int fi = frequency.previousIndex();
                    if(di == fi)
                    {
                        
                        
                        os_ver_1_uncompress_binary.write(inte);
                        os_ver_1_compress_binary.writeUTF(GammaEncoding(inte));
                       // write1.write(inte+" ");//
                        //write2.write(GammaEncoding(inte)+"");//
                        
                        break;
                    }
                                       
                }
            }
           //  write1.write("} [ ");//
            // write2.write("}[");//
        }
       // MaxTerm and doclength for version1
        for(int i =0 ;i<1400;i++){
            os_ver_1_uncompress_binary.writeUTF("[DocNo : "+(i+1)+ " MaxTF :"+MAXTF.get(i+1)+"doclen : "+Doclen.get(i+1)+"]");
             os_ver_1_compress_binary.writeUTF("DocNo : "+DeltaEncoding(i+1)+ " MaxTF : "+MAXTF.get(i+1)+"doclen : "+Doclen.get(i+1)+"]");
             
        //  write1.write("[DocNo : "+(i+1)+ " MaxTF :"+MAXTF.get(i+1)+"doclen : "+Doclen.get(i+1)+"]");
          // write2.write("DocNo : "+DeltaEncoding(i+1)+ " MaxTF : "+MAXTF.get(i+1)+"doclen : "+Doclen.get(i+1));
        }
       
        
      
      
      
      
      
      //writing version2
      for(int i=0;i<invertedIndexStem.size();i++)
        {
           
            IndexValues result = (IndexValues)invertedIndexStem.get(i);
           os_ver_2_uncompress_binary.writeUTF(result.term);
            os_ver_2_uncompress_binary.write((int)result.docFreq);
            os_ver_2_compress_binary.writeUTF(result.term);
            os_ver_2_compress_binary.writeUTF(GammaEncoding(result.docFreq));
            
            // write3.write(result.term+" "+(int)result.docFreq+" {");
             //write4.write(result.term+" "+(int)result.docFreq+"{");
           // System.out.println("term " + result.term +result.docFreq);
            ListIterator<Long> documentIds = result.docIds.listIterator();
            while(documentIds.hasNext())
            {
                Integer docIdList = Integer.parseInt(documentIds.next().toString());
                int di = documentIds.previousIndex();
                
                os_ver_2_uncompress_binary.write(docIdList);
                os_ver_2_compress_binary.writeUTF(DeltaEncoding(docIdList));
                
                // write3.write(" "+docIdList+": ");
                 //write4.write(""+DeltaEncoding(docIdList)+":");
               // System.out.println(docIdList);
                ListIterator<Long> frequency = result.termFreq.listIterator();
                
                while(frequency.hasNext())
                {
                    Integer inte = Integer.parseInt(frequency.next().toString());
                    int fi = frequency.previousIndex();
                    if(di == fi)
                    {
                        os_ver_2_uncompress_binary.write(inte);
                        os_ver_2_compress_binary.writeUTF(GammaEncoding(inte));
                       // write3.write(inte+" ");
                        //write4.write(GammaEncoding(inte)+"");
                      
                       
                        break;
                    }
                                       
                }
            }
            // write3.write("} [ ");
             //write4.write("}[");
        }
      
      // MaxTerm and doclength for version2
        for(int i =0 ;i<1400;i++){
            os_ver_2_uncompress_binary.writeUTF("[DocNo : "+(i+1)+ " MaxTF :"+MAXTF.get(i+1)+"doclen : "+Doclen.get(i+1)+"]");
             os_ver_2_compress_binary.writeUTF("DocNo : "+DeltaEncoding(i+1)+ " MaxTF : "+MAXTF.get(i+1)+"doclen : "+Doclen.get(i+1)+"]");
            
        //  write3.write("[DocNo : "+(i+1)+ " MaxTF :"+MAXTFStem.get(i+1)+"doclen : "+DoclenStem.get(i+1)+"]");
          // write4.write("DocNo : "+DeltaEncoding(i+1)+ " MaxTF : "+MAXTFStem.get(i+1)+"doclen : "+DoclenStem.get(i+1));
        }
       
      
      
      

  }
  
  //get gamma code
   public static String GammaEncoding(int number){
            String binarynumber = Integer.toBinaryString(number);
            String offset = binarynumber.substring(1);
            int offset_length = offset.length();
            String unary = "";
            for (int i = 0; i < offset_length; i++){
                unary = unary + "1";
            }
            unary = unary + "0";
            String gamma = unary + offset;
        return gamma;
    }
  //get delta code
  public static String DeltaEncoding(int number)
	{
		String binarynumber = Integer.toBinaryString(number);
		String offset = binarynumber;
		int offset_length = offset.length();
                String GammaCode = GammaEncoding(offset_length);
                String actual_offset = binarynumber.substring(1);
                String delta = GammaCode+actual_offset;
                return delta;
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
