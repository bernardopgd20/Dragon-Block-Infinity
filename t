#!/usr/bin/env python3
import sys
from pathlib import Path

ROOT = Path(__file__).resolve().parent
SRC = ROOT / "src"
OUTPUT = ROOT / "projeto.txt"

SKIP_DIRS = {".git", ".gradle", "build", "bin", "out"}
SKIP_EXTS = {
    ".png", ".jpg", ".jpeg", ".gif", ".bmp", ".ico", ".webp",
    ".jar", ".class", ".zip", ".gz", ".tar", ".rar", ".7z",
    ".ttf", ".otf", ".woff", ".woff2",
}


def should_process(path: Path) -> bool:
    if any(part in SKIP_DIRS for part in path.parts):
        return False
    if path.suffix.lower() in SKIP_EXTS:
        return False
    return True


def collect_text_files(base: Path):
    files = []
    if not base.exists():
        return files

    for path in sorted(base.rglob("*")):
        if not path.is_file():
            continue
        if not should_process(path):
            continue
        try:
            text = path.read_text(encoding="utf-8")
        except (UnicodeDecodeError, OSError):
            continue
        rel = path.relative_to(ROOT).as_posix()
        files.append((rel, text))

    return files


def main():
    items = collect_text_files(SRC)
    if not items:
        OUTPUT.write_text("Nenhum arquivo de código encontrado em src.\n", encoding="utf-8")
        print(f"Arquivo criado: {OUTPUT}")
        return

    sections = []
    for rel, text in items:
        sections.append(f"===== {rel} =====\n{text}\n\n")

    OUTPUT.write_text("".join(sections), encoding="utf-8")
    print(f"Arquivo criado: {OUTPUT}")
    print(f"Arquivos processados: {len(items)}")


if __name__ == "__main__":
    main()
