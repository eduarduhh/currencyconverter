# 💱 Currency Converter API

Esta é uma API REST para conversão de moedas entre diferentes países, com persistência de transações por usuário.  
Construída em **Kotlin** com **Spring Boot**, simula chamadas a um serviço externo de taxas de câmbio e armazena o histórico de conversões.

---

## 📌 Objetivo do Projeto

O propósito principal desta aplicação é demonstrar:

- Integração com API externa via `FeignClient`
- Persistência com Spring Data JPA
- Validação com Bean Validation
- Tratamento global de exceções com `@RestControllerAdvice`
- Organização por camadas e boas práticas de arquitetura REST

> Além disso, este projeto marca o início de uma nova fase como desenvolvedor:  
> _"Após alguns anos de experiência com Java, aprender Kotlin tem sido uma experiência extremamente gratificante."_ 🙌

---

## ⚙️ Tecnologias e Escolhas

| Tecnologia | Motivo |
|------------|--------|
| **Kotlin** | Sintaxe concisa, interoperável com Java, moderna |
| **Spring Boot 3.4.5** | Facilidade de configuração e produtividade |
| **Spring Data JPA** | Persistência simplificada |
| **FeignClient** | Integração externa declarativa |
| **H2 Database (memória)** | Ideal para testes e desenvolvimento local |
| **Swagger/OpenAPI (springdoc)** | Documentação automática e interativa |
| **MockK** | Testes unitários idiomáticos em Kotlin |
| **ExceptionHandler global** | Centraliza o tratamento de falhas da aplicação |

---

## 🧱 Estrutura de Camadas

\`\`\`
br.com.eduarduhh.currencyconverter
├── controller       # Camada web (REST)
├── service          # Lógica de negócio
├── repository       # Interfaces JPA
├── model            # Entidades do banco
├── dto              # Modelos de entrada/saída
├── client           # Integração com API externa (Feign)
├── exception        # Tratamento de erros personalizados
└── config           # Configurações (Swagger, Properties, etc)
\`\`\`

---

## 🚀 Como rodar o projeto localmente

### 🧰 Requisitos

- JDK 21
- Gradle 8.5+ ou `./gradlew`
- Kotlin 1.9.25+

---

### ▶️ Passos

1. Clone o repositório:
   \`\`\`bash
   git clone https://github.com/eduarduhh/currencyconverter.git
   cd currencyconverter
   \`\`\`

2. Rode o app com Gradle:
   \`\`\`bash
   ./gradlew bootRun
   \`\`\`

3. Acesse a documentação:
    - Swagger: [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)
    - H2 Console (se ativado): [http://localhost:8080/h2-console](http://localhost:8080/h2-console)

---

## ✅ Testes

Rodar os testes unitários:

\`\`\`bash
./gradlew test
\`\`\`

---

## 📬 Endpoints principais

| Verbo | Endpoint                        | Descrição                         |
|-------|----------------------------------|-----------------------------------|
| POST  | `/api/v1/currency/convert`      | Realiza conversão de moeda       |
| GET   | `/api/transaction/{userId}`     | Lista transações do usuário      |

---

## 🧪 Observações

- Toda exceção retorna `ApiError` com detalhes amigáveis
- O projeto usa `@ConfigurationProperties` para leitura de configuração da API externa

---

## 📚 Próximos passos (ideias de evolução)

- Suporte a persistência real com PostgreSQL
- Cache de taxas de câmbio para performance
- Autenticação de usuários
- Deploy contínuo com Docker/Railway

---

Feito com ❤️ por [Eduardo](https://github.com/eduarduhh)
