#!/usr/bin/env bash
# Create separate GitHub repos for AI/ML projects currently under projects/
# Run from the portfolio repo root on your Mac:
#   bash scripts/publish-separate-repos.sh

set -euo pipefail

ROOT="$(cd "$(dirname "$0")/.." && pwd)"
OWNER="AkshaySriKiran"

publish() {
  local folder="$1"
  local repo="$2"
  local desc="$3"
  local src="$ROOT/projects/$folder"
  local dest="/tmp/$repo-publish"

  if [[ ! -d "$src" ]]; then
    echo "Missing $src — pull latest portfolio first."
    exit 1
  fi

  echo "==> Publishing $repo"
  rm -rf "$dest"
  mkdir -p "$dest"
  # Copy contents; skip nested .git if any
  rsync -a --exclude '.git' "$src/" "$dest/"
  cd "$dest"
  git init -b main
  git add .
  git commit -m "Initial commit: $desc"
  gh repo create "$OWNER/$repo" --public --description "$desc" --source=. --remote=origin --push
  echo "Done: https://github.com/$OWNER/$repo"
}

command -v gh >/dev/null || { echo "Install GitHub CLI: https://cli.github.com/"; exit 1; }
gh auth status >/dev/null || { echo "Run: gh auth login"; exit 1; }

publish "song-popularity-recommendation" "song-popularity-recommendation" \
  "Song Popularity Prediction and User Recommendation System"
publish "video-game-popularity-prediction" "video-game-popularity-prediction" \
  "Video Game Popularity Prediction with ensemble ML models"
publish "spotycharts" "spotycharts" \
  "SpotyCharts — Spotify Charts Hadoop MapReduce + Power BI (NJIT DS644)"

echo
echo "All repos published. Then update portfolio links if needed and remove projects/."
