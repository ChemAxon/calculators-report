#!/bin/bash

../scripts/html-reporter.sh \
    --ref-prop "Standard Value" \
    --act-chemterm "basicpka('1')" \
    --html-template ../calculators-reports-ui/dist/index.html \
    --report-calc-name "basic pKa" \
    --report-cli "evaluate -e \"basicpka('1')\" CHEMBL26_3301362_AZ_bpka.sdf" \
    --report-molset-ref "https://www.ebi.ac.uk/chembl/assay_report_card/CHEMBL3301362/" \
    --output chemterm_example_report.html \
    ../data/CHEMBL26_3301362_AZ_bpka.sdf
