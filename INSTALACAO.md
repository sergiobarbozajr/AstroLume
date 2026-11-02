# Instalar o AstroLume

## 1. Baixar o APK

Na página do repositório, aba **Releases**. Cada versão sai em duas publicações:

| Publicação | Arquivo | Para |
| --- | --- | --- |
| `v0.42` | `AstroLumeTV.apk` | a TV |
| `celular-v0.42` | `AstroLume-celular.apk` | o celular |

Se a aba estiver vazia, é porque o último teste no emulador reprovou: a publicação só acontece
depois que ele passa.

## 2. Liberar a instalação na TV

**Configurações → Apps → Segurança e restrições → Fontes desconhecidas** e autorize o app que
você vai usar para receber o arquivo (`Send files to TV` ou `Downloader`).

## 3. Levar o arquivo até a TV

**Opção A — Send files to TV** (o caminho mais simples)
1. Instale o `Send files to TV` na TV e no celular.
2. Ponha o `AstroLumeTV.apk` no celular, abra o app, toque em **Send** e escolha a TV.
3. Na TV, abra o arquivo recebido e confirme.

**Opção B — Downloader (AFTVnews)**
1. Instale o `Downloader` na TV.
2. Digite o endereço direto do APK (o link da Release serve).

**Opção C — depuração por Wi-Fi** (para testar durante o desenvolvimento)
1. Na TV: **Configurações → Sistema → Sobre** e clique 7× em **Versão do build** para liberar
   as opções do desenvolvedor.
2. **Opções do desenvolvedor → Depuração por Wi-Fi**, ativar e anotar o endereço e a porta.
3. No computador:
   ```bash
   adb pair  IP:PORTA_DE_PAREAMENTO   # código mostrado na TV
   adb connect IP:5555
   adb install -r AstroLumeTV.apk
   ```

## 4. Atualizar

O app confere sozinho se existe versão nova (**Configurações → Atualizar o app**) e instala
por cima. Como as versões são assinadas com a mesma chave, a atualização mantém suas contas,
favoritos e histórico.

Se aparecer *"o app não consegue conferir"*, o repositório está privado: as Releases precisam
estar públicas para o app lê-las.

## 5. Primeira configuração do IPTV

**Configurações → Conta IPTV**. Duas abas:

| Aba | Para quê | Campos |
| --- | --- | --- |
| **Conta** | provedor no padrão Xtream Codes | endereço, usuário, senha |
| **Endereço** | lista `.m3u`, catálogo JSON, vídeo direto, `rtsp://` ou site | só o endereço |

Cada aba guarda **3 acessos**. Embaixo de cada uma ficam, sempre no mesmo lugar,
**Remover este acesso** e **Apagar todos** — cada um pede confirmação na tela.

Senha do conteúdo adulto: padrão `0000`, trocável em **Configurações → Conteúdo adulto**.
