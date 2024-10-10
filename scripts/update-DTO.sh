DEFAULT_BRANCH_NAME="main"

if [ "$#" -eq 0 ]; then
  BRANCH_NAME="$DEFAULT_BRANCH_NAME"
elif [ "$#" -eq 1 ]; then
  BRANCH_NAME="$1"
else
  echo "Usage: $0 [branch_name]"
  exit 1
fi

REPO_ROOT=$(git rev-parse --show-toplevel)
LOCAL_SUSGAME_PATH="$REPO_ROOT/app/src/main/java/edu/agh/susgame"
LOCAL_DTO_PATH="$LOCAL_SUSGAME_PATH/dto"

TMP_PATH="$REPO_ROOT/scripts/tmp"
REPO_DTO_PATH="$TMP_PATH/SusGameDTO/src/main/kotlin/edu/agh/susgame/dto"

echo "Cleaning up current DTO files"
rm -r "$LOCAL_DTO_PATH"

echo "Cloning $BRANCH_NAME branch from DTO repository"
mkdir "$TMP_PATH"
cd "$TMP_PATH" || exit
git clone --branch "$BRANCH_NAME" https://github.com/Nepommuck/SusGameDTO.git

echo "Copying cloned files"
mkdir "$LOCAL_DTO_PATH"
cp -r "$REPO_DTO_PATH" "$LOCAL_SUSGAME_PATH"

echo "Adding copied files to git"
git add "$LOCAL_DTO_PATH"

echo "Adding a warning message to files"
find "$LOCAL_DTO_PATH" -type f | while read -r file; do
  sed -i "1i // WARNING: THIS FILE WAS CLONED AUTOMATICALLY FROM 'SusGameDTO' GITHUB REPOSITORY" "$file"
  sed -i "2i // IT SHOULD NOT BE EDITED IN ANY WAY" "$file"
  sed -i "3i // IN ORDER TO CHANGE THIS DTO, COMMIT TO 'SusGameDTO' GITHUB REPOSITORY" "$file"
  sed -i "4i // IN ORDER TO UPDATE THIS FILE TO NEWEST VERSION, RUN 'scripts/update-DTO.sh'\n" "$file"
done

echo "Cleaning up"
cd "$REPO_ROOT" || exit
rm -rf "$TMP_PATH"

echo "DTO script finished execution"