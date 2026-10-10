#!/usr/bin/env bash
set -Eeuo pipefail

usage() {
  echo "Usage: $0 <package name> <project name> <mod id>"
}

if [[ $# -ne 3 ]]; then
  usage
  exit 1
fi

base="$(cd -- "$(dirname -- "$(readlink -f -- "$0")")" && pwd)"
package_name="$1"
project_name="$2"
modid="$3"
package_dir="${package_name//./\/}"
script_path="$(readlink -f -- "$0")"

echo "starting @ $base"

escape_sed_replacement() {
  printf '%s' "$1" | sed 's/[&|\\]/\\&/g'
}

pkg="$(escape_sed_replacement "$package_name")"
proj="$(escape_sed_replacement "$project_name")"
mod="$(escape_sed_replacement "$modid")"

find "$base/src/main" -type f -exec sed -i \
  -e "s|com\.example\.mod|$pkg|g" \
  -e "s|com\.example|$pkg|g" \
  -e "s|modbase|$mod|g" {} +

for config in gradle.properties build.gradle.kts; do
  if [[ -f "$base/$config" ]]; then
    sed -i \
      -e "s|com\.example\.mod|$pkg|g" \
      -e "s|com\.example|$pkg|g" \
      -e "s|modbase|$mod|g" \
      "$base/$config"
  fi
done

if [[ -f "$base/settings.gradle.kts" ]]; then
  sed -i "s|modbase|$proj|g" "$base/settings.gradle.kts"
fi

for source in java kotlin; do
  root="$base/src/main/$source"
  old="$root/com/example/mod"
  new="$root/$package_dir"

  [[ -d "$root" ]] || continue

  if [[ -d "$old" ]]; then
    if [[ -e "$new" ]]; then
      echo "Destination already exists: $new" >&2
      exit 1
    fi

    mkdir -p "$(dirname "$new")"
    mv -- "$old" "$new"
  fi

  old="$root/com/example"
  if [[ -d "$old" ]]; then
    mkdir -p "$new"
    find "$old" -mindepth 1 -maxdepth 1 -exec mv -t "$new" -- {} +
    rmdir "$old"
  fi

  rmdir "$root/com" 2>/dev/null || true
done

if [[ -d "$base/src/main/resources" ]]; then
  for kind in f d; do
    if [[ "$kind" == f ]]; then
      find "$base/src/main/resources" -depth -type f -name '*modbase*' \
        -exec bash -c '
          replacement=$1
          shift
          for path do
            name="$(basename -- "$path")"
            target="$(dirname -- "$path")/${name//modbase/$replacement}"
            if [[ "$path" != "$target" ]]; then
              mv -- "$path" "$target"
            fi
          done
        ' bash "$modid" {} +
    else
      find "$base/src/main/resources" -depth -type d -name '*modbase*' \
        -exec bash -c '
          replacement=$1
          shift
          for path do
            name="$(basename -- "$path")"
            target="$(dirname -- "$path")/${name//modbase/$replacement}"
            if [[ "$path" != "$target" ]]; then
              mv -- "$path" "$target"
            fi
          done
        ' bash "$modid" {} +
    fi
  done
fi

rm -f "$base/.github/workflows/templateInit.yml"
rm -- "$script_path"

echo "template complete :D"