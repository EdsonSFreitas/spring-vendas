**Boas Práticas Adotadas Nessa API REST com Spring Framework Até o Momento:**


<details open><summary>Neste projeto estou aplicando uma série de boas práticas para garantir a qualidade e a eficiência do código da minha API REST com Spring Framework. Estou aplicando técnicas diferentes para demostrar diversas maneiras de implementar os recursos explorados e algumas permissões foram propositalmente mantidas para facilitar o acesso como a console do h2 no ambiente de dev, mas ciente de que um projeto em produção deve seguir padrões de desenvolvimento definidos no escopo do projeto/equipe.</summary>

**1. Estrutura de Pacotes Organizada:** Mantenho uma estrutura de pacotes bem organizada, dividindo os componentes em controladores, serviços, repositórios e modelos (DTOs/Entidades).

**2. Nomenclatura Convincente:** Utilizo nomenclaturas descritivas e significativas para classes, métodos e variáveis, tornando o código mais compreensível.

**3. DTOs para Comunicação:** Emprego DTOs para representar dados enviados e recebidos pela API, protegendo detalhes da implementação.

**4. Validações de Entrada:** Implemento validações para garantir a integridade dos dados fornecidos pelos clientes da API.

**5. Respostas HTTP Adequadas:** Utilizo os códigos de status HTTP apropriados nas respostas da API, assegurando respostas claras e adequadas.

**6. Tratamento de Exceções:** Implemento um tratamento de exceções personalizado para fornecer respostas apropriadas em caso de erros e evitar a exposição de detalhes técnicos.

**7. Paginação e Ordenação:** Implemento recursos de paginação e ordenação para simplificar a navegação pelos resultados da API e um limit de requisições.

**8. Uso Consciente do Banco de Dados:** Evito consultas complexas ou repetitivas ao banco de dados, considerando a implementação de caches para otimizar o desempenho. As credenciais do banco ficam protegidas por meio de variáveis de ambiente e uso bancos de dados diferentes conforme ambiente (prod com PostgreSQL, test ou dev com H2).

**9. Código Limpo e Legível:** Mantenho meu código limpo, sigo as convenções de estilo e asseguro que ele permaneça altamente legível. Adoto uso constante de Optional, conjuntos imutáveis ao usar métodos getters etc.

**10. Princípios SOLID:** Aplico os princípios SOLID (Single Responsibility, Open/Closed, Liskov Substitution, Interface Segregation, Dependency Inversion) para manter meu código limpo e altamente modular.

**11. Boas Práticas de Segurança:** Apliquei implementações de segurança, incluindo autenticação, autorização e validação de token, para proteger a API contra ameaças. Também apliquei configuração global para o CORS, validação de complexidade de senha e validação se o username já está em uso no banco de dados.

**12. Documentação Clara:** Implementei springdoc-openapi-ui para fornecer uma documentação clara e abrangente da API, descrevendo o uso de cada recurso, parâmetros necessários e formatos de resposta. As requisições por meio do Swagger também exigem autorização via JWT de modo global ou por endpoint.
</details>
<br>

<details><summary> Implementações pendentes:</summary>

** Max Retry, Bloqueio de usuario: ** Criar limite de tentativas de autenticação e bloquear usuário apóx X tentativas malsucedidas, registrar no banco as tentativas de autenticação malsucedidas etc.

** Testes Unitários: ** Criar os testes unitários para garantir a funcionalidade correta dos componentes da API, aumentando a confiabilidade do sistema.

** Monitoramento e Logs: ** Integrar registros de logs e métricas para facilitar a identificação e resolução de problemas operacionais.

** Versionamento da API: ** Implementar uma estratégia de versionamento da API para garantir compatibilidade com clientes existentes.
</details>
<br>
<details><summary> Como executar esse projeto:</summary>

- Para executar o projeto faça o clone do repositório, no diretório onde executar esse comando será criado uma pasta com o nome spring-vendas

  ```bash
  git clone https://github.com/EdsonSFreitas/spring-vendas.git
  ```

- Credenciais do banco: O projeto foi configurado para que o usuário e a senha de acesso ao banco estejam registradas apenas como variáveis de ambiente, portanto, crie as variáveis de ambiente de nome "DB_USER" e "DB_PASS" no Sistema Operacional ou direto na IDE de sua escolha. Por exemplo, usando Linux basta executar esses dois comandos no terminal informando o usuario e senha do seu banco relacional entre aspas duplas como no exemplo abaixo.

    ```bash
    export DB_USER="sa"
    export DB_PASS="senha987"
    ```

- Precisamos também definir um hash base64 para a variável JWT_SECRET_DEV ou JWT_SECRET_PRD (depende de qual ambiente pretende usar). 
- Esse hash pode ser gerado em diversos sites ou direto no shell Linux. Use o comando echo com uma frase qualquer encadeado com o openssl para gerar o hash, repare que existe propositalmente um espaço em branco antes do comando echo, isso é uma boa prática para que o comando não fique gravado no history do shell.

    ```bash
     echo -n 'olá, sejam bem vindo ao meu projeto API Vendas' | openssl base64
    ```

-  Se usou a frase acima o resultado será sempre esse hash:

    ```bash
    b2zDoSwgc2VqYW0gYmVtIHZpbmRvIGFvIG1ldSBwcm9qZXRvIEFQSSBWZW5kYXM=
    ```

- Define o hash como valor para a variável que pretende usar, nesse exemplo usei a JWT_SECRET_DEV

    ```bash
    export JWT_SECRET_DEV="b2zDoSwgc2VqYW0gYmVtIHZpbmRvIGFvIG1ldSBwcm9qZXRvIEFQSSBWZW5kYXM="
    ```

- Compilação: Entre no diretório que contém o arquivo pom.xml e gere o artefato .jar com o comando mvn do maven:

    ```bash
    cd spring-vendas/
    mvn clean package -DskipTests
    ```

- Execução: Ao término do comando será gerado o arquivo vendas-0.9.8-SNAPSHOT.jar dentro da pasta target. Na mesma sessão do shell Linux que usou para definir as variáveis de ambiente execute o comando para entrar no diretório target e execute a aplicação com o comando:

    ```bash
    cd target/
    java -jar vendas-0.9.8-SNAPSHOT.jar
    ```

- Com isso você terá acesso ao http://localhost:8080/swagger-ui/index.html para usar todos os endpoints disponíveis.
- No endpoint /api/usuarios você deve criar um usuário com senha contendo caracteres minúsculo, maiúsculo, número e ao menos um caractere especial. Em seguida acesse o endpoint /api/usuarios/auth preencha o exemplo JSON com login e senha criados na etapa anterior, será gerado o token que você deve usar para autorizar o acesso via ícone de cadeado com nome “Authorize” disponível no canto direito superior para que o mesmo token seja usado em todos os endpoints ou pode autorizar somente o endpoint que quiser usando o ícone de cadeado ao lado cada endpoint.


</details>