Search-Engine has implemented in Java using indexing, porter stemmer algorithm, lemmatization. Used Cranfield collection for data. Created the compressed version as well as uncompressed version of indexes. Used delta encoding for the document-id and gamma code for the frequency information.

DESIGN DECISIONS:
1. The program access the cranfield collection. The path to the cranfield collection is given
as command line argument while running the program.

2. The stopwords and hw3.queries files are loaded into program. Both files should kept in
the source folder.

3. The program reads each query from hw3.queries file, indexes query and process it by
stemming and then query is searched in each document in the cranfield collection.

4. Porter stemmer algorithm is used for stemming the queries as well as building index for
cranfield collection.

5. HashMaps are used for storing queries.
6. 
6. From the index created for cranfield the parameters for each documents such as term
frequency, mac term frequency, doclength are collected. These parameters are stored in
hash map.

7. Then these information is used to calculate weights for each document. All these works
will be performed in weightFunctions() in the program.

8. The weights are calculated using the formula.
W1 = (0.4 + 0.6 * log (tf + 0.5) / log (maxtf + 1.0)) * (log (collectionsize / df)/
log(collectionsize))
W2 = (0.4 + 0.6 * (tf / (tf + 0.5 + 1.5 * (doclen / avgdoclen))) * log (collectionsize / df)/
log (collectionsize))
collection size is calculated by using a counter while indexing.
Avgdoclen is calculated using the formula (totalDoclength/collectionSize);

9. After calculating the weights, it will be stored in a treemap with scores as index. So
scores will get sorted automatically in a tree hash map.

10. The top ten documents for each queries are displayed in required order with rank,
external identifier, score and headline. Headline is extracted from the document using
the jsoup jar file.

11. Relevancy for each result is analyzed manually and the result is given as follows.
Effects Noticed in two Weighing Schemes
The results obtained from two weighing functions has difference. The difference is because
both functions give priority to the computed scores rather than the semantic meaning of the
document and query.
The W1 function gives more importance the maximum number of term frequency. So it gives
more score to documents with higher term frequency. If a term has large number of occurrences
and representative of the content of that document then it will be given more score. This is a drawback
in W1 function.
The W2 function is a variation of Okapi term weighting. If a term appears ten times or twenty
times it gives the same score. It does not give higher score if a particular index term is repeating
in a document. Also it gives higher score to a searching term present in a short document than
a searching term present in a long document. It does not give much importance to the semantic
meaning of the query.
