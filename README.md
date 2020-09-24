calculators-reports
===================

This is an open source distribution of our tools used to generate 
[Calculator performance reports](https://disco.chemaxon.com/calculators/). 

**IMPORTANT**: this distribution is **under construction** and provided **AS-IS**.
If you have any question, suggestion please feel free to contact us at 
[`calculators-support@chemaxon.com`](mailto:calculators-support@chemaxon.com)



Getting started 1 - compilation
-----------------------------

Make sure that valid ChemAxon licenses (evaluation or production) for the calculators you intend
to use are available and [installed](https://docs.chemaxon.com/Installing+Licenses).

  * When you have a [JChem](https://chemaxon.com/products/jchem-engines) distribution
    [downloaded](https://chemaxon.com/products/jchem-engines/download), point
    the build to the contained `lib/jchem.jar` (using **relative** path, eg `../`):

    ``` bash
    ./gradlew -PcxnJchemJar=../jchem/lib/jchem.jar createScripts
    ```

    Note that some dependencies of this example (like [junit](https://mvnrepository.com/artifact/junit/junit)) is
    retrieved from the public
    [Maven Central repository](https://docs.gradle.org/current/userguide/declaring_repositories.html#sub:maven_central).


  * ChemAxon [public repository (hub)](https://docs.chemaxon.com/display/docs/Public+Repository) can
    also be used to retrieve dependencies. Make sure your [ChemAxon pass](https://pass.chemaxon.com/login) email address
    is available and you acquire Public Repository API key from <https://accounts.chemaxon.com/my/settings>.

    ``` bash
    ./gradlew -PcxnHubUser=<YOUR_PASS_EMAIL> -PcxnHubPass=<YOUR_HUB_API_KEY> createScripts
    ```

Getting started 2 - generate report
-----------------------------------

  * Generate report from SDF file (reference and calculated/actual values will be read from the specified SDF tags):

    ``` bash
    ./scripts/html-reporter.sh \
        --ref-prop "Standard Value" \
        --act-prop "Strongest basic pKa" \
        --act-prop-version "ver_1" \
        --html-template calculators-reports-ui/dist/index.html \
        --report-calc-name "basic pKa" \
        --report-cli "cxcalc pka -i 0 -a 0 -b 1 CHEMBL26_3301362_AZ_bpka.sdf" \
        --report-molset-ref "https://www.ebi.ac.uk/chembl/assay_report_card/CHEMBL3301362/" \
        --output sdf_example_report.html \
        data/CHEMBL26_3301362_AZ_bpka.sdf
    ```

  * Or calculated values can be generated via [Chemical Term](https://docs.chemaxon.com/Chemical_Terms.html) functions:

    ``` bash
    ./scripts/html-reporter.sh \
        --ref-prop "Standard Value" \
        --act-chemterm "basicpka('1')" \
        --html-template calculators-reports-ui/dist/index.html \
        --report-calc-name "basic pKa" \
        --report-cli "evaluate -e \"basicpka('1')\" CHEMBL26_3301362_AZ_bpka.sdf" \
        --report-molset-ref "https://www.ebi.ac.uk/chembl/assay_report_card/CHEMBL3301362/" \
        --output chemterm_example_report.html \
        data/CHEMBL26_3301362_AZ_bpka.sdf
    ```

  * The `dataset-writer.sh` generates the raw calculation results into a JSON file. It can be used to create a report by the html reporter script:

    ``` bash
    ./scripts/dataset-writer.sh \
        --propRef "Standard Value" \
        --calcType "basic pKa" \
        --chemTerm "basicpka('1')" \
        --version "ver_1" \
        --output chemterm_example_dataset.json \
        data/CHEMBL26_3301362_AZ_bpka.sdf

    ./scripts/html-reporter.sh \
        --html-template calculators-reports-ui/dist/index.html \
        --report-cli "cxcalc pka -i 0 -a 0 -b 1 CHEMBL26_3301362_AZ_bpka.sdf" \
        --output dataset_example_report.html \
        chemterm_example_dataset.json
    ```

**Notes**:
 - The example scripts above are available in `examples` directory.
 - The `html-reporter.sh -h` prints the usage and the list of all available command line options.
 - More JSON datasets can be added as historical data with the `html-reporter.sh --report-historical-data` option. The generated report will contain a Delta distribution vs Version histogram ([Example](https://disco.chemaxon.com/calculators/reports/CHEMBL26-AZ_bpka_Eq_T_Report.html)).
 - A new template file [calculators-reports-ui/dist/index.html] can be built with the `./gradlew buildClient` command (if the UI sources changed).

Licensing
---------

The content of this project (this git repository) is distributed under the Apache License 2.0. Some dependencies of this
project are **ChemAxon proprietary products** which are **not** covered by this license.
Please note that unauthorized redistribution of ChemAxon proprietary products is not allowed.