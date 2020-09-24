# calculators-reports-core

## Usage examples

### HTMLReporterCLI

This CLI generates report from a source file that is a 
 - .json dataset, or
 - .csv or .sdf that holds ref value(-p), and act value calculated by a provided chemterm (-c)
 - .csv or .sdf that holds ref value(-p), and act value also (-pa). Version also should be setted in this case.(-v)
  
Examples:

HTMLReporterCLI LogP-1.0-martel_set.json
 
HTMLReporterCLI -p "LogP" -c "logP()" 3mols.sdf
 
HTMLReporterCLI -p "LogPRef" -pa "logPAct" -v 1.0 TEST1.csv
 
**Help:**

HTMLReporterCLI -h

**Read mol and ref value from .csv, calculate logP, and generate report:**

HTMLReporterCLI	-p "LogP" --chemTerm "logP()" TEST1.csv

**Use .sdf input instead:**

HTMLReporterCLI -p "LOGP" --chemTerm "logP()" 3mols.sdf

**Write output to file instead of console:**

HTMLReporterCLI	-p "LogP" --chemTerm "logP()" -o output.json TEST1.csv

**Use html template:**

HTMLReporterCLI -p "LogP" --chemTerm "logP()" -t test_template.html TEST2.csv

**Include history files:**

HTMLReporterCLI -p "LOGP" --chemTerm "logP()" 3mols.sdf -hf "LogP-martel_set-5.4.json;LogP-martel_set-argon.json"


**Other options**

**Set distribution range:**

HTMLReporterCLI -p "LogP" --chemTerm "logP()" -d "0.4 1.1" TMP/TEST1.csv




### DatasetWriterCLI
This creates a temporary dataset from the inputs for for future reportings. 

Reads mols and ref values from file and 
- calculates act value by chemterm 
- or gets act value from input file also.
- writes out .json dataset file.

**Read mol and ref value from .csv, calculate logP, and creates dataset file: LogP-19.25-ESM708.json **

DatasetWriterCLI -p "LogP" --chemTerm "logP()" -oa TMP/ESM708.csv

**Both values from .sdf: Read mol and ref value from .sdf, and creates dataset file: LogP-1.0-3mols.json **

DatasetWriterCLI -p "LogP" -pa "LogP2" -v 1.0 -oa TMP/3mols.sdf

**Other examples**
DatasetWriterCLI -p "Experimental_LogP" -pa "Actual_LogP" -v "5.4" -oa TMP/martel_set_logp_5.4.sdf
DatasetWriterCLI -p "Experimental_LogP" -pa "Actual_LogP" -v "19.26" -oa TMP/martel_set_logp_19.26.sdf


### MultisetHTMLReporterCLI

It reads multiple dataset json files 

(eg: datasets of multiple versions of a calculator), then create a multi report from them.


**Examples**

MultisetHTMLReporterCLI LogP-19.26-martel_set_logp_19.26.json LogP-5.4-martel_set_logp_5.4.json


### Example to create report with historical data
```
Download different versions of jchem, and go to their bin directory, then:

1) Run cxcalcs on sdf to put calculated value as a new tag 
./cxcalcx64.exe -S -t LogP_ACT "martel_set_logp.sdf" -o martel_set_logp_argon.sdf logp
./cxcalcx64.exe -S -t LogP_ACT "martel_set_logp.sdf" -o martel_set_logp_barium.sdf logp
...

2) Create datasets from sdfs

../scripts/DatasetWriterCLI -p LogP -pa LogP_ACT -mn martel_set martel_set_logp_argon.sdf -v argon -oa
../scripts/DatasetWriterCLI -p LogP -pa LogP_ACT -mn martel_set martel_set_logp_barium.sdf -v barium -oa
...


3) Use -hf parameter in reporter with generated dataset files.

../scripts/HTMLReporterCLI -p "LogP" --chemTerm "logP()" -o output2.html --template index.html martel_set_logp.sdf 
-hf "LogP-barium-martel_set.json;LogP-carbon-martel_set.json" 
```
