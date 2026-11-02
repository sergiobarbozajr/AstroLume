# Como se trabalha neste projeto

Estas não são boas intenções: são as regras que já evitaram erro repetido.

## Regras do dono do projeto

1. **Nada vai para o `main` sem passar o teste no emulador.** Trabalho vai para um branch
   `teste-<versão>`; só depois que a esteira fica verde é que entra no `main`, e só então o
   APK é publicado.
2. **Toda atualização confere fluidez.** Mediana e p95 de tempo de quadro, tela parada e
   reprodução. Se não passar, não publica.
3. **IPTV só da conta contratada pelo usuário.** Credenciais ficam no aparelho. Nenhum painel
   pirata, em nenhuma circunstância.
4. **Não se modifica nem se redistribui APK de terceiro.** Quando um serviço de outra pessoa é
   usado como referência, o que se faz é um leitor genérico de formato aberto — não uma cópia
   do app nem raspagem de API privada.
5. **Nada de personagem, logo ou letra de música de terceiro** desenhado ou reproduzido.
6. **Comunicação**: português do Brasil, formato CTO Virtual (Resumo Executivo → Consenso dos
   Especialistas → Decisão Final), direto, com o passo a passo quando houver o que fazer.

## Antes de abrir um commit

```bash
python3 tools/ordem.py          # função local usada antes de ser declarada
./gradlew :app:assembleTvRelease
```

`tools/ordem.py` é a primeira etapa da esteira. Em Kotlin, função local declarada depois do uso
não compila, e o erro sai como `Unresolved reference` — quatro rodadas de emulador foram
perdidas nisso antes dele existir.

## Medir antes de mexer

A regra mais importante do projeto: **não se muda código atrás de um número sem medir.**
Toda melhoria de desempenho aqui veio de `dumpsys gfxinfo`, framestats ou `logcat`, nunca de
suposição. E quando a suposição estava errada — "o emulador é lento por natureza" — a medição
mostrou as Configurações do Android a 5 ms no mesmo emulador, e a suposição foi desfeita por
escrito.

## Portões que precisam falhar quando não medem

Um portão que só reprova número ruim deixa passar o teste que não rodou. Foi o que aconteceu
entre a 0.29 e a 0.39: o gancho de teste existia só na versão de depuração, a esteira media a
de produção, e o teste de reprodução passava **vazio** em silêncio por dez versões.
Portão novo tem que reprovar nos dois casos: número ruim **e** ausência de medida.

## Esteira

| Workflow | Quando roda | O que faz |
| --- | --- | --- |
| `smoke.yml` | todo push | compila as duas variantes, testa no emulador, mede fluidez |
| `build.yml` | depois do `smoke.yml` passar no `main` | publica o APK de produção |
| `capturas.yml` | todo push | captura os temas no emulador Android 14 |
| `home.yml` | mudanças em `home/` | o launcher |

O executor do passo do emulador **não aceita laço de várias linhas** nem função de shell —
isso já derrubou a esteira duas vezes. Comando de laço vai em uma linha só.

## Mensagem de commit

Em português, dizendo **o que mudou e por quê**, não o quê em termos de arquivo. O porquê é o
que serve depois:

> `dar 30 segundos ao servidor antes da nova tentativa: IPTV costuma demorar para liberar o
> canal, e reconectar cedo faz ele comecar tudo de novo`
