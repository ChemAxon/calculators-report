#!/bin/bash

../scripts/html-reporter.sh \
    --ref-prop "Standard Value" \
    --act-prop "Strongest basic pKa" \
    --act-prop-version "ver_1" \
    --html-template ../calculators-reports-ui/dist/index.html \
    --report-calc-name "basic pKa" \
    --report-cli "cxcalc pka -i 0 -a 0 -b 1 CHEMBL26_3301362_AZ_bpka.sdf" \
    --report-molset-ref "https://www.ebi.ac.uk/chembl/assay_report_card/CHEMBL3301362/" \
    --output sdf_prop_example_report.html \
    ../data/CHEMBL26_3301362_AZ_bpka.sdf
