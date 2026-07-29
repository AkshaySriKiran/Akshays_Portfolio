# SpotyCharts — Spotify Charts Big Data Project

NJIT DS644 project analyzing Spotify chart streams with Java MapReduce and Power BI.

## Expected contents
Copy your local project files into this folder, then commit:

```bash
cp -R "/Users/akshayryali/BIG_DATA_PROJECT/"* \
  /Users/akshayryali/portfolio/projects/spotycharts/
cd /Users/akshayryali/portfolio
git add projects/spotycharts
git commit -m "Add SpotyCharts big data project source"
git push origin cursor/portfolio-website-5609
```

## Typical structure
- `Java_SourceCode/` — MapReduce jobs (`TotalStreamsByArtist`, `TopRegionsByStreams`, `AverageStreamsByTrend`)
- `output/` — aggregated CSV results
- `DS644 Data Visualisation.pbix` — Power BI dashboard
- `SpotyCharts_BigData_Project_Report.pdf` — project report
- `Commands.txt` — run commands
