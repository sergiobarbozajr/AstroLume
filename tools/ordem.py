#!/usr/bin/env python3
"""
Confere a ordem das funcoes locais do MainActivity.

Kotlin exige que uma funcao local so seja usada depois de declarada. Como o arquivo e grande
e tudo vive dentro de uma funcao composable, e facil declarar algo depois de quem usa.
Este script pega esse caso em segundos, em vez de esperar sete minutos do emulador.
"""
import re
import sys

ARQ = "app/src/main/java/com/auroratv/tv/MainActivity.kt"
linhas = open(ARQ, encoding="utf-8").read().split("\n")

# funcoes locais: identadas com 4 espacos dentro da composable
decl = {}
for i, l in enumerate(linhas):
    m = re.match(r"^    fun ([A-Za-z_][A-Za-z0-9_]*)\(", l)
    if m and m.group(1) not in decl:
        decl[m.group(1)] = i

erros = []
for nome, onde in decl.items():
    for i, l in enumerate(linhas):
        if i >= onde:
            break
        if re.match(r"^\s*(//|\*|/\*)", l):
            continue
        if re.search(r"(?<![A-Za-z0-9_.])" + re.escape(nome) + r"\s*\(", l):
            erros.append(f"{ARQ}:{i+1}  usa '{nome}' antes de declarar (declarada na linha {onde+1})")
            break

if erros:
    print("Ordem das funcoes locais:")
    for e in erros:
        print("  " + e)
    sys.exit(1)
print(f"ordem ok ({len(decl)} funcoes locais)")
