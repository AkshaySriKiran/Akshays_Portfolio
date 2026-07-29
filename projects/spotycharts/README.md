# SpotyCharts — Analyzing Spotify Charts

NJIT **DS644: Introduction to Big Data** team project by Akshay Sri Kiran Ryali and collaborators.

Processed the Kaggle [Spotify Charts](https://www.kaggle.com/datasets/dhruvildave/spotify-charts) dataset (~1.5–2 GB) with **Hadoop MapReduce**, then visualized results in **Power BI**.

## MapReduce jobs
1. **TotalStreamsByArtist** — aggregate streams per artist (with combiner)
2. **TopRegionsByStreams** — top 5 regions by total streams
3. **AverageStreamsByTrend** — mean streams by trend (`MOVE_UP`, `MOVE_DOWN`, `NEW_ENTRY`, `SAME_POSITION`)

## Cluster scaling notes
Jobs were timed on 3 / 4 / 6 VMs; e.g. Total Streams by Artist on 1.5 GB dropped from ~320s (3 VMs) to ~210s (6 VMs).

## Folder layout
| Path | Contents |
|------|----------|
| `Java_SourceCode/` | MapReduce Java sources + JARs |
| `output/` | CSV aggregations |
| `Commands.txt` | HDFS upload + `hadoop jar` run steps |
| `DS644 Data Visualisation.pbix` | Power BI dashboard |
| `SpotyCharts_BigData_Project_Report.pdf` | Full project report |
| `ScreenShots/` | Result screenshots |

## Run (Hadoop)
See `Commands.txt` for the full HDFS + compile + `hadoop jar` sequence.
