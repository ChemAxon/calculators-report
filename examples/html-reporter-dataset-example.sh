#!/bin/sh

../scripts/dataset-writer.sh \
    --propRef "Standard Value" \
    --calcType "basic pKa" \
    --chemTerm "basicpka('1')" \
    --version "ver_1" \
    --output chemterm_example_dataset.json \
    ../data/CHEMBL26_3301362_AZ_bpka.sdf

../scripts/html-reporter.sh \
    --html-template ../calculators-reports-ui/dist/index.html \
    --report-cli "cxcalc pka -i 0 -a 0 -b 1 CHEMBL26_3301362_AZ_bpka.sdf" \
    --output dataset_example_report.html \
    chemterm_example_dataset.json

