# CSVComparator
CSVComparator is an java API which can be used to compare two CSV files cell by cell on the basis of key given by user, it gives two files as result,details file and summary file.

Methods: 

void compare(yourResultFileName,expectedResultFileName,resultDetailsFileName,summaryFileName,0) :

yourResultFileName = .csv file which having your result data.
expectedResultFileName = .csv file, data which you are expecting. (can say, Baseline)
resultDetailsFileName = .csv file on which you want your result details.
summaryFileName = .csv file on which you want summary report.

void closeAll() : This method is compulsory to call at the end otherwise the result files will not be created.

Limitations Right Now:
1. the last argument in compare method specify the keyIndex through which comparison operation has been taken place. In the above example keyIndex is 0 i.e 'id'.
2. Both the .csv file should have same keyIndex and same number of columns and should be in same order.


