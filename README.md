## JavaHeapFile
Create Heap File by Java, Search text in Heap File 
## complie
javac dbload.java
javac dbquery.java

## run dbload
command:
java dbload -p pagesize datafile
examlpe:
java dbload -p 4096 test.csv

## run dbquery
command:
java dbquery text pagesize
examlpe:
java dbload PRICE 4096