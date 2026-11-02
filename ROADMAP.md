# O que falta

Em ordem de prioridade acordada.

## Antes de considerar o projeto pronto

**1. Trocar a chave de assinatura.**
Hoje as versões de produção são assinadas com a `debug.keystore` que está **dentro do
repositório**. Qualquer pessoa com o repositório pode assinar um APK que a TV aceita como
atualização do seu. Para trocar: gerar uma chave nova, guardá-la como *secret* no GitHub, e
avisar que a primeira atualização depois disso exige **desinstalar e instalar de novo** (o
Android não aceita troca de assinatura por cima).
*Combinado: "deixa assinatura quando estiver o projeto perfeito".*

**2. Tirar a permissão de localização.**
O app pede `ACCESS_FINE_LOCATION` por um motivo só: o Android esconde o nome do Wi-Fi de quem
não tem essa permissão, e a tela de rede mostra esse nome. Dá para mostrar "conectado" sem o
nome e devolver a permissão.

**3. Dividir o `MainActivity.kt`.**
3618 linhas numa função composta só. É dele que vêm os erros de ordem de função local — quatro
rodadas de emulador perdidas até `tools/ordem.py` existir. A divisão natural: telas
(`TelaConteudo`, `TelaConfiguracoes`, `TelaIptv`), painéis (`TecladoBox`, `ConfirmBox`,
`SoundBox`…) e o despachante de teclas, cada um em seu arquivo.

## Esperando você

**4. Endereço do catálogo do Saimo.**
Para a TV ler o conteúdo direto em vez de abrir o site, falta saber de qual endereço o site
busca os dados. Dois caminhos:
- testar `canais-saimo.pages.dev` e `filmes-saimo.pages.dev` na aba **Endereço**; ou
- usar o **detector de fonte** em `https://saimo-tv.pages.dev/movies`, que abre o site uma vez
  e mostra os endereços que ele chamou.
Depois me mande a linha de diagnóstico que a aba mostrar.

**5. Medição no aparelho de verdade.**
A última que você mandou foi antes da 0.37. O emulador hoje dá 23 ms de mediana, mas a TCL é
que decide. Com depuração por Wi-Fi ligada eu colho CPU, GPU, temperatura e tempo de quadro.

## Depois

- **AstroLume Home** (launcher, módulo `home/`, branch `teste-home`) — pausado por sua escolha.
- Guia de programação (EPG) para os canais que entregam.
- Legendas e faixas de áudio escolhíveis na tela do player.
- Continuar assistindo entre aparelhos — hoje a retomada é só local, e você pediu para manter
  tudo no aparelho.
