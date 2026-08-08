#!/usr/bin/env bash
# Create AkshaySriKiran/Venu_Portfolio and push this site, then print Pages steps.
set -euo pipefail

OWNER="${GITHUB_OWNER:-AkshaySriKiran}"
REPO="Venu_Portfolio"
ROOT="$(cd "$(dirname "$0")/.." && pwd)"

command -v gh >/dev/null || { echo "Install GitHub CLI: https://cli.github.com/"; exit 1; }
gh auth status >/dev/null || { echo "Run: gh auth login"; exit 1; }

cd "$ROOT"

if [[ ! -d .git ]]; then
  git init -b main
fi

git add .
git status
git commit -m "Initial Venu Simhachalam Ryali portfolio site" || true

if gh repo view "$OWNER/$REPO" >/dev/null 2>&1; then
  echo "Repo exists — setting remote and pushing..."
  git remote remove origin 2>/dev/null || true
  git remote add origin "https://github.com/$OWNER/$REPO.git"
  git push -u origin main
else
  echo "Creating public repo $OWNER/$REPO ..."
  gh repo create "$OWNER/$REPO" \
    --public \
    --description "Portfolio — Venu Simhachalam Ryali (Water Treatment & Construction)" \
    --source=. \
    --remote=origin \
    --push
fi

echo
echo "Enable GitHub Pages:"
echo "  https://github.com/$OWNER/$REPO/settings/pages"
echo "  Source: Deploy from a branch → main → / (root) → Save"
echo
echo "Share URL:"
echo "  https://$(echo "$OWNER" | tr '[:upper:]' '[:lower:]').github.io/$REPO/"
