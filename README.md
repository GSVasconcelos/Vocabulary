# 📚 Vocabulary - Active Recall & Vocabulary Booster

<div align="center">

![Android](https://img.shields.io/badge/Platform-Android-3DDC84?style=for-the-badge&logo=android&logoColor=white)
![Java](https://img.shields.io/badge/Language-Java-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white)
![Room Database](https://img.shields.io/badge/Database-Room%20SQLite-4285F4?style=for-the-badge&logo=sqlite&logoColor=white)
![Material Design 3](https://img.shields.io/badge/Design-Material%203-757575?style=for-the-badge&logo=materialdesign&logoColor=white)
![License](https://img.shields.io/badge/License-MIT-blue.svg?style=for-the-badge)

<p align="center">
  <b>Um aplicativo Android moderno focado no aprendizado e memorização ativa (Active Recall) de vocabulários, termos e associações através de listas personalizadas e sessões gamificadas.</b>
</p>

[📥 Baixar APK](#-download-do-apk) • [✨ Funcionalidades](#-funcionalidades) • [🎨 Temas](#-temas-personalizados) • [🌐 Idiomas](#-suporte-multilíngue-7-idiomas) • [🛠️ Tecnologias](#️-arquitetura-e-tecnologias) • [🚀 Como Executar](#-como-executar-o-projeto)

</div>

---

## 📥 Download do APK

Experimente o **Vocabulary** diretamente no seu dispositivo Android!

<div align="center">

[![Download APK](https://img.shields.io/badge/Download-APK%20(Versão%20Mais%20Recente)-2ea44f?style=for-the-badge&logo=android&logoColor=white)](https://github.com/Cabrueira/Vocabulary/releases/latest)

> 💡 *Você também pode acessar a aba **[Releases](https://github.com/Cabrueira/Vocabulary/releases)** do repositório para baixar o arquivo `.apk` da versão desejada.*

</div>

### 📲 Como instalar o APK no seu celular:
1. Baixe o arquivo `app-debug.apk` ou `app-release.apk` no seu celular ou transfira pelo computador.
2. Abra o arquivo baixado através do gerenciador de arquivos ou da notificação de download.
3. Se solicitado, permita a instalação a partir do navegador/gerenciador (*"Permitir desta fonte"* nas configurações de segurança do Android).
4. Clique em **Instalar** e pronto! Você já pode impulsionar seu vocabulário.

---

## ✨ Funcionalidades

- 🗂️ **Listas e Decks Dinâmicos:**
  - Crie listas temáticas ilimitadas (ex: *Inglês*, *Mandarim*, *Termos Médicos*, *Versículos Bíblicos*, *Programação*).
  - Contador dinâmico de palavras por lista em tempo real.
  - Exclusão segura de listas com diálogo de confirmação para evitar perdas acidentais.

- 📝 **Pares de Vocabulário e Associações:**
  - Adicione palavras com par **Texto Principal** $\leftrightarrow$ **Associação / Significado**.
  - Confirmação de exclusão individual para cada palavra.
  - Estatísticas de desempenho por palavra (total de revisões e histórico de acertos).

- 🧠 **Sessão de Recapitulação Ativa (Active Recall Gamificado):**
  - Embaralha as palavras da lista para evitar memorização por ordem.
  - Apresenta o termo principal e desafia o usuário a digitar a resposta correspondente sem vê-la.
  - Feedback visual imediato com cores adaptadas para acerto e erro.
  - **Contador de Streak (Sequência 🔥):** Acompanhe quantas palavras você acertou consecutivamente!
  - Barra de progresso linear e tela final de encerramento com porcentagem de aproveitamento e precisão.

- ⌨️ **Suporte Nativo a Acentos e Caracteres Internacionais:**
  - Reconhecimento completo de acentuação na língua portuguesa (`Á`, `á`, `é`, `í`, `ó`, `ú`, `ç`, `ã`, `õ`, `â`, `ê`).
  - Suporte a dead-keys em teclados físicos e emuladores PC.
  - Totalmente compatível com caracteres não-latinos como ideogramas chineses (Hanzi) e japoneses (Kanji/Kana).

- ⚙️ **Tela de Configurações Completa:**
  - Área dedicada para personalização da experiência do usuário, acessível diretamente pela engrenagem na barra superior.

---

## 🎨 Temas Personalizados

O aplicativo conta com seu próprio motor de temas independente do modo claro/escuro do sistema operacional, permitindo que você escolha o visual que melhor combina com o seu momento:

| Tema | Descrição | Paleta de Cores |
| :--- | :--- | :--- |
| **☀️ Modo Claro** | Visual leve, limpo e revigorante | Fundo branco suave com acentos em azul celeste |
| **🧛 Modo Drácula** | Tema escuro oficial, confortável para a vista à noite | Fundo grafite profundo (`#282A36`), contrastes em roxo (`#BD93F9`) e verde neon suave |
| **☕ Modo Café** | Visual aconchegante e vintage | Tons de café cremoso, marrom expresso e detalhes em creme |

---

## 🌐 Suporte Multilíngue (7 Idiomas)

O aplicativo conta com suporte nativo a internacionalização por aplicativo (**AndroidX Per-App Language**), permitindo que você alterne a língua do app a qualquer instante nas **Configurações**:

- 🇧🇷 **Português**
- 🇺🇸 **English**
- 🇫🇷 **Français**
- 🇪🇸 **Español**
- 🇩🇪 **Deutsch**
- 🇨🇳 **中文 (Chinês)**
- 🇯🇵 **日本語 (Japonês)**

---

## 🛠️ Arquitetura e Tecnologias

O projeto foi construído seguindo as melhores práticas do ecossistema Android:

- **Linguagem:** 100% Java (sem dependências Kotlin no código-fonte)
- **Padrão de Arquitetura:** MVVM (*Model - View - ViewModel*)
- **Banco de Dados Local:** **Room Database** (SQLite) com relacionamentos 1:N e *Foreign Key Cascade Delete*
- **Componentes do Android Jetpack:**
  - `ViewModel` & `LiveData` para observabilidade reativa do ciclo de vida
  - `ViewBinding` para manipulação segura e rápida de elementos de UI
  - `Material Components (M3)` para cards com cantos arredondados, botões elevados, diálogos e inputs delineados
  - `AppCompatDelegate` & `LocaleListCompat` para troca dinâmica de temas e idiomas sem reiniciar o sistema operacional

```
com.example.vocabulary
├── data
│   ├── dao          # Interfaces de acesso ao banco (Room DAO)
│   ├── model        # Entidades do SQLite (VocabularyList, VocabularyItem)
│   ├── AppDatabase  # Instância Room Database Singleton
│   └── VocabularyRepository
├── ui
│   ├── activity     # MainActivity, ListDetailActivity, GameActivity, SettingsActivity
│   ├── adapter      # RecyclerView Adapters para Listas e Palavras
│   ├── util         # ThemeManager, LocaleManager, AccentInputHelper
│   └── viewmodel    # VocabularyViewModel, GameViewModel
```

---

## 🚀 Como Executar o Projeto

### Pré-requisitos:
- **Android Studio** (versão Ladybug, Jellyfish, Iguana ou superior)
- **JDK 17** ou superior configurado
- Dispositivo Android físico ou Emulador com **Android 8.0 (API 26)** ou superior

### Passos:
1. Clone o repositório:
   ```bash
   git clone https://github.com/Cabrueira/Vocabulary.git
   ```
2. Abra o projeto no **Android Studio**.
3. Aguarde o Android Studio realizar a sincronização automática do Gradle.
4. Conecte seu dispositivo ou inicie um emulador.
5. Clique em **Run ▶️** (Shift + F10) para compilar e iniciar o aplicativo.

### 📦 Como Gerar o seu próprio APK no Android Studio:
1. No menu superior, vá em **Build** > **Build Bundle(s) / APK(s)** > **Build APK(s)**.
2. Ao concluir, clique na notificação no canto inferior direito em **locate** para encontrar o arquivo `app-debug.apk`.
3. Para publicar no GitHub, basta criar uma nova **Release** na página do repositório e arrastar o `.apk` gerado para os anexos da release!

---

## 📄 Licença

Este projeto está sob a licença **MIT** - veja o arquivo [LICENSE](LICENSE) para mais detalhes.

---

<div align="center">
  Desenvolvido com dedicação por <b>Cabrueira</b> 🚀
</div>
